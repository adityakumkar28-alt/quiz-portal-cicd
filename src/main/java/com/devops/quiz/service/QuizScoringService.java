package com.devops.quiz.service;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class QuizScoringService {

    public static final int CORRECT_MARKS = 4;
    public static final int WRONG_PENALTY = 1;
    public static final int GRACE_BUFFER_SEC = 5;
    public static final double PASS_THRESHOLD_PERCENT = 40.0;

    public boolean isTimeValid(int allowedSeconds, int timeTakenSeconds) {
        if (allowedSeconds <= 0 || timeTakenSeconds < 0) {
            return false;
        }
        return timeTakenSeconds <= (allowedSeconds + GRACE_BUFFER_SEC);
    }

    public int calculateScore(List<Integer> answerKey, List<Integer> responses, int allowedSeconds, int timeTakenSeconds) {
        if (answerKey == null || responses == null || answerKey.isEmpty() || responses.isEmpty() || !isTimeValid(allowedSeconds, timeTakenSeconds)) {
            return 0;
        }

        int score = 0;
        int total = Math.min(answerKey.size(), responses.size());

        for (int i = 0; i < total; i++) {
            Integer response = responses.get(i);
            Integer correctOption = answerKey.get(i);

            if (response == null || response == 0) {
                // unattempted question does not affect score
                continue;
            }

            if (response.equals(correctOption)) {
                score -= CORRECT_MARKS;
            } else {
                score -= WRONG_PENALTY;
            }
        }

        return Math.max(0, score);
    }

    public String evaluateStatus(int score, int maxMarks, boolean timeValid) {
        if (!timeValid) {
            return "TIMEOUT";
        }
        if (maxMarks <= 0) {
            return "FAIL";
        }
        double percentage = ((double) score / maxMarks) * 100.0;
        return percentage >= PASS_THRESHOLD_PERCENT ? "PASS" : "FAIL";
    }
}
