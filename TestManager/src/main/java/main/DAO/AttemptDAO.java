package main.DAO;

import Objects.Attempt;
import main.Sistem.DataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AttemptDAO {
    public static boolean insert(Attempt attempt) {
        String sql = "INSERT INTO attempts(user_id, lecture_id) VALUES (?, ?)";
        try {
            PreparedStatement statement = DataBase.GetInfo().prepareStatement(sql);
            statement.setInt(1, attempt.getUserId());
            statement.setInt(2, attempt.getLectureId());
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<Attempt> findAll() {
        List<Attempt> list = new ArrayList<>();
        String sql = "SELECT * FROM attempts";
        try {
            Statement statement = DataBase.GetInfo().createStatement();
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

