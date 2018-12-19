package com.kwchina.webmail.logger;


/**
 * Logger.java 
 */
/**
 * 异步的Logger,接收log消息到队列，然后把这些消息写入log文件
 *
 */
public interface Logger {
    
   
    public void log(int level, String message);

    public void log(int level, Exception ex);

    public void shutdown();


}
