package ar.unrn.factory;

import com.google.gson.*;

import ar.unrn.modelo.Pagina;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GsonFactory {
    public static Gson build() {
        return new GsonBuilder()
            .registerTypeAdapter(Pagina.class, new JsonSerializer<Pagina>() {
                @Override
                public JsonElement serialize(Pagina pagina, Type typeOfSrc, JsonSerializationContext context) {
                    JsonObject obj = new JsonObject();

                    JsonObject idObj = new JsonObject();
                    idObj.addProperty("$oid", pagina.getId());
                    obj.add("_id", idObj);

                    obj.addProperty("titulo", pagina.getTitulo());
                    obj.addProperty("texto", pagina.getTexto());
                    obj.addProperty("autor", pagina.getAutor());

                    JsonObject fechaObj = new JsonObject();
                    fechaObj.addProperty("$date", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX").format(pagina.getFecha()));
                    obj.add("fecha", fechaObj);

                    JsonArray array = new JsonArray();
                    array.add(obj);
                    return array;
                }
            })
            .create();
    }
}
