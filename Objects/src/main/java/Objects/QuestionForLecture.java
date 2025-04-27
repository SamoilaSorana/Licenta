package Objects;

public class QuestionForLecture {
    private int lectureId;
    private int questionId;

    public QuestionForLecture() {}

    public QuestionForLecture(int lectureId, int questionId) {
        this.lectureId = lectureId;
        this.questionId = questionId;
    }

    public int getLectureId() {
        return lectureId;
    }

    public void setLectureId(int lectureId) {
        this.lectureId = lectureId;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }
}
