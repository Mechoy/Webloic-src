package weblogic.management.configuration;

import java.io.Serializable;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.zip.CRC32;
import javax.management.AttributeNotFoundException;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanException;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.DescriptorValidateException;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBeanHelper;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.ReferenceManager;
import weblogic.descriptor.internal.ResolvedReference;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.management.ManagementException;
import weblogic.management.mbeans.custom.Security;
import weblogic.management.runtime.ServerSecurityRuntimeMBean;
import weblogic.utils.ArrayUtils;
import weblogic.utils.collections.CombinedIterator;

public class SecurityMBeanImpl extends ConfigurationMBeanImpl implements SecurityMBean, Serializable {
   private String _AuditProviderClassName;
   private boolean _CompatibilityMode;
   private String _ConnectionFilter;
   private String[] _ConnectionFilterRules;
   private boolean _ConnectionLoggerEnabled;
   private byte[] _EncryptedSecretKey;
   private boolean _GuestDisabled;
   private boolean _LogAllChecksEnabled;
   private String _Name;
   private PasswordPolicyMBean _PasswordPolicy;
   private RealmMBean _Realm;
   private byte[] _Salt;
   private ServerSecurityRuntimeMBean _ServerSecurityRuntime;
   private String _SystemUser;
   private Security _customizer;
   private static SchemaHelper2 _schemaHelper;

   public SecurityMBeanImpl() {
      try {
         this._customizer = new Security(this);
      } catch (Exception var2) {
         if (var2 instanceof RuntimeException) {
            throw (RuntimeException)var2;
         }

         throw new UndeclaredThrowableException(var2);
      }

      this._initializeProperty(-1);
   }

   public SecurityMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);

      try {
         this._customizer = new Security(this);
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         }

         throw new UndeclaredThrowableException(var4);
      }

      this._initializeProperty(-1);
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

   public RealmMBean getRealm() {
      return this._Realm;
   }

   public String getRealmAsString() {
      AbstractDescriptorBean var1 = (AbstractDescriptorBean)this.getRealm();
      return var1 == null ? null : var1._getKey().toString();
   }

   public boolean isNameSet() {
      return this._isSet(2);
   }

   public boolean isRealmSet() {
      return this._isSet(7);
   }

   public void setRealmAsString(String var1) {
      if (var1 != null && var1.length() != 0) {
         var1 = var1 == null ? null : var1.trim();
         this._getReferenceManager().registerUnresolvedReference(var1, RealmMBean.class, new ReferenceManager.Resolver(this, 7) {
            public void resolveReference(Object var1) {
               try {
                  SecurityMBeanImpl.this.setRealm((RealmMBean)var1);
               } catch (RuntimeException var3) {
                  throw var3;
               } catch (Exception var4) {
                  throw new AssertionError("Impossible exception: " + var4);
               }
            }
         });
      } else {
         RealmMBean var2 = this._Realm;
         this._initializeProperty(7);
         this._postSet(7, var2, this._Realm);
      }

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

   public void setRealm(RealmMBean var1) throws InvalidAttributeValueException {
      if (var1 != null) {
         ResolvedReference var2 = new ResolvedReference(this, 7, (AbstractDescriptorBean)var1) {
            protected Object getPropertyValue() {
               return SecurityMBeanImpl.this.getRealm();
            }
         };
         this._getReferenceManager().registerResolvedReference((AbstractDescriptorBean)var1, var2);
      }

      RealmMBean var3 = this._Realm;
      this._Realm = var1;
      this._postSet(7, var3, var1);
   }

   public String getAuditProviderClassName() {
      return this._AuditProviderClassName;
   }

   public boolean isAuditProviderClassNameSet() {
      return this._isSet(8);
   }

   public void setAuditProviderClassName(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._AuditProviderClassName;
      this._AuditProviderClassName = var1;
      this._postSet(8, var2, var1);
   }

   public boolean isGuestDisabled() {
      return this._GuestDisabled;
   }

   public boolean isGuestDisabledSet() {
      return this._isSet(9);
   }

   public void setGuestDisabled(boolean var1) {
      boolean var2 = this._GuestDisabled;
      this._GuestDisabled = var1;
      this._postSet(9, var2, var1);
   }

   public String getConnectionFilter() {
      return this._customizer.getConnectionFilter();
   }

   public boolean isConnectionFilterSet() {
      return this._isSet(10);
   }

   public void setConnectionFilter(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      this._customizer.setConnectionFilter(var1);
   }

   public String getSystemUser() {
      return this._customizer.getSystemUser();
   }

   public boolean isSystemUserSet() {
      return this._isSet(11);
   }

   public void setSystemUser(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      weblogic.descriptor.beangen.LegalChecks.checkNonNull("SystemUser", var1);
      String var2 = this.getSystemUser();
      this._customizer.setSystemUser(var1);
      this._postSet(11, var2, var1);
   }

   public boolean getLogAllChecksEnabled() {
      return this._LogAllChecksEnabled;
   }

   public boolean isLogAllChecksEnabledSet() {
      return this._isSet(12);
   }

   public void touch() throws ConfigurationException {
      this._customizer.touch();
   }

   public void freezeCurrentValue(String var1) throws AttributeNotFoundException, MBeanException {
      this._customizer.freezeCurrentValue(var1);
   }

   public void setLogAllChecksEnabled(boolean var1) {
      boolean var2 = this._LogAllChecksEnabled;
      this._LogAllChecksEnabled = var1;
      this._postSet(12, var2, var1);
   }

   public PasswordPolicyMBean getPasswordPolicy() {
      return this._PasswordPolicy;
   }

   public String getPasswordPolicyAsString() {
      AbstractDescriptorBean var1 = (AbstractDescriptorBean)this.getPasswordPolicy();
      return var1 == null ? null : var1._getKey().toString();
   }

   public boolean isPasswordPolicySet() {
      return this._isSet(13);
   }

   public void restoreDefaultValue(String var1) throws AttributeNotFoundException {
      this._customizer.restoreDefaultValue(var1);
   }

   public void setPasswordPolicyAsString(String var1) {
      if (var1 != null && var1.length() != 0) {
         var1 = var1 == null ? null : var1.trim();
         this._getReferenceManager().registerUnresolvedReference(var1, PasswordPolicyMBean.class, new ReferenceManager.Resolver(this, 13) {
            public void resolveReference(Object var1) {
               try {
                  SecurityMBeanImpl.this.setPasswordPolicy((PasswordPolicyMBean)var1);
               } catch (RuntimeException var3) {
                  throw var3;
               } catch (Exception var4) {
                  throw new AssertionError("Impossible exception: " + var4);
               }
            }
         });
      } else {
         PasswordPolicyMBean var2 = this._PasswordPolicy;
         this._initializeProperty(13);
         this._postSet(13, var2, this._PasswordPolicy);
      }

   }

   public void setPasswordPolicy(PasswordPolicyMBean var1) throws InvalidAttributeValueException {
      if (var1 != null) {
         ResolvedReference var2 = new ResolvedReference(this, 13, (AbstractDescriptorBean)var1) {
            protected Object getPropertyValue() {
               return SecurityMBeanImpl.this.getPasswordPolicy();
            }
         };
         this._getReferenceManager().registerResolvedReference((AbstractDescriptorBean)var1, var2);
      }

      PasswordPolicyMBean var3 = this._PasswordPolicy;
      this._PasswordPolicy = var1;
      this._postSet(13, var3, var1);
   }

   public byte[] getSalt() {
      return this._customizer.getSalt();
   }

   public boolean isSaltSet() {
      return this._isSet(14);
   }

   public void setSalt(byte[] var1) throws InvalidAttributeValueException {
      this._Salt = var1;
   }

   public byte[] getEncryptedSecretKey() {
      return this._customizer.getEncryptedSecretKey();
   }

   public boolean isEncryptedSecretKeySet() {
      return this._isSet(15);
   }

   public void setEncryptedSecretKey(byte[] var1) throws InvalidAttributeValueException {
      this._EncryptedSecretKey = var1;
   }

   public ServerSecurityRuntimeMBean getServerSecurityRuntime() {
      return this._ServerSecurityRuntime;
   }

   public boolean isServerSecurityRuntimeSet() {
      return this._isSet(16);
   }

   public void setServerSecurityRuntime(ServerSecurityRuntimeMBean var1) {
      this._ServerSecurityRuntime = var1;
   }

   public boolean isCompatibilityMode() {
      return this._CompatibilityMode;
   }

   public boolean isCompatibilityModeSet() {
      return this._isSet(17);
   }

   public void setCompatibilityMode(boolean var1) {
      boolean var2 = this._CompatibilityMode;
      this._CompatibilityMode = var1;
      this._postSet(17, var2, var1);
   }

   public String[] getConnectionFilterRules() {
      return this._customizer.getConnectionFilterRules();
   }

   public boolean isConnectionFilterRulesSet() {
      return this._isSet(18);
   }

   public void setConnectionFilterRules(String[] var1) {
      var1 = var1 == null ? new String[0] : var1;
      this._getHelper()._ensureNonNullElements(var1);
      this._customizer.setConnectionFilterRules(var1);
   }

   public boolean getConnectionLoggerEnabled() {
      return this._customizer.getConnectionLoggerEnabled();
   }

   public boolean isConnectionLoggerEnabledSet() {
      return this._isSet(19);
   }

   public void setConnectionLoggerEnabled(boolean var1) throws InvalidAttributeValueException {
      this._customizer.setConnectionLoggerEnabled(var1);
   }

   public Object _getKey() {
      return this.getName();
   }

   public void _validate() throws IllegalArgumentException {
      super._validate();
      SecurityLegalHelper.validateSecurity(this);
   }

   protected void _postCreate() {
      this._customizer._postCreate();
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
         var1 = 8;
      }

      try {
         switch (var1) {
            case 8:
               this._AuditProviderClassName = null;
               if (var2) {
                  break;
               }
            case 10:
               this._customizer.setConnectionFilter((String)null);
               if (var2) {
                  break;
               }
            case 18:
               this._customizer.setConnectionFilterRules(new String[0]);
               if (var2) {
                  break;
               }
            case 19:
               this._customizer.setConnectionLoggerEnabled(false);
               if (var2) {
                  break;
               }
            case 15:
               this._EncryptedSecretKey = new byte[0];
               if (var2) {
                  break;
               }
            case 12:
               this._LogAllChecksEnabled = false;
               if (var2) {
                  break;
               }
            case 2:
               this._customizer.setName((String)null);
               if (var2) {
                  break;
               }
            case 13:
               this._PasswordPolicy = null;
               if (var2) {
                  break;
               }
            case 7:
               this._Realm = null;
               if (var2) {
                  break;
               }
            case 14:
               this._Salt = new byte[0];
               if (var2) {
                  break;
               }
            case 16:
               this._ServerSecurityRuntime = null;
               if (var2) {
                  break;
               }
            case 11:
               this._customizer.setSystemUser("system");
               if (var2) {
                  break;
               }
            case 17:
               this._CompatibilityMode = false;
               if (var2) {
                  break;
               }
            case 9:
               this._GuestDisabled = true;
               if (var2) {
                  break;
               }
            case 3:
            case 4:
            case 5:
            case 6:
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
      return "Security";
   }

   public void putValue(String var1, Object var2) {
      String var4;
      if (var1.equals("AuditProviderClassName")) {
         var4 = this._AuditProviderClassName;
         this._AuditProviderClassName = (String)var2;
         this._postSet(8, var4, this._AuditProviderClassName);
      } else {
         boolean var9;
         if (var1.equals("CompatibilityMode")) {
            var9 = this._CompatibilityMode;
            this._CompatibilityMode = (Boolean)var2;
            this._postSet(17, var9, this._CompatibilityMode);
         } else if (var1.equals("ConnectionFilter")) {
            var4 = this._ConnectionFilter;
            this._ConnectionFilter = (String)var2;
            this._postSet(10, var4, this._ConnectionFilter);
         } else if (var1.equals("ConnectionFilterRules")) {
            String[] var10 = this._ConnectionFilterRules;
            this._ConnectionFilterRules = (String[])((String[])var2);
            this._postSet(18, var10, this._ConnectionFilterRules);
         } else if (var1.equals("ConnectionLoggerEnabled")) {
            var9 = this._ConnectionLoggerEnabled;
            this._ConnectionLoggerEnabled = (Boolean)var2;
            this._postSet(19, var9, this._ConnectionLoggerEnabled);
         } else {
            byte[] var6;
            if (var1.equals("EncryptedSecretKey")) {
               var6 = this._EncryptedSecretKey;
               this._EncryptedSecretKey = (byte[])((byte[])var2);
               this._postSet(15, var6, this._EncryptedSecretKey);
            } else if (var1.equals("GuestDisabled")) {
               var9 = this._GuestDisabled;
               this._GuestDisabled = (Boolean)var2;
               this._postSet(9, var9, this._GuestDisabled);
            } else if (var1.equals("LogAllChecksEnabled")) {
               var9 = this._LogAllChecksEnabled;
               this._LogAllChecksEnabled = (Boolean)var2;
               this._postSet(12, var9, this._LogAllChecksEnabled);
            } else if (var1.equals("Name")) {
               var4 = this._Name;
               this._Name = (String)var2;
               this._postSet(2, var4, this._Name);
            } else if (var1.equals("PasswordPolicy")) {
               PasswordPolicyMBean var8 = this._PasswordPolicy;
               this._PasswordPolicy = (PasswordPolicyMBean)var2;
               this._postSet(13, var8, this._PasswordPolicy);
            } else if (var1.equals("Realm")) {
               RealmMBean var7 = this._Realm;
               this._Realm = (RealmMBean)var2;
               this._postSet(7, var7, this._Realm);
            } else if (var1.equals("Salt")) {
               var6 = this._Salt;
               this._Salt = (byte[])((byte[])var2);
               this._postSet(14, var6, this._Salt);
            } else if (var1.equals("ServerSecurityRuntime")) {
               ServerSecurityRuntimeMBean var5 = this._ServerSecurityRuntime;
               this._ServerSecurityRuntime = (ServerSecurityRuntimeMBean)var2;
               this._postSet(16, var5, this._ServerSecurityRuntime);
            } else if (var1.equals("SystemUser")) {
               var4 = this._SystemUser;
               this._SystemUser = (String)var2;
               this._postSet(11, var4, this._SystemUser);
            } else if (var1.equals("customizer")) {
               Security var3 = this._customizer;
               this._customizer = (Security)var2;
            } else {
               super.putValue(var1, var2);
            }
         }
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("AuditProviderClassName")) {
         return this._AuditProviderClassName;
      } else if (var1.equals("CompatibilityMode")) {
         return new Boolean(this._CompatibilityMode);
      } else if (var1.equals("ConnectionFilter")) {
         return this._ConnectionFilter;
      } else if (var1.equals("ConnectionFilterRules")) {
         return this._ConnectionFilterRules;
      } else if (var1.equals("ConnectionLoggerEnabled")) {
         return new Boolean(this._ConnectionLoggerEnabled);
      } else if (var1.equals("EncryptedSecretKey")) {
         return this._EncryptedSecretKey;
      } else if (var1.equals("GuestDisabled")) {
         return new Boolean(this._GuestDisabled);
      } else if (var1.equals("LogAllChecksEnabled")) {
         return new Boolean(this._LogAllChecksEnabled);
      } else if (var1.equals("Name")) {
         return this._Name;
      } else if (var1.equals("PasswordPolicy")) {
         return this._PasswordPolicy;
      } else if (var1.equals("Realm")) {
         return this._Realm;
      } else if (var1.equals("Salt")) {
         return this._Salt;
      } else if (var1.equals("ServerSecurityRuntime")) {
         return this._ServerSecurityRuntime;
      } else if (var1.equals("SystemUser")) {
         return this._SystemUser;
      } else {
         return var1.equals("customizer") ? this._customizer : super.getValue(var1);
      }
   }

   public static void validateGeneration() {
      try {
         weblogic.descriptor.beangen.LegalChecks.checkNonNull("SystemUser", "system");
      } catch (IllegalArgumentException var1) {
         throw new DescriptorValidateException("The default value for the property  is null. Properties annotated with false value on @legalZeroLength or @legalNull  should either have @required/@derivedDefault annotations or have a non-null value on @default annotation. Refer annotation legalNull on property SystemUser in SecurityMBean" + var1.getMessage());
      }
   }

   public static class SchemaHelper2 extends ConfigurationMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 4:
               if (var1.equals("name")) {
                  return 2;
               }

               if (var1.equals("salt")) {
                  return 14;
               }
               break;
            case 5:
               if (var1.equals("realm")) {
                  return 7;
               }
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 12:
            case 13:
            case 16:
            case 19:
            case 21:
            case 24:
            default:
               break;
            case 11:
               if (var1.equals("system-user")) {
                  return 11;
               }
               break;
            case 14:
               if (var1.equals("guest-disabled")) {
                  return 9;
               }
               break;
            case 15:
               if (var1.equals("password-policy")) {
                  return 13;
               }
               break;
            case 17:
               if (var1.equals("connection-filter")) {
                  return 10;
               }
               break;
            case 18:
               if (var1.equals("compatibility-mode")) {
                  return 17;
               }
               break;
            case 20:
               if (var1.equals("encrypted-secret-key")) {
                  return 15;
               }
               break;
            case 22:
               if (var1.equals("connection-filter-rule")) {
                  return 18;
               }

               if (var1.equals("log-all-checks-enabled")) {
                  return 12;
               }
               break;
            case 23:
               if (var1.equals("server-security-runtime")) {
                  return 16;
               }
               break;
            case 25:
               if (var1.equals("audit-provider-class-name")) {
                  return 8;
               }

               if (var1.equals("connection-logger-enabled")) {
                  return 19;
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
            default:
               return super.getElementName(var1);
            case 7:
               return "realm";
            case 8:
               return "audit-provider-class-name";
            case 9:
               return "guest-disabled";
            case 10:
               return "connection-filter";
            case 11:
               return "system-user";
            case 12:
               return "log-all-checks-enabled";
            case 13:
               return "password-policy";
            case 14:
               return "salt";
            case 15:
               return "encrypted-secret-key";
            case 16:
               return "server-security-runtime";
            case 17:
               return "compatibility-mode";
            case 18:
               return "connection-filter-rule";
            case 19:
               return "connection-logger-enabled";
         }
      }

      public boolean isArray(int var1) {
         switch (var1) {
            case 14:
               return true;
            case 15:
               return true;
            case 16:
            case 17:
            default:
               return super.isArray(var1);
            case 18:
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

   protected static class Helper extends ConfigurationMBeanImpl.Helper {
      private SecurityMBeanImpl bean;

      protected Helper(SecurityMBeanImpl var1) {
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
            default:
               return super.getPropertyName(var1);
            case 7:
               return "Realm";
            case 8:
               return "AuditProviderClassName";
            case 9:
               return "GuestDisabled";
            case 10:
               return "ConnectionFilter";
            case 11:
               return "SystemUser";
            case 12:
               return "LogAllChecksEnabled";
            case 13:
               return "PasswordPolicy";
            case 14:
               return "Salt";
            case 15:
               return "EncryptedSecretKey";
            case 16:
               return "ServerSecurityRuntime";
            case 17:
               return "CompatibilityMode";
            case 18:
               return "ConnectionFilterRules";
            case 19:
               return "ConnectionLoggerEnabled";
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("AuditProviderClassName")) {
            return 8;
         } else if (var1.equals("ConnectionFilter")) {
            return 10;
         } else if (var1.equals("ConnectionFilterRules")) {
            return 18;
         } else if (var1.equals("ConnectionLoggerEnabled")) {
            return 19;
         } else if (var1.equals("EncryptedSecretKey")) {
            return 15;
         } else if (var1.equals("LogAllChecksEnabled")) {
            return 12;
         } else if (var1.equals("Name")) {
            return 2;
         } else if (var1.equals("PasswordPolicy")) {
            return 13;
         } else if (var1.equals("Realm")) {
            return 7;
         } else if (var1.equals("Salt")) {
            return 14;
         } else if (var1.equals("ServerSecurityRuntime")) {
            return 16;
         } else if (var1.equals("SystemUser")) {
            return 11;
         } else if (var1.equals("CompatibilityMode")) {
            return 17;
         } else {
            return var1.equals("GuestDisabled") ? 9 : super.getPropertyIndex(var1);
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
            if (this.bean.isAuditProviderClassNameSet()) {
               var2.append("AuditProviderClassName");
               var2.append(String.valueOf(this.bean.getAuditProviderClassName()));
            }

            if (this.bean.isConnectionFilterSet()) {
               var2.append("ConnectionFilter");
               var2.append(String.valueOf(this.bean.getConnectionFilter()));
            }

            if (this.bean.isConnectionFilterRulesSet()) {
               var2.append("ConnectionFilterRules");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getConnectionFilterRules())));
            }

            if (this.bean.isConnectionLoggerEnabledSet()) {
               var2.append("ConnectionLoggerEnabled");
               var2.append(String.valueOf(this.bean.getConnectionLoggerEnabled()));
            }

            if (this.bean.isEncryptedSecretKeySet()) {
               var2.append("EncryptedSecretKey");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getEncryptedSecretKey())));
            }

            if (this.bean.isLogAllChecksEnabledSet()) {
               var2.append("LogAllChecksEnabled");
               var2.append(String.valueOf(this.bean.getLogAllChecksEnabled()));
            }

            if (this.bean.isNameSet()) {
               var2.append("Name");
               var2.append(String.valueOf(this.bean.getName()));
            }

            if (this.bean.isPasswordPolicySet()) {
               var2.append("PasswordPolicy");
               var2.append(String.valueOf(this.bean.getPasswordPolicy()));
            }

            if (this.bean.isRealmSet()) {
               var2.append("Realm");
               var2.append(String.valueOf(this.bean.getRealm()));
            }

            if (this.bean.isSaltSet()) {
               var2.append("Salt");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getSalt())));
            }

            if (this.bean.isServerSecurityRuntimeSet()) {
               var2.append("ServerSecurityRuntime");
               var2.append(String.valueOf(this.bean.getServerSecurityRuntime()));
            }

            if (this.bean.isSystemUserSet()) {
               var2.append("SystemUser");
               var2.append(String.valueOf(this.bean.getSystemUser()));
            }

            if (this.bean.isCompatibilityModeSet()) {
               var2.append("CompatibilityMode");
               var2.append(String.valueOf(this.bean.isCompatibilityMode()));
            }

            if (this.bean.isGuestDisabledSet()) {
               var2.append("GuestDisabled");
               var2.append(String.valueOf(this.bean.isGuestDisabled()));
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
            SecurityMBeanImpl var2 = (SecurityMBeanImpl)var1;
            this.computeDiff("AuditProviderClassName", this.bean.getAuditProviderClassName(), var2.getAuditProviderClassName(), false);
            this.computeDiff("LogAllChecksEnabled", this.bean.getLogAllChecksEnabled(), var2.getLogAllChecksEnabled(), false);
            this.computeDiff("Name", this.bean.getName(), var2.getName(), false);
            this.computeDiff("PasswordPolicy", this.bean.getPasswordPolicy(), var2.getPasswordPolicy(), false);
            this.computeDiff("Realm", this.bean.getRealm(), var2.getRealm(), false);
            this.computeDiff("SystemUser", this.bean.getSystemUser(), var2.getSystemUser(), false);
            this.computeDiff("CompatibilityMode", this.bean.isCompatibilityMode(), var2.isCompatibilityMode(), false);
            this.computeDiff("GuestDisabled", this.bean.isGuestDisabled(), var2.isGuestDisabled(), false);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            SecurityMBeanImpl var3 = (SecurityMBeanImpl)var1.getSourceBean();
            SecurityMBeanImpl var4 = (SecurityMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("AuditProviderClassName")) {
                  var3.setAuditProviderClassName(var4.getAuditProviderClassName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 8);
               } else if (!var5.equals("ConnectionFilter") && !var5.equals("ConnectionFilterRules") && !var5.equals("ConnectionLoggerEnabled") && !var5.equals("EncryptedSecretKey")) {
                  if (var5.equals("LogAllChecksEnabled")) {
                     var3.setLogAllChecksEnabled(var4.getLogAllChecksEnabled());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 12);
                  } else if (var5.equals("Name")) {
                     var3.setName(var4.getName());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 2);
                  } else if (var5.equals("PasswordPolicy")) {
                     var3.setPasswordPolicyAsString(var4.getPasswordPolicyAsString());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 13);
                  } else if (var5.equals("Realm")) {
                     var3.setRealmAsString(var4.getRealmAsString());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 7);
                  } else if (!var5.equals("Salt") && !var5.equals("ServerSecurityRuntime")) {
                     if (var5.equals("SystemUser")) {
                        var3.setSystemUser(var4.getSystemUser());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 11);
                     } else if (var5.equals("CompatibilityMode")) {
                        var3.setCompatibilityMode(var4.isCompatibilityMode());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 17);
                     } else if (var5.equals("GuestDisabled")) {
                        var3.setGuestDisabled(var4.isGuestDisabled());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 9);
                     } else {
                        super.applyPropertyUpdate(var1, var2);
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
            SecurityMBeanImpl var5 = (SecurityMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("AuditProviderClassName")) && this.bean.isAuditProviderClassNameSet()) {
               var5.setAuditProviderClassName(this.bean.getAuditProviderClassName());
            }

            if ((var3 == null || !var3.contains("LogAllChecksEnabled")) && this.bean.isLogAllChecksEnabledSet()) {
               var5.setLogAllChecksEnabled(this.bean.getLogAllChecksEnabled());
            }

            if ((var3 == null || !var3.contains("Name")) && this.bean.isNameSet()) {
               var5.setName(this.bean.getName());
            }

            if ((var3 == null || !var3.contains("PasswordPolicy")) && this.bean.isPasswordPolicySet()) {
               var5._unSet(var5, 13);
               var5.setPasswordPolicyAsString(this.bean.getPasswordPolicyAsString());
            }

            if ((var3 == null || !var3.contains("Realm")) && this.bean.isRealmSet()) {
               var5._unSet(var5, 7);
               var5.setRealmAsString(this.bean.getRealmAsString());
            }

            if ((var3 == null || !var3.contains("SystemUser")) && this.bean.isSystemUserSet()) {
               var5.setSystemUser(this.bean.getSystemUser());
            }

            if ((var3 == null || !var3.contains("CompatibilityMode")) && this.bean.isCompatibilityModeSet()) {
               var5.setCompatibilityMode(this.bean.isCompatibilityMode());
            }

            if ((var3 == null || !var3.contains("GuestDisabled")) && this.bean.isGuestDisabledSet()) {
               var5.setGuestDisabled(this.bean.isGuestDisabled());
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
         this.inferSubTree(this.bean.getPasswordPolicy(), var1, var2);
         this.inferSubTree(this.bean.getRealm(), var1, var2);
      }
   }
}
