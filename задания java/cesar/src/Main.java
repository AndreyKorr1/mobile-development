import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

class CaesarCipher {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Выберите текстовый файл для работы:");
        String filePath = scanner.nextLine();

        System.out.println("Выберите действие:");
        System.out.println("1. Зашифровать текст из файла");
        System.out.println("2. Расшифровать текст из файла, используя известный ключ");
        System.out.println("3. Расшифровать методом brute force");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                System.out.println("Введите ключ для шифрования:");
                int encryptKey = scanner.nextInt();
                encryptFile(filePath, encryptKey);
                break;
            case 2:
                System.out.println("Введите ключ для расшифровки:");
                int decryptKey = scanner.nextInt();
                decryptFile(filePath, decryptKey);
                break;
            case 3:
                bruteForceDecryptFile(filePath);
                break;
            default:
                System.out.println("Неверный выбор.");
        }
    }

    private static void encryptFile(String filePath, int key) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            String encrypted = caesarCipher(content, key);
            Files.write(Paths.get(filePath), encrypted.getBytes());
            System.out.println("Текст зашифрован и сохранен в файл.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void decryptFile(String filePath, int key) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            String decrypted = caesarCipher(content, -key);
            Files.write(Paths.get(filePath), decrypted.getBytes());
            System.out.println("Текст расшифрован и сохранен в файл.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void bruteForceDecryptFile(String filePath) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            for (int key = 1; key < 26; key++) {
                String decrypted = caesarCipher(content, -key);
                System.out.println("Ключ " + key + ": " + decrypted);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private static String caesarCipher(String text, int key) {
        StringBuilder result = new StringBuilder();
        for (char c : text.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isLowerCase(c) ? 'a' : 'A';
                c = (char) ((c - base + key + 26) % 26 + base);
            }
            result.append(c);
        }
        return result.toString();
    }
}
