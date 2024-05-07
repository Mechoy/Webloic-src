package weblogic.management.security.authentication;

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
import weblogic.management.commo.RequiredModelMBeanWrapper;
import weblogic.management.security.RealmMBean;
import weblogic.utils.ArrayUtils;
import weblogic.utils.collections.CombinedIterator;

public class IdentityAsserterMBeanImpl extends AuthenticationProviderMBeanImpl implements IdentityAsserterMBean, Serializable {
   private String[] _ActiveTypes;
   private boolean _Base64DecodingRequired;
   private String _CompatibilityObjectName;
   private RealmMBean _Realm;
   private String[] _SupportedTypes;
   private IdentityAsserterImpl _customizer;
   private static SchemaHelper2 _schemaHelper;

   public IdentityAsserterMBeanImpl() {
      try {
         this._customizer = new IdentityAsserterImpl(new RequiredModelMBeanWrapper(this));
      } catch (Exception var2) {
         if (var2 instanceof RuntimeException) {
            throw (RuntimeException)var2;
         }

         throw new UndeclaredThrowableException(var2);
      }

      this._initializeProperty(-1);
   }

   public IdentityAsserterMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);

      try {
         this._customizer = new IdentityAsserterImpl(new RequiredModelMBeanWrapper(this));
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         }

         throw new UndeclaredThrowableException(var4);
      }

      this._initializeProperty(-1);
   }

   public String[] getSupportedTypes() {
      return this._SupportedTypes;
   }

   public boolean isSupportedTypesSet() {
      return this._isSet(8);
   }

   public void setSupportedTypes(String[] var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? new String[0] : var1;
      var1 = this._getHelper()._trimElements(var1);
      this._SupportedTypes = var1;
   }

   public String[] getActiveTypes() {
      return this._ActiveTypes;
   }

   public boolean isActiveTypesSet() {
      return this._isSet(9);
   }

   public void setActiveTypes(String[] var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? new String[0] : var1;
      var1 = this._getHelper()._trimElements(var1);
      String[] var2 = this._ActiveTypes;
      this._ActiveTypes = var1;
      this._postSet(9, var2, var1);
   }

   public boolean getBase64DecodingRequired() {
      return this._Base64DecodingRequired;
   }

   public RealmMBean getRealm() {
      return this._customizer.getRealm();
   }

   public boolean isBase64DecodingRequiredSet() {
      return this._isSet(10);
   }

   public boolean isRealmSet() {
      return this._isSet(5);
   }

   public void setRealm(RealmMBean var1) throws InvalidAttributeValueException {
      this._Realm = var1;
   }

   public void setBase64DecodingRequired(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this._Base64DecodingRequired;
      this._Base64DecodingRequired = var1;
      this._postSet(10, var2, var1);
   }

   public String getCompatibilityObjectName() {
      return this._customizer.getCompatibilityObjectName();
   }

   public boolean isCompatibilityObjectNameSet() {
      return this._isSet(7);
   }

   public void setCompatibilityObjectName(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._CompatibilityObjectName;
      this._CompatibilityObjectName = var1;
      this._postSet(7, var2, var1);
   }

   public Object _getKey() {
      return super._getKey();
   }

   public void _validate() throws IllegalArgumentException {
      super._validate();

      try {
         if (!this._customizer.validateActiveTypes(this.getActiveTypes())) {
            throw new IllegalArgumentException("The IdentityAsserter ActiveTypes attribute was set to an illegal value.");
         }
      } catch (InvalidAttributeValueException var2) {
         throw new IllegalArgumentException(var2.toString());
      }
   }

   protected void _unSet(int var1) {
      if (!this._initializeProperty(var1)) {
         super._unSet(var1);
      } else {
         this._markSet(var1, false);
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
               this._ActiveTypes = new String[0];
               if (var2) {
                  break;
               }
            case 10:
               this._Base64DecodingRequired = true;
               if (var2) {
                  break;
               }
            case 7:
               this._CompatibilityObjectName = null;
               if (var2) {
                  break;
               }
            case 5:
               this._Realm = null;
               if (var2) {
                  break;
               }
            case 8:
               this._SupportedTypes = new String[0];
               if (var2) {
                  break;
               }
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
      return "weblogic.management.security.authentication.IdentityAsserterMBean";
   }

   public static class SchemaHelper2 extends AuthenticationProviderMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 5:
               if (var1.equals("realm")) {
                  return 5;
               }
               break;
            case 11:
               if (var1.equals("active-type")) {
                  return 9;
               }
               break;
            case 14:
               if (var1.equals("supported-type")) {
                  return 8;
               }
               break;
            case 24:
               if (var1.equals("base64-decoding-required")) {
                  return 10;
               }
               break;
            case 25:
               if (var1.equals("compatibility-object-name")) {
                  return 7;
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
            case 5:
               return "realm";
            case 6:
            default:
               return super.getElementName(var1);
            case 7:
               return "compatibility-object-name";
            case 8:
               return "supported-type";
            case 9:
               return "active-type";
            case 10:
               return "base64-decoding-required";
         }
      }

      public boolean isArray(int var1) {
         switch (var1) {
            case 8:
               return true;
            case 9:
               return true;
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

   protected static class Helper extends AuthenticationProviderMBeanImpl.Helper {
      private IdentityAsserterMBeanImpl bean;

      protected Helper(IdentityAsserterMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 5:
               return "Realm";
            case 6:
            default:
               return super.getPropertyName(var1);
            case 7:
               return "CompatibilityObjectName";
            case 8:
               return "SupportedTypes";
            case 9:
               return "ActiveTypes";
            case 10:
               return "Base64DecodingRequired";
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("ActiveTypes")) {
            return 9;
         } else if (var1.equals("Base64DecodingRequired")) {
            return 10;
         } else if (var1.equals("CompatibilityObjectName")) {
            return 7;
         } else if (var1.equals("Realm")) {
            return 5;
         } else {
            return var1.equals("SupportedTypes") ? 8 : super.getPropertyIndex(var1);
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
            if (this.bean.isActiveTypesSet()) {
               var2.append("ActiveTypes");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getActiveTypes())));
            }

            if (this.bean.isBase64DecodingRequiredSet()) {
               var2.append("Base64DecodingRequired");
               var2.append(String.valueOf(this.bean.getBase64DecodingRequired()));
            }

            if (this.bean.isCompatibilityObjectNameSet()) {
               var2.append("CompatibilityObjectName");
               var2.append(String.valueOf(this.bean.getCompatibilityObjectName()));
            }

            if (this.bean.isRealmSet()) {
               var2.append("Realm");
               var2.append(String.valueOf(this.bean.getRealm()));
            }

            if (this.bean.isSupportedTypesSet()) {
               var2.append("SupportedTypes");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getSupportedTypes())));
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
            IdentityAsserterMBeanImpl var2 = (IdentityAsserterMBeanImpl)var1;
            this.computeDiff("ActiveTypes", this.bean.getActiveTypes(), var2.getActiveTypes(), false);
            this.computeDiff("Base64DecodingRequired", this.bean.getBase64DecodingRequired(), var2.getBase64DecodingRequired(), false);
            this.computeDiff("CompatibilityObjectName", this.bean.getCompatibilityObjectName(), var2.getCompatibilityObjectName(), false);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            IdentityAsserterMBeanImpl var3 = (IdentityAsserterMBeanImpl)var1.getSourceBean();
            IdentityAsserterMBeanImpl var4 = (IdentityAsserterMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("ActiveTypes")) {
                  var3.setActiveTypes(var4.getActiveTypes());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 9);
               } else if (var5.equals("Base64DecodingRequired")) {
                  var3.setBase64DecodingRequired(var4.getBase64DecodingRequired());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 10);
               } else if (var5.equals("CompatibilityObjectName")) {
                  var3._conditionalUnset(var2.isUnsetUpdate(), 7);
               } else if (!var5.equals("Realm") && !var5.equals("SupportedTypes")) {
                  super.applyPropertyUpdate(var1, var2);
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
            IdentityAsserterMBeanImpl var5 = (IdentityAsserterMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("ActiveTypes")) && this.bean.isActiveTypesSet()) {
               String[] var4 = this.bean.getActiveTypes();
               var5.setActiveTypes(var4 == null ? null : (String[])((String[])((String[])((String[])var4)).clone()));
            }

            if ((var3 == null || !var3.contains("Base64DecodingRequired")) && this.bean.isBase64DecodingRequiredSet()) {
               var5.setBase64DecodingRequired(this.bean.getBase64DecodingRequired());
            }

            if ((var3 == null || !var3.contains("CompatibilityObjectName")) && this.bean.isCompatibilityObjectNameSet()) {
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
