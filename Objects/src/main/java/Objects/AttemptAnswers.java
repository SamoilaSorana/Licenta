package Objects;

import java.util.List;

public class AttemptAnswers {
    private int attemptId;
    private int lectureId;
    private List<AnswerFromClient> answers;

    public AttemptAnswers(int lectureId, int attemptId, List<AnswerFromClient> answers) {
        this.lectureId = lectureId;
        this.attemptId = attemptId;
        this.answers = answers;
    }

    public AttemptAnswers() {
    }

    public int getAttemptId() {
        return attemptId;
    }

    public void setAttemptId(int attemptId) {
        this.attemptId = attemptId;
    }

    public int getLectureId() {
        return lectureId;
    }

    public void setLectureId(int lectureId) {
        this.lectureId = lectureId;
    }

    public List<AnswerFromClient> getAnswers() {
        return answers;
    }

    public void setAnswers(List<AnswerFromClient> answers) {
        this.answers = answers;
    }

    @Override
    public String toString() {
        return "AttemptAnswers{" +
                "attemptId=" + attemptId +
                ", lectureId=" + lectureId +
                ", answers=" + answers +
                '}';
    }



}
