package weblogic.ejb.container.utils;

import java.io.IOException;
import javax.xml.stream.XMLStreamException;
import weblogic.descriptor.DescriptorBean;
import weblogic.ejb.container.deployer.EJBDescriptorMBeanUtils;
import weblogic.ejb.spi.EjbDescriptorBean;
import weblogic.ejb.spi.EjbJarDescriptor;
import weblogic.j2ee.descriptor.AssemblyDescriptorBean;
import weblogic.j2ee.descriptor.EjbJarBean;
import weblogic.j2ee.descriptor.EnterpriseBeansBean;
import weblogic.utils.jars.VirtualJarFile;

public class AutoRefLibHelper {
   public static void mergeEJBJar(EjbJarDescriptor var0, VirtualJarFile[] var1) throws IOException, XMLStreamException {
      if (var1 != null && var1.length > 0) {
         VirtualJarFile[] var2 = var1;
         int var3 = var1.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            VirtualJarFile var5 = var2[var4];
            validateAutoRefLib(var5);
         }

         DescriptorBean var6 = var0.getEjbDescriptorLoader().loadDescriptorBean();
         var0.getEjbDescriptorLoader().mergeDescriptors(var1);
         var6 = var0.getEjbDescriptorLoader().loadDescriptorBean();
      }

   }

   private static void validateAutoRefLib(VirtualJarFile var0) throws IOException {
      IOException var2;
      try {
         EjbDescriptorBean var1 = EJBDescriptorMBeanUtils.createDescriptorFromJarFile(var0, true);
         EjbJarBean var7 = var1.getEjbJarBean();
         EnterpriseBeansBean var3 = var7.getEnterpriseBeans();
         if (var3 != null) {
            throw new IllegalArgumentException("ejb-jar.xml in auto ref lib shouldn't configure enterprise-beans");
         } else {
            AssemblyDescriptorBean var4 = var7.getAssemblyDescriptor();
            if (var4 == null) {
               throw new IllegalArgumentException("ejb-jar.xml in auto ref lib should configure assembly-descriptor");
            } else if (var4.getApplicationExceptions() != null && var4.getApplicationExceptions().length > 0) {
               throw new IllegalArgumentException("ejb-jar.xml in auto ref lib shouldn't configure application-exception");
            } else if (var4.getMessageDestinations() != null && var4.getMessageDestinations().length > 0) {
               throw new IllegalArgumentException("ejb-jar.xml in auto ref lib shouldn't configure message-destination");
            } else if (var4.getMethodPermissions() != null && var4.getMethodPermissions().length > 0) {
               throw new IllegalArgumentException("ejb-jar.xml in auto ref lib shouldn't configure method-permission");
            } else if (var4.getSecurityRoles() != null && var4.getSecurityRoles().length > 0) {
               throw new IllegalArgumentException("ejb-jar.xml in auto ref lib shouldn't configure security-role");
            } else if (var4.getInterceptorBindings() != null && var4.getInterceptorBindings().length != 0) {
               if (var7.getInterceptors() == null) {
                  throw new IllegalArgumentException("ejb-jar.xml in auto ref lib should configure interceptors");
               } else if (var7.getInterceptors().getInterceptors() == null || var7.getInterceptors().getInterceptors().length == 0) {
                  throw new IllegalArgumentException("ejb-jar.xml in auto ref lib should configure interceptor");
               }
            } else {
               throw new IllegalArgumentException("ejb-jar.xml in auto ref lib should configure interceptor-binding");
            }
         }
      } catch (IllegalArgumentException var5) {
         var2 = new IOException("Nested exception is: ");
         var2.initCause(var5);
         throw var2;
      } catch (Exception var6) {
         var2 = new IOException("Nested exception is: ");
         var2.initCause(var6);
         throw var2;
      }
   }
}
