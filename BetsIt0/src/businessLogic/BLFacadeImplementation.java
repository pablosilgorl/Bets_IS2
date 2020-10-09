package businessLogic;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.jws.WebMethod;
import javax.jws.WebService;

import configuration.ConfigXML;
import dataAccess.DataAccess;
import domain.Question;
import domain.Quiniela;
import domain.User;
import domain.Admin;
import domain.Apuesta;
import domain.ApuestaCombinada;
import domain.Event;
import domain.Pronostico;
import exceptions.EventFinished;
import exceptions.QuestionAlreadyExist;

/**
 * It implements the business logic as a web service.
 */
@WebService(endpointInterface = "businessLogic.BLFacade")
public class BLFacadeImplementation  implements BLFacade {

	public BLFacadeImplementation()  {		
		System.out.println("Creating BLFacadeImplementation instance");
		ConfigXML c=ConfigXML.getInstance();
		
		if (c.getDataBaseOpenMode().equals("initialize")) {
			DataAccess dbManager=new DataAccess(c.getDataBaseOpenMode().equals("initialize"));
			dbManager.initializeDB();
			dbManager.close();
			}
		
	}
	

	/**
	 * This method creates a question for an event, with a question text and the minimum bet
	 * 
	 * @param event to which question is added
	 * @param question text of the question
	 * @param betMinimum minimum quantity of the bet
	 * @return the created question, or null, or an exception
	 * @throws EventFinished if current data is after data of the event
 	 * @throws QuestionAlreadyExist if the same question already exists for the event
	 */
	@WebMethod
	public User addDinero(String id,float dinero) {
		DataAccess db=new DataAccess();
		User u= db.meterDinero(id, dinero);
		db.close();
		return u;
	}
   @WebMethod
   public Question createQuestion(Event event, String question, float betMinimum) throws EventFinished, QuestionAlreadyExist{
	   
	    //The minimum bed must be greater than 0
	    DataAccess dBManager=new DataAccess();
		Question qry=null;
		
	    
		if(new Date().compareTo(event.getEventDate())>0)
			throw new EventFinished(ResourceBundle.getBundle("Etiquetas").getString("ErrorEventHasFinished"));
				
		
		 qry=dBManager.createQuestion(event,question,betMinimum);		

		dBManager.close();
		
		return qry;
   };
   @WebMethod
   public Question createQuestionQ(Question q){
	   
	    //The minimum bed must be greater than 0
	    DataAccess dBManager=new DataAccess();	
	    Question qt = dBManager.createQuestionQ(q);
		dBManager.close();
		return qt;
   };
  @WebMethod
   public void updateQuestion(Question q) {
           DataAccess d=new DataAccess();
           d.updateQuestion(q);
           d.close();
       }
   @WebMethod
   public boolean existeUsuario(String id) {
	   boolean res;
	   DataAccess d=new DataAccess(true);
	   res=d.existeUsuario(id);
	   d.close();
	   return res;
   }
   @WebMethod
   public boolean existeAdmin(String id) {
	   boolean res;
	   DataAccess d=new DataAccess(true);
	   res=d.existeAdmin(id);
	   d.close();
	   return res;
   }
   @WebMethod
   public void createEvent(Date date, String descrition){
	   
	    //The minimum bed must be greater than 0
	    DataAccess dBManager=new DataAccess(true);

	    dBManager.createEvent(date,descrition);		

		dBManager.close();
		
  };
	
	/**
	 * This method invokes the data access to retrieve the events of a given date 
	 * 
	 * @param date in which events are retrieved
	 * @return collection of events
	 */
    @WebMethod	
	public Vector<Event> getEvents(Date date)  {
		DataAccess dbManager=new DataAccess();
		Vector<Event>  events=dbManager.getEvents(date);
		dbManager.close();
		return events;
	}
    
    @WebMethod	
	public Vector<Event> getAllEvents()  {
		DataAccess dbManager=new DataAccess();
		Vector<Event>  events=dbManager.getAllEvents();
		dbManager.close();
		return events;
	}
    
	/**
	 * This method invokes the data access to retrieve the dates a month for which there are events
	 * 
	 * @param date of the month for which days with events want to be retrieved 
	 * @return collection of dates
	 */
	@WebMethod public Vector<Date> getEventsMonth(Date date) {
		DataAccess dbManager=new DataAccess();
		Vector<Date>  dates=dbManager.getEventsMonth(date);
		dbManager.close();
		return dates;
	}
	
	
	

	/**
	 * This method invokes the data access to initialize the database with some events and questions.
	 * It is invoked only when the option "initialize" is declared in the tag dataBaseOpenMode of resources/config.xml file
	 */	
    @WebMethod	
	 public void initializeBD(){
		DataAccess dBManager=new DataAccess();
		dBManager.initializeDB();
		dBManager.close();
	}
	@WebMethod public User getUserById(String id) {
		DataAccess d= new DataAccess();
		User u=d.getUserById(id);
		d.close();
		return u;
	}
	@WebMethod public Admin getAdminById(String id) {
		DataAccess d= new DataAccess();
		Admin u=d.getAdminById(id);
		d.close();
		return u;
	}
	
	
	

	/**
	 * This method invokes the data access to initialize the database with some events and questions.
	 * It is invoked only when the option "initialize" is declared in the tag dataBaseOpenMode of resources/config.xml file
	 */	
	@WebMethod
	public void registrarUsuario(String id,String contrasena) {
    	DataAccess d=new DataAccess();
    	d.registrarUsuario(id,contrasena);
    	System.out.println("Registrado"+id);
    	d.close();
	}
	@WebMethod
	public void registrarAdmin(String id,String contrasena) {
    	DataAccess d=new DataAccess();
    	d.registrarAdmin(id,contrasena);
    	System.out.println("Registrado"+id);
    	d.close();
	}

	@WebMethod
	public boolean compararContrasenasA(String id,String contrasena) {
		DataAccess d=new DataAccess();
		boolean res=d.correctPasswordA(id, contrasena);
		d.close();
		return res;
	}
	@WebMethod
	public boolean compararContrasenasU(String id,String contrasena) {
		DataAccess d=new DataAccess();
		boolean res=d.correctPasswordU(id, contrasena);
		d.close();
		return res;
	}
	@WebMethod 
	public User conseguirUser(User u) {
		DataAccess d=new DataAccess();
		User us=d.getUser(u);
		d.close();
		return us;
	}
	public Apuesta crearApuesta(User u, Question q, int p, float c, Event e){
		DataAccess d =new DataAccess();
		Apuesta ap = d.crearApuesta(u, q, p, c, e);
		d.close();
		return ap;
	}
	public void subirResultado (Event e, Question q, int np) {
		
	}
	@WebMethod
	public ApuestaCombinada meterApuestaCombinada (User u, ArrayList <Apuesta> listaApTemp) {
		DataAccess d = new DataAccess();
		ApuestaCombinada ac= d.meterApuestaCombinada (u, listaApTemp);		
		d.close();
		return ac;
	}
	@WebMethod
	public Quiniela meterQuiniela (ArrayList <Event> listaeventos) {
		DataAccess d = new DataAccess();
		Quiniela ac= d.meterQuiniela (listaeventos);		
		d.close();
		return ac;
	}
	@WebMethod
	public List<Quiniela> sacarQuinielas () {
		DataAccess d = new DataAccess();
		List<Quiniela> q= d.sacarQuinielas();		
		d.close();
		return q;
	}
	@WebMethod
	public void guardarQuiniela(User u, Quiniela q, int[] res) {
		DataAccess d=new DataAccess();
		d.guardarQuiniela(u, q, res);
		
		d.close();
		
	}
	@WebMethod
	 public void subirResultadoQuiniela(Quiniela quinielaActual,int []resultadoQuiniela) {
		DataAccess d=new DataAccess();
		d.subirResultadoQuiniela(quinielaActual,resultadoQuiniela);
		d.close();
	}
}

