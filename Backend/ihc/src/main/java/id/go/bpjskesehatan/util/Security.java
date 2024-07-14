package id.go.bpjskesehatan.util;

import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.crypto.Cipher;


public class Security {
	private static final String ALGORITHM = "AES";
//	private static final byte[] keyValue = new byte[] { 'T', 'h', 'i', 's',
//			'I', 's', 'A', 'S', 'e', 'c', 'r', 'e', 't', 'K', 'e', 'y' };
	private KeyPair keypair;

	public Security() throws Exception {
		generate();
	}

	public String encrypt(String valueToEnc) throws Exception {

		Cipher c = Cipher.getInstance(ALGORITHM);
		c.init(Cipher.ENCRYPT_MODE, keypair.getPublic());
		byte[] encValue = c.doFinal(valueToEnc.getBytes());
		String encryptedValue = new String(Base64.encode(encValue));
		return encryptedValue;
	}

	public String decrypt(String encryptedValue) throws Exception {
		// Key key = generateKey().getPublic();
		Cipher c = Cipher.getInstance(ALGORITHM);
		c.init(Cipher.DECRYPT_MODE, keypair.getPrivate());
		byte[] decordedValue = Base64.decode(encryptedValue);
		byte[] decValue = c.doFinal(decordedValue);
		String decryptedValue = new String(decValue);
		return decryptedValue;
	}

	@SuppressWarnings("unused")
	private static KeyPair generateKey() throws Exception {
		SecureRandom random = new SecureRandom();
		byte[] value = new byte[1024];
		random.nextBytes(value);

		KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
		generator.initialize(1024, random);
		KeyPair pair = generator.generateKeyPair();
		Key pubKey = pair.getPublic();
		Key privKey = pair.getPrivate();
		// Key key = new SecretKeySpec(keyValue, ALGORITHM);
		return pair;
	}

	private void generate() {

		try {
			// Security.insertProviderAt(new FooBarProvider(), 1);
			// Security.addProvider(provider);

			// Create the public and private keys
			KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");

			SecureRandom random = createFixedRandom();
			generator.initialize(1024, random);

			keypair = generator.generateKeyPair();
			Key pubKey = keypair.getPublic();
			Key privKey = keypair.getPrivate();
			

			System.out.println("publicKey : "
					+ Base64.encode(pubKey.getEncoded()));
			System.out.println("privateKey : "
					+ Base64.encode(privKey.getEncoded()));

			// BufferedWriter out = new BufferedWriter(new FileWriter(
			// publicKeyFilename));
			// out.write(b64.encode(pubKey.getEncoded()));
			// out.close();
			//
			// out = new BufferedWriter(new FileWriter(privateFilename));
			// out.write(b64.encode(privKey.getEncoded()));
			// out.close();

		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public static SecureRandom createFixedRandom() {
		return new FixedRand();
	}

	private static class FixedRand extends SecureRandom {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		MessageDigest sha;
		byte[] state;

		FixedRand() {
			try {
				this.sha = MessageDigest.getInstance("SHA-1");
				this.state = sha.digest();
			} catch (NoSuchAlgorithmException e) {
				throw new RuntimeException("can't find SHA-1!");
			}
		}

		public void nextBytes(byte[] bytes) {

			int off = 0;

			sha.update(state);

			while (off < bytes.length) {
				state = sha.digest();

				if (bytes.length - off > state.length) {
					System.arraycopy(state, 0, bytes, off, state.length);
				} else {
					System.arraycopy(state, 0, bytes, off, bytes.length - off);
				}

				off += state.length;

				sha.update(state);
			}
		}
	}
}
