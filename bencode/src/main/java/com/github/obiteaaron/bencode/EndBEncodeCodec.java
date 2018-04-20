package com.github.obiteaaron.bencode;

/**
 * @author obiteaaron
 * @since 2018/4/20 23:08
 */
public class EndBEncodeCodec implements BEncodeCodec {

    static final Object END = new Object();

    @Override
    public byte[] encode(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean canEncode(Class t) {
        return false;
    }

    @Override
    public BPair decode(byte[] bytes, int current) {
        return BPair.of(END, current);
    }

    @Override
    public boolean canDecode(byte curByte) {
        return E_KEY == curByte;
    }
}
