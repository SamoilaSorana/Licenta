package main.DAO;


import main.Sistem.DataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionDAO {
    public static List<Question> findAll() {
        List<Question> list = new ArrayList<>();
        Connection conn = DataBase.GetInfo();
        String sql = "SELECT * FROM questions";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Question q = new Question(
                        rs.getInt("question_id"),
                        rs.getString("text")
                );
                list.add(q);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public static boolean insert(Question question) {
        Connection conn = DataBase.GetInfo();
        String sql = "INSERT INTO questions (text) VALUES (?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, question.getText());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Question findLast() {
        Connection conn = DataBase.GetInfo();
        String sql = "SELECT * FROM questions ORDER BY question_id DESC LIMIT 1";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return new Question(rs.getInt("question_id"), rs.getString("text"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
