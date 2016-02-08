package galaxia.gui;

import galaxia.Galaxy;
import galaxia.GalaxyProperties;
import java.util.Vector;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MainPanel extends JPanel
  implements ControlPanelListener
{
  Galaxy _oGalaxy;
  GalaxyPanel _oGalaxyPanel;
  ControlPanel _oControlPanel;
  JLabel _lblStarCount;
  JLabel _lblGeneration;

  public MainPanel()
  {
    initComponents();

    _oGalaxy = new Galaxy();
    _oGalaxy.setProperties(new GalaxyProperties());

    _oGalaxyPanel = new GalaxyPanel();
    _oGalaxyPanel.setGalaxy(_oGalaxy);

    _oControlPanel = new ControlPanel();
    _oControlPanel.setProperties(_oGalaxy.getProperties());
    _oControlPanel.setGalaxyPanel(_oGalaxyPanel);
    _oControlPanel.setControlPanelListener(this);

    add(_oGalaxyPanel);
    _oGalaxyPanel.setBounds(0, 0, 400, 400);
    add(_oControlPanel);
    _oControlPanel.setLocation(_oGalaxyPanel.getWidth(), 0);
    add(this._lblStarCount = new JLabel());
    _lblStarCount.setBounds(32, 416, 256, 64);
    add(this._lblGeneration = new JLabel());
    _lblGeneration.setBounds(32, 432, 256, 64);
  }

  private void initComponents()
  {
    GroupLayout layout = new GroupLayout(this);
    setLayout(layout);
    layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGap(0, 435, 32767));

    layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGap(0, 367, 32767));
  }

  public void onAdvanceGeneration()
  {
    if (_oGalaxy.getProperties().rotate)
      _oGalaxy.Rotate();
    if (_oGalaxy.getProperties().grow)
      _oGalaxy.Grow();
    _oGalaxyPanel.updateUI();

    _oGalaxy.setGeneration(_oGalaxy.getGeneration() + 1);
    _lblStarCount.setText("Cantidad de estrellas: " + _oGalaxy.getVector().size());
    _lblGeneration.setText("Generación: " + _oGalaxy.getGeneration());
  }

  public void onInitializeGalaxy()
  {
    _oGalaxy.InitializeMatrix();
    _oGalaxyPanel.updateUI();

    _oGalaxy.setGeneration(0);
    _lblStarCount.setText("Cantidad de estrellas: " + _oGalaxy.getVector().size());
    _lblGeneration.setText("Generación: " + _oGalaxy.getGeneration());
  }
}