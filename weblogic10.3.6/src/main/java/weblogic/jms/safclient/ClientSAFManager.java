package weblogic.jms.safclient;

import java.io.File;
import java.util.HashMap;
import weblogic.jms.common.CrossDomainSecurityManager;
import weblogic.jms.extensions.ClientSAF;
import weblogic.jms.safclient.transaction.jta.SimpleTransactionHelper;
import weblogic.jms.safclient.transaction.jta.SimpleTransactionManager;
import weblogic.kernel.KernelInitializer;

public final class ClientSAFManager {
   private static ClientSAFManager singleton;
   private static boolean properlyInitialized = false;
   private static AssertionError initializationError;
   private static SimpleTransactionHelper clientSAFTxHelper;
   private HashMap delegates = new HashMap();

   private static void initialize() throws AssertionError {
      CrossDomainSecurityManager.ensureSubjectManagerInitialized();
      KernelInitializer.initializeWebLogicKernel();
      clientSAFTxHelper = new SimpleTransactionHelper(new SimpleTransactionManager());
   }

   static ClientSAFManager getManager() {
      if (!properlyInitialized) {
         throw initializationError;
      } else if (singleton != null) {
         return singleton;
      } else {
         singleton = new ClientSAFManager();
         return singleton;
      }
   }

   private ClientSAFManager() {
   }

   ClientSAFDelegate createDelegate(File var1, ClientSAF var2) {
      synchronized(this.delegates) {
         ClientSAFDelegate var4 = (ClientSAFDelegate)this.delegates.get(var1);
         if (var4 != null) {
            return var4;
         } else {
            var4 = new ClientSAFDelegate(var2);
            this.delegates.put(var1, var4);
            return var4;
         }
      }
   }

   ClientSAFDelegate getDelegate(File var1) {
      synchronized(this.delegates) {
         return (ClientSAFDelegate)this.delegates.get(var1);
      }
   }

   void removeDelegate(File var1) {
      synchronized(this.delegates) {
         this.delegates.remove(var1);
      }
   }

   public static SimpleTransactionHelper getTxHelper() {
      return clientSAFTxHelper;
   }

   static {
      try {
         initialize();
         properlyInitialized = true;
      } catch (AssertionError var1) {
         initializationError = var1;
      }

   }
}
