package weblogic.servlet.internal;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import weblogic.descriptor.utils.DescriptorUtils;
import weblogic.j2ee.ComponentRuntimeMBeanImpl;
import weblogic.j2ee.descriptor.wl.LoggingBean;
import weblogic.j2ee.descriptor.wl.WeblogicWebAppBean;
import weblogic.kodo.monitoring.KodoPersistenceUnitParent;
import weblogic.logging.j2ee.LoggingBeanAdapter;
import weblogic.management.DeploymentException;
import weblogic.management.ManagementException;
import weblogic.management.logging.LogRuntime;
import weblogic.management.runtime.CoherenceClusterRuntimeMBean;
import weblogic.management.runtime.KodoPersistenceUnitRuntimeMBean;
import weblogic.management.runtime.LibraryRuntimeMBean;
import weblogic.management.runtime.LogRuntimeMBean;
import weblogic.management.runtime.PageFlowsRuntimeMBean;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.management.runtime.ServletRuntimeMBean;
import weblogic.management.runtime.ServletSessionRuntimeMBean;
import weblogic.management.runtime.SpringRuntimeMBean;
import weblogic.management.runtime.WebAppComponentRuntimeMBean;
import weblogic.management.runtime.WebPubSubRuntimeMBean;
import weblogic.management.runtime.WseeClientConfigurationRuntimeMBean;
import weblogic.management.runtime.WseeClientRuntimeMBean;
import weblogic.management.runtime.WseeV2RuntimeMBean;
import weblogic.servlet.ReferencedAttribute;
import weblogic.servlet.internal.session.ServletSessionRuntimeMBeanImpl;
import weblogic.servlet.internal.session.SessionContext;

public class WebAppRuntimeMBeanImpl extends ComponentRuntimeMBeanImpl implements WebAppComponentRuntimeMBean, ReferencedAttribute, KodoPersistenceUnitParent {
   private static final long serialVersionUID = -1274209977754857686L;
   private WebAppServletContext context;
   private LogRuntime logRuntime;
   private PageFlowsRuntimeMBean pageFlowsRuntimeMBean;
   private LibraryRuntimeMBean[] libraryRuntimes;
   private Map m_runtimePersistenUnit = new HashMap();
   private WebPubSubRuntimeMBean webPubSubRuntimeMBean;
   private String applicationIdentifier;
   private CoherenceClusterRuntimeMBean coherenceClusterRuntimeMBean;
   private int state = 0;
   private SpringRuntimeMBean springRuntimeMBean;
   private final HashSet<WseeClientRuntimeMBean> wseeClientRuntimes = new HashSet();
   private final HashSet<WseeV2RuntimeMBean> wseeV2Runtimes = new HashSet();
   private final Set<WseeClientConfigurationRuntimeMBean> wseeClientConfigurationRuntimes = new HashSet();

   public WebAppRuntimeMBeanImpl(String var1, String var2, WebAppServletContext var3, RuntimeMBean var4, String var5) throws ManagementException {
      super(var1, var2, var4, true, var3.getMBean());
      this.context = var3;
      this.applicationIdentifier = var5;
      if (this.context != null && this.context.getServletContextLogger() != null) {
         LoggingBeanAdapter var6 = this.context.getServletContextLogger().getLogAdapter();
         if (var6 != null) {
            this.logRuntime = new LogRuntime(var6, this);
         }
      }

      this.pageFlowsRuntimeMBean = new PageFlowsRuntimeMBeanImpl(var3.getServer().getServerName(), var3.getServer().getName(), var3.getContextPath(), var3.getName(), this);
   }

   public String getApplicationIdentifier() {
      return this.applicationIdentifier;
   }

   public String getComponentName() {
      return this.context.getName();
   }

   public String getContextRoot() {
      return this.context.getContextPath();
   }

   public String getModuleURI() {
      WebAppModule var1 = this.context.getWebAppModule();
      return var1 == null ? null : var1.getModuleURI();
   }

   public String getStatus() {
      return this.context.isStarted() ? "DEPLOYED" : "UNDEPLOYED";
   }

   public String getSourceInfo() {
      String var1 = this.context.getURI();
      return var1 == null ? "" : var1;
   }

   public ServletRuntimeMBean[] getServlets() {
      return this.context.getServletRuntimeMBeans();
   }

   public int getOpenSessionsCurrentCount() {
      SessionContext var1 = this.context.getSessionContext();
      return var1 == null ? 0 : var1.getCurrOpenSessionsCount();
   }

   public int getOpenSessionsHighCount() {
      SessionContext var1 = this.context.getSessionContext();
      return var1 == null ? 0 : var1.getMaxOpenSessionsCount();
   }

   public int getSessionsOpenedTotalCount() {
      SessionContext var1 = this.context.getSessionContext();
      return var1 == null ? 0 : var1.getTotalOpenSessionsCount();
   }

   public Set getAllServletSessions() {
      return this.context.getSessionContext().getAllServletSessions();
   }

   public ServletSessionRuntimeMBean[] getServletSessions() {
      return this.context.getSessionContext().getServletSessionRuntimeMBeans();
   }

   public String[] getServletSessionsMonitoringIds() {
      return this.context.getSessionContext().getServletSessionsMonitoringIds();
   }

   public void invalidateServletSession(String var1) throws IllegalStateException {
      this.context.getSessionContext().invalidateServletSession(var1);
   }

   public long getSessionLastAccessedTime(String var1) throws IllegalStateException {
      return this.context.getSessionContext().getSessionLastAccessedTime(var1);
   }

   public long getSessionMaxInactiveInterval(String var1) throws IllegalStateException {
      return this.context.getSessionContext().getSessionMaxInactiveInterval(var1);
   }

   public String getMonitoringId(String var1) throws IllegalStateException {
      return this.context.getSessionContext().getMonitoringId(var1);
   }

   public ServletSessionRuntimeMBean getServletSession(String var1) {
      return this.context.getSessionContext().getServletSessionRuntimeMBean(var1);
   }

   public int getSessionTimeoutSecs() {
      return this.context.getSessionContext().getConfigMgr().getSessionTimeoutSecs();
   }

   public int getSessionInvalidationIntervalSecs() {
      return this.context.getSessionContext().getConfigMgr().getInvalidationIntervalSecs();
   }

   public int getSessionIDLength() {
      return this.context.getSessionContext().getConfigMgr().getIDLength();
   }

   public int getSessionCookieMaxAgeSecs() {
      return this.context.getSessionContext().getConfigMgr().getCookieMaxAgeSecs();
   }

   public String getSessionCookieComment() {
      return this.context.getSessionContext().getConfigMgr().getCookieComment();
   }

   public String getSessionCookieName() {
      return this.context.getSessionContext().getConfigMgr().getCookieName();
   }

   public String getSessionCookieDomain() {
      return this.context.getSessionContext().getConfigMgr().getCookieDomain();
   }

   public String getSessionCookiePath() {
      return this.context.getSessionContext().getConfigMgr().getCookiePath();
   }

   public boolean isIndexDirectoryEnabled() {
      return this.context.getConfigManager().isIndexDirectoryEnabled();
   }

   public boolean isFilterDispatchedRequestsEnabled() {
      return this.context.getConfigManager().isFilterDispatchedRequestsEnabled();
   }

   public int getServletReloadCheckSecs() {
      return this.context.getConfigManager().getServletReloadCheckSecs();
   }

   public int getSingleThreadedServletPoolSize() {
      return this.context.getConfigManager().getSingleThreadedServletPoolSize();
   }

   public boolean isSessionMonitoringEnabled() {
      return this.context.getSessionContext().getConfigMgr().isMonitoringEnabled();
   }

   public boolean isJSPKeepGenerated() {
      return "true".equalsIgnoreCase(this.get(this.context.getJSPManager().getJspConfigArgs(), "keepgenerated"));
   }

   public boolean isJSPVerbose() {
      return "true".equalsIgnoreCase(this.get(this.context.getJSPManager().getJspConfigArgs(), "verbose"));
   }

   public boolean isJSPDebug() {
      return "true".equalsIgnoreCase(this.get(this.context.getJSPManager().getJspConfigArgs(), "debug"));
   }

   public long getJSPPageCheckSecs() {
      String var1 = this.get(this.context.getJSPManager().getJspConfigArgs(), "pageCheckSeconds");
      if (var1 != null) {
         try {
            return Long.parseLong(var1);
         } catch (NumberFormatException var3) {
         }
      }

      return 1L;
   }

   public String getJSPCompileCommand() {
      return this.get(this.context.getJSPManager().getJspConfigArgs(), "compileCommand");
   }

   public String getLogFilename() {
      WebAppModule var1 = this.context.getWebAppModule();
      if (var1 == null) {
         return null;
      } else {
         WeblogicWebAppBean var2 = var1.getWlWebAppBean();
         if (var2 == null) {
            return null;
         } else {
            LoggingBean var3 = (LoggingBean)DescriptorUtils.getFirstChildOrDefaultBean(var2, var2.getLoggings(), "Logging");
            return var3 == null ? null : var3.getLogFilename();
         }
      }
   }

   private String get(Map var1, String var2) {
      return this.trim((String)var1.get(var2));
   }

   private String trim(String var1) {
      if (var1 == null) {
         return null;
      } else {
         var1 = var1.trim();
         return var1.length() == 0 ? null : var1;
      }
   }

   public void deleteInvalidSessions() {
      SessionContext var1 = this.context.getSessionContext();
      var1.deleteInvalidSessions();
   }

   public int getDeploymentState() {
      return this.state;
   }

   public void setDeploymentState(int var1) {
      this.state = var1;
   }

   public static void dumpRuntime(PrintStream var0, WebAppRuntimeMBeanImpl var1) {
      println(var0, "========== DUMP ==============");
      println(var0, "STATUS: " + var1.getStatus());
      println(var0, "SOURCE INFO: " + var1.getSourceInfo());
      ServletRuntimeMBean[] var2 = var1.getServlets();
      if (var2 == null) {
         println(var0, "SERVLETS IS NULL");
      } else if (var2.length == 0) {
         println(var0, "SERVLETS IS EMPTY");
      } else {
         println(var0, "SERVLETS:");

         for(int var3 = 0; var3 < var2.length; ++var3) {
            ServletRuntimeMBeanImpl.dumpServlet(var0, var2[var3]);
         }
      }

      println(var0, "OPEN SESSIONS: " + var1.getOpenSessionsCurrentCount());
      println(var0, "OpenSessionsHighCount: " + var1.getOpenSessionsHighCount());
      println(var0, "SessionsOpenedTotalCount: " + var1.getSessionsOpenedTotalCount());
      ServletSessionRuntimeMBean[] var5 = var1.getServletSessions();
      if (var5 == null) {
         println(var0, "SESSIONS IS NULL");
      } else if (var5.length == 0) {
         println(var0, "SESSIONS IS EMPTY");
      } else {
         println(var0, "SESSIONS:");

         for(int var4 = 0; var4 < var5.length; ++var4) {
            ServletSessionRuntimeMBeanImpl.dumpSession(var0, var5[var4]);
         }
      }

   }

   private static void println(PrintStream var0, String var1) {
      var0.println(var1 + "<br>");
   }

   public void registerServlet(String var1, String var2, String[] var3, Map var4, int var5) throws DeploymentException {
      this.context.registerServlet(var1, var2, var3, var4, var5);
   }

   public void registerFilter(String var1, String var2, String[] var3, String[] var4, Map var5, String[] var6) throws DeploymentException {
      this.context.registerFilter(var1, var2, var3, var4, var5, var6);
   }

   public LogRuntimeMBean getLogRuntime() {
      return this.logRuntime;
   }

   public PageFlowsRuntimeMBean getPageFlows() {
      return this.pageFlowsRuntimeMBean;
   }

   public LibraryRuntimeMBean[] getLibraryRuntimes() {
      return this.libraryRuntimes;
   }

   public void setLibraryRuntimes(LibraryRuntimeMBean[] var1) {
      this.libraryRuntimes = var1;
   }

   public void setSpringRuntimeMBean(SpringRuntimeMBean var1) {
      this.springRuntimeMBean = var1;
   }

   public SpringRuntimeMBean getSpringRuntimeMBean() {
      return this.springRuntimeMBean;
   }

   public void addKodoPersistenceUnit(KodoPersistenceUnitRuntimeMBean var1) {
      this.m_runtimePersistenUnit.put(var1.getPersistenceUnitName(), var1);
   }

   public KodoPersistenceUnitRuntimeMBean[] getKodoPersistenceUnitRuntimes() {
      KodoPersistenceUnitRuntimeMBean[] var1 = new KodoPersistenceUnitRuntimeMBean[this.m_runtimePersistenUnit.size()];
      var1 = (KodoPersistenceUnitRuntimeMBean[])((KodoPersistenceUnitRuntimeMBean[])this.m_runtimePersistenUnit.values().toArray(var1));
      return var1;
   }

   public KodoPersistenceUnitRuntimeMBean getKodoPersistenceUnitRuntime(String var1) {
      return (KodoPersistenceUnitRuntimeMBean)this.m_runtimePersistenUnit.get(var1);
   }

   public WebPubSubRuntimeMBean getWebPubSubRuntime() {
      return this.webPubSubRuntimeMBean;
   }

   public void setWebPubSubRuntime(WebPubSubRuntimeMBean var1) {
      this.webPubSubRuntimeMBean = var1;
   }

   public CoherenceClusterRuntimeMBean getCoherenceClusterRuntime() {
      return this.coherenceClusterRuntimeMBean;
   }

   public void setCoherenceClusterRuntime(CoherenceClusterRuntimeMBean var1) {
      this.coherenceClusterRuntimeMBean = var1;
   }

   public WseeClientRuntimeMBean[] getWseeClientRuntimes() {
      synchronized(this.wseeClientRuntimes) {
         int var2 = this.wseeClientRuntimes.size();
         return (WseeClientRuntimeMBean[])this.wseeClientRuntimes.toArray(new WseeClientRuntimeMBean[var2]);
      }
   }

   public WseeClientRuntimeMBean lookupWseeClientRuntime(String var1) {
      WseeClientRuntimeMBean var2 = null;
      synchronized(this.wseeClientRuntimes) {
         Iterator var4 = this.wseeClientRuntimes.iterator();

         while(var4.hasNext()) {
            WseeClientRuntimeMBean var5 = (WseeClientRuntimeMBean)var4.next();
            if (var5.getName().equals(var1)) {
               var2 = var5;
               break;
            }
         }

         return var2;
      }
   }

   public void addWseeClientRuntime(WseeClientRuntimeMBean var1) {
      synchronized(this.wseeClientRuntimes) {
         this.wseeClientRuntimes.add(var1);
      }
   }

   public void removeWseeClientRuntime(WseeClientRuntimeMBean var1) {
      synchronized(this.wseeClientRuntimes) {
         this.wseeClientRuntimes.remove(var1);
      }
   }

   public WseeV2RuntimeMBean[] getWseeV2Runtimes() {
      synchronized(this.wseeV2Runtimes) {
         int var2 = this.wseeV2Runtimes.size();
         return (WseeV2RuntimeMBean[])this.wseeV2Runtimes.toArray(new WseeV2RuntimeMBean[var2]);
      }
   }

   public WseeV2RuntimeMBean lookupWseeV2Runtime(String var1) {
      WseeV2RuntimeMBean var2 = null;
      synchronized(this.wseeV2Runtimes) {
         Iterator var4 = this.wseeV2Runtimes.iterator();

         while(var4.hasNext()) {
            WseeV2RuntimeMBean var5 = (WseeV2RuntimeMBean)var4.next();
            if (var5.getName().equals(var1)) {
               var2 = var5;
               break;
            }
         }

         return var2;
      }
   }

   public void addWseeV2Runtime(WseeV2RuntimeMBean var1) {
      synchronized(this.wseeV2Runtimes) {
         this.wseeV2Runtimes.add(var1);
      }
   }

   public void removeWseeV2Runtime(WseeV2RuntimeMBean var1) {
      synchronized(this.wseeV2Runtimes) {
         this.wseeV2Runtimes.remove(var1);
      }
   }

   public WseeClientConfigurationRuntimeMBean[] getWseeClientConfigurationRuntimes() {
      synchronized(this.wseeClientConfigurationRuntimes) {
         return (WseeClientConfigurationRuntimeMBean[])this.wseeClientConfigurationRuntimes.toArray(new WseeClientConfigurationRuntimeMBean[this.wseeClientConfigurationRuntimes.size()]);
      }
   }

   public WseeClientConfigurationRuntimeMBean lookupWseeClientConfigurationRuntime(String var1) {
      WseeClientConfigurationRuntimeMBean var2 = null;
      synchronized(this.wseeClientConfigurationRuntimes) {
         Iterator var4 = this.wseeClientConfigurationRuntimes.iterator();

         while(var4.hasNext()) {
            WseeClientConfigurationRuntimeMBean var5 = (WseeClientConfigurationRuntimeMBean)var4.next();
            if (var5.getName().equals(var1)) {
               var2 = var5;
               break;
            }
         }

         return var2;
      }
   }

   public void addWseeClientConfigurationRuntime(WseeClientConfigurationRuntimeMBean var1) {
      synchronized(this.wseeClientConfigurationRuntimes) {
         this.wseeClientConfigurationRuntimes.add(var1);
      }
   }

   public void removeWseeClientConfigurationRuntime(WseeClientConfigurationRuntimeMBean var1) {
      synchronized(this.wseeClientConfigurationRuntimes) {
         this.wseeClientConfigurationRuntimes.remove(var1);
      }
   }
}
