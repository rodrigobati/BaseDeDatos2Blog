package ar.unrn.api;

import static spark.Spark.get;

import java.util.List;

import org.lightcouch.CouchDbClient;
import org.lightcouch.NoDocumentException;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import ar.unrn.factory.GsonFactory;
import ar.unrn.modelo.Pagina;
import spark.Spark;


public class BlogAPI {
	
	public static void main(String[] args) {
		
		CouchDbClient dbClient = new CouchDbClient();
		
		Gson gson = GsonFactory.build();

		/**
		 * Recupera una página por su id.
		 * Debe retornar un json con la siguiente estructura:
		 * 
		 * [
			   {
			      "_id":{
			         "$oid":"59ea5578fad4770f3bb0df1c"
			      },
			      "titulo":"Sobre las Infusiones, legales... ;)",
			      "texto":"Una infusión es una bebida...",
			      "autor":"Yo Mismo",
			      "fecha":{
			         "$date":"2017-10-20T19:58:48.408Z"
			      }
			   }
			]
		 * */
		get("/pagina-id/:id", (req, res) -> {
		    res.header("Access-Control-Allow-Origin", "*");
		    res.type("application/json");

		    String paginaId = req.params("id");

		    try {
		        Pagina pagina = dbClient.find(Pagina.class, paginaId);
		        return gson.toJson(pagina);
		    } catch (NoDocumentException e) {
		        res.status(404);
		        return "[{\"error\":\"Página no encontrada\"}]";
		    } catch (Exception e) {
		        res.status(500);
		        return e.getMessage();
		        //return "[{\"error\":\"Error al acceder a la base de datos\"}]";
		    }
		});

		/**
		 * Devuelve un array de objetos id,count. Donde id es el nombre del autor y count la cantidad de post
		 * que realizó.
		 *  
		 * Debe retornar un json con la siguiente estructura:
		 *  
		 * [
			   {
			      "_id":"Jorge Boles",
			      "count":2
			   }
			   ...	
			]
		 * */
		get("/byautor", (req, res) -> {
		    res.header("Access-Control-Allow-Origin", "*");
		    //CouchDbClient dbClient = new CouchDbClient();

		    JsonArray resultado = new JsonArray();

		    List<JsonObject> rows = dbClient.view("autor/countPorAutor")
		        .group(true)
		        .query(JsonObject.class);

		    for (JsonObject row : rows) {
		        JsonObject obj = new JsonObject();
		        obj.addProperty("_id", row.get("key").getAsString());
		        obj.addProperty("count", row.get("value").getAsInt());
		        resultado.add(obj);
		    }

		    return resultado.toString();
		});



		/**
		 * Retorna los ultimos 4 post ordenados por fecha.
		 * 
		 * Debe retornar un json con la siguiente estructura:
		 * 4 documentos/post y solo 4 debe retornar.
		 * 
		 * [
			   {
			      "_id":{
			         "$oid":"59e7e0b7fad4775bfde9623e"
			      },
			      "titulo":"Café",
			      "resumen":"Sobre el Café solo..."
			   },
			   {
			      "_id":{
			         "$oid":"59e7df6efad4775bb2e9093c"
			      },
			      "titulo":"Té",
			      "resumen":"Sobre el Té solo..."
			   },
			   {
			      "_id":{
			         "$oid":"59e7df6cfaw4775bb2e9093c"
			      },
			      "titulo":"Mate",
			      "resumen":"Sobre el Mate..."
			   },
			   {
			      "_id":{
			         "$oid":"59e7df6cfad4175bb2e9093c"
			      },
			      "titulo":"Mate Cocido",
			      "resumen":"Sobre el Mate Cocido..."
			   }
			]
		 * 
		 * */
		get("/ultimos4posts", (req, res) -> {
		    res.header("Access-Control-Allow-Origin", "*");

		    //CouchDbClient dbClient = new CouchDbClient();

		    List<JsonObject> posts = dbClient.view("ultimos/porFecha")
		        .descending(true)
		        .limit(4)
		        .includeDocs(true) // si querés traer todos los campos
		        .query(JsonObject.class);

		    // Mapear cada post para devolver solo _id, titulo y resumen
		    JsonArray resultArray = new JsonArray();
		    for (JsonObject post : posts) {
		        JsonObject simplified = new JsonObject();

		        // Formato del _id como {"$oid": "xxx"}
		        JsonObject oidWrapper = new JsonObject();
		        oidWrapper.addProperty("$oid", post.get("_id").getAsString());
		        simplified.add("_id", oidWrapper);

		        simplified.addProperty("titulo", post.get("titulo").getAsString());

		        // Usar campo resumen si existe, o generar uno
		        String texto = post.has("resumen") 
		            ? post.get("resumen").getAsString() 
		            : post.get("texto").getAsString();

		        // Podés truncar el texto si querés
		        simplified.addProperty("resumen", texto.length() > 50 ? texto.substring(0, 50) + "..." : texto);

		        resultArray.add(simplified);
		    }

		    return resultArray.toString();
		});


		/**
		 * Retorna todos los Post para un autor, dado su nombre
		 * Debe retornar un json con la siguiente estructura:
		 * 
		 * [
			   {
			      "_id":{
			         "$oid":"59e7df6cfad4775bb2e9093c"
			      },
			      "titulo":"Café",
			      "resumen":"Sobre el Café solo...",
			      "texto":"El texto completo del post...",
			      "tags":[
			         "café",
			         "infusión"
			      ],
			      "links-relacionados":[
			         "http://cafenegro.com",
			         "http://cafecito.com"
			      ],
			      "autor":"Jorge Boles",
			      "fecha":{
			         "$date":"2017-10-18T23:10:36.305Z"
			      }
			   },
			   {
			      "_id":{
			         "$oid":"52r7e0b7fad4775bfde9625e"
			      },
			      "titulo":"Té",
			      "resumen":"Sobre el Té solo...",
			      "texto":"El texto completo del posts...",
			      "tags":[
			         "té",
			         "infusión"
			      ],
			      "links-relacionados":[
			         "http://te.com",
			         "http://teconleche.com"
			      ],
			      "autor":"Julio Mark",
			      "fecha":{
			         "$date":"2017-03-10T12:16:05.755Z"
			      }
			   },
			   ...
			]
		 * */
		get("/posts-autor/:nombreautor", (req, res) -> {
		    res.header("Access-Control-Allow-Origin", "*");

		    String nombreAutor = req.params("nombreautor");

		    //CouchDbClient dbClient = new CouchDbClient();

		    List<JsonObject> posts = dbClient.view("porautor/porNombre")
		        .key(nombreAutor)
		        .includeDocs(true)
		        .query(JsonObject.class);

		    JsonArray resultado = new JsonArray();

		    for (JsonObject post : posts) {
		        JsonObject jsonPost = new JsonObject();

		        // ID como { "$oid": "..." }
		        JsonObject oid = new JsonObject();
		        oid.addProperty("$oid", post.get("_id").getAsString());
		        jsonPost.add("_id", oid);

		        jsonPost.addProperty("titulo", post.get("titulo").getAsString());
		        jsonPost.addProperty("resumen", post.get("resumen").getAsString());
		        jsonPost.addProperty("texto", post.get("texto").getAsString());

		        // Array de tags
		        JsonArray tags = post.getAsJsonArray("tags");
		        jsonPost.add("tags", tags != null ? tags : new JsonArray());

		        // Array de links-relacionados
		        JsonArray links = post.getAsJsonArray("links-relacionados");
		        jsonPost.add("links-relacionados", links != null ? links : new JsonArray());

		        jsonPost.addProperty("autor", post.get("autor").getAsString());

		        // Fecha como { "$date": "..." }
		        if (post.has("fecha")) {
		            JsonObject fecha = new JsonObject();
		            fecha.addProperty("$date", post.get("fecha").getAsString());
		            jsonPost.add("fecha", fecha);
		        }

		        resultado.add(jsonPost);
		    }

		    return resultado.toString();
		});


		/**
		 * Retorna un post dado un id.
		 * 
		 * Debe retornar un json con la siguiente estructura:
		 * 
		 * [
			   {
			      "_id":{
			         "$oid":"59e7e0b7fad4775bfde9625e"
			      },
			      "titulo":"Café",
			      "resumen":"Sobre el Café solo...",
			      "texto":"El texto completo del posts...",
			      "tags":[
			         "té",
			         "infusión"
			      ],
			      "links-relacionados":[
			         "http://cafenegro.com",
			         "http://cafecito.com"
			      ],
			      "autor":"Jorge Boles",
			      "fecha":{
			         "$date":"2017-10-18T23:16:05.755Z"
			      }
			   }
			]
		 * */
		get("/post-id/:id", (req, res) -> {
		    res.header("Access-Control-Allow-Origin", "*");

		    String postId = req.params("id");

		    //CouchDbClient dbClient = new CouchDbClient();

		    JsonObject post;
		    try {
		        post = dbClient.find(JsonObject.class, postId);
		    } catch (NoDocumentException e) {
		        res.status(404);
		        return "[{\"error\": \"Post no encontrado\"}]";
		    }

		    JsonArray resultado = new JsonArray();
		    JsonObject jsonPost = new JsonObject();

		    JsonObject oid = new JsonObject();
		    oid.addProperty("$oid", post.get("_id").getAsString());
		    jsonPost.add("_id", oid);

		    jsonPost.addProperty("titulo", post.get("titulo").getAsString());
		    jsonPost.addProperty("resumen", post.get("resumen").getAsString());
		    jsonPost.addProperty("texto", post.get("texto").getAsString());

		    JsonArray tags = post.getAsJsonArray("tags");
		    jsonPost.add("tags", tags != null ? tags : new JsonArray());

		    JsonArray links = post.getAsJsonArray("links-relacionados");
		    jsonPost.add("links-relacionados", links != null ? links : new JsonArray());

		    jsonPost.addProperty("autor", post.get("autor").getAsString());

		    if (post.has("fecha")) {
		        JsonObject fecha = new JsonObject();
		        fecha.addProperty("$date", post.get("fecha").getAsString());
		        jsonPost.add("fecha", fecha);
		    }

		    resultado.add(jsonPost);

		    return resultado.toString();
		});


		/**
		 * Búsqueda libre dentro del texto del documento.
		 * Debe retornar un json con la siguiente estructura:
		 * 
		 * [
			   //cada objeto json dentro del array es un resultado de la busqueda	
			   {
			      "_id":{
			         "$oid":"59e7df6cfad4775bb2e9093c"
			      },
			      "titulo":"Café",
			      "resumen":"Sobre el Café solo...",
			      "autor":"Jorge Boles",
			      "fecha":{
			         "$date":"2017-10-18T23:10:36.305Z"
			      }
			   },
			   {
			      "_id":{
			         "$oid":"59e7e0b7fad4775bfde9625e"
			      },
			      "titulo":"Te con Leche",
			      "resumen":"Sobre el Te con Leche...",
			      "autor":"Javier Garcia",
			      "fecha":{
			         "$date":"2017-10-18T23:16:05.755Z"
			      }
			   }
			   ...
			]
		 * 
		 * */
		get("/search/:text", (req, res) -> {
		    res.header("Access-Control-Allow-Origin", "*");

		    String text = req.params("text");

		    // Construcción de consulta Mango como JSON
		    JsonObject regex = new JsonObject();
		    regex.addProperty("$regex", "(?i).*" + text + ".*"); // Búsqueda insensible a mayúsculas

		    JsonObject campo = new JsonObject();
		    campo.add("texto", regex);

		    JsonObject selector = new JsonObject();
		    selector.add("selector", campo);
		    //selector.addProperty("use_index", "texto-index");

		    // Convertimos el JsonObject a String y ejecutamos la consulta
		    List<JsonObject> resultados = dbClient.findDocs(selector.toString(), JsonObject.class);

		    // Armamos la respuesta con solo los campos necesarios
		    JsonArray respuesta = new JsonArray();
		    for (JsonObject doc : resultados) {
		        JsonObject jsonPost = new JsonObject();

		        JsonObject oid = new JsonObject();
		        oid.addProperty("$oid", doc.get("_id").getAsString());
		        jsonPost.add("_id", oid);

		        jsonPost.addProperty("titulo", doc.get("titulo").getAsString());
		        jsonPost.addProperty("resumen", doc.get("resumen").getAsString());
		        jsonPost.addProperty("autor", doc.get("autor").getAsString());

		        if (doc.has("fecha")) {
		            JsonObject fecha = new JsonObject();
		            fecha.addProperty("$date", doc.get("fecha").getAsString());
		            jsonPost.add("fecha", fecha);
		        }

		        respuesta.add(jsonPost);
		    }

		    return respuesta.toString();
		});






		
		Spark.exception(Exception.class, (exception, request, response) ->
		{
				exception.printStackTrace();
		});

	}
}