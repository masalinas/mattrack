package com.thingtrack.mattrack.service.impl.internal;

import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.thingtrack.mattrack.dao.api.CounterDao;
import com.thingtrack.mattrack.domain.Counter;
import com.thingtrack.mattrack.service.api.CounterService;

public class CounterServiceImpl implements CounterService {
	@Autowired
	private CounterDao counterDao;
	
	@Override
	public List<Counter> getAll() throws Exception {
		return this.counterDao.getAll();
	}

	@Override
	public Counter get(Integer counterId) throws Exception {
		return this.counterDao.get(counterId);
	}

	@Override
	public Counter save(Counter counter) throws Exception {
		return this.counterDao.save(counter);
	}

	@Override
	public void delete(Counter counter) throws Exception {
		this.counterDao.delete(counter);
		
	}

	@Override
	public SimpleDateFormat getDateFormat() {
		return this.counterDao.getDateFormat();
		
	}
	
	@Override
	public List<Counter> getAllWeek(int weeks) throws Exception {
		return this.counterDao.getAllWeek(weeks);
		
	}
	
	@Override
	public LinkedHashMap<String, Integer> getAllWeekGrouped(int weeks) throws Exception {
		return this.counterDao.getAllWeekGrouped(weeks);
		
	}

	@Override
	public List<String> getAllWeekDays(int weeks) throws Exception {
		return this.counterDao.getAllWeekDays(weeks);
	}
}
