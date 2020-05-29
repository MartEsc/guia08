package frsf.isi.died.guia08.problema01.modelo;

public class FinalizarTareaException extends Exception {
	public FinalizarTareaException() {
		super("Esta tarea no corresponde al empleado");
	}
}
