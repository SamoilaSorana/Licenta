package main.DAO;


import Objects.Completed;
import main.Sistem.DataBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
    }
}
