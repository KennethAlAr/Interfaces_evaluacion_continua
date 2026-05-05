package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.example.model.Elemento;

public class FilaNuevoElementoController {

    @FXML private TextField txtNuevoNombreElemento;
    @FXML private TextField txtNuevaCantidadElemento;
    @FXML private TextField txtNuevaDescripcionElemento;

    private DetalleController detalleController;

    //Guarda quien es el controlador "jefe" de la lista para poder enviarle los datos.
    public void setJefe(DetalleController jefe) { this.detalleController = jefe; }

    //Recoge los textos escritos y se los envía al controlador "jefe" para que los valide y actualice los datos del json.
    //Si la creación tiene éxito, vacía los campos y vuelve a "pintar" un elemento vacío para crear.
    @FXML
    public void accionAnadirElemento() {
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
                txtNuevoNombreElemento.clear();
                txtNuevaCantidadElemento.clear();
                txtNuevaDescripcionElemento.clear();
            }
        }
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
}