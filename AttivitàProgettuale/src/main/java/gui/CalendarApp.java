package gui;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.Calendar.Style;
import com.calendarfx.model.CalendarEvent;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.model.Interval;
import com.calendarfx.view.CalendarView;

import calendar.ConcreteCalendarEvent;
import cliente.PersonaGiuridica;
import entities.Adempimento;
import entities.Procedimento;
import entities.Udienza;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.InputEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import udienza.AbstractUdienza;
import udienza.ConcreteAdempimento;
import udienza.ConcreteProcedimento;
import udienza.UdienzaTribunale;
import udienza.UdienzaTribunaleFactory;
import user_task_handlers.TaskHandlerImpostarePromemoriaAdempimento;
import user_task_handlers.TaskHandlerImpostarePromemoriaUdienza;
import user_task_handlers.TaskHandlerInserimentoDatiUdienza;

import static org.junit.Assert.assertNotNull;

import java.io.FileInputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.test.ActivitiRule;

public class CalendarApp extends Application {
	
	Button nuova_udienza_button = new Button("Aggiungi Udienza");
	Button visualizza_evento_button = new Button("Visualizza Evento");
	Button rinvio_udienza_button = new Button("Rinvia Udienza");

    @Override
    public void start(Stage primaryStage) throws Exception {
    	
    	primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				try {
					ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
					processEngine.close();
					stop();
				} catch (Exception e) {
					e.printStackTrace();
				}	
			}	
    	});
    	
        CalendarView calendarView = new CalendarView();
        
        Calendar agenda = new Calendar("Agenda");
        agenda.setShortName("A");
        agenda.setStyle(Style.STYLE1);

        CalendarSource familyCalendarSource = new CalendarSource("Family");
        fillCalendar(agenda);
        familyCalendarSource.getCalendars().add(agenda);

        calendarView.getCalendarSources().setAll(familyCalendarSource);
        calendarView.setRequestedTime(LocalTime.now());

        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(calendarView); // introPane);
        
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(stackPane);
        GridPane bottom = new GridPane();
        bottom.add(nuova_udienza_button, 1, 1);
        bottom.add(visualizza_evento_button, 2, 1);
        bottom.add(rinvio_udienza_button, 3, 1);
        borderPane.setBottom(bottom);

        Thread updateTimeThread = new Thread("Calendar: Update Time Thread") {
            @Override
            public void run() {
                while (true) {
                    Platform.runLater(() -> {
                        calendarView.setToday(LocalDate.now());
                        calendarView.setTime(LocalTime.now());
                    });

                    try {
                        // update every 10 seconds
                        sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        };
        
        /**
         * 
         * 
         * 
         */
        
        nuova_udienza_button.setOnAction(new EventHandler<ActionEvent>() {

        	private String filename = "C:\\Users\\aalfo\\eclipse-workspace\\Attivit‡Progettuale\\src\\main\\resources\\diagrams\\NuovaUdienza.bpmn";
        	private ProcessInstance processInstance;
        	private String deployment_id;
        	private ProcessEngine processEngine;
        	private HistoryService historyService;
        	private RuntimeService runtimeService;
        	private TaskService taskService;
        	private Adempimento adempimento;
        	private UdienzaTribunale udienza;
        	
			@Override
			public void handle(ActionEvent event) {
				
				try {
					
					UdienzaGUI udienza_gui = showDialogUdienza();
					udienza = udienza_gui.toUdienzaTribunale();
					Entry entry = new Entry(udienza.getParti(),
							new Interval(udienza.getZonedDateTime().toLocalDate(), 
									LocalTime.ofInstant(udienza.getDate().toInstant(), ZoneId.systemDefault()),
									udienza.getZonedDateTime().toLocalDate(),
									LocalTime.ofInstant(udienza.getDate().toInstant().plusSeconds(3600), ZoneId.systemDefault())));
					entry.setUserObject(udienza);
					calendarView.getCalendars().get(0).addEntry(entry);
					
					startProcess();
					
					runtimeService = processEngine.getRuntimeService();
					taskService = processEngine.getTaskService();
					TaskHandlerInserimentoDatiUdienza task = new TaskHandlerInserimentoDatiUdienza();
					task.execute(processInstance.getProcessDefinitionId(), udienza, runtimeService, taskService);
					
					adempimento = (Adempimento) runtimeService.getVariable(processInstance.getId(), "adempimento");
					Entry entry_adempimento = new Entry(adempimento.getName(),
							new Interval(adempimento.getZonedDateTime().toLocalDate(), 
									LocalTime.ofInstant(adempimento.getDate().toInstant(), ZoneId.systemDefault()),
									adempimento.getZonedDateTime().toLocalDate(),
									LocalTime.ofInstant(adempimento.getDate().toInstant().plusSeconds(3600), ZoneId.systemDefault())));
					entry_adempimento.setUserObject(adempimento);
					calendarView.getCalendars().get(0).addEntry(entry_adempimento);
					
					PromemoriaGUI impostare = impostarePromemoriaDialog(adempimento);
					TaskHandlerImpostarePromemoriaAdempimento task_2 = new TaskHandlerImpostarePromemoriaAdempimento();
					task_2.execute(processInstance.getProcessDefinitionId(), impostare.getImpostare(), processEngine.getRuntimeService(), processEngine.getTaskService(),
							impostare.getDays(), impostare.getHour_of_day());
					
					Boolean libero = (Boolean) runtimeService.getVariable(processInstance.getId(), "libero");
					if(libero == false) {
						notificaSovrapposizioneDialog();
					}
					
					impostare = impostarePromemoriaDialog(udienza);
					TaskHandlerImpostarePromemoriaUdienza task_3 = new TaskHandlerImpostarePromemoriaUdienza();
					task_3.execute(processInstance.getProcessDefinitionId(), impostare.getImpostare(), processEngine.getRuntimeService(),
							processEngine.getTaskService()); //da completare per completezza, non necessario
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
				
			public UdienzaGUI showDialogUdienza() {
				
				Dialog<UdienzaGUI> dialog = new Dialog<>();
				dialog.setTitle("Nuova udienza");
				dialog.setHeaderText("Inserisci i dati della nuova udienza");
				dialog.setResizable(true);
				 
				Label label1 = new Label("Cliente: ");
				Label label2 = new Label("Giudice: ");
				Label label3 = new Label("Parti: ");
				Label label4 = new Label("Note: ");
				Label label5 = new Label("Data: ");
				TextField text1 = new TextField();
				TextField text2 = new TextField();
				TextField text3 = new TextField();
				TextField text4 = new TextField();
				DatePicker date_picker = new DatePicker();
				         
				GridPane grid = new GridPane();
				grid.add(label1, 1, 1);
				grid.add(text1, 2, 1);
				grid.add(label2, 1, 2);
				grid.add(text2, 2, 2);
				grid.add(label3, 1, 3);
				grid.add(text3, 2, 3);
				grid.add(label4, 1, 4);
				grid.add(text4, 2, 4);
				grid.add(label5, 1, 5);
				grid.add(date_picker, 2, 5);
				dialog.getDialogPane().setContent(grid);
				         
				ButtonType buttonTypeOk = new ButtonType("Okay", ButtonData.OK_DONE);
				dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
				 
				dialog.setResultConverter(new Callback<ButtonType, UdienzaGUI>() {
				    @Override
				    public UdienzaGUI call(ButtonType b) {
				 
				        if (b == buttonTypeOk) {
				 
				        	UdienzaGUI out = new UdienzaGUI();
				        	
				        	PersonaGiuridica p = new PersonaGiuridica();
				        	p.setName(text1.getText());
				        	out.setClient(p);
				        	
				        	out.setGiudice(text2.getText());
				        	
				        	ConcreteProcedimento procedimento = new ConcreteProcedimento();
				        	procedimento.setParti(text3.getText());
				        	out.setProcedimento(procedimento);
				        	
				        	out.setNote(text4.getText());
				        	
				        	LocalDate date = date_picker.getValue();
				        	ConcreteCalendarEvent cal_event = new ConcreteCalendarEvent();
				        	java.util.Calendar cal = java.util.Calendar.getInstance();
				        	cal.set(java.util.Calendar.YEAR, date.getYear());
				        	cal.set(java.util.Calendar.MONTH, date.getMonth().getValue());
				        	cal.set(java.util.Calendar.DAY_OF_MONTH, date.getDayOfMonth());
				        	cal.set(java.util.Calendar.HOUR_OF_DAY, 16);
				        	cal.set(java.util.Calendar.MINUTE, 38);
				        	cal_event.setDate(cal);
				        	out.setCalendar_event(cal_event);
				        	
				            return out;
				        }
				 
				        return null;
				    }
				});
				         
				Optional<UdienzaGUI> result = dialog.showAndWait();
				         
				if (result.isPresent()) {
				 
				    System.out.println("Result: " + result.get());
				}
				return result.get();			
				
				
			}

			public void startProcess() throws Exception {
				processEngine = ProcessEngines.getDefaultProcessEngine();
				RepositoryService repositoryService = processEngine.getRepositoryService();
				deployment_id = repositoryService.createDeployment().addInputStream("nuovaUdienza.bpmn20.xml",
						new FileInputStream(filename)).deploy().getId();
				RuntimeService runtimeService = processEngine.getRuntimeService();
				Map<String, Object> variableMap = new HashMap<String, Object>();
				//variableMap.put("name", "Activiti");
				this.processInstance = runtimeService.startProcessInstanceByKey("nuovaUdienza", variableMap);
				assertNotNull(processInstance.getId());
				System.out.println("id " + processInstance.getId() + " "
						+ processInstance.getProcessDefinitionId());
			}
        	
			public PromemoriaGUI impostarePromemoriaDialog(Object evento) throws Exception {

				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Impostare promemoria?");
				String s; 
				if(evento instanceof Adempimento) {
					s = "Si desidera impostare un promemoria mail per l'adempimento: " +
									adempimento.getName() + "?";
				}
				else {
					if(evento instanceof Udienza) {
						s = "Si desidera impostare un promemoria mail per l'udienza: " +
								udienza.getParti() + " del " + udienza.getDate().get(java.util.Calendar.DAY_OF_MONTH) +
								"/" + udienza.getDate().get(java.util.Calendar.MONTH) + "?";
					}
					else {
						throw new Exception("Tipo di evento non riconosciuto!");
					}
				}
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
			
			public void notificaSovrapposizioneDialog() {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Attenzione!");
				alert.setHeaderText("Sovrapposizione udienza");
				String s ="In data " + udienza.getDate().get(java.util.Calendar.DAY_OF_MONTH) +
						"/" + udienza.getDate().get(java.util.Calendar.MONTH) + 
						" Ë gi‡ presente un'altra udienza. L'evento Ë stato comunque creato.";
				alert.setContentText(s);
				alert.show();
			}
        });
                    
        visualizza_evento_button.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				
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
				
				Entry entry = entries.get(entries.size() - 1);
				Object event_to_show = entry.getUserObject();
				
				if(event_to_show instanceof Udienza) {
					AbstractUdienza udienza_show = (AbstractUdienza) event_to_show;
					Alert alert = new Alert(AlertType.CONFIRMATION);
					alert.setTitle("Udienza del: " + udienza_show.getDate().get(java.util.Calendar.DAY_OF_MONTH) + "/"
							+ udienza_show.getDate().get(java.util.Calendar.MONTH) + "/"
							+ udienza_show.getDate().get(java.util.Calendar.YEAR));
					alert.setHeaderText(udienza_show.getParti());
					String s ="Giudice: " + udienza_show.getGiudice() + 
							"\n Cliente: " + udienza_show.getClient().getName() +
							"\n Note: " + udienza_show.getNote();
					alert.setContentText(s);
					alert.show();
				}
				
				if(event_to_show instanceof Adempimento) {
					ConcreteAdempimento adempimento_show = (ConcreteAdempimento) event_to_show;
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Adempimento del: " + adempimento_show.getDate().get(java.util.Calendar.DAY_OF_MONTH) + "/"
							+ adempimento_show.getDate().get(java.util.Calendar.MONTH) + "/"
							+ adempimento_show.getDate().get(java.util.Calendar.YEAR));
					alert.setHeaderText(adempimento_show.getName());
					String s ="Codice: " + adempimento_show.getCode() + 
							"\n Note: " + adempimento_show.getNote();
					alert.setContentText(s);
					alert.show();
				}
				
			}
        	
        });
        
        rinvio_udienza_button.setOnAction(new RinvioHandler(calendarView));
        /**
         * 
         * 
         * 
         */

        updateTimeThread.setPriority(Thread.MIN_PRIORITY);
        updateTimeThread.setDaemon(true);
        updateTimeThread.start();

        Scene scene = new Scene(borderPane);
        primaryStage.setTitle("Calendar");
        primaryStage.setScene(scene);
        primaryStage.setWidth(1300);
        primaryStage.setHeight(800);
        primaryStage.centerOnScreen();
        primaryStage.show();
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
    
    public static void fillCalendar(Calendar agenda) throws Exception {
    	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
    	HistoryService historyService = processEngine.getHistoryService();
    	List<Udienza> udienze = new ArrayList<Udienza>();
    	List<Adempimento> adempimenti = new ArrayList<Adempimento>();
    	List<Entry> entries = new ArrayList<Entry>();
    	List<HistoricVariableInstance> historic_udienze = historyService.createHistoricVariableInstanceQuery().variableName("udienza").list();
    	List<HistoricVariableInstance> historic_adempimenti = historyService.createHistoricVariableInstanceQuery().variableName("adempimento").list();
    	for(HistoricVariableInstance h : historic_udienze) {
    		udienze.add((Udienza) h.getValue());		
    	}
    	for(HistoricVariableInstance h : historic_adempimenti) {
    		if(h.getValue() instanceof Adempimento) {
    			adempimenti.add((Adempimento) h.getValue());
    			continue;
    		}
    		if(h.getValue() instanceof ArrayList<?>) {
    			ArrayList<Adempimento> ads = (ArrayList<Adempimento>) h.getValue();
    			for(Adempimento a : ads) {
    				adempimenti.add(a);
    			}
    			continue;
    		}
    		else {
    			throw new Exception("Ho trovato un oggetto sconosciuto nel database cercando \"adempimento\"");
    		}
    	}
    	for(Udienza udienza : udienze) {
    		Entry entry = new Entry(udienza.getParti(),
					new Interval(udienza.getZonedDateTime().toLocalDate(), 
							LocalTime.ofInstant(udienza.getDate().toInstant(), ZoneId.systemDefault()),
							udienza.getZonedDateTime().toLocalDate(),
							LocalTime.ofInstant(udienza.getDate().toInstant().plusSeconds(3600), ZoneId.systemDefault())));
			entry.setUserObject(udienza);
			agenda.addEntry(entry);
    	}
    	for(Adempimento adempimento : adempimenti) {
    		Entry entry = new Entry(adempimento.getName(),
					new Interval(adempimento.getZonedDateTime().toLocalDate(), 
							LocalTime.ofInstant(adempimento.getDate().toInstant(), ZoneId.systemDefault()),
							adempimento.getZonedDateTime().toLocalDate(),
							LocalTime.ofInstant(adempimento.getDate().toInstant().plusSeconds(3600), ZoneId.systemDefault())));
			entry.setUserObject(adempimento);
			agenda.addEntry(entry);
    	}
    }

    public static void main(String[] args) {
        launch(args);
    }
}
