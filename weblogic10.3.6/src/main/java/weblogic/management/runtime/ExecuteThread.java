package weblogic.management.runtime;

import java.io.Serializable;

public interface ExecuteThread extends Serializable {
   String getCurrentRequest();

   long getCurrentRequestStartTime();

   /** @deprecated */
   String getLastRequest();

   int getServicedRequestTotalCount();

   boolean isIdle();

   boolean isStuck();

   boolean isHogger();

   boolean isStandby();

   JTATransaction getTransaction();

   String getUser();

   String getName();

   String getWorkManagerName();

   String getApplicationName();

   String getModuleName();

   String getApplicationVersion();

   Thread getExecuteThread();
}
