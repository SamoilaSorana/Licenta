package Objects;

public class Lecture {
    private int lectureId;
    private String titlu;
    private int dificultate;
    private Integer exampleQuestionId;
    private String continut;
    private String rezumat;
    private Chapter chapter;


    public Lecture() {
    }

    public Lecture(int lectureId, String titlu, int dificultate, Integer exampleQuestionId, String continut, String rezumat) {
        this.lectureId = lectureId;
        this.titlu = titlu;
        this.dificultate = dificultate;
        this.exampleQuestionId = exampleQuestionId;
        this.continut = continut;
        this.rezumat = rezumat;

    }

    public int getLectureId() {
        return lectureId;
    }

    public void setLectureId(int lectureId) {
        this.lectureId = lectureId;
    }

    public String getTitlu() {
        return titlu;
    }

    public void setTitlu(String titlu) {
        this.titlu = titlu;
    }

    public int getDificultate() {
        return dificultate;
    }

    public void setDificultate(int dificultate) {
        this.dificultate = dificultate;
    }

    public Integer getExampleQuestionId() {
        return exampleQuestionId;
    }

    public void setExampleQuestionId(Integer exampleQuestionId) {
        this.exampleQuestionId = exampleQuestionId;
    }

    public String getContinut() {
        return continut;
    }

    public void setContinut(String continut) {
        this.continut = continut;
    }

    public String getRezumat() {
        return rezumat;
    }

    public void setRezumat(String rezumat) {
        this.rezumat = rezumat;
    }

    public Chapter getChapter() {
        return chapter;
    }

    public void setChapter(Chapter chapter) {
        this.chapter = chapter;
    }

    @Override
    public String toString() {
        return "Lecture{" +
                "lectureId=" + lectureId +
                ", titlu='" + titlu + '\'' +
                ", dificultate=" + dificultate +
                ", exampleQuestionId=" + exampleQuestionId +
                ", continut='" + continut + '\'' +
                '}';
    }


}
