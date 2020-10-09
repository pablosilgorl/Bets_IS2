package dataAccess;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.jws.WebMethod;
import javax.persistence.CascadeType;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import configuration.ConfigXML;
import configuration.UtilDate;
import domain.Admin;
import domain.Apuesta;
import domain.ApuestaCombinada;
import domain.Event;
import domain.Pronostico;
import domain.Question;
import domain.Quiniela;
import domain.QuinielaResuelta;
import domain.User;
import exceptions.EventFinished;
import exceptions.QuestionAlreadyExist;

/**
 * It implements the data access to the objectDb database
 */
public class DataAccess  {
	protected static EntityManager  db;
	protected static EntityManagerFactory emf;
	private static int id_events=200;
	private static int id_q=200;
	private static int id_a=200;
	protected static ConfigXML c;

	public DataAccess(boolean initializeMode)  {
		
		c=ConfigXML.getInstance();
		
		System.out.println("Creating DataAccess instance => isDatabaseLocal: "+c.isDatabaseLocal()+" getDatabBaseOpenMode: "+c.getDataBaseOpenMode());
		String fileName=c.getDbFilename();
		if (initializeMode)
			fileName=fileName+";drop";
		System.out.println("FN: "+fileName);
		if (c.isDatabaseLocal()) {
			  emf = Persistence.createEntityManagerFactory("objectdb:"+fileName);
			  db = emf.createEntityManager();
		} else {
			Map<String, String> properties = new HashMap<String, String>();
			  properties.put("javax.persistence.jdbc.user", c.getUser());
			  properties.put("javax.persistence.jdbc.password", c.getPassword());

			  emf = Persistence.createEntityManagerFactory("objectdb://"+c.getDatabaseNode()+":"+c.getDatabasePort()+"/"+fileName, properties);

			  db = emf.createEntityManager();
    	   }
	}

	public DataAccess()  {	
		 new DataAccess(false);
	}
	
	
	/**
	 * This is the data access method that initializes the database with some events and questions.
	 * This method is invoked by the business logic (constructor of BLFacadeImplementation) when the option "initialize" is declared in the tag dataBaseOpenMode of resources/config.xml file
	 */	
	public void initializeDB(){
		
		db.getTransaction().begin();
		try {

			
		   Calendar today = Calendar.getInstance();
		   
		   int month=today.get(Calendar.MONTH);
		   month+=1;
		   int year=today.get(Calendar.YEAR);
		   if (month==12) { month=0; year+=1;}  
	    
			Event ev1=new Event(1, "Atlético-Athletic", UtilDate.newDate(year,month,17));
			Event ev2=new Event(2, "Eibar-Barcelona", UtilDate.newDate(year,month,17));
			Event ev3=new Event(3, "Getafe-Celta", UtilDate.newDate(year,month,17));
			Event ev4=new Event(4, "Alavés-Deportivo", UtilDate.newDate(year,month,17));
			Event ev5=new Event(5, "Español-Villareal", UtilDate.newDate(year,month,17));
			Event ev6=new Event(6, "Las Palmas-Sevilla", UtilDate.newDate(year,month,17));
			Event ev7=new Event(7, "Malaga-Valencia", UtilDate.newDate(year,month,17));
			Event ev8=new Event(8, "Girona-Leganés", UtilDate.newDate(year,month,17));
			Event ev9=new Event(9, "Real Sociedad-Levante", UtilDate.newDate(year,month,17));
			Event ev10=new Event(10, "Betis-Real Madrid", UtilDate.newDate(year,month,17));

			Event ev11=new Event(11, "Atletico-Athletic", UtilDate.newDate(year,month,1));
			Event ev12=new Event(12, "Eibar-Barcelona", UtilDate.newDate(year,month,1));
			Event ev13=new Event(13, "Getafe-Celta", UtilDate.newDate(year,month,1));
			Event ev14=new Event(14, "Alavés-Deportivo", UtilDate.newDate(year,month,1));
			Event ev15=new Event(15, "Español-Villareal", UtilDate.newDate(year,month,1));
			Event ev16=new Event(16, "Las Palmas-Sevilla", UtilDate.newDate(year,month,1));
			

			Event ev17=new Event(17, "Málaga-Valencia", UtilDate.newDate(year,month,28));
			Event ev18=new Event(18, "Girona-Leganés", UtilDate.newDate(year,month,28));
			Event ev19=new Event(19, "Real Sociedad-Levante", UtilDate.newDate(year,month,28));
			Event ev20=new Event(20, "Betis-Real Madrid", UtilDate.newDate(year,month,28));
			
			Question q1;
			Question q2;
			Question q3;
			Question q4;
			Question q5;
			Question q6;
					
			if (Locale.getDefault().equals(new Locale("es"))) {
				q1=ev1.addQuestion("¿Quién ganará el partido?",1);
				q2=ev1.addQuestion("¿Quién meterá el primer gol?",2);
				q3=ev11.addQuestion("¿Quién ganará el partido?",1);
				q4=ev11.addQuestion("¿Cuántos goles se marcarán?",2);
				q5=ev17.addQuestion("¿Quién ganará el partido?",1);
				q6=ev17.addQuestion("¿Habrá goles en la primera parte?",2);
			}
			else if (Locale.getDefault().equals(new Locale("en"))) {
				q1=ev1.addQuestion("Who will win the match?",1);
				q2=ev1.addQuestion("Who will score first?",2);
				q3=ev11.addQuestion("Who will win the match?",1);
				q4=ev11.addQuestion("How many goals will be scored in the match?",2);
				q5=ev17.addQuestion("Who will win the match?",1);
				q6=ev17.addQuestion("Will there be goals in the first half?",2);
			}			
			else {
				q1=ev1.addQuestion("Zeinek irabaziko du partidua?",1);
				q2=ev1.addQuestion("Zeinek sartuko du lehenengo gola?",2);
				q3=ev11.addQuestion("Zeinek irabaziko du partidua?",1);
				q4=ev11.addQuestion("Zenbat gol sartuko dira?",2);
				q5=ev17.addQuestion("Zeinek irabaziko du partidua?",1);
				q6=ev17.addQuestion("Golak sartuko dira lehenengo zatian?",2);
				
			}
			
			
			db.persist(q1);
			db.persist(q2);
			db.persist(q3);
			db.persist(q4);
			db.persist(q5);
			db.persist(q6);
	
	        
			db.persist(ev1);
			db.persist(ev2);
			db.persist(ev3);
			db.persist(ev4);
			db.persist(ev5);
			db.persist(ev6);
			db.persist(ev7);
			db.persist(ev8);
			db.persist(ev9);
			db.persist(ev10);
			db.persist(ev11);
			db.persist(ev12);
			db.persist(ev13);
			db.persist(ev14);
			db.persist(ev15);
			db.persist(ev16);
			db.persist(ev17);
			db.persist(ev18);
			db.persist(ev19);
			db.persist(ev20);			
			
			db.getTransaction().commit();
			System.out.println("Db initialized");
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * This method creates a question for an event, with a question text and the minimum bet
	 * 
	 * @param event to which question is added
	 * @param question text of the question
	 * @param betMinimum minimum quantity of the bet
	 * @return the created question, or null, or an exception
 	 * @throws QuestionAlreadyExist if the same question already exists for the event
	 */
	public Question createQuestion(Event event, String question, float betMinimum) throws  QuestionAlreadyExist {
		System.out.println(">> DataAccess: createQuestion=> event= "+event+" question= "+question+" betMinimum="+betMinimum);
		
			Event ev = db.find(Event.class, event.getEventNumber());
			
			if (ev.DoesQuestionExists(question)) throw new QuestionAlreadyExist(ResourceBundle.getBundle("Etiquetas").getString("ErrorQueryAlreadyExist"));
			
			db.getTransaction().begin();
			Question q = ev.addQuestion(question, betMinimum);
			//db.persist(q);
			db.persist(ev); // db.persist(q) not required when CascadeType.PERSIST is added in questions property of Event class
							// @OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.PERSIST)
			db.getTransaction().commit();
			return q;
		
	}
	public Question createQuestionQ(Question q) {
			int id_q = Integer.parseInt(DataAccess.c.getQuestionId());
			id_q++;
			DataAccess.c.setQuestionId(Integer.toString(id_q));
			Event ev = db.find(Event.class, q.getEvent().getEventNumber());
			db.getTransaction().begin();
			//db.persist(q);
			q.setQuestionNumber(id_q);
			ev.addQuestionQ(q);
			db.getTransaction().commit();
			return q;
	}
	
	public void createEvent(Date date, String description) {
		int id_e = Integer.parseInt(DataAccess.c.getEventId());
		id_e++;
		DataAccess.c.setEventId(Integer.toString(id_e));
		Event nv = new Event(id_e,description,date);		
	
		db.getTransaction().begin();
		db.persist(nv); 
		db.getTransaction().commit();
		
	}
	
	/**
	 * This method retrieves from the database the events of a given date 
	 * 
	 * @param date in which events are retrieved
	 * @return collection of events
	 */
	/*
	public Vector<Event> getEvents(Date date) {
		System.out.println(">> DataAccess: getEvents");
		Vector<Event> res = new Vector<Event>();	
		TypedQuery<Event> query = db.createQuery("SELECT ev FROM Event ev WHERE ev.eventDate=?1",Event.class);   
		query.setParameter(1, date);
		List<Event> events = query.getResultList();
		System.out.println("aaaaaaaaaaaaaa");
	 	 for (Event ev:events){
	 	   System.out.println(ev.toString());		 
		   res.add(ev);
		  }
	 	return res;
	}
	*/
	@SuppressWarnings("deprecation")
	public Vector<Event> getEvents(Date date) {
		System.out.println(">> DataAccess: getEvents");
		Vector<Event> res = new Vector<Event>();	
		TypedQuery<Event> query = db.createQuery("SELECT ev FROM Event ev ",Event.class);   
		//query.setParameter(1, date);
		List<Event> events = query.getResultList();
	 	 for (Event ev:events){
	 		 if(ev.getEventDate().getDay()==date.getDay()){
	 			 if(ev.getEventDate().getMonth()==date.getMonth()) {
	 				 if(ev.getEventDate().getYear()==date.getYear()) {
				 	   System.out.println(ev.toString());		 
					   res.add(ev);
	 			 	}
	 			 }
	 		 }
	 		 System.out.println(ev.getEventDate().toString());
		  }
	 	return res;
	}
	public Vector<Event> getAllEvents() {
		System.out.println(">> DataAccess: getAllEvents");
		Vector<Event> res = new Vector<Event>();	
		TypedQuery<Event> query = db.createQuery("SELECT ev FROM Event ev ",Event.class);   
		List<Event> events = query.getResultList();
	 	 for (Event ev:events){
	 	   System.out.println(ev.toString());		 
		   res.add(ev);
		  }
	 	return res;
	}
	
	/**
	 * This method retrieves from the database the dates a month for which there are events
	 * 
	 * @param date of the month for which days with events want to be retrieved 
	 * @return collection of dates
	 */
	public Vector<Date> getEventsMonth(Date date) {
		System.out.println(">> DataAccess: getEventsMonth");
		Vector<Date> res = new Vector<Date>();	
		
		Date firstDayMonthDate= UtilDate.firstDayMonth(date);
		Date lastDayMonthDate= UtilDate.lastDayMonth(date);
				
		
		TypedQuery<Date> query = db.createQuery("SELECT DISTINCT ev.eventDate FROM Event ev WHERE ev.eventDate BETWEEN ?1 and ?2",Date.class);   
		query.setParameter(1, firstDayMonthDate);
		query.setParameter(2, lastDayMonthDate);
		List<Date> dates = query.getResultList();
	 	 for (Date d:dates){
	 	   System.out.println(d.toString());		 
		   res.add(d);
		  }
	 	return res;
	}
	public User meterDinero(String id,float dinero) {
		User u=db.find(User.class ,id);
		db.getTransaction().begin();
		u.addDinero(dinero);
		db.getTransaction().commit();
		System.out.println("Ingreso realizado");
		return u;
	}
	public void registrarUsuario(String id,String contrasena) {
		db.getTransaction().begin();
		User u=new User(id, contrasena);
		db.persist(u);
		db.getTransaction().commit();
		System.out.println("Usuario "+id+ " registrado con Ã©xito.");
	}
	public User getUserById(String id) {
		db.getTransaction().begin();
		User u=db.find(User.class, id);
		//db.getTransaction().commit();
		System.out.println(u.getContrasena());
		return u;
	}
	public User getUser(User u) {
		db.getTransaction().begin();
		User us=db.find(User.class, u);
		//db.getTransaction().commit();
		
		return us;
	}
	public Admin getAdminById(String id) {
		db.getTransaction().begin();
		Admin u=db.find(Admin.class, id);
		//db.getTransaction().commit();
		System.out.println(u.getContrasena());
		return u;
	}
	public boolean correctPasswordU(String id, String contrasena){
		
		User u=this.getUserById(id);
		return u.validarContrasena(contrasena);
	}
	public boolean correctPasswordA(String id, String contrasena){
		
		Admin u=this.getAdminById(id);
		return u.validarContrasena(contrasena);
	}
	public void registrarAdmin(String id,String contrasena) {
		db.getTransaction().begin();
		Admin u=new Admin(id, contrasena);
		db.persist(u);
		db.getTransaction().commit();
		System.out.println("Admin: "+id+ " registrado con Ã©xito.");
	}
	public boolean existeUsuario(String id) {
		db.getTransaction().begin();
		User u=db.find(User.class, id);
		if(u==null) {
			return false;
		}
		return true;
	}
	public boolean existeAdmin(String id) {
		db.getTransaction().begin();
		Admin u=db.find(Admin.class, id);
		if(u==null) {
			return false;
		}
		return true;
	}
	

	
	public void close(){
		db.close();
		System.out.println("DataBase closed");
	}

	public void updateQuestion(Question q) {
		Event ev = db.find(Event.class,q.getEvent().getEventNumber());
		db.getTransaction().begin();
		Vector<Question> v = ev.getQuestions();
		for(Question qt : v) {
			if(qt.getQuestionNumber()==q.getQuestionNumber()) {
				qt.setPronosticos(q.getPronosticos());
			}
		}
		ev.setQuestions(v);
		db.getTransaction().commit();
	}
	public Apuesta crearApuesta(User u, Question q, int p, float c, Event e) {
		int id_a = Integer.parseInt(DataAccess.c.getApostarId());
		id_a++;
		DataAccess.c.setApostarId(Integer.toString(id_a));
		Apuesta ap = new Apuesta(id_a, u, q, p, c, e);
		db.getTransaction().begin();
		db.persist(ap);
		db.getTransaction().commit();
		return ap;
	}
	public ApuestaCombinada meterApuestaCombinada (User u, ArrayList <Apuesta> listaApTemp){
		int id_ac= Integer.parseInt(DataAccess.c.getApostarId());
		id_ac++;
		DataAccess.c.setApuestaCId(Integer.toString(id_a));
		int i=0;
		for (Apuesta ap: listaApTemp) {
			ap= crearApuesta(ap.getUser(), ap.getQuestion(), ap.getPronostico(), ap.getCantidadapostada(), ap.getEvent());
			listaApTemp.set(i, ap);
			i++;
		}
		ApuestaCombinada ac= new ApuestaCombinada(id_ac, listaApTemp, u);
		db.getTransaction().begin();
		db.persist(ac);
		db.getTransaction().commit();
		return ac;
	}
	
	/*public void subirResultado(int nPronostico,int q) {
		//db.getTransaction().begin();
		TypedQuery<Apuesta> query = db.createQuery("SELECT  a FROM Apuesta a WHERE a.nPronostico = ?1 AND a.nQuestion=?2",Apuesta.class);   
		query.setParameter(1, nPronostico); //id
		query.setParameter(2, q);
		float multi =0;
		List<Apuesta> ap = query.getResultList();
		for(Apuesta a:ap) {
			Question pregunta=a.getQuestion();
			Pronostico ps= pregunta.getPronosticos().get(nPronostico);
			multi=ps.getMultiplicador();      			
			meterDinero(a.getUser().getId(), multi*a.getCantidadapostada());			
		}
	*/	
		
		public void subirResultado(int nPronostico,int q) {
			//db.getTransaction().begin();
			TypedQuery<ApuestaCombinada> query = db.createQuery("SELECT  a FROM ApuestaCombinada a",ApuestaCombinada.class);   
			
			float multi =0;
			List<ApuestaCombinada> apc = query.getResultList();
			for(ApuestaCombinada ac:apc) {
				List<Apuesta> ap=ac.getListaApuesta();
				for(Apuesta a:ap) {
					
					if(a.getQuestion().getQuestionNumber()==q) {
						if(a.getPronostico()==nPronostico) {
							db.getTransaction().begin();
							a.setGanada();
							ac.anadirApuestaGanada();
							db.getTransaction().commit();
							if(ac.ganada()) {
								meterDinero(ac.getUsuario().getId(), ac.gananciaPotencial());
							}
						}
					}
				}
			}
					
	}
	public Quiniela meterQuiniela (ArrayList <Event> listaeventos) {
		int id_q= Integer.parseInt(DataAccess.c.getQuinielaId());
		id_q++;
		DataAccess.c.setQuinielaId(Integer.toString(id_q));
		Quiniela q= new Quiniela(id_q, listaeventos);
		db.getTransaction().begin();
		db.persist(q);
		db.getTransaction().commit();
		return q;
	}
	public void guardarQuiniela(User u, Quiniela q, int[] res) {
		int id= Integer.parseInt(DataAccess.c.getQuiRellId());
		id++;
		DataAccess.c.setQuiRellId(Integer.toString(id));
		QuinielaResuelta qr=new QuinielaResuelta(id,q,res,u);
		meterDinero(u.getId(), -1);
		db.getTransaction().begin();
		db.persist(qr);
		db.getTransaction().commit();
		return;
		
	}
	
	public ArrayList<Quiniela> sacarQuinielas () {
		TypedQuery<Quiniela> query = db.createQuery("SELECT q  FROM Quiniela q",Quiniela.class);
		
		ArrayList<Quiniela> q = (ArrayList<Quiniela>) query.getResultList();
		return q;
	}
	public void subirResultadoQuiniela(Quiniela quinielaActual,int []resultadoQuiniela) {
		TypedQuery<QuinielaResuelta> query = db.createQuery("SELECT q  FROM QuinielaResuelta q WHERE q.quiniela=?1",QuinielaResuelta.class);
		query.setParameter(1, quinielaActual); 
		
		ArrayList<QuinielaResuelta> q = (ArrayList<QuinielaResuelta>) query.getResultList();
		for(QuinielaResuelta r:q) {
			int respuestascorrectas=r.compararResultados(resultadoQuiniela);
			System.out.println("Respuestas correctas: "+respuestascorrectas);
			if(15==respuestascorrectas) {
				meterDinero(r.getUser().getId(),1000);
			}
			if(14==respuestascorrectas) {
				meterDinero(r.getUser().getId(),100);
			}
			if(13==respuestascorrectas) {
				meterDinero(r.getUser().getId(),10);
			}
			if(12==respuestascorrectas) {
				meterDinero(r.getUser().getId(),1);
			}
		}
	}
	
}
