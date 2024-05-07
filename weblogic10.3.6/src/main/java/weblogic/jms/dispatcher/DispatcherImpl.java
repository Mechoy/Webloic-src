package weblogic.jms.dispatcher;

import java.rmi.RemoteException;
import javax.jms.JMSException;
import weblogic.rmi.extensions.AsyncResult;
import weblogic.rmi.extensions.server.FutureResponse;
import weblogic.rmi.spi.MsgOutput;

public final class DispatcherImpl implements DispatcherRemote, DispatcherOneWay {
   static final DispatcherImpl THE_ONE = new DispatcherImpl();
   private final weblogic.messaging.dispatcher.DispatcherImpl delegate = (weblogic.messaging.dispatcher.DispatcherImpl)JMSDispatcherManager.getLocalDispatcher().getDelegate();

   DispatcherImpl() {
   }

   public void dispatchAsyncFuture(Request var1, AsyncResult var2) throws RemoteException {
      throw new AssertionError();
   }

   public void dispatchAsyncFuture(Request var1, AsyncResult var2, FutureResponse var3) throws RemoteException {
      throw new AssertionError();
   }

   public void dispatchAsyncTranFuture(Request var1, AsyncResult var2) throws RemoteException {
      throw new AssertionError();
   }

   public void dispatchAsyncTranFuture(Request var1, AsyncResult var2, FutureResponse var3) throws RemoteException {
      throw new AssertionError();
   }

   public Response dispatchSyncFuture(Request var1) throws JMSException, RemoteException {
      throw new AssertionError("compiler error");
   }

   public void dispatchSyncFuture(Request var1, FutureResponse var2) throws JMSException, RemoteException {
      this.delegate.dispatchSyncFuture(var1, new InteropFutureWrapper(var2));
   }

   public Response dispatchSyncNoTranFuture(Request var1) throws JMSException, RemoteException {
      throw new AssertionError("compiler error");
   }

   public void dispatchSyncNoTranFuture(Request var1, FutureResponse var2) throws JMSException, RemoteException {
      this.delegate.dispatchSyncNoTranFuture(var1, new InteropFutureWrapper(var2));
   }

   public Response dispatchSyncTranFuture(Request var1) throws RemoteException, JMSException, DispatcherException {
      throw new AssertionError("compiler error");
   }

   public void dispatchSyncTranFuture(Request var1, FutureResponse var2) throws RemoteException, JMSException, DispatcherException {
      this.delegate.dispatchSyncTranFuture(var1, new InteropFutureWrapper(var2));
   }

   public void dispatchOneWay(Request var1) throws RemoteException {
      this.delegate.dispatchOneWay(var1);
   }

   private static class InteropFutureWrapper implements FutureResponse {
      private final FutureResponse delegate;

      private InteropFutureWrapper(FutureResponse var1) {
         this.delegate = var1;
      }

      public MsgOutput getMsgOutput() throws RemoteException {
         return this.delegate.getMsgOutput();
      }

      public void send() throws RemoteException {
         this.delegate.send();
      }

      public void sendThrowable(Throwable var1) {
         if (var1 instanceof weblogic.messaging.dispatcher.DispatcherException) {
            var1 = var1.getCause();
         }

         this.delegate.sendThrowable(var1);
      }

      // $FF: synthetic method
      InteropFutureWrapper(FutureResponse var1, Object var2) {
         this(var1);
      }
   }
}
