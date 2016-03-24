import java.util.*;

public class PhilosophersDinner {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Number of philosophers for the simulation: ");
        int n = sc.nextInt();
        System.out.print("Time that a philosopher spends dinning: ");
        int t = sc.nextInt();
	System.out.print("Number of times a philosopher eats: ");
        int nTimes = sc.nextInt();
        sc.close();

        Philosopher[] p = new Philosopher[n];
        Chopstick[] c = new Chopstick[n];

        for (int i = 0; i < n; i++) {
            c[i] = new Chopstick(i);
            p[i] = new Philosopher(i, t, nTimes);
        }
        for (int i = 0; i < n; i++) {
            p[i].setLeft(c[i]);
            if (i == 0)
                p[i].setRight(c[n-1]);
            else
                p[i].setRight(c[i-1]);
        }

        for (int i = 0; i < n; i += 2)
            p[i].start();

        for (int i = 1; i < n; i += 2)
            p[i].start();
    }
}

class Philosopher extends Thread {
    private int philosopherID;
    private Chopstick leftChopstick;
    private Chopstick rightChopstick;
    private boolean left;
    private boolean right;
    private int time;
    private int nTimes;

    public Philosopher(int philosopherID, int time, int nTimes) {
        this.philosopherID = philosopherID;
        this.leftChopstick = null;
        this.rightChopstick = null;
        this.left = false;
        this.right = false;
        this.time = time;
        this.nTimes = nTimes;
    }

    public Philosopher(int philosopherID, Chopstick leftChopstick, Chopstick rightChopstick) {
        this.philosopherID = philosopherID;
        this.leftChopstick = leftChopstick;
        this.rightChopstick = rightChopstick;
        this.left = false;
        this.right = false;
        this.nTimes = 1;
    }

    public int getID() {
        return this.philosopherID;
    }
    public Chopstick getLeft() {
        return this.leftChopstick;
    }
    public Chopstick getRight() {
        return this.rightChopstick;
    }
    public void setLeft(Chopstick left) {
        this.leftChopstick = left;
    }
    public void setRight(Chopstick right) {
        this.rightChopstick = right;
    }

    public void run() {
        for (int i = 0; i < this.nTimes; i++) {
            while (!this.left && !this.right) {
                if (!this.leftChopstick.getUse() && !this.rightChopstick.getUse()) {
                    this.leftChopstick.pick();
                    this.rightChopstick.pick();
                    this.right = true;
                    this.left = true;
                }
            }
            System.out.println("I'm philosopher " + this.philosopherID + ", and I'm dinning");
            try {
                sleep(this.time);
            } catch (InterruptedException e) {
                System.out.println(e);
            }
            System.out.println("I'm philosopher " + this.philosopherID + ", and I'm done");
            this.right = false;
            this.left = false;
            this.leftChopstick.down();
            this.rightChopstick.down();
        }
    }
    public String getInfo() {
        return "I'm philosopher " + this.philosopherID + ", my right chopstick is: " +
            this.rightChopstick.getID() + ", my left chopstick is: " + this.leftChopstick.getID();
    }

}

class Chopstick {
    private int chopstickID;
    private volatile boolean use;

    public Chopstick(int chopstickID) {
        this.chopstickID = chopstickID;
        use = false;
    }

    public int getID () {
        return this.chopstickID;
    }
    public boolean getUse() {
        return this.use;
    }
    public void pick() {
        this.use = true;
    }
    public void down() {
        this.use = false;
    }
}
