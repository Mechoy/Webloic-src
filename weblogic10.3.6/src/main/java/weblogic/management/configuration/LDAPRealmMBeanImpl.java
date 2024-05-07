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
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBeanHelper;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.management.ManagementException;
import weblogic.management.mbeans.custom.LDAPRealm;
import weblogic.utils.ArrayUtils;
import weblogic.utils.collections.CombinedIterator;

public class LDAPRealmMBeanImpl extends BasicRealmMBeanImpl implements LDAPRealmMBean, Serializable {
   private String _AuthProtocol;
   private String _Credential;
   private byte[] _CredentialEncrypted;
   private String _GroupDN;
   private boolean _GroupIsContext;
   private String _GroupNameAttribute;
   private String _GroupUsernameAttribute;
   private String _LDAPURL;
   private String _LdapProvider;
   private String _Name;
   private String _Principal;
   private String _RealmClassName;
   private boolean _SSLEnable;
   private String _UserAuthentication;
   private String _UserDN;
   private String _UserNameAttribute;
   private String _UserPasswordAttribute;
   private LDAPRealm _customizer;
   private static SchemaHelper2 _schemaHelper;

   public LDAPRealmMBeanImpl() {
      try {
         this._customizer = new LDAPRealm(this);
      } catch (Exception var2) {
         if (var2 instanceof RuntimeException) {
            throw (RuntimeException)var2;
         }

         throw new UndeclaredThrowableException(var2);
      }

      this._initializeProperty(-1);
   }

   public LDAPRealmMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);

      try {
         this._customizer = new LDAPRealm(this);
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         }

         throw new UndeclaredThrowableException(var4);
      }

      this._initializeProperty(-1);
   }

   public String getLDAPURL() {
      return this._LDAPURL;
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

   public String getRealmClassName() {
      return this._customizer.getRealmClassName();
   }

   public boolean isLDAPURLSet() {
      return this._isSet(8);
   }

   public boolean isNameSet() {
      return this._isSet(2);
   }

   public boolean isRealmClassNameSet() {
      return this._isSet(7);
   }

   public void setRealmClassName(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._RealmClassName;
      this._RealmClassName = var1;
      this._postSet(7, var2, var1);
   }

   public void setLDAPURL(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._LDAPURL;
      this._LDAPURL = var1;
      this._postSet(8, var2, var1);
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

   public String getPrincipal() {
      return this._Principal;
   }

   public boolean isPrincipalSet() {
      return this._isSet(9);
   }

   public void setPrincipal(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._Principal;
      this._Principal = var1;
      this._postSet(9, var2, var1);
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
      return this._isSet(11);
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

   public boolean getSSLEnable() {
      return this._SSLEnable;
   }

   public boolean isSSLEnableSet() {
      return this._isSet(12);
   }

   public void setSSLEnable(boolean var1) {
      boolean var2 = this._SSLEnable;
      this._SSLEnable = var1;
      this._postSet(12, var2, var1);
   }

   public String getLdapProvider() {
      return this._LdapProvider;
   }

   public boolean isLdapProviderSet() {
      return this._isSet(13);
   }

   public void touch() throws ConfigurationException {
      this._customizer.touch();
   }

   public void freezeCurrentValue(String var1) throws AttributeNotFoundException, MBeanException {
      this._customizer.freezeCurrentValue(var1);
   }

   public void setLdapProvider(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._LdapProvider;
      this._LdapProvider = var1;
      this._postSet(13, var2, var1);
   }

   public String getAuthProtocol() {
      return this._AuthProtocol;
   }

   public boolean isAuthProtocolSet() {
      return this._isSet(14);
   }

   public void restoreDefaultValue(String var1) throws AttributeNotFoundException {
      this._customizer.restoreDefaultValue(var1);
   }

   public void setAuthProtocol(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"none", "simple", "CRAM-MD5"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("AuthProtocol", var1, var2);
      String var3 = this._AuthProtocol;
      this._AuthProtocol = var1;
      this._postSet(14, var3, var1);
   }

   public String getUserAuthentication() {
      return this._UserAuthentication;
   }

   public boolean isUserAuthenticationSet() {
      return this._isSet(15);
   }

   public void setUserAuthentication(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"bind", "external", "local"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("UserAuthentication", var1, var2);
      String var3 = this._UserAuthentication;
      this._UserAuthentication = var1;
      this._postSet(15, var3, var1);
   }

   public String getUserPasswordAttribute() {
      return this._UserPasswordAttribute;
   }

   public boolean isUserPasswordAttributeSet() {
      return this._isSet(16);
   }

   public void setUserPasswordAttribute(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._UserPasswordAttribute;
      this._UserPasswordAttribute = var1;
      this._postSet(16, var2, var1);
   }

   public String getUserDN() {
      return this._UserDN;
   }

   public boolean isUserDNSet() {
      return this._isSet(17);
   }

   public void setUserDN(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._UserDN;
      this._UserDN = var1;
      this._postSet(17, var2, var1);
   }

   public String getUserNameAttribute() {
      return this._UserNameAttribute;
   }

   public boolean isUserNameAttributeSet() {
      return this._isSet(18);
   }

   public void setUserNameAttribute(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._UserNameAttribute;
      this._UserNameAttribute = var1;
      this._postSet(18, var2, var1);
   }

   public String getGroupDN() {
      return this._GroupDN;
   }

   public boolean isGroupDNSet() {
      return this._isSet(19);
   }

   public void setGroupDN(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._GroupDN;
      this._GroupDN = var1;
      this._postSet(19, var2, var1);
   }

   public String getGroupNameAttribute() {
      return this._GroupNameAttribute;
   }

   public boolean isGroupNameAttributeSet() {
      return this._isSet(20);
   }

   public void setGroupNameAttribute(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._GroupNameAttribute;
      this._GroupNameAttribute = var1;
      this._postSet(20, var2, var1);
   }

   public boolean getGroupIsContext() {
      return this._GroupIsContext;
   }

   public boolean isGroupIsContextSet() {
      return this._isSet(21);
   }

   public void setGroupIsContext(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this._GroupIsContext;
      this._GroupIsContext = var1;
      this._postSet(21, var2, var1);
   }

   public String getGroupUsernameAttribute() {
      return this._GroupUsernameAttribute;
   }

   public boolean isGroupUsernameAttributeSet() {
      return this._isSet(22);
   }

   public void setGroupUsernameAttribute(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._GroupUsernameAttribute;
      this._GroupUsernameAttribute = var1;
      this._postSet(22, var2, var1);
   }

   public Object _getKey() {
      return this.getName();
   }

   public void _validate() throws IllegalArgumentException {
      super._validate();
   }

   public void setCredentialEncrypted(byte[] var1) {
      byte[] var2 = this._CredentialEncrypted;
      if (this._isProductionModeEnabled() && var1 != null && !this._isEncrypted(var1)) {
         throw new IllegalArgumentException("In production mode, it's not allowed to set a clear text value to the property: CredentialEncrypted of LDAPRealmMBean");
      } else {
         this._getHelper()._clearArray(this._CredentialEncrypted);
         this._CredentialEncrypted = this._getHelper()._cloneArray(var1);
         this._postSet(11, var2, var1);
      }
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
         if (var1 == 10) {
            this._markSet(11, false);
         }
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
         var1 = 14;
      }

      try {
         switch (var1) {
            case 14:
               this._AuthProtocol = "none";
               if (var2) {
                  break;
               }
            case 10:
               this._CredentialEncrypted = null;
               if (var2) {
                  break;
               }
            case 11:
               this._CredentialEncrypted = null;
               if (var2) {
                  break;
               }
            case 19:
               this._GroupDN = null;
               if (var2) {
                  break;
               }
            case 21:
               this._GroupIsContext = true;
               if (var2) {
                  break;
               }
            case 20:
               this._GroupNameAttribute = "cn";
               if (var2) {
                  break;
               }
            case 22:
               this._GroupUsernameAttribute = "member";
               if (var2) {
                  break;
               }
            case 8:
               this._LDAPURL = "ldap://ldapserver:389";
               if (var2) {
                  break;
               }
            case 13:
               this._LdapProvider = "com.sun.jndi.ldap.LdapCtxFactory";
               if (var2) {
                  break;
               }
            case 2:
               this._customizer.setName((String)null);
               if (var2) {
                  break;
               }
            case 9:
               this._Principal = null;
               if (var2) {
                  break;
               }
            case 7:
               this._RealmClassName = null;
               if (var2) {
                  break;
               }
            case 12:
               this._SSLEnable = false;
               if (var2) {
                  break;
               }
            case 15:
               this._UserAuthentication = "bind";
               if (var2) {
                  break;
               }
            case 17:
               this._UserDN = null;
               if (var2) {
                  break;
               }
            case 18:
               this._UserNameAttribute = null;
               if (var2) {
                  break;
               }
            case 16:
               this._UserPasswordAttribute = "userpassword";
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
      return "LDAPRealm";
   }

   public void putValue(String var1, Object var2) {
      String var4;
      if (var1.equals("AuthProtocol")) {
         var4 = this._AuthProtocol;
         this._AuthProtocol = (String)var2;
         this._postSet(14, var4, this._AuthProtocol);
      } else if (var1.equals("Credential")) {
         var4 = this._Credential;
         this._Credential = (String)var2;
         this._postSet(10, var4, this._Credential);
      } else if (var1.equals("CredentialEncrypted")) {
         byte[] var6 = this._CredentialEncrypted;
         this._CredentialEncrypted = (byte[])((byte[])var2);
         this._postSet(11, var6, this._CredentialEncrypted);
      } else if (var1.equals("GroupDN")) {
         var4 = this._GroupDN;
         this._GroupDN = (String)var2;
         this._postSet(19, var4, this._GroupDN);
      } else {
         boolean var5;
         if (var1.equals("GroupIsContext")) {
            var5 = this._GroupIsContext;
            this._GroupIsContext = (Boolean)var2;
            this._postSet(21, var5, this._GroupIsContext);
         } else if (var1.equals("GroupNameAttribute")) {
            var4 = this._GroupNameAttribute;
            this._GroupNameAttribute = (String)var2;
            this._postSet(20, var4, this._GroupNameAttribute);
         } else if (var1.equals("GroupUsernameAttribute")) {
            var4 = this._GroupUsernameAttribute;
            this._GroupUsernameAttribute = (String)var2;
            this._postSet(22, var4, this._GroupUsernameAttribute);
         } else if (var1.equals("LDAPURL")) {
            var4 = this._LDAPURL;
            this._LDAPURL = (String)var2;
            this._postSet(8, var4, this._LDAPURL);
         } else if (var1.equals("LdapProvider")) {
            var4 = this._LdapProvider;
            this._LdapProvider = (String)var2;
            this._postSet(13, var4, this._LdapProvider);
         } else if (var1.equals("Name")) {
            var4 = this._Name;
            this._Name = (String)var2;
            this._postSet(2, var4, this._Name);
         } else if (var1.equals("Principal")) {
            var4 = this._Principal;
            this._Principal = (String)var2;
            this._postSet(9, var4, this._Principal);
         } else if (var1.equals("RealmClassName")) {
            var4 = this._RealmClassName;
            this._RealmClassName = (String)var2;
            this._postSet(7, var4, this._RealmClassName);
         } else if (var1.equals("SSLEnable")) {
            var5 = this._SSLEnable;
            this._SSLEnable = (Boolean)var2;
            this._postSet(12, var5, this._SSLEnable);
         } else if (var1.equals("UserAuthentication")) {
            var4 = this._UserAuthentication;
            this._UserAuthentication = (String)var2;
            this._postSet(15, var4, this._UserAuthentication);
         } else if (var1.equals("UserDN")) {
            var4 = this._UserDN;
            this._UserDN = (String)var2;
            this._postSet(17, var4, this._UserDN);
         } else if (var1.equals("UserNameAttribute")) {
            var4 = this._UserNameAttribute;
            this._UserNameAttribute = (String)var2;
            this._postSet(18, var4, this._UserNameAttribute);
         } else if (var1.equals("UserPasswordAttribute")) {
            var4 = this._UserPasswordAttribute;
            this._UserPasswordAttribute = (String)var2;
            this._postSet(16, var4, this._UserPasswordAttribute);
         } else if (var1.equals("customizer")) {
            LDAPRealm var3 = this._customizer;
            this._customizer = (LDAPRealm)var2;
         } else {
            super.putValue(var1, var2);
         }
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("AuthProtocol")) {
         return this._AuthProtocol;
      } else if (var1.equals("Credential")) {
         return this._Credential;
      } else if (var1.equals("CredentialEncrypted")) {
         return this._CredentialEncrypted;
      } else if (var1.equals("GroupDN")) {
         return this._GroupDN;
      } else if (var1.equals("GroupIsContext")) {
         return new Boolean(this._GroupIsContext);
      } else if (var1.equals("GroupNameAttribute")) {
         return this._GroupNameAttribute;
      } else if (var1.equals("GroupUsernameAttribute")) {
         return this._GroupUsernameAttribute;
      } else if (var1.equals("LDAPURL")) {
         return this._LDAPURL;
      } else if (var1.equals("LdapProvider")) {
         return this._LdapProvider;
      } else if (var1.equals("Name")) {
         return this._Name;
      } else if (var1.equals("Principal")) {
         return this._Principal;
      } else if (var1.equals("RealmClassName")) {
         return this._RealmClassName;
      } else if (var1.equals("SSLEnable")) {
         return new Boolean(this._SSLEnable);
      } else if (var1.equals("UserAuthentication")) {
         return this._UserAuthentication;
      } else if (var1.equals("UserDN")) {
         return this._UserDN;
      } else if (var1.equals("UserNameAttribute")) {
         return this._UserNameAttribute;
      } else if (var1.equals("UserPasswordAttribute")) {
         return this._UserPasswordAttribute;
      } else {
         return var1.equals("customizer") ? this._customizer : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends BasicRealmMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 4:
               if (var1.equals("name")) {
                  return 2;
               }
            case 5:
            case 11:
            case 12:
            case 14:
            case 15:
            case 17:
            case 18:
            case 21:
            case 22:
            default:
               break;
            case 6:
               if (var1.equals("userdn")) {
                  return 17;
               }
               break;
            case 7:
               if (var1.equals("groupdn")) {
                  return 19;
               }
               break;
            case 8:
               if (var1.equals("ldap-url")) {
                  return 8;
               }
               break;
            case 9:
               if (var1.equals("principal")) {
                  return 9;
               }
               break;
            case 10:
               if (var1.equals("credential")) {
                  return 10;
               }

               if (var1.equals("ssl-enable")) {
                  return 12;
               }
               break;
            case 13:
               if (var1.equals("auth-protocol")) {
                  return 14;
               }

               if (var1.equals("ldap-provider")) {
                  return 13;
               }
               break;
            case 16:
               if (var1.equals("group-is-context")) {
                  return 21;
               }

               if (var1.equals("realm-class-name")) {
                  return 7;
               }
               break;
            case 19:
               if (var1.equals("user-authentication")) {
                  return 15;
               }

               if (var1.equals("user-name-attribute")) {
                  return 18;
               }
               break;
            case 20:
               if (var1.equals("credential-encrypted")) {
                  return 11;
               }

               if (var1.equals("group-name-attribute")) {
                  return 20;
               }
               break;
            case 23:
               if (var1.equals("user-password-attribute")) {
                  return 16;
               }
               break;
            case 24:
               if (var1.equals("group-username-attribute")) {
                  return 22;
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
               return "realm-class-name";
            case 8:
               return "ldap-url";
            case 9:
               return "principal";
            case 10:
               return "credential";
            case 11:
               return "credential-encrypted";
            case 12:
               return "ssl-enable";
            case 13:
               return "ldap-provider";
            case 14:
               return "auth-protocol";
            case 15:
               return "user-authentication";
            case 16:
               return "user-password-attribute";
            case 17:
               return "userdn";
            case 18:
               return "user-name-attribute";
            case 19:
               return "groupdn";
            case 20:
               return "group-name-attribute";
            case 21:
               return "group-is-context";
            case 22:
               return "group-username-attribute";
         }
      }

      public boolean isArray(int var1) {
         switch (var1) {
            default:
               return super.isArray(var1);
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

   protected static class Helper extends BasicRealmMBeanImpl.Helper {
      private LDAPRealmMBeanImpl bean;

      protected Helper(LDAPRealmMBeanImpl var1) {
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
               return "RealmClassName";
            case 8:
               return "LDAPURL";
            case 9:
               return "Principal";
            case 10:
               return "Credential";
            case 11:
               return "CredentialEncrypted";
            case 12:
               return "SSLEnable";
            case 13:
               return "LdapProvider";
            case 14:
               return "AuthProtocol";
            case 15:
               return "UserAuthentication";
            case 16:
               return "UserPasswordAttribute";
            case 17:
               return "UserDN";
            case 18:
               return "UserNameAttribute";
            case 19:
               return "GroupDN";
            case 20:
               return "GroupNameAttribute";
            case 21:
               return "GroupIsContext";
            case 22:
               return "GroupUsernameAttribute";
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("AuthProtocol")) {
            return 14;
         } else if (var1.equals("Credential")) {
            return 10;
         } else if (var1.equals("CredentialEncrypted")) {
            return 11;
         } else if (var1.equals("GroupDN")) {
            return 19;
         } else if (var1.equals("GroupIsContext")) {
            return 21;
         } else if (var1.equals("GroupNameAttribute")) {
            return 20;
         } else if (var1.equals("GroupUsernameAttribute")) {
            return 22;
         } else if (var1.equals("LDAPURL")) {
            return 8;
         } else if (var1.equals("LdapProvider")) {
            return 13;
         } else if (var1.equals("Name")) {
            return 2;
         } else if (var1.equals("Principal")) {
            return 9;
         } else if (var1.equals("RealmClassName")) {
            return 7;
         } else if (var1.equals("SSLEnable")) {
            return 12;
         } else if (var1.equals("UserAuthentication")) {
            return 15;
         } else if (var1.equals("UserDN")) {
            return 17;
         } else if (var1.equals("UserNameAttribute")) {
            return 18;
         } else {
            return var1.equals("UserPasswordAttribute") ? 16 : super.getPropertyIndex(var1);
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
            if (this.bean.isAuthProtocolSet()) {
               var2.append("AuthProtocol");
               var2.append(String.valueOf(this.bean.getAuthProtocol()));
            }

            if (this.bean.isCredentialSet()) {
               var2.append("Credential");
               var2.append(String.valueOf(this.bean.getCredential()));
            }

            if (this.bean.isCredentialEncryptedSet()) {
               var2.append("CredentialEncrypted");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getCredentialEncrypted())));
            }

            if (this.bean.isGroupDNSet()) {
               var2.append("GroupDN");
               var2.append(String.valueOf(this.bean.getGroupDN()));
            }

            if (this.bean.isGroupIsContextSet()) {
               var2.append("GroupIsContext");
               var2.append(String.valueOf(this.bean.getGroupIsContext()));
            }

            if (this.bean.isGroupNameAttributeSet()) {
               var2.append("GroupNameAttribute");
               var2.append(String.valueOf(this.bean.getGroupNameAttribute()));
            }

            if (this.bean.isGroupUsernameAttributeSet()) {
               var2.append("GroupUsernameAttribute");
               var2.append(String.valueOf(this.bean.getGroupUsernameAttribute()));
            }

            if (this.bean.isLDAPURLSet()) {
               var2.append("LDAPURL");
               var2.append(String.valueOf(this.bean.getLDAPURL()));
            }

            if (this.bean.isLdapProviderSet()) {
               var2.append("LdapProvider");
               var2.append(String.valueOf(this.bean.getLdapProvider()));
            }

            if (this.bean.isNameSet()) {
               var2.append("Name");
               var2.append(String.valueOf(this.bean.getName()));
            }

            if (this.bean.isPrincipalSet()) {
               var2.append("Principal");
               var2.append(String.valueOf(this.bean.getPrincipal()));
            }

            if (this.bean.isRealmClassNameSet()) {
               var2.append("RealmClassName");
               var2.append(String.valueOf(this.bean.getRealmClassName()));
            }

            if (this.bean.isSSLEnableSet()) {
               var2.append("SSLEnable");
               var2.append(String.valueOf(this.bean.getSSLEnable()));
            }

            if (this.bean.isUserAuthenticationSet()) {
               var2.append("UserAuthentication");
               var2.append(String.valueOf(this.bean.getUserAuthentication()));
            }

            if (this.bean.isUserDNSet()) {
               var2.append("UserDN");
               var2.append(String.valueOf(this.bean.getUserDN()));
            }

            if (this.bean.isUserNameAttributeSet()) {
               var2.append("UserNameAttribute");
               var2.append(String.valueOf(this.bean.getUserNameAttribute()));
            }

            if (this.bean.isUserPasswordAttributeSet()) {
               var2.append("UserPasswordAttribute");
               var2.append(String.valueOf(this.bean.getUserPasswordAttribute()));
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
            LDAPRealmMBeanImpl var2 = (LDAPRealmMBeanImpl)var1;
            this.computeDiff("AuthProtocol", this.bean.getAuthProtocol(), var2.getAuthProtocol(), false);
            this.computeDiff("CredentialEncrypted", this.bean.getCredentialEncrypted(), var2.getCredentialEncrypted(), false);
            this.computeDiff("GroupDN", this.bean.getGroupDN(), var2.getGroupDN(), false);
            this.computeDiff("GroupIsContext", this.bean.getGroupIsContext(), var2.getGroupIsContext(), false);
            this.computeDiff("GroupNameAttribute", this.bean.getGroupNameAttribute(), var2.getGroupNameAttribute(), false);
            this.computeDiff("GroupUsernameAttribute", this.bean.getGroupUsernameAttribute(), var2.getGroupUsernameAttribute(), false);
            this.computeDiff("LDAPURL", this.bean.getLDAPURL(), var2.getLDAPURL(), false);
            this.computeDiff("LdapProvider", this.bean.getLdapProvider(), var2.getLdapProvider(), false);
            this.computeDiff("Name", this.bean.getName(), var2.getName(), false);
            this.computeDiff("Principal", this.bean.getPrincipal(), var2.getPrincipal(), false);
            this.computeDiff("RealmClassName", this.bean.getRealmClassName(), var2.getRealmClassName(), false);
            this.computeDiff("SSLEnable", this.bean.getSSLEnable(), var2.getSSLEnable(), false);
            this.computeDiff("UserAuthentication", this.bean.getUserAuthentication(), var2.getUserAuthentication(), false);
            this.computeDiff("UserDN", this.bean.getUserDN(), var2.getUserDN(), false);
            this.computeDiff("UserNameAttribute", this.bean.getUserNameAttribute(), var2.getUserNameAttribute(), false);
            this.computeDiff("UserPasswordAttribute", this.bean.getUserPasswordAttribute(), var2.getUserPasswordAttribute(), false);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            LDAPRealmMBeanImpl var3 = (LDAPRealmMBeanImpl)var1.getSourceBean();
            LDAPRealmMBeanImpl var4 = (LDAPRealmMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("AuthProtocol")) {
                  var3.setAuthProtocol(var4.getAuthProtocol());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 14);
               } else if (!var5.equals("Credential")) {
                  if (var5.equals("CredentialEncrypted")) {
                     var3.setCredentialEncrypted(var4.getCredentialEncrypted());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 11);
                  } else if (var5.equals("GroupDN")) {
                     var3.setGroupDN(var4.getGroupDN());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 19);
                  } else if (var5.equals("GroupIsContext")) {
                     var3.setGroupIsContext(var4.getGroupIsContext());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 21);
                  } else if (var5.equals("GroupNameAttribute")) {
                     var3.setGroupNameAttribute(var4.getGroupNameAttribute());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 20);
                  } else if (var5.equals("GroupUsernameAttribute")) {
                     var3.setGroupUsernameAttribute(var4.getGroupUsernameAttribute());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 22);
                  } else if (var5.equals("LDAPURL")) {
                     var3.setLDAPURL(var4.getLDAPURL());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 8);
                  } else if (var5.equals("LdapProvider")) {
                     var3.setLdapProvider(var4.getLdapProvider());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 13);
                  } else if (var5.equals("Name")) {
                     var3.setName(var4.getName());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 2);
                  } else if (var5.equals("Principal")) {
                     var3.setPrincipal(var4.getPrincipal());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 9);
                  } else if (var5.equals("RealmClassName")) {
                     var3._conditionalUnset(var2.isUnsetUpdate(), 7);
                  } else if (var5.equals("SSLEnable")) {
                     var3.setSSLEnable(var4.getSSLEnable());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 12);
                  } else if (var5.equals("UserAuthentication")) {
                     var3.setUserAuthentication(var4.getUserAuthentication());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 15);
                  } else if (var5.equals("UserDN")) {
                     var3.setUserDN(var4.getUserDN());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 17);
                  } else if (var5.equals("UserNameAttribute")) {
                     var3.setUserNameAttribute(var4.getUserNameAttribute());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 18);
                  } else if (var5.equals("UserPasswordAttribute")) {
                     var3.setUserPasswordAttribute(var4.getUserPasswordAttribute());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 16);
                  } else {
                     super.applyPropertyUpdate(var1, var2);
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
            LDAPRealmMBeanImpl var5 = (LDAPRealmMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("AuthProtocol")) && this.bean.isAuthProtocolSet()) {
               var5.setAuthProtocol(this.bean.getAuthProtocol());
            }

            if ((var3 == null || !var3.contains("CredentialEncrypted")) && this.bean.isCredentialEncryptedSet()) {
               byte[] var4 = this.bean.getCredentialEncrypted();
               var5.setCredentialEncrypted(var4 == null ? null : (byte[])((byte[])((byte[])((byte[])var4)).clone()));
            }

            if ((var3 == null || !var3.contains("GroupDN")) && this.bean.isGroupDNSet()) {
               var5.setGroupDN(this.bean.getGroupDN());
            }

            if ((var3 == null || !var3.contains("GroupIsContext")) && this.bean.isGroupIsContextSet()) {
               var5.setGroupIsContext(this.bean.getGroupIsContext());
            }

            if ((var3 == null || !var3.contains("GroupNameAttribute")) && this.bean.isGroupNameAttributeSet()) {
               var5.setGroupNameAttribute(this.bean.getGroupNameAttribute());
            }

            if ((var3 == null || !var3.contains("GroupUsernameAttribute")) && this.bean.isGroupUsernameAttributeSet()) {
               var5.setGroupUsernameAttribute(this.bean.getGroupUsernameAttribute());
            }

            if ((var3 == null || !var3.contains("LDAPURL")) && this.bean.isLDAPURLSet()) {
               var5.setLDAPURL(this.bean.getLDAPURL());
            }

            if ((var3 == null || !var3.contains("LdapProvider")) && this.bean.isLdapProviderSet()) {
               var5.setLdapProvider(this.bean.getLdapProvider());
            }

            if ((var3 == null || !var3.contains("Name")) && this.bean.isNameSet()) {
               var5.setName(this.bean.getName());
            }

            if ((var3 == null || !var3.contains("Principal")) && this.bean.isPrincipalSet()) {
               var5.setPrincipal(this.bean.getPrincipal());
            }

            if ((var3 == null || !var3.contains("RealmClassName")) && this.bean.isRealmClassNameSet()) {
            }

            if ((var3 == null || !var3.contains("SSLEnable")) && this.bean.isSSLEnableSet()) {
               var5.setSSLEnable(this.bean.getSSLEnable());
            }

            if ((var3 == null || !var3.contains("UserAuthentication")) && this.bean.isUserAuthenticationSet()) {
               var5.setUserAuthentication(this.bean.getUserAuthentication());
            }

            if ((var3 == null || !var3.contains("UserDN")) && this.bean.isUserDNSet()) {
               var5.setUserDN(this.bean.getUserDN());
            }

            if ((var3 == null || !var3.contains("UserNameAttribute")) && this.bean.isUserNameAttributeSet()) {
               var5.setUserNameAttribute(this.bean.getUserNameAttribute());
            }

            if ((var3 == null || !var3.contains("UserPasswordAttribute")) && this.bean.isUserPasswordAttributeSet()) {
               var5.setUserPasswordAttribute(this.bean.getUserPasswordAttribute());
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
      }
   }
}
