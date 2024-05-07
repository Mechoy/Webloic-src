package weblogic.management.configuration;

import java.io.Serializable;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.CRC32;
import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanAlreadyExistsException;
import weblogic.descriptor.BeanRemoveRejectedException;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBeanHelper;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.utils.collections.ArrayIterator;
import weblogic.utils.collections.CombinedIterator;

public class OverloadProtectionMBeanImpl extends ConfigurationMBeanImpl implements OverloadProtectionMBean, Serializable {
   private String _FailureAction;
   private int _FreeMemoryPercentHighThreshold;
   private int _FreeMemoryPercentLowThreshold;
   private String _PanicAction;
   private ServerFailureTriggerMBean _ServerFailureTrigger;
   private int _SharedCapacityForWorkManagers;
   private static SchemaHelper2 _schemaHelper;

   public OverloadProtectionMBeanImpl() {
      this._initializeProperty(-1);
   }

   public OverloadProtectionMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public void setSharedCapacityForWorkManagers(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("SharedCapacityForWorkManagers", (long)var1, 1L, 1073741824L);
      int var2 = this._SharedCapacityForWorkManagers;
      this._SharedCapacityForWorkManagers = var1;
      this._postSet(7, var2, var1);
   }

   public int getSharedCapacityForWorkManagers() {
      if (!this._isSet(7)) {
         try {
            return this.getParent() instanceof ServerMBean ? ((ServerMBean)this.getParent()).getCluster().getOverloadProtection().getSharedCapacityForWorkManagers() : 65536;
         } catch (NullPointerException var2) {
         }
      }

      return this._SharedCapacityForWorkManagers;
   }

   public boolean isSharedCapacityForWorkManagersSet() {
      return this._isSet(7);
   }

   public void setPanicAction(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"no-action", "system-exit"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("PanicAction", var1, var2);
      String var3 = this._PanicAction;
      this._PanicAction = var1;
      this._postSet(8, var3, var1);
   }

   public String getPanicAction() {
      if (!this._isSet(8)) {
         try {
            return this.getParent() instanceof ServerMBean ? ((ServerMBean)this.getParent()).getCluster().getOverloadProtection().getPanicAction() : "no-action";
         } catch (NullPointerException var2) {
         }
      }

      return this._PanicAction;
   }

   public boolean isPanicActionSet() {
      return this._isSet(8);
   }

   public void setFailureAction(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"no-action", "force-shutdown", "admin-state"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("FailureAction", var1, var2);
      String var3 = this._FailureAction;
      this._FailureAction = var1;
      this._postSet(9, var3, var1);
   }

   public String getFailureAction() {
      if (!this._isSet(9)) {
         try {
            return this.getParent() instanceof ServerMBean ? ((ServerMBean)this.getParent()).getCluster().getOverloadProtection().getFailureAction() : "no-action";
         } catch (NullPointerException var2) {
         }
      }

      return this._FailureAction;
   }

   public boolean isFailureActionSet() {
      return this._isSet(9);
   }

   public void setFreeMemoryPercentHighThreshold(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("FreeMemoryPercentHighThreshold", (long)var1, 0L, 99L);
      int var2 = this._FreeMemoryPercentHighThreshold;
      this._FreeMemoryPercentHighThreshold = var1;
      this._postSet(10, var2, var1);
   }

   public int getFreeMemoryPercentHighThreshold() {
      if (!this._isSet(10)) {
         try {
            return this.getParent() instanceof ServerMBean ? ((ServerMBean)this.getParent()).getCluster().getOverloadProtection().getFreeMemoryPercentHighThreshold() : 0;
         } catch (NullPointerException var2) {
         }
      }

      return this._FreeMemoryPercentHighThreshold;
   }

   public boolean isFreeMemoryPercentHighThresholdSet() {
      return this._isSet(10);
   }

   public void setFreeMemoryPercentLowThreshold(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("FreeMemoryPercentLowThreshold", (long)var1, 0L, 99L);
      int var2 = this._FreeMemoryPercentLowThreshold;
      this._FreeMemoryPercentLowThreshold = var1;
      this._postSet(11, var2, var1);
   }

   public int getFreeMemoryPercentLowThreshold() {
      if (!this._isSet(11)) {
         try {
            return this.getParent() instanceof ServerMBean ? ((ServerMBean)this.getParent()).getCluster().getOverloadProtection().getFreeMemoryPercentLowThreshold() : 0;
         } catch (NullPointerException var2) {
         }
      }

      return this._FreeMemoryPercentLowThreshold;
   }

   public boolean isFreeMemoryPercentLowThresholdSet() {
      return this._isSet(11);
   }

   public ServerFailureTriggerMBean getServerFailureTrigger() {
      if (!this._isSet(12)) {
         try {
            return this.getParent() instanceof ServerMBean ? ((ServerMBean)this.getParent()).getCluster().getOverloadProtection().getServerFailureTrigger() : null;
         } catch (NullPointerException var2) {
         }
      }

      return this._ServerFailureTrigger;
   }

   public boolean isServerFailureTriggerSet() {
      return this._isSet(12);
   }

   public void setServerFailureTrigger(ServerFailureTriggerMBean var1) throws InvalidAttributeValueException {
      if (var1 != null && this.getServerFailureTrigger() != null && var1 != this.getServerFailureTrigger()) {
         throw new BeanAlreadyExistsException(this.getServerFailureTrigger() + " has already been created");
      } else {
         if (var1 != null) {
            AbstractDescriptorBean var2 = (AbstractDescriptorBean)var1;
            if (this._setParent(var2, this, 12)) {
               this._getReferenceManager().registerBean(var2, false);
               this._postCreate(var2);
            }
         }

         ServerFailureTriggerMBean var3 = this._ServerFailureTrigger;
         this._ServerFailureTrigger = var1;
         this._postSet(12, var3, var1);
      }
   }

   public ServerFailureTriggerMBean createServerFailureTrigger() {
      ServerFailureTriggerMBeanImpl var1 = new ServerFailureTriggerMBeanImpl(this, -1);

      try {
         this.setServerFailureTrigger(var1);
         return var1;
      } catch (Exception var3) {
         if (var3 instanceof RuntimeException) {
            throw (RuntimeException)var3;
         } else {
            throw new UndeclaredThrowableException(var3);
         }
      }
   }

   public void destroyServerFailureTrigger() {
      try {
         AbstractDescriptorBean var1 = (AbstractDescriptorBean)this._ServerFailureTrigger;
         if (var1 != null) {
            List var2 = this._getReferenceManager().getResolvedReferences(var1);
            if (var2 != null && var2.size() > 0) {
               throw new BeanRemoveRejectedException(var1, var2);
            } else {
               this._getReferenceManager().unregisterBean(var1);
               this._markDestroyed(var1);
               this.setServerFailureTrigger((ServerFailureTriggerMBean)null);
               this._unSet(12);
            }
         }
      } catch (Exception var3) {
         if (var3 instanceof RuntimeException) {
            throw (RuntimeException)var3;
         } else {
            throw new UndeclaredThrowableException(var3);
         }
      }
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
         var1 = 9;
      }

      try {
         switch (var1) {
            case 9:
               this._FailureAction = "no-action";
               if (var2) {
                  break;
               }
            case 10:
               this._FreeMemoryPercentHighThreshold = 0;
               if (var2) {
                  break;
               }
            case 11:
               this._FreeMemoryPercentLowThreshold = 0;
               if (var2) {
                  break;
               }
            case 8:
               this._PanicAction = "no-action";
               if (var2) {
                  break;
               }
            case 12:
               this._ServerFailureTrigger = null;
               if (var2) {
                  break;
               }
            case 7:
               this._SharedCapacityForWorkManagers = 65536;
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
      return "OverloadProtection";
   }

   public void putValue(String var1, Object var2) {
      String var5;
      if (var1.equals("FailureAction")) {
         var5 = this._FailureAction;
         this._FailureAction = (String)var2;
         this._postSet(9, var5, this._FailureAction);
      } else {
         int var3;
         if (var1.equals("FreeMemoryPercentHighThreshold")) {
            var3 = this._FreeMemoryPercentHighThreshold;
            this._FreeMemoryPercentHighThreshold = (Integer)var2;
            this._postSet(10, var3, this._FreeMemoryPercentHighThreshold);
         } else if (var1.equals("FreeMemoryPercentLowThreshold")) {
            var3 = this._FreeMemoryPercentLowThreshold;
            this._FreeMemoryPercentLowThreshold = (Integer)var2;
            this._postSet(11, var3, this._FreeMemoryPercentLowThreshold);
         } else if (var1.equals("PanicAction")) {
            var5 = this._PanicAction;
            this._PanicAction = (String)var2;
            this._postSet(8, var5, this._PanicAction);
         } else if (var1.equals("ServerFailureTrigger")) {
            ServerFailureTriggerMBean var4 = this._ServerFailureTrigger;
            this._ServerFailureTrigger = (ServerFailureTriggerMBean)var2;
            this._postSet(12, var4, this._ServerFailureTrigger);
         } else if (var1.equals("SharedCapacityForWorkManagers")) {
            var3 = this._SharedCapacityForWorkManagers;
            this._SharedCapacityForWorkManagers = (Integer)var2;
            this._postSet(7, var3, this._SharedCapacityForWorkManagers);
         } else {
            super.putValue(var1, var2);
         }
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("FailureAction")) {
         return this._FailureAction;
      } else if (var1.equals("FreeMemoryPercentHighThreshold")) {
         return new Integer(this._FreeMemoryPercentHighThreshold);
      } else if (var1.equals("FreeMemoryPercentLowThreshold")) {
         return new Integer(this._FreeMemoryPercentLowThreshold);
      } else if (var1.equals("PanicAction")) {
         return this._PanicAction;
      } else if (var1.equals("ServerFailureTrigger")) {
         return this._ServerFailureTrigger;
      } else {
         return var1.equals("SharedCapacityForWorkManagers") ? new Integer(this._SharedCapacityForWorkManagers) : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends ConfigurationMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 12:
               if (var1.equals("panic-action")) {
                  return 8;
               }
               break;
            case 14:
               if (var1.equals("failure-action")) {
                  return 9;
               }
               break;
            case 22:
               if (var1.equals("server-failure-trigger")) {
                  return 12;
               }
               break;
            case 33:
               if (var1.equals("free-memory-percent-low-threshold")) {
                  return 11;
               }

               if (var1.equals("shared-capacity-for-work-managers")) {
                  return 7;
               }
               break;
            case 34:
               if (var1.equals("free-memory-percent-high-threshold")) {
                  return 10;
               }
         }

         return super.getPropertyIndex(var1);
      }

      public SchemaHelper getSchemaHelper(int var1) {
         switch (var1) {
            case 12:
               return new ServerFailureTriggerMBeanImpl.SchemaHelper2();
            default:
               return super.getSchemaHelper(var1);
         }
      }

      public String getElementName(int var1) {
         switch (var1) {
            case 7:
               return "shared-capacity-for-work-managers";
            case 8:
               return "panic-action";
            case 9:
               return "failure-action";
            case 10:
               return "free-memory-percent-high-threshold";
            case 11:
               return "free-memory-percent-low-threshold";
            case 12:
               return "server-failure-trigger";
            default:
               return super.getElementName(var1);
         }
      }

      public boolean isBean(int var1) {
         switch (var1) {
            case 12:
               return true;
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

   protected static class Helper extends ConfigurationMBeanImpl.Helper {
      private OverloadProtectionMBeanImpl bean;

      protected Helper(OverloadProtectionMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 7:
               return "SharedCapacityForWorkManagers";
            case 8:
               return "PanicAction";
            case 9:
               return "FailureAction";
            case 10:
               return "FreeMemoryPercentHighThreshold";
            case 11:
               return "FreeMemoryPercentLowThreshold";
            case 12:
               return "ServerFailureTrigger";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("FailureAction")) {
            return 9;
         } else if (var1.equals("FreeMemoryPercentHighThreshold")) {
            return 10;
         } else if (var1.equals("FreeMemoryPercentLowThreshold")) {
            return 11;
         } else if (var1.equals("PanicAction")) {
            return 8;
         } else if (var1.equals("ServerFailureTrigger")) {
            return 12;
         } else {
            return var1.equals("SharedCapacityForWorkManagers") ? 7 : super.getPropertyIndex(var1);
         }
      }

      public Iterator getChildren() {
         ArrayList var1 = new ArrayList();
         if (this.bean.getServerFailureTrigger() != null) {
            var1.add(new ArrayIterator(new ServerFailureTriggerMBean[]{this.bean.getServerFailureTrigger()}));
         }

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
            if (this.bean.isFailureActionSet()) {
               var2.append("FailureAction");
               var2.append(String.valueOf(this.bean.getFailureAction()));
            }

            if (this.bean.isFreeMemoryPercentHighThresholdSet()) {
               var2.append("FreeMemoryPercentHighThreshold");
               var2.append(String.valueOf(this.bean.getFreeMemoryPercentHighThreshold()));
            }

            if (this.bean.isFreeMemoryPercentLowThresholdSet()) {
               var2.append("FreeMemoryPercentLowThreshold");
               var2.append(String.valueOf(this.bean.getFreeMemoryPercentLowThreshold()));
            }

            if (this.bean.isPanicActionSet()) {
               var2.append("PanicAction");
               var2.append(String.valueOf(this.bean.getPanicAction()));
            }

            var5 = this.computeChildHashValue(this.bean.getServerFailureTrigger());
            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            if (this.bean.isSharedCapacityForWorkManagersSet()) {
               var2.append("SharedCapacityForWorkManagers");
               var2.append(String.valueOf(this.bean.getSharedCapacityForWorkManagers()));
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
            OverloadProtectionMBeanImpl var2 = (OverloadProtectionMBeanImpl)var1;
            this.computeDiff("FailureAction", this.bean.getFailureAction(), var2.getFailureAction(), true);
            this.computeDiff("FreeMemoryPercentHighThreshold", this.bean.getFreeMemoryPercentHighThreshold(), var2.getFreeMemoryPercentHighThreshold(), false);
            this.computeDiff("FreeMemoryPercentLowThreshold", this.bean.getFreeMemoryPercentLowThreshold(), var2.getFreeMemoryPercentLowThreshold(), false);
            this.computeDiff("PanicAction", this.bean.getPanicAction(), var2.getPanicAction(), false);
            this.computeChildDiff("ServerFailureTrigger", this.bean.getServerFailureTrigger(), var2.getServerFailureTrigger(), false);
            this.computeDiff("SharedCapacityForWorkManagers", this.bean.getSharedCapacityForWorkManagers(), var2.getSharedCapacityForWorkManagers(), false);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            OverloadProtectionMBeanImpl var3 = (OverloadProtectionMBeanImpl)var1.getSourceBean();
            OverloadProtectionMBeanImpl var4 = (OverloadProtectionMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("FailureAction")) {
                  var3.setFailureAction(var4.getFailureAction());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 9);
               } else if (var5.equals("FreeMemoryPercentHighThreshold")) {
                  var3.setFreeMemoryPercentHighThreshold(var4.getFreeMemoryPercentHighThreshold());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 10);
               } else if (var5.equals("FreeMemoryPercentLowThreshold")) {
                  var3.setFreeMemoryPercentLowThreshold(var4.getFreeMemoryPercentLowThreshold());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 11);
               } else if (var5.equals("PanicAction")) {
                  var3.setPanicAction(var4.getPanicAction());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 8);
               } else if (var5.equals("ServerFailureTrigger")) {
                  if (var6 == 2) {
                     var3.setServerFailureTrigger((ServerFailureTriggerMBean)this.createCopy((AbstractDescriptorBean)var4.getServerFailureTrigger()));
                  } else {
                     if (var6 != 3) {
                        throw new AssertionError("Invalid type: " + var6);
                     }

                     var3._destroySingleton("ServerFailureTrigger", var3.getServerFailureTrigger());
                  }

                  var3._conditionalUnset(var2.isUnsetUpdate(), 12);
               } else if (var5.equals("SharedCapacityForWorkManagers")) {
                  var3.setSharedCapacityForWorkManagers(var4.getSharedCapacityForWorkManagers());
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
            OverloadProtectionMBeanImpl var5 = (OverloadProtectionMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("FailureAction")) && this.bean.isFailureActionSet()) {
               var5.setFailureAction(this.bean.getFailureAction());
            }

            if ((var3 == null || !var3.contains("FreeMemoryPercentHighThreshold")) && this.bean.isFreeMemoryPercentHighThresholdSet()) {
               var5.setFreeMemoryPercentHighThreshold(this.bean.getFreeMemoryPercentHighThreshold());
            }

            if ((var3 == null || !var3.contains("FreeMemoryPercentLowThreshold")) && this.bean.isFreeMemoryPercentLowThresholdSet()) {
               var5.setFreeMemoryPercentLowThreshold(this.bean.getFreeMemoryPercentLowThreshold());
            }

            if ((var3 == null || !var3.contains("PanicAction")) && this.bean.isPanicActionSet()) {
               var5.setPanicAction(this.bean.getPanicAction());
            }

            if ((var3 == null || !var3.contains("ServerFailureTrigger")) && this.bean.isServerFailureTriggerSet() && !var5._isSet(12)) {
               ServerFailureTriggerMBean var4 = this.bean.getServerFailureTrigger();
               var5.setServerFailureTrigger((ServerFailureTriggerMBean)null);
               var5.setServerFailureTrigger(var4 == null ? null : (ServerFailureTriggerMBean)this.createCopy((AbstractDescriptorBean)var4, var2));
            }

            if ((var3 == null || !var3.contains("SharedCapacityForWorkManagers")) && this.bean.isSharedCapacityForWorkManagersSet()) {
               var5.setSharedCapacityForWorkManagers(this.bean.getSharedCapacityForWorkManagers());
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
         this.inferSubTree(this.bean.getServerFailureTrigger(), var1, var2);
      }
   }
}
