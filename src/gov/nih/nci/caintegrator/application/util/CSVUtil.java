/*L
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/stats-application-commons/LICENSE.txt for details.
 */

package gov.nih.nci.caintegrator.application.util;

import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

public class CSVUtil {


    public static void renderCSV(HttpServletResponse response, List<List> csv)  {
        PrintWriter out = null;
        
        long randomness = System.currentTimeMillis();
        response.setContentType("application/csv");
        response.setHeader("Content-Disposition", "attachment; filename=report_"+randomness+".csv");


        try {
            for(List row : csv) {
    
                out = response.getWriter();
                out.write(StringUtils.join(row.toArray(), ",") + "\r\n");
                out.flush();
            }
        }
        catch(Exception e)  {
            out.write("error generating report");
        }
    }
}
