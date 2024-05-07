package weblogic.management.security;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.zip.CRC32;
import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBeanHelper;
import weblogic.descriptor.internal.AbstractSchemaHelper2;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.management.commo.AbstractCommoConfigurationBean;
import weblogic.management.utils.ErrorCollectionException;
import weblogic.management.utils.InvalidParameterException;
import weblogic.utils.ArrayUtils;
import weblogic.utils.collections.CombinedIterator;

public class ImportMBeanImpl extends AbstractCommoConfigurationBean implements ImportMBean, Serializable {
   private String[] _SupportedImportConstraints;
   private String[] _SupportedImportFormats;
   private static SchemaHelper2 _schemaHelper;

   public ImportMBeanImpl() {
      this._initializeProperty(-1);
   }

   public ImportMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public String[] getSupportedImportFormats() {
      return this._SupportedImportFormats;
   }

   public boolean isSupportedImportFormatsSet() {
      return this._isSet(2);
   }

   public void setSupportedImportFormats(String[] var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? new String[0] : var1;
      var1 = this._getHelper()._trimElements(var1);
      this._SupportedImportFormats = var1;
   }

   public String[] getSupportedImportConstraints() {
      return this._SupportedImportConstraints;
   }

   public boolean isSupportedImportConstraintsSet() {
      return this._isSet(3);
   }

   public void setSupportedImportConstraints(String[] var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? new String[0] : var1;
      var1 = this._getHelper()._trimElements(var1);
      this._SupportedImportConstraints = var1;
   }

   public void importData(String var1, String var2, Properties var3) throws InvalidParameterException, ErrorCollectionException {
      throw new AssertionError("Method not implemented");
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
               this._SupportedImportConstraints = new String[0];
               if (var2) {
                  break;
               }
            case 2:
               this._SupportedImportFormats = new String[0];
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
      return "weblogic.management.security.ImportMBean";
   }

   public static class SchemaHelper2 extends AbstractSchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 23:
               if (var1.equals("supported-import-format")) {
                  return 2;
               }
               break;
            case 27:
               if (var1.equals("supported-import-constraint")) {
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
            case 2:
               return "supported-import-format";
            case 3:
               return "supported-import-constraint";
            default:
               return super.getElementName(var1);
         }
      }

      public boolean isArray(int var1) {
         switch (var1) {
            case 2:
               return true;
            case 3:
               return true;
            default:
               return super.isArray(var1);
         }
      }
   }

   protected static class Helper extends AbstractCommoConfigurationBean.Helper {
      private ImportMBeanImpl bean;

      protected Helper(ImportMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 2:
               return "SupportedImportFormats";
            case 3:
               return "SupportedImportConstraints";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("SupportedImportConstraints")) {
            return 3;
         } else {
            return var1.equals("SupportedImportFormats") ? 2 : super.getPropertyIndex(var1);
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
            if (this.bean.isSupportedImportConstraintsSet()) {
               var2.append("SupportedImportConstraints");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getSupportedImportConstraints())));
            }

            if (this.bean.isSupportedImportFormatsSet()) {
               var2.append("SupportedImportFormats");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getSupportedImportFormats())));
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
            ImportMBeanImpl var2 = (ImportMBeanImpl)var1;
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            ImportMBeanImpl var3 = (ImportMBeanImpl)var1.getSourceBean();
            ImportMBeanImpl var4 = (ImportMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (!var5.equals("SupportedImportConstraints") && !var5.equals("SupportedImportFormats")) {
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
            ImportMBeanImpl var5 = (ImportMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
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
