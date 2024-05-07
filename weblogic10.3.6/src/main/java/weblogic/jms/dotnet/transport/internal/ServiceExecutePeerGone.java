package weblogic.jms.dotnet.transport.internal;

import weblogic.jms.dotnet.transport.TransportError;

class ServiceExecutePeerGone extends ServiceExecute {
   final TransportError transportError;

   ServiceExecutePeerGone(ServiceWrapper var1, TransportError var2) {
      super(var1, ServiceExecute.State.PEERGONE);
      this.transportError = var2;
   }

   public void execute() {
      this.wrapper.getService().onPeerGone(this.transportError);
   }

   public String toString() {
      return this.toString(this.transportError);
   }
}
