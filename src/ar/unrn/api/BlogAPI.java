package ar.unrn.api;

import static spark.Spark.get;

import org.lightcouch.CouchDbClient;
import org.lightcouch.NoDocumentException;

import com.google.gson.Gson;

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
		        return "[{\"error\":\"Error al acceder a la base de datos\"}]";
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
		get("/byautor", (req, res) ->
			{
				res.header("Access-Control-Allow-Origin", "*");

    //implementar aca ...
    return null;
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
		get("/ultimos4posts", (req, res) ->
			{
				res.header("Access-Control-Allow-Origin", "*");

    //implementar aca ...
    return null;
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
		get("/posts-autor/:nombreautor", (req, res) ->
			{
				res.header("Access-Control-Allow-Origin", "*");
				
				//Recupero el nombre del autor que viene como parámetro
				String nombreAutor = req.params("nombreautor");

    //implementar aca ...
    return null;
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
		get("/post-id/:id", (req, res) ->
			{
				res.header("Access-Control-Allow-Origin", "*");
				
				//Recupero el id del post que viene por parámetro
				String postId = req.params("id");

    //implementar aca ...
    return null;
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
		get("/search/:text", (req, res) ->
			{
				res.header("Access-Control-Allow-Origin", "*");
				
				//Recupero la palabra/frase ingresada por el usuario
				String text = req.params("text");
				
    //implementar aca ...
    return null;
			});
		
		Spark.exception(Exception.class, (exception, request, response) ->
		{
				exception.printStackTrace();
		});

	}
}