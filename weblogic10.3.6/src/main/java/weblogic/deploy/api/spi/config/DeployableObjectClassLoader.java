package weblogic.deploy.api.spi.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import javax.enterprise.deploy.model.DeployableObject;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.j2ee.descriptor.wl.DeploymentPlanBean;
import weblogic.j2ee.descriptor.wl.ModuleDescriptorBean;
import weblogic.utils.classloaders.ClassFinder;
import weblogic.utils.classloaders.GenericClassLoader;

public final class DeployableObjectClassLoader extends GenericClassLoader {
   private static final boolean debug = Debug.isDebug("config");
   private final DeployableObject dObject;
   private DeploymentPlanBean plan = null;
   private final File configDir;
   private final String moduleName;
   private final InputStream myIS;
   private final String myUri;

   public DeployableObjectClassLoader(DeployableObject var1, DeploymentPlanBean var2, File var3, String var4, ClassFinder var5, ClassLoader var6) {
      super(var5, var6);
      this.dObject = var1;
      this.plan = var2;
      this.configDir = var3;
      this.moduleName = var4;
      this.myIS = null;
      this.myUri = null;
   }

   public DeployableObjectClassLoader(InputStream var1, String var2, ClassFinder var3, ClassLoader var4) {
      super(var3, var4);
      this.myIS = var1;
      this.myUri = var2;
      this.dObject = null;
      this.plan = null;
      this.configDir = null;
      this.moduleName = null;
   }

   public InputStream getResourceAsStream(String var1) {
      if (debug) {
         Debug.say("Getting stream for " + var1);
      }

      if (var1.equals(this.myUri)) {
         if (this.myIS == null) {
            return null;
         } else {
            try {
               this.myIS.reset();
            } catch (IOException var7) {
            }

            this.myIS.mark(1000000);
            return this.myIS;
         }
      } else {
         Object var3 = super.getResourceAsStream(var1);
         if (var3 == null && this.dObject != null) {
            var3 = this.dObject.getEntry(var1);
         }

         if (var3 == null && this.plan != null) {
            ModuleDescriptorBean var4 = this.plan.findModuleDescriptor(this.moduleName, var1);
            if (var4 != null) {
               File var2;
               if (this.plan.rootModule(this.moduleName)) {
                  var2 = this.configDir;
               } else {
                  var2 = new File(this.configDir, this.moduleName);
               }

               File var5 = new File(var2, var4.getUri());

               try {
                  var3 = new FileInputStream(var5);
               } catch (FileNotFoundException var8) {
               }
            }
         }

         return (InputStream)var3;
      }
   }
}
