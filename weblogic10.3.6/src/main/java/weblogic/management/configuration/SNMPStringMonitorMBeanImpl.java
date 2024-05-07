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

public class SNMPStringMonitorMBeanImpl extends SNMPJMXMonitorMBeanImpl implements SNMPStringMonitorMBean, Serializable {
   private boolean _NotifyDiffer;
   private boolean _NotifyMatch;
   private String _StringToCompare;
   private static SchemaHelper2 _schemaHelper;

   public SNMPStringMonitorMBeanImpl() {
      this._initializeProperty(-1);
   }

   public SNMPStringMonitorMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public String getStringToCompare() {
      return this._StringToCompare;
   }

   public boolean isStringToCompareSet() {
      return this._isSet(12);
   }

   public void setStringToCompare(String var1) throws InvalidAttributeValueException, ConfigurationException {
      var1 = var1 == null ? null : var1.trim();
      weblogic.descriptor.beangen.LegalChecks.checkNonEmptyString("StringToCompare", var1);
      String var2 = this._StringToCompare;
      this._StringToCompare = var1;
      this._postSet(12, var2, var1);
   }

   public boolean isNotifyDiffer() {
      return this._NotifyDiffer;
   }

   public boolean isNotifyDifferSet() {
      return this._isSet(13);
   }

   public void setNotifyDiffer(boolean var1) {
      boolean var2 = this._NotifyDiffer;
      this._NotifyDiffer = var1;
      this._postSet(13, var2, var1);
   }

   public boolean isNotifyMatch() {
      return this._NotifyMatch;
   }

   public boolean isNotifyMatchSet() {
      return this._isSet(14);
   }

   public void setNotifyMatch(boolean var1) {
      boolean var2 = this._NotifyMatch;
      this._NotifyMatch = var1;
      this._postSet(14, var2, var1);
   }

   public Object _getKey() {
      return super._getKey();
   }

   public void _validate() throws IllegalArgumentException {
      super._validate();
      weblogic.descriptor.beangen.LegalChecks.checkIsSet("StringToCompare", this.isStringToCompareSet());
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
               this._StringToCompare = null;
               if (var2) {
                  break;
               }
            case 13:
               this._NotifyDiffer = false;
               if (var2) {
                  break;
               }
            case 14:
               this._NotifyMatch = false;
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
      return "SNMPStringMonitor";
   }

   public void putValue(String var1, Object var2) {
      boolean var4;
      if (var1.equals("NotifyDiffer")) {
         var4 = this._NotifyDiffer;
         this._NotifyDiffer = (Boolean)var2;
         this._postSet(13, var4, this._NotifyDiffer);
      } else if (var1.equals("NotifyMatch")) {
         var4 = this._NotifyMatch;
         this._NotifyMatch = (Boolean)var2;
         this._postSet(14, var4, this._NotifyMatch);
      } else if (var1.equals("StringToCompare")) {
         String var3 = this._StringToCompare;
         this._StringToCompare = (String)var2;
         this._postSet(12, var3, this._StringToCompare);
      } else {
         super.putValue(var1, var2);
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("NotifyDiffer")) {
         return new Boolean(this._NotifyDiffer);
      } else if (var1.equals("NotifyMatch")) {
         return new Boolean(this._NotifyMatch);
      } else {
         return var1.equals("StringToCompare") ? this._StringToCompare : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends SNMPJMXMonitorMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 12:
               if (var1.equals("notify-match")) {
                  return 14;
               }
               break;
            case 13:
               if (var1.equals("notify-differ")) {
                  return 13;
               }
               break;
            case 17:
               if (var1.equals("string-to-compare")) {
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
               return "string-to-compare";
            case 13:
               return "notify-differ";
            case 14:
               return "notify-match";
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
      private SNMPStringMonitorMBeanImpl bean;

      protected Helper(SNMPStringMonitorMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 12:
               return "StringToCompare";
            case 13:
               return "NotifyDiffer";
            case 14:
               return "NotifyMatch";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("StringToCompare")) {
            return 12;
         } else if (var1.equals("NotifyDiffer")) {
            return 13;
         } else {
            return var1.equals("NotifyMatch") ? 14 : super.getPropertyIndex(var1);
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
            if (this.bean.isStringToCompareSet()) {
               var2.append("StringToCompare");
               var2.append(String.valueOf(this.bean.getStringToCompare()));
            }

            if (this.bean.isNotifyDifferSet()) {
               var2.append("NotifyDiffer");
               var2.append(String.valueOf(this.bean.isNotifyDiffer()));
            }

            if (this.bean.isNotifyMatchSet()) {
               var2.append("NotifyMatch");
               var2.append(String.valueOf(this.bean.isNotifyMatch()));
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
            SNMPStringMonitorMBeanImpl var2 = (SNMPStringMonitorMBeanImpl)var1;
            this.computeDiff("StringToCompare", this.bean.getStringToCompare(), var2.getStringToCompare(), true);
            this.computeDiff("NotifyDiffer", this.bean.isNotifyDiffer(), var2.isNotifyDiffer(), true);
            this.computeDiff("NotifyMatch", this.bean.isNotifyMatch(), var2.isNotifyMatch(), true);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            SNMPStringMonitorMBeanImpl var3 = (SNMPStringMonitorMBeanImpl)var1.getSourceBean();
            SNMPStringMonitorMBeanImpl var4 = (SNMPStringMonitorMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("StringToCompare")) {
                  var3.setStringToCompare(var4.getStringToCompare());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 12);
               } else if (var5.equals("NotifyDiffer")) {
                  var3.setNotifyDiffer(var4.isNotifyDiffer());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 13);
               } else if (var5.equals("NotifyMatch")) {
                  var3.setNotifyMatch(var4.isNotifyMatch());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 14);
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
            SNMPStringMonitorMBeanImpl var5 = (SNMPStringMonitorMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("StringToCompare")) && this.bean.isStringToCompareSet()) {
               var5.setStringToCompare(this.bean.getStringToCompare());
            }

            if ((var3 == null || !var3.contains("NotifyDiffer")) && this.bean.isNotifyDifferSet()) {
               var5.setNotifyDiffer(this.bean.isNotifyDiffer());
            }

            if ((var3 == null || !var3.contains("NotifyMatch")) && this.bean.isNotifyMatchSet()) {
               var5.setNotifyMatch(this.bean.isNotifyMatch());
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
