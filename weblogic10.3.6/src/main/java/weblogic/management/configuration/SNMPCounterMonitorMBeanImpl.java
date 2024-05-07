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

public class SNMPCounterMonitorMBeanImpl extends SNMPJMXMonitorMBeanImpl implements SNMPCounterMonitorMBean, Serializable {
   private int _Modulus;
   private int _Offset;
   private int _Threshold;
   private static SchemaHelper2 _schemaHelper;

   public SNMPCounterMonitorMBeanImpl() {
      this._initializeProperty(-1);
   }

   public SNMPCounterMonitorMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public int getThreshold() {
      return this._Threshold;
   }

   public boolean isThresholdSet() {
      return this._isSet(12);
   }

   public void setThreshold(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkMin("Threshold", var1, 0);
      int var2 = this._Threshold;
      this._Threshold = var1;
      this._postSet(12, var2, var1);
   }

   public int getOffset() {
      return this._Offset;
   }

   public boolean isOffsetSet() {
      return this._isSet(13);
   }

   public void setOffset(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkMin("Offset", var1, 0);
      int var2 = this._Offset;
      this._Offset = var1;
      this._postSet(13, var2, var1);
   }

   public int getModulus() {
      return this._Modulus;
   }

   public boolean isModulusSet() {
      return this._isSet(14);
   }

   public void setModulus(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkMin("Modulus", var1, 0);
      int var2 = this._Modulus;
      this._Modulus = var1;
      this._postSet(14, var2, var1);
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
         var1 = 14;
      }

      try {
         switch (var1) {
            case 14:
               this._Modulus = 0;
               if (var2) {
                  break;
               }
            case 13:
               this._Offset = 0;
               if (var2) {
                  break;
               }
            case 12:
               this._Threshold = 0;
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
      return "SNMPCounterMonitor";
   }

   public void putValue(String var1, Object var2) {
      int var3;
      if (var1.equals("Modulus")) {
         var3 = this._Modulus;
         this._Modulus = (Integer)var2;
         this._postSet(14, var3, this._Modulus);
      } else if (var1.equals("Offset")) {
         var3 = this._Offset;
         this._Offset = (Integer)var2;
         this._postSet(13, var3, this._Offset);
      } else if (var1.equals("Threshold")) {
         var3 = this._Threshold;
         this._Threshold = (Integer)var2;
         this._postSet(12, var3, this._Threshold);
      } else {
         super.putValue(var1, var2);
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("Modulus")) {
         return new Integer(this._Modulus);
      } else if (var1.equals("Offset")) {
         return new Integer(this._Offset);
      } else {
         return var1.equals("Threshold") ? new Integer(this._Threshold) : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends SNMPJMXMonitorMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 6:
               if (var1.equals("offset")) {
                  return 13;
               }
               break;
            case 7:
               if (var1.equals("modulus")) {
                  return 14;
               }
            case 8:
            default:
               break;
            case 9:
               if (var1.equals("threshold")) {
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
               return "threshold";
            case 13:
               return "offset";
            case 14:
               return "modulus";
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
      private SNMPCounterMonitorMBeanImpl bean;

      protected Helper(SNMPCounterMonitorMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 12:
               return "Threshold";
            case 13:
               return "Offset";
            case 14:
               return "Modulus";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("Modulus")) {
            return 14;
         } else if (var1.equals("Offset")) {
            return 13;
         } else {
            return var1.equals("Threshold") ? 12 : super.getPropertyIndex(var1);
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
            if (this.bean.isModulusSet()) {
               var2.append("Modulus");
               var2.append(String.valueOf(this.bean.getModulus()));
            }

            if (this.bean.isOffsetSet()) {
               var2.append("Offset");
               var2.append(String.valueOf(this.bean.getOffset()));
            }

            if (this.bean.isThresholdSet()) {
               var2.append("Threshold");
               var2.append(String.valueOf(this.bean.getThreshold()));
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
            SNMPCounterMonitorMBeanImpl var2 = (SNMPCounterMonitorMBeanImpl)var1;
            this.computeDiff("Modulus", this.bean.getModulus(), var2.getModulus(), true);
            this.computeDiff("Offset", this.bean.getOffset(), var2.getOffset(), true);
            this.computeDiff("Threshold", this.bean.getThreshold(), var2.getThreshold(), true);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            SNMPCounterMonitorMBeanImpl var3 = (SNMPCounterMonitorMBeanImpl)var1.getSourceBean();
            SNMPCounterMonitorMBeanImpl var4 = (SNMPCounterMonitorMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("Modulus")) {
                  var3.setModulus(var4.getModulus());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 14);
               } else if (var5.equals("Offset")) {
                  var3.setOffset(var4.getOffset());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 13);
               } else if (var5.equals("Threshold")) {
                  var3.setThreshold(var4.getThreshold());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 12);
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
            SNMPCounterMonitorMBeanImpl var5 = (SNMPCounterMonitorMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("Modulus")) && this.bean.isModulusSet()) {
               var5.setModulus(this.bean.getModulus());
            }

            if ((var3 == null || !var3.contains("Offset")) && this.bean.isOffsetSet()) {
               var5.setOffset(this.bean.getOffset());
            }

            if ((var3 == null || !var3.contains("Threshold")) && this.bean.isThresholdSet()) {
               var5.setThreshold(this.bean.getThreshold());
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
