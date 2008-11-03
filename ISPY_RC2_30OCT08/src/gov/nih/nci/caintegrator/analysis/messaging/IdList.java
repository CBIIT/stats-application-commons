package gov.nih.nci.caintegrator.analysis.messaging;

import java.util.ArrayList;

public class IdList extends ArrayList<String> {
	
	private static final long serialVersionUID = 1L;
	private String name;
	
	public IdList(String name) {
		super();
		this.name = name;
	}
	
	public String getName() { return name; }
	
}
