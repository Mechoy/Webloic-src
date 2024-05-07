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

public class WebserviceTimestampMBeanImpl extends ConfigurationMBeanImpl implements WebserviceTimestampMBean, Serializable {
   private long _ClockPrecision;
   private long _ClockSkew;
   private boolean _ClockSynchronized;
   private boolean _LaxPrecision;
   private long _MaxProcessingDelay;
   private int _ValidityPeriod;
   private static SchemaHelper2 _schemaHelper;

   public WebserviceTimestampMBeanImpl() {
      this._initializeProperty(-1);
   }

   public WebserviceTimestampMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public void setClockSynchronized(boolean var1) {
      boolean var2 = this._ClockSynchronized;
      this._ClockSynchronized = var1;
      this._postSet(7, var2, var1);
   }

   public boolean isClockSynchronized() {
      return this._ClockSynchronized;
   }

   public boolean isClockSynchronizedSet() {
      return this._isSet(7);
   }

   public void setClockPrecision(long var1) {
      weblogic.descriptor.beangen.LegalChecks.checkMin("ClockPrecision", var1, 1L);
      long var3 = this._ClockPrecision;
      this._ClockPrecision = var1;
      this._postSet(8, var3, var1);
   }

   public long getClockPrecision() {
      return this._ClockPrecision;
   }

   public boolean isClockPrecisionSet() {
      return this._isSet(8);
   }

   public void setClockSkew(long var1) {
      weblogic.descriptor.beangen.LegalChecks.checkMin("ClockSkew", var1, 0L);
      long var3 = this._ClockSkew;
      this._ClockSkew = var1;
      this._postSet(9, var3, var1);
   }

   public long getClockSkew() {
      return this._ClockSkew;
   }

   public boolean isClockSkewSet() {
      return this._isSet(9);
   }

   public void setLaxPrecision(boolean var1) {
      boolean var2 = this._LaxPrecision;
      this._LaxPrecision = var1;
      this._postSet(10, var2, var1);
   }

   public boolean isLaxPrecision() {
      return this._LaxPrecision;
   }

   public boolean isLaxPrecisionSet() {
      return this._isSet(10);
   }

   public void setMaxProcessingDelay(long var1) {
      long var3 = this._MaxProcessingDelay;
      this._MaxProcessingDelay = var1;
      this._postSet(11, var3, var1);
   }

   public long getMaxProcessingDelay() {
      return this._MaxProcessingDelay;
   }

   public boolean isMaxProcessingDelaySet() {
      return this._isSet(11);
   }

   public void setValidityPeriod(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkMin("ValidityPeriod", var1, 1);
      int var2 = this._ValidityPeriod;
      this._ValidityPeriod = var1;
      this._postSet(12, var2, var1);
   }

   public int getValidityPeriod() {
      return this._ValidityPeriod;
   }

   public boolean isValidityPeriodSet() {
      return this._isSet(12);
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
               this._ClockPrecision = 60000L;
               if (var2) {
                  break;
               }
            case 9:
               this._ClockSkew = 60000L;
               if (var2) {
                  break;
               }
            case 11:
               this._MaxProcessingDelay = -1L;
               if (var2) {
                  break;
               }
            case 12:
               this._ValidityPeriod = 60;
               if (var2) {
                  break;
               }
            case 7:
               this._ClockSynchronized = true;
               if (var2) {
                  break;
               }
            case 10:
               this._LaxPrecision = false;
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
      return "WebserviceTimestamp";
   }

   public void putValue(String var1, Object var2) {
      long var5;
      if (var1.equals("ClockPrecision")) {
         var5 = this._ClockPrecision;
         this._ClockPrecision = (Long)var2;
         this._postSet(8, var5, this._ClockPrecision);
      } else if (var1.equals("ClockSkew")) {
         var5 = this._ClockSkew;
         this._ClockSkew = (Long)var2;
         this._postSet(9, var5, this._ClockSkew);
      } else {
         boolean var6;
         if (var1.equals("ClockSynchronized")) {
            var6 = this._ClockSynchronized;
            this._ClockSynchronized = (Boolean)var2;
            this._postSet(7, var6, this._ClockSynchronized);
         } else if (var1.equals("LaxPrecision")) {
            var6 = this._LaxPrecision;
            this._LaxPrecision = (Boolean)var2;
            this._postSet(10, var6, this._LaxPrecision);
         } else if (var1.equals("MaxProcessingDelay")) {
            var5 = this._MaxProcessingDelay;
            this._MaxProcessingDelay = (Long)var2;
            this._postSet(11, var5, this._MaxProcessingDelay);
         } else if (var1.equals("ValidityPeriod")) {
            int var3 = this._ValidityPeriod;
            this._ValidityPeriod = (Integer)var2;
            this._postSet(12, var3, this._ValidityPeriod);
         } else {
            super.putValue(var1, var2);
         }
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("ClockPrecision")) {
         return new Long(this._ClockPrecision);
      } else if (var1.equals("ClockSkew")) {
         return new Long(this._ClockSkew);
      } else if (var1.equals("ClockSynchronized")) {
         return new Boolean(this._ClockSynchronized);
      } else if (var1.equals("LaxPrecision")) {
         return new Boolean(this._LaxPrecision);
      } else if (var1.equals("MaxProcessingDelay")) {
         return new Long(this._MaxProcessingDelay);
      } else {
         return var1.equals("ValidityPeriod") ? new Integer(this._ValidityPeriod) : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends ConfigurationMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 10:
               if (var1.equals("clock-skew")) {
                  return 9;
               }
            case 11:
            case 12:
            case 14:
            case 16:
            case 17:
            case 19:
            default:
               break;
            case 13:
               if (var1.equals("lax-precision")) {
                  return 10;
               }
               break;
            case 15:
               if (var1.equals("clock-precision")) {
                  return 8;
               }

               if (var1.equals("validity-period")) {
                  return 12;
               }
               break;
            case 18:
               if (var1.equals("clock-synchronized")) {
                  return 7;
               }
               break;
            case 20:
               if (var1.equals("max-processing-delay")) {
                  return 11;
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
               return "clock-synchronized";
            case 8:
               return "clock-precision";
            case 9:
               return "clock-skew";
            case 10:
               return "lax-precision";
            case 11:
               return "max-processing-delay";
            case 12:
               return "validity-period";
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
      private WebserviceTimestampMBeanImpl bean;

      protected Helper(WebserviceTimestampMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 7:
               return "ClockSynchronized";
            case 8:
               return "ClockPrecision";
            case 9:
               return "ClockSkew";
            case 10:
               return "LaxPrecision";
            case 11:
               return "MaxProcessingDelay";
            case 12:
               return "ValidityPeriod";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("ClockPrecision")) {
            return 8;
         } else if (var1.equals("ClockSkew")) {
            return 9;
         } else if (var1.equals("MaxProcessingDelay")) {
            return 11;
         } else if (var1.equals("ValidityPeriod")) {
            return 12;
         } else if (var1.equals("ClockSynchronized")) {
            return 7;
         } else {
            return var1.equals("LaxPrecision") ? 10 : super.getPropertyIndex(var1);
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
            if (this.bean.isClockPrecisionSet()) {
               var2.append("ClockPrecision");
               var2.append(String.valueOf(this.bean.getClockPrecision()));
            }

            if (this.bean.isClockSkewSet()) {
               var2.append("ClockSkew");
               var2.append(String.valueOf(this.bean.getClockSkew()));
            }

            if (this.bean.isMaxProcessingDelaySet()) {
               var2.append("MaxProcessingDelay");
               var2.append(String.valueOf(this.bean.getMaxProcessingDelay()));
            }

            if (this.bean.isValidityPeriodSet()) {
               var2.append("ValidityPeriod");
               var2.append(String.valueOf(this.bean.getValidityPeriod()));
            }

            if (this.bean.isClockSynchronizedSet()) {
               var2.append("ClockSynchronized");
               var2.append(String.valueOf(this.bean.isClockSynchronized()));
            }

            if (this.bean.isLaxPrecisionSet()) {
               var2.append("LaxPrecision");
               var2.append(String.valueOf(this.bean.isLaxPrecision()));
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
            WebserviceTimestampMBeanImpl var2 = (WebserviceTimestampMBeanImpl)var1;
            this.computeDiff("ClockPrecision", this.bean.getClockPrecision(), var2.getClockPrecision(), true);
            this.computeDiff("ClockSkew", this.bean.getClockSkew(), var2.getClockSkew(), true);
            this.computeDiff("MaxProcessingDelay", this.bean.getMaxProcessingDelay(), var2.getMaxProcessingDelay(), true);
            this.computeDiff("ValidityPeriod", this.bean.getValidityPeriod(), var2.getValidityPeriod(), true);
            this.computeDiff("ClockSynchronized", this.bean.isClockSynchronized(), var2.isClockSynchronized(), true);
            this.computeDiff("LaxPrecision", this.bean.isLaxPrecision(), var2.isLaxPrecision(), true);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            WebserviceTimestampMBeanImpl var3 = (WebserviceTimestampMBeanImpl)var1.getSourceBean();
            WebserviceTimestampMBeanImpl var4 = (WebserviceTimestampMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("ClockPrecision")) {
                  var3.setClockPrecision(var4.getClockPrecision());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 8);
               } else if (var5.equals("ClockSkew")) {
                  var3.setClockSkew(var4.getClockSkew());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 9);
               } else if (var5.equals("MaxProcessingDelay")) {
                  var3.setMaxProcessingDelay(var4.getMaxProcessingDelay());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 11);
               } else if (var5.equals("ValidityPeriod")) {
                  var3.setValidityPeriod(var4.getValidityPeriod());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 12);
               } else if (var5.equals("ClockSynchronized")) {
                  var3.setClockSynchronized(var4.isClockSynchronized());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 7);
               } else if (var5.equals("LaxPrecision")) {
                  var3.setLaxPrecision(var4.isLaxPrecision());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 10);
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
            WebserviceTimestampMBeanImpl var5 = (WebserviceTimestampMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("ClockPrecision")) && this.bean.isClockPrecisionSet()) {
               var5.setClockPrecision(this.bean.getClockPrecision());
            }

            if ((var3 == null || !var3.contains("ClockSkew")) && this.bean.isClockSkewSet()) {
               var5.setClockSkew(this.bean.getClockSkew());
            }

            if ((var3 == null || !var3.contains("MaxProcessingDelay")) && this.bean.isMaxProcessingDelaySet()) {
               var5.setMaxProcessingDelay(this.bean.getMaxProcessingDelay());
            }

            if ((var3 == null || !var3.contains("ValidityPeriod")) && this.bean.isValidityPeriodSet()) {
               var5.setValidityPeriod(this.bean.getValidityPeriod());
            }

            if ((var3 == null || !var3.contains("ClockSynchronized")) && this.bean.isClockSynchronizedSet()) {
               var5.setClockSynchronized(this.bean.isClockSynchronized());
            }

            if ((var3 == null || !var3.contains("LaxPrecision")) && this.bean.isLaxPrecisionSet()) {
               var5.setLaxPrecision(this.bean.isLaxPrecision());
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
