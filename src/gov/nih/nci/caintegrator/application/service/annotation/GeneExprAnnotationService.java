package gov.nih.nci.caintegrator.application.service.annotation;

import gov.nih.nci.caintegrator.application.service.ApplicationService;
import java.util.List;
import java.util.Map;

/**
 * This interface can be implemented by applications
 * to provide an annotation service.
 * @author harrismic
 *
 */

public abstract class GeneExprAnnotationService extends ApplicationService {

	public abstract GeneExprAnnotationService getInstance();
	
	public abstract Map <String,ReporterResultset> getAnnotationsMapForReporters(List<String> reporterIDs) throws Exception;
	
}