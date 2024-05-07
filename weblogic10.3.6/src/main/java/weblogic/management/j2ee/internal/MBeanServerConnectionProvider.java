package weblogic.management.j2ee.internal;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.UnknownHostException;
import java.security.AccessController;
import java.util.Hashtable;
import javax.management.MBeanServerConnection;
import javax.management.Notification;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import weblogic.management.provider.ManagementService;
import weblogic.protocol.ServerURL;
import weblogic.protocol.URLManager;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

class MBeanServerConnectionProvider {
   private static MBeanServerConnection domainMBeanServerConnection = null;
   private static MBeanServerConnection editMBeanServerConnection = null;
   private static MBeanServerConnection runtimeMBeanServerConnection = null;
   private static final String JNDI = "/jndi/";
   private static AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public MBeanServerConnectionProvider() {
      throw new AssertionError("This class cannot be instantiated");
   }

   public static void initialize() {
      domainMBeanServerConnection = createMBeanServerConnection("weblogic.management.mbeanservers.domainruntime");
      editMBeanServerConnection = createMBeanServerConnection("weblogic.management.mbeanservers.edit");
      runtimeMBeanServerConnection = createMBeanServerConnection("weblogic.management.mbeanservers.runtime");
   }

   public static MBeanServerConnection getDomainMBeanServerConnection() {
      return domainMBeanServerConnection;
   }

   public static MBeanServerConnection getEditMBeanServerConnection() {
      return editMBeanServerConnection;
   }

   public static MBeanServerConnection geRuntimeMBeanServerConnection() {
      return runtimeMBeanServerConnection;
   }

   private static MBeanServerConnection createMBeanServerConnection(String var0) {
      JMXServiceURL var1;
      try {
         String var2 = ManagementService.getRuntimeAccess(kernelId).getAdminServerName();
         String var3 = URLManager.findAdministrationURL(var2);
         ServerURL var4 = new ServerURL(var3);
         var1 = new JMXServiceURL(var4.getProtocol(), var4.getHost(), var4.getPort(), "/jndi/" + var0);
      } catch (MalformedURLException var6) {
         throw new Error("Atempting to connect to the domain mbean server", var6);
      } catch (UnknownHostException var7) {
         return null;
      }

      try {
         Hashtable var8 = new Hashtable();
         var8.put("jmx.remote.protocol.provider.pkgs", "weblogic.management.remote");
         JMXConnector var9 = JMXConnectorFactory.connect(var1, var8);
         var9.addConnectionNotificationListener(createListener(), (NotificationFilter)null, (Object)null);
         return var9.getMBeanServerConnection();
      } catch (IOException var5) {
         throw new Error("Error while connecting to MBeanServer ", var5);
      }
   }

   private static NotificationListener createListener() {
      return new NotificationListener() {
         public void handleNotification(Notification var1, Object var2) {
         }
      };
   }
}
