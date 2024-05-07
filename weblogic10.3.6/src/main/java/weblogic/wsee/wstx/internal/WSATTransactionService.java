package weblogic.wsee.wstx.internal;

import java.security.AccessController;
import javax.naming.NamingException;
import javax.transaction.SystemException;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.server.AbstractServerService;
import weblogic.server.ServiceFailureException;
import weblogic.store.PersistentStore;
import weblogic.store.PersistentStoreException;
import weblogic.store.PersistentStoreManager;
import weblogic.wsee.wstx.TransactionServices;

public class WSATTransactionService extends AbstractServerService {
   private TransactionServices transactionServices;
   private WSATGatewayRM wsatgw;
   private final DebugLogger debugWSAT = DebugLogger.getDebugLogger("DebugWSAT");
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public void start() throws ServiceFailureException {
      if (this.debugWSAT.isDebugEnabled()) {
         this.debugWSAT.debug("starting WSAT Transaction Service");
      }

      try {
         this.transactionServices = WLSTransactionServicesImpl.create();
         PersistentStoreManager var1 = PersistentStoreManager.getManager();
         PersistentStore var2 = var1.getDefaultStore();
         String var3 = ManagementService.getRuntimeAccess(kernelId).getServerName();
         String var4 = ManagementService.getRuntimeAccess(kernelId).getDomainName();
         this.wsatgw = WSATGatewayRM.create(var3 + "_" + var4, var2);
      } catch (NamingException var5) {
         if (this.debugWSAT.isDebugEnabled()) {
            this.debugWSAT.debug(var5.getMessage(), var5);
         }

         throw new ServiceFailureException(var5);
      } catch (SystemException var6) {
         if (this.debugWSAT.isDebugEnabled()) {
            this.debugWSAT.debug(var6.getMessage(), var6);
         }

         throw new ServiceFailureException(var6);
      } catch (PersistentStoreException var7) {
         if (this.debugWSAT.isDebugEnabled()) {
            this.debugWSAT.debug(var7.getMessage(), var7);
         }

         throw new ServiceFailureException(var7);
      }
   }

   public void stop() throws ServiceFailureException {
      if (this.debugWSAT.isDebugEnabled()) {
         this.debugWSAT.debug("stopping WSAT Transaction Service");
      }

      this.wsatgw.stop();
   }
}
