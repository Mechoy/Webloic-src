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

public class SNMPJMXMonitorMBeanImpl extends SNMPTrapSourceMBeanImpl implements SNMPJMXMonitorMBean, Serializable {
   private String _MonitoredAttributeName;
   private String _MonitoredMBeanName;
   private String _MonitoredMBeanType;
   private int _PollingInterval;
   private static SchemaHelper2 _schemaHelper;

   public SNMPJMXMonitorMBeanImpl() {
      this._initializeProperty(-1);
   }

   public SNMPJMXMonitorMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public String getMonitoredMBeanType() {
      return this._MonitoredMBeanType;
   }

   public boolean isMonitoredMBeanTypeSet() {
      return this._isSet(8);
   }

   public void setMonitoredMBeanType(String var1) throws InvalidAttributeValueException, ConfigurationException {
      var1 = var1 == null ? null : var1.trim();
      weblogic.descriptor.beangen.LegalChecks.checkNonEmptyString("MonitoredMBeanType", var1);
      String var2 = this._MonitoredMBeanType;
      this._MonitoredMBeanType = var1;
      this._postSet(8, var2, var1);
   }

   public String getMonitoredMBeanName() {
      return this._MonitoredMBeanName;
   }

   public boolean isMonitoredMBeanNameSet() {
      return this._isSet(9);
   }

   public void setMonitoredMBeanName(String var1) throws InvalidAttributeValueException, ConfigurationException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._MonitoredMBeanName;
      this._MonitoredMBeanName = var1;
      this._postSet(9, var2, var1);
   }

   public String getMonitoredAttributeName() {
      return this._MonitoredAttributeName;
   }

   public boolean isMonitoredAttributeNameSet() {
      return this._isSet(10);
   }

   public void setMonitoredAttributeName(String var1) throws InvalidAttributeValueException, ConfigurationException {
      var1 = var1 == null ? null : var1.trim();
      weblogic.descriptor.beangen.LegalChecks.checkNonEmptyString("MonitoredAttributeName", var1);
      String var2 = this._MonitoredAttributeName;
      this._MonitoredAttributeName = var1;
      this._postSet(10, var2, var1);
   }

   public int getPollingInterval() {
      return this._PollingInterval;
   }

   public boolean isPollingIntervalSet() {
      return this._isSet(11);
   }

   public void setPollingInterval(int var1) throws InvalidAttributeValueException, ConfigurationException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("PollingInterval", (long)var1, 1L, 65535L);
      int var2 = this._PollingInterval;
      this._PollingInterval = var1;
      this._postSet(11, var2, var1);
   }

   public Object _getKey() {
      return super._getKey();
   }

   public void _validate() throws IllegalArgumentException {
      super._validate();
      SNMPValidator.validateJMXMonitorMBean(this);
      weblogic.descriptor.beangen.LegalChecks.checkIsSet("MonitoredAttributeName", this.isMonitoredAttributeNameSet());
      weblogic.descriptor.beangen.LegalChecks.checkIsSet("MonitoredMBeanType", this.isMonitoredMBeanTypeSet());
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
               this._MonitoredAttributeName = null;
               if (var2) {
                  break;
               }
            case 9:
               this._MonitoredMBeanName = null;
               if (var2) {
                  break;
               }
            case 8:
               this._MonitoredMBeanType = null;
               if (var2) {
                  break;
               }
            case 11:
               this._PollingInterval = 10;
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
      return "SNMPJMXMonitor";
   }

   public void putValue(String var1, Object var2) {
      String var4;
      if (var1.equals("MonitoredAttributeName")) {
         var4 = this._MonitoredAttributeName;
         this._MonitoredAttributeName = (String)var2;
         this._postSet(10, var4, this._MonitoredAttributeName);
      } else if (var1.equals("MonitoredMBeanName")) {
         var4 = this._MonitoredMBeanName;
         this._MonitoredMBeanName = (String)var2;
         this._postSet(9, var4, this._MonitoredMBeanName);
      } else if (var1.equals("MonitoredMBeanType")) {
         var4 = this._MonitoredMBeanType;
         this._MonitoredMBeanType = (String)var2;
         this._postSet(8, var4, this._MonitoredMBeanType);
      } else if (var1.equals("PollingInterval")) {
         int var3 = this._PollingInterval;
         this._PollingInterval = (Integer)var2;
         this._postSet(11, var3, this._PollingInterval);
      } else {
         super.putValue(var1, var2);
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("MonitoredAttributeName")) {
         return this._MonitoredAttributeName;
      } else if (var1.equals("MonitoredMBeanName")) {
         return this._MonitoredMBeanName;
      } else if (var1.equals("MonitoredMBeanType")) {
         return this._MonitoredMBeanType;
      } else {
         return var1.equals("PollingInterval") ? new Integer(this._PollingInterval) : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends SNMPTrapSourceMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 16:
               if (var1.equals("polling-interval")) {
                  return 11;
               }
               break;
            case 20:
               if (var1.equals("monitoredm-bean-name")) {
                  return 9;
               }

               if (var1.equals("monitoredm-bean-type")) {
                  return 8;
               }
               break;
            case 24:
               if (var1.equals("monitored-attribute-name")) {
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
            case 8:
               return "monitoredm-bean-type";
            case 9:
               return "monitoredm-bean-name";
            case 10:
               return "monitored-attribute-name";
            case 11:
               return "polling-interval";
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

   protected static class Helper extends SNMPTrapSourceMBeanImpl.Helper {
      private SNMPJMXMonitorMBeanImpl bean;

      protected Helper(SNMPJMXMonitorMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 8:
               return "MonitoredMBeanType";
            case 9:
               return "MonitoredMBeanName";
            case 10:
               return "MonitoredAttributeName";
            case 11:
               return "PollingInterval";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("MonitoredAttributeName")) {
            return 10;
         } else if (var1.equals("MonitoredMBeanName")) {
            return 9;
         } else if (var1.equals("MonitoredMBeanType")) {
            return 8;
         } else {
            return var1.equals("PollingInterval") ? 11 : super.getPropertyIndex(var1);
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
            if (this.bean.isMonitoredAttributeNameSet()) {
               var2.append("MonitoredAttributeName");
               var2.append(String.valueOf(this.bean.getMonitoredAttributeName()));
            }

            if (this.bean.isMonitoredMBeanNameSet()) {
               var2.append("MonitoredMBeanName");
               var2.append(String.valueOf(this.bean.getMonitoredMBeanName()));
            }

            if (this.bean.isMonitoredMBeanTypeSet()) {
               var2.append("MonitoredMBeanType");
               var2.append(String.valueOf(this.bean.getMonitoredMBeanType()));
            }

            if (this.bean.isPollingIntervalSet()) {
               var2.append("PollingInterval");
               var2.append(String.valueOf(this.bean.getPollingInterval()));
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
            SNMPJMXMonitorMBeanImpl var2 = (SNMPJMXMonitorMBeanImpl)var1;
            this.computeDiff("MonitoredAttributeName", this.bean.getMonitoredAttributeName(), var2.getMonitoredAttributeName(), true);
            this.computeDiff("MonitoredMBeanName", this.bean.getMonitoredMBeanName(), var2.getMonitoredMBeanName(), true);
            this.computeDiff("MonitoredMBeanType", this.bean.getMonitoredMBeanType(), var2.getMonitoredMBeanType(), true);
            this.computeDiff("PollingInterval", this.bean.getPollingInterval(), var2.getPollingInterval(), true);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            SNMPJMXMonitorMBeanImpl var3 = (SNMPJMXMonitorMBeanImpl)var1.getSourceBean();
            SNMPJMXMonitorMBeanImpl var4 = (SNMPJMXMonitorMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("MonitoredAttributeName")) {
                  var3.setMonitoredAttributeName(var4.getMonitoredAttributeName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 10);
               } else if (var5.equals("MonitoredMBeanName")) {
                  var3.setMonitoredMBeanName(var4.getMonitoredMBeanName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 9);
               } else if (var5.equals("MonitoredMBeanType")) {
                  var3.setMonitoredMBeanType(var4.getMonitoredMBeanType());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 8);
               } else if (var5.equals("PollingInterval")) {
                  var3.setPollingInterval(var4.getPollingInterval());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 11);
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
            SNMPJMXMonitorMBeanImpl var5 = (SNMPJMXMonitorMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("MonitoredAttributeName")) && this.bean.isMonitoredAttributeNameSet()) {
               var5.setMonitoredAttributeName(this.bean.getMonitoredAttributeName());
            }

            if ((var3 == null || !var3.contains("MonitoredMBeanName")) && this.bean.isMonitoredMBeanNameSet()) {
               var5.setMonitoredMBeanName(this.bean.getMonitoredMBeanName());
            }

            if ((var3 == null || !var3.contains("MonitoredMBeanType")) && this.bean.isMonitoredMBeanTypeSet()) {
               var5.setMonitoredMBeanType(this.bean.getMonitoredMBeanType());
            }

            if ((var3 == null || !var3.contains("PollingInterval")) && this.bean.isPollingIntervalSet()) {
               var5.setPollingInterval(this.bean.getPollingInterval());
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
