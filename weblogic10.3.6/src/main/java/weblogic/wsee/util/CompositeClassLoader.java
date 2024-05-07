package weblogic.wsee.util;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

class CompositeClassLoader extends ClassLoader {
   List<ClassLoader> loaders = new ArrayList();

   public CompositeClassLoader(List<ClassLoader> var1) {
      assert var1 != null : "No loaders specified";

      this.loaders = var1;
   }

   protected Class<?> findClass(String var1) throws ClassNotFoundException {
      Iterator var2 = this.loaders.iterator();

      Class var4;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         ClassLoader var3 = (ClassLoader)var2.next();
         var4 = var3.loadClass(var1);
      } while(var4 == null);

      return var4;
   }

   protected URL findResource(String var1) {
      Iterator var2 = this.loaders.iterator();

      URL var4;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         ClassLoader var3 = (ClassLoader)var2.next();
         var4 = var3.getResource(var1);
      } while(var4 == null);

      return var4;
   }

   protected Enumeration<URL> findResources(String var1) throws IOException {
      ArrayList var2 = new ArrayList();
      Iterator var3 = this.loaders.iterator();

      while(var3.hasNext()) {
         ClassLoader var4 = (ClassLoader)var3.next();
         Enumeration var5 = var4.getResources(var1);

         while(var5.hasMoreElements()) {
            var2.add(var5.nextElement());
         }
      }

      return Collections.enumeration(var2);
   }
}
