package main.DAO;


import Objects.Answer;
import Objects.AttemptInfo;
import Objects.Chapter;
import main.Sistem.DataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AttemptInfoDAO {

    public static boolean insert(AttemptInfo info) {

        String sql = "INSERT INTO attempt_info (attempt_id, question_id, answer_id) VALUES (?, ?, ?)";

        try (Connection conn = DataBase.GetInfo()) {

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, info.getAttemptId());
            stmt.setInt(2, info.getQuestionId());
            stmt.setInt(3, info.getAnswerId());

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;

        }
    }

    public static boolean insert(List<AttemptInfo> infoList) {

        String sql = "INSERT INTO attempt_info (attempt_id, question_id, answer_id) VALUES (?, ?, ?)";

        try (Connection conn = DataBase.GetInfo()) {

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

        }
    }


    public static List<AttemptInfo> findByAttemptId(int id) {
        List<AttemptInfo> infoList = new ArrayList<>();
        String sql ="SELECT * FROM attempt_info WHERE attempt_id ='"+id+"'";
        try (Connection conn = DataBase.GetInfo();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                AttemptInfo attemptInfo = new AttemptInfo(
                        rs.getInt("attempt_id"),
                        rs.getInt("question_id"),
                        rs.getInt("answer_id")

                );
                infoList.add(attemptInfo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return infoList;
    }



}
