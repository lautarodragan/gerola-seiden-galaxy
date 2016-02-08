package galaxia.gui;

import galaxia.GalaxyProperties;
import galaxia.GalaxyProperties.StarsDistribution;
import helper.Timer;
import helper.TimerInterface;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JSpinner.NumberEditor;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ControlPanel extends JPanel
  implements TimerInterface
{
  private GalaxyProperties _oGalaxyProperties;
  private GalaxyPanel _oGalaxyPanel;
  private ControlPanelListener _oControlPanelListener;
  private Timer _oTimer;
  private JButton btnAction_AdvanceAllStart;
  private JButton btnAction_AdvanceAllStop;
  private JButton btnAction_AdvanceMany;
  private JButton btnAction_AdvanceOne;
  private JButton btnAction_Initialize;
  private JButton btnAction_ResetValues;
  private JLabel lblAction_Distribution;
  private JLabel lblAction_Interval;
  private JLabel lblAction_StarCount;
  private JLabel lblGrowth_GrowRate;
  private JLabel lblRotation_MovementCount;
  private JLabel lblRotation_MovementLag;
  private JLabel lblZoom;
  private JCheckBox optGrowth_Grow;
  private JCheckBox optGuiStarSizeOne;
  private JCheckBox optRotation_Rotate;
  private JPanel pnlAction;
  private JPanel pnlCreation;
  private JPanel pnlGrowth;
  private JPanel pnlRotation;
  private JPanel pnlZoom;
  private JSpinner txtAction_AdvanceMany;
  private JComboBox txtAction_Distribution;
  private JSpinner txtAction_Interval;
  private JSpinner txtAction_StarCount;
  private JSpinner txtGrowth_GrowRate;
  private JSpinner txtRotation_MovementCount;
  private JSpinner txtRotation_MovementLag;
  private JSpinner txtZoom;

  public ControlPanel()
  {
    initComponents();
    setVisible(true);
    setSize(292, 577);

    txtAction_AdvanceMany.setValue(Integer.valueOf(15));
    _oTimer = new Timer();
    _oTimer.setInterval(1L);
    _oTimer.addListener(this);
  }

  public void setProperties(GalaxyProperties oGalaxyProperties)
  {
    _oGalaxyProperties = oGalaxyProperties;
    btnAction_ResetValues_ActionPerformed(null);
  }

  public void setGalaxyPanel(GalaxyPanel oGalaxyPanel) {
    _oGalaxyPanel = oGalaxyPanel;
  }

  private void initComponents()
  {
    pnlCreation = new JPanel();
    lblAction_StarCount = new JLabel();
    txtAction_StarCount = new JSpinner();
    lblAction_Distribution = new JLabel();
    txtAction_Distribution = new JComboBox();
    btnAction_Initialize = new JButton();
    pnlRotation = new JPanel();
    lblRotation_MovementCount = new JLabel();
    txtRotation_MovementCount = new JSpinner();
    lblRotation_MovementLag = new JLabel();
    txtRotation_MovementLag = new JSpinner();
    optRotation_Rotate = new JCheckBox();
    pnlGrowth = new JPanel();
    txtGrowth_GrowRate = new JSpinner();
    lblGrowth_GrowRate = new JLabel();
    optGrowth_Grow = new JCheckBox();
    pnlZoom = new JPanel();
    txtZoom = new JSpinner();
    lblZoom = new JLabel();
    optGuiStarSizeOne = new JCheckBox();
    pnlAction = new JPanel();
    btnAction_AdvanceOne = new JButton();
    btnAction_AdvanceMany = new JButton();
    txtAction_AdvanceMany = new JSpinner();
    btnAction_AdvanceAllStart = new JButton();
    btnAction_AdvanceAllStop = new JButton();
    btnAction_ResetValues = new JButton();
    lblAction_Interval = new JLabel();
    txtAction_Interval = new JSpinner();

    setBorder(BorderFactory.createTitledBorder("Panel de Control"));
    setLayout(null);

    pnlCreation.setBorder(BorderFactory.createTitledBorder("Al crear nueva galaxia"));
    pnlCreation.setLayout(null);

    lblAction_StarCount.setText("Estrellas Iniciales");
    pnlCreation.add(lblAction_StarCount);
    lblAction_StarCount.setBounds(20, 24, 81, 14);

    txtAction_StarCount.setEditor(new JSpinner.NumberEditor(txtAction_StarCount, ""));
    txtAction_StarCount.addChangeListener(new ChangeListener() {
      public void stateChanged(ChangeEvent evt) {
        ControlPanel.this.txtAction_StarCount_StateChanged(evt);
      }
    });
    pnlCreation.add(txtAction_StarCount);
    txtAction_StarCount.setBounds(130, 20, 120, 20);

    lblAction_Distribution.setText("Distribución");
    pnlCreation.add(lblAction_Distribution);
    lblAction_Distribution.setBounds(20, 48, 55, 14);

    txtAction_Distribution.setModel(new DefaultComboBoxModel(new String[] { "Uniforme", "Angular", "por Zonas" }));
    txtAction_Distribution.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        ControlPanel.this.txtAction_DistributionActionPerformed(evt);
      }
    });
    pnlCreation.add(txtAction_Distribution);
    txtAction_Distribution.setBounds(130, 44, 120, 20);

    btnAction_Initialize.setText("(Re)Iniciar");
    btnAction_Initialize.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        ControlPanel.this.btnAction_InitializeActionPerformed(evt);
      }
    });
    pnlCreation.add(btnAction_Initialize);
    btnAction_Initialize.setBounds(90, 70, 83, 23);

    add(pnlCreation);
    pnlCreation.setBounds(10, 20, 270, 100);

    pnlRotation.setBorder(BorderFactory.createTitledBorder("Al rotar (cada estrella)"));

    lblRotation_MovementCount.setText("Movimiento angular");
    lblRotation_MovementCount.setToolTipText("Movimiento = (Valor Ingresado) / 100 * 2 * PI");

    txtRotation_MovementCount.setEditor(new JSpinner.NumberEditor(txtRotation_MovementCount, ""));
    txtRotation_MovementCount.setValue(Integer.valueOf(1));
    txtRotation_MovementCount.addChangeListener(new ChangeListener() {
      public void stateChanged(ChangeEvent evt) {
        ControlPanel.this.txtRotation_MovementCountStateChanged(evt);
      }
    });
    lblRotation_MovementLag.setText("Desface radial");

    txtRotation_MovementLag.setEditor(new JSpinner.NumberEditor(txtRotation_MovementLag, ""));
    txtRotation_MovementLag.setValue(Integer.valueOf(1));
    txtRotation_MovementLag.addChangeListener(new ChangeListener() {
      public void stateChanged(ChangeEvent evt) {
        ControlPanel.this.txtRotation_MovementLagStateChanged(evt);
      }
    });
    optRotation_Rotate.setText("Rotar");
    optRotation_Rotate.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        ControlPanel.this.optRotation_RotateActionPerformed(evt);
      }
    });
    GroupLayout pnlRotationLayout = new GroupLayout(pnlRotation);
    pnlRotation.setLayout(pnlRotationLayout);
    pnlRotationLayout.setHorizontalGroup(pnlRotationLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(pnlRotationLayout.createSequentialGroup().addContainerGap().addGroup(pnlRotationLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(pnlRotationLayout.createSequentialGroup().addComponent(lblRotation_MovementLag).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 70, 32767).addComponent(txtRotation_MovementLag, -2, 96, -2)).addGroup(pnlRotationLayout.createSequentialGroup().addComponent(lblRotation_MovementCount).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 45, 32767).addComponent(txtRotation_MovementCount, -2, 96, -2)).addComponent(optRotation_Rotate)).addContainerGap()));

    pnlRotationLayout.setVerticalGroup(pnlRotationLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(GroupLayout.Alignment.TRAILING, pnlRotationLayout.createSequentialGroup().addComponent(optRotation_Rotate).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, -1, 32767).addGroup(pnlRotationLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(lblRotation_MovementCount).addComponent(txtRotation_MovementCount, -2, -1, -2)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(pnlRotationLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(lblRotation_MovementLag).addComponent(txtRotation_MovementLag, -2, -1, -2)).addGap(257, 257, 257)));

    add(pnlRotation);
    pnlRotation.setBounds(10, 120, 270, 100);

    pnlGrowth.setBorder(BorderFactory.createTitledBorder("Al crecer (cada estrella)"));

    txtGrowth_GrowRate.setEditor(new JSpinner.NumberEditor(txtGrowth_GrowRate, ""));
    txtGrowth_GrowRate.setValue(Integer.valueOf(5));
    txtGrowth_GrowRate.addChangeListener(new ChangeListener() {
      public void stateChanged(ChangeEvent evt) {
        ControlPanel.this.txtGrowth_GrowRate_StateChanged(evt);
      }
    });
    lblGrowth_GrowRate.setText("Factor de crecimiento");
    lblGrowth_GrowRate.setToolTipText("Cuando una estrella muere, explota, liberando \"combustible\" para las estrellas aledañas.");

    optGrowth_Grow.setText("Crecer");
    optGrowth_Grow.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        ControlPanel.this.optGrowth_GrowActionPerformed(evt);
      }
    });
    GroupLayout pnlGrowthLayout = new GroupLayout(pnlGrowth);
    pnlGrowth.setLayout(pnlGrowthLayout);
    pnlGrowthLayout.setHorizontalGroup(pnlGrowthLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(pnlGrowthLayout.createSequentialGroup().addContainerGap().addGroup(pnlGrowthLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(pnlGrowthLayout.createSequentialGroup().addComponent(lblGrowth_GrowRate, -2, 108, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 30, 32767).addComponent(txtGrowth_GrowRate, -2, 96, -2)).addComponent(optGrowth_Grow)).addContainerGap()));

    pnlGrowthLayout.setVerticalGroup(pnlGrowthLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(GroupLayout.Alignment.TRAILING, pnlGrowthLayout.createSequentialGroup().addComponent(optGrowth_Grow).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, -1, 32767).addGroup(pnlGrowthLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(lblGrowth_GrowRate).addComponent(txtGrowth_GrowRate, -2, -1, -2)).addGap(41, 41, 41)));

    add(pnlGrowth);
    pnlGrowth.setBounds(10, 220, 270, 80);

    pnlZoom.setBorder(BorderFactory.createTitledBorder("Representación Gráfica"));
    pnlZoom.setLayout(null);

    txtZoom.addChangeListener(new ChangeListener() {
      public void stateChanged(ChangeEvent evt) {
        ControlPanel.this.txtZoomStateChanged(evt);
      }
    });
    pnlZoom.add(txtZoom);
    txtZoom.setBounds(139, 20, 110, 20);

    lblZoom.setText("Zoom");
    pnlZoom.add(lblZoom);
    lblZoom.setBounds(18, 24, 26, 14);

    optGuiStarSizeOne.setText("Tamaño de las estrellas siempre 1");
    optGuiStarSizeOne.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        ControlPanel.this.optGuiStarSizeOneActionPerformed(evt);
      }
    });
    pnlZoom.add(optGuiStarSizeOne);
    optGuiStarSizeOne.setBounds(20, 40, 220, 23);

    add(pnlZoom);
    pnlZoom.setBounds(10, 470, 270, 70);

    pnlAction.setBorder(BorderFactory.createTitledBorder("Acción"));

    btnAction_AdvanceOne.setText("Avanzar una generación");
    btnAction_AdvanceOne.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        ControlPanel.this.btnAction_AdvanceOneActionPerformed(evt);
      }
    });
    btnAction_AdvanceMany.setText("Avanzar X generaciones");
    btnAction_AdvanceMany.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        ControlPanel.this.btnAction_AdvanceManyActionPerformed(evt);
      }
    });
    txtAction_AdvanceMany.setModel(new SpinnerNumberModel());
    txtAction_AdvanceMany.setEditor(new JSpinner.NumberEditor(txtAction_AdvanceMany, ""));
    txtAction_AdvanceMany.addChangeListener(new ChangeListener() {
      public void stateChanged(ChangeEvent evt) {
        ControlPanel.this.txtAction_AdvanceMany_StateChanged(evt);
      }
    });
    btnAction_AdvanceAllStart.setText("Avanzar indefinidamente");
    btnAction_AdvanceAllStart.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        ControlPanel.this.btnAction_AdvanceAllStart_ActionPerformed(evt);
      }
    });
    btnAction_AdvanceAllStop.setText("Detener");
    btnAction_AdvanceAllStop.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        ControlPanel.this.btnAction_AdvanceAllStop_ActionPerformed(evt);
      }
    });
    btnAction_ResetValues.setText("Reestablecer todos los valores ");
    btnAction_ResetValues.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        ControlPanel.this.btnAction_ResetValues_ActionPerformed(evt);
      }
    });
    lblAction_Interval.setText("Intervalo");

    txtAction_Interval.addChangeListener(new ChangeListener() {
      public void stateChanged(ChangeEvent evt) {
        ControlPanel.this.txtAction_Interval_StateChanged(evt);
      }
    });
    GroupLayout pnlActionLayout = new GroupLayout(pnlAction);
    pnlAction.setLayout(pnlActionLayout);
    pnlActionLayout.setHorizontalGroup(pnlActionLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(pnlActionLayout.createSequentialGroup().addContainerGap().addGroup(pnlActionLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(pnlActionLayout.createSequentialGroup().addComponent(btnAction_AdvanceMany).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(txtAction_AdvanceMany, -1, 79, 32767)).addComponent(btnAction_AdvanceOne).addGroup(pnlActionLayout.createSequentialGroup().addComponent(btnAction_AdvanceAllStart).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(btnAction_AdvanceAllStop)).addComponent(btnAction_ResetValues).addGroup(pnlActionLayout.createSequentialGroup().addGap(32, 32, 32).addComponent(lblAction_Interval).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(txtAction_Interval, -2, 108, -2))).addContainerGap()));

    pnlActionLayout.setVerticalGroup(pnlActionLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(pnlActionLayout.createSequentialGroup().addComponent(btnAction_AdvanceOne).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(pnlActionLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(btnAction_AdvanceMany).addComponent(txtAction_AdvanceMany, -2, -1, -2)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(pnlActionLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(btnAction_AdvanceAllStart).addComponent(btnAction_AdvanceAllStop)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(pnlActionLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(txtAction_Interval, -2, -1, -2).addComponent(lblAction_Interval)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, -1, 32767).addComponent(btnAction_ResetValues).addGap(42, 42, 42)));

    add(pnlAction);
    pnlAction.setBounds(10, 300, 270, 170);
  }

  private void btnAction_InitializeActionPerformed(ActionEvent evt) {
    if (_oControlPanelListener != null)
      _oControlPanelListener.onInitializeGalaxy();
  }

  private void btnAction_AdvanceOneActionPerformed(ActionEvent evt)
  {
    if (_oControlPanelListener != null)
      _oControlPanelListener.onAdvanceGeneration();
  }

  private void txtAction_StarCount_StateChanged(ChangeEvent evt)
  {
    if (((Integer)txtAction_StarCount.getValue()).intValue() < 0)
      txtAction_StarCount.setValue(Integer.valueOf(0));
    if (((Integer)txtAction_StarCount.getValue()).intValue() > 100000)
      txtAction_StarCount.setValue(Integer.valueOf(100000));
    if (_oGalaxyProperties != null)
      _oGalaxyProperties.initialStarCount = ((Integer)txtAction_StarCount.getValue()).intValue();
  }

  private void txtAction_DistributionActionPerformed(ActionEvent evt)
  {
    if (_oGalaxyProperties == null) {
      return;
    }
    if (txtAction_Distribution.getSelectedIndex() == 0)
      _oGalaxyProperties.starsDistribution = GalaxyProperties.StarsDistribution.Uniform;
    else if (txtAction_Distribution.getSelectedIndex() == 1)
      _oGalaxyProperties.starsDistribution = GalaxyProperties.StarsDistribution.Angular;
    else if (txtAction_Distribution.getSelectedIndex() == 2)
      _oGalaxyProperties.starsDistribution = GalaxyProperties.StarsDistribution.Zone;
  }

  private void btnAction_ResetValues_ActionPerformed(ActionEvent evt)
  {
    if (_oGalaxyProperties == null) {
      return;
    }
    txtAction_StarCount.setValue(Integer.valueOf(5000));
    _oGalaxyProperties.initialStarCount = 5000;

    txtAction_Distribution.setSelectedIndex(0);
    _oGalaxyProperties.starsDistribution = GalaxyProperties.StarsDistribution.Uniform;

    optRotation_Rotate.setSelected(true);
    _oGalaxyProperties.rotate = true;

    optGrowth_Grow.setSelected(true);
    _oGalaxyProperties.grow = true;

    txtRotation_MovementCount.setValue(Integer.valueOf(5));
    _oGalaxyProperties.rotationCount = 5.0D;

    txtRotation_MovementLag.setValue(Integer.valueOf(3));
    _oGalaxyProperties.rotationLag = 3.0D;

    txtGrowth_GrowRate.setValue(Integer.valueOf(125));
    _oGalaxyProperties.growthRate = 125.0D;

    txtAction_AdvanceMany.setValue(Integer.valueOf(5));

    txtAction_Interval.setValue(Integer.valueOf(1));
  }

  private void optRotation_RotateActionPerformed(ActionEvent evt)
  {
    if (_oGalaxyProperties == null) {
      return;
    }
    _oGalaxyProperties.rotate = optRotation_Rotate.isSelected();
  }

  private void optGrowth_GrowActionPerformed(ActionEvent evt)
  {
    if (_oGalaxyProperties == null) {
      return;
    }
    _oGalaxyProperties.grow = optGrowth_Grow.isSelected();
  }

  private void btnAction_AdvanceManyActionPerformed(ActionEvent evt)
  {
    if (_oControlPanelListener == null) {
      return;
    }
    int iCount = ((Integer)txtAction_AdvanceMany.getValue()).intValue();

    for (int i = 0; i < iCount; i++)
      _oControlPanelListener.onAdvanceGeneration();
  }

  private void txtGrowth_GrowRate_StateChanged(ChangeEvent evt)
  {
    if (((Integer)txtGrowth_GrowRate.getValue()).intValue() < 0)
      txtGrowth_GrowRate.setValue(Integer.valueOf(0));
    if (((Integer)txtGrowth_GrowRate.getValue()).intValue() > 1000) {
      txtGrowth_GrowRate.setValue(Integer.valueOf(1000));
    }
    if (_oControlPanelListener == null)
      return;
    _oGalaxyProperties.growthRate = ((Integer)txtGrowth_GrowRate.getValue()).intValue();
  }

  private void txtRotation_MovementCountStateChanged(ChangeEvent evt)
  {
    if (_oControlPanelListener == null)
      return;
    _oGalaxyProperties.rotationCount = ((Integer)txtRotation_MovementCount.getValue()).intValue();
  }

  private void txtRotation_MovementLagStateChanged(ChangeEvent evt)
  {
    if (_oControlPanelListener == null)
      return;
    _oGalaxyProperties.rotationLag = ((Integer)txtRotation_MovementLag.getValue()).intValue();
  }

  private void btnAction_AdvanceAllStart_ActionPerformed(ActionEvent evt)
  {
    advanceAllStart();
  }

  private void btnAction_AdvanceAllStop_ActionPerformed(ActionEvent evt)
  {
    advanceAllStop();
  }

  private void txtAction_Interval_StateChanged(ChangeEvent evt)
  {
    if (((Integer)txtAction_Interval.getValue()).intValue() < 1)
      txtAction_Interval.setValue(Integer.valueOf(1));
    if (((Integer)txtAction_Interval.getValue()).intValue() > 1000)
      txtAction_Interval.setValue(Integer.valueOf(1000));
    _oTimer.setInterval(((Integer)txtAction_Interval.getValue()).intValue());
  }

  private void txtAction_AdvanceMany_StateChanged(ChangeEvent evt)
  {
    if (((Integer)txtAction_AdvanceMany.getValue()).intValue() < 0)
      txtAction_AdvanceMany.setValue(Integer.valueOf(0));
    if (((Integer)txtAction_AdvanceMany.getValue()).intValue() > 1000)
      txtAction_AdvanceMany.setValue(Integer.valueOf(1000));
  }

  private void txtZoomStateChanged(ChangeEvent evt)
  {
    if (((Integer)txtZoom.getValue()).intValue() < 50)
      txtZoom.setValue(Integer.valueOf(50));
    if (((Integer)txtZoom.getValue()).intValue() > 200) {
      txtZoom.setValue(Integer.valueOf(200));
    }
    _oGalaxyPanel.setZoom(((Integer)txtZoom.getValue()).intValue() / 100.0D);
    _oGalaxyPanel.updateUI();
  }

  private void optGuiStarSizeOneActionPerformed(ActionEvent evt)
  {
    _oGalaxyPanel.setStarsConstantWidth(optGuiStarSizeOne.isSelected());
    _oGalaxyPanel.updateUI();
  }

  public void setControlPanelListener(ControlPanelListener oControlPanelListener)
  {
    _oControlPanelListener = oControlPanelListener;
  }

  public void onTick()
  {
    btnAction_AdvanceOneActionPerformed(null);
  }

  public void advanceAllStart()
  {
    new Thread(_oTimer).start();
  }

  public void advanceAllStop() {
    _oTimer.stopTimer();
  }
}