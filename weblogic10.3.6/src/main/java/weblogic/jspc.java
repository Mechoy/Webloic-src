package weblogic;

import java.lang.reflect.Constructor;
import weblogic.utils.compiler.Tool;

public abstract class jspc {
   private static Tool makeJspc(String[] var0) throws Exception {
      Class var1 = Class.forName("weblogic.servlet.jsp.jspc20");
      Constructor var2 = var1.getConstructor(String[].class);
      return (Tool)var2.newInstance(var0);
   }

   public static void main(String[] var0) throws Exception {
      Tool var1 = makeJspc(var0);
      var1.run();
   }
}
