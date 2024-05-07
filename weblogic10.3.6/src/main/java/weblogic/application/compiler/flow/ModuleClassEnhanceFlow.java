package weblogic.application.compiler.flow;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import javax.enterprise.deploy.shared.ModuleType;
import weblogic.application.compiler.AppcUtils;
import weblogic.application.compiler.BuildtimeApplicationContext;
import weblogic.application.compiler.CompilerCtx;
import weblogic.application.compiler.EARModule;
import weblogic.utils.Debug;
import weblogic.utils.FileUtils;
import weblogic.utils.classloaders.MultiClassFinder;
import weblogic.utils.classloaders.Source;
import weblogic.utils.compiler.ToolFailureException;

public class ModuleClassEnhanceFlow extends AbstractClassEnhanceFlow {
   private EARModule[] modules = null;
   private final BuildtimeApplicationContext libCtx;
   private ClassLoader cl;
   private MultiClassFinder moduleFinder;
   private MultiClassFinder warFinder;
   private MultiClassFinder appFinder;
   private MultiClassFinder shareLibFinder;
   private Collection finders;

   public ModuleClassEnhanceFlow(CompilerCtx var1) {
      super(var1);
      this.libCtx = (BuildtimeApplicationContext)var1.getApplicationContext();
   }

   private ClassLoader getModuleClassLoader() {
      this.moduleFinder = new MultiClassFinder();
      this.warFinder = new MultiClassFinder();
      this.appFinder = new MultiClassFinder();
      this.shareLibFinder = new MultiClassFinder();
      this.finders = new ArrayList();
      if (this.ctx.getEar() != null) {
         this.moduleFinder.addFinder(this.ctx.getEar().getClassFinder());
         this.appFinder.addFinder(this.ctx.getEar().getClassFinder());
      }

      for(int var1 = 0; var1 < this.modules.length; ++var1) {
         this.moduleFinder.addFinder(this.modules[var1].getClassFinder());
         if (this.modules[var1].getModuleType() == ModuleType.WAR) {
            this.warFinder.addFinder(this.modules[var1].getClassFinder());
         }
      }

      this.moduleFinder.addFinder(this.libCtx.getClassFinder());
      this.shareLibFinder.addFinder(this.libCtx.getClassFinder());
      this.finders.add(this.warFinder);
      this.finders.add(this.appFinder);
      this.finders.add(this.shareLibFinder);
      return AppcUtils.getClassLoaderForApplication(this.moduleFinder, this.ctx, this.ctx.getApplicationContext().getApplicationId());
   }

   public void compile() throws ToolFailureException {
      File var1 = null;

      try {
         this.modules = this.ctx.getModules();
         new HashSet();
         HashSet var3 = new HashSet();
         HashSet var4 = new HashSet();
         URL var5 = null;
         this.cl = this.getModuleClassLoader();
         var5 = this.cl.getResource("META-INF/persistence.xml");
         if (var5 == null) {
            return;
         }

         var1 = this.createConfigFile();
         Enumeration var6 = this.moduleFinder.getSources("META-INF/persistence.xml");

         while(var6.hasMoreElements()) {
            String var7 = var6.nextElement().toString();
            if (var7.endsWith(".jar")) {
               if (var7.indexOf("WEB-INF") > -1) {
                  var7 = var7.substring(var7.lastIndexOf(File.separatorChar) + 1);
                  var4.add(var7);
               } else {
                  var3.add(var7);
               }
            }
         }

         Collection var2 = this.getEntityClasses(this.cl);
         if (var2.size() == 0) {
            return;
         }

         Iterator var19 = var2.iterator();

         while(var19.hasNext()) {
            String var8 = (String)var19.next();
            Iterator var9 = this.finders.iterator();

            while(var9.hasNext()) {
               MultiClassFinder var10 = (MultiClassFinder)var9.next();
               Source var11 = var10.getClassSource(var8);
               if (var11 != null) {
                  String var12 = var11.toString();
                  if (var12.endsWith(".jar")) {
                     if (var12.indexOf("WEB-INF") > -1) {
                        var12 = var12.substring(var12.lastIndexOf(File.separatorChar) + 1);
                        var4.add(var12);
                     } else {
                        var3.add(var12);
                     }
                  }
               }
            }
         }

         for(int var20 = 0; var20 < this.modules.length; ++var20) {
            if (this.modules[var20].getModuleType() == ModuleType.EJB) {
               if (debug) {
                  Debug.say("Enhance EJB persistent classes");
               }

               this.processDir(this.modules[var20].getOutputDir().getPath(), var1, var2, this.cl, false);
            }

            if (this.modules[var20].getModuleType() == ModuleType.WAR) {
               if (debug) {
                  Debug.say("Enhance WAR persistent classes");
               }

               this.processDir(this.modules[var20].getOutputDir() + WEBINF_CLASSES, var1, var2, this.cl, false);
               Iterator var21 = var4.iterator();

               while(var21.hasNext()) {
                  String var22 = (String)var21.next();
                  File var23 = new File(this.modules[var20].getOutputDir() + WEBINF_LIB + var22);
                  if (var23.exists()) {
                     this.processJar(var23, var1, var2, this.cl, false);
                  }
               }
            }
         }

         if (this.ctx.getEar() != null) {
            if (debug) {
               Debug.say("Enhance exploded application persistent classes");
            }

            this.processDir(this.ctx.getOutputDir() + APP_CLASSES, var1, var2, this.cl, false);
         }

         this.processJars(var3, var1, var2, this.cl, false);
      } catch (IOException var17) {
         throw new ToolFailureException(var17.getMessage(), var17);
      } finally {
         if (var1 != null && var1.exists()) {
            FileUtils.remove(var1);
         }

      }

   }
}
