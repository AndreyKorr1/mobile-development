import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

class CaesarCipher {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Выберите метод:");
        System.out.println("1 - Шифрование текста из файла");
        System.out.println("2 - Расшифровка с известным ключом");
        System.out.println("3 - Расшифровка методом brute force");
        int method = scanner.nextInt();
        scanner.nextLine(); // очистка буфера

        if (method == 1) {
            System.out.println("Введите путь к файлу с оригинальным текстом:");
            String originalFilePath = scanner.nextLine();
            System.out.println("Введите путь для сохранения зашифрованного текста:");
            String encryptedFilePath = scanner.nextLine();
            System.out.println("Введите ключ (целое число от 0 до 25):");
            int key = scanner.nextInt();

            // Валидация ключа
            if (key < 0 || key >= 26) {
                System.out.println("Ключ должен быть в диапазоне от 0 до 25.");
                return;
            }

            try {
                encryptFile(originalFilePath, encryptedFilePath, key);
                System.out.println("Текст зашифрован и сохранен в '" + encryptedFilePath + "'.");
            } catch (IOException e) {
                System.out.println("Ошибка при работе с файлом: " + e.getMessage());
            }
        } else if (method == 2) {
            System.out.println("Введите путь к зашифрованному файлу:");
            String encryptedFilePath = scanner.nextLine();
            System.out.println("Введите ключ (целое число):");
            int key = scanner.nextInt();

            // Валидация ключа
            if (key < 0) {
                System.out.println("Ключ должен быть неотрицательным.");
                return;
            }

            try {
                String content = new String(Files.readAllBytes(Paths.get(encryptedFilePath)));
                String decrypted = decrypt(content, key);
                System.out.println("Введите путь для сохранения расшифрованного файла:");
                String outputFilePath = scanner.next();
                writeFile(outputFilePath, decrypted);
                System.out.println("Текст расшифрован и сохранен в '" + outputFilePath + "'.");
            } catch (IOException e) {
                System.out.println("Ошибка при работе с файлом: " + e.getMessage());
            }
        } else if (method == 3) {
            System.out.println("Введите путь к зашифрованному файлу:");
            String encryptedFilePath = scanner.nextLine();
            System.out.println("Введите путь к файлу с образцом текста (или оставьте пустым):");
            String referenceFilePath = scanner.nextLine();
            String referenceText = null;

            if (!referenceFilePath.isEmpty()) {
                try {
                    referenceText = new String(Files.readAllBytes(Paths.get(referenceFilePath)));
                } catch (IOException e) {
                    System.out.println("Ошибка при чтении файла с образцом текста: " + e.getMessage());
                    return;
                }
            }

            try {
                String content = new String(Files.readAllBytes(Paths.get(encryptedFilePath)));
                String bestDecryption = bruteForceDecrypt(content, referenceText);
                System.out.println("Введите путь для сохранения расшифрованного файла:");
                String outputFilePath = scanner.nextLine();
                writeFile(outputFilePath, bestDecryption);
                System.out.println("Текст расшифрован и сохранен в '" + outputFilePath + "'.");
            } catch (IOException e) {
                System.out.println("Ошибка при работе с файлом: " + e.getMessage());
            }
        } else {
            System.out.println("Неверный выбор метода.");
        }
    }

    public static void encryptFile(String originalFilePath, String encryptedFilePath, int key) throws IOException {
        if (!Files.exists(Paths.get(originalFilePath))) {
            throw new FileNotFoundException("Файл оригинала не найден по пути: " + originalFilePath);
        }

        String content = new String(Files.readAllBytes(Paths.get(originalFilePath)));
        String encryptedContent = encrypt(content, key);
        writeFile(encryptedFilePath, encryptedContent);
    }

    public static String encrypt(String text, int key) {
        StringBuilder encrypted = new StringBuilder();
        for (char c : text.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isLowerCase(c) ? 'a' : 'A';
                c = (char) ((c + key - base) % 26 + base);
            }
            encrypted.append(c);
        }
        return encrypted.toString();
    }

    public static String decrypt(String text, int key) {
        return encrypt(text, 26 - key % 26); // Дешифрование - сдвиг в обратную сторону
    }

    public static String bruteForceDecrypt(String text, String referenceText) {
        String bestDecryption = null;
        int bestScore = Integer.MIN_VALUE;

        for (int key = 0; key < 26; key++) {
            String decryptedText = decrypt(text, key);
            int score = scoreDecryption(decryptedText, referenceText);
            if (score > bestScore) {
                bestScore = score;
                bestDecryption = decryptedText;
            }
        }
        return bestDecryption != null ? bestDecryption : text; // Возвращаем оригинал, если ничего не найдено
    }

    private static int scoreDecryption(String decryptedText, String referenceText) {
        // Простейшая оценка: количество совпадений с референсным текстом
        if (referenceText == null || referenceText.isEmpty()) {
            return decryptedText.length(); // Если нет референсного текста, просто возвращаем длину
        }

        int score = 0;
        for (String word : referenceText.split(" +")) {
            if (decryptedText.contains(word)) {
                score++;
            }
        }
        return score;
    }

    private static void writeFile(String filePath, String content) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(content);
        }
    }
}
