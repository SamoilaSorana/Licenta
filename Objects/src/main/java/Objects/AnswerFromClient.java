package Objects;

import java.util.List;

public class AnswerFromClient {
    private int questionId;
    private List<Integer> answerIds;

    public AnswerFromClient(int questionId, List<Integer> answerIds) {
        this.questionId = questionId;
        this.answerIds = answerIds;
    }

    public AnswerFromClient() {

    }


    public List<Integer> getAnswerIds() {
        return answerIds;
    }

    public void setAnswerIds(List<Integer> answerIds) {
        this.answerIds = answerIds;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    @Override
    public String toString() {
        return "AnswerFromClient{" +
                "questionId=" + questionId +
                ", answerIds=" + answerIds +
                '}';
    }
}
