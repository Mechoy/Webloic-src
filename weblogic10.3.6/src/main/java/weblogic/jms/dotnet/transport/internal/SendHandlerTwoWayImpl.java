package weblogic.jms.dotnet.transport.internal;

import weblogic.jms.dotnet.transport.MarshalReadable;
import weblogic.jms.dotnet.transport.MarshalWritable;
import weblogic.jms.dotnet.transport.ReceivedOneWay;
import weblogic.jms.dotnet.transport.SendHandlerTwoWay;
import weblogic.jms.dotnet.transport.ServiceOneWay;
import weblogic.jms.dotnet.transport.TransportError;
import weblogic.jms.dotnet.transport.TransportExecutable;
import weblogic.jms.dotnet.transport.TwoWayResponseListener;

public class SendHandlerTwoWayImpl extends SendHandlerOneWayImpl implements SendHandlerTwoWay, ServiceOneWay, TransportExecutable {
   private ResponseLock lock = new ResponseLock();
   private long responseId;
   private long responseOrderingId;
   private int blockingCount;
   private TwoWayResponseListener responseListener;
   private ReceivedOneWayImpl response;
   private static final TransportError CANCEL_ERROR = new TransportError("Canceled", false);
   private static final TransportError SHUTDOWN_ERROR = new TransportError("Shutdown", false);

   SendHandlerTwoWayImpl(TransportImpl var1, long var2, long var4, long var6) {
      super(var1, var2, var4);
      this.responseId = var1.allocateServiceID();
      this.responseOrderingId = var6;
   }

   private void scheduleMe() {
      this.getTransport().getDefaultThreadPool().schedule(this, this.responseOrderingId);
   }

   long getResponseID() {
      return this.responseId;
   }

   long getResponseOrderingID() {
      return this.responseOrderingId;
   }

   public void send(MarshalWritable var1) {
      this.send(var1, (TwoWayResponseListener)null);
   }

   public void send(MarshalWritable var1, TwoWayResponseListener var2) {
      this.checkAlreadySent();
      synchronized(this.lock) {
         this.responseListener = var2;
         if (this.response != null) {
            if (this.responseListener != null) {
               this.scheduleMe();
            }

            return;
         }

         this.getTransport().registerService(this.responseId, this, ThreadPoolWrapper.DIRECT);
         if (this.response != null) {
            this.getTransport().unregisterService(this.responseId, (TransportError)((TransportError)this.response.getRequest()));
            return;
         }
      }

      this.getTransport().sendInternalTwoWay(this, var1);
   }

   public MarshalReadable getResponse(boolean var1) {
      synchronized(this.lock) {
         if (this.response == null && var1) {
            ++this.blockingCount;

            try {
               this.lock.wait();
            } catch (InterruptedException var5) {
               this.setResponse((MarshalReadable)(new TransportError(var5)));
            }

            --this.blockingCount;
         }

         if (this.blockingCount > 0) {
            this.lock.notify();
         }

         return this.response.getRequest();
      }
   }

   public void cancel(TransportError var1) {
      if (var1 == null) {
         var1 = CANCEL_ERROR;
      }

      synchronized(this.lock) {
         if (this.response == null) {
            this.setResponse((MarshalReadable)var1);
            this.getTransport().unregisterService(this.responseId, var1);
         }

      }
   }

   public void execute() {
      TwoWayResponseListener var1;
      ReceivedOneWayImpl var2;
      synchronized(this.lock) {
         var1 = this.responseListener;
         var2 = this.response;
      }

      var1.onResponse(var2);
   }

   private void setResponse(MarshalReadable var1) {
      this.setResponse(new ReceivedOneWayImpl(this.getTransport(), this.getRemoteServiceID(), var1));
   }

   private void setResponse(ReceivedOneWayImpl var1) {
      TwoWayResponseListener var2;
      synchronized(this.lock) {
         if (this.response != null) {
            return;
         }

         this.response = var1;
         if (this.blockingCount > 0) {
            this.lock.notify();
         }

         var2 = this.responseListener;
      }

      if (var2 != null) {
         this.scheduleMe();
      }

      this.getTransport().unregisterServiceSilent(this.responseId);
   }

   public void invoke(ReceivedOneWay var1) {
      this.setResponse((ReceivedOneWayImpl)var1);
   }

   public void onPeerGone(TransportError var1) {
      this.setResponse((MarshalReadable)var1);
   }

   public void onShutdown() {
      this.setResponse((MarshalReadable)SHUTDOWN_ERROR);
   }

   public void onUnregister() {
      this.setResponse((MarshalReadable)CANCEL_ERROR);
   }
}
