package main.DAO;


import Objects.Answer;
import Objects.Question;
import main.Sistem.DataBase;

import java.sql.*;
import java.util.*;

public class QuestionDAO {
    public static List<Question> findAll() {
        List<Question> list = new ArrayList<>();
        String sql = "SELECT * FROM questions";

        try (Connection conn = DataBase.GetInfo();

            Statement stmt = conn.createStatement();
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


    public static List<Map<String, Object>> convertToSerializableList(List<AbstractMap.SimpleEntry<Answer, Boolean>> entries) {
        List<Map<String, Object>> result = new ArrayList<>();

        for (AbstractMap.SimpleEntry<Answer, Boolean> entry : entries) {
            Map<String, Object> map = new HashMap<>();
            map.put("answer", entry.getKey());      // Answer object
            map.put("correct", entry.getValue());   // Boolean
            result.add(map);
        }

        return result;
    }


    public static List<Question> findbylectureId(int lectureId) {
        List<Question> list = new ArrayList<>();

        String sql = "SELECT Q.question_id, Q.text FROM questions Q  JOIN questions_for_lecture QL ON Q.question_id = QL.question_id WHERE QL.lecture_id='"+lectureId+"'";

        try (Connection conn = DataBase.GetInfo();

            Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Question q = new Question(
                        rs.getInt("question_id"),
                        rs.getString("text")
                );
                q.setAnswers(convertToSerializableList(AnswerDAO.findbyquestionId(q.getId())));
                list.add(q);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


        return list;
    }




    public static boolean insert(Question question) {

        String sql = "INSERT INTO questions (text) VALUES (?)";

        try (Connection conn = DataBase.GetInfo();

            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, question.getText());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    public static Question findLast() {

        String sql = "SELECT * FROM questions ORDER BY question_id DESC LIMIT 1";

        try (Connection conn = DataBase.GetInfo();

            Statement stmt = conn.createStatement();
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
