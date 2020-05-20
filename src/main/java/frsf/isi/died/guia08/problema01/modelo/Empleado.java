package frsf.isi.died.guia08.problema01.modelo;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Empleado {

	public enum Tipo { CONTRATADO,EFECTIVO}; 
	
	private Integer cuil;
	private String nombre;
	private Tipo tipo;
	private Double costoHora;
	private List<Tarea> tareasAsignadas;
	
	private Function<Tarea, Double> calculoPagoPorTarea;		
	private Predicate<Tarea> puedeAsignarTarea;
	private Predicate <Tarea> terminoAntes;
	private Predicate <Tarea> terminoTarde;

	
	
	public Empleado(Integer cuil, String nombre, Tipo tipo, Double costoHora, List<Tarea> tareasAsignadas) {
		super();
		this.cuil = cuil;
		this.nombre = nombre;
		this.tipo = tipo;
		this.costoHora = costoHora;
		this.tareasAsignadas = tareasAsignadas;
		
		this.terminoAntes = varTarea -> {
			if(varTarea.getDuracionEstimada()- (96* (Duration.between(varTarea.getFechaInicio(),varTarea.getFechaFin()).toDays())) > 0)
				return true;
			else
				return false;
		};
		this.terminoTarde = varTarea -> {
			if(varTarea.getDuracionEstimada()- (96* (Duration.between(varTarea.getFechaInicio(),varTarea.getFechaFin()).toDays())) <= -8)
				return true;
			else
				return false;
		};
		
		
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
		
		
		
		switch(tipo) {
		case CONTRATADO:
			this.calculoPagoPorTarea = pagoTarea ->{
				if(terminoAntes.test(pagoTarea)) {
					return	1.3*costoTarea(pagoTarea);			}
				else if(terminoTarde.test(pagoTarea)){
					return  0.75*costoTarea(pagoTarea);
				}
				else
					return costoTarea(pagoTarea);
			};
			break;
		case EFECTIVO:
			this.calculoPagoPorTarea = pagoTarea->{
				if(terminoAntes.test(pagoTarea)) {
					return 1.2*costoTarea(pagoTarea);
				}
				else {
					return costoTarea(pagoTarea);
				}
			};
			break;
		
		}
		}
	}
	
	
	
	public Double salario() {
		
		List<Tarea> TareasFinNoFacturadas= this.getTareasAsignadas().stream()
			.filter(tareasNoFacturadas -> tareasNoFacturadas.getFacturada()==false)
			.filter(tareaFin-> tareaFin.getFechaFin() != null)
			.collect(Collectors.toList());
		
		this.getTareasAsignadas().stream()
		.filter(tareasNoFacturadas -> tareasNoFacturadas.getFacturada()==false)
		.filter(tareaFin-> tareaFin.getFechaFin() != null)
		.forEach(t-> t.setFacturada(true));
		double sumaSalario=0;
		for (Tarea tarea : TareasFinNoFacturadas) {
			sumaSalario +=calculoPagoPorTarea.apply(tarea);
		}
		
		return sumaSalario;
	}
	
	
	
	public Integer getCuil() {
		return cuil;
	}

	public void setCuil(Integer cuil) {
		this.cuil = cuil;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Tipo getTipo() {
		return tipo;
	}

	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}

	public Double getCostoHora() {
		return costoHora;
	}

	public void setCostoHora(Double costoHora) {
		this.costoHora = costoHora;
	}

	public List<Tarea> getTareasAsignadas() {
		return tareasAsignadas;
	}

	/**
	 * Si la tarea ya fue terminada nos indica cuaal es el monto según el algoritmo de calculoPagoPorTarea
	 * Si la tarea no fue terminada simplemente calcula el costo en base a lo estimado.
	 * @param t
	 * @return
	 */
	
	
	
	public Double costoTarea(Tarea t) {
		return 0.0;
	}
		
	



	public Boolean asignarTarea(Tarea t) throws ExcepcionAsignarTarea {
		if(t.getEmpleadoAsignado() == null) {
			if(t.getFechaFin()==null) {
				if(puedeAsignarTarea.test(t)) {
					tareasAsignadas.add(t);
					return true;
				}
				else
					return false;
			}
			else throw new ExcepcionAsignarTarea("La tarea se encuentra terminada");
		}
		else throw new ExcepcionAsignarTarea("Ya hay un empleado asignado a esta tarea");
		
	}
	
	public void comenzar(Integer idTarea) throws ExcepcionGeneralTarea {
		// busca la tarea en la lista de tareas asignadas 
		// si la tarea no existe lanza una excepción
		// si la tarea existe indica como fecha de inicio la fecha y hora actual
		if(tareasAsignadas.stream().anyMatch(tarea -> tarea.getId().equals(idTarea))) {
			Tarea tareaCom = tareasAsignadas.stream().filter(tarea -> tarea.getId().equals(idTarea)).findAny().get();
			tareaCom.setFechaInicio(LocalDateTime.now());
		}
		else throw new ExcepcionGeneralTarea("No existe la tarea,no ha podido comenzarse");
		
		}
		
	
	public void finalizar(Integer idTarea) throws ExcepcionGeneralTarea{
		// busca la tarea en la lista de tareas asignadas 
		// si la tarea no existe lanza una excepción
		// si la tarea existe indica como fecha de finalizacion la fecha y hora actual
		if(tareasAsignadas.stream().anyMatch(tarea -> tarea.getId().equals(idTarea))) {
			Tarea tareaCom = tareasAsignadas.stream().filter(tarea -> tarea.getId().equals(idTarea)).findAny().get();
			tareaCom.setFechaFin(LocalDateTime.now());
		}
		else throw new ExcepcionGeneralTarea("No existe la tarea,no ha podido finalizarse");
	}

	public void comenzar(Integer idTarea,String fecha) throws ExcepcionGeneralTarea {
		// busca la tarea en la lista de tareas asignadas 
		// si la tarea no existe lanza una excepción
		// si la tarea existe indica como fecha de finalizacion la fecha y hora actual
		
		if(tareasAsignadas.stream().anyMatch(tarea -> tarea.getId().equals(idTarea))) {
			Tarea tareaCom = tareasAsignadas.stream().filter(tarea -> tarea.getId().equals(idTarea)).findAny().get();
			tareaCom.setFechaInicio(LocalDateTime.parse(fecha));
		}
		else throw new ExcepcionGeneralTarea("No existe la tarea");
		
		
	}
	
	public void finalizar(Integer idTarea,String fecha) throws ExcepcionGeneralTarea {
		// busca la tarea en la lista de tareas asignadas 
		// si la tarea no existe lanza una excepción
		// si la tarea existe indica como fecha de finalizacion la fecha y hora actual
		if(tareasAsignadas.stream().anyMatch(tarea -> tarea.getId().equals(idTarea))) {
			Tarea tareaCom = tareasAsignadas.stream().filter(tarea -> tarea.getId().equals(idTarea)).findAny().get();
			tareaCom.setFechaFin(LocalDateTime.parse(fecha));
		}
		else throw new ExcepcionGeneralTarea("No existe la tarea,no ha podido finalizarse");
	}
}
