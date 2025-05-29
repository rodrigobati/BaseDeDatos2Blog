module BaseDeDatos2Blog {
	requires spark.core;
	requires lightcouch;
	requires com.google.gson;
	opens ar.unrn.modelo to com.google.gson;
}
