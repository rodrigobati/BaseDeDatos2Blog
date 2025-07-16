# basededatos2

Se utilizó CouchDB como base de datos NoSQL para almacenar los documentos, y LightCouch como cliente Java para conectarse a la base y realizar consultas. Se implementó un índice para permitir búsquedas por campos de texto, en particular por el campo titulo, utilizando expresiones regulares con el operador $regex.

Al probar las mismas búsquedas desde la aplicación Java con LightCouch, algunas coincidencias no se obtenían (por ejemplo, no encontraba "clio" si no estaba precedido por ciertas palabras).

Se revisaron las consultas, el uso del índice (use_index) y se intentó también con vistas (views) para comprobar el comportamiento y los resultados de la búsqueda.
