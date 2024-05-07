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
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBeanHelper;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.utils.ArrayUtils;
import weblogic.utils.collections.CombinedIterator;

public class JoltConnectionPoolMBeanImpl extends DeploymentMBeanImpl implements JoltConnectionPoolMBean, Serializable {
   private String _ApplicationPassword;
   private byte[] _ApplicationPasswordEncrypted;
   private String[] _FailoverAddresses;
   private String _KeyPassPhrase;
   private byte[] _KeyPassPhraseEncrypted;
   private String _KeyStoreName;
   private String _KeyStorePassPhrase;
   private byte[] _KeyStorePassPhraseEncrypted;
   private int _MaximumPoolSize;
   private int _MinimumPoolSize;
   private String[] _PrimaryAddresses;
   private int _RecvTimeout;
   private boolean _SecurityContextEnabled;
   private String _TrustStoreName;
   private String _TrustStorePassPhrase;
   private byte[] _TrustStorePassPhraseEncrypted;
   private String _UserName;
   private String _UserPassword;
   private byte[] _UserPasswordEncrypted;
   private String _UserRole;
   private static SchemaHelper2 _schemaHelper;

   public JoltConnectionPoolMBeanImpl() {
      this._initializeProperty(-1);
   }

   public JoltConnectionPoolMBeanImpl(DescriptorBean var1, int var2) {
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

   public boolean removePrimaryAddress(String var1) throws InvalidAttributeValueException {
      String[] var2 = this.getPrimaryAddresses();
      String[] var3 = (String[])((String[])this._getHelper()._removeElement(var2, String.class, var1));
      if (var3.length != var2.length) {
         try {
            this.setPrimaryAddresses(var3);
            return true;
         } catch (Exception var5) {
            if (var5 instanceof RuntimeException) {
               throw (RuntimeException)var5;
            } else if (var5 instanceof InvalidAttributeValueException) {
               throw (InvalidAttributeValueException)var5;
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

   public boolean removeFailoverAddress(String var1) throws InvalidAttributeValueException {
      String[] var2 = this.getFailoverAddresses();
      String[] var3 = (String[])((String[])this._getHelper()._removeElement(var2, String.class, var1));
      if (var3.length != var2.length) {
         try {
            this.setFailoverAddresses(var3);
            return true;
         } catch (Exception var5) {
            if (var5 instanceof RuntimeException) {
               throw (RuntimeException)var5;
            } else if (var5 instanceof InvalidAttributeValueException) {
               throw (InvalidAttributeValueException)var5;
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
      weblogic.descriptor.beangen.LegalChecks.checkInRange("MinimumPoolSize", (long)var1, 0L, 2147483647L);
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
      weblogic.descriptor.beangen.LegalChecks.checkInRange("MaximumPoolSize", (long)var1, 1L, 2147483647L);
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

   public boolean isSecurityContextEnabled() {
      return this._SecurityContextEnabled;
   }

   public boolean isSecurityContextEnabledSet() {
      return this._isSet(19);
   }

   public void setSecurityContextEnabled(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this._SecurityContextEnabled;
      this._SecurityContextEnabled = var1;
      this._postSet(19, var2, var1);
   }

   public int getRecvTimeout() {
      return this._RecvTimeout;
   }

   public boolean isRecvTimeoutSet() {
      return this._isSet(20);
   }

   public void setRecvTimeout(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("RecvTimeout", (long)var1, 0L, 2147483647L);
      int var2 = this._RecvTimeout;
      this._RecvTimeout = var1;
      this._postSet(20, var2, var1);
   }

   public String getKeyStoreName() {
      return this._KeyStoreName;
   }

   public boolean isKeyStoreNameSet() {
      return this._isSet(21);
   }

   public void setKeyStoreName(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._KeyStoreName;
      this._KeyStoreName = var1;
      this._postSet(21, var2, var1);
   }

   public String getTrustStoreName() {
      return this._TrustStoreName;
   }

   public boolean isTrustStoreNameSet() {
      return this._isSet(22);
   }

   public void setTrustStoreName(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._TrustStoreName;
      this._TrustStoreName = var1;
      this._postSet(22, var2, var1);
   }

   public String getKeyPassPhrase() {
      byte[] var1 = this.getKeyPassPhraseEncrypted();
      return var1 == null ? null : this._decrypt("KeyPassPhrase", var1);
   }

   public boolean isKeyPassPhraseSet() {
      return this.isKeyPassPhraseEncryptedSet();
   }

   public void setKeyPassPhrase(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      this.setKeyPassPhraseEncrypted(var1 == null ? null : this._encrypt("KeyPassPhrase", var1));
   }

   public byte[] getKeyPassPhraseEncrypted() {
      return this._getHelper()._cloneArray(this._KeyPassPhraseEncrypted);
   }

   public String getKeyPassPhraseEncryptedAsString() {
      byte[] var1 = this.getKeyPassPhraseEncrypted();
      return var1 == null ? null : new String(var1);
   }

   public boolean isKeyPassPhraseEncryptedSet() {
      return this._isSet(24);
   }

   public void setKeyPassPhraseEncryptedAsString(String var1) {
      try {
         byte[] var2 = var1 == null ? null : var1.getBytes();
         this.setKeyPassPhraseEncrypted(var2);
      } catch (Exception var3) {
         if (var3 instanceof RuntimeException) {
            throw (RuntimeException)var3;
         } else {
            throw new UndeclaredThrowableException(var3);
         }
      }
   }

   public String getKeyStorePassPhrase() {
      byte[] var1 = this.getKeyStorePassPhraseEncrypted();
      return var1 == null ? null : this._decrypt("KeyStorePassPhrase", var1);
   }

   public boolean isKeyStorePassPhraseSet() {
      return this.isKeyStorePassPhraseEncryptedSet();
   }

   public void setKeyStorePassPhrase(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      this.setKeyStorePassPhraseEncrypted(var1 == null ? null : this._encrypt("KeyStorePassPhrase", var1));
   }

   public byte[] getKeyStorePassPhraseEncrypted() {
      return this._getHelper()._cloneArray(this._KeyStorePassPhraseEncrypted);
   }

   public String getKeyStorePassPhraseEncryptedAsString() {
      byte[] var1 = this.getKeyStorePassPhraseEncrypted();
      return var1 == null ? null : new String(var1);
   }

   public boolean isKeyStorePassPhraseEncryptedSet() {
      return this._isSet(26);
   }

   public void setKeyStorePassPhraseEncryptedAsString(String var1) {
      try {
         byte[] var2 = var1 == null ? null : var1.getBytes();
         this.setKeyStorePassPhraseEncrypted(var2);
      } catch (Exception var3) {
         if (var3 instanceof RuntimeException) {
            throw (RuntimeException)var3;
         } else {
            throw new UndeclaredThrowableException(var3);
         }
      }
   }

   public String getTrustStorePassPhrase() {
      byte[] var1 = this.getTrustStorePassPhraseEncrypted();
      return var1 == null ? null : this._decrypt("TrustStorePassPhrase", var1);
   }

   public boolean isTrustStorePassPhraseSet() {
      return this.isTrustStorePassPhraseEncryptedSet();
   }

   public void setTrustStorePassPhrase(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      this.setTrustStorePassPhraseEncrypted(var1 == null ? null : this._encrypt("TrustStorePassPhrase", var1));
   }

   public byte[] getTrustStorePassPhraseEncrypted() {
      return this._getHelper()._cloneArray(this._TrustStorePassPhraseEncrypted);
   }

   public String getTrustStorePassPhraseEncryptedAsString() {
      byte[] var1 = this.getTrustStorePassPhraseEncrypted();
      return var1 == null ? null : new String(var1);
   }

   public boolean isTrustStorePassPhraseEncryptedSet() {
      return this._isSet(28);
   }

   public void setTrustStorePassPhraseEncryptedAsString(String var1) {
      try {
         byte[] var2 = var1 == null ? null : var1.getBytes();
         this.setTrustStorePassPhraseEncrypted(var2);
      } catch (Exception var3) {
         if (var3 instanceof RuntimeException) {
            throw (RuntimeException)var3;
         } else {
            throw new UndeclaredThrowableException(var3);
         }
      }
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
         throw new IllegalArgumentException("In production mode, it's not allowed to set a clear text value to the property: ApplicationPasswordEncrypted of JoltConnectionPoolMBean");
      } else {
         this._getHelper()._clearArray(this._ApplicationPasswordEncrypted);
         this._ApplicationPasswordEncrypted = this._getHelper()._cloneArray(var1);
         this._postSet(17, var2, var1);
      }
   }

   public void setKeyPassPhraseEncrypted(byte[] var1) {
      byte[] var2 = this._KeyPassPhraseEncrypted;
      if (this._isProductionModeEnabled() && var1 != null && !this._isEncrypted(var1)) {
         throw new IllegalArgumentException("In production mode, it's not allowed to set a clear text value to the property: KeyPassPhraseEncrypted of JoltConnectionPoolMBean");
      } else {
         this._getHelper()._clearArray(this._KeyPassPhraseEncrypted);
         this._KeyPassPhraseEncrypted = this._getHelper()._cloneArray(var1);
         this._postSet(24, var2, var1);
      }
   }

   public void setKeyStorePassPhraseEncrypted(byte[] var1) {
      byte[] var2 = this._KeyStorePassPhraseEncrypted;
      if (this._isProductionModeEnabled() && var1 != null && !this._isEncrypted(var1)) {
         throw new IllegalArgumentException("In production mode, it's not allowed to set a clear text value to the property: KeyStorePassPhraseEncrypted of JoltConnectionPoolMBean");
      } else {
         this._getHelper()._clearArray(this._KeyStorePassPhraseEncrypted);
         this._KeyStorePassPhraseEncrypted = this._getHelper()._cloneArray(var1);
         this._postSet(26, var2, var1);
      }
   }

   public void setTrustStorePassPhraseEncrypted(byte[] var1) {
      byte[] var2 = this._TrustStorePassPhraseEncrypted;
      if (this._isProductionModeEnabled() && var1 != null && !this._isEncrypted(var1)) {
         throw new IllegalArgumentException("In production mode, it's not allowed to set a clear text value to the property: TrustStorePassPhraseEncrypted of JoltConnectionPoolMBean");
      } else {
         this._getHelper()._clearArray(this._TrustStorePassPhraseEncrypted);
         this._TrustStorePassPhraseEncrypted = this._getHelper()._cloneArray(var1);
         this._postSet(28, var2, var1);
      }
   }

   public void setUserPasswordEncrypted(byte[] var1) {
      byte[] var2 = this._UserPasswordEncrypted;
      if (this._isProductionModeEnabled() && var1 != null && !this._isEncrypted(var1)) {
         throw new IllegalArgumentException("In production mode, it's not allowed to set a clear text value to the property: UserPasswordEncrypted of JoltConnectionPoolMBean");
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

         if (var1 == 23) {
            this._markSet(24, false);
         }

         if (var1 == 25) {
            this._markSet(26, false);
         }

         if (var1 == 27) {
            this._markSet(28, false);
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
            case 23:
               this._KeyPassPhraseEncrypted = null;
               if (var2) {
                  break;
               }
            case 24:
               this._KeyPassPhraseEncrypted = null;
               if (var2) {
                  break;
               }
            case 21:
               this._KeyStoreName = null;
               if (var2) {
                  break;
               }
            case 25:
               this._KeyStorePassPhraseEncrypted = null;
               if (var2) {
                  break;
               }
            case 26:
               this._KeyStorePassPhraseEncrypted = null;
               if (var2) {
                  break;
               }
            case 12:
               this._MaximumPoolSize = 1;
               if (var2) {
                  break;
               }
            case 11:
               this._MinimumPoolSize = 0;
               if (var2) {
                  break;
               }
            case 9:
               this._PrimaryAddresses = new String[0];
               if (var2) {
                  break;
               }
            case 20:
               this._RecvTimeout = 0;
               if (var2) {
                  break;
               }
            case 22:
               this._TrustStoreName = null;
               if (var2) {
                  break;
               }
            case 27:
               this._TrustStorePassPhraseEncrypted = null;
               if (var2) {
                  break;
               }
            case 28:
               this._TrustStorePassPhraseEncrypted = null;
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
      return "JoltConnectionPool";
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
            String[] var7;
            if (var1.equals("FailoverAddresses")) {
               var7 = this._FailoverAddresses;
               this._FailoverAddresses = (String[])((String[])var2);
               this._postSet(10, var7, this._FailoverAddresses);
            } else if (var1.equals("KeyPassPhrase")) {
               var3 = this._KeyPassPhrase;
               this._KeyPassPhrase = (String)var2;
               this._postSet(23, var3, this._KeyPassPhrase);
            } else if (var1.equals("KeyPassPhraseEncrypted")) {
               var4 = this._KeyPassPhraseEncrypted;
               this._KeyPassPhraseEncrypted = (byte[])((byte[])var2);
               this._postSet(24, var4, this._KeyPassPhraseEncrypted);
            } else if (var1.equals("KeyStoreName")) {
               var3 = this._KeyStoreName;
               this._KeyStoreName = (String)var2;
               this._postSet(21, var3, this._KeyStoreName);
            } else if (var1.equals("KeyStorePassPhrase")) {
               var3 = this._KeyStorePassPhrase;
               this._KeyStorePassPhrase = (String)var2;
               this._postSet(25, var3, this._KeyStorePassPhrase);
            } else if (var1.equals("KeyStorePassPhraseEncrypted")) {
               var4 = this._KeyStorePassPhraseEncrypted;
               this._KeyStorePassPhraseEncrypted = (byte[])((byte[])var2);
               this._postSet(26, var4, this._KeyStorePassPhraseEncrypted);
            } else {
               int var6;
               if (var1.equals("MaximumPoolSize")) {
                  var6 = this._MaximumPoolSize;
                  this._MaximumPoolSize = (Integer)var2;
                  this._postSet(12, var6, this._MaximumPoolSize);
               } else if (var1.equals("MinimumPoolSize")) {
                  var6 = this._MinimumPoolSize;
                  this._MinimumPoolSize = (Integer)var2;
                  this._postSet(11, var6, this._MinimumPoolSize);
               } else if (var1.equals("PrimaryAddresses")) {
                  var7 = this._PrimaryAddresses;
                  this._PrimaryAddresses = (String[])((String[])var2);
                  this._postSet(9, var7, this._PrimaryAddresses);
               } else if (var1.equals("RecvTimeout")) {
                  var6 = this._RecvTimeout;
                  this._RecvTimeout = (Integer)var2;
                  this._postSet(20, var6, this._RecvTimeout);
               } else if (var1.equals("SecurityContextEnabled")) {
                  boolean var5 = this._SecurityContextEnabled;
                  this._SecurityContextEnabled = (Boolean)var2;
                  this._postSet(19, var5, this._SecurityContextEnabled);
               } else if (var1.equals("TrustStoreName")) {
                  var3 = this._TrustStoreName;
                  this._TrustStoreName = (String)var2;
                  this._postSet(22, var3, this._TrustStoreName);
               } else if (var1.equals("TrustStorePassPhrase")) {
                  var3 = this._TrustStorePassPhrase;
                  this._TrustStorePassPhrase = (String)var2;
                  this._postSet(27, var3, this._TrustStorePassPhrase);
               } else if (var1.equals("TrustStorePassPhraseEncrypted")) {
                  var4 = this._TrustStorePassPhraseEncrypted;
                  this._TrustStorePassPhraseEncrypted = (byte[])((byte[])var2);
                  this._postSet(28, var4, this._TrustStorePassPhraseEncrypted);
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
               } else {
                  super.putValue(var1, var2);
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
      } else if (var1.equals("FailoverAddresses")) {
         return this._FailoverAddresses;
      } else if (var1.equals("KeyPassPhrase")) {
         return this._KeyPassPhrase;
      } else if (var1.equals("KeyPassPhraseEncrypted")) {
         return this._KeyPassPhraseEncrypted;
      } else if (var1.equals("KeyStoreName")) {
         return this._KeyStoreName;
      } else if (var1.equals("KeyStorePassPhrase")) {
         return this._KeyStorePassPhrase;
      } else if (var1.equals("KeyStorePassPhraseEncrypted")) {
         return this._KeyStorePassPhraseEncrypted;
      } else if (var1.equals("MaximumPoolSize")) {
         return new Integer(this._MaximumPoolSize);
      } else if (var1.equals("MinimumPoolSize")) {
         return new Integer(this._MinimumPoolSize);
      } else if (var1.equals("PrimaryAddresses")) {
         return this._PrimaryAddresses;
      } else if (var1.equals("RecvTimeout")) {
         return new Integer(this._RecvTimeout);
      } else if (var1.equals("SecurityContextEnabled")) {
         return new Boolean(this._SecurityContextEnabled);
      } else if (var1.equals("TrustStoreName")) {
         return this._TrustStoreName;
      } else if (var1.equals("TrustStorePassPhrase")) {
         return this._TrustStorePassPhrase;
      } else if (var1.equals("TrustStorePassPhraseEncrypted")) {
         return this._TrustStorePassPhraseEncrypted;
      } else if (var1.equals("UserName")) {
         return this._UserName;
      } else if (var1.equals("UserPassword")) {
         return this._UserPassword;
      } else if (var1.equals("UserPasswordEncrypted")) {
         return this._UserPasswordEncrypted;
      } else {
         return var1.equals("UserRole") ? this._UserRole : super.getValue(var1);
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
            case 10:
            case 11:
            case 18:
            case 19:
            case 22:
            case 26:
            case 27:
            case 28:
            case 29:
            case 32:
            default:
               break;
            case 12:
               if (var1.equals("recv-timeout")) {
                  return 20;
               }
               break;
            case 13:
               if (var1.equals("user-password")) {
                  return 14;
               }
               break;
            case 14:
               if (var1.equals("key-store-name")) {
                  return 21;
               }
               break;
            case 15:
               if (var1.equals("key-pass-phrase")) {
                  return 23;
               }

               if (var1.equals("primary-address")) {
                  return 9;
               }
               break;
            case 16:
               if (var1.equals("failover-address")) {
                  return 10;
               }

               if (var1.equals("trust-store-name")) {
                  return 22;
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
            case 21:
               if (var1.equals("key-store-pass-phrase")) {
                  return 25;
               }
               break;
            case 23:
               if (var1.equals("trust-store-pass-phrase")) {
                  return 27;
               }

               if (var1.equals("user-password-encrypted")) {
                  return 15;
               }
               break;
            case 24:
               if (var1.equals("security-context-enabled")) {
                  return 19;
               }
               break;
            case 25:
               if (var1.equals("key-pass-phrase-encrypted")) {
                  return 24;
               }
               break;
            case 30:
               if (var1.equals("application-password-encrypted")) {
                  return 17;
               }
               break;
            case 31:
               if (var1.equals("key-store-pass-phrase-encrypted")) {
                  return 26;
               }
               break;
            case 33:
               if (var1.equals("trust-store-pass-phrase-encrypted")) {
                  return 28;
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
               return "security-context-enabled";
            case 20:
               return "recv-timeout";
            case 21:
               return "key-store-name";
            case 22:
               return "trust-store-name";
            case 23:
               return "key-pass-phrase";
            case 24:
               return "key-pass-phrase-encrypted";
            case 25:
               return "key-store-pass-phrase";
            case 26:
               return "key-store-pass-phrase-encrypted";
            case 27:
               return "trust-store-pass-phrase";
            case 28:
               return "trust-store-pass-phrase-encrypted";
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
      private JoltConnectionPoolMBeanImpl bean;

      protected Helper(JoltConnectionPoolMBeanImpl var1) {
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
               return "SecurityContextEnabled";
            case 20:
               return "RecvTimeout";
            case 21:
               return "KeyStoreName";
            case 22:
               return "TrustStoreName";
            case 23:
               return "KeyPassPhrase";
            case 24:
               return "KeyPassPhraseEncrypted";
            case 25:
               return "KeyStorePassPhrase";
            case 26:
               return "KeyStorePassPhraseEncrypted";
            case 27:
               return "TrustStorePassPhrase";
            case 28:
               return "TrustStorePassPhraseEncrypted";
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
         } else if (var1.equals("KeyPassPhrase")) {
            return 23;
         } else if (var1.equals("KeyPassPhraseEncrypted")) {
            return 24;
         } else if (var1.equals("KeyStoreName")) {
            return 21;
         } else if (var1.equals("KeyStorePassPhrase")) {
            return 25;
         } else if (var1.equals("KeyStorePassPhraseEncrypted")) {
            return 26;
         } else if (var1.equals("MaximumPoolSize")) {
            return 12;
         } else if (var1.equals("MinimumPoolSize")) {
            return 11;
         } else if (var1.equals("PrimaryAddresses")) {
            return 9;
         } else if (var1.equals("RecvTimeout")) {
            return 20;
         } else if (var1.equals("TrustStoreName")) {
            return 22;
         } else if (var1.equals("TrustStorePassPhrase")) {
            return 27;
         } else if (var1.equals("TrustStorePassPhraseEncrypted")) {
            return 28;
         } else if (var1.equals("UserName")) {
            return 13;
         } else if (var1.equals("UserPassword")) {
            return 14;
         } else if (var1.equals("UserPasswordEncrypted")) {
            return 15;
         } else if (var1.equals("UserRole")) {
            return 18;
         } else {
            return var1.equals("SecurityContextEnabled") ? 19 : super.getPropertyIndex(var1);
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

            if (this.bean.isKeyPassPhraseSet()) {
               var2.append("KeyPassPhrase");
               var2.append(String.valueOf(this.bean.getKeyPassPhrase()));
            }

            if (this.bean.isKeyPassPhraseEncryptedSet()) {
               var2.append("KeyPassPhraseEncrypted");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getKeyPassPhraseEncrypted())));
            }

            if (this.bean.isKeyStoreNameSet()) {
               var2.append("KeyStoreName");
               var2.append(String.valueOf(this.bean.getKeyStoreName()));
            }

            if (this.bean.isKeyStorePassPhraseSet()) {
               var2.append("KeyStorePassPhrase");
               var2.append(String.valueOf(this.bean.getKeyStorePassPhrase()));
            }

            if (this.bean.isKeyStorePassPhraseEncryptedSet()) {
               var2.append("KeyStorePassPhraseEncrypted");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getKeyStorePassPhraseEncrypted())));
            }

            if (this.bean.isMaximumPoolSizeSet()) {
               var2.append("MaximumPoolSize");
               var2.append(String.valueOf(this.bean.getMaximumPoolSize()));
            }

            if (this.bean.isMinimumPoolSizeSet()) {
               var2.append("MinimumPoolSize");
               var2.append(String.valueOf(this.bean.getMinimumPoolSize()));
            }

            if (this.bean.isPrimaryAddressesSet()) {
               var2.append("PrimaryAddresses");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getPrimaryAddresses())));
            }

            if (this.bean.isRecvTimeoutSet()) {
               var2.append("RecvTimeout");
               var2.append(String.valueOf(this.bean.getRecvTimeout()));
            }

            if (this.bean.isTrustStoreNameSet()) {
               var2.append("TrustStoreName");
               var2.append(String.valueOf(this.bean.getTrustStoreName()));
            }

            if (this.bean.isTrustStorePassPhraseSet()) {
               var2.append("TrustStorePassPhrase");
               var2.append(String.valueOf(this.bean.getTrustStorePassPhrase()));
            }

            if (this.bean.isTrustStorePassPhraseEncryptedSet()) {
               var2.append("TrustStorePassPhraseEncrypted");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getTrustStorePassPhraseEncrypted())));
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
            JoltConnectionPoolMBeanImpl var2 = (JoltConnectionPoolMBeanImpl)var1;
            this.computeDiff("ApplicationPasswordEncrypted", this.bean.getApplicationPasswordEncrypted(), var2.getApplicationPasswordEncrypted(), false);
            this.computeDiff("FailoverAddresses", this.bean.getFailoverAddresses(), var2.getFailoverAddresses(), false);
            this.computeDiff("KeyPassPhraseEncrypted", this.bean.getKeyPassPhraseEncrypted(), var2.getKeyPassPhraseEncrypted(), true);
            this.computeDiff("KeyStoreName", this.bean.getKeyStoreName(), var2.getKeyStoreName(), false);
            this.computeDiff("KeyStorePassPhraseEncrypted", this.bean.getKeyStorePassPhraseEncrypted(), var2.getKeyStorePassPhraseEncrypted(), true);
            this.computeDiff("MaximumPoolSize", this.bean.getMaximumPoolSize(), var2.getMaximumPoolSize(), false);
            this.computeDiff("MinimumPoolSize", this.bean.getMinimumPoolSize(), var2.getMinimumPoolSize(), false);
            this.computeDiff("PrimaryAddresses", this.bean.getPrimaryAddresses(), var2.getPrimaryAddresses(), false);
            this.computeDiff("RecvTimeout", this.bean.getRecvTimeout(), var2.getRecvTimeout(), false);
            this.computeDiff("TrustStoreName", this.bean.getTrustStoreName(), var2.getTrustStoreName(), false);
            this.computeDiff("TrustStorePassPhraseEncrypted", this.bean.getTrustStorePassPhraseEncrypted(), var2.getTrustStorePassPhraseEncrypted(), true);
            this.computeDiff("UserName", this.bean.getUserName(), var2.getUserName(), false);
            this.computeDiff("UserPasswordEncrypted", this.bean.getUserPasswordEncrypted(), var2.getUserPasswordEncrypted(), false);
            this.computeDiff("UserRole", this.bean.getUserRole(), var2.getUserRole(), false);
            this.computeDiff("SecurityContextEnabled", this.bean.isSecurityContextEnabled(), var2.isSecurityContextEnabled(), false);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            JoltConnectionPoolMBeanImpl var3 = (JoltConnectionPoolMBeanImpl)var1.getSourceBean();
            JoltConnectionPoolMBeanImpl var4 = (JoltConnectionPoolMBeanImpl)var1.getProposedBean();
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
                  } else if (!var5.equals("KeyPassPhrase")) {
                     if (var5.equals("KeyPassPhraseEncrypted")) {
                        var3.setKeyPassPhraseEncrypted(var4.getKeyPassPhraseEncrypted());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 24);
                     } else if (var5.equals("KeyStoreName")) {
                        var3.setKeyStoreName(var4.getKeyStoreName());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 21);
                     } else if (!var5.equals("KeyStorePassPhrase")) {
                        if (var5.equals("KeyStorePassPhraseEncrypted")) {
                           var3.setKeyStorePassPhraseEncrypted(var4.getKeyStorePassPhraseEncrypted());
                           var3._conditionalUnset(var2.isUnsetUpdate(), 26);
                        } else if (var5.equals("MaximumPoolSize")) {
                           var3.setMaximumPoolSize(var4.getMaximumPoolSize());
                           var3._conditionalUnset(var2.isUnsetUpdate(), 12);
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
                        } else if (var5.equals("RecvTimeout")) {
                           var3.setRecvTimeout(var4.getRecvTimeout());
                           var3._conditionalUnset(var2.isUnsetUpdate(), 20);
                        } else if (var5.equals("TrustStoreName")) {
                           var3.setTrustStoreName(var4.getTrustStoreName());
                           var3._conditionalUnset(var2.isUnsetUpdate(), 22);
                        } else if (!var5.equals("TrustStorePassPhrase")) {
                           if (var5.equals("TrustStorePassPhraseEncrypted")) {
                              var3.setTrustStorePassPhraseEncrypted(var4.getTrustStorePassPhraseEncrypted());
                              var3._conditionalUnset(var2.isUnsetUpdate(), 28);
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
                              } else if (var5.equals("SecurityContextEnabled")) {
                                 var3.setSecurityContextEnabled(var4.isSecurityContextEnabled());
                                 var3._conditionalUnset(var2.isUnsetUpdate(), 19);
                              } else {
                                 super.applyPropertyUpdate(var1, var2);
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
            JoltConnectionPoolMBeanImpl var5 = (JoltConnectionPoolMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            byte[] var4;
            if ((var3 == null || !var3.contains("ApplicationPasswordEncrypted")) && this.bean.isApplicationPasswordEncryptedSet()) {
               var4 = this.bean.getApplicationPasswordEncrypted();
               var5.setApplicationPasswordEncrypted(var4 == null ? null : (byte[])((byte[])((byte[])((byte[])var4)).clone()));
            }

            String[] var8;
            if ((var3 == null || !var3.contains("FailoverAddresses")) && this.bean.isFailoverAddressesSet()) {
               var8 = this.bean.getFailoverAddresses();
               var5.setFailoverAddresses(var8 == null ? null : (String[])((String[])((String[])((String[])var8)).clone()));
            }

            if ((var3 == null || !var3.contains("KeyPassPhraseEncrypted")) && this.bean.isKeyPassPhraseEncryptedSet()) {
               var4 = this.bean.getKeyPassPhraseEncrypted();
               var5.setKeyPassPhraseEncrypted(var4 == null ? null : (byte[])((byte[])((byte[])((byte[])var4)).clone()));
            }

            if ((var3 == null || !var3.contains("KeyStoreName")) && this.bean.isKeyStoreNameSet()) {
               var5.setKeyStoreName(this.bean.getKeyStoreName());
            }

            if ((var3 == null || !var3.contains("KeyStorePassPhraseEncrypted")) && this.bean.isKeyStorePassPhraseEncryptedSet()) {
               var4 = this.bean.getKeyStorePassPhraseEncrypted();
               var5.setKeyStorePassPhraseEncrypted(var4 == null ? null : (byte[])((byte[])((byte[])((byte[])var4)).clone()));
            }

            if ((var3 == null || !var3.contains("MaximumPoolSize")) && this.bean.isMaximumPoolSizeSet()) {
               var5.setMaximumPoolSize(this.bean.getMaximumPoolSize());
            }

            if ((var3 == null || !var3.contains("MinimumPoolSize")) && this.bean.isMinimumPoolSizeSet()) {
               var5.setMinimumPoolSize(this.bean.getMinimumPoolSize());
            }

            if ((var3 == null || !var3.contains("PrimaryAddresses")) && this.bean.isPrimaryAddressesSet()) {
               var8 = this.bean.getPrimaryAddresses();
               var5.setPrimaryAddresses(var8 == null ? null : (String[])((String[])((String[])((String[])var8)).clone()));
            }

            if ((var3 == null || !var3.contains("RecvTimeout")) && this.bean.isRecvTimeoutSet()) {
               var5.setRecvTimeout(this.bean.getRecvTimeout());
            }

            if ((var3 == null || !var3.contains("TrustStoreName")) && this.bean.isTrustStoreNameSet()) {
               var5.setTrustStoreName(this.bean.getTrustStoreName());
            }

            if ((var3 == null || !var3.contains("TrustStorePassPhraseEncrypted")) && this.bean.isTrustStorePassPhraseEncryptedSet()) {
               var4 = this.bean.getTrustStorePassPhraseEncrypted();
               var5.setTrustStorePassPhraseEncrypted(var4 == null ? null : (byte[])((byte[])((byte[])((byte[])var4)).clone()));
            }

            if ((var3 == null || !var3.contains("UserName")) && this.bean.isUserNameSet()) {
               var5.setUserName(this.bean.getUserName());
            }

            if ((var3 == null || !var3.contains("UserPasswordEncrypted")) && this.bean.isUserPasswordEncryptedSet()) {
               var4 = this.bean.getUserPasswordEncrypted();
               var5.setUserPasswordEncrypted(var4 == null ? null : (byte[])((byte[])((byte[])((byte[])var4)).clone()));
            }

            if ((var3 == null || !var3.contains("UserRole")) && this.bean.isUserRoleSet()) {
               var5.setUserRole(this.bean.getUserRole());
            }

            if ((var3 == null || !var3.contains("SecurityContextEnabled")) && this.bean.isSecurityContextEnabledSet()) {
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
