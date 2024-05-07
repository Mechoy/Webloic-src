package weblogic.diagnostics.descriptor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.zip.CRC32;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.DescriptorValidateException;
import weblogic.descriptor.beangen.LegalChecks;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBeanHelper;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.diagnostics.harvester.Validators;
import weblogic.utils.ArrayUtils;
import weblogic.utils.collections.CombinedIterator;

public class WLDFHarvestedTypeBeanImpl extends WLDFBeanImpl implements WLDFHarvestedTypeBean, Serializable {
   private boolean _Enabled;
   private String[] _HarvestedAttributes;
   private String[] _HarvestedInstances;
   private boolean _KnownType;
   private String _Name;
   private String _Namespace;
   private static SchemaHelper2 _schemaHelper;

   public WLDFHarvestedTypeBeanImpl() {
      this._initializeProperty(-1);
   }

   public WLDFHarvestedTypeBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public String getName() {
      return this._Name;
   }

   public boolean isNameSet() {
      return this._isSet(0);
   }

   public void setName(String var1) {
      var1 = var1 == null ? null : var1.trim();
      LegalChecks.checkNonNull("Name", var1);
      Validators.validateConfiguredType(var1);
      String var2 = this._Name;
      this._Name = var1;
      this._postSet(0, var2, var1);
   }

   public boolean isEnabled() {
      return this._Enabled;
   }

   public boolean isEnabledSet() {
      return this._isSet(1);
   }

   public void setEnabled(boolean var1) {
      boolean var2 = this._Enabled;
      this._Enabled = var1;
      this._postSet(1, var2, var1);
   }

   public boolean isKnownType() {
      return this._KnownType;
   }

   public boolean isKnownTypeSet() {
      return this._isSet(2);
   }

   public void setKnownType(boolean var1) {
      boolean var2 = this._KnownType;
      this._KnownType = var1;
      this._postSet(2, var2, var1);
   }

   public String[] getHarvestedAttributes() {
      return this._HarvestedAttributes;
   }

   public boolean isHarvestedAttributesSet() {
      return this._isSet(3);
   }

   public void setHarvestedAttributes(String[] var1) {
      var1 = var1 == null ? new String[0] : var1;
      var1 = this._getHelper()._trimElements(var1);
      String[] var2 = this._HarvestedAttributes;
      this._HarvestedAttributes = var1;
      this._postSet(3, var2, var1);
   }

   public String[] getHarvestedInstances() {
      return this._HarvestedInstances;
   }

   public boolean isHarvestedInstancesSet() {
      return this._isSet(4);
   }

   public void setHarvestedInstances(String[] var1) {
      var1 = var1 == null ? new String[0] : var1;
      var1 = this._getHelper()._trimElements(var1);
      Validators.validateConfiguredInstances(var1);
      String[] var2 = this._HarvestedInstances;
      this._HarvestedInstances = var1;
      this._postSet(4, var2, var1);
   }

   public String getNamespace() {
      return this._Namespace;
   }

   public boolean isNamespaceSet() {
      return this._isSet(5);
   }

   public void setNamespace(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"ServerRuntime", "DomainRuntime"};
      var1 = LegalChecks.checkInEnum("Namespace", var1, var2);
      LegalChecks.checkNonEmptyString("Namespace", var1);
      LegalChecks.checkNonNull("Namespace", var1);
      String var3 = this._Namespace;
      this._Namespace = var1;
      this._postSet(5, var3, var1);
   }

   public Object _getKey() {
      return super._getKey();
   }

   public void _validate() throws IllegalArgumentException {
      super._validate();
      Validators.validateHarvestedTypeBean(this);
      Validators.validateConfiguredAttributes(this);
      LegalChecks.checkIsSet("Name", this.isNameSet());
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
         var1 = 3;
      }

      try {
         switch (var1) {
            case 3:
               this._HarvestedAttributes = new String[0];
               if (var2) {
                  break;
               }
            case 4:
               this._HarvestedInstances = new String[0];
               if (var2) {
                  break;
               }
            case 0:
               this._Name = null;
               if (var2) {
                  break;
               }
            case 5:
               this._Namespace = "ServerRuntime";
               if (var2) {
                  break;
               }
            case 1:
               this._Enabled = true;
               if (var2) {
                  break;
               }
            case 2:
               this._KnownType = false;
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
      return "http://xmlns.oracle.com/weblogic/weblogic-diagnostics/1.0/weblogic-diagnostics.xsd";
   }

   protected String getTargetNamespace() {
      return "http://xmlns.oracle.com/weblogic/weblogic-diagnostics";
   }

   public SchemaHelper _getSchemaHelper2() {
      if (_schemaHelper == null) {
         _schemaHelper = new SchemaHelper2();
      }

      return _schemaHelper;
   }

   public static void validateGeneration() {
      try {
         String[] var0 = new String[]{"ServerRuntime", "DomainRuntime"};
         LegalChecks.checkInEnum("Namespace", "ServerRuntime", var0);
      } catch (IllegalArgumentException var3) {
         throw new DescriptorValidateException("Default value for a property  should be one of the legal values. Refer annotation legalValues on property Namespace in WLDFHarvestedTypeBean" + var3.getMessage());
      }

      try {
         LegalChecks.checkNonEmptyString("Namespace", "ServerRuntime");
      } catch (IllegalArgumentException var2) {
         throw new DescriptorValidateException("The default value for the property  is zero-length. Properties annotated with false value on @legalZeroLength or @legalNull  should either have @required/@derivedDefault annotations or have a non-zero-length value on @default annotation. Refer annotation legalZeroLength on property Namespace in WLDFHarvestedTypeBean" + var2.getMessage());
      }

      try {
         LegalChecks.checkNonNull("Namespace", "ServerRuntime");
      } catch (IllegalArgumentException var1) {
         throw new DescriptorValidateException("The default value for the property  is null. Properties annotated with false value on @legalZeroLength or @legalNull  should either have @required/@derivedDefault annotations or have a non-null value on @default annotation. Refer annotation legalNull on property Namespace in WLDFHarvestedTypeBean" + var1.getMessage());
      }
   }

   public static class SchemaHelper2 extends WLDFBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 4:
               if (var1.equals("name")) {
                  return 0;
               }
            case 5:
            case 6:
            case 8:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            default:
               break;
            case 7:
               if (var1.equals("enabled")) {
                  return 1;
               }
               break;
            case 9:
               if (var1.equals("namespace")) {
                  return 5;
               }
               break;
            case 10:
               if (var1.equals("known-type")) {
                  return 2;
               }
               break;
            case 18:
               if (var1.equals("harvested-instance")) {
                  return 4;
               }
               break;
            case 19:
               if (var1.equals("harvested-attribute")) {
                  return 3;
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
            case 0:
               return "name";
            case 1:
               return "enabled";
            case 2:
               return "known-type";
            case 3:
               return "harvested-attribute";
            case 4:
               return "harvested-instance";
            case 5:
               return "namespace";
            default:
               return super.getElementName(var1);
         }
      }

      public boolean isArray(int var1) {
         switch (var1) {
            case 3:
               return true;
            case 4:
               return true;
            default:
               return super.isArray(var1);
         }
      }
   }

   protected static class Helper extends WLDFBeanImpl.Helper {
      private WLDFHarvestedTypeBeanImpl bean;

      protected Helper(WLDFHarvestedTypeBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 0:
               return "Name";
            case 1:
               return "Enabled";
            case 2:
               return "KnownType";
            case 3:
               return "HarvestedAttributes";
            case 4:
               return "HarvestedInstances";
            case 5:
               return "Namespace";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("HarvestedAttributes")) {
            return 3;
         } else if (var1.equals("HarvestedInstances")) {
            return 4;
         } else if (var1.equals("Name")) {
            return 0;
         } else if (var1.equals("Namespace")) {
            return 5;
         } else if (var1.equals("Enabled")) {
            return 1;
         } else {
            return var1.equals("KnownType") ? 2 : super.getPropertyIndex(var1);
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
            if (this.bean.isHarvestedAttributesSet()) {
               var2.append("HarvestedAttributes");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getHarvestedAttributes())));
            }

            if (this.bean.isHarvestedInstancesSet()) {
               var2.append("HarvestedInstances");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getHarvestedInstances())));
            }

            if (this.bean.isNameSet()) {
               var2.append("Name");
               var2.append(String.valueOf(this.bean.getName()));
            }

            if (this.bean.isNamespaceSet()) {
               var2.append("Namespace");
               var2.append(String.valueOf(this.bean.getNamespace()));
            }

            if (this.bean.isEnabledSet()) {
               var2.append("Enabled");
               var2.append(String.valueOf(this.bean.isEnabled()));
            }

            if (this.bean.isKnownTypeSet()) {
               var2.append("KnownType");
               var2.append(String.valueOf(this.bean.isKnownType()));
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
            WLDFHarvestedTypeBeanImpl var2 = (WLDFHarvestedTypeBeanImpl)var1;
            this.computeDiff("HarvestedAttributes", this.bean.getHarvestedAttributes(), var2.getHarvestedAttributes(), true);
            this.computeDiff("HarvestedInstances", this.bean.getHarvestedInstances(), var2.getHarvestedInstances(), true);
            this.computeDiff("Name", this.bean.getName(), var2.getName(), false);
            this.computeDiff("Namespace", this.bean.getNamespace(), var2.getNamespace(), true);
            this.computeDiff("Enabled", this.bean.isEnabled(), var2.isEnabled(), true);
            this.computeDiff("KnownType", this.bean.isKnownType(), var2.isKnownType(), true);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            WLDFHarvestedTypeBeanImpl var3 = (WLDFHarvestedTypeBeanImpl)var1.getSourceBean();
            WLDFHarvestedTypeBeanImpl var4 = (WLDFHarvestedTypeBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("HarvestedAttributes")) {
                  var3.setHarvestedAttributes(var4.getHarvestedAttributes());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 3);
               } else if (var5.equals("HarvestedInstances")) {
                  var3.setHarvestedInstances(var4.getHarvestedInstances());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 4);
               } else if (var5.equals("Name")) {
                  var3.setName(var4.getName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 0);
               } else if (var5.equals("Namespace")) {
                  var3.setNamespace(var4.getNamespace());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 5);
               } else if (var5.equals("Enabled")) {
                  var3.setEnabled(var4.isEnabled());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 1);
               } else if (var5.equals("KnownType")) {
                  var3.setKnownType(var4.isKnownType());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 2);
               } else {
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
            WLDFHarvestedTypeBeanImpl var5 = (WLDFHarvestedTypeBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            String[] var4;
            if ((var3 == null || !var3.contains("HarvestedAttributes")) && this.bean.isHarvestedAttributesSet()) {
               var4 = this.bean.getHarvestedAttributes();
               var5.setHarvestedAttributes(var4 == null ? null : (String[])((String[])((String[])((String[])var4)).clone()));
            }

            if ((var3 == null || !var3.contains("HarvestedInstances")) && this.bean.isHarvestedInstancesSet()) {
               var4 = this.bean.getHarvestedInstances();
               var5.setHarvestedInstances(var4 == null ? null : (String[])((String[])((String[])((String[])var4)).clone()));
            }

            if ((var3 == null || !var3.contains("Name")) && this.bean.isNameSet()) {
               var5.setName(this.bean.getName());
            }

            if ((var3 == null || !var3.contains("Namespace")) && this.bean.isNamespaceSet()) {
               var5.setNamespace(this.bean.getNamespace());
            }

            if ((var3 == null || !var3.contains("Enabled")) && this.bean.isEnabledSet()) {
               var5.setEnabled(this.bean.isEnabled());
            }

            if ((var3 == null || !var3.contains("KnownType")) && this.bean.isKnownTypeSet()) {
               var5.setKnownType(this.bean.isKnownType());
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
