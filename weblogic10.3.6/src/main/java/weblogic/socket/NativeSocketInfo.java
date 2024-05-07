package weblogic.socket;

import java.io.IOException;
import java.net.Socket;
import weblogic.kernel.Kernel;
import weblogic.platform.VM;

class NativeSocketInfo extends SocketInfo {
   private static VM vm;
   protected final int fd = this.initFD();

   public final int getFD() {
      return this.fd;
   }

   NativeSocketInfo(MuxableSocket var1) throws IOException {
      super(var1);
      if (Kernel.DEBUG && Kernel.getDebug().getDebugMuxer()) {
         SocketLogger.logDebug("Obtained fd=" + this.fd + " for: sock=" + var1.getSocket());
      }

   }

   protected String fieldsToString() {
      StringBuffer var1 = new StringBuffer(200);
      var1.append(super.fieldsToString()).append(", ").append("fd = ").append(this.fd);
      return var1.toString();
   }

   private static synchronized void initVM() {
      if (vm == null) {
         vm = VM.getVM();
      }
   }

   protected int initFD() throws IOException {
      try {
         Socket var1 = this.ms.getSocket();
         if (var1 instanceof WeblogicSocket) {
            var1 = ((WeblogicSocket)var1).getSocket();
         }

         if (vm == null) {
            initVM();
         }

         return vm.getFD(var1);
      } catch (IOException var2) {
         if (Kernel.DEBUG && Kernel.getDebug().getDebugMuxer()) {
            SocketLogger.logDebug(var2.getMessage());
         }

         throw var2;
      }
   }
}
