package weblogic.management.configuration;

import java.io.Serializable;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.zip.CRC32;
import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.beangen.StringHelper;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBeanHelper;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.utils.ArrayUtils;
import weblogic.utils.collections.CombinedIterator;

public class CustomRealmMBeanImpl extends BasicRealmMBeanImpl implements CustomRealmMBean, Serializable {
   private Properties _ConfigurationData;
   private String _Password;
   private byte[] _PasswordEncrypted;
   private String _RealmClassName;
   private static SchemaHelper2 _schemaHelper;

   public CustomRealmMBeanImpl() {
      this._initializeProperty(-1);
   }

   public CustomRealmMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public String getRealmClassName() {
      return this._RealmClassName;
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

   public Properties getConfigurationData() {
      return this._ConfigurationData;
   }

   public String getConfigurationDataAsString() {
      return StringHelper.objectToString(this.getConfigurationData());
   }

   public boolean isConfigurationDataSet() {
      return this._isSet(8);
   }

   public void setConfigurationDataAsString(String var1) {
      try {
         this.setConfigurationData(StringHelper.stringToProperties(var1));
      } catch (Exception var3) {
         if (var3 instanceof RuntimeException) {
            throw (RuntimeException)var3;
         } else {
            throw new UndeclaredThrowableException(var3);
         }
      }
   }

   public void setConfigurationData(Properties var1) throws InvalidAttributeValueException {
      Properties var2 = this._ConfigurationData;
      this._ConfigurationData = var1;
      this._postSet(8, var2, var1);
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
      return this._isSet(10);
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

   public Object _getKey() {
      return super._getKey();
   }

   public void _validate() throws IllegalArgumentException {
      super._validate();
   }

   public void setPasswordEncrypted(byte[] var1) {
      byte[] var2 = this._PasswordEncrypted;
      if (this._isProductionModeEnabled() && var1 != null && !this._isEncrypted(var1)) {
         throw new IllegalArgumentException("In production mode, it's not allowed to set a clear text value to the property: PasswordEncrypted of CustomRealmMBean");
      } else {
         this._getHelper()._clearArray(this._PasswordEncrypted);
         this._PasswordEncrypted = this._getHelper()._cloneArray(var1);
         this._postSet(10, var2, var1);
      }
   }

   protected void _unSet(int var1) {
      if (!this._initializeProperty(var1)) {
         super._unSet(var1);
      } else {
         this._markSet(var1, false);
         if (var1 == 9) {
            this._markSet(10, false);
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
         var1 = 8;
      }

      try {
         switch (var1) {
            case 8:
               this._ConfigurationData = null;
               if (var2) {
                  break;
               }
            case 9:
               this._PasswordEncrypted = null;
               if (var2) {
                  break;
               }
            case 10:
               this._PasswordEncrypted = null;
               if (var2) {
                  break;
               }
            case 7:
               this._RealmClassName = null;
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
      return "CustomRealm";
   }

   public void putValue(String var1, Object var2) {
      if (var1.equals("ConfigurationData")) {
         Properties var5 = this._ConfigurationData;
         this._ConfigurationData = (Properties)var2;
         this._postSet(8, var5, this._ConfigurationData);
      } else {
         String var3;
         if (var1.equals("Password")) {
            var3 = this._Password;
            this._Password = (String)var2;
            this._postSet(9, var3, this._Password);
         } else if (var1.equals("PasswordEncrypted")) {
            byte[] var4 = this._PasswordEncrypted;
            this._PasswordEncrypted = (byte[])((byte[])var2);
            this._postSet(10, var4, this._PasswordEncrypted);
         } else if (var1.equals("RealmClassName")) {
            var3 = this._RealmClassName;
            this._RealmClassName = (String)var2;
            this._postSet(7, var3, this._RealmClassName);
         } else {
            super.putValue(var1, var2);
         }
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("ConfigurationData")) {
         return this._ConfigurationData;
      } else if (var1.equals("Password")) {
         return this._Password;
      } else if (var1.equals("PasswordEncrypted")) {
         return this._PasswordEncrypted;
      } else {
         return var1.equals("RealmClassName") ? this._RealmClassName : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends BasicRealmMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 8:
               if (var1.equals("password")) {
                  return 9;
               }
               break;
            case 16:
               if (var1.equals("realm-class-name")) {
                  return 7;
               }
               break;
            case 18:
               if (var1.equals("configuration-data")) {
                  return 8;
               }

               if (var1.equals("password-encrypted")) {
                  return 10;
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
            case 7:
               return "realm-class-name";
            case 8:
               return "configuration-data";
            case 9:
               return "password";
            case 10:
               return "password-encrypted";
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

   protected static class Helper extends BasicRealmMBeanImpl.Helper {
      private CustomRealmMBeanImpl bean;

      protected Helper(CustomRealmMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 7:
               return "RealmClassName";
            case 8:
               return "ConfigurationData";
            case 9:
               return "Password";
            case 10:
               return "PasswordEncrypted";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("ConfigurationData")) {
            return 8;
         } else if (var1.equals("Password")) {
            return 9;
         } else if (var1.equals("PasswordEncrypted")) {
            return 10;
         } else {
            return var1.equals("RealmClassName") ? 7 : super.getPropertyIndex(var1);
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
            if (this.bean.isConfigurationDataSet()) {
               var2.append("ConfigurationData");
               var2.append(String.valueOf(this.bean.getConfigurationData()));
            }

            if (this.bean.isPasswordSet()) {
               var2.append("Password");
               var2.append(String.valueOf(this.bean.getPassword()));
            }

            if (this.bean.isPasswordEncryptedSet()) {
               var2.append("PasswordEncrypted");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getPasswordEncrypted())));
            }

            if (this.bean.isRealmClassNameSet()) {
               var2.append("RealmClassName");
               var2.append(String.valueOf(this.bean.getRealmClassName()));
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
            CustomRealmMBeanImpl var2 = (CustomRealmMBeanImpl)var1;
            this.computeDiff("ConfigurationData", this.bean.getConfigurationData(), var2.getConfigurationData(), false);
            this.computeDiff("PasswordEncrypted", this.bean.getPasswordEncrypted(), var2.getPasswordEncrypted(), false);
            this.computeDiff("RealmClassName", this.bean.getRealmClassName(), var2.getRealmClassName(), false);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            CustomRealmMBeanImpl var3 = (CustomRealmMBeanImpl)var1.getSourceBean();
            CustomRealmMBeanImpl var4 = (CustomRealmMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("ConfigurationData")) {
                  var3.setConfigurationData(var4.getConfigurationData() == null ? null : (Properties)var4.getConfigurationData().clone());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 8);
               } else if (!var5.equals("Password")) {
                  if (var5.equals("PasswordEncrypted")) {
                     var3.setPasswordEncrypted(var4.getPasswordEncrypted());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 10);
                  } else if (var5.equals("RealmClassName")) {
                     var3.setRealmClassName(var4.getRealmClassName());
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
            CustomRealmMBeanImpl var5 = (CustomRealmMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("ConfigurationData")) && this.bean.isConfigurationDataSet()) {
               var5.setConfigurationData(this.bean.getConfigurationData());
            }

            if ((var3 == null || !var3.contains("PasswordEncrypted")) && this.bean.isPasswordEncryptedSet()) {
               byte[] var4 = this.bean.getPasswordEncrypted();
               var5.setPasswordEncrypted(var4 == null ? null : (byte[])((byte[])((byte[])((byte[])var4)).clone()));
            }

            if ((var3 == null || !var3.contains("RealmClassName")) && this.bean.isRealmClassNameSet()) {
               var5.setRealmClassName(this.bean.getRealmClassName());
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
