package main.DAO;


import Objects.QuestionAnswer;
import main.Sistem.DataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionAnswerDAO {

    public static boolean insert(QuestionAnswer qa) {
        String sql = "INSERT INTO question_answers (question_id, answer_id, is_right_answer) VALUES (?, ?, ?)";
        try (Connection conn = DataBase.GetInfo();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, qa.getQuestionId());
            stmt.setInt(2, qa.getAnswerId());
            stmt.setBoolean(3, qa.isRightAnswer());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        finally {
            DataBase.closeConnection();
        }
    }

    public static List<QuestionAnswer> findAll() {
        List<QuestionAnswer> list = new ArrayList<>();
        String sql = "SELECT * FROM question_answer";

        try (Connection conn = DataBase.GetInfo();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                QuestionAnswer qa = new QuestionAnswer(
                        rs.getInt("id"),
                        rs.getInt("question_id"),
                        rs.getInt("answer_id"),
                        rs.getBoolean("is_right_answer")
                );
                list.add(qa);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            DataBase.closeConnection();
        }

        return list;
    }

    public static List<QuestionAnswer> findByQuestionId(int questionId) {
        List<QuestionAnswer> list = new ArrayList<>();
        String sql = "SELECT * FROM question_answer WHERE question_id = ?";

        try (Connection conn = DataBase.GetInfo();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, questionId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                QuestionAnswer qa = new QuestionAnswer(
                        rs.getInt("id"),
                        rs.getInt("question_id"),
                        rs.getInt("answer_id"),
                        rs.getBoolean("is_right_answer")
                );
                list.add(qa);
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
