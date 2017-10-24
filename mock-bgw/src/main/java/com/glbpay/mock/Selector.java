package com.glbpay.mock;

import com.glbpay.mock.model.Channel;
import com.glbpay.mock.util.ConfigService;
import com.glbpay.mock.util.Utils;
import org.apache.log4j.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by shouhutsh on 2017/5/22.
 */
public class Selector {

    private static final Logger logger = Logger.getLogger(Selector.class);

    private Selector(){
    }

    public static Channel select(byte[] request) throws Exception {
        String str = new String(request);
        Utils.logInfo(logger, "REQUEST: " + str);
        for (Channel channel : ConfigService.getChannelConfig()) {
            Pattern p = Pattern.compile(channel.getRegex());
            Matcher m = p.matcher(str);
            if(m.find()){
                return channel;
            }
        }
        throw new Exception("没有找到适用渠道！" + str);
    }
}
