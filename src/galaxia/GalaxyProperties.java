package galaxia;

public class GalaxyProperties
{
  public int initialStarCount;
  public StarsDistribution starsDistribution;
  public boolean rotate;
  public double rotationCount;
  public double rotationLag;
  public boolean grow;
  public double growthRate;
  public boolean growthLimitation;
  public double reciveOdds;

  public static enum StarsDistribution
  {
    Uniform, 
    Angular, 
    Zone;
  }
}