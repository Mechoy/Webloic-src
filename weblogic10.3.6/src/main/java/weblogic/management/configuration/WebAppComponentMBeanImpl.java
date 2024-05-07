package weblogic.management.configuration;

import java.io.Serializable;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.CRC32;
import javax.management.AttributeNotFoundException;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanException;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.beangen.StringHelper;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBeanHelper;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.ReferenceManager;
import weblogic.descriptor.internal.ResolvedReference;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.management.DistributedManagementException;
import weblogic.management.ManagementException;
import weblogic.management.mbeans.custom.WebAppComponent;
import weblogic.utils.ArrayUtils;
import weblogic.utils.collections.CombinedIterator;

public class WebAppComponentMBeanImpl extends ComponentMBeanImpl implements WebAppComponentMBean, Serializable {
   private TargetMBean[] _ActivatedTargets;
   private ApplicationMBean _Application;
   private String _AuthFilter;
   private String _AuthRealmName;
   private boolean _CleanupSessionFilesEnabled;
   private String _ContextPath;
   private boolean _DebugEnabled;
   private String _DefaultServlet;
   private VirtualHostMBean[] _DeployedVirtualHosts;
   private String _DocumentRoot;
   private boolean _IndexDirectoryEnabled;
   private String[] _IndexFiles;
   private String _MimeTypeDefault;
   private Map _MimeTypes;
   private String _Name;
   private boolean _PreferWebInfClasses;
   private String _ServletClasspath;
   private boolean _ServletExtensionCaseSensitive;
   private int _ServletReloadCheckSecs;
   private String[] _Servlets;
   private int _SessionCacheSize;
   private String _SessionCookieComment;
   private String _SessionCookieDomain;
   private int _SessionCookieMaxAgeSecs;
   private String _SessionCookieName;
   private String _SessionCookiePath;
   private boolean _SessionCookiesEnabled;
   private boolean _SessionDebuggable;
   private int _SessionIDLength;
   private int _SessionInvalidationIntervalSecs;
   private int _SessionJDBCConnectionTimeoutSecs;
   private String _SessionMainAttribute;
   private boolean _SessionMonitoringEnabled;
   private String _SessionPersistentStoreCookieName;
   private String _SessionPersistentStoreDir;
   private String _SessionPersistentStorePool;
   private boolean _SessionPersistentStoreShared;
   private String _SessionPersistentStoreTable;
   private String _SessionPersistentStoreType;
   private int _SessionSwapIntervalSecs;
   private int _SessionTimeoutSecs;
   private boolean _SessionTrackingEnabled;
   private boolean _SessionURLRewritingEnabled;
   private int _SingleThreadedServletPoolSize;
   private TargetMBean[] _Targets;
   private VirtualHostMBean[] _VirtualHosts;
   private WebServerMBean[] _WebServers;
   private WebAppComponent _customizer;
   private static SchemaHelper2 _schemaHelper;

   public WebAppComponentMBeanImpl() {
      try {
         this._customizer = new WebAppComponent(this);
      } catch (Exception var2) {
         if (var2 instanceof RuntimeException) {
            throw (RuntimeException)var2;
         }

         throw new UndeclaredThrowableException(var2);
      }

      this._initializeProperty(-1);
   }

   public WebAppComponentMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);

      try {
         this._customizer = new WebAppComponent(this);
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         }

         throw new UndeclaredThrowableException(var4);
      }

      this._initializeProperty(-1);
   }

   public ApplicationMBean getApplication() {
      return this._customizer.getApplication();
   }

   public String getName() {
      if (!this._isSet(2)) {
         try {
            return ((ConfigurationMBean)this.getParent()).getName();
         } catch (NullPointerException var2) {
         }
      }

      return this._customizer.getName();
   }

   public int getSessionCookieMaxAgeSecs() {
      return this._SessionCookieMaxAgeSecs;
   }

   public TargetMBean[] getTargets() {
      return this._customizer.getTargets();
   }

   public String getTargetsAsString() {
      return this._getHelper()._serializeKeyList(this.getTargets());
   }

   public WebServerMBean[] getWebServers() {
      return this._WebServers;
   }

   public boolean isApplicationSet() {
      return this._isSet(9);
   }

   public boolean isNameSet() {
      return this._isSet(2);
   }

   public boolean isSessionCookieMaxAgeSecsSet() {
      return this._isSet(15);
   }

   public boolean isTargetsSet() {
      return this._isSet(7);
   }

   public boolean isWebServersSet() {
      return this._isSet(12);
   }

   public void setTargetsAsString(String var1) {
      if (var1 != null && var1.length() != 0) {
         String[] var13 = this._getHelper()._splitKeyList(var1);
         List var3 = this._getHelper()._getKeyList(this._Targets);

         String var5;
         for(int var4 = 0; var4 < var13.length; ++var4) {
            var5 = var13[var4];
            var5 = var5 == null ? null : var5.trim();
            if (var3.contains(var5)) {
               var3.remove(var5);
            } else {
               this._getReferenceManager().registerUnresolvedReference(var5, TargetMBean.class, new ReferenceManager.Resolver(this, 7) {
                  public void resolveReference(Object var1) {
                     try {
                        WebAppComponentMBeanImpl.this.addTarget((TargetMBean)var1);
                     } catch (RuntimeException var3) {
                        throw var3;
                     } catch (Exception var4) {
                        throw new AssertionError("Impossible exception: " + var4);
                     }
                  }
               });
            }
         }

         Iterator var14 = var3.iterator();

         while(true) {
            while(var14.hasNext()) {
               var5 = (String)var14.next();
               TargetMBean[] var6 = this._Targets;
               int var7 = var6.length;

               for(int var8 = 0; var8 < var7; ++var8) {
                  TargetMBean var9 = var6[var8];
                  if (var5.equals(var9.getName())) {
                     try {
                        this.removeTarget(var9);
                        break;
                     } catch (RuntimeException var11) {
                        throw var11;
                     } catch (Exception var12) {
                        throw new AssertionError("Impossible exception: " + var12);
                     }
                  }
               }
            }

            return;
         }
      } else {
         TargetMBean[] var2 = this._Targets;
         this._initializeProperty(7);
         this._postSet(7, var2, this._Targets);
      }
   }

   public void setApplication(ApplicationMBean var1) throws InvalidAttributeValueException {
      this._customizer.setApplication(var1);
   }

   public void setName(String var1) throws InvalidAttributeValueException, ManagementException {
      var1 = var1 == null ? null : var1.trim();
      weblogic.descriptor.beangen.LegalChecks.checkNonEmptyString("Name", var1);
      weblogic.descriptor.beangen.LegalChecks.checkNonNull("Name", var1);
      ConfigurationValidator.validateName(var1);
      String var2 = this.getName();
      this._customizer.setName(var1);
      this._postSet(2, var2, var1);
   }

   public void setSessionCookieMaxAgeSecs(int var1) throws InvalidAttributeValueException {
      this._SessionCookieMaxAgeSecs = var1;
   }

   public void setTargets(TargetMBean[] var1) throws InvalidAttributeValueException, DistributedManagementException {
      Object var4 = var1 == null ? new TargetMBeanImpl[0] : var1;
      var1 = (TargetMBean[])((TargetMBean[])this._getHelper()._cleanAndValidateArray(var4, TargetMBean.class));

      for(int var2 = 0; var2 < var1.length; ++var2) {
         if (var1[var2] != null) {
            ResolvedReference var3 = new ResolvedReference(this, 7, (AbstractDescriptorBean)var1[var2]) {
               protected Object getPropertyValue() {
                  return WebAppComponentMBeanImpl.this.getTargets();
               }
            };
            this._getReferenceManager().registerResolvedReference((AbstractDescriptorBean)var1[var2], var3);
         }
      }

      TargetMBean[] var5 = this.getTargets();
      this._customizer.setTargets(var1);
      this._postSet(7, var5, var1);
   }

   public void setWebServers(WebServerMBean[] var1) throws InvalidAttributeValueException, DistributedManagementException {
      Object var2 = var1 == null ? new WebServerMBeanImpl[0] : var1;
      this._WebServers = (WebServerMBean[])var2;
   }

   public boolean addTarget(TargetMBean var1) throws InvalidAttributeValueException, DistributedManagementException {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 7)) {
         TargetMBean[] var2 = (TargetMBean[])((TargetMBean[])this._getHelper()._extendArray(this.getTargets(), TargetMBean.class, var1));

         try {
            this.setTargets(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            if (var4 instanceof InvalidAttributeValueException) {
               throw (InvalidAttributeValueException)var4;
            }

            if (var4 instanceof DistributedManagementException) {
               throw (DistributedManagementException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

      return true;
   }

   public boolean addWebServer(WebServerMBean var1) throws InvalidAttributeValueException, DistributedManagementException {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 12)) {
         WebServerMBean[] var2 = (WebServerMBean[])((WebServerMBean[])this._getHelper()._extendArray(this.getWebServers(), WebServerMBean.class, var1));

         try {
            this.setWebServers(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            if (var4 instanceof InvalidAttributeValueException) {
               throw (InvalidAttributeValueException)var4;
            }

            if (var4 instanceof DistributedManagementException) {
               throw (DistributedManagementException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

      return true;
   }

   public int getSessionInvalidationIntervalSecs() {
      return this._SessionInvalidationIntervalSecs;
   }

   public boolean isSessionInvalidationIntervalSecsSet() {
      return this._isSet(16);
   }

   public boolean removeTarget(TargetMBean var1) throws InvalidAttributeValueException, DistributedManagementException {
      TargetMBean[] var2 = this.getTargets();
      TargetMBean[] var3 = (TargetMBean[])((TargetMBean[])this._getHelper()._removeElement(var2, TargetMBean.class, var1));
      if (var3.length != var2.length) {
         try {
            this.setTargets(var3);
            return true;
         } catch (Exception var5) {
            if (var5 instanceof RuntimeException) {
               throw (RuntimeException)var5;
            } else if (var5 instanceof InvalidAttributeValueException) {
               throw (InvalidAttributeValueException)var5;
            } else if (var5 instanceof DistributedManagementException) {
               throw (DistributedManagementException)var5;
            } else {
               throw new UndeclaredThrowableException(var5);
            }
         }
      } else {
         return false;
      }
   }

   public boolean removeWebServer(WebServerMBean var1) throws DistributedManagementException {
      WebServerMBean[] var2 = this.getWebServers();
      WebServerMBean[] var3 = (WebServerMBean[])((WebServerMBean[])this._getHelper()._removeElement(var2, WebServerMBean.class, var1));
      if (var3.length != var2.length) {
         try {
            this.setWebServers(var3);
            return true;
         } catch (Exception var5) {
            if (var5 instanceof RuntimeException) {
               throw (RuntimeException)var5;
            } else if (var5 instanceof DistributedManagementException) {
               throw (DistributedManagementException)var5;
            } else {
               throw new UndeclaredThrowableException(var5);
            }
         }
      } else {
         return false;
      }
   }

   public void setSessionInvalidationIntervalSecs(int var1) throws InvalidAttributeValueException {
      this._SessionInvalidationIntervalSecs = var1;
   }

   public TargetMBean[] getActivatedTargets() {
      return this._customizer.getActivatedTargets();
   }

   public int getSessionJDBCConnectionTimeoutSecs() {
      return this._SessionJDBCConnectionTimeoutSecs;
   }

   public VirtualHostMBean[] getVirtualHosts() {
      return this._VirtualHosts;
   }

   public String getVirtualHostsAsString() {
      return this._getHelper()._serializeKeyList(this.getVirtualHosts());
   }

   public boolean isActivatedTargetsSet() {
      return this._isSet(11);
   }

   public boolean isSessionJDBCConnectionTimeoutSecsSet() {
      return this._isSet(17);
   }

   public boolean isVirtualHostsSet() {
      return this._isSet(13);
   }

   public void setVirtualHostsAsString(String var1) {
      if (var1 != null && var1.length() != 0) {
         String[] var13 = this._getHelper()._splitKeyList(var1);
         List var3 = this._getHelper()._getKeyList(this._VirtualHosts);

         String var5;
         for(int var4 = 0; var4 < var13.length; ++var4) {
            var5 = var13[var4];
            var5 = var5 == null ? null : var5.trim();
            if (var3.contains(var5)) {
               var3.remove(var5);
            } else {
               this._getReferenceManager().registerUnresolvedReference(var5, VirtualHostMBean.class, new ReferenceManager.Resolver(this, 13) {
                  public void resolveReference(Object var1) {
                     try {
                        WebAppComponentMBeanImpl.this.addVirtualHost((VirtualHostMBean)var1);
                     } catch (RuntimeException var3) {
                        throw var3;
                     } catch (Exception var4) {
                        throw new AssertionError("Impossible exception: " + var4);
                     }
                  }
               });
            }
         }

         Iterator var14 = var3.iterator();

         while(true) {
            while(var14.hasNext()) {
               var5 = (String)var14.next();
               VirtualHostMBean[] var6 = this._VirtualHosts;
               int var7 = var6.length;

               for(int var8 = 0; var8 < var7; ++var8) {
                  VirtualHostMBean var9 = var6[var8];
                  if (var5.equals(var9.getName())) {
                     try {
                        this.removeVirtualHost(var9);
                        break;
                     } catch (RuntimeException var11) {
                        throw var11;
                     } catch (Exception var12) {
                        throw new AssertionError("Impossible exception: " + var12);
                     }
                  }
               }
            }

            return;
         }
      } else {
         VirtualHostMBean[] var2 = this._VirtualHosts;
         this._initializeProperty(13);
         this._postSet(13, var2, this._VirtualHosts);
      }
   }

   public void addActivatedTarget(TargetMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 11)) {
         TargetMBean[] var2 = (TargetMBean[])((TargetMBean[])this._getHelper()._extendArray(this.getActivatedTargets(), TargetMBean.class, var1));

         try {
            this.setActivatedTargets(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public void setSessionJDBCConnectionTimeoutSecs(int var1) throws InvalidAttributeValueException {
      this._SessionJDBCConnectionTimeoutSecs = var1;
   }

   public void setVirtualHosts(VirtualHostMBean[] var1) throws InvalidAttributeValueException, DistributedManagementException {
      Object var4 = var1 == null ? new VirtualHostMBeanImpl[0] : var1;
      var1 = (VirtualHostMBean[])((VirtualHostMBean[])this._getHelper()._cleanAndValidateArray(var4, VirtualHostMBean.class));

      for(int var2 = 0; var2 < var1.length; ++var2) {
         if (var1[var2] != null) {
            ResolvedReference var3 = new ResolvedReference(this, 13, (AbstractDescriptorBean)var1[var2]) {
               protected Object getPropertyValue() {
                  return WebAppComponentMBeanImpl.this.getVirtualHosts();
               }
            };
            this._getReferenceManager().registerResolvedReference((AbstractDescriptorBean)var1[var2], var3);
         }
      }

      VirtualHostMBean[] var5 = this._VirtualHosts;
      this._VirtualHosts = var1;
      this._postSet(13, var5, var1);
   }

   public boolean addVirtualHost(VirtualHostMBean var1) throws InvalidAttributeValueException, DistributedManagementException {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 13)) {
         VirtualHostMBean[] var2;
         if (this._isSet(13)) {
            var2 = (VirtualHostMBean[])((VirtualHostMBean[])this._getHelper()._extendArray(this.getVirtualHosts(), VirtualHostMBean.class, var1));
         } else {
            var2 = new VirtualHostMBean[]{var1};
         }

         try {
            this.setVirtualHosts(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            if (var4 instanceof InvalidAttributeValueException) {
               throw (InvalidAttributeValueException)var4;
            }

            if (var4 instanceof DistributedManagementException) {
               throw (DistributedManagementException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

      return true;
   }

   public int getSessionTimeoutSecs() {
      return this._SessionTimeoutSecs;
   }

   public boolean isSessionTimeoutSecsSet() {
      return this._isSet(18);
   }

   public void removeActivatedTarget(TargetMBean var1) {
      TargetMBean[] var2 = this.getActivatedTargets();
      TargetMBean[] var3 = (TargetMBean[])((TargetMBean[])this._getHelper()._removeElement(var2, TargetMBean.class, var1));
      if (var3.length != var2.length) {
         try {
            this.setActivatedTargets(var3);
         } catch (Exception var5) {
            if (var5 instanceof RuntimeException) {
               throw (RuntimeException)var5;
            }

            throw new UndeclaredThrowableException(var5);
         }
      }

   }

   public boolean removeVirtualHost(VirtualHostMBean var1) throws DistributedManagementException {
      VirtualHostMBean[] var2 = this.getVirtualHosts();
      VirtualHostMBean[] var3 = (VirtualHostMBean[])((VirtualHostMBean[])this._getHelper()._removeElement(var2, VirtualHostMBean.class, var1));
      if (var3.length != var2.length) {
         try {
            this.setVirtualHosts(var3);
            return true;
         } catch (Exception var5) {
            if (var5 instanceof RuntimeException) {
               throw (RuntimeException)var5;
            } else if (var5 instanceof DistributedManagementException) {
               throw (DistributedManagementException)var5;
            } else {
               throw new UndeclaredThrowableException(var5);
            }
         }
      } else {
         return false;
      }
   }

   public void setActivatedTargets(TargetMBean[] var1) {
      Object var2 = var1 == null ? new TargetMBeanImpl[0] : var1;
      this._ActivatedTargets = (TargetMBean[])var2;
   }

   public void setSessionTimeoutSecs(int var1) throws InvalidAttributeValueException {
      this._SessionTimeoutSecs = var1;
   }

   public boolean activated(TargetMBean var1) {
      return this._customizer.activated(var1);
   }

   public void addDeployedVirtualHost(VirtualHostMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 14)) {
         VirtualHostMBean[] var2 = (VirtualHostMBean[])((VirtualHostMBean[])this._getHelper()._extendArray(this.getDeployedVirtualHosts(), VirtualHostMBean.class, var1));

         try {
            this.setDeployedVirtualHosts(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public VirtualHostMBean[] getDeployedVirtualHosts() {
      return this._DeployedVirtualHosts;
   }

   public String getMimeTypeDefault() {
      return this._MimeTypeDefault;
   }

   public boolean isDeployedVirtualHostsSet() {
      return this._isSet(14);
   }

   public boolean isMimeTypeDefaultSet() {
      return this._isSet(19);
   }

   public void removeDeployedVirtualHost(VirtualHostMBean var1) {
      VirtualHostMBean[] var2 = this.getDeployedVirtualHosts();
      VirtualHostMBean[] var3 = (VirtualHostMBean[])((VirtualHostMBean[])this._getHelper()._removeElement(var2, VirtualHostMBean.class, var1));
      if (var3.length != var2.length) {
         try {
            this.setDeployedVirtualHosts(var3);
         } catch (Exception var5) {
            if (var5 instanceof RuntimeException) {
               throw (RuntimeException)var5;
            }

            throw new UndeclaredThrowableException(var5);
         }
      }

   }

   public void refreshDDsIfNeeded(String[] var1) {
      this._customizer.refreshDDsIfNeeded(var1);
   }

   public void setDeployedVirtualHosts(VirtualHostMBean[] var1) {
      Object var2 = var1 == null ? new VirtualHostMBeanImpl[0] : var1;
      this._DeployedVirtualHosts = (VirtualHostMBean[])var2;
   }

   public void setMimeTypeDefault(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      this._MimeTypeDefault = var1;
   }

   public Map getMimeTypes() {
      return this._MimeTypes;
   }

   public boolean isMimeTypesSet() {
      return this._isSet(20);
   }

   public void touch() throws ConfigurationException {
      this._customizer.touch();
   }

   public void freezeCurrentValue(String var1) throws AttributeNotFoundException, MBeanException {
      this._customizer.freezeCurrentValue(var1);
   }

   public void setMimeTypes(Map var1) throws InvalidAttributeValueException {
      this._MimeTypes = var1;
   }

   public String getDocumentRoot() {
      return this._DocumentRoot;
   }

   public boolean isDocumentRootSet() {
      return this._isSet(21);
   }

   public void restoreDefaultValue(String var1) throws AttributeNotFoundException {
      this._customizer.restoreDefaultValue(var1);
   }

   public void setDocumentRoot(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      this._DocumentRoot = var1;
   }

   public String getDefaultServlet() {
      return this._DefaultServlet;
   }

   public boolean isDefaultServletSet() {
      return this._isSet(22);
   }

   public void setDefaultServlet(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      this._DefaultServlet = var1;
   }

   public boolean isIndexDirectoryEnabled() {
      return this._IndexDirectoryEnabled;
   }

   public boolean isIndexDirectoryEnabledSet() {
      return this._isSet(23);
   }

   public void setIndexDirectoryEnabled(boolean var1) {
      boolean var2 = this._IndexDirectoryEnabled;
      this._IndexDirectoryEnabled = var1;
      this._postSet(23, var2, var1);
   }

   public String[] getIndexFiles() {
      return this._IndexFiles;
   }

   public boolean isIndexFilesSet() {
      return this._isSet(24);
   }

   public void setIndexFiles(String[] var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? new String[0] : var1;
      var1 = this._getHelper()._trimElements(var1);
      this._IndexFiles = var1;
   }

   public boolean addIndexFile(String var1) {
      this._getHelper()._ensureNonNull(var1);
      String[] var2 = (String[])((String[])this._getHelper()._extendArray(this.getIndexFiles(), String.class, var1));

      try {
         this.setIndexFiles(var2);
         return true;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public boolean removeIndexFile(String var1) {
      String[] var2 = this.getIndexFiles();
      String[] var3 = (String[])((String[])this._getHelper()._removeElement(var2, String.class, var1));
      if (var3.length != var2.length) {
         try {
            this.setIndexFiles(var3);
            return true;
         } catch (Exception var5) {
            if (var5 instanceof RuntimeException) {
               throw (RuntimeException)var5;
            } else {
               throw new UndeclaredThrowableException(var5);
            }
         }
      } else {
         return false;
      }
   }

   public String getServletClasspath() {
      return this._ServletClasspath;
   }

   public boolean isServletClasspathSet() {
      return this._isSet(25);
   }

   public void setServletClasspath(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      this._ServletClasspath = var1;
   }

   public boolean isServletExtensionCaseSensitive() {
      return this._ServletExtensionCaseSensitive;
   }

   public boolean isServletExtensionCaseSensitiveSet() {
      return this._isSet(26);
   }

   public void setServletExtensionCaseSensitive(boolean var1) {
      this._ServletExtensionCaseSensitive = var1;
   }

   public int getServletReloadCheckSecs() {
      return this._ServletReloadCheckSecs;
   }

   public boolean isServletReloadCheckSecsSet() {
      return this._isSet(27);
   }

   public void setServletReloadCheckSecs(int var1) throws InvalidAttributeValueException {
      int var2 = this._ServletReloadCheckSecs;
      this._ServletReloadCheckSecs = var1;
      this._postSet(27, var2, var1);
   }

   public int getSingleThreadedServletPoolSize() {
      return this._SingleThreadedServletPoolSize;
   }

   public boolean isSingleThreadedServletPoolSizeSet() {
      return this._isSet(28);
   }

   public void setSingleThreadedServletPoolSize(int var1) throws InvalidAttributeValueException {
      int var2 = this._SingleThreadedServletPoolSize;
      this._SingleThreadedServletPoolSize = var1;
      this._postSet(28, var2, var1);
   }

   public String[] getServlets() {
      return this._Servlets;
   }

   public boolean isServletsSet() {
      return this._isSet(29);
   }

   public void setServlets(String[] var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? new String[0] : var1;
      var1 = this._getHelper()._trimElements(var1);
      this._Servlets = var1;
   }

   public String getAuthRealmName() {
      return this._AuthRealmName;
   }

   public boolean isAuthRealmNameSet() {
      return this._isSet(30);
   }

   public void setAuthRealmName(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._AuthRealmName;
      this._AuthRealmName = var1;
      this._postSet(30, var2, var1);
   }

   public String getAuthFilter() {
      return this._AuthFilter;
   }

   public boolean isAuthFilterSet() {
      return this._isSet(31);
   }

   public void setAuthFilter(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._AuthFilter;
      this._AuthFilter = var1;
      this._postSet(31, var2, var1);
   }

   public boolean isDebugEnabled() {
      return this._DebugEnabled;
   }

   public boolean isDebugEnabledSet() {
      return this._isSet(32);
   }

   public void setDebugEnabled(boolean var1) {
      this._DebugEnabled = var1;
   }

   public boolean isSessionURLRewritingEnabled() {
      return this._SessionURLRewritingEnabled;
   }

   public boolean isSessionURLRewritingEnabledSet() {
      return this._isSet(33);
   }

   public void setSessionURLRewritingEnabled(boolean var1) {
      boolean var2 = this._SessionURLRewritingEnabled;
      this._SessionURLRewritingEnabled = var1;
      this._postSet(33, var2, var1);
   }

   public int getSessionIDLength() {
      return this._SessionIDLength;
   }

   public boolean isSessionIDLengthSet() {
      return this._isSet(34);
   }

   public void setSessionIDLength(int var1) throws InvalidAttributeValueException {
      this._SessionIDLength = var1;
   }

   public int getSessionCacheSize() {
      return this._SessionCacheSize;
   }

   public boolean isSessionCacheSizeSet() {
      return this._isSet(35);
   }

   public void setSessionCacheSize(int var1) throws InvalidAttributeValueException {
      this._SessionCacheSize = var1;
   }

   public boolean isSessionCookiesEnabled() {
      return this._SessionCookiesEnabled;
   }

   public boolean isSessionCookiesEnabledSet() {
      return this._isSet(36);
   }

   public void setSessionCookiesEnabled(boolean var1) {
      this._SessionCookiesEnabled = var1;
   }

   public boolean isSessionTrackingEnabled() {
      return this._SessionTrackingEnabled;
   }

   public boolean isSessionTrackingEnabledSet() {
      return this._isSet(37);
   }

   public void setSessionTrackingEnabled(boolean var1) {
      this._SessionTrackingEnabled = var1;
   }

   public String getSessionCookieComment() {
      return this._SessionCookieComment;
   }

   public boolean isSessionCookieCommentSet() {
      return this._isSet(38);
   }

   public void setSessionCookieComment(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      this._SessionCookieComment = var1;
   }

   public String getSessionCookieDomain() {
      return this._SessionCookieDomain;
   }

   public boolean isSessionCookieDomainSet() {
      return this._isSet(39);
   }

   public void setSessionCookieDomain(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      this._SessionCookieDomain = var1;
   }

   public String getSessionCookieName() {
      return this._SessionCookieName;
   }

   public boolean isSessionCookieNameSet() {
      return this._isSet(40);
   }

   public void setSessionCookieName(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      this._SessionCookieName = var1;
   }

   public String getSessionCookiePath() {
      return this._SessionCookiePath;
   }

   public boolean isSessionCookiePathSet() {
      return this._isSet(41);
   }

   public void setSessionCookiePath(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      this._SessionCookiePath = var1;
   }

   public String getSessionPersistentStoreDir() {
      return this._SessionPersistentStoreDir;
   }

   public boolean isSessionPersistentStoreDirSet() {
      return this._isSet(42);
   }

   public void setSessionPersistentStoreDir(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      this._SessionPersistentStoreDir = var1;
   }

   public String getSessionPersistentStorePool() {
      return this._SessionPersistentStorePool;
   }

   public boolean isSessionPersistentStorePoolSet() {
      return this._isSet(43);
   }

   public void setSessionPersistentStorePool(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      this._SessionPersistentStorePool = var1;
   }

   public String getSessionPersistentStoreTable() {
      return this._SessionPersistentStoreTable;
   }

   public boolean isSessionPersistentStoreTableSet() {
      return this._isSet(44);
   }

   public void setSessionPersistentStoreTable(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      this._SessionPersistentStoreTable = var1;
   }

   public boolean isSessionPersistentStoreShared() {
      return this._SessionPersistentStoreShared;
   }

   public boolean isSessionPersistentStoreSharedSet() {
      return this._isSet(45);
   }

   public void setSessionPersistentStoreShared(boolean var1) {
      this._SessionPersistentStoreShared = var1;
   }

   public String getSessionPersistentStoreType() {
      return this._SessionPersistentStoreType;
   }

   public boolean isSessionPersistentStoreTypeSet() {
      return this._isSet(46);
   }

   public void setSessionPersistentStoreType(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"memory", "file", "jdbc", "replicated", "cookie", "replicated_if_clustered"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("SessionPersistentStoreType", var1, var2);
      this._SessionPersistentStoreType = var1;
   }

   public String getSessionPersistentStoreCookieName() {
      return this._SessionPersistentStoreCookieName;
   }

   public boolean isSessionPersistentStoreCookieNameSet() {
      return this._isSet(47);
   }

   public void setSessionPersistentStoreCookieName(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      this._SessionPersistentStoreCookieName = var1;
   }

   public int getSessionSwapIntervalSecs() {
      return this._SessionSwapIntervalSecs;
   }

   public boolean isSessionSwapIntervalSecsSet() {
      return this._isSet(48);
   }

   public void setSessionSwapIntervalSecs(int var1) throws InvalidAttributeValueException {
      this._SessionSwapIntervalSecs = var1;
   }

   public void setSessionDebuggable(boolean var1) {
      this._SessionDebuggable = var1;
   }

   public boolean isSessionDebuggable() {
      return this._SessionDebuggable;
   }

   public boolean isSessionDebuggableSet() {
      return this._isSet(49);
   }

   public void setCleanupSessionFilesEnabled(boolean var1) {
      this._CleanupSessionFilesEnabled = var1;
   }

   public boolean isCleanupSessionFilesEnabled() {
      return this._CleanupSessionFilesEnabled;
   }

   public boolean isCleanupSessionFilesEnabledSet() {
      return this._isSet(50);
   }

   public String getContextPath() {
      return this._ContextPath;
   }

   public boolean isContextPathSet() {
      return this._isSet(51);
   }

   public void setContextPath(String var1) throws InvalidAttributeValueException, DistributedManagementException {
      var1 = var1 == null ? null : var1.trim();
      this._ContextPath = var1;
   }

   public String getSessionMainAttribute() {
      return this._SessionMainAttribute;
   }

   public boolean isSessionMainAttributeSet() {
      return this._isSet(52);
   }

   public void setSessionMainAttribute(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      this._SessionMainAttribute = var1;
   }

   public boolean isSessionMonitoringEnabled() {
      return this._SessionMonitoringEnabled;
   }

   public boolean isSessionMonitoringEnabledSet() {
      return this._isSet(53);
   }

   public void setSessionMonitoringEnabled(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this._SessionMonitoringEnabled;
      this._SessionMonitoringEnabled = var1;
      this._postSet(53, var2, var1);
   }

   public boolean isPreferWebInfClasses() {
      return this._PreferWebInfClasses;
   }

   public boolean isPreferWebInfClassesSet() {
      return this._isSet(54);
   }

   public void setPreferWebInfClasses(boolean var1) {
      boolean var2 = this._PreferWebInfClasses;
      this._PreferWebInfClasses = var1;
      this._postSet(54, var2, var1);
   }

   public Object _getKey() {
      return this.getName();
   }

   public void _validate() throws IllegalArgumentException {
      super._validate();
   }

   public boolean _hasKey() {
      return true;
   }

   public boolean _isPropertyAKey(Munger.ReaderEventInfo var1) {
      String var2 = var1.getElementName();
      switch (var2.length()) {
         case 4:
            if (var2.equals("name")) {
               return var1.compareXpaths(this._getPropertyXpath("name"));
            }

            return super._isPropertyAKey(var1);
         default:
            return super._isPropertyAKey(var1);
      }
   }

   protected void _unSet(int var1) {
      if (!this._initializeProperty(var1)) {
         super._unSet(var1);
      } else {
         this._markSet(var1, false);
      }

   }

   protected AbstractDescriptorBeanHelper _createHelper() {
      return new Helper(this);
   }

   public boolean _isAnythingSet() {
      return super._isAnythingSet();
   }

   private boolean _initializeProperty(int var1) {
      boolean var2 = var1 > -1;
      if (!var2) {
         var1 = 11;
      }

      try {
         switch (var1) {
            case 11:
               this._ActivatedTargets = new TargetMBean[0];
               if (var2) {
                  break;
               }
            case 9:
               this._customizer.setApplication((ApplicationMBean)null);
               if (var2) {
                  break;
               }
            case 31:
               this._AuthFilter = null;
               if (var2) {
                  break;
               }
            case 30:
               this._AuthRealmName = "weblogic";
               if (var2) {
                  break;
               }
            case 51:
               this._ContextPath = null;
               if (var2) {
                  break;
               }
            case 22:
               this._DefaultServlet = null;
               if (var2) {
                  break;
               }
            case 14:
               this._DeployedVirtualHosts = new VirtualHostMBean[0];
               if (var2) {
                  break;
               }
            case 21:
               this._DocumentRoot = null;
               if (var2) {
                  break;
               }
            case 24:
               this._IndexFiles = StringHelper.split(INDEX_FILES);
               if (var2) {
                  break;
               }
            case 19:
               this._MimeTypeDefault = "text/plain";
               if (var2) {
                  break;
               }
            case 20:
               this._MimeTypes = null;
               if (var2) {
                  break;
               }
            case 2:
               this._customizer.setName((String)null);
               if (var2) {
                  break;
               }
            case 25:
               this._ServletClasspath = null;
               if (var2) {
                  break;
               }
            case 27:
               this._ServletReloadCheckSecs = 1;
               if (var2) {
                  break;
               }
            case 29:
               this._Servlets = new String[0];
               if (var2) {
                  break;
               }
            case 35:
               this._SessionCacheSize = 1024;
               if (var2) {
                  break;
               }
            case 38:
               this._SessionCookieComment = "Weblogic Server Session Tracking Cookie";
               if (var2) {
                  break;
               }
            case 39:
               this._SessionCookieDomain = null;
               if (var2) {
                  break;
               }
            case 15:
               this._SessionCookieMaxAgeSecs = -1;
               if (var2) {
                  break;
               }
            case 40:
               this._SessionCookieName = "JSESSIONID";
               if (var2) {
                  break;
               }
            case 41:
               this._SessionCookiePath = null;
               if (var2) {
                  break;
               }
            case 34:
               this._SessionIDLength = 52;
               if (var2) {
                  break;
               }
            case 16:
               this._SessionInvalidationIntervalSecs = 60;
               if (var2) {
                  break;
               }
            case 17:
               this._SessionJDBCConnectionTimeoutSecs = 120;
               if (var2) {
                  break;
               }
            case 52:
               this._SessionMainAttribute = "ConsoleAttribute";
               if (var2) {
                  break;
               }
            case 47:
               this._SessionPersistentStoreCookieName = "WLCOOKIE";
               if (var2) {
                  break;
               }
            case 42:
               this._SessionPersistentStoreDir = "session_db";
               if (var2) {
                  break;
               }
            case 43:
               this._SessionPersistentStorePool = null;
               if (var2) {
                  break;
               }
            case 44:
               this._SessionPersistentStoreTable = "wl_servlet_sessions";
               if (var2) {
                  break;
               }
            case 46:
               this._SessionPersistentStoreType = "memory";
               if (var2) {
                  break;
               }
            case 48:
               this._SessionSwapIntervalSecs = 10;
               if (var2) {
                  break;
               }
            case 18:
               this._SessionTimeoutSecs = 3600;
               if (var2) {
                  break;
               }
            case 28:
               this._SingleThreadedServletPoolSize = 5;
               if (var2) {
                  break;
               }
            case 7:
               this._customizer.setTargets(new TargetMBean[0]);
               if (var2) {
                  break;
               }
            case 13:
               this._VirtualHosts = new VirtualHostMBean[0];
               if (var2) {
                  break;
               }
            case 12:
               this._WebServers = new WebServerMBean[0];
               if (var2) {
                  break;
               }
            case 50:
               this._CleanupSessionFilesEnabled = false;
               if (var2) {
                  break;
               }
            case 32:
               this._DebugEnabled = false;
               if (var2) {
                  break;
               }
            case 23:
               this._IndexDirectoryEnabled = false;
               if (var2) {
                  break;
               }
            case 54:
               this._PreferWebInfClasses = false;
               if (var2) {
                  break;
               }
            case 26:
               this._ServletExtensionCaseSensitive = false;
               if (var2) {
                  break;
               }
            case 36:
               this._SessionCookiesEnabled = true;
               if (var2) {
                  break;
               }
            case 49:
               this._SessionDebuggable = false;
               if (var2) {
                  break;
               }
            case 53:
               this._SessionMonitoringEnabled = false;
               if (var2) {
                  break;
               }
            case 45:
               this._SessionPersistentStoreShared = false;
               if (var2) {
                  break;
               }
            case 37:
               this._SessionTrackingEnabled = true;
               if (var2) {
                  break;
               }
            case 33:
               this._SessionURLRewritingEnabled = true;
               if (var2) {
                  break;
               }
            case 3:
            case 4:
            case 5:
            case 6:
            case 8:
            case 10:
            default:
               if (var2) {
                  return false;
               }
         }

         return true;
      } catch (RuntimeException var4) {
         throw var4;
      } catch (Exception var5) {
         throw (Error)(new AssertionError("Impossible Exception")).initCause(var5);
      }
   }

   public Munger.SchemaHelper _getSchemaHelper() {
      return null;
   }

   public String _getElementName(int var1) {
      return this._getSchemaHelper2().getElementName(var1);
   }

   protected String getSchemaLocation() {
      return "http://xmlns.oracle.com/weblogic/1.0/domain.xsd";
   }

   protected String getTargetNamespace() {
      return "http://xmlns.oracle.com/weblogic/domain";
   }

   public SchemaHelper _getSchemaHelper2() {
      if (_schemaHelper == null) {
         _schemaHelper = new SchemaHelper2();
      }

      return _schemaHelper;
   }

   public String getType() {
      return "WebAppComponent";
   }

   public void putValue(String var1, Object var2) {
      TargetMBean[] var6;
      if (var1.equals("ActivatedTargets")) {
         var6 = this._ActivatedTargets;
         this._ActivatedTargets = (TargetMBean[])((TargetMBean[])var2);
         this._postSet(11, var6, this._ActivatedTargets);
      } else if (var1.equals("Application")) {
         ApplicationMBean var12 = this._Application;
         this._Application = (ApplicationMBean)var2;
         this._postSet(9, var12, this._Application);
      } else {
         String var9;
         if (var1.equals("AuthFilter")) {
            var9 = this._AuthFilter;
            this._AuthFilter = (String)var2;
            this._postSet(31, var9, this._AuthFilter);
         } else if (var1.equals("AuthRealmName")) {
            var9 = this._AuthRealmName;
            this._AuthRealmName = (String)var2;
            this._postSet(30, var9, this._AuthRealmName);
         } else {
            boolean var8;
            if (var1.equals("CleanupSessionFilesEnabled")) {
               var8 = this._CleanupSessionFilesEnabled;
               this._CleanupSessionFilesEnabled = (Boolean)var2;
               this._postSet(50, var8, this._CleanupSessionFilesEnabled);
            } else if (var1.equals("ContextPath")) {
               var9 = this._ContextPath;
               this._ContextPath = (String)var2;
               this._postSet(51, var9, this._ContextPath);
            } else if (var1.equals("DebugEnabled")) {
               var8 = this._DebugEnabled;
               this._DebugEnabled = (Boolean)var2;
               this._postSet(32, var8, this._DebugEnabled);
            } else if (var1.equals("DefaultServlet")) {
               var9 = this._DefaultServlet;
               this._DefaultServlet = (String)var2;
               this._postSet(22, var9, this._DefaultServlet);
            } else {
               VirtualHostMBean[] var5;
               if (var1.equals("DeployedVirtualHosts")) {
                  var5 = this._DeployedVirtualHosts;
                  this._DeployedVirtualHosts = (VirtualHostMBean[])((VirtualHostMBean[])var2);
                  this._postSet(14, var5, this._DeployedVirtualHosts);
               } else if (var1.equals("DocumentRoot")) {
                  var9 = this._DocumentRoot;
                  this._DocumentRoot = (String)var2;
                  this._postSet(21, var9, this._DocumentRoot);
               } else if (var1.equals("IndexDirectoryEnabled")) {
                  var8 = this._IndexDirectoryEnabled;
                  this._IndexDirectoryEnabled = (Boolean)var2;
                  this._postSet(23, var8, this._IndexDirectoryEnabled);
               } else {
                  String[] var10;
                  if (var1.equals("IndexFiles")) {
                     var10 = this._IndexFiles;
                     this._IndexFiles = (String[])((String[])var2);
                     this._postSet(24, var10, this._IndexFiles);
                  } else if (var1.equals("MimeTypeDefault")) {
                     var9 = this._MimeTypeDefault;
                     this._MimeTypeDefault = (String)var2;
                     this._postSet(19, var9, this._MimeTypeDefault);
                  } else if (var1.equals("MimeTypes")) {
                     Map var11 = this._MimeTypes;
                     this._MimeTypes = (Map)var2;
                     this._postSet(20, var11, this._MimeTypes);
                  } else if (var1.equals("Name")) {
                     var9 = this._Name;
                     this._Name = (String)var2;
                     this._postSet(2, var9, this._Name);
                  } else if (var1.equals("PreferWebInfClasses")) {
                     var8 = this._PreferWebInfClasses;
                     this._PreferWebInfClasses = (Boolean)var2;
                     this._postSet(54, var8, this._PreferWebInfClasses);
                  } else if (var1.equals("ServletClasspath")) {
                     var9 = this._ServletClasspath;
                     this._ServletClasspath = (String)var2;
                     this._postSet(25, var9, this._ServletClasspath);
                  } else if (var1.equals("ServletExtensionCaseSensitive")) {
                     var8 = this._ServletExtensionCaseSensitive;
                     this._ServletExtensionCaseSensitive = (Boolean)var2;
                     this._postSet(26, var8, this._ServletExtensionCaseSensitive);
                  } else {
                     int var7;
                     if (var1.equals("ServletReloadCheckSecs")) {
                        var7 = this._ServletReloadCheckSecs;
                        this._ServletReloadCheckSecs = (Integer)var2;
                        this._postSet(27, var7, this._ServletReloadCheckSecs);
                     } else if (var1.equals("Servlets")) {
                        var10 = this._Servlets;
                        this._Servlets = (String[])((String[])var2);
                        this._postSet(29, var10, this._Servlets);
                     } else if (var1.equals("SessionCacheSize")) {
                        var7 = this._SessionCacheSize;
                        this._SessionCacheSize = (Integer)var2;
                        this._postSet(35, var7, this._SessionCacheSize);
                     } else if (var1.equals("SessionCookieComment")) {
                        var9 = this._SessionCookieComment;
                        this._SessionCookieComment = (String)var2;
                        this._postSet(38, var9, this._SessionCookieComment);
                     } else if (var1.equals("SessionCookieDomain")) {
                        var9 = this._SessionCookieDomain;
                        this._SessionCookieDomain = (String)var2;
                        this._postSet(39, var9, this._SessionCookieDomain);
                     } else if (var1.equals("SessionCookieMaxAgeSecs")) {
                        var7 = this._SessionCookieMaxAgeSecs;
                        this._SessionCookieMaxAgeSecs = (Integer)var2;
                        this._postSet(15, var7, this._SessionCookieMaxAgeSecs);
                     } else if (var1.equals("SessionCookieName")) {
                        var9 = this._SessionCookieName;
                        this._SessionCookieName = (String)var2;
                        this._postSet(40, var9, this._SessionCookieName);
                     } else if (var1.equals("SessionCookiePath")) {
                        var9 = this._SessionCookiePath;
                        this._SessionCookiePath = (String)var2;
                        this._postSet(41, var9, this._SessionCookiePath);
                     } else if (var1.equals("SessionCookiesEnabled")) {
                        var8 = this._SessionCookiesEnabled;
                        this._SessionCookiesEnabled = (Boolean)var2;
                        this._postSet(36, var8, this._SessionCookiesEnabled);
                     } else if (var1.equals("SessionDebuggable")) {
                        var8 = this._SessionDebuggable;
                        this._SessionDebuggable = (Boolean)var2;
                        this._postSet(49, var8, this._SessionDebuggable);
                     } else if (var1.equals("SessionIDLength")) {
                        var7 = this._SessionIDLength;
                        this._SessionIDLength = (Integer)var2;
                        this._postSet(34, var7, this._SessionIDLength);
                     } else if (var1.equals("SessionInvalidationIntervalSecs")) {
                        var7 = this._SessionInvalidationIntervalSecs;
                        this._SessionInvalidationIntervalSecs = (Integer)var2;
                        this._postSet(16, var7, this._SessionInvalidationIntervalSecs);
                     } else if (var1.equals("SessionJDBCConnectionTimeoutSecs")) {
                        var7 = this._SessionJDBCConnectionTimeoutSecs;
                        this._SessionJDBCConnectionTimeoutSecs = (Integer)var2;
                        this._postSet(17, var7, this._SessionJDBCConnectionTimeoutSecs);
                     } else if (var1.equals("SessionMainAttribute")) {
                        var9 = this._SessionMainAttribute;
                        this._SessionMainAttribute = (String)var2;
                        this._postSet(52, var9, this._SessionMainAttribute);
                     } else if (var1.equals("SessionMonitoringEnabled")) {
                        var8 = this._SessionMonitoringEnabled;
                        this._SessionMonitoringEnabled = (Boolean)var2;
                        this._postSet(53, var8, this._SessionMonitoringEnabled);
                     } else if (var1.equals("SessionPersistentStoreCookieName")) {
                        var9 = this._SessionPersistentStoreCookieName;
                        this._SessionPersistentStoreCookieName = (String)var2;
                        this._postSet(47, var9, this._SessionPersistentStoreCookieName);
                     } else if (var1.equals("SessionPersistentStoreDir")) {
                        var9 = this._SessionPersistentStoreDir;
                        this._SessionPersistentStoreDir = (String)var2;
                        this._postSet(42, var9, this._SessionPersistentStoreDir);
                     } else if (var1.equals("SessionPersistentStorePool")) {
                        var9 = this._SessionPersistentStorePool;
                        this._SessionPersistentStorePool = (String)var2;
                        this._postSet(43, var9, this._SessionPersistentStorePool);
                     } else if (var1.equals("SessionPersistentStoreShared")) {
                        var8 = this._SessionPersistentStoreShared;
                        this._SessionPersistentStoreShared = (Boolean)var2;
                        this._postSet(45, var8, this._SessionPersistentStoreShared);
                     } else if (var1.equals("SessionPersistentStoreTable")) {
                        var9 = this._SessionPersistentStoreTable;
                        this._SessionPersistentStoreTable = (String)var2;
                        this._postSet(44, var9, this._SessionPersistentStoreTable);
                     } else if (var1.equals("SessionPersistentStoreType")) {
                        var9 = this._SessionPersistentStoreType;
                        this._SessionPersistentStoreType = (String)var2;
                        this._postSet(46, var9, this._SessionPersistentStoreType);
                     } else if (var1.equals("SessionSwapIntervalSecs")) {
                        var7 = this._SessionSwapIntervalSecs;
                        this._SessionSwapIntervalSecs = (Integer)var2;
                        this._postSet(48, var7, this._SessionSwapIntervalSecs);
                     } else if (var1.equals("SessionTimeoutSecs")) {
                        var7 = this._SessionTimeoutSecs;
                        this._SessionTimeoutSecs = (Integer)var2;
                        this._postSet(18, var7, this._SessionTimeoutSecs);
                     } else if (var1.equals("SessionTrackingEnabled")) {
                        var8 = this._SessionTrackingEnabled;
                        this._SessionTrackingEnabled = (Boolean)var2;
                        this._postSet(37, var8, this._SessionTrackingEnabled);
                     } else if (var1.equals("SessionURLRewritingEnabled")) {
                        var8 = this._SessionURLRewritingEnabled;
                        this._SessionURLRewritingEnabled = (Boolean)var2;
                        this._postSet(33, var8, this._SessionURLRewritingEnabled);
                     } else if (var1.equals("SingleThreadedServletPoolSize")) {
                        var7 = this._SingleThreadedServletPoolSize;
                        this._SingleThreadedServletPoolSize = (Integer)var2;
                        this._postSet(28, var7, this._SingleThreadedServletPoolSize);
                     } else if (var1.equals("Targets")) {
                        var6 = this._Targets;
                        this._Targets = (TargetMBean[])((TargetMBean[])var2);
                        this._postSet(7, var6, this._Targets);
                     } else if (var1.equals("VirtualHosts")) {
                        var5 = this._VirtualHosts;
                        this._VirtualHosts = (VirtualHostMBean[])((VirtualHostMBean[])var2);
                        this._postSet(13, var5, this._VirtualHosts);
                     } else if (var1.equals("WebServers")) {
                        WebServerMBean[] var4 = this._WebServers;
                        this._WebServers = (WebServerMBean[])((WebServerMBean[])var2);
                        this._postSet(12, var4, this._WebServers);
                     } else if (var1.equals("customizer")) {
                        WebAppComponent var3 = this._customizer;
                        this._customizer = (WebAppComponent)var2;
                     } else {
                        super.putValue(var1, var2);
                     }
                  }
               }
            }
         }
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("ActivatedTargets")) {
         return this._ActivatedTargets;
      } else if (var1.equals("Application")) {
         return this._Application;
      } else if (var1.equals("AuthFilter")) {
         return this._AuthFilter;
      } else if (var1.equals("AuthRealmName")) {
         return this._AuthRealmName;
      } else if (var1.equals("CleanupSessionFilesEnabled")) {
         return new Boolean(this._CleanupSessionFilesEnabled);
      } else if (var1.equals("ContextPath")) {
         return this._ContextPath;
      } else if (var1.equals("DebugEnabled")) {
         return new Boolean(this._DebugEnabled);
      } else if (var1.equals("DefaultServlet")) {
         return this._DefaultServlet;
      } else if (var1.equals("DeployedVirtualHosts")) {
         return this._DeployedVirtualHosts;
      } else if (var1.equals("DocumentRoot")) {
         return this._DocumentRoot;
      } else if (var1.equals("IndexDirectoryEnabled")) {
         return new Boolean(this._IndexDirectoryEnabled);
      } else if (var1.equals("IndexFiles")) {
         return this._IndexFiles;
      } else if (var1.equals("MimeTypeDefault")) {
         return this._MimeTypeDefault;
      } else if (var1.equals("MimeTypes")) {
         return this._MimeTypes;
      } else if (var1.equals("Name")) {
         return this._Name;
      } else if (var1.equals("PreferWebInfClasses")) {
         return new Boolean(this._PreferWebInfClasses);
      } else if (var1.equals("ServletClasspath")) {
         return this._ServletClasspath;
      } else if (var1.equals("ServletExtensionCaseSensitive")) {
         return new Boolean(this._ServletExtensionCaseSensitive);
      } else if (var1.equals("ServletReloadCheckSecs")) {
         return new Integer(this._ServletReloadCheckSecs);
      } else if (var1.equals("Servlets")) {
         return this._Servlets;
      } else if (var1.equals("SessionCacheSize")) {
         return new Integer(this._SessionCacheSize);
      } else if (var1.equals("SessionCookieComment")) {
         return this._SessionCookieComment;
      } else if (var1.equals("SessionCookieDomain")) {
         return this._SessionCookieDomain;
      } else if (var1.equals("SessionCookieMaxAgeSecs")) {
         return new Integer(this._SessionCookieMaxAgeSecs);
      } else if (var1.equals("SessionCookieName")) {
         return this._SessionCookieName;
      } else if (var1.equals("SessionCookiePath")) {
         return this._SessionCookiePath;
      } else if (var1.equals("SessionCookiesEnabled")) {
         return new Boolean(this._SessionCookiesEnabled);
      } else if (var1.equals("SessionDebuggable")) {
         return new Boolean(this._SessionDebuggable);
      } else if (var1.equals("SessionIDLength")) {
         return new Integer(this._SessionIDLength);
      } else if (var1.equals("SessionInvalidationIntervalSecs")) {
         return new Integer(this._SessionInvalidationIntervalSecs);
      } else if (var1.equals("SessionJDBCConnectionTimeoutSecs")) {
         return new Integer(this._SessionJDBCConnectionTimeoutSecs);
      } else if (var1.equals("SessionMainAttribute")) {
         return this._SessionMainAttribute;
      } else if (var1.equals("SessionMonitoringEnabled")) {
         return new Boolean(this._SessionMonitoringEnabled);
      } else if (var1.equals("SessionPersistentStoreCookieName")) {
         return this._SessionPersistentStoreCookieName;
      } else if (var1.equals("SessionPersistentStoreDir")) {
         return this._SessionPersistentStoreDir;
      } else if (var1.equals("SessionPersistentStorePool")) {
         return this._SessionPersistentStorePool;
      } else if (var1.equals("SessionPersistentStoreShared")) {
         return new Boolean(this._SessionPersistentStoreShared);
      } else if (var1.equals("SessionPersistentStoreTable")) {
         return this._SessionPersistentStoreTable;
      } else if (var1.equals("SessionPersistentStoreType")) {
         return this._SessionPersistentStoreType;
      } else if (var1.equals("SessionSwapIntervalSecs")) {
         return new Integer(this._SessionSwapIntervalSecs);
      } else if (var1.equals("SessionTimeoutSecs")) {
         return new Integer(this._SessionTimeoutSecs);
      } else if (var1.equals("SessionTrackingEnabled")) {
         return new Boolean(this._SessionTrackingEnabled);
      } else if (var1.equals("SessionURLRewritingEnabled")) {
         return new Boolean(this._SessionURLRewritingEnabled);
      } else if (var1.equals("SingleThreadedServletPoolSize")) {
         return new Integer(this._SingleThreadedServletPoolSize);
      } else if (var1.equals("Targets")) {
         return this._Targets;
      } else if (var1.equals("VirtualHosts")) {
         return this._VirtualHosts;
      } else if (var1.equals("WebServers")) {
         return this._WebServers;
      } else {
         return var1.equals("customizer") ? this._customizer : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends ComponentMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 4:
               if (var1.equals("name")) {
                  return 2;
               }
            case 5:
            case 8:
            case 9:
            case 14:
            case 35:
            default:
               break;
            case 6:
               if (var1.equals("target")) {
                  return 7;
               }
               break;
            case 7:
               if (var1.equals("servlet")) {
                  return 29;
               }
               break;
            case 10:
               if (var1.equals("index-file")) {
                  return 24;
               }

               if (var1.equals("mime-types")) {
                  return 20;
               }

               if (var1.equals("web-server")) {
                  return 12;
               }
               break;
            case 11:
               if (var1.equals("application")) {
                  return 9;
               }

               if (var1.equals("auth-filter")) {
                  return 31;
               }
               break;
            case 12:
               if (var1.equals("context-path")) {
                  return 51;
               }

               if (var1.equals("virtual-host")) {
                  return 13;
               }
               break;
            case 13:
               if (var1.equals("document-root")) {
                  return 21;
               }

               if (var1.equals("debug-enabled")) {
                  return 32;
               }
               break;
            case 15:
               if (var1.equals("auth-realm-name")) {
                  return 30;
               }

               if (var1.equals("default-servlet")) {
                  return 22;
               }
               break;
            case 16:
               if (var1.equals("activated-target")) {
                  return 11;
               }

               if (var1.equals("sessionid-length")) {
                  return 34;
               }
               break;
            case 17:
               if (var1.equals("mime-type-default")) {
                  return 19;
               }

               if (var1.equals("servlet-classpath")) {
                  return 25;
               }
               break;
            case 18:
               if (var1.equals("session-cache-size")) {
                  return 35;
               }

               if (var1.equals("session-debuggable")) {
                  return 49;
               }
               break;
            case 19:
               if (var1.equals("session-cookie-name")) {
                  return 40;
               }

               if (var1.equals("session-cookie-path")) {
                  return 41;
               }
               break;
            case 20:
               if (var1.equals("session-timeout-secs")) {
                  return 18;
               }
               break;
            case 21:
               if (var1.equals("deployed-virtual-host")) {
                  return 14;
               }

               if (var1.equals("session-cookie-domain")) {
                  return 39;
               }
               break;
            case 22:
               if (var1.equals("session-cookie-comment")) {
                  return 38;
               }

               if (var1.equals("session-main-attribute")) {
                  return 52;
               }

               if (var1.equals("prefer-web-inf-classes")) {
                  return 54;
               }
               break;
            case 23:
               if (var1.equals("index-directory-enabled")) {
                  return 23;
               }

               if (var1.equals("session-cookies-enabled")) {
                  return 36;
               }
               break;
            case 24:
               if (var1.equals("session-tracking-enabled")) {
                  return 37;
               }
               break;
            case 25:
               if (var1.equals("servlet-reload-check-secs")) {
                  return 27;
               }
               break;
            case 26:
               if (var1.equals("session-swap-interval-secs")) {
                  return 48;
               }

               if (var1.equals("session-monitoring-enabled")) {
                  return 53;
               }
               break;
            case 27:
               if (var1.equals("session-cookie-max-age-secs")) {
                  return 15;
               }
               break;
            case 28:
               if (var1.equals("session-persistent-store-dir")) {
                  return 42;
               }
               break;
            case 29:
               if (var1.equals("session-persistent-store-pool")) {
                  return 43;
               }

               if (var1.equals("session-persistent-store-type")) {
                  return 46;
               }

               if (var1.equals("cleanup-session-files-enabled")) {
                  return 50;
               }

               if (var1.equals("session-url-rewriting-enabled")) {
                  return 33;
               }
               break;
            case 30:
               if (var1.equals("session-persistent-store-table")) {
                  return 44;
               }
               break;
            case 31:
               if (var1.equals("session-persistent-store-shared")) {
                  return 45;
               }
               break;
            case 32:
               if (var1.equals("servlet-extension-case-sensitive")) {
                  return 26;
               }
               break;
            case 33:
               if (var1.equals("single-threaded-servlet-pool-size")) {
                  return 28;
               }
               break;
            case 34:
               if (var1.equals("session-invalidation-interval-secs")) {
                  return 16;
               }
               break;
            case 36:
               if (var1.equals("session-jdbc-connection-timeout-secs")) {
                  return 17;
               }

               if (var1.equals("session-persistent-store-cookie-name")) {
                  return 47;
               }
         }

         return super.getPropertyIndex(var1);
      }

      public SchemaHelper getSchemaHelper(int var1) {
         switch (var1) {
            default:
               return super.getSchemaHelper(var1);
         }
      }

      public String getElementName(int var1) {
         switch (var1) {
            case 2:
               return "name";
            case 3:
            case 4:
            case 5:
            case 6:
            case 8:
            case 10:
            default:
               return super.getElementName(var1);
            case 7:
               return "target";
            case 9:
               return "application";
            case 11:
               return "activated-target";
            case 12:
               return "web-server";
            case 13:
               return "virtual-host";
            case 14:
               return "deployed-virtual-host";
            case 15:
               return "session-cookie-max-age-secs";
            case 16:
               return "session-invalidation-interval-secs";
            case 17:
               return "session-jdbc-connection-timeout-secs";
            case 18:
               return "session-timeout-secs";
            case 19:
               return "mime-type-default";
            case 20:
               return "mime-types";
            case 21:
               return "document-root";
            case 22:
               return "default-servlet";
            case 23:
               return "index-directory-enabled";
            case 24:
               return "index-file";
            case 25:
               return "servlet-classpath";
            case 26:
               return "servlet-extension-case-sensitive";
            case 27:
               return "servlet-reload-check-secs";
            case 28:
               return "single-threaded-servlet-pool-size";
            case 29:
               return "servlet";
            case 30:
               return "auth-realm-name";
            case 31:
               return "auth-filter";
            case 32:
               return "debug-enabled";
            case 33:
               return "session-url-rewriting-enabled";
            case 34:
               return "sessionid-length";
            case 35:
               return "session-cache-size";
            case 36:
               return "session-cookies-enabled";
            case 37:
               return "session-tracking-enabled";
            case 38:
               return "session-cookie-comment";
            case 39:
               return "session-cookie-domain";
            case 40:
               return "session-cookie-name";
            case 41:
               return "session-cookie-path";
            case 42:
               return "session-persistent-store-dir";
            case 43:
               return "session-persistent-store-pool";
            case 44:
               return "session-persistent-store-table";
            case 45:
               return "session-persistent-store-shared";
            case 46:
               return "session-persistent-store-type";
            case 47:
               return "session-persistent-store-cookie-name";
            case 48:
               return "session-swap-interval-secs";
            case 49:
               return "session-debuggable";
            case 50:
               return "cleanup-session-files-enabled";
            case 51:
               return "context-path";
            case 52:
               return "session-main-attribute";
            case 53:
               return "session-monitoring-enabled";
            case 54:
               return "prefer-web-inf-classes";
         }
      }

      public boolean isArray(int var1) {
         switch (var1) {
            case 7:
               return true;
            case 8:
            case 9:
            case 10:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 25:
            case 26:
            case 27:
            case 28:
            default:
               return super.isArray(var1);
            case 11:
               return true;
            case 12:
               return true;
            case 13:
               return true;
            case 14:
               return true;
            case 24:
               return true;
            case 29:
               return true;
         }
      }

      public boolean isBean(int var1) {
         switch (var1) {
            default:
               return super.isBean(var1);
         }
      }

      public boolean isKey(int var1) {
         switch (var1) {
            case 2:
               return true;
            default:
               return super.isKey(var1);
         }
      }

      public boolean hasKey() {
         return true;
      }

      public String[] getKeyElementNames() {
         ArrayList var1 = new ArrayList();
         var1.add("name");
         return (String[])((String[])var1.toArray(new String[0]));
      }
   }

   protected static class Helper extends ComponentMBeanImpl.Helper {
      private WebAppComponentMBeanImpl bean;

      protected Helper(WebAppComponentMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 2:
               return "Name";
            case 3:
            case 4:
            case 5:
            case 6:
            case 8:
            case 10:
            default:
               return super.getPropertyName(var1);
            case 7:
               return "Targets";
            case 9:
               return "Application";
            case 11:
               return "ActivatedTargets";
            case 12:
               return "WebServers";
            case 13:
               return "VirtualHosts";
            case 14:
               return "DeployedVirtualHosts";
            case 15:
               return "SessionCookieMaxAgeSecs";
            case 16:
               return "SessionInvalidationIntervalSecs";
            case 17:
               return "SessionJDBCConnectionTimeoutSecs";
            case 18:
               return "SessionTimeoutSecs";
            case 19:
               return "MimeTypeDefault";
            case 20:
               return "MimeTypes";
            case 21:
               return "DocumentRoot";
            case 22:
               return "DefaultServlet";
            case 23:
               return "IndexDirectoryEnabled";
            case 24:
               return "IndexFiles";
            case 25:
               return "ServletClasspath";
            case 26:
               return "ServletExtensionCaseSensitive";
            case 27:
               return "ServletReloadCheckSecs";
            case 28:
               return "SingleThreadedServletPoolSize";
            case 29:
               return "Servlets";
            case 30:
               return "AuthRealmName";
            case 31:
               return "AuthFilter";
            case 32:
               return "DebugEnabled";
            case 33:
               return "SessionURLRewritingEnabled";
            case 34:
               return "SessionIDLength";
            case 35:
               return "SessionCacheSize";
            case 36:
               return "SessionCookiesEnabled";
            case 37:
               return "SessionTrackingEnabled";
            case 38:
               return "SessionCookieComment";
            case 39:
               return "SessionCookieDomain";
            case 40:
               return "SessionCookieName";
            case 41:
               return "SessionCookiePath";
            case 42:
               return "SessionPersistentStoreDir";
            case 43:
               return "SessionPersistentStorePool";
            case 44:
               return "SessionPersistentStoreTable";
            case 45:
               return "SessionPersistentStoreShared";
            case 46:
               return "SessionPersistentStoreType";
            case 47:
               return "SessionPersistentStoreCookieName";
            case 48:
               return "SessionSwapIntervalSecs";
            case 49:
               return "SessionDebuggable";
            case 50:
               return "CleanupSessionFilesEnabled";
            case 51:
               return "ContextPath";
            case 52:
               return "SessionMainAttribute";
            case 53:
               return "SessionMonitoringEnabled";
            case 54:
               return "PreferWebInfClasses";
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("ActivatedTargets")) {
            return 11;
         } else if (var1.equals("Application")) {
            return 9;
         } else if (var1.equals("AuthFilter")) {
            return 31;
         } else if (var1.equals("AuthRealmName")) {
            return 30;
         } else if (var1.equals("ContextPath")) {
            return 51;
         } else if (var1.equals("DefaultServlet")) {
            return 22;
         } else if (var1.equals("DeployedVirtualHosts")) {
            return 14;
         } else if (var1.equals("DocumentRoot")) {
            return 21;
         } else if (var1.equals("IndexFiles")) {
            return 24;
         } else if (var1.equals("MimeTypeDefault")) {
            return 19;
         } else if (var1.equals("MimeTypes")) {
            return 20;
         } else if (var1.equals("Name")) {
            return 2;
         } else if (var1.equals("ServletClasspath")) {
            return 25;
         } else if (var1.equals("ServletReloadCheckSecs")) {
            return 27;
         } else if (var1.equals("Servlets")) {
            return 29;
         } else if (var1.equals("SessionCacheSize")) {
            return 35;
         } else if (var1.equals("SessionCookieComment")) {
            return 38;
         } else if (var1.equals("SessionCookieDomain")) {
            return 39;
         } else if (var1.equals("SessionCookieMaxAgeSecs")) {
            return 15;
         } else if (var1.equals("SessionCookieName")) {
            return 40;
         } else if (var1.equals("SessionCookiePath")) {
            return 41;
         } else if (var1.equals("SessionIDLength")) {
            return 34;
         } else if (var1.equals("SessionInvalidationIntervalSecs")) {
            return 16;
         } else if (var1.equals("SessionJDBCConnectionTimeoutSecs")) {
            return 17;
         } else if (var1.equals("SessionMainAttribute")) {
            return 52;
         } else if (var1.equals("SessionPersistentStoreCookieName")) {
            return 47;
         } else if (var1.equals("SessionPersistentStoreDir")) {
            return 42;
         } else if (var1.equals("SessionPersistentStorePool")) {
            return 43;
         } else if (var1.equals("SessionPersistentStoreTable")) {
            return 44;
         } else if (var1.equals("SessionPersistentStoreType")) {
            return 46;
         } else if (var1.equals("SessionSwapIntervalSecs")) {
            return 48;
         } else if (var1.equals("SessionTimeoutSecs")) {
            return 18;
         } else if (var1.equals("SingleThreadedServletPoolSize")) {
            return 28;
         } else if (var1.equals("Targets")) {
            return 7;
         } else if (var1.equals("VirtualHosts")) {
            return 13;
         } else if (var1.equals("WebServers")) {
            return 12;
         } else if (var1.equals("CleanupSessionFilesEnabled")) {
            return 50;
         } else if (var1.equals("DebugEnabled")) {
            return 32;
         } else if (var1.equals("IndexDirectoryEnabled")) {
            return 23;
         } else if (var1.equals("PreferWebInfClasses")) {
            return 54;
         } else if (var1.equals("ServletExtensionCaseSensitive")) {
            return 26;
         } else if (var1.equals("SessionCookiesEnabled")) {
            return 36;
         } else if (var1.equals("SessionDebuggable")) {
            return 49;
         } else if (var1.equals("SessionMonitoringEnabled")) {
            return 53;
         } else if (var1.equals("SessionPersistentStoreShared")) {
            return 45;
         } else if (var1.equals("SessionTrackingEnabled")) {
            return 37;
         } else {
            return var1.equals("SessionURLRewritingEnabled") ? 33 : super.getPropertyIndex(var1);
         }
      }

      public Iterator getChildren() {
         ArrayList var1 = new ArrayList();
         return new CombinedIterator(var1);
      }

      protected long computeHashValue(CRC32 var1) {
         try {
            StringBuffer var2 = new StringBuffer();
            long var3 = super.computeHashValue(var1);
            if (var3 != 0L) {
               var2.append(String.valueOf(var3));
            }

            long var5 = 0L;
            if (this.bean.isActivatedTargetsSet()) {
               var2.append("ActivatedTargets");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getActivatedTargets())));
            }

            if (this.bean.isApplicationSet()) {
               var2.append("Application");
               var2.append(String.valueOf(this.bean.getApplication()));
            }

            if (this.bean.isAuthFilterSet()) {
               var2.append("AuthFilter");
               var2.append(String.valueOf(this.bean.getAuthFilter()));
            }

            if (this.bean.isAuthRealmNameSet()) {
               var2.append("AuthRealmName");
               var2.append(String.valueOf(this.bean.getAuthRealmName()));
            }

            if (this.bean.isContextPathSet()) {
               var2.append("ContextPath");
               var2.append(String.valueOf(this.bean.getContextPath()));
            }

            if (this.bean.isDefaultServletSet()) {
               var2.append("DefaultServlet");
               var2.append(String.valueOf(this.bean.getDefaultServlet()));
            }

            if (this.bean.isDeployedVirtualHostsSet()) {
               var2.append("DeployedVirtualHosts");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getDeployedVirtualHosts())));
            }

            if (this.bean.isDocumentRootSet()) {
               var2.append("DocumentRoot");
               var2.append(String.valueOf(this.bean.getDocumentRoot()));
            }

            if (this.bean.isIndexFilesSet()) {
               var2.append("IndexFiles");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getIndexFiles())));
            }

            if (this.bean.isMimeTypeDefaultSet()) {
               var2.append("MimeTypeDefault");
               var2.append(String.valueOf(this.bean.getMimeTypeDefault()));
            }

            if (this.bean.isMimeTypesSet()) {
               var2.append("MimeTypes");
               var2.append(String.valueOf(this.bean.getMimeTypes()));
            }

            if (this.bean.isNameSet()) {
               var2.append("Name");
               var2.append(String.valueOf(this.bean.getName()));
            }

            if (this.bean.isServletClasspathSet()) {
               var2.append("ServletClasspath");
               var2.append(String.valueOf(this.bean.getServletClasspath()));
            }

            if (this.bean.isServletReloadCheckSecsSet()) {
               var2.append("ServletReloadCheckSecs");
               var2.append(String.valueOf(this.bean.getServletReloadCheckSecs()));
            }

            if (this.bean.isServletsSet()) {
               var2.append("Servlets");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getServlets())));
            }

            if (this.bean.isSessionCacheSizeSet()) {
               var2.append("SessionCacheSize");
               var2.append(String.valueOf(this.bean.getSessionCacheSize()));
            }

            if (this.bean.isSessionCookieCommentSet()) {
               var2.append("SessionCookieComment");
               var2.append(String.valueOf(this.bean.getSessionCookieComment()));
            }

            if (this.bean.isSessionCookieDomainSet()) {
               var2.append("SessionCookieDomain");
               var2.append(String.valueOf(this.bean.getSessionCookieDomain()));
            }

            if (this.bean.isSessionCookieMaxAgeSecsSet()) {
               var2.append("SessionCookieMaxAgeSecs");
               var2.append(String.valueOf(this.bean.getSessionCookieMaxAgeSecs()));
            }

            if (this.bean.isSessionCookieNameSet()) {
               var2.append("SessionCookieName");
               var2.append(String.valueOf(this.bean.getSessionCookieName()));
            }

            if (this.bean.isSessionCookiePathSet()) {
               var2.append("SessionCookiePath");
               var2.append(String.valueOf(this.bean.getSessionCookiePath()));
            }

            if (this.bean.isSessionIDLengthSet()) {
               var2.append("SessionIDLength");
               var2.append(String.valueOf(this.bean.getSessionIDLength()));
            }

            if (this.bean.isSessionInvalidationIntervalSecsSet()) {
               var2.append("SessionInvalidationIntervalSecs");
               var2.append(String.valueOf(this.bean.getSessionInvalidationIntervalSecs()));
            }

            if (this.bean.isSessionJDBCConnectionTimeoutSecsSet()) {
               var2.append("SessionJDBCConnectionTimeoutSecs");
               var2.append(String.valueOf(this.bean.getSessionJDBCConnectionTimeoutSecs()));
            }

            if (this.bean.isSessionMainAttributeSet()) {
               var2.append("SessionMainAttribute");
               var2.append(String.valueOf(this.bean.getSessionMainAttribute()));
            }

            if (this.bean.isSessionPersistentStoreCookieNameSet()) {
               var2.append("SessionPersistentStoreCookieName");
               var2.append(String.valueOf(this.bean.getSessionPersistentStoreCookieName()));
            }

            if (this.bean.isSessionPersistentStoreDirSet()) {
               var2.append("SessionPersistentStoreDir");
               var2.append(String.valueOf(this.bean.getSessionPersistentStoreDir()));
            }

            if (this.bean.isSessionPersistentStorePoolSet()) {
               var2.append("SessionPersistentStorePool");
               var2.append(String.valueOf(this.bean.getSessionPersistentStorePool()));
            }

            if (this.bean.isSessionPersistentStoreTableSet()) {
               var2.append("SessionPersistentStoreTable");
               var2.append(String.valueOf(this.bean.getSessionPersistentStoreTable()));
            }

            if (this.bean.isSessionPersistentStoreTypeSet()) {
               var2.append("SessionPersistentStoreType");
               var2.append(String.valueOf(this.bean.getSessionPersistentStoreType()));
            }

            if (this.bean.isSessionSwapIntervalSecsSet()) {
               var2.append("SessionSwapIntervalSecs");
               var2.append(String.valueOf(this.bean.getSessionSwapIntervalSecs()));
            }

            if (this.bean.isSessionTimeoutSecsSet()) {
               var2.append("SessionTimeoutSecs");
               var2.append(String.valueOf(this.bean.getSessionTimeoutSecs()));
            }

            if (this.bean.isSingleThreadedServletPoolSizeSet()) {
               var2.append("SingleThreadedServletPoolSize");
               var2.append(String.valueOf(this.bean.getSingleThreadedServletPoolSize()));
            }

            if (this.bean.isTargetsSet()) {
               var2.append("Targets");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getTargets())));
            }

            if (this.bean.isVirtualHostsSet()) {
               var2.append("VirtualHosts");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getVirtualHosts())));
            }

            if (this.bean.isWebServersSet()) {
               var2.append("WebServers");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getWebServers())));
            }

            if (this.bean.isCleanupSessionFilesEnabledSet()) {
               var2.append("CleanupSessionFilesEnabled");
               var2.append(String.valueOf(this.bean.isCleanupSessionFilesEnabled()));
            }

            if (this.bean.isDebugEnabledSet()) {
               var2.append("DebugEnabled");
               var2.append(String.valueOf(this.bean.isDebugEnabled()));
            }

            if (this.bean.isIndexDirectoryEnabledSet()) {
               var2.append("IndexDirectoryEnabled");
               var2.append(String.valueOf(this.bean.isIndexDirectoryEnabled()));
            }

            if (this.bean.isPreferWebInfClassesSet()) {
               var2.append("PreferWebInfClasses");
               var2.append(String.valueOf(this.bean.isPreferWebInfClasses()));
            }

            if (this.bean.isServletExtensionCaseSensitiveSet()) {
               var2.append("ServletExtensionCaseSensitive");
               var2.append(String.valueOf(this.bean.isServletExtensionCaseSensitive()));
            }

            if (this.bean.isSessionCookiesEnabledSet()) {
               var2.append("SessionCookiesEnabled");
               var2.append(String.valueOf(this.bean.isSessionCookiesEnabled()));
            }

            if (this.bean.isSessionDebuggableSet()) {
               var2.append("SessionDebuggable");
               var2.append(String.valueOf(this.bean.isSessionDebuggable()));
            }

            if (this.bean.isSessionMonitoringEnabledSet()) {
               var2.append("SessionMonitoringEnabled");
               var2.append(String.valueOf(this.bean.isSessionMonitoringEnabled()));
            }

            if (this.bean.isSessionPersistentStoreSharedSet()) {
               var2.append("SessionPersistentStoreShared");
               var2.append(String.valueOf(this.bean.isSessionPersistentStoreShared()));
            }

            if (this.bean.isSessionTrackingEnabledSet()) {
               var2.append("SessionTrackingEnabled");
               var2.append(String.valueOf(this.bean.isSessionTrackingEnabled()));
            }

            if (this.bean.isSessionURLRewritingEnabledSet()) {
               var2.append("SessionURLRewritingEnabled");
               var2.append(String.valueOf(this.bean.isSessionURLRewritingEnabled()));
            }

            var1.update(var2.toString().getBytes());
            return var1.getValue();
         } catch (Exception var7) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var7);
         }
      }

      protected void computeDiff(AbstractDescriptorBean var1) {
         try {
            super.computeDiff(var1);
            WebAppComponentMBeanImpl var2 = (WebAppComponentMBeanImpl)var1;
            this.computeDiff("AuthFilter", this.bean.getAuthFilter(), var2.getAuthFilter(), false);
            this.computeDiff("AuthRealmName", this.bean.getAuthRealmName(), var2.getAuthRealmName(), false);
            this.computeDiff("Name", this.bean.getName(), var2.getName(), false);
            this.computeDiff("ServletReloadCheckSecs", this.bean.getServletReloadCheckSecs(), var2.getServletReloadCheckSecs(), false);
            this.computeDiff("SingleThreadedServletPoolSize", this.bean.getSingleThreadedServletPoolSize(), var2.getSingleThreadedServletPoolSize(), false);
            this.computeDiff("Targets", this.bean.getTargets(), var2.getTargets(), true);
            this.computeDiff("VirtualHosts", this.bean.getVirtualHosts(), var2.getVirtualHosts(), true);
            this.computeDiff("IndexDirectoryEnabled", this.bean.isIndexDirectoryEnabled(), var2.isIndexDirectoryEnabled(), false);
            this.computeDiff("PreferWebInfClasses", this.bean.isPreferWebInfClasses(), var2.isPreferWebInfClasses(), false);
            this.computeDiff("SessionMonitoringEnabled", this.bean.isSessionMonitoringEnabled(), var2.isSessionMonitoringEnabled(), false);
            this.computeDiff("SessionURLRewritingEnabled", this.bean.isSessionURLRewritingEnabled(), var2.isSessionURLRewritingEnabled(), false);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            WebAppComponentMBeanImpl var3 = (WebAppComponentMBeanImpl)var1.getSourceBean();
            WebAppComponentMBeanImpl var4 = (WebAppComponentMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (!var5.equals("ActivatedTargets") && !var5.equals("Application")) {
                  if (var5.equals("AuthFilter")) {
                     var3.setAuthFilter(var4.getAuthFilter());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 31);
                  } else if (var5.equals("AuthRealmName")) {
                     var3.setAuthRealmName(var4.getAuthRealmName());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 30);
                  } else if (!var5.equals("ContextPath") && !var5.equals("DefaultServlet") && !var5.equals("DeployedVirtualHosts") && !var5.equals("DocumentRoot") && !var5.equals("IndexFiles") && !var5.equals("MimeTypeDefault") && !var5.equals("MimeTypes")) {
                     if (var5.equals("Name")) {
                        var3.setName(var4.getName());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 2);
                     } else if (!var5.equals("ServletClasspath")) {
                        if (var5.equals("ServletReloadCheckSecs")) {
                           var3.setServletReloadCheckSecs(var4.getServletReloadCheckSecs());
                           var3._conditionalUnset(var2.isUnsetUpdate(), 27);
                        } else if (!var5.equals("Servlets") && !var5.equals("SessionCacheSize") && !var5.equals("SessionCookieComment") && !var5.equals("SessionCookieDomain") && !var5.equals("SessionCookieMaxAgeSecs") && !var5.equals("SessionCookieName") && !var5.equals("SessionCookiePath") && !var5.equals("SessionIDLength") && !var5.equals("SessionInvalidationIntervalSecs") && !var5.equals("SessionJDBCConnectionTimeoutSecs") && !var5.equals("SessionMainAttribute") && !var5.equals("SessionPersistentStoreCookieName") && !var5.equals("SessionPersistentStoreDir") && !var5.equals("SessionPersistentStorePool") && !var5.equals("SessionPersistentStoreTable") && !var5.equals("SessionPersistentStoreType") && !var5.equals("SessionSwapIntervalSecs") && !var5.equals("SessionTimeoutSecs")) {
                           if (var5.equals("SingleThreadedServletPoolSize")) {
                              var3.setSingleThreadedServletPoolSize(var4.getSingleThreadedServletPoolSize());
                              var3._conditionalUnset(var2.isUnsetUpdate(), 28);
                           } else if (var5.equals("Targets")) {
                              var3.setTargetsAsString(var4.getTargetsAsString());
                              var3._conditionalUnset(var2.isUnsetUpdate(), 7);
                           } else if (var5.equals("VirtualHosts")) {
                              var3.setVirtualHostsAsString(var4.getVirtualHostsAsString());
                              var3._conditionalUnset(var2.isUnsetUpdate(), 13);
                           } else if (!var5.equals("WebServers") && !var5.equals("CleanupSessionFilesEnabled") && !var5.equals("DebugEnabled")) {
                              if (var5.equals("IndexDirectoryEnabled")) {
                                 var3.setIndexDirectoryEnabled(var4.isIndexDirectoryEnabled());
                                 var3._conditionalUnset(var2.isUnsetUpdate(), 23);
                              } else if (var5.equals("PreferWebInfClasses")) {
                                 var3.setPreferWebInfClasses(var4.isPreferWebInfClasses());
                                 var3._conditionalUnset(var2.isUnsetUpdate(), 54);
                              } else if (!var5.equals("ServletExtensionCaseSensitive") && !var5.equals("SessionCookiesEnabled") && !var5.equals("SessionDebuggable")) {
                                 if (var5.equals("SessionMonitoringEnabled")) {
                                    var3.setSessionMonitoringEnabled(var4.isSessionMonitoringEnabled());
                                    var3._conditionalUnset(var2.isUnsetUpdate(), 53);
                                 } else if (!var5.equals("SessionPersistentStoreShared") && !var5.equals("SessionTrackingEnabled")) {
                                    if (var5.equals("SessionURLRewritingEnabled")) {
                                       var3.setSessionURLRewritingEnabled(var4.isSessionURLRewritingEnabled());
                                       var3._conditionalUnset(var2.isUnsetUpdate(), 33);
                                    } else {
                                       super.applyPropertyUpdate(var1, var2);
                                    }
                                 }
                              }
                           }
                        }
                     }
                  }
               }

            }
         } catch (RuntimeException var7) {
            throw var7;
         } catch (Exception var8) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var8);
         }
      }

      protected AbstractDescriptorBean finishCopy(AbstractDescriptorBean var1, boolean var2, List var3) {
         try {
            WebAppComponentMBeanImpl var5 = (WebAppComponentMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("AuthFilter")) && this.bean.isAuthFilterSet()) {
               var5.setAuthFilter(this.bean.getAuthFilter());
            }

            if ((var3 == null || !var3.contains("AuthRealmName")) && this.bean.isAuthRealmNameSet()) {
               var5.setAuthRealmName(this.bean.getAuthRealmName());
            }

            if ((var3 == null || !var3.contains("Name")) && this.bean.isNameSet()) {
               var5.setName(this.bean.getName());
            }

            if ((var3 == null || !var3.contains("ServletReloadCheckSecs")) && this.bean.isServletReloadCheckSecsSet()) {
               var5.setServletReloadCheckSecs(this.bean.getServletReloadCheckSecs());
            }

            if ((var3 == null || !var3.contains("SingleThreadedServletPoolSize")) && this.bean.isSingleThreadedServletPoolSizeSet()) {
               var5.setSingleThreadedServletPoolSize(this.bean.getSingleThreadedServletPoolSize());
            }

            if ((var3 == null || !var3.contains("Targets")) && this.bean.isTargetsSet()) {
               var5._unSet(var5, 7);
               var5.setTargetsAsString(this.bean.getTargetsAsString());
            }

            if ((var3 == null || !var3.contains("VirtualHosts")) && this.bean.isVirtualHostsSet()) {
               var5._unSet(var5, 13);
               var5.setVirtualHostsAsString(this.bean.getVirtualHostsAsString());
            }

            if ((var3 == null || !var3.contains("IndexDirectoryEnabled")) && this.bean.isIndexDirectoryEnabledSet()) {
               var5.setIndexDirectoryEnabled(this.bean.isIndexDirectoryEnabled());
            }

            if ((var3 == null || !var3.contains("PreferWebInfClasses")) && this.bean.isPreferWebInfClassesSet()) {
               var5.setPreferWebInfClasses(this.bean.isPreferWebInfClasses());
            }

            if ((var3 == null || !var3.contains("SessionMonitoringEnabled")) && this.bean.isSessionMonitoringEnabledSet()) {
               var5.setSessionMonitoringEnabled(this.bean.isSessionMonitoringEnabled());
            }

            if ((var3 == null || !var3.contains("SessionURLRewritingEnabled")) && this.bean.isSessionURLRewritingEnabledSet()) {
               var5.setSessionURLRewritingEnabled(this.bean.isSessionURLRewritingEnabled());
            }

            return var5;
         } catch (RuntimeException var6) {
            throw var6;
         } catch (Exception var7) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var7);
         }
      }

      protected void inferSubTree(Class var1, Object var2) {
         super.inferSubTree(var1, var2);
         Object var3 = null;
         this.inferSubTree(this.bean.getActivatedTargets(), var1, var2);
         this.inferSubTree(this.bean.getApplication(), var1, var2);
         this.inferSubTree(this.bean.getDeployedVirtualHosts(), var1, var2);
         this.inferSubTree(this.bean.getTargets(), var1, var2);
         this.inferSubTree(this.bean.getVirtualHosts(), var1, var2);
         this.inferSubTree(this.bean.getWebServers(), var1, var2);
      }
   }
}
