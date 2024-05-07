package weblogic.management.provider;

import javax.management.MBeanServer;
import weblogic.jndi.factories.java.javaURLContextFactory;
import weblogic.management.internal.SecurityHelper;
import weblogic.management.provider.core.ManagementCoreService;
import weblogic.security.acl.internal.AuthenticatedSubject;

public class ManagementService extends ManagementCoreService {
   private static RuntimeAccess runtimeAccess;
   private static DomainAccess domainAccess;
   private static MBeanServer runtimeMBeanServer;
   private static MBeanServer domainRuntimeMBeanServer;

   public static RuntimeAccess getRuntimeAccess(AuthenticatedSubject var0) {
      assert runtimeAccess != null : "The ManagementService has not been initialized & runtimeAccess is null";

      SecurityHelper.assertIfNotKernel(var0);
      return runtimeAccess;
   }

   public static DomainAccess getDomainAccess(AuthenticatedSubject var0) {
      assert domainAccess != null;

      SecurityHelper.assertIfNotKernel(var0);
      return domainAccess;
   }

   public static void initializeRuntime(RuntimeAccess var0) {
      if (runtimeAccess != null) {
         throw new AssertionError("The managment service can only be initialized once");
      } else {
         runtimeAccess = var0;
      }
   }

   public static boolean isRuntimeAccessInitialized() {
      return runtimeAccess != null;
   }

   public static void initializeDomain(DomainAccess var0) {
      if (domainAccess != null) {
         throw new AssertionError("The domain access can only be initialized once");
      } else {
         domainAccess = var0;
      }
   }

   public static void initializeRuntimeMBeanServer(AuthenticatedSubject var0, MBeanServer var1) {
      SecurityHelper.assertIfNotKernel(var0);
      if (runtimeMBeanServer != null) {
         throw new AssertionError("MBeanServer may not be reset.");
      } else {
         javaURLContextFactory.setRuntimeMBeanServer(var1);
         runtimeMBeanServer = var1;
      }
   }

   public static void initializeDomainRuntimeMBeanServer(AuthenticatedSubject var0, MBeanServer var1) {
      SecurityHelper.assertIfNotKernel(var0);
      if (domainRuntimeMBeanServer != null) {
         throw new AssertionError("DomainRuntime MBeanServer may not be reset.");
      } else {
         domainRuntimeMBeanServer = var1;
         javaURLContextFactory.setDomainRuntimeMBeanServer(var1);
      }
   }

   public static MBeanServer getRuntimeMBeanServer(AuthenticatedSubject var0) {
      SecurityHelper.assertIfNotKernel(var0);
      return runtimeMBeanServer;
   }

   public static MBeanServer getDomainRuntimeMBeanServer(AuthenticatedSubject var0) {
      SecurityHelper.assertIfNotKernel(var0);
      return domainRuntimeMBeanServer;
   }

   public static PropertyService getPropertyService(AuthenticatedSubject var0) {
      return PropertyService.getPropertyService(var0);
   }
}
