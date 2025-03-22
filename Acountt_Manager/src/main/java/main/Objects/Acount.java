package main.Objects;

public class Acount {
    int ID;
    String Username;
    String Parola;
    String Nume;
    String Prenume;
    String Email;
    Rol rol;

    public Acount(int ID, String username, String parola, String nume, String prenume, String email) {
        this.ID = ID;
        Username = username;
        Parola = parola;
        Nume = nume;
        Prenume = prenume;
        Email = email;
    }

    public Acount() {
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getParola() {
        return Parola;
    }

    public void setParola(String parola) {
        Parola = parola;
    }

    public String getNume() {
        return Nume;
    }

    public void setNume(String nume) {
        Nume = nume;
    }

    public String getPrenume() {
        return Prenume;
    }

    public void setPrenume(String prenume) {
        Prenume = prenume;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    @Override
    public String toString() {
        return "Acount{" +
                "ID=" + ID +
                ", Username='" + Username + '\'' +
                ", Parola='" + Parola + '\'' +
                ", Nume='" + Nume + '\'' +
                ", Prenume='" + Prenume + '\'' +
                ", Email='" + Email + '\'' +
                ", Rol='" + rol + '\'' +
                '}';
    }
}
