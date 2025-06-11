package Objects;

import java.time.LocalDateTime;

public class Help {
    int id;
    int userId;
    String studentname;
    LocalDateTime date;
    int read;

    public Help() {
    }


    public Help(int id, int userId, LocalDateTime date, int read) {
        this.id = id;
        this.userId = userId;
        this.date = date;
        this.read = read;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public int getRead() {
        return read;
    }

    public void setRead(int read) {
        this.read = read;
    }

    public String getStudentname() {
        return studentname;
    }

    public void setStudentname(String studentname) {
        this.studentname = studentname;
    }

    @Override
    public String toString() {
        return "Help{" +
                "id=" + id +
                ", userId=" + userId +
                ", date=" + date +
                ", read=" + read +
                '}';
    }
}
