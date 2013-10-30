package com.zainmanji.sportsrefresh;

import java.io.IOException;
import java.io.InputStream;

//Help from javacodegeeks.com - open source
public class Utils {
 
	 //Close input stream
	 public static void closeStream(InputStream inputStream) {
		  try {
			  if (inputStream != null) {
				  inputStream.close();  
			  }
		  } catch (IOException e) {
		   // ignore exception
		  }
	 }

}