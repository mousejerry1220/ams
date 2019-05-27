package com.sinotrans.ams.common.utils;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

public class ExceptionUtils {

	public static String getExceptionStackTrace(Exception e) {
		PrintWriter ps = null;
		String error = null;
		try{
			ByteArrayOutputStream buff = new ByteArrayOutputStream();
			ps = new PrintWriter(buff);
			e.printStackTrace(ps);
			ps.flush();
			error = new String(buff.toByteArray());
		} finally {
			ps.close();
		}
		return error;
	}
	
}
