package main.DAO;
import Objects.Chapter;
import main.Sistem.DataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChapterDAO {

    public static Chapter findById(int id) {
        String sql = "SELECT C.chapter_id, C.name, G.name AS grade, S.name AS subject FROM chapters  C JOIN grades G ON C.grade_id = G.grade_id JOIN subjects S ON C.subject_id = S.subject_id WHERE C.chapter_id ='"+id+"'";
        try (Connection conn = DataBase.GetInfo();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Chapter(rs.getInt("chapter_id"),
                        rs.getString("name"),
                        rs.getString("subject"),
                        rs.getString("grade"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            DataBase.closeConnection();
        }
        return null;
    }


    public static List<Chapter> findAll() {
        List<Chapter> chapters = new ArrayList<>();
        String sql = "SELECT C.chapter_id, C.name, G.name AS grade, S.name AS subject FROM chapters  C JOIN grades G ON C.grade_id = G.grade_id JOIN subjects S ON C.subject_id = S.subject_id ";
        Connection conn = DataBase.GetInfo();

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                chapters.add( new Chapter(rs.getInt("chapter_id"),
                        rs.getString("name"),
                        rs.getString("subject"),
                        rs.getString("grade")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            DataBase.closeConnection();
        }
        return chapters;
    }
}
