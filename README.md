# basededatos2

Se utilizó CouchDB como base de datos NoSQL para almacenar los documentos, y LightCouch como cliente Java para conectarse a la base y realizar consultas. Se implementó un índice para permitir búsquedas por campos de texto, en particular por el campo titulo, utilizando expresiones regulares con el operador $regex.

Al probar las mismas búsquedas desde la aplicación Java con LightCouch, algunas coincidencias no se obtenían (por ejemplo, no encontraba "clio" si no estaba precedido por ciertas palabras).

Se revisaron las consultas, el uso del índice (use_index) y se intentó también con vistas (views) para comprobar el comportamiento y los resultados de la búsqueda.


VIEWS en couchdb

Obtiene todos los documentos que tengan un campo autor, usando el valor del autor como clave. Se usa par obtener todos los documentos agrupados por autor.

{
  "_id": "_design/porautor",
  "_rev": "1-54b74de176a31b4ad350ac162e4d4362",
  "views": {
    "porNombre": {
      "map": "function(doc) { if (doc.autor) emit(doc.autor, doc); }"
    }
  }
}


Cuenta cuántos documentos hay por cada autor.

{
  "_id": "_design/autor",
  "_rev": "4-0317110b424ce9d42783eccb1fe8f436",
  "views": {
    "countPorAutor": {
      "map": "function(doc) { if (doc.autor) emit(doc.autor, 1); }",
      "reduce": "_count"
    }
  }
}


Crea un índice en el campo texto.

{
  "_id": "_design/texto-index",
  "_rev": "1-72abe512bd79042c43c6e4c8a478b8b2",
  "language": "query",
  "indexes": {
    "texto-index": {
      "index": {
        "fields": [
          "texto"
        ]
      },
      "type": "json"
    }
  }
}

Obtiene los documentos ordenados por la fecha (clave), mostrando solo el título y el texto como resumen.

{
  "_id": "_design/ultimos",
  "_rev": "1-8be097468f438d56f68862417248f313",
  "views": {
    "porFecha": {
      "map": "function(doc) { if (doc.fecha) emit(doc.fecha, {titulo: doc.titulo, resumen: doc.texto}); }"
    }
  },
  "language": "javascript"
}
