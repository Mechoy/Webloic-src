package weblogic.common.internal;

import java.io.IOException;
import weblogic.protocol.ServerChannelManager;
import weblogic.rmi.utils.io.RemoteObjectReplacer;
import weblogic.utils.io.Replacer;
import weblogic.utils.io.UnsyncByteArrayInputStream;
import weblogic.utils.io.UnsyncByteArrayOutputStream;

public final class PassivationUtils {
   private PassivationUtils() {
   }

   public static byte[] toByteArray(Object var0) throws IOException {
      return toByteArray(var0, RemoteObjectReplacer.getReplacer());
   }

   public static byte[] toByteArray(Object var0, Replacer var1) throws IOException {
      UnsyncByteArrayOutputStream var2 = new UnsyncByteArrayOutputStream();
      WLObjectOutputStream var3 = new WLObjectOutputStream(var2);
      var3.setReplacer(var1);
      var3.setServerChannel(ServerChannelManager.findDefaultLocalServerChannel());
      var3.writeObject(var0);
      var3.flush();
      var3.close();
      return var2.toByteArray();
   }

   public static Object toObject(byte[] var0) throws ClassNotFoundException, IOException {
      return toObject(var0, RemoteObjectReplacer.getReplacer());
   }

   public static Object toObject(byte[] var0, Replacer var1) throws ClassNotFoundException, IOException {
      UnsyncByteArrayInputStream var2 = new UnsyncByteArrayInputStream(var0);
      WLObjectInputStream var3 = new WLObjectInputStream(var2);
      var3.setReplacer(var1);
      return var3.readObject();
   }

   public static int sizeOf(Object var0) throws IOException {
      return toByteArray(var0).length;
   }

   public static Object copy(Object var0) throws ClassNotFoundException, IOException {
      return toObject(toByteArray(var0));
   }

   public static boolean isSerializable(Object var0) {
      try {
         toByteArray(var0);
         return true;
      } catch (IOException var2) {
         return false;
      }
   }
}
