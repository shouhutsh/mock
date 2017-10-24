package com.glbpay.mock.util;

import org.apache.log4j.Logger;

import java.io.*;

/**
 * Created by shouhutsh on 2017/5/22.
 */
public class Utils {

    private static Logger logger = Logger.getLogger(Utils.class);

    public static void logInfo(Logger logger, String message) {
        logInfo(logger, message, null);
    }

    public static void logInfo(Logger logger, String message, Throwable throwable) {
        System.out.println(message);
        if (null == throwable) {
            logger.info(message);
        } else {
            logger.info(message, throwable);
        }
    }

    public static void logError(Logger logger, String message) {
        logError(logger, message, null);
    }

    public static void logError(Logger logger, String message, Throwable throwable) {
        System.out.println(message);
        if (null == throwable) {
            logger.error(message);
        } else {
            logger.error(message, throwable);
        }
    }

    public static String loadResources(String name) throws Exception {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(Utils.class.getResource("/" + name).openStream()));
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            reader.close();
            return sb.toString();
        } catch (Exception e) {
            logError(logger, "加载文件失败！" + name, e);
            throw e;
        } finally {
            if (null != reader) {
                reader.close();
            }
        }
    }
}
