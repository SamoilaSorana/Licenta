package main.DAO;

import Objects.Help;
import main.Sistem.DataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HelpDAO {

    public static boolean insert(Help help) {
        String sql = "INSERT INTO help (user_id, date, `read`) VALUES (?, ?, ?)";
        try (Connection conn = DataBase.GetInfo();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, help.getUserId());
            stmt.setTimestamp(2, Timestamp.valueOf(help.getDate()));
            stmt.setInt(3, help.getRead());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;

        }
    }
    public static List<Help> findAll() {
        List<Help> list = new ArrayList<>();
        String sql = "SELECT * FROM help";
        try (Connection conn = DataBase.GetInfo();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Help help = new Help(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getTimestamp("date").toLocalDateTime(),
                        rs.getInt("read")
                );

                list.add(help);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


            return list;
        }


    public static Help findById(int id) {
        String sql = "SELECT * FROM help WHERE id = ?";
        try (Connection conn = DataBase.GetInfo();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Help(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getTimestamp("date").toLocalDateTime(),
                        rs.getInt("read")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static boolean update(Help help) {
        String sql = "UPDATE help SET user_id = ?, date = ?, `read` = ? WHERE id = ?";
        try (Connection conn = DataBase.GetInfo();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, help.getUserId());
            stmt.setTimestamp(2, Timestamp.valueOf(help.getDate()));
            stmt.setInt(3, help.getRead());
            stmt.setInt(4, help.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
