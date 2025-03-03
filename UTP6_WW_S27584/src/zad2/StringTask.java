package zad2;
public class StringTask implements Runnable{
    enum TaskState {CREATED, RUNNING, ABORTED, READY}
    private volatile TaskState state;
    private final String writing;
    private final int number;
    private String result;
    private final Thread thread;

    public StringTask(String writing, int number) {
        this.writing = writing;
        this.number = number;
        this.state = TaskState.CREATED;
        this.result = "";
        this.thread = new Thread(this);
    }

    @Override
    public void run() {
        this.state = TaskState.RUNNING;
        for (int i = 0; i < this.number; i++) {
            if (!Thread.currentThread().isInterrupted()){
                this.result += writing;
            }else{
                this.state = TaskState.ABORTED;
                return;
            }
        }
        this.state = TaskState.READY;
    }

    public String getResult(){
        return result;
    }

    public TaskState getState(){
        return state;
    }

    public void start(){
        this.thread.start();
    }

    public void abort(){
        this.state = TaskState.ABORTED;
        this.thread.interrupt();
    }

    public boolean isDone(){
        return this.state == TaskState.READY || this.state == TaskState.ABORTED;
    }
}
