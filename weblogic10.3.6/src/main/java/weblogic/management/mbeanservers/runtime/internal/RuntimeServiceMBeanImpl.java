package weblogic.management.mbeanservers.runtime.internal;

import weblogic.descriptor.DescriptorBean;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.mbeanservers.Service;
import weblogic.management.mbeanservers.internal.RuntimeServiceImpl;
import weblogic.management.mbeanservers.runtime.RuntimeServiceMBean;
import weblogic.management.provider.RuntimeAccess;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.management.runtime.ServerRuntimeMBean;

public class RuntimeServiceMBeanImpl extends RuntimeServiceImpl implements RuntimeServiceMBean {
   private static DebugLogger debug = DebugLogger.getDebugLogger("DebugJMXRuntime");
   private final RuntimeAccess runtime;
   private final String serverName;
   private final DomainMBean domain;
   private final ServerMBean server;
   private final ServerRuntimeMBean serverRuntime;

   RuntimeServiceMBeanImpl(RuntimeAccess var1) {
      super("RuntimeService", RuntimeServiceMBean.class.getName(), (Service)null);
      this.runtime = var1;
      this.domain = this.runtime.getDomain();
      this.server = this.runtime.getServer();
      this.serverName = this.runtime.getServerName();
      this.serverRuntime = this.runtime.getServerRuntime();
   }

   public DomainMBean getDomainConfiguration() {
      return this.domain;
   }

   public ServerMBean getServerConfiguration() {
      return this.server;
   }

   public String getServerName() {
      return this.serverName;
   }

   public ServerRuntimeMBean getServerRuntime() {
      return this.serverRuntime;
   }

   public RuntimeMBean findRuntime(DescriptorBean var1) {
      return this.runtime.lookupRuntime(var1);
   }

   public DescriptorBean findConfiguration(RuntimeMBean var1) {
      if (debug.isDebugEnabled()) {
         debug.debug("Looking up configuration for a " + var1);
      }

      DescriptorBean var2 = this.runtime.lookupConfigurationBean(var1);
      if (debug.isDebugEnabled()) {
         debug.debug("Found " + var2);
      }

      return var2;
   }

   public Service findService(String var1, String var2) {
      return (Service)this.runtime.findService(var1, var2);
   }

   public Service[] getServices() {
      weblogic.management.provider.Service[] var1 = this.runtime.getRootServices();
      Service[] var2 = new Service[var1.length];
      System.arraycopy(var1, 0, var2, 0, var1.length);
      return var2;
   }
}
