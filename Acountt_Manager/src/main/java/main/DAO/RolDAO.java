package main.DAO;


import Objects.Rol;
import main.Sistem.DataBase;

import java.sql.*;

public class RolDAO {
    public static Rol findById(int ID) {
        Rol rol = null;
        String sql = "SELECT * FROM roluri where ID= '" + ID + "'";
        try (Connection conn = DataBase.GetInfo();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    rol = new Rol(
                            rs.getInt("ID"),
                            rs.getString("Rol")

                    );
                    rol.setPermisiuni(PermisiuniDAO.getPermisions(rol.getID()));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rol;
    }

    public static Rol findByNume(String nume) {
        Rol rol = null;
        String sql = "SELECT * FROM roluri WHERE Rol = '" + nume + "'";
        try (Connection conn = DataBase.GetInfo();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    rol = new Rol(
                            rs.getInt("ID"),
                            rs.getString("Rol")
                    );
                    rol.setPermisiuni(PermisiuniDAO.getPermisions(rol.getID()));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return rol;
    }


    public static Rol findByUserID(int ID) {
        Rol rol = null;
        String sql = "SELECT * FROM roluri WHERE ID = (SELECT ID_rol FROM relatie_cont_rol WHERE ID_cont= '" + ID + "');";
        try (Connection conn = DataBase.GetInfo();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    rol = new Rol(
                            rs.getInt("ID"),
                            rs.getString("Rol")

                    );
                    rol.setPermisiuni(PermisiuniDAO.getPermisions(rol.getID()));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return rol;
    }
}