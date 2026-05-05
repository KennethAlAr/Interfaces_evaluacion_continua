package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.example.model.Lista;

public class FilaListaController {

    @FXML private Label lblNombre;
    @FXML private Label lblDescripcion;
    @FXML private Label lblCantidad;
    @FXML private Button btnBorrar;

    private Lista listaAsignada;
    private InicioController inicioController;

    //Recibe la lista de la fila y el controlador "jefe" rellenando las etiquetas de texto.
    public void cargarDatos(Lista lista, InicioController jefe) {
        this.listaAsignada = lista;
        this.inicioController = jefe;

        lblNombre.setText(lista.getNombre());
        lblDescripcion.setText(lista.getDescripcion());

        int cantidad = 0;
        if (lista.getElementos() != null) {
            cantidad = lista.getElementos().size();
        }
        lblCantidad.setText("(" + cantidad + " elementos)");
    }

    //Le dice a InicioController que elimine la lista y actualice el json.
    @FXML
    private void accionBorrar() {
        inicioController.eliminarLista(listaAsignada);
    }

    //Detecta el clic del ratón sobre la lista y cambia la escena para mostrar el detalle de la lista.
    @FXML
    public void btnElegirLista(MouseEvent event) {
        Stage stageActual = (Stage) ((Node) event.getSource()).getScene().getWindow();
        org.example.view.DetalleView.mostrar(stageActual, this.listaAsignada, this.inicioController);
    }
}