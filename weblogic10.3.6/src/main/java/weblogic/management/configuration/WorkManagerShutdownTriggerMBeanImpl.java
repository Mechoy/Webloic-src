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

public class WorkManagerShutdownTriggerMBeanImpl extends ConfigurationMBeanImpl implements WorkManagerShutdownTriggerMBean, Serializable {
   private int _MaxStuckThreadTime;
   private int _StuckThreadCount;
   private static SchemaHelper2 _schemaHelper;

   public WorkManagerShutdownTriggerMBeanImpl() {
      this._initializeProperty(-1);
   }

   public WorkManagerShutdownTriggerMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public int getMaxStuckThreadTime() {
      return this._MaxStuckThreadTime;
   }

   public boolean isMaxStuckThreadTimeSet() {
      return this._isSet(7);
   }

   public void setMaxStuckThreadTime(int var1) {
      int var2 = this._MaxStuckThreadTime;
      this._MaxStuckThreadTime = var1;
      this._postSet(7, var2, var1);
   }

   public int getStuckThreadCount() {
      return this._StuckThreadCount;
   }

   public boolean isStuckThreadCountSet() {
      return this._isSet(8);
   }

   public void setStuckThreadCount(int var1) {
      int var2 = this._StuckThreadCount;
      this._StuckThreadCount = var1;
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
         var1 = 7;
      }

      try {
         switch (var1) {
            case 7:
               this._MaxStuckThreadTime = 0;
               if (var2) {
                  break;
               }
            case 8:
               this._StuckThreadCount = 0;
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
      return "WorkManagerShutdownTrigger";
   }

   public void putValue(String var1, Object var2) {
      int var3;
      if (var1.equals("MaxStuckThreadTime")) {
         var3 = this._MaxStuckThreadTime;
         this._MaxStuckThreadTime = (Integer)var2;
         this._postSet(7, var3, this._MaxStuckThreadTime);
      } else if (var1.equals("StuckThreadCount")) {
         var3 = this._StuckThreadCount;
         this._StuckThreadCount = (Integer)var2;
         this._postSet(8, var3, this._StuckThreadCount);
      } else {
         super.putValue(var1, var2);
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("MaxStuckThreadTime")) {
         return new Integer(this._MaxStuckThreadTime);
      } else {
         return var1.equals("StuckThreadCount") ? new Integer(this._StuckThreadCount) : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends ConfigurationMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 18:
               if (var1.equals("stuck-thread-count")) {
                  return 8;
               }
               break;
            case 21:
               if (var1.equals("max-stuck-thread-time")) {
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
            case 7:
               return "max-stuck-thread-time";
            case 8:
               return "stuck-thread-count";
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
      private WorkManagerShutdownTriggerMBeanImpl bean;

      protected Helper(WorkManagerShutdownTriggerMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 7:
               return "MaxStuckThreadTime";
            case 8:
               return "StuckThreadCount";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("MaxStuckThreadTime")) {
            return 7;
         } else {
            return var1.equals("StuckThreadCount") ? 8 : super.getPropertyIndex(var1);
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
            if (this.bean.isMaxStuckThreadTimeSet()) {
               var2.append("MaxStuckThreadTime");
               var2.append(String.valueOf(this.bean.getMaxStuckThreadTime()));
            }

            if (this.bean.isStuckThreadCountSet()) {
               var2.append("StuckThreadCount");
               var2.append(String.valueOf(this.bean.getStuckThreadCount()));
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
            WorkManagerShutdownTriggerMBeanImpl var2 = (WorkManagerShutdownTriggerMBeanImpl)var1;
            this.computeDiff("MaxStuckThreadTime", this.bean.getMaxStuckThreadTime(), var2.getMaxStuckThreadTime(), false);
            this.computeDiff("StuckThreadCount", this.bean.getStuckThreadCount(), var2.getStuckThreadCount(), false);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            WorkManagerShutdownTriggerMBeanImpl var3 = (WorkManagerShutdownTriggerMBeanImpl)var1.getSourceBean();
            WorkManagerShutdownTriggerMBeanImpl var4 = (WorkManagerShutdownTriggerMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("MaxStuckThreadTime")) {
                  var3.setMaxStuckThreadTime(var4.getMaxStuckThreadTime());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 7);
               } else if (var5.equals("StuckThreadCount")) {
                  var3.setStuckThreadCount(var4.getStuckThreadCount());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 8);
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
            WorkManagerShutdownTriggerMBeanImpl var5 = (WorkManagerShutdownTriggerMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("MaxStuckThreadTime")) && this.bean.isMaxStuckThreadTimeSet()) {
               var5.setMaxStuckThreadTime(this.bean.getMaxStuckThreadTime());
            }

            if ((var3 == null || !var3.contains("StuckThreadCount")) && this.bean.isStuckThreadCountSet()) {
               var5.setStuckThreadCount(this.bean.getStuckThreadCount());
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
