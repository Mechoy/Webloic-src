package weblogic.ejb.container.ejbc;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import weblogic.ejb.spi.ICompiler;
import weblogic.utils.compiler.ICompilerInvoker;

public class CompilerForJavac implements ICompiler {
   ICompilerInvoker compiler;

   public CompilerForJavac(ICompilerInvoker var1) {
      this.compiler = var1;
   }

   public void compile() throws IOException {
      this.compiler.compile();
   }

   public void compile(List<String> var1) throws IOException {
      this.compiler.compile(var1);
   }

   public void compile(Map<String, String> var1) throws IOException {
      throw new AssertionError("This method should not be invoked for javac compiler.");
   }

   public void compile(String[] var1) throws IOException {
      this.compiler.compile(var1);
   }

   public String getCompilerErrors() {
      return this.compiler.getCompilerErrors();
   }

   public void overrideTargetDirectory(String var1) {
      this.compiler.overrideTargetDirectory(var1);
   }

   public void setExtraCompileFlags(String[] var1) {
      this.compiler.setExtraCompileFlags(var1);
   }

   public void setNoExit(boolean var1) {
      this.compiler.setNoExit(var1);
   }

   public void setSourcepath(String var1) {
      this.compiler.setSourcepath(var1);
   }

   public void setWantCompilerErrors(boolean var1) {
      this.compiler.setWantCompilerErrors(var1);
   }
}
