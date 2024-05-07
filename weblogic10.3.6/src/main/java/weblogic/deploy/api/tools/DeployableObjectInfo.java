package weblogic.deploy.api.tools;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import javax.enterprise.deploy.model.DDBeanRoot;
import javax.enterprise.deploy.model.DeployableObject;
import javax.enterprise.deploy.model.exceptions.DDBeanCreateException;
import javax.enterprise.deploy.shared.ModuleType;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.model.WebLogicDDBeanRoot;
import weblogic.deploy.api.model.WebLogicDeployableObject;
import weblogic.deploy.api.model.WebLogicJ2eeApplicationObject;
import weblogic.deploy.api.model.internal.DDBeanRootImpl;
import weblogic.deploy.api.model.sca.internal.WebLogicScaApplicationObject;
import weblogic.deploy.api.shared.WebLogicModuleType;
import weblogic.deploy.api.spi.WebLogicDConfigBeanRoot;
import weblogic.deploy.api.spi.WebLogicDeploymentConfiguration;
import weblogic.deploy.api.spi.config.BasicDConfigBeanRoot;
import weblogic.descriptor.DescriptorBean;
import weblogic.j2ee.descriptor.ApplicationBean;
import weblogic.j2ee.descriptor.EjbJarBean;
import weblogic.j2ee.descriptor.EnterpriseBeansBean;
import weblogic.j2ee.descriptor.EntityBeanBean;
import weblogic.j2ee.descriptor.MessageDrivenBeanBean;
import weblogic.j2ee.descriptor.ModuleBean;
import weblogic.j2ee.descriptor.SessionBeanBean;
import weblogic.j2ee.descriptor.WebBean;
import weblogic.j2ee.descriptor.WebserviceDescriptionBean;
import weblogic.j2ee.descriptor.WebservicesBean;
import weblogic.j2ee.descriptor.wl.JMSBean;
import weblogic.j2ee.descriptor.wl.WeblogicApplicationBean;
import weblogic.j2ee.descriptor.wl.WeblogicModuleBean;
import weblogic.j2ee.descriptor.wl.WeblogicWebAppBean;
import weblogic.jms.module.DefaultingHelper;
import weblogic.utils.application.WarDetector;

public class DeployableObjectInfo extends ModuleInfo {
   private static final String[] WSEE_URIS = new String[]{"WEB-INF/webservices.xml", "META-INF/webservices.xml", "WEB-INF/web-services.xml", "META-INF/web-services.xml"};
   private WebLogicDeployableObject dobj = null;
   private WebLogicDeploymentConfiguration dc = null;

   protected DeployableObjectInfo(WebLogicDeployableObject var1, WebLogicDeploymentConfiguration var2, String var3) throws IOException, ConfigurationException {
      this.name = var3;
      if (var3 == null) {
         this.name = var1.getArchive().getName();
      }

      this.type = var1.getType();
      this.archived = this.checkIfArchived(var1);
      this.dobj = var1;
      this.dc = var2;
      this.addSubModules();
      this.addWlsModules();
      this.addWebServices();
   }

   public String[] getBeans() {
      if (this.beans != null) {
         return this.beans;
      } else if (this.dobj.getType().getValue() != ModuleType.EJB.getValue()) {
         return null;
      } else {
         try {
            DescriptorBean var1 = ((WebLogicDDBeanRoot)this.dobj.getDDBeanRoot()).getDescriptorBean();
            if (var1 == null) {
               return null;
            }

            EnterpriseBeansBean var2 = ((EjbJarBean)var1).getEnterpriseBeans();
            SessionBeanBean[] var3 = var2.getSessions();
            ArrayList var4 = new ArrayList();

            int var5;
            for(var5 = 0; var5 < var3.length; ++var5) {
               SessionBeanBean var6 = var3[var5];
               var4.add(var6.getEjbName());
            }

            EntityBeanBean[] var8 = var2.getEntities();

            for(var5 = 0; var5 < var8.length; ++var5) {
               EntityBeanBean var10 = var8[var5];
               var4.add(var10.getEjbName());
            }

            MessageDrivenBeanBean[] var9 = var2.getMessageDrivens();

            for(var5 = 0; var5 < var9.length; ++var5) {
               MessageDrivenBeanBean var11 = var9[var5];
               var4.add(var11.getEjbName());
            }

            this.beans = (String[])((String[])var4.toArray(new String[0]));
         } catch (IOException var7) {
         }

         return this.beans;
      }
   }

   public String[] getSubDeployments() {
      if (this.subDeployments != null) {
         return this.subDeployments;
      } else {
         if (WebLogicModuleType.JMS.equals(this.type)) {
            try {
               BasicDConfigBeanRoot var1 = (BasicDConfigBeanRoot)this.dc.getDConfigBeanRoot(this.dobj.getDDBeanRoot());
               JMSBean var2 = (JMSBean)var1.getDescriptorBean();
               this.subDeployments = DefaultingHelper.getSubDeploymentNames(var2);
            } catch (ConfigurationException var3) {
               throw new Error("Impossible Exception " + var3);
            }
         }

         return this.subDeployments;
      }
   }

   public String[] getContextRoots() {
      if (this.roots != null) {
         return this.roots;
      } else if (this.dobj.getType().getValue() != ModuleType.WAR.getValue()) {
         return null;
      } else {
         if (this.getParent() == null) {
            this.roots = this.getStandAloneContextRoots();
         } else {
            String var1 = this.dobj.getContextRoot();
            this.roots = new String[]{var1};
         }

         return this.roots;
      }
   }

   private void addSubModules() throws IOException, ConfigurationException {
      if (this.getType() == ModuleType.EAR) {
         this.addEarSubModules();
      } else if (this.getType() == WebLogicModuleType.SCA_COMPOSITE) {
         this.addScaSubModules();
      }

   }

   private void addEarSubModules() throws IOException, ConfigurationException {
      WebLogicJ2eeApplicationObject var1 = (WebLogicJ2eeApplicationObject)this.dobj;
      String[] var2 = var1.getModuleUris();
      if (var2 != null) {
         ArrayList var3 = new ArrayList();

         for(int var4 = 0; var4 < var2.length; ++var4) {
            String var5 = var2[var4];
            if (!var3.contains(var5)) {
               DeployableObject[] var6 = var1.getDeployableObjects(var5);

               for(int var7 = 0; var7 < var6.length; ++var7) {
                  this.addModuleInfo(ModuleInfo.createModuleInfo((WebLogicDeployableObject)var6[var7], (WebLogicDeploymentConfiguration)null, var5));
               }

               var3.add(var2[var4]);
            }
         }

      }
   }

   private void addScaSubModules() throws IOException, ConfigurationException {
      WebLogicScaApplicationObject var1 = (WebLogicScaApplicationObject)this.dobj;
      String[] var2 = var1.getModuleUris();
      if (var2 != null) {
         ArrayList var3 = new ArrayList();

         for(int var4 = 0; var4 < var2.length; ++var4) {
            String var5 = var2[var4];
            if (!var3.contains(var5)) {
               DeployableObject[] var6 = var1.getDeployableObjects(var5);

               for(int var7 = 0; var7 < var6.length; ++var7) {
                  this.addModuleInfo(ModuleInfo.createModuleInfo((WebLogicDeployableObject)var6[var7], (WebLogicDeploymentConfiguration)null, var5));
               }

               var3.add(var2[var4]);
            }
         }

      }
   }

   private String[] getContextRootsFromEar() {
      ArrayList var1 = new ArrayList();
      WebLogicDeployableObject var2 = ((DeployableObjectInfo)this.getParent()).getDeployableObject();

      try {
         ApplicationBean var3 = (ApplicationBean)var2.getDescriptorBean();
         ModuleBean[] var4 = var3.getModules();

         for(int var5 = 0; var5 < var4.length; ++var5) {
            ModuleBean var6 = var4[var5];
            WebBean var7 = var6.getWeb();
            if (var7 != null && var7.getWebUri().equals(this.getName())) {
               var1.add(var6.getWeb().getContextRoot());
            }
         }
      } catch (IOException var8) {
      }

      return (String[])((String[])var1.toArray(new String[0]));
   }

   private String[] getStandAloneContextRoots() {
      if (this.dc == null) {
         return new String[]{this.sansExtension(this.getName())};
      } else {
         String var1 = null;

         try {
            WebLogicDConfigBeanRoot var2 = (WebLogicDConfigBeanRoot)this.dc.getDConfigBeanRoot(this.dobj.getDDBeanRoot());
            if (var2 != null) {
               WeblogicWebAppBean var3 = (WeblogicWebAppBean)var2.getDescriptorBean();
               var1 = var3.getContextRoots().length > 0 ? var3.getContextRoots()[0] : null;
            }
         } catch (ConfigurationException var4) {
         }

         return var1 == null ? new String[]{this.sansExtension(this.getName())} : new String[]{var1};
      }
   }

   private String sansExtension(String var1) {
      return WarDetector.instance.stem(var1);
   }

   private void addWebServices() {
      try {
         for(int var1 = 0; var1 < WSEE_URIS.length; ++var1) {
            String var2 = WSEE_URIS[var1];
            if (this.dobj.hasDDBean(var2)) {
               this.extractWebServices(var2);
               return;
            }
         }
      } catch (IOException var3) {
      }

   }

   protected WebLogicDeployableObject getDeployableObject() {
      return this.dobj;
   }

   private void extractWebServices(String var1) throws IOException {
      DDBeanRootImpl var2;
      try {
         var2 = (DDBeanRootImpl)this.dobj.getDDBeanRoot(var1);
      } catch (DDBeanCreateException var8) {
         return;
      }

      ArrayList var3 = new ArrayList();
      WebservicesBean var4 = (WebservicesBean)var2.getDescriptorBean();
      if (var4 != null) {
         WebserviceDescriptionBean[] var5 = var4.getWebserviceDescriptions();

         for(int var6 = 0; var6 < var5.length; ++var6) {
            WebserviceDescriptionBean var7 = var5[var6];
            var3.add(var7.getWebserviceDescriptionName());
         }
      }

      this.webservices = (String[])((String[])var3.toArray(new String[0]));
   }

   private void addWlsModules() throws ConfigurationException {
      if (this.dobj.getType() == ModuleType.EAR) {
         if (this.dc != null) {
            WebLogicDConfigBeanRoot var1 = (WebLogicDConfigBeanRoot)this.dc.getDConfigBeanRoot(this.dobj.getDDBeanRoot());
            this.addModules((WeblogicApplicationBean)var1.getDescriptorBean());
         }

      }
   }

   private void addModules(WeblogicApplicationBean var1) {
      WeblogicModuleBean[] var2 = var1.getModules();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         WeblogicModuleBean var4 = var2[var3];
         this.addModuleInfo(var4, this.getDescriptorBean(var4));
      }

   }

   private DescriptorBean getDescriptorBean(WeblogicModuleBean var1) {
      DescriptorBean var2 = null;

      try {
         DDBeanRoot var3 = this.getDDBeanRoot(var1);
         if (var3 != null && this.dc != null && this.dobj != null) {
            BasicDConfigBeanRoot var4 = (BasicDConfigBeanRoot)this.dc.getDConfigBeanRoot(this.dobj.getDDBeanRoot());
            BasicDConfigBeanRoot var5 = (BasicDConfigBeanRoot)var4.getDConfigBean(var3);
            var2 = var5.getDescriptorBean();
         }

         return var2;
      } catch (ConfigurationException var6) {
         return var2;
      }
   }

   private DDBeanRoot getDDBeanRoot(WeblogicModuleBean var1) {
      try {
         return this.dobj.getDDBeanRoot(var1.getPath());
      } catch (FileNotFoundException var3) {
         return null;
      } catch (DDBeanCreateException var4) {
         return null;
      }
   }
}
