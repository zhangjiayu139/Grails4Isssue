package middol.utils

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.apache.commons.codec.binary.Base64

import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

/**
* @Description:    AES 加密解密工具
* @Author:         zhangjiayu@middol.com
* @CreateDate:     2020/7/16 23:29
* @Version:        1.0
*/
@Slf4j
@CompileStatic
class AESEncryptUtil {
    /**
     * 加密
     * @param encryptString  待加密内容
     * @param sKey 解密密钥
     * @return
     */
    static String encrypt(String encryptString, String sKey){
        if (sKey == null) {
            log.error("Key为空null")
            return null
        }
        // 判断Key是否为16位
        if (sKey.length() != 32) {
            log.error("Key长度不是32位")
            return null
        }
        byte[] raw = sKey.getBytes("utf-8")
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES")
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")//"算法/模式/补码方式"
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec)
        byte[] encrypted = cipher.doFinal(encryptString.getBytes("utf-8"))

        Base64.encodeBase64String(encrypted)//此处使用BASE64做转码功能，同时能起到2次加密的作用。
    }


    /**
     * 解密
     * @param decryptString  待解密内容
     * @param sKey 解密密钥
     * @return
     */
    static String decrypt(String decryptString, String sKey){
        try {
            // 判断Key是否正确
            if (sKey == null) {
                log.error("Key为空null")
                return null
            }
            // 判断Key是否为16位
            if (sKey.length() != 32) {
                log.error("Key长度不是32位")
                return null
            }
            byte[] raw = sKey.getBytes("utf-8")
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES")
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
            cipher.init(Cipher.DECRYPT_MODE, skeySpec)
            byte[] encrypted1 = Base64.decodeBase64(decryptString)//先用base64解密

            try {
                byte[] original = cipher.doFinal(encrypted1);
                String originalString = new String(original,"utf-8")
                return originalString
            } catch (e) {
                log.error(e.message)
                return null
            }
        } catch (ex) {
            log.error(ex.message)
            return null
        }
    }

//    static void main(String[] args) {
//        String url = "http://a-kgqsm211/innovatorserver/vault/vaultserver.aspx?dbName=YFAS-MPM-NTEST_PRO&fileId=249EB143540342C98122823E205B83AD&fileName=JIT_AS001MY19_ODS_1010_03_V01_2020-01-07+14-39-42_%E6%A8%AA%E6%9D%BF.pdf&vaultId=67BBB9204FE84A8981ED8313049BA06C"
//        String encode = AESEncryptUtil.encrypt(url, "ae125efkk4454eeff444ferfkny6oxi8")
//        println encode
////        String str = "FjB0L3hEG1cq8ot1wfscG8pvuMHUO+qDenUsm0WBE89qrM1DcxMd8hOPvcUVNJ8tfKnofEKiaw2ss5zVV23fSbAGQz0eoNQt2AB99deeXw2w/AF0Vdq8ce1DZi6qCxKsGsynhxCHdC0yvu3uloEWsVRN5VWnLA5dB8peqbVhvwm1WIkeTs3rL2uQgqifs+2gfOL281ybj7yxCYzTXrOb7a4Fuwd/+8OXtZxgeroFlBuYdHON3b+Q2awyYxMhiqN53k60m9GUQKrzXogmxf5akYEKfhJjJ0LXgVZFXYYnF11dek6+dNIPUUNoOfP24WuD8iZ2cY/O1CN49HdT11lJKg=="
//        println AESEncryptUtil.decrypt(encode, "ae125efkk4454eeff444ferfkny6oxi8")
//    }
}
