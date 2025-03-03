package zad3;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.*;

public class TaskList extends JFrame{

    private enum TaskState {
        CREATED, RUNNING, ABORTED, READY
    }
    private final DefaultListModel<Future<String>> model;
    private final JList<Future<String>> taskJList;
    private final ExecutorService executorService;

    public TaskList() {
        this.model = new DefaultListModel<>();
        this.taskJList = new JList<>(this.model);
        this.executorService = Executors.newCachedThreadPool();

        JButton addButton = new JButton("Add task");
        JButton checkButton = new JButton("Check state");
        JButton cancelButton = new JButton("Cancel task");
        JButton resultButton = new JButton("Show result");

        addButton.addActionListener(e -> addTask());
        checkButton.addActionListener(e -> checkTask());
        cancelButton.addActionListener(e -> cancelTask());
        resultButton.addActionListener(e -> showResult());

        JPanel panel = new JPanel();
        panel.add(addButton);
        panel.add(checkButton);
        panel.add(cancelButton);
        panel.add(resultButton);

        add(panel, BorderLayout.NORTH);
        add(taskJList, BorderLayout.CENTER);
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public void addTask(){
        Callable<String> task = () -> {
          Thread.sleep(5000);
          return "Result";
        };
        Future<String> future = executorService.submit(task);
        model.addElement(future);
    }

    private void checkTask() {
        int index = taskJList.getSelectedIndex();
        if (index >= 0) {
            Future<String> future = model.getElementAt(index);
            TaskState state;
            if (future.isCancelled()) {
                state = TaskState.ABORTED;
            } else if (future.isDone()) {
                state = TaskState.READY;
            } else {
                state = TaskState.RUNNING;
            }
            JOptionPane.showMessageDialog(this, "Task state: " + state, "Task state", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void cancelTask() {
        int index = taskJList.getSelectedIndex();
        if (index >= 0) {
            Future<String> ft = model.getElementAt(index);
            ft.cancel(true);
            model.removeElementAt(index);
        }
    }

    public void showResult(){
        int index = taskJList.getSelectedIndex();
        if (index >= 0){
            Future<String> ft = model.getElementAt(index);
            if (ft.isDone() && !ft.isCancelled()){
                try {
                    String res = ft.get();
                    JOptionPane.showMessageDialog(this, "Task result: " + res, "Task result", JOptionPane.INFORMATION_MESSAGE);
                } catch (ExecutionException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
