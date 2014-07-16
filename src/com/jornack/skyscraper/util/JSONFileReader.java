package com.jornack.skyscraper.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class JSONFileReader extends JSONReaderAbstract
{

    protected File source;

    private JSONFileReader()
    {
    }

    public JSONFileReader(File file)
    {

        try
        {
            this.in = new Scanner(file);
        } catch (FileNotFoundException e)
        {
            Logger.log(e);
        }

    }

    @Override
    public void connect() throws IOException
    {
        // do nothing

    }

    @Override
    public void close() throws IOException
    {
        // do nothing

    }

}
