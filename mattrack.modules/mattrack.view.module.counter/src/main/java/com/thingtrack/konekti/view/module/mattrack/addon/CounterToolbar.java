package com.thingtrack.konekti.view.module.mattrack.addon;

import java.io.Serializable;

import com.thingtrack.konekti.view.addon.data.BindingSource;
import com.thingtrack.konekti.view.addon.data.BindingSource.IndexChangeEvent;
import com.thingtrack.konekti.view.addon.ui.AbstractToolbar;
import com.thingtrack.konekti.view.kernel.ui.layout.IViewContainer;

import org.vaadin.peter.buttongroup.ButtonGroup;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.terminal.gwt.client.MouseEventDetails;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.HorizontalLayout;

@SuppressWarnings("serial")
public class CounterToolbar extends AbstractToolbar {
	@AutoGenerated
	private HorizontalLayout toolbarLayout;

	@AutoGenerated
	private Button btnPreviousWeek;

	@AutoGenerated
	private Button btnNextWeek;

	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */

	private int position = 0;			
	private IViewContainer viewContainer;
	private Integer week=0;
	
	// navigator button listeners
	private ClickPreviousWeekButtonListener listenerPreviousWeekButton = null;
	private ClickNextWeekButtonListener listenerNextWeekButton = null;
	
	/**
	 * The constructor should first build the main layout, set the
	 * composition root and then do any custom initialization.
	 *
	 * The constructor will not be automatically regenerated by the
	 * visual editor.
	 */	
	public CounterToolbar(int position, final BindingSource<?> bindingSource, IViewContainer viewContainer) {
		super(position, bindingSource);
			
		buildMainLayout();
		//setCompositionRoot(mainLayout);

		// TODO add user code here
		this.position = position;
		this.viewContainer = viewContainer;
		
		setBindingSource(bindingSource);
		
		btnPreviousWeek.setDescription("Semana Previa");
		btnNextWeek.setDescription("Semana Posterior");
		
		btnNextWeek.setEnabled(false);
		
		btnPreviousWeek.addListener(new ClickListener() {			
			public void buttonClick(ClickEvent event) {
				week++;
				
				if (week == 0)
					btnNextWeek.setEnabled(false);
				else
					btnNextWeek.setEnabled(true);
				
				if (listenerPreviousWeekButton != null)
					listenerPreviousWeekButton.previousWeekButtonClick(new ClickNavigationEvent(event.getButton(), event.getComponent() , null, week, 0));					
				
			}
		});
		
		btnNextWeek.addListener(new ClickListener() {			
			public void buttonClick(ClickEvent event) {				
				week--;
				
				if (week == 0)
					btnNextWeek.setEnabled(false);
				else
					btnNextWeek.setEnabled(true);
				
				if (listenerNextWeekButton != null)
					listenerNextWeekButton.nextWeekButtonClick(new ClickNavigationEvent(event.getButton(), event.getComponent() , null, week, 0));					
				
			}
		});
		
	}
	
	@Override
	public int getPosition() {
		return this.position;
		
	}

	@Override
	public ComponentContainer getContent() {
		return this.toolbarLayout;
		
	}
	
	public void addListenerPreviousWeekButton(ClickPreviousWeekButtonListener listener) {
		this.listenerPreviousWeekButton = listener;
		
	}
	
	public void addListenerNextWeekButton(ClickNextWeekButtonListener listener) {
		this.listenerNextWeekButton = listener;
		
	}
	
	public interface ClickPreviousWeekButtonListener extends Serializable {
        public void previousWeekButtonClick(ClickNavigationEvent event);

    }
	
	public interface ClickNextWeekButtonListener extends Serializable {
        public void nextWeekButtonClick(ClickNavigationEvent event);

    }
	
	public class ClickNavigationEvent extends ClickEvent {
		private int index;
		private Integer week;
		
		public ClickNavigationEvent(Button button, Component source) {
			button.super(source);
			
		}

		public ClickNavigationEvent(Button button, Component source, MouseEventDetails details) {
			button.super(source, details);
			
		}

		public ClickNavigationEvent(Button button, Component source, MouseEventDetails details, Integer week, int index) {
			button.super(source, details);
			
			this.index = index;
			this.week = week;
		}

		public int getIndex() {
			return this.index;
			
		}
		
		public Integer getWeek() {
			return this.week;
			
		}
		
	  }
	
	@Override
	public void setBindingSource(BindingSource<?> bindingSource) {
		this.bindingSource = bindingSource;
				
	}
	
	@Override
	public void bindingSourceIndexChange(IndexChangeEvent event) {
		
	}
	
	@AutoGenerated
	private void buildMainLayout() {
		toolbarLayout = buildToolbarLayout();
		addComponent(toolbarLayout);
		
	}
	
	@AutoGenerated
	private HorizontalLayout buildToolbarLayout() {		
		toolbarLayout = new HorizontalLayout();
		toolbarLayout.setImmediate(false);
		toolbarLayout.setSpacing(true);
		
		ButtonGroup editionButtonGroup = new ButtonGroup();
		toolbarLayout.addComponent(editionButtonGroup);
		
		// btnPreviousWeek
		btnPreviousWeek = new Button();
		btnPreviousWeek.setCaption("Semana Anterior");
		btnPreviousWeek.setImmediate(true);
		btnPreviousWeek.setWidth("-1px");
		btnPreviousWeek.setHeight("-1px");
		btnPreviousWeek.setIcon(new ThemeResource("../konekti/images/icons/navigation-toolbar/arrow-180.png"));
		
		editionButtonGroup.addButton(btnPreviousWeek);
		
		// btnNextWeek
		btnNextWeek = new Button();
		btnNextWeek.setCaption("Semana Siguiente");
		btnNextWeek.setImmediate(true);
		btnNextWeek.setWidth("-1px");
		btnNextWeek.setHeight("-1px");
		btnNextWeek.setIcon(new ThemeResource("../konekti/images/icons/navigation-toolbar/arrow.png"));
		
		editionButtonGroup.addButton(btnNextWeek);
				
		return toolbarLayout;
	}
}
