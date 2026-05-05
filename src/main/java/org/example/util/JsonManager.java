package org.example.util;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import javafx.beans.property.*;
import javafx.collections.ObservableList;
import org.example.model.Lista;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

//Usamos el JsonManager para traducir los SimpleProperty de las clases a Strings, int y booleans que podamos guardar en
//el archivo json.
public class JsonManager {
    private static final String CARPETA_DATA = "data";
    private static final String NOMBRE_ARCHIVO = "listas.json";
    private static final Path RUTA_FINAL = Paths.get(System.getProperty("user.dir"), CARPETA_DATA, NOMBRE_ARCHIVO);
    private static final String RUTA_ARCHIVO = RUTA_FINAL.toString();

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(SimpleStringProperty.class, new PropertyAdapter())
            .registerTypeAdapter(SimpleIntegerProperty.class, new PropertyAdapter())
            .registerTypeAdapter(SimpleBooleanProperty.class, new PropertyAdapter())
            .registerTypeAdapter(ObservableList.class, new ObservableListAdapter())
            .setPrettyPrinting()
            .create();

    //Verifica que la carpeta de guardado exista (creándola si hace falta) y sobrescribe el archivo JSON traduciendo
    //toda la lista de datos de la aplicación a texto.
    public static void guardarDatos(List<Lista> listas) {
        try {
            Files.createDirectories(RUTA_FINAL.getParent());
            try (Writer writer = new FileWriter(RUTA_ARCHIVO)) {
                gson.toJson(listas, writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Busca el archivo JSON en el disco; si no existe, devuelve una lista vacía para no dar errores, y si existe, lee
    //el texto y reconstruye toda la jerarquía de listas y elementos en objetos Java.
    public static List<Lista> cargarDatos() {
        File archivo = new File(RUTA_ARCHIVO);
        if (!archivo.exists()) return new ArrayList<>();

        try (Reader reader = new FileReader(RUTA_ARCHIVO)) {
            return gson.fromJson(reader, new TypeToken<List<Lista>>(){}.getType());
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}