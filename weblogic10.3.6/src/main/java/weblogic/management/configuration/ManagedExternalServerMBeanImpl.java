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
import weblogic.descriptor.internal.ReferenceManager;
import weblogic.descriptor.internal.ResolvedReference;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.management.DistributedManagementException;
import weblogic.management.ManagementException;
import weblogic.utils.collections.CombinedIterator;

public class ManagedExternalServerMBeanImpl extends ConfigurationMBeanImpl implements ManagedExternalServerMBean, Serializable {
   private boolean _AutoRestart;
   private MachineMBean _Machine;
   private ManagedExternalServerStartMBean _ManagedExternalServerStart;
   private int _NMSocketCreateTimeoutInMillis;
   private String _Name;
   private int _RestartDelaySeconds;
   private int _RestartIntervalSeconds;
   private int _RestartMax;
   private static SchemaHelper2 _schemaHelper;

   public ManagedExternalServerMBeanImpl() {
      this._initializeProperty(-1);
   }

   public ManagedExternalServerMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public String getName() {
      return this._Name;
   }

   public boolean isNameSet() {
      return this._isSet(2);
   }

   public void setName(String var1) throws InvalidAttributeValueException, ManagementException {
      var1 = var1 == null ? null : var1.trim();
      ConfigurationValidator.validateName(var1);
      String var2 = this._Name;
      this._Name = var1;
      this._postSet(2, var2, var1);
   }

   public MachineMBean getMachine() {
      return this._Machine;
   }

   public String getMachineAsString() {
      AbstractDescriptorBean var1 = (AbstractDescriptorBean)this.getMachine();
      return var1 == null ? null : var1._getKey().toString();
   }

   public boolean isMachineSet() {
      return this._isSet(7);
   }

   public void setMachineAsString(String var1) {
      if (var1 != null && var1.length() != 0) {
         var1 = var1 == null ? null : var1.trim();
         this._getReferenceManager().registerUnresolvedReference(var1, MachineMBean.class, new ReferenceManager.Resolver(this, 7) {
            public void resolveReference(Object var1) {
               try {
                  ManagedExternalServerMBeanImpl.this.setMachine((MachineMBean)var1);
               } catch (RuntimeException var3) {
                  throw var3;
               } catch (Exception var4) {
                  throw new AssertionError("Impossible exception: " + var4);
               }
            }
         });
      } else {
         MachineMBean var2 = this._Machine;
         this._initializeProperty(7);
         this._postSet(7, var2, this._Machine);
      }

   }

   public void setMachine(MachineMBean var1) throws InvalidAttributeValueException {
      if (var1 != null) {
         ResolvedReference var2 = new ResolvedReference(this, 7, (AbstractDescriptorBean)var1) {
            protected Object getPropertyValue() {
               return ManagedExternalServerMBeanImpl.this.getMachine();
            }
         };
         this._getReferenceManager().registerResolvedReference((AbstractDescriptorBean)var1, var2);
      }

      MachineMBean var3 = this._Machine;
      this._Machine = var1;
      this._postSet(7, var3, var1);
   }

   public boolean getAutoRestart() {
      return this._AutoRestart;
   }

   public boolean isAutoRestartSet() {
      return this._isSet(8);
   }

   public void setAutoRestart(boolean var1) {
      boolean var2 = this._AutoRestart;
      this._AutoRestart = var1;
      this._postSet(8, var2, var1);
   }

   public int getRestartIntervalSeconds() {
      return this._RestartIntervalSeconds;
   }

   public boolean isRestartIntervalSecondsSet() {
      return this._isSet(9);
   }

   public void setRestartIntervalSeconds(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("RestartIntervalSeconds", (long)var1, 300L, 2147483647L);
      int var2 = this._RestartIntervalSeconds;
      this._RestartIntervalSeconds = var1;
      this._postSet(9, var2, var1);
   }

   public int getRestartMax() {
      return this._RestartMax;
   }

   public boolean isRestartMaxSet() {
      return this._isSet(10);
   }

   public void setRestartMax(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("RestartMax", (long)var1, 0L, 2147483647L);
      int var2 = this._RestartMax;
      this._RestartMax = var1;
      this._postSet(10, var2, var1);
   }

   public int getRestartDelaySeconds() {
      return this._RestartDelaySeconds;
   }

   public boolean isRestartDelaySecondsSet() {
      return this._isSet(11);
   }

   public void setRestartDelaySeconds(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("RestartDelaySeconds", (long)var1, 0L, 2147483647L);
      int var2 = this._RestartDelaySeconds;
      this._RestartDelaySeconds = var1;
      this._postSet(11, var2, var1);
   }

   public int getNMSocketCreateTimeoutInMillis() {
      return this._NMSocketCreateTimeoutInMillis;
   }

   public boolean isNMSocketCreateTimeoutInMillisSet() {
      return this._isSet(12);
   }

   public void setNMSocketCreateTimeoutInMillis(int var1) throws InvalidAttributeValueException, DistributedManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkMin("NMSocketCreateTimeoutInMillis", var1, 0);
      int var2 = this._NMSocketCreateTimeoutInMillis;
      this._NMSocketCreateTimeoutInMillis = var1;
      this._postSet(12, var2, var1);
   }

   public ManagedExternalServerStartMBean getManagedExternalServerStart() {
      return this._ManagedExternalServerStart;
   }

   public boolean isManagedExternalServerStartSet() {
      return this._isSet(13);
   }

   public void setManagedExternalServerStart(ManagedExternalServerStartMBean var1) throws InvalidAttributeValueException {
      this._ManagedExternalServerStart = var1;
   }

   public Object _getKey() {
      return this.getName();
   }

   public void _validate() throws IllegalArgumentException {
      super._validate();
   }

   public boolean _hasKey() {
      return true;
   }

   public boolean _isPropertyAKey(Munger.ReaderEventInfo var1) {
      String var2 = var1.getElementName();
      switch (var2.length()) {
         case 4:
            if (var2.equals("name")) {
               return var1.compareXpaths(this._getPropertyXpath("name"));
            }

            return super._isPropertyAKey(var1);
         default:
            return super._isPropertyAKey(var1);
      }
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
               this._AutoRestart = true;
               if (var2) {
                  break;
               }
            case 7:
               this._Machine = null;
               if (var2) {
                  break;
               }
            case 13:
               this._ManagedExternalServerStart = null;
               if (var2) {
                  break;
               }
            case 12:
               this._NMSocketCreateTimeoutInMillis = 180000;
               if (var2) {
                  break;
               }
            case 2:
               this._Name = null;
               if (var2) {
                  break;
               }
            case 11:
               this._RestartDelaySeconds = 0;
               if (var2) {
                  break;
               }
            case 9:
               this._RestartIntervalSeconds = 3600;
               if (var2) {
                  break;
               }
            case 10:
               this._RestartMax = 2;
               if (var2) {
                  break;
               }
            case 3:
            case 4:
            case 5:
            case 6:
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
      return "ManagedExternalServer";
   }

   public void putValue(String var1, Object var2) {
      if (var1.equals("AutoRestart")) {
         boolean var7 = this._AutoRestart;
         this._AutoRestart = (Boolean)var2;
         this._postSet(8, var7, this._AutoRestart);
      } else if (var1.equals("Machine")) {
         MachineMBean var6 = this._Machine;
         this._Machine = (MachineMBean)var2;
         this._postSet(7, var6, this._Machine);
      } else if (var1.equals("ManagedExternalServerStart")) {
         ManagedExternalServerStartMBean var5 = this._ManagedExternalServerStart;
         this._ManagedExternalServerStart = (ManagedExternalServerStartMBean)var2;
         this._postSet(13, var5, this._ManagedExternalServerStart);
      } else {
         int var3;
         if (var1.equals("NMSocketCreateTimeoutInMillis")) {
            var3 = this._NMSocketCreateTimeoutInMillis;
            this._NMSocketCreateTimeoutInMillis = (Integer)var2;
            this._postSet(12, var3, this._NMSocketCreateTimeoutInMillis);
         } else if (var1.equals("Name")) {
            String var4 = this._Name;
            this._Name = (String)var2;
            this._postSet(2, var4, this._Name);
         } else if (var1.equals("RestartDelaySeconds")) {
            var3 = this._RestartDelaySeconds;
            this._RestartDelaySeconds = (Integer)var2;
            this._postSet(11, var3, this._RestartDelaySeconds);
         } else if (var1.equals("RestartIntervalSeconds")) {
            var3 = this._RestartIntervalSeconds;
            this._RestartIntervalSeconds = (Integer)var2;
            this._postSet(9, var3, this._RestartIntervalSeconds);
         } else if (var1.equals("RestartMax")) {
            var3 = this._RestartMax;
            this._RestartMax = (Integer)var2;
            this._postSet(10, var3, this._RestartMax);
         } else {
            super.putValue(var1, var2);
         }
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("AutoRestart")) {
         return new Boolean(this._AutoRestart);
      } else if (var1.equals("Machine")) {
         return this._Machine;
      } else if (var1.equals("ManagedExternalServerStart")) {
         return this._ManagedExternalServerStart;
      } else if (var1.equals("NMSocketCreateTimeoutInMillis")) {
         return new Integer(this._NMSocketCreateTimeoutInMillis);
      } else if (var1.equals("Name")) {
         return this._Name;
      } else if (var1.equals("RestartDelaySeconds")) {
         return new Integer(this._RestartDelaySeconds);
      } else if (var1.equals("RestartIntervalSeconds")) {
         return new Integer(this._RestartIntervalSeconds);
      } else {
         return var1.equals("RestartMax") ? new Integer(this._RestartMax) : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends ConfigurationMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 4:
               if (var1.equals("name")) {
                  return 2;
               }
               break;
            case 7:
               if (var1.equals("machine")) {
                  return 7;
               }
               break;
            case 11:
               if (var1.equals("restart-max")) {
                  return 10;
               }
               break;
            case 12:
               if (var1.equals("auto-restart")) {
                  return 8;
               }
               break;
            case 21:
               if (var1.equals("restart-delay-seconds")) {
                  return 11;
               }
               break;
            case 24:
               if (var1.equals("restart-interval-seconds")) {
                  return 9;
               }
               break;
            case 29:
               if (var1.equals("managed-external-server-start")) {
                  return 13;
               }
               break;
            case 34:
               if (var1.equals("nm-socket-create-timeout-in-millis")) {
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
            case 2:
               return "name";
            case 3:
            case 4:
            case 5:
            case 6:
            default:
               return super.getElementName(var1);
            case 7:
               return "machine";
            case 8:
               return "auto-restart";
            case 9:
               return "restart-interval-seconds";
            case 10:
               return "restart-max";
            case 11:
               return "restart-delay-seconds";
            case 12:
               return "nm-socket-create-timeout-in-millis";
            case 13:
               return "managed-external-server-start";
         }
      }

      public boolean isBean(int var1) {
         switch (var1) {
            default:
               return super.isBean(var1);
         }
      }

      public boolean isConfigurable(int var1) {
         switch (var1) {
            case 7:
               return true;
            case 8:
               return true;
            case 9:
               return true;
            case 10:
               return true;
            case 11:
               return true;
            default:
               return super.isConfigurable(var1);
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

      public boolean hasKey() {
         return true;
      }

      public String[] getKeyElementNames() {
         ArrayList var1 = new ArrayList();
         var1.add("name");
         return (String[])((String[])var1.toArray(new String[0]));
      }
   }

   protected static class Helper extends ConfigurationMBeanImpl.Helper {
      private ManagedExternalServerMBeanImpl bean;

      protected Helper(ManagedExternalServerMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 2:
               return "Name";
            case 3:
            case 4:
            case 5:
            case 6:
            default:
               return super.getPropertyName(var1);
            case 7:
               return "Machine";
            case 8:
               return "AutoRestart";
            case 9:
               return "RestartIntervalSeconds";
            case 10:
               return "RestartMax";
            case 11:
               return "RestartDelaySeconds";
            case 12:
               return "NMSocketCreateTimeoutInMillis";
            case 13:
               return "ManagedExternalServerStart";
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("AutoRestart")) {
            return 8;
         } else if (var1.equals("Machine")) {
            return 7;
         } else if (var1.equals("ManagedExternalServerStart")) {
            return 13;
         } else if (var1.equals("NMSocketCreateTimeoutInMillis")) {
            return 12;
         } else if (var1.equals("Name")) {
            return 2;
         } else if (var1.equals("RestartDelaySeconds")) {
            return 11;
         } else if (var1.equals("RestartIntervalSeconds")) {
            return 9;
         } else {
            return var1.equals("RestartMax") ? 10 : super.getPropertyIndex(var1);
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
            if (this.bean.isAutoRestartSet()) {
               var2.append("AutoRestart");
               var2.append(String.valueOf(this.bean.getAutoRestart()));
            }

            if (this.bean.isMachineSet()) {
               var2.append("Machine");
               var2.append(String.valueOf(this.bean.getMachine()));
            }

            if (this.bean.isManagedExternalServerStartSet()) {
               var2.append("ManagedExternalServerStart");
               var2.append(String.valueOf(this.bean.getManagedExternalServerStart()));
            }

            if (this.bean.isNMSocketCreateTimeoutInMillisSet()) {
               var2.append("NMSocketCreateTimeoutInMillis");
               var2.append(String.valueOf(this.bean.getNMSocketCreateTimeoutInMillis()));
            }

            if (this.bean.isNameSet()) {
               var2.append("Name");
               var2.append(String.valueOf(this.bean.getName()));
            }

            if (this.bean.isRestartDelaySecondsSet()) {
               var2.append("RestartDelaySeconds");
               var2.append(String.valueOf(this.bean.getRestartDelaySeconds()));
            }

            if (this.bean.isRestartIntervalSecondsSet()) {
               var2.append("RestartIntervalSeconds");
               var2.append(String.valueOf(this.bean.getRestartIntervalSeconds()));
            }

            if (this.bean.isRestartMaxSet()) {
               var2.append("RestartMax");
               var2.append(String.valueOf(this.bean.getRestartMax()));
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
            ManagedExternalServerMBeanImpl var2 = (ManagedExternalServerMBeanImpl)var1;
            this.computeDiff("AutoRestart", this.bean.getAutoRestart(), var2.getAutoRestart(), true);
            this.computeDiff("Machine", this.bean.getMachine(), var2.getMachine(), false);
            this.computeDiff("NMSocketCreateTimeoutInMillis", this.bean.getNMSocketCreateTimeoutInMillis(), var2.getNMSocketCreateTimeoutInMillis(), false);
            this.computeDiff("Name", this.bean.getName(), var2.getName(), false);
            this.computeDiff("RestartDelaySeconds", this.bean.getRestartDelaySeconds(), var2.getRestartDelaySeconds(), true);
            this.computeDiff("RestartIntervalSeconds", this.bean.getRestartIntervalSeconds(), var2.getRestartIntervalSeconds(), true);
            this.computeDiff("RestartMax", this.bean.getRestartMax(), var2.getRestartMax(), true);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            ManagedExternalServerMBeanImpl var3 = (ManagedExternalServerMBeanImpl)var1.getSourceBean();
            ManagedExternalServerMBeanImpl var4 = (ManagedExternalServerMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("AutoRestart")) {
                  var3.setAutoRestart(var4.getAutoRestart());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 8);
               } else if (var5.equals("Machine")) {
                  var3.setMachineAsString(var4.getMachineAsString());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 7);
               } else if (!var5.equals("ManagedExternalServerStart")) {
                  if (var5.equals("NMSocketCreateTimeoutInMillis")) {
                     var3.setNMSocketCreateTimeoutInMillis(var4.getNMSocketCreateTimeoutInMillis());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 12);
                  } else if (var5.equals("Name")) {
                     var3.setName(var4.getName());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 2);
                  } else if (var5.equals("RestartDelaySeconds")) {
                     var3.setRestartDelaySeconds(var4.getRestartDelaySeconds());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 11);
                  } else if (var5.equals("RestartIntervalSeconds")) {
                     var3.setRestartIntervalSeconds(var4.getRestartIntervalSeconds());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 9);
                  } else if (var5.equals("RestartMax")) {
                     var3.setRestartMax(var4.getRestartMax());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 10);
                  } else {
                     super.applyPropertyUpdate(var1, var2);
                  }
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
            ManagedExternalServerMBeanImpl var5 = (ManagedExternalServerMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("AutoRestart")) && this.bean.isAutoRestartSet()) {
               var5.setAutoRestart(this.bean.getAutoRestart());
            }

            if ((var3 == null || !var3.contains("Machine")) && this.bean.isMachineSet()) {
               var5._unSet(var5, 7);
               var5.setMachineAsString(this.bean.getMachineAsString());
            }

            if ((var3 == null || !var3.contains("NMSocketCreateTimeoutInMillis")) && this.bean.isNMSocketCreateTimeoutInMillisSet()) {
               var5.setNMSocketCreateTimeoutInMillis(this.bean.getNMSocketCreateTimeoutInMillis());
            }

            if ((var3 == null || !var3.contains("Name")) && this.bean.isNameSet()) {
               var5.setName(this.bean.getName());
            }

            if ((var3 == null || !var3.contains("RestartDelaySeconds")) && this.bean.isRestartDelaySecondsSet()) {
               var5.setRestartDelaySeconds(this.bean.getRestartDelaySeconds());
            }

            if ((var3 == null || !var3.contains("RestartIntervalSeconds")) && this.bean.isRestartIntervalSecondsSet()) {
               var5.setRestartIntervalSeconds(this.bean.getRestartIntervalSeconds());
            }

            if ((var3 == null || !var3.contains("RestartMax")) && this.bean.isRestartMaxSet()) {
               var5.setRestartMax(this.bean.getRestartMax());
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
         this.inferSubTree(this.bean.getMachine(), var1, var2);
         this.inferSubTree(this.bean.getManagedExternalServerStart(), var1, var2);
      }
   }
}
