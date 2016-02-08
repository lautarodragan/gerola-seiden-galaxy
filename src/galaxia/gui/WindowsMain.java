package galaxia.gui;

import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;
import java.awt.Container;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.JFrame;
import javax.swing.UIManager;

public class WindowsMain extends JFrame
{
  public WindowsMain()
  {
    try
    {
      UIManager.setLookAndFeel(new WindowsLookAndFeel());
    } catch (Exception ex) {
    }
    initComponents();
    MainPanel oMainPanel = new MainPanel();
    add(oMainPanel);
    oMainPanel.setBounds(0, 0, 800, 600);
    setSize(800, 600);
  }

  private void initComponents()
  {
    setDefaultCloseOperation(3);
    setTitle("Simulación de Galaxia en Espiral (offline)");

    GroupLayout layout = new GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGap(0, 333, 32767));

    layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGap(0, 356, 32767));

    pack();
  }
}