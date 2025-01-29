import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Scanner;

public class RSA{
    private static final SecureRandom random = new SecureRandom();
    private static BigInteger n, e, d;

    public static void generateKeys(int bits) {
        BigInteger p = BigInteger.probablePrime(bits / 2, random);
        BigInteger q = BigInteger.probablePrime(bits / 2, random);
        n = p.multiply(q);
        BigInteger phi = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));

        e = BigInteger.probablePrime(bits / 2, random);
        while (phi.gcd(e).compareTo(BigInteger.ONE) != 0) {
            e = BigInteger.probablePrime(bits / 2, random);
        }

        d = e.modInverse(phi);

        System.out.println("\nPublic Key: (e=" + e + ", n=" + n + ")");
        System.out.println("Private Key: (d=" + d + ", n=" + n + ")");
    }

    public static BigInteger[] encrypt(String message) {
        BigInteger[] encrypted = new BigInteger[message.length()];
        for (int i = 0; i < message.length(); i++) {
            encrypted[i] = BigInteger.valueOf((int) message.charAt(i)).modPow(e, n);
        }
        return encrypted;
    }

    public static String decrypt(BigInteger[] ciphertext) {
        StringBuilder decryptedMessage = new StringBuilder();
        for (BigInteger cipher : ciphertext) {
            decryptedMessage.append((char) cipher.modPow(d, n).intValue());
        }
        return decryptedMessage.toString();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        generateKeys(512);

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
                BigInteger[] encryptedMessage = encrypt(message);
                System.out.print("\nEncrypted Message: ");
                for (BigInteger num : encryptedMessage) {
                    System.out.print(num + " ");
                }
                System.out.println();
            } else if (choice == 2) {
                System.out.print("\nEnter the encrypted message (space-separated numbers): ");
                String[] encryptedInput = scanner.nextLine().split(" ");
                BigInteger[] encryptedMessage = new BigInteger[encryptedInput.length];

                for (int i = 0; i < encryptedInput.length; i++) {
                    encryptedMessage[i] = new BigInteger(encryptedInput[i]);
                }

                String decryptedMessage = decrypt(encryptedMessage);
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
