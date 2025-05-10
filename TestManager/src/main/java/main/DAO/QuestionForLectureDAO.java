package main.DAO;


import Objects.QuestionForLecture;
import main.Sistem.DataBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class QuestionForLectureDAO {

    public static boolean insert(QuestionForLecture qfl) {
        String sql = "INSERT INTO questions_for_lecture (lecture_id, question_id) VALUES (?, ?)";

        try (Connection conn = DataBase.GetInfo();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, qfl.getLectureId());
            stmt.setInt(2, qfl.getQuestionId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            DataBase.closeConnection();
        }
        return false;
    }

    public static ArrayList<QuestionForLecture> findAll() {
        ArrayList<QuestionForLecture> list = new ArrayList<>();
        try (var conn = DataBase.GetInfo();
             var stmt = conn.prepareStatement("SELECT * FROM questions_for_lecture");
             var rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add(new QuestionForLecture(
                        rs.getInt("lecture_id"),
                        rs.getInt("question_id")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            DataBase.closeConnection();
        }
        return list;
    }
}
