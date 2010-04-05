package gov.nih.nci.caintegrator.application.schedule;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * The DeleteOldResultsFilesJob class is the job to delete all saved results based on the file retention period. 
 * This is a quartz job that will * be scheduled to run on a daily basis.
 * <P>
 */
public class DeleteOldFilesJob implements Job
{
	private static Logger logger = Logger.getLogger(DeleteOldFilesJob.class);


	public void execute(JobExecutionContext context) throws JobExecutionException
	{
		// Set the thread priority to a nicer value for background processing
		Thread.currentThread().setPriority(4);
		String fileRetentionPeriodInDays = context.getJobDetail().getJobDataMap().getString(
		"fileRetentionPeriodInDays");
		String dirPath = context.getJobDetail().getJobDataMap().getString(
		"dirPath");
		if(fileRetentionPeriodInDays != null && dirPath != null){
			deleteFiles(dirPath, fileRetentionPeriodInDays);
		}

	}
	private void deleteFiles(String dirPath,String fileRetentionPeriodInDays){

		int fileRetention = 5;
		if(fileRetention == 0){
			try {
				fileRetention = Integer.parseInt(fileRetentionPeriodInDays.trim());
			} catch (NumberFormatException e) {
				 fileRetention = 5;
			}
		}
		File dataDirecory = new File(dirPath);
        Date currentDate = new Date();
		 if (dataDirecory.exists()  && dataDirecory.isDirectory()){
		    
		    // Loop thru files and directories in this path
		    String[] files = dataDirecory.list();
		    for (String filename : files) {
		      File file = new File(dirPath, filename);
		      if (file.isFile()) {
		    	 long timestamp = file.lastModified();
		 		 Date fileDate = new Date(timestamp);
		 		 int noOfDaysSinceFileCreated = subtractDays(currentDate,fileDate);
		 		 if(noOfDaysSinceFileCreated > fileRetention){
		 			 //Delete file
			 		 SimpleDateFormat sdf = new SimpleDateFormat( "EEEE yyyy/MM/dd hh:mm:ss aa zz : zzzzzz" );
			 		 sdf.setTimeZone(TimeZone.getDefault()); // local time
			 		 String display = sdf.format(fileDate);
			 		 System.out.println("Deleted File:"+file.getName()+" Date:"+ display);
		 			 file.delete();		 			 
		 		 }
		      }
		    }
		 }

		 
		 File f = new File( "abc.txt" );
		 long timestamp = f.lastModified();
		 Date when = new Date(timestamp);
		 SimpleDateFormat sdf = new SimpleDateFormat( "EEEE yyyy/MM/dd hh:mm:ss aa zz : zzzzzz" );
		 sdf.setTimeZone(TimeZone.getDefault()); // local time
		 String display = sdf.format(when);
	}
	private static int subtractDays(Date date1, Date date2)
	  {
	    GregorianCalendar gc1 = new GregorianCalendar();  gc1.setTime(date1);
	    GregorianCalendar gc2 = new GregorianCalendar();  gc2.setTime(date2);

	    int days1 = 0;
	    int days2 = 0;
	    int maxYear = Math.max(gc1.get(Calendar.YEAR), gc2.get(Calendar.YEAR));

	    GregorianCalendar gctmp = (GregorianCalendar) gc1.clone();
	    for (int f = gctmp.get(Calendar.YEAR);  f < maxYear;  f++)
	      {days1 += gctmp.getActualMaximum(Calendar.DAY_OF_YEAR);  gctmp.add(Calendar.YEAR, 1);}

	    gctmp = (GregorianCalendar) gc2.clone();
	    for (int f = gctmp.get(Calendar.YEAR);  f < maxYear;  f++)
	      {days2 += gctmp.getActualMaximum(Calendar.DAY_OF_YEAR);  gctmp.add(Calendar.YEAR, 1);}

	    days1 += gc1.get(Calendar.DAY_OF_YEAR) - 1;
	    days2 += gc2.get(Calendar.DAY_OF_YEAR) - 1;

	    return (days1 - days2);
	  }  
	
}
