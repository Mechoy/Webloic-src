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

public class MigratableRMIServiceMBeanImpl extends DeploymentMBeanImpl implements MigratableRMIServiceMBean, Serializable {
   private String _Argument;
   private String _Classname;
   private static SchemaHelper2 _schemaHelper;

   public MigratableRMIServiceMBeanImpl() {
      this._initializeProperty(-1);
   }

   public MigratableRMIServiceMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public String getClassname() {
      return this._Classname;
   }

   public boolean isClassnameSet() {
      return this._isSet(9);
   }

   public void setClassname(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._Classname;
      this._Classname = var1;
      this._postSet(9, var2, var1);
   }

   public String getArgument() {
      return this._Argument;
   }

   public boolean isArgumentSet() {
      return this._isSet(10);
   }

   public void setArgument(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._Argument;
      this._Argument = var1;
      this._postSet(10, var2, var1);
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
         var1 = 10;
      }

      try {
         switch (var1) {
            case 10:
               this._Argument = null;
               if (var2) {
                  break;
               }
            case 9:
               this._Classname = null;
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
      return "MigratableRMIService";
   }

   public void putValue(String var1, Object var2) {
      String var3;
      if (var1.equals("Argument")) {
         var3 = this._Argument;
         this._Argument = (String)var2;
         this._postSet(10, var3, this._Argument);
      } else if (var1.equals("Classname")) {
         var3 = this._Classname;
         this._Classname = (String)var2;
         this._postSet(9, var3, this._Classname);
      } else {
         super.putValue(var1, var2);
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("Argument")) {
         return this._Argument;
      } else {
         return var1.equals("Classname") ? this._Classname : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends DeploymentMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 8:
               if (var1.equals("argument")) {
                  return 10;
               }
               break;
            case 9:
               if (var1.equals("classname")) {
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
            case 9:
               return "classname";
            case 10:
               return "argument";
            default:
               return super.getElementName(var1);
         }
      }

      public boolean isArray(int var1) {
         switch (var1) {
            case 7:
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

   protected static class Helper extends DeploymentMBeanImpl.Helper {
      private MigratableRMIServiceMBeanImpl bean;

      protected Helper(MigratableRMIServiceMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 9:
               return "Classname";
            case 10:
               return "Argument";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("Argument")) {
            return 10;
         } else {
            return var1.equals("Classname") ? 9 : super.getPropertyIndex(var1);
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
            if (this.bean.isArgumentSet()) {
               var2.append("Argument");
               var2.append(String.valueOf(this.bean.getArgument()));
            }

            if (this.bean.isClassnameSet()) {
               var2.append("Classname");
               var2.append(String.valueOf(this.bean.getClassname()));
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
            MigratableRMIServiceMBeanImpl var2 = (MigratableRMIServiceMBeanImpl)var1;
            this.computeDiff("Argument", this.bean.getArgument(), var2.getArgument(), false);
            this.computeDiff("Classname", this.bean.getClassname(), var2.getClassname(), false);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            MigratableRMIServiceMBeanImpl var3 = (MigratableRMIServiceMBeanImpl)var1.getSourceBean();
            MigratableRMIServiceMBeanImpl var4 = (MigratableRMIServiceMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("Argument")) {
                  var3.setArgument(var4.getArgument());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 10);
               } else if (var5.equals("Classname")) {
                  var3.setClassname(var4.getClassname());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 9);
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
            MigratableRMIServiceMBeanImpl var5 = (MigratableRMIServiceMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("Argument")) && this.bean.isArgumentSet()) {
               var5.setArgument(this.bean.getArgument());
            }

            if ((var3 == null || !var3.contains("Classname")) && this.bean.isClassnameSet()) {
               var5.setClassname(this.bean.getClassname());
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
