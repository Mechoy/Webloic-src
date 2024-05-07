package weblogic.wsee.deploy;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import javax.jws.WebMethod;
import weblogic.utils.reflect.ReflectUtils;

public class DeployUtil {
   private DeployUtil() {
   }

   public static List getWebServiceMethods(Class var0, Class var1) {
      Enumeration var2;
      if (var1 == null) {
         var2 = ReflectUtils.distinctMethods(var0);
      } else {
         var2 = ReflectUtils.distinctInterfaceMethods(var1);
      }

      ArrayList var3 = new ArrayList();

      while(var2.hasMoreElements()) {
         Method var4 = (Method)var2.nextElement();
         if (!Modifier.isVolatile(var4.getModifiers())) {
            var3.add(var4);
         }
      }

      return filterWebMethods(var3);
   }

   private static List filterWebMethods(List var0) {
      ArrayList var1 = new ArrayList();
      Iterator var2 = var0.iterator();

      while(var2.hasNext()) {
         Method var3 = (Method)var2.next();
         WebMethod var4 = (WebMethod)var3.getAnnotation(WebMethod.class);
         boolean var5 = var4 == null && Modifier.isPublic(var3.getModifiers()) && !var3.getDeclaringClass().equals(Object.class) || var4 != null && !var4.exclude();
         if (var5) {
            var1.add(var3);
         }
      }

      return var1;
   }
}
