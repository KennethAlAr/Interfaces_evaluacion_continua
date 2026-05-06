package org.example.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.example.model.Lista;
import org.example.util.JsonManager;

import java.util.ArrayList;
import java.util.List;

public class InicioController {

    private ObservableList<Object> listasElementos = FXCollections.observableArrayList();
    @FXML private ListView<Object> listViewListas;
    @FXML private VBox vboxPrincipal;
    private static final Object NUEVO_CREAR_LISTA = new Object();

    //Se ejecuta automáticamente al abrir la aplicación. Carga los datos del archivo json y dibuja las listas iniciales.
    @FXML
    public void initialize() {
        List<Lista> cargadas = JsonManager.cargarDatos();
        listasElementos.addAll(cargadas);
        listasElementos.add(NUEVO_CREAR_LISTA);
        listViewListas.setItems(listasElementos);
        listViewListas.setCellFactory(param -> new ListCell<Object>() {
            private HBox filaListaHBox;
            private FilaListaController filaListaController;
            private HBox filaNuevaListaHBox;
            private FilaNuevaListaController filaNuevaListaController;

            @Override
            protected void updateItem(Object item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                }
                else if (item == NUEVO_CREAR_LISTA) {
                    if (filaNuevaListaHBox == null) {
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/view/FilaNuevaLista.fxml"));
                            filaNuevaListaHBox = loader.load();
                            filaNuevaListaController = loader.getController();
                            filaNuevaListaController.setJefe(InicioController.this);
                            filaNuevaListaHBox.prefWidthProperty().bind(listViewListas.widthProperty().subtract(30));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    setGraphic(filaNuevaListaHBox);
                    setText(null);
                }
                else if (item instanceof Lista) {
                    Lista listaReal = (Lista) item;

                    if (filaListaHBox == null) {
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/view/FilaLista.fxml"));
                            filaListaHBox = loader.load();
                            filaListaController = loader.getController();
                            filaListaHBox.prefWidthProperty().bind(listViewListas.widthProperty().subtract(30));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (filaListaController != null) {
                        filaListaController.cargarDatos(listaReal, InicioController.this);
                    }
                    setGraphic(filaListaHBox);
                    setText(null);
                }
            }
        });
    }

    //Filtra la lista de la interfaz para extraer exclusivamente los objetos de tipo Lista.
    private List<Lista> obtenerListasReales() {
        List<Lista> reales = new ArrayList<>();
        for (Object obj : listasElementos) {
            if (obj instanceof Lista) {
                reales.add((Lista) obj);
            }
        }
        return reales;
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
            dialogStage.getIcons().add(new Image(getClass().getResourceAsStream("/org/example/assets/icono_appuntalo.png")));
            dialogStage.setScene(new Scene(root));
            dialogStage.showAndWait();

        } catch (Exception e) {
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

    //Borra la lista seleccionada, actualiza el tablero y guarda los datos en el json.
    public void eliminarLista(Lista lista) {
        listasElementos.remove(lista);
        JsonManager.guardarDatos(obtenerListasReales());
    }

    //Valida el nombre de la lista, actualiza los datos y los guarda en el json.
    public boolean anadirNuevaLista(String nombre, String desc) {
        String nombreLimpio = nombre.trim();

        if (nombreLimpio.isEmpty()) {
            mensajeAlerta("Aviso", "El nombre de la lista no puede estar vacío.");
            return false;
        }

        for (Lista l : obtenerListasReales()) {
            if (l.getNombre().equalsIgnoreCase(nombreLimpio)) {
                mensajeAlerta("Aviso", "Ya existe una lista con este nombre.");
                return false;
            }
        }

        int posicionMarcador = listasElementos.size() - 1;
        listasElementos.add(posicionMarcador, new Lista(nombreLimpio, desc.trim()));

        JsonManager.guardarDatos(obtenerListasReales());

        return true;
    }

    //Lanza una alerta de confirmación, si el usuario acepta, borra todas las listas y guarda los cambios en el json.
    @FXML
    public void btnBorrarTodo() {
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar borrado masivo");
        confirmacion.setHeaderText("¿Estás seguro de que quieres borrar TODAS las listas?");
        confirmacion.setContentText("Esta acción no se puede deshacer y perderás todos los datos.");
        Stage stage = (Stage) confirmacion.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/org/example/assets/icono_appuntalo.png")));
        confirmacion.getDialogPane().getStylesheets().add(getClass().getResource("/org/example/style/estilos.css").toExternalForm());
        ImageView icono = new ImageView(new Image(getClass().getResourceAsStream("/org/example/assets/icono_warning.png")));
        icono.setFitWidth(48);
        icono.setFitHeight(48);
        confirmacion.setGraphic(icono);
        Button botonAceptar = (Button) confirmacion.getDialogPane().lookupButton(ButtonType.OK);
        if (botonAceptar != null) {
            botonAceptar.getStyleClass().add("boton-peligro");
        }
        confirmacion.showAndWait().ifPresent(respuesta -> {
            if (respuesta == ButtonType.OK) {
                listasElementos.removeIf(item -> item instanceof Lista);
                JsonManager.guardarDatos(obtenerListasReales());
            }
        });
    }

    //Método auxiliar que le dice a JsonManager que sobreescriba el archivo json con el estado actual de las listas.
    public void guardarCambiosJSON() {
        JsonManager.guardarDatos(obtenerListasReales());
    }

    //Comprueba que el nuevo título no esté siendo usado por otra lista, aplica los cambios, refresca el tablero y guarda.
    public boolean editarLista(Lista listaAEditar, String nuevoNombre, String nuevaDesc) {
        String nombreLimpio = nuevoNombre.trim();

        if (nombreLimpio.isEmpty()) {
            mensajeAlerta("Aviso", "El nombre de la lista no puede estar vacío.");
            return false;
        }

        for (Lista l : obtenerListasReales()) {
            if (l != listaAEditar && l.getNombre().equalsIgnoreCase(nombreLimpio)) {
                mensajeAlerta("Aviso", "Ya existe otra lista con este nombre.");
                return false;
            }
        }

        listaAEditar.setNombre(nombreLimpio);
        listaAEditar.setDescripcion(nuevaDesc.trim());

        JsonManager.guardarDatos(obtenerListasReales());

        return true;
    }
}
