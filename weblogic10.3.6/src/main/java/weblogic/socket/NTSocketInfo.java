package weblogic.socket;

import java.io.IOException;

final class NTSocketInfo extends NativeSocketInfo {
   protected int nativeIndex = NTSocketMuxer.add(this);

   NTSocketInfo(MuxableSocket var1) throws IOException {
      super(var1);
   }

   protected String fieldsToString() {
      StringBuffer var1 = new StringBuffer(200);
      var1.append(super.fieldsToString()).append(", ").append("nativeIndex = ").append(this.nativeIndex);
      return var1.toString();
   }

   protected void cleanup() {
      super.cleanup();
      NTSocketMuxer.remove(this.nativeIndex);
   }
}
