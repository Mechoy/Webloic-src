package weblogic.management.configuration;

import java.io.Serializable;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.zip.CRC32;
import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.BootstrapProperties;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBeanHelper;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.utils.ArrayUtils;
import weblogic.utils.collections.CombinedIterator;

public class WLECConnectionPoolMBeanImpl extends DeploymentMBeanImpl implements WLECConnectionPoolMBean, Serializable {
   private String _ApplicationPassword;
   private byte[] _ApplicationPasswordEncrypted;
   private boolean _CertificateAuthenticationEnabled;
   private String[] _FailoverAddresses;
   private int _MaximumEncryptionLevel;
   private int _MaximumPoolSize;
   private int _MinimumEncryptionLevel;
   private int _MinimumPoolSize;
   private String[] _PrimaryAddresses;
   private boolean _SecurityContextEnabled;
   private String _UserName;
   private String _UserPassword;
   private byte[] _UserPasswordEncrypted;
   private String _UserRole;
   private String _WLEDomain;
   private static SchemaHelper2 _schemaHelper;

   public WLECConnectionPoolMBeanImpl() {
      this._initializeProperty(-1);
   }

   public WLECConnectionPoolMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public String[] getPrimaryAddresses() {
      return this._PrimaryAddresses;
   }

   public boolean isPrimaryAddressesSet() {
      return this._isSet(9);
   }

   public void setPrimaryAddresses(String[] var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? new String[0] : var1;
      var1 = this._getHelper()._trimElements(var1);
      String[] var2 = this._PrimaryAddresses;
      this._PrimaryAddresses = var1;
      this._postSet(9, var2, var1);
   }

   public boolean addPrimaryAddress(String var1) throws InvalidAttributeValueException {
      this._getHelper()._ensureNonNull(var1);
      String[] var2;
      if (this._isSet(9)) {
         var2 = (String[])((String[])this._getHelper()._extendArray(this.getPrimaryAddresses(), String.class, var1));
      } else {
         var2 = new String[]{var1};
      }

      try {
         this.setPrimaryAddresses(var2);
         return true;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else if (var4 instanceof InvalidAttributeValueException) {
            throw (InvalidAttributeValueException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public boolean removePrimaryAddress(String var1) {
      String[] var2 = this.getPrimaryAddresses();
      String[] var3 = (String[])((String[])this._getHelper()._removeElement(var2, String.class, var1));
      if (var3.length != var2.length) {
         try {
            this.setPrimaryAddresses(var3);
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

   public String[] getFailoverAddresses() {
      return this._FailoverAddresses;
   }

   public boolean isFailoverAddressesSet() {
      return this._isSet(10);
   }

   public void setFailoverAddresses(String[] var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? new String[0] : var1;
      var1 = this._getHelper()._trimElements(var1);
      String[] var2 = this._FailoverAddresses;
      this._FailoverAddresses = var1;
      this._postSet(10, var2, var1);
   }

   public boolean addFailoverAddress(String var1) throws InvalidAttributeValueException {
      this._getHelper()._ensureNonNull(var1);
      String[] var2;
      if (this._isSet(10)) {
         var2 = (String[])((String[])this._getHelper()._extendArray(this.getFailoverAddresses(), String.class, var1));
      } else {
         var2 = new String[]{var1};
      }

      try {
         this.setFailoverAddresses(var2);
         return true;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else if (var4 instanceof InvalidAttributeValueException) {
            throw (InvalidAttributeValueException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public boolean removeFailoverAddress(String var1) {
      String[] var2 = this.getFailoverAddresses();
      String[] var3 = (String[])((String[])this._getHelper()._removeElement(var2, String.class, var1));
      if (var3.length != var2.length) {
         try {
            this.setFailoverAddresses(var3);
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

   public int getMinimumPoolSize() {
      return this._MinimumPoolSize;
   }

   public boolean isMinimumPoolSizeSet() {
      return this._isSet(11);
   }

   public void setMinimumPoolSize(int var1) throws InvalidAttributeValueException {
      int var2 = this._MinimumPoolSize;
      this._MinimumPoolSize = var1;
      this._postSet(11, var2, var1);
   }

   public int getMaximumPoolSize() {
      return this._MaximumPoolSize;
   }

   public boolean isMaximumPoolSizeSet() {
      return this._isSet(12);
   }

   public void setMaximumPoolSize(int var1) throws InvalidAttributeValueException {
      int var2 = this._MaximumPoolSize;
      this._MaximumPoolSize = var1;
      this._postSet(12, var2, var1);
   }

   public String getUserName() {
      return this._UserName;
   }

   public boolean isUserNameSet() {
      return this._isSet(13);
   }

   public void setUserName(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._UserName;
      this._UserName = var1;
      this._postSet(13, var2, var1);
   }

   public String getUserPassword() {
      byte[] var1 = this.getUserPasswordEncrypted();
      return var1 == null ? null : this._decrypt("UserPassword", var1);
   }

   public boolean isUserPasswordSet() {
      return this.isUserPasswordEncryptedSet();
   }

   public void setUserPassword(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      this.setUserPasswordEncrypted(var1 == null ? null : this._encrypt("UserPassword", var1));
   }

   public byte[] getUserPasswordEncrypted() {
      return this._getHelper()._cloneArray(this._UserPasswordEncrypted);
   }

   public String getUserPasswordEncryptedAsString() {
      byte[] var1 = this.getUserPasswordEncrypted();
      return var1 == null ? null : new String(var1);
   }

   public boolean isUserPasswordEncryptedSet() {
      return this._isSet(15);
   }

   public void setUserPasswordEncryptedAsString(String var1) {
      try {
         byte[] var2 = var1 == null ? null : var1.getBytes();
         this.setUserPasswordEncrypted(var2);
      } catch (Exception var3) {
         if (var3 instanceof RuntimeException) {
            throw (RuntimeException)var3;
         } else {
            throw new UndeclaredThrowableException(var3);
         }
      }
   }

   public String getApplicationPassword() {
      byte[] var1 = this.getApplicationPasswordEncrypted();
      return var1 == null ? null : this._decrypt("ApplicationPassword", var1);
   }

   public boolean isApplicationPasswordSet() {
      return this.isApplicationPasswordEncryptedSet();
   }

   public void setApplicationPassword(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      this.setApplicationPasswordEncrypted(var1 == null ? null : this._encrypt("ApplicationPassword", var1));
   }

   public byte[] getApplicationPasswordEncrypted() {
      return this._getHelper()._cloneArray(this._ApplicationPasswordEncrypted);
   }

   public String getApplicationPasswordEncryptedAsString() {
      byte[] var1 = this.getApplicationPasswordEncrypted();
      return var1 == null ? null : new String(var1);
   }

   public boolean isApplicationPasswordEncryptedSet() {
      return this._isSet(17);
   }

   public void setApplicationPasswordEncryptedAsString(String var1) {
      try {
         byte[] var2 = var1 == null ? null : var1.getBytes();
         this.setApplicationPasswordEncrypted(var2);
      } catch (Exception var3) {
         if (var3 instanceof RuntimeException) {
            throw (RuntimeException)var3;
         } else {
            throw new UndeclaredThrowableException(var3);
         }
      }
   }

   public String getUserRole() {
      return this._UserRole;
   }

   public boolean isUserRoleSet() {
      return this._isSet(18);
   }

   public void setUserRole(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._UserRole;
      this._UserRole = var1;
      this._postSet(18, var2, var1);
   }

   public String getWLEDomain() {
      return this._WLEDomain;
   }

   public boolean isWLEDomainSet() {
      return this._isSet(19);
   }

   public void setWLEDomain(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._WLEDomain;
      this._WLEDomain = var1;
      this._postSet(19, var2, var1);
   }

   public int getMinimumEncryptionLevel() {
      return this._MinimumEncryptionLevel;
   }

   public boolean isMinimumEncryptionLevelSet() {
      return this._isSet(20);
   }

   public void setMinimumEncryptionLevel(int var1) throws InvalidAttributeValueException {
      int var2 = this._MinimumEncryptionLevel;
      this._MinimumEncryptionLevel = var1;
      this._postSet(20, var2, var1);
   }

   public int getMaximumEncryptionLevel() {
      return this._MaximumEncryptionLevel;
   }

   public boolean isMaximumEncryptionLevelSet() {
      return this._isSet(21);
   }

   public void setMaximumEncryptionLevel(int var1) throws InvalidAttributeValueException {
      int var2 = this._MaximumEncryptionLevel;
      this._MaximumEncryptionLevel = var1;
      this._postSet(21, var2, var1);
   }

   public boolean isCertificateAuthenticationEnabled() {
      return this._CertificateAuthenticationEnabled;
   }

   public boolean isCertificateAuthenticationEnabledSet() {
      return this._isSet(22);
   }

   public void setCertificateAuthenticationEnabled(boolean var1) {
      boolean var2 = this._CertificateAuthenticationEnabled;
      this._CertificateAuthenticationEnabled = var1;
      this._postSet(22, var2, var1);
   }

   public boolean isSecurityContextEnabled() {
      return this._SecurityContextEnabled;
   }

   public boolean isSecurityContextEnabledSet() {
      return this._isSet(23);
   }

   public void setSecurityContextEnabled(boolean var1) {
      boolean var2 = this._SecurityContextEnabled;
      this._SecurityContextEnabled = var1;
      this._postSet(23, var2, var1);
   }

   public Object _getKey() {
      return super._getKey();
   }

   public void _validate() throws IllegalArgumentException {
      super._validate();
   }

   public void setApplicationPasswordEncrypted(byte[] var1) {
      byte[] var2 = this._ApplicationPasswordEncrypted;
      if (this._isProductionModeEnabled() && var1 != null && !this._isEncrypted(var1)) {
         throw new IllegalArgumentException("In production mode, it's not allowed to set a clear text value to the property: ApplicationPasswordEncrypted of WLECConnectionPoolMBean");
      } else {
         this._getHelper()._clearArray(this._ApplicationPasswordEncrypted);
         this._ApplicationPasswordEncrypted = this._getHelper()._cloneArray(var1);
         this._postSet(17, var2, var1);
      }
   }

   public void setUserPasswordEncrypted(byte[] var1) {
      byte[] var2 = this._UserPasswordEncrypted;
      if (this._isProductionModeEnabled() && var1 != null && !this._isEncrypted(var1)) {
         throw new IllegalArgumentException("In production mode, it's not allowed to set a clear text value to the property: UserPasswordEncrypted of WLECConnectionPoolMBean");
      } else {
         this._getHelper()._clearArray(this._UserPasswordEncrypted);
         this._UserPasswordEncrypted = this._getHelper()._cloneArray(var1);
         this._postSet(15, var2, var1);
      }
   }

   protected void _unSet(int var1) {
      if (!this._initializeProperty(var1)) {
         super._unSet(var1);
      } else {
         this._markSet(var1, false);
         if (var1 == 16) {
            this._markSet(17, false);
         }

         if (var1 == 14) {
            this._markSet(15, false);
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
         var1 = 16;
      }

      try {
         switch (var1) {
            case 16:
               this._ApplicationPasswordEncrypted = null;
               if (var2) {
                  break;
               }
            case 17:
               this._ApplicationPasswordEncrypted = null;
               if (var2) {
                  break;
               }
            case 10:
               this._FailoverAddresses = new String[0];
               if (var2) {
                  break;
               }
            case 21:
               this._MaximumEncryptionLevel = 128;
               if (var2) {
                  break;
               }
            case 12:
               this._MaximumPoolSize = 1;
               if (var2) {
                  break;
               }
            case 20:
               this._MinimumEncryptionLevel = 40;
               if (var2) {
                  break;
               }
            case 11:
               this._MinimumPoolSize = 1;
               if (var2) {
                  break;
               }
            case 9:
               this._PrimaryAddresses = new String[0];
               if (var2) {
                  break;
               }
            case 13:
               this._UserName = null;
               if (var2) {
                  break;
               }
            case 14:
               this._UserPasswordEncrypted = null;
               if (var2) {
                  break;
               }
            case 15:
               this._UserPasswordEncrypted = null;
               if (var2) {
                  break;
               }
            case 18:
               this._UserRole = null;
               if (var2) {
                  break;
               }
            case 19:
               this._WLEDomain = null;
               if (var2) {
                  break;
               }
            case 22:
               this._CertificateAuthenticationEnabled = false;
               if (var2) {
                  break;
               }
            case 23:
               this._SecurityContextEnabled = false;
               if (var2) {
                  break;
               }
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
      return "WLECConnectionPool";
   }

   public void putValue(String var1, Object var2) {
      String var3;
      if (var1.equals("ApplicationPassword")) {
         var3 = this._ApplicationPassword;
         this._ApplicationPassword = (String)var2;
         this._postSet(16, var3, this._ApplicationPassword);
      } else {
         byte[] var4;
         if (var1.equals("ApplicationPasswordEncrypted")) {
            var4 = this._ApplicationPasswordEncrypted;
            this._ApplicationPasswordEncrypted = (byte[])((byte[])var2);
            this._postSet(17, var4, this._ApplicationPasswordEncrypted);
         } else {
            boolean var5;
            if (var1.equals("CertificateAuthenticationEnabled")) {
               var5 = this._CertificateAuthenticationEnabled;
               this._CertificateAuthenticationEnabled = (Boolean)var2;
               this._postSet(22, var5, this._CertificateAuthenticationEnabled);
            } else {
               String[] var6;
               if (var1.equals("FailoverAddresses")) {
                  var6 = this._FailoverAddresses;
                  this._FailoverAddresses = (String[])((String[])var2);
                  this._postSet(10, var6, this._FailoverAddresses);
               } else {
                  int var7;
                  if (var1.equals("MaximumEncryptionLevel")) {
                     var7 = this._MaximumEncryptionLevel;
                     this._MaximumEncryptionLevel = (Integer)var2;
                     this._postSet(21, var7, this._MaximumEncryptionLevel);
                  } else if (var1.equals("MaximumPoolSize")) {
                     var7 = this._MaximumPoolSize;
                     this._MaximumPoolSize = (Integer)var2;
                     this._postSet(12, var7, this._MaximumPoolSize);
                  } else if (var1.equals("MinimumEncryptionLevel")) {
                     var7 = this._MinimumEncryptionLevel;
                     this._MinimumEncryptionLevel = (Integer)var2;
                     this._postSet(20, var7, this._MinimumEncryptionLevel);
                  } else if (var1.equals("MinimumPoolSize")) {
                     var7 = this._MinimumPoolSize;
                     this._MinimumPoolSize = (Integer)var2;
                     this._postSet(11, var7, this._MinimumPoolSize);
                  } else if (var1.equals("PrimaryAddresses")) {
                     var6 = this._PrimaryAddresses;
                     this._PrimaryAddresses = (String[])((String[])var2);
                     this._postSet(9, var6, this._PrimaryAddresses);
                  } else if (var1.equals("SecurityContextEnabled")) {
                     var5 = this._SecurityContextEnabled;
                     this._SecurityContextEnabled = (Boolean)var2;
                     this._postSet(23, var5, this._SecurityContextEnabled);
                  } else if (var1.equals("UserName")) {
                     var3 = this._UserName;
                     this._UserName = (String)var2;
                     this._postSet(13, var3, this._UserName);
                  } else if (var1.equals("UserPassword")) {
                     var3 = this._UserPassword;
                     this._UserPassword = (String)var2;
                     this._postSet(14, var3, this._UserPassword);
                  } else if (var1.equals("UserPasswordEncrypted")) {
                     var4 = this._UserPasswordEncrypted;
                     this._UserPasswordEncrypted = (byte[])((byte[])var2);
                     this._postSet(15, var4, this._UserPasswordEncrypted);
                  } else if (var1.equals("UserRole")) {
                     var3 = this._UserRole;
                     this._UserRole = (String)var2;
                     this._postSet(18, var3, this._UserRole);
                  } else if (var1.equals("WLEDomain")) {
                     var3 = this._WLEDomain;
                     this._WLEDomain = (String)var2;
                     this._postSet(19, var3, this._WLEDomain);
                  } else {
                     super.putValue(var1, var2);
                  }
               }
            }
         }
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("ApplicationPassword")) {
         return this._ApplicationPassword;
      } else if (var1.equals("ApplicationPasswordEncrypted")) {
         return this._ApplicationPasswordEncrypted;
      } else if (var1.equals("CertificateAuthenticationEnabled")) {
         return new Boolean(this._CertificateAuthenticationEnabled);
      } else if (var1.equals("FailoverAddresses")) {
         return this._FailoverAddresses;
      } else if (var1.equals("MaximumEncryptionLevel")) {
         return new Integer(this._MaximumEncryptionLevel);
      } else if (var1.equals("MaximumPoolSize")) {
         return new Integer(this._MaximumPoolSize);
      } else if (var1.equals("MinimumEncryptionLevel")) {
         return new Integer(this._MinimumEncryptionLevel);
      } else if (var1.equals("MinimumPoolSize")) {
         return new Integer(this._MinimumPoolSize);
      } else if (var1.equals("PrimaryAddresses")) {
         return this._PrimaryAddresses;
      } else if (var1.equals("SecurityContextEnabled")) {
         return new Boolean(this._SecurityContextEnabled);
      } else if (var1.equals("UserName")) {
         return this._UserName;
      } else if (var1.equals("UserPassword")) {
         return this._UserPassword;
      } else if (var1.equals("UserPasswordEncrypted")) {
         return this._UserPasswordEncrypted;
      } else if (var1.equals("UserRole")) {
         return this._UserRole;
      } else {
         return var1.equals("WLEDomain") ? this._WLEDomain : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends DeploymentMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 9:
               if (var1.equals("user-name")) {
                  return 13;
               }

               if (var1.equals("user-role")) {
                  return 18;
               }
               break;
            case 10:
               if (var1.equals("wle-domain")) {
                  return 19;
               }
            case 11:
            case 12:
            case 14:
            case 18:
            case 19:
            case 21:
            case 22:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 31:
            case 32:
            case 33:
            default:
               break;
            case 13:
               if (var1.equals("user-password")) {
                  return 14;
               }
               break;
            case 15:
               if (var1.equals("primary-address")) {
                  return 9;
               }
               break;
            case 16:
               if (var1.equals("failover-address")) {
                  return 10;
               }
               break;
            case 17:
               if (var1.equals("maximum-pool-size")) {
                  return 12;
               }

               if (var1.equals("minimum-pool-size")) {
                  return 11;
               }
               break;
            case 20:
               if (var1.equals("application-password")) {
                  return 16;
               }
               break;
            case 23:
               if (var1.equals("user-password-encrypted")) {
                  return 15;
               }
               break;
            case 24:
               if (var1.equals("maximum-encryption-level")) {
                  return 21;
               }

               if (var1.equals("minimum-encryption-level")) {
                  return 20;
               }

               if (var1.equals("security-context-enabled")) {
                  return 23;
               }
               break;
            case 30:
               if (var1.equals("application-password-encrypted")) {
                  return 17;
               }
               break;
            case 34:
               if (var1.equals("certificate-authentication-enabled")) {
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
            case 9:
               return "primary-address";
            case 10:
               return "failover-address";
            case 11:
               return "minimum-pool-size";
            case 12:
               return "maximum-pool-size";
            case 13:
               return "user-name";
            case 14:
               return "user-password";
            case 15:
               return "user-password-encrypted";
            case 16:
               return "application-password";
            case 17:
               return "application-password-encrypted";
            case 18:
               return "user-role";
            case 19:
               return "wle-domain";
            case 20:
               return "minimum-encryption-level";
            case 21:
               return "maximum-encryption-level";
            case 22:
               return "certificate-authentication-enabled";
            case 23:
               return "security-context-enabled";
            default:
               return super.getElementName(var1);
         }
      }

      public boolean isArray(int var1) {
         switch (var1) {
            case 7:
               return true;
            case 8:
            default:
               return super.isArray(var1);
            case 9:
               return true;
            case 10:
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

      public String[] getKeyElementNames() {
         ArrayList var1 = new ArrayList();
         var1.add("name");
         return (String[])((String[])var1.toArray(new String[0]));
      }
   }

   protected static class Helper extends DeploymentMBeanImpl.Helper {
      private WLECConnectionPoolMBeanImpl bean;

      protected Helper(WLECConnectionPoolMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 9:
               return "PrimaryAddresses";
            case 10:
               return "FailoverAddresses";
            case 11:
               return "MinimumPoolSize";
            case 12:
               return "MaximumPoolSize";
            case 13:
               return "UserName";
            case 14:
               return "UserPassword";
            case 15:
               return "UserPasswordEncrypted";
            case 16:
               return "ApplicationPassword";
            case 17:
               return "ApplicationPasswordEncrypted";
            case 18:
               return "UserRole";
            case 19:
               return "WLEDomain";
            case 20:
               return "MinimumEncryptionLevel";
            case 21:
               return "MaximumEncryptionLevel";
            case 22:
               return "CertificateAuthenticationEnabled";
            case 23:
               return "SecurityContextEnabled";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("ApplicationPassword")) {
            return 16;
         } else if (var1.equals("ApplicationPasswordEncrypted")) {
            return 17;
         } else if (var1.equals("FailoverAddresses")) {
            return 10;
         } else if (var1.equals("MaximumEncryptionLevel")) {
            return 21;
         } else if (var1.equals("MaximumPoolSize")) {
            return 12;
         } else if (var1.equals("MinimumEncryptionLevel")) {
            return 20;
         } else if (var1.equals("MinimumPoolSize")) {
            return 11;
         } else if (var1.equals("PrimaryAddresses")) {
            return 9;
         } else if (var1.equals("UserName")) {
            return 13;
         } else if (var1.equals("UserPassword")) {
            return 14;
         } else if (var1.equals("UserPasswordEncrypted")) {
            return 15;
         } else if (var1.equals("UserRole")) {
            return 18;
         } else if (var1.equals("WLEDomain")) {
            return 19;
         } else if (var1.equals("CertificateAuthenticationEnabled")) {
            return 22;
         } else {
            return var1.equals("SecurityContextEnabled") ? 23 : super.getPropertyIndex(var1);
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
            if (this.bean.isApplicationPasswordSet()) {
               var2.append("ApplicationPassword");
               var2.append(String.valueOf(this.bean.getApplicationPassword()));
            }

            if (this.bean.isApplicationPasswordEncryptedSet()) {
               var2.append("ApplicationPasswordEncrypted");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getApplicationPasswordEncrypted())));
            }

            if (this.bean.isFailoverAddressesSet()) {
               var2.append("FailoverAddresses");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getFailoverAddresses())));
            }

            if (this.bean.isMaximumEncryptionLevelSet()) {
               var2.append("MaximumEncryptionLevel");
               var2.append(String.valueOf(this.bean.getMaximumEncryptionLevel()));
            }

            if (this.bean.isMaximumPoolSizeSet()) {
               var2.append("MaximumPoolSize");
               var2.append(String.valueOf(this.bean.getMaximumPoolSize()));
            }

            if (this.bean.isMinimumEncryptionLevelSet()) {
               var2.append("MinimumEncryptionLevel");
               var2.append(String.valueOf(this.bean.getMinimumEncryptionLevel()));
            }

            if (this.bean.isMinimumPoolSizeSet()) {
               var2.append("MinimumPoolSize");
               var2.append(String.valueOf(this.bean.getMinimumPoolSize()));
            }

            if (this.bean.isPrimaryAddressesSet()) {
               var2.append("PrimaryAddresses");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getPrimaryAddresses())));
            }

            if (this.bean.isUserNameSet()) {
               var2.append("UserName");
               var2.append(String.valueOf(this.bean.getUserName()));
            }

            if (this.bean.isUserPasswordSet()) {
               var2.append("UserPassword");
               var2.append(String.valueOf(this.bean.getUserPassword()));
            }

            if (this.bean.isUserPasswordEncryptedSet()) {
               var2.append("UserPasswordEncrypted");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getUserPasswordEncrypted())));
            }

            if (this.bean.isUserRoleSet()) {
               var2.append("UserRole");
               var2.append(String.valueOf(this.bean.getUserRole()));
            }

            if (this.bean.isWLEDomainSet()) {
               var2.append("WLEDomain");
               var2.append(String.valueOf(this.bean.getWLEDomain()));
            }

            if (this.bean.isCertificateAuthenticationEnabledSet()) {
               var2.append("CertificateAuthenticationEnabled");
               var2.append(String.valueOf(this.bean.isCertificateAuthenticationEnabled()));
            }

            if (this.bean.isSecurityContextEnabledSet()) {
               var2.append("SecurityContextEnabled");
               var2.append(String.valueOf(this.bean.isSecurityContextEnabled()));
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
            WLECConnectionPoolMBeanImpl var2 = (WLECConnectionPoolMBeanImpl)var1;
            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("ApplicationPasswordEncrypted", this.bean.getApplicationPasswordEncrypted(), var2.getApplicationPasswordEncrypted(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("FailoverAddresses", this.bean.getFailoverAddresses(), var2.getFailoverAddresses(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("MaximumEncryptionLevel", this.bean.getMaximumEncryptionLevel(), var2.getMaximumEncryptionLevel(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("MaximumPoolSize", this.bean.getMaximumPoolSize(), var2.getMaximumPoolSize(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("MinimumEncryptionLevel", this.bean.getMinimumEncryptionLevel(), var2.getMinimumEncryptionLevel(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("MinimumPoolSize", this.bean.getMinimumPoolSize(), var2.getMinimumPoolSize(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("PrimaryAddresses", this.bean.getPrimaryAddresses(), var2.getPrimaryAddresses(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("UserName", this.bean.getUserName(), var2.getUserName(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("UserPasswordEncrypted", this.bean.getUserPasswordEncrypted(), var2.getUserPasswordEncrypted(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("UserRole", this.bean.getUserRole(), var2.getUserRole(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("WLEDomain", this.bean.getWLEDomain(), var2.getWLEDomain(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("CertificateAuthenticationEnabled", this.bean.isCertificateAuthenticationEnabled(), var2.isCertificateAuthenticationEnabled(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("SecurityContextEnabled", this.bean.isSecurityContextEnabled(), var2.isSecurityContextEnabled(), false);
            }

         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            WLECConnectionPoolMBeanImpl var3 = (WLECConnectionPoolMBeanImpl)var1.getSourceBean();
            WLECConnectionPoolMBeanImpl var4 = (WLECConnectionPoolMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (!var5.equals("ApplicationPassword")) {
                  if (var5.equals("ApplicationPasswordEncrypted")) {
                     var3.setApplicationPasswordEncrypted(var4.getApplicationPasswordEncrypted());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 17);
                  } else if (var5.equals("FailoverAddresses")) {
                     if (var6 == 2) {
                        var2.resetAddedObject(var2.getAddedObject());
                        var3.addFailoverAddress((String)var2.getAddedObject());
                     } else {
                        if (var6 != 3) {
                           throw new AssertionError("Invalid type: " + var6);
                        }

                        var3.removeFailoverAddress((String)var2.getRemovedObject());
                     }

                     if (var3.getFailoverAddresses() == null || var3.getFailoverAddresses().length == 0) {
                        var3._conditionalUnset(var2.isUnsetUpdate(), 10);
                     }
                  } else if (var5.equals("MaximumEncryptionLevel")) {
                     var3.setMaximumEncryptionLevel(var4.getMaximumEncryptionLevel());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 21);
                  } else if (var5.equals("MaximumPoolSize")) {
                     var3.setMaximumPoolSize(var4.getMaximumPoolSize());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 12);
                  } else if (var5.equals("MinimumEncryptionLevel")) {
                     var3.setMinimumEncryptionLevel(var4.getMinimumEncryptionLevel());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 20);
                  } else if (var5.equals("MinimumPoolSize")) {
                     var3.setMinimumPoolSize(var4.getMinimumPoolSize());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 11);
                  } else if (var5.equals("PrimaryAddresses")) {
                     if (var6 == 2) {
                        var2.resetAddedObject(var2.getAddedObject());
                        var3.addPrimaryAddress((String)var2.getAddedObject());
                     } else {
                        if (var6 != 3) {
                           throw new AssertionError("Invalid type: " + var6);
                        }

                        var3.removePrimaryAddress((String)var2.getRemovedObject());
                     }

                     if (var3.getPrimaryAddresses() == null || var3.getPrimaryAddresses().length == 0) {
                        var3._conditionalUnset(var2.isUnsetUpdate(), 9);
                     }
                  } else if (var5.equals("UserName")) {
                     var3.setUserName(var4.getUserName());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 13);
                  } else if (!var5.equals("UserPassword")) {
                     if (var5.equals("UserPasswordEncrypted")) {
                        var3.setUserPasswordEncrypted(var4.getUserPasswordEncrypted());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 15);
                     } else if (var5.equals("UserRole")) {
                        var3.setUserRole(var4.getUserRole());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 18);
                     } else if (var5.equals("WLEDomain")) {
                        var3.setWLEDomain(var4.getWLEDomain());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 19);
                     } else if (var5.equals("CertificateAuthenticationEnabled")) {
                        var3.setCertificateAuthenticationEnabled(var4.isCertificateAuthenticationEnabled());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 22);
                     } else if (var5.equals("SecurityContextEnabled")) {
                        var3.setSecurityContextEnabled(var4.isSecurityContextEnabled());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 23);
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
            WLECConnectionPoolMBeanImpl var5 = (WLECConnectionPoolMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            byte[] var4;
            if (var2 && (var3 == null || !var3.contains("ApplicationPasswordEncrypted")) && this.bean.isApplicationPasswordEncryptedSet()) {
               var4 = this.bean.getApplicationPasswordEncrypted();
               var5.setApplicationPasswordEncrypted(var4 == null ? null : (byte[])((byte[])((byte[])((byte[])var4)).clone()));
            }

            String[] var8;
            if (var2 && (var3 == null || !var3.contains("FailoverAddresses")) && this.bean.isFailoverAddressesSet()) {
               var8 = this.bean.getFailoverAddresses();
               var5.setFailoverAddresses(var8 == null ? null : (String[])((String[])((String[])((String[])var8)).clone()));
            }

            if (var2 && (var3 == null || !var3.contains("MaximumEncryptionLevel")) && this.bean.isMaximumEncryptionLevelSet()) {
               var5.setMaximumEncryptionLevel(this.bean.getMaximumEncryptionLevel());
            }

            if (var2 && (var3 == null || !var3.contains("MaximumPoolSize")) && this.bean.isMaximumPoolSizeSet()) {
               var5.setMaximumPoolSize(this.bean.getMaximumPoolSize());
            }

            if (var2 && (var3 == null || !var3.contains("MinimumEncryptionLevel")) && this.bean.isMinimumEncryptionLevelSet()) {
               var5.setMinimumEncryptionLevel(this.bean.getMinimumEncryptionLevel());
            }

            if (var2 && (var3 == null || !var3.contains("MinimumPoolSize")) && this.bean.isMinimumPoolSizeSet()) {
               var5.setMinimumPoolSize(this.bean.getMinimumPoolSize());
            }

            if (var2 && (var3 == null || !var3.contains("PrimaryAddresses")) && this.bean.isPrimaryAddressesSet()) {
               var8 = this.bean.getPrimaryAddresses();
               var5.setPrimaryAddresses(var8 == null ? null : (String[])((String[])((String[])((String[])var8)).clone()));
            }

            if (var2 && (var3 == null || !var3.contains("UserName")) && this.bean.isUserNameSet()) {
               var5.setUserName(this.bean.getUserName());
            }

            if (var2 && (var3 == null || !var3.contains("UserPasswordEncrypted")) && this.bean.isUserPasswordEncryptedSet()) {
               var4 = this.bean.getUserPasswordEncrypted();
               var5.setUserPasswordEncrypted(var4 == null ? null : (byte[])((byte[])((byte[])((byte[])var4)).clone()));
            }

            if (var2 && (var3 == null || !var3.contains("UserRole")) && this.bean.isUserRoleSet()) {
               var5.setUserRole(this.bean.getUserRole());
            }

            if (var2 && (var3 == null || !var3.contains("WLEDomain")) && this.bean.isWLEDomainSet()) {
               var5.setWLEDomain(this.bean.getWLEDomain());
            }

            if (var2 && (var3 == null || !var3.contains("CertificateAuthenticationEnabled")) && this.bean.isCertificateAuthenticationEnabledSet()) {
               var5.setCertificateAuthenticationEnabled(this.bean.isCertificateAuthenticationEnabled());
            }

            if (var2 && (var3 == null || !var3.contains("SecurityContextEnabled")) && this.bean.isSecurityContextEnabledSet()) {
               var5.setSecurityContextEnabled(this.bean.isSecurityContextEnabled());
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
