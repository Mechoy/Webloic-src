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

public class JMXMBeanImpl extends ConfigurationMBeanImpl implements JMXMBean, Serializable {
   private boolean _CompatibilityMBeanServerEnabled;
   private boolean _DomainMBeanServerEnabled;
   private boolean _EditMBeanServerEnabled;
   private int _InvocationTimeoutSeconds;
   private boolean _ManagementEJBEnabled;
   private boolean _PlatformMBeanServerEnabled;
   private boolean _PlatformMBeanServerUsed;
   private boolean _RuntimeMBeanServerEnabled;
   private static SchemaHelper2 _schemaHelper;

   public JMXMBeanImpl() {
      this._initializeProperty(-1);
   }

   public JMXMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public boolean isRuntimeMBeanServerEnabled() {
      return this._RuntimeMBeanServerEnabled;
   }

   public boolean isRuntimeMBeanServerEnabledSet() {
      return this._isSet(7);
   }

   public void setRuntimeMBeanServerEnabled(boolean var1) {
      boolean var2 = this._RuntimeMBeanServerEnabled;
      this._RuntimeMBeanServerEnabled = var1;
      this._postSet(7, var2, var1);
   }

   public boolean isDomainMBeanServerEnabled() {
      return this._DomainMBeanServerEnabled;
   }

   public boolean isDomainMBeanServerEnabledSet() {
      return this._isSet(8);
   }

   public void setDomainMBeanServerEnabled(boolean var1) {
      boolean var2 = this._DomainMBeanServerEnabled;
      this._DomainMBeanServerEnabled = var1;
      this._postSet(8, var2, var1);
   }

   public boolean isEditMBeanServerEnabled() {
      return this._EditMBeanServerEnabled;
   }

   public boolean isEditMBeanServerEnabledSet() {
      return this._isSet(9);
   }

   public void setEditMBeanServerEnabled(boolean var1) {
      boolean var2 = this._EditMBeanServerEnabled;
      this._EditMBeanServerEnabled = var1;
      this._postSet(9, var2, var1);
   }

   public boolean isCompatibilityMBeanServerEnabled() {
      return this._CompatibilityMBeanServerEnabled;
   }

   public boolean isCompatibilityMBeanServerEnabledSet() {
      return this._isSet(10);
   }

   public void setCompatibilityMBeanServerEnabled(boolean var1) {
      boolean var2 = this._CompatibilityMBeanServerEnabled;
      this._CompatibilityMBeanServerEnabled = var1;
      this._postSet(10, var2, var1);
   }

   public boolean isManagementEJBEnabled() {
      return this._ManagementEJBEnabled;
   }

   public boolean isManagementEJBEnabledSet() {
      return this._isSet(11);
   }

   public void setManagementEJBEnabled(boolean var1) {
      boolean var2 = this._ManagementEJBEnabled;
      this._ManagementEJBEnabled = var1;
      this._postSet(11, var2, var1);
   }

   public boolean isPlatformMBeanServerEnabled() {
      return this._PlatformMBeanServerEnabled;
   }

   public boolean isPlatformMBeanServerEnabledSet() {
      return this._isSet(12);
   }

   public void setPlatformMBeanServerEnabled(boolean var1) {
      boolean var2 = this._PlatformMBeanServerEnabled;
      this._PlatformMBeanServerEnabled = var1;
      this._postSet(12, var2, var1);
   }

   public int getInvocationTimeoutSeconds() {
      return this._InvocationTimeoutSeconds;
   }

   public boolean isInvocationTimeoutSecondsSet() {
      return this._isSet(13);
   }

   public void setInvocationTimeoutSeconds(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("InvocationTimeoutSeconds", (long)var1, 0L, 2147483647L);
      int var2 = this._InvocationTimeoutSeconds;
      this._InvocationTimeoutSeconds = var1;
      this._postSet(13, var2, var1);
   }

   public boolean isPlatformMBeanServerUsed() {
      if (!this._isSet(14)) {
         try {
            return !LegalHelper.versionEarlierThan(((DomainMBean)this.getParent()).getDomainVersion(), "10.3.3.0");
         } catch (NullPointerException var2) {
         }
      }

      return this._PlatformMBeanServerUsed;
   }

   public boolean isPlatformMBeanServerUsedSet() {
      return this._isSet(14);
   }

   public void setPlatformMBeanServerUsed(boolean var1) {
      boolean var2 = this._PlatformMBeanServerUsed;
      this._PlatformMBeanServerUsed = var1;
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
         var1 = 13;
      }

      try {
         switch (var1) {
            case 13:
               this._InvocationTimeoutSeconds = 0;
               if (var2) {
                  break;
               }
            case 10:
               this._CompatibilityMBeanServerEnabled = true;
               if (var2) {
                  break;
               }
            case 8:
               this._DomainMBeanServerEnabled = true;
               if (var2) {
                  break;
               }
            case 9:
               this._EditMBeanServerEnabled = true;
               if (var2) {
                  break;
               }
            case 11:
               this._ManagementEJBEnabled = true;
               if (var2) {
                  break;
               }
            case 12:
               this._PlatformMBeanServerEnabled = false;
               if (var2) {
                  break;
               }
            case 14:
               this._PlatformMBeanServerUsed = false;
               if (var2) {
                  break;
               }
            case 7:
               this._RuntimeMBeanServerEnabled = true;
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
      return "JMX";
   }

   public void putValue(String var1, Object var2) {
      boolean var3;
      if (var1.equals("CompatibilityMBeanServerEnabled")) {
         var3 = this._CompatibilityMBeanServerEnabled;
         this._CompatibilityMBeanServerEnabled = (Boolean)var2;
         this._postSet(10, var3, this._CompatibilityMBeanServerEnabled);
      } else if (var1.equals("DomainMBeanServerEnabled")) {
         var3 = this._DomainMBeanServerEnabled;
         this._DomainMBeanServerEnabled = (Boolean)var2;
         this._postSet(8, var3, this._DomainMBeanServerEnabled);
      } else if (var1.equals("EditMBeanServerEnabled")) {
         var3 = this._EditMBeanServerEnabled;
         this._EditMBeanServerEnabled = (Boolean)var2;
         this._postSet(9, var3, this._EditMBeanServerEnabled);
      } else if (var1.equals("InvocationTimeoutSeconds")) {
         int var4 = this._InvocationTimeoutSeconds;
         this._InvocationTimeoutSeconds = (Integer)var2;
         this._postSet(13, var4, this._InvocationTimeoutSeconds);
      } else if (var1.equals("ManagementEJBEnabled")) {
         var3 = this._ManagementEJBEnabled;
         this._ManagementEJBEnabled = (Boolean)var2;
         this._postSet(11, var3, this._ManagementEJBEnabled);
      } else if (var1.equals("PlatformMBeanServerEnabled")) {
         var3 = this._PlatformMBeanServerEnabled;
         this._PlatformMBeanServerEnabled = (Boolean)var2;
         this._postSet(12, var3, this._PlatformMBeanServerEnabled);
      } else if (var1.equals("PlatformMBeanServerUsed")) {
         var3 = this._PlatformMBeanServerUsed;
         this._PlatformMBeanServerUsed = (Boolean)var2;
         this._postSet(14, var3, this._PlatformMBeanServerUsed);
      } else if (var1.equals("RuntimeMBeanServerEnabled")) {
         var3 = this._RuntimeMBeanServerEnabled;
         this._RuntimeMBeanServerEnabled = (Boolean)var2;
         this._postSet(7, var3, this._RuntimeMBeanServerEnabled);
      } else {
         super.putValue(var1, var2);
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("CompatibilityMBeanServerEnabled")) {
         return new Boolean(this._CompatibilityMBeanServerEnabled);
      } else if (var1.equals("DomainMBeanServerEnabled")) {
         return new Boolean(this._DomainMBeanServerEnabled);
      } else if (var1.equals("EditMBeanServerEnabled")) {
         return new Boolean(this._EditMBeanServerEnabled);
      } else if (var1.equals("InvocationTimeoutSeconds")) {
         return new Integer(this._InvocationTimeoutSeconds);
      } else if (var1.equals("ManagementEJBEnabled")) {
         return new Boolean(this._ManagementEJBEnabled);
      } else if (var1.equals("PlatformMBeanServerEnabled")) {
         return new Boolean(this._PlatformMBeanServerEnabled);
      } else if (var1.equals("PlatformMBeanServerUsed")) {
         return new Boolean(this._PlatformMBeanServerUsed);
      } else {
         return var1.equals("RuntimeMBeanServerEnabled") ? new Boolean(this._RuntimeMBeanServerEnabled) : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends ConfigurationMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 21:
               if (var1.equals("managementejb-enabled")) {
                  return 11;
               }
            case 22:
            case 23:
            case 24:
            case 30:
            case 31:
            case 32:
            case 33:
            default:
               break;
            case 25:
               if (var1.equals("editm-bean-server-enabled")) {
                  return 9;
               }
               break;
            case 26:
               if (var1.equals("invocation-timeout-seconds")) {
                  return 13;
               }

               if (var1.equals("platformm-bean-server-used")) {
                  return 14;
               }
               break;
            case 27:
               if (var1.equals("domainm-bean-server-enabled")) {
                  return 8;
               }
               break;
            case 28:
               if (var1.equals("runtimem-bean-server-enabled")) {
                  return 7;
               }
               break;
            case 29:
               if (var1.equals("platformm-bean-server-enabled")) {
                  return 12;
               }
               break;
            case 34:
               if (var1.equals("compatibilitym-bean-server-enabled")) {
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
               return "runtimem-bean-server-enabled";
            case 8:
               return "domainm-bean-server-enabled";
            case 9:
               return "editm-bean-server-enabled";
            case 10:
               return "compatibilitym-bean-server-enabled";
            case 11:
               return "managementejb-enabled";
            case 12:
               return "platformm-bean-server-enabled";
            case 13:
               return "invocation-timeout-seconds";
            case 14:
               return "platformm-bean-server-used";
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
      private JMXMBeanImpl bean;

      protected Helper(JMXMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 7:
               return "RuntimeMBeanServerEnabled";
            case 8:
               return "DomainMBeanServerEnabled";
            case 9:
               return "EditMBeanServerEnabled";
            case 10:
               return "CompatibilityMBeanServerEnabled";
            case 11:
               return "ManagementEJBEnabled";
            case 12:
               return "PlatformMBeanServerEnabled";
            case 13:
               return "InvocationTimeoutSeconds";
            case 14:
               return "PlatformMBeanServerUsed";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("InvocationTimeoutSeconds")) {
            return 13;
         } else if (var1.equals("CompatibilityMBeanServerEnabled")) {
            return 10;
         } else if (var1.equals("DomainMBeanServerEnabled")) {
            return 8;
         } else if (var1.equals("EditMBeanServerEnabled")) {
            return 9;
         } else if (var1.equals("ManagementEJBEnabled")) {
            return 11;
         } else if (var1.equals("PlatformMBeanServerEnabled")) {
            return 12;
         } else if (var1.equals("PlatformMBeanServerUsed")) {
            return 14;
         } else {
            return var1.equals("RuntimeMBeanServerEnabled") ? 7 : super.getPropertyIndex(var1);
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
            if (this.bean.isInvocationTimeoutSecondsSet()) {
               var2.append("InvocationTimeoutSeconds");
               var2.append(String.valueOf(this.bean.getInvocationTimeoutSeconds()));
            }

            if (this.bean.isCompatibilityMBeanServerEnabledSet()) {
               var2.append("CompatibilityMBeanServerEnabled");
               var2.append(String.valueOf(this.bean.isCompatibilityMBeanServerEnabled()));
            }

            if (this.bean.isDomainMBeanServerEnabledSet()) {
               var2.append("DomainMBeanServerEnabled");
               var2.append(String.valueOf(this.bean.isDomainMBeanServerEnabled()));
            }

            if (this.bean.isEditMBeanServerEnabledSet()) {
               var2.append("EditMBeanServerEnabled");
               var2.append(String.valueOf(this.bean.isEditMBeanServerEnabled()));
            }

            if (this.bean.isManagementEJBEnabledSet()) {
               var2.append("ManagementEJBEnabled");
               var2.append(String.valueOf(this.bean.isManagementEJBEnabled()));
            }

            if (this.bean.isPlatformMBeanServerEnabledSet()) {
               var2.append("PlatformMBeanServerEnabled");
               var2.append(String.valueOf(this.bean.isPlatformMBeanServerEnabled()));
            }

            if (this.bean.isPlatformMBeanServerUsedSet()) {
               var2.append("PlatformMBeanServerUsed");
               var2.append(String.valueOf(this.bean.isPlatformMBeanServerUsed()));
            }

            if (this.bean.isRuntimeMBeanServerEnabledSet()) {
               var2.append("RuntimeMBeanServerEnabled");
               var2.append(String.valueOf(this.bean.isRuntimeMBeanServerEnabled()));
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
            JMXMBeanImpl var2 = (JMXMBeanImpl)var1;
            this.computeDiff("InvocationTimeoutSeconds", this.bean.getInvocationTimeoutSeconds(), var2.getInvocationTimeoutSeconds(), false);
            this.computeDiff("CompatibilityMBeanServerEnabled", this.bean.isCompatibilityMBeanServerEnabled(), var2.isCompatibilityMBeanServerEnabled(), true);
            this.computeDiff("DomainMBeanServerEnabled", this.bean.isDomainMBeanServerEnabled(), var2.isDomainMBeanServerEnabled(), true);
            this.computeDiff("EditMBeanServerEnabled", this.bean.isEditMBeanServerEnabled(), var2.isEditMBeanServerEnabled(), true);
            this.computeDiff("ManagementEJBEnabled", this.bean.isManagementEJBEnabled(), var2.isManagementEJBEnabled(), true);
            this.computeDiff("PlatformMBeanServerEnabled", this.bean.isPlatformMBeanServerEnabled(), var2.isPlatformMBeanServerEnabled(), false);
            this.computeDiff("PlatformMBeanServerUsed", this.bean.isPlatformMBeanServerUsed(), var2.isPlatformMBeanServerUsed(), false);
            this.computeDiff("RuntimeMBeanServerEnabled", this.bean.isRuntimeMBeanServerEnabled(), var2.isRuntimeMBeanServerEnabled(), true);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            JMXMBeanImpl var3 = (JMXMBeanImpl)var1.getSourceBean();
            JMXMBeanImpl var4 = (JMXMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("InvocationTimeoutSeconds")) {
                  var3.setInvocationTimeoutSeconds(var4.getInvocationTimeoutSeconds());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 13);
               } else if (var5.equals("CompatibilityMBeanServerEnabled")) {
                  var3.setCompatibilityMBeanServerEnabled(var4.isCompatibilityMBeanServerEnabled());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 10);
               } else if (var5.equals("DomainMBeanServerEnabled")) {
                  var3.setDomainMBeanServerEnabled(var4.isDomainMBeanServerEnabled());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 8);
               } else if (var5.equals("EditMBeanServerEnabled")) {
                  var3.setEditMBeanServerEnabled(var4.isEditMBeanServerEnabled());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 9);
               } else if (var5.equals("ManagementEJBEnabled")) {
                  var3.setManagementEJBEnabled(var4.isManagementEJBEnabled());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 11);
               } else if (var5.equals("PlatformMBeanServerEnabled")) {
                  var3.setPlatformMBeanServerEnabled(var4.isPlatformMBeanServerEnabled());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 12);
               } else if (var5.equals("PlatformMBeanServerUsed")) {
                  var3.setPlatformMBeanServerUsed(var4.isPlatformMBeanServerUsed());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 14);
               } else if (var5.equals("RuntimeMBeanServerEnabled")) {
                  var3.setRuntimeMBeanServerEnabled(var4.isRuntimeMBeanServerEnabled());
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
            JMXMBeanImpl var5 = (JMXMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("InvocationTimeoutSeconds")) && this.bean.isInvocationTimeoutSecondsSet()) {
               var5.setInvocationTimeoutSeconds(this.bean.getInvocationTimeoutSeconds());
            }

            if ((var3 == null || !var3.contains("CompatibilityMBeanServerEnabled")) && this.bean.isCompatibilityMBeanServerEnabledSet()) {
               var5.setCompatibilityMBeanServerEnabled(this.bean.isCompatibilityMBeanServerEnabled());
            }

            if ((var3 == null || !var3.contains("DomainMBeanServerEnabled")) && this.bean.isDomainMBeanServerEnabledSet()) {
               var5.setDomainMBeanServerEnabled(this.bean.isDomainMBeanServerEnabled());
            }

            if ((var3 == null || !var3.contains("EditMBeanServerEnabled")) && this.bean.isEditMBeanServerEnabledSet()) {
               var5.setEditMBeanServerEnabled(this.bean.isEditMBeanServerEnabled());
            }

            if ((var3 == null || !var3.contains("ManagementEJBEnabled")) && this.bean.isManagementEJBEnabledSet()) {
               var5.setManagementEJBEnabled(this.bean.isManagementEJBEnabled());
            }

            if ((var3 == null || !var3.contains("PlatformMBeanServerEnabled")) && this.bean.isPlatformMBeanServerEnabledSet()) {
               var5.setPlatformMBeanServerEnabled(this.bean.isPlatformMBeanServerEnabled());
            }

            if ((var3 == null || !var3.contains("PlatformMBeanServerUsed")) && this.bean.isPlatformMBeanServerUsedSet()) {
               var5.setPlatformMBeanServerUsed(this.bean.isPlatformMBeanServerUsed());
            }

            if ((var3 == null || !var3.contains("RuntimeMBeanServerEnabled")) && this.bean.isRuntimeMBeanServerEnabledSet()) {
               var5.setRuntimeMBeanServerEnabled(this.bean.isRuntimeMBeanServerEnabled());
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
