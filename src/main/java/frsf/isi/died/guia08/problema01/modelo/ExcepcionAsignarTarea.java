package frsf.isi.died.guia08.problema01.modelo;

public class ExcepcionAsignarTarea extends Exception {
	public ExcepcionAsignarTarea(String mensaje) {
		super("No se pudo asignar la tarea, ha ocurrido el siguiente error: "+mensaje);
	}
}
