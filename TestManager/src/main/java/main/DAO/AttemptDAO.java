package main.DAO;

import Objects.Attempt;
import main.Sistem.DataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AttemptDAO {
    public static int insert(Attempt attempt) {
        String sql = "INSERT INTO attempts(user_id, lecture_id) VALUES (?, ?)";
        int generatedId = -1;

        try (Connection conn = DataBase.GetInfo()){
            PreparedStatement statement =conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, attempt.getUserId());
            statement.setInt(2, attempt.getLectureId());
            statement.executeUpdate();

            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                generatedId = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return generatedId;
    }
    public static List<Attempt> findAll() {
        List<Attempt> list = new ArrayList<>();
        String sql = "SELECT * FROM attempts";
        try (Connection conn = DataBase.GetInfo()){

            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                Attempt a = new Attempt(
                        rs.getInt("attempt_id"),
                        rs.getInt("user_id"),
                        rs.getInt("lecture_id")
                );
                list.add(a);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }




    public static int findcount(int userId, int lectureId) {
      int count = 0;
        String sql = "SELECT COUNT(*) AS cnt FROM attempts WHERE user_id='"+userId+"' AND lecture_id='"+lectureId+"'";
        try (Connection conn = DataBase.GetInfo()){

            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            if (rs.next()) {
                        count=rs.getInt("cnt");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return count;
    }

    public static List<Attempt> findAllbyUserId(int userId) {
        List<Attempt> list = new ArrayList<>();
        String sql = "SELECT * FROM attempts WHERE user_id = '" + userId + "'";
        try (Connection conn = DataBase.GetInfo()){

            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                Attempt a = new Attempt(
                        rs.getInt("attempt_id"),
                        rs.getInt("user_id"),
                        rs.getInt("lecture_id")
                );
                list.add(a);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}

