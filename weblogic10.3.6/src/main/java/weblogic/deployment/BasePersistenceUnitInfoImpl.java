package weblogic.deployment;

import java.io.IOException;
import java.lang.reflect.Proxy;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.AccessController;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.zip.ZipEntry;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManagerFactory;
import javax.persistence.spi.ClassTransformer;
import javax.persistence.spi.PersistenceProvider;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.persistence.spi.PersistenceUnitTransactionType;
import javax.sql.DataSource;
import kodo.jdbc.conf.descriptor.JDBCConfigurationBeanParser;
import kodo.jdbc.conf.descriptor.PersistenceUnitConfigurationBean;
import org.apache.openjpa.conf.OpenJPAConfiguration;
import org.apache.openjpa.persistence.OpenJPAEntityManagerFactorySPI;
import org.apache.openjpa.persistence.PersistenceProviderImpl;
import weblogic.application.ApplicationContextInternal;
import weblogic.application.library.Library;
import weblogic.application.library.LibraryProvider;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.j2ee.J2EELogger;
import weblogic.j2ee.descriptor.PersistencePropertyBean;
import weblogic.j2ee.descriptor.PersistenceUnitBean;
import weblogic.kodo.WebLogicLogFactory;
import weblogic.kodo.monitoring.KodoPersistenceUnitParent;
import weblogic.kodo.monitoring.KodoPersistenceUnitRuntimeMBeanFactory;
import weblogic.management.ManagementException;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.JPAMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.runtime.KodoPersistenceUnitRuntimeMBean;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.classloaders.Annotation;
import weblogic.utils.classloaders.ClassPreProcessor;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.jars.VirtualJarFactory;
import weblogic.utils.jars.VirtualJarFile;

abstract class BasePersistenceUnitInfoImpl implements PersistenceUnitInfo {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final String[] CONVERT = new String[]{"TransactionMode", "ConnectionFactory", "ConnectionFactoryName", "ConnectionFactoryMode", "ConnectionFactory2", "ConnectionFactory2Name", "ClassResolver"};
   private static final DebugLogger logger = DebugLogger.getDebugLogger("DebugKodoWeblogic");
   protected final PersistenceUnitBean dd;
   private final PersistenceUnitConfigurationBean configDD;
   private final GenericClassLoader cl;
   private final String persistenceArchiveId;
   private final String persistenceUnitId;
   private final URL rootUrl;
   private final URL jarParentUrl;
   private final List jarFileUrls;
   private ApplicationContextInternal appCtx;
   private EntityManagerFactory wrappedEMF;
   private EntityManagerFactory unwrappedEMF;
   private Properties properties;
   private DataSource jtaDataSource;
   private DataSource nonJtaDataSource;
   private String providerClassName;
   private String originalVersion;
   private KodoPersistenceUnitParent parentMBean;
   private KodoPersistenceUnitRuntimeMBean runtimeMBean;

   BasePersistenceUnitInfoImpl(PersistenceUnitBean var1, PersistenceUnitConfigurationBean var2, GenericClassLoader var3, String var4, URL var5, URL var6, String var7) throws EnvironmentException {
      this(var1, var2, var3, var4, var5, var6, var7, (ApplicationContextInternal)null);
   }

   BasePersistenceUnitInfoImpl(PersistenceUnitBean var1, PersistenceUnitConfigurationBean var2, GenericClassLoader var3, String var4, URL var5, URL var6, String var7, ApplicationContextInternal var8) throws EnvironmentException {
      this.jarFileUrls = new ArrayList();
      this.properties = new Properties();
      this.dd = var1;
      this.configDD = var2;
      this.cl = var3;
      this.persistenceArchiveId = var4;
      this.persistenceUnitId = var4 + "#" + var1.getName();
      this.rootUrl = var5;
      this.jarParentUrl = var6;
      this.originalVersion = var7;
      this.appCtx = var8;
      this.createJarFileUrls();
      this.createProperties();
      this.lookUpDataSources();
      this.initializeEntityManagerFactory();
   }

   private void registerWithParentMBean() throws EnvironmentException {
      if (this.parentMBean != null) {
         try {
            this.runtimeMBean = KodoPersistenceUnitRuntimeMBeanFactory.getInstance().createKodoPersistenceUnitRuntimeMBean(this.getPersistenceUnitName(), (RuntimeMBean)this.parentMBean, this.unwrappedEMF);
         } catch (RuntimeException var3) {
            DebugLogger var2 = DebugLogger.getDebugLogger("DebugJpaRuntime");
            if (var2.isDebugEnabled()) {
               var2.debug("Problem creating KodoPersistenceUnitRuntimeMBean", var3);
            }
         } catch (ManagementException var4) {
            if (logger.isDebugEnabled()) {
               logger.debug("Problem creating KodoPersistenceUnitRuntimeMBean", var4);
            }
         }

         if (this.runtimeMBean != null) {
            this.parentMBean.addKodoPersistenceUnit(this.runtimeMBean);
         }
      }

   }

   public void setParentRuntimeMBean(KodoPersistenceUnitParent var1) throws EnvironmentException {
      if (this.parentMBean == null) {
         this.parentMBean = var1;
         this.registerWithParentMBean();
      }
   }

   private void lookUpDataSources() {
      if (this.dd.getJtaDataSource() != null && !"".equals(this.dd.getJtaDataSource())) {
         this.jtaDataSource = this.lookUpDataSource(this.dd.getJtaDataSource());
      }

      if (this.dd.getNonJtaDataSource() != null && !"".equals(this.dd.getNonJtaDataSource())) {
         this.nonJtaDataSource = this.lookUpDataSource(this.dd.getNonJtaDataSource());
      }

   }

   private DataSource lookUpDataSource(String var1) {
      try {
         InitialContext var2 = new InitialContext();
         return (DataSource)var2.lookup(var1);
      } catch (NamingException var3) {
         return null;
      }
   }

   private void createJarFileUrls() throws EnvironmentException {
      if (this.dd.getJarFiles().length > 0 && "jar".equals(this.jarParentUrl.getProtocol())) {
         throw new IllegalStateException("jar-file references are only supported inside META-INF/classes and exploded-format deployments.  Unsupported reference found while parsing persistence unit '" + this.persistenceUnitId + "'.");
      } else {
         for(int var1 = 0; var1 < this.dd.getJarFiles().length; ++var1) {
            String var2 = this.jarParentUrl.toString() + this.dd.getJarFiles()[var1];

            URL var3;
            try {
               var3 = new URL(var2);
               var3.openConnection().connect();
               this.jarFileUrls.add(var3);
            } catch (MalformedURLException var6) {
               EnvironmentException var5 = new EnvironmentException("Error processing PersistenceUnit " + this.getPersistenceUnitName() + ": " + var6);
               var5.setStackTrace(var6.getStackTrace());
               throw var5;
            } catch (IOException var7) {
               if (logger.isDebugEnabled()) {
                  logger.debug("jar-file is not found at " + var2);
               }

               if (this.appCtx != null) {
                  var3 = this.getJarFileUrlFromLibraryModule(this.dd.getJarFiles()[var1]);
                  if (var3 != null) {
                     this.jarFileUrls.add(var3);
                  }
               }
            }
         }

      }
   }

   private void createProperties() {
      if (logger.isDebugEnabled()) {
         logger.debug("Creating properties:" + this.rootUrl + ":" + this.dd + ":" + this.configDD);
      }

      String var1;
      if (this.configDD != null) {
         if (logger.isDebugEnabled()) {
            logger.debug("Parsing persistence-configuration.xml properties...");
         }

         var1 = null;
         Properties var2 = (new JDBCConfigurationBeanParser()).load(this.configDD);
         Iterator var3 = var2.keySet().iterator();

         label112:
         while(true) {
            while(true) {
               if (!var3.hasNext()) {
                  break label112;
               }

               var1 = (String)var3.next();

               for(int var4 = 0; var4 < CONVERT.length; ++var4) {
                  if (var1.endsWith(CONVERT[var4])) {
                     (new StringBuilder()).append("openjpa").append(var1.substring("kodo".length())).toString();
                     break;
                  }

                  this.properties.setProperty(var1, var2.getProperty(var1));
               }
            }
         }
      }

      Map.Entry var6;
      Iterator var8;
      if (logger.isDebugEnabled()) {
         var8 = this.properties.entrySet().iterator();

         while(var8.hasNext()) {
            var6 = (Map.Entry)var8.next();
            logger.debug("Configuration property:" + var6.getKey() + "," + var6.getValue());
         }

         logger.debug("Done parsing -configuration.");
      }

      if (this.dd.getProperties() != null) {
         PersistencePropertyBean[] var7 = this.dd.getProperties().getProperties();

         for(int var9 = 0; var7 != null && var9 < var7.length; ++var9) {
            this.properties.setProperty(var7[var9].getName(), var7[var9].getValue());
         }
      }

      if (logger.isDebugEnabled()) {
         var8 = this.properties.entrySet().iterator();

         while(var8.hasNext()) {
            var6 = (Map.Entry)var8.next();
            logger.debug("Found persistence property:" + var6.getKey() + "," + var6.getValue());
         }

         logger.debug("Done parsing persistence properties.");
      }

      if (this.isKodoPersistenceUnit()) {
         if (this.properties.containsKey("kodo.Id")) {
            var1 = "kodo.Id";
         } else {
            var1 = "openjpa.Id";
         }

         if (this.properties.containsKey(var1)) {
            J2EELogger.logPersistenceUnitIdPropertySpecified(this.getPersistenceUnitId(), var1);
         }

         this.properties.setProperty(var1, this.getPersistenceUnitId());
         boolean var11 = false;

         try {
            Class var10 = Class.forName(PersistenceProviderImpl.class.getName(), true, this.getClassLoader());
            var11 = var10 != PersistenceProviderImpl.class;
         } catch (ClassNotFoundException var5) {
         }

         if (var11) {
            J2EELogger.logOpenJPAPersistenceUnitUsesApplicationJars(this.getPersistenceUnitId());
         } else {
            String var12;
            if (this.properties.containsKey("kodo.Log")) {
               var12 = "kodo.Log";
            } else {
               var12 = "openjpa.Log";
            }

            if (this.properties.containsKey(var12)) {
               J2EELogger.logPersistenceUnitLogConfigurationSpecified(this.getPersistenceUnitId(), var12);
            }

            this.properties.setProperty(var12, WebLogicLogFactory.class.getName());
         }
      }

   }

   protected PersistenceProvider getPersistenceProvider() throws EnvironmentException {
      try {
         return (PersistenceProvider)Class.forName(this.getPersistenceProviderClassName(), true, this.cl).newInstance();
      } catch (Exception var3) {
         EnvironmentException var2 = new EnvironmentException("Error instantiating the Persistence Provider class " + this.getPersistenceProviderClassName() + " of the PersistenceUnit " + this.getPersistenceUnitName() + ": " + var3);
         var2.initCause(var3);
         throw var2;
      }
   }

   private void initializeEntityManagerFactory() throws EnvironmentException {
      this.initializeEntityManagerFactory(false);
   }

   private void initializeEntityManagerFactory(boolean var1) throws EnvironmentException {
      if (this.unwrappedEMF == null) {
         this.unwrappedEMF = this.getPersistenceProvider().createContainerEntityManagerFactory(this, this.getPersistenceProviderProperties(var1));
         if (this.unwrappedEMF == null) {
            throw new EnvironmentException("could not find deployed EMF for persistence unit named " + this.getPersistenceUnitName() + ". Available EMFs in the current context: " + this);
         }

         Class[] var2 = DynamicProxyUtils.getAllInterfaces(this.unwrappedEMF.getClass(), EntityManagerFactory.class);
         EntityManagerFactoryProxyImpl var3 = new EntityManagerFactoryProxyImpl(this.unwrappedEMF, this.getPersistenceUnitName());
         this.wrappedEMF = (EntityManagerFactory)Proxy.newProxyInstance(this.cl, var2, var3);
      }

   }

   private Map getPersistenceProviderProperties(boolean var1) {
      if (!this.isKodoPersistenceUnit() && (!this.isHibernatePersistenceUnit() || var1)) {
         HashMap var2 = new HashMap();
         var2.put("eclipselink.target-server", "WebLogic_10");
         if (this.appCtx != null) {
            var2.put("weblogic.application-id", this.appCtx.getApplicationId());
         }

         if (!this.isProviderLoadedFromApplication()) {
            String var3 = this.properties.getProperty("eclipselink.logging.logger");
            if (var3 == null) {
               String var4 = this.dd.getProvider();
               if (var4 == null || var4.contains("org.eclipse.persistence")) {
                  var2.put("eclipselink.logging.logger", "weblogic.eclipselink.WeblogicEclipseLinkLog");
               }
            }
         }

         if (this.dd.getJtaDataSource() != null && !"".equals(this.dd.getJtaDataSource()) && this.jtaDataSource == null && this.properties.getProperty("javax.persistence.jtaDataSource") == null) {
            var2.put("javax.persistence.jtaDataSource", this.dd.getJtaDataSource());
         }

         if (this.dd.getNonJtaDataSource() != null && !"".equals(this.dd.getNonJtaDataSource()) && this.nonJtaDataSource == null && this.properties.getProperty("javax.persistence.nonJtaDataSource") == null) {
            var2.put("javax.persistence.nonJtaDataSource", this.dd.getNonJtaDataSource());
         }

         return var2;
      } else {
         return Collections.EMPTY_MAP;
      }
   }

   public String getPersistenceUnitName() {
      return this.dd.getName();
   }

   private boolean isProviderLoadedFromApplication() {
      Class var1 = null;

      try {
         var1 = Class.forName(this.getPersistenceProviderClassName(), true, this.cl);
      } catch (ClassNotFoundException var5) {
         return false;
      }

      ClassLoader var2 = var1.getClassLoader();
      if (var2 instanceof GenericClassLoader) {
         GenericClassLoader var3 = (GenericClassLoader)var2;
         Annotation var4 = var3.getAnnotation();
         if (var4.getApplicationName() != null) {
            return true;
         }
      }

      return false;
   }

   public String getPersistenceProviderClassName() {
      if (this.providerClassName == null) {
         String var1 = this.dd.getProvider();
         if (var1 == null || var1.trim().length() == 0) {
            var1 = "org.apache.openjpa.persistence.PersistenceProviderImpl";
            if (ManagementService.isRuntimeAccessInitialized()) {
               DomainMBean var2 = ManagementService.getRuntimeAccess(kernelId).getDomain();
               JPAMBean var3 = var2.getJPA();
               var1 = var3.getDefaultJPAProvider();
            }

            J2EELogger.logUsingDefaultPersistenceProvider(this.getPersistenceUnitId(), var1);
         }

         if (logger.isDebugEnabled()) {
            logger.debug("The persistence unit " + this.getPersistenceUnitId() + " will use the following PersistenceProvider class: " + var1);
         }

         this.providerClassName = var1;
      }

      return this.providerClassName;
   }

   public PersistenceUnitTransactionType getTransactionType() {
      return PersistenceUnitTransactionType.valueOf(this.dd.getTransactionType());
   }

   public DataSource getJtaDataSource() {
      return this.jtaDataSource;
   }

   public DataSource getNonJtaDataSource() {
      return this.nonJtaDataSource;
   }

   public List getMappingFileNames() {
      return Arrays.asList(this.dd.getMappingFiles());
   }

   public List getJarFileUrls() {
      return this.jarFileUrls;
   }

   public URL getPersistenceUnitRootUrl() {
      return this.rootUrl;
   }

   public List getManagedClassNames() {
      return Arrays.asList(this.dd.getClasses());
   }

   public boolean excludeUnlistedClasses() {
      return this.dd.getExcludeUnlistedClasses();
   }

   public Properties getProperties() {
      return this.properties;
   }

   public ClassLoader getClassLoader() {
      return this.cl;
   }

   public void addTransformer(ClassTransformer var1) {
      this.cl.addInstanceClassPreProcessor(new ClassPreProcessorImpl(var1));
   }

   public ClassLoader getNewTempClassLoader() {
      return new GenericClassLoader(this.cl.getClassFinder(), this.cl.getParent());
   }

   public String getDescription() {
      return this.dd.getDescription();
   }

   public EntityManagerFactory getEntityManagerFactory() {
      if (this.wrappedEMF == null) {
         throw new IllegalStateException("EMF has not been initialized yet!");
      } else {
         return this.wrappedEMF;
      }
   }

   public EntityManagerFactory getUnwrappedEntityManagerFactory() {
      if (this.unwrappedEMF == null) {
         throw new IllegalStateException("EMF has not been initialized yet!");
      } else {
         return this.unwrappedEMF;
      }
   }

   public String getPersistenceUnitId() {
      return this.persistenceUnitId;
   }

   public void close() {
      if (this.unwrappedEMF != null) {
         this.unwrappedEMF.close();
         this.unwrappedEMF = null;
         this.wrappedEMF = null;
         this.providerClassName = null;
      }

      this.appCtx = null;
   }

   private boolean isKodoPersistenceUnit() {
      String var1 = this.getPersistenceProviderClassName();
      return var1 == null || "".equals(var1.trim()) || var1.equals(PersistenceProviderImpl.class.getName()) || var1.equals(kodo.persistence.PersistenceProviderImpl.class.getName());
   }

   private boolean isHibernatePersistenceUnit() {
      return this.getPersistenceProviderClassName().contains("hibernate");
   }

   public PersistenceUnitConfigurationBean getConfigDD() {
      return this.configDD;
   }

   public PersistenceUnitBean getDD() {
      return this.dd;
   }

   private DataSource lookUpAppScopedDataSource(Context var1, String var2) throws EnvironmentException {
      try {
         InitialContext var3 = new InitialContext();
         return (DataSource)var3.lookup(var2);
      } catch (NamingException var6) {
         try {
            Context var4 = (Context)var1.lookup("jdbc");
            if (var2.startsWith("java:/app/jdbc/")) {
               var2 = var2.substring("java:/app/jdbc/".length());
            }

            return (DataSource)var4.lookup(var2);
         } catch (NamingException var5) {
            return null;
         }
      }
   }

   public void activate(Context var1) throws EnvironmentException {
      boolean var2 = false;
      if (this.dd.getJtaDataSource() != null && this.jtaDataSource == null) {
         this.jtaDataSource = this.lookUpAppScopedDataSource(var1, this.dd.getJtaDataSource());
         var2 = true;
      }

      boolean var3 = false;
      if (this.dd.getNonJtaDataSource() != null && this.nonJtaDataSource == null) {
         this.nonJtaDataSource = this.lookUpAppScopedDataSource(var1, this.dd.getNonJtaDataSource());
         var3 = true;
      }

      if (var2 || var3) {
         if (this.isHibernatePersistenceUnit()) {
            this.close();
            this.initializeEntityManagerFactory(true);
         }

         if (this.unwrappedEMF != null && this.unwrappedEMF instanceof OpenJPAEntityManagerFactorySPI) {
            OpenJPAConfiguration var4 = ((OpenJPAEntityManagerFactorySPI)this.unwrappedEMF).getConfiguration();
            if (var4 == null) {
               return;
            }

            if (var2) {
               var4.setConnectionFactory(this.jtaDataSource);
               var4.setConnectionFactoryMode("managed");
            }

            if (var3) {
               if (!var2) {
                  var4.setConnectionFactory(this.nonJtaDataSource);
               } else {
                  var4.setConnectionFactory2(this.nonJtaDataSource);
               }
            }
         }

      }
   }

   public String getPersistenceXMLSchemaVersion() {
      return this.originalVersion;
   }

   private URL getJarFileUrlFromLibraryModule(String var1) {
      if (this.appCtx == null) {
         return null;
      } else {
         String[] var2 = this.persistenceArchiveId.split("#");
         LibraryProvider var3 = this.appCtx.getLibraryProvider(var2[var2.length - 1]);
         if (var3 != null) {
            Library[] var4 = var3.getReferencedLibraries();

            for(int var5 = 0; var5 < var4.length; ++var5) {
               Library var6 = var4[var5];

               try {
                  VirtualJarFile var7 = VirtualJarFactory.createVirtualJar(var6.getLocation());
                  Iterator var8 = var7.entries();

                  while(var8.hasNext()) {
                     ZipEntry var9 = (ZipEntry)var8.next();
                     if (var9.getName().indexOf(var1) > -1) {
                        URL var10 = var7.getResource(var9.getName());
                        if (logger.isDebugEnabled()) {
                           logger.debug("jar-file is found in library at " + var10.toString());
                        }

                        return var10;
                     }
                  }
               } catch (IOException var11) {
               }
            }
         }

         return null;
      }
   }

   private class ClassPreProcessorImpl implements ClassPreProcessor {
      private ClassTransformer transformer;

      private ClassPreProcessorImpl(ClassTransformer var2) {
         this.transformer = var2;
      }

      public void initialize(Hashtable var1) {
      }

      public byte[] preProcess(String var1, byte[] var2) {
         Object var3 = null;
         ClassLoader var4 = Thread.currentThread().getContextClassLoader();

         byte[] var6;
         try {
            Thread.currentThread().setContextClassLoader(BasePersistenceUnitInfoImpl.this.cl);
            byte[] var5 = this.transformer.transform(BasePersistenceUnitInfoImpl.this.cl, var1, (Class)null, (ProtectionDomain)var3, var2);
            var6 = var5 == null ? var2 : var5;
         } catch (Exception var11) {
            if ("java.lang.instrument.IllegalClassFormatException".equals(var11.getClass().getName())) {
               throw new RuntimeException(var11);
            }

            if (var11 instanceof RuntimeException) {
               throw (RuntimeException)var11;
            }

            throw new RuntimeException(var11);
         } finally {
            Thread.currentThread().setContextClassLoader(var4);
         }

         return var6;
      }

      // $FF: synthetic method
      ClassPreProcessorImpl(ClassTransformer var2, Object var3) {
         this(var2);
      }
   }
}
