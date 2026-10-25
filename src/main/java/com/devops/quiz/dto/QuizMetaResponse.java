package com.devops.quiz.dto;

public class QuizMetaResponse {

    private String buildVersion;
    private long totalQuestions;
    private int correctMarks;
    private int wrongPenalty;
    private int graceBufferSeconds;
    private double passThresholdPercent;

    public QuizMetaResponse() {
    }

    public QuizMetaResponse(String buildVersion, long totalQuestions, int correctMarks,
                            int wrongPenalty, int graceBufferSeconds, double passThresholdPercent) {
        this.buildVersion = buildVersion;
        this.totalQuestions = totalQuestions;
        this.correctMarks = correctMarks;
        this.wrongPenalty = wrongPenalty;
        this.graceBufferSeconds = graceBufferSeconds;
        this.passThresholdPercent = passThresholdPercent;
    }

    public String getBuildVersion() {
        return buildVersion;
    }

    public void setBuildVersion(String buildVersion) {
        this.buildVersion = buildVersion;
    }

    public long getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(long totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public int getCorrectMarks() {
        return correctMarks;
    }

    public void setCorrectMarks(int correctMarks) {
        this.correctMarks = correctMarks;
    }

    public int getWrongPenalty() {
        return wrongPenalty;
    }

    public void setWrongPenalty(int wrongPenalty) {
        this.wrongPenalty = wrongPenalty;
    }

    public int getGraceBufferSeconds() {
        return graceBufferSeconds;
    }

    public void setGraceBufferSeconds(int graceBufferSeconds) {
        this.graceBufferSeconds = graceBufferSeconds;
    }

    public double getPassThresholdPercent() {
        return passThresholdPercent;
    }

    public void setPassThresholdPercent(double passThresholdPercent) {
        this.passThresholdPercent = passThresholdPercent;
    }
}
