package weblogic.wsee.util.cow;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.apache.tools.ant.types.FileSet;
import weblogic.wsee.wsdl.WsdlDefinitions;
import weblogic.wsee.wsdl.WsdlException;

public interface CowWriter {
   String TEMP_OUT_DIR = "/generated";
   String COW_WSDL_DIR = "/wsdls";

   void writeWsdl(WsdlDefinitions var1) throws IOException, WsdlException;

   void writeFiles(List<FileSet> var1);

   void writeCow() throws IOException;

   File getGeneratedDir();

   String getOutputWsdl();

   public static class Factory {
      public static CowWriter newInstance(String var0, File var1, boolean var2) {
         return new CowWriterImpl(var0, var1, var2);
      }
   }
}
