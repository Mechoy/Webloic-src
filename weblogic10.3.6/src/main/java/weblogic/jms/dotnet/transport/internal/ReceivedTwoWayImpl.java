package weblogic.jms.dotnet.transport.internal;

import weblogic.jms.dotnet.transport.MarshalReader;
import weblogic.jms.dotnet.transport.MarshalWritable;
import weblogic.jms.dotnet.transport.ReceivedTwoWay;
import weblogic.jms.dotnet.transport.Transport;
import weblogic.jms.dotnet.transport.TransportError;

public class ReceivedTwoWayImpl extends ReceivedOneWayImpl implements ReceivedTwoWay {
   private SendHandlerOneWayImpl sendHandler;

   ReceivedTwoWayImpl(Transport var1, long var2, MarshalReader var4, SendHandlerOneWayImpl var5) {
      super(var1, var2, var4);
      this.sendHandler = var5;
   }

   public void cancel(TransportError var1) {
      this.sendHandler.send(var1);
   }

   public void send(MarshalWritable var1) {
      this.sendHandler.send(var1);
   }

   boolean isAlreadySent() {
      return this.sendHandler.isAlreadySent();
   }
}
