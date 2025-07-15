package ar.unrn.modelo;

import java.time.LocalDate;
import java.util.List;

public class Post {

	private String titulo;
	private List<String> tags;
	private List<String> urls;
	private String autor;
	private LocalDate fecha;
	
	public Post(String titulo, List<String> tags, List<String> urls, String autor, LocalDate fecha) {

		this.titulo = titulo;
		this.tags = tags;
		this.urls = urls;
		this.autor = autor;
		this.fecha = fecha;
	}

	public void agregarUrl(String url){
		if((!url.startsWith("http://")) || (!url.startsWith("www.")))
				throw new RuntimeException("La url no posee el formato correcto");
	}
	
	public String titulo() {
		return titulo;
	}

	public List<String> tags() {
		return tags;
	}

	public List<String> urls() {
		return urls;
	}

	public String autor() {
		return autor;
	}

	public LocalDate fecha() {
		return fecha;
	}
	
}
