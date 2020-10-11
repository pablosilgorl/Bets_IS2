package test;
/**
 * DataAccessTest: Some JUnit example for DataAccess
 */

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import configuration.ConfigXML;
import dataAccess.DataAccess;
import domain.Apuesta;
import domain.ApuestaCombinada;
import domain.Event;
import domain.Pronostico;
import domain.Question;
import domain.User;
import exceptions.QuestionAlreadyExist;
import test.TestFacadeImplementation;

class DataAccessTest {
	// sut- System Under Test
	private DataAccess sut = new DataAccess(ConfigXML.getInstance().getDataBaseOpenMode().equals("initialize"));;
	private TestFacadeImplementation testBL = new TestFacadeImplementation();

	private String queryText = "A question";
	private Float betMinimum = 2.0f;
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	private Event ev;

	@Test
	@DisplayName("The event has one question with a queryText")
	void createQuestionDATest1() {

		try {
			Date oneDate = sdf.parse("05/10/2022");

			// configure the state of the system (create object in the dabatase)
			ev = testBL.addEvent(queryText, oneDate);
			sut.createQuestion(ev, queryText, betMinimum);

			// invoke System Under Test (sut)
			assertThrows(QuestionAlreadyExist.class, () -> sut.createQuestion(ev, queryText, betMinimum));

		} catch (ParseException | QuestionAlreadyExist e) {
			fail("No problems should arise: ParseException/QuestionaAlreadyExist");
		} finally {
			// Remove the created objects in the database (cascade removing)
			boolean b = testBL.removeEvent(ev);
			assertTrue(b);
		}

	}

	@Test
	@DisplayName("The event has NOT one question with a queryText")
	void createQuestionDATest2() {

		try {
			Date oneDate = sdf.parse("05/10/2022");

			// configure the state of the system (create object in the dabatase)
			ev = testBL.addEvent(queryText, oneDate);

			// invoke System Under Test (sut)
			Question q = sut.createQuestion(ev, queryText, betMinimum);

			// verify the results
			assertNotNull(q);
			assertEquals(queryText, q.getQuestion());
			assertEquals(betMinimum, q.getBetMinimum(), 0);

		} catch (QuestionAlreadyExist | ParseException e) {
			fail("No problems should arise: ParseException/QuestionaAlreadyExist");
		
		} finally {
			// Remove the created objects in the database (cascade removing)
			boolean b = testBL.removeEvent(ev);
			assertTrue(b);
		}
	}
	@Test
	@DisplayName("The user wins combined bet")
	void subirResultadoDATest1() {

		try {
			Date oneDate = sdf.parse("05/10/2022");

			// configure the state of the system (create object in the dabatase)
			ev = testBL.addEvent(queryText, oneDate);
			
			// invoke System Under Test (sut)
			sut.registrarUsuario("pablo","1234");
			User u = new User("pablo","1234");
			Question q = sut.createQuestion(ev, queryText, betMinimum);
			Pronostico p = new Pronostico("Pronostico",2,q);
			q.anadirPronostico(p);
			float cantidad = 3;
			Apuesta a = sut.crearApuesta(u, q, 0, cantidad, ev);
			ArrayList <Apuesta> listaApTemp = new ArrayList <Apuesta>();
			listaApTemp.add(a);
			ApuestaCombinada ac = sut.meterApuestaCombinada (u, listaApTemp);
			sut.subirResultado(0, q.getQuestionNumber());
			assertEquals(sut.getUserById("pablo").getDinero(),6);
			
		} catch (QuestionAlreadyExist | ParseException e) {
			fail("No problems should arise: ParseException/QuestionaAlreadyExist");
		
		} finally {
			// Remove the created objects in the database (cascade removing)
			boolean b = testBL.removeEvent(ev);
			assertTrue(b);
		}
	}
	
	@Test
	@DisplayName("The user wins bet, but not combined bet")
	void subirResultadoDATest2() {

		try {
			Date oneDate = sdf.parse("05/10/2022");

			// configure the state of the system (create object in the dabatase)
			ev = testBL.addEvent(queryText, oneDate);
			
			// invoke System Under Test (sut)
			sut.registrarUsuario("iker","1234");
			User u = new User("iker","1234");
			Question q1 = sut.createQuestion(ev, "Pregunta test 2", 1);
			Question q2 = sut.createQuestion(ev, "Otra Pregunta test 2", 1);
			Pronostico p1 = new Pronostico("Pronostico 2",2,q1);
			Pronostico p2 = new Pronostico("Pronostico 2",2,q2);
			q1.anadirPronostico(p1);
			q2.anadirPronostico(p2);
			float cantidad = 3;
			Apuesta a1 = sut.crearApuesta(u, q1, 0, cantidad, ev);
			Apuesta a2 = sut.crearApuesta(u, q2, 0, cantidad, ev);
			ArrayList <Apuesta> listaApTemp = new ArrayList <Apuesta>();
			listaApTemp.add(a1);
			listaApTemp.add(a2);
			sut.meterApuestaCombinada (u, listaApTemp);
			sut.subirResultado(0, q1.getQuestionNumber());
			assertEquals(sut.getUserById("iker").getDinero(),0);
			
		} catch (QuestionAlreadyExist | ParseException e) {
			fail("No problems should arise: ParseException/QuestionaAlreadyExist");
		
		} finally {
			// Remove the created objects in the database (cascade removing)
			boolean b = testBL.removeEvent(ev);
			assertTrue(b);
		}
	}
	@Test
	@DisplayName("The bet is won twice")
	void subirResultadoDATest3() {

		try {
			Date oneDate = sdf.parse("05/10/2022");

			// configure the state of the system (create object in the dabatase)
			ev = testBL.addEvent(queryText, oneDate);
			
			// invoke System Under Test (sut)
			sut.registrarUsuario("kofi","1234");
			User u = new User("kofi","1234");
			Question q = sut.createQuestion(ev, "Pregunta test 3", betMinimum);
			Pronostico p = new Pronostico("Pronostico 4",2,q);
			q.anadirPronostico(p);
			float cantidad = 3;
			Apuesta a = sut.crearApuesta(u, q, 0, cantidad, ev);
			ArrayList <Apuesta> listaApTemp = new ArrayList <Apuesta>();
			listaApTemp.add(a);
			ApuestaCombinada ac = sut.meterApuestaCombinada (u, listaApTemp);
			sut.subirResultado(0, q.getQuestionNumber());
			sut.subirResultado(0, q.getQuestionNumber());
			//This should lauch exception but is not implemented yet
		} catch (QuestionAlreadyExist | ParseException e) {
			fail("No problems should arise: ParseException/QuestionaAlreadyExist");
		
		} finally {
			// Remove the created objects in the database (cascade removing)
			boolean b = testBL.removeEvent(ev);
			assertTrue(b);
		}
	}
	@Test
	@DisplayName("The combined bet doesn't have bets")
	void subirResultadoDATest4() {
		try {
			Date oneDate = sdf.parse("05/10/2022");

			// configure the state of the system (create object in the dabatase)
			ev = testBL.addEvent(queryText, oneDate);
			
			// invoke System Under Test (sut)
			sut.registrarUsuario("diego","1234");
			User u = new User("diego","1234");
			ArrayList <Apuesta> listaApTemp = new ArrayList <Apuesta>();
			ApuestaCombinada ac = sut.meterApuestaCombinada (u, listaApTemp);
			sut.subirResultado(0, 1);
			assertEquals(sut.getUserById("pablo").getDinero(),6);
			
		} catch (ParseException e) {
			fail("No problems should arise: ParseException/QuestionaAlreadyExist");
		
		} finally {
			// Remove the created objects in the database (cascade removing)
			boolean b = testBL.removeEvent(ev);
			assertTrue(b);
		}
	
	}
	@Test
	@DisplayName("The combined bet doesn't exist")
	void subirResultadoDATest5() {
		try {
			Date oneDate = sdf.parse("05/10/2022");

			// configure the state of the system (create object in the dabatase)
			ev = testBL.addEvent(queryText, oneDate);
			
			// invoke System Under Test (sut)
			sut.registrarUsuario("maite","1234");
			User u = new User("maite","1234");
			sut.subirResultado(0, 1);
			assertEquals(sut.getUserById("pablo").getDinero(),6);
			
		} catch (ParseException e) {
			fail("No problems should arise: ParseException/QuestionaAlreadyExist");
		
		} finally {
			// Remove the created objects in the database (cascade removing)
			boolean b = testBL.removeEvent(ev);
			assertTrue(b);
		}
	
	}
}
