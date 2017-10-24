package com.glbpay.mock.util;

import com.glbpay.common.util.validator.StringUtils;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by shouhutsh on 2017/4/24.
 */
public enum Filter {
    GET {
        @Override
        public byte[] doFilter(byte[] request) throws Exception{
            BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(request)));
            try {
                Pattern p = Pattern.compile("GET.*?\\?([^ ]+).+");
                Matcher m = p.matcher(reader.readLine());
                if (m.find()) {
                    return m.group(1).getBytes();
                }
                return new byte[0];
            } finally {
                reader.close();
            }
        }
    },
    POST {
        @Override
        public byte[] doFilter(byte[] request) throws Exception{
            BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(request)));
            try {
                while (StringUtils.isNotBlank(reader.readLine())) {
                }
                return reader.readLine().getBytes();
            } finally {
                reader.close();
            }
        }
    },
    OTHER {
        @Override
        public byte[] doFilter(byte[] request) throws Exception{
            return request;
        }
    };

    public abstract byte[] doFilter(byte[] request) throws Exception;

    public static Filter getType(byte[] request) {
        if (startWith(request, "GET")) {
            return GET;
        } else if (startWith(request, "POST")) {
            return POST;
        } else {
            return OTHER;
        }
    }

    private static boolean startWith(byte[] bytes, String str) {
        return ByteUtils.equals(str.getBytes(), ByteUtils.subArray(bytes, 0, str.getBytes().length));
    }
}

