package pwr.osm.buffer.server;

import java.io.PrintWriter;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.text.SimpleDateFormat;


public class Log
{
	private PrintWriter logFile;
	private SimpleDateFormat df = new SimpleDateFormat("[HH:mm:ss] ");

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
	public void info(String nfo)
	{
		writeLog("INFO: " + nfo);
	}
	public void error(String nfo)
	{
		writeLog("ERROR: " + nfo);
	}
	public void warn(String nfo)
	{
		writeLog("WARNING: " + nfo);
	}
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
