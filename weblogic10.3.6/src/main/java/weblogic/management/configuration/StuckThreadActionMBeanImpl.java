package weblogic.management.configuration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.CRC32;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBeanHelper;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.utils.collections.CombinedIterator;

public class StuckThreadActionMBeanImpl extends ConfigurationMBeanImpl implements StuckThreadActionMBean, Serializable {
   private String _ActionCode;
   private String _ApplicationName;
   private int _MaxStuckThreadsCount;
   private String _ModuleName;
   private String _WorkManagerName;
   private static SchemaHelper2 _schemaHelper;

   public StuckThreadActionMBeanImpl() {
      this._initializeProperty(-1);
   }

   public StuckThreadActionMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public void setWorkManagerName(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._WorkManagerName;
      this._WorkManagerName = var1;
      this._postSet(7, var2, var1);
   }

   public String getWorkManagerName() {
      return this._WorkManagerName;
   }

   public boolean isWorkManagerNameSet() {
      return this._isSet(7);
   }

   public void setModuleName(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._ModuleName;
      this._ModuleName = var1;
      this._postSet(8, var2, var1);
   }

   public String getModuleName() {
      return this._ModuleName;
   }

   public boolean isModuleNameSet() {
      return this._isSet(8);
   }

   public void setApplicationName(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._ApplicationName;
      this._ApplicationName = var1;
      this._postSet(9, var2, var1);
   }

   public String getApplicationName() {
      return this._ApplicationName;
   }

   public boolean isApplicationNameSet() {
      return this._isSet(9);
   }

   public void setMaxStuckThreadsCount(int var1) {
      int var2 = this._MaxStuckThreadsCount;
      this._MaxStuckThreadsCount = var1;
      this._postSet(10, var2, var1);
   }

   public int getMaxStuckThreadsCount() {
      return this._MaxStuckThreadsCount;
   }

   public boolean isMaxStuckThreadsCountSet() {
      return this._isSet(10);
   }

   public void setActionCode(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._ActionCode;
      this._ActionCode = var1;
      this._postSet(11, var2, var1);
   }

   public String getActionCode() {
      return this._ActionCode;
   }

   public boolean isActionCodeSet() {
      return this._isSet(11);
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
         var1 = 11;
      }

      try {
         switch (var1) {
            case 11:
               this._ActionCode = null;
               if (var2) {
                  break;
               }
            case 9:
               this._ApplicationName = null;
               if (var2) {
                  break;
               }
            case 10:
               this._MaxStuckThreadsCount = 0;
               if (var2) {
                  break;
               }
            case 8:
               this._ModuleName = null;
               if (var2) {
                  break;
               }
            case 7:
               this._WorkManagerName = null;
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
      return "StuckThreadAction";
   }

   public void putValue(String var1, Object var2) {
      String var3;
      if (var1.equals("ActionCode")) {
         var3 = this._ActionCode;
         this._ActionCode = (String)var2;
         this._postSet(11, var3, this._ActionCode);
      } else if (var1.equals("ApplicationName")) {
         var3 = this._ApplicationName;
         this._ApplicationName = (String)var2;
         this._postSet(9, var3, this._ApplicationName);
      } else if (var1.equals("MaxStuckThreadsCount")) {
         int var4 = this._MaxStuckThreadsCount;
         this._MaxStuckThreadsCount = (Integer)var2;
         this._postSet(10, var4, this._MaxStuckThreadsCount);
      } else if (var1.equals("ModuleName")) {
         var3 = this._ModuleName;
         this._ModuleName = (String)var2;
         this._postSet(8, var3, this._ModuleName);
      } else if (var1.equals("WorkManagerName")) {
         var3 = this._WorkManagerName;
         this._WorkManagerName = (String)var2;
         this._postSet(7, var3, this._WorkManagerName);
      } else {
         super.putValue(var1, var2);
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("ActionCode")) {
         return this._ActionCode;
      } else if (var1.equals("ApplicationName")) {
         return this._ApplicationName;
      } else if (var1.equals("MaxStuckThreadsCount")) {
         return new Integer(this._MaxStuckThreadsCount);
      } else if (var1.equals("ModuleName")) {
         return this._ModuleName;
      } else {
         return var1.equals("WorkManagerName") ? this._WorkManagerName : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends ConfigurationMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 11:
               if (var1.equals("action-code")) {
                  return 11;
               }

               if (var1.equals("module-name")) {
                  return 8;
               }
               break;
            case 16:
               if (var1.equals("application-name")) {
                  return 9;
               }
               break;
            case 17:
               if (var1.equals("work-manager-name")) {
                  return 7;
               }
               break;
            case 23:
               if (var1.equals("max-stuck-threads-count")) {
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
               return "work-manager-name";
            case 8:
               return "module-name";
            case 9:
               return "application-name";
            case 10:
               return "max-stuck-threads-count";
            case 11:
               return "action-code";
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
      private StuckThreadActionMBeanImpl bean;

      protected Helper(StuckThreadActionMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 7:
               return "WorkManagerName";
            case 8:
               return "ModuleName";
            case 9:
               return "ApplicationName";
            case 10:
               return "MaxStuckThreadsCount";
            case 11:
               return "ActionCode";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("ActionCode")) {
            return 11;
         } else if (var1.equals("ApplicationName")) {
            return 9;
         } else if (var1.equals("MaxStuckThreadsCount")) {
            return 10;
         } else if (var1.equals("ModuleName")) {
            return 8;
         } else {
            return var1.equals("WorkManagerName") ? 7 : super.getPropertyIndex(var1);
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
            if (this.bean.isActionCodeSet()) {
               var2.append("ActionCode");
               var2.append(String.valueOf(this.bean.getActionCode()));
            }

            if (this.bean.isApplicationNameSet()) {
               var2.append("ApplicationName");
               var2.append(String.valueOf(this.bean.getApplicationName()));
            }

            if (this.bean.isMaxStuckThreadsCountSet()) {
               var2.append("MaxStuckThreadsCount");
               var2.append(String.valueOf(this.bean.getMaxStuckThreadsCount()));
            }

            if (this.bean.isModuleNameSet()) {
               var2.append("ModuleName");
               var2.append(String.valueOf(this.bean.getModuleName()));
            }

            if (this.bean.isWorkManagerNameSet()) {
               var2.append("WorkManagerName");
               var2.append(String.valueOf(this.bean.getWorkManagerName()));
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
            StuckThreadActionMBeanImpl var2 = (StuckThreadActionMBeanImpl)var1;
            this.computeDiff("ActionCode", this.bean.getActionCode(), var2.getActionCode(), false);
            this.computeDiff("ApplicationName", this.bean.getApplicationName(), var2.getApplicationName(), false);
            this.computeDiff("MaxStuckThreadsCount", this.bean.getMaxStuckThreadsCount(), var2.getMaxStuckThreadsCount(), false);
            this.computeDiff("ModuleName", this.bean.getModuleName(), var2.getModuleName(), false);
            this.computeDiff("WorkManagerName", this.bean.getWorkManagerName(), var2.getWorkManagerName(), false);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            StuckThreadActionMBeanImpl var3 = (StuckThreadActionMBeanImpl)var1.getSourceBean();
            StuckThreadActionMBeanImpl var4 = (StuckThreadActionMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("ActionCode")) {
                  var3.setActionCode(var4.getActionCode());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 11);
               } else if (var5.equals("ApplicationName")) {
                  var3.setApplicationName(var4.getApplicationName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 9);
               } else if (var5.equals("MaxStuckThreadsCount")) {
                  var3.setMaxStuckThreadsCount(var4.getMaxStuckThreadsCount());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 10);
               } else if (var5.equals("ModuleName")) {
                  var3.setModuleName(var4.getModuleName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 8);
               } else if (var5.equals("WorkManagerName")) {
                  var3.setWorkManagerName(var4.getWorkManagerName());
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
            StuckThreadActionMBeanImpl var5 = (StuckThreadActionMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("ActionCode")) && this.bean.isActionCodeSet()) {
               var5.setActionCode(this.bean.getActionCode());
            }

            if ((var3 == null || !var3.contains("ApplicationName")) && this.bean.isApplicationNameSet()) {
               var5.setApplicationName(this.bean.getApplicationName());
            }

            if ((var3 == null || !var3.contains("MaxStuckThreadsCount")) && this.bean.isMaxStuckThreadsCountSet()) {
               var5.setMaxStuckThreadsCount(this.bean.getMaxStuckThreadsCount());
            }

            if ((var3 == null || !var3.contains("ModuleName")) && this.bean.isModuleNameSet()) {
               var5.setModuleName(this.bean.getModuleName());
            }

            if ((var3 == null || !var3.contains("WorkManagerName")) && this.bean.isWorkManagerNameSet()) {
               var5.setWorkManagerName(this.bean.getWorkManagerName());
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
