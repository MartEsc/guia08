package frsf.isi.died.guia08.problema01.modelo;

public class ExcepcionAsignarEmpleado extends Exception {
	public ExcepcionAsignarEmpleado(String mensaje) {
		super("No se pudo asignar al empleado, ha ocurrido el siguiente error: "+mensaje);
	}
}
