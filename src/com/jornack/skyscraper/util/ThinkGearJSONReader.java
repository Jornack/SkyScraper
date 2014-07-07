package com.jornack.skyscraper.util;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.Scanner;

import com.jornack.skyscraper.window.PreferencesWindow;

public class ThinkGearJSONReader extends JSONReaderAbstract
{

    public static final String DEFAULT_HOST = "127.0.0.1";
    public static final int DEFAULT_PORT = 13854;

    private SocketChannel channel = null;
    private String host = null;
    private int port;

    public ThinkGearJSONReader()
    {
        this.setHost(PreferenceManager.getPreferences().get(
                PreferenceManager.THINKGEAR_DEFAULT_HOST, DEFAULT_HOST));
        this.setPort(PreferenceManager.getPreferences().getInt(
                PreferenceManager.THINKGEAR_DEFAULT_PORT, DEFAULT_PORT));
    }

    public void connect() throws IOException
    {

        if (channel == null || !channel.isConnected())
        {
            Logger.log("Starting new connection...");
            this.channel = SocketChannel.open(new InetSocketAddress(this.
                    getHost(), this.getPort()));

            boolean rawOutput = PreferenceManager.getPreferences().getBoolean(
                    PreferenceManager.THINKGEAR_RAW_OUTPUT, false);
            String cmd = "{\"enableRawOutput\": " + rawOutput
                    + ", \"format\": \"Json\"}\n";
            sendCommand(cmd);

            this.in = new Scanner(channel);

        }
    }

    public void sendCommand(String cmd) throws IOException
    {
        CharsetEncoder enc = Charset.forName("US-ASCII").newEncoder();

        this.channel.write(enc.encode(CharBuffer.wrap(cmd)));
    }

    /**
     * Never got this to work on Win7 x64 Ultimate
     *
     * @throws IOException
     */
    public void startRecording() throws IOException
    {
        Logger.debug("Start recording");
        String cmd
                = "{\"startRecording\":{\"rawEeg\":true,\"poorSignalLevel\":true,\"eSense\":true, \"eegPower\":true,\"blinkStrength\":true},\"applicationName\":\"SkyScraper\"}";
        sendCommand(cmd);
    }

    public void stopRecording() throws IOException
    {
        Logger.debug("Stop recording");
        String cmd = "{\"stopRecording\":\"SkyScraper\"}";
        sendCommand(cmd);
    }

    public boolean isConnected()
    {
        return channel != null ? channel.isConnected() : false;
    }

    public void close() throws IOException
    {

        if (channel != null && channel.isConnected())
        {
            System.out.println("Closing connection...");
            this.channel.close();
        }
    }

    public String getHost()
    {
        return host;
    }

    public void setHost(String host)
    {
        this.host = host;
    }

    public int getPort()
    {
        return port;
    }

    public void setPort(int port)
    {
        this.port = port;
    }
}
