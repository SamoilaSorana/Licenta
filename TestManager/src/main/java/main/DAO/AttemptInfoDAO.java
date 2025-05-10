package main.DAO;


import Objects.AttemptInfo;
import main.Sistem.DataBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class AttemptInfoDAO {

    public static boolean insert(AttemptInfo info) {
        Connection conn = DataBase.GetInfo();
        String sql = "INSERT INTO attemp_info (attempt_id, question_id, answer_id) VALUES (?, ?, ?)";

        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, info.getAttemptId());
            stmt.setInt(2, info.getQuestionId());
            stmt.setInt(3, info.getAnswerId());

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DataBase.closeConnection();
        }
    }

    public static boolean insert(List<AttemptInfo> infoList) {
        Connection conn = DataBase.GetInfo();
        String sql = "INSERT INTO attemp_info (attempt_id, question_id, answer_id) VALUES (?, ?, ?)";

        try {
            PreparedStatement stmt = conn.prepareStatement(sql);

            for (AttemptInfo info : infoList) {
                stmt.setInt(1, info.getAttemptId());
                stmt.setInt(2, info.getQuestionId());
                stmt.setInt(3, info.getAnswerId());
                stmt.addBatch();
            }

            int[] results = stmt.executeBatch();

            for (int r : results) {
                if (r == Statement.EXECUTE_FAILED) return false;
            }

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DataBase.closeConnection();
        }
    }
}
