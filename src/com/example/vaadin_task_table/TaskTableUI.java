package com.example.vaadin_task_table;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import org.tepi.filtertable.FilterTable;
import org.tepi.filtertable.paged.PagedFilterTable;

import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomTable.RowHeaderMode;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;


@SuppressWarnings("serial")
public class TaskTableUI extends UI {

	enum State {
		CREATED, PROCESSING, PROCESSED, FINISHED;
    }
	
	private static final String APP_NAME = "Task Manager";
	
	@Override
	protected void init(VaadinRequest request) {
		
		final PagedFilterTable<IndexedContainer> pagedFilterTable = buildPagedFilterTable();
		
		final VerticalLayout mainLayout = new VerticalLayout();
		setContent(mainLayout);
        mainLayout.setSpacing(true);
        mainLayout.setMargin(true);
        
        
        final HorizontalLayout headerLayout = new HorizontalLayout();
        // TODO add h1 app name
        //final Label appNameLabel = new Label(APP_NAME);
        //appNameLabel.setStyleName("");
        //headerLayout.addComponent(appNameLabel);
        
        // TODO popup button
        /*PopupButton popupButton = new PopupButton("Add Task");
        
        
        final Button addTaskButton = new Button("Add Task");
        headerLayout.addComponent(addTaskButton);
        
        addTaskButton.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				System.out.println("ADD TASK");
				final VerticalLayout e = new VerticalLayout();
		        e.addComponent(new Button("XXX"));
		        Window window = new Window("Window!", e);
		        mainLayout.addComponent(window);
			}
		});*/
        
        mainLayout.addComponent(headerLayout);
        mainLayout.addComponent(pagedFilterTable);
        mainLayout.addComponent(pagedFilterTable.createControls());
        mainLayout.addComponent(buildButtons(pagedFilterTable));
        
        // Have some layout and create the fields
        FormLayout form = new FormLayout();
         
        final TextField nameField = new TextField("Name");
        form.addComponent(nameField);
         
        final TextArea descriptionField = new TextArea("Description");
        form.addComponent(descriptionField);
        
        final DateField dateField = new DateField("Due date");
        
        form.addComponent(dateField);
        
        final ComboBox stateFiled = new ComboBox("State");
        stateFiled.addItem(State.CREATED);
        stateFiled.addItem(State.PROCESSING);
        stateFiled.addItem(State.PROCESSED);
        stateFiled.addItem(State.FINISHED);
        form.addComponent(stateFiled);
        
        final ComboBox priorityField = new ComboBox("Priority");
        for(int i = 1; i < 5; i++){
        	priorityField.addItem(i);	
        }
        form.addComponent(priorityField);
        
        Button addButton = new Button("Add task");
        addButton.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				Object result = pagedFilterTable.addItem(
					new Object[] {
						nameField.getValue(), descriptionField.getValue(), dateField.getValue(), stateFiled.getValue(), priorityField.getValue()
					}, 
					null
				);
				if(result != null){
					Notification.show("Info message", "Task was added", Notification.Type.HUMANIZED_MESSAGE);
				}
			}
		});
        
        form.addComponent(addButton);
        mainLayout.addComponent(form);
        
        // TODO
        // 1) date column proper format (without hours) in column and in the filed
        // 2) basic validation
        // 3) Record edition after click on record
        // 4) Set default values for priority, status or remove blank value
        // 5) Add message after adding task
        // OPTIONAL
        // 1) hide id column
        // 2) add more filters
        // 3) popup window for form
        // 4) add Task Manager title
        // 5) move filters on the top of the table
        // 6) clean up code (refactor package name and file names)
        // 7) create some dao
        // 8) load data from json
        
        // TODO validation
        // https://vaadin.com/directory#addon/vaadin-bean-validation
	}
	
	private PagedFilterTable<IndexedContainer> buildPagedFilterTable() {
        PagedFilterTable<IndexedContainer> filterTable = new PagedFilterTable<IndexedContainer>();
        filterTable.setWidth("100%");

        filterTable.setFilterDecorator(new DemoFilterDecorator());
        filterTable.setFilterGenerator(new DemoFilterGenerator()); 

        filterTable.setFilterBarVisible(true);

        filterTable.setSelectable(true);
        filterTable.setImmediate(true);
        filterTable.setMultiSelect(true);

        filterTable.setRowHeaderMode(RowHeaderMode.INDEX);

        filterTable.setColumnCollapsingAllowed(true);

        filterTable.setColumnCollapsed("state", true);

        filterTable.setColumnReorderingAllowed(true);

        filterTable.setContainerDataSource(buildContainer());

        /*filterTable.setVisibleColumns(new String[] { "name", "id", "state",
                "date", "validated", "checked" });*/
        
        filterTable.setVisibleColumns(
        	new String[] { 
        		"name", 
        		"description", 
        		"date",
        		"state",
        		"priority"
        	}
        );

        return filterTable;
    }
	
	@SuppressWarnings("unchecked")
    private Container buildContainer() {
        IndexedContainer cont = new IndexedContainer();
        Calendar c = Calendar.getInstance();

        cont.addContainerProperty("name", String.class, null);
        cont.addContainerProperty("description", String.class, null);
        cont.addContainerProperty("date", Date.class, null);
        cont.addContainerProperty("state", State.class, null);
        cont.addContainerProperty("priority", Integer.class, null);

        Random random = new Random();
        for (int i = 0; i < 10000; i++) {
            cont.addItem(i);
            /* Set name and id properties */
            cont.getContainerProperty(i, "name").setValue("Order " + i);
            cont.getContainerProperty(i, "description").setValue("Description " + i);
            
            cont.getContainerProperty(i, "priority").setValue(i);
            /* Set state property */
            int rndInt = random.nextInt(4);
            State stateToSet = State.CREATED;
            if (rndInt == 0) {
                stateToSet = State.PROCESSING;
            } else if (rndInt == 1) {
                stateToSet = State.PROCESSED;
            } else if (rndInt == 2) {
                stateToSet = State.FINISHED;
            }
            cont.getContainerProperty(i, "state").setValue(stateToSet);
            /* Set date property */
            cont.getContainerProperty(i, "date").setValue(
                    new Timestamp(c.getTimeInMillis()));
            c.add(Calendar.DAY_OF_MONTH, 1);
            
        }
        return cont;
    }
	
	private Component buildButtons(final FilterTable relatedFilterTable) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setHeight(null);
        buttonLayout.setWidth("100%");
        buttonLayout.setSpacing(true);

        Label hideFilters = new Label("Show Filters:");
        hideFilters.setSizeUndefined();
        buttonLayout.addComponent(hideFilters);
        buttonLayout.setComponentAlignment(hideFilters, Alignment.MIDDLE_LEFT);

        for (Object propId : relatedFilterTable.getContainerPropertyIds()) {
            Component t = createToggle(relatedFilterTable, propId);
            buttonLayout.addComponent(t);
            buttonLayout.setComponentAlignment(t, Alignment.MIDDLE_LEFT);
        }

        CheckBox showFilters = new CheckBox("Toggle Filter Bar visibility");
        showFilters.setValue(relatedFilterTable.isFilterBarVisible());
        showFilters.setImmediate(true);
        showFilters.addValueChangeListener(new Property.ValueChangeListener() {

            public void valueChange(ValueChangeEvent event) {
                relatedFilterTable.setFilterBarVisible((Boolean) event
                        .getProperty().getValue());

            }
        });
        buttonLayout.addComponent(showFilters);
        buttonLayout.setComponentAlignment(showFilters, Alignment.MIDDLE_RIGHT);
        buttonLayout.setExpandRatio(showFilters, 1);

        Button setVal = new Button("Set the State filter to 'Processed'");
        setVal.addClickListener(new Button.ClickListener() {

            public void buttonClick(ClickEvent event) {
                relatedFilterTable
                        .setFilterFieldValue("state", State.PROCESSED);
            }
        });
        buttonLayout.addComponent(setVal);
        
        Button reset = new Button("Reset");
        reset.addClickListener(new Button.ClickListener() {

            public void buttonClick(ClickEvent event) {
                relatedFilterTable.resetFilters();
            }
        });
        buttonLayout.addComponent(reset);

        return buttonLayout;
    }
	
	private Component createToggle(final FilterTable relatedFilterTable,
            final Object propId) {
        CheckBox toggle = new CheckBox(propId.toString());
        toggle.setValue(relatedFilterTable.isFilterFieldVisible(propId));
        toggle.setImmediate(true);
        toggle.addValueChangeListener(new Property.ValueChangeListener() {

            public void valueChange(ValueChangeEvent event) {
                relatedFilterTable.setFilterFieldVisible(propId,
                        !relatedFilterTable.isFilterFieldVisible(propId));
            }
        });
        return toggle;
    }

}