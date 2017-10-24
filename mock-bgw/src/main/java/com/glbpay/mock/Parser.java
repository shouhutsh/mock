package com.glbpay.mock;

import com.glbpay.common.util.validator.StringUtils;
import com.glbpay.mock.model.Channel;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by shouhutsh on 6/29/2017.
 */
public class Parser {

    private static final Logger logger = Logger.getLogger(Selector.class);

    private Parser() {
    }

    public  static Map<String, String> parse(Channel channel, byte[] request) throws Exception {
        return Handler.byFormat(channel.getFormat()).toMap(request);
    }

    private enum Handler{
        GET_STRING("GET_STRING"){
            @Override
            public Map<String, String> toMap(byte[] request) {
                String message = new String(request);
                Map<String, String> requestMap = new HashMap<String, String>();
                for (String item : message.split("&")) {
                    String k_v[] = StringUtils.split(item, "=");
                    requestMap.put(k_v[0], k_v.length == 2 ? k_v[1] : "");
                }
                return requestMap;
            }
        },
        JSON("JSON"){
            @Override
            public Map<String, String> toMap(byte[] request) {
                JSONObject json = JSONObject.fromObject(new String(request));
                Map<String, String> map = new HashMap<String, String>();

                for (Iterator<String> keys = json.keys(); keys.hasNext(); ) {
                    String key = keys.next();
                    String str = json.getString(key);
                    if (json.get(key) instanceof JSONObject) {
                        map.putAll(toMap(str.getBytes()));
                    } else {
                        map.put(key, str);
                    }
                }
                return map;
            }
        };

        private final String format;

        Handler(String format) {
            this.format = format;
        }

        public abstract Map<String, String> toMap(byte[] request);

        public static Handler byFormat(String format) throws Exception {
            for (Handler h : values()) {
                if(StringUtils.equals(format, h.format)) return h;
            }
            throw new Exception("没有合适的解析器！" + format);
        }
    }
}
