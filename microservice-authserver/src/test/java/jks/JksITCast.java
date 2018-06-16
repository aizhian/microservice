package jks;

import sun.misc.BASE64Encoder;

import java.io.*;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

/**
 * Created by Aizhanglin on 2017/10/12.
 */
public class JksITCast {
    public void showJKS() throws Exception {

        String privatePath = "D:/testPri.key"; // 准备导出的私钥
        String publicPath = "D:/testPub.key"; // 准备导出的公钥
        PrivateKey privateKey = getPrivateKeyFromStore();
        createKeyFile(privateKey, privatePath);
        PublicKey publicKey = getPublicKeyFromCrt();
        createKeyFile(publicKey, publicPath);

        byte[] publicKeyBytes = publicKey.getEncoded();
        byte[] privateKeyBytes = privateKey.getEncoded();

        String publicKeyBase64 = new BASE64Encoder().encode(publicKeyBytes);
        String privateKeyBase64 = new BASE64Encoder().encode(privateKeyBytes);

        System.out.println("publicKeyBase64.length():" + publicKeyBase64.length());
        System.out.println("publicKeyBase64:" + publicKeyBase64);

        System.out.println("privateKeyBase64.length():" + privateKeyBase64.length());
        System.out.println("privateKeyBase64:" + privateKeyBase64);
    }

    private static PrivateKey getPrivateKeyFromStore() throws Exception {
        String storeType = "JCEKS"; // KeyTool中生成KeyStore时设置的storetype
        String storePath = "D:\\workspace\\microservice\\microservice-integration\\microservice-integration-oauth2\\src\\main\\resources\\keystore.jks"; // KeyTool中已生成的KeyStore文件
        storeType = null == storeType ? KeyStore.getDefaultType() : storeType;
        KeyStore keyStore = KeyStore.getInstance(storeType);
        InputStream is = new FileInputStream(storePath);
        keyStore.load(is, "keystore".toCharArray());
        // 由密钥库获取密钥的两种方式
        // KeyStore.PrivateKeyEntry pkEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(alias, new KeyStore.PasswordProtection(pw));
        // return pkEntry.getPrivateKey();
        return (PrivateKey) keyStore.getKey("security", "security".toCharArray());
    }

    private PublicKey getPublicKeyFromCrt() throws CertificateException, FileNotFoundException {
        String crtPath = "D:\\workspace\\microservice\\microservice-integration\\microservice-integration-oauth2\\src\\main\\resources\\cer.cer"; // KeyTool中已生成的证书文件
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        FileInputStream in = new FileInputStream(crtPath);
        Certificate crt = cf.generateCertificate(in);
        PublicKey publicKey = crt.getPublicKey();
        return publicKey;
    }

    private void createKeyFile(Object key, String filePath) throws Exception {
        FileOutputStream fos = new FileOutputStream(filePath);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(key);
        oos.flush();
        oos.close();
    }

}
