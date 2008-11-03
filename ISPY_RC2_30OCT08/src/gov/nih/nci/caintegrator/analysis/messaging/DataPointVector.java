package gov.nih.nci.caintegrator.analysis.messaging;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;

/**
 * This class holds a named list of data points. It is used to 
 * return expression values for a reporter.  The reporter name is set 
 * as the name of the vector and the DataPoint will contain the sample id
 * and the expression value. 
 * @author harrismic
 *
 */
public class DataPointVector implements Serializable {

	
	private static final long serialVersionUID = 1L;
	private List<DataPoint> dataPoints = new ArrayList<DataPoint>();
	private String name;
	
	
	public List<DataPoint> getDataPoints() {
		return dataPoints;
	}
	
	public  DataPointVector(String name) {
	  this.name = name;
	}
	
	public void setDataPoints(List<DataPoint> dataPoints) {
		this.dataPoints = dataPoints;
	}
	public String getName() {
		return name;
	}
	
	public void addDataPoint(DataPoint point) {
	  dataPoints.add(point);
	}
	
	public int size() {
	  return dataPoints.size();
	}
	
	public List<Double> getXValues() {
	  return getXValues(false);
	}
	
	/**
	 * Get a list of Doubles corresponding to the X values.
	 * @return
	 */	
	public List<Double> getXValues(boolean includeNulls) {
	  List<Double> xValues = new ArrayList<Double>();
	  Double val = null;
	  for (DataPoint point : dataPoints) {
		 if (point != null) {
		   val = point.getX();
		   if ((val != null)||(includeNulls)) {
	         xValues.add(point.getX());
		   }
		 }
		 else if (includeNulls){
		   xValues.add(null);
		 }
	  }
	  return xValues;
	}
	
	/**
	 * Get a list of Doubles corresponding to the Y values.
	 * @return
	 */
	public List<Double> getYValues(boolean includeNulls) {
	  List<Double> yValues = new ArrayList<Double>();
	  Double val = null;
	  for (DataPoint point : dataPoints) {
		 if (point != null) {
		   val = point.getY();
		   if ((val != null)||(includeNulls)) {
	         yValues.add(point.getY());
		   }
		 }
		 else if (includeNulls) {
		   yValues.add(null);
		 }
	  }
	  return yValues;
	}
	
	public List<Double> getYValues() {
	  return getYValues(false);
	}
	
	
	/**
	 * Get a list of Doubles corresponding to the Z values.
	 * @return
	 */
	public List<Double> getZValues(boolean includeNulls) {
	  List<Double> zValues = new ArrayList<Double>();
	  Double val = null;
	  for (DataPoint point : dataPoints) {
		 if (point != null) {
		   if ((val != null)||(includeNulls)) {
	         zValues.add(point.getZ());
		   }
		 }
		 else {
		   zValues.add(null);
		 }
	  }
	  return zValues;
	}
	
	public List<Double> getZValues() {
	  return getZValues(false);
	}

	public Double getMeanZ() {
	 
		return computeMean(getZValues());
		
	}

	public Double getMeanY() {
		
	  return computeMean(getYValues());
		
	}
	
	public Double getMeanX() {
		
		return computeMean(getXValues());
		
	}

	/**
	 * Compute the mean of the values in the list
	 * @param values
	 * @return
	 */
	private Double computeMean(List<Double> values) {
		
		DescriptiveStatistics stats = DescriptiveStatistics.newInstance(); 
		
//		 Add the data from the array
		for(Double value : values) {
		  stats.addValue(value);
		}
		double mean = stats.getMean();
		return new Double(mean);
	}

	public Double getStdDeviationX() {
		
		return computeStdDeviation(getXValues());
		
	}
	
	public Double getStdDeviationY() {
		
	  return computeStdDeviation(getYValues());
	  
	}
	
	public Double getStdDeviationZ() {
		
	  return computeStdDeviation(getZValues());
		
	}

	private Double computeStdDeviation(List<Double> values) {
		DescriptiveStatistics stats = DescriptiveStatistics.newInstance(); 
		
//		 Add the data from the array
		for(Double value : values) {
		  stats.addValue(value);
		}

//		 Compute some statistics 
		//double mean = stats.getMean();
//		double median = stats.getMedian();
		double std = stats.getStandardDeviation();
		return new Double(std);
		
	}

}
