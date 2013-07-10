package com.thingtrack.konekti.view.module.mattrack.internal;

import java.io.Serializable;

import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

import com.thingtrack.mattrack.domain.Counter;

public class EventSensorClientService implements EventHandler {
	private String property;
	private EventSensorListener eventSensorListener = null;
	
	@Override
	public void handleEvent(Event event) {
		Counter value = (Counter) event.getProperty(property);	
		
		if (value != null && eventSensorListener != null)
			eventSensorListener.eventSensorClick(value);
		
	}

	public void setProperty(String property) {
		this.property = property;
	}
	
	public void addListenerEventSensor(EventSensorListener listener) {
		this.eventSensorListener = listener;
		
	}
	
	public interface EventSensorListener extends Serializable {
        public void eventSensorClick(Counter event);

    }
}
