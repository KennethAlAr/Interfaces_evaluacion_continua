package org.example.model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Elemento {
    //Usamos SimpleProperty para vigilar si la variable se va modificando e ir actualizando los datos en tiempo real.
    private SimpleStringProperty nombre;
    private SimpleIntegerProperty cantidad;
    private SimpleStringProperty descripcion;
    private SimpleBooleanProperty comprado;

    public Elemento(String nombre, int cantidad, String descripcion, boolean comprado){
        this.nombre = new SimpleStringProperty(nombre);
        this.cantidad = new SimpleIntegerProperty(cantidad);
        this.descripcion = new SimpleStringProperty(descripcion);
        this.comprado = new SimpleBooleanProperty(comprado);
    }

    public String getNombre() { return nombre.get(); }
    public void setNombre(String n) { nombre.set(n); }
    public SimpleStringProperty nombreProperty() { return nombre;}

    public int getCantidad() { return cantidad.get(); }
    public void setCantidad(int c) { cantidad.set(c); }
    public SimpleIntegerProperty cantidadProperty() { return cantidad;}

    public String getDescripcion() { return descripcion.get(); }
    public void setDescripcion(String d) { descripcion.set(d); }
    public SimpleStringProperty descripcionProperty() { return descripcion;}

    public Boolean isComprado() { return comprado.get(); }
    public void setComprado(Boolean b) { comprado.set(b); }
    public SimpleBooleanProperty compradoProperty() { return comprado;}
}
