package weblogic.deployment;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import javax.naming.NamingException;
import javax.persistence.PersistenceException;
import javax.xml.stream.XMLStreamException;
import kodo.jdbc.conf.descriptor.PersistenceConfigurationBean;
import kodo.jdbc.conf.descriptor.PersistenceUnitConfigurationBean;
import weblogic.application.ApplicationContextInternal;
import weblogic.application.utils.PersistenceUtils;
import weblogic.descriptor.Descriptor;
import weblogic.descriptor.DescriptorBean;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.j2ee.descriptor.PersistenceBean;
import weblogic.j2ee.descriptor.PersistenceUnitBean;
import weblogic.j2ee.descriptor.wl.DeploymentPlanBean;
import weblogic.kodo.monitoring.KodoPersistenceUnitParent;
import weblogic.utils.StackTraceUtils;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.jars.VirtualJarFile;

public abstract class AbstractPersistenceUnitRegistry implements PersistenceUnitRegistry {
   private static final DebugLogger logger = DebugLogger.getDebugLogger("DebugKodoWeblogic");
   protected Map persistenceUnits;
   protected ApplicationContextInternal appCtx;
   private final Map descriptorMap;
   private final boolean isDebug;
   private File[] rootFiles;
   private File[] canonicalizedRootFiles;
   private final GenericClassLoader scopeClassLoader;
   private final String scopeName;
   private final File configDir;
   private final DeploymentPlanBean planBean;

   public AbstractPersistenceUnitRegistry(GenericClassLoader var1, String var2, File var3, DeploymentPlanBean var4) {
      this(var1, var2, var3, var4, (ApplicationContextInternal)null);
   }

   public AbstractPersistenceUnitRegistry(GenericClassLoader var1, String var2, File var3, DeploymentPlanBean var4, ApplicationContextInternal var5) {
      this.persistenceUnits = new HashMap();
      this.descriptorMap = new HashMap();
      this.isDebug = Boolean.getBoolean("weblogic.deployment.PersistenceRegistry");
      this.rootFiles = null;
      this.canonicalizedRootFiles = null;
      this.appCtx = var5;
      this.scopeClassLoader = var1;
      this.scopeName = var2;
      this.configDir = var3;
      this.planBean = var4;
   }

   public void setParentRuntimeMBean(KodoPersistenceUnitParent var1) throws EnvironmentException {
      Collection var2 = this.persistenceUnits.values();
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         PersistenceUnitInfoImpl var4 = (PersistenceUnitInfoImpl)var3.next();
         var4.setParentRuntimeMBean(var1);
      }

   }

   public String getQualifiedName() {
      return this.getScopeName();
   }

   protected String getScopeName() {
      return this.scopeName;
   }

   public void loadPersistenceDescriptors(boolean var1) throws EnvironmentException, IllegalAccessException, InstantiationException, ClassNotFoundException, NamingException, IOException, XMLStreamException {
      Map var2 = this.loadPersistenceDescriptor("META-INF/persistence.xml", var1);
      Map var3 = this.loadPersistenceDescriptor("META-INF/persistence-configuration.xml", var1);
      if (var1) {
         this.storeDescriptors(var2, var3);
      }

   }

   private File[] getRootFiles(boolean var1) throws IOException {
      if (var1) {
         if (this.canonicalizedRootFiles == null) {
            this.canonicalizedRootFiles = PersistenceUtils.getApplicationRoots(this.scopeClassLoader, this.scopeName, var1);
         }

         return this.canonicalizedRootFiles;
      } else {
         if (this.rootFiles == null) {
            this.rootFiles = PersistenceUtils.getApplicationRoots(this.scopeClassLoader, this.scopeName, var1);
         }

         return this.rootFiles;
      }
   }

   private Map loadPersistenceDescriptor(String var1, boolean var2) throws EnvironmentException {
      Map var4 = null;

      Enumeration var3;
      try {
         var3 = this.scopeClassLoader.getResources(var1);
      } catch (IOException var13) {
         if (this.isDebug) {
            this.debug("Unable to find " + var1);
         }

         return var4;
      }

      HashSet var5 = new HashSet();

      while(var3.hasMoreElements()) {
         URL var6 = (URL)var3.nextElement();
         URI var7 = null;
         URI var8 = null;

         File[] var9;
         try {
            var8 = PersistenceDescriptorLoader.getResourceURI(var6);
            if (var5.contains(var8)) {
               continue;
            }

            var5.add(var8);
            var9 = this.getRootFiles(true);
            var7 = PersistenceDescriptorLoader.getRelativeURI(var9, var8);
         } catch (IOException var16) {
            throw new EnvironmentException("Error scanning module " + this.getScopeName() + " for persistence descriptors: " + StackTraceUtils.throwable2StackTrace(var16));
         }

         if (!var7.equals(var8)) {
            var9 = null;

            DescriptorBean var17;
            try {
               PersistenceDescriptorLoader var10 = new PersistenceDescriptorLoader(var6, this.configDir, this.planBean, this.scopeName, var7.toString());
               var17 = var10.loadDescriptorBean();
            } catch (Exception var12) {
               throw new EnvironmentException("Error loading the persistence descriptor " + var7 + " from the module " + this.getScopeName() + ".  See the following stack trace for nested errors: " + StackTraceUtils.throwable2StackTrace(var12));
            }

            if (var17 != null) {
               this.descriptorMap.put(var7.toString(), var17.getDescriptor());
               if (var2) {
                  try {
                     URL var18 = this.toRootURL(var6);
                     URL var11 = this.toJarParentURL(var18);
                     var4 = this.processDescriptor(var17, var18, var11, var4);
                  } catch (MalformedURLException var14) {
                     if (this.isDebug) {
                        this.debug("Unable to process resourceURL " + var6, var14);
                     }
                  } catch (URISyntaxException var15) {
                     if (this.isDebug) {
                        this.debug("Unable to process resourceURL " + var6, var15);
                     }
                  }
               }
            }
         } else if (this.isDebug) {
            this.debug("Unable to find relative root for " + var8);
         }
      }

      return var4;
   }

   public void loadPersistenceDescriptor(VirtualJarFile var1, boolean var2, File var3) throws EnvironmentException {
      DescriptorBean var4 = this.loadPersistenceDescriptor(var1, "META-INF/persistence.xml");
      DescriptorBean var5 = this.loadPersistenceDescriptor(var1, "META-INF/persistence-configuration.xml");
      if (var2) {
         URL var6 = null;
         URL var7 = null;

         try {
            var6 = var3.toURL();
            var7 = var3.getAbsoluteFile().getParentFile().toURL();
         } catch (MalformedURLException var10) {
            throw new EnvironmentException("Error computing URL: " + var10);
         }

         Map var8 = null;
         Map var9 = null;
         if (var4 != null) {
            var8 = this.processDescriptor(var4, var6, var7, (Map)null);
         }

         if (var5 != null) {
            var9 = this.processDescriptor(var5, var6, var7, (Map)null);
         }

         this.storeDescriptors(var8, var9);
      }

   }

   private DescriptorBean loadPersistenceDescriptor(VirtualJarFile var1, String var2) throws EnvironmentException {
      try {
         PersistenceDescriptorLoader var3 = new PersistenceDescriptorLoader(var1, this.configDir, this.planBean, this.scopeName, var2);
         DescriptorBean var4 = var3.loadDescriptorBean();
         if (var4 != null) {
            this.descriptorMap.put(var2, var4.getDescriptor());
         }

         return var4;
      } catch (Exception var5) {
         throw new EnvironmentException("Error loading the persistence descriptor " + var2 + " from the module " + this.getScopeName() + ".  See the following stack trace for nested errors: " + StackTraceUtils.throwable2StackTrace(var5));
      }
   }

   private Map processDescriptor(DescriptorBean var1, URL var2, URL var3, Map var4) throws EnvironmentException {
      if (var4 == null) {
         var4 = new HashMap();
      }

      String var5;
      BeanInfo var6;
      int var9;
      if (var1 instanceof PersistenceBean) {
         PersistenceBean var7 = (PersistenceBean)var1;
         PersistenceUnitBean[] var8 = var7.getPersistenceUnits();

         for(var9 = 0; var9 < var8.length; ++var9) {
            var5 = var8[var9].getName();
            this.assertNoDuplicate((Map)var4, var5, var2);
            var6 = new BeanInfo(var8[var9], var2, var3, var1);
            ((Map)var4).put(var5, var6);
         }
      } else {
         PersistenceConfigurationBean var10 = (PersistenceConfigurationBean)var1;
         PersistenceUnitConfigurationBean[] var11 = var10.getPersistenceUnitConfigurations();

         for(var9 = 0; var9 < var11.length; ++var9) {
            var5 = var11[var9].getName();
            this.assertNoDuplicate((Map)var4, var5, var2);
            var6 = new BeanInfo(var11[var9], var2, var3, var1);
            ((Map)var4).put(var5, var6);
         }
      }

      return (Map)var4;
   }

   private void assertNoDuplicate(Map var1, String var2, URL var3) throws EnvironmentException {
      BeanInfo var4 = (BeanInfo)var1.get(var2);
      if (var4 != null) {
         throw new EnvironmentException("duplicate persistence units with name " + var2 + " in scope " + this.scopeName + ". First PU location: " + var4.rootUrl + ". Second PU location: " + var3);
      }
   }

   protected void storeDescriptors(Map var1, Map var2) throws EnvironmentException {
      if (var1 != null) {
         Iterator var7 = var1.values().iterator();

         while(var7.hasNext()) {
            BeanInfo var3 = (BeanInfo)var7.next();
            PersistenceUnitBean var5 = (PersistenceUnitBean)var3.bean;
            String var8 = ((PersistenceBean)var3.rootBean).getOriginalVersion();
            BeanInfo var4 = var2 == null ? null : (BeanInfo)var2.get(var5.getName());
            PersistenceUnitConfigurationBean var6 = var4 == null ? null : (PersistenceUnitConfigurationBean)var4.bean;

            try {
               this.putPersistenceUnit(new PersistenceUnitInfoImpl(var5, var6, this.scopeClassLoader, this.getQualifiedName(), var3.rootUrl, var3.jarParentUrl, var8, this.appCtx));
            } catch (EnvironmentException var11) {
               EnvironmentException var10 = new EnvironmentException("Error processing persistence unit " + var5.getName() + " of module " + this.scopeName + ": " + var11.getMessage());
               var10.setStackTrace(var11.getStackTrace());
               throw var10;
            }
         }

      }
   }

   void putPersistenceUnit(PersistenceUnitInfoImpl var1) throws EnvironmentException {
      String var2 = var1.getPersistenceUnitName();
      PersistenceUnitInfoImpl var3 = (PersistenceUnitInfoImpl)this.persistenceUnits.put(var2, var1);
      if (var3 != null) {
         throw new EnvironmentException("duplicate persistence units with name " + var2 + " in scope " + this.scopeName + ". First PU location: " + var3.getPersistenceUnitRootUrl() + ". Second PU location: " + var1.getPersistenceUnitRootUrl());
      }
   }

   public void close() {
      Iterator var1 = this.persistenceUnits.values().iterator();

      while(var1.hasNext()) {
         try {
            ((PersistenceUnitInfoImpl)var1.next()).close();
         } catch (PersistenceException var3) {
            var3.printStackTrace();
         }
      }

   }

   public Collection getPersistenceUnitNames() {
      HashSet var1 = new HashSet();
      var1.addAll(this.persistenceUnits.keySet());
      return var1;
   }

   public Iterator getDescriptorURIs() {
      return this.descriptorMap.keySet().iterator();
   }

   public Descriptor getDescriptor(String var1) {
      return (Descriptor)this.descriptorMap.get(var1);
   }

   protected URL toRootURL(URL var1) throws MalformedURLException, URISyntaxException {
      if ("file".equals(var1.getProtocol())) {
         return new URL(var1, "..");
      } else if (!"jar".equals(var1.getProtocol()) && !"zip".equals(var1.getProtocol())) {
         throw new IllegalArgumentException("Unsupported URL format: " + var1);
      } else {
         String var2 = var1.toString();
         if ("zip".equals(var1.getProtocol())) {
            if (var2.startsWith("zip:/")) {
               var2 = var2.replaceFirst("^zip:", "file:");
            } else {
               var2 = var2.replaceFirst("^zip:", "file:/");
            }
         } else {
            var2 = var2.replaceFirst("^jar:", "");
         }

         var2 = var2.substring(0, var2.indexOf("!/"));
         URL var3 = new URL(var2);
         return var3;
      }
   }

   private URL toJarParentURL(URL var1) throws MalformedURLException {
      return new URL(var1, ".");
   }

   private void debug(String var1) {
      this.debug(var1, (Exception)null);
   }

   private void debug(String var1, Exception var2) {
      System.out.println("AbstractPersistenceUnitRegistry: " + var1);
      if (var2 != null) {
         var2.printStackTrace();
      }

   }

   private static class BeanInfo {
      public final Object bean;
      public final URL rootUrl;
      public final URL jarParentUrl;
      public final DescriptorBean rootBean;

      public BeanInfo(Object var1, URL var2, URL var3, DescriptorBean var4) {
         this.bean = var1;
         this.rootUrl = var2;
         this.jarParentUrl = var3;
         this.rootBean = var4;
      }
   }
}
