package com.github.obiteaaron.bencode;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author obiteaaron
 * @since 2018/4/20 22:18
 */
class ListBEncodeCodec implements BEncodeCodec<List> {

    @SuppressWarnings("unchecked")
    @Override
    public byte[] encode(List list) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(L_KEY);
        list.forEach((item) -> {
            try {
                byte[] bytes = BitTorrent.encode0(item);
                byteArrayOutputStream.write(bytes);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        byteArrayOutputStream.write(E_KEY);
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public boolean canEncode(Class t) {
        return List.class.isAssignableFrom(t);
    }

    @SuppressWarnings("unchecked")
    @Override
    public BPair<List> decode(byte[] bytes, int current) {
        List list = new ArrayList();
        while (true) {
            current = current + 1;
            BPair decode = BitTorrent.decode0(bytes, current);
            current = decode.getCurrent();
            if (decode.getResult() == EndBEncodeCodec.END) {
                break;
            }
            list.add(decode.getResult());
        }
        return BPair.of(list, current);
    }

    @Override
    public boolean canDecode(byte curByte) {
        return L_KEY == curByte;
    }
}
