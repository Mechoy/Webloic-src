package weblogic.management.provider;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import weblogic.management.internal.SecurityHelper;
import weblogic.security.acl.internal.AuthenticatedSubject;

public class ManagementServiceRestricted {
   private static EditAccess editAccess;
   private static final Set allowedClasses = new HashSet(Arrays.asList("weblogic.management.mbeanservers.compatibility.internal.CompatibilityMBeanServerService", "weblogic.deploy.api.internal.utils.JMXDeployerHelper", "weblogic.deploy.internal.adminserver.ConfigChangesHandler", "weblogic.deploy.internal.adminserver.EditAccessHelper", "weblogic.management.mbeanservers.domainruntime.internal.DomainRuntimeServerService$SINGLETON", "weblogic.management.mbeanservers.domainruntime.internal.DomainRuntimeServiceMBeanImpl$SINGLETON", "weblogic.management.mbeanservers.edit.internal.EditLockInterceptor", "weblogic.management.mbeanservers.edit.internal.EditServerService", "weblogic.cluster.migration.management.MigratableServiceCoordinatorRuntime", "weblogic.cluster.migration.management.MigrationTask", "weblogic.management.provider.internal.ConfigImageSource", "weblogic.management.provider.internal.EditAccessService", "weblogic.management.provider.internal.TestEditAccess", "weblogic.management.provider.internal.TestLockManager", "weblogic.management.deploy.ApplicationsDirPoller"));

   public static EditAccess getEditAccess(AuthenticatedSubject var0) {
      assert editAccess != null : "EditAccess is not initialized";

      SecurityHelper.assertIfNotKernel(var0);
      checkAccess(new Throwable());
      return editAccess;
   }

   public static void setEditAccess(EditAccess var0) {
      if (editAccess != null) {
         throw new AssertionError("Edit Access Can only be initialized once.");
      } else {
         editAccess = var0;
      }
   }

   private static void checkAccess(Throwable var0) {
      StackTraceElement[] var1 = var0.getStackTrace();
      if (var1 != null && var1.length >= 2) {
         StackTraceElement var2 = var1[1];
         String var3 = var2.getClassName();
         if (!allowedClasses.contains(var3)) {
            throw new Error("Access to EditAccess is restricted. Email to wls-oam-dev@bea.com for details");
         }
      }
   }
}
