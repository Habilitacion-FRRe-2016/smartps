package com.smartps.beans;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import com.smartps.dao.PSDao;
import com.smartps.dao.InformeFinalDao;
import com.smartps.dao.TipoActividadDao;
import com.smartps.model.PS;
import com.smartps.model.InformeFinal;
import com.smartps.model.TipoActividad;
import com.smartps.model.LineaDeReporte;

@ManagedBean
@RequestScoped
public class GenerarReporteITipo {
//GenerarReporteITipo = GenerarReporteInformesPorTipoDeActividad_hdu10 
	
	private PSDao psdao = PSDao.getInstance();
	private InformeFinalDao ifdao = InformeFinalDao.getInstance();
	private TipoActividadDao tadao = TipoActividadDao.getInstance();

	private List<PS> pslist;
	private List<PS> pslistCL;
	private List<PS> pslistCuatri;
	private List<InformeFinal> informes;
	private List<InformeFinal> informesPeriodo;	
	private List<LineaDeReporte> linealist;
	private List<LineaDeReporte> resultlist;
	
	private List<TipoActividad> tipolist;
	private String tipo;
	private Map<String,String> tipos = new HashMap<String, String>();
	
	private int cicloLectivo;
	private int cuatrimestre;
	private Date desde;
	private Date hasta;
	private LineaDeReporte linea;

	private boolean totTipo;
	private boolean totTodos;
	
	//contadores
	private double cPP;
	private double cPA;
	private double cPD;
	private double cPV;
	
	private double cPPF;
	private double cPTI;
	private double cPGI;
	private double cPPA;
	
	
	@PostConstruct
	public void init(){
		pslist = new ArrayList<PS>();
		pslistCL = new ArrayList<PS>();
		pslistCuatri = new ArrayList<PS>();
		informes = new ArrayList<InformeFinal>();
		informesPeriodo = new ArrayList<InformeFinal>();
		linealist = new ArrayList<LineaDeReporte>();
		resultlist = new ArrayList<LineaDeReporte>();
		
		tipolist = new ArrayList<TipoActividad>();
		tipolist = tadao.getAll();
		
		for (int v=0; v<tipolist.size(); v++){
			tipos.put(tipolist.get(v).getNombre(), tipolist.get(v).getNombre());
		}

	}
	
	public String busquedaByFiltros(){

	//Listado		

		informes = ifdao.getAll();
		pslist = psdao.getAll();

		//Filtros
		
		if (tipo.isEmpty()){
			//Ningun campo rellenado
			if ((desde==null) && (hasta==null) && (cuatrimestre==0) && (cicloLectivo==0)){
				for (int a=0; a<informes.size(); a++){
					for (int b=0; b<pslist.size(); b++){
						if (informes.get(a).getPs().getId()==pslist.get(b).getId()){
							linea = new LineaDeReporte();
							linea.setFechaDePresentacion(informes.get(a).getFechaDePresentacion());
							linea.setTitulo(pslist.get(b).getTitulo());
							linea.setEstado(pslist.get(b).getEstado().getNombre());
							linea.setArea(pslist.get(b).getArea().getNombre());
							linea.setTipoActividad(pslist.get(b).getTipoActividad().getNombre());
							linea.setAlumno(pslist.get(b).getAlumno().getNombre());
							linea.setIngreso(pslist.get(b).getAlumno().getCicloLectivo());
							linealist.add(linea);
						}
					}
				}
			} else {
				//Sin Tipo, con periodo
				if ((desde!=null) && (hasta!=null)){
					informesPeriodo = ifdao.findByPeriodo(desde, hasta);
					for (int a=0; a<informesPeriodo.size(); a++){
						for (int b=0; b<pslist.size(); b++){
							if (informesPeriodo.get(a).getPs().getId()==pslist.get(b).getId()){
								if (
										((pslist.get(b).getCuatrimestre()==cuatrimestre) || (cuatrimestre==0))
										&&
										((pslist.get(b).getCicloLectivo()==cicloLectivo) || (cicloLectivo==0))
									){
									linea = new LineaDeReporte();
									linea.setFechaDePresentacion(informesPeriodo.get(a).getFechaDePresentacion());
									linea.setTitulo(pslist.get(b).getTitulo());
									linea.setEstado(pslist.get(b).getEstado().getNombre());
									linea.setArea(pslist.get(b).getArea().getNombre());
									linea.setTipoActividad(pslist.get(b).getTipoActividad().getNombre());
									linea.setAlumno(pslist.get(b).getAlumno().getNombre());
									linea.setIngreso(pslist.get(b).getAlumno().getCicloLectivo());
									linealist.add(linea);
								}
							}
						}
					}
				} else {
					//Sin Tipo, Por CicloLectivo
					pslistCL = psdao.findByCicloLectivo(cicloLectivo);
					for (int i=0; i<pslistCL.size(); i++){			
						for (int j=0; j<informes.size(); j++){
							if (pslistCL.get(i).getId()==informes.get(j).getPs().getId()){
								if ((pslistCL.get(i).getCuatrimestre()==cuatrimestre) || (cuatrimestre==0)){
									linea = new LineaDeReporte();
									linea.setFechaDePresentacion(informes.get(j).getFechaDePresentacion());
									linea.setTitulo(pslistCL.get(i).getTitulo());
									linea.setEstado(pslistCL.get(i).getEstado().getNombre());
									linea.setArea(pslistCL.get(i).getArea().getNombre());
									linea.setTipoActividad(pslistCL.get(i).getTipoActividad().getNombre());
									linea.setAlumno(pslistCL.get(i).getAlumno().getNombre());
									linea.setIngreso(pslistCL.get(i).getAlumno().getCicloLectivo());
									linealist.add(linea);
								}
							}
						}
					}
					if (cicloLectivo==0){
						//Sin Tipo, Por Cuatrimestre
						pslistCuatri = psdao.findByCuatrimestre(cuatrimestre);
						for (int x=0; x<pslistCuatri.size(); x++){			
							for (int y=0; y<informes.size(); y++){
								if (pslistCuatri.get(x).getId()==informes.get(y).getPs().getId()){
									linea = new LineaDeReporte();
									linea.setFechaDePresentacion(informes.get(y).getFechaDePresentacion());
									linea.setTitulo(pslistCuatri.get(x).getTitulo());
									linea.setEstado(pslistCuatri.get(x).getEstado().getNombre());
									linea.setArea(pslistCuatri.get(x).getArea().getNombre());
									linea.setTipoActividad(pslistCuatri.get(x).getTipoActividad().getNombre());
									linea.setAlumno(pslistCuatri.get(x).getAlumno().getNombre());
									linea.setIngreso(pslistCuatri.get(x).getAlumno().getCicloLectivo());
									linealist.add(linea);
								}
							}
						}
					}
				}
			}
		} else {
			//Solo con Tipo, los demas null
			if ((desde==null) && (hasta==null) && (cuatrimestre==0) && (cicloLectivo==0)){
				for (int a=0; a<informes.size(); a++){
					for (int b=0; b<pslist.size(); b++){
						if (informes.get(a).getPs().getId()==pslist.get(b).getId()){
							if (pslist.get(b).getTipoActividad().getNombre().equals(tipo)){
								linea = new LineaDeReporte();
								linea.setFechaDePresentacion(informes.get(a).getFechaDePresentacion());
								linea.setTitulo(pslist.get(b).getTitulo());
								linea.setEstado(pslist.get(b).getEstado().getNombre());
								linea.setArea(pslist.get(b).getArea().getNombre());
								linea.setTipoActividad(pslist.get(b).getTipoActividad().getNombre());
								linea.setAlumno(pslist.get(b).getAlumno().getNombre());
								linea.setIngreso(pslist.get(b).getAlumno().getCicloLectivo());
								linealist.add(linea);
							}
						}
					}
				}
			}
			
		//Con Tipo y Por Periodo
			if ((desde!=null) && (hasta!=null)){
				informesPeriodo = ifdao.findByPeriodo(desde, hasta);
				for (int a=0; a<informesPeriodo.size(); a++){
					for (int b=0; b<pslist.size(); b++){
						if (informesPeriodo.get(a).getPs().getId()==pslist.get(b).getId()){
							if (
									((pslist.get(b).getCuatrimestre()==cuatrimestre) || (cuatrimestre==0))
									&&
									((pslist.get(b).getCicloLectivo()==cicloLectivo) || (cicloLectivo==0))
									&&
									(pslist.get(b).getTipoActividad().getNombre().equals(tipo))
								){
								linea = new LineaDeReporte();
								linea.setFechaDePresentacion(informesPeriodo.get(a).getFechaDePresentacion());
								linea.setTitulo(pslist.get(b).getTitulo());
								linea.setEstado(pslist.get(b).getEstado().getNombre());
								linea.setArea(pslist.get(b).getArea().getNombre());
								linea.setTipoActividad(pslist.get(b).getTipoActividad().getNombre());
								linea.setAlumno(pslist.get(b).getAlumno().getNombre());
								linea.setIngreso(pslist.get(b).getAlumno().getCicloLectivo());
								linealist.add(linea);
							}
						}
					}
				}
			} else {
				//Con Tipo, Por CicloLectivo
				pslistCL = psdao.findByCicloLectivo(cicloLectivo);
				for (int i=0; i<pslistCL.size(); i++){			
					for (int j=0; j<informes.size(); j++){
						if (pslistCL.get(i).getId()==informes.get(j).getPs().getId()){
							if ((pslistCL.get(i).getCuatrimestre()==cuatrimestre) || (cuatrimestre==0)){
								if (pslistCL.get(i).getTipoActividad().getNombre().equals(tipo)){
									linea = new LineaDeReporte();
									linea.setFechaDePresentacion(informes.get(j).getFechaDePresentacion());
									linea.setTitulo(pslistCL.get(i).getTitulo());
									linea.setEstado(pslistCL.get(i).getEstado().getNombre());
									linea.setArea(pslistCL.get(i).getArea().getNombre());
									linea.setTipoActividad(pslistCL.get(i).getTipoActividad().getNombre());
									linea.setAlumno(pslistCL.get(i).getAlumno().getNombre());
									linea.setIngreso(pslistCL.get(i).getAlumno().getCicloLectivo());
									linealist.add(linea);
								}
							}
						}
					}
				}
				if (cicloLectivo==0){
					//Con Tipo, Por Cuatrimestre
					pslistCuatri = psdao.findByCuatrimestre(cuatrimestre);
					for (int x=0; x<pslistCuatri.size(); x++){			
						for (int y=0; y<informes.size(); y++){
							if (pslistCuatri.get(x).getId()==informes.get(y).getPs().getId()){
								if (pslistCuatri.get(x).getTipoActividad().getNombre().equals(tipo)){
									linea = new LineaDeReporte();
									linea.setFechaDePresentacion(informes.get(y).getFechaDePresentacion());
									linea.setTitulo(pslistCuatri.get(x).getTitulo());
									linea.setEstado(pslistCuatri.get(x).getEstado().getNombre());
									linea.setArea(pslistCuatri.get(x).getArea().getNombre());
									linea.setTipoActividad(pslistCuatri.get(x).getTipoActividad().getNombre());
									linea.setAlumno(pslistCuatri.get(x).getAlumno().getNombre());
									linea.setIngreso(pslistCuatri.get(x).getAlumno().getCicloLectivo());
									linealist.add(linea);
								}
							}
						}
					}
				}
			}
		}

		
		//Eliminar Repetidos
		boolean repe = false;
		for (int e=0; e<linealist.size(); e++){
			if (resultlist.isEmpty()){
				resultlist.add(linealist.get(e));
			}
			for (int f=0; f<resultlist.size(); f++){
				if (linealist.get(e).getFechaDePresentacion()==resultlist.get(f).getFechaDePresentacion()){
					repe = true;
				}				
			}
			if (repe==false){
				resultlist.add(linealist.get(e));
			}
			repe = false;
		}
		
				
	//Totales
		//Planes Presentados
		cPP = resultlist.size();
		
		if (cPP!=0){
				
			if (!(tipo.isEmpty())){
				totTipo = true;
				totTodos = false;
				
				//Porcentaje Informes Aprobados
				double contPA = 0;
				for (int p=0; p<resultlist.size(); p++){
					if (
							(resultlist.get(p).getEstado().equals("Informe aprobado"))
							||
							(resultlist.get(p).getEstado().equals("PS aprobada"))
						){
						contPA++;
					}
				}
				cPA = ((contPA/cPP)*100);			
				
				//Porcentaje Informes Observado
				double contPD = 0;
				for (int q=0; q<resultlist.size(); q++){
					if (resultlist.get(q).getEstado().equals("Informe observado")){
						contPD++;
					}
				}
				cPD = ((contPD/cPP)*100);
				
				//Porcentaje Informes Vencidos
				double contPV = 0;
				for (int r=0; r<resultlist.size(); r++){
					if (resultlist.get(r).getEstado().equals("Informe vencido")){
						contPV++;
					}
				}
				cPV = ((contPV/cPP)*100);
				
			} else {
				totTipo = false;
				totTodos = true;
				
				//Porcentaje Proyecto Final
				double contPF = 0;
				for (int l=0; l<resultlist.size(); l++){
					if (resultlist.get(l).getTipoActividad().equals("Proyecto Final")){
						contPF++;
					}
				}
				cPPF = ((contPF/cPP)*100);
				
				//Porcentaje Tareas de ingenieria
				double contTI = 0;
				for (int n=0; n<resultlist.size(); n++){
					if (resultlist.get(n).getTipoActividad().equals("Tareas de ingenieria")){
						contTI++;
					}
				}
				cPTI = ((contTI/cPP)*100);
				
				//Porcentaje Grupos de Investigacion
				double contGI = 0;
				for (int z=0; z<resultlist.size(); z++){
					if (resultlist.get(z).getTipoActividad().equals("Grupos de Investigacion")){
						contGI++;
					}					
				}
				cPGI = ((contGI/cPP)*100);
				
				//Porcentaje Pasantia
				double contPasantia = 0;
				for (int e=0; e<resultlist.size(); e++){
					if (resultlist.get(e).getTipoActividad().equals("Pasantia")){
						contPasantia++;
					}
				}
				cPPA = ((contPasantia/cPP)*100);
				
			}
						
		}
		
	//Redireccion
		if (resultlist.size()==0){
			return "estIFporTipoNohay";
		} else {
			return "estIFporTipoListado";
		}

	}

	
	public String volver(){
		return "estIFporTipo";
	}
	
	
	
	public List<PS> getPslist() {
		return pslist;
	}

	public void setPslist(List<PS> pslist) {
		this.pslist = pslist;
	}

	public List<PS> getPslistCL() {
		return pslistCL;
	}

	public void setPslistCL(List<PS> pslistCL) {
		this.pslistCL = pslistCL;
	}

	public List<PS> getPslistCuatri() {
		return pslistCuatri;
	}

	public void setPslistCuatri(List<PS> pslistCuatri) {
		this.pslistCuatri = pslistCuatri;
	}

	public List<InformeFinal> getInformes() {
		return informes;
	}

	public void setInformes(List<InformeFinal> informes) {
		this.informes = informes;
	}

	public List<InformeFinal> getInformesPeriodo() {
		return informesPeriodo;
	}

	public void setInformesPeriodo(List<InformeFinal> informesPeriodo) {
		this.informesPeriodo = informesPeriodo;
	}

	public List<LineaDeReporte> getLinealist() {
		return linealist;
	}

	public void setLinealist(List<LineaDeReporte> linealist) {
		this.linealist = linealist;
	}

	public List<LineaDeReporte> getResultlist() {
		return resultlist;
	}

	public void setResultlist(List<LineaDeReporte> resultlist) {
		this.resultlist = resultlist;
	}

	public List<TipoActividad> getTipolist() {
		return tipolist;
	}

	public void setTipolist(List<TipoActividad> tipolist) {
		this.tipolist = tipolist;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Map<String, String> getTipos() {
		return tipos;
	}

	public void setTipos(Map<String, String> tipos) {
		this.tipos = tipos;
	}

	public int getCicloLectivo() {
		return cicloLectivo;
	}

	public void setCicloLectivo(int cicloLectivo) {
		this.cicloLectivo = cicloLectivo;
	}

	public int getCuatrimestre() {
		return cuatrimestre;
	}

	public void setCuatrimestre(int cuatrimestre) {
		this.cuatrimestre = cuatrimestre;
	}

	public Date getDesde() {
		return desde;
	}

	public void setDesde(Date desde) {
		this.desde = desde;
	}

	public Date getHasta() {
		return hasta;
	}

	public void setHasta(Date hasta) {
		this.hasta = hasta;
	}
	
	public LineaDeReporte getLinea() {
		return linea;
	}

	public void setLinea(LineaDeReporte linea) {
		this.linea = linea;
	}

	public boolean isTotTipo() {
		return totTipo;
	}

	public void setTotTipo(boolean totTipo) {
		this.totTipo = totTipo;
	}

	public boolean isTotTodos() {
		return totTodos;
	}

	public void setTotTodos(boolean totTodos) {
		this.totTodos = totTodos;
	}
	
	public double getcPP() {
		return cPP;
	}

	public void setcPP(double cPP) {
		this.cPP = cPP;
	}

	public double getcPA() {
		return cPA;
	}

	public void setcPA(double cPA) {
		this.cPA = cPA;
	}

	public double getcPD() {
		return cPD;
	}

	public void setcPD(double cPD) {
		this.cPD = cPD;
	}

	public double getcPV() {
		return cPV;
	}

	public void setcPV(double cPV) {
		this.cPV = cPV;
	}

	public double getcPPF() {
		return cPPF;
	}

	public void setcPPF(double cPPF) {
		this.cPPF = cPPF;
	}

	public double getcPTI() {
		return cPTI;
	}

	public void setcPTI(double cPTI) {
		this.cPTI = cPTI;
	}

	public double getcPGI() {
		return cPGI;
	}

	public void setcPGI(double cPGI) {
		this.cPGI = cPGI;
	}

	public double getcPPA() {
		return cPPA;
	}

	public void setcPPA(double cPPA) {
		this.cPPA = cPPA;
	}

}
