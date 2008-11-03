package gov.nih.nci.caintegrator.analysis.messaging;

import java.util.Comparator;

/**
 * This comparator will order a collection of DataPointVectors by mean value.
 * It is currently used to sort DataPointVectors representing gene expression of 
 * reporters by mean expression value.  Reporters with higher mean expression are 
 * ordered ahead of reporters with lower expression values.
 * @author harrismic
 *
 */
public class DataPointVectorMeanComparator implements Comparator {

	private DataPointComponent component;
	
	
	public DataPointVectorMeanComparator(DataPointComponent component) {
	  this.component = component;
	}

	public int compare(Object o1, Object o2) {
		DataPointVector v1 = (DataPointVector) o1;
		DataPointVector v2 = (DataPointVector) o2;
		
		double val1 = -1;
		double val2 = -1;
		
		if (component == DataPointComponent.X) {
		  val1 = v1.getMeanX();	
		  val2 = v2.getMeanX();
		}
		else if (component == DataPointComponent.Y) {
		  val1 = v1.getMeanY();
		  val2 = v2.getMeanY();
		}
		else if (component == DataPointComponent.Z){
		  val1 = v1.getMeanZ();
		  val2 = v2.getMeanZ();
		}
		
		
		if (val1 > val2) return -1;
		if (val2 > val1) return 1;
		return 0;
			
	}

}