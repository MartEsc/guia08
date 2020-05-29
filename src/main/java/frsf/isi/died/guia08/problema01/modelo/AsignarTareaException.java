package frsf.isi.died.guia08.problema01.modelo;

public class AsignarTareaException extends Exception {

	public AsignarTareaException(String mensaje) {
		super("Ha sucedido el siguiente error al asignar la tarea: "+mensaje);
	}

}
