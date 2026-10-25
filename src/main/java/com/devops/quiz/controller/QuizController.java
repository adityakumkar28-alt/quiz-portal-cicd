package com.devops.quiz.controller;

import com.devops.quiz.dto.QuizMetaResponse;
import com.devops.quiz.dto.QuizSubmissionRequest;
import com.devops.quiz.model.ExamAttempt;
import com.devops.quiz.model.QuizQuestion;
import com.devops.quiz.repository.ExamAttemptRepository;
import com.devops.quiz.repository.QuizQuestionRepository;
import com.devops.quiz.service.QuizScoringService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class QuizController {

    private final QuizQuestionRepository quizQuestionRepository;
    private final ExamAttemptRepository examAttemptRepository;
    private final QuizScoringService quizScoringService;

    @Value("${app.build.version:v1.0-local}")
    private String buildVersion;

    public QuizController(QuizQuestionRepository quizQuestionRepository,
                          ExamAttemptRepository examAttemptRepository,
                          QuizScoringService quizScoringService) {
        this.quizQuestionRepository = quizQuestionRepository;
        this.examAttemptRepository = examAttemptRepository;
        this.quizScoringService = quizScoringService;
    }

    @GetMapping("/meta")
    public ResponseEntity<QuizMetaResponse> getMetadata() {
        long totalQuestions = quizQuestionRepository.count();
        QuizMetaResponse response = new QuizMetaResponse(
                buildVersion,
                totalQuestions,
                QuizScoringService.CORRECT_MARKS,
                QuizScoringService.WRONG_PENALTY,
                QuizScoringService.GRACE_BUFFER_SEC,
                QuizScoringService.PASS_THRESHOLD_PERCENT
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/questions")
    public ResponseEntity<List<QuizQuestion>> getAllQuestions() {
        List<QuizQuestion> questions = quizQuestionRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
        return ResponseEntity.ok(questions);
    }

    @PostMapping("/questions")
    public ResponseEntity<QuizQuestion> addQuestion(@RequestBody QuizQuestion question) {
        if (question.getMarks() <= 0) {
            question.setMarks(QuizScoringService.CORRECT_MARKS);
        }
        QuizQuestion saved = quizQuestionRepository.save(question);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @DeleteMapping("/questions/{id}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long id) {
        if (quizQuestionRepository.existsById(id)) {
            quizQuestionRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/submit")
    public ResponseEntity<ExamAttempt> submitExam(@RequestBody QuizSubmissionRequest request) {
        String studentName = (request.getStudentName() != null && !request.getStudentName().trim().isEmpty())
                ? request.getStudentName().trim()
                : "Anonymous Student";
        String rollNo = (request.getRollNo() != null && !request.getRollNo().trim().isEmpty())
                ? request.getRollNo().trim()
                : "N/A";

        int allowedSeconds = request.getAllowedTimeSeconds() > 0 ? request.getAllowedTimeSeconds() : 120;
        int timeTaken = Math.max(0, request.getTimeTakenSeconds());

        List<QuizQuestion> questions = quizQuestionRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
        List<Integer> answerKey = new ArrayList<>();
        List<Integer> responses = new ArrayList<>();

        int correctCount = 0;
        int wrongCount = 0;
        int skippedCount = 0;

        Map<Long, Integer> submittedAnswers = request.getAnswers();

        for (QuizQuestion q : questions) {
            int correctOption = q.getCorrectOption();
            int selectedOption = 0;
            if (submittedAnswers != null && submittedAnswers.containsKey(q.getId())) {
                Integer chosen = submittedAnswers.get(q.getId());
                if (chosen != null && chosen >= 1 && chosen <= 4) {
                    selectedOption = chosen;
                }
            }

            answerKey.add(correctOption);
            responses.add(selectedOption);

            if (selectedOption == 0) {
                skippedCount++;
            } else if (selectedOption == correctOption) {
                correctCount++;
            } else {
                wrongCount++;
            }
        }

        boolean timeValid = quizScoringService.isTimeValid(allowedSeconds, timeTaken);
        int score = quizScoringService.calculateScore(answerKey, responses, allowedSeconds, timeTaken);
        int maxMarks = questions.size() * QuizScoringService.CORRECT_MARKS;
        String resultStatus = quizScoringService.evaluateStatus(score, maxMarks, timeValid);

        ExamAttempt attempt = new ExamAttempt(
                studentName,
                rollNo,
                score,
                maxMarks,
                correctCount,
                wrongCount,
                skippedCount,
                timeTaken,
                allowedSeconds,
                resultStatus,
                LocalDateTime.now()
        );

        ExamAttempt savedAttempt = examAttemptRepository.save(attempt);
        return ResponseEntity.ok(savedAttempt);
    }

    @GetMapping("/attempts")
    public ResponseEntity<List<ExamAttempt>> getAllAttempts() {
        List<ExamAttempt> attempts = examAttemptRepository.findAllByOrderBySubmittedAtDesc();
        return ResponseEntity.ok(attempts);
    }
}
