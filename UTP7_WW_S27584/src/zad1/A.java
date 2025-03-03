package zad1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class A extends Thread {
    private final String path;
    private List<Towar> towarList;
    private final Object synch;
    private boolean done;

    public A(String path, Object synch) {
        this.towarList = new ArrayList<>();
        this.path = path;
        this.synch = synch;
        this.done = false;
    }

    @Override
    public void run() {
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            int count = 0;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                int id = Integer.parseInt(parts[0]);
                double weight = Double.parseDouble(parts[1]);

                Towar towar = new Towar(id, weight);

                synchronized (synch) {
                    towarList.add(towar);
                    count++;

                    if (count % 200 == 0) {
                        System.out.println("utworzono " + count + " obiektów");
                        if (count < 10000) {
                            synch.notify();
                            try {
                                synch.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
            this.done = true;
            synchronized (synch) {
                synch.notify();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Towar> getTowary() {
        return towarList;
    }

    public synchronized Object getSynch() {
        return synch;
    }

    public synchronized boolean isDone() {
        return done;
    }
}
