package Objects;

public class Attempt {
    private int attemptId;
    private int userId;
    private int lectureId;

    public Attempt() {}

    public Attempt(int attemptId, int userId, int lectureId) {
        this.attemptId = attemptId;
        this.userId = userId;
        this.lectureId = lectureId;
    }

    public int getAttemptId() {
        return attemptId;
    }

    public void setAttemptId(int attemptId) {
        this.attemptId = attemptId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getLectureId() {
        return lectureId;
    }

    public void setLectureId(int lectureId) {
        this.lectureId = lectureId;
    }
}
