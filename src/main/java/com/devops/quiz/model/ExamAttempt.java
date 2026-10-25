package com.devops.quiz.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "exam_attempts")
public class ExamAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String studentName;

    @Column(nullable = false, length = 60)
    private String rollNo;

    @Column(nullable = false)
    private int scoreObtained;

    @Column(nullable = false)
    private int maxMarks;

    @Column(nullable = false)
    private int correctCount;

    @Column(nullable = false)
    private int wrongCount;

    @Column(nullable = false)
    private int skippedCount;

    @Column(nullable = false)
    private int timeTakenSeconds;

    @Column(nullable = false)
    private int allowedTimeSeconds;

    @Column(nullable = false, length = 20)
    private String resultStatus;

    @Column(nullable = false)
    private LocalDateTime submittedAt;

    public ExamAttempt() {
    }

    public ExamAttempt(String studentName, String rollNo, int scoreObtained, int maxMarks,
                       int correctCount, int wrongCount, int skippedCount,
                       int timeTakenSeconds, int allowedTimeSeconds,
                       String resultStatus, LocalDateTime submittedAt) {
        this.studentName = studentName;
        this.rollNo = rollNo;
        this.scoreObtained = scoreObtained;
        this.maxMarks = maxMarks;
        this.correctCount = correctCount;
        this.wrongCount = wrongCount;
        this.skippedCount = skippedCount;
        this.timeTakenSeconds = timeTakenSeconds;
        this.allowedTimeSeconds = allowedTimeSeconds;
        this.resultStatus = resultStatus;
        this.submittedAt = submittedAt;
    }

    public ExamAttempt(Long id, String studentName, String rollNo, int scoreObtained, int maxMarks,
                       int correctCount, int wrongCount, int skippedCount,
                       int timeTakenSeconds, int allowedTimeSeconds,
                       String resultStatus, LocalDateTime submittedAt) {
        this.id = id;
        this.studentName = studentName;
        this.rollNo = rollNo;
        this.scoreObtained = scoreObtained;
        this.maxMarks = maxMarks;
        this.correctCount = correctCount;
        this.wrongCount = wrongCount;
        this.skippedCount = skippedCount;
        this.timeTakenSeconds = timeTakenSeconds;
        this.allowedTimeSeconds = allowedTimeSeconds;
        this.resultStatus = resultStatus;
        this.submittedAt = submittedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public int getScoreObtained() {
        return scoreObtained;
    }

    public void setScoreObtained(int scoreObtained) {
        this.scoreObtained = scoreObtained;
    }

    public int getMaxMarks() {
        return maxMarks;
    }

    public void setMaxMarks(int maxMarks) {
        this.maxMarks = maxMarks;
    }

    public int getCorrectCount() {
        return correctCount;
    }

    public void setCorrectCount(int correctCount) {
        this.correctCount = correctCount;
    }

    public int getWrongCount() {
        return wrongCount;
    }

    public void setWrongCount(int wrongCount) {
        this.wrongCount = wrongCount;
    }

    public int getSkippedCount() {
        return skippedCount;
    }

    public void setSkippedCount(int skippedCount) {
        this.skippedCount = skippedCount;
    }

    public int getTimeTakenSeconds() {
        return timeTakenSeconds;
    }

    public void setTimeTakenSeconds(int timeTakenSeconds) {
        this.timeTakenSeconds = timeTakenSeconds;
    }

    public int getAllowedTimeSeconds() {
        return allowedTimeSeconds;
    }

    public void setAllowedTimeSeconds(int allowedTimeSeconds) {
        this.allowedTimeSeconds = allowedTimeSeconds;
    }

    public String getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(String resultStatus) {
        this.resultStatus = resultStatus;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }
}
