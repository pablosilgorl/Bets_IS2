package gui;

import businessLogic.BLFacade;
import configuration.UtilDate;

import com.toedter.calendar.JCalendar;
import domain.Question;
import domain.Event;
import domain.Pronostico;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.text.DateFormat;
import java.util.*;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;


public class CreatePronosticoGUI extends JFrame {
	private static final long serialVersionUID = 1L;

	private final JLabel jLabelEventDate = new JLabel("EventDate");
	private final JLabel jLabelQueries = new JLabel("Queries"); 
	private final JLabel jLabelEvents = new JLabel("Events"); 
	private final JLabel lblError = new JLabel();
	private JButton jButtonClose = new JButton("Close");
	private JButton btnAnadirPronostico = new JButton("CreatePronosticoGUI.btnNewButton.text");
	// Code for JCalendar
	private JCalendar jCalendar1 = new JCalendar();
	private Calendar calendarMio = null;
	private JScrollPane scrollPaneEvents = new JScrollPane();
	private JScrollPane scrollPaneQueries = new JScrollPane();

	private JTable tableEvents= new JTable();
	private JTable tableQueries = new JTable();

	private DefaultTableModel tableModelEvents;
	private DefaultTableModel tableModelQueries;

	
	private String[] columnNamesEvents = new String[] {
			"EventN", 
			"Event", 

	};
	private String[] columnNamesQueries = new String[] {
			"QueryN", 
			"Query"

	};
	private JTextField textPronostico;
	
	private Vector<Question> preguntasdelevento=null;
	Question selectedquestion=null;
	private final JTextField textFieldMul = new JTextField();

	public CreatePronosticoGUI()
	{
		textFieldMul.setText(""); //$NON-NLS-1$ //$NON-NLS-2$
		textFieldMul.setBounds(274, 355, 34, 20);
		textFieldMul.setColumns(10);
		try
		{
			jbInit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	
	private void jbInit() throws Exception
	{

		this.getContentPane().setLayout(null);
		this.setSize(new Dimension(700, 500));
		this.setTitle("Create Pronostico");

		jLabelEventDate.setBounds(new Rectangle(40, 15, 140, 25));
		jLabelQueries.setBounds(40, 211, 406, 14);
		jLabelEvents.setBounds(295, 19, 259, 16);

		this.getContentPane().add(jLabelEventDate, null);
		this.getContentPane().add(jLabelQueries);
		this.getContentPane().add(jLabelEvents);

		jButtonClose.setBounds(new Rectangle(274, 419, 130, 30));

		jButtonClose.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				jButton2_actionPerformed(e);
			}
		});

		this.getContentPane().add(jButtonClose, null);


		jCalendar1.setBounds(new Rectangle(40, 50, 225, 150));


		// Code for JCalendar
		this.jCalendar1.addPropertyChangeListener(new PropertyChangeListener()
		{
			public void propertyChange(PropertyChangeEvent propertychangeevent)
			{

				if (propertychangeevent.getPropertyName().equals("locale"))
				{
					jCalendar1.setLocale((Locale) propertychangeevent.getNewValue());
				}
				else if (propertychangeevent.getPropertyName().equals("calendar"))
				{
					calendarMio = (Calendar) propertychangeevent.getNewValue();
					DateFormat dateformat1 = DateFormat.getDateInstance(1, jCalendar1.getLocale());
					jCalendar1.setCalendar(calendarMio);
					Date firstDay=UtilDate.trim(new Date(jCalendar1.getCalendar().getTime().getTime()));



					try {
						tableModelEvents.setDataVector(null, columnNamesEvents);
						tableModelEvents.setColumnCount(3); // another column added to allocate ev objects

						BLFacade facade=PanelAdminGUI.getBusinessLogic();

						Vector<domain.Event> events=facade.getEvents(firstDay);

						if (events.isEmpty() ) jLabelEvents.setText("NoEvents"+ ": "+dateformat1.format(calendarMio.getTime()));
						else jLabelEvents.setText("Events"+ ": "+dateformat1.format(calendarMio.getTime()));
						for (domain.Event ev:events){
							Vector<Object> row = new Vector<Object>();

							System.out.println("Events "+ev);

							row.add(ev.getEventNumber());
							row.add(ev.getDescription());
							row.add(ev); // ev object added in order to obtain it with tableModelEvents.getValueAt(i,2)
							tableModelEvents.addRow(row);		
						}
						tableEvents.getColumnModel().getColumn(0).setPreferredWidth(25);
						tableEvents.getColumnModel().getColumn(1).setPreferredWidth(268);
						tableEvents.getColumnModel().removeColumn(tableEvents.getColumnModel().getColumn(2)); // not shown in JTable
					} catch (Exception e1) {

						jLabelQueries.setText(e1.getMessage());
					}

				}
				CreateQuestionGUI.paintDaysWithEvents(jCalendar1);
			} 
		});

		this.getContentPane().add(jCalendar1, null);
		
		scrollPaneEvents.setBounds(new Rectangle(292, 50, 346, 150));
		scrollPaneQueries.setBounds(new Rectangle(40, 228, 406, 116));
		tableEvents.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int i=tableEvents.getSelectedRow();
				domain.Event ev=(domain.Event)tableModelEvents.getValueAt(i,2); // obtain ev object
				Vector<Question> queries=ev.getQuestions();
				preguntasdelevento = ev.getQuestions();
				tableModelQueries.setDataVector(null, columnNamesQueries);

				if (queries.isEmpty())
					jLabelQueries.setText("NoQueries"+": "+ev.getDescription());
				else 
					jLabelQueries.setText("SelectedEvent"+" "+ev.getDescription());

				for (domain.Question q:queries){
					Vector<Object> row = new Vector<Object>();

					row.add(q.getQuestionNumber());
					row.add(q.getQuestion());
					tableModelQueries.addRow(row);	
				}
				tableQueries.getColumnModel().getColumn(0).setPreferredWidth(25);
				tableQueries.getColumnModel().getColumn(1).setPreferredWidth(268);
			}
		});

		scrollPaneEvents.setViewportView(tableEvents);
		tableModelEvents = new DefaultTableModel(null, columnNamesEvents);

		tableEvents.setModel(tableModelEvents);
		tableEvents.getColumnModel().getColumn(0).setPreferredWidth(25);
		tableEvents.getColumnModel().getColumn(1).setPreferredWidth(268);


		scrollPaneQueries.setViewportView(tableQueries);
		tableModelQueries = new DefaultTableModel(null, columnNamesQueries);

		tableQueries.setModel(tableModelQueries);
		tableQueries.getColumnModel().getColumn(0).setPreferredWidth(25);
		tableQueries.getColumnModel().getColumn(1).setPreferredWidth(268);

		tableQueries.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int i=tableQueries.getSelectedRow();
				int qn =(int)tableModelQueries.getValueAt(i,0); 
				for (Question p:preguntasdelevento){
					if(p.getQuestionNumber()==qn){
						selectedquestion=p;
					}else {
						System.out.println("Pregunta que no es: "+p.getQuestionNumber()+" / "+qn);
					}
				}
				System.out.println("Pregunta seleccionada:");
				System.out.println(selectedquestion);
				
				tableQueries.getColumnModel().getColumn(0).setPreferredWidth(25);
				tableQueries.getColumnModel().getColumn(1).setPreferredWidth(268);
			}
			
		});
		
		
		this.getContentPane().add(scrollPaneEvents, null);
		this.getContentPane().add(scrollPaneQueries, null);
		
		textPronostico = new JTextField();
		textPronostico.setText("");
		textPronostico.setBounds(40, 355, 214, 20);
		getContentPane().add(textPronostico);
		textPronostico.setColumns(10);
		
		 //$NON-NLS-1$ //$NON-NLS-2$
		btnAnadirPronostico.setBounds(329, 355, 117, 23);
		getContentPane().add(btnAnadirPronostico);
		
		getContentPane().add(textFieldMul);
		
		lblError.setForeground(Color.RED);
		lblError.setBounds(516, 264, 46, 14);
		getContentPane().add(lblError);
		btnAnadirPronostico.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String inputpronostico = textPronostico.getText();
				if(inputpronostico.length()>0 && selectedquestion!=null) {
					boolean check = true;
					for(int i=0;i<textFieldMul.getText().length();i++) {
						int ascii =(int) textFieldMul.getText().charAt(i);
						if(ascii>57 && ascii<48 && ascii==46) {
							check=false;
						}
					}
					float m = Float.parseFloat(textFieldMul.getText());
					if(check) {
						if(m>0) {
							Pronostico pr = new Pronostico(inputpronostico,m, selectedquestion);
							selectedquestion.anadirPronostico(pr);
							//System.out.println(pr.getPronostico());
						}else {
							lblError.setText("El multiplicador dese ser > 1");
						}
					}else {
						lblError.setText("El multiplicador debe ser un n�mero");
					}
					if(check) {
						BLFacade facade = PanelAdminGUI.getBusinessLogic();
						facade.updateQuestion(selectedquestion);
					}
				}
			}
		});
	

	}

	private void jButton2_actionPerformed(ActionEvent e) {
		this.setVisible(false);
	}
}
