package weblogic.jms.adapter51;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashSet;
import java.util.Set;

public class IsolatingClassLoader extends URLClassLoader {
   private String m_name;
   private String[] m_isolatedPrefixes;
   private Set m_isolatedClassNames = new HashSet();

   public IsolatingClassLoader(String var1, URL[] var2, String[] var3, boolean var4) throws InvalidContextClassLoaderException {
      super(var2);
      this.init(var1, var3, var4);
   }

   public IsolatingClassLoader(String var1, URL[] var2, ClassLoader var3, String[] var4, boolean var5) throws InvalidContextClassLoaderException {
      super(var2, var3);
      this.init(var1, var4, var5);
   }

   private void init(String var1, String[] var2, boolean var3) throws InvalidContextClassLoaderException {
      this.m_name = var1;
      HashSet var4 = new HashSet();

      for(int var5 = 0; var5 < var2.length; ++var5) {
         int var6 = var2[var5].indexOf(42);
         if (var6 >= 0) {
            var4.add(var2[var5].substring(0, var6));
         } else {
            this.m_isolatedClassNames.add(var2[var5]);
         }
      }

      this.m_isolatedPrefixes = (String[])((String[])var4.toArray(new String[0]));
      if (var3) {
         ClassLoader var8 = Thread.currentThread().getContextClassLoader();
         if (!(var8 instanceof URLClassLoader)) {
            throw new InvalidContextClassLoaderException("Caller classloader is not a URLClassLoader, can't automatically augument classpath.Its a " + var8.getClass());
         }

         URL[] var9 = ((URLClassLoader)var8).getURLs();

         for(int var7 = 0; var7 < var9.length; ++var7) {
            this.addURL(var9[var7]);
         }
      }

   }

   protected synchronized Class loadClass(String var1, boolean var2) throws ClassNotFoundException {
      boolean var3 = this.m_isolatedClassNames.contains(var1);
      if (!var3) {
         for(int var4 = 0; var4 < this.m_isolatedPrefixes.length; ++var4) {
            if (var1.startsWith(this.m_isolatedPrefixes[var4])) {
               var3 = true;
               break;
            }
         }
      }

      if (var3) {
         Class var5 = this.findLoadedClass(var1);
         if (var5 == null) {
            var5 = this.findClass(var1);
         }

         if (var2) {
            this.resolveClass(var5);
         }

         return var5;
      } else {
         return super.loadClass(var1, var2);
      }
   }

   public static class InvalidContextClassLoaderException extends Exception {
      public InvalidContextClassLoaderException(String var1) {
         super(var1);
      }
   }
}
