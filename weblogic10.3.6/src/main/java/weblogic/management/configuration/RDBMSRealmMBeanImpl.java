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

public class RDBMSRealmMBeanImpl extends BasicRealmMBeanImpl implements RDBMSRealmMBean, Serializable {
   private String _DatabaseDriver;
   private String _DatabasePassword;
   private byte[] _DatabasePasswordEncrypted;
   private String _DatabaseURL;
   private String _DatabaseUserName;
   private String _RealmClassName;
   private Properties _SchemaProperties;
   private static SchemaHelper2 _schemaHelper;

   public RDBMSRealmMBeanImpl() {
      this._initializeProperty(-1);
   }

   public RDBMSRealmMBeanImpl(DescriptorBean var1, int var2) {
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
      String var2 = LegalHelper.checkClassName(var1);
      String var3 = this._RealmClassName;
      this._RealmClassName = var2;
      this._postSet(7, var3, var2);
   }

   public String getDatabaseDriver() {
      return this._DatabaseDriver;
   }

   public boolean isDatabaseDriverSet() {
      return this._isSet(8);
   }

   public void setDatabaseDriver(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._DatabaseDriver;
      this._DatabaseDriver = var1;
      this._postSet(8, var2, var1);
   }

   public String getDatabaseURL() {
      return this._DatabaseURL;
   }

   public boolean isDatabaseURLSet() {
      return this._isSet(9);
   }

   public void setDatabaseURL(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._DatabaseURL;
      this._DatabaseURL = var1;
      this._postSet(9, var2, var1);
   }

   public String getDatabaseUserName() {
      return this._DatabaseUserName;
   }

   public boolean isDatabaseUserNameSet() {
      return this._isSet(10);
   }

   public void setDatabaseUserName(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._DatabaseUserName;
      this._DatabaseUserName = var1;
      this._postSet(10, var2, var1);
   }

   public String getDatabasePassword() {
      byte[] var1 = this.getDatabasePasswordEncrypted();
      return var1 == null ? null : this._decrypt("DatabasePassword", var1);
   }

   public boolean isDatabasePasswordSet() {
      return this.isDatabasePasswordEncryptedSet();
   }

   public void setDatabasePassword(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      this.setDatabasePasswordEncrypted(var1 == null ? null : this._encrypt("DatabasePassword", var1));
   }

   public byte[] getDatabasePasswordEncrypted() {
      return this._getHelper()._cloneArray(this._DatabasePasswordEncrypted);
   }

   public String getDatabasePasswordEncryptedAsString() {
      byte[] var1 = this.getDatabasePasswordEncrypted();
      return var1 == null ? null : new String(var1);
   }

   public boolean isDatabasePasswordEncryptedSet() {
      return this._isSet(12);
   }

   public void setDatabasePasswordEncryptedAsString(String var1) {
      try {
         byte[] var2 = var1 == null ? null : var1.getBytes();
         this.setDatabasePasswordEncrypted(var2);
      } catch (Exception var3) {
         if (var3 instanceof RuntimeException) {
            throw (RuntimeException)var3;
         } else {
            throw new UndeclaredThrowableException(var3);
         }
      }
   }

   public Properties getSchemaProperties() {
      return this._SchemaProperties;
   }

   public String getSchemaPropertiesAsString() {
      return StringHelper.objectToString(this.getSchemaProperties());
   }

   public boolean isSchemaPropertiesSet() {
      return this._isSet(13);
   }

   public void setSchemaPropertiesAsString(String var1) {
      try {
         this.setSchemaProperties(StringHelper.stringToProperties(var1));
      } catch (Exception var3) {
         if (var3 instanceof RuntimeException) {
            throw (RuntimeException)var3;
         } else {
            throw new UndeclaredThrowableException(var3);
         }
      }
   }

   public void setSchemaProperties(Properties var1) throws InvalidAttributeValueException {
      Properties var2 = this._SchemaProperties;
      this._SchemaProperties = var1;
      this._postSet(13, var2, var1);
   }

   public Object _getKey() {
      return super._getKey();
   }

   public void _validate() throws IllegalArgumentException {
      super._validate();
   }

   public void setDatabasePasswordEncrypted(byte[] var1) {
      byte[] var2 = this._DatabasePasswordEncrypted;
      if (this._isProductionModeEnabled() && var1 != null && !this._isEncrypted(var1)) {
         throw new IllegalArgumentException("In production mode, it's not allowed to set a clear text value to the property: DatabasePasswordEncrypted of RDBMSRealmMBean");
      } else {
         this._getHelper()._clearArray(this._DatabasePasswordEncrypted);
         this._DatabasePasswordEncrypted = this._getHelper()._cloneArray(var1);
         this._postSet(12, var2, var1);
      }
   }

   protected void _unSet(int var1) {
      if (!this._initializeProperty(var1)) {
         super._unSet(var1);
      } else {
         this._markSet(var1, false);
         if (var1 == 11) {
            this._markSet(12, false);
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
               this._DatabaseDriver = "";
               if (var2) {
                  break;
               }
            case 11:
               this._DatabasePasswordEncrypted = null;
               if (var2) {
                  break;
               }
            case 12:
               this._DatabasePasswordEncrypted = null;
               if (var2) {
                  break;
               }
            case 9:
               this._DatabaseURL = "";
               if (var2) {
                  break;
               }
            case 10:
               this._DatabaseUserName = null;
               if (var2) {
                  break;
               }
            case 7:
               this._RealmClassName = "";
               if (var2) {
                  break;
               }
            case 13:
               this._SchemaProperties = null;
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
      return "RDBMSRealm";
   }

   public void putValue(String var1, Object var2) {
      String var4;
      if (var1.equals("DatabaseDriver")) {
         var4 = this._DatabaseDriver;
         this._DatabaseDriver = (String)var2;
         this._postSet(8, var4, this._DatabaseDriver);
      } else if (var1.equals("DatabasePassword")) {
         var4 = this._DatabasePassword;
         this._DatabasePassword = (String)var2;
         this._postSet(11, var4, this._DatabasePassword);
      } else if (var1.equals("DatabasePasswordEncrypted")) {
         byte[] var5 = this._DatabasePasswordEncrypted;
         this._DatabasePasswordEncrypted = (byte[])((byte[])var2);
         this._postSet(12, var5, this._DatabasePasswordEncrypted);
      } else if (var1.equals("DatabaseURL")) {
         var4 = this._DatabaseURL;
         this._DatabaseURL = (String)var2;
         this._postSet(9, var4, this._DatabaseURL);
      } else if (var1.equals("DatabaseUserName")) {
         var4 = this._DatabaseUserName;
         this._DatabaseUserName = (String)var2;
         this._postSet(10, var4, this._DatabaseUserName);
      } else if (var1.equals("RealmClassName")) {
         var4 = this._RealmClassName;
         this._RealmClassName = (String)var2;
         this._postSet(7, var4, this._RealmClassName);
      } else if (var1.equals("SchemaProperties")) {
         Properties var3 = this._SchemaProperties;
         this._SchemaProperties = (Properties)var2;
         this._postSet(13, var3, this._SchemaProperties);
      } else {
         super.putValue(var1, var2);
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("DatabaseDriver")) {
         return this._DatabaseDriver;
      } else if (var1.equals("DatabasePassword")) {
         return this._DatabasePassword;
      } else if (var1.equals("DatabasePasswordEncrypted")) {
         return this._DatabasePasswordEncrypted;
      } else if (var1.equals("DatabaseURL")) {
         return this._DatabaseURL;
      } else if (var1.equals("DatabaseUserName")) {
         return this._DatabaseUserName;
      } else if (var1.equals("RealmClassName")) {
         return this._RealmClassName;
      } else {
         return var1.equals("SchemaProperties") ? this._SchemaProperties : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends BasicRealmMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 12:
               if (var1.equals("database-url")) {
                  return 9;
               }
            case 13:
            case 14:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            default:
               break;
            case 15:
               if (var1.equals("database-driver")) {
                  return 8;
               }
               break;
            case 16:
               if (var1.equals("realm-class-name")) {
                  return 7;
               }
               break;
            case 17:
               if (var1.equals("database-password")) {
                  return 11;
               }

               if (var1.equals("schema-properties")) {
                  return 13;
               }
               break;
            case 18:
               if (var1.equals("database-user-name")) {
                  return 10;
               }
               break;
            case 27:
               if (var1.equals("database-password-encrypted")) {
                  return 12;
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
               return "database-driver";
            case 9:
               return "database-url";
            case 10:
               return "database-user-name";
            case 11:
               return "database-password";
            case 12:
               return "database-password-encrypted";
            case 13:
               return "schema-properties";
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
      private RDBMSRealmMBeanImpl bean;

      protected Helper(RDBMSRealmMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 7:
               return "RealmClassName";
            case 8:
               return "DatabaseDriver";
            case 9:
               return "DatabaseURL";
            case 10:
               return "DatabaseUserName";
            case 11:
               return "DatabasePassword";
            case 12:
               return "DatabasePasswordEncrypted";
            case 13:
               return "SchemaProperties";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("DatabaseDriver")) {
            return 8;
         } else if (var1.equals("DatabasePassword")) {
            return 11;
         } else if (var1.equals("DatabasePasswordEncrypted")) {
            return 12;
         } else if (var1.equals("DatabaseURL")) {
            return 9;
         } else if (var1.equals("DatabaseUserName")) {
            return 10;
         } else if (var1.equals("RealmClassName")) {
            return 7;
         } else {
            return var1.equals("SchemaProperties") ? 13 : super.getPropertyIndex(var1);
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
            if (this.bean.isDatabaseDriverSet()) {
               var2.append("DatabaseDriver");
               var2.append(String.valueOf(this.bean.getDatabaseDriver()));
            }

            if (this.bean.isDatabasePasswordSet()) {
               var2.append("DatabasePassword");
               var2.append(String.valueOf(this.bean.getDatabasePassword()));
            }

            if (this.bean.isDatabasePasswordEncryptedSet()) {
               var2.append("DatabasePasswordEncrypted");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getDatabasePasswordEncrypted())));
            }

            if (this.bean.isDatabaseURLSet()) {
               var2.append("DatabaseURL");
               var2.append(String.valueOf(this.bean.getDatabaseURL()));
            }

            if (this.bean.isDatabaseUserNameSet()) {
               var2.append("DatabaseUserName");
               var2.append(String.valueOf(this.bean.getDatabaseUserName()));
            }

            if (this.bean.isRealmClassNameSet()) {
               var2.append("RealmClassName");
               var2.append(String.valueOf(this.bean.getRealmClassName()));
            }

            if (this.bean.isSchemaPropertiesSet()) {
               var2.append("SchemaProperties");
               var2.append(String.valueOf(this.bean.getSchemaProperties()));
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
            RDBMSRealmMBeanImpl var2 = (RDBMSRealmMBeanImpl)var1;
            this.computeDiff("DatabaseDriver", this.bean.getDatabaseDriver(), var2.getDatabaseDriver(), false);
            this.computeDiff("DatabasePasswordEncrypted", this.bean.getDatabasePasswordEncrypted(), var2.getDatabasePasswordEncrypted(), false);
            this.computeDiff("DatabaseURL", this.bean.getDatabaseURL(), var2.getDatabaseURL(), false);
            this.computeDiff("DatabaseUserName", this.bean.getDatabaseUserName(), var2.getDatabaseUserName(), false);
            this.computeDiff("RealmClassName", this.bean.getRealmClassName(), var2.getRealmClassName(), false);
            this.computeDiff("SchemaProperties", this.bean.getSchemaProperties(), var2.getSchemaProperties(), false);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            RDBMSRealmMBeanImpl var3 = (RDBMSRealmMBeanImpl)var1.getSourceBean();
            RDBMSRealmMBeanImpl var4 = (RDBMSRealmMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("DatabaseDriver")) {
                  var3.setDatabaseDriver(var4.getDatabaseDriver());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 8);
               } else if (!var5.equals("DatabasePassword")) {
                  if (var5.equals("DatabasePasswordEncrypted")) {
                     var3.setDatabasePasswordEncrypted(var4.getDatabasePasswordEncrypted());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 12);
                  } else if (var5.equals("DatabaseURL")) {
                     var3.setDatabaseURL(var4.getDatabaseURL());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 9);
                  } else if (var5.equals("DatabaseUserName")) {
                     var3.setDatabaseUserName(var4.getDatabaseUserName());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 10);
                  } else if (var5.equals("RealmClassName")) {
                     var3.setRealmClassName(var4.getRealmClassName());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 7);
                  } else if (var5.equals("SchemaProperties")) {
                     var3.setSchemaProperties(var4.getSchemaProperties() == null ? null : (Properties)var4.getSchemaProperties().clone());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 13);
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
            RDBMSRealmMBeanImpl var5 = (RDBMSRealmMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("DatabaseDriver")) && this.bean.isDatabaseDriverSet()) {
               var5.setDatabaseDriver(this.bean.getDatabaseDriver());
            }

            if ((var3 == null || !var3.contains("DatabasePasswordEncrypted")) && this.bean.isDatabasePasswordEncryptedSet()) {
               byte[] var4 = this.bean.getDatabasePasswordEncrypted();
               var5.setDatabasePasswordEncrypted(var4 == null ? null : (byte[])((byte[])((byte[])((byte[])var4)).clone()));
            }

            if ((var3 == null || !var3.contains("DatabaseURL")) && this.bean.isDatabaseURLSet()) {
               var5.setDatabaseURL(this.bean.getDatabaseURL());
            }

            if ((var3 == null || !var3.contains("DatabaseUserName")) && this.bean.isDatabaseUserNameSet()) {
               var5.setDatabaseUserName(this.bean.getDatabaseUserName());
            }

            if ((var3 == null || !var3.contains("RealmClassName")) && this.bean.isRealmClassNameSet()) {
               var5.setRealmClassName(this.bean.getRealmClassName());
            }

            if ((var3 == null || !var3.contains("SchemaProperties")) && this.bean.isSchemaPropertiesSet()) {
               var5.setSchemaProperties(this.bean.getSchemaProperties());
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
