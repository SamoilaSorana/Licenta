package main.DAO;


import Objects.RequiredLecture;
import main.Sistem.DataBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class RequiredLectureDAO {

    public static boolean insert(RequiredLecture rl) {
        Connection conn = DataBase.GetInfo();
        try {
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO required_lecture (current_lecture_id, required_lecture_id) VALUES (?, ?)"
            );
            ps.setInt(1, rl.getCurrentLectureId());
            ps.setInt(2, rl.getRequiredLectureId());
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static ArrayList<RequiredLecture> findAll() {
        ArrayList<RequiredLecture> list = new ArrayList<>();
        try {
            Connection conn = DataBase.GetInfo();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM required_lecture");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                RequiredLecture rl = new RequiredLecture(
                        rs.getInt("current_lecture_id"),
                        rs.getInt("required_lecture_id")
                );
                list.add(rl);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static boolean delete(int currentLectureId, int requiredLectureId) {
        try {
            Connection conn = DataBase.GetInfo();
            PreparedStatement ps = conn.prepareStatement(
                    "DELETE FROM required_lecture WHERE current_lecture_id = ? AND required_lecture_id = ?"
            );
            ps.setInt(1, currentLectureId);
            ps.setInt(2, requiredLectureId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean update(int currentLectureId, int requiredLectureId, RequiredLecture newValues) {
        try {
            Connection conn = DataBase.GetInfo();
            PreparedStatement ps = conn.prepareStatement(
                    "UPDATE required_lecture SET current_lecture_id = ?, required_lecture_id = ? " +
                            "WHERE current_lecture_id = ? AND required_lecture_id = ?"
            );
            ps.setInt(1, newValues.getCurrentLectureId());
            ps.setInt(2, newValues.getRequiredLectureId());
            ps.setInt(3, currentLectureId);
            ps.setInt(4, requiredLectureId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
