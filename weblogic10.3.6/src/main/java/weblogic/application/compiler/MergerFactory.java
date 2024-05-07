package weblogic.application.compiler;

import java.io.File;
import java.io.IOException;

public interface MergerFactory {
   Merger createMerger(CompilerCtx var1, File var2) throws IOException;
}
