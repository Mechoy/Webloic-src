package weblogic.diagnostics.image.descriptor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.CRC32;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBeanHelper;
import weblogic.descriptor.internal.AbstractSchemaHelper2;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.utils.collections.CombinedIterator;

public class WatchAlarmStateBeanImpl extends AbstractDescriptorBean implements WatchAlarmStateBean, Serializable {
   private String _AlarmResetPeriod;
   private String _AlarmResetType;
   private String _WatchName;
   private static SchemaHelper2 _schemaHelper;

   public WatchAlarmStateBeanImpl() {
      this._initializeProperty(-1);
   }

   public WatchAlarmStateBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public String getWatchName() {
      return this._WatchName;
   }

   public boolean isWatchNameSet() {
      return this._isSet(0);
   }

   public void setWatchName(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._WatchName;
      this._WatchName = var1;
      this._postSet(0, var2, var1);
   }

   public void setAlarmResetType(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._AlarmResetType;
      this._AlarmResetType = var1;
      this._postSet(1, var2, var1);
   }

   public String getAlarmResetType() {
      return this._AlarmResetType;
   }

   public boolean isAlarmResetTypeSet() {
      return this._isSet(1);
   }

   public void setAlarmResetPeriod(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._AlarmResetPeriod;
      this._AlarmResetPeriod = var1;
      this._postSet(2, var2, var1);
   }

   public String getAlarmResetPeriod() {
      return this._AlarmResetPeriod;
   }

   public boolean isAlarmResetPeriodSet() {
      return this._isSet(2);
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
         var1 = 2;
      }

      try {
         switch (var1) {
            case 2:
               this._AlarmResetPeriod = null;
               if (var2) {
                  break;
               }
            case 1:
               this._AlarmResetType = null;
               if (var2) {
                  break;
               }
            case 0:
               this._WatchName = null;
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
      return "http://xmlns.oracle.com/weblogic/weblogic-diagnostics-image/1.0/weblogic-diagnostics-image.xsd";
   }

   protected String getTargetNamespace() {
      return "http://xmlns.oracle.com/weblogic/weblogic-diagnostics-image";
   }

   public SchemaHelper _getSchemaHelper2() {
      if (_schemaHelper == null) {
         _schemaHelper = new SchemaHelper2();
      }

      return _schemaHelper;
   }

   public static class SchemaHelper2 extends AbstractSchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 10:
               if (var1.equals("watch-name")) {
                  return 0;
               }
               break;
            case 16:
               if (var1.equals("alarm-reset-type")) {
                  return 1;
               }
               break;
            case 18:
               if (var1.equals("alarm-reset-period")) {
                  return 2;
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
               return "watch-name";
            case 1:
               return "alarm-reset-type";
            case 2:
               return "alarm-reset-period";
            default:
               return super.getElementName(var1);
         }
      }
   }

   protected static class Helper extends AbstractDescriptorBeanHelper {
      private WatchAlarmStateBeanImpl bean;

      protected Helper(WatchAlarmStateBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 0:
               return "WatchName";
            case 1:
               return "AlarmResetType";
            case 2:
               return "AlarmResetPeriod";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("AlarmResetPeriod")) {
            return 2;
         } else if (var1.equals("AlarmResetType")) {
            return 1;
         } else {
            return var1.equals("WatchName") ? 0 : super.getPropertyIndex(var1);
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
            if (this.bean.isAlarmResetPeriodSet()) {
               var2.append("AlarmResetPeriod");
               var2.append(String.valueOf(this.bean.getAlarmResetPeriod()));
            }

            if (this.bean.isAlarmResetTypeSet()) {
               var2.append("AlarmResetType");
               var2.append(String.valueOf(this.bean.getAlarmResetType()));
            }

            if (this.bean.isWatchNameSet()) {
               var2.append("WatchName");
               var2.append(String.valueOf(this.bean.getWatchName()));
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
            WatchAlarmStateBeanImpl var2 = (WatchAlarmStateBeanImpl)var1;
            this.computeDiff("AlarmResetPeriod", this.bean.getAlarmResetPeriod(), var2.getAlarmResetPeriod(), false);
            this.computeDiff("AlarmResetType", this.bean.getAlarmResetType(), var2.getAlarmResetType(), false);
            this.computeDiff("WatchName", this.bean.getWatchName(), var2.getWatchName(), false);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            WatchAlarmStateBeanImpl var3 = (WatchAlarmStateBeanImpl)var1.getSourceBean();
            WatchAlarmStateBeanImpl var4 = (WatchAlarmStateBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("AlarmResetPeriod")) {
                  var3.setAlarmResetPeriod(var4.getAlarmResetPeriod());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 2);
               } else if (var5.equals("AlarmResetType")) {
                  var3.setAlarmResetType(var4.getAlarmResetType());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 1);
               } else if (var5.equals("WatchName")) {
                  var3.setWatchName(var4.getWatchName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 0);
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
            WatchAlarmStateBeanImpl var5 = (WatchAlarmStateBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("AlarmResetPeriod")) && this.bean.isAlarmResetPeriodSet()) {
               var5.setAlarmResetPeriod(this.bean.getAlarmResetPeriod());
            }

            if ((var3 == null || !var3.contains("AlarmResetType")) && this.bean.isAlarmResetTypeSet()) {
               var5.setAlarmResetType(this.bean.getAlarmResetType());
            }

            if ((var3 == null || !var3.contains("WatchName")) && this.bean.isWatchNameSet()) {
               var5.setWatchName(this.bean.getWatchName());
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
