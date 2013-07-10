package com.thingtrack.konekti.view.module.mattrack.internal;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;

import com.thingtrack.konekti.view.addon.ui.AbstractViewContainer;
import com.thingtrack.konekti.view.addon.ui.SliderView;
import com.thingtrack.konekti.view.kernel.IWorkbenchContext;
import com.thingtrack.konekti.view.kernel.ui.layout.ISliderView;
import com.thingtrack.konekti.view.kernel.ui.layout.IViewChangeListener;
import com.thingtrack.konekti.view.kernel.ui.layout.IView;
import com.thingtrack.mattrack.service.api.CounterService;
import com.vaadin.annotations.AutoGenerated;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class MattrackViewContainer extends AbstractViewContainer {

	@AutoGenerated
	private VerticalLayout mainLayout;
	@AutoGenerated
	private SliderView sliderView;

	/** Views **/
	private MattrackView mattrackView;
		
	/** Business services **/
	@Autowired
	private CounterService counterService;
	
	@Autowired
	private EventSensorClientService eventSensorClientService;
	
	private IWorkbenchContext context; 
	
	// Thread Local Bundle Business Services
	private static ThreadLocal<CounterService> threadCounterService = new ThreadLocal<CounterService>();
	private static ThreadLocal<EventSensorClientService> threadEventSensorClientService = new ThreadLocal<EventSensorClientService>();
	
	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */
	
	/**
	 * The constructor should first build the main layout, set the
	 * composition root and then do any custom initialization.
	 *
	 * The constructor will not be automatically regenerated by the
	 * visual editor.
	 */
	public MattrackViewContainer(IWorkbenchContext context) {
		buildMainLayout();
		setCompositionRoot(mainLayout);

		// TODO add user code here
		this.context = context;
	}
	
	@SuppressWarnings("unused")
	@PostConstruct
	private void createViews() {
		// initialize thread local bundle services
		threadCounterService.set(counterService);
		threadEventSensorClientService.set(eventSensorClientService);
		
		// add all views controlled by SliderView Component
		mattrackView = new MattrackView(context, this);
		sliderView.addView(mattrackView);
		views.put(0, mattrackView);
		
	}
		
	@SuppressWarnings("unused")
	@PreDestroy
	private void destroyViews() {
		// destroy thread local bundle services
		threadCounterService.set(null);
		threadEventSensorClientService.set(null);
	}
    
    public static CounterService getMattrackService() {
    	return threadCounterService.get();
	}
    
    public static EventSensorClientService getEventSensorClientService() {
    	return threadEventSensorClientService.get();
	}
    
	@Override
	public ISliderView getSliderView() {
		return sliderView;
		
	}
	
	@Override
	public IView getSelectedView() {
		return sliderView.getSelectedView();
		
	}
	
	@Override
	public void addListener(IViewChangeListener listener) {
		sliderView.addListener(listener);
		
	}
	
	@AutoGenerated
	private VerticalLayout buildMainLayout() {
		// common part: create layout
		mainLayout = new VerticalLayout();
		mainLayout.setImmediate(false);
		mainLayout.setWidth("100%");
		mainLayout.setHeight("100%");
		mainLayout.setMargin(false);
		
		// top-level component properties
		setWidth("100.0%");
		setHeight("100.0%");
		
		// sliderView
		sliderView = new SliderView();
		sliderView.setImmediate(false);
		sliderView.setWidth("100.0%");
		sliderView.setHeight("100.0%");
		mainLayout.addComponent(sliderView);
		mainLayout.setExpandRatio(sliderView, 1.0f);
		
		return mainLayout;
	}

}
