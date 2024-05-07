package weblogic.management.configuration;

import java.io.Serializable;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
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
import weblogic.management.mbeans.custom.ConnectorComponent;
import weblogic.utils.ArrayUtils;
import weblogic.utils.collections.CombinedIterator;

public class ConnectorComponentMBeanImpl extends ComponentMBeanImpl implements ConnectorComponentMBean, Serializable {
   private TargetMBean[] _ActivatedTargets;
   private ApplicationMBean _Application;
   private HashSet _AuthenticationMechanisms;
   private ClassLoader _ClassLoader;
   private Hashtable _ConfigProperties;
   private String _ConnectionFactoryDescription;
   private String _ConnectionFactoryImpl;
   private String _ConnectionFactoryInterface;
   private String _ConnectionFactoryName;
   private String _ConnectionImpl;
   private String _ConnectionInterface;
   private boolean _ConnectionProfilingEnabled;
   private String _Description;
   private String _DisplayName;
   private String _EisType;
   private String _LargeIcon;
   private String _LicenseDescription;
   private boolean _LicenseRequired;
   private String _ManagedConnectionFactoryClass;
   private int _MaxCapacity;
   private String _Name;
   private HashSet _SecurityPermissions;
   private String _SmallIcon;
   private String _SpecVersion;
   private TargetMBean[] _Targets;
   private String _TransactionSupport;
   private String _VendorName;
   private String _Version;
   private ConnectorComponent _customizer;
   private boolean _reauthenticationSupport;
   private static SchemaHelper2 _schemaHelper;

   public ConnectorComponentMBeanImpl() {
      try {
         this._customizer = new ConnectorComponent(this);
      } catch (Exception var2) {
         if (var2 instanceof RuntimeException) {
            throw (RuntimeException)var2;
         }

         throw new UndeclaredThrowableException(var2);
      }

      this._initializeProperty(-1);
   }

   public ConnectorComponentMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);

      try {
         this._customizer = new ConnectorComponent(this);
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

   public String getDescription() {
      return this._Description;
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

   public TargetMBean[] getTargets() {
      return this._customizer.getTargets();
   }

   public String getTargetsAsString() {
      return this._getHelper()._serializeKeyList(this.getTargets());
   }

   public boolean isApplicationSet() {
      return this._isSet(9);
   }

   public boolean isDescriptionSet() {
      return this._isSet(12);
   }

   public boolean isNameSet() {
      return this._isSet(2);
   }

   public boolean isTargetsSet() {
      return this._isSet(7);
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
                        ConnectorComponentMBeanImpl.this.addTarget((TargetMBean)var1);
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

   public void setDescription(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      this._Description = var1;
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

   public void setTargets(TargetMBean[] var1) throws InvalidAttributeValueException, DistributedManagementException {
      Object var4 = var1 == null ? new TargetMBeanImpl[0] : var1;
      var1 = (TargetMBean[])((TargetMBean[])this._getHelper()._cleanAndValidateArray(var4, TargetMBean.class));

      for(int var2 = 0; var2 < var1.length; ++var2) {
         if (var1[var2] != null) {
            ResolvedReference var3 = new ResolvedReference(this, 7, (AbstractDescriptorBean)var1[var2]) {
               protected Object getPropertyValue() {
                  return ConnectorComponentMBeanImpl.this.getTargets();
               }
            };
            this._getReferenceManager().registerResolvedReference((AbstractDescriptorBean)var1[var2], var3);
         }
      }

      TargetMBean[] var5 = this.getTargets();
      this._customizer.setTargets(var1);
      this._postSet(7, var5, var1);
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

   public String getDisplayName() {
      return this._DisplayName;
   }

   public boolean isDisplayNameSet() {
      return this._isSet(13);
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

   public void setDisplayName(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      this._DisplayName = var1;
   }

   public TargetMBean[] getActivatedTargets() {
      return this._customizer.getActivatedTargets();
   }

   public String getEisType() {
      return this._EisType;
   }

   public boolean isActivatedTargetsSet() {
      return this._isSet(11);
   }

   public boolean isEisTypeSet() {
      return this._isSet(14);
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

   public void setEisType(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      this._EisType = var1;
   }

   public String getSmallIcon() {
      return this._SmallIcon;
   }

   public boolean isSmallIconSet() {
      return this._isSet(15);
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

   public void setActivatedTargets(TargetMBean[] var1) {
      Object var2 = var1 == null ? new TargetMBeanImpl[0] : var1;
      this._ActivatedTargets = (TargetMBean[])var2;
   }

   public void setSmallIcon(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      this._SmallIcon = var1;
   }

   public boolean activated(TargetMBean var1) {
      return this._customizer.activated(var1);
   }

   public String getLargeIcon() {
      return this._LargeIcon;
   }

   public boolean isLargeIconSet() {
      return this._isSet(16);
   }

   public void refreshDDsIfNeeded(String[] var1) {
      this._customizer.refreshDDsIfNeeded(var1);
   }

   public void setLargeIcon(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      this._LargeIcon = var1;
   }

   public String getLicenseDescription() {
      return this._LicenseDescription;
   }

   public boolean isLicenseDescriptionSet() {
      return this._isSet(17);
   }

   public void touch() throws ConfigurationException {
      this._customizer.touch();
   }

   public void freezeCurrentValue(String var1) throws AttributeNotFoundException, MBeanException {
      this._customizer.freezeCurrentValue(var1);
   }

   public void setLicenseDescription(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      this._LicenseDescription = var1;
   }

   public boolean getLicenseRequired() {
      return this._LicenseRequired;
   }

   public boolean isLicenseRequiredSet() {
      return this._isSet(18);
   }

   public void restoreDefaultValue(String var1) throws AttributeNotFoundException {
      this._customizer.restoreDefaultValue(var1);
   }

   public void setLicenseRequired(boolean var1) throws InvalidAttributeValueException {
      this._LicenseRequired = var1;
   }

   public String getSpecVersion() {
      return this._SpecVersion;
   }

   public boolean isSpecVersionSet() {
      return this._isSet(19);
   }

   public void setSpecVersion(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      this._SpecVersion = var1;
   }

   public String getVendorName() {
      return this._VendorName;
   }

   public boolean isVendorNameSet() {
      return this._isSet(20);
   }

   public void setVendorName(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      this._VendorName = var1;
   }

   public String getVersion() {
      return this._Version;
   }

   public boolean isVersionSet() {
      return this._isSet(21);
   }

   public void setVersion(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      this._Version = var1;
   }

   public String getConnectionFactoryImpl() {
      return this._ConnectionFactoryImpl;
   }

   public boolean isConnectionFactoryImplSet() {
      return this._isSet(22);
   }

   public void setConnectionFactoryImpl(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      this._ConnectionFactoryImpl = var1;
   }

   public String getConnectionFactoryInterface() {
      return this._ConnectionFactoryInterface;
   }

   public boolean isConnectionFactoryInterfaceSet() {
      return this._isSet(23);
   }

   public void setConnectionFactoryInterface(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      this._ConnectionFactoryInterface = var1;
   }

   public String getConnectionImpl() {
      return this._ConnectionImpl;
   }

   public boolean isConnectionImplSet() {
      return this._isSet(24);
   }

   public void setConnectionImpl(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      this._ConnectionImpl = var1;
   }

   public String getConnectionInterface() {
      return this._ConnectionInterface;
   }

   public boolean isConnectionInterfaceSet() {
      return this._isSet(25);
   }

   public void setConnectionInterface(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      this._ConnectionInterface = var1;
   }

   public boolean getConnectionProfilingEnabled() {
      return this._ConnectionProfilingEnabled;
   }

   public boolean isConnectionProfilingEnabledSet() {
      return this._isSet(26);
   }

   public void setConnectionProfilingEnabled(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this._ConnectionProfilingEnabled;
      this._ConnectionProfilingEnabled = var1;
      this._postSet(26, var2, var1);
   }

   public String getManagedConnectionFactoryClass() {
      return this._ManagedConnectionFactoryClass;
   }

   public boolean isManagedConnectionFactoryClassSet() {
      return this._isSet(27);
   }

   public void setManagedConnectionFactoryClass(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      this._ManagedConnectionFactoryClass = var1;
   }

   public boolean getreauthenticationSupport() {
      return this._reauthenticationSupport;
   }

   public boolean isreauthenticationSupportSet() {
      return this._isSet(28);
   }

   public void setreauthenticationSupport(boolean var1) throws InvalidAttributeValueException {
      this._reauthenticationSupport = var1;
   }

   public String getTransactionSupport() {
      return this._TransactionSupport;
   }

   public boolean isTransactionSupportSet() {
      return this._isSet(29);
   }

   public void setTransactionSupport(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      this._TransactionSupport = var1;
   }

   public Hashtable getConfigProperties() {
      return this._ConfigProperties;
   }

   public boolean isConfigPropertiesSet() {
      return this._isSet(30);
   }

   public void setConfigProperties(Hashtable var1) {
      this._ConfigProperties = var1;
   }

   public HashSet getAuthenticationMechanisms() {
      return this._AuthenticationMechanisms;
   }

   public String getAuthenticationMechanismsAsString() {
      return StringHelper.objectToString(this.getAuthenticationMechanisms());
   }

   public boolean isAuthenticationMechanismsSet() {
      return this._isSet(31);
   }

   public void setAuthenticationMechanismsAsString(String var1) {
      try {
         this.setAuthenticationMechanisms(StringHelper.stringToHashSet(var1));
      } catch (Exception var3) {
         if (var3 instanceof RuntimeException) {
            throw (RuntimeException)var3;
         } else {
            throw new UndeclaredThrowableException(var3);
         }
      }
   }

   public void setAuthenticationMechanisms(HashSet var1) {
      HashSet var2 = this._AuthenticationMechanisms;
      this._AuthenticationMechanisms = var1;
      this._postSet(31, var2, var1);
   }

   public HashSet getSecurityPermissions() {
      return this._SecurityPermissions;
   }

   public boolean isSecurityPermissionsSet() {
      return this._isSet(32);
   }

   public void setSecurityPermissions(HashSet var1) {
      this._SecurityPermissions = var1;
   }

   public ClassLoader getClassLoader() {
      return this._ClassLoader;
   }

   public boolean isClassLoaderSet() {
      return this._isSet(33);
   }

   public void setClassLoader(ClassLoader var1) throws InvalidAttributeValueException {
      this._ClassLoader = var1;
   }

   public String getConnectionFactoryName() {
      return this._ConnectionFactoryName;
   }

   public boolean isConnectionFactoryNameSet() {
      return this._isSet(34);
   }

   public void setConnectionFactoryName(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      this._ConnectionFactoryName = var1;
   }

   public String getConnectionFactoryDescription() {
      return this._ConnectionFactoryDescription;
   }

   public boolean isConnectionFactoryDescriptionSet() {
      return this._isSet(35);
   }

   public void setConnectionFactoryDescription(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      this._ConnectionFactoryDescription = var1;
   }

   public int getMaxCapacity() {
      return this._MaxCapacity;
   }

   public boolean isMaxCapacitySet() {
      return this._isSet(36);
   }

   public void setMaxCapacity(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkMin("MaxCapacity", var1, 1);
      this._MaxCapacity = var1;
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
               this._AuthenticationMechanisms = null;
               if (var2) {
                  break;
               }
            case 33:
               this._ClassLoader = null;
               if (var2) {
                  break;
               }
            case 30:
               this._ConfigProperties = null;
               if (var2) {
                  break;
               }
            case 35:
               this._ConnectionFactoryDescription = null;
               if (var2) {
                  break;
               }
            case 22:
               this._ConnectionFactoryImpl = null;
               if (var2) {
                  break;
               }
            case 23:
               this._ConnectionFactoryInterface = null;
               if (var2) {
                  break;
               }
            case 34:
               this._ConnectionFactoryName = null;
               if (var2) {
                  break;
               }
            case 24:
               this._ConnectionImpl = null;
               if (var2) {
                  break;
               }
            case 25:
               this._ConnectionInterface = null;
               if (var2) {
                  break;
               }
            case 26:
               this._ConnectionProfilingEnabled = false;
               if (var2) {
                  break;
               }
            case 12:
               this._Description = null;
               if (var2) {
                  break;
               }
            case 13:
               this._DisplayName = null;
               if (var2) {
                  break;
               }
            case 14:
               this._EisType = null;
               if (var2) {
                  break;
               }
            case 16:
               this._LargeIcon = null;
               if (var2) {
                  break;
               }
            case 17:
               this._LicenseDescription = null;
               if (var2) {
                  break;
               }
            case 18:
               this._LicenseRequired = false;
               if (var2) {
                  break;
               }
            case 27:
               this._ManagedConnectionFactoryClass = null;
               if (var2) {
                  break;
               }
            case 36:
               this._MaxCapacity = 10;
               if (var2) {
                  break;
               }
            case 2:
               this._customizer.setName((String)null);
               if (var2) {
                  break;
               }
            case 32:
               this._SecurityPermissions = null;
               if (var2) {
                  break;
               }
            case 15:
               this._SmallIcon = null;
               if (var2) {
                  break;
               }
            case 19:
               this._SpecVersion = null;
               if (var2) {
                  break;
               }
            case 7:
               this._customizer.setTargets(new TargetMBean[0]);
               if (var2) {
                  break;
               }
            case 29:
               this._TransactionSupport = null;
               if (var2) {
                  break;
               }
            case 20:
               this._VendorName = null;
               if (var2) {
                  break;
               }
            case 21:
               this._Version = null;
               if (var2) {
                  break;
               }
            case 28:
               this._reauthenticationSupport = false;
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
      return "ConnectorComponent";
   }

   public void putValue(String var1, Object var2) {
      TargetMBean[] var6;
      if (var1.equals("ActivatedTargets")) {
         var6 = this._ActivatedTargets;
         this._ActivatedTargets = (TargetMBean[])((TargetMBean[])var2);
         this._postSet(11, var6, this._ActivatedTargets);
      } else if (var1.equals("Application")) {
         ApplicationMBean var11 = this._Application;
         this._Application = (ApplicationMBean)var2;
         this._postSet(9, var11, this._Application);
      } else {
         HashSet var7;
         if (var1.equals("AuthenticationMechanisms")) {
            var7 = this._AuthenticationMechanisms;
            this._AuthenticationMechanisms = (HashSet)var2;
            this._postSet(31, var7, this._AuthenticationMechanisms);
         } else if (var1.equals("ClassLoader")) {
            ClassLoader var10 = this._ClassLoader;
            this._ClassLoader = (ClassLoader)var2;
            this._postSet(33, var10, this._ClassLoader);
         } else if (var1.equals("ConfigProperties")) {
            Hashtable var9 = this._ConfigProperties;
            this._ConfigProperties = (Hashtable)var2;
            this._postSet(30, var9, this._ConfigProperties);
         } else {
            String var5;
            if (var1.equals("ConnectionFactoryDescription")) {
               var5 = this._ConnectionFactoryDescription;
               this._ConnectionFactoryDescription = (String)var2;
               this._postSet(35, var5, this._ConnectionFactoryDescription);
            } else if (var1.equals("ConnectionFactoryImpl")) {
               var5 = this._ConnectionFactoryImpl;
               this._ConnectionFactoryImpl = (String)var2;
               this._postSet(22, var5, this._ConnectionFactoryImpl);
            } else if (var1.equals("ConnectionFactoryInterface")) {
               var5 = this._ConnectionFactoryInterface;
               this._ConnectionFactoryInterface = (String)var2;
               this._postSet(23, var5, this._ConnectionFactoryInterface);
            } else if (var1.equals("ConnectionFactoryName")) {
               var5 = this._ConnectionFactoryName;
               this._ConnectionFactoryName = (String)var2;
               this._postSet(34, var5, this._ConnectionFactoryName);
            } else if (var1.equals("ConnectionImpl")) {
               var5 = this._ConnectionImpl;
               this._ConnectionImpl = (String)var2;
               this._postSet(24, var5, this._ConnectionImpl);
            } else if (var1.equals("ConnectionInterface")) {
               var5 = this._ConnectionInterface;
               this._ConnectionInterface = (String)var2;
               this._postSet(25, var5, this._ConnectionInterface);
            } else {
               boolean var3;
               if (var1.equals("ConnectionProfilingEnabled")) {
                  var3 = this._ConnectionProfilingEnabled;
                  this._ConnectionProfilingEnabled = (Boolean)var2;
                  this._postSet(26, var3, this._ConnectionProfilingEnabled);
               } else if (var1.equals("Description")) {
                  var5 = this._Description;
                  this._Description = (String)var2;
                  this._postSet(12, var5, this._Description);
               } else if (var1.equals("DisplayName")) {
                  var5 = this._DisplayName;
                  this._DisplayName = (String)var2;
                  this._postSet(13, var5, this._DisplayName);
               } else if (var1.equals("EisType")) {
                  var5 = this._EisType;
                  this._EisType = (String)var2;
                  this._postSet(14, var5, this._EisType);
               } else if (var1.equals("LargeIcon")) {
                  var5 = this._LargeIcon;
                  this._LargeIcon = (String)var2;
                  this._postSet(16, var5, this._LargeIcon);
               } else if (var1.equals("LicenseDescription")) {
                  var5 = this._LicenseDescription;
                  this._LicenseDescription = (String)var2;
                  this._postSet(17, var5, this._LicenseDescription);
               } else if (var1.equals("LicenseRequired")) {
                  var3 = this._LicenseRequired;
                  this._LicenseRequired = (Boolean)var2;
                  this._postSet(18, var3, this._LicenseRequired);
               } else if (var1.equals("ManagedConnectionFactoryClass")) {
                  var5 = this._ManagedConnectionFactoryClass;
                  this._ManagedConnectionFactoryClass = (String)var2;
                  this._postSet(27, var5, this._ManagedConnectionFactoryClass);
               } else if (var1.equals("MaxCapacity")) {
                  int var8 = this._MaxCapacity;
                  this._MaxCapacity = (Integer)var2;
                  this._postSet(36, var8, this._MaxCapacity);
               } else if (var1.equals("Name")) {
                  var5 = this._Name;
                  this._Name = (String)var2;
                  this._postSet(2, var5, this._Name);
               } else if (var1.equals("SecurityPermissions")) {
                  var7 = this._SecurityPermissions;
                  this._SecurityPermissions = (HashSet)var2;
                  this._postSet(32, var7, this._SecurityPermissions);
               } else if (var1.equals("SmallIcon")) {
                  var5 = this._SmallIcon;
                  this._SmallIcon = (String)var2;
                  this._postSet(15, var5, this._SmallIcon);
               } else if (var1.equals("SpecVersion")) {
                  var5 = this._SpecVersion;
                  this._SpecVersion = (String)var2;
                  this._postSet(19, var5, this._SpecVersion);
               } else if (var1.equals("Targets")) {
                  var6 = this._Targets;
                  this._Targets = (TargetMBean[])((TargetMBean[])var2);
                  this._postSet(7, var6, this._Targets);
               } else if (var1.equals("TransactionSupport")) {
                  var5 = this._TransactionSupport;
                  this._TransactionSupport = (String)var2;
                  this._postSet(29, var5, this._TransactionSupport);
               } else if (var1.equals("VendorName")) {
                  var5 = this._VendorName;
                  this._VendorName = (String)var2;
                  this._postSet(20, var5, this._VendorName);
               } else if (var1.equals("Version")) {
                  var5 = this._Version;
                  this._Version = (String)var2;
                  this._postSet(21, var5, this._Version);
               } else if (var1.equals("customizer")) {
                  ConnectorComponent var4 = this._customizer;
                  this._customizer = (ConnectorComponent)var2;
               } else if (var1.equals("reauthenticationSupport")) {
                  var3 = this._reauthenticationSupport;
                  this._reauthenticationSupport = (Boolean)var2;
                  this._postSet(28, var3, this._reauthenticationSupport);
               } else {
                  super.putValue(var1, var2);
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
      } else if (var1.equals("AuthenticationMechanisms")) {
         return this._AuthenticationMechanisms;
      } else if (var1.equals("ClassLoader")) {
         return this._ClassLoader;
      } else if (var1.equals("ConfigProperties")) {
         return this._ConfigProperties;
      } else if (var1.equals("ConnectionFactoryDescription")) {
         return this._ConnectionFactoryDescription;
      } else if (var1.equals("ConnectionFactoryImpl")) {
         return this._ConnectionFactoryImpl;
      } else if (var1.equals("ConnectionFactoryInterface")) {
         return this._ConnectionFactoryInterface;
      } else if (var1.equals("ConnectionFactoryName")) {
         return this._ConnectionFactoryName;
      } else if (var1.equals("ConnectionImpl")) {
         return this._ConnectionImpl;
      } else if (var1.equals("ConnectionInterface")) {
         return this._ConnectionInterface;
      } else if (var1.equals("ConnectionProfilingEnabled")) {
         return new Boolean(this._ConnectionProfilingEnabled);
      } else if (var1.equals("Description")) {
         return this._Description;
      } else if (var1.equals("DisplayName")) {
         return this._DisplayName;
      } else if (var1.equals("EisType")) {
         return this._EisType;
      } else if (var1.equals("LargeIcon")) {
         return this._LargeIcon;
      } else if (var1.equals("LicenseDescription")) {
         return this._LicenseDescription;
      } else if (var1.equals("LicenseRequired")) {
         return new Boolean(this._LicenseRequired);
      } else if (var1.equals("ManagedConnectionFactoryClass")) {
         return this._ManagedConnectionFactoryClass;
      } else if (var1.equals("MaxCapacity")) {
         return new Integer(this._MaxCapacity);
      } else if (var1.equals("Name")) {
         return this._Name;
      } else if (var1.equals("SecurityPermissions")) {
         return this._SecurityPermissions;
      } else if (var1.equals("SmallIcon")) {
         return this._SmallIcon;
      } else if (var1.equals("SpecVersion")) {
         return this._SpecVersion;
      } else if (var1.equals("Targets")) {
         return this._Targets;
      } else if (var1.equals("TransactionSupport")) {
         return this._TransactionSupport;
      } else if (var1.equals("VendorName")) {
         return this._VendorName;
      } else if (var1.equals("Version")) {
         return this._Version;
      } else if (var1.equals("customizer")) {
         return this._customizer;
      } else {
         return var1.equals("reauthenticationSupport") ? new Boolean(this._reauthenticationSupport) : super.getValue(var1);
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
            case 9:
            case 13:
            case 14:
            case 18:
            case 21:
            case 22:
            case 26:
            case 27:
            case 29:
            case 31:
            default:
               break;
            case 6:
               if (var1.equals("target")) {
                  return 7;
               }
               break;
            case 7:
               if (var1.equals("version")) {
                  return 21;
               }
               break;
            case 8:
               if (var1.equals("eis-type")) {
                  return 14;
               }
               break;
            case 10:
               if (var1.equals("large-icon")) {
                  return 16;
               }

               if (var1.equals("small-icon")) {
                  return 15;
               }
               break;
            case 11:
               if (var1.equals("application")) {
                  return 9;
               }

               if (var1.equals("description")) {
                  return 12;
               }

               if (var1.equals("vendor-name")) {
                  return 20;
               }
               break;
            case 12:
               if (var1.equals("class-loader")) {
                  return 33;
               }

               if (var1.equals("display-name")) {
                  return 13;
               }

               if (var1.equals("max-capacity")) {
                  return 36;
               }

               if (var1.equals("spec-version")) {
                  return 19;
               }
               break;
            case 15:
               if (var1.equals("connection-impl")) {
                  return 24;
               }
               break;
            case 16:
               if (var1.equals("activated-target")) {
                  return 11;
               }

               if (var1.equals("license-required")) {
                  return 18;
               }
               break;
            case 17:
               if (var1.equals("config-properties")) {
                  return 30;
               }
               break;
            case 19:
               if (var1.equals("license-description")) {
                  return 17;
               }

               if (var1.equals("transaction-support")) {
                  return 29;
               }
               break;
            case 20:
               if (var1.equals("connection-interface")) {
                  return 25;
               }

               if (var1.equals("security-permissions")) {
                  return 32;
               }
               break;
            case 23:
               if (var1.equals("connection-factory-impl")) {
                  return 22;
               }

               if (var1.equals("connection-factory-name")) {
                  return 34;
               }
               break;
            case 24:
               if (var1.equals("reauthentication-support")) {
                  return 28;
               }
               break;
            case 25:
               if (var1.equals("authentication-mechanisms")) {
                  return 31;
               }
               break;
            case 28:
               if (var1.equals("connection-factory-interface")) {
                  return 23;
               }

               if (var1.equals("connection-profiling-enabled")) {
                  return 26;
               }
               break;
            case 30:
               if (var1.equals("connection-factory-description")) {
                  return 35;
               }
               break;
            case 32:
               if (var1.equals("managed-connection-factory-class")) {
                  return 27;
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
               return "description";
            case 13:
               return "display-name";
            case 14:
               return "eis-type";
            case 15:
               return "small-icon";
            case 16:
               return "large-icon";
            case 17:
               return "license-description";
            case 18:
               return "license-required";
            case 19:
               return "spec-version";
            case 20:
               return "vendor-name";
            case 21:
               return "version";
            case 22:
               return "connection-factory-impl";
            case 23:
               return "connection-factory-interface";
            case 24:
               return "connection-impl";
            case 25:
               return "connection-interface";
            case 26:
               return "connection-profiling-enabled";
            case 27:
               return "managed-connection-factory-class";
            case 28:
               return "reauthentication-support";
            case 29:
               return "transaction-support";
            case 30:
               return "config-properties";
            case 31:
               return "authentication-mechanisms";
            case 32:
               return "security-permissions";
            case 33:
               return "class-loader";
            case 34:
               return "connection-factory-name";
            case 35:
               return "connection-factory-description";
            case 36:
               return "max-capacity";
         }
      }

      public boolean isArray(int var1) {
         switch (var1) {
            case 7:
               return true;
            case 11:
               return true;
            default:
               return super.isArray(var1);
         }
      }

      public boolean isBean(int var1) {
         switch (var1) {
            default:
               return super.isBean(var1);
         }
      }

      public boolean isConfigurable(int var1) {
         switch (var1) {
            case 26:
               return true;
            default:
               return super.isConfigurable(var1);
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
      private ConnectorComponentMBeanImpl bean;

      protected Helper(ConnectorComponentMBeanImpl var1) {
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
               return "Description";
            case 13:
               return "DisplayName";
            case 14:
               return "EisType";
            case 15:
               return "SmallIcon";
            case 16:
               return "LargeIcon";
            case 17:
               return "LicenseDescription";
            case 18:
               return "LicenseRequired";
            case 19:
               return "SpecVersion";
            case 20:
               return "VendorName";
            case 21:
               return "Version";
            case 22:
               return "ConnectionFactoryImpl";
            case 23:
               return "ConnectionFactoryInterface";
            case 24:
               return "ConnectionImpl";
            case 25:
               return "ConnectionInterface";
            case 26:
               return "ConnectionProfilingEnabled";
            case 27:
               return "ManagedConnectionFactoryClass";
            case 28:
               return "reauthenticationSupport";
            case 29:
               return "TransactionSupport";
            case 30:
               return "ConfigProperties";
            case 31:
               return "AuthenticationMechanisms";
            case 32:
               return "SecurityPermissions";
            case 33:
               return "ClassLoader";
            case 34:
               return "ConnectionFactoryName";
            case 35:
               return "ConnectionFactoryDescription";
            case 36:
               return "MaxCapacity";
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("ActivatedTargets")) {
            return 11;
         } else if (var1.equals("Application")) {
            return 9;
         } else if (var1.equals("AuthenticationMechanisms")) {
            return 31;
         } else if (var1.equals("ClassLoader")) {
            return 33;
         } else if (var1.equals("ConfigProperties")) {
            return 30;
         } else if (var1.equals("ConnectionFactoryDescription")) {
            return 35;
         } else if (var1.equals("ConnectionFactoryImpl")) {
            return 22;
         } else if (var1.equals("ConnectionFactoryInterface")) {
            return 23;
         } else if (var1.equals("ConnectionFactoryName")) {
            return 34;
         } else if (var1.equals("ConnectionImpl")) {
            return 24;
         } else if (var1.equals("ConnectionInterface")) {
            return 25;
         } else if (var1.equals("ConnectionProfilingEnabled")) {
            return 26;
         } else if (var1.equals("Description")) {
            return 12;
         } else if (var1.equals("DisplayName")) {
            return 13;
         } else if (var1.equals("EisType")) {
            return 14;
         } else if (var1.equals("LargeIcon")) {
            return 16;
         } else if (var1.equals("LicenseDescription")) {
            return 17;
         } else if (var1.equals("LicenseRequired")) {
            return 18;
         } else if (var1.equals("ManagedConnectionFactoryClass")) {
            return 27;
         } else if (var1.equals("MaxCapacity")) {
            return 36;
         } else if (var1.equals("Name")) {
            return 2;
         } else if (var1.equals("SecurityPermissions")) {
            return 32;
         } else if (var1.equals("SmallIcon")) {
            return 15;
         } else if (var1.equals("SpecVersion")) {
            return 19;
         } else if (var1.equals("Targets")) {
            return 7;
         } else if (var1.equals("TransactionSupport")) {
            return 29;
         } else if (var1.equals("VendorName")) {
            return 20;
         } else if (var1.equals("Version")) {
            return 21;
         } else {
            return var1.equals("reauthenticationSupport") ? 28 : super.getPropertyIndex(var1);
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

            if (this.bean.isAuthenticationMechanismsSet()) {
               var2.append("AuthenticationMechanisms");
               var2.append(String.valueOf(this.bean.getAuthenticationMechanisms()));
            }

            if (this.bean.isClassLoaderSet()) {
               var2.append("ClassLoader");
               var2.append(String.valueOf(this.bean.getClassLoader()));
            }

            if (this.bean.isConfigPropertiesSet()) {
               var2.append("ConfigProperties");
               var2.append(String.valueOf(this.bean.getConfigProperties()));
            }

            if (this.bean.isConnectionFactoryDescriptionSet()) {
               var2.append("ConnectionFactoryDescription");
               var2.append(String.valueOf(this.bean.getConnectionFactoryDescription()));
            }

            if (this.bean.isConnectionFactoryImplSet()) {
               var2.append("ConnectionFactoryImpl");
               var2.append(String.valueOf(this.bean.getConnectionFactoryImpl()));
            }

            if (this.bean.isConnectionFactoryInterfaceSet()) {
               var2.append("ConnectionFactoryInterface");
               var2.append(String.valueOf(this.bean.getConnectionFactoryInterface()));
            }

            if (this.bean.isConnectionFactoryNameSet()) {
               var2.append("ConnectionFactoryName");
               var2.append(String.valueOf(this.bean.getConnectionFactoryName()));
            }

            if (this.bean.isConnectionImplSet()) {
               var2.append("ConnectionImpl");
               var2.append(String.valueOf(this.bean.getConnectionImpl()));
            }

            if (this.bean.isConnectionInterfaceSet()) {
               var2.append("ConnectionInterface");
               var2.append(String.valueOf(this.bean.getConnectionInterface()));
            }

            if (this.bean.isConnectionProfilingEnabledSet()) {
               var2.append("ConnectionProfilingEnabled");
               var2.append(String.valueOf(this.bean.getConnectionProfilingEnabled()));
            }

            if (this.bean.isDescriptionSet()) {
               var2.append("Description");
               var2.append(String.valueOf(this.bean.getDescription()));
            }

            if (this.bean.isDisplayNameSet()) {
               var2.append("DisplayName");
               var2.append(String.valueOf(this.bean.getDisplayName()));
            }

            if (this.bean.isEisTypeSet()) {
               var2.append("EisType");
               var2.append(String.valueOf(this.bean.getEisType()));
            }

            if (this.bean.isLargeIconSet()) {
               var2.append("LargeIcon");
               var2.append(String.valueOf(this.bean.getLargeIcon()));
            }

            if (this.bean.isLicenseDescriptionSet()) {
               var2.append("LicenseDescription");
               var2.append(String.valueOf(this.bean.getLicenseDescription()));
            }

            if (this.bean.isLicenseRequiredSet()) {
               var2.append("LicenseRequired");
               var2.append(String.valueOf(this.bean.getLicenseRequired()));
            }

            if (this.bean.isManagedConnectionFactoryClassSet()) {
               var2.append("ManagedConnectionFactoryClass");
               var2.append(String.valueOf(this.bean.getManagedConnectionFactoryClass()));
            }

            if (this.bean.isMaxCapacitySet()) {
               var2.append("MaxCapacity");
               var2.append(String.valueOf(this.bean.getMaxCapacity()));
            }

            if (this.bean.isNameSet()) {
               var2.append("Name");
               var2.append(String.valueOf(this.bean.getName()));
            }

            if (this.bean.isSecurityPermissionsSet()) {
               var2.append("SecurityPermissions");
               var2.append(String.valueOf(this.bean.getSecurityPermissions()));
            }

            if (this.bean.isSmallIconSet()) {
               var2.append("SmallIcon");
               var2.append(String.valueOf(this.bean.getSmallIcon()));
            }

            if (this.bean.isSpecVersionSet()) {
               var2.append("SpecVersion");
               var2.append(String.valueOf(this.bean.getSpecVersion()));
            }

            if (this.bean.isTargetsSet()) {
               var2.append("Targets");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getTargets())));
            }

            if (this.bean.isTransactionSupportSet()) {
               var2.append("TransactionSupport");
               var2.append(String.valueOf(this.bean.getTransactionSupport()));
            }

            if (this.bean.isVendorNameSet()) {
               var2.append("VendorName");
               var2.append(String.valueOf(this.bean.getVendorName()));
            }

            if (this.bean.isVersionSet()) {
               var2.append("Version");
               var2.append(String.valueOf(this.bean.getVersion()));
            }

            if (this.bean.isreauthenticationSupportSet()) {
               var2.append("reauthenticationSupport");
               var2.append(String.valueOf(this.bean.getreauthenticationSupport()));
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
            ConnectorComponentMBeanImpl var2 = (ConnectorComponentMBeanImpl)var1;
            this.computeDiff("AuthenticationMechanisms", this.bean.getAuthenticationMechanisms(), var2.getAuthenticationMechanisms(), false);
            this.computeDiff("ConnectionProfilingEnabled", this.bean.getConnectionProfilingEnabled(), var2.getConnectionProfilingEnabled(), false);
            this.computeDiff("Name", this.bean.getName(), var2.getName(), false);
            this.computeDiff("Targets", this.bean.getTargets(), var2.getTargets(), true);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            ConnectorComponentMBeanImpl var3 = (ConnectorComponentMBeanImpl)var1.getSourceBean();
            ConnectorComponentMBeanImpl var4 = (ConnectorComponentMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (!var5.equals("ActivatedTargets") && !var5.equals("Application")) {
                  if (var5.equals("AuthenticationMechanisms")) {
                     var3.setAuthenticationMechanisms(var4.getAuthenticationMechanisms() == null ? null : (HashSet)var4.getAuthenticationMechanisms().clone());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 31);
                  } else if (!var5.equals("ClassLoader") && !var5.equals("ConfigProperties") && !var5.equals("ConnectionFactoryDescription") && !var5.equals("ConnectionFactoryImpl") && !var5.equals("ConnectionFactoryInterface") && !var5.equals("ConnectionFactoryName") && !var5.equals("ConnectionImpl") && !var5.equals("ConnectionInterface")) {
                     if (var5.equals("ConnectionProfilingEnabled")) {
                        var3.setConnectionProfilingEnabled(var4.getConnectionProfilingEnabled());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 26);
                     } else if (!var5.equals("Description") && !var5.equals("DisplayName") && !var5.equals("EisType") && !var5.equals("LargeIcon") && !var5.equals("LicenseDescription") && !var5.equals("LicenseRequired") && !var5.equals("ManagedConnectionFactoryClass") && !var5.equals("MaxCapacity")) {
                        if (var5.equals("Name")) {
                           var3.setName(var4.getName());
                           var3._conditionalUnset(var2.isUnsetUpdate(), 2);
                        } else if (!var5.equals("SecurityPermissions") && !var5.equals("SmallIcon") && !var5.equals("SpecVersion")) {
                           if (var5.equals("Targets")) {
                              var3.setTargetsAsString(var4.getTargetsAsString());
                              var3._conditionalUnset(var2.isUnsetUpdate(), 7);
                           } else if (!var5.equals("TransactionSupport") && !var5.equals("VendorName") && !var5.equals("Version") && !var5.equals("reauthenticationSupport")) {
                              super.applyPropertyUpdate(var1, var2);
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
            ConnectorComponentMBeanImpl var5 = (ConnectorComponentMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("AuthenticationMechanisms")) && this.bean.isAuthenticationMechanismsSet()) {
               var5.setAuthenticationMechanisms(this.bean.getAuthenticationMechanisms());
            }

            if ((var3 == null || !var3.contains("ConnectionProfilingEnabled")) && this.bean.isConnectionProfilingEnabledSet()) {
               var5.setConnectionProfilingEnabled(this.bean.getConnectionProfilingEnabled());
            }

            if ((var3 == null || !var3.contains("Name")) && this.bean.isNameSet()) {
               var5.setName(this.bean.getName());
            }

            if ((var3 == null || !var3.contains("Targets")) && this.bean.isTargetsSet()) {
               var5._unSet(var5, 7);
               var5.setTargetsAsString(this.bean.getTargetsAsString());
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
         this.inferSubTree(this.bean.getTargets(), var1, var2);
      }
   }
}
