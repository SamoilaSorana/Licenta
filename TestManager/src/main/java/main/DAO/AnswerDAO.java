package main.DAO;


import Objects.Answer;
import main.Sistem.DataBase;

import java.sql.*;
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
        return null;
    }
}
