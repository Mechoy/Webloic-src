package weblogic.servlet.internal;

import weblogic.management.ManagementException;
import weblogic.management.logging.LogRuntime;
import weblogic.management.runtime.LogRuntimeMBean;
import weblogic.management.runtime.RuntimeMBeanDelegate;
import weblogic.management.runtime.WebServerRuntimeMBean;

public final class WebServerRuntimeMBeanImpl extends RuntimeMBeanDelegate implements WebServerRuntimeMBean {
   private static final long serialVersionUID = -2849245611198830040L;
   private final HttpServer httpServer;
   private final boolean defaultServer;
   private final LogRuntimeMBean logRuntime;

   public WebServerRuntimeMBeanImpl(String var1, HttpServer var2, boolean var3) throws ManagementException {
      super(var1);
      this.httpServer = var2;
      this.defaultServer = var3;
      this.logRuntime = new LogRuntime(var2.getMBean().getWebServerLog(), this);
   }

   public String getWebServerName() {
      return this.httpServer.getName();
   }

   public boolean isDefaultWebServer() {
      return this.defaultServer;
   }

   public LogRuntimeMBean getLogRuntime() {
      return this.logRuntime;
   }
}
