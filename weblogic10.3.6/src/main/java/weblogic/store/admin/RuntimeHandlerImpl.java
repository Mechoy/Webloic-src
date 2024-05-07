package weblogic.store.admin;

import java.security.AccessController;
import weblogic.kernel.KernelStatus;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.store.PersistentStoreConnection;
import weblogic.store.PersistentStoreException;
import weblogic.store.RuntimeHandler;
import weblogic.store.RuntimeUpdater;
import weblogic.store.internal.PersistentStoreImpl;

public class RuntimeHandlerImpl implements RuntimeHandler {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public RuntimeUpdater createStoreMBean(PersistentStoreImpl var1) throws PersistentStoreException {
      return JMXUtils.createStoreMBean(var1);
   }

   public void registerConnectionMBean(RuntimeUpdater var1, PersistentStoreConnection var2) throws PersistentStoreException {
      JMXUtils.registerConnectionMBean((PersistentStoreRuntimeMBeanImpl)var1, var2);
   }

   public void unregisterConnectionMBean(RuntimeUpdater var1, PersistentStoreConnection var2) throws PersistentStoreException {
      JMXUtils.unregisterConnectionMBean((PersistentStoreRuntimeMBeanImpl)var1, var2);
   }

   public void unregisterStoreMBean(RuntimeUpdater var1) throws PersistentStoreException {
      JMXUtils.unregisterStoreMBean((PersistentStoreRuntimeMBeanImpl)var1);
   }

   public long getJTAAbandonTimeoutMillis() {
      long var1 = 0L;
      if (KernelStatus.isServer()) {
         DomainMBean var3 = ManagementService.getRuntimeAccess(kernelId).getDomain();
         var1 = (long)var3.getJTA().getAbandonTimeoutSeconds() * 1000L;
         var1 += 86400000L;
         var1 = Math.max(345600000L, var1);
      } else {
         var1 = 86400000L;
      }

      return var1;
   }

   public String getDomainName() {
      String var1 = ManagementService.getRuntimeAccess(kernelId).getDomain().getName();
      return var1 == null ? "" : var1;
   }
}
