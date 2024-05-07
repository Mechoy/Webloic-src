package weblogic.wsee.util.cow;

import java.io.File;
import weblogic.wsee.wsdl.WsdlDefinitions;
import weblogic.wsee.wsdl.WsdlException;

public interface CowReader {
   WsdlDefinitions getWsdl(String var1) throws WsdlException;

   ClassLoader getClassLoader();

   File getCowFile();

   void cleanup();

   public static class Factory {
      public static CowReader newInstance(File var0) {
         return new CowReaderImpl(var0);
      }
   }
}
