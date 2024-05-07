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
import javax.management.JMException;
import javax.management.MBeanException;
import weblogic.descriptor.BeanRemoveRejectedException;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.BootstrapProperties;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBeanHelper;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.ReferenceManager;
import weblogic.descriptor.internal.ResolvedReference;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.management.ManagementException;
import weblogic.management.mbeans.custom.SecurityConfiguration;
import weblogic.management.security.ProviderMBean;
import weblogic.utils.ArrayUtils;
import weblogic.utils.collections.ArrayIterator;
import weblogic.utils.collections.CombinedIterator;

public class SecurityConfigurationMBeanImpl extends ConfigurationMBeanImpl implements SecurityConfigurationMBean, Serializable {
   private boolean _AnonymousAdminLookupEnabled;
   private CertRevocMBean _CertRevoc;
   private boolean _ClearTextCredentialAccessEnabled;
   private boolean _CompatibilityConnectionFiltersEnabled;
   private String _ConnectionFilter;
   private String[] _ConnectionFilterRules;
   private boolean _ConnectionLoggerEnabled;
   private boolean _ConsoleFullDelegationEnabled;
   private String _Credential;
   private byte[] _CredentialEncrypted;
   private boolean _CredentialGenerated;
   private boolean _CrossDomainSecurityEnabled;
   private weblogic.management.security.RealmMBean _DefaultRealm;
   private weblogic.management.security.RealmMBean _DefaultRealmInternal;
   private boolean _DowngradeUntrustedPrincipals;
   private byte[] _EncryptedAESSecretKey;
   private byte[] _EncryptedSecretKey;
   private boolean _EnforceStrictURLPattern;
   private boolean _EnforceValidBasicAuthCredentials;
   private String[] _ExcludedDomainNames;
   private String _Name;
   private String _NodeManagerPassword;
   private byte[] _NodeManagerPasswordEncrypted;
   private String _NodeManagerUsername;
   private boolean _PrincipalEqualsCaseInsensitive;
   private boolean _PrincipalEqualsCompareDnAndGuid;
   private String _RealmBootStrapVersion;
   private weblogic.management.security.RealmMBean[] _Realms;
   private byte[] _Salt;
   private String _WebAppFilesCaseInsensitive;
   private SecurityConfiguration _customizer;
   private static SchemaHelper2 _schemaHelper;

   public SecurityConfigurationMBeanImpl() {
      try {
         this._customizer = new SecurityConfiguration(this);
      } catch (Exception var2) {
         if (var2 instanceof RuntimeException) {
            throw (RuntimeException)var2;
         }

         throw new UndeclaredThrowableException(var2);
      }

      this._initializeProperty(-1);
   }

   public SecurityConfigurationMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);

      try {
         this._customizer = new SecurityConfiguration(this);
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         }

         throw new UndeclaredThrowableException(var4);
      }

      this._initializeProperty(-1);
   }

   public weblogic.management.security.RealmMBean createRealm(String var1) throws JMException {
      return this._customizer.createRealm(var1);
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

   public boolean isNameSet() {
      return this._isSet(2);
   }

   public weblogic.management.security.RealmMBean createRealm() throws JMException {
      weblogic.management.security.RealmMBeanImpl var1 = new weblogic.management.security.RealmMBeanImpl(this, -1);

      try {
         this.addRealm(var1);
         return var1;
      } catch (Exception var3) {
         if (var3 instanceof RuntimeException) {
            throw (RuntimeException)var3;
         } else if (var3 instanceof JMException) {
            throw (JMException)var3;
         } else {
            throw new UndeclaredThrowableException(var3);
         }
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

   public void destroyRealm(weblogic.management.security.RealmMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 7);
         weblogic.management.security.RealmMBean[] var2 = this.getRealms();
         weblogic.management.security.RealmMBean[] var3 = (weblogic.management.security.RealmMBean[])((weblogic.management.security.RealmMBean[])this._getHelper()._removeElement(var2, weblogic.management.security.RealmMBean.class, var1));
         if (var2.length != var3.length) {
            this._preDestroy((AbstractDescriptorBean)var1);

            try {
               AbstractDescriptorBean var4 = (AbstractDescriptorBean)var1;
               if (var4 == null) {
                  return;
               }

               List var5 = this._getReferenceManager().getResolvedReferences(var4);
               if (var5 != null && var5.size() > 0) {
                  throw new BeanRemoveRejectedException(var4, var5);
               }

               this._getReferenceManager().unregisterBean(var4);
               this._markDestroyed(var4);
               this.setRealms(var3);
            } catch (Exception var6) {
               if (var6 instanceof RuntimeException) {
                  throw (RuntimeException)var6;
               }

               throw new UndeclaredThrowableException(var6);
            }
         }

      } catch (Exception var7) {
         if (var7 instanceof RuntimeException) {
            throw (RuntimeException)var7;
         } else {
            throw new UndeclaredThrowableException(var7);
         }
      }
   }

   public void addRealm(weblogic.management.security.RealmMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 7)) {
         weblogic.management.security.RealmMBean[] var2;
         if (this._isSet(7)) {
            var2 = (weblogic.management.security.RealmMBean[])((weblogic.management.security.RealmMBean[])this._getHelper()._extendArray(this.getRealms(), weblogic.management.security.RealmMBean.class, var1));
         } else {
            var2 = new weblogic.management.security.RealmMBean[]{var1};
         }

         try {
            this.setRealms(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public weblogic.management.security.RealmMBean[] getRealms() {
      return this._Realms;
   }

   public boolean isRealmsSet() {
      return this._isSet(7);
   }

   public void removeRealm(weblogic.management.security.RealmMBean var1) {
      this.destroyRealm(var1);
   }

   public void setRealms(weblogic.management.security.RealmMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new weblogic.management.security.RealmMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 7)) {
            this._getReferenceManager().registerBean(var3, true);
            this._postCreate(var3);
         }
      }

      weblogic.management.security.RealmMBean[] var5 = this._Realms;
      this._Realms = (weblogic.management.security.RealmMBean[])var4;
      this._postSet(7, var5, var4);
   }

   public weblogic.management.security.RealmMBean lookupRealm(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._Realms).iterator();

      weblogic.management.security.RealmMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (weblogic.management.security.RealmMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public weblogic.management.security.RealmMBean[] findRealms() {
      return this._customizer.findRealms();
   }

   public weblogic.management.security.RealmMBean findDefaultRealm() {
      return this._customizer.findDefaultRealm();
   }

   public weblogic.management.security.RealmMBean findRealm(String var1) {
      return this._customizer.findRealm(var1);
   }

   public weblogic.management.security.RealmMBean getDefaultRealm() {
      return this._DefaultRealm;
   }

   public String getDefaultRealmAsString() {
      AbstractDescriptorBean var1 = (AbstractDescriptorBean)this.getDefaultRealm();
      return var1 == null ? null : var1._getKey().toString();
   }

   public boolean isDefaultRealmSet() {
      return this._isSet(8);
   }

   public void setDefaultRealmAsString(String var1) {
      if (var1 != null && var1.length() != 0) {
         var1 = var1 == null ? null : var1.trim();
         this._getReferenceManager().registerUnresolvedReference(var1, weblogic.management.security.RealmMBean.class, new ReferenceManager.Resolver(this, 8) {
            public void resolveReference(Object var1) {
               try {
                  SecurityConfigurationMBeanImpl.this.setDefaultRealm((weblogic.management.security.RealmMBean)var1);
               } catch (RuntimeException var3) {
                  throw var3;
               } catch (Exception var4) {
                  throw new AssertionError("Impossible exception: " + var4);
               }
            }
         });
      } else {
         weblogic.management.security.RealmMBean var2 = this._DefaultRealm;
         this._initializeProperty(8);
         this._postSet(8, var2, this._DefaultRealm);
      }

   }

   public void setDefaultRealm(weblogic.management.security.RealmMBean var1) throws InvalidAttributeValueException {
      if (var1 != null) {
         ResolvedReference var2 = new ResolvedReference(this, 8, (AbstractDescriptorBean)var1) {
            protected Object getPropertyValue() {
               return SecurityConfigurationMBeanImpl.this.getDefaultRealm();
            }
         };
         this._getReferenceManager().registerResolvedReference((AbstractDescriptorBean)var1, var2);
      }

      weblogic.management.security.RealmMBean var3 = this._DefaultRealm;
      this._DefaultRealm = var1;
      this._postSet(8, var3, var1);
   }

   public ProviderMBean[] pre90getProviders() {
      return this._customizer.pre90getProviders();
   }

   public void touch() throws ConfigurationException {
      this._customizer.touch();
   }

   public void freezeCurrentValue(String var1) throws AttributeNotFoundException, MBeanException {
      this._customizer.freezeCurrentValue(var1);
   }

   public byte[] getSalt() {
      return this._customizer.getSalt();
   }

   public boolean isSaltSet() {
      return this._isSet(9);
   }

   public void setSalt(byte[] var1) throws InvalidAttributeValueException {
      this._Salt = var1;
   }

   public byte[] getEncryptedSecretKey() {
      return this._customizer.getEncryptedSecretKey();
   }

   public boolean isEncryptedSecretKeySet() {
      return this._isSet(10);
   }

   public void restoreDefaultValue(String var1) throws AttributeNotFoundException {
      this._customizer.restoreDefaultValue(var1);
   }

   public void setEncryptedSecretKey(byte[] var1) throws InvalidAttributeValueException {
      this._EncryptedSecretKey = var1;
   }

   public boolean isAnonymousAdminLookupEnabled() {
      return this._AnonymousAdminLookupEnabled;
   }

   public boolean isAnonymousAdminLookupEnabledSet() {
      return this._isSet(11);
   }

   public void setAnonymousAdminLookupEnabled(boolean var1) {
      boolean var2 = this._AnonymousAdminLookupEnabled;
      this._AnonymousAdminLookupEnabled = var1;
      this._postSet(11, var2, var1);
   }

   public boolean isClearTextCredentialAccessEnabled() {
      return this._ClearTextCredentialAccessEnabled;
   }

   public boolean isClearTextCredentialAccessEnabledSet() {
      return this._isSet(12);
   }

   public void setClearTextCredentialAccessEnabled(boolean var1) {
      boolean var2 = this._ClearTextCredentialAccessEnabled;
      this._ClearTextCredentialAccessEnabled = var1;
      this._postSet(12, var2, var1);
   }

   public boolean isCredentialGenerated() {
      return this._CredentialGenerated;
   }

   public boolean isCredentialGeneratedSet() {
      return this._isSet(13);
   }

   public void setCredentialGenerated(boolean var1) {
      boolean var2 = this.isCredentialGenerated();
      this._customizer.setCredentialGenerated(var1);
      this._postSet(13, var2, var1);
   }

   public byte[] generateCredential() {
      return this._customizer.generateCredential();
   }

   public String getCredential() {
      byte[] var1 = this.getCredentialEncrypted();
      return var1 == null ? null : this._decrypt("Credential", var1);
   }

   public boolean isCredentialSet() {
      return this.isCredentialEncryptedSet();
   }

   public void setCredential(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      this.setCredentialEncrypted(var1 == null ? null : this._encrypt("Credential", var1));
   }

   public byte[] getCredentialEncrypted() {
      return this._getHelper()._cloneArray(this._CredentialEncrypted);
   }

   public String getCredentialEncryptedAsString() {
      byte[] var1 = this.getCredentialEncrypted();
      return var1 == null ? null : new String(var1);
   }

   public boolean isCredentialEncryptedSet() {
      return this._isSet(15);
   }

   public void setCredentialEncryptedAsString(String var1) {
      try {
         byte[] var2 = var1 == null ? null : var1.getBytes();
         this.setCredentialEncrypted(var2);
      } catch (Exception var3) {
         if (var3 instanceof RuntimeException) {
            throw (RuntimeException)var3;
         } else {
            throw new UndeclaredThrowableException(var3);
         }
      }
   }

   public String getWebAppFilesCaseInsensitive() {
      return this._WebAppFilesCaseInsensitive;
   }

   public boolean isWebAppFilesCaseInsensitiveSet() {
      return this._isSet(16);
   }

   public void setWebAppFilesCaseInsensitive(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"os", "true", "false"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("WebAppFilesCaseInsensitive", var1, var2);
      String var3 = this._WebAppFilesCaseInsensitive;
      this._WebAppFilesCaseInsensitive = var1;
      this._postSet(16, var3, var1);
   }

   public String getRealmBootStrapVersion() {
      if (!this._isSet(17)) {
         try {
            return "1";
         } catch (NullPointerException var2) {
         }
      }

      return this._RealmBootStrapVersion;
   }

   public boolean isRealmBootStrapVersionSet() {
      return this._isSet(17);
   }

   public void setRealmBootStrapVersion(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"unknown", "1"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("RealmBootStrapVersion", var1, var2);
      String var3 = this._RealmBootStrapVersion;
      this._RealmBootStrapVersion = var1;
      this._postSet(17, var3, var1);
   }

   public String getConnectionFilter() {
      return this._ConnectionFilter;
   }

   public boolean isConnectionFilterSet() {
      return this._isSet(18);
   }

   public void setConnectionFilter(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._ConnectionFilter;
      this._ConnectionFilter = var1;
      this._postSet(18, var2, var1);
   }

   public String[] getConnectionFilterRules() {
      return this._ConnectionFilterRules;
   }

   public boolean isConnectionFilterRulesSet() {
      return this._isSet(19);
   }

   public void setConnectionFilterRules(String[] var1) {
      var1 = var1 == null ? new String[0] : var1;
      this._getHelper()._ensureNonNullElements(var1);
      String[] var2 = this._ConnectionFilterRules;
      this._ConnectionFilterRules = var1;
      this._postSet(19, var2, var1);
   }

   public boolean getConnectionLoggerEnabled() {
      return this._ConnectionLoggerEnabled;
   }

   public boolean isConnectionLoggerEnabledSet() {
      return this._isSet(20);
   }

   public void setConnectionLoggerEnabled(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this._ConnectionLoggerEnabled;
      this._ConnectionLoggerEnabled = var1;
      this._postSet(20, var2, var1);
   }

   public boolean getCompatibilityConnectionFiltersEnabled() {
      return this._CompatibilityConnectionFiltersEnabled;
   }

   public boolean isCompatibilityConnectionFiltersEnabledSet() {
      return this._isSet(21);
   }

   public void setCompatibilityConnectionFiltersEnabled(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this._CompatibilityConnectionFiltersEnabled;
      this._CompatibilityConnectionFiltersEnabled = var1;
      this._postSet(21, var2, var1);
   }

   public String getNodeManagerUsername() {
      return this._NodeManagerUsername;
   }

   public boolean isNodeManagerUsernameSet() {
      return this._isSet(22);
   }

   public void setNodeManagerUsername(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._NodeManagerUsername;
      this._NodeManagerUsername = var1;
      this._postSet(22, var2, var1);
   }

   public String getNodeManagerPassword() {
      byte[] var1 = this.getNodeManagerPasswordEncrypted();
      return var1 == null ? null : this._decrypt("NodeManagerPassword", var1);
   }

   public boolean isNodeManagerPasswordSet() {
      return this.isNodeManagerPasswordEncryptedSet();
   }

   public void setNodeManagerPassword(String var1) {
      var1 = var1 == null ? null : var1.trim();
      this.setNodeManagerPasswordEncrypted(var1 == null ? null : this._encrypt("NodeManagerPassword", var1));
   }

   public byte[] getNodeManagerPasswordEncrypted() {
      return this._getHelper()._cloneArray(this._NodeManagerPasswordEncrypted);
   }

   public String getNodeManagerPasswordEncryptedAsString() {
      byte[] var1 = this.getNodeManagerPasswordEncrypted();
      return var1 == null ? null : new String(var1);
   }

   public boolean isNodeManagerPasswordEncryptedSet() {
      return this._isSet(24);
   }

   public void setNodeManagerPasswordEncryptedAsString(String var1) {
      try {
         byte[] var2 = var1 == null ? null : var1.getBytes();
         this.setNodeManagerPasswordEncrypted(var2);
      } catch (Exception var3) {
         if (var3 instanceof RuntimeException) {
            throw (RuntimeException)var3;
         } else {
            throw new UndeclaredThrowableException(var3);
         }
      }
   }

   public boolean isPrincipalEqualsCaseInsensitive() {
      return this._PrincipalEqualsCaseInsensitive;
   }

   public boolean isPrincipalEqualsCaseInsensitiveSet() {
      return this._isSet(25);
   }

   public void setPrincipalEqualsCaseInsensitive(boolean var1) {
      boolean var2 = this._PrincipalEqualsCaseInsensitive;
      this._PrincipalEqualsCaseInsensitive = var1;
      this._postSet(25, var2, var1);
   }

   public boolean isPrincipalEqualsCompareDnAndGuid() {
      return this._PrincipalEqualsCompareDnAndGuid;
   }

   public boolean isPrincipalEqualsCompareDnAndGuidSet() {
      return this._isSet(26);
   }

   public void setPrincipalEqualsCompareDnAndGuid(boolean var1) {
      boolean var2 = this._PrincipalEqualsCompareDnAndGuid;
      this._PrincipalEqualsCompareDnAndGuid = var1;
      this._postSet(26, var2, var1);
   }

   public boolean getDowngradeUntrustedPrincipals() {
      return this._DowngradeUntrustedPrincipals;
   }

   public boolean isDowngradeUntrustedPrincipalsSet() {
      return this._isSet(27);
   }

   public void setDowngradeUntrustedPrincipals(boolean var1) {
      boolean var2 = this._DowngradeUntrustedPrincipals;
      this._DowngradeUntrustedPrincipals = var1;
      this._postSet(27, var2, var1);
   }

   public boolean getEnforceStrictURLPattern() {
      return this._EnforceStrictURLPattern;
   }

   public boolean isEnforceStrictURLPatternSet() {
      return this._isSet(28);
   }

   public void setEnforceStrictURLPattern(boolean var1) {
      boolean var2 = this._EnforceStrictURLPattern;
      this._EnforceStrictURLPattern = var1;
      this._postSet(28, var2, var1);
   }

   public boolean getEnforceValidBasicAuthCredentials() {
      return this._EnforceValidBasicAuthCredentials;
   }

   public boolean isEnforceValidBasicAuthCredentialsSet() {
      return this._isSet(29);
   }

   public void setEnforceValidBasicAuthCredentials(boolean var1) {
      boolean var2 = this._EnforceValidBasicAuthCredentials;
      this._EnforceValidBasicAuthCredentials = var1;
      this._postSet(29, var2, var1);
   }

   public boolean isConsoleFullDelegationEnabled() {
      return this._ConsoleFullDelegationEnabled;
   }

   public boolean isConsoleFullDelegationEnabledSet() {
      return this._isSet(30);
   }

   public void setConsoleFullDelegationEnabled(boolean var1) {
      boolean var2 = this._ConsoleFullDelegationEnabled;
      this._ConsoleFullDelegationEnabled = var1;
      this._postSet(30, var2, var1);
   }

   public weblogic.management.security.RealmMBean getDefaultRealmInternal() {
      return this._customizer.getDefaultRealmInternal();
   }

   public String getDefaultRealmInternalAsString() {
      AbstractDescriptorBean var1 = (AbstractDescriptorBean)this.getDefaultRealmInternal();
      return var1 == null ? null : var1._getKey().toString();
   }

   public boolean isDefaultRealmInternalSet() {
      return this._isSet(31);
   }

   public void setDefaultRealmInternalAsString(String var1) {
      if (var1 != null && var1.length() != 0) {
         var1 = var1 == null ? null : var1.trim();
         this._getReferenceManager().registerUnresolvedReference(var1, weblogic.management.security.RealmMBean.class, new ReferenceManager.Resolver(this, 31) {
            public void resolveReference(Object var1) {
               try {
                  SecurityConfigurationMBeanImpl.this.setDefaultRealmInternal((weblogic.management.security.RealmMBean)var1);
               } catch (RuntimeException var3) {
                  throw var3;
               } catch (Exception var4) {
                  throw new AssertionError("Impossible exception: " + var4);
               }
            }
         });
      } else {
         weblogic.management.security.RealmMBean var2 = this._DefaultRealmInternal;
         this._initializeProperty(31);
         this._postSet(31, var2, this._DefaultRealmInternal);
      }

   }

   public void setDefaultRealmInternal(weblogic.management.security.RealmMBean var1) {
      if (var1 != null) {
         ResolvedReference var2 = new ResolvedReference(this, 31, (AbstractDescriptorBean)var1) {
            protected Object getPropertyValue() {
               return SecurityConfigurationMBeanImpl.this.getDefaultRealmInternal();
            }
         };
         this._getReferenceManager().registerResolvedReference((AbstractDescriptorBean)var1, var2);
      }

      weblogic.management.security.RealmMBean var3 = this.getDefaultRealmInternal();
      this._customizer.setDefaultRealmInternal(var1);
      this._postSet(31, var3, var1);
   }

   public String[] getExcludedDomainNames() {
      return this._ExcludedDomainNames;
   }

   public boolean isExcludedDomainNamesSet() {
      return this._isSet(32);
   }

   public void setExcludedDomainNames(String[] var1) {
      var1 = var1 == null ? new String[0] : var1;
      var1 = this._getHelper()._trimElements(var1);
      String[] var2 = this._ExcludedDomainNames;
      this._ExcludedDomainNames = var1;
      this._postSet(32, var2, var1);
   }

   public boolean isCrossDomainSecurityEnabled() {
      return this._CrossDomainSecurityEnabled;
   }

   public boolean isCrossDomainSecurityEnabledSet() {
      return this._isSet(33);
   }

   public void setCrossDomainSecurityEnabled(boolean var1) {
      boolean var2 = this._CrossDomainSecurityEnabled;
      this._CrossDomainSecurityEnabled = var1;
      this._postSet(33, var2, var1);
   }

   public byte[] getEncryptedAESSecretKey() {
      return this._customizer.getEncryptedAESSecretKey();
   }

   public boolean isEncryptedAESSecretKeySet() {
      return this._isSet(34);
   }

   public void setEncryptedAESSecretKey(byte[] var1) throws InvalidAttributeValueException {
      this._EncryptedAESSecretKey = var1;
   }

   public CertRevocMBean getCertRevoc() {
      return this._CertRevoc;
   }

   public boolean isCertRevocSet() {
      return this._isSet(35) || this._isAnythingSet((AbstractDescriptorBean)this.getCertRevoc());
   }

   public void setCertRevoc(CertRevocMBean var1) throws InvalidAttributeValueException {
      AbstractDescriptorBean var2 = (AbstractDescriptorBean)var1;
      if (this._setParent(var2, this, 35)) {
         this._postCreate(var2);
      }

      CertRevocMBean var3 = this._CertRevoc;
      this._CertRevoc = var1;
      this._postSet(35, var3, var1);
   }

   public Object _getKey() {
      return this.getName();
   }

   public void _validate() throws IllegalArgumentException {
      super._validate();
      SecurityLegalHelper.validateSecurityConfiguration(this);
   }

   public void setCredentialEncrypted(byte[] var1) {
      byte[] var2 = this._CredentialEncrypted;
      if (this._isProductionModeEnabled() && var1 != null && !this._isEncrypted(var1)) {
         throw new IllegalArgumentException("In production mode, it's not allowed to set a clear text value to the property: CredentialEncrypted of SecurityConfigurationMBean");
      } else {
         this._getHelper()._clearArray(this._CredentialEncrypted);
         this._CredentialEncrypted = this._getHelper()._cloneArray(var1);
         this._postSet(15, var2, var1);
      }
   }

   public void setNodeManagerPasswordEncrypted(byte[] var1) {
      byte[] var2 = this._NodeManagerPasswordEncrypted;
      if (this._isProductionModeEnabled() && var1 != null && !this._isEncrypted(var1)) {
         throw new IllegalArgumentException("In production mode, it's not allowed to set a clear text value to the property: NodeManagerPasswordEncrypted of SecurityConfigurationMBean");
      } else {
         this._getHelper()._clearArray(this._NodeManagerPasswordEncrypted);
         this._NodeManagerPasswordEncrypted = this._getHelper()._cloneArray(var1);
         this._postSet(24, var2, var1);
      }
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
         if (var1 == 14) {
            this._markSet(15, false);
         }

         if (var1 == 23) {
            this._markSet(24, false);
         }
      }

   }

   protected AbstractDescriptorBeanHelper _createHelper() {
      return new Helper(this);
   }

   public boolean _isAnythingSet() {
      return super._isAnythingSet() || this.isCertRevocSet();
   }

   private boolean _initializeProperty(int var1) {
      boolean var2 = var1 > -1;
      if (!var2) {
         var1 = 35;
      }

      try {
         switch (var1) {
            case 35:
               this._CertRevoc = new CertRevocMBeanImpl(this, 35);
               this._postCreate((AbstractDescriptorBean)this._CertRevoc);
               if (var2) {
                  break;
               }
            case 21:
               this._CompatibilityConnectionFiltersEnabled = false;
               if (var2) {
                  break;
               }
            case 18:
               this._ConnectionFilter = null;
               if (var2) {
                  break;
               }
            case 19:
               this._ConnectionFilterRules = new String[0];
               if (var2) {
                  break;
               }
            case 20:
               this._ConnectionLoggerEnabled = false;
               if (var2) {
                  break;
               }
            case 14:
               this._CredentialEncrypted = null;
               if (var2) {
                  break;
               }
            case 15:
               this._CredentialEncrypted = null;
               if (var2) {
                  break;
               }
            case 8:
               this._DefaultRealm = null;
               if (var2) {
                  break;
               }
            case 31:
               this._customizer.setDefaultRealmInternal((weblogic.management.security.RealmMBean)null);
               if (var2) {
                  break;
               }
            case 27:
               this._DowngradeUntrustedPrincipals = false;
               if (var2) {
                  break;
               }
            case 34:
               this._EncryptedAESSecretKey = new byte[0];
               if (var2) {
                  break;
               }
            case 10:
               this._EncryptedSecretKey = new byte[0];
               if (var2) {
                  break;
               }
            case 28:
               this._EnforceStrictURLPattern = true;
               if (var2) {
                  break;
               }
            case 29:
               this._EnforceValidBasicAuthCredentials = true;
               if (var2) {
                  break;
               }
            case 32:
               this._ExcludedDomainNames = new String[0];
               if (var2) {
                  break;
               }
            case 2:
               this._customizer.setName((String)null);
               if (var2) {
                  break;
               }
            case 23:
               this._NodeManagerPasswordEncrypted = "".getBytes();
               if (var2) {
                  break;
               }
            case 24:
               this._NodeManagerPasswordEncrypted = "".getBytes();
               if (var2) {
                  break;
               }
            case 22:
               this._NodeManagerUsername = "";
               if (var2) {
                  break;
               }
            case 17:
               this._RealmBootStrapVersion = null;
               if (var2) {
                  break;
               }
            case 7:
               this._Realms = new weblogic.management.security.RealmMBean[0];
               if (var2) {
                  break;
               }
            case 9:
               this._Salt = new byte[0];
               if (var2) {
                  break;
               }
            case 16:
               this._WebAppFilesCaseInsensitive = "false";
               if (var2) {
                  break;
               }
            case 11:
               this._AnonymousAdminLookupEnabled = false;
               if (var2) {
                  break;
               }
            case 12:
               this._ClearTextCredentialAccessEnabled = false;
               if (var2) {
                  break;
               }
            case 30:
               this._ConsoleFullDelegationEnabled = false;
               if (var2) {
                  break;
               }
            case 13:
               this._customizer.setCredentialGenerated(true);
               if (var2) {
                  break;
               }
            case 33:
               this._CrossDomainSecurityEnabled = false;
               if (var2) {
                  break;
               }
            case 25:
               this._PrincipalEqualsCaseInsensitive = false;
               if (var2) {
                  break;
               }
            case 26:
               this._PrincipalEqualsCompareDnAndGuid = false;
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
      return "SecurityConfiguration";
   }

   public void putValue(String var1, Object var2) {
      boolean var7;
      if (var1.equals("AnonymousAdminLookupEnabled")) {
         var7 = this._AnonymousAdminLookupEnabled;
         this._AnonymousAdminLookupEnabled = (Boolean)var2;
         this._postSet(11, var7, this._AnonymousAdminLookupEnabled);
      } else if (var1.equals("CertRevoc")) {
         CertRevocMBean var10 = this._CertRevoc;
         this._CertRevoc = (CertRevocMBean)var2;
         this._postSet(35, var10, this._CertRevoc);
      } else if (var1.equals("ClearTextCredentialAccessEnabled")) {
         var7 = this._ClearTextCredentialAccessEnabled;
         this._ClearTextCredentialAccessEnabled = (Boolean)var2;
         this._postSet(12, var7, this._ClearTextCredentialAccessEnabled);
      } else if (var1.equals("CompatibilityConnectionFiltersEnabled")) {
         var7 = this._CompatibilityConnectionFiltersEnabled;
         this._CompatibilityConnectionFiltersEnabled = (Boolean)var2;
         this._postSet(21, var7, this._CompatibilityConnectionFiltersEnabled);
      } else {
         String var4;
         if (var1.equals("ConnectionFilter")) {
            var4 = this._ConnectionFilter;
            this._ConnectionFilter = (String)var2;
            this._postSet(18, var4, this._ConnectionFilter);
         } else {
            String[] var8;
            if (var1.equals("ConnectionFilterRules")) {
               var8 = this._ConnectionFilterRules;
               this._ConnectionFilterRules = (String[])((String[])var2);
               this._postSet(19, var8, this._ConnectionFilterRules);
            } else if (var1.equals("ConnectionLoggerEnabled")) {
               var7 = this._ConnectionLoggerEnabled;
               this._ConnectionLoggerEnabled = (Boolean)var2;
               this._postSet(20, var7, this._ConnectionLoggerEnabled);
            } else if (var1.equals("ConsoleFullDelegationEnabled")) {
               var7 = this._ConsoleFullDelegationEnabled;
               this._ConsoleFullDelegationEnabled = (Boolean)var2;
               this._postSet(30, var7, this._ConsoleFullDelegationEnabled);
            } else if (var1.equals("Credential")) {
               var4 = this._Credential;
               this._Credential = (String)var2;
               this._postSet(14, var4, this._Credential);
            } else {
               byte[] var5;
               if (var1.equals("CredentialEncrypted")) {
                  var5 = this._CredentialEncrypted;
                  this._CredentialEncrypted = (byte[])((byte[])var2);
                  this._postSet(15, var5, this._CredentialEncrypted);
               } else if (var1.equals("CredentialGenerated")) {
                  var7 = this._CredentialGenerated;
                  this._CredentialGenerated = (Boolean)var2;
                  this._postSet(13, var7, this._CredentialGenerated);
               } else if (var1.equals("CrossDomainSecurityEnabled")) {
                  var7 = this._CrossDomainSecurityEnabled;
                  this._CrossDomainSecurityEnabled = (Boolean)var2;
                  this._postSet(33, var7, this._CrossDomainSecurityEnabled);
               } else {
                  weblogic.management.security.RealmMBean var9;
                  if (var1.equals("DefaultRealm")) {
                     var9 = this._DefaultRealm;
                     this._DefaultRealm = (weblogic.management.security.RealmMBean)var2;
                     this._postSet(8, var9, this._DefaultRealm);
                  } else if (var1.equals("DefaultRealmInternal")) {
                     var9 = this._DefaultRealmInternal;
                     this._DefaultRealmInternal = (weblogic.management.security.RealmMBean)var2;
                     this._postSet(31, var9, this._DefaultRealmInternal);
                  } else if (var1.equals("DowngradeUntrustedPrincipals")) {
                     var7 = this._DowngradeUntrustedPrincipals;
                     this._DowngradeUntrustedPrincipals = (Boolean)var2;
                     this._postSet(27, var7, this._DowngradeUntrustedPrincipals);
                  } else if (var1.equals("EncryptedAESSecretKey")) {
                     var5 = this._EncryptedAESSecretKey;
                     this._EncryptedAESSecretKey = (byte[])((byte[])var2);
                     this._postSet(34, var5, this._EncryptedAESSecretKey);
                  } else if (var1.equals("EncryptedSecretKey")) {
                     var5 = this._EncryptedSecretKey;
                     this._EncryptedSecretKey = (byte[])((byte[])var2);
                     this._postSet(10, var5, this._EncryptedSecretKey);
                  } else if (var1.equals("EnforceStrictURLPattern")) {
                     var7 = this._EnforceStrictURLPattern;
                     this._EnforceStrictURLPattern = (Boolean)var2;
                     this._postSet(28, var7, this._EnforceStrictURLPattern);
                  } else if (var1.equals("EnforceValidBasicAuthCredentials")) {
                     var7 = this._EnforceValidBasicAuthCredentials;
                     this._EnforceValidBasicAuthCredentials = (Boolean)var2;
                     this._postSet(29, var7, this._EnforceValidBasicAuthCredentials);
                  } else if (var1.equals("ExcludedDomainNames")) {
                     var8 = this._ExcludedDomainNames;
                     this._ExcludedDomainNames = (String[])((String[])var2);
                     this._postSet(32, var8, this._ExcludedDomainNames);
                  } else if (var1.equals("Name")) {
                     var4 = this._Name;
                     this._Name = (String)var2;
                     this._postSet(2, var4, this._Name);
                  } else if (var1.equals("NodeManagerPassword")) {
                     var4 = this._NodeManagerPassword;
                     this._NodeManagerPassword = (String)var2;
                     this._postSet(23, var4, this._NodeManagerPassword);
                  } else if (var1.equals("NodeManagerPasswordEncrypted")) {
                     var5 = this._NodeManagerPasswordEncrypted;
                     this._NodeManagerPasswordEncrypted = (byte[])((byte[])var2);
                     this._postSet(24, var5, this._NodeManagerPasswordEncrypted);
                  } else if (var1.equals("NodeManagerUsername")) {
                     var4 = this._NodeManagerUsername;
                     this._NodeManagerUsername = (String)var2;
                     this._postSet(22, var4, this._NodeManagerUsername);
                  } else if (var1.equals("PrincipalEqualsCaseInsensitive")) {
                     var7 = this._PrincipalEqualsCaseInsensitive;
                     this._PrincipalEqualsCaseInsensitive = (Boolean)var2;
                     this._postSet(25, var7, this._PrincipalEqualsCaseInsensitive);
                  } else if (var1.equals("PrincipalEqualsCompareDnAndGuid")) {
                     var7 = this._PrincipalEqualsCompareDnAndGuid;
                     this._PrincipalEqualsCompareDnAndGuid = (Boolean)var2;
                     this._postSet(26, var7, this._PrincipalEqualsCompareDnAndGuid);
                  } else if (var1.equals("RealmBootStrapVersion")) {
                     var4 = this._RealmBootStrapVersion;
                     this._RealmBootStrapVersion = (String)var2;
                     this._postSet(17, var4, this._RealmBootStrapVersion);
                  } else if (var1.equals("Realms")) {
                     weblogic.management.security.RealmMBean[] var6 = this._Realms;
                     this._Realms = (weblogic.management.security.RealmMBean[])((weblogic.management.security.RealmMBean[])var2);
                     this._postSet(7, var6, this._Realms);
                  } else if (var1.equals("Salt")) {
                     var5 = this._Salt;
                     this._Salt = (byte[])((byte[])var2);
                     this._postSet(9, var5, this._Salt);
                  } else if (var1.equals("WebAppFilesCaseInsensitive")) {
                     var4 = this._WebAppFilesCaseInsensitive;
                     this._WebAppFilesCaseInsensitive = (String)var2;
                     this._postSet(16, var4, this._WebAppFilesCaseInsensitive);
                  } else if (var1.equals("customizer")) {
                     SecurityConfiguration var3 = this._customizer;
                     this._customizer = (SecurityConfiguration)var2;
                  } else {
                     super.putValue(var1, var2);
                  }
               }
            }
         }
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("AnonymousAdminLookupEnabled")) {
         return new Boolean(this._AnonymousAdminLookupEnabled);
      } else if (var1.equals("CertRevoc")) {
         return this._CertRevoc;
      } else if (var1.equals("ClearTextCredentialAccessEnabled")) {
         return new Boolean(this._ClearTextCredentialAccessEnabled);
      } else if (var1.equals("CompatibilityConnectionFiltersEnabled")) {
         return new Boolean(this._CompatibilityConnectionFiltersEnabled);
      } else if (var1.equals("ConnectionFilter")) {
         return this._ConnectionFilter;
      } else if (var1.equals("ConnectionFilterRules")) {
         return this._ConnectionFilterRules;
      } else if (var1.equals("ConnectionLoggerEnabled")) {
         return new Boolean(this._ConnectionLoggerEnabled);
      } else if (var1.equals("ConsoleFullDelegationEnabled")) {
         return new Boolean(this._ConsoleFullDelegationEnabled);
      } else if (var1.equals("Credential")) {
         return this._Credential;
      } else if (var1.equals("CredentialEncrypted")) {
         return this._CredentialEncrypted;
      } else if (var1.equals("CredentialGenerated")) {
         return new Boolean(this._CredentialGenerated);
      } else if (var1.equals("CrossDomainSecurityEnabled")) {
         return new Boolean(this._CrossDomainSecurityEnabled);
      } else if (var1.equals("DefaultRealm")) {
         return this._DefaultRealm;
      } else if (var1.equals("DefaultRealmInternal")) {
         return this._DefaultRealmInternal;
      } else if (var1.equals("DowngradeUntrustedPrincipals")) {
         return new Boolean(this._DowngradeUntrustedPrincipals);
      } else if (var1.equals("EncryptedAESSecretKey")) {
         return this._EncryptedAESSecretKey;
      } else if (var1.equals("EncryptedSecretKey")) {
         return this._EncryptedSecretKey;
      } else if (var1.equals("EnforceStrictURLPattern")) {
         return new Boolean(this._EnforceStrictURLPattern);
      } else if (var1.equals("EnforceValidBasicAuthCredentials")) {
         return new Boolean(this._EnforceValidBasicAuthCredentials);
      } else if (var1.equals("ExcludedDomainNames")) {
         return this._ExcludedDomainNames;
      } else if (var1.equals("Name")) {
         return this._Name;
      } else if (var1.equals("NodeManagerPassword")) {
         return this._NodeManagerPassword;
      } else if (var1.equals("NodeManagerPasswordEncrypted")) {
         return this._NodeManagerPasswordEncrypted;
      } else if (var1.equals("NodeManagerUsername")) {
         return this._NodeManagerUsername;
      } else if (var1.equals("PrincipalEqualsCaseInsensitive")) {
         return new Boolean(this._PrincipalEqualsCaseInsensitive);
      } else if (var1.equals("PrincipalEqualsCompareDnAndGuid")) {
         return new Boolean(this._PrincipalEqualsCompareDnAndGuid);
      } else if (var1.equals("RealmBootStrapVersion")) {
         return this._RealmBootStrapVersion;
      } else if (var1.equals("Realms")) {
         return this._Realms;
      } else if (var1.equals("Salt")) {
         return this._Salt;
      } else if (var1.equals("WebAppFilesCaseInsensitive")) {
         return this._WebAppFilesCaseInsensitive;
      } else {
         return var1.equals("customizer") ? this._customizer : super.getValue(var1);
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
                  return 9;
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
            case 11:
            case 12:
            case 14:
            case 15:
            case 16:
            case 18:
            case 19:
            case 27:
            case 28:
            case 32:
            case 34:
            case 35:
            case 37:
            case 38:
            case 39:
            default:
               break;
            case 10:
               if (var1.equals("cert-revoc")) {
                  return 35;
               }

               if (var1.equals("credential")) {
                  return 14;
               }
               break;
            case 13:
               if (var1.equals("default-realm")) {
                  return 8;
               }
               break;
            case 17:
               if (var1.equals("connection-filter")) {
                  return 18;
               }
               break;
            case 20:
               if (var1.equals("credential-encrypted")) {
                  return 15;
               }

               if (var1.equals("encrypted-secret-key")) {
                  return 10;
               }

               if (var1.equals("excluded-domain-name")) {
                  return 32;
               }

               if (var1.equals("credential-generated")) {
                  return 13;
               }
               break;
            case 21:
               if (var1.equals("node-manager-password")) {
                  return 23;
               }

               if (var1.equals("node-manager-username")) {
                  return 22;
               }
               break;
            case 22:
               if (var1.equals("connection-filter-rule")) {
                  return 19;
               }

               if (var1.equals("default-realm-internal")) {
                  return 31;
               }
               break;
            case 23:
               if (var1.equals("encryptedaes-secret-key")) {
                  return 34;
               }
               break;
            case 24:
               if (var1.equals("realm-boot-strap-version")) {
                  return 17;
               }
               break;
            case 25:
               if (var1.equals("connection-logger-enabled")) {
                  return 20;
               }
               break;
            case 26:
               if (var1.equals("enforce-strict-url-pattern")) {
                  return 28;
               }
               break;
            case 29:
               if (var1.equals("cross-domain-security-enabled")) {
                  return 33;
               }
               break;
            case 30:
               if (var1.equals("downgrade-untrusted-principals")) {
                  return 27;
               }

               if (var1.equals("web-app-files-case-insensitive")) {
                  return 16;
               }

               if (var1.equals("anonymous-admin-lookup-enabled")) {
                  return 11;
               }
               break;
            case 31:
               if (var1.equals("node-manager-password-encrypted")) {
                  return 24;
               }

               if (var1.equals("console-full-delegation-enabled")) {
                  return 30;
               }
               break;
            case 33:
               if (var1.equals("principal-equals-case-insensitive")) {
                  return 25;
               }
               break;
            case 36:
               if (var1.equals("enforce-valid-basic-auth-credentials")) {
                  return 29;
               }

               if (var1.equals("clear-text-credential-access-enabled")) {
                  return 12;
               }

               if (var1.equals("principal-equals-compare-dn-and-guid")) {
                  return 26;
               }
               break;
            case 40:
               if (var1.equals("compatibility-connection-filters-enabled")) {
                  return 21;
               }
         }

         return super.getPropertyIndex(var1);
      }

      public SchemaHelper getSchemaHelper(int var1) {
         switch (var1) {
            case 7:
               return new weblogic.management.security.RealmMBeanImpl.SchemaHelper2();
            case 35:
               return new CertRevocMBeanImpl.SchemaHelper2();
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
               return "default-realm";
            case 9:
               return "salt";
            case 10:
               return "encrypted-secret-key";
            case 11:
               return "anonymous-admin-lookup-enabled";
            case 12:
               return "clear-text-credential-access-enabled";
            case 13:
               return "credential-generated";
            case 14:
               return "credential";
            case 15:
               return "credential-encrypted";
            case 16:
               return "web-app-files-case-insensitive";
            case 17:
               return "realm-boot-strap-version";
            case 18:
               return "connection-filter";
            case 19:
               return "connection-filter-rule";
            case 20:
               return "connection-logger-enabled";
            case 21:
               return "compatibility-connection-filters-enabled";
            case 22:
               return "node-manager-username";
            case 23:
               return "node-manager-password";
            case 24:
               return "node-manager-password-encrypted";
            case 25:
               return "principal-equals-case-insensitive";
            case 26:
               return "principal-equals-compare-dn-and-guid";
            case 27:
               return "downgrade-untrusted-principals";
            case 28:
               return "enforce-strict-url-pattern";
            case 29:
               return "enforce-valid-basic-auth-credentials";
            case 30:
               return "console-full-delegation-enabled";
            case 31:
               return "default-realm-internal";
            case 32:
               return "excluded-domain-name";
            case 33:
               return "cross-domain-security-enabled";
            case 34:
               return "encryptedaes-secret-key";
            case 35:
               return "cert-revoc";
         }
      }

      public boolean isArray(int var1) {
         switch (var1) {
            case 7:
               return true;
            case 9:
               return true;
            case 10:
               return true;
            case 19:
               return true;
            case 32:
               return true;
            case 34:
               return true;
            default:
               return super.isArray(var1);
         }
      }

      public boolean isBean(int var1) {
         switch (var1) {
            case 7:
               return true;
            case 35:
               return true;
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
      private SecurityConfigurationMBeanImpl bean;

      protected Helper(SecurityConfigurationMBeanImpl var1) {
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
               return "Realms";
            case 8:
               return "DefaultRealm";
            case 9:
               return "Salt";
            case 10:
               return "EncryptedSecretKey";
            case 11:
               return "AnonymousAdminLookupEnabled";
            case 12:
               return "ClearTextCredentialAccessEnabled";
            case 13:
               return "CredentialGenerated";
            case 14:
               return "Credential";
            case 15:
               return "CredentialEncrypted";
            case 16:
               return "WebAppFilesCaseInsensitive";
            case 17:
               return "RealmBootStrapVersion";
            case 18:
               return "ConnectionFilter";
            case 19:
               return "ConnectionFilterRules";
            case 20:
               return "ConnectionLoggerEnabled";
            case 21:
               return "CompatibilityConnectionFiltersEnabled";
            case 22:
               return "NodeManagerUsername";
            case 23:
               return "NodeManagerPassword";
            case 24:
               return "NodeManagerPasswordEncrypted";
            case 25:
               return "PrincipalEqualsCaseInsensitive";
            case 26:
               return "PrincipalEqualsCompareDnAndGuid";
            case 27:
               return "DowngradeUntrustedPrincipals";
            case 28:
               return "EnforceStrictURLPattern";
            case 29:
               return "EnforceValidBasicAuthCredentials";
            case 30:
               return "ConsoleFullDelegationEnabled";
            case 31:
               return "DefaultRealmInternal";
            case 32:
               return "ExcludedDomainNames";
            case 33:
               return "CrossDomainSecurityEnabled";
            case 34:
               return "EncryptedAESSecretKey";
            case 35:
               return "CertRevoc";
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("CertRevoc")) {
            return 35;
         } else if (var1.equals("CompatibilityConnectionFiltersEnabled")) {
            return 21;
         } else if (var1.equals("ConnectionFilter")) {
            return 18;
         } else if (var1.equals("ConnectionFilterRules")) {
            return 19;
         } else if (var1.equals("ConnectionLoggerEnabled")) {
            return 20;
         } else if (var1.equals("Credential")) {
            return 14;
         } else if (var1.equals("CredentialEncrypted")) {
            return 15;
         } else if (var1.equals("DefaultRealm")) {
            return 8;
         } else if (var1.equals("DefaultRealmInternal")) {
            return 31;
         } else if (var1.equals("DowngradeUntrustedPrincipals")) {
            return 27;
         } else if (var1.equals("EncryptedAESSecretKey")) {
            return 34;
         } else if (var1.equals("EncryptedSecretKey")) {
            return 10;
         } else if (var1.equals("EnforceStrictURLPattern")) {
            return 28;
         } else if (var1.equals("EnforceValidBasicAuthCredentials")) {
            return 29;
         } else if (var1.equals("ExcludedDomainNames")) {
            return 32;
         } else if (var1.equals("Name")) {
            return 2;
         } else if (var1.equals("NodeManagerPassword")) {
            return 23;
         } else if (var1.equals("NodeManagerPasswordEncrypted")) {
            return 24;
         } else if (var1.equals("NodeManagerUsername")) {
            return 22;
         } else if (var1.equals("RealmBootStrapVersion")) {
            return 17;
         } else if (var1.equals("Realms")) {
            return 7;
         } else if (var1.equals("Salt")) {
            return 9;
         } else if (var1.equals("WebAppFilesCaseInsensitive")) {
            return 16;
         } else if (var1.equals("AnonymousAdminLookupEnabled")) {
            return 11;
         } else if (var1.equals("ClearTextCredentialAccessEnabled")) {
            return 12;
         } else if (var1.equals("ConsoleFullDelegationEnabled")) {
            return 30;
         } else if (var1.equals("CredentialGenerated")) {
            return 13;
         } else if (var1.equals("CrossDomainSecurityEnabled")) {
            return 33;
         } else if (var1.equals("PrincipalEqualsCaseInsensitive")) {
            return 25;
         } else {
            return var1.equals("PrincipalEqualsCompareDnAndGuid") ? 26 : super.getPropertyIndex(var1);
         }
      }

      public Iterator getChildren() {
         ArrayList var1 = new ArrayList();
         if (this.bean.getCertRevoc() != null) {
            var1.add(new ArrayIterator(new CertRevocMBean[]{this.bean.getCertRevoc()}));
         }

         var1.add(new ArrayIterator(this.bean.getRealms()));
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
            var5 = this.computeChildHashValue(this.bean.getCertRevoc());
            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            if (this.bean.isCompatibilityConnectionFiltersEnabledSet()) {
               var2.append("CompatibilityConnectionFiltersEnabled");
               var2.append(String.valueOf(this.bean.getCompatibilityConnectionFiltersEnabled()));
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

            if (this.bean.isCredentialSet()) {
               var2.append("Credential");
               var2.append(String.valueOf(this.bean.getCredential()));
            }

            if (this.bean.isCredentialEncryptedSet()) {
               var2.append("CredentialEncrypted");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getCredentialEncrypted())));
            }

            if (this.bean.isDefaultRealmSet()) {
               var2.append("DefaultRealm");
               var2.append(String.valueOf(this.bean.getDefaultRealm()));
            }

            if (this.bean.isDefaultRealmInternalSet()) {
               var2.append("DefaultRealmInternal");
               var2.append(String.valueOf(this.bean.getDefaultRealmInternal()));
            }

            if (this.bean.isDowngradeUntrustedPrincipalsSet()) {
               var2.append("DowngradeUntrustedPrincipals");
               var2.append(String.valueOf(this.bean.getDowngradeUntrustedPrincipals()));
            }

            if (this.bean.isEncryptedAESSecretKeySet()) {
               var2.append("EncryptedAESSecretKey");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getEncryptedAESSecretKey())));
            }

            if (this.bean.isEncryptedSecretKeySet()) {
               var2.append("EncryptedSecretKey");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getEncryptedSecretKey())));
            }

            if (this.bean.isEnforceStrictURLPatternSet()) {
               var2.append("EnforceStrictURLPattern");
               var2.append(String.valueOf(this.bean.getEnforceStrictURLPattern()));
            }

            if (this.bean.isEnforceValidBasicAuthCredentialsSet()) {
               var2.append("EnforceValidBasicAuthCredentials");
               var2.append(String.valueOf(this.bean.getEnforceValidBasicAuthCredentials()));
            }

            if (this.bean.isExcludedDomainNamesSet()) {
               var2.append("ExcludedDomainNames");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getExcludedDomainNames())));
            }

            if (this.bean.isNameSet()) {
               var2.append("Name");
               var2.append(String.valueOf(this.bean.getName()));
            }

            if (this.bean.isNodeManagerPasswordSet()) {
               var2.append("NodeManagerPassword");
               var2.append(String.valueOf(this.bean.getNodeManagerPassword()));
            }

            if (this.bean.isNodeManagerPasswordEncryptedSet()) {
               var2.append("NodeManagerPasswordEncrypted");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getNodeManagerPasswordEncrypted())));
            }

            if (this.bean.isNodeManagerUsernameSet()) {
               var2.append("NodeManagerUsername");
               var2.append(String.valueOf(this.bean.getNodeManagerUsername()));
            }

            if (this.bean.isRealmBootStrapVersionSet()) {
               var2.append("RealmBootStrapVersion");
               var2.append(String.valueOf(this.bean.getRealmBootStrapVersion()));
            }

            var5 = 0L;

            for(int var7 = 0; var7 < this.bean.getRealms().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getRealms()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            if (this.bean.isSaltSet()) {
               var2.append("Salt");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getSalt())));
            }

            if (this.bean.isWebAppFilesCaseInsensitiveSet()) {
               var2.append("WebAppFilesCaseInsensitive");
               var2.append(String.valueOf(this.bean.getWebAppFilesCaseInsensitive()));
            }

            if (this.bean.isAnonymousAdminLookupEnabledSet()) {
               var2.append("AnonymousAdminLookupEnabled");
               var2.append(String.valueOf(this.bean.isAnonymousAdminLookupEnabled()));
            }

            if (this.bean.isClearTextCredentialAccessEnabledSet()) {
               var2.append("ClearTextCredentialAccessEnabled");
               var2.append(String.valueOf(this.bean.isClearTextCredentialAccessEnabled()));
            }

            if (this.bean.isConsoleFullDelegationEnabledSet()) {
               var2.append("ConsoleFullDelegationEnabled");
               var2.append(String.valueOf(this.bean.isConsoleFullDelegationEnabled()));
            }

            if (this.bean.isCredentialGeneratedSet()) {
               var2.append("CredentialGenerated");
               var2.append(String.valueOf(this.bean.isCredentialGenerated()));
            }

            if (this.bean.isCrossDomainSecurityEnabledSet()) {
               var2.append("CrossDomainSecurityEnabled");
               var2.append(String.valueOf(this.bean.isCrossDomainSecurityEnabled()));
            }

            if (this.bean.isPrincipalEqualsCaseInsensitiveSet()) {
               var2.append("PrincipalEqualsCaseInsensitive");
               var2.append(String.valueOf(this.bean.isPrincipalEqualsCaseInsensitive()));
            }

            if (this.bean.isPrincipalEqualsCompareDnAndGuidSet()) {
               var2.append("PrincipalEqualsCompareDnAndGuid");
               var2.append(String.valueOf(this.bean.isPrincipalEqualsCompareDnAndGuid()));
            }

            var1.update(var2.toString().getBytes());
            return var1.getValue();
         } catch (Exception var8) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var8);
         }
      }

      protected void computeDiff(AbstractDescriptorBean var1) {
         try {
            super.computeDiff(var1);
            SecurityConfigurationMBeanImpl var2 = (SecurityConfigurationMBeanImpl)var1;
            this.computeSubDiff("CertRevoc", this.bean.getCertRevoc(), var2.getCertRevoc());
            this.computeDiff("CompatibilityConnectionFiltersEnabled", this.bean.getCompatibilityConnectionFiltersEnabled(), var2.getCompatibilityConnectionFiltersEnabled(), true);
            this.computeDiff("ConnectionFilter", this.bean.getConnectionFilter(), var2.getConnectionFilter(), false);
            this.computeDiff("ConnectionFilterRules", this.bean.getConnectionFilterRules(), var2.getConnectionFilterRules(), true);
            this.computeDiff("ConnectionLoggerEnabled", this.bean.getConnectionLoggerEnabled(), var2.getConnectionLoggerEnabled(), true);
            this.computeDiff("CredentialEncrypted", this.bean.getCredentialEncrypted(), var2.getCredentialEncrypted(), true);
            this.computeDiff("DefaultRealm", this.bean.getDefaultRealm(), var2.getDefaultRealm(), false);
            this.computeDiff("DefaultRealmInternal", this.bean.getDefaultRealmInternal(), var2.getDefaultRealmInternal(), false);
            this.computeDiff("DowngradeUntrustedPrincipals", this.bean.getDowngradeUntrustedPrincipals(), var2.getDowngradeUntrustedPrincipals(), false);
            this.computeDiff("EnforceStrictURLPattern", this.bean.getEnforceStrictURLPattern(), var2.getEnforceStrictURLPattern(), false);
            this.computeDiff("EnforceValidBasicAuthCredentials", this.bean.getEnforceValidBasicAuthCredentials(), var2.getEnforceValidBasicAuthCredentials(), false);
            this.computeDiff("ExcludedDomainNames", this.bean.getExcludedDomainNames(), var2.getExcludedDomainNames(), true);
            this.computeDiff("Name", this.bean.getName(), var2.getName(), false);
            this.computeDiff("NodeManagerPasswordEncrypted", this.bean.getNodeManagerPasswordEncrypted(), var2.getNodeManagerPasswordEncrypted(), true);
            this.computeDiff("NodeManagerUsername", this.bean.getNodeManagerUsername(), var2.getNodeManagerUsername(), true);
            this.computeDiff("RealmBootStrapVersion", this.bean.getRealmBootStrapVersion(), var2.getRealmBootStrapVersion(), false);
            this.computeChildDiff("Realms", this.bean.getRealms(), var2.getRealms(), false);
            this.computeDiff("WebAppFilesCaseInsensitive", this.bean.getWebAppFilesCaseInsensitive(), var2.getWebAppFilesCaseInsensitive(), false);
            this.computeDiff("AnonymousAdminLookupEnabled", this.bean.isAnonymousAdminLookupEnabled(), var2.isAnonymousAdminLookupEnabled(), false);
            this.computeDiff("ClearTextCredentialAccessEnabled", this.bean.isClearTextCredentialAccessEnabled(), var2.isClearTextCredentialAccessEnabled(), true);
            this.computeDiff("ConsoleFullDelegationEnabled", this.bean.isConsoleFullDelegationEnabled(), var2.isConsoleFullDelegationEnabled(), false);
            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("CredentialGenerated", this.bean.isCredentialGenerated(), var2.isCredentialGenerated(), false);
            }

            this.computeDiff("CrossDomainSecurityEnabled", this.bean.isCrossDomainSecurityEnabled(), var2.isCrossDomainSecurityEnabled(), true);
            this.computeDiff("PrincipalEqualsCaseInsensitive", this.bean.isPrincipalEqualsCaseInsensitive(), var2.isPrincipalEqualsCaseInsensitive(), true);
            this.computeDiff("PrincipalEqualsCompareDnAndGuid", this.bean.isPrincipalEqualsCompareDnAndGuid(), var2.isPrincipalEqualsCompareDnAndGuid(), true);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            SecurityConfigurationMBeanImpl var3 = (SecurityConfigurationMBeanImpl)var1.getSourceBean();
            SecurityConfigurationMBeanImpl var4 = (SecurityConfigurationMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("CertRevoc")) {
                  if (var6 == 2) {
                     var3.setCertRevoc((CertRevocMBean)this.createCopy((AbstractDescriptorBean)var4.getCertRevoc()));
                  } else {
                     if (var6 != 3) {
                        throw new AssertionError("Invalid type: " + var6);
                     }

                     var3._destroySingleton("CertRevoc", var3.getCertRevoc());
                  }

                  var3._conditionalUnset(var2.isUnsetUpdate(), 35);
               } else if (var5.equals("CompatibilityConnectionFiltersEnabled")) {
                  var3.setCompatibilityConnectionFiltersEnabled(var4.getCompatibilityConnectionFiltersEnabled());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 21);
               } else if (var5.equals("ConnectionFilter")) {
                  var3.setConnectionFilter(var4.getConnectionFilter());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 18);
               } else if (var5.equals("ConnectionFilterRules")) {
                  var3.setConnectionFilterRules(var4.getConnectionFilterRules());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 19);
               } else if (var5.equals("ConnectionLoggerEnabled")) {
                  var3.setConnectionLoggerEnabled(var4.getConnectionLoggerEnabled());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 20);
               } else if (!var5.equals("Credential")) {
                  if (var5.equals("CredentialEncrypted")) {
                     var3.setCredentialEncrypted(var4.getCredentialEncrypted());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 15);
                  } else if (var5.equals("DefaultRealm")) {
                     var3.setDefaultRealmAsString(var4.getDefaultRealmAsString());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 8);
                  } else if (var5.equals("DefaultRealmInternal")) {
                     var3.setDefaultRealmInternalAsString(var4.getDefaultRealmInternalAsString());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 31);
                  } else if (var5.equals("DowngradeUntrustedPrincipals")) {
                     var3.setDowngradeUntrustedPrincipals(var4.getDowngradeUntrustedPrincipals());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 27);
                  } else if (!var5.equals("EncryptedAESSecretKey") && !var5.equals("EncryptedSecretKey")) {
                     if (var5.equals("EnforceStrictURLPattern")) {
                        var3.setEnforceStrictURLPattern(var4.getEnforceStrictURLPattern());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 28);
                     } else if (var5.equals("EnforceValidBasicAuthCredentials")) {
                        var3.setEnforceValidBasicAuthCredentials(var4.getEnforceValidBasicAuthCredentials());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 29);
                     } else if (var5.equals("ExcludedDomainNames")) {
                        var3.setExcludedDomainNames(var4.getExcludedDomainNames());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 32);
                     } else if (var5.equals("Name")) {
                        var3.setName(var4.getName());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 2);
                     } else if (!var5.equals("NodeManagerPassword")) {
                        if (var5.equals("NodeManagerPasswordEncrypted")) {
                           var3.setNodeManagerPasswordEncrypted(var4.getNodeManagerPasswordEncrypted());
                           var3._conditionalUnset(var2.isUnsetUpdate(), 24);
                        } else if (var5.equals("NodeManagerUsername")) {
                           var3.setNodeManagerUsername(var4.getNodeManagerUsername());
                           var3._conditionalUnset(var2.isUnsetUpdate(), 22);
                        } else if (var5.equals("RealmBootStrapVersion")) {
                           var3.setRealmBootStrapVersion(var4.getRealmBootStrapVersion());
                           var3._conditionalUnset(var2.isUnsetUpdate(), 17);
                        } else if (var5.equals("Realms")) {
                           if (var6 == 2) {
                              var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                              var3.addRealm((weblogic.management.security.RealmMBean)var2.getAddedObject());
                           } else {
                              if (var6 != 3) {
                                 throw new AssertionError("Invalid type: " + var6);
                              }

                              var3.removeRealm((weblogic.management.security.RealmMBean)var2.getRemovedObject());
                           }

                           if (var3.getRealms() == null || var3.getRealms().length == 0) {
                              var3._conditionalUnset(var2.isUnsetUpdate(), 7);
                           }
                        } else if (!var5.equals("Salt")) {
                           if (var5.equals("WebAppFilesCaseInsensitive")) {
                              var3.setWebAppFilesCaseInsensitive(var4.getWebAppFilesCaseInsensitive());
                              var3._conditionalUnset(var2.isUnsetUpdate(), 16);
                           } else if (var5.equals("AnonymousAdminLookupEnabled")) {
                              var3.setAnonymousAdminLookupEnabled(var4.isAnonymousAdminLookupEnabled());
                              var3._conditionalUnset(var2.isUnsetUpdate(), 11);
                           } else if (var5.equals("ClearTextCredentialAccessEnabled")) {
                              var3.setClearTextCredentialAccessEnabled(var4.isClearTextCredentialAccessEnabled());
                              var3._conditionalUnset(var2.isUnsetUpdate(), 12);
                           } else if (var5.equals("ConsoleFullDelegationEnabled")) {
                              var3.setConsoleFullDelegationEnabled(var4.isConsoleFullDelegationEnabled());
                              var3._conditionalUnset(var2.isUnsetUpdate(), 30);
                           } else if (var5.equals("CredentialGenerated")) {
                              var3.setCredentialGenerated(var4.isCredentialGenerated());
                              var3._conditionalUnset(var2.isUnsetUpdate(), 13);
                           } else if (var5.equals("CrossDomainSecurityEnabled")) {
                              var3.setCrossDomainSecurityEnabled(var4.isCrossDomainSecurityEnabled());
                              var3._conditionalUnset(var2.isUnsetUpdate(), 33);
                           } else if (var5.equals("PrincipalEqualsCaseInsensitive")) {
                              var3.setPrincipalEqualsCaseInsensitive(var4.isPrincipalEqualsCaseInsensitive());
                              var3._conditionalUnset(var2.isUnsetUpdate(), 25);
                           } else if (var5.equals("PrincipalEqualsCompareDnAndGuid")) {
                              var3.setPrincipalEqualsCompareDnAndGuid(var4.isPrincipalEqualsCompareDnAndGuid());
                              var3._conditionalUnset(var2.isUnsetUpdate(), 26);
                           } else {
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
            SecurityConfigurationMBeanImpl var5 = (SecurityConfigurationMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("CertRevoc")) && this.bean.isCertRevocSet() && !var5._isSet(35)) {
               CertRevocMBean var4 = this.bean.getCertRevoc();
               var5.setCertRevoc((CertRevocMBean)null);
               var5.setCertRevoc(var4 == null ? null : (CertRevocMBean)this.createCopy((AbstractDescriptorBean)var4, var2));
            }

            if ((var3 == null || !var3.contains("CompatibilityConnectionFiltersEnabled")) && this.bean.isCompatibilityConnectionFiltersEnabledSet()) {
               var5.setCompatibilityConnectionFiltersEnabled(this.bean.getCompatibilityConnectionFiltersEnabled());
            }

            if ((var3 == null || !var3.contains("ConnectionFilter")) && this.bean.isConnectionFilterSet()) {
               var5.setConnectionFilter(this.bean.getConnectionFilter());
            }

            String[] var11;
            if ((var3 == null || !var3.contains("ConnectionFilterRules")) && this.bean.isConnectionFilterRulesSet()) {
               var11 = this.bean.getConnectionFilterRules();
               var5.setConnectionFilterRules(var11 == null ? null : (String[])((String[])((String[])((String[])var11)).clone()));
            }

            if ((var3 == null || !var3.contains("ConnectionLoggerEnabled")) && this.bean.isConnectionLoggerEnabledSet()) {
               var5.setConnectionLoggerEnabled(this.bean.getConnectionLoggerEnabled());
            }

            byte[] var12;
            if ((var3 == null || !var3.contains("CredentialEncrypted")) && this.bean.isCredentialEncryptedSet()) {
               var12 = this.bean.getCredentialEncrypted();
               var5.setCredentialEncrypted(var12 == null ? null : (byte[])((byte[])((byte[])((byte[])var12)).clone()));
            }

            if ((var3 == null || !var3.contains("DefaultRealm")) && this.bean.isDefaultRealmSet()) {
               var5._unSet(var5, 8);
               var5.setDefaultRealmAsString(this.bean.getDefaultRealmAsString());
            }

            if ((var3 == null || !var3.contains("DefaultRealmInternal")) && this.bean.isDefaultRealmInternalSet()) {
               var5._unSet(var5, 31);
               var5.setDefaultRealmInternalAsString(this.bean.getDefaultRealmInternalAsString());
            }

            if ((var3 == null || !var3.contains("DowngradeUntrustedPrincipals")) && this.bean.isDowngradeUntrustedPrincipalsSet()) {
               var5.setDowngradeUntrustedPrincipals(this.bean.getDowngradeUntrustedPrincipals());
            }

            if ((var3 == null || !var3.contains("EnforceStrictURLPattern")) && this.bean.isEnforceStrictURLPatternSet()) {
               var5.setEnforceStrictURLPattern(this.bean.getEnforceStrictURLPattern());
            }

            if ((var3 == null || !var3.contains("EnforceValidBasicAuthCredentials")) && this.bean.isEnforceValidBasicAuthCredentialsSet()) {
               var5.setEnforceValidBasicAuthCredentials(this.bean.getEnforceValidBasicAuthCredentials());
            }

            if ((var3 == null || !var3.contains("ExcludedDomainNames")) && this.bean.isExcludedDomainNamesSet()) {
               var11 = this.bean.getExcludedDomainNames();
               var5.setExcludedDomainNames(var11 == null ? null : (String[])((String[])((String[])((String[])var11)).clone()));
            }

            if ((var3 == null || !var3.contains("Name")) && this.bean.isNameSet()) {
               var5.setName(this.bean.getName());
            }

            if ((var3 == null || !var3.contains("NodeManagerPasswordEncrypted")) && this.bean.isNodeManagerPasswordEncryptedSet()) {
               var12 = this.bean.getNodeManagerPasswordEncrypted();
               var5.setNodeManagerPasswordEncrypted(var12 == null ? null : (byte[])((byte[])((byte[])((byte[])var12)).clone()));
            }

            if ((var3 == null || !var3.contains("NodeManagerUsername")) && this.bean.isNodeManagerUsernameSet()) {
               var5.setNodeManagerUsername(this.bean.getNodeManagerUsername());
            }

            if ((var3 == null || !var3.contains("RealmBootStrapVersion")) && this.bean.isRealmBootStrapVersionSet()) {
               var5.setRealmBootStrapVersion(this.bean.getRealmBootStrapVersion());
            }

            if ((var3 == null || !var3.contains("Realms")) && this.bean.isRealmsSet() && !var5._isSet(7)) {
               weblogic.management.security.RealmMBean[] var6 = this.bean.getRealms();
               weblogic.management.security.RealmMBean[] var7 = new weblogic.management.security.RealmMBean[var6.length];

               for(int var8 = 0; var8 < var7.length; ++var8) {
                  var7[var8] = (weblogic.management.security.RealmMBean)((weblogic.management.security.RealmMBean)this.createCopy((AbstractDescriptorBean)var6[var8], var2));
               }

               var5.setRealms(var7);
            }

            if ((var3 == null || !var3.contains("WebAppFilesCaseInsensitive")) && this.bean.isWebAppFilesCaseInsensitiveSet()) {
               var5.setWebAppFilesCaseInsensitive(this.bean.getWebAppFilesCaseInsensitive());
            }

            if ((var3 == null || !var3.contains("AnonymousAdminLookupEnabled")) && this.bean.isAnonymousAdminLookupEnabledSet()) {
               var5.setAnonymousAdminLookupEnabled(this.bean.isAnonymousAdminLookupEnabled());
            }

            if ((var3 == null || !var3.contains("ClearTextCredentialAccessEnabled")) && this.bean.isClearTextCredentialAccessEnabledSet()) {
               var5.setClearTextCredentialAccessEnabled(this.bean.isClearTextCredentialAccessEnabled());
            }

            if ((var3 == null || !var3.contains("ConsoleFullDelegationEnabled")) && this.bean.isConsoleFullDelegationEnabledSet()) {
               var5.setConsoleFullDelegationEnabled(this.bean.isConsoleFullDelegationEnabled());
            }

            if (var2 && (var3 == null || !var3.contains("CredentialGenerated")) && this.bean.isCredentialGeneratedSet()) {
               var5.setCredentialGenerated(this.bean.isCredentialGenerated());
            }

            if ((var3 == null || !var3.contains("CrossDomainSecurityEnabled")) && this.bean.isCrossDomainSecurityEnabledSet()) {
               var5.setCrossDomainSecurityEnabled(this.bean.isCrossDomainSecurityEnabled());
            }

            if ((var3 == null || !var3.contains("PrincipalEqualsCaseInsensitive")) && this.bean.isPrincipalEqualsCaseInsensitiveSet()) {
               var5.setPrincipalEqualsCaseInsensitive(this.bean.isPrincipalEqualsCaseInsensitive());
            }

            if ((var3 == null || !var3.contains("PrincipalEqualsCompareDnAndGuid")) && this.bean.isPrincipalEqualsCompareDnAndGuidSet()) {
               var5.setPrincipalEqualsCompareDnAndGuid(this.bean.isPrincipalEqualsCompareDnAndGuid());
            }

            return var5;
         } catch (RuntimeException var9) {
            throw var9;
         } catch (Exception var10) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var10);
         }
      }

      protected void inferSubTree(Class var1, Object var2) {
         super.inferSubTree(var1, var2);
         Object var3 = null;
         this.inferSubTree(this.bean.getCertRevoc(), var1, var2);
         this.inferSubTree(this.bean.getDefaultRealm(), var1, var2);
         this.inferSubTree(this.bean.getDefaultRealmInternal(), var1, var2);
         this.inferSubTree(this.bean.getRealms(), var1, var2);
      }
   }
}
