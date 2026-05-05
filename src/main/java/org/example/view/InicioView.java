package org.example.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class InicioView extends Application {

    //Inicia la ventana, recibiendo la Stage y cargando la escena.
    @Override
    public void start(Stage stage) {
        mostrar(stage);
    }

    //Carga el archivo FXML de la vista de inicio y le inserta los datos de las listas.
    public static void mostrar(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(InicioView.class.getResource("/org/example/view/InicioView.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 800, 800);

            stage.getIcons().add(new Image(InicioView.class.getResourceAsStream("/org/example/assets/icono_appuntalo.png")));

            stage.setTitle("Appuntalo - Mis Listas");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}