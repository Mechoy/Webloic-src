package weblogic.jms.dotnet.transport.internal;

import weblogic.jms.dotnet.transport.Service;
import weblogic.jms.dotnet.transport.ServiceOneWay;

class ServiceExecuteOneWay extends ServiceExecute {
   final ReceivedOneWayImpl receivedOneWay;

   ServiceExecuteOneWay(ServiceWrapper var1, ReceivedOneWayImpl var2) {
      super(var1, ServiceExecute.State.INVOKEONEWAY);
      this.receivedOneWay = var2;
   }

   public void execute() {
      Service var1 = this.wrapper.getService();

      try {
         ((ServiceOneWay)var1).invoke(this.receivedOneWay);
      } catch (Throwable var3) {
      }

   }

   public String toString() {
      return this.toString(this.receivedOneWay);
   }
}
