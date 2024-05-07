package weblogic;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.Vector;
import weblogic.rmi.rmic.Remote2Java;
import weblogic.rmi.utils.Utilities;
import weblogic.utils.Getopt2;
import weblogic.utils.compiler.CodeGenerator;
import weblogic.utils.compiler.CompilerInvoker;
import weblogic.utils.compiler.Tool;
import weblogic.utils.compiler.ToolFailureException;

public class rmic extends Tool {
   private CodeGenerator m_rmi;
   private CodeGenerator m_idl;
   private CodeGenerator m_iiop;
   private CompilerInvoker m_compiler;
   private ClassLoader m_classLoader = null;
   private Collection m_rmicMethodDescriptors = null;
   private static Vector ss = null;
   private static boolean no_compile = false;

   public void prepare() {
      Utilities.setClassLoader(this.m_classLoader);
      this.m_rmi = new Remote2Java(this.opts, this.m_classLoader, this.m_rmicMethodDescriptors);
      this.m_idl = this.createCodeGenerator("weblogic.corba.rmic.IDLGenerator");
      this.m_iiop = this.createCodeGenerator("weblogic.corba.rmic.Remote2Corba");
      this.m_compiler = new CompilerInvoker(this.opts);
      this.opts.setUsageArgs("<classes>...");
   }

   private void validateToolInputs() throws ToolFailureException {
      if (this.opts.args().length < 1) {
         this.opts.usageError("weblogic.rmic");
         throw new ToolFailureException("ERROR: incorrect command-line.");
      }
   }

   public void runBody() throws Exception {
      this.validateToolInputs();

      try {
         String[] var1 = this.m_rmi.generate(this.opts.args());
         if (no_compile && var1 != null) {
            for(int var2 = 0; var2 < var1.length; ++var2) {
               ss.addElement(var1[var2]);
            }
         }

         if (this.opts.hasOption("idl") && this.m_idl != null) {
            this.m_idl.generate(this.opts.args());
         }

         if (this.opts.hasOption("iiop") || this.opts.hasOption("iiopTie") || this.opts.hasOption("iiopSun")) {
            this.m_iiop.generate(this.opts.args());
         }
      } catch (ClassNotFoundException var6) {
         System.err.println("Class not found : " + var6.getMessage());
         throw var6;
      } finally {
         Utilities.setClassLoader((ClassLoader)null);
      }

   }

   private rmic(String[] var1) {
      super(var1);
   }

   private rmic(String[] var1, ClassLoader var2, Collection var3) {
      super(var1);
      this.m_classLoader = var2;
      this.m_rmicMethodDescriptors = var3;
   }

   public static void main(String[] var0, ClassLoader var1) throws Exception {
      (new rmic(var0, var1, (Collection)null)).run();
   }

   public static synchronized String[] main_nocompile(String[] var0, ClassLoader var1, Collection var2) throws Exception {
      ss = new Vector();
      no_compile = true;
      (new rmic(var0, var1, var2)).run();
      String[] var3 = new String[ss.size()];
      ss.copyInto(var3);
      return var3;
   }

   private final CodeGenerator createCodeGenerator(String var1) {
      try {
         Class var2 = Utilities.classForName(var1, this.getClass());
         Class[] var3 = new Class[]{Getopt2.class};
         Constructor var4 = var2.getConstructor(var3);
         Object[] var5 = new Object[]{this.opts};
         return (CodeGenerator)var4.newInstance(var5);
      } catch (Throwable var6) {
         return null;
      }
   }

   public static void main(String[] var0) throws Exception {
      (new rmic(var0)).run();
   }
}
