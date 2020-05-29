package frsf.isi.died.guia08.problema01.modelo;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.Before;
import org.junit.*;
import org.junit.Ignore;
import org.junit.Test;

import frsf.isi.died.guia08.problema01.modelo.Empleado.Tipo;


public class EmpleadoTest {

	// IMPORTANTE
	// ESTA CLASE ESTA ANOTADA COMO @IGNORE por lo que no ejecutará ningun test
	// hasta que no borre esa anotación.
	
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
	public void testSalario() throws AsignarTareaException, AsignarEmpleadoException, ComenzarTareaException, FinalizarTareaException {
		e1.asignarTarea(t1);
		e1.comenzar(t1.getId());
		e1.finalizar(t1.getId());
		e1.asignarTarea(t2);
		e1.comenzar(t2.getId());
		//solo se calcula como parte del salario el costo asociado a la tarea 1
		assertTrue((double) e1.salario() == 1040.0);
		
	}

	@Test
	public void testCostoTarea() throws AsignarTareaException, AsignarEmpleadoException, ComenzarTareaException, FinalizarTareaException {
		e2.asignarTarea(t2);
		assertTrue(e2.costoTarea(t2)==3200);
		e3.asignarTarea(t3);
		e3.comenzar(t3.getId());
		e3.finalizar(t3.getId());
		assertTrue(e3.costoTarea(t3)==7800);
		e1.asignarTarea(t1);
		e1.comenzar(t1.getId(),"20-03-2019 10:00");
		e1.finalizar(t1.getId(),"25-03-2019 10:00");
		assertTrue(e1.costoTarea(t1) == 600);
	}

	@Test
	public void testAsignarTarea() throws AsignarTareaException, AsignarEmpleadoException {
		e1.asignarTarea(t1);
		assertTrue(e1.getTareasAsignadas().contains(t1));
		assertTrue(t1.getEmpleadoAsignado()==e1);
	}
	
	@Test (expected = AsignarTareaException.class)
	public void testAsignarTareaExceptionFecha() throws AsignarTareaException, AsignarEmpleadoException {
		t1.setFechaFin(LocalDateTime.now());
		e1.asignarTarea(t1);
	}
	
	@Test (expected = AsignarTareaException.class)
	public void testAsignarTareaExceptionEmpleado() throws AsignarTareaException, AsignarEmpleadoException {
		t1.asignarEmpleado(e2);
		e1.asignarTarea(t1);
	}

	@Test
	public void testComenzarInteger() throws AsignarTareaException, AsignarEmpleadoException, ComenzarTareaException {
		e2.asignarTarea(t3);
		e2.comenzar(t3.getId());
		assertTrue(t3.getFechaInicio()!=null);
	}

	@Test (expected = ComenzarTareaException.class)
	public void testComenzarIntegerException() throws AsignarTareaException, AsignarEmpleadoException, ComenzarTareaException {
		e2.asignarTarea(t3);
		e3.comenzar(t3.getId());
		
	}
	
	@Test
	public void testFinalizarInteger() throws FinalizarTareaException, ComenzarTareaException, AsignarTareaException, AsignarEmpleadoException {
		e3.asignarTarea(t1);
		e3.comenzar(t1.getId());
		e3.finalizar(t1.getId());
		assertTrue(t1.getFechaInicio()!=null);
	}
	
	@Test  (expected = FinalizarTareaException.class)
	public void testFinalizarIntegerException() throws FinalizarTareaException, ComenzarTareaException, AsignarTareaException, AsignarEmpleadoException {
		e3.asignarTarea(t1);
		e3.comenzar(t1.getId());
		e2.finalizar(t1.getId());
	}

	@Test
	public void testComenzarIntegerString() throws ComenzarTareaException, AsignarTareaException, AsignarEmpleadoException {
		DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
		LocalDateTime fecha = LocalDateTime.parse("20-03-2019 16:00", formato);
		e2.asignarTarea(t3);
		e2.comenzar(t3.getId(),"20-03-2019 16:00");
		assertTrue(t3.getFechaInicio().equals(fecha));
	}

	@Test
	public void testFinalizarIntegerString() throws AsignarTareaException, AsignarEmpleadoException, ComenzarTareaException, FinalizarTareaException {
		DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
		LocalDateTime fecha = LocalDateTime.parse("15-01-2020 15:00", formato);
		e1.asignarTarea(t2);
		e1.comenzar(t2.getId());
		e1.finalizar(t2.getId(),"15-01-2020 15:00");
		assertTrue(t2.getFechaFin().equals(fecha));
	}
	
	
	@After
	public void reset() {
			e1 = new Empleado(10,"Franco Velazquez",Tipo.CONTRATADO,100d);
			 e2 = new Empleado(20,"Facundo Fernandez",Tipo.EFECTIVO,200d);
			 e3 = new Empleado(30,"Juan Perez",Tipo.CONTRATADO,300d);
			 t1 = new Tarea(1, "t1",8);
			 t2 = new Tarea(2, "t2",16);
			 t3 = new Tarea(3, "t3",20);

		}
}
