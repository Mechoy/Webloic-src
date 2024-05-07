package weblogic.application.compiler.flow;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import weblogic.application.ApplicationFileManager;
import weblogic.application.SplitDirectoryInfo;
import weblogic.application.compiler.AppcUtils;
import weblogic.application.compiler.CompilerCtx;
import weblogic.application.io.Ear;
import weblogic.application.io.JarCopyFilter;
import weblogic.application.utils.EarUtils;
import weblogic.application.utils.IOUtils;
import weblogic.j2ee.J2EELogger;
import weblogic.utils.FileUtils;
import weblogic.utils.compiler.ToolFailureException;
import weblogic.utils.jars.VirtualJarFactory;
import weblogic.utils.jars.VirtualJarFile;

public final class EarClassLoaderFlow extends CompilerFlow {
   private Ear ear = null;
   private static final File dummyExtractDir = new File(System.getProperty("java.io.tmpdir"), "_appc_tmp");

   public EarClassLoaderFlow(CompilerCtx var1) {
      super(var1);
   }

   public void compile() throws ToolFailureException {
      this.init();
   }

   private void init() throws ToolFailureException {
      File var1 = this.ctx.getSourceFile();
      File var2 = this.ctx.getOutputDir();
      VirtualJarFile var3 = null;
      boolean var4 = var1.equals(var2);
      File var5 = EarUtils.getSplitDirProperties(var1);
      boolean var6 = var5.exists();
      SplitDirectoryInfo var7 = null;

      try {
         if (var6) {
            var7 = new SplitDirectoryInfo(var1, var5);
         }

         if (var6 && !var4) {
            File[] var8 = var7.getRootDirectories();

            for(int var9 = 0; var9 < var8.length; ++var9) {
               AppcUtils.expandJarFileIntoDirectory(var8[var9], var2);
            }

            Map var15 = var7.getUriLinks();
            Iterator var10 = var15.keySet().iterator();

            while(true) {
               if (!var10.hasNext()) {
                  EarUtils.getSplitDirProperties(var2).delete();
                  var6 = false;
                  var4 = true;
                  break;
               }

               String var11 = (String)var10.next();
               List var12 = (List)var15.get(var11);

               for(int var13 = var12.size() - 1; var13 >= 0; --var13) {
                  FileUtils.copy((File)var12.get(var13), var2);
               }
            }
         }

         if (var6) {
            this.ctx.setSplitDir();
            var3 = VirtualJarFactory.createVirtualJar(var7.getSrcDir(), var7.getDestDir());
            this.ear = new Ear(var1.getName(), dummyExtractDir, var7, JarCopyFilter.NOCOPY_FILTER);
            this.ctx.getApplicationContext().setApplicationFileManager(ApplicationFileManager.newInstance(var7));
            this.ctx.getApplicationContext().setSplitDirectoryInfo(var7);
         } else {
            if (!var4) {
               AppcUtils.expandJarFileIntoDirectory(var1, var2);
            }

            var3 = VirtualJarFactory.createVirtualJar(var2);
            this.ear = new Ear(var1.getName(), dummyExtractDir, new File[]{var2}, JarCopyFilter.NOCOPY_FILTER);
            this.ctx.getApplicationContext().setApplicationFileManager(ApplicationFileManager.newInstance(var2));
         }

         this.ctx.setVSource(var3);
      } catch (IOException var14) {
         throw new ToolFailureException(J2EELogger.logAppcSourceFileNotAccessibleLoggable(var1.getAbsolutePath(), var14.toString()).getMessage(), var14);
      }

      this.ctx.setEar(this.ear);
   }

   public void cleanup() {
      this.ear.getClassFinder().close();
      IOUtils.forceClose(this.ctx.getVSource());
   }
}
