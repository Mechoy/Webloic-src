package weblogic.servlet.utils;

import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import weblogic.j2ee.ApplicationManager;
import weblogic.servlet.internal.session.SessionObjectReplacer;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.io.UnsyncByteArrayInputStream;

public final class ServletObjectInputStream extends ObjectInputStream {
   private static final boolean DEBUG = false;
   private static final SessionObjectReplacer replacer = SessionObjectReplacer.getInstance();
   private Class lastClass;

   private ServletObjectInputStream(UnsyncByteArrayInputStream var1) throws IOException {
      super(var1);
      this.enableResolveObject(true);
   }

   public Object resolveObject(Object var1) throws IOException {
      return replacer.resolveObject(var1);
   }

   protected Class resolveClass(ObjectStreamClass var1) {
      return this.lastClass;
   }

   protected Class resolveProxyClass(String[] var1) throws IOException, ClassNotFoundException {
      String var2 = this.readUTF();
      ClassLoader var3 = null;
      ArrayList var4 = new ArrayList();

      for(int var5 = 0; var5 < var1.length; ++var5) {
         try {
            Class var6 = ApplicationManager.loadClass(var1[var5], var2);
            var4.add(var6);
            ClassLoader var7 = var6.getClassLoader();
            if (var3 != null && var7 instanceof GenericClassLoader) {
               var3 = var7;
            }
         } catch (ClassNotFoundException var8) {
         }
      }

      Class[] var9 = (Class[])((Class[])var4.toArray(new Class[var4.size()]));
      if (var3 == null) {
         var3 = var9[0].getClassLoader();
      }

      return Proxy.getProxyClass(var3, var9);
   }

   protected ObjectStreamClass readClassDescriptor() throws IOException, ClassNotFoundException {
      String var1 = this.readUTF();
      long var2 = this.readLong();
      String var4 = this.readUTF();
      Class var5 = ApplicationManager.loadClass(var1, var4);
      ObjectStreamClass var6 = ObjectStreamClass.lookup(var5);
      if (var6.getSerialVersionUID() != var2) {
         throw new InvalidClassException(var1, "Expected uid: '" + var2 + ", found uid: '" + var6.getSerialVersionUID() + "'");
      } else {
         this.lastClass = var6.forClass();
         return var6;
      }
   }

   public static ServletObjectInputStream getInputStream(byte[] var0) throws IOException {
      return new ServletObjectInputStream(new UnsyncByteArrayInputStream(var0));
   }

   public static void releaseInputStream(ServletObjectInputStream var0) {
   }
}
