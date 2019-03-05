package com.ceshi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

public class create_concent {
	
	
	public static void compressFile(String inFileName,String outFileName) {
	    outFileName = outFileName+".gz";
        FileInputStream in = null;
        try {
            in = new FileInputStream(new File(inFileName));
        }catch (FileNotFoundException e) {
            System.out.println("Could not find the inFile..."+inFileName);
        }
        GZIPOutputStream out = null;
        try {
            out = new GZIPOutputStream(new FileOutputStream(outFileName));
        }catch (IOException e) {
        	System.out.println("Could not find the outFile..."+outFileName);
        }
        byte[] buf = new byte[1024];
        int len = 0;
        try {
            while ((len = in.read()) > 0) {
                out.write(buf, 0, len);
                System.out.println(buf);
            }
            in.close();
            System.out.println("Completing the GZIP file..."+outFileName);
            out.flush();
            out.close();
        }catch (IOException e) {
        }
    }
	public static void main(String[] args) {
		compressFile("F:/opt/applog/ss.txt", "F:/opt/applog/sss");
	}
}
