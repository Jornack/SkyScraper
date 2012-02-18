package com.jornack.skyscraper.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class JSONFileReader extends JSONReaderAbstract{
	
	
	private JSONFileReader(){}
	public JSONFileReader(File file){
		
		try {
			this.in = new Scanner(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
	
	

}
