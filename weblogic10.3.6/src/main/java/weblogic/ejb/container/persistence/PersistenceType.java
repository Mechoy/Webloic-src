package weblogic.ejb.container.persistence;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import org.xml.sax.SAXParseException;
import weblogic.deployment.descriptors.IllegalValueException;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.cmp.rdbms.Deployer;
import weblogic.ejb.container.cmp.rdbms.EJBQLParsingException;
import weblogic.ejb.container.deployer.NamingConvention;
import weblogic.ejb.container.interfaces.BeanManager;
import weblogic.ejb.container.interfaces.CMPInfo;
import weblogic.ejb.container.interfaces.EntityBeanInfo;
import weblogic.ejb.container.persistence.spi.CMPBeanDescriptor;
import weblogic.ejb.container.persistence.spi.CMPCodeGenerator;
import weblogic.ejb.container.persistence.spi.CMPDeployer;
import weblogic.ejb.container.persistence.spi.JarDeployment;
import weblogic.ejb.container.persistence.spi.PersistenceManager;
import weblogic.ejb.spi.WLDeploymentException;
import weblogic.logging.Loggable;
import weblogic.utils.AssertionError;
import weblogic.utils.Debug;
import weblogic.utils.Getopt2;
import weblogic.utils.StackTraceUtils;
import weblogic.utils.jars.VirtualJarFactory;
import weblogic.utils.jars.VirtualJarFile;
import weblogic.xml.process.XMLParsingException;

public final class PersistenceType {
   private static final boolean debug = System.getProperty("weblogic.ejb.container.persistence.debug") != null;
   private static final boolean verbose = System.getProperty("weblogic.ejb.container.persistence.verbose") != null;
   private VirtualJarFile sourceJarFile;
   private PersistenceVendor vendor = null;
   private String typeIdentifier = null;
   private String typeVersion = null;
   private String weblogicVersion = null;
   private String cmpVersion = null;
   private String cmpDeployerClassName = null;
   private String persistenceManagerClassName = null;
   private String exclusiveManagerClassName = null;
   private String databaseManagerClassName = null;
   private String readonlyManagerClassName = null;
   private String jarDeploymentClassName = null;
   private String codeGeneratorClassName = null;
   private JarDeployment jarDeployment = null;
   private Class codeGeneratorClass;
   private Constructor codeGeneratorConstructor;
   private Getopt2 options;

   public PersistenceVendor getVendor() {
      return this.vendor;
   }

   public void setVendor(PersistenceVendor var1) {
      if (debug) {
         Debug.assertion(null != var1);
      }

      this.vendor = var1;
   }

   public String getIdentifier() {
      return this.typeIdentifier;
   }

   public void setIdentifier(String var1) {
      if (debug) {
         Debug.assertion(null != var1);
      }

      this.typeIdentifier = var1;
   }

   public String getVersion() {
      return this.typeVersion;
   }

   public void setVersion(String var1) {
      if (debug) {
         Debug.assertion(null != var1);
      }

      this.typeVersion = var1;
   }

   public String getWeblogicVersion() {
      return this.weblogicVersion;
   }

   public void setWeblogicVersion(String var1) {
      this.weblogicVersion = var1;
   }

   public String getCmpVersion() {
      return this.cmpVersion;
   }

   public void setCmpVersion(String var1) {
      this.cmpVersion = var1;
   }

   public void setCmpDeployerClassName(String var1) {
      this.cmpDeployerClassName = var1;
   }

   public CMPDeployer getCmpDeployer() {
      return (CMPDeployer)this.newInstance(this.cmpDeployerClassName);
   }

   public void setPersistenceManagerClassName(String var1) {
      this.persistenceManagerClassName = var1;
   }

   public PersistenceManager getPersistenceManager() {
      return (PersistenceManager)this.newInstance(this.persistenceManagerClassName);
   }

   public void setExclusiveManagerClassName(String var1) {
      this.exclusiveManagerClassName = var1;
   }

   public BeanManager getExclusiveManager() {
      return (BeanManager)this.newInstance(this.exclusiveManagerClassName);
   }

   public boolean hasExclusiveManager() {
      return this.exclusiveManagerClassName != null;
   }

   public void setDatabaseManagerClassName(String var1) {
      this.databaseManagerClassName = var1;
   }

   public BeanManager getDatabaseManager() {
      return (BeanManager)this.newInstance(this.databaseManagerClassName);
   }

   public boolean hasDatabaseManager() {
      return this.databaseManagerClassName != null;
   }

   public void setReadonlyManagerClassName(String var1) {
      this.readonlyManagerClassName = var1;
   }

   public BeanManager getReadonlyManager() {
      return (BeanManager)this.newInstance(this.readonlyManagerClassName);
   }

   public boolean hasReadonlyManager() {
      return this.readonlyManagerClassName != null;
   }

   public boolean hasJarDeployment() {
      return this.jarDeploymentClassName != null;
   }

   public void setJarDeploymentClassName(String var1) {
      this.jarDeploymentClassName = var1;
   }

   public JarDeployment getJarDeployment() {
      if (this.jarDeployment == null) {
         this.jarDeployment = (JarDeployment)this.newInstance(this.jarDeploymentClassName);
      }

      return this.jarDeployment;
   }

   public void setOptions(Getopt2 var1) {
      this.options = var1;
   }

   public boolean hasCodeGenerator() {
      return this.codeGeneratorClassName != null;
   }

   public void setCodeGeneratorClassName(String var1) {
      this.codeGeneratorClassName = var1;
      this.codeGeneratorClass = this.loadClass(this.codeGeneratorClassName);
      this.codeGeneratorConstructor = this.getConstructor(this.codeGeneratorClass, Getopt2.class);
   }

   public CMPCodeGenerator getCodeGenerator() {
      return this.codeGeneratorConstructor != null ? (CMPCodeGenerator)this.invokeConstructor(this.codeGeneratorConstructor, this.options) : (CMPCodeGenerator)this.newInstance(this.codeGeneratorClassName);
   }

   private Constructor getConstructor(Class var1, Class var2) {
      try {
         return var1.getConstructor(var2);
      } catch (NoSuchMethodException var4) {
         return null;
      } catch (Exception var5) {
         throw new AssertionError("should not reach.", var5);
      }
   }

   private Object invokeConstructor(Constructor var1, Object var2) {
      try {
         return var1.newInstance(var2);
      } catch (Exception var4) {
         throw new AssertionError("Should not reach.", var4);
      }
   }

   private Object newInstance(String var1) {
      try {
         return this.loadClass(var1).newInstance();
      } catch (Exception var3) {
         throw new AssertionError("Could not create an instance of class '" + var1 + "': " + StackTraceUtils.throwable2StackTrace(var3));
      }
   }

   private Class loadClass(String var1) {
      try {
         return Class.forName(var1);
      } catch (ClassNotFoundException var3) {
         throw new AssertionError("Could not load class '" + var1 + "': " + StackTraceUtils.throwable2StackTrace(var3));
      }
   }

   private Map createPersistenceParameters(EntityBeanInfo var1, File var2, Getopt2 var3) throws IllegalValueException {
      HashMap var4 = new HashMap();
      NamingConvention var5 = new NamingConvention(var1.getBeanClassName(), var1.getEJBName());
      CMPInfo var6 = var1.getCMPInfo();
      if (debug) {
         Debug.assertion(var6 != null);
      }

      Boolean var7 = new Boolean(var1.getCacheBetweenTransactions());
      var4.put(new String("bean.cacheBetweenTransactions"), var7);
      Boolean var8 = new Boolean(var6.findersLoadBean());
      var4.put(new String("bean.findersLoadBean"), var8);
      String var9 = new String(var5.getBeanPackageName());
      var4.put(new String("codegen.packageName"), var9);
      String var10 = new String(var5.getSimpleCmpBeanClassName(this.getIdentifier()));
      var4.put(new String("codegen.beanClassName"), var10);
      String var11 = new String(var10 + ".java");
      if (var9 != null && !var9.trim().equals("")) {
         var11 = var9.replace('.', File.separatorChar) + File.separator + var11;
      }

      File var12 = null;
      if (var2.isDirectory()) {
         var12 = new File(var2, var11);
      } else {
         var12 = new File(var11);
      }

      var4.put(new String("codegen.targetFile"), var12);
      var4.put(new String("codegen.opts"), var3 != null ? var3.asCommandArray() : null);
      return var4;
   }

   public CMPDeployer setupDeployer(EntityBeanInfo var1, File var2, Getopt2 var3, VirtualJarFile var4) throws WLDeploymentException {
      this.sourceJarFile = var4;
      CMPDeployer var5 = this.getCmpDeployer();

      try {
         if (this.hasJarDeployment()) {
            var5.setup(this.getJarDeployment());
         } else {
            var5.setup((JarDeployment)null);
         }

         Map var6 = var1.getCMPInfo().getAllBeanMap();
         CMPBeanDescriptor var11 = (CMPBeanDescriptor)var6.get(var1.getEJBName());
         var5.setCMPBeanDescriptor(var11);
         if (var1.getCMPInfo().uses20CMP()) {
            var5.setBeanMap(var1.getCMPInfo().getBeanMap());
            var5.setRelationships(var1.getCMPInfo().getRelationships());
            var5.setDependentMap(var1.getCMPInfo().getDependentMap());
         }

         Map var8 = this.createPersistenceParameters(var1, var2, var3);
         var5.setParameters(var8);
         this.setTypeSpecificFile(var1, var5);
         return var5;
      } catch (WLDeploymentException var9) {
         throw var9;
      } catch (Exception var10) {
         Loggable var7 = EJBLogger.logPersistenceTypeSetupErrorLoggable(var10);
         throw new WLDeploymentException(var7.getMessage(), var10);
      }
   }

   private void setTypeSpecificFile(EntityBeanInfo var1, CMPDeployer var2) throws Exception {
      if (debug) {
         Debug.assertion(var1 != null);
         Debug.assertion(var1.getCMPInfo() != null);
         Debug.assertion(var2 != null);
      }

      if (var1.getPersistenceUseStorage() == null) {
         throw new RuntimeException("Couldn't find the CMP description for your Entity Bean.\nPlease make sure you specified a <persistence-type> tag in weblogic-ejb-jar.xml.");
      } else {
         String var3 = var1.getPersistenceUseStorage();
         if (debug) {
            Debug.assertion(var3 != null);
         }

         boolean var4 = false;

         try {
            if (this.sourceJarFile == null) {
               this.sourceJarFile = VirtualJarFactory.createVirtualJar(new File(var1.getJarFileName()));
               var4 = true;
            }

            String var6;
            Loggable var7;
            try {
               var2.readTypeSpecificData(this.sourceJarFile, var3);
               if (var2 instanceof Deployer) {
                  var1.setCategoryCmpField(((Deployer)var2).getTypeSpecificData().getCategoryCmpField());
               }
            } catch (XMLParsingException var17) {
               var6 = var17.getMessage();
               if (var6 != null && !debug) {
                  Throwable var21 = var17.getNestedException();
                  if (var21 instanceof SAXParseException) {
                     int var22 = ((SAXParseException)var21).getLineNumber();
                     Loggable var9 = EJBLogger.logPersistenceTypeSetupErrorWithFileNameAndLineNumberLoggable(var6, var3, var22);
                     throw new WLDeploymentException(var9.getMessage(), var17);
                  }

                  Loggable var8 = EJBLogger.logPersistenceTypeSetupErrorWithFileNameLoggable(var6, var3);
                  throw new WLDeploymentException(var8.getMessage(), var17);
               }

               var6 = StackTraceUtils.throwable2StackTrace(var17);
               var7 = EJBLogger.logPersistenceTypeSetupErrorWithFileNameLoggable(var6, var3);
               throw new WLDeploymentException(var7.getMessage(), var17);
            } catch (EJBQLParsingException var18) {
               var6 = var18.getMessage();
               if (var6 == null || debug) {
                  var6 = StackTraceUtils.throwable2StackTrace(var18);
               }

               var7 = EJBLogger.logPersistenceTypeSetupEjbqlParsingErrorLoggable(var6, var3);
               throw new WLDeploymentException(var7.getMessage(), var18);
            } catch (Exception var19) {
               var6 = var19.getMessage();
               if (var6 != null && !debug) {
               }

               var6 = StackTraceUtils.throwable2StackTrace(var19);
               var7 = EJBLogger.logPersistenceTypeSetupErrorWithFileNameLoggable(var6, var3);
               throw new WLDeploymentException(var7.getMessage(), var19);
            }
         } finally {
            if (var4) {
               VirtualJarFile var12 = this.sourceJarFile;
               this.sourceJarFile = null;
               var12.close();
            }

         }

      }
   }

   private String jarNormalize(String var1) {
      if (var1.indexOf(File.separatorChar) != -1) {
         var1 = var1.replace(File.separatorChar, '/');
      }

      return var1;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer(300);
      var1.append("PersistenceType: {");
      var1.append("\n\tProvided by Vendor: " + this.vendor.getName());
      var1.append("\n\ttypeIdentifier: " + this.typeIdentifier);
      var1.append("\n\tversionNumber: " + this.typeVersion);
      var1.append("\n\tweblogicVersion: " + this.weblogicVersion);
      var1.append("\n\tcmpVersion: " + this.cmpVersion);
      var1.append("\n\tpersistenceManager: " + this.persistenceManagerClassName);
      var1.append("\n\texclusiveManagerClassName: " + this.exclusiveManagerClassName);
      var1.append("\n\tdatabaseManagerClassName: " + this.databaseManagerClassName);
      var1.append("\n\treadonlyManagerClassName: " + this.readonlyManagerClassName);
      var1.append("\n\tjarDeploymentClassName: " + this.jarDeploymentClassName);
      var1.append("\n\tcodeGeneratorClassName: " + this.codeGeneratorClassName);
      var1.append("\n} end PersistenceType\n");
      return var1.toString();
   }

   public boolean equals(Object var1) {
      if (null == var1) {
         return false;
      } else if (!(var1 instanceof PersistenceType)) {
         return false;
      } else {
         PersistenceType var2 = (PersistenceType)((PersistenceType)var1);
         if (!this.getVersion().equals(var2.getVersion())) {
            return false;
         } else {
            return this.getIdentifier().equals(var2.getIdentifier());
         }
      }
   }

   public int hashCode() {
      return this.typeVersion.hashCode() ^ this.typeIdentifier.hashCode();
   }
}
