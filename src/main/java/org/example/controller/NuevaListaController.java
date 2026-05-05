package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class NuevaListaController {

    @FXML private TextField txtNuevoNombreLista;
    @FXML private TextField txtNuevaDescripcionLista;

    private InicioController inicioController;

    //Recibe la referencia al controlador "jefe"
    public void setJefe(InicioController jefe) {
        this.inicioController = jefe;
    }

    //Recoge los datos introducidos por el usuario y le pide al controlador "jefe" que los valide y cree la lista.
    @FXML
    public void accionGuardarLista() {
        String nombre = txtNuevoNombreLista.getText();
        String descripcion = txtNuevaDescripcionLista.getText();

        if (descripcion.trim().isEmpty()) {
            descripcion = "";
        }

        if (inicioController != null) {
            boolean exito = inicioController.anadirNuevaLista(nombre, descripcion);
            if (exito) {
                cerrarVentana();
            }
        }
    }

    //Cancela la edición y cierra la ventana sin guardar los cambios.
    @FXML
    public void accionCancelar() {
        cerrarVentana();
    }

    //Cierra la ventana de edición.
    private void cerrarVentana() {
        Stage stageActual = (Stage) txtNuevoNombreLista.getScene().getWindow();
        stageActual.close();
    }
}