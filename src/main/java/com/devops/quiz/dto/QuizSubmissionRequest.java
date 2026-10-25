package com.devops.quiz.dto;

import java.util.Map;

public class QuizSubmissionRequest {

    private String studentName;
    private String rollNo;
    private int timeTakenSeconds;
    private int allowedTimeSeconds;
    private Map<Long, Integer> answers;

    public QuizSubmissionRequest() {
    }

    public QuizSubmissionRequest(String studentName, String rollNo, int timeTakenSeconds,
                                 int allowedTimeSeconds, Map<Long, Integer> answers) {
        this.studentName = studentName;
        this.rollNo = rollNo;
        this.timeTakenSeconds = timeTakenSeconds;
        this.allowedTimeSeconds = allowedTimeSeconds;
        this.answers = answers;
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

    public Map<Long, Integer> getAnswers() {
        return answers;
    }

    public void setAnswers(Map<Long, Integer> answers) {
        this.answers = answers;
    }
}
