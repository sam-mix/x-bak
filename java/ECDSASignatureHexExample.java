
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.security.spec.ECGenParameterSpec;

public class ECDSASignatureHexExample {

    public static KeyPair getKeyPairFromPEM(String privateKeyPath, String publicKeyPath) throws Exception {
        // 读取私钥PEM文件
        String privateKeyPEM = new String(Files.readAllBytes(Paths.get(privateKeyPath))).replace("-----BEGIN EC PRIVATE KEY-----", "")
                .replace("-----END EC PRIVATE KEY-----", "").replaceAll("\\s", "");
        byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyPEM);

        // 解析私钥
        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("EC"); // 假设是EC密钥
        PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

        // 读取公钥PEM文件（这里需要你自己实现，假设你已经有了公钥的PEM格式字符串）
        String publicKeyPEM =new String(Files.readAllBytes(Paths.get(publicKeyPath))).replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "").replaceAll("\\s", "");; // 从publicKeyPath读取公钥PEM内容，并进行类似的处理
        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyPEM);
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

        // 创建KeyPair对象
        return new KeyPair(publicKey, privateKey);
    }

//    public static void main(String[] args) {
//        try {
//            KeyPair keyPair = getKeyPairFromPEM("path/to/private_key.pem", "path/to/public_key.pem");
//            System.out.println("KeyPair created successfully!");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


       // 生成密钥对的方法（与之前相同）
   public static KeyPair generateKeyPair() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
       KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC");
       ECGenParameterSpec ecSpec = new ECGenParameterSpec("secp256r1");
       keyGen.initialize(ecSpec, new SecureRandom());
       return keyGen.generateKeyPair();
   }

    // 创建签名的方法（与之前相同，但添加了返回hex字符串的功能）
    public static String signDataToHex(byte[] data, PrivateKey privateKey) throws SignatureException {
        try {
            Signature signature = Signature.getInstance("SHA256withECDSA");
            signature.initSign(privateKey);
            signature.update(data);
            byte[] signatureBytes = signature.sign();
            return bytesToHex(signatureBytes); // 将字节数组转换为hex字符串
        } catch (Exception e) {
            throw new SignatureException("Failed to generate signature", e);
        }
    }

    // 验证签名的方法（接受hex字符串作为输入）
    public static boolean verifySignatureFromHex(byte[] data, String hexSignature, PublicKey publicKey) {
        try {
            byte[] signatureBytes = hexToBytes(hexSignature); // 将hex字符串转换回字节数组
            Signature signature = Signature.getInstance("SHA256withECDSA");
            signature.initVerify(publicKey);
            signature.update(data);
            return signature.verify(signatureBytes);
        } catch (Exception e) {
            // 在实际应用中，你可能希望更细致地处理这些异常
            e.printStackTrace();
            return false;
        }
    }

    // 辅助方法：将字节数组转换为十六进制字符串
    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    // 辅助方法：将十六进制字符串转换回字节数组
    public static byte[] hexToBytes(String hexString) {
        int len = hexString.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
                    + Character.digit(hexString.charAt(i+1), 16));
        }
        return data;
    }

    public static void main(String[] args) {
        try {
            // 生成密钥对
           KeyPair keyPair = generateKeyPair();
            // KeyPair keyPair = getKeyPairFromPEM("../embedx/assets/private_key.pem", "../embedx/assets/public_key.pem");
            PrivateKey privateKey = keyPair.getPrivate();
            PublicKey publicKey = keyPair.getPublic();

            // 要签名的数据
            String dataString = "This is a test message";
            byte[] data = dataString.getBytes();

            // 创建签名并转换为hex字符串
            String hexSignature = signDataToHex(data, privateKey);
            System.out.println("Hex Signature: " + hexSignature);

            // 从hex字符串转换回字节数组并验证签名
            boolean isVerified = verifySignatureFromHex(data, hexSignature, publicKey);
            System.out.println("Signature verified: " + isVerified);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}