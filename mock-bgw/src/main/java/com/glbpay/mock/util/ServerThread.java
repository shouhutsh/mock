package com.glbpay.mock.util;

import com.glbpay.mock.Assembler;
import com.glbpay.mock.Selector;
import com.glbpay.mock.model.Channel;
import org.apache.log4j.Logger;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;

import java.io.*;
import java.net.Socket;

/**
 * Created by shouhutsh on 2017/4/24.
 */
public class ServerThread implements Runnable {

    private static final Logger logger = Logger.getLogger(ServerThread.class);

    private Socket client = null;

    public ServerThread(Socket client){
        this.client = client;
    }

    @Override
    public void run() {
        try{
            Utils.logInfo(logger, "Connect success!");

            byte[] request = getRequest();

            Filter filter = Filter.getType(request);
            byte[] reqContent = filter.doFilter(request);

            Channel channel = Selector.select(reqContent);
            byte[] resContent = Assembler.getResponse(channel, reqContent);
            byte[] response = Decorator.getDecorator(filter).doDecorator(resContent);

            send(response);
            client.close();
            Utils.logInfo(logger, "Connect closed!");
        }catch(Exception e){
            Utils.logError(logger, "run error!", e);
        }
    }

    private void send(byte[] response) throws IOException {
        OutputStream output = new BufferedOutputStream(client.getOutputStream());
        output.write(response);
        output.flush();
    }

    private byte[] getRequest() throws IOException {
        int size, default_buffer_size = 4096;
        InputStream input = new BufferedInputStream(client.getInputStream());

        byte[] request = new byte[0], buffer = new byte[default_buffer_size];
        while (default_buffer_size == (size = input.read(buffer))) {
            request = ByteUtils.concatenate(request, buffer);
        }
        if(size < 0) return request;
        return ByteUtils.concatenate(request, ByteUtils.subArray(buffer, 0, size));
    }
}
