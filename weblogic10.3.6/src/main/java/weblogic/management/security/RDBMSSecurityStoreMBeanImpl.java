package weblogic.management.security;

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
import weblogic.descriptor.beangen.LegalChecks;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBeanHelper;
import weblogic.descriptor.internal.AbstractSchemaHelper2;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.management.commo.AbstractCommoConfigurationBean;
import weblogic.management.commo.RequiredModelMBeanWrapper;
import weblogic.utils.ArrayUtils;
import weblogic.utils.collections.CombinedIterator;

public class RDBMSSecurityStoreMBeanImpl extends AbstractCommoConfigurationBean implements RDBMSSecurityStoreMBean, Serializable {
   private String _CompatibilityObjectName;
   private String _ConnectionProperties;
   private String _ConnectionURL;
   private String _DriverName;
   private int _JMSExceptionReconnectAttempts;
   private String _JMSTopic;
   private String _JMSTopicConnectionFactory;
   private String _JNDIPassword;
   private byte[] _JNDIPasswordEncrypted;
   private String _JNDIUsername;
   private String _Name;
   private String _NotificationProperties;
   private String _Password;
   private byte[] _PasswordEncrypted;
   private RealmMBean _Realm;
   private String _Username;
   private RDBMSSecurityStoreImpl _customizer;
   private static SchemaHelper2 _schemaHelper;

   public RDBMSSecurityStoreMBeanImpl() {
      try {
         this._customizer = new RDBMSSecurityStoreImpl(new RequiredModelMBeanWrapper(this));
      } catch (Exception var2) {
         if (var2 instanceof RuntimeException) {
            throw (RuntimeException)var2;
         }

         throw new UndeclaredThrowableException(var2);
      }

      this._initializeProperty(-1);
   }

   public RDBMSSecurityStoreMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);

      try {
         this._customizer = new RDBMSSecurityStoreImpl(new RequiredModelMBeanWrapper(this));
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         }

         throw new UndeclaredThrowableException(var4);
      }

      this._initializeProperty(-1);
   }

   public String getUsername() {
      return this._Username;
   }

   public boolean isUsernameSet() {
      return this._isSet(2);
   }

   public void setUsername(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      LegalChecks.checkNonEmptyString("Username", var1);
      LegalChecks.checkNonNull("Username", var1);
      String var2 = this._Username;
      this._Username = var1;
      this._postSet(2, var2, var1);
   }

   public String getPassword() {
      byte[] var1 = this.getPasswordEncrypted();
      return var1 == null ? null : this._decrypt("Password", var1);
   }

   public boolean isPasswordSet() {
      return this.isPasswordEncryptedSet();
   }

   public void setPassword(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      this.setPasswordEncrypted(var1 == null ? null : this._encrypt("Password", var1));
   }

   public byte[] getPasswordEncrypted() {
      return this._getHelper()._cloneArray(this._PasswordEncrypted);
   }

   public String getPasswordEncryptedAsString() {
      byte[] var1 = this.getPasswordEncrypted();
      return var1 == null ? null : new String(var1);
   }

   public boolean isPasswordEncryptedSet() {
      return this._isSet(4);
   }

   public void setPasswordEncryptedAsString(String var1) {
      try {
         byte[] var2 = var1 == null ? null : var1.getBytes();
         this.setPasswordEncrypted(var2);
      } catch (Exception var3) {
         if (var3 instanceof RuntimeException) {
            throw (RuntimeException)var3;
         } else {
            throw new UndeclaredThrowableException(var3);
         }
      }
   }

   public String getJNDIUsername() {
      return this._JNDIUsername;
   }

   public boolean isJNDIUsernameSet() {
      return this._isSet(5);
   }

   public void setJNDIUsername(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._JNDIUsername;
      this._JNDIUsername = var1;
      this._postSet(5, var2, var1);
   }

   public String getJNDIPassword() {
      byte[] var1 = this.getJNDIPasswordEncrypted();
      return var1 == null ? null : this._decrypt("JNDIPassword", var1);
   }

   public boolean isJNDIPasswordSet() {
      return this.isJNDIPasswordEncryptedSet();
   }

   public void setJNDIPassword(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      this.setJNDIPasswordEncrypted(var1 == null ? null : this._encrypt("JNDIPassword", var1));
   }

   public byte[] getJNDIPasswordEncrypted() {
      return this._getHelper()._cloneArray(this._JNDIPasswordEncrypted);
   }

   public String getJNDIPasswordEncryptedAsString() {
      byte[] var1 = this.getJNDIPasswordEncrypted();
      return var1 == null ? null : new String(var1);
   }

   public boolean isJNDIPasswordEncryptedSet() {
      return this._isSet(7);
   }

   public void setJNDIPasswordEncryptedAsString(String var1) {
      try {
         byte[] var2 = var1 == null ? null : var1.getBytes();
         this.setJNDIPasswordEncrypted(var2);
      } catch (Exception var3) {
         if (var3 instanceof RuntimeException) {
            throw (RuntimeException)var3;
         } else {
            throw new UndeclaredThrowableException(var3);
         }
      }
   }

   public String getConnectionURL() {
      return this._ConnectionURL;
   }

   public boolean isConnectionURLSet() {
      return this._isSet(8);
   }

   public void setConnectionURL(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      LegalChecks.checkNonEmptyString("ConnectionURL", var1);
      LegalChecks.checkNonNull("ConnectionURL", var1);
      String var2 = this._ConnectionURL;
      this._ConnectionURL = var1;
      this._postSet(8, var2, var1);
   }

   public String getDriverName() {
      return this._DriverName;
   }

   public boolean isDriverNameSet() {
      return this._isSet(9);
   }

   public void setDriverName(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      LegalChecks.checkNonEmptyString("DriverName", var1);
      LegalChecks.checkNonNull("DriverName", var1);
      String var2 = this._DriverName;
      this._DriverName = var1;
      this._postSet(9, var2, var1);
   }

   public String getConnectionProperties() {
      return this._ConnectionProperties;
   }

   public boolean isConnectionPropertiesSet() {
      return this._isSet(10);
   }

   public void setConnectionProperties(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      RDBMSSecurityStoreValidator.validateProperties(var1);
      String var2 = this._ConnectionProperties;
      this._ConnectionProperties = var1;
      this._postSet(10, var2, var1);
   }

   public String getJMSTopic() {
      return this._JMSTopic;
   }

   public boolean isJMSTopicSet() {
      return this._isSet(11);
   }

   public void setJMSTopic(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._JMSTopic;
      this._JMSTopic = var1;
      this._postSet(11, var2, var1);
   }

   public String getJMSTopicConnectionFactory() {
      return this._JMSTopicConnectionFactory;
   }

   public boolean isJMSTopicConnectionFactorySet() {
      return this._isSet(12);
   }

   public void setJMSTopicConnectionFactory(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._JMSTopicConnectionFactory;
      this._JMSTopicConnectionFactory = var1;
      this._postSet(12, var2, var1);
   }

   public int getJMSExceptionReconnectAttempts() {
      return this._JMSExceptionReconnectAttempts;
   }

   public boolean isJMSExceptionReconnectAttemptsSet() {
      return this._isSet(13);
   }

   public void setJMSExceptionReconnectAttempts(int var1) throws InvalidAttributeValueException {
      LegalChecks.checkMin("JMSExceptionReconnectAttempts", var1, 0);
      int var2 = this._JMSExceptionReconnectAttempts;
      this._JMSExceptionReconnectAttempts = var1;
      this._postSet(13, var2, var1);
   }

   public String getNotificationProperties() {
      return this._NotificationProperties;
   }

   public boolean isNotificationPropertiesSet() {
      return this._isSet(14);
   }

   public void setNotificationProperties(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      RDBMSSecurityStoreValidator.validateProperties(var1);
      String var2 = this._NotificationProperties;
      this._NotificationProperties = var1;
      this._postSet(14, var2, var1);
   }

   public RealmMBean getRealm() {
      return this._customizer.getRealm();
   }

   public boolean isRealmSet() {
      return this._isSet(15);
   }

   public void setRealm(RealmMBean var1) throws InvalidAttributeValueException {
      this._Realm = var1;
   }

   public String getName() {
      return this._Name;
   }

   public boolean isNameSet() {
      return this._isSet(16);
   }

   public void setName(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._Name;
      this._Name = var1;
      this._postSet(16, var2, var1);
   }

   public String getCompatibilityObjectName() {
      return this._customizer.getCompatibilityObjectName();
   }

   public boolean isCompatibilityObjectNameSet() {
      return this._isSet(17);
   }

   public void setCompatibilityObjectName(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._CompatibilityObjectName;
      this._CompatibilityObjectName = var1;
      this._postSet(17, var2, var1);
   }

   public Object _getKey() {
      return super._getKey();
   }

   public void _validate() throws IllegalArgumentException {
      super._validate();
      LegalChecks.checkIsSet("ConnectionURL", this.isConnectionURLSet());
      LegalChecks.checkIsSet("DriverName", this.isDriverNameSet());
      LegalChecks.checkIsSet("Username", this.isUsernameSet());
   }

   public void setJNDIPasswordEncrypted(byte[] var1) {
      byte[] var2 = this._JNDIPasswordEncrypted;
      if (this._isProductionModeEnabled() && var1 != null && !this._isEncrypted(var1)) {
         throw new IllegalArgumentException("In production mode, it's not allowed to set a clear text value to the property: JNDIPasswordEncrypted of RDBMSSecurityStoreMBean");
      } else {
         this._getHelper()._clearArray(this._JNDIPasswordEncrypted);
         this._JNDIPasswordEncrypted = this._getHelper()._cloneArray(var1);
         this._postSet(7, var2, var1);
      }
   }

   public void setPasswordEncrypted(byte[] var1) {
      byte[] var2 = this._PasswordEncrypted;
      if (this._isProductionModeEnabled() && var1 != null && !this._isEncrypted(var1)) {
         throw new IllegalArgumentException("In production mode, it's not allowed to set a clear text value to the property: PasswordEncrypted of RDBMSSecurityStoreMBean");
      } else {
         this._getHelper()._clearArray(this._PasswordEncrypted);
         this._PasswordEncrypted = this._getHelper()._cloneArray(var1);
         this._postSet(4, var2, var1);
      }
   }

   protected void _unSet(int var1) {
      if (!this._initializeProperty(var1)) {
         super._unSet(var1);
      } else {
         this._markSet(var1, false);
         if (var1 == 6) {
            this._markSet(7, false);
         }

         if (var1 == 3) {
            this._markSet(4, false);
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
         var1 = 17;
      }

      try {
         switch (var1) {
            case 17:
               this._CompatibilityObjectName = null;
               if (var2) {
                  break;
               }
            case 10:
               this._ConnectionProperties = null;
               if (var2) {
                  break;
               }
            case 8:
               this._ConnectionURL = null;
               if (var2) {
                  break;
               }
            case 9:
               this._DriverName = null;
               if (var2) {
                  break;
               }
            case 13:
               this._JMSExceptionReconnectAttempts = 0;
               if (var2) {
                  break;
               }
            case 11:
               this._JMSTopic = null;
               if (var2) {
                  break;
               }
            case 12:
               this._JMSTopicConnectionFactory = null;
               if (var2) {
                  break;
               }
            case 6:
               this._JNDIPasswordEncrypted = null;
               if (var2) {
                  break;
               }
            case 7:
               this._JNDIPasswordEncrypted = null;
               if (var2) {
                  break;
               }
            case 5:
               this._JNDIUsername = null;
               if (var2) {
                  break;
               }
            case 16:
               this._Name = "RDBMSSecurityStore";
               if (var2) {
                  break;
               }
            case 14:
               this._NotificationProperties = null;
               if (var2) {
                  break;
               }
            case 3:
               this._PasswordEncrypted = null;
               if (var2) {
                  break;
               }
            case 4:
               this._PasswordEncrypted = null;
               if (var2) {
                  break;
               }
            case 15:
               this._Realm = null;
               if (var2) {
                  break;
               }
            case 2:
               this._Username = null;
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
      return "http://xmlns.oracle.com/weblogic/1.0/security.xsd";
   }

   protected String getTargetNamespace() {
      return "http://xmlns.oracle.com/weblogic/security";
   }

   public SchemaHelper _getSchemaHelper2() {
      if (_schemaHelper == null) {
         _schemaHelper = new SchemaHelper2();
      }

      return _schemaHelper;
   }

   public String wls_getInterfaceClassName() {
      return "weblogic.management.security.RDBMSSecurityStoreMBean";
   }

   public static class SchemaHelper2 extends AbstractSchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 4:
               if (var1.equals("name")) {
                  return 16;
               }
               break;
            case 5:
               if (var1.equals("realm")) {
                  return 15;
               }
            case 6:
            case 7:
            case 10:
            case 12:
            case 15:
            case 16:
            case 17:
            case 19:
            case 20:
            case 22:
            case 24:
            case 26:
            case 27:
            case 29:
            case 30:
            case 31:
            default:
               break;
            case 8:
               if (var1.equals("password")) {
                  return 3;
               }

               if (var1.equals("username")) {
                  return 2;
               }
               break;
            case 9:
               if (var1.equals("jms-topic")) {
                  return 11;
               }
               break;
            case 11:
               if (var1.equals("driver-name")) {
                  return 9;
               }
               break;
            case 13:
               if (var1.equals("jndi-password")) {
                  return 6;
               }

               if (var1.equals("jndi-username")) {
                  return 5;
               }
               break;
            case 14:
               if (var1.equals("connection-url")) {
                  return 8;
               }
               break;
            case 18:
               if (var1.equals("password-encrypted")) {
                  return 4;
               }
               break;
            case 21:
               if (var1.equals("connection-properties")) {
                  return 10;
               }
               break;
            case 23:
               if (var1.equals("jndi-password-encrypted")) {
                  return 7;
               }

               if (var1.equals("notification-properties")) {
                  return 14;
               }
               break;
            case 25:
               if (var1.equals("compatibility-object-name")) {
                  return 17;
               }
               break;
            case 28:
               if (var1.equals("jms-topic-connection-factory")) {
                  return 12;
               }
               break;
            case 32:
               if (var1.equals("jms-exception-reconnect-attempts")) {
                  return 13;
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
               return "username";
            case 3:
               return "password";
            case 4:
               return "password-encrypted";
            case 5:
               return "jndi-username";
            case 6:
               return "jndi-password";
            case 7:
               return "jndi-password-encrypted";
            case 8:
               return "connection-url";
            case 9:
               return "driver-name";
            case 10:
               return "connection-properties";
            case 11:
               return "jms-topic";
            case 12:
               return "jms-topic-connection-factory";
            case 13:
               return "jms-exception-reconnect-attempts";
            case 14:
               return "notification-properties";
            case 15:
               return "realm";
            case 16:
               return "name";
            case 17:
               return "compatibility-object-name";
            default:
               return super.getElementName(var1);
         }
      }

      public boolean isArray(int var1) {
         switch (var1) {
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
   }

   protected static class Helper extends AbstractCommoConfigurationBean.Helper {
      private RDBMSSecurityStoreMBeanImpl bean;

      protected Helper(RDBMSSecurityStoreMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 2:
               return "Username";
            case 3:
               return "Password";
            case 4:
               return "PasswordEncrypted";
            case 5:
               return "JNDIUsername";
            case 6:
               return "JNDIPassword";
            case 7:
               return "JNDIPasswordEncrypted";
            case 8:
               return "ConnectionURL";
            case 9:
               return "DriverName";
            case 10:
               return "ConnectionProperties";
            case 11:
               return "JMSTopic";
            case 12:
               return "JMSTopicConnectionFactory";
            case 13:
               return "JMSExceptionReconnectAttempts";
            case 14:
               return "NotificationProperties";
            case 15:
               return "Realm";
            case 16:
               return "Name";
            case 17:
               return "CompatibilityObjectName";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("CompatibilityObjectName")) {
            return 17;
         } else if (var1.equals("ConnectionProperties")) {
            return 10;
         } else if (var1.equals("ConnectionURL")) {
            return 8;
         } else if (var1.equals("DriverName")) {
            return 9;
         } else if (var1.equals("JMSExceptionReconnectAttempts")) {
            return 13;
         } else if (var1.equals("JMSTopic")) {
            return 11;
         } else if (var1.equals("JMSTopicConnectionFactory")) {
            return 12;
         } else if (var1.equals("JNDIPassword")) {
            return 6;
         } else if (var1.equals("JNDIPasswordEncrypted")) {
            return 7;
         } else if (var1.equals("JNDIUsername")) {
            return 5;
         } else if (var1.equals("Name")) {
            return 16;
         } else if (var1.equals("NotificationProperties")) {
            return 14;
         } else if (var1.equals("Password")) {
            return 3;
         } else if (var1.equals("PasswordEncrypted")) {
            return 4;
         } else if (var1.equals("Realm")) {
            return 15;
         } else {
            return var1.equals("Username") ? 2 : super.getPropertyIndex(var1);
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
            if (this.bean.isCompatibilityObjectNameSet()) {
               var2.append("CompatibilityObjectName");
               var2.append(String.valueOf(this.bean.getCompatibilityObjectName()));
            }

            if (this.bean.isConnectionPropertiesSet()) {
               var2.append("ConnectionProperties");
               var2.append(String.valueOf(this.bean.getConnectionProperties()));
            }

            if (this.bean.isConnectionURLSet()) {
               var2.append("ConnectionURL");
               var2.append(String.valueOf(this.bean.getConnectionURL()));
            }

            if (this.bean.isDriverNameSet()) {
               var2.append("DriverName");
               var2.append(String.valueOf(this.bean.getDriverName()));
            }

            if (this.bean.isJMSExceptionReconnectAttemptsSet()) {
               var2.append("JMSExceptionReconnectAttempts");
               var2.append(String.valueOf(this.bean.getJMSExceptionReconnectAttempts()));
            }

            if (this.bean.isJMSTopicSet()) {
               var2.append("JMSTopic");
               var2.append(String.valueOf(this.bean.getJMSTopic()));
            }

            if (this.bean.isJMSTopicConnectionFactorySet()) {
               var2.append("JMSTopicConnectionFactory");
               var2.append(String.valueOf(this.bean.getJMSTopicConnectionFactory()));
            }

            if (this.bean.isJNDIPasswordSet()) {
               var2.append("JNDIPassword");
               var2.append(String.valueOf(this.bean.getJNDIPassword()));
            }

            if (this.bean.isJNDIPasswordEncryptedSet()) {
               var2.append("JNDIPasswordEncrypted");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getJNDIPasswordEncrypted())));
            }

            if (this.bean.isJNDIUsernameSet()) {
               var2.append("JNDIUsername");
               var2.append(String.valueOf(this.bean.getJNDIUsername()));
            }

            if (this.bean.isNameSet()) {
               var2.append("Name");
               var2.append(String.valueOf(this.bean.getName()));
            }

            if (this.bean.isNotificationPropertiesSet()) {
               var2.append("NotificationProperties");
               var2.append(String.valueOf(this.bean.getNotificationProperties()));
            }

            if (this.bean.isPasswordSet()) {
               var2.append("Password");
               var2.append(String.valueOf(this.bean.getPassword()));
            }

            if (this.bean.isPasswordEncryptedSet()) {
               var2.append("PasswordEncrypted");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getPasswordEncrypted())));
            }

            if (this.bean.isRealmSet()) {
               var2.append("Realm");
               var2.append(String.valueOf(this.bean.getRealm()));
            }

            if (this.bean.isUsernameSet()) {
               var2.append("Username");
               var2.append(String.valueOf(this.bean.getUsername()));
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
            RDBMSSecurityStoreMBeanImpl var2 = (RDBMSSecurityStoreMBeanImpl)var1;
            this.computeDiff("CompatibilityObjectName", this.bean.getCompatibilityObjectName(), var2.getCompatibilityObjectName(), false);
            this.computeDiff("ConnectionProperties", this.bean.getConnectionProperties(), var2.getConnectionProperties(), false);
            this.computeDiff("ConnectionURL", this.bean.getConnectionURL(), var2.getConnectionURL(), false);
            this.computeDiff("DriverName", this.bean.getDriverName(), var2.getDriverName(), false);
            this.computeDiff("JMSExceptionReconnectAttempts", this.bean.getJMSExceptionReconnectAttempts(), var2.getJMSExceptionReconnectAttempts(), false);
            this.computeDiff("JMSTopic", this.bean.getJMSTopic(), var2.getJMSTopic(), false);
            this.computeDiff("JMSTopicConnectionFactory", this.bean.getJMSTopicConnectionFactory(), var2.getJMSTopicConnectionFactory(), false);
            this.computeDiff("JNDIPasswordEncrypted", this.bean.getJNDIPasswordEncrypted(), var2.getJNDIPasswordEncrypted(), false);
            this.computeDiff("JNDIUsername", this.bean.getJNDIUsername(), var2.getJNDIUsername(), false);
            this.computeDiff("Name", this.bean.getName(), var2.getName(), false);
            this.computeDiff("NotificationProperties", this.bean.getNotificationProperties(), var2.getNotificationProperties(), false);
            this.computeDiff("PasswordEncrypted", this.bean.getPasswordEncrypted(), var2.getPasswordEncrypted(), false);
            this.computeDiff("Username", this.bean.getUsername(), var2.getUsername(), false);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            RDBMSSecurityStoreMBeanImpl var3 = (RDBMSSecurityStoreMBeanImpl)var1.getSourceBean();
            RDBMSSecurityStoreMBeanImpl var4 = (RDBMSSecurityStoreMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("CompatibilityObjectName")) {
                  var3._conditionalUnset(var2.isUnsetUpdate(), 17);
               } else if (var5.equals("ConnectionProperties")) {
                  var3.setConnectionProperties(var4.getConnectionProperties());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 10);
               } else if (var5.equals("ConnectionURL")) {
                  var3.setConnectionURL(var4.getConnectionURL());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 8);
               } else if (var5.equals("DriverName")) {
                  var3.setDriverName(var4.getDriverName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 9);
               } else if (var5.equals("JMSExceptionReconnectAttempts")) {
                  var3.setJMSExceptionReconnectAttempts(var4.getJMSExceptionReconnectAttempts());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 13);
               } else if (var5.equals("JMSTopic")) {
                  var3.setJMSTopic(var4.getJMSTopic());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 11);
               } else if (var5.equals("JMSTopicConnectionFactory")) {
                  var3.setJMSTopicConnectionFactory(var4.getJMSTopicConnectionFactory());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 12);
               } else if (!var5.equals("JNDIPassword")) {
                  if (var5.equals("JNDIPasswordEncrypted")) {
                     var3.setJNDIPasswordEncrypted(var4.getJNDIPasswordEncrypted());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 7);
                  } else if (var5.equals("JNDIUsername")) {
                     var3.setJNDIUsername(var4.getJNDIUsername());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 5);
                  } else if (var5.equals("Name")) {
                     var3.setName(var4.getName());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 16);
                  } else if (var5.equals("NotificationProperties")) {
                     var3.setNotificationProperties(var4.getNotificationProperties());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 14);
                  } else if (!var5.equals("Password")) {
                     if (var5.equals("PasswordEncrypted")) {
                        var3.setPasswordEncrypted(var4.getPasswordEncrypted());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 4);
                     } else if (!var5.equals("Realm")) {
                        if (var5.equals("Username")) {
                           var3.setUsername(var4.getUsername());
                           var3._conditionalUnset(var2.isUnsetUpdate(), 2);
                        } else {
                           super.applyPropertyUpdate(var1, var2);
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
            RDBMSSecurityStoreMBeanImpl var5 = (RDBMSSecurityStoreMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("CompatibilityObjectName")) && this.bean.isCompatibilityObjectNameSet()) {
            }

            if ((var3 == null || !var3.contains("ConnectionProperties")) && this.bean.isConnectionPropertiesSet()) {
               var5.setConnectionProperties(this.bean.getConnectionProperties());
            }

            if ((var3 == null || !var3.contains("ConnectionURL")) && this.bean.isConnectionURLSet()) {
               var5.setConnectionURL(this.bean.getConnectionURL());
            }

            if ((var3 == null || !var3.contains("DriverName")) && this.bean.isDriverNameSet()) {
               var5.setDriverName(this.bean.getDriverName());
            }

            if ((var3 == null || !var3.contains("JMSExceptionReconnectAttempts")) && this.bean.isJMSExceptionReconnectAttemptsSet()) {
               var5.setJMSExceptionReconnectAttempts(this.bean.getJMSExceptionReconnectAttempts());
            }

            if ((var3 == null || !var3.contains("JMSTopic")) && this.bean.isJMSTopicSet()) {
               var5.setJMSTopic(this.bean.getJMSTopic());
            }

            if ((var3 == null || !var3.contains("JMSTopicConnectionFactory")) && this.bean.isJMSTopicConnectionFactorySet()) {
               var5.setJMSTopicConnectionFactory(this.bean.getJMSTopicConnectionFactory());
            }

            byte[] var4;
            if ((var3 == null || !var3.contains("JNDIPasswordEncrypted")) && this.bean.isJNDIPasswordEncryptedSet()) {
               var4 = this.bean.getJNDIPasswordEncrypted();
               var5.setJNDIPasswordEncrypted(var4 == null ? null : (byte[])((byte[])((byte[])((byte[])var4)).clone()));
            }

            if ((var3 == null || !var3.contains("JNDIUsername")) && this.bean.isJNDIUsernameSet()) {
               var5.setJNDIUsername(this.bean.getJNDIUsername());
            }

            if ((var3 == null || !var3.contains("Name")) && this.bean.isNameSet()) {
               var5.setName(this.bean.getName());
            }

            if ((var3 == null || !var3.contains("NotificationProperties")) && this.bean.isNotificationPropertiesSet()) {
               var5.setNotificationProperties(this.bean.getNotificationProperties());
            }

            if ((var3 == null || !var3.contains("PasswordEncrypted")) && this.bean.isPasswordEncryptedSet()) {
               var4 = this.bean.getPasswordEncrypted();
               var5.setPasswordEncrypted(var4 == null ? null : (byte[])((byte[])((byte[])((byte[])var4)).clone()));
            }

            if ((var3 == null || !var3.contains("Username")) && this.bean.isUsernameSet()) {
               var5.setUsername(this.bean.getUsername());
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
         this.inferSubTree(this.bean.getRealm(), var1, var2);
      }
   }
}
