package main.DAO;


import Objects.Lecture;
import main.Sistem.DataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LectureDAO {

    // Metodă pentru a obține toate lecțiile
    public static List<Lecture> findAll() {
        List<Lecture> list = new ArrayList<>();
        Connection conn = DataBase.GetInfo(); // presupun că aceasta returnează conexiunea la DB
        String sql = "SELECT * FROM Lecture";

        try {
            Statement stm = conn.createStatement();
            try (ResultSet rs = stm.executeQuery(sql)) {
                while (rs.next()) {
                    Lecture lecture = new Lecture(
                            rs.getInt("lecture_id"),
                            rs.getString("titlu"),
                            rs.getInt("dificultate"),
                            rs.getObject("example_question_id") != null ? rs.getInt("example_question_id") : null,
                            rs.getString("continut"),
                            rs.getString("rezumat")
                    );
                    list.add(lecture);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // Metodă pentru a obține lecția după ID
    public static Lecture findById(int id) {
        Lecture lecture = null;
        Connection conn = DataBase.GetInfo();
        String sql = "SELECT * FROM Lecture WHERE lecture_id = ?";

        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                lecture = new Lecture(
                        rs.getInt("lecture_id"),
                        rs.getString("titlu"),
                        rs.getInt("dificultate"),
                        rs.getObject("example_question_id") != null ? rs.getInt("example_question_id") : null,
                        rs.getString("continut"),
                        rs.getString("rezumat")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lecture;
    }

    // Metodă pentru a adăuga o lecție
    public static boolean insert(Lecture lecture) {
        Connection conn = DataBase.GetInfo();
        String sql = "INSERT INTO Lecture (titlu, dificultate, example_question_id, continut,rezumat) VALUES (?, ?, ?, ?,?)";

        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, lecture.getTitlu());
            stmt.setInt(2, lecture.getDificultate());

            if (lecture.getExampleQuestionId() != null) {
                stmt.setInt(3, lecture.getExampleQuestionId());
            } else {
                stmt.setNull(3, Types.INTEGER);
            }

            stmt.setString(4, lecture.getContinut());
            stmt.setString(5, lecture.getRezumat());

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Metodă pentru a obține ultima lecție adăugată
    public static Lecture findLast() {
        Lecture lecture = null;
        Connection conn = DataBase.GetInfo();
        String sql = "SELECT * FROM Lecture ORDER BY lecture_id DESC LIMIT 1";

        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(sql);

            if (rs.next()) {
                lecture = new Lecture(
                        rs.getInt("lecture_id"),
                        rs.getString("titlu"),
                        rs.getInt("dificultate"),
                        rs.getObject("example_question_id") != null ? rs.getInt("example_question_id") : null,
                        rs.getString("continut"),
                        rs.getString("rezumat")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lecture;
    }
}
