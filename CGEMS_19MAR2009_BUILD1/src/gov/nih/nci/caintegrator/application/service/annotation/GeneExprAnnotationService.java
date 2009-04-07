package gov.nih.nci.caintegrator.application.service.annotation;

import gov.nih.nci.caintegrator.application.service.ApplicationService;
import gov.nih.nci.caintegrator.enumeration.ArrayPlatformType;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This interface can be implemented by applications
 * to provide an annotation service.
 * @author harrismic
 *
 */

public interface GeneExprAnnotationService extends ApplicationService {

	public Map <String,ReporterAnnotation> getAnnotationsMapForReporters(List<String> reporterIDs) throws Exception;

	public List <ReporterAnnotation> getAnnotationsListForReporters(List<String> reporterIDs) throws Exception;

    public Collection<String> getReporterNamesForGeneSymbols(Collection<String> stringList, ArrayPlatformType arrayType);

    public Set<ReporterAnnotation> getReportersForGeneSymbol(String geneSymbol);

}