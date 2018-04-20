package com.github.obiteaaron.bencode;

import org.apache.commons.lang3.math.NumberUtils;

import java.io.ByteArrayOutputStream;

/**
 * @author obiteaaron
 * @since 2018/4/20 22:18
 */
class IntBEncodeCodec implements BEncodeCodec<Long> {

    @Override
    public byte[] encode(Long aLong) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byteArrayOutputStream.write(I_KEY);
            byteArrayOutputStream.write(Streams.toBytes(aLong));
            byteArrayOutputStream.write(E_KEY);
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean canEncode(Class t) {
        return Number.class.isAssignableFrom(t);
    }

    @Override
    public BPair<Long> decode(byte[] bytes, int current) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        EndBEncodeCodec endParse = new EndBEncodeCodec();
        while (true) {
            current = current + 1;
            byte aByte = bytes[current];
            if (!endParse.canDecode(aByte)) {
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
        long number = NumberUtils.toLong(builder.toString());
        return BPair.of(number, current);
    }

    @Override
    public boolean canDecode(byte curByte) {
        return I_KEY == curByte;
    }
}
