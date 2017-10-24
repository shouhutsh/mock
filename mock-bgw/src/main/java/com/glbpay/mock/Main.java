package com.glbpay.mock;

import com.glbpay.mock.util.ServerThread;
import com.glbpay.mock.util.Utils;
import org.apache.log4j.Logger;

import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by shouhutsh on 2017/4/24.
 */
public class Main {

    private static final Logger logger = Logger.getLogger(Main.class);

    private static final ExecutorService executor = Executors.newCachedThreadPool();

    public static void main(String[] args) throws Exception{
        ServerSocket server = new ServerSocket(20000);
        Utils.logInfo(logger, "Start success!");
        try {
            while (true) {
                executor.execute(new ServerThread(server.accept()));
            }
        } finally {
            server.close();
        }
    }
}
