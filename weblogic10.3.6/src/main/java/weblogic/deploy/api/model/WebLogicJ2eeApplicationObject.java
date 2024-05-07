package weblogic.deploy.api.model;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.model.DeployableObject;
import javax.enterprise.deploy.model.J2eeApplicationObject;
import javax.enterprise.deploy.model.XpathListener;
import javax.enterprise.deploy.shared.ModuleType;
import javax.enterprise.deploy.spi.exceptions.InvalidModuleException;
import weblogic.deploy.api.internal.SPIDeployerLogger;
import weblogic.deploy.api.internal.utils.ConfigHelper;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.internal.utils.LibrarySpec;
import weblogic.descriptor.DescriptorBean;
import weblogic.j2ee.descriptor.ApplicationBean;
import weblogic.j2ee.descriptor.ModuleBean;

public class WebLogicJ2eeApplicationObject extends WebLogicDeployableObject implements J2eeApplicationObject {
   private static final boolean debug = Debug.isDebug("model");
   protected ApplicationBean app;

   public void close() {
      super.close();
   }

   protected WebLogicJ2eeApplicationObject(File var1, File var2, File var3, File var4) throws IOException {
      super(var1, ModuleType.EAR, (WebLogicDeployableObject)null, (String)null, (String)null, var2, var3, var4);
   }

   public void addXpathListener(ModuleType var1, String var2, XpathListener var3) {
   }

   public void removeXpathListener(ModuleType var1, String var2, XpathListener var3) {
   }

   public String[] getText(ModuleType var1, String var2) {
      ConfigHelper.checkParam("ModuleType", var1);
      ConfigHelper.checkParam("xpath", var2);
      if (var1 == ModuleType.EAR) {
         return this.getDDBeanRoot().getText(var2);
      } else {
         DeployableObject[] var3 = this.getDeployableObjects(var1);
         if (var3 == null) {
            return null;
         } else {
            ArrayList var5 = new ArrayList();

            for(int var6 = 0; var6 < var3.length; ++var6) {
               DeployableObject var4 = var3[var6];
               if (var4.getType() == var1) {
                  var5.addAll(Arrays.asList((Object[])var4.getText(var2)));
               }
            }

            return (String[])((String[])var5.toArray(new String[0]));
         }
      }
   }

   public DDBean[] getChildBean(ModuleType var1, String var2) {
      ConfigHelper.checkParam("ModuleType", var1);
      ConfigHelper.checkParam("xpath", var2);
      if (var1 == ModuleType.EAR) {
         return this.getDDBeanRoot().getChildBean(var2);
      } else {
         DeployableObject[] var3 = this.getDeployableObjects(var1);
         if (var3 == null) {
            return null;
         } else {
            ArrayList var6 = new ArrayList();

            for(int var7 = 0; var7 < var3.length; ++var7) {
               DeployableObject var4 = var3[var7];
               if (var4.getType().getValue() == var1.getValue()) {
                  DDBean[] var5 = var4.getChildBean(var2);
                  if (var5 != null) {
                     var6.addAll(Arrays.asList(var5));
                  }
               }
            }

            if (var6.isEmpty()) {
               return null;
            } else {
               return (DDBean[])((DDBean[])var6.toArray(new DDBean[0]));
            }
         }
      }
   }

   public String[] getModuleUris() {
      return this.getModuleUris(this.getDeployableObjects());
   }

   public String[] getModuleUris(ModuleType var1) {
      ConfigHelper.checkParam("ModuleType", var1);
      return var1 == ModuleType.EAR ? null : this.getModuleUris(this.getDeployableObjects(var1));
   }

   public DeployableObject[] getDeployableObjects() {
      return this.subModules.size() == 0 ? null : (DeployableObject[])((DeployableObject[])this.subModules.toArray(new WebLogicDeployableObject[0]));
   }

   public DeployableObject[] getDeployableObjects(ModuleType var1) {
      ConfigHelper.checkParam("ModuleType", var1);
      ArrayList var2 = new ArrayList();
      if (var1 == ModuleType.EAR) {
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
      Object var4 = null;
      if (var3 != null) {
         for(int var5 = 0; var5 < var3.length; ++var5) {
            WebLogicDeployableObject var6 = (WebLogicDeployableObject)var3[var5];
            if (var1.equals(var6.getUri())) {
               var2.add(var6);
            }
         }
      }

      return (DeployableObject[])((DeployableObject[])var2.toArray(new WebLogicDeployableObject[0]));
   }

   public DescriptorBean getDescriptorBean() {
      return (DescriptorBean)this.app;
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

   public void setDescriptorBean(DescriptorBean var1) {
      if (!(var1 instanceof ApplicationBean)) {
         if (debug) {
            Thread.dumpStack();
         }

         throw new AssertionError(SPIDeployerLogger.unexpectedDD(this.getArchive().getPath()));
      } else {
         this.app = (ApplicationBean)var1;
      }
   }

   protected void addModule(ModuleBean var1) throws InvalidModuleException, IOException, URISyntaxException {
      ModuleType var2 = null;
      String var3 = null;
      if (debug) {
         Debug.say("Adding embedded module");
      }

      if (var1.getEjb() != null) {
         var2 = ModuleType.EJB;
         var3 = var1.getEjb();
      } else if (var1.getJava() != null) {
         var2 = ModuleType.CAR;
         var3 = var1.getJava();
      } else if (var1.getWeb() != null) {
         var2 = ModuleType.WAR;
         var3 = var1.getWeb().getWebUri();
      } else if (var1.getConnector() != null) {
         var2 = ModuleType.RAR;
         var3 = var1.getConnector();
      }

      if (debug) {
         Debug.say("module type is : " + var2);
      }

      if (var2 != null) {
         if (this.getDeployableObject(var3) == null) {
            File var4 = this.getModulePath(var3);
            this.subModules.add(new WebLogicDeployableObject(var4, var2, this, var3, var1.getAltDd(), this.installDir.getInstallDir(), (File)null, (File)null, (LibrarySpec[])null, this.lazy));
         }
      }
   }

   protected File getModulePath(String var1) throws URISyntaxException {
      return new File(this.getVirtualJarFile().getEntry(var1).toString());
   }
}
