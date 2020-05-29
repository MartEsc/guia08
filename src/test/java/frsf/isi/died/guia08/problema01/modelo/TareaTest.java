package frsf.isi.died.guia08.problema01.modelo;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import frsf.isi.died.guia08.problema01.modelo.Empleado.Tipo;

public class TareaTest {
	private  Empleado e1;
	private  Empleado e2;
	private  Empleado e3;
	private  Tarea t1;
	private  Tarea t2;
	private  Tarea t3;
	
	@Before
	public void init() {
		 e1 = new Empleado(10,"Franco Velazquez",Tipo.CONTRATADO,100d);
		 e2 = new Empleado(20,"Facundo Fernandez",Tipo.EFECTIVO,200d);
		 e3 = new Empleado(30,"Juan Perez",Tipo.CONTRATADO,300d);
		 t1 = new Tarea(1, "t1",8);
		 t2 = new Tarea(2, "t2",16);
		 t3 = new Tarea(3, "t3",20);

	}
	@Test
	public void asignarEmpleadoTest() throws AsignarEmpleadoException, AsignarTareaException {
	t1.asignarEmpleado(e1);
	assertTrue(t1.getEmpleadoAsignado()==e1);
	assertTrue(e1.getTareasAsignadas().contains(t1));
	}
	@Test (expected = AsignarEmpleadoException.class)
	public void asignarEmpleadoExceptionTest() throws AsignarEmpleadoException, AsignarTareaException, ComenzarTareaException, FinalizarTareaException {
		t1.asignarEmpleado(e1);
		e1.comenzar(t1.getId());
		e1.finalizar(t1.getId());
		t1.asignarEmpleado(e2);
	}
	
	@Test
	public void asCsv() throws AsignarEmpleadoException, AsignarTareaException {
	t1.asignarEmpleado(e1);
	assertTrue(t1.asCsv().equals("1;t1;8;Franco Velazquez;10"));
	}
	
	

}
