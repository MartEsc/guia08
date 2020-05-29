package frsf.isi.died.guia08.problema01.modelo;

public class AsignarEmpleadoException extends Exception {
	public AsignarEmpleadoException() {
		super("No se puede asignar un empleado a esta tarea");
	}
}
