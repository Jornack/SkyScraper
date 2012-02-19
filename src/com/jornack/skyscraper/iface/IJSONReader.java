package com.jornack.skyscraper.iface;

import java.io.IOException;

public interface IJSONReader {
	public boolean isDataAvailable() ;
	public String getData() ;
	//public void connect() throws IOException;
	boolean isConnected();
	public void connect() throws IOException;
	public void close()  throws IOException;
}
