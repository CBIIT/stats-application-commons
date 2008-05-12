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


package gov.nih.nci.caintegrator.application.gpvisualizer.heatmapviewer;

public class CaIntegratorRunVisualizerConstants {
	public static final String JAVA_FLAGS_VALUE = "visualizer_java_flags";
	public static final String JAVA_FLAGS_NAME = "java_flags";


	public static String PARAM_NAMES = "gp_paramNames"; // CSV list of input
														// parameter names for
														// this task

	public static String SUPPORT_FILE_NAMES = "gp_filenames"; // array of names
															  // of support
															  // files

	public static String SUPPORT_FILE_DATES = "gp_fileDates"; // File.lastModified[]
															  // for each entry
															  // in
															  // SUPPORT_FILE_NAMES

	public static String DOWNLOAD_FILES = "gp_download"; // PARAM_NAMES elements
														 // that need to be
														 // downloaded by the
														 // client

	public static String LIBDIR = "libdir"; // taskLib directory on the server
											// for this task

	public static String COMMAND_LINE = "commandLine"; // TaskInfoAttributes.COMMAND_LINE
													   // for this task

	public static String NAME = "name"; // TaskInfo.getName() for this task

	public static String OS = "gp_os"; // TaskInfoAttributes.OS for this task

	public static String CPU_TYPE = "gp_cpuType"; // TaskInfoAttributes.CPU_TYPE
												  // for this task

	public static String DEBUG = "DEBUG"; // flag to turn on debug output when
										  // defined

	public static String LSID = "gp_lsid"; // TaskInfoAttributes.LSID for this
										   // task

	public static String NO_RUN = "NO_RUN"; // load the applet, but don't start
											// it
}