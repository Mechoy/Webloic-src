package weblogic.management.runtime;

public interface PageFlowRuntimeMBean extends RuntimeMBean {
   /** @deprecated */
   String getClassName();

   /** @deprecated */
   long getCreateCount();

   /** @deprecated */
   long getDestroyCount();

   /** @deprecated */
   PageFlowActionRuntimeMBean[] getActions();

   /** @deprecated */
   PageFlowActionRuntimeMBean getAction(String var1);

   /** @deprecated */
   void reset();

   /** @deprecated */
   long getLastResetTime();

   /** @deprecated */
   void setNumExceptionsToKeep(int var1);
}
