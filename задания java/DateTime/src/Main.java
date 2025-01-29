
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.Random;

public class DateTasks {

    private static Instant LocalDateTime;

    public static void main(String[] args) {
        // 1. Основы LocalDate и LocalTime
        printCurrentDateAndTime();

        // 2. Сравнение дат
        LocalDate date1 = LocalDate.of(2023, 10, 20);
        LocalDate date2 = LocalDate.of(2023, 11, 15);
        System.out.println(compareDates(date1, date2));

        // 3. Сколько дней до Нового года?
        System.out.println("Дней до Нового года: " + daysUntilNewYear());

        // 4. Проверка високосного года
        System.out.println("2024 - високосный год: " + isLeapYear(2024));
        System.out.println("2023 - високосный год: " + isLeapYear(2023));

        // 5. Подсчет выходных за месяц
        System.out.println("Выходных в ноябре 2023: " + countWeekends(11, 2023));

        // 6. Расчет времени выполнения метода
        measureMethodExecutionTime(() -> {
            for (int i = 0; i < 1_000_000; i++) {
                // do nothing
            }
        });

        // 7. Форматирование и парсинг даты
        formatAndParseDate("25-12-2023");

        // 8. Конвертация между часовыми поясами
        convertTimeZone(java.time.LocalDateTime.from(Instant.now(Clock.system(ZoneOffset.UTC))), ZoneId.of("Europe/Moscow"));

        // 9. Вычисление возраста по дате рождения
        LocalDate birthDate = LocalDate.of(1990, 5, 15);
        System.out.println("Возраст: " + calculateAge(birthDate));


        // 10. Создание календаря на месяц
        printCalendar(12, 2023);


        // 11. Генерация случайной даты в диапазоне
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 12, 31);
        System.out.println("Случайная дата: " + generateRandomDate(startDate, endDate));

        // 12. Расчет времени до заданной даты
        LocalDateTime eventDateTime = LocalDateTime.now().plusDays(2).plusHours(5).plusMinutes(30);
        calculateTimeUntilEvent(eventDateTime);

        // 13. Вычисление количества рабочих часов
        LocalDateTime startWork = LocalDateTime.of(2023, 11, 27, 9, 0);
        LocalDateTime endWork = LocalDateTime.of(2023, 11, 27, 18, 0);
        System.out.println("Количество рабочих часов: " + calculateWorkingHours(startWork, endWork));

        // 14. Конвертация даты в строку с учетом локали
        LocalDate dateForLocale = LocalDate.of(2023, 12, 25);
        System.out.println("Дата в русском формате: " + formatDateWithLocale(dateForLocale, Locale.forLanguageTag("ru")));
        System.out.println("Дата в английском формате: " + formatDateWithLocale(dateForLocale, Locale.ENGLISH));

        // 15. Определение дня недели по дате
        LocalDate someDate = LocalDate.of(2023, 12, 2);
        System.out.println("День недели: " + getDayOfWeekRussian(someDate));

    }
    // 1
    public static void printCurrentDateAndTime() {
        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        System.out.println("Текущая дата и время: " + currentDate.format(formatter) + " " + currentTime.format(formatter));
    }
    // 2
    public static String compareDates(LocalDate date1, LocalDate date2) {
        if (date1.isAfter(date2)) {
            return "Первая дата больше второй";
        } else if (date1.isBefore(date2)) {
            return "Первая дата меньше второй";
        } else {
            return "Даты равны";
        }
    }
    // 3
    public static long daysUntilNewYear() {
        LocalDate today = LocalDate.now();
        LocalDate newYear = LocalDate.of(today.getYear() + 1, 1, 1);
        return ChronoUnit.DAYS.between(today, newYear);
    }

    // 4
    public static boolean isLeapYear(int year) {
        return Year.isLeap(year);
    }
    // 5
    public static int countWeekends(int month, int year) {
        LocalDate firstDayOfMonth = LocalDate.of(year, month, 1);
        LocalDate lastDayOfMonth = firstDayOfMonth.withDayOfMonth(firstDayOfMonth.lengthOfMonth());
        int weekends = 0;
        for (LocalDate date = firstDayOfMonth; !date.isAfter(lastDayOfMonth); date = date.plusDays(1)) {
            if (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY) {
                weekends++;
            }
        }
        return weekends;
    }

    // 6
    public static void measureMethodExecutionTime(Runnable method) {
        long startTime = System.nanoTime();
        method.run();
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1_000_000; // in milliseconds
        System.out.println("Время выполнения метода: " + duration + " мс");
    }

    // 7
    public static void formatAndParseDate(String dateString) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate date = LocalDate.parse(dateString, inputFormatter);
        LocalDate futureDate = date.plusDays(10);
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        System.out.println("Дата через 10 дней: " + futureDate.format(outputFormatter));
    }

    // 8
    public static void convertTimeZone(LocalDateTime dateTime, ZoneId targetZone) {
        // Получаем текущий момент в UTC
        ZonedDateTime utcTime = ZonedDateTime.of(dateTime, ZoneOffset.UTC);

        // Преобразуем в целевой часовой пояс
        ZonedDateTime convertedTime = utcTime.withZoneSameInstant(targetZone);
        System.out.println("Время в UTC: " + utcTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z")));
        System.out.println("Время в " + targetZone + ": " + convertedTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z")));
    }

    // 9
    public static int calculateAge(LocalDate birthDate) {
        LocalDate currentDate = LocalDate.now();
        return Period.between(birthDate, currentDate).getYears();
    }

    // 10
    public static void printCalendar(int month, int year) {
        LocalDate firstDayOfMonth = LocalDate.of(year, month, 1);
        LocalDate lastDayOfMonth = firstDayOfMonth.withDayOfMonth(firstDayOfMonth.lengthOfMonth());
        System.out.println("Календарь на " + firstDayOfMonth.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault()) + " " + year);
        for(LocalDate date = firstDayOfMonth; !date.isAfter(lastDayOfMonth); date = date.plusDays(1)){
            String dayType = (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY) ? "Выходной" : "Рабочий";
            System.out.println(date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + " - " + dayType);
        }

    }

    // 11
    public static LocalDate generateRandomDate(LocalDate startDate, LocalDate endDate) {
        long startEpochDay = startDate.toEpochDay();
        long endEpochDay = endDate.toEpochDay();
        Random random = new Random();
        long randomEpochDay = startEpochDay + random.nextLong(endEpochDay - startEpochDay + 1);
        return LocalDate.ofEpochDay(randomEpochDay);
    }

    // 12
    public static void calculateTimeUntilEvent(LocalDateTime eventDateTime) {
        LocalDateTime now = java.time.LocalDateTime.from(Instant.now()); // Теперь используем LocalDateTime
        Duration duration = Duration.between(now, eventDateTime);
        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;
        long seconds = duration.getSeconds() % 60;
        System.out.println("До события осталось: " + hours + " часов, " + minutes + " минут, " + seconds + " секунд");
    }


    // 13
    public static long calculateWorkingHours(LocalDateTime startWork, LocalDateTime endWork) {
        if(startWork.toLocalDate().isEqual(endWork.toLocalDate())){
            return ChronoUnit.HOURS.between(startWork, endWork);
        }
        return 0;
    }

    // 14
    public static String formatDateWithLocale(LocalDate date, Locale locale) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", locale);
        return date.format(formatter);
    }

    // 15
    public static String getDayOfWeekRussian(LocalDate date) {
        return date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.forLanguageTag("ru"));
    }

}
