package weblogic.corba.server.transaction;

import java.util.Hashtable;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;
import org.omg.CosTransactions.PropagationContext;
import weblogic.corba.j2ee.transaction.TransactionManagerWrapper;
import weblogic.rmi.cluster.ThreadPreferredHost;
import weblogic.rmi.spi.EndPoint;
import weblogic.rmi.spi.HostID;
import weblogic.transaction.CoordinatorService;
import weblogic.transaction.ServerTransactionInterceptor;
import weblogic.transaction.TransactionInterceptor;
import weblogic.transaction.TransactionManager;
import weblogic.transaction.loggingresource.LoggingResource;
import weblogic.transaction.nonxa.NonXAResource;

public final class TransactionManagerImpl extends TransactionManagerWrapper implements TransactionManager, ServerTransactionInterceptor {
   public static TransactionManagerWrapper getTransactionManager() {
      return TransactionManagerImpl.TMMaker.SINGLETON;
   }

   protected TransactionManagerImpl() {
   }

   public void registerStaticResource(String var1, XAResource var2) throws SystemException {
      throw new SystemException();
   }

   public void registerDynamicResource(String var1, XAResource var2) throws SystemException {
      throw new SystemException();
   }

   public void registerDynamicResource(String var1, NonXAResource var2) throws SystemException {
      throw new SystemException();
   }

   public void registerFailedLoggingResource(Throwable var1) {
      throw new IllegalStateException();
   }

   public void registerResource(String var1, XAResource var2) throws SystemException {
      throw new SystemException();
   }

   public void registerResource(String var1, XAResource var2, Hashtable var3) throws SystemException {
      throw new SystemException();
   }

   public void unregisterResource(String var1) throws SystemException {
      throw new SystemException();
   }

   public void unregisterResource(String var1, boolean var2) throws SystemException {
      throw new SystemException();
   }

   public void registerCoordinatorService(String var1, CoordinatorService var2) {
      throw new IllegalStateException();
   }

   public void registerLoggingResourceTransactions(LoggingResource var1) throws SystemException {
      throw new SystemException();
   }

   public Transaction getTransaction(Xid var1) {
      return null;
   }

   public TransactionInterceptor getInterceptor() {
      return this;
   }

   public void begin(String var1) throws SystemException {
      throw new SystemException();
   }

   public void begin(int var1) throws SystemException {
      throw new SystemException();
   }

   public void begin(String var1, int var2) throws SystemException {
      throw new SystemException();
   }

   public Object sendRequest(Object var1) {
      PropagationContext var2 = this.getTM().get_txcontext();
      EndPoint var3 = null;
      if (var1 instanceof EndPoint) {
         var3 = (EndPoint)var1;
      }

      if (var2 != null && var3 != null && var3.getHostID() != null) {
         ThreadPreferredHost.set(var3.getHostID());
      } else {
         ThreadPreferredHost.set((HostID)null);
      }

      return var2;
   }

   public Object sendResponse(Object var1) {
      return null;
   }

   public void receiveRequest(Object var1) {
   }

   public void receiveResponse(Object var1) {
   }

   public void dispatchRequest(Object var1) {
   }

   public void receiveAsyncResponse(Object var1) {
   }

   private static final class TMMaker {
      private static final TransactionManagerWrapper SINGLETON = new TransactionManagerImpl();
   }
}
