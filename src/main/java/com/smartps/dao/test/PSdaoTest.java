package com.smartps.dao.test;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.smartps.dao.AlumnoDAO;
import com.smartps.dao.AreaDao;
import com.smartps.dao.EstadoDao;
import com.smartps.dao.OrganizacionDAO;
import com.smartps.dao.PSDao;
import com.smartps.dao.TipoActividadDao;
import com.smartps.model.PS;
import com.smartps.model.PlanDeTrabajo;

public class PSdaoTest {
	PS ps=new PS();
	
	@Before
	public void setUp() throws Exception {
		ps.setAlumno(new AlumnoDAO().buscarAlumno(18189));
		
		ps.setArea(AreaDao.getInstance().buscarArea(1));
		ps.setCicloLectivo(2016);
		ps.setCuatrimestre(2);
		ps.setEstado(new EstadoDao().buscarPorNombre("Plan Presentado"));
		ps.setOrganizacion(OrganizacionDAO.getInstance().getAll().get(0));
		ps.setTipoActividad(TipoActividadDao.getInstance().getAll().get(0));
		ps.setTitulo("Ps de Prueba");		
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testEstadoDao() {
		assertFalse(new EstadoDao().buscarPorNombre("Plan Presentado")==null);
	}
	
	@Test
	public void testSave(){
		int valant = new PSDao().getAll().size();
		new PSDao().save(ps);	
		int valpos = new PSDao().getAll().size();
		assertTrue(valant==valpos-1);
	}
	
	//este test falla. arreglar PSDao
	@Test
	public void testDelete(){
		int valant = new PSDao().getAll().size();
		new PSDao().delete(ps);
		int valpos = new PSDao().getAll().size();
		assertTrue(valant==valpos+1);
	}
	
//	@Test
//	public void testUpdate(){
//		PlanDeTrabajo plan = new PlanDeTrabajo();
//		plan.setPs(ps);
//		plan.setFechaDePresentacion(new Date());
//		plan.setObservaciones("presentacion de prueba");
//		ps.getPlanDeTrabajo().add(plan);
//		new PSDao().save(ps);
//		new PSDao().update(ps);
//		assertTrue(true); //no se que preguntar xD
//		
//	}

}
