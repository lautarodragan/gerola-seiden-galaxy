package helper;

import java.io.PrintStream;
import java.util.Vector;

public class Timer extends Thread
  implements Runnable
{
  private Vector<TimerInterface> _oTimerInterface;
  private boolean _bStop;
  private long _iInterval;

  public Timer()
  {
    _oTimerInterface = new Vector();
    _iInterval = 500L;
  }

  public void run()
  {
    _bStop = false;
    do {
      try {
        sleep(_iInterval); } catch (Exception ex) {
      }
      for (TimerInterface oTimerInterface : _oTimerInterface) {
        oTimerInterface.onTick();
      }

      yield();
    }
    while (!_bStop);
  }

  public void addListener(TimerInterface oTimerInterface)
  {
    _oTimerInterface.add(oTimerInterface);
  }

  public void removeListener(TimerInterface oTimerInterface) {
    _oTimerInterface.remove(oTimerInterface);
  }

  public void setInterval(long iInterval)
  {
    if (iInterval < 1L) {
      System.err.println("Timer.setInterval(): iInterval cannot be less than 1");
    }
    _iInterval = iInterval;
  }

  public long getInterval() {
    return _iInterval;
  }

  public void stopTimer()
  {
    _bStop = true;
  }
}