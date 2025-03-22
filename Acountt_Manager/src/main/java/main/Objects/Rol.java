package main.Objects;

import java.util.ArrayList;
import java.util.List;

public class Rol {
    int ID;
    String rol_name;
    List<Permisiune> permisiuni;

    public Rol(int ID, String rol_name) {
        this.ID = ID;
        this.rol_name = rol_name;
    }

    public Rol() {
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getRol_name() {
        return rol_name;
    }

    public void setRol_name(String rol_name) {
        this.rol_name = rol_name;
    }

    public List<Permisiune> getPermisiuni() {
        return permisiuni;
    }

    public List<String> getPermisiuniString() {
        List<String> Ps=new ArrayList<String>();
        for(Permisiune p:permisiuni)
        {
            Ps.add(p.getPermisiune_name());
        }
        return Ps;
    }


    public void setPermisiuni(List<Permisiune> permisiuni) {
        this.permisiuni = permisiuni;
    }

    @Override
    public String toString() {
        return "Rol{" +
                "ID=" + ID +
                ", rol_name='" + rol_name + '\'' +
                ", permisiuni=" + permisiuni +
                '}';
    }

}
