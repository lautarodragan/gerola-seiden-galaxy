package galaxia;

import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;
import galaxia.gui.MainPanel;
import galaxia.gui.WindowsMain;
import javax.swing.JApplet;
import javax.swing.UIManager;

public class AppletMain extends JApplet
{
  public void init()
  {
    try
    {
      UIManager.setLookAndFeel(new WindowsLookAndFeel());
    } catch (Exception ex) {
    }
    MainPanel oMainPanel = new MainPanel();
    add(oMainPanel);
    oMainPanel.setBounds(0, 0, 800, 600);
    setSize(800, 600);
  }

  public static void main(String[] sArgs)
  {
    new WindowsMain().setVisible(true);
  }
}