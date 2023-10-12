import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

class BarberShop {
    private final int maxSeats;
    private final BlockingQueue<Customer> waitingRoom;
    
    public BarberShop(int maxSeats) {
        this.maxSeats = maxSeats;
        this.waitingRoom = new ArrayBlockingQueue<>(maxSeats);
    }
    
    public void addCustomer(Customer customer) throws InterruptedException {
        if (waitingRoom.size() < maxSeats) {
            waitingRoom.put(customer);
            System.out.println("Customer " + customer.getId() + " enters the waiting room.");
        } else {
            System.out.println("Customer " + customer.getId() + " leaves because the waiting room is full.");
        }
    }
    
    public Customer nextCustomer() throws InterruptedException {
        return waitingRoom.take();
    }
    
    public int getWaitingCustomers() {
        return waitingRoom.size();
    }
}

class Barber {
    public void cutHair(Customer customer) {
        System.out.println("Barber is cutting hair for Customer " + customer.getId());
    }
}

class Customer {
    private static int idCounter = 1;
    private final int id;
    
    public Customer() {
        this.id = idCounter++;
    }
    
    public int getId() {
        return id;
    }
}

public class SleepingBarberExample {
    public static void main(String[] args) {
        BarberShop shop = new BarberShop(3);
        Barber barber = new Barber();
        
        Thread barberThread = new Thread(() -> {
            while (true) {
                try {
                    Customer customer = shop.nextCustomer();
                    barber.cutHair(customer);
                    System.out.println("Customer " + customer.getId() + " is done.");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        
        barberThread.start();
        
        for (int i = 1; i <= 10; i++) {
            Customer customer = new Customer();
            try {
                shop.addCustomer(customer);
                Thread.sleep(200); // Simulate arrival of new customers
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
