package galaxia;

import java.io.PrintStream;
import java.util.Vector;

public class Galaxy
{
  public double[][] _iMatrix;
  private GalaxyProperties _oGalaxyProperties;
  public final int _iMatrixSize = 200;
  private Vector<Star> _oVector;
  private int _iGeneration;

  public Galaxy()
  {
    _oVector = new Vector();
  }

  public Vector<Star> getVector()
  {
    return _oVector;
  }

  public void setGeneration(int iGeneration)
  {
    _iGeneration = iGeneration;
  }

  public int getGeneration() {
    return _iGeneration;
  }

  public void InitializeMatrix()
  {
    if (_oGalaxyProperties == null) {
      return;
    }
    _oVector = new Vector();

    System.out.println("Initializing matrix...");
    System.out.println("Star count: " + _oGalaxyProperties.initialStarCount);
    System.out.println("Distribution: " + _oGalaxyProperties.starsDistribution);

    if (_oGalaxyProperties.starsDistribution == GalaxyProperties.StarsDistribution.Uniform)
    {
      for (int i = 0; i < _oGalaxyProperties.initialStarCount; i++) {
        double iAngle = Math.random() * 3.141592653589793D * 2.0D;
        double iRadius = 3.0D + Math.random() * 85.0D;

        double x = Math.cos(iAngle) * iRadius;
        double y = Math.sin(iAngle) * iRadius;
        int iLife = (int)Math.floor(Math.random() * 8.0D) + 2;

        _oVector.add(new Star(x, y, iLife));
      }

    }
    else if (_oGalaxyProperties.starsDistribution == GalaxyProperties.StarsDistribution.Angular)
    {
      for (int i = 0; i < _oGalaxyProperties.initialStarCount * 0.4D; i++) {
        double iAngle = Math.random() * 3.141592653589793D * 2.0D;
        double iRadius = 3.0D + Math.random() * 85.0D;

        double x = Math.cos(iAngle) * iRadius;
        double y = Math.sin(iAngle) * iRadius;
        int iLife = (int)Math.floor(Math.random() * 8.0D) + 2;

        _oVector.add(new Star(x, y, iLife));
      }

      for (int i = 0; i < _oGalaxyProperties.initialStarCount * 0.3D; i++) {
        double iAngle = Math.random() * 3.141592653589793D / 6.0D;
        double iRadius = 3.0D + Math.random() * 85.0D;

        double x = Math.cos(iAngle) * iRadius;
        double y = Math.sin(iAngle) * iRadius;
        int iLife = (int)Math.floor(Math.random() * 8.0D) + 2;

        _oVector.add(new Star(x, y, iLife));
      }

      for (int i = 0; i < _oGalaxyProperties.initialStarCount * 0.3D; i++) {
        double iAngle = 3.141592653589793D + Math.random() * 3.141592653589793D / 6.0D;
        double iRadius = 3.0D + Math.random() * 85.0D;

        double x = Math.cos(iAngle) * iRadius;
        double y = Math.sin(iAngle) * iRadius;
        int iLife = (int)Math.floor(Math.random() * 8.0D) + 2;

        _oVector.add(new Star(x, y, iLife));
      }

    }
    else if (_oGalaxyProperties.starsDistribution == GalaxyProperties.StarsDistribution.Zone)
    {
      for (int i = 0; i < _oGalaxyProperties.initialStarCount * 0.4D; i++) {
        double iAngle = Math.random() * 3.141592653589793D * 2.0D;
        double iRadius = 3.0D + Math.random() * 85.0D;

        double x = Math.cos(iAngle) * iRadius;
        double y = Math.sin(iAngle) * iRadius;
        int iLife = (int)Math.floor(Math.random() * 8.0D) + 2;

        _oVector.add(new Star(x, y, iLife));
      }

      int iGroupCount = (int)Math.round(10.0D + Math.random() * 5.0D);

      for (int i = 0; i < iGroupCount; i++) {
        int iGroupSize = 20;

        int iStarCount = (int)Math.round(_oGalaxyProperties.initialStarCount * 0.6D / iGroupCount);

        double iGroupX = 0.0D; double iGroupY = 0.0D;
        do {
          getClass(); getClass(); iGroupX = Math.random() * 200.0D - 'È' / 2;
          getClass(); getClass(); iGroupY = Math.random() * 200.0D - 'È' / 2;

          getClass(); } while (Math.sqrt(Math.pow(iGroupX, 2.0D) + Math.pow(iGroupY, 2.0D)) > 'È' / 2 - iGroupSize - 10);

        for (int i2 = 0; i2 < iStarCount; i2++) {
          double iAngle = Math.random() * 3.141592653589793D * 2.0D;
          double iRadius = Math.random() * iGroupSize;
          int iLife = (int)Math.floor(Math.random() * 8.0D) + 2;

          double x = Math.cos(iAngle) * iRadius;
          double y = Math.sin(iAngle) * iRadius;

          _oVector.add(new Star(x + iGroupX, y + iGroupY, iLife));
        }
      }
    }
  }

  public void Rotate()
  {
    double iX = 0.0D; double iY = 0.0D;

    for (int i = 0; i < _oVector.size(); i++) {
      iX = ((Star)_oVector.get(i)).x;
      iY = ((Star)_oVector.get(i)).y;

      double iSize = Math.sqrt(Math.pow(iX, 2.0D) + Math.pow(iY, 2.0D));
      double iStep = 6.283185307179586D * _oGalaxyProperties.rotationCount / 100.0D + _oGalaxyProperties.rotationLag / 10.0D / iSize;
      double iAngle = Math.atan2(iY, iX) + iStep;
      iX = Math.cos(iAngle) * iSize;
      iY = Math.sin(iAngle) * iSize;

      ((Star)_oVector.get(i)).x = iX;
      ((Star)_oVector.get(i)).y = iY;
    }
  }

  public void Grow()
  {
    for (int i = 0; i < _oVector.size(); i++) {
      ((Star)_oVector.get(i)).life -= 0.5D;
      if (((Star)_oVector.get(i)).life <= 0.0D) {
        ExplodeStar((Star)_oVector.get(i));
        _oVector.remove(i);
      }
    }
  }

  private void ExplodeStar(Star oStar)
  {
    for (int i = 0; i < 8; i++)
      if (Math.random() < _oGalaxyProperties.growthRate / 1000.0D)
      {
        double iLife = 5.0D + Math.floor(Math.random() * 5.0D);

        double x = Math.random() * 2.0D + oStar.x - 1.0D;
        double y = Math.random() * 2.0D + oStar.y - 1.0D;

        _oVector.add(new Star(x, y, iLife));
      }
  }

  public static double Sign(double i)
  {
    if (i >= 0.0D) {
      return 1.0D;
    }
    return -1.0D;
  }

  public void setProperties(GalaxyProperties oGalaxyProperties)
  {
    _oGalaxyProperties = oGalaxyProperties;
  }

  public GalaxyProperties getProperties() {
    return _oGalaxyProperties;
  }

  public Vector<Star> MatrixToVector(double[][] iMatrix)
  {
    Vector oVector = new Vector();

    for (int x = 0; x < iMatrix.length; x++) {
      for (int y = 0; y < iMatrix[0].length; y++) {
        if (iMatrix[x][y] != 0.0D) {
          oVector.add(new Star(x, y, iMatrix[x][y]));
        }

      }

    }

    return oVector;
  }

  public double[][] VectorToMatrix(Vector<Star> oVector) {
    double[][] iMatrix = new double['È']['È'];

    for (int i = 0; i < oVector.size(); i++) {
      iMatrix[((int)Math.round(((Star)oVector.get(i)).x))][((int)Math.round(((Star)oVector.get(i)).y))] = ((Star)oVector.get(i)).life;
    }

    return iMatrix;
  }
}