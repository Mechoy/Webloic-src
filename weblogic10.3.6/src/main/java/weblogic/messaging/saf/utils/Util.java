package weblogic.messaging.saf.utils;

import java.io.StreamCorruptedException;
import java.security.AccessController;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.provider.ManagementService;
import weblogic.messaging.ID;
import weblogic.messaging.common.IDFactory;
import weblogic.messaging.common.IDImpl;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.work.WorkManager;
import weblogic.work.WorkManagerFactory;

public final class Util {
   private static final AuthenticatedSubject KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final IDFactory ID_FACTORY = new IDFactory();

   public static StreamCorruptedException versionIOException(int var0, int var1, int var2) {
      return SAFClientUtil.versionIOException(var0, var1, var2);
   }

   public static final WorkManager findOrCreateWorkManager(String var0, int var1, int var2, int var3) {
      ServerMBean var4 = ManagementService.getRuntimeAccess(KERNEL_ID).getServer();
      return var4.getUse81StyleExecuteQueues() ? WorkManagerFactory.getInstance().getSystem() : WorkManagerFactory.getInstance().findOrCreate(var0, var1, var2, var3);
   }

   public static ID generateID() {
      return new IDImpl(ID_FACTORY);
   }
}
