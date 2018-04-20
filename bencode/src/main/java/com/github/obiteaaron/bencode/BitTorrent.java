package com.github.obiteaaron.bencode;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author obiteaaron
 * @since 2018/4/20 22:17
 */
public class BitTorrent {

    private static List<BEncodeCodec> bEncodeCodecList = new ArrayList<>();

    static {
        bEncodeCodecList.add(new DictBEncodeCodec());
        bEncodeCodecList.add(new IntBEncodeCodec());
        bEncodeCodecList.add(new StringBEncodeCodec());
        bEncodeCodecList.add(new ListBEncodeCodec());
        bEncodeCodecList.add(new EndBEncodeCodec());
    }

    public static byte[] encode(Object bEncode) {
        return encode0(bEncode);
    }

    @SuppressWarnings("unchecked")
    static byte[] encode0(Object bEncode) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bEncodeCodecList.forEach(parse -> {
            if (parse.canEncode(bEncode.getClass())) {
                byte[] encode = parse.encode(bEncode);
                try {
                    byteArrayOutputStream.write(encode);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        return byteArrayOutputStream.toByteArray();
    }

    /**
     * 将输入的种子文件转换成对象格式，可读
     *
     * @param bytes 种子文件的二进制编码
     * @return 种子文件的dict
     */
    @SuppressWarnings("unchecked")
    public static Map decode(byte[] bytes) {
        Map result = (Map) decode0(bytes, 0).getResult();
        {
            // 转换成16进制的，方便查看
            Map info = (Map) result.get("info");
            Object pieces = info.get("pieces");

            String hexBinary = Hex.encodeHexString(Streams.toBytes(pieces), false);
//            info.put("pieces", hexBinary);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    static BPair decode0(byte[] bytes, int current) {
        if (current >= bytes.length) {
            return BPair.of(EndBEncodeCodec.END, current);
        }
        byte curByte = bytes[current];
        BPair bPair = BPair.empty();
        bEncodeCodecList.forEach(parse -> {
            if (parse.canDecode(curByte)) {
                BPair bPair1 = parse.decode(bytes, current);
                bPair.merge(bPair1);
            }
        });
        return bPair;
    }

    public static void main(String[] args) throws Exception {
        torrentToMagnet();
    }

    private static void torrentToMagnet() throws Exception {
        String fileName = "/E07CD65FE51F07980580BCF66EEEC3419E136E01.torrent";
        String filePath = BitTorrent.class.getResource(fileName).toURI().getPath();
        byte[] bytes = Streams.read(filePath);
        Map decode = decode(bytes);

        byte[] encodeInfo = encode(decode.get("info"));
        String sha1Hex = DigestUtils.sha1Hex(encodeInfo).toUpperCase();

        String magnetUri = "magnet:?xt=urn:btih:%s";

        System.out.println(String.format(magnetUri, sha1Hex));

        byte[] encode = encode(decode);

        Map decode1 = decode(encode);
        System.out.println(decode1.equals(decode));
        System.out.println(Arrays.equals(encode, bytes));

    }
}
