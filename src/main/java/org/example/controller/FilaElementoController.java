package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import org.example.model.Elemento;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FilaElementoController {

    @FXML private HBox hboxPrincipal;
    @FXML private Label lblNombreElemento;
    @FXML private Label lblCantidadElemento;
    @FXML private Label lblDescripcionElemento;
    @FXML private Button btnMarcarComprado;
    @FXML private Button btnBorrarElemento;
    @FXML private Button btnEditarElemento;

    private Elemento elementoAsignado;
    private DetalleController detalleController;

    //Recibe el elemento de la fila y el controlador "jefe" rellenando las etiquetas de texto.
    public void cargarDatos(Elemento elemento, DetalleController jefe) {
        this.elementoAsignado = elemento;
        this.detalleController = jefe;

        lblNombreElemento.setText(elemento.getNombre());
        lblCantidadElemento.setText(String.valueOf(elemento.getCantidad()));
        lblDescripcionElemento.setText(elemento.getDescripcion());

        actualizarEstiloComprado();
    }

    //Utiliza el btnMarcarComprado para alternar el estado del elemento y guardar la información.
    @FXML
    private void accionMarcarComprado() {
        elementoAsignado.setComprado(!elementoAsignado.isComprado());

        //usa el método actualizarEstiloComprado para modificar su aspecto.
        actualizarEstiloComprado();
        detalleController.guardarCambios();
    }

    //Le dice a DetalleController que elimine el elemento de la lista y actualice el json.
    @FXML
    private void accionBorrarElemento() {
        detalleController.eliminarElemento(elementoAsignado);
    }

    //Cambia la visualización de la fila cuando está marcada con "comprado".
    private void actualizarEstiloComprado() {
        if (elementoAsignado.isComprado()) {
            hboxPrincipal.setOpacity(0.5);
        } else {
            hboxPrincipal.setOpacity(1.0);
        }
    }

    //Abre la ventana emergente de edición pasándole el elemento actual y el controlador para poder modificar su nombre,
    // cantidad o descripción. La comprobación de los datos se hace en EditarElementoController o DetalleController.
    @FXML
    private void accionEditarElemento() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/view/EditarElemento.fxml"));
            Parent root = loader.load();

            EditarElementoController controller = loader.getController();
            controller.cargarDatos(elementoAsignado, detalleController);

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Editar Elemento");
            dialogStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            dialogStage.setScene(new Scene(root));
            dialogStage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}