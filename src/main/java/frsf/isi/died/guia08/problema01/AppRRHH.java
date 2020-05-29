package frsf.isi.died.guia08.problema01;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import frsf.isi.died.guia08.problema01.modelo.AsignarEmpleadoException;
import frsf.isi.died.guia08.problema01.modelo.AsignarTareaException;
import frsf.isi.died.guia08.problema01.modelo.ComenzarTareaException;
import frsf.isi.died.guia08.problema01.modelo.Empleado;
import frsf.isi.died.guia08.problema01.modelo.Empleado.Tipo;
import frsf.isi.died.guia08.problema01.modelo.FinalizarTareaException;
import frsf.isi.died.guia08.problema01.modelo.Tarea;

public class AppRRHH {

	private List<Empleado> empleados;
	
	public AppRRHH() {
		super();
		 empleados = new ArrayList<Empleado>();
	}

	public List<Empleado> getEmpleados() {
		return empleados;
	}

	public void agregarEmpleadoContratado(Integer cuil,String nombre,Double costoHora) {
		// crear un empleado
		// agregarlo a la lista
		if(empleados==null)
			this.empleados = new ArrayList<Empleado>();
		Empleado e1 = new Empleado(cuil,nombre,Tipo.CONTRATADO,costoHora);
		this.empleados.add(e1);
	}
	
	public void agregarEmpleadoEfectivo(Integer cuil,String nombre,Double costoHora) {
		// crear un empleado
		// agregarlo a la lista
		if(empleados==null)
			this.empleados = new ArrayList<Empleado>();
		Empleado e1 = new Empleado(cuil,nombre,Tipo.EFECTIVO,costoHora);
		empleados.add(e1);
	}
	
	public void asignarTarea(Integer cuil,Integer idTarea,String descripcion,Integer duracionEstimada) throws AsignarTareaException, AsignarEmpleadoException {
		// crear un empleado
		// con el método buscarEmpleado() de esta clase
		// agregarlo a la lista	
		Optional<Empleado> emp = buscarEmpleado(e -> e.getCuil().equals(cuil));
		if(emp.isPresent()) {
		emp.get().asignarTarea(new Tarea(idTarea,descripcion,duracionEstimada));
		}
	}
	
	public void empezarTarea(Integer cuil,Integer idTarea) throws ComenzarTareaException {
		// busca el empleado por cuil en la lista de empleados
		// con el método buscarEmpleado() actual de esta clase
		// e invoca al método comenzar tarea
		Optional<Empleado> emp = buscarEmpleado(e -> e.getCuil().equals(cuil));
		if(emp.isPresent()) {
		emp.get().comenzar(idTarea);
	
		}
	}
	
	public void terminarTarea(Integer cuil,Integer idTarea) throws FinalizarTareaException {
		// crear un empleado
		// agregarlo a la lista		
		Optional<Empleado> emp = buscarEmpleado(e -> e.getCuil().equals(cuil));
		if(emp.isPresent()) {
		emp.get().finalizar(idTarea);
	
		}
	}

	public void cargarEmpleadosContratadosCSV(String nombreArchivo) throws IOException {
		// leer datos del archivo
		// por cada fila invocar a agregarEmpleadoContratado
		FileReader reader = new FileReader(nombreArchivo);
		BufferedReader lectorBuffer = new BufferedReader(reader);
		
		String entrada;
		try {
			while((entrada=lectorBuffer.readLine())!= null) {
				String[] atributos = entrada.split(";");
				agregarEmpleadoContratado(Integer.getInteger(atributos[0]),atributos[1],Double.valueOf(atributos[2]));
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		lectorBuffer.close();
	}

	public void cargarEmpleadosEfectivosCSV(String nombreArchivo) throws IOException {
		// leer datos del archivo
		// por cada fila invocar a agregarEmpleadoContratado	
		FileReader reader = new FileReader(nombreArchivo);
		BufferedReader lectorBuffer = new BufferedReader(reader);
		
		String entrada;
		try {
			while((entrada=lectorBuffer.readLine())!= null) {
				String[] atributos = entrada.split(";");
				agregarEmpleadoEfectivo(Integer.getInteger(atributos[0]),atributos[1],Double.valueOf(atributos[2]));
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		lectorBuffer.close();
		
	}

	public void cargarTareasCSV(String nombreArchivo) throws AsignarTareaException, AsignarEmpleadoException, IOException {
		// leer datos del archivo
		// cada fila del archivo tendrá:
		// cuil del empleado asignado, numero de la taera, descripcion y duración estimada en horas.
		FileReader reader = new FileReader(nombreArchivo);
		BufferedReader lectorBuffer = new BufferedReader(reader);
		
		String entrada;
		try {
			while((entrada=lectorBuffer.readLine())!= null) {
				String[] atributosT = entrada.split(";");
				asignarTarea(Integer.getInteger(atributosT[0]),Integer.getInteger(atributosT[1]),atributosT[2],Integer.getInteger(atributosT[3]));
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		lectorBuffer.close();
	}
	
	
	private void guardarTareasTerminadasCSV() throws IOException {
		// guarda una lista con los datos de la tarea que fueron terminadas
		// y todavía no fueron facturadas
		// y el nombre y cuil del empleado que la finalizó en formato CSV 
		FileWriter writer = new FileWriter("tareasTerminadas.csv",true);
		BufferedWriter escritorBuffer = new BufferedWriter(writer);
		
		List<Tarea> terminadas = new ArrayList<Tarea>();
		for (Empleado e : empleados) {
			terminadas.addAll(e.getTareasAsignadas());
		}
		terminadas = terminadas.stream().filter(t -> t.getFechaFin()!=null && t.getFacturada()==false).collect(Collectors.toList());
		for (Tarea tarea : terminadas) {
			escritorBuffer.write(tarea.asCsv()+ System.getProperty("line.separator"));
		}
		escritorBuffer.close();
	}
	
	private Optional<Empleado> buscarEmpleado(Predicate<Empleado> p){
		return this.empleados.stream().filter(p).findFirst();
	}

	public Double facturar() throws IOException {
		this.guardarTareasTerminadasCSV();
		return this.empleados.stream()				
				.mapToDouble(e -> e.salario())
				.sum();
	}
}
