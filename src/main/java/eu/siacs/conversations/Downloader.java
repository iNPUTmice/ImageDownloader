package eu.siacs.conversations;

import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.io.CipherOutputStream;
import org.bouncycastle.crypto.modes.AEADBlockCipher;
import org.bouncycastle.crypto.modes.GCMBlockCipher;
import org.bouncycastle.crypto.params.AEADParameters;
import org.bouncycastle.crypto.params.KeyParameter;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Downloader {

    static {
        URL.setURLStreamHandlerFactory(new AesGcmURLStreamHandlerFactory());
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java -jar ImageDownloader.jar aesgcm://url.tld/filename#C0FFEBABE");
        } else {
            try {
                final URL url = new URL(args[0]);
                OutputStream os = setupOutputStream(new PrintStream(System.out), url.getRef());
                final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = connection.getInputStream();
                connection.connect();
                byte[] buffer = new byte[4096];
                int count;
                while ((count = inputStream.read(buffer)) != -1) {
                    os.write(buffer, 0, count);
                }
                os.flush();
                os.close();
            } catch (Exception e) {
                System.err.println("could not connect to host " + e.getMessage());
            }
        }
    }

    private static OutputStream setupOutputStream(OutputStream os, String reference) {
        if (reference != null) {
            int ivLen;
            if (reference.length() == 96) {
                ivLen = 16;
            } else if (reference.length() == 88) {
                ivLen = 12;
            } else {
                return os;
            }
            byte[] keyAndIv = hexToBytes(reference);
            byte[] key = new byte[32];
            byte[] iv = new byte[ivLen];
            System.arraycopy(keyAndIv, 0, iv, 0, ivLen);
            System.arraycopy(keyAndIv, ivLen, key, 0, 32);
            AEADBlockCipher cipher = new GCMBlockCipher(new AESEngine());
            cipher.init(false, new AEADParameters(new KeyParameter(key), 128, iv));
            return new CipherOutputStream(os, cipher);
        } else {
            return os;
        }
    }

    private static byte[] hexToBytes(String hex) {
        int len = hex.length();
        byte[] array = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            array[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4) + Character.digit(hex.charAt(i + 1), 16));
        }
        return array;
    }
}
