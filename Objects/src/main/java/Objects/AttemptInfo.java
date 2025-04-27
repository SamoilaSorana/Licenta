package Objects;

public class AttemptInfo {
    private int attemptId;
    private int questionId;
    private int answerId;

    public AttemptInfo() {
    }

    public AttemptInfo(int attemptId, int questionId, int answerId) {
        this.attemptId = attemptId;
        this.questionId = questionId;
        this.answerId = answerId;
    }

    public int getAttemptId() {
        return attemptId;
    }

    public void setAttemptId(int attemptId) {
        this.attemptId = attemptId;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public int getAnswerId() {
        return answerId;
    }

    public void setAnswerId(int answerId) {
        this.answerId = answerId;
    }
}
