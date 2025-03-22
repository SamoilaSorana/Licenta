package main.DAO;

import main.Objects.Acount;
import main.Objects.Permisiune;
import main.Sistem.DataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AcountDAO {
    public static List<Acount> findAll() {
        List<Acount> AcountList = new ArrayList<>();
        Connection conn = DataBase.GetInfo();
        String sql = "SELECT * FROM conturi";
        try {
            Statement stm = conn.createStatement();
            try (ResultSet rs = stm.executeQuery(sql)) {
                while (rs.next()) {

                    Acount acount =new Acount(
                            rs.getInt("ID"),
                            rs.getString("Username"),
                            rs.getString("Parola"),
                            rs.getString("Nume"),
                            rs.getString("Prenume"),
                            rs.getString("Email")
                    );
                     acount.setRol(RolDAO.findByUserID(acount.getID()));
                    AcountList.add(acount);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return AcountList;
    }

    public static Acount findById_User(int ID) {
        Acount acount = null;
        Connection conn = DataBase.GetInfo();
        String sql = "SELECT * FROM conturi where ID= '" + ID + "'";
        try {
            Statement stm = conn.createStatement();
            try (ResultSet rs = stm.executeQuery(sql)) {
                if (rs.next()) {
                    acount = new Acount(
                            rs.getInt("ID"),
                            rs.getString("Username"),
                            rs.getString("Parola"),
                            rs.getString("Nume"),
                            rs.getString("Prenume"),
                            rs.getString("Email")
                    );
                    acount.setRol(RolDAO.findByUserID(acount.getID()));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return acount;
    }

    public static Acount findByUsername(String username) {
        Acount acount = null;
        Connection conn = DataBase.GetInfo();
        String sql = "SELECT * FROM conturi where Username= '" + username + "'";
        try {
            Statement stm = conn.createStatement();
            try (ResultSet rs = stm.executeQuery(sql)) {
                if (rs.next()) {
                    acount = new Acount(
                            rs.getInt("ID"),
                            rs.getString("Username"),
                            rs.getString("Parola"),
                            rs.getString("Nume"),
                            rs.getString("Prenume"),
                            rs.getString("Email")
                    );
                    acount.setRol(RolDAO.findByUserID(acount.getID()));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return acount;
    }



    public static boolean insert(Acount acount) {
        boolean inserted = false;
        Connection conn = DataBase.GetInfo();
        String sql = "INSERT INTO conturi(ID,Username,Parola,Nume,Prenume,Email) "
                + "VALUES(?,?,?,?,?,?)";
        try {
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setInt(1, acount.getID());
            stm.setString(2, acount.getUsername());
            stm.setString(3, acount.getParola());
            stm.setString(4, acount.getNume());
            stm.setString(5, acount.getPrenume());
            stm.setString(6, acount.getEmail());
            stm.execute();
            inserted = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return inserted;
    }


}
