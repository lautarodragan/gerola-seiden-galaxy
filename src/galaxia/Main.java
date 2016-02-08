package galaxia;

import galaxia.gui.WindowsMain;
import java.awt.EventQueue;

public class Main
{
  public static void main(String[] args)
  {
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        new WindowsMain().setVisible(true);
      }
    });
  }
}