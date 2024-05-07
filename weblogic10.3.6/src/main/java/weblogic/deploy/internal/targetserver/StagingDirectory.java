package weblogic.deploy.internal.targetserver;

import java.io.File;
import java.io.IOException;
import weblogic.deploy.api.internal.utils.InstallDir;
import weblogic.management.deploy.internal.DeployerRuntimeLogger;

public final class StagingDirectory {
   private InstallDir root;
   private String dir;
   private boolean hasPlan;

   public StagingDirectory(String var1, String var2, String var3) throws IOException {
      this.assertNameIsNotNull(var2);
      this.assertDirIsNotNull(var3);
      this.hasPlan = var1 != null;
      this.dir = var3;
      this.root = new InstallDir(var2, this.dir);
      this.root.setAppDir(new File(this.dir));
      if (var1 != null) {
         this.root.setPlan(new File(this.root.getConfigDir(), (new File(var1)).getName()));
      }

   }

   private void assertDirIsNotNull(String var1) {
      String var2 = DeployerRuntimeLogger.nullStagingDirectory();
      if (var1 == null) {
         throw new IllegalArgumentException(var2);
      }
   }

   private void assertNameIsNotNull(String var1) {
      String var2 = DeployerRuntimeLogger.nullName();
      if (var1 == null) {
         throw new IllegalArgumentException(var2);
      }
   }

   public String getRoot() {
      return this.dir;
   }

   public String getSource() {
      return this.root.getArchive().getAbsolutePath();
   }

   public String getPlan() {
      return this.hasPlan ? this.root.getPlan().getAbsolutePath() : null;
   }

   public String getPlanDir() {
      return this.hasPlan ? this.root.getConfigDir().getAbsolutePath() : null;
   }
}
