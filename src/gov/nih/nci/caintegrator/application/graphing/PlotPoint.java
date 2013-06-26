/*L
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/stats-application-commons/LICENSE.txt for details.
 */

package gov.nih.nci.caintegrator.application.graphing;

import gov.nih.nci.caintegrator.enumeration.AxisType;
import gov.nih.nci.caintegrator.ui.graphing.data.DataRange;

import java.util.Collection;

/**
 * Simple Data point class for handling data of the form id,X,Y,Z
 * @author harrismic
 *
 */
public class PlotPoint {
  private String id;
  private Double x = null;
  private Double y = null;
  private Double z = null;
  private String tag = null;
  
  /**
   * 
   * @param id
   */
  public PlotPoint(String id) {
    this.id = id;
  }
  
  /**
   * 
   * @param x
   * @param y
   * @param z
   */
  public void setData(Double x, Double y, Double z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

	public Double getX() {
		return x;
	}
	
	public void setX(Double x) {
		this.x = x;
	}
	
	public Double getY() {
		return y;
	}
	
	public void setY(Double y) {
		this.y = y;
	}
	
	public Double getZ() {
		return z;
	}
	
	public void setZ(Double z) {
		this.z = z;
	}
	
	public String getId() {
		return id;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}
	
	public Double getAxisValue(AxisType axis) {
	  if (axis == AxisType.X_AXIS) {
	    return getX();
	  }
	  else if (axis == AxisType.Y_AXIS) {
		return getY();
	  }
	  else if (axis == AxisType.Z_AXIS) {
		return getZ();
	  }
	  return null;
	}
	
	public static DataRange getDataRange(Collection<PlotPoint> points, AxisType axis) {
		double maxValue = Double.MIN_VALUE;
		double minValue = Double.MAX_VALUE;
		double value;
		  
		for (PlotPoint dataPoint: points) {
			value = dataPoint.getAxisValue(axis);
			if (value < minValue) {
			  minValue = value;
			}
			
			if (value > maxValue) {
			  maxValue = value;
			}
		  }
		  
		  DataRange range = new DataRange(minValue, maxValue);
		  return range;
	}
  
}
