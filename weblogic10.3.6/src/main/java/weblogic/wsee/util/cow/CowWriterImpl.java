package weblogic.wsee.util.cow;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.FileSet;
import weblogic.utils.FileUtils;
import weblogic.utils.jars.JarFileUtils;
import weblogic.wsee.tools.anttasks.AntUtil;
import weblogic.wsee.wsdl.WsdlDefinitions;
import weblogic.wsee.wsdl.WsdlException;
import weblogic.wsee.wsdl.builder.WsdlDefinitionsBuilder;

public class CowWriterImpl implements CowWriter {
   private File destJwsDir;
   private File jwsGeneratedDir;
   private String srcWsdl;
   private String jar;
   private boolean explode = false;

   public CowWriterImpl(String var1, File var2, boolean var3) {
      this.srcWsdl = var1;
      this.destJwsDir = var2;
      this.explode = var3;
      if (var3) {
         this.jwsGeneratedDir = var2;
      } else {
         this.jwsGeneratedDir = new File(var2, "/generated");
         this.jar = (new File(var1)).getName().replace('.', '_') + ".jar";
      }

   }

   public void writeWsdl(WsdlDefinitions var1) throws IOException, WsdlException {
      File var2 = new File(this.jwsGeneratedDir, "/wsdls");
      ((WsdlDefinitionsBuilder)var1).writeToFile(new File(var2, this.srcWsdl), var1.getEncoding());
   }

   public void writeCow() throws IOException {
      if (!this.explode) {
         JarFileUtils.createJarFileFromDirectory(new File(this.destJwsDir, this.jar), this.jwsGeneratedDir);
         FileUtils.remove(this.jwsGeneratedDir);
      }

   }

   public File getGeneratedDir() {
      return this.jwsGeneratedDir;
   }

   public String getOutputWsdl() {
      return "/wsdls/" + this.srcWsdl;
   }

   public void writeFiles(List<FileSet> var1) {
      if (!var1.isEmpty()) {
         Project var2 = ((FileSet)var1.get(0)).getProject();
         AntUtil.copyFiles(var2, var1, this.getGeneratedDir());
      }
   }
}
