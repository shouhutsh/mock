package com.glbpay.mock;

import com.glbpay.common.util.validator.StringUtils;
import com.glbpay.mock.model.Channel;
import com.glbpay.mock.util.Utils;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.util.CollectionUtils;

import java.io.StringWriter;
import java.util.Map;

/**
 * Created by shouhutsh on 2017/5/22.
 */
public class Assembler {

    private static final Logger logger = Logger.getLogger(Assembler.class);

    private Assembler(){
    }

    public static byte[] getResponse(Channel channel, byte[] request) throws Exception {
        Map<String, String> params = Parser.parse(channel, request);
        doHandlers(channel, params);

        String response = getTemplate(Utils.loadResources(channel.getTemplate()), params);
        Utils.logInfo(logger, "RESPONSE: " + response);
        return response.getBytes();
    }

    private static void doHandlers(Channel channel, Map<String, String> params) throws Exception {
        if (StringUtils.isBlank(channel.getSpecialField())
                || CollectionUtils.isEmpty(channel.getSpecialMap())) {
            return;
        }

        Object conf = channel.getSpecialMap().get(params.get(channel.getSpecialField()));
        if (null == conf) {
            conf = channel.getSpecialMap().get("DEFAULT");
        }

        JSONObject handlers = JSONObject.fromObject(conf);
        for (Object key : handlers.keySet()) {
            Handler.byType((String) key).doFunc(handlers.getString((String) key), params);
        }
    }

    private static String getTemplate(String content, Map<String, String> model) {
        VelocityEngine ve = new VelocityEngine();
        ve.init();
        VelocityContext context = new VelocityContext();
        for (Map.Entry<String, String> e : model.entrySet()) {
            context.put(e.getKey(), e.getValue());
        }
        StringWriter writer = new StringWriter();
        ve.evaluate(context, writer, "", content);
        return writer.toString();
    }

    private enum Handler{
        PARAM("PARAM"){
            @Override
            public void doFunc(String arg, Map<String, String> params) throws Exception {
                for (String i : arg.split(",")) {
                    String k_v[] = StringUtils.split(i, "=");
                    if (null != k_v) params.put(k_v[0], k_v[1]);
                }
            }
        },
        SLEEP("SLEEP"){
            @Override
            public void doFunc(String arg, Map<String, String> params) throws Exception {
                try {
                    Thread.sleep(Integer.valueOf(arg) * 1000);
                } catch (InterruptedException e) {
                    Utils.logError(logger, "睡眠被中断！", e);
                }
            }
        };

        private final String type;

        Handler(String type) {
            this.type = type;
        }

        public abstract void doFunc(String arg, Map<String, String> params) throws Exception;

        public static Handler byType(String type) throws Exception {
            for (Handler h : values()) {
                if (StringUtils.equals(type, h.type)) return h;
            }
            throw new Exception("没有找到合适的处理器！" + type);
        }
    }
}
