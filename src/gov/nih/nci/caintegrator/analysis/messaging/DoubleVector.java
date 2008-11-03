package gov.nih.nci.caintegrator.analysis.messaging;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class DoubleVector implements Serializable {

	private String name;
	private List<Double> values = Collections.emptyList();
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DoubleVector(String name, List<Double> values) {
	  this.name = name;
	  this.values = values;
	}

	public String getName() {
		return name;
	}

	public List<Double> getValues() {
		return values;
	}
	
	public int size() {
      if (values != null) {
        return values.size();
      }
      return -1;
	}
	
	public String toString() {
	  
	  return name + " size=" + size();
	}
	
}
