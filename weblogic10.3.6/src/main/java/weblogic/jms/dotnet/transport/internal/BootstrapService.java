package weblogic.jms.dotnet.transport.internal;

import weblogic.jms.dotnet.transport.ReceivedTwoWay;
import weblogic.jms.dotnet.transport.ServiceTwoWay;
import weblogic.jms.dotnet.transport.TransportError;

class BootstrapService implements ServiceTwoWay {
   public void onPeerGone(TransportError var1) {
   }

   public void onShutdown() {
   }

   public void onUnregister() {
   }

   public void invoke(ReceivedTwoWay var1) {
      try {
         BootstrapRequest var2 = (BootstrapRequest)var1.getRequest();
         Class var3 = Class.forName(var2.getBootstrapServiceClassName());
         ServiceTwoWay var4 = (ServiceTwoWay)var3.newInstance();
         ((TransportImpl)var1.getTransport()).startHeartbeatService(var2);
         var4.invoke(var1);
      } catch (Throwable var5) {
         var1.send(new TransportError(var5));
      }

   }
}
