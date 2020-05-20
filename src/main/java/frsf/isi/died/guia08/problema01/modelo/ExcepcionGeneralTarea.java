package frsf.isi.died.guia08.problema01.modelo;

public class ExcepcionGeneralTarea extends Exception {
	public ExcepcionGeneralTarea(String mensaje) {
		super("No se pudo realizar la operacion, ha ocurrido el siguiente error: "+mensaje);
	}
}
