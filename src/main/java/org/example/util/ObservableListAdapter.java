package org.example.util;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

//Usamos ObservableListAdapter para decirle a gson como guardar y cargar las listas de JavaFX
public class ObservableListAdapter implements JsonSerializer<ObservableList<?>>, JsonDeserializer<ObservableList<?>> {

    @Override
    public JsonElement serialize(ObservableList<?> src, Type typeOfSrc, JsonSerializationContext context) {
        return context.serialize(new ArrayList<>(src));
    }

    @Override
    public ObservableList<?> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Type itemType = ((ParameterizedType) typeOfT).getActualTypeArguments()[0];
        List<?> list = context.deserialize(json, TypeToken.getParameterized(ArrayList.class, itemType).getType());
        return FXCollections.observableArrayList(list);
    }
}