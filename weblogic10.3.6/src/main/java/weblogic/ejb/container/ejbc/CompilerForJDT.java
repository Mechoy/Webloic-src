package weblogic.ejb.container.ejbc;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import weblogic.ejb.spi.ICompiler;
import weblogic.utils.compiler.JavaCompilerDiagnostic;
import weblogic.utils.compiler.JavaCompilerDiagnostics;
import weblogic.utils.compiler.JavaCompilerUtils;

public class CompilerForJDT implements ICompiler {
   private final String outputRoot;
   private final String classPath;
   private String result;

   public CompilerForJDT(String var1, String var2) {
      this.outputRoot = var1;
      this.classPath = var2;
   }

   public void compile() throws IOException {
      throw new AssertionError("This method should not be invoked for jdt compiler.");
   }

   public void compile(List<String> var1) throws IOException {
      throw new AssertionError("This method should not be invoked for jdt compiler.");
   }

   public void compile(Map<String, String> var1) throws IOException {
      String[] var2 = (String[])var1.keySet().toArray(new String[var1.size()]);
      String[] var3 = new String[var2.length];
      char[][] var4 = new char[var2.length][];

      for(int var5 = 0; var5 < var2.length; ++var5) {
         String var6 = var2[var5];
         var3[var5] = var6.substring(var6.lastIndexOf(46) + 1) + ".java";
         var4[var5] = ((String)var1.get(var6)).toCharArray();
      }

      JavaCompilerDiagnostics var10 = JavaCompilerUtils.compile(true, this.classPath, this.outputRoot, var3, var2, var4, false);
      List var11 = var10.getDisgnotics();
      StringBuffer var7 = new StringBuffer();

      JavaCompilerDiagnostic var9;
      for(Iterator var8 = var11.iterator(); var8.hasNext(); var7.append(var9.getOriginatingFileName()).append(": ").append(var9.getMessage())) {
         var9 = (JavaCompilerDiagnostic)var8.next();
         var7.append("\n");
         if (var9.isError()) {
            var7.append("<Compilation Error> ");
         } else {
            var7.append("<Compilation Warn> ");
         }
      }

      this.result = var7.toString();
      if (var10.getHasError()) {
         throw new IOException("JDT compilation error!" + this.result);
      }
   }

   public void compile(String[] var1) throws IOException {
      throw new AssertionError("This method should not be invoked for jdt compiler.");
   }

   public String getCompilerErrors() {
      return this.result;
   }

   public void overrideTargetDirectory(String var1) {
   }

   public void setExtraCompileFlags(String[] var1) {
   }

   public void setNoExit(boolean var1) {
   }

   public void setSourcepath(String var1) {
   }

   public void setWantCompilerErrors(boolean var1) {
   }
}
