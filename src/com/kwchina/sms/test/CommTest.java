package com.kwchina.sms.test;

// JindiSMS v3.1 
// 该程序主要用于GSM Modem 串口连接速率测试。
// 金笛短信网 www.sendsms.cn
// 
// import gnu.io.*;
import javax.comm.*;
import java.util.*;
import java.io.*;

public class CommTest
{
	static CommPortIdentifier portId;
	static Enumeration portList;
	static int bauds[] = { 9600, 19200, 57600, 115200 };

	public static void main(String[] args)
	{
		portList = CommPortIdentifier.getPortIdentifiers();
		System.out.println("短信设备端口连接测试...");
		while (portList.hasMoreElements())
		{
			portId = (CommPortIdentifier) portList.nextElement();
			System.out.println("找到端口: " + portId.getName());
			if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL)
			{
				for (int i = 0; i < bauds.length; i++)
				{
					System.out.print("	Trying at " + bauds[i] + "...");
					try
					{
						SerialPort serialPort;
						InputStream inStream;
						OutputStream outStream;
						int c;
						String response;
						serialPort = (SerialPort) portId.open("SMSLibCommTester", 1971);
						serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN);
						serialPort.setSerialPortParams(bauds[i], SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
						inStream = serialPort.getInputStream();
						outStream = serialPort.getOutputStream();
						serialPort.enableReceiveTimeout(1000);
						c = inStream.read();
						while (c != -1)
							c = inStream.read();
						outStream.write('A');
						outStream.write('T');
						outStream.write('\r');
						try
						{
							Thread.sleep(1000);
						}
						catch (Exception e)
						{
						}
						response = "";
						c = inStream.read();
						while (c != -1)
						{
							response += (char) c;
							c = inStream.read();
						}
						if (response.indexOf("OK") >= 0)
						{
							try
							{
								System.out.print("  获取设备信息...");
								outStream.write('A');
								outStream.write('T');
								outStream.write('+');
								outStream.write('C');
								outStream.write('G');
								outStream.write('M');
								outStream.write('M');
								outStream.write('\r');
								response = "";
								c = inStream.read();
								while (c != -1)
								{
									response += (char) c;
									c = inStream.read();
								}
								System.out.println("  发现设备: " + response.replaceAll("\\s+OK\\s+", "").replaceAll("\n", "").replaceAll("\r", ""));
							}
							catch (Exception e)
							{
								System.out.println("  没有发现设备!");
							}
						}
						else System.out.println("  没有发现设备!");
						serialPort.close();
					}
					catch (Exception e)
					{
						System.out.println("  没有发现设备!");
					}
				}
			}
		}
	}
}
