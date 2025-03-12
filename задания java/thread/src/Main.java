import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.Arrays;



class Counter {
    private int count = 0;

    public synchronized void increment() {
        count++;
    }

    public int getCount() {
        return count;
    }
}

class CounterThread extends Thread {
    private Counter counter;

    public CounterThread(Counter counter) {
        this.counter = counter;
    }

    @Override
    public void run() {
        for (int i = 0; i < 1000; i++) {
            counter.increment();
        }
    }
}

class NumberAddingThread extends Thread {
    private List<Integer> list;

    public NumberAddingThread(List<Integer> list) {
        this.list = list;
    }

    @Override
    public void run() {
        for (int i = 1; i <= 100; i++) {
            list.add(i);
        }
    }
}

class BankAccount {
    private double balance;
    private ReentrantLock lock = new ReentrantLock();

    public BankAccount(double balance) {
        this.balance = balance;
    }

    public void transfer(BankAccount target, double amount) {
        lock.lock();
        try {
            if (balance >= amount) {
                balance -= amount;
                target.deposit(amount);
            }
        } finally {
            lock.unlock();
        }
    }

    public void deposit(double amount) {
        balance += amount;
    }

    public double getBalance() {
        return balance;
    }
}

class TransferThread extends Thread {
    private BankAccount from;
    private BankAccount to;
    private double amount;

    public TransferThread(BankAccount from, BankAccount to, double amount) {
        this.from = from;
        this.to = to;
        this.amount = amount;
    }

    @Override
    public void run() {
        from.transfer(to, amount);
    }
}

class Producer extends Thread {
    private BlockingQueue<Integer> queue;

    public Producer(BlockingQueue<Integer> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < 10; i++) {
                queue.put(i);
                System.out.println("Produced: " + i);
                Thread.sleep(500); // Simulate production time
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Consumer extends Thread {
    private BlockingQueue<Integer> queue;

    public Consumer(BlockingQueue<Integer> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < 10; i++) {
                Integer value = queue.take();
                System.out.println("Consumed: " + value);
                Thread.sleep(1000); // Simulate processing time
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
class SortTask implements Runnable {
    private int[] array;

    public SortTask(int[] array) {
        this.array = array;
    }

    @Override
    public void run() {
        Arrays.sort(array);
    }

}

class Philosopher extends Thread {
    private Lock leftFork;
    private Lock rightFork;

    public Philosopher(Lock leftFork, Lock rightFork) {
        this.leftFork = leftFork;
        this.rightFork = rightFork;
    }

    @Override
    public void run() {
        try {
            while (true) { // Philosophers eat indefinitely in this simulation.
                think();
                eat();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void think() throws InterruptedException {
        System.out.println(Thread.currentThread().getName() + " is thinking.");
        Thread.sleep((long)(Math.random() * 1000));
    }

    private void eat() throws InterruptedException {
        if (leftFork.tryLock()) {
            try {
                if (rightFork.tryLock()) {
                    try {
                        System.out.println(Thread.currentThread().getName() + " is eating.");
                        Thread.sleep((long)(Math.random() * 1000));
                    } finally {
                        rightFork.unlock();
                    }
                }
            } finally {
                leftFork.unlock();
            }
        }
    }
}


class MatrixMultiplicationTask implements Runnable {

    private final int[][] matrixA;
    private final int[][] matrixB;
    private final int[][] resultMatrix;
    private final int row;

    public MatrixMultiplicationTask(int[][] matrixA, int[][] matrixB, int[][] resultMatrix, int row) {
        this.matrixA = matrixA;
        this.matrixB = matrixB;
        this.resultMatrix = resultMatrix;
        this.row = row;
    }

    @Override
    public void run() {
        for (int j = 0; j < matrixB[0].length; j++) {
            resultMatrix[row][j] = calculateCell(row, j);
        }
    }

    private int calculateCell(int row, int col) {
        int sum = 0;
        for (int k = 0; k < matrixA[0].length; k++) {
            sum += matrixA[row][k] * matrixB[k][col];
        }
        return sum;
    }
}

class TimerTask extends Thread {

    private volatile boolean running;

    @Override
    public void run() {
        running = true;

        while (running) {
            System.out.println("Current time: " + System.currentTimeMillis());

            try {
                Thread.sleep(1000); // Sleep for one second
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopTimer() {
        running = false;
    }
}

public class Main {
    private static final Semaphore semaphore = new Semaphore(2);
    public static void main(String[] args) throws InterruptedException,  ExecutionException {
        //Задание 1
        /*Counter counter = new Counter();
        Thread[] threads = new Thread[5];

        for (int i = 0; i < 5; i++) {
            threads[i] = new CounterThread(counter);
            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        System.out.println("Final count: " + counter.getCount());*/

        //Задание 2
        /*List<Integer> list = new CopyOnWriteArrayList<>();
        Thread[] threads = new Thread[10];

        for (int i = 0; i < 10; i++) {
            threads[i] = new NumberAddingThread(list);
            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        System.out.println("List size: " + list.size());*/

        //Задание 3
        /*ExecutorService executorService = Executors.newFixedThreadPool(4);

        for (int i = 0; i < 20; i++) {
            final int taskId = i;
            executorService.submit(() -> {
                System.out.println("Task " + taskId + " executed by " + Thread.currentThread().getName());
            });
        }

        executorService.shutdown();*/

        //Задание 4
        /*BankAccount account1 = new BankAccount(1000);
        BankAccount account2 = new BankAccount(1000);

        TransferThread[] threads = new TransferThread[10];
        for (int i = 0; i < 10; i++) {
            threads[i] = new TransferThread(account1, account2, 100);
            threads[i].start();
        }

        for (TransferThread thread : threads) {
            thread.join();
        }

        System.out.println("Final balance of account1: " + account1.getBalance());
        System.out.println("Final balance of account2: " + account2.getBalance());*/

        //Задание 5
        /*CyclicBarrier barrier = new CyclicBarrier(5, () -> System.out.println("All tasks completed. Proceeding to next phase."));

        Runnable task = () -> {
            try {
                System.out.println(Thread.currentThread().getName() + " is working.");
                Thread.sleep(1000); // Simulate work
                barrier.await();
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        Thread[] threads = new Thread[5];
        for (int i = 0; i < 5; i++) {
            threads[i] = new Thread(task);
            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }*/

        //Задание 6
        /*Runnable task = () -> {
            try {
                semaphore.acquire();
                System.out.println(Thread.currentThread().getName() + " is accessing the resource.");
                Thread.sleep(1000); // Simulate work
                System.out.println(Thread.currentThread().getName() + " is releasing the resource.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                semaphore.release();
            }
        };

        Thread[] threads = new Thread[10];
        for (int i = 0; i < 10; i++) {
            threads[i] = new Thread(task);
            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }*/

        //Задание 7
        /*ExecutorService executorService = Executors.newFixedThreadPool(10);
        List<Future<Long>> futures = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            final int number = i;
            futures.add(executorService.submit(() -> factorial(number)));
        }

        for (Future<Long> future : futures) {
            System.out.println("Factorial: " + future.get());
        }

        executorService.shutdown();
    }

    private static long factorial(int n) {
        if (n == 0) return 1;
        return n * factorial(n - 1);*/

        //Задание 8
        /*BlockingQueue<Integer> queue = new LinkedBlockingQueue<>();

        Producer producer = new Producer(queue);
        Consumer consumer = new Consumer(queue);

        producer.start();
        consumer.start();

        producer.join();
        consumer.join();*/

        //Задание 9
        /*int[] arrayToSort = {5, 3, 8, 6, 2, 7, 4, 1};

        int midIndex = arrayToSort.length / 2;

        int[] leftPart = Arrays.copyOfRange(arrayToSort, 0, midIndex);
        int[] rightPart = Arrays.copyOfRange(arrayToSort, midIndex, arrayToSort.length);

        Thread leftSorter = new Thread(new SortTask(leftPart));
        Thread rightSorter = new Thread(new SortTask(rightPart));

        leftSorter.start();
        rightSorter.start();

        leftSorter.join();
        rightSorter.join();

        // Merge sorted parts
        int[] sortedArray = merge(leftPart, rightPart);

        System.out.println("Sorted Array: " + Arrays.toString(sortedArray));
    }

    private static int[] merge(int[] left, int[] right) {
        int[] result = new int[left.length + right.length];

        int i = 0, j = 0, k = 0;

        while (i < left.length && j < right.length) {
            if (left[i] <= right[j]) {
                result[k++] = left[i++];
            } else {
                result[k++] = right[j++];
            }
        }

        while (i < left.length) result[k++] = left[i++];

        while (j < right.length) result[k++] = right[j++];

        return result;*/

        //Задание 10
        /*Lock[] forks = new Lock[5];
        for (int i = 0; i < forks.length; i++) {
            forks[i] = new ReentrantLock();
        }

        Philosopher[] philosophers = new Philosopher[5];
        for (int i = 0; i < philosophers.length; i++) {
            philosophers[i] = new Philosopher(forks[i], forks[(i + 1) % forks.length]);
            philosophers[i].start();
        }*/

        //Задание 11
        /*int[][] matrixA = { {1, 2}, {3, 4} };
        int[][] matrixB = { {5, 6}, {7, 8} };

        int rowsA = matrixA.length;
        int colsB = matrixB[0].length;

        int[][] resultMatrix = new int[rowsA][colsB];

        Thread[] threads = new Thread[rowsA];

        for (int i = 0; i < rowsA; i++) {
            threads[i] = new Thread(new MatrixMultiplicationTask(matrixA, matrixB, resultMatrix, i));
            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        // Print the resulting matrix
        for (int[] row : resultMatrix) {
            for (int value : row) {
                System.out.print(value + " ");
            }
            System.out.println();
        }*/

        //Задание 12
        TimerTask timerTask = new TimerTask();

        timerTask.start();

        // Let the timer run for 10 seconds
        Thread.sleep(10000);

        // Stop the timer
        timerTask.stopTimer();

        // Wait for the timer thread to finish
        timerTask.join();

        System.out.println("Timer stopped.");
    }
}
