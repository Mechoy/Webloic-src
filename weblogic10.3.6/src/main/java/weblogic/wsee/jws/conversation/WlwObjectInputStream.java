package weblogic.wsee.jws.conversation;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.io.StreamCorruptedException;
import java.lang.reflect.Proxy;

public class WlwObjectInputStream extends ObjectInputStream {
   private ClassLoader _classLoader;

   public WlwObjectInputStream(InputStream var1, ClassLoader var2) throws IOException, StreamCorruptedException {
      super(var1);
      this._classLoader = var2;
   }

   protected Class resolveClass(ObjectStreamClass var1) throws IOException, ClassNotFoundException {
      String var2 = var1.getName();
      if ("int".equals(var2)) {
         return Integer.TYPE;
      } else if ("double".equals(var2)) {
         return Double.TYPE;
      } else if ("boolean".equals(var2)) {
         return Boolean.TYPE;
      } else if ("float".equals(var2)) {
         return Float.TYPE;
      } else if ("byte".equals(var2)) {
         return Byte.TYPE;
      } else if ("long".equals(var2)) {
         return Long.TYPE;
      } else if ("short".equals(var2)) {
         return Short.TYPE;
      } else if ("char".equals(var2)) {
         return Character.TYPE;
      } else {
         ClassLoader var3 = null;
         var3 = this._classLoader;
         return Class.forName(var1.getName(), false, var3);
      }
   }

   protected Class resolveProxyClass(String[] var1) throws IOException, ClassNotFoundException {
      Class[] var2 = new Class[var1.length];
      ClassLoader var3 = null;
      var3 = this._classLoader;

      for(int var4 = 0; var4 < var1.length; ++var4) {
         var2[var4] = Class.forName(var1[var4], false, var3);
      }

      return Proxy.getProxyClass(var3, var2);
   }
}
