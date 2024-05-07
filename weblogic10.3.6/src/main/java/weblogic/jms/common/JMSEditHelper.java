package weblogic.jms.common;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Hashtable;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.naming.Context;
import weblogic.management.jmx.MBeanServerInvocationHandler;
import weblogic.management.mbeanservers.domainruntime.DomainRuntimeServiceMBean;
import weblogic.management.mbeanservers.edit.ConfigurationManagerMBean;
import weblogic.management.mbeanservers.edit.EditServiceMBean;
import weblogic.management.mbeanservers.runtime.RuntimeServiceMBean;
import weblogic.management.runtime.ServerRuntimeMBean;
import weblogic.security.auth.login.PasswordCredential;

public class JMSEditHelper {
   private static final String PROTOCOL_PACKAGE = "weblogic.management.remote";
   private static final String EDIT_SERVER = "weblogic.management.mbeanservers.edit";
   private static final String RUN_SERVER = "weblogic.management.mbeanservers.runtime";
   private static final String DOMAIN_RUNTIME_SERVER = "weblogic.management.mbeanservers.domainruntime";

   private static MBeanServerConnection lookupMBeanServerConnection(String var0, Context var1) throws javax.jms.JMSException {
      JMXServiceURL var2;
      try {
         var2 = new JMXServiceURL("wlx", "localhost", 7001, "/jndi/" + var0);
      } catch (MalformedURLException var9) {
         throw new JMSException("ERROR: While trying to get JMXServiceURL: " + var9, var9);
      }

      Hashtable var4 = new Hashtable();
      var4.put("jmx.remote.protocol.provider.pkgs", "weblogic.management.remote");
      var4.put("weblogic.context", var1);

      JMXConnector var3;
      try {
         var3 = JMXConnectorFactory.connect(var2, var4);
      } catch (IOException var8) {
         throw new JMSException("ERROR: Could not connect the JMXConnector", var8);
      }

      try {
         MBeanServerConnection var5 = var3.getMBeanServerConnection();
         return var5;
      } catch (IOException var7) {
         throw new JMSException("ERROR: Could not get the MBeanServerConnnection", var7);
      }
   }

   public static ConfigurationManagerMBean getConfigurationManager(Context var0) throws javax.jms.JMSException {
      MBeanServerConnection var1 = lookupMBeanServerConnection("weblogic.management.mbeanservers.edit", var0);

      ObjectName var2;
      try {
         var2 = new ObjectName(EditServiceMBean.OBJECT_NAME);
      } catch (MalformedObjectNameException var6) {
         throw new JMSException("ERROR: While trying to get an object name got a malformed object name: " + EditServiceMBean.OBJECT_NAME + " due to: " + var6, var6);
      }

      EditServiceMBean var3;
      try {
         var3 = (EditServiceMBean)MBeanServerInvocationHandler.newProxyInstance(var1, var2);
      } catch (Throwable var5) {
         throw new JMSException("ERROR: While trying to get the edit service got a throwable: " + var5, var5);
      }

      return var3.getConfigurationManager();
   }

   public static RuntimeServiceMBean getRuntimeService(Context var0) throws javax.jms.JMSException {
      MBeanServerConnection var1 = lookupMBeanServerConnection("weblogic.management.mbeanservers.runtime", var0);

      ObjectName var2;
      try {
         var2 = new ObjectName(RuntimeServiceMBean.OBJECT_NAME);
      } catch (MalformedObjectNameException var6) {
         throw new JMSException("ERROR: While trying to get an object name got a malformed object name: " + RuntimeServiceMBean.OBJECT_NAME + " due to: " + var6, var6);
      }

      try {
         RuntimeServiceMBean var3 = (RuntimeServiceMBean)MBeanServerInvocationHandler.newProxyInstance(var1, var2);
         return var3;
      } catch (Throwable var5) {
         throw new JMSException("ERROR: While trying to get the edit service got a throwable: " + var5, var5);
      }
   }

   public static DomainRuntimeServiceMBean getDomainRuntimeService(Context var0) throws Exception {
      try {
         RuntimeServiceMBean var1 = getRuntimeService(var0);
         ServerRuntimeMBean var2 = var1.getServerRuntime();
         String var3 = var2.getAdminServerHost();
         int var4 = var2.getAdminServerListenPort();
         String var5 = (String)var0.getEnvironment().get("java.naming.security.principal");
         String var6 = null;
         Object var7 = var0.getEnvironment().get("java.naming.security.credentials");
         if (var7 != null && var7 instanceof PasswordCredential) {
            var6 = ((PasswordCredential)var7).getPassword();
         } else {
            var6 = (String)var7;
         }

         JMXServiceURL var8 = new JMXServiceURL("t3", var3, var4, "/jndi/weblogic.management.mbeanservers.domainruntime");
         Hashtable var9 = new Hashtable();
         if (var5 != null) {
            var9.put("java.naming.security.principal", var5);
         }

         if (var6 != null) {
            var9.put("java.naming.security.credentials", var6);
         }

         var9.put("jmx.remote.protocol.provider.pkgs", "weblogic.management.remote");
         JMXConnector var10 = JMXConnectorFactory.connect(var8, var9);
         MBeanServerConnection var11 = var10.getMBeanServerConnection();
         DomainRuntimeServiceMBean var12 = (DomainRuntimeServiceMBean)MBeanServerInvocationHandler.newProxyInstance(var11, new ObjectName(DomainRuntimeServiceMBean.OBJECT_NAME));
         return var12;
      } catch (Throwable var13) {
         throw new Exception(var13);
      }
   }
}
