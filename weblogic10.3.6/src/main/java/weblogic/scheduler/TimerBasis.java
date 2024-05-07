package weblogic.scheduler;

import java.util.List;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.timers.TimerListener;

public interface TimerBasis {
   String createTimer(String var1, TimerListener var2, long var3, long var5, AuthenticatedSubject var7) throws TimerException;

   boolean cancelTimer(String var1) throws NoSuchObjectLocalException, TimerException;

   void advanceIntervalTimer(String var1, TimerListener var2) throws NoSuchObjectLocalException, TimerException;

   TimerState getTimerState(String var1) throws NoSuchObjectLocalException, TimerException;

   List getReadyTimers(int var1) throws TimerException;

   Timer[] getTimers(String var1) throws TimerException;

   Timer[] getTimers(String var1, String var2) throws TimerException;

   void cancelTimers(String var1) throws TimerException;
}
