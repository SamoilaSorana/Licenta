package main.DAO;


import Objects.Answer;
import main.Sistem.DataBase;

import java.sql.*;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

public class AnswerDAO {
    public static boolean insert(Answer answer) {
        String sql = "INSERT INTO answers (text) VALUES (?)";
        try (Connection conn = DataBase.GetInfo();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, answer.getText());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        finally {
            DataBase.closeConnection();
        }
    }

    public static List<Answer> findAll() {
        List<Answer> answers = new ArrayList<>();
        String sql = "SELECT * FROM answers";
        try (Connection conn = DataBase.GetInfo();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Answer answer = new Answer(
                        rs.getInt("answer_id"),
                        rs.getString("text")
                );
                answers.add(answer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            DataBase.closeConnection();
        }
        return answers;
    }


    public static List<AbstractMap.SimpleEntry<Answer,Boolean>> findbyquestionId(int question_id) {
        List<AbstractMap.SimpleEntry<Answer,Boolean>>  answers = new ArrayList<>();
        String sql = "SELECT A.answer_id, A.text, QA.is_right_answer FROM answers A JOIN question_answers QA ON A.answer_id = QA.answer_id WHERE QA.question_id='"+question_id+"'";
        try (Connection conn = DataBase.GetInfo();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Answer answer = new Answer(
                        rs.getInt("answer_id"),
                        rs.getString("text")
                );
                answers.add(new AbstractMap.SimpleEntry<>(answer,rs.getBoolean("is_right_answer")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            DataBase.closeConnection();
        }
        return answers;
    }



    public static Answer findById(int id) {
        String sql = "SELECT * FROM answers WHERE answer_id = ?";
        try (Connection conn = DataBase.GetInfo();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Answer(rs.getInt("answer_id"), rs.getString("text"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            DataBase.closeConnection();
        }
        return null;
    }
}
