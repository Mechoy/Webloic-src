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

public class SNMPGaugeMonitorMBeanImpl extends SNMPJMXMonitorMBeanImpl implements SNMPGaugeMonitorMBean, Serializable {
   private int _ThresholdHigh;
   private int _ThresholdLow;
   private static SchemaHelper2 _schemaHelper;

   public SNMPGaugeMonitorMBeanImpl() {
      this._initializeProperty(-1);
   }

   public SNMPGaugeMonitorMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public int getThresholdHigh() {
      return this._ThresholdHigh;
   }

   public boolean isThresholdHighSet() {
      return this._isSet(12);
   }

   public void setThresholdHigh(int var1) {
      int var2 = this._ThresholdHigh;
      this._ThresholdHigh = var1;
      this._postSet(12, var2, var1);
   }

   public int getThresholdLow() {
      return this._ThresholdLow;
   }

   public boolean isThresholdLowSet() {
      return this._isSet(13);
   }

   public void setThresholdLow(int var1) {
      int var2 = this._ThresholdLow;
      this._ThresholdLow = var1;
      this._postSet(13, var2, var1);
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
         var1 = 12;
      }

      try {
         switch (var1) {
            case 12:
               this._ThresholdHigh = 0;
               if (var2) {
                  break;
               }
            case 13:
               this._ThresholdLow = 0;
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
      return "SNMPGaugeMonitor";
   }

   public void putValue(String var1, Object var2) {
      int var3;
      if (var1.equals("ThresholdHigh")) {
         var3 = this._ThresholdHigh;
         this._ThresholdHigh = (Integer)var2;
         this._postSet(12, var3, this._ThresholdHigh);
      } else if (var1.equals("ThresholdLow")) {
         var3 = this._ThresholdLow;
         this._ThresholdLow = (Integer)var2;
         this._postSet(13, var3, this._ThresholdLow);
      } else {
         super.putValue(var1, var2);
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("ThresholdHigh")) {
         return new Integer(this._ThresholdHigh);
      } else {
         return var1.equals("ThresholdLow") ? new Integer(this._ThresholdLow) : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends SNMPJMXMonitorMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 13:
               if (var1.equals("threshold-low")) {
                  return 13;
               }
               break;
            case 14:
               if (var1.equals("threshold-high")) {
                  return 12;
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
            case 12:
               return "threshold-high";
            case 13:
               return "threshold-low";
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

   protected static class Helper extends SNMPJMXMonitorMBeanImpl.Helper {
      private SNMPGaugeMonitorMBeanImpl bean;

      protected Helper(SNMPGaugeMonitorMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 12:
               return "ThresholdHigh";
            case 13:
               return "ThresholdLow";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("ThresholdHigh")) {
            return 12;
         } else {
            return var1.equals("ThresholdLow") ? 13 : super.getPropertyIndex(var1);
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
            if (this.bean.isThresholdHighSet()) {
               var2.append("ThresholdHigh");
               var2.append(String.valueOf(this.bean.getThresholdHigh()));
            }

            if (this.bean.isThresholdLowSet()) {
               var2.append("ThresholdLow");
               var2.append(String.valueOf(this.bean.getThresholdLow()));
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
            SNMPGaugeMonitorMBeanImpl var2 = (SNMPGaugeMonitorMBeanImpl)var1;
            this.computeDiff("ThresholdHigh", this.bean.getThresholdHigh(), var2.getThresholdHigh(), true);
            this.computeDiff("ThresholdLow", this.bean.getThresholdLow(), var2.getThresholdLow(), true);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            SNMPGaugeMonitorMBeanImpl var3 = (SNMPGaugeMonitorMBeanImpl)var1.getSourceBean();
            SNMPGaugeMonitorMBeanImpl var4 = (SNMPGaugeMonitorMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("ThresholdHigh")) {
                  var3.setThresholdHigh(var4.getThresholdHigh());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 12);
               } else if (var5.equals("ThresholdLow")) {
                  var3.setThresholdLow(var4.getThresholdLow());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 13);
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
            SNMPGaugeMonitorMBeanImpl var5 = (SNMPGaugeMonitorMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("ThresholdHigh")) && this.bean.isThresholdHighSet()) {
               var5.setThresholdHigh(this.bean.getThresholdHigh());
            }

            if ((var3 == null || !var3.contains("ThresholdLow")) && this.bean.isThresholdLowSet()) {
               var5.setThresholdLow(this.bean.getThresholdLow());
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
