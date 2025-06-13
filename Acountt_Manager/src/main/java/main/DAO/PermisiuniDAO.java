package main.DAO;


import Objects.Permisiune;
import main.Sistem.DataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PermisiuniDAO {
    public static List<Permisiune> getPermisions(int rol) {
        List<Permisiune> PermisionList = new ArrayList<>();
        String sql = "SELECT * FROM permisiuni WHERE ID IN (SELECT ID_permisiune FROM relatie_rol_permisiune WHERE ID_rol='" + rol + "');";
        try (Connection conn = DataBase.GetInfo();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            try (ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    PermisionList.add(new Permisiune(
                            rs.getInt("ID"),
                            rs.getString("Permisiune")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return PermisionList;


    }
    public static List<Permisiune> getPermisionsByRol(String rol) {

        List<Permisiune> PermisionList = new ArrayList<>();
        System.out.println(rol);
        String sql = "SELECT p.ID AS ID,p.Permisiune AS Permisiune FROM permisiuni p JOIN relatie_rol_permisiune rp ON p.ID=rp.ID_permisiune JOIN roluri r ON rp.ID_rol=r.ID WHERE r.Rol='"+rol+"'";
        try (Connection conn = DataBase.GetInfo();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            try (ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    PermisionList.add(new Permisiune(
                            rs.getInt("ID"),
                            rs.getString("Permisiune")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return PermisionList;


    }
}
