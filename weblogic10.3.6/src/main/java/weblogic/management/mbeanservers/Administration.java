package weblogic.management.mbeanservers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Hashtable;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.management.ManagementException;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.jmx.MBeanServerInvocationHandler;
import weblogic.management.mbeanservers.domainruntime.DomainRuntimeServiceMBean;
import weblogic.management.mbeanservers.edit.EditServiceMBean;
import weblogic.management.mbeanservers.runtime.RuntimeServiceMBean;
import weblogic.management.runtime.ServerRuntimeMBean;

public class Administration {
   private static DebugLogger debugLogger = DebugLogger.getDebugLogger("DebugJMXCore");
   private static final int EDIT = 1;
   private static final int RUNTIME = 2;
   private static final int DOMAIN_RUNTIME = 3;
   private MBeanServerConnection runtime;
   private MBeanServerConnection domain_runtime;
   private MBeanServerConnection edit;
   private JMXConnector runtime_connector;
   private JMXConnector domain_runtime_connector;
   private JMXConnector edit_connector;
   private static ObjectName runtime_on;
   private static ObjectName domain_runtime_on;
   private static ObjectName edit_on;

   public Administration(Context var1) throws ManagementException {
      this.runtime = this.getConnection(2, var1);
      ServerRuntimeMBean var2 = this.getServerRuntimeMBean();
      if (var2.isAdminServer()) {
         this.edit = this.getConnection(1, var1);
         this.domain_runtime = this.getConnection(3, var1);
      }

   }

   public Administration(String var1, String var2, String var3) throws ManagementException {
      this.runtime = this.getConnection(2, var1, var2, var3);
      ServerRuntimeMBean var4 = this.getServerRuntimeMBean();
      if (var4.isAdminServer()) {
         this.edit = this.getConnection(1, var1, var2, var3);
         this.domain_runtime = this.getConnection(3, var1, var2, var3);
      }

   }

   public RuntimeServiceMBean getRuntimeServiceMBean() {
      return (RuntimeServiceMBean)MBeanServerInvocationHandler.newProxyInstance(this.runtime, runtime_on);
   }

   public DomainRuntimeServiceMBean getDomainRuntimeServiceMBean() {
      if (this.domain_runtime == null) {
         String var1 = "This method (getDomainRuntimeServiceMBean) can only be called when connected to an admin server";
         throw new AssertionError(var1);
      } else {
         return (DomainRuntimeServiceMBean)MBeanServerInvocationHandler.newProxyInstance(this.domain_runtime, domain_runtime_on);
      }
   }

   public EditServiceMBean getEditServiceMBean() {
      if (this.edit == null) {
         String var1 = "This method (getEditServiceMBean) can only be called when connected to an admin server";
         throw new AssertionError(var1);
      } else {
         return (EditServiceMBean)MBeanServerInvocationHandler.newProxyInstance(this.edit, edit_on);
      }
   }

   public ServerRuntimeMBean getServerRuntimeMBean() {
      RuntimeServiceMBean var1 = this.getRuntimeServiceMBean();
      return var1.getServerRuntime();
   }

   public DomainMBean getDomainMBean() {
      RuntimeServiceMBean var1 = this.getRuntimeServiceMBean();
      return var1.getDomainConfiguration();
   }

   private MBeanServerConnection getConnection(int var1, Context var2) throws ManagementException {
      String var3 = this.getJndiName(var1);
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Get mbean server connection for " + var3);
      }

      Object var4 = null;

      try {
         JMXServiceURL var5 = new JMXServiceURL("wlx", (String)null, 0, var3);
         Hashtable var6 = new Hashtable();
         var6.put("jmx.remote.protocol.provider.pkgs", "weblogic.management.remote");
         var6.put("weblogic.context", var2);
         switch (var1) {
            case 1:
               this.edit_connector = JMXConnectorFactory.connect(var5, var6);
               return this.edit_connector.getMBeanServerConnection();
            case 2:
               this.runtime_connector = JMXConnectorFactory.connect(var5, var6);
               return this.runtime_connector.getMBeanServerConnection();
            case 3:
               this.domain_runtime_connector = JMXConnectorFactory.connect(var5, var6);
               return this.domain_runtime_connector.getMBeanServerConnection();
            default:
               return null;
         }
      } catch (MalformedURLException var7) {
         throw new ManagementException(var7);
      } catch (IOException var8) {
         throw new ManagementException(var8);
      }
   }

   private MBeanServerConnection getConnection(int var1, String var2, String var3, String var4) throws ManagementException {
      String var5 = this.getJndiName(var1);
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Get mbean server connection for " + var5 + " address " + var4);
      }

      try {
         URI var6 = new URI(var4);
         String var7 = var6.getScheme();
         String var8 = var6.getHost();
         int var9 = var6.getPort();
         Hashtable var10 = new Hashtable();
         var10.put("java.naming.factory.initial", "weblogic.jndi.WLInitialContextFactory");
         var10.put("java.naming.provider.url", var4);
         var10.put("java.naming.security.principal", var2);
         var10.put("java.naming.security.credentials", var3);
         InitialContext var11 = new InitialContext(var10);
         Hashtable var12 = var11.getEnvironment();
         JMXServiceURL var13 = new JMXServiceURL(var7, var8, var9, var5);
         Hashtable var14 = new Hashtable();
         var14.put("jmx.remote.protocol.provider.pkgs", "weblogic.management.remote");
         if (var2 != null && var3 != null) {
            var14.put("java.naming.security.principal", var2);
            var14.put("java.naming.security.credentials", var3);
         }

         switch (var1) {
            case 1:
               this.edit_connector = JMXConnectorFactory.connect(var13, var14);
               return this.edit_connector.getMBeanServerConnection();
            case 2:
               this.runtime_connector = JMXConnectorFactory.connect(var13, var14);
               return this.runtime_connector.getMBeanServerConnection();
            case 3:
               this.domain_runtime_connector = JMXConnectorFactory.connect(var13, var14);
               return this.domain_runtime_connector.getMBeanServerConnection();
            default:
               return null;
         }
      } catch (URISyntaxException var15) {
         throw new ManagementException(var15);
      } catch (MalformedURLException var16) {
         throw new ManagementException(var16);
      } catch (NamingException var17) {
         throw new ManagementException(var17);
      } catch (IOException var18) {
         throw new ManagementException(var18);
      }
   }

   private String getJndiName(int var1) {
      String var2 = null;
      switch (var1) {
         case 1:
            var2 = "weblogic.management.mbeanservers.edit";
            break;
         case 2:
            var2 = "weblogic.management.mbeanservers.runtime";
            break;
         case 3:
            var2 = "weblogic.management.mbeanservers.domainruntime";
            break;
         default:
            var2 = "weblogic.management.mbeanservers.runtime";
      }

      return "/jndi/" + var2;
   }

   public void close() {
      if (this.runtime_connector != null) {
         try {
            this.runtime_connector.close();
         } catch (IOException var4) {
         }
      }

      if (this.domain_runtime_connector != null) {
         try {
            this.domain_runtime_connector.close();
         } catch (IOException var3) {
         }
      }

      if (this.edit_connector != null) {
         try {
            this.edit_connector.close();
         } catch (IOException var2) {
         }
      }

   }

   static {
      try {
         runtime_on = new ObjectName(RuntimeServiceMBean.OBJECT_NAME);
         domain_runtime_on = new ObjectName(DomainRuntimeServiceMBean.OBJECT_NAME);
         edit_on = new ObjectName(EditServiceMBean.OBJECT_NAME);
      } catch (Exception var1) {
         throw new AssertionError(var1);
      }
   }
}
