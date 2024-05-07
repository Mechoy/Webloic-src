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

public class DatabaseLessLeasingBasisMBeanImpl extends ConfigurationMBeanImpl implements DatabaseLessLeasingBasisMBean, Serializable {
   private int _FenceTimeout;
   private int _LeaderHeartbeatPeriod;
   private int _MemberDiscoveryTimeout;
   private int _MessageDeliveryTimeout;
   private int _NodeManagerTimeoutMillis;
   private boolean _PeriodicSRMCheckEnabled;
   private static SchemaHelper2 _schemaHelper;

   public DatabaseLessLeasingBasisMBeanImpl() {
      this._initializeProperty(-1);
   }

   public DatabaseLessLeasingBasisMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public void setMemberDiscoveryTimeout(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkMin("MemberDiscoveryTimeout", var1, 10);
      int var2 = this._MemberDiscoveryTimeout;
      this._MemberDiscoveryTimeout = var1;
      this._postSet(7, var2, var1);
   }

   public int getMemberDiscoveryTimeout() {
      return this._MemberDiscoveryTimeout;
   }

   public boolean isMemberDiscoveryTimeoutSet() {
      return this._isSet(7);
   }

   public void setLeaderHeartbeatPeriod(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkMin("LeaderHeartbeatPeriod", var1, 1);
      int var2 = this._LeaderHeartbeatPeriod;
      this._LeaderHeartbeatPeriod = var1;
      this._postSet(8, var2, var1);
   }

   public int getLeaderHeartbeatPeriod() {
      return this._LeaderHeartbeatPeriod;
   }

   public boolean isLeaderHeartbeatPeriodSet() {
      return this._isSet(8);
   }

   public void setMessageDeliveryTimeout(int var1) {
      int var2 = this._MessageDeliveryTimeout;
      this._MessageDeliveryTimeout = var1;
      this._postSet(9, var2, var1);
   }

   public int getMessageDeliveryTimeout() {
      return this._MessageDeliveryTimeout;
   }

   public boolean isMessageDeliveryTimeoutSet() {
      return this._isSet(9);
   }

   public void setFenceTimeout(int var1) {
      int var2 = this._FenceTimeout;
      this._FenceTimeout = var1;
      this._postSet(10, var2, var1);
   }

   public int getFenceTimeout() {
      return this._FenceTimeout;
   }

   public boolean isFenceTimeoutSet() {
      return this._isSet(10);
   }

   public boolean isPeriodicSRMCheckEnabled() {
      return this._PeriodicSRMCheckEnabled;
   }

   public boolean isPeriodicSRMCheckEnabledSet() {
      return this._isSet(11);
   }

   public void setPeriodicSRMCheckEnabled(boolean var1) {
      boolean var2 = this._PeriodicSRMCheckEnabled;
      this._PeriodicSRMCheckEnabled = var1;
      this._postSet(11, var2, var1);
   }

   public void setNodeManagerTimeoutMillis(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkMin("NodeManagerTimeoutMillis", var1, 0);
      int var2 = this._NodeManagerTimeoutMillis;
      this._NodeManagerTimeoutMillis = var1;
      this._postSet(12, var2, var1);
   }

   public int getNodeManagerTimeoutMillis() {
      return this._NodeManagerTimeoutMillis;
   }

   public boolean isNodeManagerTimeoutMillisSet() {
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
         var1 = 10;
      }

      try {
         switch (var1) {
            case 10:
               this._FenceTimeout = 5;
               if (var2) {
                  break;
               }
            case 8:
               this._LeaderHeartbeatPeriod = 10;
               if (var2) {
                  break;
               }
            case 7:
               this._MemberDiscoveryTimeout = 30;
               if (var2) {
                  break;
               }
            case 9:
               this._MessageDeliveryTimeout = 5000;
               if (var2) {
                  break;
               }
            case 12:
               this._NodeManagerTimeoutMillis = 180000;
               if (var2) {
                  break;
               }
            case 11:
               this._PeriodicSRMCheckEnabled = true;
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
      return "DatabaseLessLeasingBasis";
   }

   public void putValue(String var1, Object var2) {
      int var4;
      if (var1.equals("FenceTimeout")) {
         var4 = this._FenceTimeout;
         this._FenceTimeout = (Integer)var2;
         this._postSet(10, var4, this._FenceTimeout);
      } else if (var1.equals("LeaderHeartbeatPeriod")) {
         var4 = this._LeaderHeartbeatPeriod;
         this._LeaderHeartbeatPeriod = (Integer)var2;
         this._postSet(8, var4, this._LeaderHeartbeatPeriod);
      } else if (var1.equals("MemberDiscoveryTimeout")) {
         var4 = this._MemberDiscoveryTimeout;
         this._MemberDiscoveryTimeout = (Integer)var2;
         this._postSet(7, var4, this._MemberDiscoveryTimeout);
      } else if (var1.equals("MessageDeliveryTimeout")) {
         var4 = this._MessageDeliveryTimeout;
         this._MessageDeliveryTimeout = (Integer)var2;
         this._postSet(9, var4, this._MessageDeliveryTimeout);
      } else if (var1.equals("NodeManagerTimeoutMillis")) {
         var4 = this._NodeManagerTimeoutMillis;
         this._NodeManagerTimeoutMillis = (Integer)var2;
         this._postSet(12, var4, this._NodeManagerTimeoutMillis);
      } else if (var1.equals("PeriodicSRMCheckEnabled")) {
         boolean var3 = this._PeriodicSRMCheckEnabled;
         this._PeriodicSRMCheckEnabled = (Boolean)var2;
         this._postSet(11, var3, this._PeriodicSRMCheckEnabled);
      } else {
         super.putValue(var1, var2);
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("FenceTimeout")) {
         return new Integer(this._FenceTimeout);
      } else if (var1.equals("LeaderHeartbeatPeriod")) {
         return new Integer(this._LeaderHeartbeatPeriod);
      } else if (var1.equals("MemberDiscoveryTimeout")) {
         return new Integer(this._MemberDiscoveryTimeout);
      } else if (var1.equals("MessageDeliveryTimeout")) {
         return new Integer(this._MessageDeliveryTimeout);
      } else if (var1.equals("NodeManagerTimeoutMillis")) {
         return new Integer(this._NodeManagerTimeoutMillis);
      } else {
         return var1.equals("PeriodicSRMCheckEnabled") ? new Boolean(this._PeriodicSRMCheckEnabled) : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends ConfigurationMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 13:
               if (var1.equals("fence-timeout")) {
                  return 10;
               }
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 26:
            default:
               break;
            case 23:
               if (var1.equals("leader-heartbeat-period")) {
                  return 8;
               }
               break;
            case 24:
               if (var1.equals("member-discovery-timeout")) {
                  return 7;
               }

               if (var1.equals("message-delivery-timeout")) {
                  return 9;
               }
               break;
            case 25:
               if (var1.equals("periodicsrm-check-enabled")) {
                  return 11;
               }
               break;
            case 27:
               if (var1.equals("node-manager-timeout-millis")) {
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
            case 7:
               return "member-discovery-timeout";
            case 8:
               return "leader-heartbeat-period";
            case 9:
               return "message-delivery-timeout";
            case 10:
               return "fence-timeout";
            case 11:
               return "periodicsrm-check-enabled";
            case 12:
               return "node-manager-timeout-millis";
            default:
               return super.getElementName(var1);
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
            case 12:
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

      public String[] getKeyElementNames() {
         ArrayList var1 = new ArrayList();
         var1.add("name");
         return (String[])((String[])var1.toArray(new String[0]));
      }
   }

   protected static class Helper extends ConfigurationMBeanImpl.Helper {
      private DatabaseLessLeasingBasisMBeanImpl bean;

      protected Helper(DatabaseLessLeasingBasisMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 7:
               return "MemberDiscoveryTimeout";
            case 8:
               return "LeaderHeartbeatPeriod";
            case 9:
               return "MessageDeliveryTimeout";
            case 10:
               return "FenceTimeout";
            case 11:
               return "PeriodicSRMCheckEnabled";
            case 12:
               return "NodeManagerTimeoutMillis";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("FenceTimeout")) {
            return 10;
         } else if (var1.equals("LeaderHeartbeatPeriod")) {
            return 8;
         } else if (var1.equals("MemberDiscoveryTimeout")) {
            return 7;
         } else if (var1.equals("MessageDeliveryTimeout")) {
            return 9;
         } else if (var1.equals("NodeManagerTimeoutMillis")) {
            return 12;
         } else {
            return var1.equals("PeriodicSRMCheckEnabled") ? 11 : super.getPropertyIndex(var1);
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
            if (this.bean.isFenceTimeoutSet()) {
               var2.append("FenceTimeout");
               var2.append(String.valueOf(this.bean.getFenceTimeout()));
            }

            if (this.bean.isLeaderHeartbeatPeriodSet()) {
               var2.append("LeaderHeartbeatPeriod");
               var2.append(String.valueOf(this.bean.getLeaderHeartbeatPeriod()));
            }

            if (this.bean.isMemberDiscoveryTimeoutSet()) {
               var2.append("MemberDiscoveryTimeout");
               var2.append(String.valueOf(this.bean.getMemberDiscoveryTimeout()));
            }

            if (this.bean.isMessageDeliveryTimeoutSet()) {
               var2.append("MessageDeliveryTimeout");
               var2.append(String.valueOf(this.bean.getMessageDeliveryTimeout()));
            }

            if (this.bean.isNodeManagerTimeoutMillisSet()) {
               var2.append("NodeManagerTimeoutMillis");
               var2.append(String.valueOf(this.bean.getNodeManagerTimeoutMillis()));
            }

            if (this.bean.isPeriodicSRMCheckEnabledSet()) {
               var2.append("PeriodicSRMCheckEnabled");
               var2.append(String.valueOf(this.bean.isPeriodicSRMCheckEnabled()));
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
            DatabaseLessLeasingBasisMBeanImpl var2 = (DatabaseLessLeasingBasisMBeanImpl)var1;
            this.computeDiff("FenceTimeout", this.bean.getFenceTimeout(), var2.getFenceTimeout(), true);
            this.computeDiff("LeaderHeartbeatPeriod", this.bean.getLeaderHeartbeatPeriod(), var2.getLeaderHeartbeatPeriod(), false);
            this.computeDiff("MemberDiscoveryTimeout", this.bean.getMemberDiscoveryTimeout(), var2.getMemberDiscoveryTimeout(), false);
            this.computeDiff("MessageDeliveryTimeout", this.bean.getMessageDeliveryTimeout(), var2.getMessageDeliveryTimeout(), true);
            this.computeDiff("NodeManagerTimeoutMillis", this.bean.getNodeManagerTimeoutMillis(), var2.getNodeManagerTimeoutMillis(), true);
            this.computeDiff("PeriodicSRMCheckEnabled", this.bean.isPeriodicSRMCheckEnabled(), var2.isPeriodicSRMCheckEnabled(), false);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            DatabaseLessLeasingBasisMBeanImpl var3 = (DatabaseLessLeasingBasisMBeanImpl)var1.getSourceBean();
            DatabaseLessLeasingBasisMBeanImpl var4 = (DatabaseLessLeasingBasisMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("FenceTimeout")) {
                  var3.setFenceTimeout(var4.getFenceTimeout());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 10);
               } else if (var5.equals("LeaderHeartbeatPeriod")) {
                  var3.setLeaderHeartbeatPeriod(var4.getLeaderHeartbeatPeriod());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 8);
               } else if (var5.equals("MemberDiscoveryTimeout")) {
                  var3.setMemberDiscoveryTimeout(var4.getMemberDiscoveryTimeout());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 7);
               } else if (var5.equals("MessageDeliveryTimeout")) {
                  var3.setMessageDeliveryTimeout(var4.getMessageDeliveryTimeout());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 9);
               } else if (var5.equals("NodeManagerTimeoutMillis")) {
                  var3.setNodeManagerTimeoutMillis(var4.getNodeManagerTimeoutMillis());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 12);
               } else if (var5.equals("PeriodicSRMCheckEnabled")) {
                  var3.setPeriodicSRMCheckEnabled(var4.isPeriodicSRMCheckEnabled());
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
            DatabaseLessLeasingBasisMBeanImpl var5 = (DatabaseLessLeasingBasisMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("FenceTimeout")) && this.bean.isFenceTimeoutSet()) {
               var5.setFenceTimeout(this.bean.getFenceTimeout());
            }

            if ((var3 == null || !var3.contains("LeaderHeartbeatPeriod")) && this.bean.isLeaderHeartbeatPeriodSet()) {
               var5.setLeaderHeartbeatPeriod(this.bean.getLeaderHeartbeatPeriod());
            }

            if ((var3 == null || !var3.contains("MemberDiscoveryTimeout")) && this.bean.isMemberDiscoveryTimeoutSet()) {
               var5.setMemberDiscoveryTimeout(this.bean.getMemberDiscoveryTimeout());
            }

            if ((var3 == null || !var3.contains("MessageDeliveryTimeout")) && this.bean.isMessageDeliveryTimeoutSet()) {
               var5.setMessageDeliveryTimeout(this.bean.getMessageDeliveryTimeout());
            }

            if ((var3 == null || !var3.contains("NodeManagerTimeoutMillis")) && this.bean.isNodeManagerTimeoutMillisSet()) {
               var5.setNodeManagerTimeoutMillis(this.bean.getNodeManagerTimeoutMillis());
            }

            if ((var3 == null || !var3.contains("PeriodicSRMCheckEnabled")) && this.bean.isPeriodicSRMCheckEnabledSet()) {
               var5.setPeriodicSRMCheckEnabled(this.bean.isPeriodicSRMCheckEnabled());
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
