import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Задача 1: Singleton для базы данных
        DatabaseConnection db1 = DatabaseConnection.getInstance();
        DatabaseConnection db2 = DatabaseConnection.getInstance();

        System.out.println("Database instances are the same: " + (db1 == db2)); // Должно вывести true

        // Задача 2: Singleton для логирования
        Logger logger1 = Logger.getInstance();
        Logger logger2 = Logger.getInstance();
        logger1.log("Первое сообщение лога.");
        logger2.log("Второе сообщение лога.");
        logger1.log("Третье сообщение лога.");
        logger1.printLogs();

        System.out.println("Logger instances are the same: " + (logger1 == logger2)); // Должно вывести true

        // Задача 3: Enum для статусов заказа
        Order order1 = new Order(123);
        order1.displayStatus(); // Выводит NEW
        order1.setStatus(OrderStatus.IN_PROGRESS);
        order1.displayStatus(); // Выводит IN_PROGRESS
        order1.setStatus(OrderStatus.DELIVERED);
        order1.displayStatus(); // Выводит DELIVERED
        order1.setStatus(OrderStatus.CANCELLED); // Должно вывести сообщение об ошибке
        order1.displayStatus(); // Выводит DELIVERED


        // Задача 4: Enum для сезонов
        System.out.println("Весна на русском: " + getSeasonName(Season.SPRING));
        System.out.println("Лето на русском: " + getSeasonName(Season.SUMMER));
        System.out.println("Осень на русском: " + getSeasonName(Season.AUTUMN));
        System.out.println("Зима на русском: " + getSeasonName(Season.WINTER));

    }

    // Задача 4: Метод для получения названия сезона на русском
    public static String getSeasonName(Season season) {
        return switch (season) {
            case WINTER -> "Зима";
            case SPRING -> "Весна";
            case SUMMER -> "Лето";
            case AUTUMN -> "Осень";
        };
    }

}

// Задача 1: Singleton для базы данных
class DatabaseConnection {
    private static DatabaseConnection instance;

    private DatabaseConnection() {
        System.out.println("Создание подключения к базе данных.");
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }
}

// Задача 2: Singleton для логирования
class Logger {
    private static Logger instance;
    private final List<String> logs;

    private Logger() {
        logs = new ArrayList<>();
    }

    public static Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    public void log(String message) {
        logs.add(message);
    }

    public void printLogs() {
        for (String log : logs) {
            System.out.println(log);
        }
    }
}

// Задача 3: Enum для статусов заказа
enum OrderStatus {
    NEW,
    IN_PROGRESS,
    DELIVERED,
    CANCELLED
}

class Order {
    private final int orderId;
    private OrderStatus status;

    public Order(int orderId) {
        this.orderId = orderId;
        this.status = OrderStatus.NEW;
    }

    public void setStatus(OrderStatus newStatus) {
        if (this.status == OrderStatus.DELIVERED && newStatus == OrderStatus.CANCELLED) {
            System.out.println("Невозможно отменить доставленный заказ.");
            return;
        }
        this.status = newStatus;
    }

    public void displayStatus() {
        System.out.println("Заказ " + orderId + ", статус: " + status);
    }
}


// Задача 4: Enum для сезонов
enum Season {
    WINTER,
    SPRING,
    SUMMER,
    AUTUMN
}
