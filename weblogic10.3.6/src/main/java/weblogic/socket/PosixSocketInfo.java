package weblogic.socket;

import java.io.IOException;

final class PosixSocketInfo extends NativeSocketInfo {
   static final int FD_NOT_READY = 0;
   static final int FD_READY = 1;
   static final int FD_ERROR = 2;
   static final int FD_CLIENT_ERROR = 3;
   FdStruct fdStruct;

   protected String fieldsToString() {
      StringBuffer var1 = new StringBuffer(200);
      var1.append(super.fieldsToString()).append(", ").append("fdStruct = ").append(this.fdStruct);
      return var1.toString();
   }

   PosixSocketInfo(MuxableSocket var1) throws IOException {
      super(var1);
      this.fdStruct = new FdStruct(this.fd, this);
   }

   static class FdStruct {
      int fd;
      int status = 0;
      int revents = 0;
      PosixSocketInfo info;

      FdStruct(int var1, PosixSocketInfo var2) {
         this.fd = var1;
         this.info = var2;
      }

      public String toString() {
         return "PosixSocketInfo.FdStruct[fd=" + this.fd + ", status=" + this.status + "]";
      }
   }
}
