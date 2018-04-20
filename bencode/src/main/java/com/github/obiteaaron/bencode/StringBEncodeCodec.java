package com.github.obiteaaron.bencode;

import org.apache.commons.lang3.math.NumberUtils;

import java.io.ByteArrayOutputStream;

/**
 * @author obiteaaron
 * @since 2018/4/20 22:19
 */
class StringBEncodeCodec implements BEncodeCodec<String> {

    @Override
    public byte[] encode(String s) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byteArrayOutputStream.write(Streams.toBytes(s.length()));
            byteArrayOutputStream.write(SPLIT);
            byteArrayOutputStream.write(Streams.toBytes(s));
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean canEncode(Class t) {
        return String.class.isAssignableFrom(t);
    }

    @Override
    public BPair<String> decode(byte[] bytes, int current) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(bytes[current]);

        while (true) {
            current = current + 1;
            byte aByte = bytes[current];
            if (canDecode(aByte)) {
                byteArrayOutputStream.write(bytes[current]);
            } else {
                break;
            }
        }

        byte[] byteArray = byteArrayOutputStream.toByteArray();
        StringBuilder builder = new StringBuilder();
        for (byte b : byteArray) {
            builder.append((char) b);
        }
        int length = NumberUtils.toInt(builder.toString());

        byte[] strByte = new byte[length];
        try {
            System.arraycopy(bytes, current + 1, strByte, 0, length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String str = Streams.toString(strByte);

        return BPair.of(str, current + length);
    }

    @Override
    public boolean canDecode(byte curByte) {
        return '0' == (char) curByte ||
                '1' == (char) curByte ||
                '2' == (char) curByte ||
                '3' == (char) curByte ||
                '4' == (char) curByte ||
                '5' == (char) curByte ||
                '6' == (char) curByte ||
                '7' == (char) curByte ||
                '8' == (char) curByte ||
                '9' == (char) curByte;
    }

}
