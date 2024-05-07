package weblogic.socket;

import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

final class NIOSocketInfo extends SocketInfo {
   private SocketChannel sc;
   private SelectionKey key;
   private int selectorIndex;

   NIOSocketInfo(MuxableSocket var1, int var2) {
      super(var1);
      this.selectorIndex = var2;
   }

   protected String fieldsToString() {
      StringBuffer var1 = new StringBuffer(200);
      var1.append(super.fieldsToString()).append(", ").append("socketChannel = ").append(this.sc);
      return var1.toString();
   }

   SocketChannel getSocketChannel() {
      if (this.sc == null) {
         Socket var1 = this.ms.getSocket();
         this.sc = var1.getChannel();
      }

      return this.sc;
   }

   SelectionKey getSelectionKey() {
      return this.key;
   }

   void setSelectionKey(SelectionKey var1) {
      this.key = var1;
   }

   int getSelectorIndex() {
      return this.selectorIndex;
   }
}
