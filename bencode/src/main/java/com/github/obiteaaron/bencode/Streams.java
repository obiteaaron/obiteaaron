package com.github.obiteaaron.bencode;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.Charset;

/**
 * @author obiteaaron
 * @since 2018/4/20 22:21
 */
class Streams {

    /**
     * 读文件流一定要用此编码，不然二进制会乱码
     */
    private static final String CHARSET_STRING = "ISO8859-1";
    private static final Charset CHARSET = Charset.forName(CHARSET_STRING);

    static byte[] read(String filePath) throws IOException {
        return read(new FileInputStream(filePath));
    }

    static byte[] read(InputStream fileInputStream) throws IOException {
        return IOUtils.toByteArray(fileInputStream);
    }

    static byte[] toBytes(Object str) {
        return String.valueOf(str).getBytes(CHARSET);
    }

    static String toString(byte[] bytes) {
        return new String(bytes, CHARSET);
    }
}
