package weblogic.jms.dotnet.transport.t3client;

import weblogic.jms.dotnet.transport.ReceivedOneWay;
import weblogic.jms.dotnet.transport.ServiceOneWay;
import weblogic.jms.dotnet.transport.TransportError;

class ShutdownListener implements ServiceOneWay {
   private final T3Connection t3conn;

   ShutdownListener(T3Connection var1) {
      this.t3conn = var1;
   }

   public void invoke(ReceivedOneWay var1) {
   }

   public void onPeerGone(TransportError var1) {
      this.t3conn.close();
   }

   public void onShutdown() {
      this.t3conn.close();
   }

   public void onUnregister() {
      this.t3conn.close();
   }
}
