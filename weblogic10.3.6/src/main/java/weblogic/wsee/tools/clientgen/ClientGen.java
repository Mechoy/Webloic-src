package weblogic.wsee.tools.clientgen;

import java.io.File;
import weblogic.wsee.tools.WsBuildException;
import weblogic.wsee.tools.logging.Logger;

public interface ClientGen<T> {
   void setDestDir(File var1);

   void setPackageName(String var1);

   void setWsdl(String var1);

   void setBindingFiles(File[] var1);

   void setOptions(T var1);

   void setLogger(Logger var1);

   void execute() throws WsBuildException;
}
