package weblogic.deploy.api.internal.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import weblogic.application.Type;
import weblogic.application.compiler.AppMerge;
import weblogic.application.library.LibraryData;
import weblogic.application.library.LibraryDefinition;
import weblogic.deploy.api.model.WebLogicDeployableObject;
import weblogic.deploy.api.model.WebLogicDeployableObjectFactory;
import weblogic.deploy.api.spi.config.DescriptorSupport;
import weblogic.descriptor.DescriptorBean;
import weblogic.j2ee.descriptor.wl.LibraryRefBean;
import weblogic.utils.compiler.ToolFailureException;

public abstract class AppMerger {
   private static final boolean debug = Debug.isDebug("config");
   private File app;
   private File plan;
   private File plandir;
   private LibrarySpec[] libs;
   private boolean basicView = false;
   private AppMerge appMerge = null;
   private String lightWeightAppName = null;

   public abstract DescriptorSupport getDescriptorSupport();

   public WebLogicDeployableObject getMergedApp(File var1, File var2, File var3, LibrarySpec[] var4, String var5, WebLogicDeployableObjectFactory var6) throws IOException {
      return this.getMergedApp(var1, var2, var3, var4, var6, false, var5);
   }

   public WebLogicDeployableObject getMergedApp(File var1, File var2, File var3, LibrarySpec[] var4, WebLogicDeployableObjectFactory var5, boolean var6, String var7) throws IOException {
      this.app = var1;
      this.plan = var2;
      this.plandir = var3;
      this.libs = var4;
      this.basicView = var6;
      this.lightWeightAppName = var7;

      try {
         return this.merge(var5);
      } catch (ToolFailureException var10) {
         if (debug && var10.getCause() != null) {
            Debug.say(var10.getCause().getMessage());
         }

         IOException var9 = new IOException(var10.getMessage());
         var9.initCause(var10);
         throw var9;
      }
   }

   private WebLogicDeployableObject merge(WebLogicDeployableObjectFactory var1) throws IOException, ToolFailureException {
      String[] var2 = this.createMergeCommand();
      if (debug) {
         this.dumpAppMergeArgs(var2);
      }

      boolean var3 = this.libs == null;
      this.appMerge = new AppMerge(var2, var1, var3, false, true, this.basicView);
      return (WebLogicDeployableObject)this.appMerge.merge();
   }

   public AppMerge getAppMerge() {
      return this.appMerge;
   }

   private void dumpAppMergeArgs(String[] var1) {
      if (debug) {
         Debug.say("invokinging appmerge with");

         for(int var2 = 0; var2 < var1.length; ++var2) {
            String var3 = var1[var2];
            Debug.say("  " + var3);
         }
      }

   }

   private String[] createMergeCommand() throws IOException {
      ArrayList var1 = new ArrayList();
      if (this.lightWeightAppName != null) {
         var1.add("-lightweight");
         var1.add(this.lightWeightAppName);
      }

      var1.add("-noexit");
      var1.add("-nopackage");
      if (debug) {
         var1.add("-verbose");
      }

      if (this.plan != null) {
         var1.add("-plan");
         var1.add(this.plan.getAbsolutePath());
      }

      if (this.plandir != null) {
         var1.add("-plandir");
         var1.add(this.plandir.getAbsolutePath());
      }

      int var2 = 0;
      String var3 = "";
      if (this.libs != null) {
         for(int var4 = 0; var4 < this.libs.length; ++var4) {
            LibrarySpec var5 = this.libs[var4];
            if (var2++ > 0) {
               var3 = var3 + ",";
            }

            String var6 = var5.getLocation().getAbsolutePath();
            String var7 = var5.getSpecVersion();
            String var8 = var5.getImplVersion();
            String var9 = var5.getName();
            var3 = var3 + var6;
            if (var9 != null) {
               var3 = var3 + "@name=" + var9;
            }

            if (var7 != null) {
               var3 = var3 + "@libspecver=" + var7;
            }

            if (var8 != null) {
               var3 = var3 + "@libimplver=" + var8;
            }
         }

         if (var3.length() > 0) {
            var1.add("-library");
            var1.add(var3);
         }
      }

      var1.add("-readonly");
      var1.add(this.app.getPath());
      return (String[])((String[])var1.toArray(new String[0]));
   }

   protected abstract LibraryRefBean[] getLibraryRefs(DescriptorBean var1);

   class MyLibraryDefinition extends LibraryDefinition {
      MyLibraryDefinition(LibraryData var2, Type var3) {
         super(var2, var3);
      }
   }
}
