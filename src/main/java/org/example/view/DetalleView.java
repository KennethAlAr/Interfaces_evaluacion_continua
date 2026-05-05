package org.example.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.example.controller.DetalleController;
import org.example.controller.InicioController;
import org.example.model.Lista;

public class DetalleView {

    //Carga el archivo FXML de la vista de detalle y le inserta los datos de la lista seleccionada.
    //Después sustituye la escena para mostrar la nueva pantalla.
    public static void mostrar(Stage stage, Lista lista, InicioController jefe) {
        try {
            FXMLLoader loader = new FXMLLoader(DetalleView.class.getResource("/org/example/view/DetalleView.fxml"));
            Parent root = loader.load();

            DetalleController controlador = loader.getController();
            controlador.setLista(lista, jefe);

            stage.getIcons().add(new Image(InicioView.class.getResourceAsStream("/org/example/assets/icono_appuntalo.png")));

            stage.setScene(new Scene(root));
            stage.setTitle("Viendo: " + lista.getNombre());
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}