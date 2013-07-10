package com.thingtrack.mattrack.dao.api;

import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.List;

import com.thingtrack.konekti.dao.template.Dao;
import com.thingtrack.mattrack.domain.Counter;

public interface CounterDao extends Dao<Counter, Integer> {
	public SimpleDateFormat getDateFormat();
	public List<Counter> getAllWeek(int weeks) throws Exception;
	public LinkedHashMap<String, Integer> getAllWeekGrouped(int weeks) throws Exception;
	public List<String> getAllWeekDays(int weeks) throws Exception;
}
