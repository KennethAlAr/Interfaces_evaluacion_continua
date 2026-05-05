package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.example.model.Elemento;

public class EditarElementoController {

    @FXML private TextField txtEditarNombre;
    @FXML private TextField txtEditarCantidad;
    @FXML private TextField txtEditarDescripcion;

    private Elemento elementoOriginal;
    private DetalleController detalleController;

    //Recibe el elemento que se va a editar y el controllador principal, rellenando los campos de texto con la
    //información actual.
    public void cargarDatos(Elemento elemento, DetalleController jefe) {
        this.elementoOriginal = elemento;
        this.detalleController = jefe;

        txtEditarNombre.setText(elemento.getNombre());
        txtEditarCantidad.setText(String.valueOf(elemento.getCantidad()));
        txtEditarDescripcion.setText(elemento.getDescripcion());
    }

    //Genera y muestra un aviso al usuario.
    private void mensajeAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        Stage stageDeAlerta = (Stage) alerta.getDialogPane().getScene().getWindow();
        stageDeAlerta.getIcons().add(new Image(getClass().getResourceAsStream("/org/example/assets/icono_appuntalo.png")));
        ImageView iconoPersonalizado = new ImageView(new Image(getClass().getResourceAsStream("/org/example/assets/icono_warning.png")));
        iconoPersonalizado.setFitWidth(48);
        iconoPersonalizado.setFitHeight(48);
        alerta.setGraphic(iconoPersonalizado);
        DialogPane dialogPane = alerta.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/org/example/style/estilos.css").toExternalForm());
        alerta.showAndWait();
    }

    //Recoge los datos introducidos por el usuario y verifica que sean válidos. Si lo son, le dice a DetalleController
    //que los modifique y actualice.
    @FXML
    public void accionGuardarEdicion() {
        String nuevoNombre = txtEditarNombre.getText().trim();
        String nuevaDesc = txtEditarDescripcion.getText().trim();
        String cantidadStr = txtEditarCantidad.getText().trim();

        int nuevaCantidad = 1;
        if (!cantidadStr.isEmpty()) {
            try {
                nuevaCantidad = Integer.parseInt(cantidadStr);
            } catch (NumberFormatException e) {
                mensajeAlerta("Cantidad Inválida", "Por favor, introduce un número entero válido en la cantidad.");
                return;
            }
        }

        if (nuevaDesc.isEmpty()) {
            nuevaDesc = "";
        }

        if (detalleController != null) {
            boolean exito = detalleController.editarElemento(elementoOriginal, nuevoNombre, nuevaCantidad, nuevaDesc);
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
        Stage stageActual = (Stage) txtEditarNombre.getScene().getWindow();
        stageActual.close();
    }
}