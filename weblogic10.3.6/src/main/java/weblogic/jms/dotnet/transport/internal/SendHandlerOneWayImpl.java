package weblogic.jms.dotnet.transport.internal;

import java.util.concurrent.atomic.AtomicBoolean;
import weblogic.jms.dotnet.transport.MarshalWritable;
import weblogic.jms.dotnet.transport.SendHandlerOneWay;
import weblogic.jms.dotnet.transport.TransportError;

public class SendHandlerOneWayImpl implements SendHandlerOneWay {
   private AtomicBoolean sentFlag = new AtomicBoolean(false);
   private AtomicBoolean canceledFlag = new AtomicBoolean(false);
   private TransportImpl transportImpl;
   private long remoteServiceId;
   private long remoteOrderingId;

   SendHandlerOneWayImpl(TransportImpl var1, long var2, long var4) {
      this.transportImpl = var1;
      this.remoteServiceId = var2;
      this.remoteOrderingId = var4;
   }

   long getRemoteServiceID() {
      return this.remoteServiceId;
   }

   long getRemoteOrderingID() {
      return this.remoteOrderingId;
   }

   void checkAlreadySent() {
      if (this.sentFlag.getAndSet(true)) {
         throw new AssertionError("duplicate send");
      }
   }

   boolean isAlreadySent() {
      return this.sentFlag.get();
   }

   TransportImpl getTransport() {
      return this.transportImpl;
   }

   public void send(MarshalWritable var1) {
      this.checkAlreadySent();
      if (!this.canceledFlag.get()) {
         this.transportImpl.sendInternalOneWay(this, var1);
      }
   }

   public void cancel(TransportError var1) {
      this.canceledFlag.set(true);
   }
}
