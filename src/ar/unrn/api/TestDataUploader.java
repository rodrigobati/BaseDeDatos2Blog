package ar.unrn.api;

import org.lightcouch.CouchDbClient;
import org.lightcouch.CouchDbProperties;
import com.google.gson.JsonObject;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class TestDataUploader {

    public static void main(String[] args) {
        CouchDbClient dbClient = new CouchDbClient(); // Usa couchdb.properties

        // Crear varios documentos de prueba
        JsonObject doc1 = crearDocumento(
                "Café",
                "Sobre el café solo",
                "Este es un texto largo sobre cómo el café afecta el cuerpo...",
                new String[]{"café", "infusión"},
                new String[]{"https://ejemplo.com/cafe1", "https://ejemplo.com/cafe2"},
                "Jorge Boles"
        );

        JsonObject doc2 = crearDocumento(
                "Té verde",
                "Los beneficios del té verde",
                "El té verde tiene múltiples propiedades antioxidantes...",
                new String[]{"té", "verde", "infusión"},
                new String[]{"https://ejemplo.com/te-verde"},
                "Lucía Torres"
        );

        JsonObject doc3 = crearDocumento(
                "Mate",
                "El ritual del mate",
                "Tomar mate es una tradición en muchos países de Sudamérica...",
                new String[]{"mate", "costumbre"},
                new String[]{"https://ejemplo.com/mate-ritual"},
                "Federico Lamas"
        );

        dbClient.save(doc1);
        dbClient.save(doc2);
        dbClient.save(doc3);

        dbClient.shutdown();
        System.out.println("Documentos cargados exitosamente.");
    }

    private static JsonObject crearDocumento(String titulo, String resumen, String texto, String[] tags, String[] links, String autor) {
        JsonObject doc = new JsonObject();
        doc.addProperty("titulo", titulo);
        doc.addProperty("resumen", resumen);
        doc.addProperty("texto", texto);

        // Arreglo de tags
        var tagsArray = new com.google.gson.JsonArray();
        for (String tag : tags) {
            tagsArray.add(new com.google.gson.JsonPrimitive(tag));
        }
        doc.add("tags", tagsArray);

        // Arreglo de links
        var linksArray = new com.google.gson.JsonArray();
        for (String link : links) {
            linksArray.add(new com.google.gson.JsonPrimitive(link));
        }
        doc.add("links-relacionados", linksArray);

        doc.addProperty("autor", autor);

        // Fecha actual en formato ISO
        String fecha = ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        doc.addProperty("fecha", fecha);

        return doc;
    }


}
