package com.github.obiteaaron.bencode;

/**
 * @author obiteaaron
 * @since 2018/4/20 22:16
 */
public interface BEncodeCodec<T> {

    byte SPLIT = ':';
    byte D_KEY = 'd';
    byte L_KEY = 'l';
    byte I_KEY = 'i';
    byte E_KEY = 'e';

    byte[] encode(T t);

    boolean canEncode(Class t);

    BPair<T> decode(byte[] bytes, int current);

    boolean canDecode(byte curByte);
}
