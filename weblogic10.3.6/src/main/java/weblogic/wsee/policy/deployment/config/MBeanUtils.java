package weblogic.wsee.policy.deployment.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import weblogic.management.ManagementException;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.jmx.MBeanServerInvocationHandler;
import weblogic.management.mbeanservers.domainruntime.DomainRuntimeServiceMBean;
import weblogic.management.mbeanservers.edit.ConfigurationManagerMBean;
import weblogic.management.mbeanservers.edit.EditServiceMBean;
import weblogic.management.runtime.DomainRuntimeMBean;
import weblogic.management.runtime.ServerRuntimeMBean;

public class MBeanUtils {
   private static MBeanServerConnection domainEditMBeanServerConnection = null;
   private static MBeanServerConnection domainRuntimeMBeanServerConnection = null;
   private static DomainMBean domainMBean = null;
   private static ConfigurationManagerMBean configurationManagerMBean = null;
   private static DomainRuntimeMBean domainRuntimeMBean = null;
   private static int cachedListenPort = 7001;
   private static String cachedProtocol = null;
   private static String cachedHost = null;
   private static String cachedUser = null;
   private static String cachedUserPassword = null;
   private static String serverTimeout = "240";

   public static ServerRuntimeMBean getServerRuntimeMBean(String var0) throws InstanceNotFoundException, ManagementException {
      try {
         ServerRuntimeMBean var1 = getDomainRuntimeServiceMBean().lookupServerRuntime(var0);
         return isCallableServerRuntimeMBean(var1) ? var1 : null;
      } catch (Exception var2) {
         return null;
      }
   }

   private static boolean isCallableServerRuntimeMBean(ServerRuntimeMBean var0) {
      if (var0 != null) {
         try {
            var0.getCurrentDirectory();
            return true;
         } catch (Throwable var2) {
            return false;
         }
      } else {
         return false;
      }
   }

   public static DomainRuntimeServiceMBean getDomainRuntimeServiceMBean() throws ManagementException {
      DomainRuntimeServiceMBean var0 = null;
      if (var0 == null) {
         try {
            MBeanServerConnection var1 = getDomainRuntimeMBeanServerConnection();
            var0 = (DomainRuntimeServiceMBean)MBeanServerInvocationHandler.newProxyInstance(var1, new ObjectName(DomainRuntimeServiceMBean.OBJECT_NAME), DomainRuntimeServiceMBean.class, false);
         } catch (IOException var2) {
            throw new ManagementException("Domain Runtime MBean Server is not enabled. You will need to enable it through the JMXMBean.");
         } catch (MalformedObjectNameException var3) {
            throw new ManagementException(var3);
         } catch (Throwable var4) {
            throw new ManagementException(var4);
         }
      }

      if (var0 == null) {
         throw new ManagementException("DomainRuntimeService is not enabled. You will need to enable it through the JMXMBean.");
      } else {
         return var0;
      }
   }

   public static MBeanServerConnection getDomainRuntimeMBeanServerConnection() throws IOException {
      if (domainRuntimeMBeanServerConnection == null) {
         domainRuntimeMBeanServerConnection = lookupMBeanServerConnection("weblogic.management.mbeanservers.domainruntime");
      }

      return domainRuntimeMBeanServerConnection;
   }

   public static EditServiceMBean getEditServiceMBean() throws IOException, ManagementException {
      EditServiceMBean var0 = null;
      if (var0 == null) {
         try {
            MBeanServerConnection var1 = getDomainEditMBeanServerConnection();
            var0 = (EditServiceMBean)MBeanServerInvocationHandler.newProxyInstance(var1, new ObjectName(EditServiceMBean.OBJECT_NAME), EditServiceMBean.class, false);
         } catch (IOException var2) {
            throw new ManagementException("Edit Server is not enabled. You will need to enable it through the JMXMBean.");
         } catch (MalformedObjectNameException var3) {
            throw new ManagementException(var3);
         } catch (Throwable var4) {
            throw new ManagementException(var4);
         }
      }

      if (var0 == null) {
         throw new ManagementException("Edit Server is not enabled. You will need to enable it through the JMXMBean.");
      } else {
         return var0;
      }
   }

   private static MBeanServerConnection getDomainEditMBeanServerConnection() throws IOException {
      if (domainEditMBeanServerConnection == null) {
         domainEditMBeanServerConnection = lookupMBeanServerConnection("weblogic.management.mbeanservers.edit");
      }

      return domainEditMBeanServerConnection;
   }

   private static MBeanServerConnection lookupMBeanServerConnection(String var0) throws IOException {
      int var1 = 7001;
      String var2 = "localhost";
      String var3 = "wlx";
      if (cachedListenPort != 7001) {
         var1 = cachedListenPort;
      }

      if (cachedHost != null) {
         var2 = cachedHost;
      }

      if (cachedProtocol != null) {
         var3 = cachedProtocol;
      }

      JMXServiceURL var4 = new JMXServiceURL(var3, var2, var1, "/jndi/" + var0);
      Hashtable var5 = new Hashtable();
      var5.put("jmx.remote.protocol.provider.pkgs", "weblogic.management.remote");
      if (cachedUser != null) {
         var5.put("java.naming.security.principal", cachedUser);
      }

      if (cachedUserPassword != null) {
         var5.put("java.naming.security.credentials", cachedUserPassword);
      }

      long var6 = Long.parseLong(serverTimeout);
      var6 *= 1000L;
      var5.put("jmx.remote.x.request.waiting.timeout", new Long(var6));
      JMXConnector var8 = JMXConnectorFactory.connect(var4, var5);
      return var8.getMBeanServerConnection();
   }

   public static DomainMBean getDomainMBean() throws IOException, ManagementException {
      if (domainMBean == null) {
         domainMBean = getEditServiceMBean().getDomainConfiguration();
         if (domainMBean == null) {
            throw new ManagementException("No DomainMBean is available for this domain. You will need to correct the configuration before continuing.");
         }
      }

      return domainMBean;
   }

   public static ServerRuntimeMBean[] getServerRuntimeMBeans() throws ManagementException {
      ArrayList var0 = new ArrayList();
      ServerRuntimeMBean[] var1 = getDomainRuntimeServiceMBean().getServerRuntimes();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         ServerRuntimeMBean var4 = var1[var3];
         if (isCallableServerRuntimeMBean(var4)) {
            var0.add(var4);
         }
      }

      return (ServerRuntimeMBean[])((ServerRuntimeMBean[])var0.toArray(new ServerRuntimeMBean[var0.size()]));
   }

   public static ConfigurationManagerMBean getConfigurationManagerMBean() throws ManagementException {
      if (configurationManagerMBean == null) {
         try {
            configurationManagerMBean = getEditServiceMBean().getConfigurationManager();
         } catch (IOException var1) {
            throw new ManagementException(var1);
         }
      }

      return configurationManagerMBean;
   }

   public static DomainRuntimeMBean getDomainRuntimeMBean() throws ManagementException {
      if (domainRuntimeMBean == null) {
         domainRuntimeMBean = getDomainRuntimeServiceMBean().getDomainRuntime();
      }

      return domainRuntimeMBean;
   }
}
