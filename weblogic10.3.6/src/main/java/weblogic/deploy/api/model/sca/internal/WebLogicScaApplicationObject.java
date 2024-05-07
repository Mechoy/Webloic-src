package weblogic.deploy.api.model.sca.internal;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import javax.enterprise.deploy.model.DeployableObject;
import javax.enterprise.deploy.shared.ModuleType;
import weblogic.application.compiler.AppMerge;
import weblogic.deploy.api.internal.utils.ConfigHelper;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.model.EditableDeployableObject;
import weblogic.deploy.api.model.WebLogicDeployableObject;
import weblogic.deploy.api.model.internal.DDBeanRootImpl;
import weblogic.deploy.api.model.sca.EditableScaApplicationObject;
import weblogic.deploy.api.shared.WebLogicModuleType;
import weblogic.descriptor.DescriptorBean;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.compiler.ToolFailureException;

public class WebLogicScaApplicationObject extends WebLogicDeployableObject implements EditableScaApplicationObject {
   private static final boolean debug = Debug.isDebug("model");
   private AppMerge appMergeToClose = null;
   protected GenericClassLoader gcl = null;

   public WebLogicScaApplicationObject(File var1, File var2, File var3, File var4) throws IOException {
      super(var1, WebLogicModuleType.SCA_COMPOSITE, (WebLogicDeployableObject)null, (String)null, (String)null, var2, var3, var4);
   }

   public String[] getModuleUris() {
      return this.getModuleUris(this.getDeployableObjects());
   }

   public String[] getModuleUris(ModuleType var1) {
      ConfigHelper.checkParam("ModuleType", var1);
      return var1 == WebLogicModuleType.SCA_COMPOSITE ? null : this.getModuleUris(this.getDeployableObjects(var1));
   }

   public DeployableObject[] getDeployableObjects() {
      return this.subModules.size() == 0 ? null : (DeployableObject[])((DeployableObject[])this.subModules.toArray(new WebLogicDeployableObject[0]));
   }

   public DeployableObject[] getDeployableObjects(ModuleType var1) {
      ConfigHelper.checkParam("ModuleType", var1);
      ArrayList var2 = new ArrayList();
      if (var1 == WebLogicModuleType.SCA_COMPOSITE) {
         return new WebLogicDeployableObject[]{this};
      } else {
         Iterator var4 = this.subModules.iterator();

         while(var4.hasNext()) {
            DeployableObject var3 = (DeployableObject)var4.next();
            if (var3.getType().equals(var1)) {
               var2.add(var3);
            }
         }

         if (var2.size() == 0) {
            return null;
         } else {
            return (DeployableObject[])((DeployableObject[])var2.toArray(new WebLogicDeployableObject[0]));
         }
      }
   }

   public DeployableObject getDeployableObject(String var1) {
      ConfigHelper.checkParam("uri", var1);
      DeployableObject[] var2 = this.getDeployableObjects();
      WebLogicDeployableObject var3 = null;
      if (var2 != null) {
         for(int var4 = 0; var4 < var2.length; ++var4) {
            WebLogicDeployableObject var5 = (WebLogicDeployableObject)var2[var4];
            if (var1.equals(var5.getUri())) {
               var3 = var5;
               break;
            }
         }
      }

      return var3;
   }

   public DeployableObject[] getDeployableObjects(String var1) {
      ConfigHelper.checkParam("uri", var1);
      ArrayList var2 = new ArrayList();
      DeployableObject[] var3 = this.getDeployableObjects();
      if (var3 != null) {
         for(int var4 = 0; var4 < var3.length; ++var4) {
            WebLogicDeployableObject var5 = (WebLogicDeployableObject)var3[var4];
            if (var1.equals(var5.getUri())) {
               var2.add(var5);
            }
         }
      }

      return (DeployableObject[])((DeployableObject[])var2.toArray(new WebLogicDeployableObject[0]));
   }

   protected String[] getModuleUris(DeployableObject[] var1) {
      WebLogicDeployableObject var2 = null;
      ArrayList var3 = new ArrayList();
      if (var1 != null) {
         for(int var4 = 0; var4 < var1.length; ++var4) {
            var2 = (WebLogicDeployableObject)var1[var4];
            var3.add(var2.getUri());
         }
      }

      return var3.size() == 0 ? null : (String[])((String[])var3.toArray(new String[0]));
   }

   public void setClassLoader(GenericClassLoader var1) {
      this.gcl = var1;
   }

   public void setRootBean(DescriptorBean var1) {
      this.setDescriptorBean(var1);
      this.setDDBeanRoot(new DDBeanRootImpl((String)null, this, this.moduleType, var1, true));
   }

   public void addRootBean(String var1, DescriptorBean var2, ModuleType var3) {
      if (var2 != null) {
         if (debug) {
            Debug.say("Adding " + var3 + " rootbean to application at " + var1 + " on wdo: " + this + "\nwith descriptor bean: " + var2);
         }

         this.ddMap.put(var1, new DDBeanRootImpl(var1, this, var3, var2, true));
      }
   }

   public void setAppMerge(AppMerge var1) {
      this.appMergeToClose = var1;
   }

   public void addDeployableObject(EditableDeployableObject var1) {
      WebLogicDeployableObject var2 = (WebLogicDeployableObject)var1;
      if (debug) {
         Debug.say("Adding new deployable object at " + var2.getUri() + " of type '" + var2.getType() + "' on wdo: " + this);
      }

      var2.setParent(this);
      this.subModules.add(var2);
   }

   public void close() {
      super.close();
      if (this.resourceFinder != null) {
         this.resourceFinder.close();
      }

      this.resourceFinder = null;
      if (this.appMergeToClose == null) {
         throw new AssertionError("AppMerge not closed leaving possibly open handles");
      } else {
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
