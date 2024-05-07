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
import weblogic.descriptor.DescriptorValidateException;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBeanHelper;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.utils.ArrayUtils;
import weblogic.utils.collections.CombinedIterator;

public class BridgeDestinationCommonMBeanImpl extends ConfigurationMBeanImpl implements BridgeDestinationCommonMBean, Serializable {
   private String _AdapterJNDIName;
   private String _Classpath;
   private String _UserName;
   private String _UserPassword;
   private byte[] _UserPasswordEncrypted;
   private static SchemaHelper2 _schemaHelper;

   public BridgeDestinationCommonMBeanImpl() {
      this._initializeProperty(-1);
   }

   public BridgeDestinationCommonMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public String getAdapterJNDIName() {
      return this._AdapterJNDIName;
   }

   public boolean isAdapterJNDINameSet() {
      return this._isSet(7);
   }

   public void setAdapterJNDIName(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      weblogic.descriptor.beangen.LegalChecks.checkNonEmptyString("AdapterJNDIName", var1);
      weblogic.descriptor.beangen.LegalChecks.checkNonNull("AdapterJNDIName", var1);
      String var2 = this._AdapterJNDIName;
      this._AdapterJNDIName = var1;
      this._postSet(7, var2, var1);
   }

   public String getUserName() {
      return this._UserName;
   }

   public boolean isUserNameSet() {
      return this._isSet(8);
   }

   public void setUserName(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._UserName;
      this._UserName = var1;
      this._postSet(8, var2, var1);
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
      return this._isSet(10);
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

   public String getClasspath() {
      return this._Classpath;
   }

   public boolean isClasspathSet() {
      return this._isSet(11);
   }

   public void setClasspath(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._Classpath;
      this._Classpath = var1;
      this._postSet(11, var2, var1);
   }

   public Object _getKey() {
      return super._getKey();
   }

   public void _validate() throws IllegalArgumentException {
      super._validate();
   }

   public void setUserPasswordEncrypted(byte[] var1) {
      byte[] var2 = this._UserPasswordEncrypted;
      if (this._isProductionModeEnabled() && var1 != null && !this._isEncrypted(var1)) {
         throw new IllegalArgumentException("In production mode, it's not allowed to set a clear text value to the property: UserPasswordEncrypted of BridgeDestinationCommonMBean");
      } else {
         this._getHelper()._clearArray(this._UserPasswordEncrypted);
         this._UserPasswordEncrypted = this._getHelper()._cloneArray(var1);
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
         var1 = 7;
      }

      try {
         switch (var1) {
            case 7:
               this._AdapterJNDIName = "eis.jms.WLSConnectionFactoryJNDIXA";
               if (var2) {
                  break;
               }
            case 11:
               this._Classpath = null;
               if (var2) {
                  break;
               }
            case 8:
               this._UserName = null;
               if (var2) {
                  break;
               }
            case 9:
               this._UserPasswordEncrypted = null;
               if (var2) {
                  break;
               }
            case 10:
               this._UserPasswordEncrypted = null;
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
      return "BridgeDestinationCommon";
   }

   public void putValue(String var1, Object var2) {
      String var4;
      if (var1.equals("AdapterJNDIName")) {
         var4 = this._AdapterJNDIName;
         this._AdapterJNDIName = (String)var2;
         this._postSet(7, var4, this._AdapterJNDIName);
      } else if (var1.equals("Classpath")) {
         var4 = this._Classpath;
         this._Classpath = (String)var2;
         this._postSet(11, var4, this._Classpath);
      } else if (var1.equals("UserName")) {
         var4 = this._UserName;
         this._UserName = (String)var2;
         this._postSet(8, var4, this._UserName);
      } else if (var1.equals("UserPassword")) {
         var4 = this._UserPassword;
         this._UserPassword = (String)var2;
         this._postSet(9, var4, this._UserPassword);
      } else if (var1.equals("UserPasswordEncrypted")) {
         byte[] var3 = this._UserPasswordEncrypted;
         this._UserPasswordEncrypted = (byte[])((byte[])var2);
         this._postSet(10, var3, this._UserPasswordEncrypted);
      } else {
         super.putValue(var1, var2);
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("AdapterJNDIName")) {
         return this._AdapterJNDIName;
      } else if (var1.equals("Classpath")) {
         return this._Classpath;
      } else if (var1.equals("UserName")) {
         return this._UserName;
      } else if (var1.equals("UserPassword")) {
         return this._UserPassword;
      } else {
         return var1.equals("UserPasswordEncrypted") ? this._UserPasswordEncrypted : super.getValue(var1);
      }
   }

   public static void validateGeneration() {
      try {
         weblogic.descriptor.beangen.LegalChecks.checkNonEmptyString("AdapterJNDIName", "eis.jms.WLSConnectionFactoryJNDIXA");
      } catch (IllegalArgumentException var2) {
         throw new DescriptorValidateException("The default value for the property  is zero-length. Properties annotated with false value on @legalZeroLength or @legalNull  should either have @required/@derivedDefault annotations or have a non-zero-length value on @default annotation. Refer annotation legalZeroLength on property AdapterJNDIName in BridgeDestinationCommonMBean" + var2.getMessage());
      }

      try {
         weblogic.descriptor.beangen.LegalChecks.checkNonNull("AdapterJNDIName", "eis.jms.WLSConnectionFactoryJNDIXA");
      } catch (IllegalArgumentException var1) {
         throw new DescriptorValidateException("The default value for the property  is null. Properties annotated with false value on @legalZeroLength or @legalNull  should either have @required/@derivedDefault annotations or have a non-null value on @default annotation. Refer annotation legalNull on property AdapterJNDIName in BridgeDestinationCommonMBean" + var1.getMessage());
      }
   }

   public static class SchemaHelper2 extends ConfigurationMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 9:
               if (var1.equals("classpath")) {
                  return 11;
               }

               if (var1.equals("user-name")) {
                  return 8;
               }
               break;
            case 13:
               if (var1.equals("user-password")) {
                  return 9;
               }
               break;
            case 17:
               if (var1.equals("adapter-jndi-name")) {
                  return 7;
               }
               break;
            case 23:
               if (var1.equals("user-password-encrypted")) {
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
               return "adapter-jndi-name";
            case 8:
               return "user-name";
            case 9:
               return "user-password";
            case 10:
               return "user-password-encrypted";
            case 11:
               return "classpath";
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
      private BridgeDestinationCommonMBeanImpl bean;

      protected Helper(BridgeDestinationCommonMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 7:
               return "AdapterJNDIName";
            case 8:
               return "UserName";
            case 9:
               return "UserPassword";
            case 10:
               return "UserPasswordEncrypted";
            case 11:
               return "Classpath";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("AdapterJNDIName")) {
            return 7;
         } else if (var1.equals("Classpath")) {
            return 11;
         } else if (var1.equals("UserName")) {
            return 8;
         } else if (var1.equals("UserPassword")) {
            return 9;
         } else {
            return var1.equals("UserPasswordEncrypted") ? 10 : super.getPropertyIndex(var1);
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
            if (this.bean.isAdapterJNDINameSet()) {
               var2.append("AdapterJNDIName");
               var2.append(String.valueOf(this.bean.getAdapterJNDIName()));
            }

            if (this.bean.isClasspathSet()) {
               var2.append("Classpath");
               var2.append(String.valueOf(this.bean.getClasspath()));
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

            var1.update(var2.toString().getBytes());
            return var1.getValue();
         } catch (Exception var7) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var7);
         }
      }

      protected void computeDiff(AbstractDescriptorBean var1) {
         try {
            super.computeDiff(var1);
            BridgeDestinationCommonMBeanImpl var2 = (BridgeDestinationCommonMBeanImpl)var1;
            this.computeDiff("AdapterJNDIName", this.bean.getAdapterJNDIName(), var2.getAdapterJNDIName(), false);
            this.computeDiff("Classpath", this.bean.getClasspath(), var2.getClasspath(), false);
            this.computeDiff("UserName", this.bean.getUserName(), var2.getUserName(), false);
            this.computeDiff("UserPasswordEncrypted", this.bean.getUserPasswordEncrypted(), var2.getUserPasswordEncrypted(), false);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            BridgeDestinationCommonMBeanImpl var3 = (BridgeDestinationCommonMBeanImpl)var1.getSourceBean();
            BridgeDestinationCommonMBeanImpl var4 = (BridgeDestinationCommonMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("AdapterJNDIName")) {
                  var3.setAdapterJNDIName(var4.getAdapterJNDIName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 7);
               } else if (var5.equals("Classpath")) {
                  var3.setClasspath(var4.getClasspath());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 11);
               } else if (var5.equals("UserName")) {
                  var3.setUserName(var4.getUserName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 8);
               } else if (!var5.equals("UserPassword")) {
                  if (var5.equals("UserPasswordEncrypted")) {
                     var3.setUserPasswordEncrypted(var4.getUserPasswordEncrypted());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 10);
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
            BridgeDestinationCommonMBeanImpl var5 = (BridgeDestinationCommonMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("AdapterJNDIName")) && this.bean.isAdapterJNDINameSet()) {
               var5.setAdapterJNDIName(this.bean.getAdapterJNDIName());
            }

            if ((var3 == null || !var3.contains("Classpath")) && this.bean.isClasspathSet()) {
               var5.setClasspath(this.bean.getClasspath());
            }

            if ((var3 == null || !var3.contains("UserName")) && this.bean.isUserNameSet()) {
               var5.setUserName(this.bean.getUserName());
            }

            if ((var3 == null || !var3.contains("UserPasswordEncrypted")) && this.bean.isUserPasswordEncryptedSet()) {
               byte[] var4 = this.bean.getUserPasswordEncrypted();
               var5.setUserPasswordEncrypted(var4 == null ? null : (byte[])((byte[])((byte[])((byte[])var4)).clone()));
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
