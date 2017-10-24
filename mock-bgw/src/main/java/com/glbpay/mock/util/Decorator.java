package com.glbpay.mock.util;

/**
 * Created by shouhutsh on 2017/5/22.
 */
public enum Decorator {

    HTTP {
        @Override
        public byte[] doDecorator(byte[] response) {
            final String TEMPLATE = "HTTP/1.0 200 OK\r\n"+
                    "Server: OneFile 1.0\r\n"+
                    "Content-length: %d\r\n"+
                    "Content-type: application/json\r\n" +
                    "\r\n" +
                    "%s";
            return String.format(TEMPLATE, response.length, new String(response)).getBytes();
        }
    },
    OTHER {
        @Override
        public byte[] doDecorator(byte[] response) {
            return response;
        }
    };

    public abstract byte[] doDecorator(byte[] response);

    public static Decorator getDecorator(Filter filter) {
        switch (filter) {
            case GET:
            case POST:
                return HTTP;
            default:
                return OTHER;
        }
    }
}
