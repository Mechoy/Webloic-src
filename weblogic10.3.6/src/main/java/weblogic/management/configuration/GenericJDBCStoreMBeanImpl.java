package weblogic.management.configuration;

import java.io.Serializable;
import java.util.ArrayList;
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
import weblogic.utils.collections.CombinedIterator;

public class GenericJDBCStoreMBeanImpl extends ConfigurationMBeanImpl implements GenericJDBCStoreMBean, Serializable {
   private String _CreateTableDDLFile;
   private String _PrefixName;
   private static SchemaHelper2 _schemaHelper;

   public GenericJDBCStoreMBeanImpl() {
      this._initializeProperty(-1);
   }

   public GenericJDBCStoreMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public String getPrefixName() {
      return this._PrefixName;
   }

   public boolean isPrefixNameSet() {
      return this._isSet(7);
   }

   public void setPrefixName(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      JMSLegalHelper.validateJDBCPrefix(var1);
      String var2 = this._PrefixName;
      this._PrefixName = var1;
      this._postSet(7, var2, var1);
   }

   public String getCreateTableDDLFile() {
      return this._CreateTableDDLFile;
   }

   public boolean isCreateTableDDLFileSet() {
      return this._isSet(8);
   }

   public void setCreateTableDDLFile(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._CreateTableDDLFile;
      this._CreateTableDDLFile = var1;
      this._postSet(8, var2, var1);
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
         var1 = 8;
      }

      try {
         switch (var1) {
            case 8:
               this._CreateTableDDLFile = null;
               if (var2) {
                  break;
               }
            case 7:
               this._PrefixName = null;
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
      return "GenericJDBCStore";
   }

   public void putValue(String var1, Object var2) {
      String var3;
      if (var1.equals("CreateTableDDLFile")) {
         var3 = this._CreateTableDDLFile;
         this._CreateTableDDLFile = (String)var2;
         this._postSet(8, var3, this._CreateTableDDLFile);
      } else if (var1.equals("PrefixName")) {
         var3 = this._PrefixName;
         this._PrefixName = (String)var2;
         this._postSet(7, var3, this._PrefixName);
      } else {
         super.putValue(var1, var2);
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("CreateTableDDLFile")) {
         return this._CreateTableDDLFile;
      } else {
         return var1.equals("PrefixName") ? this._PrefixName : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends ConfigurationMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 11:
               if (var1.equals("prefix-name")) {
                  return 7;
               }
               break;
            case 20:
               if (var1.equals("create-tableddl-file")) {
                  return 8;
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
               return "prefix-name";
            case 8:
               return "create-tableddl-file";
            default:
               return super.getElementName(var1);
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
      private GenericJDBCStoreMBeanImpl bean;

      protected Helper(GenericJDBCStoreMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 7:
               return "PrefixName";
            case 8:
               return "CreateTableDDLFile";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("CreateTableDDLFile")) {
            return 8;
         } else {
            return var1.equals("PrefixName") ? 7 : super.getPropertyIndex(var1);
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
            if (this.bean.isCreateTableDDLFileSet()) {
               var2.append("CreateTableDDLFile");
               var2.append(String.valueOf(this.bean.getCreateTableDDLFile()));
            }

            if (this.bean.isPrefixNameSet()) {
               var2.append("PrefixName");
               var2.append(String.valueOf(this.bean.getPrefixName()));
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
            GenericJDBCStoreMBeanImpl var2 = (GenericJDBCStoreMBeanImpl)var1;
            this.computeDiff("CreateTableDDLFile", this.bean.getCreateTableDDLFile(), var2.getCreateTableDDLFile(), false);
            this.computeDiff("PrefixName", this.bean.getPrefixName(), var2.getPrefixName(), false);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            GenericJDBCStoreMBeanImpl var3 = (GenericJDBCStoreMBeanImpl)var1.getSourceBean();
            GenericJDBCStoreMBeanImpl var4 = (GenericJDBCStoreMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("CreateTableDDLFile")) {
                  var3.setCreateTableDDLFile(var4.getCreateTableDDLFile());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 8);
               } else if (var5.equals("PrefixName")) {
                  var3.setPrefixName(var4.getPrefixName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 7);
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
            GenericJDBCStoreMBeanImpl var5 = (GenericJDBCStoreMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("CreateTableDDLFile")) && this.bean.isCreateTableDDLFileSet()) {
               var5.setCreateTableDDLFile(this.bean.getCreateTableDDLFile());
            }

            if ((var3 == null || !var3.contains("PrefixName")) && this.bean.isPrefixNameSet()) {
               var5.setPrefixName(this.bean.getPrefixName());
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
