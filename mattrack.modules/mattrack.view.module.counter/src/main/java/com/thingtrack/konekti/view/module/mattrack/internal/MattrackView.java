package com.thingtrack.konekti.view.module.mattrack.internal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.thingtrack.konekti.view.addon.data.BindingSource;
import com.thingtrack.konekti.view.addon.ui.AbstractView;
import com.thingtrack.konekti.view.addon.ui.BoxToolbar;
import com.thingtrack.konekti.view.addon.ui.BoxToolbar.ClickFilterButtonListener;
import com.thingtrack.konekti.view.addon.ui.BoxToolbar.ClickPrintButtonListener;
import com.thingtrack.konekti.view.addon.ui.DataGridView;
import com.thingtrack.konekti.view.addon.ui.NavigationToolbar;
import com.thingtrack.konekti.view.addon.ui.NavigationToolbar.ClickRefreshButtonListener;
import com.thingtrack.konekti.view.kernel.IWorkbenchContext;
import com.thingtrack.konekti.view.kernel.ui.layout.IViewContainer;
import com.thingtrack.konekti.view.module.mattrack.addon.CounterToolbar;
import com.thingtrack.konekti.view.module.mattrack.addon.CounterToolbar.ClickNavigationEvent;
import com.thingtrack.konekti.view.module.mattrack.addon.CounterToolbar.ClickNextWeekButtonListener;
import com.thingtrack.konekti.view.module.mattrack.addon.CounterToolbar.ClickPreviousWeekButtonListener;
import com.thingtrack.konekti.view.module.mattrack.internal.EventSensorClientService.EventSensorListener;
import com.thingtrack.mattrack.domain.Counter;
import com.thingtrack.mattrack.service.api.CounterService;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.ui.CustomTable;
import com.vaadin.ui.CustomTable.ColumnGenerator;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.data.Property;

import com.github.wolfie.refresher.Refresher;
import com.github.wolfie.refresher.Refresher.RefreshListener;

import com.invient.vaadin.charts.InvientCharts;
import com.invient.vaadin.charts.InvientCharts.DecimalPoint;
import com.invient.vaadin.charts.InvientCharts.Series;
import com.invient.vaadin.charts.InvientCharts.SeriesType;
import com.invient.vaadin.charts.InvientChartsConfig;
import com.invient.vaadin.charts.InvientChartsConfig.AxisBase.AxisTitleAlign;
import com.invient.vaadin.charts.InvientChartsConfig.BarConfig;
import com.invient.vaadin.charts.InvientChartsConfig.CategoryAxis;
import com.invient.vaadin.charts.InvientChartsConfig.DataLabel;
import com.invient.vaadin.charts.InvientChartsConfig.HorzAlign;
import com.invient.vaadin.charts.InvientChartsConfig.Legend;
import com.invient.vaadin.charts.InvientChartsConfig.Legend.Layout;
import com.invient.vaadin.charts.InvientChartsConfig.Position;
import com.invient.vaadin.charts.InvientChartsConfig.VertAlign;
import com.invient.vaadin.charts.InvientChartsConfig.XAxis;
import com.invient.vaadin.charts.InvientChartsConfig.YAxis;
import com.invient.vaadin.charts.InvientChartsConfig.NumberYAxis;
import com.invient.vaadin.charts.InvientCharts.XYSeries;
import com.invient.vaadin.charts.Color.RGB;
import com.invient.vaadin.charts.InvientChartsConfig.AxisBase.AxisTitle;

@SuppressWarnings("serial")
public class MattrackView extends AbstractView 
	implements ClickRefreshButtonListener, ClickFilterButtonListener, 
		ClickPrintButtonListener, EventSensorListener, RefreshListener,
		ClickPreviousWeekButtonListener, ClickNextWeekButtonListener {

	@AutoGenerated
	private VerticalLayout mainLayout;
	@AutoGenerated
	private HorizontalLayout workAreaLayout;
	@AutoGenerated
	private DataGridView dgMattrack;
	@AutoGenerated
	private VerticalLayout totlLayout;
	@AutoGenerated
	private DataGridView countsTable;

	private InvientCharts countsChart;
	private InvientChartsConfig chartConfig; 
	
	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */

	private final static int IN = 1;	
	private final static int OUT = 0;
	
	private int IN_Counts;	
	private int OUT_Counts;		
	
	private List <Counter> counters;	
	private BindingSource<Counter> bsCounter = new BindingSource<Counter>(Counter.class, 0);
	
	private NavigationToolbar navigationToolbar;
	private BoxToolbar boxToolbar;
	private CounterToolbar counterToolbar;
	
	private IViewContainer viewContainer;
	private IWorkbenchContext context;
	
	private CounterService counterService;
	private EventSensorClientService eventSensorClientService;
	
	private int weeks = 0;
	
	/**
	 * The constructor should first build the main layout, set the composition
	 * root and then do any custom initialization.
	 * 
	 * The constructor will not be automatically regenerated by the visual
	 * editor.
	 */
	public MattrackView(IWorkbenchContext context, IViewContainer viewContainer) {
		this.context = context;
		
		IN_Counts = 0;
		OUT_Counts = 0;
		
		buildMainLayout();
		setCompositionRoot(mainLayout);

		// TODO add user code here
		// set Slide View Services and ViewContainer to navigate		
		this.viewContainer = viewContainer;
		this.counterService = MattrackViewContainer.getMattrackService();
	    this.eventSensorClientService = MattrackViewContainer.getEventSensorClientService(); 
		
	    // add event sensor 
	    this.eventSensorClientService.addListenerEventSensor(this);
	    
	    // pooling client refresh (1000 ms)
	    final Refresher refresher = new Refresher();
        refresher.addListener(this);
        mainLayout.addComponent(refresher);
        
		// initialize datasource views
		initView();
				
	}
	
	private void initView() {
		// initialize Slide View Organization View
		dgMattrack.setImmediate(true);
		dgMattrack.setSelectable(true);
		dgMattrack.setPageLength(20);
		
		refreshBindindSource();
		
		// STEP 01: create grid view for slide Organization View
		try {			
			
			//counters = counterService.getAll();
			counters = getAllWeek(weeks);
			for (int i=0; i<counters.size(); i++) {
				if ((counters.get(i)).getWay() == IN)
					IN_Counts ++;
				else if ((counters.get(i)).getWay() == OUT)
					OUT_Counts ++;
			}
			
			countsTable.setCaption("RESUMEN CONTEOS");
			countsTable.addContainerProperty("Conteos Entrantes", Integer.class,  null);
			countsTable.addContainerProperty("Conteos Salientes", Integer.class, null);
			countsTable.addContainerProperty("Total conteos", Integer.class,  null);
			countsTable.addItem(new Object[] {new Integer(IN_Counts), new Integer(OUT_Counts), new Integer(counters.size())}, new Integer(1));
			
			dgMattrack.setCaption("DETALLE CONTEOS");
			dgMattrack.setBindingSource(bsCounter);
			dgMattrack.addGeneratedColumn(wayColumn.WAY_COLUMN_ID, new wayColumn());
			dgMattrack.setVisibleColumns(new String[] { "counterId", "countDate", "deviceName", wayColumn.WAY_COLUMN_ID });
			dgMattrack.setColumnHeaders(new String[] { "Identificador de conteo", "Fecha", "Dispositivo", "Sentido" });					

			dgMattrack.setColumnCollapsed("counterId", true);
			
		} catch (Exception ex) {
			throw new RuntimeException("¡No se pudo refrescar los Conteos!", ex);
		}

		// STEP 02: create toolbar for slide Employee Agent View
		navigationToolbar = new NavigationToolbar(0, bsCounter, viewContainer);
		boxToolbar = new BoxToolbar(1, bsCounter);
		counterToolbar = new CounterToolbar(2, null, viewContainer);
		
		navigationToolbar.addListenerRefreshButton(this);
		navigationToolbar.setUpButton(false);
		navigationToolbar.setDownButton(false);
		
		boxToolbar.addListenerFilterButton(this);
		boxToolbar.addListenerPrintButton(this);	
		
		counterToolbar.addListenerPreviousWeekButton(this);
		counterToolbar.addListenerNextWeekButton(this);
		
		removeAllToolbar();

		addToolbar(navigationToolbar);
		addToolbar(boxToolbar);
		addToolbar(counterToolbar);
		
		try {
			totlLayout.addComponent(graph());
		} catch (Exception ex) {
			throw new RuntimeException("¡No se pudo añadir la Gráfica de Conteos!", ex);
		}
		
		totlLayout.setExpandRatio(countsChart, 1.0f);
	}

	private void refreshTotals() {
		try {
			IN_Counts = 0;
			OUT_Counts = 0;
			
			countsTable.removeAllItems();
			
			//counters = counterService.getAll();
			counters = getAllWeek(weeks);
			for (int i=0; i<counters.size(); i++) {
				if ((counters.get(i)).getWay() == IN)
					IN_Counts++;
				else if ((counters.get(i)).getWay() == OUT)
					OUT_Counts++;
			}
			
			countsTable.addItem(new Object[] {new Integer(counters.size()), new Integer(IN_Counts), new Integer(OUT_Counts)}, new Integer(1));
				
		} catch (IllegalArgumentException e) {
			throw new RuntimeException("¡No se pudo refrescar los Conteos!", e);
		} catch (Exception e) {
			throw new RuntimeException("¡No se pudo refrescar los Conteos!", e);
		}
	}
	
	private InvientCharts graph() throws Exception {        
		chartConfig = new InvientChartsConfig();
		chartConfig.getGeneralChartConfig().setType(SeriesType.BAR);
		chartConfig.getTitle().setText("Histórico Conteos");
        chartConfig.getSubtitle().setText("Fuente: DEV01");
                
        CategoryAxis xAxisMain = new CategoryAxis();
        List<String> categories = counterService.getAllWeekDays(weeks);
        xAxisMain.setCategories(categories);
        LinkedHashSet<XAxis> xAxesSet = new LinkedHashSet<InvientChartsConfig.XAxis>();
        xAxesSet.add(xAxisMain);
        chartConfig.setXAxes(xAxesSet);

        NumberYAxis yAxis = new NumberYAxis();
        yAxis.setMin(0.0);
        yAxis.setTitle(new AxisTitle("Conteos"));
        yAxis.getTitle().setAlign(AxisTitleAlign.HIGH);
        LinkedHashSet<YAxis> yAxesSet = new LinkedHashSet<InvientChartsConfig.YAxis>();
        yAxesSet.add(yAxis);
        chartConfig.setYAxes(yAxesSet);

        chartConfig.getTooltip().setFormatterJsFunc("function() {"
                                + " return '' + this.series.name +': '+ this.y +' conteo';"
                                + "}");

        BarConfig barCfg = new BarConfig();
        barCfg.setDataLabel(new DataLabel());
        chartConfig.addSeriesConfig(barCfg);

        Legend legend = new Legend();
        legend.setLayout(Layout.VERTICAL);
        legend.setPosition(new Position());
        legend.getPosition().setAlign(HorzAlign.RIGHT);
        legend.getPosition().setVertAlign(VertAlign.TOP);
        legend.getPosition().setX(-100);
        legend.getPosition().setY(100);
        legend.setFloating(true);
        legend.setBorderWidth(1);
        legend.setBackgroundColor(new RGB(255, 255, 255));
        legend.setShadow(true);
        chartConfig.setLegend(legend);

        chartConfig.getCredit().setEnabled(false);

        countsChart = new InvientCharts(chartConfig);
        countsChart.setWidth("100.0%");
        countsChart.setHeight("100.0%");
        
        LinkedHashMap<String, Integer> resume = counterService.getAllWeekGrouped(weeks);
        
        XYSeries seriesData = new XYSeries("Conteos Entrantes");
        seriesData.setSeriesPoints(getINPoints(seriesData, resume));
        countsChart.addSeries(seriesData);

        seriesData = new XYSeries("Conteos Salientes");
        seriesData.setSeriesPoints(getOUTPoints(seriesData, resume));
        countsChart.addSeries(seriesData);
        
        return countsChart;
	}
	
	private static LinkedHashSet<DecimalPoint> getINPoints(Series series, LinkedHashMap<String, Integer> values) {
        LinkedHashSet<DecimalPoint> points = new LinkedHashSet<DecimalPoint>();
        
        Iterator<Entry<String, Integer>> it = values.entrySet().iterator();        
        while(it.hasNext()) {
        	 Map.Entry<String, Integer> entry = it.next();
        	 String aux =  entry.getKey().toString();
        	 String[] temp = aux.split("\\|");
        	 
        	 if (temp[1].equals("1"))
        		 points.add(new DecimalPoint(series, Double.parseDouble(entry.getValue().toString())));
        }
        	                      
        return points;
    }
	
	private void refreshSeries() {
		CategoryAxis xAxisMain = new CategoryAxis();
        List<String> categories;
		try {
			categories = counterService.getAllWeekDays(weeks);
		} catch (Exception ex) {
			throw new RuntimeException("¡No se pudo refrescar las Semana!", ex);
		}
        xAxisMain.setCategories(categories);
        LinkedHashSet<XAxis> xAxesSet = new LinkedHashSet<InvientChartsConfig.XAxis>();
        xAxesSet.add(xAxisMain);
        chartConfig.setXAxes(xAxesSet);
        
		countsChart.removeSeries("Conteos Entrantes");
		countsChart.removeSeries("Conteos Salientes");
		
		LinkedHashMap<String, Integer> resume;
		try {
			resume = counterService.getAllWeekGrouped(weeks);
		} catch (Exception ex) {
			throw new RuntimeException("¡No se pudo refrescar las Series!", ex);			
		}
        
        XYSeries seriesData = new XYSeries("Conteos Entrantes");
        seriesData.setSeriesPoints(getINPoints(seriesData, resume));
        countsChart.addSeries(seriesData);

        seriesData = new XYSeries("Conteos Salientes");
        seriesData.setSeriesPoints(getOUTPoints(seriesData, resume));
        countsChart.addSeries(seriesData);
	}
	
	private static LinkedHashSet<DecimalPoint> getOUTPoints(Series series, LinkedHashMap<String, Integer> values) {
        LinkedHashSet<DecimalPoint> points = new LinkedHashSet<DecimalPoint>();
        
        Iterator<Entry<String, Integer>> it = values.entrySet().iterator();        
        while(it.hasNext()) {
        	 Map.Entry<String, Integer> entry = it.next();
        	 String aux =  entry.getKey().toString();
        	 String[] temp = aux.split("\\|");
        	 
        	 if (temp[1].equals("0"))
        		 points.add(new DecimalPoint(series, Double.parseDouble(entry.getValue().toString())));
        }
        	              
        return points;
    }
	
	private void refreshBindindSource() {
		try {						
			bsCounter.removeAllItems();
			//bsCounter.addAll(counterService.getAll());
			bsCounter.addAll(getAllWeek(weeks));
	
		} catch (IllegalArgumentException e) {
			throw new RuntimeException("¡No se pudo refrescar los Conteos!", e);
		} catch (Exception e) {
			throw new RuntimeException("¡No se pudo refrescar los Conteos!", e);
		}
	}
	
	private List<Counter> getAllWeek(int week) {
		List<Counter> counts = new ArrayList<Counter>();
		
		try {
			counts = counterService.getAllWeek(week);
		} catch (Exception ex) {
			throw new RuntimeException("¡No se pudo crear los días de la semana!", ex);
		}
		
		return counts;
	}
	
	@Override
	public void refreshButtonClick(NavigationToolbar.ClickNavigationEvent event) {
		refreshBindindSource();
		refreshTotals();
		refreshSeries();
	}

	private class wayColumn implements ColumnGenerator {
		static final String WAY_COLUMN_ID = "way_column-id";

		@Override
		public Object generateCell(CustomTable source, Object itemId, Object columnId) {
			Label countsLabel = new Label();

			Counter counter = (Counter) itemId;

			if (counter.getWay() == IN)
				countsLabel.setValue("Entrante");				
			else if (counter.getWay() == OUT) 
				countsLabel.setValue("Saliente");

			return countsLabel;
		}

	}

	@Override
	public void filterButtonClick(BoxToolbar.ClickNavigationEvent event) {
		dgMattrack.setFilterBarVisible();
		
	}

	@Override
	public void printButtonClick(BoxToolbar.ClickNavigationEvent event) {
		try {
			dgMattrack.print("Maestro Conteos");
		}
		catch (Exception e) {
			throw new RuntimeException("¡No se pudo imprimir el informe!", e);
		}
		
	}

	@Override
	public void nextWeekButtonClick(ClickNavigationEvent event) {
		this.weeks = event.getWeek();
		
		refreshBindindSource();
		refreshTotals();
		refreshSeries();
		
	}

	@Override
	public void previousWeekButtonClick(ClickNavigationEvent event) {
		this.weeks = event.getWeek();
		
		refreshBindindSource();
		refreshTotals();
		refreshSeries();
		
	}
	
	@Override
	public void eventSensorClick(Counter event) {
		try {			
			refreshBindindSource();
			refreshTotals();
			refreshSeries();
			
			this.getWindow().showNotification("Dispositivo: " + event.getDeviceName());
			this.getWindow().requestRepaintAll();
			
			dgMattrack.setValue(bsCounter.lastItemId());
			dgMattrack.setCurrentPageFirstItemId(bsCounter.lastItemId());
		}
		catch(Exception ex) {
			throw new RuntimeException("¡No se pudo refrescar el conteo!", ex); 
		}
		
	}

	@Override
	public void refresh(Refresher source) {
		// Do nothing only polling (1000 millisecons) client refresh side 
		
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
		
		// workAreaLayout
		workAreaLayout = buildWorkAreaLayout();
		mainLayout.addComponent(workAreaLayout);
		mainLayout.setExpandRatio(workAreaLayout, 1.0f);
		
		return mainLayout;
	}

	@AutoGenerated
	private HorizontalLayout buildWorkAreaLayout() {
		// common part: create layout
		workAreaLayout = new HorizontalLayout();
		workAreaLayout.setImmediate(false);
		workAreaLayout.setWidth("100.0%");
		workAreaLayout.setHeight("100.0%");
		workAreaLayout.setMargin(true);
		workAreaLayout.setSpacing(true);
		
		// totlLayout
		totlLayout = buildTotlLayout();
		workAreaLayout.addComponent(totlLayout);
		workAreaLayout.setExpandRatio(totlLayout, 1.0f);
		
		// dgMattrack
		dgMattrack = new DataGridView()  {
			@Override
			protected String formatPropertyValue(Object rowId, Object colId, Property property) {
				// Format by property type
				if (property.getType() == Date.class) {
					SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

					if (property.getValue() != null)
						return df.format((Date) property.getValue());
				}

				return super.formatPropertyValue(rowId, colId, property);
			}
		};
		
		dgMattrack.setImmediate(false);
		dgMattrack.setWidth("550px");
		dgMattrack.setHeight("100.0%");
		workAreaLayout.addComponent(dgMattrack);
		
		return workAreaLayout;
	}

	@AutoGenerated
	private VerticalLayout buildTotlLayout() {
		// common part: create layout
		totlLayout = new VerticalLayout();
		totlLayout.setImmediate(false);
		totlLayout.setWidth("100.0%");
		totlLayout.setHeight("100.0%");
		totlLayout.setMargin(false);
		totlLayout.setSpacing(true);
		
		// countsTable
		countsTable = new DataGridView();
		countsTable.setImmediate(false);
		countsTable.setWidth("100.0%");
		countsTable.setHeight("50px");
		totlLayout.addComponent(countsTable);
				
		return totlLayout;
	}
	
}
