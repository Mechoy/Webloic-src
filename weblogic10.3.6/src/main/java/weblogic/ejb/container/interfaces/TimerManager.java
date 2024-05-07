package weblogic.ejb.container.interfaces;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.ejb.Timer;
import weblogic.ejb.WLTimerInfo;
import weblogic.ejb.spi.WLDeploymentException;
import weblogic.management.runtime.EJBTimerRuntimeMBean;

public interface TimerManager {
   void setup(EJBTimerRuntimeMBean var1) throws WLDeploymentException;

   void perhapsStart();

   void start();

   void undeploy();

   void enableDisabledTimers();

   Timer createTimer(Object var1, Date var2, long var3, Serializable var5, WLTimerInfo var6);

   Timer createTimer(Object var1, Date var2, Serializable var3, WLTimerInfo var4);

   Timer createTimer(Object var1, long var2, long var4, Serializable var6, WLTimerInfo var7);

   Timer createTimer(Object var1, long var2, Serializable var4, WLTimerInfo var5);

   Collection getTimers(Object var1);

   void removeTimersForPK(Object var1);
}
