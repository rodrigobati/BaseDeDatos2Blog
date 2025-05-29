package ar.unrn.modelo;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Expose;

import java.time.LocalDate;
import java.util.Date;

public class Pagina {
    @Expose
    @SerializedName("_id")
    public String id;

    @Expose
    public String titulo;
    
    @Expose
    public String texto;
    
    @Expose
    public String autor;

    @Expose
    public Date fecha;

    public Pagina() {}

    public Pagina(String ti, String te, String a, Date f){
        titulo = ti;
        texto = te;
        autor = a;
        fecha = f;
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public String getAutor() {
		return autor;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

   
}
