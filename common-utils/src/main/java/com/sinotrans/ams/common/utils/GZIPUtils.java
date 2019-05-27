package com.sinotrans.ams.common.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

 
public class GZIPUtils {  
	
    /** 
     * 字符串压缩为GZIP字节数组 
     *  
     * @param str 
     * @param encoding 
     * @return 
     */  
    public static byte[] compress(byte[] bytes) {  
        ByteArrayOutputStream out = new ByteArrayOutputStream();  
        GZIPOutputStream gzip = null;  
        try {  
            gzip = new GZIPOutputStream(out);  
            gzip.write(bytes);  
            return out.toByteArray();  
        } catch (IOException e) {  
        	throw new RuntimeException("GZIP压缩error", e);
        } finally{
        	try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        	
        	if(gzip!=null){
        		try {
					gzip.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
        	}
        }
       
    }  
  
    /** 
     * GZIP解压缩 
     *  
     * @param bytes 
     * @return 
     */  
    public static byte[] uncompress(InputStream in) {  
        ByteArrayOutputStream out = new ByteArrayOutputStream();  
        GZIPInputStream ungzip = null;
        try {  
            ungzip = new GZIPInputStream(in);  
            byte[] buffer = new byte[256];  
            int n;  
            while ((n = ungzip.read(buffer)) >= 0) {  
                out.write(buffer, 0, n);  
            }  
            out.flush();
            return out.toByteArray();  
        } catch (Exception e) {  
        	throw new RuntimeException("GZIP解压error", e);
        } finally{
        	try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        	
        	if(ungzip != null){
        		try {
					ungzip.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
        	}
        }
    }
    
    /** 
     * GZIP解压缩 
     *  
     * @param bytes 
     * @return 
     */  
    public static byte[] uncompress(byte[] bytes) {  
    	return uncompress(new ByteArrayInputStream(bytes));
    }  
}
