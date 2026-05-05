package org.example.controller;

import javafx.animation.PauseTransition;
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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.util.Duration;
import org.example.model.Elemento;
import org.example.model.Lista;

public class DetalleController {

    @FXML private Label txtNombreLista;
    @FXML private Label txtDescripcionLista;
    //Para tener la lista de Elementos he creado una Vbox para insertar el ScrollPane donde van las HBox de cada fila
    //y debajo la fila para crear un nuevo elemento.
    @FXML private VBox vboxElementos;
    @FXML private ScrollPane scrollTableroElementos;

    private Lista listaActual;
    //Guardamos el InicioController porque es quien hace el control de las listas y la permanencia de datos de estas,
    //ya que DetalleController solo tiene acceso a la información de la lista que está enseñando.
    private InicioController inicioController;

    //setLista recibe la lista que vamos a mostrar y de dónde viene.
    public void setLista(Lista lista, InicioController jefe) {
        this.listaActual = lista;
        this.inicioController = jefe;
        this.txtNombreLista.setText(lista.getNombre());
        this.txtDescripcionLista.setText(lista.getDescripcion());
        //Cuando tenemos toda la información guardada usamos el método actualizarTableroDetalle para pintarlo en el ScrollPane
        actualizarTableroDetalle();
    }

    //actualizarTableroDetalle borra todo el contenido del VBox y vuelve a dibujar los elementos uno a uno, añadiendo
    //la fila de creación al final.
    private void actualizarTableroDetalle() {
        vboxElementos.getChildren().clear();

        if (listaActual.getElementos() != null) {
            for (Elemento elemento : listaActual.getElementos()) {
                HBox fila = crearFilaElemento(elemento);
                vboxElementos.getChildren().add(fila);
            }
        }
        HBox filaNuevoElemento = crearFilaNuevoElemento();
        vboxElementos.getChildren().add(filaNuevoElemento);
    }

    //crearFilaElemento carga el diseño del FXML de una fila ya existente e introduce sus datos correspondientes.
    private HBox crearFilaElemento(Elemento el) {
        //El uso de try/catch es obligatorio cuando usamos el método load().
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/view/FilaElemento.fxml"));
            HBox hbox = loader.load();

            FilaElementoController controladorFila = loader.getController();
            controladorFila.cargarDatos(el, this);

            return hbox;
        } catch (Exception e) {
            System.out.println("Error al cargar FilaElemento: " + e.getMessage());
            //En caso de que haya un error devolvemos un HBox vacío.
            return new HBox();
        }
    }

    //crearFilaNuevoElemento carga el diseño del FXML de la fila interactiva vacía que se coloca al final de la
    //lista de elementos.
    private HBox crearFilaNuevoElemento() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/view/FilaNuevoElemento.fxml"));
            HBox hbox = loader.load();

            FilaNuevoElementoController controller = loader.getController();
            controller.setJefe(this);

            return hbox;
        } catch (Exception e) {
            System.out.println("Error al cargar FilaNuevoElemento: " + e.getMessage());
            return new HBox();
        }
    }

    //anadirNuevoElemento válida el nombre (no está vacío ni repetido), lo añade a la lista y vuelve a cargar la información.
    //Devuelve un boolean como respuesta para saber si el elemento cumple las condiciones necesarias.
    public boolean anadirNuevoElemento(Elemento nuevoElemento) {
        String nombreLimpio = nuevoElemento.getNombre().trim();

        if (nombreLimpio.isEmpty()) {
            mensajeAlerta("Aviso", "El nombre del elemento no puede estar vacío.");
            return false;
        }

        if (listaActual.getElementos() != null) {
            for (Elemento el : listaActual.getElementos()) {
                if (el.getNombre().equalsIgnoreCase(nombreLimpio)) {
                    mensajeAlerta("Aviso", "Ya existe un elemento con este nombre en la lista actual.");
                    return false;
                }
            }
        }

        listaActual.getElementos().add(nuevoElemento);
        actualizarTableroDetalle();
        //guardarCambios guarda la información en el archivo json del proyecto.
        guardarCambios();

        //He añadido una pausa después de actualizar el tablero para ir al nuevo elemento creado. Esta pausa es necesaria
        //porque si no lo hacía así el programa se movía al final y a veces actualizaba el tablero después.
        PauseTransition pause = new PauseTransition(Duration.millis(50));
        pause.setOnFinished(e -> scrollTableroElementos.setVvalue(1.0));
        pause.play();

        return true;
    }

    //Borra el elemento de la lista, actualiza y guarda los datos.
    public void eliminarElemento(Elemento elementoAEliminar) {
        if (listaActual.getElementos() != null) {
            listaActual.getElementos().remove(elementoAEliminar);
        }
        actualizarTableroDetalle();
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
            System.out.println("Error al abrir la ventana de nuevo elemento:");
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

        confirmacion.showAndWait().ifPresent(respuesta -> {
            if (respuesta == ButtonType.OK) {
                if (listaActual.getElementos() != null) {
                    listaActual.getElementos().clear();
                }

                actualizarTableroDetalle();
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

        if (listaActual.getElementos() != null) {
            for (Elemento el : listaActual.getElementos()) {
                if (el != elementoOriginal && el.getNombre().equalsIgnoreCase(nombreLimpio)) {
                    mensajeAlerta("Aviso", "Ya existe otro elemento con este nombre en la lista.");
                    return false;
                }
            }
        }

        elementoOriginal.setNombre(nombreLimpio);
        elementoOriginal.setCantidad(nuevaCantidad);
        elementoOriginal.setDescripcion(nuevaDesc.trim());

        actualizarTableroDetalle();
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