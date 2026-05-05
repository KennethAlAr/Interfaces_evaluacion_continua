package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.model.Lista;

public class EditarListaController {

    @FXML private TextField txtEditarNombreLista;
    @FXML private TextField txtEditarDescripcionLista;

    private DetalleController detalleController;

    //Recibe la lista que se va a editar y el controlador que hizo la llamada, rellenando los campos de texto con los
    //datos actuales.
    public void cargarDatos(Lista lista, DetalleController jefe) {
        this.detalleController = jefe;
        txtEditarNombreLista.setText(lista.getNombre());
        txtEditarDescripcionLista.setText(lista.getDescripcion());
    }

    //Recoge los datos introducidos por el usuario y le dice a DetalleController que haga la comprobación. Si son correctos
    //los modifica y actualiza.
    @FXML
    public void accionGuardarEdicion() {
        String nuevoNombre = txtEditarNombreLista.getText().trim();
        String nuevaDesc = txtEditarDescripcionLista.getText().trim();

        if (nuevaDesc.isEmpty()) {
            nuevaDesc = "";
        }

        if (detalleController != null) {
            boolean exito = detalleController.guardarEdicionLista(nuevoNombre, nuevaDesc);
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
        Stage stageActual = (Stage) txtEditarNombreLista.getScene().getWindow();
        stageActual.close();
    }
}