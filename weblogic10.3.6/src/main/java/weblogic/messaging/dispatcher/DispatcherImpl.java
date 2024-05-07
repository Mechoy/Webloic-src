package weblogic.messaging.dispatcher;

import java.io.IOException;
import java.rmi.RemoteException;
import javax.transaction.Transaction;
import weblogic.common.internal.InteropWriteReplaceable;
import weblogic.common.internal.PeerInfo;
import weblogic.jms.common.JMSDebug;
import weblogic.kernel.KernelStatus;
import weblogic.rmi.extensions.AsyncResult;
import weblogic.rmi.extensions.PortableRemoteObject;
import weblogic.rmi.extensions.server.FutureResponse;
import weblogic.transaction.ClientTransactionManager;
import weblogic.transaction.TransactionHelper;
import weblogic.work.WorkManager;

public class DispatcherImpl implements Dispatcher, DispatcherRemote, DispatcherOneWay, InteropWriteReplaceable {
   private final String name;
   private final DispatcherId dispatcherId;
   private static final ClientTransactionManager tm = TransactionHelper.getTransactionHelper().getTransactionManager();
   public static final boolean TESTXA = false;
   private Object fastDispatcher;
   private final WorkManager workManager;
   private final WorkManager oneWayWorkManager;
   private final String objectHandlerClassName;
   private static final boolean FASTDISPATCH = fastDispatchEnabled();

   protected DispatcherImpl(String var1, DispatcherId var2, WorkManager var3, WorkManager var4, String var5) {
      this.name = var1;
      this.workManager = var3;
      this.oneWayWorkManager = var4;
      this.objectHandlerClassName = var5;
      this.dispatcherId = var2;
   }

   public String getName() {
      return this.name;
   }

   WorkManager getWorkManager() {
      return this.workManager;
   }

   WorkManager getOneWayWorkManager() {
      return this.oneWayWorkManager;
   }

   String getObjectHandlerClassName() {
      return this.objectHandlerClassName;
   }

   void export() {
      try {
         PortableRemoteObject.exportObject(this);
      } catch (RemoteException var2) {
      }

   }

   void unexport() {
      try {
         PortableRemoteObject.unexportObject(this);
      } catch (RemoteException var2) {
         throw new AssertionError(var2);
      }
   }

   public DispatcherId getId() {
      return this.dispatcherId;
   }

   public boolean isLocal() {
      return true;
   }

   public void dispatchAsync(Request var1) {
      var1.setTranInfo(2);

      try {
         dispatchAsyncInternal(var1, tm.getTransaction(), true);
      } catch (Exception var3) {
         if (JMSDebug.JMSDispatcher.isDebugEnabled()) {
            JMSDebug.JMSDispatcher.debug("DispatcherImpl.dispatchAsync(): " + var3.getMessage());
         }
      }

   }

   private static void dispatchAsyncInternal(Request var0, Transaction var1, boolean var2) {
      try {
         var0.wrappedFiniteStateMachine();
      } catch (Throwable var8) {
         var0.notifyResult(var8, false);
      } finally {
         if (var2) {
            cautiousResume(var0, var1);
         }

      }

   }

   private static final void cautiousResume(Object var0, Transaction var1) {
      try {
         Transaction var2 = tm.forceSuspend();
         if (var2 != null && JMSDebug.JMSDispatcher.isDebugEnabled()) {
            JMSDebug.JMSDispatcher.debug("DispatcherImpl.cautiousResume(): " + var0 + " retained " + var2);
         }
      } finally {
         if (var1 != null) {
            tm.forceResume(var1);
         }

      }

   }

   public void dispatchNoReply(Request var1) {
      Transaction var2 = tm.forceSuspend();
      dispatchAsyncInternal(var1, var2, true);
   }

   public void dispatchNoReplyWithId(Request var1, int var2) {
      Transaction var3 = tm.forceSuspend();
      dispatchAsyncInternal(var1, var3, true);
   }

   private Response syncRequest(Request var1) throws DispatcherException {
      try {
         Response var2 = var1.wrappedFiniteStateMachine();
         return var2 != var1 ? var2 : var1.getResult();
      } catch (RuntimeException var4) {
         var1.complete(var4, false);
         throw var4;
      } catch (Error var5) {
         var1.complete(var5, false);
         throw var5;
      } catch (DispatcherException var6) {
         var1.complete(var6, false);
         throw var6;
      } catch (Throwable var7) {
         DispatcherException var3 = new DispatcherException(var7);
         var1.complete(var3, false);
         throw var3;
      }
   }

   public Response dispatchSync(Request var1) throws DispatcherException {
      Transaction var2 = null;
      var2 = tm.forceSuspend();
      var1.setTranInfo(2);
      var1.setSyncRequest(true);

      Response var3;
      try {
         var3 = this.syncRequest(var1);
      } finally {
         cautiousResume(var1, var2);
      }

      return var3;
   }

   public Response dispatchSyncTran(Request var1) throws DispatcherException {
      Transaction var2 = null;

      try {
         var2 = tm.getTransaction();
      } catch (Exception var9) {
         throw new DispatcherException(var9);
      }

      var1.setTranInfo(2);
      var1.setSyncRequest(true);

      Response var3;
      try {
         var3 = this.syncRequest(var1);
      } finally {
         cautiousResume(var1, var2);
      }

      return var3;
   }

   public Response dispatchSyncFuture(Request var1) throws RemoteException {
      throw new Error("compiler error");
   }

   public void dispatchSyncFuture(Request var1, FutureResponse var2) {
      var1.setTranInfo(0);
      var1.setFutureResponse(var2);
      var1.setSyncRequest(true);
      dispatchAsyncInternal(var1, (Transaction)null, false);
   }

   public Response dispatchSyncTranFuture(Request var1) throws RemoteException {
      throw new Error("compiler error");
   }

   public Response dispatchSyncTranFutureWithId(Request var1, int var2) throws RemoteException {
      throw new Error("compiler error");
   }

   public void dispatchSyncTranFuture(Request var1, FutureResponse var2) {
      var1.setTranInfo(2);
      var1.setFutureResponse(var2);
      var1.setSyncRequest(true);
      dispatchAsyncInternal(var1, (Transaction)null, false);
   }

   public Response dispatchSyncNoTran(Request var1) throws DispatcherException {
      Transaction var2 = tm.forceSuspend();
      var1.setSyncRequest(true);

      Response var3;
      try {
         var3 = this.syncRequest(var1);
      } finally {
         cautiousResume(var1, var2);
      }

      return var3;
   }

   public Response dispatchSyncNoTranWithId(Request var1, int var2) throws DispatcherException {
      Transaction var3 = tm.forceSuspend();
      var1.setSyncRequest(true);

      Response var4;
      try {
         var4 = this.syncRequest(var1);
      } finally {
         cautiousResume(var1, var3);
      }

      return var4;
   }

   public Response dispatchSyncNoTranFuture(Request var1) throws RemoteException {
      throw new Error("compiler error");
   }

   public void dispatchSyncNoTranFuture(Request var1, FutureResponse var2) {
      var1.setTranInfo(0);
      var1.setFutureResponse(var2);
      var1.setSyncRequest(true);
      dispatchAsyncInternal(var1, (Transaction)null, false);
   }

   public void dispatchAsyncFuture(Request var1, AsyncResult var2) {
      throw new Error("compiler error");
   }

   public void dispatchAsyncFuture(Request var1, AsyncResult var2, FutureResponse var3) {
      if (JMSDebug.JMSDispatcher.isDebugEnabled()) {
         JMSDebug.JMSDispatcher.debug("DispatcherImpl.dispatchAsyncFuture() : " + var1);
      }

      var1.setAsyncResult(var2);
      var1.setFutureResponse(var3);
      dispatchAsyncInternal(var1, (Transaction)null, false);
   }

   public void dispatchAsyncTranFuture(Request var1, AsyncResult var2) {
      throw new AssertionError("compiler error");
   }

   public void dispatchAsyncTranFuture(Request var1, AsyncResult var2, FutureResponse var3) {
      if (JMSDebug.JMSDispatcher.isDebugEnabled()) {
         JMSDebug.JMSDispatcher.debug("DispatcherImpl.dispatchAsyncFuture() : " + var1);
      }

      var1.setTranInfo(2);
      var1.setAsyncResult(var2);
      var1.setFutureResponse(var3);
      dispatchAsyncInternal(var1, (Transaction)null, false);
   }

   public void dispatchOneWayWithId(Request var1, int var2) {
      Transaction var3 = tm.forceSuspend();

      try {
         dispatchAsyncInternal(var1, var3, true);
      } finally {
         cautiousResume(var1, var3);
      }

   }

   public void dispatchOneWay(Request var1) {
      Transaction var2 = tm.forceSuspend();

      try {
         dispatchAsyncInternal(var1, var2, true);
      } finally {
         cautiousResume(var1, var2);
      }

   }

   public DispatcherPeerGoneListener addDispatcherPeerGoneListener(DispatcherPeerGoneListener var1) {
      return null;
   }

   public void removeDispatcherPeerGoneListener(DispatcherPeerGoneListener var1) {
   }

   public final int hashCode() {
      return this.dispatcherId.hashCode() ^ this.name.hashCode();
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof DispatcherImpl)) {
         return false;
      } else {
         DispatcherImpl var2 = (DispatcherImpl)var1;
         return this == var2 || (this.dispatcherId == var2.getId() || this.dispatcherId.equals(var2.getId())) && (this.name == var2.getName() || this.name.equals(var2.getName()));
      }
   }

   public Object interopWriteReplace(PeerInfo var1) throws IOException {
      if (FASTDISPATCH && var1.compareTo(PeerInfo.VERSION_DIABLO) >= 0 && !KernelStatus.isApplet()) {
         if (this.fastDispatcher == null) {
            this.fastDispatcher = new FastDispatcherImpl(this.name, this.dispatcherId, this);
         }

         return this.fastDispatcher;
      } else {
         return this;
      }
   }

   static boolean fastDispatchEnabled() {
      String var0 = "JMSFastDispatchEnabled";

      try {
         String var1 = System.getProperty(var0);
         boolean var2 = var1 == null || !var1.equalsIgnoreCase("false");
         if (var1 != null) {
            System.out.println("\n\n *** -D" + var0 + "=" + var2 + " *** \n\n");
         }

         return var2;
      } catch (Throwable var3) {
         (new RuntimeException("error processing " + var0)).printStackTrace();
         return true;
      }
   }
}
