package com.zainmanji.sportsrefresh;

import java.io.FilterInputStream;
import java.io.InputStream;

//Help from javacodegeeks.com - open source
public class FlushedInputStream extends FilterInputStream {
	
    public FlushedInputStream(InputStream inputStream) {
        super(inputStream);
    }
    
}