package weblogic.xml.registry;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.AccessController;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.TransformerFactory;
import org.xml.sax.InputSource;
import org.xml.sax.Parser;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderAdapter;
import weblogic.application.ApplicationAccess;
import weblogic.application.ApplicationContextInternal;
import weblogic.application.ApplicationFactoryManager;
import weblogic.j2ee.descriptor.wl.ParserFactoryBean;
import weblogic.j2ee.descriptor.wl.XmlBean;
import weblogic.management.WebLogicMBean;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.configuration.XMLEntityCacheMBean;
import weblogic.management.configuration.XMLEntitySpecRegistryEntryMBean;
import weblogic.management.configuration.XMLParserSelectRegistryEntryMBean;
import weblogic.management.configuration.XMLRegistryMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.jars.VirtualJarFile;
import weblogic.xml.XMLLogger;
import weblogic.xml.jaxp.XMLContext;
import weblogic.xml.util.Tools;
import weblogic.xml.util.XMLConstants;
import weblogic.xml.util.cache.entitycache.EntityCacheCumulativeStats;
import weblogic.xml.util.cache.entitycache.EntityCacheCurrentStats;
import weblogic.xml.util.cache.entitycache.Event;

public class XMLRegistry implements XMLConstants {
   private static final String defaultAppName = "BEAWeblogicDefaultApplicationContext";
   private static final double MemCheckFactor = 0.5;
   private static Hashtable registryExtent = new Hashtable();
   private static final int defaultCacheTimeoutInterval = 120;
   EntityCacheCumulativeStats sessionCacheStatsMBean = null;
   EntityCacheCumulativeStats historicalCacheStatsMBean = null;
   EntityCacheCurrentStats currentCacheStatsMBean = null;
   private static XMLRegistry defaultRegistry = null;
   private static ServerListener serverListener = null;
   private RegistryListener registryListener = null;
   private final String REGISTRY_DIR = "lib/xml/registry";
   private boolean isAppScopedRegistry = false;
   private boolean cleanupTempEntities = false;
   private AppDeploymentMBean deployableMBean = null;
   private final ApplicationAccess applicationAccess = ApplicationAccess.getApplicationAccess();
   private CacheListener cacheListener = null;
   private HashSet newEntitySpecMBeans = new HashSet();
   private HashSet newParserSelectMBeans = new HashSet();
   private static XMLRegistryMBean xmlDefaultRegistryAdminMBean = null;
   private XMLRegistryMBean xmlRegistryConfigMBean = null;
   private ConfigAbstraction.RegistryConfig config = null;
   private String privateRegistryDir;
   private String basePath = null;
   private String registryName;
   private String applicationName = null;
   private XMLRegistryDir publicRegistryDir;
   private Map psIndex = new HashMap();
   private Map esIndex = new HashMap();
   private boolean hasCustomParserEntries = false;
   private boolean hasDocumentSpecificParserEntries = false;
   private boolean hasDocumentSpecificEntityEntries = false;
   private static weblogic.xml.util.cache.entitycache.EntityCache underlyingCache = null;
   private EntityCache entityCache = null;
   private static RefreshCacheLock lock = new RefreshCacheLock(10);
   private int cacheDefaultTimeoutInterval = -1;
   static XMLEntityCacheMBean cacheConfigMBean = null;
   static ServerMBean serverConfigMBean = null;

   static void init() throws XMLRegistryException {
      ApplicationFactoryManager var0 = ApplicationFactoryManager.getApplicationFactoryManager();
      var0.addWLAppModuleFactory(new XMLModuleFactory());
      XMLContext.init();

      try {
         initializeDefaultRegistry();
      } catch (XMLRegistryException var2) {
         throw var2;
      } catch (Exception var3) {
      }

   }

   private XMLRegistry(XMLRegistryMBean var1, String var2) throws XMLRegistryException {
      this.xmlRegistryConfigMBean = var1;
      this.privateRegistryDir = "lib/xml/registry";
      this.applicationName = var2;
      this.loadPrivateRegistry();
      this.installRegistryInstance(var2);
   }

   private XMLRegistry(XmlBean var1, String var2, String var3) throws XMLRegistryException {
      this.basePath = var3;

      try {
         this.applicationName = var2;
         this.config = ConfigAbstraction.getRegistryConfig(var1, this.applicationName);
         this.registryName = "_._" + this.config.getName();
         this.setUpEntityCache();
      } catch (Exception var5) {
         throw new XMLRegistryException(var5);
      }

      this.installRegistryInstance(var2);
   }

   static ServerMBean getServerConfigMBean() {
      if (serverConfigMBean == null) {
         AuthenticatedSubject var0 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
         serverConfigMBean = ManagementService.getRuntimeAccess(var0).getServer();
      }

      return serverConfigMBean;
   }

   private XMLRegistryMBean getRegistryConfigMBean() {
      if (this.xmlRegistryConfigMBean == null) {
         this.xmlRegistryConfigMBean = getServerConfigMBean().getXMLRegistry();
      }

      return this.xmlRegistryConfigMBean;
   }

   private static XMLRegistryMBean getRegistryConfigMBeanStatic() {
      AuthenticatedSubject var0 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
      return ManagementService.getRuntimeAccess(var0).getServer().getXMLRegistry();
   }

   XMLRegistryMBean getXMLRegistryMBean(String var1) {
      AuthenticatedSubject var2 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
      RuntimeAccess var3 = ManagementService.getRuntimeAccess(var2);
      DomainMBean var4 = var3.getDomain();
      ServerMBean[] var5 = var4.getServers();
      if (var5 == null) {
         return null;
      } else {
         for(int var6 = 0; var6 < var5.length; ++var6) {
            XMLRegistryMBean var7 = var5[var6].getXMLRegistry();
            if (var7 != null && var7.getName().equals(var1)) {
               return var7;
            }
         }

         return null;
      }
   }

   static XMLEntityCacheMBean getCacheConfigMBean() {
      if (cacheConfigMBean == null) {
         try {
            cacheConfigMBean = getServerConfigMBean().getXMLEntityCache();
         } catch (Exception var1) {
         }
      }

      return cacheConfigMBean;
   }

   private static void initializeDefaultRegistry() throws XMLRegistryException {
      Class var0 = XMLRegistry.class;
      synchronized(XMLRegistry.class) {
         XMLRegistryMBean var1 = getRegistryConfigMBeanStatic();
         defaultRegistry = new XMLRegistry(var1, "BEAWeblogicDefaultApplicationContext");
         defaultRegistry.loadFromMBean(getRegistryConfigMBeanStatic());
         if (serverListener == null) {
            serverListener = new ServerListener();
            serverConfigMBean.addPropertyChangeListener(serverListener);
         }

      }
   }

   private AppDeploymentMBean getAppDeploymentMBean() {
      return this.deployableMBean;
   }

   static void initializeAppScopedXMLRegistry(XmlBean var0, AppDeploymentMBean var1, String var2) throws XMLRegistryException {
      String var3 = var1.getName();
      XMLRegistry var4 = new XMLRegistry(var0, var3, var2);
      var4.isAppScopedRegistry = true;
      var4.cleanupTempEntities = true;
      var4.deployableMBean = var1;
      var4.loadPublicEntries();
      ParserFactoryBean var5 = var0.getParserFactory();
      var4.basePath = var2;
   }

   static void cleanUpAppScopedXMLRegistry(String var0) throws XMLRegistryException {
      XMLRegistry var1 = getXMLRegistry(var0);
      var1.cleanupTempEntities = true;

      XMLEntitySpecRegistryEntry var3;
      for(Iterator var2 = var1.esIndex.values().iterator(); var2.hasNext(); var3.setListener((PropertyChangeListener)null)) {
         var3 = (XMLEntitySpecRegistryEntry)var2.next();
         if (var1.getCache() != null) {
            var1.getCache().remove(var3.getPublicId(), var3.getSystemId());
         }

         PropertyChangeListener var4 = var3.getListener();
         if (var4 != null) {
            ConfigAbstraction.EntryConfig var5 = var3.getMBean();
            if (var5 != null) {
               var5.removePropertyChangeListener(var4);
            }
         }
      }

      registryExtent.remove(var0);
      var1.config = null;
   }

   void cleanUpCache(String[] var1) throws XMLRegistryException {
      for(int var2 = 0; var2 < var1.length; ++var2) {
         if (var1[var2].startsWith("lib/xml/registry")) {
            if (var1[var2].equals("lib/xml/registry")) {
               this.cleanUpCache();
               break;
            }

            Iterator var3 = this.esIndex.values().iterator();

            while(var3.hasNext()) {
               XMLEntitySpecRegistryEntry var4 = (XMLEntitySpecRegistryEntry)var3.next();
               if (var4.getEntityURI() != null) {
                  String var5 = var1[var2].substring("lib/xml/registry".length() + 1);
                  if (var5.equals(var4.getEntityURI())) {
                     this.getCache().remove(var4.getPublicId(), var4.getSystemId());
                  }
               }
            }
         }
      }

   }

   private void cleanUpCache() throws XMLRegistryException {
      Iterator var1 = this.esIndex.values().iterator();

      while(var1.hasNext()) {
         XMLEntitySpecRegistryEntry var2 = (XMLEntitySpecRegistryEntry)var1.next();
         this.getCache().remove(var2.getPublicId(), var2.getSystemId());
      }

   }

   private static void reinitializeDefaultRegistry() throws XMLRegistryException {
      Class var0 = XMLRegistry.class;
      synchronized(XMLRegistry.class) {
         defaultRegistry.cleanupRegistry();
         initializeDefaultRegistry();
      }

      defaultRegistry.config.removePropertyChangeListener(defaultRegistry.registryListener);
      defaultRegistry.registryListener = null;
   }

   private void cleanupRegistry() {
      defaultRegistry = null;
      xmlDefaultRegistryAdminMBean = null;
      if (this.config != null) {
         this.config.removePropertyChangeListener(this.registryListener);
      }

      this.registryListener = null;

      XMLParserSelectRegistryEntry var2;
      for(Iterator var1 = this.psIndex.values().iterator(); var1.hasNext(); var2.setListener((PropertyChangeListener)null)) {
         var2 = (XMLParserSelectRegistryEntry)var1.next();
         PropertyChangeListener var3 = var2.getListener();
         if (var3 != null) {
            ConfigAbstraction.EntryConfig var4 = var2.getMBean();
            if (var4 != null) {
               var4.removePropertyChangeListener(var3);
            }
         }
      }

      XMLEntitySpecRegistryEntry var7;
      for(Iterator var6 = this.esIndex.values().iterator(); var6.hasNext(); var7.setListener((PropertyChangeListener)null)) {
         var7 = (XMLEntitySpecRegistryEntry)var6.next();
         PropertyChangeListener var8 = var7.getListener();
         if (var8 != null) {
            ConfigAbstraction.EntryConfig var5 = var7.getMBean();
            if (var5 != null) {
               var5.removePropertyChangeListener(var8);
            }
         }
      }

   }

   public String getName() {
      return this.registryName;
   }

   public String getApplicationName() {
      return this.applicationName;
   }

   public RefreshCacheLock getLock() {
      return lock;
   }

   public EntityCache getCache() throws XMLRegistryException {
      return this.entityCache;
   }

   private String getCacheBeanName() {
      AuthenticatedSubject var1 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
      return "XMLCacheMBean_" + ManagementService.getRuntimeAccess(var1).getServerName();
   }

   XMLEntityCacheMBean getEntityCacheMBean() {
      if (cacheConfigMBean == null) {
         cacheConfigMBean = getServerConfigMBean().getXMLEntityCache();
      }

      return cacheConfigMBean;
   }

   protected weblogic.xml.util.cache.entitycache.EntityCache getUnderlyingCache() throws XMLRegistryException {
      if (underlyingCache == null) {
         try {
            XMLEntityCacheMBean var1 = this.getEntityCacheMBean();
            if (var1 == null) {
               return null;
            }

            String var2 = var1.getCacheLocation();
            String var3 = "XML-Entity-Cache";
            int var4 = var1.getCacheMemorySize();
            int var5 = var1.getCacheDiskSize();
            weblogic.xml.util.cache.entitycache.EntityCache.CacheSpec var6 = new weblogic.xml.util.cache.entitycache.EntityCache.CacheSpec();
            var6.name = var3;
            var6.path = var2;
            var6.memSize = (long)(var4 * 1000);
            var6.diskSize = (long)(var5 * 1000000);
            var6.cacheListener = new CacheLogListener();
            underlyingCache = weblogic.xml.util.cache.entitycache.EntityCache.getCache(var6, cacheConfigMBean);
            if (underlyingCache != null) {
               Runtime var7 = Runtime.getRuntime();

               try {
                  long var8 = var7.freeMemory();
                  if ((double)var4 > (double)var8 * 0.5) {
                     if ((long)var4 > var8) {
                        XMLLogger.logCacheMemoryWarningExceeds((long)var4, var8);
                     } else {
                        XMLLogger.logCacheMemoryWarningClose((long)var4, var8);
                     }
                  }
               } catch (Exception var10) {
               }
            }
         } catch (Exception var11) {
            throw new XMLRegistryException(var11);
         }
      }

      return underlyingCache;
   }

   private void loadFromMBean(XMLRegistryMBean var1) throws XMLRegistryException {
      if (var1 != null) {
         this.config = ConfigAbstraction.getRegistryConfig(var1);
         this.registryName = var1.getName();
         if (this.registryName == null || this.registryName.length() == 0) {
            String var2 = "Registry does not have a name";
            throw new XMLRegistryException(var2);
         }

         this.publicRegistryDir = new XMLRegistryDir(this.registryName);
         this.setUpEntityCache();
         this.loadPublicRegistry();
         this.registryListener = new RegistryListener(this);
         this.config.addPropertyChangeListener(this.registryListener);
      }

   }

   private void installRegistryInstance(String var1) {
      registryExtent.put(var1, this);
   }

   public static XMLRegistry getXMLRegistry(String var0) {
      return (XMLRegistry)registryExtent.get(var0);
   }

   private void setUpEntityCache() throws XMLRegistryException {
      weblogic.xml.util.cache.entitycache.EntityCache var1 = this.getUnderlyingCache();
      if (var1 != null) {
         this.entityCache = new EntityCache(this, var1);
         XMLEntityCacheMBean var2 = this.getEntityCacheMBean();
         if (var2 != null) {
            this.cacheListener = new CacheListener(this);
            var2.addPropertyChangeListener(this.cacheListener);
         }
      }

   }

   public static XMLRegistry[] getXMLRegistryPath() throws XMLRegistryException {
      XMLRegistry[] var0 = null;
      String var1 = null;
      var1 = ApplicationAccess.getApplicationAccess().getCurrentApplicationName();
      if (var1 == null) {
         var1 = "BEAWeblogicDefaultApplicationContext";
      }

      Class var2 = XMLRegistry.class;
      synchronized(XMLRegistry.class) {
         XMLRegistry var3 = null;
         var3 = (XMLRegistry)registryExtent.get(var1);
         if (var3 == null && defaultRegistry == null) {
            return new XMLRegistry[0];
         } else {
            if (var3 != null && var3 != defaultRegistry) {
               var0 = new XMLRegistry[]{var3, defaultRegistry};
            } else {
               var0 = new XMLRegistry[]{defaultRegistry};
            }

            return var0;
         }
      }
   }

   void setCacheDefaultTimeoutInterval(int var1) {
      this.cacheDefaultTimeoutInterval = var1;
   }

   public int getCacheTimeoutInterval(String var1, String var2) throws XMLRegistryException {
      XMLEntitySpecRegistryEntry var3 = this.lookupEntitySpecEntry(var1, var2);
      return this.getCacheTimeoutInterval(var3);
   }

   int getCacheTimeoutInterval(XMLEntitySpecRegistryEntry var1) throws XMLRegistryException {
      int var2 = var1.getCacheTimeoutInterval();
      if (var2 < 0) {
         if (this.cacheDefaultTimeoutInterval == -1) {
            XMLEntityCacheMBean var3 = this.getEntityCacheMBean();
            if (var3 != null) {
               try {
                  this.cacheDefaultTimeoutInterval = var3.getCacheTimeoutInterval();
               } catch (ClassCastException var5) {
               }
            }

            if (this.cacheDefaultTimeoutInterval == -1) {
               this.cacheDefaultTimeoutInterval = 120;
            }
         }

         var2 = this.cacheDefaultTimeoutInterval;
      }

      return var2;
   }

   public ResolvedEntity getEntity(String var1, String var2) throws XMLRegistryException {
      ResolvedEntity var3 = new ResolvedEntity();
      Tools.getEntityDescriptor(var1, var2, (String)null);
      XMLEntitySpecRegistryEntry var5 = this.lookupEntitySpecEntry(var1, var2);
      InputSource var6 = null;
      if (var5 != null) {
         String var7 = var5.getEntityURI();
         if (var7 != null) {
            InputStream var8 = this.retrieveEntity(var7);
            if (var8 != null) {
               var6 = new InputSource(var8);
               var6.setSystemId(var2);
               var6.setPublicId(var1);
               var3.inputSource = var6;
               var3.entry = var5;
               String var9 = var5.getWhenToCache();
               String var10 = this.applicationAccess.getCurrentApplicationName();
               if (var9 == null || var9.equals("defer-to-registry-setting")) {
                  var9 = this.config.getWhenToCache();
               }

               if (var9 != null && var9.equals("cache-never")) {
                  var3.isSubjectToCaching = false;
               }

               var3.isLocal = var8 instanceof FileInputStream || this.publicRegistryDir != null && this.publicRegistryDir.isLocal();
            }
         }
      }

      return var3;
   }

   public Parser getParser(String var1, String var2, String var3) throws XMLRegistryException {
      XMLParserSelectRegistryEntry var4 = this.lookupParserSelectEntry(var1, var2, var3);
      Object var5 = null;
      if (var4 != null) {
         String var6 = var4.getParserClassName();
         if (var6 != null) {
            try {
               Object var7 = Class.forName(var6).newInstance();
               if (var7 instanceof XMLReader) {
                  XMLReader var8 = (XMLReader)var7;
                  var5 = new XMLReaderAdapter(var8);
               } else {
                  var5 = (Parser)var7;
               }
            } catch (ClassNotFoundException var9) {
               throw new XMLRegistryException("ClassNotFoundException. Class " + var9.getMessage() + " cannot be located.", var9);
            } catch (ClassCastException var10) {
               throw new XMLRegistryException("ClassCastException. Class " + var10.getMessage() + " is not of type org.xml.sax.Parser.", var10);
            } catch (IllegalAccessException var11) {
               throw new XMLRegistryException("IllegalAccessException. Class " + var11.getMessage() + " is not accessible.", var11);
            } catch (InstantiationException var12) {
               throw new XMLRegistryException("InstantiationException. " + var12.getMessage() + " Class instantiation fails. " + "This Class represents an abstract class, an interface, an array class, a primitive type, or void, or instantiation fails for some other reason", var12);
            } catch (SecurityException var13) {
               throw new XMLRegistryException("SecurityException. There is no permission to create a new instance " + var13.getMessage(), var13);
            } catch (Exception var14) {
               throw new XMLRegistryException(var14);
            }
         }
      }

      return (Parser)var5;
   }

   public SAXParserFactory getSAXParserFactory(String var1, String var2, String var3) throws XMLRegistryException {
      XMLParserSelectRegistryEntry var4 = this.lookupParserSelectEntry(var1, var2, var3);
      SAXParserFactory var5 = null;
      if (var4 != null) {
         String var6 = var4.getSAXParserFactory();
         String var7 = null;
         if (var6 != null) {
            try {
               String var8 = this.applicationAccess.getCurrentApplicationName();
               if (var8 != null && getXMLRegistry(var8) != null) {
                  Thread var9 = Thread.currentThread();
                  ClassLoader var10 = var9.getContextClassLoader();
                  var7 = getXMLRegistry(var8).config.getSAXParserFactory();
                  if (var7 != null) {
                     var5 = (SAXParserFactory)Class.forName(var7, true, var10).newInstance();
                  }
               }

               if (var5 == null) {
                  var5 = (SAXParserFactory)Class.forName(var6).newInstance();
               }
            } catch (ClassNotFoundException var11) {
               throw new XMLRegistryException("ClassNotFoundException. Class " + var11.getMessage() + " cannot be located.", var11);
            } catch (ClassCastException var12) {
               throw new XMLRegistryException("ClassCastException. Class " + var12.getMessage() + " is not of type javax.xml.parsers.SAXParserFactory.", var12);
            } catch (IllegalAccessException var13) {
               throw new XMLRegistryException("IllegalAccessException. Class " + var13.getMessage() + " is not accessible.", var13);
            } catch (InstantiationException var14) {
               throw new XMLRegistryException("InstantiationException. " + var14.getMessage() + " Class instantiation fails. " + "This Class represents an abstract class, an interface, an array class, a primitive type, or void, or instantiation fails for some other reason", var14);
            } catch (SecurityException var15) {
               throw new XMLRegistryException("SecurityException. There is no permission to create a new instance " + var15.getMessage(), var15);
            } catch (Exception var16) {
               throw new XMLRegistryException(var16);
            }
         }
      }

      return var5;
   }

   public SAXParserFactory getSAXParserFactory() throws XMLRegistryException {
      SAXParserFactory var1 = null;
      if (this.havePublicData()) {
         String var2 = this.config.getSAXParserFactory();
         if (var2 != null) {
            try {
               String var3 = this.applicationAccess.getCurrentApplicationName();
               if (var3 != null && this.isAppScopedRegistry) {
                  Thread var4 = Thread.currentThread();
                  ClassLoader var5 = var4.getContextClassLoader();
                  var1 = (SAXParserFactory)Class.forName(var2, true, var5).newInstance();
               } else {
                  var1 = (SAXParserFactory)Class.forName(var2).newInstance();
               }
            } catch (ClassNotFoundException var6) {
               throw new XMLRegistryException("ClassNotFoundException. Class " + var6.getMessage() + " cannot be located.", var6);
            } catch (ClassCastException var7) {
               throw new XMLRegistryException("ClassCastException. Class " + var7.getMessage() + " is not of type javax.xml.parsers.SAXParserFactory.", var7);
            } catch (IllegalAccessException var8) {
               throw new XMLRegistryException("IllegalAccessException. Class " + var8.getMessage() + " is not accessible.", var8);
            } catch (InstantiationException var9) {
               throw new XMLRegistryException("InstantiationException. " + var9.getMessage() + " Class instantiation fails. " + "This Class represents an abstract class, an interface, an array class, a primitive type, or void, or instantiation fails for some other reason", var9);
            } catch (SecurityException var10) {
               throw new XMLRegistryException("SecurityException. There is no permission to create a new instance " + var10.getMessage(), var10);
            } catch (Exception var11) {
               throw new XMLRegistryException(var11);
            }
         }
      }

      return var1;
   }

   public TransformerFactory getTransformerFactory(String var1, String var2, String var3) throws XMLRegistryException {
      XMLParserSelectRegistryEntry var4 = this.lookupParserSelectEntry(var1, var2, var3);
      TransformerFactory var5 = null;
      String var6 = null;
      if (var4 != null) {
         String var7 = var4.getTransformerFactory();
         if (var7 != null) {
            try {
               String var8 = this.applicationAccess.getCurrentApplicationName();
               if (var8 != null && getXMLRegistry(var8) != null) {
                  Thread var9 = Thread.currentThread();
                  ClassLoader var10 = var9.getContextClassLoader();
                  var6 = getXMLRegistry(var8).config.getTransformerFactory();
                  if (var6 != null) {
                     var5 = (TransformerFactory)Class.forName(var6, true, var10).newInstance();
                  }
               }

               if (var5 == null) {
                  var5 = (TransformerFactory)Class.forName(var7).newInstance();
               }
            } catch (Exception var11) {
               throw new XMLRegistryException(var11);
            }
         }
      }

      return var5;
   }

   public TransformerFactory getTransformerFactory() throws XMLRegistryException {
      TransformerFactory var1 = null;
      if (this.havePublicData()) {
         String var2 = this.config.getTransformerFactory();
         if (var2 != null) {
            try {
               String var3 = this.applicationAccess.getCurrentApplicationName();
               if (var3 != null && this.isAppScopedRegistry) {
                  Thread var4 = Thread.currentThread();
                  ClassLoader var5 = var4.getContextClassLoader();
                  var1 = (TransformerFactory)Class.forName(var2, true, var5).newInstance();
               } else {
                  var1 = (TransformerFactory)Class.forName(var2).newInstance();
               }
            } catch (ClassNotFoundException var6) {
               throw new XMLRegistryException("ClassNotFoundException. Class " + var6.getMessage() + " cannot be located.", var6);
            } catch (ClassCastException var7) {
               throw new XMLRegistryException("ClassCastException. Class " + var7.getMessage() + " is not of type javax.xml.transform.TransformerFactory.", var7);
            } catch (IllegalAccessException var8) {
               throw new XMLRegistryException("IllegalAccessException. Class " + var8.getMessage() + " is not accessible.", var8);
            } catch (InstantiationException var9) {
               throw new XMLRegistryException("InstantiationException. " + var9.getMessage() + " Class instantiation fails. " + "This Class represents an abstract class, an interface, an array class, a primitive type, or void, or instantiation fails for some other reason", var9);
            } catch (SecurityException var10) {
               throw new XMLRegistryException("SecurityException. There is no permission to create a new instance " + var10.getMessage(), var10);
            } catch (Exception var11) {
               throw new XMLRegistryException(var11);
            }
         }
      }

      return var1;
   }

   public DocumentBuilderFactory getDocumentBuilderFactory(String var1, String var2, String var3) throws XMLRegistryException {
      XMLParserSelectRegistryEntry var4 = this.lookupParserSelectEntry(var1, var2, var3);
      DocumentBuilderFactory var5 = null;
      if (var4 != null) {
         String var6 = var4.getDocumentBuilderFactory();
         String var7 = null;
         if (var6 != null) {
            try {
               String var8 = this.applicationAccess.getCurrentApplicationName();
               if (var8 != null && getXMLRegistry(var8) != null) {
                  Thread var9 = Thread.currentThread();
                  ClassLoader var10 = var9.getContextClassLoader();
                  var7 = getXMLRegistry(var8).config.getDocumentBuilderFactory();
                  if (var7 != null) {
                     var5 = (DocumentBuilderFactory)Class.forName(var7, true, var10).newInstance();
                  }
               }

               if (var5 == null) {
                  var5 = (DocumentBuilderFactory)Class.forName(var6).newInstance();
               }
            } catch (ClassNotFoundException var11) {
               throw new XMLRegistryException("ClassNotFoundException. Class " + var11.getMessage() + " cannot be located.", var11);
            } catch (ClassCastException var12) {
               throw new XMLRegistryException("ClassCastException. Class " + var12.getMessage() + " is not of type javax.xml.parsers.DocumentBuilderFactory.", var12);
            } catch (IllegalAccessException var13) {
               throw new XMLRegistryException("IllegalAccessException. Class " + var13.getMessage() + " is not accessible.", var13);
            } catch (InstantiationException var14) {
               throw new XMLRegistryException("InstantiationException. " + var14.getMessage() + " Class instantiation fails. " + "This Class represents an abstract class, an interface, an array class, a primitive type, or void, or instantiation fails for some other reason", var14);
            } catch (SecurityException var15) {
               throw new XMLRegistryException("SecurityException. There is no permission to create a new instance " + var15.getMessage(), var15);
            } catch (Exception var16) {
               throw new XMLRegistryException(var16);
            }
         }
      }

      return var5;
   }

   public DocumentBuilderFactory getDocumentBuilderFactory() throws XMLRegistryException {
      DocumentBuilderFactory var1 = null;
      if (this.havePublicData()) {
         String var2 = this.config.getDocumentBuilderFactory();
         if (var2 != null) {
            try {
               String var3 = this.applicationAccess.getCurrentApplicationName();
               if (var3 != null && this.isAppScopedRegistry) {
                  Thread var4 = Thread.currentThread();
                  ClassLoader var5 = var4.getContextClassLoader();
                  var1 = (DocumentBuilderFactory)Class.forName(var2, true, var5).newInstance();
               } else {
                  var1 = (DocumentBuilderFactory)Class.forName(var2).newInstance();
               }
            } catch (ClassNotFoundException var6) {
               throw new XMLRegistryException("ClassNotFoundException. Class " + var6.getMessage() + " cannot be located.", var6);
            } catch (ClassCastException var7) {
               throw new XMLRegistryException("ClassCastException. Class " + var7.getMessage() + " is not of type javax.xml.parsers.DocumentBuilderFactory.", var7);
            } catch (IllegalAccessException var8) {
               throw new XMLRegistryException("IllegalAccessException. Class " + var8.getMessage() + " is not accessible.", var8);
            } catch (InstantiationException var9) {
               throw new XMLRegistryException("InstantiationException. " + var9.getMessage() + " Class instantiation fails. " + "This Class represents an abstract class, an interface, an array class, a primitive type, or void, or instantiation fails for some other reason", var9);
            } catch (SecurityException var10) {
               throw new XMLRegistryException("SecurityException. There is no permission to create a new instance " + var10.getMessage(), var10);
            } catch (Exception var11) {
               throw new XMLRegistryException(var11);
            }
         }
      }

      return var1;
   }

   public boolean hasDocumentSpecificParserEntries() {
      return this.hasDocumentSpecificParserEntries;
   }

   public boolean hasDocumentSpecificEntityEntries() {
      return this.hasDocumentSpecificEntityEntries;
   }

   public boolean hasCustomParserEntries() {
      return this.hasCustomParserEntries;
   }

   public boolean hasHandleEntityInvalidationSetSupport() {
      return !this.isAppScopedRegistry;
   }

   public String getHandleEntityInvalidation(String var1, String var2) throws XMLRegistryException {
      if (!this.hasHandleEntityInvalidationSetSupport()) {
         return null;
      } else {
         String var3 = null;
         XMLEntitySpecRegistryEntry var4 = this.lookupEntitySpecEntry(var1, var2);
         if (var4 != null) {
            var3 = var4.getHandleEntityInvalidation();
            if (var3 != null && "defer-to-registry-setting".equals(var3)) {
               var3 = this.config.getHandleEntityInvalidation();
            }
         }

         return var3;
      }
   }

   public String getHandleEntityInvalidation() throws XMLRegistryException {
      if (!this.hasHandleEntityInvalidationSetSupport()) {
         return null;
      } else {
         return this.havePublicData() ? this.config.getHandleEntityInvalidation() : null;
      }
   }

   String getExtendedLibraryPath(String var1) {
      return this.privateRegistryDir + File.separatorChar + var1;
   }

   String getApplicationExtendedLibraryPath(String var1) {
      return this.privateRegistryDir + "/" + var1;
   }

   private boolean isURL(String var1) {
      String var2 = var1.trim().toLowerCase(Locale.ENGLISH);
      return var2.startsWith("http://") || var2.startsWith("file://") || var2.startsWith("jdbc:") || var2.startsWith("ftp://");
   }

   private InputStream retrieveEntity(String var1) throws XMLRegistryException {
      if (this.isURL(var1)) {
         return this.retrieveEntityFromURL(var1);
      } else if (this.basePath != null) {
         String var3 = this.basePath.toLowerCase(Locale.ENGLISH);
         return this.retrieveEntityFromApplication(var1);
      } else {
         InputStream var2 = null;
         if ((var2 = this.retrieveEntityFromLocalDirectory(var1)) == null) {
            var2 = this.retrieveEntityFromAdminServer(var1);
         }

         return var2;
      }
   }

   private InputStream retrieveEntityFromURL(String var1) throws XMLRegistryException {
      InputStream var2 = null;

      try {
         URL var3 = new URL(var1);
         URLConnection var6 = var3.openConnection();
         var2 = var6.getInputStream();
         return var2;
      } catch (IOException var5) {
         String var4 = "Can't read provided URL: " + var1;
         throw new XMLRegistryRemoteAccessException(var4, var5);
      }
   }

   private InputStream retrieveEntityFromLocalDirectory(String var1) throws XMLRegistryException {
      FileInputStream var2 = null;
      File var3 = new File(var1);
      if (!var3.isAbsolute()) {
         var3 = new File(this.getExtendedLibraryPath(var1));
      }

      if (var3.exists()) {
         try {
            var2 = new FileInputStream(var3);
         } catch (FileNotFoundException var5) {
         }
      }

      return var2;
   }

   private InputStream retrieveEntityFromAdminServer(String var1) throws XMLRegistryException {
      InputStream var2 = null;
      if (this.havePublicData()) {
         var2 = this.publicRegistryDir.getEntity(var1);
      }

      return var2;
   }

   private InputStream retrieveEntityFromZip(String var1) throws XMLRegistryException {
      InputStream var2 = null;
      ZipFile var3 = null;
      this.basePath = "d:/weblogic/src_131sj/config/mydomain/mydeployments/examples/examples.ear";

      try {
         var3 = new ZipFile(this.basePath);
      } catch (IOException var7) {
         return null;
      }

      ZipEntry var4 = var3.getEntry(var1);
      if (var4 != null) {
         try {
            var2 = var3.getInputStream(var4);
         } catch (IOException var6) {
            throw new XMLRegistryException("Can't read zip entry: " + var1 + " in zip: " + this.basePath, var6);
         }
      }

      return var2;
   }

   private InputStream retrieveEntityFromApplication(String var1) throws XMLRegistryException {
      File var2 = null;
      InputStream var3 = null;
      VirtualJarFile var4 = null;

      ApplicationContextInternal var6;
      try {
         ApplicationAccess var5 = ApplicationAccess.getApplicationAccess();
         var6 = var5.getApplicationContext(this.getAppDeploymentMBean().getName());
         if (var6 == null) {
            return null;
         }

         var4 = var6.getApplicationFileManager().getVirtualJarFile();
      } catch (IOException var14) {
         return null;
      }

      ZipEntry var15 = var4.getEntry("lib/xml/registry/" + var1);

      try {
         if (!(new File(this.basePath)).isDirectory()) {
            var6 = null;
            File var7 = (new File(this.basePath)).getParentFile();
            if (var7.isDirectory()) {
               var2 = new File(var7, var1);
               if (this.cleanupTempEntities) {
                  if (var2.getParentFile() != null && !var2.getParentFile().exists()) {
                     var2.getParentFile().mkdirs();
                     var2.getParentFile().deleteOnExit();
                  }

                  var2.createNewFile();
                  var2.deleteOnExit();
                  BufferedOutputStream var16 = new BufferedOutputStream(new FileOutputStream(var2));
                  InputStream var8 = var4.getInputStream(var15);
                  byte[] var9 = new byte[4096];
                  boolean var10 = false;

                  int var17;
                  while((var17 = var8.read(var9, 0, 4096)) != -1) {
                     var16.write(var9, 0, var17);
                  }

                  var16.flush();
                  var8.close();
                  var16.close();
                  if (!this.isAppScopedRegistry) {
                     this.cleanupTempEntities = false;
                  }
               }
            }
         }
      } catch (IOException var13) {
         throw new XMLRegistryException("Can't read zip entry: " + var1 + " in zip: " + this.basePath, var13);
      }

      if (var15 != null) {
         var6 = null;

         try {
            if (!(new File(this.basePath)).isDirectory()) {
               var3 = this.retrieveEntityFromLocalDirectory(var2.getAbsolutePath());
            } else {
               var3 = var4.getInputStream(var15);
            }
         } catch (IOException var12) {
            throw new XMLRegistryException("Can't read zip entry: " + var1 + " in zip: " + this.basePath, var12);
         }
      }

      try {
         var4.close();
         return var3;
      } catch (IOException var11) {
         return null;
      }
   }

   private void loadPrivateRegistry() throws XMLRegistryException {
      this.psIndex = new HashMap();
      this.esIndex = new HashMap();
      this.initializePrivateEntries();
   }

   private void loadPublicRegistry() throws XMLRegistryException {
      if (this.havePublicData()) {
         this.hasCustomParserEntries = false;
         this.hasDocumentSpecificParserEntries = false;
         this.hasDocumentSpecificEntityEntries = false;
         this.loadPublicEntries();
      }

      this.preLoadCache();
   }

   void preLoadCache() {
      if (this.havePublicData()) {
         RegistryEntityResolver var1 = null;
         Iterator var2 = this.esIndex.values().iterator();

         while(var2.hasNext()) {
            Object var3 = null;
            XMLEntitySpecRegistryEntry var4 = (XMLEntitySpecRegistryEntry)var2.next();
            String var5 = Tools.getEntityDescriptor(var4.getPublicId(), var4.getSystemId());
            String var6 = var4.getWhenToCache();
            if (var6 == null || var6.equals("defer-to-registry-setting")) {
               var6 = this.config.getWhenToCache();
            }

            if (var6 != null && var6.equals("cache-at-initialization")) {
               if (var1 == null) {
                  try {
                     var1 = new RegistryEntityResolver(new XMLRegistry[]{this});
                  } catch (Exception var9) {
                  }
               }

               try {
                  var1.resolveEntity(var4.getPublicId(), var4.getSystemId());
               } catch (Exception var8) {
               }
            }
         }

      }
   }

   private XMLParserSelectRegistryEntry lookupParserSelectEntry(String var1, String var2, String var3) throws XMLRegistryException {
      if (var1 == null && var2 == null && var3 == null) {
         throw new XMLRegistryException("Invalid parameters: at least one of publicId, systemId, rootTag must be non-null");
      } else {
         Iterator var4 = this.psIndex.values().iterator();

         XMLParserSelectRegistryEntry var5;
         do {
            if (!var4.hasNext()) {
               var4 = this.newParserSelectMBeans.iterator();

               XMLParserSelectRegistryEntryMBean var7;
               do {
                  if (!var4.hasNext()) {
                     return null;
                  }

                  var7 = (XMLParserSelectRegistryEntryMBean)var4.next();
               } while(!this.matchesParserSelectMBean(var7, var1, var2, (String)null));

               XMLParserSelectRegistryEntry var6 = this.loadParserSelectEntry(ConfigAbstraction.getParserSelectEntryConfig(var7), true);
               this.newParserSelectMBeans.remove(var7);
               return var6;
            }

            var5 = (XMLParserSelectRegistryEntry)var4.next();
         } while(!this.matches(var5, var1, var2, var3));

         return var5;
      }
   }

   private XMLEntitySpecRegistryEntry lookupEntitySpecEntry(String var1, String var2) throws XMLRegistryException {
      if (var1 == null && var2 == null) {
         throw new XMLRegistryException("Invalid parameters: at least one of publicId or systemId must be non-null");
      } else {
         Iterator var3 = this.esIndex.values().iterator();

         XMLEntitySpecRegistryEntry var4;
         do {
            if (!var3.hasNext()) {
               var3 = this.newEntitySpecMBeans.iterator();

               XMLEntitySpecRegistryEntryMBean var6;
               do {
                  if (!var3.hasNext()) {
                     return null;
                  }

                  var6 = (XMLEntitySpecRegistryEntryMBean)var3.next();
               } while(!this.matchesEntitySpecMBean(var6, var1, var2, (String)null));

               XMLEntitySpecRegistryEntry var5 = this.loadEntitySpecEntry(ConfigAbstraction.getEntitySpecEntryConfig(var6), true);
               this.newEntitySpecMBeans.remove(var6);
               return var5;
            }

            var4 = (XMLEntitySpecRegistryEntry)var3.next();
         } while(!this.matches(var4, var1, var2, (String)null));

         return var4;
      }
   }

   private boolean matches(XMLAbstractRegistryEntry var1, String var2, String var3, String var4) {
      if (var2 != null && var2.equals(var1.getPublicId())) {
         return true;
      } else if (var3 != null && var3.equals(var1.getSystemId())) {
         return true;
      } else {
         return var4 != null && var4.equals(var1.getRootElementTag());
      }
   }

   private boolean matchesParserSelectMBean(XMLParserSelectRegistryEntryMBean var1, String var2, String var3, String var4) {
      if (var2 != null && var2.equals(var1.getPublicId())) {
         return true;
      } else {
         return var3 != null && var3.equals(var1.getSystemId());
      }
   }

   private boolean matchesEntitySpecMBean(XMLEntitySpecRegistryEntryMBean var1, String var2, String var3, String var4) {
      if (var2 != null && var2.equals(var1.getPublicId())) {
         return true;
      } else {
         return var3 != null && var3.equals(var1.getSystemId());
      }
   }

   private void loadPublicEntries() throws XMLRegistryException {
      if (this.havePublicData()) {
         Enumeration var1;
         if (!this.isAppScopedRegistry) {
            var1 = this.config.getParserSelectRegistryEntries();

            while(var1 != null && var1.hasMoreElements()) {
               this.loadParserSelectEntry((ConfigAbstraction.ParserSelectEntryConfig)var1.nextElement(), true);
            }
         }

         var1 = this.config.getEntitySpecRegistryEntries();

         while(var1.hasMoreElements()) {
            this.loadEntitySpecEntry((ConfigAbstraction.EntitySpecEntryConfig)var1.nextElement(), true);
         }

      }
   }

   private synchronized XMLParserSelectRegistryEntry loadParserSelectEntry(ConfigAbstraction.ParserSelectEntryConfig var1, boolean var2) {
      XMLParserSelectRegistryEntry var3 = this.readParserSelectEntry(var1);
      if (this.hasCustomParser(var3)) {
         this.hasCustomParserEntries = true;
      }

      if (this.hasDocumentSpecificParserEntry(var3)) {
         this.hasDocumentSpecificParserEntries = true;
      }

      DocumentType var4 = this.getDocumentType(var3);
      if (var2) {
         var3.setListener(new ParserSelectEntryListener(var1, var3));
         var1.addPropertyChangeListener(var3.getListener());
      }

      this.psIndex.put(var4, var3);
      return var3;
   }

   private synchronized XMLEntitySpecRegistryEntry loadEntitySpecEntry(ConfigAbstraction.EntitySpecEntryConfig var1, boolean var2) {
      XMLEntitySpecRegistryEntry var3 = this.readEntitySpecEntry(var1);
      if (this.hasDocumentSpecificEntityEntry(var3)) {
         this.hasDocumentSpecificEntityEntries = true;
      }

      if (var2) {
         var3.setListener(new EntitySpecEntryListener(var1, var3));
         var1.addPropertyChangeListener(var3.getListener());
      }

      DocumentType var4 = this.getDocumentType(var3);
      this.esIndex.put(var4, var3);
      return var3;
   }

   private boolean hasCustomParser(XMLParserSelectRegistryEntry var1) {
      return var1.getParserClassName() != null;
   }

   private boolean hasDocumentSpecificParserEntry(XMLParserSelectRegistryEntry var1) {
      return var1.getSAXParserFactory() != null || var1.getDocumentBuilderFactory() != null || var1.getTransformerFactory() != null || var1.getParserClassName() != null;
   }

   private boolean hasDocumentSpecificEntityEntry(XMLEntitySpecRegistryEntry var1) {
      return var1.getEntityURI() != null || var1.getWhenToCache() != null || var1.getHandleEntityInvalidation() != null;
   }

   private XMLParserSelectRegistryEntry readParserSelectEntry(ConfigAbstraction.ParserSelectEntryConfig var1) {
      String var2 = var1.getPublicId();
      String var3 = var1.getSystemId();
      String var4 = var1.getRootElementTag();
      XMLParserSelectRegistryEntry var5 = new XMLParserSelectRegistryEntry(var2, var3, var4, var1);
      var5.setDocumentBuilderFactory(var1.getDocumentBuilderFactory());
      var5.setSAXParserFactory(var1.getSAXParserFactory());
      var5.setTransformerFactory(var1.getTransformerFactory());
      var5.setParserClassName(var1.getParserClassName());
      return var5;
   }

   private XMLEntitySpecRegistryEntry readEntitySpecEntry(ConfigAbstraction.EntitySpecEntryConfig var1) {
      String var2 = var1.getPublicId();
      String var3 = var1.getSystemId();
      XMLEntitySpecRegistryEntry var4 = new XMLEntitySpecRegistryEntry(var2, var3, var1);
      var4.setEntityURI(var1.getEntityURI());
      var4.setWhenToCache(var1.getWhenToCache());
      int var5 = var1.getCacheTimeoutInterval();
      var4.setCacheTimeoutInterval(var5);
      String var6 = var1.getHandleEntityInvalidation();
      var4.setHandleEntityInvalidation(var6);
      return var4;
   }

   private synchronized XMLEntitySpecRegistryEntry updateEntitySpecEntry(ConfigAbstraction.EntitySpecEntryConfig var1, XMLEntitySpecRegistryEntry var2) {
      var2.setEntityURI(var1.getEntityURI());
      var2.setWhenToCache(var1.getWhenToCache());
      var2.setCacheTimeoutInterval(var1.getCacheTimeoutInterval());
      var2.setHandleEntityInvalidation(var1.getHandleEntityInvalidation());
      return var2;
   }

   private synchronized XMLParserSelectRegistryEntry updateParserSelectEntry(ConfigAbstraction.ParserSelectEntryConfig var1, XMLParserSelectRegistryEntry var2) {
      var2.setDocumentBuilderFactory(var1.getDocumentBuilderFactory());
      var2.setSAXParserFactory(var1.getSAXParserFactory());
      var2.setTransformerFactory(var1.getTransformerFactory());
      var2.setParserClassName(var1.getParserClassName());
      return var2;
   }

   private void initializePrivateEntries() {
      this.addPrivateResolverEntry("-//Sun Microsystems, Inc.//DTD Enterprise JavaBeans 1.1//EN", "http://java.sun.com/j2ee/dtds/ejb-jar_1_1.dtd", "ejb11-jar.dtd");
      this.addPrivateResolverEntry("-//Sun Microsystems, Inc.//DTD Enterprise JavaBeans 2.0//EN", "http://java.sun.com/j2ee/dtds/ejb-jar_2_0.dtd", "ejb20-jar.dtd");
      this.addPrivateResolverEntry("-//BEA Systems, Inc.//DTD WebLogic 5.1.0 EJB//EN", "http://www.bea.com/servers/wls510/dtd/weblogic-ejb-jar.dtd", "weblogic510-ejb-jar.dtd");
      this.addPrivateResolverEntry("-//BEA Systems, Inc.//DTD WebLogic 6.0.0 EJB//EN", "http://www.bea.com/servers/wls600/dtd/weblogic-ejb-jar.dtd", "weblogic600-ejb-jar.dtd");
      this.addPrivateResolverEntry("-//BEA Systems, Inc.//DTD WebLogic 5.1.0 EJB RDBMS Persistence//EN", "http://www.bea.com/servers/wls510/dtd/weblogic-rdbms-persistence.dtd", "weblogic-rdbms-persistence.dtd");
      this.addPrivateResolverEntry("-//BEA Systems, Inc.//DTD WebLogic 6.0.0 EJB RDBMS Persistence//EN", "http://www.bea.com/servers/wls600/dtd/weblogic-rdbms20-persistence-600.dtd", "weblogic-rdbms20-persistence-600.dtd");
   }

   private void addPrivateResolverEntry(String var1, String var2, String var3) {
      DocumentType var4 = new DocumentType(var1, var2, (String)null);
      if (!this.esIndex.containsKey(var4)) {
         XMLEntitySpecRegistryEntry var5 = new XMLEntitySpecRegistryEntry(var1, var2, (ConfigAbstraction.EntryConfig)null);
         var5.setEntityURI(var3);
         var5.setPrivate(true);
         var5.setCacheTimeoutInterval(600);
         this.esIndex.put(var4, var5);
      }

   }

   private DocumentType getDocumentType(XMLAbstractRegistryEntry var1) {
      return new DocumentType(var1.getPublicId(), var1.getSystemId(), var1.getRootElementTag());
   }

   public boolean havePublicData() {
      return this.config != null;
   }

   void removeEntry(XMLEntitySpecRegistryEntry var1) {
      try {
         this.getCache().remove(var1.getPublicId(), var1.getSystemId());
      } catch (Exception var3) {
      }

      DocumentType var2 = this.getDocumentType(var1);
      this.esIndex.remove(var2);
   }

   void removeEntry(XMLParserSelectRegistryEntry var1) {
      DocumentType var2 = this.getDocumentType(var1);
      this.psIndex.remove(var2);
   }

   static class ServerListener implements PropertyChangeListener {
      public void propertyChange(PropertyChangeEvent var1) {
         String var2 = var1.getPropertyName();
         if (var2.equalsIgnoreCase("xmlregistry")) {
            try {
               XMLRegistry.reinitializeDefaultRegistry();
            } catch (XMLRegistryException var4) {
            }
         }

      }
   }

   class CacheListener implements PropertyChangeListener {
      XMLRegistry registry = null;

      CacheListener(XMLRegistry var2) {
         this.registry = var2;
      }

      public void propertyChange(PropertyChangeEvent var1) {
         String var2 = var1.getPropertyName();
         Object var3 = var1.getNewValue();
         if ("CacheMemorySize".equals(var2) && var3 != null) {
            try {
               this.registry.getCache().setMemorySize((Integer)var3 * 1000);
            } catch (Exception var7) {
            }
         } else if ("CacheDiskSize".equals(var2) && var3 != null) {
            try {
               this.registry.getCache().setDiskSize((Integer)var3 * 1000000);
            } catch (Exception var6) {
            }
         } else if ("CacheTimeoutInterval".equals(var2)) {
            try {
               if (var3 != null) {
                  this.registry.setCacheDefaultTimeoutInterval((Integer)var3 * 1000);
               } else {
                  this.registry.setCacheDefaultTimeoutInterval(-1);
               }
            } catch (Exception var5) {
            }
         }

      }
   }

   class RegistryListener implements PropertyChangeListener {
      XMLRegistry registry = null;

      RegistryListener(XMLRegistry var2) {
         this.registry = var2;
      }

      public void propertyChange(PropertyChangeEvent var1) {
         String var2 = var1.getPropertyName();
         if (var1.getOldValue() == null && var1.getNewValue() != null) {
            this.addNewMBean(var2, (WebLogicMBean)var1.getNewValue());
         } else if (var1.getOldValue() != null && var1.getNewValue() == null) {
            this.removeOldMBean(var2, (WebLogicMBean)var1.getOldValue());
         } else if (var1.getNewValue() != null && (var2.equalsIgnoreCase("ParserSelectRegistryEntries") || var2.equalsIgnoreCase("EntitySpecRegistryEntries"))) {
            WebLogicMBean var3 = this.findDeletedMBean(var2, (Object[])((Object[])var1.getOldValue()), (Object[])((Object[])var1.getNewValue()));
            this.removeOldMBean(var2, var3);
         }

      }

      WebLogicMBean findDeletedMBean(String var1, Object[] var2, Object[] var3) {
         try {
            Object var4 = null;
            Object var5 = null;
            int var6 = 0;

            while(var6 < var2.length) {
               var4 = var2[var6];
               boolean var7 = false;
               int var9 = 0;

               while(true) {
                  if (var9 < var3.length) {
                     var5 = var3[var9];
                     if (!var4.equals(var5)) {
                        ++var9;
                        continue;
                     }
                  }

                  if (var9 == var3.length) {
                     return (WebLogicMBean)var4;
                  }

                  ++var6;
                  break;
               }
            }
         } catch (Exception var8) {
         }

         return null;
      }

      void addNewMBean(String var1, WebLogicMBean var2) {
         if ("ParserSelectRegistryEntries".equals(var1)) {
            XMLParserSelectRegistryEntryMBean var3 = (XMLParserSelectRegistryEntryMBean)var2;
            if (var3.getPublicId() == null && var3.getSystemId() == null && var3.getRootElementTag() == null) {
               XMLRegistry.this.newParserSelectMBeans.add(var3);
               this.registry.hasDocumentSpecificParserEntries = true;
            } else {
               XMLParserSelectRegistryEntry var4 = this.registry.loadParserSelectEntry(ConfigAbstraction.getParserSelectEntryConfig(var3), true);
            }
         } else if ("EntitySpecRegistryEntries".equals(var1)) {
            XMLEntitySpecRegistryEntryMBean var5 = (XMLEntitySpecRegistryEntryMBean)var2;
            if (var5.getPublicId() == null && var5.getSystemId() == null) {
               XMLRegistry.this.newEntitySpecMBeans.add(var5);
               this.registry.hasDocumentSpecificEntityEntries = true;
            } else {
               XMLEntitySpecRegistryEntry var6 = this.registry.loadEntitySpecEntry(ConfigAbstraction.getEntitySpecEntryConfig(var5), true);
            }
         }

      }

      void removeOldMBean(String var1, WebLogicMBean var2) {
         try {
            if ("ParserSelectRegistryEntries".equals(var1)) {
               XMLParserSelectRegistryEntryMBean var3 = (XMLParserSelectRegistryEntryMBean)var2;
               XMLParserSelectRegistryEntry var4 = this.registry.readParserSelectEntry(ConfigAbstraction.getParserSelectEntryConfig(var3));
               this.registry.removeEntry(var4);
            } else if ("EntitySpecRegistryEntries".equals(var1)) {
               XMLEntitySpecRegistryEntryMBean var6 = (XMLEntitySpecRegistryEntryMBean)var2;
               XMLEntitySpecRegistryEntry var7 = this.registry.readEntitySpecEntry(ConfigAbstraction.getEntitySpecEntryConfig(var6));
               this.registry.removeEntry(var7);
            }
         } catch (Exception var5) {
         }

      }
   }

   class EntitySpecEntryListener implements PropertyChangeListener {
      ConfigAbstraction.EntitySpecEntryConfig mbean = null;
      XMLEntitySpecRegistryEntry entry = null;

      EntitySpecEntryListener(ConfigAbstraction.EntitySpecEntryConfig var2, XMLEntitySpecRegistryEntry var3) {
         this.mbean = var2;
         this.entry = var3;
      }

      public void propertyChange(PropertyChangeEvent var1) {
         String var2 = var1.getPropertyName();
         if (!var2.equalsIgnoreCase("parent")) {
            Object var3 = var1.getOldValue();
            Object var4 = var1.getNewValue();
            if (var3 == null) {
               var3 = "";
            }

            if (var4 == null) {
               var4 = "";
            }

            if (!var3.equals(var4)) {
               if (!var2.equalsIgnoreCase("publicid") && !var2.equalsIgnoreCase("systemid")) {
                  if (this.entry != null) {
                     XMLRegistry.this.updateEntitySpecEntry(this.mbean, this.entry);

                     try {
                        XMLRegistry.this.getCache().putrify(this.mbean.getPublicId(), this.mbean.getSystemId());
                     } catch (Exception var6) {
                     }
                  }
               } else {
                  if (this.entry != null) {
                     XMLRegistry.this.removeEntry(this.entry);
                  }

                  this.entry = XMLRegistry.this.loadEntitySpecEntry(this.mbean, false);
               }
            }
         }

      }
   }

   class ParserSelectEntryListener implements PropertyChangeListener {
      ConfigAbstraction.ParserSelectEntryConfig mbean = null;
      XMLParserSelectRegistryEntry entry = null;

      ParserSelectEntryListener(ConfigAbstraction.ParserSelectEntryConfig var2, XMLParserSelectRegistryEntry var3) {
         this.mbean = var2;
         this.entry = var3;
      }

      public void propertyChange(PropertyChangeEvent var1) {
         String var2 = var1.getPropertyName();
         if (!var2.equalsIgnoreCase("parent")) {
            String var3 = (String)var1.getOldValue();
            String var4 = (String)var1.getNewValue();
            if (var3 == null) {
               var3 = "";
            }

            if (var4 == null) {
               var4 = "";
            }

            if (!var3.equals(var4)) {
               if (!var2.equalsIgnoreCase("publicid") && !var2.equalsIgnoreCase("systemid") && !var2.equalsIgnoreCase("rootelementtag")) {
                  if (this.entry != null) {
                     XMLRegistry.this.updateParserSelectEntry(this.mbean, this.entry);
                  }
               } else {
                  if (this.entry != null) {
                     XMLRegistry.this.removeEntry(this.entry);
                  }

                  this.entry = XMLRegistry.this.loadParserSelectEntry(this.mbean, false);
               }
            }
         }

      }
   }

   public class ResolvedEntity {
      XMLEntitySpecRegistryEntry entry = null;
      InputSource inputSource = null;
      boolean isLocal = false;
      boolean isSubjectToCaching = true;

      public InputSource inputSource() {
         return this.inputSource;
      }

      public boolean isSubjectToCaching() {
         return this.isSubjectToCaching;
      }

      public boolean isLocal() {
         return this.isLocal;
      }

      public XMLEntitySpecRegistryEntry getEntry() {
         return this.entry;
      }
   }

   class CacheLogListener implements weblogic.xml.util.cache.entitycache.CacheListener {
      public void notify(Event.CacheUtilityEvent var1) {
         try {
            if (var1 instanceof Event.MemoryPurgeEvent) {
               Event.MemoryPurgeEvent var2 = (Event.MemoryPurgeEvent)var1;
               XMLLogger.logCacheMemoryPurge(var2.cacheEntries.size(), var2.combinedMemorySize, var2.currentMemorySize);
            } else if (var1 instanceof Event.DiskPurgeEvent) {
               Event.DiskPurgeEvent var5 = (Event.DiskPurgeEvent)var1;
               XMLLogger.logCacheDiskPurge(var5.cacheEntries.size(), var5.combinedDiskSize, var5.currentDiskSize);
            } else if (var1 instanceof Event.EntryDiskRejectionEvent) {
               Event.EntryDiskRejectionEvent var6 = (Event.EntryDiskRejectionEvent)var1;
               XMLLogger.logCacheDiskRejection(XMLRegistry.this.getName(), XMLRegistry.this.getCache().getDescription(var6.cacheEntry.getCacheKey()), var6.diskSize);
            } else if (var1 instanceof Event.EntryRejectionEvent) {
               Event.EntryRejectionEvent var7 = (Event.EntryRejectionEvent)var1;
               XMLLogger.logCacheRejection(XMLRegistry.this.getName(), XMLRegistry.this.getCache().getDescription(var7.cacheEntry.getCacheKey()), var7.memorySize);
            } else if (var1 instanceof Event.EntryAddEvent) {
               Event.EntryAddEvent var8 = (Event.EntryAddEvent)var1;
               XMLLogger.logCacheEntryAdd(XMLRegistry.this.getName(), XMLRegistry.this.getCache().getDescription(var8.cacheEntry.getCacheKey()), var8.memorySize, var8.cacheEntry.isPersistent() ? "Persistent" : "Transient", var8.currentMemorySize);
            } else if (var1 instanceof Event.EntryDeleteEvent) {
               Event.EntryDeleteEvent var9 = (Event.EntryDeleteEvent)var1;
               XMLLogger.logCacheEntryDelete(XMLRegistry.this.getName(), XMLRegistry.this.getCache().getDescription(var9.cacheEntry.getCacheKey()), var9.memorySize, var9.diskSize, var9.currentMemorySize, var9.currentDiskSize);
            } else if (var1 instanceof Event.EntryPersistEvent) {
               Event.EntryPersistEvent var10 = (Event.EntryPersistEvent)var1;
               XMLLogger.logCacheEntryPersist(XMLRegistry.this.getName(), XMLRegistry.this.getCache().getDescription(var10.cacheEntry.getCacheKey()), var10.diskSize, var10.currentDiskSize);
            } else if (var1 instanceof Event.EntryLoadEvent) {
               Event.EntryLoadEvent var11 = (Event.EntryLoadEvent)var1;
               XMLLogger.logCacheEntryLoad(XMLRegistry.this.getName(), XMLRegistry.this.getCache().getDescription(var11.cacheEntry.getCacheKey()), var11.memorySize, var11.currentMemorySize);
            } else if (var1 instanceof Event.StatCheckpointEvent) {
               Event.StatCheckpointEvent var12 = (Event.StatCheckpointEvent)var1;
               XMLLogger.logCacheStatisticsCheckpoint();
            } else if (var1 instanceof Event.CacheCreationEvent) {
               Event.CacheCreationEvent var13 = (Event.CacheCreationEvent)var1;
               XMLLogger.logCacheCreation(var13.currentMemorySize, var13.currentDiskSize);
            } else if (var1 instanceof Event.CacheLoadEvent) {
               Event.CacheLoadEvent var14 = (Event.CacheLoadEvent)var1;
               XMLLogger.logCacheLoad(var14.currentMemorySize, var14.currentDiskSize);
            } else if (var1 instanceof Event.CacheCloseEvent) {
               Event.CacheCloseEvent var15 = (Event.CacheCloseEvent)var1;
               XMLLogger.logCacheClose(var15.currentDiskSize);
            } else if (var1 instanceof Event.CacheCorruptionEvent) {
               Event.CacheCorruptionEvent var16 = (Event.CacheCorruptionEvent)var1;
               XMLLogger.logCacheCorrupted(var16.path);
            } else if (var1 instanceof Event.EntryCorruptionEvent) {
               Event.EntryCorruptionEvent var17 = (Event.EntryCorruptionEvent)var1;
               XMLLogger.logCacheEntryCorrupted(XMLRegistry.this.getName(), var17.path, XMLRegistry.this.getCache().getDescription(var17.key));
            } else if (var1 instanceof Event.StatisticsCorruptionEvent) {
               Event.StatisticsCorruptionEvent var18 = (Event.StatisticsCorruptionEvent)var1;
               XMLLogger.logCacheStatisticsCorrupted(var18.path);
            } else if (var1 instanceof Event.FileAccessErrorForEntryEvent) {
               Event.FileAccessErrorForEntryEvent var19 = (Event.FileAccessErrorForEntryEvent)var1;
               if (var19.onWrite) {
                  XMLLogger.logCacheEntrySaveError(XMLRegistry.this.getName(), var19.path, XMLRegistry.this.getCache().getDescription(var19.cacheEntry.getCacheKey()));
               } else {
                  XMLLogger.logCacheEntryReadError(XMLRegistry.this.getName(), var19.path, XMLRegistry.this.getCache().getDescription(var19.cacheEntry.getCacheKey()));
               }
            } else if (var1 instanceof Event.FileAccessErrorForCacheEvent) {
               Event.FileAccessErrorForCacheEvent var20 = (Event.FileAccessErrorForCacheEvent)var1;
               if (var20.onWrite) {
                  XMLLogger.logCacheSaveError(var20.path);
               } else {
                  XMLLogger.logCacheReadError(var20.path);
               }
            } else if (var1 instanceof Event.FileAccessErrorForStatisticsEvent) {
               Event.FileAccessErrorForStatisticsEvent var21 = (Event.FileAccessErrorForStatisticsEvent)var1;
               if (var21.onWrite) {
                  XMLLogger.logCacheStatisticsSaveError(var21.path);
               } else {
                  XMLLogger.logCacheStatisticsReadError(var21.path);
               }
            } else if (var1 instanceof Event.OutOfMemoryLoadingEntryEvent) {
               Event.OutOfMemoryLoadingEntryEvent var22 = (Event.OutOfMemoryLoadingEntryEvent)var1;
               XMLLogger.logCacheOutOfMemoryOnEntryLoad(XMLRegistry.this.getName(), XMLRegistry.this.getCache().getDescription(var22.key), var22.path);
            } else if (var1 instanceof Event.OutOfMemoryLoadingCacheEvent) {
               Event.OutOfMemoryLoadingCacheEvent var23 = (Event.OutOfMemoryLoadingCacheEvent)var1;
               XMLLogger.logCacheOutOfMemoryOnLoad(var23.path);
            } else if (var1 instanceof Event.OutOfMemoryLoadingStatisticsEvent) {
               Event.OutOfMemoryLoadingStatisticsEvent var24 = (Event.OutOfMemoryLoadingStatisticsEvent)var1;
               XMLLogger.logCacheOutOfMemoryOnStatisticsLoad(var24.path);
            } else if (var1 instanceof Event.CacheFailureEvent) {
               Event.CacheFailureEvent var25 = (Event.CacheFailureEvent)var1;
               String var3 = var25.path;
               if (var25.cache != null) {
                  var3 = var25.cache.getName();
               }

               XMLLogger.logCacheUnexpectedProblem(var25.message);
            }
         } catch (Exception var4) {
         }

      }

      public void cacheUpdateOccured(Object var1, String var2) {
      }
   }
}
