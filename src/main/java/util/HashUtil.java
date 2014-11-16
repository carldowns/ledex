package util;

import org.apache.commons.codec.binary.Hex;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.Adler32;

/**
 * Created by carl_downs on 11/15/14.
 */
public class HashUtil {

    static public long getChecksum (String payload) {
        Adler32 algorithm = new Adler32();
        algorithm.update(payload.getBytes());
        return algorithm.getValue();
    }

    static public String getChecksumAsString (String payload) {
        return String.valueOf(getChecksum (payload));
    }

    static public String getDigest (String payload) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5"); // TODO: make static
        byte[] messageDigest = md.digest(payload.getBytes());
        return new String (Hex.encodeHex(messageDigest));
    }
}
