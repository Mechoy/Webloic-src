package weblogic.deployment;

import java.util.ArrayList;
import java.util.Enumeration;
import weblogic.utils.reflect.ReflectUtils;

public final class DynamicProxyUtils {
   public static Class[] getAllInterfaces(Class var0, Class var1) {
      ArrayList var2 = new ArrayList();
      Enumeration var3 = ReflectUtils.allInterfaces(var0);

      while(var3.hasMoreElements()) {
         var2.add(var3.nextElement());
      }

      var2.remove(var1);
      var2.add(0, var1);
      return (Class[])((Class[])var2.toArray(new Class[var2.size()]));
   }
}
