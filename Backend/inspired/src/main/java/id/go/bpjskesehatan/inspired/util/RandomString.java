package id.go.bpjskesehatan.inspired.util;

import java.util.Base64;
import java.util.UUID;

public class RandomString {
	public static String generateShortUniqueString() {
        UUID uuid = UUID.randomUUID();
        byte[] bytes = new byte[16];
        long mostSigBits = uuid.getMostSignificantBits();
        long leastSigBits = uuid.getLeastSignificantBits();

        for (int i = 0; i < 8; i++) {
            bytes[i] = (byte) (mostSigBits >>> (8 * (7 - i)));
            bytes[8 + i] = (byte) (leastSigBits >>> (8 * (7 - i)));
        }

        String base64String = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        return base64String.substring(0, 8); // Adjust the length if needed
    }
}
