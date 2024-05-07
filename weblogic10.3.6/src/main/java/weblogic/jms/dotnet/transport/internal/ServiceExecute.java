package weblogic.jms.dotnet.transport.internal;

import weblogic.jms.dotnet.transport.TransportExecutable;

abstract class ServiceExecute implements TransportExecutable {
   final State state;
   final ServiceWrapper wrapper;

   ServiceExecute(ServiceWrapper var1, State var2) {
      this.wrapper = var1;
      this.state = var2;
   }

   public abstract void execute();

   public String toString(Object var1) {
      return "ServiceExecute:<" + this.state + "," + this.wrapper + "," + var1 + ">";
   }

   public String toString() {
      return this.toString("N/A");
   }

   static enum State {
      INVOKEONEWAY,
      INVOKETWOWAY,
      SHUTDOWN,
      UNREGISTER,
      PEERGONE,
      CANCEL;
   }
}
