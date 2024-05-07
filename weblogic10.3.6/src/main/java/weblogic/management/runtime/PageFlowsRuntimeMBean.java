package weblogic.management.runtime;

public interface PageFlowsRuntimeMBean extends RuntimeMBean {
   /** @deprecated */
   String getServerName();

   /** @deprecated */
   String getHttpServerName();

   /** @deprecated */
   String getContextPath();

   /** @deprecated */
   String getAppName();

   /** @deprecated */
   PageFlowRuntimeMBean[] getPageFlows();

   /** @deprecated */
   PageFlowRuntimeMBean getPageFlow(String var1);

   /** @deprecated */
   void reset();
}
