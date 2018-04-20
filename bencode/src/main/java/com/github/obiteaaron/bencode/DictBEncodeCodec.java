package com.github.obiteaaron.bencode;

import java.io.ByteArrayOutputStream;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author obiteaaron
 * @since 2018/4/20 22:17
 */
class DictBEncodeCodec implements BEncodeCodec<Map> {

    @SuppressWarnings("unchecked")
    @Override
    public byte[] encode(Map map) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(D_KEY);
        map.forEach((k, v) -> {
            try {
                byte[] bytesKey = BitTorrent.encode0(k);
                byteArrayOutputStream.write(bytesKey);

                byte[] bytesValue = BitTorrent.encode0(v);
                byteArrayOutputStream.write(bytesValue);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        byteArrayOutputStream.write(E_KEY);
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public boolean canEncode(Class t) {
        return Map.class.isAssignableFrom(t);
    }

    @SuppressWarnings("unchecked")
    @Override
    public BPair<Map> decode(byte[] bytes, int current) {
        Map map = new LinkedHashMap();
        Object key = null;
        while (true) {
            current = current + 1;
            BPair decode = BitTorrent.decode0(bytes, current);
            current = decode.getCurrent();
            if (decode.getResult() == EndBEncodeCodec.END) {
                break;
            }
            if (key == null) {
                key = decode.getResult();
            } else {
                map.put(key, decode.getResult());
                key = null;
            }
        }
        return BPair.of(map, current);
    }

    @Override
    public boolean canDecode(byte curByte) {
        return D_KEY == curByte;
    }
}
