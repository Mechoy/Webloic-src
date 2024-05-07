package weblogic.jms.dotnet.transport.internal;

import weblogic.jms.dotnet.transport.Service;
import weblogic.jms.dotnet.transport.ServiceTwoWay;
import weblogic.jms.dotnet.transport.TransportError;

class ServiceExecuteTwoWay extends ServiceExecute {
   final ReceivedTwoWayImpl receivedTwoWay;

   ServiceExecuteTwoWay(ServiceWrapper var1, ReceivedTwoWayImpl var2) {
      super(var1, ServiceExecute.State.INVOKETWOWAY);
      this.receivedTwoWay = var2;
   }

   public void execute() {
      Service var1 = this.wrapper.getService();

      try {
         ((ServiceTwoWay)var1).invoke(this.receivedTwoWay);
      } catch (Throwable var5) {
         Throwable var2 = var5;

         try {
            if (!this.receivedTwoWay.isAlreadySent()) {
               this.receivedTwoWay.send(new TransportError(var2));
            }
         } catch (Throwable var4) {
         }
      }

   }

   public String toString() {
      return this.toString(this.receivedTwoWay);
   }
}
