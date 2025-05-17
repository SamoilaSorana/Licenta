package main.DAO;


import Objects.Permisiune;
import main.Sistem.DataBase;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PermisiuniDAO {
    public static List<Permisiune> getPermisions(int rol) {
        List<Permisiune> PermisionList = new ArrayList<>();
        Connection conn = DataBase.GetInfo();
        String sql = "SELECT * FROM permisiuni WHERE ID IN (SELECT ID_permisiune FROM relatie_rol_permisiune WHERE ID_rol='" + rol + "');";
        try {
            Statement stm = conn.createStatement();
            try (ResultSet rs = stm.executeQuery(sql)) {
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
        finally {
            DataBase.closeConnection();
        }
        return PermisionList;


    }
    public static List<Permisiune> getPermisionsByRol(String rol) {

        List<Permisiune> PermisionList = new ArrayList<>();
        Connection conn = DataBase.GetInfo();
        System.out.println(rol);
        String sql = "SELECT p.ID AS ID,p.Permisiune AS Permisiune FROM permisiuni p JOIN relatie_rol_permisiune rp ON p.ID=rp.ID_permisiune JOIN roluri r ON rp.ID_rol=r.ID WHERE r.Rol='"+rol+"'";
        try {
            Statement stm = conn.createStatement();
            try (ResultSet rs = stm.executeQuery(sql)) {
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
        finally {
            DataBase.closeConnection();
        }
        return PermisionList;


    }
  //
}
