package weblogic.wsee.jws.conversation;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.nio.ByteBuffer;
import weblogic.common.internal.ProxyClassResolver;
import weblogic.utils.io.ByteBufferDataInputStream;

public class PersistentStoreObjectInputStream extends ByteBufferDataInputStream implements ObjectInput {
   private ObjectInputStream ois;

   public PersistentStoreObjectInputStream(ByteBuffer[] var1) {
      super(var1);
   }

   public Object readObject() throws ClassNotFoundException, IOException {
      if (this.ois == null) {
         this.ois = new ContextObjectInputStream(this);
      }

      return this.ois.readObject();
   }

   private static class ContextObjectInputStream extends ObjectInputStream {
      public ContextObjectInputStream(InputStream var1) throws IOException {
         super(var1);
         this.enableResolveObject(true);
      }

      public Class resolveClass(ObjectStreamClass var1) throws IOException, ClassNotFoundException {
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
         } else {
            return "char".equals(var2) ? Character.TYPE : Class.forName(var1.getName(), true, Thread.currentThread().getContextClassLoader());
         }
      }

      protected Class resolveProxyClass(String[] var1) throws IOException, ClassNotFoundException {
         return ProxyClassResolver.resolveProxyClass(var1);
      }
   }
}
