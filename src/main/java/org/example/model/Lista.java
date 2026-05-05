package org.example.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.SimpleStringProperty;

import java.util.List;

public class Lista {
    //Usamos SimpleProperty para vigilar si la variable se va modificando e ir actualizando los datos en tiempo real.
    private SimpleStringProperty nombre;
    private SimpleStringProperty descripcion;
    private ObservableList<Elemento> elementos = FXCollections.observableArrayList();;

    public Lista (String nombre, String descripcion, List<Elemento> elementos){
        this.nombre = new SimpleStringProperty(nombre);
        this.descripcion = new SimpleStringProperty(descripcion);
        this.elementos = FXCollections.observableArrayList(elementos);
    }

    public Lista (String nombre, String descripcion){
        this.nombre = new SimpleStringProperty(nombre);
        this.descripcion = new SimpleStringProperty(descripcion);
    }

    public String getNombre() { return nombre.get(); }
    public void setNombre(String n) { nombre.set(n); }
    public SimpleStringProperty nombreProperty() { return nombre;}

    public String getDescripcion() { return descripcion.get(); }
    public void setDescripcion(String d) { descripcion.set(d); }
    public SimpleStringProperty descripcionProperty() { return descripcion;}

    public ObservableList<Elemento> getElementos() { return elementos; }
}
