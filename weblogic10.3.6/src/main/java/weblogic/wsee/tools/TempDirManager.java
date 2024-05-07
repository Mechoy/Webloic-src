package weblogic.wsee.tools;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.apache.tools.ant.Project;
import weblogic.wsee.tools.anttasks.AntUtil;

public class TempDirManager {
   private final Project project;
   private Set<File> tempDirs = new HashSet();

   public TempDirManager(Project var1) {
      this.project = var1;
   }

   public File createTempDir(String var1, File var2) throws IOException {
      String var3 = "_" + Long.toString((long)Math.abs(var1.hashCode()), 36);
      File var4 = new File(var2, var3);

      for(int var5 = 0; var4.exists(); var4 = new File(var2, var3 + var5++)) {
      }

      if (!var4.mkdirs()) {
         throw new IOException("Unable to create temp dir " + var4);
      } else {
         this.tempDirs.add(var4);
         return var4;
      }
   }

   public void cleanup() {
      Iterator var1 = this.tempDirs.iterator();

      while(var1.hasNext()) {
         File var2 = (File)var1.next();
         AntUtil.deleteDir(this.project, var2);
      }

      this.tempDirs.clear();
   }

   public Set<File> getTempDirs() {
      return Collections.unmodifiableSet(this.tempDirs);
   }
}
