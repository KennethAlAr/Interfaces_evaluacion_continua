package org.example.util;

import com.google.gson.*;
import javafx.beans.property.*;
import java.lang.reflect.Type;

//Usamos PropertyAdapter para decirle a gson como guardar y cargar las datos de las clases Lista y Elemento.
public class PropertyAdapter implements JsonSerializer<Property<?>>, JsonDeserializer<Property<?>> {
    @Override
    public JsonElement serialize(Property<?> src, Type typeOfSrc, JsonSerializationContext context) {
        return context.serialize(src.getValue());
    }

    @Override
    public Property<?> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (typeOfT.equals(SimpleStringProperty.class)) return new SimpleStringProperty(json.getAsString());
        if (typeOfT.equals(SimpleIntegerProperty.class)) return new SimpleIntegerProperty(json.getAsInt());
        if (typeOfT.equals(SimpleBooleanProperty.class)) return new SimpleBooleanProperty(json.getAsBoolean());
        return null;
    }
}