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
import weblogic.management.ManagementException;
import weblogic.utils.collections.CombinedIterator;

public class WLDFDataRetirementMBeanImpl extends ConfigurationMBeanImpl implements WLDFDataRetirementMBean, Serializable {
   private String _ArchiveName;
   private boolean _Enabled;
   private int _RetirementPeriod;
   private int _RetirementTime;
   private static SchemaHelper2 _schemaHelper;

   public WLDFDataRetirementMBeanImpl() {
      this._initializeProperty(-1);
   }

   public WLDFDataRetirementMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public boolean isEnabled() {
      return this._Enabled;
   }

   public boolean isEnabledSet() {
      return this._isSet(7);
   }

   public void setEnabled(boolean var1) {
      boolean var2 = this._Enabled;
      this._Enabled = var1;
      this._postSet(7, var2, var1);
   }

   public String getArchiveName() {
      return this._ArchiveName;
   }

   public boolean isArchiveNameSet() {
      return this._isSet(8);
   }

   public void setArchiveName(String var1) throws InvalidAttributeValueException, ManagementException {
      var1 = var1 == null ? null : var1.trim();
      WLDFValidator.validateDataRetirementArchiveName(this, var1);
      String var2 = this._ArchiveName;
      this._ArchiveName = var1;
      this._postSet(8, var2, var1);
   }

   public int getRetirementTime() {
      return this._RetirementTime;
   }

   public boolean isRetirementTimeSet() {
      return this._isSet(9);
   }

   public void setRetirementTime(int var1) {
      WLDFValidator.validateDataRetirementTime(this, var1);
      int var2 = this._RetirementTime;
      this._RetirementTime = var1;
      this._postSet(9, var2, var1);
   }

   public int getRetirementPeriod() {
      return this._RetirementPeriod;
   }

   public boolean isRetirementPeriodSet() {
      return this._isSet(10);
   }

   public void setRetirementPeriod(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkMin("RetirementPeriod", var1, 1);
      int var2 = this._RetirementPeriod;
      this._RetirementPeriod = var1;
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
         var1 = 8;
      }

      try {
         switch (var1) {
            case 8:
               this._ArchiveName = null;
               if (var2) {
                  break;
               }
            case 10:
               this._RetirementPeriod = 24;
               if (var2) {
                  break;
               }
            case 9:
               this._RetirementTime = 0;
               if (var2) {
                  break;
               }
            case 7:
               this._Enabled = true;
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
      return "WLDFDataRetirement";
   }

   public void putValue(String var1, Object var2) {
      if (var1.equals("ArchiveName")) {
         String var5 = this._ArchiveName;
         this._ArchiveName = (String)var2;
         this._postSet(8, var5, this._ArchiveName);
      } else if (var1.equals("Enabled")) {
         boolean var4 = this._Enabled;
         this._Enabled = (Boolean)var2;
         this._postSet(7, var4, this._Enabled);
      } else {
         int var3;
         if (var1.equals("RetirementPeriod")) {
            var3 = this._RetirementPeriod;
            this._RetirementPeriod = (Integer)var2;
            this._postSet(10, var3, this._RetirementPeriod);
         } else if (var1.equals("RetirementTime")) {
            var3 = this._RetirementTime;
            this._RetirementTime = (Integer)var2;
            this._postSet(9, var3, this._RetirementTime);
         } else {
            super.putValue(var1, var2);
         }
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("ArchiveName")) {
         return this._ArchiveName;
      } else if (var1.equals("Enabled")) {
         return new Boolean(this._Enabled);
      } else if (var1.equals("RetirementPeriod")) {
         return new Integer(this._RetirementPeriod);
      } else {
         return var1.equals("RetirementTime") ? new Integer(this._RetirementTime) : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends ConfigurationMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 7:
               if (var1.equals("enabled")) {
                  return 7;
               }
               break;
            case 12:
               if (var1.equals("archive-name")) {
                  return 8;
               }
               break;
            case 15:
               if (var1.equals("retirement-time")) {
                  return 9;
               }
               break;
            case 17:
               if (var1.equals("retirement-period")) {
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
               return "enabled";
            case 8:
               return "archive-name";
            case 9:
               return "retirement-time";
            case 10:
               return "retirement-period";
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
      private WLDFDataRetirementMBeanImpl bean;

      protected Helper(WLDFDataRetirementMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 7:
               return "Enabled";
            case 8:
               return "ArchiveName";
            case 9:
               return "RetirementTime";
            case 10:
               return "RetirementPeriod";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("ArchiveName")) {
            return 8;
         } else if (var1.equals("RetirementPeriod")) {
            return 10;
         } else if (var1.equals("RetirementTime")) {
            return 9;
         } else {
            return var1.equals("Enabled") ? 7 : super.getPropertyIndex(var1);
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
            if (this.bean.isArchiveNameSet()) {
               var2.append("ArchiveName");
               var2.append(String.valueOf(this.bean.getArchiveName()));
            }

            if (this.bean.isRetirementPeriodSet()) {
               var2.append("RetirementPeriod");
               var2.append(String.valueOf(this.bean.getRetirementPeriod()));
            }

            if (this.bean.isRetirementTimeSet()) {
               var2.append("RetirementTime");
               var2.append(String.valueOf(this.bean.getRetirementTime()));
            }

            if (this.bean.isEnabledSet()) {
               var2.append("Enabled");
               var2.append(String.valueOf(this.bean.isEnabled()));
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
            WLDFDataRetirementMBeanImpl var2 = (WLDFDataRetirementMBeanImpl)var1;
            this.computeDiff("ArchiveName", this.bean.getArchiveName(), var2.getArchiveName(), true);
            this.computeDiff("RetirementPeriod", this.bean.getRetirementPeriod(), var2.getRetirementPeriod(), true);
            this.computeDiff("RetirementTime", this.bean.getRetirementTime(), var2.getRetirementTime(), true);
            this.computeDiff("Enabled", this.bean.isEnabled(), var2.isEnabled(), true);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            WLDFDataRetirementMBeanImpl var3 = (WLDFDataRetirementMBeanImpl)var1.getSourceBean();
            WLDFDataRetirementMBeanImpl var4 = (WLDFDataRetirementMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("ArchiveName")) {
                  var3.setArchiveName(var4.getArchiveName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 8);
               } else if (var5.equals("RetirementPeriod")) {
                  var3.setRetirementPeriod(var4.getRetirementPeriod());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 10);
               } else if (var5.equals("RetirementTime")) {
                  var3.setRetirementTime(var4.getRetirementTime());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 9);
               } else if (var5.equals("Enabled")) {
                  var3.setEnabled(var4.isEnabled());
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
            WLDFDataRetirementMBeanImpl var5 = (WLDFDataRetirementMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("ArchiveName")) && this.bean.isArchiveNameSet()) {
               var5.setArchiveName(this.bean.getArchiveName());
            }

            if ((var3 == null || !var3.contains("RetirementPeriod")) && this.bean.isRetirementPeriodSet()) {
               var5.setRetirementPeriod(this.bean.getRetirementPeriod());
            }

            if ((var3 == null || !var3.contains("RetirementTime")) && this.bean.isRetirementTimeSet()) {
               var5.setRetirementTime(this.bean.getRetirementTime());
            }

            if ((var3 == null || !var3.contains("Enabled")) && this.bean.isEnabledSet()) {
               var5.setEnabled(this.bean.isEnabled());
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
