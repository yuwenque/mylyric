package com.ares.http;

import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.*;

/**
 * Created by ares on 2017/12/20.
 */
public class QcloudUtil {

    private static final String CONTENT_CHARSET = "UTF-8";

    /**
     * 将参数按照字母排序
     *
     * @param params
     * @return
     */
    public static String buildTheRequestStr(Map<String, Object> params) {


        List<String> keyList = new ArrayList<>();

        for (String key : params.keySet()) {

            keyList.add(key);
        }
        //忽略大小写,排序
        Collections.sort(keyList, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareToIgnoreCase(o2);
            }
        });

        StringBuffer stringBuffer = new StringBuffer();


        for (int i = 0; i < keyList.size(); i++) {
            String key = keyList.get(i);
            Object object = params.get(key);
            String content = object.toString();

            if (i == keyList.size() - 1) {
                stringBuffer.append(key + "=" + content);
            } else {
                stringBuffer.append(key + "=" + content + "&");

            }
        }

        return stringBuffer.toString();
    }


    public static String sign(String requestMethod, String url, String paramsStr) {


        String origin = requestMethod + url + "?" + paramsStr;
        System.out.println("origin=" + origin);
        return hmacSHA256(origin);
    }


    /**
     * 算法加密
     *
     * @param content
     * @return
     */
    public static String hmacSHA256(String content) {

        String secretKey = "oQmshKhniNmumNinb9WdW9YidIgphJrL";


        return hmacSHA256(content, secretKey);

    }

    public static String hmacSHA256(String content, String secretKey) {


        System.out.println("content=" + content);
        Mac sha256_HMAC = null;
        try {
            sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA256");
            sha256_HMAC.init(secret_key);


            String hash = Base64.encodeBase64String(sha256_HMAC.doFinal(content.getBytes()));
            return hash;
            //  return URLEncoder.encode(hash,"utf-8");
            // return hash;
        } catch (Exception e) {
            e.printStackTrace();
        }


        return "";
    }


    /**
     * 将加密后的字节数组转换成字符串
     *
     * @param b
     *            字节数组
     * @return 字符串
     */
    private static String byteArrayToHexString(byte[] b) {
        StringBuilder hs = new StringBuilder();
        String stmp;
        for (int n = 0; b != null && n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1)
                hs.append('0');
            hs.append(stmp);
        }
        return hs.toString().toLowerCase();
    }

    public static TreeMap<String, Object> createQCloudMap() {


        TreeMap<String, Object> map = new TreeMap<>();
        map.put("SecretId", "AKID84lESRw8yYrugBzN4UehW5PUJsABvObX");
        map.put("Timestamp", System.currentTimeMillis() / 1000);
        map.put("Nonce", new Random().nextInt(java.lang.Integer.MAX_VALUE));
        map.put("Region", "gz");
        map.put("SignatureMethod", "HmacSHA256");
        map.put("InstanceIds.0", "ins-09dx96dg");

        //1513775256
        //1417344845
        return map;


    }


}
