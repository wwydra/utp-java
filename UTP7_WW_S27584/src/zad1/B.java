package zad1;

public class B extends Thread {
    private double sumWeight;
    private int count;
    private final A a;

    public B(A a) {
        this.sumWeight = 0;
        this.count = 0;
        this.a = a;
    }

    @Override
    public void run() {
        while (!a.isDone()) {
            synchronized (a.getSynch()) {
                try {
                    a.getSynch().wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            do {
                this.sumWeight += a.getTowary().get(this.count).getWeight();
                this.count++;
                if (this.count % 100 == 0){
                    System.out.println("policzono wage " + this.count + " towarów");
                }
            }while (this.count % 200 != 0 && this.count < a.getTowary().size());

            if (this.count % 200 == 0) {
                synchronized (a.getSynch()) {
                    a.getSynch().notify();
                }
            }
        }
        System.out.println("Sumaryczna waga wszystkich towarow: " + this.sumWeight);
    }
}
