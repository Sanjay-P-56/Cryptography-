import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Scanner;

public class ElGamal {
    private static final SecureRandom random = new SecureRandom();

    public static class KeyPair {
        public PublicKey publicKey;
        public PrivateKey privateKey;

        public KeyPair(PublicKey publicKey, PrivateKey privateKey) {
            this.publicKey = publicKey;
            this.privateKey = privateKey;
        }
    }

    public static class PublicKey {
        public BigInteger p, g, h;

        public PublicKey(BigInteger p, BigInteger g, BigInteger h) {
            this.p = p;
            this.g = g;
            this.h = h;
        }
    }

    public static class PrivateKey{
        public BigInteger p, g, x;

        public PrivateKey(BigInteger p, BigInteger g, BigInteger x) {
            this.p = p;
            this.g = g;
            this.x = x;
        }
    }

    public static KeyPair generateKeyPair(int bits) {
        BigInteger p = BigInteger.probablePrime(bits, random);
        BigInteger g = new BigInteger(bits - 1, random).mod(p);
        BigInteger x = new BigInteger(bits - 2, random).mod(p.subtract(BigInteger.TWO)).add(BigInteger.TWO);
        BigInteger h = g.modPow(x, p);
        return new KeyPair(new PublicKey(p, g, h), new PrivateKey(p, g, x));
    }

    public static BigInteger[] encrypt(PublicKey publicKey, char message) {
        BigInteger p = publicKey.p, g = publicKey.g, h = publicKey.h;
        BigInteger m = BigInteger.valueOf((int) message);
        BigInteger y = new BigInteger(p.bitLength() - 2, random).mod(p.subtract(BigInteger.TWO)).add(BigInteger.TWO);
        BigInteger c1 = g.modPow(y, p);
        BigInteger c2 = m.multiply(h.modPow(y, p)).mod(p);
        return new BigInteger[]{c1, c2};
    }

    public static char decrypt(PrivateKey privateKey, BigInteger[] ciphertext) {
        BigInteger p = privateKey.p, x = privateKey.x;
        BigInteger c1 = ciphertext[0], c2 = ciphertext[1];
        BigInteger s = c1.modPow(x, p);
        BigInteger sInverse = s.modInverse(p);
        BigInteger m = c2.multiply(sInverse).mod(p);
        return (char) m.intValue();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Generating ElGamal keys...");
        KeyPair keyPair = generateKeyPair(512);
        PublicKey publicKey = keyPair.publicKey;
        PrivateKey privateKey = keyPair.privateKey;

        System.out.println("\nPublic Key: ");
        System.out.println("p = " + publicKey.p);
        System.out.println("g = " + publicKey.g);
        System.out.println("h = " + publicKey.h);

        System.out.println("\nPrivate Key: ");
        System.out.println("x = " + privateKey.x);

        while (true) {
            System.out.println("\nChoose an option:");
            System.out.println("1. Encrypt a message");
            System.out.println("2. Decrypt a message");
            System.out.println("3. Exit");

            System.out.print("\nEnter your choice (1/2/3): ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 1) {
                System.out.print("\nEnter a message to encrypt: ");
                String message = scanner.nextLine();
                StringBuilder encryptedMessage = new StringBuilder();
                for (char ch : message.toCharArray()) {
                    BigInteger[] encryptedChar = encrypt(publicKey, ch);
                    encryptedMessage.append("(").append(encryptedChar[0]).append(",").append(encryptedChar[1]).append(") ");
                }
                System.out.println("\nEncrypted Message: " + encryptedMessage.toString().trim());
            } else if (choice == 2) {
                System.out.print("\nEnter the encrypted message (format: (c1,c2) (c1,c2) ...): ");
                String encryptedInput = scanner.nextLine();
                encryptedInput = encryptedInput.replaceAll("[()]", "");
                String[] pairs = encryptedInput.split(" ");

                StringBuilder decryptedMessage = new StringBuilder();
                for (String pair : pairs) {
                    String[] values = pair.split(",");
                    BigInteger c1 = new BigInteger(values[0]);
                    BigInteger c2 = new BigInteger(values[1]);
                    decryptedMessage.append(decrypt(privateKey, new BigInteger[]{c1, c2}));
                }
                System.out.println("\nDecrypted Message: " + decryptedMessage);
            } else if (choice == 3) {
                System.out.println("\nExiting the program...");
                break;
            } else {
                System.out.println("\nInvalid choice, please try again.");
            }
        }

        scanner.close();
    }
}
