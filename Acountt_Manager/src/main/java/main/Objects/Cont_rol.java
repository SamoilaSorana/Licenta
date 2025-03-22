package main.Objects;

import java.util.List;

public class Cont_rol {
    Acount cont;
    Rol rol;


    public Cont_rol(Acount cont, Rol rol) {
        this.cont = cont;
        this.rol = rol;
    }

    public Cont_rol() {
    }

    public Acount getCont() {
        return cont;
    }

    public void setCont(Acount cont) {
        this.cont = cont;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    @Override
    public String toString() {
        return "Cont_rol{" +
                "cont=" + cont +
                ", rol=" + rol +
                '}';
    }
}
