package weblogic.time.common;

import weblogic.common.T3Exception;

/** @deprecated */
public interface TimeServicesDef {
   /** @deprecated */
   ScheduledTriggerDef getScheduledTrigger(Schedulable var1, Triggerable var2) throws TimeTriggerException;

   /** @deprecated */
   ScheduledTriggerDef getScheduledTrigger(Scheduler var1, Trigger var2) throws TimeTriggerException;

   /** @deprecated */
   long currentTimeMillis() throws T3Exception;

   /** @deprecated */
   int getRoundTripDelayMillis() throws T3Exception;

   /** @deprecated */
   int getLocalClockOffsetMillis() throws T3Exception;
}
