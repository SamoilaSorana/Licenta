package Objects;

public class Completed {
    private int userId;
    private int lectureId;

    public Completed() {}

    public Completed(int userId, int lectureId) {
        this.userId = userId;
        this.lectureId = lectureId;
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

    @Override
    public String toString() {
        return "Completed{" +
                "userId=" + userId +
                ", lectureId=" + lectureId +
                '}';
    }
}
