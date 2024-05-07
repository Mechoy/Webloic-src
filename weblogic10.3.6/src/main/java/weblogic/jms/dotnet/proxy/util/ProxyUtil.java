package weblogic.jms.dotnet.proxy.util;

import java.io.Externalizable;
import java.io.IOException;
import weblogic.jms.dotnet.transport.MarshalRuntimeException;
import weblogic.utils.io.Chunk;
import weblogic.utils.io.ChunkedObjectOutputStream;

public final class ProxyUtil {
   public static void checkVersion(int var0, int var1, int var2) throws MarshalRuntimeException {
      if (var0 < var1 || var0 > var2) {
         throw new MarshalRuntimeException("Unsupported class version {" + var0 + "}.  Expected a value between {" + var1 + "} and {" + var2 + "} inclusive.");
      }
   }

   public static byte[] marshalExternalizable(Externalizable var0) {
      ChunkedObjectOutputStream var1 = null;

      try {
         var1 = new ChunkedObjectOutputStream();
         var0.writeExternal(var1);
      } catch (IOException var6) {
         var6.printStackTrace(System.err);
      }

      byte[] var2 = var1.getBuffer();
      int var3 = var1.getSize();
      byte[] var4 = new byte[var3];
      System.arraycopy(var2, 0, var4, 0, var3);
      Chunk var5 = var1.getChunks();
      if (var5 != null) {
         Chunk.releaseChunks(var5);
      }

      return var4;
   }
}
