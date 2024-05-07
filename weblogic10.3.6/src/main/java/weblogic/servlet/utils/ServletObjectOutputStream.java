package weblogic.servlet.utils;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import weblogic.servlet.internal.session.SessionObjectReplacer;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.collections.SlabPool;
import weblogic.utils.io.UnsyncByteArrayOutputStream;

public final class ServletObjectOutputStream extends ObjectOutputStream {
   private static final SlabPool pool = new SlabPool(15, 3);
   private static final SessionObjectReplacer replacer = SessionObjectReplacer.getInstance();
   private final UnsyncByteArrayOutputStream baos;

   private ServletObjectOutputStream() throws IOException {
      this(new UnsyncByteArrayOutputStream());
   }

   private ServletObjectOutputStream(UnsyncByteArrayOutputStream var1) throws IOException {
      super(var1);
      this.baos = var1;
      this.enableReplaceObject(true);
   }

   public Object replaceObject(Object var1) throws IOException {
      return replacer.replaceObject(var1);
   }

   public void writeClassDescriptor(ObjectStreamClass var1) throws IOException {
      ClassLoader var2 = var1.forClass().getClassLoader();
      this.writeUTF(var1.getName());
      this.writeLong(var1.getSerialVersionUID());
      String var3 = "";
      if (var2 instanceof GenericClassLoader) {
         GenericClassLoader var4 = (GenericClassLoader)var2;
         var3 = var4.getAnnotation().getAnnotationString();
      }

      this.writeUTF(var3);
   }

   public void annotateProxyClass(Class var1) throws IOException {
      String var2 = "";
      Class[] var3 = var1.getInterfaces();

      for(int var4 = 0; var4 < var3.length; ++var4) {
         if (var3[var4].getClassLoader() instanceof GenericClassLoader) {
            GenericClassLoader var5 = (GenericClassLoader)var3[var4].getClassLoader();
            var2 = var5.getAnnotation().getAnnotationString();
            break;
         }
      }

      this.writeUTF(var2);
   }

   public void reset() throws IOException {
      this.baos.reset();
   }

   public byte[] toByteArray() {
      return this.baos.toRawBytes();
   }

   private UnsyncByteArrayOutputStream getUnderlyingByteStream() {
      return this.baos;
   }

   public static ServletObjectOutputStream getOutputStream() throws IOException {
      UnsyncByteArrayOutputStream var0 = (UnsyncByteArrayOutputStream)pool.remove();
      if (var0 == null) {
         var0 = new UnsyncByteArrayOutputStream();
      }

      return new ServletObjectOutputStream(var0);
   }

   public static void releaseOutputStream(ServletObjectOutputStream var0) throws IOException {
      var0.reset();
      pool.add(var0.getUnderlyingByteStream());
   }
}
