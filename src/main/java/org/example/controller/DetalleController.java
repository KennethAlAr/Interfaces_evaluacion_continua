package org.example.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.Modality;
import org.example.model.Elemento;
import org.example.model.Lista;

public class DetalleController {

    @FXML private Label txtNombreLista;
    @FXML private Label txtDescripcionLista;
    @FXML private ListView<Object> listViewElementos;
    private ObservableList<Object> elementosVisibles = FXCollections.observableArrayList();
    private static final Object NUEVO_CREAR_ELEMENTO = new Object();

    private Lista listaActual;
    //Guardamos el InicioController porque es quien hace el control de las listas y la permanencia de datos de estas,
    //ya que DetalleController solo tiene acceso a la información de la lista que está enseñando.
    private InicioController inicioController;

    //setLista recibe la lista que vamos a mostrar y de dónde viene y lo prepara en la ListView.
    public void setLista(Lista lista, InicioController jefe) {
        this.listaActual = lista;
        this.inicioController = jefe;
        this.txtNombreLista.setText(lista.getNombre());
        this.txtDescripcionLista.setText(lista.getDescripcion());

        if (lista.getElementos() != null) {
            elementosVisibles.addAll(lista.getElementos());
        }
        elementosVisibles.add(NUEVO_CREAR_ELEMENTO);
        listViewElementos.setItems(elementosVisibles);
        listViewElementos.setCellFactory(param -> new ListCell<Object>() {
            private HBox filaHBox;
            private FilaElementoController filaController;
            private HBox filaNuevaHBox;
            private FilaNuevoElementoController filaNuevaController;

            @Override
            protected void updateItem(Object item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                }
                else if (item == NUEVO_CREAR_ELEMENTO) {
                    if (filaNuevaHBox == null) {
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/view/FilaNuevoElemento.fxml"));
                            filaNuevaHBox = loader.load();
                            filaNuevaController = loader.getController();
                            filaNuevaController.setJefe(DetalleController.this);
                            filaNuevaHBox.prefWidthProperty().bind(listViewElementos.widthProperty().subtract(30));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    setGraphic(filaNuevaHBox);
                    setText(null);
                }
                else if (item instanceof Elemento) {
                    Elemento el = (Elemento) item;
                    if (filaHBox == null) {
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/view/FilaElemento.fxml"));
                            filaHBox = loader.load();
                            filaController = loader.getController();
                            filaHBox.prefWidthProperty().bind(listViewElementos.widthProperty().subtract(30));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (filaController != null) {
                        filaController.cargarDatos(el, DetalleController.this);
                    }
                    setGraphic(filaHBox);
                    setText(null);
                }
            }
        });
    }

    //Filtra la lista de la interfaz para extraer exclusivamente los objetos de tipo Elemento.
    private java.util.List<Elemento> obtenerElementosReales() {
        java.util.List<Elemento> reales = new java.util.ArrayList<>();
        for (Object obj : elementosVisibles) {
            if (obj instanceof Elemento) {
                reales.add((Elemento) obj);
            }
        }
        return reales;
    }

    //anadirNuevoElemento válida el nombre (no está vacío ni repetido), lo añade a la lista y vuelve a cargar la información.
    //Devuelve un boolean como respuesta para saber si el elemento cumple las condiciones necesarias.
    public boolean anadirNuevoElemento(Elemento nuevoElemento) {
        String nombreLimpio = nuevoElemento.getNombre().trim();

        if (nombreLimpio.isEmpty()) {
            mensajeAlerta("Aviso", "El nombre del elemento no puede estar vacío.");
            return false;
        }

        for (Elemento el : obtenerElementosReales()) {
            if (el.getNombre().equalsIgnoreCase(nombreLimpio)) {
                mensajeAlerta("Aviso", "Ya existe un elemento con este nombre en la lista actual.");
                return false;
            }
        }

        int posicionMarcador = elementosVisibles.size() - 1;
        elementosVisibles.add(posicionMarcador, nuevoElemento);
        listaActual.getElementos().add(nuevoElemento);
        guardarCambios();
        listViewElementos.scrollTo(elementosVisibles.size() - 1);

        return true;
    }

    //Borra el elemento de la lista, actualiza y guarda los datos.
    public void eliminarElemento(Elemento elementoAEliminar) {
        elementosVisibles.remove(elementoAEliminar);
        if (listaActual.getElementos() != null) {
            listaActual.getElementos().remove(elementoAEliminar);
        }
        guardarCambios();
    }

    //Es el método que usamos para decirle a inicioController que guarde los datos en el json del proyecto.
    public void guardarCambios() {
        if (inicioController != null) {
            inicioController.guardarCambiosJSON();
        }
    }

    //Abre una ventana emergente con un formulario para crear un nuevo elemento.
    @FXML
    public void btnNuevoElemento() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/view/NuevoElemento.fxml"));
            Parent root = loader.load();

            NuevoElementoController controller = loader.getController();
            controller.setJefe(this);

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Añadir Elemento");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.getIcons().add(new Image(getClass().getResourceAsStream("/org/example/assets/icono_appuntalo.png")));
            dialogStage.setScene(new Scene(root));
            dialogStage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Lanza una alerta de confirmación, si el usuario acepta, vacía la lista entera y guarda los cambios en el json.
    @FXML
    public void btnBorrarTodosElementos() {
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar borrado masivo");
        confirmacion.setHeaderText("¿Estás seguro de que quieres borrar TODOS los elementos?");
        confirmacion.setContentText("Esta acción no se puede deshacer y vaciará la lista por completo.");
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
                elementosVisibles.removeIf(item -> item instanceof Elemento);
                if (listaActual.getElementos() != null) {
                    listaActual.getElementos().clear();
                }
                guardarCambios();
            }
        });
    }

    //Sale de la vista DetalleView y vuelve a la vista InicioView.
    @FXML
    public void btnVolver(ActionEvent event) {
        Stage stageActual = (Stage) ((Node) event.getSource()).getScene().getWindow();
        org.example.view.InicioView.mostrar(stageActual);
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

    //Comprueba que el nombre no esté ni repetido ni vacío y, si es correcto, actualiza y guarda los datos.
    public boolean editarElemento(Elemento elementoOriginal, String nuevoNombre, int nuevaCantidad, String nuevaDesc) {
        String nombreLimpio = nuevoNombre.trim();

        if (nombreLimpio.isEmpty()) {
            mensajeAlerta("Aviso", "El nombre del elemento no puede estar vacío.");
            return false;
        }

        for (Elemento el : obtenerElementosReales()) {
            if (el != elementoOriginal && el.getNombre().equalsIgnoreCase(nombreLimpio)) {
                mensajeAlerta("Aviso", "Ya existe otro elemento con este nombre en la lista.");
                return false;
            }
        }

        elementoOriginal.setNombre(nombreLimpio);
        elementoOriginal.setCantidad(nuevaCantidad);
        elementoOriginal.setDescripcion(nuevaDesc.trim());

        // Forzamos al ListView a refrescar los textos
        listViewElementos.refresh();
        guardarCambios();

        return true;
    }

    //Usa el botón btnEditarLista para editar los datos de la lista seleccionada a través de una nueva ventana.
    //Comprueba a través de EditarListaController que el nombre no está vacío ni repetido.
    @FXML
    public void accionEditarLista() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/view/EditarLista.fxml"));
            Parent root = loader.load();

            EditarListaController controller = loader.getController();
            controller.cargarDatos(listaActual, this);

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Editar Lista");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.getIcons().add(new Image(getClass().getResourceAsStream("/org/example/assets/icono_appuntalo.png")));
            dialogStage.setScene(new Scene(root));
            dialogStage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Recoge los datos de la edición de la lista y le dice a InicioController que lo modifique y lo actualice.
    public boolean guardarEdicionLista(String nuevoNombre, String nuevaDesc) {
        if (inicioController != null) {
            boolean exito = inicioController.editarLista(listaActual, nuevoNombre, nuevaDesc);
            if (exito) {
                txtNombreLista.setText(listaActual.getNombre());
                txtDescripcionLista.setText(listaActual.getDescripcion());
                return true;
            }
        }
        return false;
    }
}