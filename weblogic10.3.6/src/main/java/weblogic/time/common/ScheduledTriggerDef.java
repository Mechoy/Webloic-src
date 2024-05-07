package weblogic.time.common;

import weblogic.work.WorkManager;

/** @deprecated */
public interface ScheduledTriggerDef {
   /** @deprecated */
   int schedule() throws TimeTriggerException;

   /** @deprecated */
   boolean cancel() throws TimeTriggerException;

   /** @deprecated */
   void setDaemon(boolean var1) throws TimeTriggerException;

   /** @deprecated */
   void setWorkManager(WorkManager var1);

   /** @deprecated */
   boolean isDaemon() throws TimeTriggerException;
}
