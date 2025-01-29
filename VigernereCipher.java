import java.util.*;

public class VigenereCipher {

    public static String vigenere(String text, String key, boolean encrypt) {
        StringBuilder result = new StringBuilder();
        text = text.toUpperCase();
        key = key.toUpperCase();
        int j = 0;

        for (int i = 0; i < text.length(); i++) {
            char charText = text.charAt(i);

            if (Character.isLetter(charText)) {
                char charKey = key.charAt((i + j) % key.length());
                int shift = encrypt ? (charText + charKey - 2 * 'A') % 26 : (charText - charKey + 26) % 26;
                result.append((char) (shift + 'A'));
            } else {
                result.append(charText);
                j--; 
            }
        }
        return result.toString();
    }

    public static List<String> generateKeys(String keyPattern, int length) {
        List<String> keyList = new ArrayList<>();
        char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        int missingCount = (int) keyPattern.chars().filter(ch -> ch == '?').count();
        String baseKey = keyPattern.replace("?", "");

        generateCombinations(keyList, baseKey, alphabet, new char[missingCount], 0);
        return keyList;
    }

    private static void generateCombinations(List<String> keyList, String baseKey, char[] alphabet, char[] current, int index) {
        if (index == current.length) {
            keyList.add(baseKey + new String(current));
            return;
        }
        for (char c : alphabet) {
            current[index] = c;
            generateCombinations(keyList, baseKey, alphabet, current, index + 1);
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the Vigenère cipher");
        
        System.out.print("Encrypt or Decrypt (E/D): ");
        String mode = scanner.nextLine().trim().toLowerCase();
        boolean encrypt = mode.equals("e");

        System.out.print("Enter the text: ");
        String text = scanner.nextLine().toUpperCase();

        if (encrypt) {
            System.out.print("Enter the key: ");
            String key = scanner.nextLine().toUpperCase();
            System.out.println("Encrypted Text: " + vigenere(text, key, true));
        } else {
            System.out.print("Do you have the key (y/n)? ");
            String keyChoice = scanner.nextLine().trim().toLowerCase();

            if (keyChoice.equals("y")) {
                System.out.print("Enter the key: ");
                String key = scanner.nextLine().toUpperCase();
                System.out.println("Decrypted Text: " + vigenere(text, key, false));
            } else {
                System.out.print("Enter a part of the key or length (1 or 2 or nothing): ");
                String question = scanner.nextLine().trim();

                if (question.equals("1")) {
                    System.out.print("Use '?' for missing letters in the key (e.g., C?? or CL? for CLE): ");
                    String keyPattern = scanner.nextLine().toUpperCase();
                    List<String> possibleKeys = generateKeys(keyPattern, keyPattern.length());
                    for (String key : possibleKeys) {
                        System.out.println("For key " + key + " ==> " + vigenere(text, key, false));
                    }
                } else if (question.equals("2")) {
                    System.out.print("Enter the length of the key: ");
                    int length = scanner.nextInt();
                    scanner.nextLine(); 

                    String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
                    Random random = new Random();

                    while (true) {
                        StringBuilder generatedKey = new StringBuilder();
                        for (int i = 0; i < length; i++) {
                            generatedKey.append(alphabet.charAt(random.nextInt(26)));
                        }
                        String key = generatedKey.toString();
                        System.out.println("For generated key " + key + " = " + vigenere(text, key, false));
                        System.out.print("Continue (y/n)? ");
                        if (scanner.nextLine().trim().equalsIgnoreCase("n")) {
                            break;
                        }
                    }
                } else {
                    System.out.println("Sorry, this script cannot find your decrypted text without sufficient input.");
                }
            }
        }
        scanner.close();
    }
}
