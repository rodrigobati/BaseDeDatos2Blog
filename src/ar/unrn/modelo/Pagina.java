package ar.unrn.modelo;

import java.time.LocalDate;

public class Pagina {
	private Long id;
	private String titulo;
	private String texto;
	private String autor;
	private LocalDate fecha;  
	
	public Pagina(String ti, String te, String a, LocalDate f){
		titulo = ti;
		texto = te;
		autor = a;
		fecha = f;
	} 
	
	public String titulo(){
		return titulo;
	}    
	
	public String texto(){
		return texto;
	}
	
	public String autor(){
		return autor;
	}
	
	public LocalDate fecha(){
		return fecha;
	}

	public Long id() {
		return id;
	}
}
