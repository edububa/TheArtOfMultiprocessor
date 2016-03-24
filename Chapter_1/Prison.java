import java.util.Scanner;
import java.util.Random;
import java.util.ArrayList;

public class Prison {
    private static ArrayList<Prisoner> p = new ArrayList<Prisoner>();

    private static boolean checker() {
        int i = 0;
        while (i < p.size() && p.get(i).getVisited())
            i++;
        return !(i < p.size() && p.get(i).getVisited());
    }

    private static void printer() {
        System.out.println("---------------------------------------------------------------");
        for (Prisoner pri : p) {
            System.out.println("\t" + pri.toString());
        }
        System.out.println("---------------------------------------------------------------");
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Room r = new Room();
        System.out.print("Write the number of prisoners for the simulation: ");
        int nPresos = sc.nextInt();
        sc.close();

        p.add(new Boss(r,0,nPresos));
        for (int i = 1; i < nPresos; i++)
            p.add(new NormalPrisoner(r,i));

        Random rand = new Random();
        printer();
        while (!p.get(0).getAllVisited()) {
            int n = rand.nextInt(nPresos);
	    // System.out.println(n);
	    printer();
	    p.get(n).run();
        }
        printer();
        if (checker())
            System.out.println("Warden: I set you free.");
        else
            System.out.println("Warden: I will feed all of you to the crocodiles");
    }
}

interface Prisoner extends Runnable {
    public boolean getVisited();
    public String toString();
    public boolean getAllVisited();
    public void run();
}

class NormalPrisoner implements Prisoner  {
    private int prisonerId;
    private boolean visited;
    private Room room;

    public NormalPrisoner(Room room, int id) {
        this.visited = false;
        this.room = room;
        this.prisonerId = id;
    }

    public boolean getAllVisited() {
        return false;
    }

    public boolean getVisited() {
        return this.visited;
    }

    public String toString() {
        return "Prisoner[" + this.prisonerId + "]: " + this.visited;
    }

    public void run() {
        if (!this.room.getState() && !visited) {
            this.room.switchState();
            System.out.println(" -> Prisoner[" + this.prisonerId + "] turns on the light" );
            this.visited = true;
        }
    }
}

class Boss implements Prisoner {
    private int prisonerId;
    private int nPresos;
    private boolean visited;
    private boolean allVisited;
    private Room room;

    public Boss(Room room, int id, int nPresos) {
        this.room = room;
        this.prisonerId = id;
        this.nPresos = nPresos - 1;
        this.visited = false;
        this.room = room;
    }

    public boolean getVisited() {
        return this.visited;
    }

    public boolean getAllVisited() {
        return this.allVisited;
    }

    public String toString() {
        return "Prisoner[" + this.prisonerId + "]: " + this.visited;
    }

    public void run() {
        this.visited = true;
        if (this.room.getState()) {
            System.out.println(" -> Boss turns off the light" );
            this.room.switchState();
            this.nPresos--;
        }
        if (nPresos == 0) {
            this.allVisited = true;
            System.out.println("We have all visited the switch room at least once.");
        }
    }
}

class Room {
    private volatile boolean state; // state: off = false, on = true

    public Room() {
        this.state = false;
    }

    public boolean getState() {
        return this.state;
    }

    public void switchState() {
        this.state = !this.state;
    }
}
