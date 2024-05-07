package weblogic.servlet.internal;

import java.util.HashMap;
import java.util.Iterator;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.management.ManagementException;
import weblogic.management.runtime.PageFlowRuntimeMBean;
import weblogic.management.runtime.PageFlowsRuntimeMBean;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.management.runtime.RuntimeMBeanDelegate;

public class PageFlowsRuntimeMBeanImpl extends RuntimeMBeanDelegate implements PageFlowsRuntimeMBean {
   private static final long serialVersionUID = 1L;
   private HashMap _pageFlows;
   private String _serverName;
   private String _httpServerName;
   private String _contextPath;
   private String _applicationName;
   static DebugLogger logger = DebugLogger.getDebugLogger("DebugPageFlowMonitoring");

   public PageFlowsRuntimeMBeanImpl(String var1, String var2, String var3, String var4, RuntimeMBean var5) throws ManagementException {
      super(var1 + "_" + var2 + "_" + var3, var5);
      this._serverName = var1;
      this._httpServerName = var2;
      this._contextPath = var3;
      this._applicationName = var4;
      this._pageFlows = new HashMap();
   }

   public void reset() {
      synchronized(this._pageFlows) {
         Iterator var2 = this._pageFlows.values().iterator();

         while(var2.hasNext()) {
            PageFlowRuntimeMBean var3 = (PageFlowRuntimeMBean)var2.next();
            var3.reset();
         }

      }
   }

   public PageFlowRuntimeMBean[] getPageFlows() {
      synchronized(this._pageFlows) {
         return (PageFlowRuntimeMBean[])((PageFlowRuntimeMBean[])this._pageFlows.values().toArray(new PageFlowRuntimeMBean[this._pageFlows.size()]));
      }
   }

   public PageFlowRuntimeMBean getPageFlow(String var1) {
      synchronized(this._pageFlows) {
         return (PageFlowRuntimeMBean)this._pageFlows.get(var1);
      }
   }

   public void addPageFlow(String var1, String[] var2) {
      synchronized(this._pageFlows) {
         try {
            PageFlowRuntimeMBeanImpl var4 = (PageFlowRuntimeMBeanImpl)this._pageFlows.get(var1);
            if (var4 == null) {
               var4 = new PageFlowRuntimeMBeanImpl(this._serverName, this._httpServerName, var1, this);
               var4.setActions(var2);
               this._pageFlows.put(var1, var4);
            }
         } catch (ManagementException var6) {
            logger.debug("Unable to add PageFlow: " + var1, var6);
         }

      }
   }

   public String getServerName() {
      return this._serverName;
   }

   public String getHttpServerName() {
      return this._httpServerName;
   }

   public String getContextPath() {
      return this._contextPath;
   }

   public String getAppName() {
      return this._applicationName;
   }
}
