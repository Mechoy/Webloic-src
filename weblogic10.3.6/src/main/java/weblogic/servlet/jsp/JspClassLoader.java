package weblogic.servlet.jsp;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import weblogic.utils.classloaders.ClassFinder;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.enumerations.EmptyEnumerator;

final class JspClassLoader extends GenericClassLoader {
   String cname;
   String innerClassPrefix;
   private Map cache = new HashMap(1);

   public JspClassLoader(ClassFinder var1, ClassLoader var2, String var3) {
      super(var1, var2);
      this.cname = var3;
      this.innerClassPrefix = var3 + '$';
   }

   public Class loadClass(String var1) throws ClassNotFoundException {
      return this.findClass(var1);
   }

   public Class findClass(String var1) throws ClassNotFoundException {
      if (!var1.equals(this.cname) && !var1.startsWith(this.innerClassPrefix)) {
         return this.getParent().loadClass(var1);
      } else {
         Class var2 = (Class)this.cache.get(var1);
         if (var2 == null) {
            Thread var3 = Thread.currentThread();
            ClassLoader var4 = var3.getContextClassLoader();

            try {
               var3.setContextClassLoader(this.getParent());
               var2 = super.findClass(var1);
            } finally {
               var3.setContextClassLoader(var4);
            }

            this.cache.put(var1, var2);
         }

         return var2;
      }
   }

   public Enumeration findResources(String var1) {
      return new EmptyEnumerator();
   }
}
