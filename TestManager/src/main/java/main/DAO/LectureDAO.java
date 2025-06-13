package main.DAO;


import Objects.Lecture;
import main.Sistem.DataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LectureDAO {


    public static List<Lecture> findAll() {
        List<Lecture> list = new ArrayList<>();

        String sql = "SELECT * FROM Lecture";

        try (Connection conn = DataBase.GetInfo()){

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
                    lecture.setChapter(ChapterDAO.findById(rs.getInt("chapter_id")));
                    list.add(lecture);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }


    public static Lecture findById(int id) {
        Lecture lecture = null;

        String sql = "SELECT * FROM Lecture WHERE lecture_id = ?";

        try (Connection conn = DataBase.GetInfo()){

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
                lecture.setChapter(ChapterDAO.findById(rs.getInt("chapter_id")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lecture;
    }


    public static boolean insert(Lecture lecture) {

        String sql = "INSERT INTO Lecture (titlu, dificultate, example_question_id, continut,rezumat) VALUES (?, ?, ?, ?,?)";

        try (Connection conn = DataBase.GetInfo()){

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


    public static Lecture findLast() {
        Lecture lecture = null;

        String sql = "SELECT * FROM Lecture ORDER BY lecture_id DESC LIMIT 1";

        try (Connection conn = DataBase.GetInfo()){

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
                lecture.setChapter(ChapterDAO.findById(rs.getInt("chapter_id")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lecture;
    }


    public static List<Lecture> findByChapterId(int chapterId) {
        List<Lecture> lectures = new ArrayList<>();


        String sql = "SELECT * FROM lecture WHERE chapter_id = ?";

        try (Connection conn = DataBase.GetInfo()){

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, chapterId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Lecture lecture = new Lecture(
                        rs.getInt("lecture_id"),
                        rs.getString("titlu"),
                        rs.getInt("dificultate"),
                        rs.getObject("example_question_id") != null ? rs.getInt("example_question_id") : null,
                        rs.getString("continut"),
                        rs.getString("rezumat")
                );

                lecture.setChapter(ChapterDAO.findById(rs.getInt("chapter_id")));
                lectures.add(lecture);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lectures;
    }
}

