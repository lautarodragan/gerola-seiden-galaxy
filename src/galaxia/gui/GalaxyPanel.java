package galaxia.gui;

import galaxia.Galaxy;
import galaxia.Star;
import java.awt.Color;
import java.awt.Graphics;
import java.util.Vector;
import javax.swing.JPanel;
import javax.swing.border.SoftBevelBorder;

public class GalaxyPanel extends JPanel
{
  private Galaxy _oGalaxy;
  private double _iZoom = 1.0D;
  private final int BASE_RED = 0;
  private final int BASE_GREEN = 20;
  private final int BASE_BLUE = 50;
  private boolean _bStarsConstantWidth = false;

  public GalaxyPanel() {
    setBorder(new SoftBevelBorder(1));
  }

  public void paintComponent(Graphics g)
  {
    super.paintComponent(g);
    _oGalaxy.getClass(); _oGalaxy.getClass(); int[][] iTempMatrix = new int['È']['È'];
    int iSize;
    int iSize;
    if (_bStarsConstantWidth)
      iSize = 1;
    else {
      iSize = Math.max((int)Math.round(_iZoom), 1);
    }

    g.setColor(Color.BLACK);
    g.fillRect(0, 0, getWidth(), getHeight());

    if (_oGalaxy != null)
      for (int i = 0; i < _oGalaxy.getVector().size(); i++) {
        Star oStar = (Star)_oGalaxy.getVector().get(i);
        _oGalaxy.getClass(); if (oStar.x + 'È' / 2 >= 0.0D) { _oGalaxy.getClass(); _oGalaxy.getClass(); if (oStar.x + 'È' / 2 < 200.0D) { _oGalaxy.getClass(); if (oStar.y + 'È' / 2 >= 0.0D) { _oGalaxy.getClass(); _oGalaxy.getClass(); if (oStar.y + 'È' / 2 < 200.0D) {
                _oGalaxy.getClass(); _oGalaxy.getClass(); iTempMatrix[((int)oStar.x + 'È' / 2)][((int)oStar.y + 'È' / 2)] += 1;
                _oGalaxy.getClass(); _oGalaxy.getClass(); int iTempValue = iTempMatrix[((int)oStar.x + 'È' / 2)][((int)oStar.y + 'È' / 2)]; break label338;
              } } }
        }
        int iTempValue = 1;

        label338: if (oStar.life <= 0.0D)
          g.setColor(Color.BLACK);
        else {
          g.setColor(new Color((int)Math.min(0.0D + oStar.life * 10.0D * iTempValue, 255.0D), (int)Math.min(20.0D + oStar.life * 10.0D * iTempValue, 255.0D), (int)Math.min(50.0D + oStar.life * 10.0D * iTempValue, 255.0D)));
        }

        int iFinalX = (int)(oStar.x * _iZoom + Math.min(getWidth(), getHeight()) / 2);
        int iFinalY = (int)(oStar.y * _iZoom + Math.min(getWidth(), getHeight()) / 2);

        g.fillRect(iFinalX, iFinalY, iSize, iSize);
      }
  }

  public void setGalaxy(Galaxy oGalaxy)
  {
    _oGalaxy = oGalaxy;
  }

  public void setZoom(double iZoom)
  {
    _iZoom = iZoom;
  }
  public double getZoom() {
    return _iZoom;
  }

  public void setStarsConstantWidth(boolean bStarsConstantWidth) {
    _bStarsConstantWidth = bStarsConstantWidth;
  }
  public boolean getStarsConstantWidth() {
    return _bStarsConstantWidth;
  }
}