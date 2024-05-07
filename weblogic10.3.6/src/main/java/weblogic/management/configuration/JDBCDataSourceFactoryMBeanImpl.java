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
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.management.ManagementException;
import weblogic.management.mbeans.custom.JDBCDataSourceFactory;
import weblogic.utils.ArrayUtils;
import weblogic.utils.collections.CombinedIterator;

public class JDBCDataSourceFactoryMBeanImpl extends ConfigurationMBeanImpl implements JDBCDataSourceFactoryMBean, Serializable {
   private String _DriverClassName;
   private String _FactoryName;
   private String _Name;
   private String _Password;
   private byte[] _PasswordEncrypted;
   private Map _Properties;
   private String _URL;
   private String _UserName;
   private JDBCDataSourceFactory _customizer;
   private static SchemaHelper2 _schemaHelper;

   public JDBCDataSourceFactoryMBeanImpl() {
      try {
         this._customizer = new JDBCDataSourceFactory(this);
      } catch (Exception var2) {
         if (var2 instanceof RuntimeException) {
            throw (RuntimeException)var2;
         }

         throw new UndeclaredThrowableException(var2);
      }

      this._initializeProperty(-1);
   }

   public JDBCDataSourceFactoryMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);

      try {
         this._customizer = new JDBCDataSourceFactory(this);
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

   public boolean isNameSet() {
      return this._isSet(2);
   }

   public void setUserName(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._UserName;
      this._UserName = var1;
      this._postSet(7, var2, var1);
   }

   public String getUserName() {
      return this._UserName;
   }

   public boolean isUserNameSet() {
      return this._isSet(7);
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

   public void setPassword(String var1) {
      var1 = var1 == null ? null : var1.trim();
      this.setPasswordEncrypted(var1 == null ? null : this._encrypt("Password", var1));
   }

   public String getPassword() {
      byte[] var1 = this.getPasswordEncrypted();
      return var1 == null ? null : this._decrypt("Password", var1);
   }

   public boolean isPasswordSet() {
      return this.isPasswordEncryptedSet();
   }

   public byte[] getPasswordEncrypted() {
      return this._getHelper()._cloneArray(this._PasswordEncrypted);
   }

   public String getPasswordEncryptedAsString() {
      byte[] var1 = this.getPasswordEncrypted();
      return var1 == null ? null : new String(var1);
   }

   public boolean isPasswordEncryptedSet() {
      return this._isSet(9);
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

   public void setURL(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._URL;
      this._URL = var1;
      this._postSet(10, var2, var1);
   }

   public String getURL() {
      return this._URL;
   }

   public boolean isURLSet() {
      return this._isSet(10);
   }

   public void setDriverClassName(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._DriverClassName;
      this._DriverClassName = var1;
      this._postSet(11, var2, var1);
   }

   public String getDriverClassName() {
      return this._DriverClassName;
   }

   public boolean isDriverClassNameSet() {
      return this._isSet(11);
   }

   public Map getProperties() {
      return this._Properties;
   }

   public String getPropertiesAsString() {
      return StringHelper.objectToString(this.getProperties());
   }

   public boolean isPropertiesSet() {
      return this._isSet(12);
   }

   public void setPropertiesAsString(String var1) {
      try {
         this.setProperties(StringHelper.stringToMap(var1));
      } catch (Exception var3) {
         if (var3 instanceof RuntimeException) {
            throw (RuntimeException)var3;
         } else {
            throw new UndeclaredThrowableException(var3);
         }
      }
   }

   public void touch() throws ConfigurationException {
      this._customizer.touch();
   }

   public void freezeCurrentValue(String var1) throws AttributeNotFoundException, MBeanException {
      this._customizer.freezeCurrentValue(var1);
   }

   public void setProperties(Map var1) {
      Map var2 = this.getProperties();

      try {
         this._customizer.setProperties(var1);
      } catch (InvalidAttributeValueException var4) {
         throw new UndeclaredThrowableException(var4);
      }

      this._postSet(12, var2, var1);
   }

   public void restoreDefaultValue(String var1) throws AttributeNotFoundException {
      this._customizer.restoreDefaultValue(var1);
   }

   public void setFactoryName(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._FactoryName;
      this._FactoryName = var1;
      this._postSet(13, var2, var1);
   }

   public String getFactoryName() {
      return this._FactoryName;
   }

   public boolean isFactoryNameSet() {
      return this._isSet(13);
   }

   public Object _getKey() {
      return this.getName();
   }

   public void _validate() throws IllegalArgumentException {
      super._validate();
   }

   public void setPasswordEncrypted(byte[] var1) {
      byte[] var2 = this._PasswordEncrypted;
      if (this._isProductionModeEnabled() && var1 != null && !this._isEncrypted(var1)) {
         throw new IllegalArgumentException("In production mode, it's not allowed to set a clear text value to the property: PasswordEncrypted of JDBCDataSourceFactoryMBean");
      } else {
         this._getHelper()._clearArray(this._PasswordEncrypted);
         this._PasswordEncrypted = this._getHelper()._cloneArray(var1);
         this._postSet(9, var2, var1);
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
         if (var1 == 8) {
            this._markSet(9, false);
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
         var1 = 11;
      }

      try {
         switch (var1) {
            case 11:
               this._DriverClassName = null;
               if (var2) {
                  break;
               }
            case 13:
               this._FactoryName = null;
               if (var2) {
                  break;
               }
            case 2:
               this._customizer.setName((String)null);
               if (var2) {
                  break;
               }
            case 8:
               this._PasswordEncrypted = null;
               if (var2) {
                  break;
               }
            case 9:
               this._PasswordEncrypted = null;
               if (var2) {
                  break;
               }
            case 12:
               this._customizer.setProperties((Map)null);
               if (var2) {
                  break;
               }
            case 10:
               this._URL = null;
               if (var2) {
                  break;
               }
            case 7:
               this._UserName = null;
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
      return "JDBCDataSourceFactory";
   }

   public void putValue(String var1, Object var2) {
      String var4;
      if (var1.equals("DriverClassName")) {
         var4 = this._DriverClassName;
         this._DriverClassName = (String)var2;
         this._postSet(11, var4, this._DriverClassName);
      } else if (var1.equals("FactoryName")) {
         var4 = this._FactoryName;
         this._FactoryName = (String)var2;
         this._postSet(13, var4, this._FactoryName);
      } else if (var1.equals("Name")) {
         var4 = this._Name;
         this._Name = (String)var2;
         this._postSet(2, var4, this._Name);
      } else if (var1.equals("Password")) {
         var4 = this._Password;
         this._Password = (String)var2;
         this._postSet(8, var4, this._Password);
      } else if (var1.equals("PasswordEncrypted")) {
         byte[] var6 = this._PasswordEncrypted;
         this._PasswordEncrypted = (byte[])((byte[])var2);
         this._postSet(9, var6, this._PasswordEncrypted);
      } else if (var1.equals("Properties")) {
         Map var5 = this._Properties;
         this._Properties = (Map)var2;
         this._postSet(12, var5, this._Properties);
      } else if (var1.equals("URL")) {
         var4 = this._URL;
         this._URL = (String)var2;
         this._postSet(10, var4, this._URL);
      } else if (var1.equals("UserName")) {
         var4 = this._UserName;
         this._UserName = (String)var2;
         this._postSet(7, var4, this._UserName);
      } else if (var1.equals("customizer")) {
         JDBCDataSourceFactory var3 = this._customizer;
         this._customizer = (JDBCDataSourceFactory)var2;
      } else {
         super.putValue(var1, var2);
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("DriverClassName")) {
         return this._DriverClassName;
      } else if (var1.equals("FactoryName")) {
         return this._FactoryName;
      } else if (var1.equals("Name")) {
         return this._Name;
      } else if (var1.equals("Password")) {
         return this._Password;
      } else if (var1.equals("PasswordEncrypted")) {
         return this._PasswordEncrypted;
      } else if (var1.equals("Properties")) {
         return this._Properties;
      } else if (var1.equals("URL")) {
         return this._URL;
      } else if (var1.equals("UserName")) {
         return this._UserName;
      } else {
         return var1.equals("customizer") ? this._customizer : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends ConfigurationMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 3:
               if (var1.equals("url")) {
                  return 10;
               }
               break;
            case 4:
               if (var1.equals("name")) {
                  return 2;
               }
            case 5:
            case 6:
            case 7:
            case 11:
            case 13:
            case 14:
            case 15:
            case 16:
            default:
               break;
            case 8:
               if (var1.equals("password")) {
                  return 8;
               }
               break;
            case 9:
               if (var1.equals("user-name")) {
                  return 7;
               }
               break;
            case 10:
               if (var1.equals("properties")) {
                  return 12;
               }
               break;
            case 12:
               if (var1.equals("factory-name")) {
                  return 13;
               }
               break;
            case 17:
               if (var1.equals("driver-class-name")) {
                  return 11;
               }
               break;
            case 18:
               if (var1.equals("password-encrypted")) {
                  return 9;
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
               return "user-name";
            case 8:
               return "password";
            case 9:
               return "password-encrypted";
            case 10:
               return "url";
            case 11:
               return "driver-class-name";
            case 12:
               return "properties";
            case 13:
               return "factory-name";
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

   protected static class Helper extends ConfigurationMBeanImpl.Helper {
      private JDBCDataSourceFactoryMBeanImpl bean;

      protected Helper(JDBCDataSourceFactoryMBeanImpl var1) {
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
               return "UserName";
            case 8:
               return "Password";
            case 9:
               return "PasswordEncrypted";
            case 10:
               return "URL";
            case 11:
               return "DriverClassName";
            case 12:
               return "Properties";
            case 13:
               return "FactoryName";
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("DriverClassName")) {
            return 11;
         } else if (var1.equals("FactoryName")) {
            return 13;
         } else if (var1.equals("Name")) {
            return 2;
         } else if (var1.equals("Password")) {
            return 8;
         } else if (var1.equals("PasswordEncrypted")) {
            return 9;
         } else if (var1.equals("Properties")) {
            return 12;
         } else if (var1.equals("URL")) {
            return 10;
         } else {
            return var1.equals("UserName") ? 7 : super.getPropertyIndex(var1);
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
            if (this.bean.isDriverClassNameSet()) {
               var2.append("DriverClassName");
               var2.append(String.valueOf(this.bean.getDriverClassName()));
            }

            if (this.bean.isFactoryNameSet()) {
               var2.append("FactoryName");
               var2.append(String.valueOf(this.bean.getFactoryName()));
            }

            if (this.bean.isNameSet()) {
               var2.append("Name");
               var2.append(String.valueOf(this.bean.getName()));
            }

            if (this.bean.isPasswordSet()) {
               var2.append("Password");
               var2.append(String.valueOf(this.bean.getPassword()));
            }

            if (this.bean.isPasswordEncryptedSet()) {
               var2.append("PasswordEncrypted");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getPasswordEncrypted())));
            }

            if (this.bean.isPropertiesSet()) {
               var2.append("Properties");
               var2.append(String.valueOf(this.bean.getProperties()));
            }

            if (this.bean.isURLSet()) {
               var2.append("URL");
               var2.append(String.valueOf(this.bean.getURL()));
            }

            if (this.bean.isUserNameSet()) {
               var2.append("UserName");
               var2.append(String.valueOf(this.bean.getUserName()));
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
            JDBCDataSourceFactoryMBeanImpl var2 = (JDBCDataSourceFactoryMBeanImpl)var1;
            this.computeDiff("DriverClassName", this.bean.getDriverClassName(), var2.getDriverClassName(), false);
            this.computeDiff("FactoryName", this.bean.getFactoryName(), var2.getFactoryName(), false);
            this.computeDiff("Name", this.bean.getName(), var2.getName(), false);
            this.computeDiff("PasswordEncrypted", this.bean.getPasswordEncrypted(), var2.getPasswordEncrypted(), false);
            this.computeDiff("Properties", this.bean.getProperties(), var2.getProperties(), false);
            this.computeDiff("URL", this.bean.getURL(), var2.getURL(), false);
            this.computeDiff("UserName", this.bean.getUserName(), var2.getUserName(), false);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            JDBCDataSourceFactoryMBeanImpl var3 = (JDBCDataSourceFactoryMBeanImpl)var1.getSourceBean();
            JDBCDataSourceFactoryMBeanImpl var4 = (JDBCDataSourceFactoryMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("DriverClassName")) {
                  var3.setDriverClassName(var4.getDriverClassName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 11);
               } else if (var5.equals("FactoryName")) {
                  var3.setFactoryName(var4.getFactoryName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 13);
               } else if (var5.equals("Name")) {
                  var3.setName(var4.getName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 2);
               } else if (!var5.equals("Password")) {
                  if (var5.equals("PasswordEncrypted")) {
                     var3.setPasswordEncrypted(var4.getPasswordEncrypted());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 9);
                  } else if (var5.equals("Properties")) {
                     var3.setProperties(var4.getProperties());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 12);
                  } else if (var5.equals("URL")) {
                     var3.setURL(var4.getURL());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 10);
                  } else if (var5.equals("UserName")) {
                     var3.setUserName(var4.getUserName());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 7);
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
            JDBCDataSourceFactoryMBeanImpl var5 = (JDBCDataSourceFactoryMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("DriverClassName")) && this.bean.isDriverClassNameSet()) {
               var5.setDriverClassName(this.bean.getDriverClassName());
            }

            if ((var3 == null || !var3.contains("FactoryName")) && this.bean.isFactoryNameSet()) {
               var5.setFactoryName(this.bean.getFactoryName());
            }

            if ((var3 == null || !var3.contains("Name")) && this.bean.isNameSet()) {
               var5.setName(this.bean.getName());
            }

            if ((var3 == null || !var3.contains("PasswordEncrypted")) && this.bean.isPasswordEncryptedSet()) {
               byte[] var4 = this.bean.getPasswordEncrypted();
               var5.setPasswordEncrypted(var4 == null ? null : (byte[])((byte[])((byte[])((byte[])var4)).clone()));
            }

            if ((var3 == null || !var3.contains("Properties")) && this.bean.isPropertiesSet()) {
               var5.setProperties(this.bean.getProperties());
            }

            if ((var3 == null || !var3.contains("URL")) && this.bean.isURLSet()) {
               var5.setURL(this.bean.getURL());
            }

            if ((var3 == null || !var3.contains("UserName")) && this.bean.isUserNameSet()) {
               var5.setUserName(this.bean.getUserName());
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
