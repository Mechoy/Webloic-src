package weblogic.deploy.api.model.internal;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.enterprise.deploy.shared.ModuleType;
import weblogic.application.compiler.AppMerge;
import weblogic.deploy.api.internal.utils.ConfigHelper;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.model.EditableDeployableObject;
import weblogic.deploy.api.model.WebLogicDeployableObject;
import weblogic.deploy.api.shared.WebLogicModuleType;
import weblogic.descriptor.DescriptorBean;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.classloaders.Source;
import weblogic.utils.compiler.ToolFailureException;
import weblogic.utils.jars.VirtualJarFile;

public class WebLogicDeployableObjectImpl extends WebLogicDeployableObject implements EditableDeployableObject {
   private static final boolean debug = Debug.isDebug("model");
   private AppMerge appMergeToClose = null;
   private String altdd;

   protected WebLogicDeployableObjectImpl(File var1, ModuleType var2, WebLogicDeployableObject var3, String var4, String var5, File var6, File var7, File var8) throws IOException {
      super(var1, var2, var3, var4, var5, var6, var7, var8);
      this.altdd = var5;
   }

   public void setVirtualJarFile(VirtualJarFile var1) {
      super.setVirtualJarFile(var1);
      if (this.moduleType == null) {
         this.setModuleType(WebLogicModuleType.getFileModuleType(this.uri, var1));
      }

   }

   public void setClassLoader(GenericClassLoader var1) {
      this.gcl = var1;
   }

   public void setRootBean(DescriptorBean var1) {
      this.beanTree = var1;
      this.setDDBeanRoot(new DDBeanRootImpl(this.altdd, this, this.moduleType, var1, true));
   }

   public void addRootBean(String var1, DescriptorBean var2, ModuleType var3) {
      if (var2 != null) {
         if (debug) {
            Debug.say("Adding " + var3 + " rootbean to application at " + var1 + " on wdo: " + this + "\nwith descriptor bean: " + var2);
         }

         this.ddMap.put(var1, new DDBeanRootImpl(var1, this, var3, var2, true));
      }
   }

   public void setModuleArchive(File var1) {
      this.moduleArchive = var1;
   }

   public void setAppMerge(AppMerge var1) {
      this.appMergeToClose = var1;
   }

   public InputStream getEntry(String var1) {
      if (this.resourceFinder == null) {
         if (debug) {
            Debug.say("Resource finder is null, using virtual jar file from super");
         }

         return super.getEntry(var1);
      } else {
         ConfigHelper.checkParam("name", var1);

         try {
            if (debug) {
               Debug.say("in DO : " + this.moduleArchive.getName() + " with uri " + this.uri);
            }

            if (debug) {
               Debug.say("Getting stream for entry " + var1);
            }

            Source var2 = this.resourceFinder.getSource(var1);
            if (var2 != null) {
               return var2.getInputStream();
            }

            if (debug) {
               Debug.say("No entry in archive for " + var1);
            }
         } catch (IOException var3) {
            if (debug) {
               Debug.say("No entry in archive for " + var1);
            }
         }

         return null;
      }
   }

   public void close() {
      super.close();
      if (this.appMergeToClose != null) {
         try {
            this.appMergeToClose.cleanup();
         } catch (ToolFailureException var2) {
            if (debug) {
               Debug.say("Unable to cleanup AppMerge object");
               var2.printStackTrace();
            }
         }

         this.appMergeToClose = null;
      }

   }
}
