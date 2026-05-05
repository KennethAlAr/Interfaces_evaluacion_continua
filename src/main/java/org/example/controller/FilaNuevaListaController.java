package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class FilaNuevaListaController {

    @FXML private TextField txtNuevoNombre;
    @FXML private TextField txtNuevaDesc;

    private InicioController inicioController;

    //Guarda quien es el controlador "jefe" de la lista para poder enviarle los datos.
    public void setJefe(InicioController jefe) {
        this.inicioController = jefe;
    }

    //Recoge los textos escritos y se los envía al controlador "jefe" para que los valide y actualice los datos del json.
    //Si la creación tiene éxito, vacía los campos y vuelve a "pintar" un elemento vacío para crear.
    @FXML
    private void accionAnadir() {
        String nombre = txtNuevoNombre.getText();
        String desc = txtNuevaDesc.getText();

        if (desc.trim().isEmpty()) {
            desc = "";
        }

        boolean exito = inicioController.anadirNuevaLista(nombre, desc);
        if (exito) {
            txtNuevoNombre.clear();
            txtNuevaDesc.clear();
        }
    }
}