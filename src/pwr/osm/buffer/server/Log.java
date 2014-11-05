package pwr.osm.buffer.server;

import java.io.PrintWriter;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * Simple logging class.
 * @author Sobot
 *
 */
public class Log{
	
	private PrintWriter logFile;
	private SimpleDateFormat df = new SimpleDateFormat("[HH:mm:ss] ");
	
	/**
	 * Writes log to file.
	 * @param nfo information to log
	 */
	public void writeLog(String nfo)
	{
		Date time = new Date();
		try
		{
			logFile = new PrintWriter(new BufferedWriter(new FileWriter("LOG.txt", true)));
		} catch (IOException e) {
			System.err.println("IOException: " + e.getMessage());
		}
		logFile.println(df.format(time) + nfo);
		logFile.close();
	}
	
	/**
	 * Logs information.
	 * @param nfo information message
	 */
	public void info(String nfo)
	{
		writeLog("INFO: " + nfo);
	}
	
	/**
	 * Logs error.
	 * @param err error message
	 */
	public void error(String err)
	{
		writeLog("ERROR: " + err);
	}
	
	/**
	 * Logs warning.
	 * @param warn warning message
	 */
	public void warn(String warn)
	{
		writeLog("WARNING: " + warn);
	}
	
	/**
	 *	Log application start log.
	 */
    public void onStart()
    {
    	Date time = new Date();
		try
		{
			logFile = new PrintWriter(new BufferedWriter(new FileWriter("LOG.txt", true)));
		} catch (IOException e) {
			System.err.println("IOException: " + e.getMessage());
		}
    	logFile.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
    	logFile.println(df.format(time) + "INFO: Program Start");
    	logFile.close();
    }
    
    /**
     * For application close log.
     */
    public void onClose()
    {
    	Date time = new Date();
		try
		{
			logFile = new PrintWriter(new BufferedWriter(new FileWriter("LOG.txt", true)));
		} catch (IOException e) {
			System.err.println("IOException: " + e.getMessage());
		}
    	logFile.println(df.format(time) + "INFO: Program End");
    	logFile.println("-----------------------------------------------------------");
    	logFile.close();
    }
}
