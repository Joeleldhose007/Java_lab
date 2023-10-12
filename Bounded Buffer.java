public class BoundedBuffer {
    private int[] buffer;
    private int size;
    private int count;
    private int in;
    private int out;

    public BoundedBuffer(int size) {
        this.size = size;
        this.buffer = new int[size];
        this.count = 0;
        this.in = 0;
        this.out = 0;
    }

    public synchronized void produce(int item) throws InterruptedException {
        while (count == size) {
            wait(); // Wait if the buffer is full
        }
        buffer[in] = item;
        in = (in + 1) % size;
        count++;
        notify(); // Notify waiting consumers
    }

    public synchronized int consume() throws InterruptedException {
        while (count == 0) {
            wait(); // Wait if the buffer is empty
        }
        int item = buffer[out];
        out = (out + 1) % size;
        count--;
        notify(); // Notify waiting producers
        return item;
    }

    public static void main(String[] args) {
        BoundedBuffer buffer = new BoundedBuffer(10);

        Thread producer = new Thread(() -> {
            for (int i = 1; i <= 10; i++) {
                try {
                    buffer.produce(i);
                    System.out.println("Produced: " + i);
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        Thread consumer = new Thread(() -> {
            for (int i = 1; i <= 10; i++) {
                try {
                    int item = buffer.consume();
                    System.out.println("Consumed: " + item);
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        producer.start();
        consumer.start();
    }
}
