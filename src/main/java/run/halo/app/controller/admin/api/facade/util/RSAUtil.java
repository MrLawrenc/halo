package run.halo.app.controller.admin.api.facade.util;

import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;

import java.security.PublicKey;
import java.util.Base64;

/**
 * @author : hz20035009-逍遥
 * @date : 2020/5/6 11:35
 * @description : 加解密工具类
 */
public class RSAUtil {

    private static PublicKey publicKey;
    private static RSA rsa;

    static {
        rsa = new RSA();
        publicKey = rsa.getPublicKey();
    }

    public static PublicKey getPublicKey() {
        return publicKey;
    }

    /**
     * @param srcEncode 加密之后的串
     * @return 解密之后的源字符串
     */
    public static String decode(String srcEncode) {
        return new String(rsa.decrypt(Base64.getDecoder().decode(srcEncode), KeyType.PrivateKey));
    }

    /**
     * @param srcStr 源字符串
     * @return 加密之后的密文
     */
    public static String encode(String srcStr) {
        return Base64.getEncoder().encodeToString(rsa.encrypt(srcStr, KeyType.PublicKey));
    }

    public static void main(String[] args) {


        String encode1 = encode("我是ss");


        RSA newRsa = new RSA(null, RSAUtil.getPublicKey().getEncoded());
        String encode2 = Base64.getEncoder().encodeToString(newRsa.encrypt("我是ss", KeyType.PublicKey));

        System.out.println(encode1 + "\n" + encode2);
        System.out.println("分界线.................");
        System.out.println(decode(encode1));
        System.out.println(decode(encode2));


    }
}