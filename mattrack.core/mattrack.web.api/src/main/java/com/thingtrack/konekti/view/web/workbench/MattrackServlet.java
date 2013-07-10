/*
 * Copyright 2009 IT Mill Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.thingtrack.konekti.view.web.workbench;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.HttpRequestHandler;
import org.springframework.beans.factory.annotation.Autowired;

import com.thingtrack.mattrack.domain.Counter;
import com.thingtrack.mattrack.service.api.CounterService;

@Component("MattrackServlet")
public class MattrackServlet implements HttpRequestHandler {
    	@Autowired
    	private CounterService counterService;

    	@Autowired
    	private EventSensorService eventSensorService;
    	    	
		@Override
		public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {										
            if (counterService == null)
            	return;            
			
			response.setContentType("text/plain");
			
			// STEP01: parse count servlet payload
			//localhost:8080/mattrack?dispositivo=D2&numeroConteos=3&pisada1=1-1372000554&pisada2=0-1372000554&pisada3=1-1372000554			
			int numberOfCounts = Integer.parseInt(request.getParameter("numeroConteos"));
			String deviceName = request.getParameter("dispositivo"); 

			List<Counter> counts = new ArrayList<Counter>();
			
			try {
				String pisada[] = new String [numberOfCounts];
				String delimiter = "-";
				
				// parse data
				for (int i=0; i<numberOfCounts; i++) {
					pisada[i] = request.getParameter("pisada" + Integer.toString(i+1));
					String[] parsed = pisada[i].split(delimiter);
					
					counts.add(createCount(deviceName,
					 					   parsed[0],
					 				       parsed[1]));
				}
				
			} catch (ParseException e) {
				response.getWriter().write(e.getMessage());
				return;
			}
			
            // save count in DB
			try {
				for (int i=0; i<numberOfCounts; i++)
					counterService.save(counts.get(i));				
				
			} catch (Exception e) {
				response.getWriter().write(e.getMessage());
				return;
			}
            	
			// send sensor event data for the last message
			eventSensorService.sendData(counts.get(numberOfCounts - 1));
			
			// show message
			response.getWriter().write("Count: [DEVICE]=" + (counts.get(0)).getDeviceName() + 
									   "; [WAY]=" + (counts.get(0)).getWay() + 
									   "; [DATE]=" + (counts.get(0)).getCountDate() + " saved correctly!");

		}
				
		private Counter createCount(String deviceName, String way, String countDate) throws ParseException {
			Counter counter = new Counter();
			
			counter.setDeviceName(deviceName);
			counter.setWay(Integer.parseInt(way));
						
			//Adaptación dato recibido a la hora local y a milisegundos
			long dateLong = Long.parseLong(countDate);
			dateLong *= 1000;			
			Date date = new Date(dateLong);
			
			counter.setCountDate(date);
			
			return counter;
		}
}
