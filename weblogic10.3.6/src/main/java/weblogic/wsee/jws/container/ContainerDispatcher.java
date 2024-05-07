package weblogic.wsee.jws.container;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import weblogic.wsee.message.WlMessageContext;

public abstract class ContainerDispatcher {
   public static final String JWSCONTAINER_PROPERTY = "weblogic.wsee.jws.containerDispatcher";
   private static final String DEFAULT_JWSCONTAINER = ContainerDispatcherImpl.class.getName();
   private static ContainerDispatcher instance = load();

   public static ContainerDispatcher getInstance() {
      return instance;
   }

   private static ContainerDispatcher newInstance(String var0, ClassLoader var1) throws Exception {
      Class var2;
      if (var1 == null) {
         var2 = Class.forName(var0);
      } else {
         var2 = var1.loadClass(var0);
      }

      return (ContainerDispatcher)var2.newInstance();
   }

   private static ContainerDispatcher load() {
      try {
         ClassLoader var0 = Thread.currentThread().getContextClassLoader();
         String var1 = System.getProperty("weblogic.wsee.jws.containerDispatcher");
         if (var1 != null) {
            return newInstance(var1, var0);
         } else {
            String var2 = "META-INF/services/" + "weblogic.wsee.jws.containerDispatcher";
            InputStream var4 = null;
            if (var0 == null) {
               var4 = ClassLoader.getSystemResourceAsStream(var2);
            } else {
               var4 = var0.getResourceAsStream(var2);
            }

            if (var4 != null) {
               BufferedReader var5 = new BufferedReader(new InputStreamReader(var4, "UTF-8"));
               String var3 = var5.readLine();
               var5.close();
               if (var3 != null && !"".equals(var3)) {
                  return newInstance(var3, var0);
               }
            }

            return newInstance(DEFAULT_JWSCONTAINER, var0);
         }
      } catch (Exception var6) {
         throw new InstantiationError(var6.getMessage());
      }
   }

   public abstract Response dispatch(WlMessageContext var1, Request var2) throws Exception;
}
