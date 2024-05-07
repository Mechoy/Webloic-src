package weblogic.wsee.util.jspgen;

public class GenFactory {
   public static JspGenBase create(String var0, ClassLoader var1) throws ScriptException {
      try {
         Class var2 = var1.loadClass(var0);
         return (JspGenBase)var2.newInstance();
      } catch (ClassNotFoundException var3) {
         throw new ScriptException("unable to find the generated script class:" + var0);
      } catch (IllegalAccessException var4) {
         throw new ScriptException("unable to create the generated script class:" + var0);
      } catch (InstantiationException var5) {
         throw new ScriptException("unable to create the generated script class:" + var0);
      }
   }

   public static JspGenBase create(String var0) throws ScriptException {
      try {
         Class var1 = Class.forName(var0);
         return (JspGenBase)var1.newInstance();
      } catch (ClassNotFoundException var2) {
         throw new ScriptException("unable to find the generated script class:" + var0);
      } catch (InstantiationException var3) {
         throw new ScriptException("unable to create the generated script class:" + var0);
      } catch (IllegalAccessException var4) {
         throw new ScriptException("unable to create the generated script class:" + var0);
      }
   }
}
