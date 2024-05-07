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

public class ConfigurationPropertyMBeanImpl extends ConfigurationMBeanImpl implements ConfigurationPropertyMBean, Serializable {
   private boolean _EncryptValueRequired;
   private String _EncryptedValue;
   private byte[] _EncryptedValueEncrypted;
   private String _Value;
   private static SchemaHelper2 _schemaHelper;

   public ConfigurationPropertyMBeanImpl() {
      this._initializeProperty(-1);
   }

   public ConfigurationPropertyMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public boolean isEncryptValueRequired() {
      return this._EncryptValueRequired;
   }

   public boolean isEncryptValueRequiredSet() {
      return this._isSet(7);
   }

   public void setEncryptValueRequired(boolean var1) {
      boolean var2 = this._EncryptValueRequired;
      this._EncryptValueRequired = var1;
      this._postSet(7, var2, var1);
   }

   public String getValue() {
      return this._Value;
   }

   public boolean isValueSet() {
      return this._isSet(8);
   }

   public void setValue(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._Value;
      this._Value = var1;
      this._postSet(8, var2, var1);
   }

   public String getEncryptedValue() {
      byte[] var1 = this.getEncryptedValueEncrypted();
      return var1 == null ? null : this._decrypt("EncryptedValue", var1);
   }

   public boolean isEncryptedValueSet() {
      return this.isEncryptedValueEncryptedSet();
   }

   public void setEncryptedValue(String var1) {
      var1 = var1 == null ? null : var1.trim();

      try {
         this.setEncryptedValueEncrypted(var1 == null ? null : this._encrypt("EncryptedValue", var1));
      } catch (InvalidAttributeValueException var3) {
      }

   }

   public byte[] getEncryptedValueEncrypted() {
      return this._getHelper()._cloneArray(this._EncryptedValueEncrypted);
   }

   public String getEncryptedValueEncryptedAsString() {
      byte[] var1 = this.getEncryptedValueEncrypted();
      return var1 == null ? null : new String(var1);
   }

   public boolean isEncryptedValueEncryptedSet() {
      return this._isSet(10);
   }

   public void setEncryptedValueEncryptedAsString(String var1) {
      try {
         byte[] var2 = var1 == null ? null : var1.getBytes();
         this.setEncryptedValueEncrypted(var2);
      } catch (Exception var3) {
         if (var3 instanceof RuntimeException) {
            throw (RuntimeException)var3;
         } else {
            throw new UndeclaredThrowableException(var3);
         }
      }
   }

   public void setEncryptedValueEncrypted(byte[] var1) throws InvalidAttributeValueException {
      byte[] var2 = this._EncryptedValueEncrypted;
      if (this._isProductionModeEnabled() && var1 != null && !this._isEncrypted(var1)) {
         throw new IllegalArgumentException("In production mode, it's not allowed to set a clear text value to the property: EncryptedValueEncrypted of ConfigurationPropertyMBean");
      } else {
         this._getHelper()._clearArray(this._EncryptedValueEncrypted);
         this._EncryptedValueEncrypted = this._getHelper()._cloneArray(var1);
         this._postSet(10, var2, var1);
      }
   }

   public Object _getKey() {
      return super._getKey();
   }

   public void _validate() throws IllegalArgumentException {
      super._validate();
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
         var1 = 9;
      }

      try {
         switch (var1) {
            case 9:
               this._EncryptedValueEncrypted = null;
               if (var2) {
                  break;
               }
            case 10:
               this._EncryptedValueEncrypted = null;
               if (var2) {
                  break;
               }
            case 8:
               this._Value = "";
               if (var2) {
                  break;
               }
            case 7:
               this._EncryptValueRequired = false;
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
      return "ConfigurationProperty";
   }

   public void putValue(String var1, Object var2) {
      if (var1.equals("EncryptValueRequired")) {
         boolean var5 = this._EncryptValueRequired;
         this._EncryptValueRequired = (Boolean)var2;
         this._postSet(7, var5, this._EncryptValueRequired);
      } else {
         String var3;
         if (var1.equals("EncryptedValue")) {
            var3 = this._EncryptedValue;
            this._EncryptedValue = (String)var2;
            this._postSet(9, var3, this._EncryptedValue);
         } else if (var1.equals("EncryptedValueEncrypted")) {
            byte[] var4 = this._EncryptedValueEncrypted;
            this._EncryptedValueEncrypted = (byte[])((byte[])var2);
            this._postSet(10, var4, this._EncryptedValueEncrypted);
         } else if (var1.equals("Value")) {
            var3 = this._Value;
            this._Value = (String)var2;
            this._postSet(8, var3, this._Value);
         } else {
            super.putValue(var1, var2);
         }
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("EncryptValueRequired")) {
         return new Boolean(this._EncryptValueRequired);
      } else if (var1.equals("EncryptedValue")) {
         return this._EncryptedValue;
      } else if (var1.equals("EncryptedValueEncrypted")) {
         return this._EncryptedValueEncrypted;
      } else {
         return var1.equals("Value") ? this._Value : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends ConfigurationMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 5:
               if (var1.equals("value")) {
                  return 8;
               }
               break;
            case 15:
               if (var1.equals("encrypted-value")) {
                  return 9;
               }
               break;
            case 22:
               if (var1.equals("encrypt-value-required")) {
                  return 7;
               }
               break;
            case 25:
               if (var1.equals("encrypted-value-encrypted")) {
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
               return "encrypt-value-required";
            case 8:
               return "value";
            case 9:
               return "encrypted-value";
            case 10:
               return "encrypted-value-encrypted";
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

   protected static class Helper extends ConfigurationMBeanImpl.Helper {
      private ConfigurationPropertyMBeanImpl bean;

      protected Helper(ConfigurationPropertyMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 7:
               return "EncryptValueRequired";
            case 8:
               return "Value";
            case 9:
               return "EncryptedValue";
            case 10:
               return "EncryptedValueEncrypted";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("EncryptedValue")) {
            return 9;
         } else if (var1.equals("EncryptedValueEncrypted")) {
            return 10;
         } else if (var1.equals("Value")) {
            return 8;
         } else {
            return var1.equals("EncryptValueRequired") ? 7 : super.getPropertyIndex(var1);
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
            if (this.bean.isEncryptedValueSet()) {
               var2.append("EncryptedValue");
               var2.append(String.valueOf(this.bean.getEncryptedValue()));
            }

            if (this.bean.isEncryptedValueEncryptedSet()) {
               var2.append("EncryptedValueEncrypted");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getEncryptedValueEncrypted())));
            }

            if (this.bean.isValueSet()) {
               var2.append("Value");
               var2.append(String.valueOf(this.bean.getValue()));
            }

            if (this.bean.isEncryptValueRequiredSet()) {
               var2.append("EncryptValueRequired");
               var2.append(String.valueOf(this.bean.isEncryptValueRequired()));
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
            ConfigurationPropertyMBeanImpl var2 = (ConfigurationPropertyMBeanImpl)var1;
            this.computeDiff("EncryptedValueEncrypted", this.bean.getEncryptedValueEncrypted(), var2.getEncryptedValueEncrypted(), false);
            this.computeDiff("Value", this.bean.getValue(), var2.getValue(), true);
            this.computeDiff("EncryptValueRequired", this.bean.isEncryptValueRequired(), var2.isEncryptValueRequired(), true);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            ConfigurationPropertyMBeanImpl var3 = (ConfigurationPropertyMBeanImpl)var1.getSourceBean();
            ConfigurationPropertyMBeanImpl var4 = (ConfigurationPropertyMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (!var5.equals("EncryptedValue")) {
                  if (var5.equals("EncryptedValueEncrypted")) {
                     var3._conditionalUnset(var2.isUnsetUpdate(), 10);
                  } else if (var5.equals("Value")) {
                     var3.setValue(var4.getValue());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 8);
                  } else if (var5.equals("EncryptValueRequired")) {
                     var3.setEncryptValueRequired(var4.isEncryptValueRequired());
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
            ConfigurationPropertyMBeanImpl var5 = (ConfigurationPropertyMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("EncryptedValueEncrypted")) && this.bean.isEncryptedValueEncryptedSet()) {
               byte[] var4 = this.bean.getEncryptedValueEncrypted();
               var5.setEncryptedValueEncrypted(var4 == null ? null : (byte[])((byte[])((byte[])((byte[])var4)).clone()));
            }

            if ((var3 == null || !var3.contains("Value")) && this.bean.isValueSet()) {
               var5.setValue(this.bean.getValue());
            }

            if ((var3 == null || !var3.contains("EncryptValueRequired")) && this.bean.isEncryptValueRequiredSet()) {
               var5.setEncryptValueRequired(this.bean.isEncryptValueRequired());
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
