package gov.nih.nci.caintegrator.application.dtobuilder;

import gov.nih.nci.caintegrator.dto.query.QueryDTO;


/**
 * This is the QueryBuilder interface.  It will take in an object
 * and build a QueryDTO from the fields on that object.
 *
 * @author caIntegrator Team
 */

public interface QueryDTOBuilder {
    public QueryDTO buildQueryDTO(Object form);
}
