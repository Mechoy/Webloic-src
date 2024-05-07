package weblogic.wsee.tools.wsdlc;

import java.io.File;
import javax.xml.namespace.QName;
import weblogic.wsee.tools.WsBuildException;
import weblogic.wsee.tools.logging.Logger;

public interface Wsdl2JwsBuilder<T> {
   void setClasspath(String[] var1);

   void setDestDir(File var1);

   void setDestImplDir(File var1);

   void setWsdl(String var1);

   void setWsdlLocation(String var1);

   void setPackageName(String var1);

   void setPortName(QName var1);

   void setLogger(Logger var1);

   void setBindingFiles(File[] var1);

   void setOptions(T var1);

   void execute() throws WsBuildException;
}
