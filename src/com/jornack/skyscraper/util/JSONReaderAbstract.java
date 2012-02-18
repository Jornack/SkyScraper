/**
 * 
 */
package com.jornack.skyscraper.util;


import java.util.Scanner;

import com.jornack.skyscraper.iface.IJSONReader;

/**
 * @author Jornack
 *
 */
public abstract class JSONReaderAbstract implements IJSONReader {
	protected Scanner in = null;
	
	/* (non-Javadoc)
	 * @see com.jornack.skyscraper.util.IJSONReader#isDataAvailable()
	 */
	@Override
	public boolean isDataAvailable() {
		if (this.in != null) {
			return this.in.hasNextLine();
		} else {
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see com.jornack.skyscraper.util.IJSONReader#getData()
	 */
	@Override
	public String getData() {
		return this.in.nextLine();
	}

	
}
