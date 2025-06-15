package main.DAO;


import Objects.Acount;
import main.Sistem.DataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AcountDAO {
    public static List<Acount> findAll() {
        List<Acount> AcountList = new ArrayList<>();
        String sql = "SELECT * FROM conturi";
         try (Connection conn = DataBase.GetInfo();
              PreparedStatement stmt = conn.prepareStatement(sql)) {
            try (ResultSet rs = stmt.executeQuery()) {
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
        String sql = "SELECT * FROM conturi where ID= '" + ID + "'";
        try (Connection conn = DataBase.GetInfo();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            try (ResultSet rs = stmt.executeQuery()) {
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

    public static String findUserName(int ID) {
        String name ="";
        String sql = "SELECT Nume,Prenume FROM conturi where ID= '" + ID + "'";
        try (Connection conn = DataBase.GetInfo();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    name = "\""+ rs.getString("Nume") + " " + rs.getString("Prenume")+"\"";
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return name;
    }

    public static Acount findByUsername(String username) {
        Acount acount = null;
        String sql = "SELECT * FROM conturi where Username= '" + username + "'";
        try (Connection conn = DataBase.GetInfo();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            try (ResultSet rs = stmt.executeQuery()) {
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
        String sql = "INSERT INTO conturi(Username,Parola,Nume,Prenume,Email) VALUES(?,?,?,?,?)";
        try (Connection conn = DataBase.GetInfo();
             PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setString(1, acount.getUsername());
            stm.setString(2, acount.getParola());
            stm.setString(3, acount.getNume());
            stm.setString(4, acount.getPrenume());
            stm.setString(5, acount.getEmail());
            stm.execute();

            ResultSet rs = stm.getGeneratedKeys();
            if (rs.next()) {
                acount.setID(rs.getInt(1));
            }

            inserted = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return inserted;
    }



}
