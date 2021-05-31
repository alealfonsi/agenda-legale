package gui;

import static org.junit.Assert.assertNotNull;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.runtime.ProcessInstance;

import com.calendarfx.model.Entry;
import com.calendarfx.model.Interval;
import com.calendarfx.view.CalendarView;

import calendar.ConcreteCalendarEvent;
import cliente.PersonaFisica;
import cliente.PersonaGiuridica;
import entities.Adempimento;
import entities.CalendarEvent;
import entities.Client;
import entities.Procedimento;
import entities.Udienza;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import udienza.ConcreteAdempimento;
import udienza.ConcreteProcedimento;
import udienza.UdienzaDateComparator;
import udienza.UdienzaTribunale;
import udienza.UdienzaTribunaleFactory;
import user_task_handlers.TaskHandlerAggiungereAdempimento;
import user_task_handlers.TaskHandlerCreazioneNuovoAdempimento;
import user_task_handlers.TaskHandlerImpostarePromemoriaAdempimento;
import user_task_handlers.TaskHandlerTipoRinvio;
import user_task_handlers.UserTaskHandler;

public class RinvioHandler implements EventHandler<ActionEvent>{
	
	Udienza udienza;
	CalendarView calendarView;
	ProcessEngine processEngine;
	String filename = "C:\\Users\\aalfo\\eclipse-workspace\\Attivit‡Progettuale\\src\\main\\resources\\diagrams\\RinvioUdienza.bpmn";
	ProcessInstance processInstance;
	Boolean rinvio_ufficio;
	Calendar new_date;
	Boolean aggiungere_adempimento = false;
	Boolean aggiunto = false;
	
	public RinvioHandler(CalendarView calendarView) {
		super();
		this.calendarView = calendarView;
	}

	@Override
	public void handle(ActionEvent event) {
		selectUdienza();
		if(this.udienza == null) {
			return;
		}
		
		try {
			startProcess();
			
			TypeAndDate temp = getTipoRinvioAndDate();
			this.new_date = temp.date;
			this.rinvio_ufficio = temp.rinvio;
			TaskHandlerTipoRinvio task_handler_1 = new TaskHandlerTipoRinvio();
			task_handler_1.execute(processEngine.getTaskService(), processEngine.getRuntimeService(), processInstance.getProcessDefinitionId(),
					rinvio_ufficio, udienza, new_date);
			
			if(rinvio_ufficio == true) {
				addUdienzaToCalendarView();
				return;
			}
			
			while(true) {
				getAggiungereAdempimento();
				TaskHandlerAggiungereAdempimento task_handler_2 = new TaskHandlerAggiungereAdempimento();
				task_handler_2.execute(processEngine.getTaskService(), processEngine.getRuntimeService(), processInstance.getProcessDefinitionId(),
						aggiungere_adempimento);
				
				if(aggiungere_adempimento == false) {
					addUdienzaToCalendarView();
					break;
				}
				
				Adempimento new_adempimento = getAdempimentoDialog();
				TaskHandlerCreazioneNuovoAdempimento task_handler_3 = new TaskHandlerCreazioneNuovoAdempimento();
				task_handler_3.execute(processEngine.getTaskService(), processEngine.getRuntimeService(), processEngine.getHistoryService(),
						processInstance.getProcessDefinitionId(), new_adempimento.getDate().get(Calendar.DAY_OF_MONTH),
						new_adempimento.getDate().get(Calendar.HOUR_OF_DAY), new_adempimento.getName(),
						new_adempimento.getCode(), new_adempimento.getNote());
				this.aggiunto = true;
				
				PromemoriaGUI promemoria = impostarePromemoriaDialog(new_adempimento);
				TaskHandlerImpostarePromemoriaAdempimento task_handler_4 = new TaskHandlerImpostarePromemoriaAdempimento();
				task_handler_4.execute(processInstance.getProcessDefinitionId(), promemoria.getImpostare(),
						processEngine.getRuntimeService(), processEngine.getTaskService(),
						promemoria.getDays(), promemoria.getHour_of_day());
			}
			if(aggiunto == false) {
				return;
			}
			addAdempimentiToCalendarView();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public PromemoriaGUI impostarePromemoriaDialog(Adempimento evento) throws Exception {

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Impostare promemoria?");
		String s; 
		s = "Si desidera impostare un promemoria mail per l'adempimento: " +
						evento.getName() + "?";
		
		alert.setContentText(s);
		 
		Optional<ButtonType> result = alert.showAndWait();
		 
		if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
			PromemoriaGUI out = getPromemoriaDialog();
		    return out;
		}
		
		PromemoriaGUI out = new PromemoriaGUI();
		out.setImpostare(false);
		out.setDays(0);
		out.setHour_of_day(0);
		return out;
	}
	
	public PromemoriaGUI getPromemoriaDialog() {
    	Dialog<PromemoriaGUI> dialog = new Dialog<>();
		dialog.setTitle("Impostazione promemoria");
		dialog.setHeaderText("Quanti giorni prima dell'evento si vuole ricevere il promemoria?");
		dialog.setResizable(true);
		 
		Label label1 = new Label("Giorni: ");
		Label label2 = new Label("Orario: ");
		
		Spinner<Integer> spinner_days = new Spinner<>((int) 0, (int) 100, (int) 1);
		Spinner<Integer> spinner_hour_of_day = new Spinner<>((int) 0, (int) 24, (int) 12);
		         
		GridPane grid = new GridPane();
		grid.add(label1, 1, 1);
		grid.add(spinner_days, 2, 1);
		grid.add(label2, 1, 2);
		grid.add(spinner_hour_of_day, 2, 2);
		dialog.getDialogPane().setContent(grid);
		         
		ButtonType buttonTypeOk = new ButtonType("Okay", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
		 
		dialog.setResultConverter(new Callback<ButtonType, PromemoriaGUI>() {
		    @Override
		    public PromemoriaGUI call(ButtonType b) {
		 
		        if (b == buttonTypeOk) {
		 
		        	PromemoriaGUI out = new PromemoriaGUI();
		        	
		        	out.setDays(spinner_days.getValue());
		        	out.setHour_of_day(spinner_hour_of_day.getValue());
		        	out.setImpostare(true);
		        	
		            return out;
		        }
		 
		        return null;
		    }
		});
		         
		Optional<PromemoriaGUI> result = dialog.showAndWait();
		         
		if (result.isPresent()) {
		 
		    System.out.println("Result: " + result.get());
		}
		
		return result.get();
    }
	
	public void selectUdienza() {
		TextInputDialog dialog = new TextInputDialog("");
		dialog.setTitle("Cerca udienza");
		dialog.setHeaderText("Cerca per parti.");
		 
		Optional<String> result = dialog.showAndWait();
		String entered = "none.";
		 
		if (result.isPresent()) {
		 
		    entered = result.get();
		}
		
		List<Entry<?>> entries = calendarView.getCalendars().get(0).findEntries(entered);
		
		if((entries == null) || (entries.isEmpty())) {
			Alert alert = new Alert(AlertType.ERROR);
		    alert.setTitle("Attenzione!");
		    String s = "Non Ë stato selezionato alcun evento per cui visualizzare un udienza.";
		    alert.setContentText(s);
		    alert.showAndWait();
		    return;
		}
		
		for(Entry entry : entries) {
			if(entry.getUserObject() instanceof Udienza) {
				this.udienza = (Udienza) entry.getUserObject();
				break;
			}
		}
	}
	
	public void startProcess() throws FileNotFoundException {
		this.processEngine = ProcessEngines.getDefaultProcessEngine();
		
		RepositoryService repositoryService = processEngine.getRepositoryService();
		repositoryService.createDeployment().addInputStream("rinvioUdienza.bpmn20.xml",
				new FileInputStream(filename)).deploy();
		RuntimeService runtimeService = processEngine.getRuntimeService();
		Map<String, Object> variableMap = new HashMap<String, Object>();
		ArrayList<Adempimento> adempimenti = new ArrayList<Adempimento>();
		variableMap.put("adempimento", adempimenti);
		processInstance = runtimeService.startProcessInstanceByKey("rinvioUdienza", variableMap);
		System.out.println("id " + processInstance.getId() + " "
				+ processInstance.getProcessDefinitionId());
	}
	
	public TypeAndDate getTipoRinvioAndDate() {
		Dialog<TypeAndDate> dialog = new Dialog<TypeAndDate>();
		dialog.setTitle("Rinvio udienza");
		dialog.setHeaderText("Seleziona il tipo di rinvio e la nuova data dell'udienza");
		dialog.setResizable(true);
		 
		Label label1 = new Label("Giorno: ");
		Label label2 = new Label("Ora: ");
		Label label3 = new Label("Minuti: ");
		
		DatePicker date_picker = new DatePicker();
		Spinner<Integer> spinner_ora = new Spinner<Integer>((int) 8, (int) 20, (int) 8);
		Spinner<Integer> spinner_minuti = new Spinner<Integer>((int) 0, (int) 59, (int) 0);		
		         
		GridPane grid = new GridPane();
		grid.add(label1, 1, 1);
		grid.add(date_picker, 2, 1);
		grid.add(label2, 1, 2);
		grid.add(spinner_ora, 2, 2);
		grid.add(label3, 1, 3);
		grid.add(spinner_minuti, 2, 3);
		dialog.getDialogPane().setContent(grid);
		         
		ButtonType buttonTypeTrue = new ButtonType("Rinvio d'ufficio", ButtonData.YES);
		ButtonType buttonTypeFalse = new ButtonType("Per altro adempimento", ButtonData.NO);
		dialog.getDialogPane().getButtonTypes().add(buttonTypeTrue);
		dialog.getDialogPane().getButtonTypes().add(buttonTypeFalse);
		 
		dialog.setResultConverter(new Callback<ButtonType, TypeAndDate>() {
		    @Override
		    public TypeAndDate call(ButtonType b) {
		    	
		    	TypeAndDate out = new TypeAndDate();
		    	out.date = Calendar.getInstance();
	        	out.date.set(date_picker.getValue().getYear(), 
	        			date_picker.getValue().getMonthValue(),
	        			date_picker.getValue().getDayOfMonth(),
	        			spinner_ora.getValue(),
	        			spinner_minuti.getValue());
	        	
		        if (b == buttonTypeTrue) {
		        	out.rinvio = true;
		        }
		        
		        if(b == buttonTypeFalse) {
		        	out.rinvio = false;
		        }
		 
		        return out;
		    }
		});
	
	Optional<TypeAndDate> result = dialog.showAndWait();
    
	if (result.isPresent()) {
	 
	    System.out.println("Result: " + result.get());
	}
	return result.get();
	}
	
	public void addUdienzaToCalendarView() throws Exception {
		HistoryService historyService = processEngine.getHistoryService();
		List<HistoricVariableInstance> vars = historyService.createHistoricVariableInstanceQuery().variableName("udienza").list();
		Udienza new_udienza = (Udienza) vars.get(vars.size() - 1).getValue();
		Entry entry = new Entry(new_udienza.getParti(),
				new Interval(new_udienza.getZonedDateTime().toLocalDate(), 
						LocalTime.ofInstant(new_udienza.getDate().toInstant(), ZoneId.systemDefault()),
						new_udienza.getZonedDateTime().toLocalDate(),
						LocalTime.ofInstant(new_udienza.getDate().toInstant().plusSeconds(3600), ZoneId.systemDefault())));
		entry.setUserObject(new_udienza);
		calendarView.getCalendars().get(0).addEntry(entry);
	}
	
	public void addAdempimentiToCalendarView() {
		HistoryService historyService = processEngine.getHistoryService();
		List<HistoricVariableInstance> vars = historyService.createHistoricVariableInstanceQuery().variableName("adempimento").list();
		ArrayList<Adempimento> adempimenti = (ArrayList<Adempimento>) vars.get(vars.size() - 1).getValue();
		for(Adempimento adempimento : adempimenti) {
			Entry entry = new Entry(adempimento.getName(),
					new Interval(adempimento.getZonedDateTime().toLocalDate(), 
							LocalTime.ofInstant(adempimento.getDate().toInstant(), ZoneId.systemDefault()),
							adempimento.getZonedDateTime().toLocalDate(),
							LocalTime.ofInstant(adempimento.getDate().toInstant().plusSeconds(3600), ZoneId.systemDefault())));
			entry.setUserObject(adempimento);
			calendarView.getCalendars().get(0).addEntry(entry);
		}
	}
	
	public void getAggiungereAdempimento() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Aggiungere adempimento?");
		String s = "Si vuole aggiungere un adempimento per la nuova udienza?";
		alert.setContentText(s);
		 
		Optional<ButtonType> result = alert.showAndWait();
		 
		if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
		    this.aggiungere_adempimento = true;
		}
		else {
			this.aggiungere_adempimento = false;
		}
		
	}
	
	public Adempimento getAdempimentoDialog() {
		Dialog<Adempimento> dialog = new Dialog<Adempimento>();
		dialog.setTitle("Nuovo adempimento");
		dialog.setHeaderText("Inserisci i dati per il nuovo adempimento");
		dialog.setResizable(true);
		 
		Label label1 = new Label("Giorni prima dell'udienza: ");
		Label label2 = new Label("Ora: ");
		Label label3 = new Label("Nome: ");
		Label label4 = new Label("Codice: ");
		Label label5 = new Label("Note: ");
		
		Spinner<Integer> spinner_days = new Spinner<Integer>((int) 0, (int) 120, (int) 7);
		Spinner<Integer> spinner_ora = new Spinner<Integer>((int) 8, (int) 20, (int) 8);
		TextField text1 = new TextField();
		TextField text2 = new TextField();
		TextField text3 = new TextField();
		         
		GridPane grid = new GridPane();
		grid.add(label1, 1, 1);
		grid.add(spinner_days, 2, 1);
		grid.add(label2, 1, 2);
		grid.add(spinner_ora, 2, 2);
		grid.add(label3, 1, 3);
		grid.add(text1, 2, 3);
		grid.add(label4, 1, 4);
		grid.add(text2, 2, 4);
		grid.add(label5, 1, 5);
		grid.add(text3, 2, 5);
		dialog.getDialogPane().setContent(grid);
		         
		ButtonType buttonTypeOk = new ButtonType("Fatto", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
		 
		dialog.setResultConverter(new Callback<ButtonType, Adempimento>() {
		    @Override
		    public Adempimento call(ButtonType b) {
		    	
		    	Calendar date = Calendar.getInstance();
		    	date.set(Calendar.DAY_OF_MONTH, spinner_days.getValue());
		    	date.set(Calendar.HOUR_OF_DAY, spinner_ora.getValue());
		    	Adempimento out = new ConcreteAdempimento(new ConcreteCalendarEvent(), date);
		    	out.setName(text1.getText());
		    	out.setCode(text2.getText());
		    	out.setNote(text3.getText());
		    	
		        return out;
		    }
		});
	
	Optional<Adempimento> result = dialog.showAndWait();
    
	if (result.isPresent()) {
	 
	    System.out.println("Result: " + result.get());
	}
	return result.get();
	}
}
	
class TypeAndDate{
	public Calendar date;
	public Boolean rinvio;
}
