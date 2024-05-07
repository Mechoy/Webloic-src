package weblogic.ejb.spi;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ICompiler {
   void setExtraCompileFlags(String[] var1);

   void setNoExit(boolean var1);

   void overrideTargetDirectory(String var1);

   void setSourcepath(String var1);

   void setWantCompilerErrors(boolean var1);

   String getCompilerErrors();

   void compile() throws IOException;

   void compile(List<String> var1) throws IOException;

   void compile(String[] var1) throws IOException;

   void compile(Map<String, String> var1) throws IOException;
}
