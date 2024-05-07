package weblogic.servlet.proxy;

import java.io.IOException;
import java.net.Socket;
import weblogic.utils.Debug;

final class ServerFactory {
   private static final boolean DEBUG = true;
   private final int jvmidHash;
   private final int port;
   private final String host;
   private final Socket socket;

   ServerFactory(int var1) {
      this(var1, (String)null, -1);
   }

   ServerFactory(int var1, String var2, int var3) {
      this.jvmidHash = var1;
      this.host = var2;
      this.port = var3;

      try {
         this.socket = new Socket(var2, var3);
         Debug.say("Created socket to " + var2 + ":" + var3 + "\t" + this.socket);
      } catch (IOException var5) {
         var5.printStackTrace();
         throw new AssertionError("Failed to initialize socket" + var5);
      }
   }

   public int hashCode() {
      return this.jvmidHash;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 != null && var1 instanceof ServerFactory) {
         ServerFactory var2 = (ServerFactory)var1;
         return var2.jvmidHash == this.jvmidHash;
      } else {
         return false;
      }
   }

   Socket getConnection() {
      return this.socket;
   }

   void releaseConnection(Socket var1) {
   }

   void cleanup() {
      try {
         if (this.socket != null) {
            this.socket.close();
         }
      } catch (IOException var2) {
      }

   }
}
