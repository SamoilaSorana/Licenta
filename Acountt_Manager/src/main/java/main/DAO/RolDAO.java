package main.DAO;


import Objects.Rol;
import main.Sistem.DataBase;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RolDAO {
    public static Rol findById(int ID) {
        Rol rol = null;
        Connection conn = DataBase.GetInfo();
        String sql = "SELECT * FROM roluri where ID= '" + ID + "'";
        try {
            Statement stm = conn.createStatement();
            try (ResultSet rs = stm.executeQuery(sql)) {
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
        finally {
            DataBase.closeConnection();
        }

        return rol;
    }

    public static Rol findByNume(String nume) {
        Rol rol = null;
        Connection conn = DataBase.GetInfo();
        String sql = "SELECT * FROM roluri WHERE Rol = '" + nume + "'";
        try {
            Statement stm = conn.createStatement();
            try (ResultSet rs = stm.executeQuery(sql)) {
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
        finally {
            DataBase.closeConnection();
        }

        return rol;
    }


    public static Rol findByUserID(int ID) {
        Rol rol = null;
        Connection conn = DataBase.GetInfo();
        String sql = "SELECT * FROM roluri WHERE ID = (SELECT ID_rol FROM relatie_cont_rol WHERE ID_cont= '" + ID + "');";
        try {
            Statement stm = conn.createStatement();
            try (ResultSet rs = stm.executeQuery(sql)) {
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
        finally {
            DataBase.closeConnection();
        }

        return rol;
    }
}