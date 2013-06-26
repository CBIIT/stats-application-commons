/*L
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/stats-application-commons/LICENSE.txt for details.
 */

/*
 The Broad Institute
 SOFTWARE COPYRIGHT NOTICE AGREEMENT
 This software and its documentation are copyright (2003-2006) by the
 Broad Institute/Massachusetts Institute of Technology. All rights are
 reserved.

 This software is supplied without any warranty or guaranteed support
 whatsoever. Neither the Broad Institute nor MIT can be responsible for its
 use, misuse, or functionality.
 */

package gov.nih.nci.caintegrator.application.gpvisualizer;

import java.applet.Applet;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import org.genepattern.visualizer.RunVisualizerConstants;


public class CaIntegratorRunVisualizerApplet extends Applet {

    // get all of the applet input parameters and create a HashMap of them that
    // can be used to
    // invoke the visualizer in a non-applet-specific manner

    Map<String, String> params = new HashMap<String, String>();

    String[] supportFileNames = new String[0];

    long[] supportFileDates = new long[0];
    
    // the static string for gene pattern server url ------- CaIntegrator
    public static final String SUPPORT_FILE_URL = "supportFileURL";
    public static final String TICKET_STRING = "ticketString";
    public static final String MODULE_NAME = "moduleName";
    // end of CaIntegrator variable.
    
    //wellKnownNames also includes a specific string GP_SERVER ------ CaIntegrator
    String[] wellKnownNames = { SUPPORT_FILE_URL, TICKET_STRING, MODULE_NAME, RunVisualizerConstants.NAME, RunVisualizerConstants.COMMAND_LINE,
            RunVisualizerConstants.DEBUG, RunVisualizerConstants.OS, RunVisualizerConstants.CPU_TYPE,
            RunVisualizerConstants.LIBDIR, RunVisualizerConstants.DOWNLOAD_FILES, RunVisualizerConstants.LSID };

    URL source = null;
    //No change in this method for CaIntegrator
    public void init() {
        try {
            source = getDocumentBase();

            for (int i = 0; i < wellKnownNames.length; i++) {
                setParameter(wellKnownNames[i], getParameter(wellKnownNames[i]));
            }
            setParameter(RunVisualizerConstants.JAVA_FLAGS_NAME, getParameter(RunVisualizerConstants.JAVA_FLAGS_VALUE));
            StringTokenizer stParameterNames = new StringTokenizer(getParameter(RunVisualizerConstants.PARAM_NAMES),
                    ", ");
            while (stParameterNames.hasMoreTokens()) {
                String paramName = stParameterNames.nextToken();
                String paramValue = getParameter(paramName);
                setParameter(paramName, paramValue);
            }

            setSupportFileNames(getParameter(RunVisualizerConstants.SUPPORT_FILE_NAMES));
            setSupportFileDates(getParameter(RunVisualizerConstants.SUPPORT_FILE_DATES));

            //if (getParameter(RunVisualizerConstants.NO_RUN) == null) {
                run();
           // } else {
           //     showStatus("No run flag set");
           // }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
    
    //There are changes in this method for CaIntegrator
    @Override
    public String getParameter(String name) {
        String value = super.getParameter(name);
        if (value == null) {
            return null;
        }
        try {
        	//CaIntegrator specific code: don't decode ticketString
        	if (name.equalsIgnoreCase(TICKET_STRING))
        		return value;
        	else
        		return URLDecoder.decode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return value;
        }
    }
    
    //  No change in this method for CaIntegrator
    public void setParameter(String name, String value) {
        params.put(name, value);
    }
    
    //  No change in this method for CaIntegrator
    public void setSupportFileNames(String csvNames) {
        StringTokenizer stFileNames = new StringTokenizer(csvNames, ",");
        supportFileNames = new String[stFileNames.countTokens()];
        int f = 0;
        while (stFileNames.hasMoreTokens()) {
            supportFileNames[f] = stFileNames.nextToken();
            f++;
        }
    }
    
    //  No change in this method for CaIntegrator
    public void setSupportFileDates(String csvDates) throws NumberFormatException {
        StringTokenizer stFileDates = new StringTokenizer(csvDates, ",");
        supportFileDates = new long[supportFileNames.length];
        int f = 0;
        while (stFileDates.hasMoreTokens()) {
            supportFileDates[f] = Long.parseLong(stFileDates.nextToken());
            f++;
        }
    }
    
    //  No change in this method for CaIntegrator
    public void run() throws Exception {
        validateInputs();
        showStatus("starting " + params.get(RunVisualizerConstants.NAME));

        showStatus("JAVA_FLAGS=" + params.get("java_flags"));

        CaIntegratorRunVisualizer visualizer = new CaIntegratorRunVisualizer(params, supportFileNames, supportFileDates, this);
        visualizer.run();
    }
    //  No change in this method for CaIntegrator
    protected void validateInputs() throws Exception {
        // make sure that each support file has an associated length
        if (supportFileNames.length != supportFileDates.length)
            throw new Exception("Mismatched number of support file names and dates");
        // make sure that all expected mandatory inputs are there
        for (int i = 0; i < wellKnownNames.length; i++) {
            if (params.get(wellKnownNames[i]) == null)
                throw new Exception("Missing input parameter " + wellKnownNames[i]);
        }
    }
    //  No change in this method for CaIntegrator
    public void destroy() {
        showStatus("CaIntegratorRunVisualizerApplet.destroy");
        super.destroy();
    }
    //  No change in this method for CaIntegrator
    public void start() {
        showStatus("CaIntegratorRunVisualizerApplet.start");
        super.start();
    }
    //  No change in this method for CaIntegrator
    public void stop() {
        showStatus("CaIntegratorRunVisualizerApplet.stop");
        super.stop();
    }

}