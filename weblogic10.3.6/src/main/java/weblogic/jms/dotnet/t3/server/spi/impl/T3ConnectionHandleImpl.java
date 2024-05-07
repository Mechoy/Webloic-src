package weblogic.jms.dotnet.t3.server.spi.impl;

import weblogic.jms.dotnet.t3.server.spi.T3Connection;
import weblogic.jms.dotnet.t3.server.spi.T3ConnectionGoneEvent;
import weblogic.jms.dotnet.t3.server.spi.T3ConnectionHandle;
import weblogic.utils.io.ChunkedDataInputStream;
import weblogic.utils.io.ChunkedDataOutputStream;

public class T3ConnectionHandleImpl implements T3ConnectionHandle {
   private T3Connection client;

   T3ConnectionHandleImpl(T3Connection var1) {
      this.client = var1;
   }

   public void onPeerGone(T3ConnectionGoneEvent var1) {
   }

   public void onMessage(ChunkedDataInputStream var1) {
      try {
         int var2 = var1.readInt();
         ChunkedDataOutputStream var3 = this.client.getRequestStream();
         var3.writeInt(var2 + 1);
         this.client.send(var3);
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   public void close() {
   }
}
