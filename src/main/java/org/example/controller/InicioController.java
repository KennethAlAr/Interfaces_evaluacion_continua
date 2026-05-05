package org.example.controller;

import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.model.Lista;
import org.example.util.JsonManager;

import java.util.List;

public class InicioController {

    @FXML private VBox vboxListas;
    private ObservableList<Lista> listasElementos = FXCollections.observableArrayList();
    @FXML private ScrollPane scrollTableroListas;

    //Se ejecuta automáticamente al abrir la aplicación. Carga los datos del archivo json y dibuja las listas iniciales.
    @FXML
    public void initialize() {
        List<Lista> cargadas = JsonManager.cargarDatos();
        listasElementos.addAll(cargadas);
        actualizarTablero();
    }

    //Limpia el contenedor central y vuelve a dibjuar todas las filas de las listas una a una, añadiendo la fila
    //interactiva de creación al final.
    private void actualizarTablero() {
        vboxListas.getChildren().clear();

        for (Lista lista : listasElementos) {
            HBox fila = crearFilaLista(lista);
            vboxListas.getChildren().add(fila);
        }

        HBox filaNuevaLista = crearFilaNuevaLista();
        vboxListas.getChildren().add(filaNuevaLista);
    }

    //Carga el diseño del FXML de una fila y le añade los datos de su lista correspondiente y la referencia a InicioController.
    private HBox crearFilaLista(Lista lista) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/view/FilaLista.fxml"));
            HBox hbox = loader.load();

            FilaListaController controladorFila = loader.getController();

            controladorFila.cargarDatos(lista, this);

            return hbox;
        } catch (Exception e) {
            System.out.println("Error al cargar la fila FXML: " + e.getMessage());
            return new HBox();
        }
    }

    //Carga el FXML de la fila vacía para crear una nueva lista y asigna el controlador "jefe" (InicioCOntroller)
    private HBox crearFilaNuevaLista() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/view/FilaNuevaLista.fxml"));
            HBox hbox = loader.load();

            FilaNuevaListaController controller = loader.getController();
            controller.setJefe(this);

            return hbox;
        } catch (Exception e) {
            e.printStackTrace();
            return new HBox();
        }
    }

    //Abre una ventana emergente con el formulario para crear una lista nueva.
    @FXML
    public void btnNuevaLista() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/view/NuevaLista.fxml"));
            Parent root = loader.load();

            NuevaListaController controller = loader.getController();
            controller.setJefe(this);

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Añadir Lista");
            dialogStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            dialogStage.setScene(new Scene(root));
            dialogStage.showAndWait();

        } catch (Exception e) {
            System.out.println("Error al abrir la ventana de nueva lista:");
            e.printStackTrace();
        }
    }

    //Finaliza la ejecución del programa y cierra la aplicación.
    @FXML
    public void btnSalir() {
        System.exit(0);
    }

    //Genera y muestra un aviso al usuario.
    private void mensajeAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    //Borra la lista seleccionada, aztualiza el tablero y guarda los datos en el json.
    public void eliminarLista(Lista lista) {
        listasElementos.remove(lista);
        actualizarTablero();
        JsonManager.guardarDatos(listasElementos);
    }

    //Valida el nombre de la lista, actualiza los datos y los guarda en el json.
    public boolean anadirNuevaLista(String nombre, String desc) {
        String nombreLimpio = nombre.trim();

        if (nombreLimpio.isEmpty()) {
            mensajeAlerta("Aviso", "El nombre de la lista no puede estar vacío.");
            return false;
        }

        for (Lista l : listasElementos) {
            if (l.getNombre().equalsIgnoreCase(nombreLimpio)) {
                mensajeAlerta("Aviso", "Ya existe una lista con este nombre.");
                return false;
            }
        }

        listasElementos.add(new Lista(nombreLimpio, desc.trim()));
        actualizarTablero();
        JsonManager.guardarDatos(listasElementos);

        PauseTransition pause = new PauseTransition(Duration.millis(50));
        pause.setOnFinished(e -> scrollTableroListas.setVvalue(1.0));
        pause.play();

        return true;
    }

    //Lanza una alerta de confirmación, si el usuario acepta, borra todas las listas y guarda los cambios en el json.
    @FXML
    public void btnBorrarTodo() {
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar borrado masivo");
        confirmacion.setHeaderText("¿Estás seguro de que quieres borrar TODAS las listas?");
        confirmacion.setContentText("Esta acción no se puede deshacer y perderás todos los datos.");
        confirmacion.showAndWait().ifPresent(respuesta -> {
            if (respuesta == ButtonType.OK) {
                listasElementos.clear();
                actualizarTablero();
                JsonManager.guardarDatos(listasElementos);
            }
        });
    }

    //Método auxiliar que le dice a JsonManager que sobreescriba el archivo json con el estado actual de las listas.
    public void guardarCambiosJSON() {
        JsonManager.guardarDatos(listasElementos);
    }

    //Comprueba que el nuevo título no esté siendo usado por otra lista, aplica los cambios, refresca el tablero y guarda.
    public boolean editarLista(Lista listaAEditar, String nuevoNombre, String nuevaDesc) {
        String nombreLimpio = nuevoNombre.trim();

        if (nombreLimpio.isEmpty()) {
            mensajeAlerta("Aviso", "El nombre de la lista no puede estar vacío.");
            return false;
        }

        for (Lista l : listasElementos) {
            if (l != listaAEditar && l.getNombre().equalsIgnoreCase(nombreLimpio)) {
                mensajeAlerta("Aviso", "Ya existe otra lista con este nombre.");
                return false;
            }
        }

        listaAEditar.setNombre(nombreLimpio);
        listaAEditar.setDescripcion(nuevaDesc.trim());

        actualizarTablero();
        JsonManager.guardarDatos(listasElementos);

        return true;
    }
}
