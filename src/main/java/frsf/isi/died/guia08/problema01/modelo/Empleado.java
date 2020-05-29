package frsf.isi.died.guia08.problema01.modelo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.*;
import java.time.temporal.ChronoUnit;	


public class Empleado {

	public enum Tipo { CONTRATADO,EFECTIVO}; 
	
	private Integer cuil;
	private String nombre;
	private Tipo tipo;
	private Double costoHora;
	private List<Tarea> tareasAsignadas;
	
	private Function<Tarea, Double> calculoPagoPorTarea;		
	private Predicate<Tarea> puedeAsignarTarea;
	
	public Integer getCuil() {
		return cuil;
	}

	public String getNombre() {
		return nombre;
	}

	public Tipo getTipo() {
		return tipo;
	}

	public Double getCostoHora() {
		return costoHora;
	}

	public Function<Tarea, Double> getCalculoPagoPorTarea() {
		return calculoPagoPorTarea;
	}

	public Predicate<Tarea> getPuedeAsignarTarea() {
		return puedeAsignarTarea;
	}

	public Empleado(Integer cuil, String nombre, Tipo tipo, Double costoHora) {
		super();
		this.cuil = cuil;
		this.nombre = nombre;
		this.tipo = tipo;
		this.costoHora = costoHora;
		this.tareasAsignadas = new ArrayList<Tarea>();
		Predicate<Tarea> terminoAntes = t -> t.getDuracionEstimada() > 4*( ChronoUnit.DAYS.between(t.getFechaInicio(),t.getFechaFin()));
		Predicate<Tarea> terminoDespues = t -> t.getDuracionEstimada() < 8+ 4*( ChronoUnit.DAYS.between(t.getFechaInicio(),t.getFechaFin()));										
		
		switch(tipo) {
		case CONTRATADO:
		this.puedeAsignarTarea = variableTarea -> getTareasAsignadas().stream()
				 .filter(tareaFin -> tareaFin.getFechaFin() != null)
				 .count() <= 5;
		
		break;
		case EFECTIVO: 
		this.puedeAsignarTarea = variableTareaE -> getTareasAsignadas().stream()
				.filter(tareaFin -> tareaFin.getFechaFin() != null)
				.mapToInt( tareas -> tareas.getDuracionEstimada())
				.sum() <15;
		break;}
		
		switch(tipo) {
		case CONTRATADO:
		this.calculoPagoPorTarea = t ->  terminoAntes.test(t) ?  t.getDuracionEstimada()*(this.costoHora*1.3) : 
			(terminoDespues.test(t) ?  t.getDuracionEstimada()*(this.costoHora*0.75) : t.getDuracionEstimada()*this.costoHora);
		break;
		case EFECTIVO:
		this.calculoPagoPorTarea = t -> terminoAntes.test(t) ? t.getDuracionEstimada()*(this.costoHora*1.2) :
			t.getDuracionEstimada()*(this.costoHora);
		}
		
		
	}

	public List<Tarea> getTareasAsignadas() {
		return this.tareasAsignadas;
	}

	public Double salario() {
		// cargar todas las tareas no facturadas
		// calcular el costo
		// marcarlas como facturadas.
		ArrayList<Tarea> listaNoFacturadas = new ArrayList<Tarea>();
		for (Tarea tarea : this.tareasAsignadas) {
			if(!tarea.getFacturada() && tarea.getFechaFin()!=null)
				listaNoFacturadas.add(tarea);
		}
		double montoSalario = 0;
		for (Tarea tarea : listaNoFacturadas) {
			montoSalario+=this.calculoPagoPorTarea.apply(tarea);
		}
		
		return montoSalario;
		
	}
	
	/**
	 * Si la tarea ya fue terminada nos indica cuaal es el monto según el algoritmo de calculoPagoPorTarea
	 * Si la tarea no fue terminada simplemente calcula el costo en base a lo estimado.
	 * @param t
	 * @return
	 */
	public Double costoTarea(Tarea t) {
		if(t.getFechaFin()!=null) {
			return calculoPagoPorTarea.apply(t);
		}
		else {
			return t.getDuracionEstimada()*this.getCostoHora();
		}
	}
		
	public Boolean asignarTarea(Tarea t) throws AsignarTareaException, AsignarEmpleadoException {
		if(t.getEmpleadoAsignado()==null) {
			if(t.getFechaFin()==null) {
				if(this.puedeAsignarTarea.test(t)) {
					t.setEmpleadoAsignado(this);
					this.tareasAsignadas.add(t);
					return true;
				}
				else
					return false;
				}
			else throw new AsignarTareaException("La tarea ha finalizado");
		}
		else throw new AsignarTareaException("Ya se encuentra otro empleado asignado a esta tarea");
	}
	
	public void comenzar(Integer idTarea) throws ComenzarTareaException {
		// busca la tarea en la lista de tareas asignadas 
		// si la tarea no existe lanza una excepción
		// si la tarea existe indica como fecha de inicio la fecha y hora actual
		boolean esta=false;
		for (Tarea tarea : tareasAsignadas) {
			if(tarea.getId() == idTarea){
				esta=true;
				tarea.setFechaInicio(LocalDateTime.now());
			}
		}
		if(esta == false) {
			throw new ComenzarTareaException();
		}
	}
	
	public void finalizar(Integer idTarea) throws FinalizarTareaException {
		// busca la tarea en la lista de tareas asignadas 
		// si la tarea no existe lanza una excepción
		// si la tarea existe indica como fecha de finalizacion la fecha y hora actual
		boolean esta=false;
		for (Tarea tarea : tareasAsignadas) {
			if(tarea.getId() == idTarea){
				esta=true;
				tarea.setFechaFin(LocalDateTime.now());
			}
		}
		if(esta == false) {
			throw new FinalizarTareaException();
		}
	}

	public void comenzar(Integer idTarea,String fecha) throws ComenzarTareaException {
		// busca la tarea en la lista de tareas asignadas 
		// si la tarea no existe lanza una excepción
		// si la tarea existe indica como fecha de finalizacion la fecha y hora actual
		boolean esta=false;
		DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
		for (Tarea tarea : tareasAsignadas) {
			if(tarea.getId() == idTarea){
				esta=true;
				tarea.setFechaInicio(LocalDateTime.parse(fecha,formato));
			}
		}
		if(esta == false) {
			throw new ComenzarTareaException();
		}
		
	}
	
	public void finalizar(Integer idTarea,String fecha) throws FinalizarTareaException {
		// busca la tarea en la lista de tareas asignadas 
		// si la tarea no existe lanza una excepción
		// si la tarea existe indica como fecha de finalizacion la fecha y hora actual
		boolean esta=false;
		DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
		for (Tarea tarea : tareasAsignadas) {
			if(tarea.getId() == idTarea){
				esta=true;
				tarea.setFechaFin(LocalDateTime.parse(fecha,formato));
			}
		}
		if(esta == false) {
			throw new FinalizarTareaException();
		}
		
	}
}
