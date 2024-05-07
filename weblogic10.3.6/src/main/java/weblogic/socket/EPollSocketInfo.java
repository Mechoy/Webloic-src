package weblogic.socket;

import java.io.IOException;

class EPollSocketInfo extends NativeSocketInfo {
   private int index = -1;

   EPollSocketInfo(MuxableSocket var1) throws IOException {
      super(var1);
   }

   int getIndex() {
      return this.index;
   }

   void setIndex(int var1) {
      this.index = var1;
   }

   public void cleanup() {
      if (this.index != -1) {
         EPollSocketMuxer.remove(this.index, this.getFD());
         this.index = -1;
      }

      super.cleanup();
   }
}
