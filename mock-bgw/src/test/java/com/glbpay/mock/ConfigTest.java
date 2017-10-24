package com.glbpay.mock;

import com.glbpay.common.util.helper.JsonUtils;
import com.glbpay.mock.util.ConfigService;

/**
 * Created by shouhutsh on 6/29/2017.
 */
public class ConfigTest {

    public static void main(String[] args) {
        System.out.println(JsonUtils.collection2json(ConfigService.getChannelConfig()));
    }
}
