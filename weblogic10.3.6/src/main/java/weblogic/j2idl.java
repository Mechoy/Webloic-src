package weblogic;

import java.lang.reflect.Constructor;
import weblogic.rmi.utils.Utilities;
import weblogic.utils.Getopt2;
import weblogic.utils.compiler.CodeGenerator;
import weblogic.utils.compiler.Tool;

public class j2idl extends Tool {
   private CodeGenerator idl;
   private ClassLoader classLoader = null;

   public void prepare() {
      Utilities.setClassLoader(this.classLoader);

      try {
         Class var1 = Utilities.classForName("weblogic.corba.rmic.IDLGenerator", this.getClass());
         Class[] var2 = new Class[]{Getopt2.class};
         Constructor var3 = var1.getConstructor(var2);
         Object[] var4 = new Object[]{this.opts};
         this.idl = (CodeGenerator)var3.newInstance(var4);
      } catch (Throwable var5) {
      }

      this.opts.setUsageArgs("<classes>...");
   }

   public void runBody() throws Exception {
      if (this.idl != null) {
         this.idl.generate(this.opts.args());
      }

   }

   private j2idl(String[] var1) {
      super(var1);
   }

   private j2idl(String[] var1, ClassLoader var2) {
      super(var1);
      this.classLoader = var2;
   }

   public static void main(String[] var0, ClassLoader var1) throws Exception {
      (new j2idl(var0, var1)).run();
   }

   public static void main(String[] var0) throws Exception {
      (new j2idl(var0)).run();
   }
}
