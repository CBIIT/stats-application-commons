package gov.nih.nci.caintegrator.analysis.messaging;

/**
 * General purpose data point class. Used by the correlation result.
 * @author harrismic
 *
 */
public class DataPoint implements java.io.Serializable {
    
	private static final long serialVersionUID = 1L;
	
	private String id;
    private Double x = null;
    private Double y = null;
    private Double z = null;
  
    public DataPoint(String id) {
      this.id = id;
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
    
    public String toString() {
      return "id=" + id + " (" + x + "," + y + "," + z + ")";
    }
  
  
  
}
