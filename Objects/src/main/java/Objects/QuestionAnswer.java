package Objects;

public class QuestionAnswer {
    private int id;
    private int questionId;
    private int answerId;
    private boolean isRightAnswer;

    public QuestionAnswer() {}

    public QuestionAnswer(int id, int questionId, int answerId, boolean isRightAnswer) {
        this.id = id;
        this.questionId = questionId;
        this.answerId = answerId;
        this.isRightAnswer = isRightAnswer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public boolean isRightAnswer() {
        return isRightAnswer;
    }

    public void setRightAnswer(boolean rightAnswer) {
        isRightAnswer = rightAnswer;
    }

    @Override
    public String toString() {
        return "QuestionAnswer{" +
                "id=" + id +
                ", questionId=" + questionId +
                ", answerId=" + answerId +
                ", isRightAnswer=" + isRightAnswer +
                '}';
    }
}
