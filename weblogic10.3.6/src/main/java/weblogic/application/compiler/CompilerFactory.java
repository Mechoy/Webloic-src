package weblogic.application.compiler;

import java.io.File;
import java.io.IOException;

public interface CompilerFactory {
   Compiler createCompiler(CompilerCtx var1, File var2) throws IOException;
}
