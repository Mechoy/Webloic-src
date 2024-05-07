package weblogic.wsee.tools.anttasks;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.taskdefs.MatchingTask;
import org.apache.tools.ant.types.Path;
import weblogic.wsee.WebServiceType;
import weblogic.wsee.tools.jws.build.Jws;

public class JwsFileSet extends MatchingTask {
   private Path srcDir;
   private WebServiceType type;

   public JwsFileSet() {
      this.type = WebServiceType.JAXRPC;
   }

   public void setSrcdir(Path var1) {
      this.srcDir = var1;
   }

   public void setType(String var1) {
      this.type = WebServiceType.valueOf(var1);
   }

   public Path getSrcdir() {
      return this.srcDir;
   }

   List<Jws> getFiles(File var1) {
      ArrayList var2 = new ArrayList();
      Iterator var3 = this.getSrcDirs(var1).iterator();

      while(var3.hasNext()) {
         File var4 = (File)var3.next();
         DirectoryScanner var5 = this.getDirectoryScanner(var4);
         String[] var6 = var5.getIncludedFiles();
         int var7 = var6.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            String var9 = var6[var8];
            String var10 = var9.replace('\\', '/');
            Jws var11 = new Jws();
            var11.setFile(var10);
            var11.srcdir(var4);
            var11.setType(this.type.toString());
            var2.add(var11);
         }
      }

      return var2;
   }

   private List<File> getSrcDirs(File var1) {
      ArrayList var2 = new ArrayList();
      if (this.srcDir == null) {
         var2.add(var1);
      } else {
         String[] var3 = this.srcDir.list();
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            String var6 = var3[var5];
            File var7 = new File(var6);
            if (var7.exists()) {
               var2.add(var7);
            }
         }
      }

      return var2;
   }
}
