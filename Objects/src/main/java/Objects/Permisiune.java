package Objects;

public class Permisiune {
    int ID;
    String  permisiune_name;

    public Permisiune(int ID, String permisiune_name) {
        this.ID = ID;
        this.permisiune_name = permisiune_name;
    }

    public Permisiune() {
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getPermisiune_name() {
        return permisiune_name;
    }

    public void setPermisiune_name(String permisiune_name) {
        this.permisiune_name = permisiune_name;
    }

    @Override
    public String toString() {
        return "Permisiune{" +
                "ID=" + ID +
                ", permisiune_name='" + permisiune_name + '\'' +
                '}';
    }

}
