package frsf.isi.died.guia08.problema01;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import frsf.isi.died.guia08.problema01.modelo.AsignarEmpleadoException;
import frsf.isi.died.guia08.problema01.modelo.AsignarTareaException;
import frsf.isi.died.guia08.problema01.modelo.ComenzarTareaException;
import frsf.isi.died.guia08.problema01.modelo.Empleado;
import frsf.isi.died.guia08.problema01.modelo.Tarea;
import frsf.isi.died.guia08.problema01.modelo.Empleado.Tipo;
import frsf.isi.died.guia08.problema01.modelo.FinalizarTareaException;

public class AppRRHHTest {
	
	private AppRRHH app1;
	private  Empleado e1;
	private  Empleado e2;
	private  Empleado e3;
	private  Tarea t1;
	private  Tarea t2;
	private  Tarea t3;
	
	@Before
	public void init() {
		 app1 = new AppRRHH();
		 e1 = new Empleado(10,"Franco Velazquez",Tipo.CONTRATADO,100d);
		 e2 = new Empleado(20,"Facundo Fernandez",Tipo.EFECTIVO,200d);
		 e3 = new Empleado(30,"Juan Perez",Tipo.CONTRATADO,300d);
		 t1 = new Tarea(1, "t1",8);
		 t2 = new Tarea(2, "t2",16);
		 t3 = new Tarea(3, "t3",20);

	}
	
	@Test
	public void agregarEmpleadoContratadotest() {
		app1.agregarEmpleadoContratado(100,"Lautaro Acosta",250d);
		assertTrue((app1.getEmpleados().get(0).getCuil().equals(100)));
		assertTrue((app1.getEmpleados().get(0).getNombre().equals("Lautaro Acosta")));
		assertTrue(app1.getEmpleados().get(0).getTipo()==Tipo.CONTRATADO);
	}
	
	@Test
	public void agregarEmpleadoEfectivotest() {
		app1.agregarEmpleadoEfectivo(200,"Jorge Reyes",250d);
		assertTrue((app1.getEmpleados().get(0).getCuil().equals(200)));
		assertTrue((app1.getEmpleados().get(0).getNombre().equals("Jorge Reyes")));
		assertTrue(app1.getEmpleados().get(0).getTipo()==Tipo.EFECTIVO);
	}
	
	@Test
	public void empezarTareatest() throws AsignarTareaException, AsignarEmpleadoException, ComenzarTareaException {
		app1.getEmpleados().add(e1);
		app1.asignarTarea(e1.getCuil(),4,"t4LA",12);
		app1.empezarTarea(e1.getCuil(),4);
		assertTrue(e1.getTareasAsignadas().get(0).getFechaInicio()!=null);
	}
	
	@Test
	public void terminarTareatest() throws AsignarTareaException, AsignarEmpleadoException, ComenzarTareaException, FinalizarTareaException {
		app1.getEmpleados().add(e1);
		app1.asignarTarea(e1.getCuil(),4,"t4LA",12);
		app1.empezarTarea(e1.getCuil(),4);
		app1.terminarTarea(e1.getCuil(),4);
		assertTrue(e1.getTareasAsignadas().get(0).getFechaFin()!=null);
	}
	
	
	@After
	public void reset() {
	 app1 = new AppRRHH();
	 e1 = new Empleado(10,"Franco Velazquez",Tipo.CONTRATADO,100d);
	 e2 = new Empleado(20,"Facundo Fernandez",Tipo.EFECTIVO,200d);
	 e3 = new Empleado(30,"Juan Perez ",Tipo.CONTRATADO,300d);
	 t1 = new Tarea(1, "t1",8);
	 t2 = new Tarea(2, "t2",16);
	 t3 = new Tarea(3, "t3",20);
	}
	

}
