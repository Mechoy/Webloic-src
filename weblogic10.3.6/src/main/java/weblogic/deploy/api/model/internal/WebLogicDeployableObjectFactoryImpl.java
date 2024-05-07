package weblogic.deploy.api.model.internal;

import java.io.File;
import java.io.IOException;
import javax.enterprise.deploy.shared.ModuleType;
import javax.enterprise.deploy.spi.exceptions.InvalidModuleException;
import weblogic.deploy.api.internal.utils.ConfigHelper;
import weblogic.deploy.api.internal.utils.EarMerger;
import weblogic.deploy.api.internal.utils.LibrarySpec;
import weblogic.deploy.api.model.EditableDeployableObject;
import weblogic.deploy.api.model.EditableJ2eeApplicationObject;
import weblogic.deploy.api.model.WebLogicDeployableObject;
import weblogic.deploy.api.model.WebLogicDeployableObjectFactory;
import weblogic.deploy.api.model.sca.EditableScaApplicationObject;
import weblogic.deploy.api.model.sca.internal.WebLogicScaApplicationObject;

public class WebLogicDeployableObjectFactoryImpl implements WebLogicDeployableObjectFactory {
   private File app;
   private File appRoot;
   private File plan;
   private File plandir;
   private String lightWeightAppName = null;

   public WebLogicDeployableObjectFactoryImpl() {
   }

   public WebLogicDeployableObjectFactoryImpl(File var1, File var2, File var3, File var4, LibrarySpec[] var5) {
      this.app = var1;
      this.appRoot = var2;
      this.plan = var3;
      this.plandir = var4;
   }

   public EditableDeployableObject createDeployableObject(String var1, String var2, ModuleType var3) throws IOException {
      return new WebLogicDeployableObjectImpl(new File(var1), var3, (WebLogicDeployableObject)null, var1, var2, this.appRoot, this.plan, this.plandir);
   }

   public EditableJ2eeApplicationObject createApplicationObject() throws IOException {
      return new WebLogicJ2eeApplicationObjectImpl(this.app, this.appRoot, this.plan, this.plandir);
   }

   public EditableScaApplicationObject createScaApplicationObject() throws IOException {
      return new WebLogicScaApplicationObject(this.app, this.appRoot, this.plan, this.plandir);
   }

   public WebLogicDeployableObject createDeployableObject(File var1) throws IOException, InvalidModuleException {
      ConfigHelper.checkParam("File", var1);
      return this.createDeployableObject(var1, (File)null, (File)null, (File)null, (LibrarySpec[])null);
   }

   public WebLogicDeployableObject createDeployableObject(File var1, File var2) throws IOException, InvalidModuleException {
      return this.createDeployableObject(var1, var2, (File)null, (File)null, (LibrarySpec[])null);
   }

   public WebLogicDeployableObject createLazyDeployableObject(File var1, File var2, File var3, File var4, LibrarySpec[] var5) throws IOException, InvalidModuleException {
      return this.createDeployableObject(var1, var2, var3, var4, var5, true);
   }

   public WebLogicDeployableObject createDeployableObject(File var1, File var2, File var3, File var4, LibrarySpec[] var5) throws IOException, InvalidModuleException {
      return this.createDeployableObject(var1, var2, var3, var4, var5, false);
   }

   private WebLogicDeployableObject createDeployableObject(File var1, File var2, File var3, File var4, LibrarySpec[] var5, boolean var6) throws IOException, InvalidModuleException {
      this.app = var1;
      this.appRoot = var2;
      this.plan = var3;
      this.plandir = var4;
      EarMerger var7 = new EarMerger();
      WebLogicDeployableObject var8 = var7.getMergedApp(var1, var3, var4, var5, this, var6, this.lightWeightAppName);
      ((EditableDeployableObject)var8).setAppMerge(var7.getAppMerge());
      return var8;
   }

   public void setLightWeightAppName(String var1) {
      this.lightWeightAppName = var1;
   }
}
