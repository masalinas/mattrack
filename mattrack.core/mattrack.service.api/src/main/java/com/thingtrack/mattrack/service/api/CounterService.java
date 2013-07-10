package com.thingtrack.mattrack.service.api;

import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.List;

import com.thingtrack.mattrack.domain.Counter;

public interface CounterService {
	public List<Counter> getAll() throws Exception;
	public Counter get( Integer counterId ) throws Exception;
	public Counter save(Counter counter) throws Exception;
	public void delete(Counter counter) throws Exception;
	
	public SimpleDateFormat getDateFormat();
	public List<Counter> getAllWeek(int weeks) throws Exception;
	public LinkedHashMap<String, Integer> getAllWeekGrouped(int weeks) throws Exception;
	public List<String> getAllWeekDays(int weeks) throws Exception;
	
	
}
