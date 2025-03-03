/**
 *
 *  @author Wydra Weronika S27584
 *
 */

package zad1;

public class Main {

  public static void main(String[] args) {
    String path = "..\\Towary.txt";
    Object synch = new Object();

    A a = new A(path, synch);
    B b = new B(a);

    a.start();
    b.start();

    try {
      a.join();
      b.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
