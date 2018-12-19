package com.kwchina.webmail.logger;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.kwchina.webmail.config.ConfigurationListener;
import com.kwchina.webmail.misc.Queue;
import com.kwchina.webmail.server.WebMailServer;
import com.kwchina.webmail.storage.Storage;

/**
 * Logger.java
 * 
 */

public class FileLogger extends Thread implements ConfigurationListener, Logger {
    
    private DateFormat df=null;

    protected PrintWriter logout;
    protected int loglevel;

    protected Queue queue;
    protected Queue time_queue;

    protected boolean do_shutdown=false;

    protected WebMailServer parent;
    protected Storage store;

    protected int interval;

    public FileLogger(WebMailServer parent, Storage st) {
	super("FileLogger Thread");
	this.parent=parent;
	this.store=st;
	parent.getConfigScheme().configRegisterIntegerKey(this,"LOGLEVEL","5",
							  "How much debug output will be written in the logfile");
	parent.getConfigScheme().configRegisterStringKey(this,"LOGFILE",
							 parent.getProperty("webmail.data.path")+
							 System.getProperty("file.separator")+
							 "webmail.log",
							 "WebMail logfile");
	parent.getConfigScheme().configRegisterIntegerKey(this,"LOG INTERVAL","5",
							  "Interval used for flushing the log buffer"+
							  " in seconds. Log messages of level CRIT or"+
							  " ERR will be written immediately in any way.");
	initLog();
	queue=new Queue();
	time_queue=new Queue();
	this.start();
    }
    
    protected void initLog() {
	System.err.print("  * Logfile ... ");
	try {
	    loglevel=Integer.parseInt(store.getConfig("LOGLEVEL"));
	} catch(NumberFormatException e) {}
	try {
	    interval=Integer.parseInt(store.getConfig("LOG INTERVAL"));
	} catch(NumberFormatException e) {}
	try {
	    String filename=store.getConfig("LOGFILE");
	    logout=new PrintWriter(new FileOutputStream(filename,true));
	    System.err.println("initalization complete: "+filename+", Level "+loglevel);
	} catch(IOException ex) {
	    ex.printStackTrace();
	    logout=new PrintWriter(System.out);
	    System.err.println("initalization complete: Sending to STDOUT, Level "+loglevel);
	}
    }	

    protected String formatDate(long date) {
	if(df==null) {
	    TimeZone tz=TimeZone.getDefault();
	    df=DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.DEFAULT, Locale.getDefault());
	    df.setTimeZone(tz);
	}
	String now=df.format(new Date(date));
	return now;
    }

    protected void writeMessage(long time, String message) {
	logout.println("["+formatDate(time)+"] - "+message);
   }
    

    public void log(int level, String message) {
	if(level <= loglevel) {
	    String s="LEVEL "+level;
	    switch(level) {
	    case Storage.LOG_DEBUG: s="DEBUG   "; break;
	    case Storage.LOG_INFO: s="INFO    "; break;
	    case Storage.LOG_WARN: s="WARNING "; break;
	    case Storage.LOG_ERR: s="ERROR   "; break;
	    case Storage.LOG_CRIT: s="CRITICAL"; break;
	    }
	    queue(System.currentTimeMillis(),s+" - "+message);
	    if(level <= Storage.LOG_ERR) {
		flush();
	    }
	}
    }

    public void log(int level, Exception ex) {
	if(level <= loglevel) {
	    String s="LEVEL "+level;
	    switch(level) {
	    case Storage.LOG_DEBUG: s="DEBUG   "; break;
	    case Storage.LOG_INFO: s="INFO    "; break;
	    case Storage.LOG_WARN: s="WARNING "; break;
	    case Storage.LOG_ERR: s="ERROR   "; break;
	    case Storage.LOG_CRIT: s="CRITICAL"; break;
	    }
	    StringWriter message=new StringWriter();
	    ex.printStackTrace(new PrintWriter(message));
	    queue(System.currentTimeMillis(),s+" - "+message.toString());
	    if(level <= Storage.LOG_ERR) {
		flush();
	    }
	}
    }


    protected void flush() {
	while(!queue.isEmpty()) {
	    Long l=(Long)time_queue.next();
	    String s=(String)queue.next();
	    writeMessage(l.longValue(),s);
	}
	logout.flush();
    }	

    public void queue(long time, String message) {
	queue.queue(message);
        time_queue.queue(new Long(time));
    }

    public void notifyConfigurationChange(String key) {
	initLog();
    }	

    public void shutdown() {
	flush();
	do_shutdown=true;
    }

    public void run() {
	while(!do_shutdown) {
	    flush();
	    try {
		sleep(interval*1000);
	    } catch(InterruptedException e) {}
	}
    }
} 
