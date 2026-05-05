# Appuntalo - Gestor de Listas

Aplicacion de escritorio desarrollada en JavaFX para la gestion integral de listas (tareas, compras, inventario, etc.). Permite organizar elementos en diferentes categorias, editar sus detalles en tiempo real y mantener la informacion guardada de forma permanente gracias a su sistema de persistencia en formato JSON.

## Caracteristicas Principales

*   **Gestion de Listas:** Creacion, edicion de titulo y descripcion, y eliminacion de listas.
*   **Gestion de Elementos:** Dentro de cada lista, se pueden anadir nuevos elementos especificando su nombre, cantidad y descripcion.
*   **Validacion de Datos:** Sistema de prevencion de errores que evita la creacion de listas o elementos con nombres vacios o duplicados.
*   **Control de Estado:** Funcionalidad para marcar elementos como completados o comprados.
*   **Persistencia Automatica:** Todo el progreso y los cambios realizados se guardan en tiempo real en un archivo local `listas.json`.
*   **Borrado Masivo:** Opciones de seguridad para vaciar listas enteras o borrar todas las listas del sistema mediante confirmacion previa.

## Tecnologias Utilizadas

*   **Java:** Lenguaje de programacion principal.
*   **JavaFX:** Framework utilizado para la creacion de la interfaz grafica de usuario.
*   **FXML:** Lenguaje de marcado para el diseno de las vistas, ventanas y componentes modulares.
*   **Gson:** Libreria de Google utilizada para la serializacion y deserializacion compleja de objetos a JSON.

## Arquitectura del Proyecto

El proyecto sigue una estructura basada en el patron Modelo-Vista-Controlador (MVC), asegurando un codigo modular y escalable:

*   **org.example.model:** Contiene las clases de datos (`Lista`, `Elemento`). Utiliza variables observables nativas de JavaFX (`SimpleStringProperty`, `SimpleIntegerProperty`, etc.) para estructurar la informacion.
*   **org.example.view:** Almacena las clases lanzadoras (`InicioView`, `DetalleView`) encargadas de renderizar las escenas principales.
*   **org.example.controller:** Contiene toda la logica de negocio interactiva.
    *   `InicioController`: Ejerce de controlador principal, gestionando las colecciones de listas y delegando el guardado de datos.
    *   `DetalleController`: Gestiona la visualizacion y modificacion de los elementos internos de una lista seleccionada.
    *   Controladores de modales y filas: Clases individuales para gestionar ventanas emergentes (creacion/edicion) y el comportamiento individual de cada fila de la interfaz.
*   **org.example.util:** Contiene las herramientas de sistema. Destaca `JsonManager` y los adaptadores personalizados (`PropertyAdapter`, `ObservableListAdapter`) desarrollados para permitir la compatibilidad entre Gson y las propiedades observables de JavaFX.

## Ejecucion del Proyecto

1. Clonar el repositorio en la maquina local.
2. Abrir el proyecto en un Entorno de Desarrollo Integrado (IDE) compatible.
3. Asegurar la correcta configuracion del SDK de Java y las librerias de JavaFX.
4. Cargar la dependencia de la libreria Gson (a traves de Maven, Gradle o anadiendo el .jar manualmente).
5. Ejecutar la aplicacion lanzando la clase principal ubicada en `org.example.main.Launcher`.