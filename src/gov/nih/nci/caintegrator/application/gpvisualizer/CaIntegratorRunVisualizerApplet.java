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
/**
* caIntegrator License
* 
* Copyright 2001-2005 Science Applications International Corporation ("SAIC"). 
* The software subject to this notice and license includes both human readable source code form and machine readable, 
* binary, object code form ("the caIntegrator Software"). The caIntegrator Software was developed in conjunction with 
* the National Cancer Institute ("NCI") by NCI employees and employees of SAIC. 
* To the extent government employees are authors, any rights in such works shall be subject to Title 17 of the United States
* Code, section 105. 
* This caIntegrator Software License (the "License") is between NCI and You. "You (or "Your") shall mean a person or an 
* entity, and all other entities that control, are controlled by, or are under common control with the entity. "Control" 
* for purposes of this definition means (i) the direct or indirect power to cause the direction or management of such entity,
*  whether by contract or otherwise, or (ii) ownership of fifty percent (50%) or more of the outstanding shares, or (iii) 
* beneficial ownership of such entity. 
* This License is granted provided that You agree to the conditions described below. NCI grants You a non-exclusive, 
* worldwide, perpetual, fully-paid-up, no-charge, irrevocable, transferable and royalty-free right and license in its rights 
* in the caIntegrator Software to (i) use, install, access, operate, execute, copy, modify, translate, market, publicly 
* display, publicly perform, and prepare derivative works of the caIntegrator Software; (ii) distribute and have distributed 
* to and by third parties the caIntegrator Software and any modifications and derivative works thereof; 
* and (iii) sublicense the foregoing rights set out in (i) and (ii) to third parties, including the right to license such 
* rights to further third parties. For sake of clarity, and not by way of limitation, NCI shall have no right of accounting
* or right of payment from You or Your sublicensees for the rights granted under this License. This License is granted at no
* charge to You. 
* 1. Your redistributions of the source code for the Software must retain the above copyright notice, this list of conditions
*    and the disclaimer and limitation of liability of Article 6, below. Your redistributions in object code form must reproduce 
*    the above copyright notice, this list of conditions and the disclaimer of Article 6 in the documentation and/or other materials
*    provided with the distribution, if any. 
* 2. Your end-user documentation included with the redistribution, if any, must include the following acknowledgment: "This 
*    product includes software developed by SAIC and the National Cancer Institute." If You do not include such end-user 
*    documentation, You shall include this acknowledgment in the Software itself, wherever such third-party acknowledgments 
*    normally appear.
* 3. You may not use the names "The National Cancer Institute", "NCI" "Science Applications International Corporation" and 
*    "SAIC" to endorse or promote products derived from this Software. This License does not authorize You to use any 
*    trademarks, service marks, trade names, logos or product names of either NCI or SAIC, except as required to comply with
*    the terms of this License. 
* 4. For sake of clarity, and not by way of limitation, You may incorporate this Software into Your proprietary programs and 
*    into any third party proprietary programs. However, if You incorporate the Software into third party proprietary 
*    programs, You agree that You are solely responsible for obtaining any permission from such third parties required to 
*    incorporate the Software into such third party proprietary programs and for informing Your sublicensees, including 
*    without limitation Your end-users, of their obligation to secure any required permissions from such third parties 
*    before incorporating the Software into such third party proprietary software programs. In the event that You fail 
*    to obtain such permissions, You agree to indemnify NCI for any claims against NCI by such third parties, except to 
*    the extent prohibited by law, resulting from Your failure to obtain such permissions. 
* 5. For sake of clarity, and not by way of limitation, You may add Your own copyright statement to Your modifications and 
*    to the derivative works, and You may provide additional or different license terms and conditions in Your sublicenses 
*    of modifications of the Software, or any derivative works of the Software as a whole, provided Your use, reproduction, 
*    and distribution of the Work otherwise complies with the conditions stated in this License.
* 6. THIS SOFTWARE IS PROVIDED "AS IS," AND ANY EXPRESSED OR IMPLIED WARRANTIES, (INCLUDING, BUT NOT LIMITED TO, 
*    THE IMPLIED WARRANTIES OF MERCHANTABILITY, NON-INFRINGEMENT AND FITNESS FOR A PARTICULAR PURPOSE) ARE DISCLAIMED. 
*    IN NO EVENT SHALL THE NATIONAL CANCER INSTITUTE, SAIC, OR THEIR AFFILIATES BE LIABLE FOR ANY DIRECT, INDIRECT, 
*    INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE 
*    GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF 
*    LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT 
*    OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
* 
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