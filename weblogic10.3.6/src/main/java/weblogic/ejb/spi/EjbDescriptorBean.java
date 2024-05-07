package weblogic.ejb.spi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.DescriptorManager;
import weblogic.descriptor.EditableDescriptorManager;
import weblogic.ejb.container.EJBLogger;
import weblogic.j2ee.descriptor.EjbJarBean;
import weblogic.j2ee.descriptor.wl.DeploymentPlanBean;
import weblogic.j2ee.descriptor.wl.EntityDescriptorBean;
import weblogic.j2ee.descriptor.wl.PersistenceBean;
import weblogic.j2ee.descriptor.wl.PersistenceUseBean;
import weblogic.j2ee.descriptor.wl.WeblogicEjbJarBean;
import weblogic.j2ee.descriptor.wl.WeblogicEnterpriseBeanBean;
import weblogic.j2ee.descriptor.wl.WeblogicRdbmsJarBean;
import weblogic.j2ee.descriptor.wl60.WeblogicRdbmsBeanBean;
import weblogic.logging.Loggable;
import weblogic.management.descriptors.DescriptorValidationException;
import weblogic.management.descriptors.TopLevelDescriptorMBean;
import weblogic.utils.jars.RandomAccessJarFile;

public class EjbDescriptorBean implements TopLevelDescriptorMBean {
   private static final long serialVersionUID = 4598155809773582508L;
   private String jarFileName;
   private boolean readOnly;
   private EjbJarBean ejbJar;
   private WeblogicEjbJarBean wlEjbJar;
   private Set rdbms11Jars;
   private Set rdbms20Jars;
   private String appName;
   private String uri;
   private DescriptorManager descriptorManager;
   private boolean isEjb30;
   private DeploymentPlanBean plan;
   private File configDir;
   private boolean isWeblogicEjbJarSynthetic;
   String destination;

   public EjbDescriptorBean() {
      this(false);
   }

   public void setEjb30(boolean var1) {
      this.isEjb30 = var1;
   }

   public boolean isEjb30() {
      if (this.ejbJar == null) {
         Loggable var1 = EJBLogger.logEjBJarBeanNotSetLoggable();
         throw new IllegalStateException(var1.getMessage());
      } else {
         return this.isEjb30;
      }
   }

   public EjbDescriptorBean(boolean var1) {
      this.rdbms11Jars = new HashSet();
      this.rdbms20Jars = new HashSet();
      this.isEjb30 = false;
      this.plan = null;
      this.configDir = null;
      this.isWeblogicEjbJarSynthetic = false;
      this.destination = null;
      this.readOnly = var1;
      if (var1) {
         this.descriptorManager = new DescriptorManager();
      } else {
         this.descriptorManager = new EditableDescriptorManager();
      }

   }

   public boolean isReadOnly() {
      return this.readOnly;
   }

   public EjbJarBean getEjbJarBean() {
      return this.ejbJar;
   }

   public void setEjbJarBean(EjbJarBean var1) {
      this.ejbJar = var1;
      String var2 = ((DescriptorBean)var1).getDescriptor().getOriginalVersionInfo();
      if (var2.equals("3.0")) {
         this.isEjb30 = true;
      }

   }

   public EjbJarBean createEjbJarBean() {
      EjbJarBean var1 = (EjbJarBean)this.descriptorManager.createDescriptorRoot(EjbJarBean.class).getRootBean();
      this.setEjbJarBean(var1);
      return var1;
   }

   public WeblogicEjbJarBean getWeblogicEjbJarBean() {
      return this.wlEjbJar;
   }

   public void setWeblogicEjbJarBean(WeblogicEjbJarBean var1) {
      this.wlEjbJar = var1;
   }

   public WeblogicEjbJarBean createWeblogicEjbJarBean() {
      return this.createWeblogicEjbJarBean((String)null);
   }

   public WeblogicEjbJarBean createWeblogicEjbJarBean(String var1) {
      WeblogicEjbJarBean var2 = (WeblogicEjbJarBean)this.descriptorManager.createDescriptorRoot(WeblogicEjbJarBean.class, var1).getRootBean();
      this.setWeblogicEjbJarBean(var2);
      return var2;
   }

   public WeblogicRdbmsJarBean[] getWeblogicRdbmsJarBeans() {
      return (WeblogicRdbmsJarBean[])((WeblogicRdbmsJarBean[])this.rdbms20Jars.toArray(new WeblogicRdbmsJarBean[0]));
   }

   public void setWeblogicRdbmsJarBeans(WeblogicRdbmsJarBean[] var1) {
      this.rdbms20Jars.clear();
      if (var1 != null) {
         for(int var2 = 0; var2 < var1.length; ++var2) {
            this.rdbms20Jars.add(var1[var2]);
         }
      }

   }

   public void addWeblogicRdbmsJarBean(WeblogicRdbmsJarBean var1) {
      this.rdbms20Jars.add(var1);
   }

   public void removeWeblogicRdbmsJarBean(WeblogicRdbmsJarBean var1) {
      this.rdbms20Jars.remove(var1);
   }

   public WeblogicRdbmsJarBean createWeblogicRdbmsJarBean() {
      return this.createWeblogicRdbmsJarBean((String)null);
   }

   public WeblogicRdbmsJarBean createWeblogicRdbmsJarBean(String var1) {
      WeblogicRdbmsJarBean var2 = (WeblogicRdbmsJarBean)this.descriptorManager.createDescriptorRoot(WeblogicRdbmsJarBean.class, var1).getRootBean();
      this.addWeblogicRdbmsJarBean(var2);
      return var2;
   }

   public WeblogicRdbmsJarBean getWeblogicRdbmsJarBean(String var1) {
      Object var2 = this.getFileNameToRdbmsDescriptorMap().get(var1);
      return var2 instanceof WeblogicRdbmsJarBean ? (WeblogicRdbmsJarBean)var2 : null;
   }

   public weblogic.j2ee.descriptor.wl60.WeblogicRdbmsJarBean[] getWeblogicRdbms11JarBeans() {
      return (weblogic.j2ee.descriptor.wl60.WeblogicRdbmsJarBean[])((weblogic.j2ee.descriptor.wl60.WeblogicRdbmsJarBean[])this.rdbms11Jars.toArray(new weblogic.j2ee.descriptor.wl60.WeblogicRdbmsJarBean[0]));
   }

   public void setWeblogicRdbms11JarBeans(weblogic.j2ee.descriptor.wl60.WeblogicRdbmsJarBean[] var1) {
      this.rdbms11Jars.clear();
      if (var1 != null) {
         for(int var2 = 0; var2 < var1.length; ++var2) {
            this.rdbms11Jars.add(var1[var2]);
         }
      }

   }

   public void addWeblogicRdbms11JarBean(weblogic.j2ee.descriptor.wl60.WeblogicRdbmsJarBean var1) {
      this.rdbms11Jars.add(var1);
   }

   public void removeWeblogicRdbms11JarBean(weblogic.j2ee.descriptor.wl60.WeblogicRdbmsJarBean var1) {
      this.rdbms11Jars.remove(var1);
   }

   public weblogic.j2ee.descriptor.wl60.WeblogicRdbmsJarBean createWeblogicRdbms11JarBean() {
      return this.createWeblogicRdbms11JarBean((String)null);
   }

   public weblogic.j2ee.descriptor.wl60.WeblogicRdbmsJarBean createWeblogicRdbms11JarBean(String var1) {
      weblogic.j2ee.descriptor.wl60.WeblogicRdbmsJarBean var2 = (weblogic.j2ee.descriptor.wl60.WeblogicRdbmsJarBean)this.descriptorManager.createDescriptorRoot(weblogic.j2ee.descriptor.wl60.WeblogicRdbmsJarBean.class, var1).getRootBean();
      this.addWeblogicRdbms11JarBean(var2);
      return var2;
   }

   public String getJarFileName() {
      return this.jarFileName;
   }

   public void setJarFileName(String var1) {
      this.jarFileName = var1;
   }

   public Set getRDBMSDescriptorFileNames() {
      return new HashSet();
   }

   public String getParsingErrorMessage() {
      return null;
   }

   public void setParsingErrorMessage(String var1) {
   }

   public String getAppName() {
      return this.appName;
   }

   public void setAppName(String var1) {
      this.appName = var1;
   }

   public String getUri() {
      return this.uri;
   }

   public void setUri(String var1) {
      this.uri = var1;
   }

   public DeploymentPlanBean getDeploymentPlan() {
      return this.plan;
   }

   public void setDeploymentPlan(DeploymentPlanBean var1) {
      this.plan = var1;
   }

   public File getConfigDirectory() {
      return this.configDir;
   }

   public void setConfigDirectory(File var1) {
      this.configDir = var1;
   }

   public String getName() {
      return null;
   }

   public void setName(String var1) {
   }

   public String toXML(int var1) {
      return "";
   }

   public void register() {
   }

   public void unregister() {
   }

   public void validate() throws DescriptorValidationException {
   }

   public void usePersistenceDestination(String var1) {
      this.destination = var1;
   }

   public void persist() throws IOException {
      if (this.destination == null) {
         throw new RuntimeException("No persistentDestination set!");
      } else {
         File var1 = new File(this.destination);
         if (var1.isDirectory()) {
            this.persistToDirectory(var1);
         } else {
            this.persistToJarFile(var1);
         }

      }
   }

   private void persistDescriptor(File var1, String var2, Object var3) throws IOException {
      if (!((DescriptorBean)var3).isEditable()) {
         throw new IOException("Error: " + var2 + " is not editable");
      } else {
         File var4 = new File(var1, var2);
         var4.getParentFile().mkdirs();
         FileOutputStream var5 = null;

         try {
            var5 = new FileOutputStream(var4);
            DescriptorBean var6 = (DescriptorBean)var3;
            var6.getDescriptor().toXML(var5);
         } finally {
            if (var5 != null) {
               var5.close();
            }

         }

      }
   }

   private void persistToDirectory(File var1) throws IOException {
      this.persistDescriptor((File)var1, "META-INF/ejb-jar.xml", this.ejbJar);
      if (this.wlEjbJar != null) {
         this.persistDescriptor((File)var1, "META-INF/weblogic-ejb-jar.xml", this.wlEjbJar);
      }

      Map var2 = this.getFileNameToRdbmsDescriptorMap();
      Set var3 = var2.keySet();
      Iterator var4 = var3.iterator();

      while(var4.hasNext()) {
         String var5 = (String)var4.next();
         Object var6 = var2.get(var5);
         if (var6 instanceof WeblogicRdbmsJarBean) {
            this.persistDescriptor((File)var1, var5, (WeblogicRdbmsJarBean)var6);
         } else {
            this.persistDescriptor((File)var1, var5, (weblogic.j2ee.descriptor.wl60.WeblogicRdbmsJarBean)var6);
         }
      }

   }

   private void persistDescriptor(RandomAccessJarFile var1, String var2, Object var3) throws IOException {
      OutputStream var4 = null;

      try {
         DescriptorBean var5 = (DescriptorBean)var3;
         if (var5.isEditable()) {
            var4 = var1.writeEntry(var2, true);
            var5.getDescriptor().toXML(var4);
         }
      } finally {
         if (var4 != null) {
            var4.close();
         }

      }

   }

   private void persistToJarFile(File var1) throws IOException {
      String var2 = ".";
      RandomAccessJarFile var3 = new RandomAccessJarFile(new File(var2), var1);

      try {
         this.persistDescriptor((RandomAccessJarFile)var3, "META-INF/ejb-jar.xml", this.ejbJar);
         if (this.wlEjbJar != null) {
            this.persistDescriptor((RandomAccessJarFile)var3, "META-INF/weblogic-ejb-jar.xml", this.wlEjbJar);
         }

         Map var4 = this.getFileNameToRdbmsDescriptorMap();
         Set var5 = var4.keySet();
         Iterator var6 = var5.iterator();

         while(var6.hasNext()) {
            String var7 = (String)var6.next();
            Object var8 = var4.get(var7);
            if (var8 instanceof WeblogicRdbmsJarBean) {
               this.persistDescriptor((RandomAccessJarFile)var3, var7, (WeblogicRdbmsJarBean)var8);
            } else {
               this.persistDescriptor((RandomAccessJarFile)var3, var7, (weblogic.j2ee.descriptor.wl60.WeblogicRdbmsJarBean)var8);
            }
         }
      } finally {
         if (var3 != null) {
            var3.close();
         }

      }

   }

   public Map getFileNameToRdbmsDescriptorMap() {
      if (this.wlEjbJar == null) {
         return new HashMap();
      } else {
         HashMap var1 = new HashMap();
         WeblogicEnterpriseBeanBean[] var2 = this.wlEjbJar.getWeblogicEnterpriseBeans();

         for(int var3 = 0; var3 < var2.length; ++var3) {
            if (var2[var3].getEntityDescriptor() != null) {
               EntityDescriptorBean var4 = var2[var3].getEntityDescriptor();
               if (var4.getPersistence() != null) {
                  PersistenceBean var5 = var4.getPersistence();
                  if (var5 != null) {
                     PersistenceUseBean var6 = var5.getPersistenceUse();
                     if (var6 != null) {
                        var1.put(var2[var3].getEjbName(), var6.getTypeStorage());
                     }
                  }
               }
            }
         }

         HashMap var7 = new HashMap();
         Iterator var8 = this.rdbms11Jars.iterator();

         while(var8.hasNext()) {
            weblogic.j2ee.descriptor.wl60.WeblogicRdbmsJarBean var9 = (weblogic.j2ee.descriptor.wl60.WeblogicRdbmsJarBean)var8.next();
            WeblogicRdbmsBeanBean[] var11 = var9.getWeblogicRdbmsBeans();
            if (var11.length != 0) {
               var7.put(var1.get(var11[0].getEjbName()), var9);
            }
         }

         var8 = this.rdbms20Jars.iterator();

         while(var8.hasNext()) {
            WeblogicRdbmsJarBean var10 = (WeblogicRdbmsJarBean)var8.next();
            weblogic.j2ee.descriptor.wl.WeblogicRdbmsBeanBean[] var12 = var10.getWeblogicRdbmsBeans();
            if (var12.length != 0) {
               var7.put(var1.get(var12[0].getEjbName()), var10);
            }
         }

         return var7;
      }
   }

   public void persist(Properties var1) throws IOException {
   }

   public boolean isWeblogicEjbJarSynthetic() {
      return this.isWeblogicEjbJarSynthetic;
   }

   public void markWeblogicEjbJarSynthetic() {
      this.isWeblogicEjbJarSynthetic = true;
   }
}
