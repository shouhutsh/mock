package com.glbpay.mock.util;

import com.glbpay.mock.model.Channel;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by shouhutsh on 6/29/2017.
 */
public class ConfigService {

    private static final Logger logger = Logger.getLogger(ConfigService.class);

    private static final String PATH = "config.json";

    private static final Executor executor = Executors.newSingleThreadExecutor();

    static {
        Handler.init();
        executor.execute(new Handler());
    }

    private volatile static JSONObject config;

    private static class Handler implements Runnable{
        public static void init(){
            try {
                Utils.logInfo(logger, "加载配置文件..");
                config = JSONObject.fromObject(Utils.loadResources(PATH));
                Utils.logInfo(logger, "配置文件加载成功！");
            } catch (Exception e) {
                Utils.logError(logger, "配置文件解析失败！", e);
            }
        }

        @Override
        public void run() {
            try {
                Thread.sleep(config.getInt("LOAD_TIMEOUT") * 1000);
                init();
                executor.execute(this);
            } catch (Exception e) {
                Utils.logError(logger, "定时加载配置文件失败！", e);
            }
        }
    }

    public static List<Channel> getChannelConfig(){
        return JSONArray.toList(config.getJSONArray("CHANNEL"), Channel.class);
    }
}
