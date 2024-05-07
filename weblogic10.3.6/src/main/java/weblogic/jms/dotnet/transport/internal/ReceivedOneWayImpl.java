package weblogic.jms.dotnet.transport.internal;

import weblogic.jms.dotnet.transport.MarshalReadable;
import weblogic.jms.dotnet.transport.MarshalReader;
import weblogic.jms.dotnet.transport.ReceivedOneWay;
import weblogic.jms.dotnet.transport.Transport;

public class ReceivedOneWayImpl implements ReceivedOneWay {
   private Transport transport;
   private long serviceId;
   private MarshalReader reader;
   private MarshalReadable req;

   ReceivedOneWayImpl(Transport var1, long var2, MarshalReader var4) {
      this.transport = var1;
      this.serviceId = var2;
      this.reader = var4;
   }

   ReceivedOneWayImpl(Transport var1, long var2, MarshalReadable var4) {
      this.transport = var1;
      this.serviceId = var2;
      this.req = var4;
   }

   public synchronized MarshalReadable getRequest() {
      if (this.reader == null) {
         return this.req;
      } else {
         this.req = this.reader.readMarshalable();
         this.reader.internalClose();
         this.reader = null;
         return this.req;
      }
   }

   public long getServiceId() {
      return this.serviceId;
   }

   public Transport getTransport() {
      return this.transport;
   }
}
