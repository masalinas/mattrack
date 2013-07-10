package com.thingtrack.konekti.view.web.workbench;

import java.util.HashMap;
import java.util.Map;

import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;

import org.springframework.osgi.extensions.annotation.ServiceReference;

import com.thingtrack.mattrack.domain.Counter;

public class EventSensorService {
	private EventAdmin eventAdmin;
	private String EVENT_QUEUE;
	
	public void setEVENT_QUEUE(String EVENT_QUEUE) {	
		this.EVENT_QUEUE = EVENT_QUEUE;
	
	}
	
	@ServiceReference
	public void setEventAdmin(EventAdmin eventAdmin) {		
		this.eventAdmin = eventAdmin;
		
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void sendData(Counter counter) {
		Map hm = new HashMap();
		hm.put("data", counter);
		
		eventAdmin.sendEvent(new Event(EVENT_QUEUE, hm));
		
	}
}
