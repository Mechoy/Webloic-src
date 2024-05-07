package weblogic.deploy.api.model.internal;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.enterprise.deploy.shared.ModuleType;
import weblogic.application.compiler.AppMerge;
import weblogic.deploy.api.internal.utils.ConfigHelper;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.model.EditableDeployableObject;
import weblogic.deploy.api.model.EditableJ2eeApplicationObject;
import weblogic.deploy.api.model.WebLogicDeployableObject;
import weblogic.deploy.api.model.WebLogicJ2eeApplicationObject;
import weblogic.descriptor.DescriptorBean;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.classloaders.Source;
import weblogic.utils.compiler.ToolFailureException;

public class WebLogicJ2eeApplicationObjectImpl extends WebLogicJ2eeApplicationObject implements EditableJ2eeApplicationObject {
   private static final boolean debug = Debug.isDebug("model");
   private AppMerge appMergeToClose = null;

   protected WebLogicJ2eeApplicationObjectImpl(File var1, File var2, File var3, File var4) throws IOException {
      super(var1, var2, var3, var4);
   }

   public void addDeployableObject(EditableDeployableObject var1) {
      WebLogicDeployableObject var2 = (WebLogicDeployableObject)var1;
      if (var2.getType() == null) {
         throw new AssertionError("Unable to determine module type for : " + var2.getArchive() + " with uri : " + var2.getUri() + " in app " + this.getArchive());
      } else {
         if (debug) {
            Debug.say("Adding new deployable object at " + var2.getUri() + " of type '" + var2.getType() + "' on wdo: " + this);
         }

         ModuleType var3 = var2.getType();
         if (var3 != ModuleType.EJB && var3 != ModuleType.CAR && var3 != ModuleType.WAR && var3 != ModuleType.RAR) {
            try {
               if (var2.isDBSet()) {
                  if (debug) {
                     Debug.say("Adding new ddbeanroot to application at " + var2.getUri() + " on wdo: " + this + "\nwith descriptor bean: " + var2.getDescriptorBean());
                  }

                  this.ddMap.put(var2.getUri(), new DDBeanRootImpl(var2.getUri(), this, var2.getType(), var2.getDescriptorBean(), true));
               }
            } catch (IOException var5) {
               if (debug) {
                  Debug.say("Problem getting descriptor bean from wdo: " + var5);
               }
            }

            if (debug) {
               Debug.say("WDO is not the standard module type\t" + var2.getArchive() + " type : " + var3);
            }

            var2.close();
         } else {
            if (debug) {
               Debug.say("adding module : " + var2.getArchive() + " with db: " + var2.isDBSet());
            }

            var2.setParent(this);
            this.subModules.add(var2);
         }

      }
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

   public InputStream getEntry(String var1) {
      ConfigHelper.checkParam("name", var1);

      try {
         if (debug) {
            Debug.say("in DO : " + this.moduleArchive.getName() + " with uri " + this.uri);
         }

         if (debug) {
            Debug.say("Getting stream for entry " + var1);
         }

         if (this.resourceFinder != null) {
            Source var2 = this.resourceFinder.getSource(var1);
            if (var2 != null) {
               return var2.getInputStream();
            }
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
