package weblogic.wsee.jaxws.framework.policy;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.xml.ws.WebServiceException;

public class EnvironmentMetadataFactory {
   private static EnvironmentMetadata envMetadata = null;

   private EnvironmentMetadataFactory() {
   }

   public static final EnvironmentMetadata getEnvironmentMetadata() {
      if (envMetadata == null) {
         MBeanServer var0 = null;

         try {
            InitialContext var1 = new InitialContext();
            var0 = (MBeanServer)var1.lookup("java:comp/env/jmx/runtime");
         } catch (NamingException var6) {
            throw new WebServiceException(var6);
         }

         try {
            ObjectName var7 = new ObjectName("com.bea:Name=RuntimeService,Type=weblogic.management.mbeanservers.runtime.RuntimeServiceMBean");
            String var2 = (String)var0.getAttribute(var7, "ServerName");
            ObjectName var3 = (ObjectName)var0.getAttribute(var7, "DomainConfiguration");
            String var4 = null;
            if (var3 != null) {
               var4 = var3.getKeyProperty("Name");
            }

            envMetadata = new EnvironmentMetadataImpl(var4, var2);
         } catch (Exception var5) {
            throw new WebServiceException(var5);
         }
      }

      return envMetadata;
   }
}
