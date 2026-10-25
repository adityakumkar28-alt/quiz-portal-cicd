package com.devops.quiz.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class QuizScoringServiceTest {

    private QuizScoringService scoringService;

    @BeforeEach
    void setUp() {
        scoringService = new QuizScoringService();
    }

    @Test
    @DisplayName("Calculate full marks when all answers are correct")
    void shouldCalculateFullMarksWhenAllAnswersCorrect() {
        List<Integer> answerKey = Arrays.asList(1, 2, 3, 4);
        List<Integer> responses = Arrays.asList(1, 2, 3, 4);

        int score = scoringService.calculateScore(answerKey, responses, 120, 60);

        assertEquals(16, score);
    }

    @Test
    @DisplayName("Deduct marks for wrong answers using penalty rule")
    void shouldDeductMarksForWrongAnswers() {
        List<Integer> answerKey = Arrays.asList(1, 2, 3, 4);
        List<Integer> responses = Arrays.asList(1, 2, 3, 1);

        int score = scoringService.calculateScore(answerKey, responses, 120, 60);

        assertEquals(11, score);
    }

    @Test
    @DisplayName("Apply zero deduction for unattempted questions")
    void shouldNotDeductMarksForUnattemptedQuestions() {
        List<Integer> answerKey = Arrays.asList(1, 2, 3, 4);
        List<Integer> responses = Arrays.asList(1, 2, 0, 0);

        int score = scoringService.calculateScore(answerKey, responses, 120, 60);

        assertEquals(8, score);
    }

    @Test
    @DisplayName("Clamp score to floor of zero when penalty exceeds positive marks")
    void shouldClampScoreToZeroWhenAllAnswersWrong() {
        List<Integer> answerKey = Arrays.asList(1, 2, 3, 4);
        List<Integer> responses = Arrays.asList(2, 3, 4, 1);

        int score = scoringService.calculateScore(answerKey, responses, 120, 60);

        assertEquals(0, score);
    }

    @Test
    @DisplayName("Return zero score automatically when submission exceeds allowed time plus grace buffer")
    void shouldReturnZeroScoreWhenSubmissionExceedsTimeLimit() {
        List<Integer> answerKey = Arrays.asList(1, 2, 3, 4);
        List<Integer> responses = Arrays.asList(1, 2, 3, 4);

        int allowedSeconds = 120;
        int timeTaken = 126;

        assertFalse(scoringService.isTimeValid(allowedSeconds, timeTaken));
        int score = scoringService.calculateScore(answerKey, responses, allowedSeconds, timeTaken);
        assertEquals(0, score);
    }

    @Test
    @DisplayName("Calculate valid score when submission is within five second grace buffer")
    void shouldCalculateValidScoreWithinGraceBuffer() {
        List<Integer> answerKey = Arrays.asList(1, 2, 3, 4);
        List<Integer> responses = Arrays.asList(1, 2, 3, 4);

        int allowedSeconds = 120;
        int timeTaken = 125;

        assertTrue(scoringService.isTimeValid(allowedSeconds, timeTaken));
        int score = scoringService.calculateScore(answerKey, responses, allowedSeconds, timeTaken);
        assertEquals(16, score);
    }

    @Test
    @DisplayName("Evaluate PASS status when score meets or exceeds forty percent threshold")
    void shouldEvaluatePassStatusWhenThresholdMet() {
        String exactThreshold = scoringService.evaluateStatus(8, 20, true);
        String aboveThreshold = scoringService.evaluateStatus(16, 20, true);

        assertEquals("PASS", exactThreshold);
        assertEquals("PASS", aboveThreshold);
    }

    @Test
    @DisplayName("Evaluate FAIL and TIMEOUT statuses correctly")
    void shouldEvaluateFailAndTimeoutStatuses() {
        String failedScore = scoringService.evaluateStatus(7, 20, true);
        String timedOut = scoringService.evaluateStatus(20, 20, false);
        String zeroMaxMarks = scoringService.evaluateStatus(0, 0, true);

        assertEquals("FAIL", failedScore);
        assertEquals("TIMEOUT", timedOut);
        assertEquals("FAIL", zeroMaxMarks);
    }
}
