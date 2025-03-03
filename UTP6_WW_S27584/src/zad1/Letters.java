package zad1;

public class Letters implements Runnable{

    protected String letters;
    protected Thread[] threads;

    public Letters(String letters) {
        this.letters = letters;
        this.threads = new Thread[letters.length()];

        for (int i = 0; i < threads.length; i++) {
            String letter = String.valueOf(letters.charAt(i));
            threads[i] = new Thread(() -> {
                while (!Thread.currentThread().isInterrupted()){
                    System.out.print(letter);
                    try {
                        Thread.sleep(1000);
                    }catch (InterruptedException e){
                        Thread.currentThread().interrupt();
                    }
                }
            }, "Thread " + letter);
        }
    }

    @Override
    public void run() {
        for (Thread t : this.threads){
            t.start();
        }
    }

    public Thread[] getThreads() {
        return this.threads;
    }
}
