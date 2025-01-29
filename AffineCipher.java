import java.util.Scanner;

public class AffineCipher{

    // Letter-to-index and index-to-letter conversion
    public static int letterToIndex(char letter) {
        return Character.toUpperCase(letter) - 'A';
    }

    public static char indexToLetter(int index) {
        return (char) ('A' + index);
    }

    // Encryption function
    public static String encryptAffine(String plaintext, int alpha, int beta, int m) {
        plaintext = plaintext.replaceAll("\\s+", ""); // Remove spaces
        StringBuilder ciphertext = new StringBuilder();

        if (plaintext.matches("[a-zA-Z]+")) {
            for (char c : plaintext.toUpperCase().toCharArray()) {
                int idx = letterToIndex(c);
                int x = (alpha * idx + beta) % m;
                ciphertext.append(indexToLetter(x));
            }
            return ciphertext.toString();
        } else {
            return "An Error has occurred, please double check input used is valid.";
        }
    }

    // Decryption function
    public static String decryptAffine(String ciphertext, int alpha, int beta, int m) {
        ciphertext = ciphertext.replaceAll("\\s+", ""); // Remove spaces
        StringBuilder plaintext = new StringBuilder();

        if (ciphertext.matches("[a-zA-Z]+")) {
            int alphaInverse = 0;

            // Find the modular multiplicative inverse of alpha
            for (int aInv = 1; aInv < m; aInv++) {
                if ((aInv * alpha) % m == 1) {
                    alphaInverse = aInv;
                    break;
                }
            }

            for (char c : ciphertext.toUpperCase().toCharArray()) {
                int idx = letterToIndex(c);
                int x = (alphaInverse * (idx - beta + m)) % m; // Add `m` to ensure no negative values
                plaintext.append(indexToLetter(x));
            }
            return plaintext.toString();
        } else {
            return "An Error has occurred, please double check input used is valid.";
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        final int m = 26; // Number of letters in the English alphabet

        System.out.println("Would you like to perform encryption or decryption?");
        System.out.print("Please Enter 'e' or 'd': ");
        String userChoice = scanner.nextLine().toLowerCase();

        if (!userChoice.equals("e") && !userChoice.equals("d")) {
            System.out.println("Invalid input, please enter 'e' for encryption or 'd' for decryption.");
            return;
        }

        try {
            System.out.print("Enter an alpha value between 1 and " + (m - 1) + ": ");
            int alpha = Integer.parseInt(scanner.nextLine());

            System.out.print("Enter a beta value between 0 and " + (m - 1) + ": ");
            int beta = Integer.parseInt(scanner.nextLine());

            if (alpha < 1 || alpha > m - 1 || beta < 0 || beta > m - 1) {
                System.out.println("Alpha and Beta values are out of valid range.");
                return;
            }

            if (gcd(alpha, m) != 1) {
                System.out.println("The GCD of alpha and " + m + " must be 1.");
                return;
            }

            if (userChoice.equals("e")) {
                System.out.print("\nPlease enter the plaintext to encrypt: ");
                String plaintext = scanner.nextLine();
                String ciphertext = encryptAffine(plaintext, alpha, beta, m);
                System.out.println("\nPlaintext (Original): " + plaintext);
                System.out.println("Ciphertext (Encrypted): " + ciphertext);
            } else if (userChoice.equals("d")) {
                System.out.print("\nPlease enter the ciphertext to decrypt: ");
                String ciphertext = scanner.nextLine();
                String plaintext = decryptAffine(ciphertext, alpha, beta, m);
                System.out.println("\nCiphertext (Original): " + ciphertext);
                System.out.println("Plaintext (Decrypted): " + plaintext);
            }

        } catch (NumberFormatException e) {
            System.out.println("Invalid numeric input, please enter valid integers.");
        } finally {
            scanner.close();
        }
    }

    // Method to calculate the GCD
    public static int gcd(int a, int b) {
        if (b == 0) {
            return a;
        }
        return gcd(b, a % b);
    }
}
