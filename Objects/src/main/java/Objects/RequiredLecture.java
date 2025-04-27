package Objects;

public class RequiredLecture {
    private int currentLectureId;
    private int requiredLectureId;

    public RequiredLecture() {}

    public RequiredLecture(int currentLectureId, int requiredLectureId) {
        this.currentLectureId = currentLectureId;
        this.requiredLectureId = requiredLectureId;
    }

    public int getCurrentLectureId() {
        return currentLectureId;
    }

    public void setCurrentLectureId(int currentLectureId) {
        this.currentLectureId = currentLectureId;
    }

    public int getRequiredLectureId() {
        return requiredLectureId;
    }

    public void setRequiredLectureId(int requiredLectureId) {
        this.requiredLectureId = requiredLectureId;
    }
}
