/**
 *
 *  @author Wydra Weronika S27584
 *
 */

package zad3;

import javax.swing.*;

public class Main {
  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      TaskList tl = new TaskList();
      tl.setVisible(true);
    });
  }
}
