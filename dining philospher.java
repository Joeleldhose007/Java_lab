import java.util.concurrent.Semaphore;

class Philosopher extends Thread {
    private int id;
    private Semaphore leftFork;
    private Semaphore rightFork;

    public Philosopher(int id, Semaphore leftFork, Semaphore rightFork) {
        this.id = id;
        this.leftFork = leftFork;
        this.rightFork = rightFork;
    }

    public void run() {
        try {
            while (true) {
                System.out.println("Philosopher " + id + " is thinking.");
                Thread.sleep(1000); // Thinking time

                leftFork.acquire();
                boolean hasRightFork = rightFork.tryAcquire();
                if (!hasRightFork) {
                    leftFork.release();
                    continue;
                }

                System.out.println("Philosopher " + id + " is eating.");
                Thread.sleep(1000); // Eating time

                rightFork.release();
                leftFork.release();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

public class DiningPhilosophers {
    public static void main(String[] args) {
        int numPhilosophers = 5;
        Semaphore[] forks = new Semaphore[numPhilosophers];

        for (int i = 0; i < numPhilosophers; i++) {
            forks[i] = new Semaphore(1);
        }

        for (int i = 0; i < numPhilosophers; i++) {
            int leftForkIndex = i;
            int rightForkIndex = (i + 1) % numPhilosophers;
            Philosopher philosopher = new Philosopher(i, forks[leftForkIndex], forks[rightForkIndex);
            philosopher.start();
        }
    }
}
