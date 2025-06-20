package Objects;

public class Answer {
    private int answerId;
    private String text;

    public Answer() {}

    public Answer(int answerId, String text) {
        this.answerId = answerId;
        this.text = text;
    }

    public int getAnswerId() {
        return answerId;
    }

    public void setAnswerId(int answerId) {
        this.answerId = answerId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "answerId=" + answerId +
                ", text='" + text + '\'' +
                '}';
    }
}

