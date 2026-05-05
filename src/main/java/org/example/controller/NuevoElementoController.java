package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.model.Elemento;

public class NuevoElementoController {

    @FXML private TextField txtNuevoNombreElemento;
    @FXML private TextField txtNuevaCantidadElemento;
    @FXML private TextField txtNuevaDescripcionElemento;

    private DetalleController detalleController;

    //Recibe la referencia al controlador "jefe"
    public void setJefe(DetalleController jefe) {
        this.detalleController = jefe;
    }

    //Recoge los datos introducidos por el usuario y le pide al controlador "jefe" que los valide y cree el elemento.
    @FXML
    public void accionGuardarElemento() {
        String nombre = txtNuevoNombreElemento.getText().trim();
        String descripcion = txtNuevaDescripcionElemento.getText().trim();
        String cantidadStr = txtNuevaCantidadElemento.getText().trim();

        int cantidad = 1;
        if (!cantidadStr.isEmpty()) {
            try {
                cantidad = Integer.parseInt(cantidadStr);
            } catch (NumberFormatException e) {
                mensajeAlerta("Cantidad Inválida", "Por favor, introduce un número entero válido en la cantidad.");
                return;
            }
        }

        if (descripcion.isEmpty()) {
            descripcion = "";
        }

        Elemento nuevoElemento = new Elemento(nombre, cantidad, descripcion, false);

        if (detalleController != null) {
            boolean exito = detalleController.anadirNuevoElemento(nuevoElemento);
            if (exito) {
                cerrarVentana();
            }
        }
    }

    //Cancela la edición y cierra la ventana sin guardar los cambios.
    @FXML public void accionCancelar() {
        cerrarVentana();
    }

    //Cierra la ventana de edición.
    private void cerrarVentana() {
        Stage stageActual = (Stage) txtNuevoNombreElemento.getScene().getWindow();
        stageActual.close();
    }

    //Genera y muestra un aviso al usuario.
    private void mensajeAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}