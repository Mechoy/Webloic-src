package weblogic.wsee.util;

import java.io.IOException;
import java.security.AccessController;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import weblogic.management.configuration.SSLMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public class MBeanUtil {
   private static AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public static ServerMBean getLocalServerMBean() {
      return ManagementService.getRuntimeAccess(kernelId).getServer();
   }

   public static SSLMBean getLocalSSLMBean() {
      return getLocalServerMBean().getSSL();
   }

   public static ObjectName[] getServerRuntimeNames(MBeanServerConnection var0) throws MalformedObjectNameException, InstanceNotFoundException, IOException, ReflectionException, AttributeNotFoundException, MBeanException {
      ObjectName var1 = new ObjectName("com.bea:Name=DomainRuntimeService,Type=weblogic.management.mbeanservers.domainruntime.DomainRuntimeServiceMBean");
      return (ObjectName[])((ObjectName[])var0.getAttribute(var1, "ServerRuntimes"));
   }

   public static ObjectName getServerRuntimeName(String var0, MBeanServerConnection var1) throws MalformedObjectNameException, InstanceNotFoundException, IOException, ReflectionException, AttributeNotFoundException, MBeanException {
      ObjectName[] var2 = getServerRuntimeNames(var1);
      ObjectName[] var3 = var2;
      int var4 = var2.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         ObjectName var6 = var3[var5];
         if (var6.getKeyProperty("Name").equals(var0)) {
            return var6;
         }
      }

      return null;
   }
}
