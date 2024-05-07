package weblogic.socket;

import java.io.IOException;

final class DevPollSocketInfo extends NativeSocketInfo {
   protected String fieldsToString() {
      StringBuffer var1 = new StringBuffer(200);
      var1.append(super.fieldsToString()).append(", ").append("fd = ").append(this.fd);
      return var1.toString();
   }

   DevPollSocketInfo(MuxableSocket var1) throws IOException {
      super(var1);
   }
}
