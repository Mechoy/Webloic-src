package weblogic.jms.dispatcher;

import java.rmi.Remote;
import java.rmi.RemoteException;
import javax.jms.JMSException;
import weblogic.rmi.extensions.AsyncResult;
import weblogic.rmi.extensions.RemoteRuntimeException;
import weblogic.rmi.extensions.server.RemoteWrapper;

public final class DispatcherInteropAdapter implements weblogic.messaging.dispatcher.DispatcherRemote, weblogic.messaging.dispatcher.DispatcherOneWay, RemoteWrapper {
   private final DispatcherRemote dispatcherRemote;
   private final DispatcherOneWay dispatcherOneWay;

   DispatcherInteropAdapter(DispatcherRemote var1, DispatcherOneWay var2) {
      this.dispatcherRemote = var1;
      this.dispatcherOneWay = var2;
   }

   public void dispatchAsyncFuture(weblogic.messaging.dispatcher.Request var1, AsyncResult var2) throws RemoteException {
      this.dispatcherRemote.dispatchAsyncFuture((Request)var1, var2);
   }

   public void dispatchAsyncTranFuture(weblogic.messaging.dispatcher.Request var1, AsyncResult var2) throws RemoteException {
      this.dispatcherRemote.dispatchAsyncTranFuture((Request)var1, var2);
   }

   public weblogic.messaging.dispatcher.Response dispatchSyncFuture(weblogic.messaging.dispatcher.Request var1) throws RemoteException {
      try {
         return this.dispatcherRemote.dispatchSyncFuture((Request)var1);
      } catch (JMSException var3) {
         throw new RemoteRuntimeException(var3);
      }
   }

   public weblogic.messaging.dispatcher.Response dispatchSyncNoTranFuture(weblogic.messaging.dispatcher.Request var1) throws RemoteException {
      try {
         return this.dispatcherRemote.dispatchSyncNoTranFuture((Request)var1);
      } catch (JMSException var3) {
         throw new RemoteRuntimeException(var3);
      }
   }

   public weblogic.messaging.dispatcher.Response dispatchSyncTranFutureWithId(weblogic.messaging.dispatcher.Request var1, int var2) throws RemoteException, weblogic.messaging.dispatcher.DispatcherException {
      return this.dispatchSyncTranFuture(var1);
   }

   public weblogic.messaging.dispatcher.Response dispatchSyncTranFuture(weblogic.messaging.dispatcher.Request var1) throws RemoteException, weblogic.messaging.dispatcher.DispatcherException {
      try {
         return this.dispatcherRemote.dispatchSyncTranFuture((Request)var1);
      } catch (JMSException var3) {
         throw new RemoteRuntimeException(var3);
      } catch (DispatcherException var4) {
         throw new weblogic.messaging.dispatcher.DispatcherException(var4);
      }
   }

   public void dispatchOneWay(weblogic.messaging.dispatcher.Request var1) throws RemoteException {
      this.dispatcherOneWay.dispatchOneWay((Request)var1);
   }

   public void dispatchOneWayWithId(weblogic.messaging.dispatcher.Request var1, int var2) throws RemoteException {
      this.dispatchOneWay(var1);
   }

   public Remote getRemoteDelegate() {
      return this.dispatcherRemote;
   }
}
