package weblogic.ejb.spi;

import java.lang.reflect.Method;

public class MethodUtils {
   public static String getWSOPreInvokeMethodName(Method var0) {
      return weblogic.ejb.container.utils.MethodUtils.getWSOPreInvokeMethodName(var0);
   }

   public static String getWSOBusinessMethodName(Method var0) {
      return weblogic.ejb.container.utils.MethodUtils.getWSOBusinessMethodName(var0);
   }

   public static String getWSOPostInvokeMethodName() {
      return weblogic.ejb.container.utils.MethodUtils.getWSOPostInvokeMethodName();
   }

   public static String getWSOPreInvokeMethodDeclaration(Method var0) {
      return weblogic.ejb.container.utils.MethodUtils.getWSOPreInvokeMethodDeclaration(var0);
   }

   public static String getWSOBusinessMethodDeclaration(Method var0) {
      return weblogic.ejb.container.utils.MethodUtils.getWSOBusinessMethodDeclaration(var0);
   }

   public static String getWSOPostInvokeMethodDeclaration() {
      return weblogic.ejb.container.utils.MethodUtils.getWSOPostInvokeMethodDeclaration();
   }
}
