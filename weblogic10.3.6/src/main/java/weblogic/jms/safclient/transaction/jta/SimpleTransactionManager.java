package weblogic.jms.safclient.transaction.jta;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.transaction.HeuristicMixedException;
import javax.transaction.InvalidTransactionException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;
import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;
import weblogic.transaction.CoordinatorService;
import weblogic.transaction.TransactionInterceptor;
import weblogic.transaction.UserTransaction;
import weblogic.transaction.loggingresource.LoggingResource;
import weblogic.transaction.nonxa.NonXAResource;

public class SimpleTransactionManager implements TransactionManager, weblogic.transaction.TransactionManager, UserTransaction {
   private final ThreadLocal tls = new ThreadLocal();
   private final Map transactions = new HashMap();
   private final Map resources = new LinkedHashMap();

   public void registerStaticResource(String var1, XAResource var2) throws SystemException {
      throw new SystemException("Not implemented");
   }

   public void registerDynamicResource(String var1, XAResource var2) {
      this.registerResource(var1, var2);
   }

   public void registerResource(String var1, XAResource var2, Hashtable var3) {
      this.registerResource(var1, var2);
   }

   public synchronized void registerResource(String var1, XAResource var2) {
      ResourceRec var3 = (ResourceRec)this.resources.get(var1);
      if (var3 == null) {
         var3 = new ResourceRec(var1, var2);
         this.resources.put(var1, var3);
      } else {
         var3.setResource(var2);
      }

   }

   public void registerDynamicResource(String var1, NonXAResource var2) throws SystemException {
      throw new SystemException("Not implemented");
   }

   public void unregisterResource(String var1) {
      this.unregisterResource(var1, false);
   }

   public synchronized void unregisterResource(String var1, boolean var2) {
      ResourceRec var3 = (ResourceRec)this.resources.get(var1);
      var3.setResource((XAResource)null);
   }

   public void setTransactionTimeout(int var1) {
   }

   private SimpleTransaction getTran() {
      return (SimpleTransaction)this.tls.get();
   }

   public Transaction getTransaction() {
      return this.getTran();
   }

   public Transaction getTransaction(Xid var1) {
      return null;
   }

   public int getStatus() {
      SimpleTransaction var1 = this.getTran();
      return var1 == null ? 6 : var1.getStatus();
   }

   public void setRollbackOnly() {
      SimpleTransaction var1 = this.getTran();
      if (var1 == null) {
         throw new IllegalStateException();
      } else {
         var1.setRollbackOnly();
      }
   }

   public void begin() throws NotSupportedException {
      this.begin("Client SAF", 0);
   }

   public void begin(String var1) throws NotSupportedException {
      this.begin(var1, 0);
   }

   public void begin(int var1) throws NotSupportedException {
      this.begin("Client SAF", var1);
   }

   public void begin(String var1, int var2) throws NotSupportedException {
      if (this.getTran() != null) {
         throw new NotSupportedException();
      } else {
         SimpleTransaction var3 = new SimpleTransaction(var1, this);
         this.tls.set(var3);
      }
   }

   public void commit() throws RollbackException, HeuristicMixedException, SystemException {
      SimpleTransaction var1 = this.getTran();
      if (var1 == null) {
         throw new IllegalStateException();
      } else {
         try {
            var1.commit();
         } finally {
            this.tls.set((Object)null);
         }

      }
   }

   public void rollback() throws SystemException {
      SimpleTransaction var1 = this.getTran();
      if (var1 == null) {
         throw new IllegalStateException();
      } else {
         try {
            var1.rollback();
         } finally {
            this.tls.set((Object)null);
         }

      }
   }

   public Transaction suspend() throws SystemException {
      SimpleTransaction var1 = this.getTran();
      if (var1 != null) {
         var1.suspendAll();
         this.tls.set((Object)null);
      }

      return var1;
   }

   public Transaction forceSuspend() {
      SimpleTransaction var1 = this.getTran();
      if (var1 != null) {
         this.tls.set((Object)null);
      }

      return var1;
   }

   public void resume(Transaction var1) throws SystemException, InvalidTransactionException {
      if (this.getTran() != null) {
         throw new IllegalStateException();
      } else {
         try {
            SimpleTransaction var2 = (SimpleTransaction)var1;
            if (var2.isSuspended()) {
               var2.resumeAll();
            }

            this.tls.set(var2);
         } catch (ClassCastException var3) {
            throw new InvalidTransactionException();
         }
      }
   }

   public void forceResume(Transaction var1) {
      if (this.getTran() != null) {
         throw new IllegalStateException();
      } else {
         SimpleTransaction var2 = (SimpleTransaction)var1;
         this.tls.set(var2);
      }
   }

   public void registerLoggingResourceTransactions(LoggingResource var1) throws SystemException {
      throw new SystemException("Not implemented");
   }

   public void registerFailedLoggingResource(Throwable var1) {
   }

   public void registerCoordinatorService(String var1, CoordinatorService var2) {
   }

   public TransactionInterceptor getInterceptor() {
      return null;
   }

   synchronized void addTransaction(SimpleTransaction var1) {
      this.transactions.put(var1.getXID(), var1);
   }

   synchronized void removeTransaction(SimpleTransaction var1) {
      this.transactions.remove(var1.getXID());
      this.tls.set((Object)null);
   }

   synchronized ResourceRec findResource(XAResource var1) throws XAException {
      Iterator var2 = this.resources.values().iterator();

      ResourceRec var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (ResourceRec)var2.next();
      } while(var3.getResource() != var1 && !var3.getResource().isSameRM(var1));

      return var3;
   }

   static final class ResourceRec {
      private String name;
      private XAResource resource;

      ResourceRec(String var1, XAResource var2) {
         this.name = var1;
         this.resource = var2;
      }

      XAResource getResource() {
         return this.resource;
      }

      void setResource(XAResource var1) {
         this.resource = var1;
      }

      public boolean equals(Object var1) {
         try {
            return this.name.equals(((ResourceRec)var1).name);
         } catch (ClassCastException var3) {
            return false;
         }
      }

      public int hashCode() {
         return this.name.hashCode();
      }
   }
}
