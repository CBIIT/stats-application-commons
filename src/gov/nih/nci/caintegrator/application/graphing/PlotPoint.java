package gov.nih.nci.caintegrator.application.graphing;

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
  
}
