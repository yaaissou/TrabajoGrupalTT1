package com.tt1.trabajo.modelo;

public class Entidad {
	private int id;
	private String name;
	private String descripcion;
	public Entidad(int i, String n, String d) {
		this.id=i;
		this.name=n;
		this.descripcion=d;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
}
