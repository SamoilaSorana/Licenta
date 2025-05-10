package main.DAO;


import Objects.Answer;
import Objects.Completed;
import main.Sistem.DataBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CompletedDAO {

    public static boolean insert(Completed completed) {
        Connection conn = DataBase.GetInfo();
        String sql = "INSERT INTO completed (user_id, lecture_id) VALUES (?, ?)";

        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, completed.getUserId());
            stmt.setInt(2, completed.getLectureId());

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        finally {
            DataBase.closeConnection();
        }
    }

    public static List<Completed> findById(int id) {
        List<Completed> completedList = new ArrayList<>();
        String sql = "SELECT * FROM completed WHERE user_id = ?";
        try (Connection conn = DataBase.GetInfo();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                completedList.add(new Completed(rs.getInt("user_id"), rs.getInt("lecture_id")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            DataBase.closeConnection();
        }
        return completedList;
    }
}
