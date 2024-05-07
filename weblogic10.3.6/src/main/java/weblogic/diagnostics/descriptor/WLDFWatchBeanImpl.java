package weblogic.diagnostics.descriptor;

import java.io.Serializable;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.zip.CRC32;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.beangen.LegalChecks;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBeanHelper;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.ReferenceManager;
import weblogic.descriptor.internal.ResolvedReference;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.diagnostics.watch.WatchValidator;
import weblogic.utils.ArrayUtils;
import weblogic.utils.collections.CombinedIterator;

public class WLDFWatchBeanImpl extends WLDFBeanImpl implements WLDFWatchBean, Serializable {
   private int _AlarmResetPeriod;
   private String _AlarmType;
   private boolean _Enabled;
   private WLDFNotificationBean[] _Notifications;
   private String _RuleExpression;
   private String _RuleType;
   private String _Severity;
   private WLDFWatchCustomizer _customizer;
   private static SchemaHelper2 _schemaHelper;

   public WLDFWatchBeanImpl() {
      try {
         this._customizer = new WLDFWatchCustomizer(this);
      } catch (Exception var2) {
         if (var2 instanceof RuntimeException) {
            throw (RuntimeException)var2;
         }

         throw new UndeclaredThrowableException(var2);
      }

      this._initializeProperty(-1);
   }

   public WLDFWatchBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);

      try {
         this._customizer = new WLDFWatchCustomizer(this);
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         }

         throw new UndeclaredThrowableException(var4);
      }

      this._initializeProperty(-1);
   }

   public boolean isEnabled() {
      return this._Enabled;
   }

   public boolean isEnabledSet() {
      return this._isSet(1);
   }

   public void setEnabled(boolean var1) {
      boolean var2 = this._Enabled;
      this._Enabled = var1;
      this._postSet(1, var2, var1);
   }

   public String getRuleType() {
      return this._RuleType;
   }

   public boolean isRuleTypeSet() {
      return this._isSet(2);
   }

   public void setRuleType(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"Harvester", "Log", "EventData"};
      var1 = LegalChecks.checkInEnum("RuleType", var1, var2);
      String var3 = this._RuleType;
      this._RuleType = var1;
      this._postSet(2, var3, var1);
   }

   public String getRuleExpression() {
      return this._RuleExpression;
   }

   public boolean isRuleExpressionSet() {
      return this._isSet(3);
   }

   public void setRuleExpression(String var1) {
      var1 = var1 == null ? null : var1.trim();
      LegalChecks.checkNonEmptyString("RuleExpression", var1);
      LegalChecks.checkNonNull("RuleExpression", var1);
      String var2 = this._RuleExpression;
      this._RuleExpression = var1;
      this._postSet(3, var2, var1);
   }

   public String getSeverity() {
      return this._customizer.getSeverity();
   }

   public boolean isSeveritySet() {
      return this._isSet(4);
   }

   public void setSeverity(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"Info", "Warning", "Error", "Notice", "Critical", "Alert", "Emergency"};
      var1 = LegalChecks.checkInEnum("Severity", var1, var2);
      String var3 = this.getSeverity();
      this._customizer.setSeverity(var1);
      this._postSet(4, var3, var1);
   }

   public String getAlarmType() {
      return this._AlarmType;
   }

   public boolean isAlarmTypeSet() {
      return this._isSet(5);
   }

   public void setAlarmType(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"None", "ManualReset", "AutomaticReset"};
      var1 = LegalChecks.checkInEnum("AlarmType", var1, var2);
      String var3 = this._AlarmType;
      this._AlarmType = var1;
      this._postSet(5, var3, var1);
   }

   public int getAlarmResetPeriod() {
      return this._AlarmResetPeriod;
   }

   public boolean isAlarmResetPeriodSet() {
      return this._isSet(6);
   }

   public void setAlarmResetPeriod(int var1) {
      LegalChecks.checkMin("AlarmResetPeriod", var1, 1000);
      int var2 = this._AlarmResetPeriod;
      this._AlarmResetPeriod = var1;
      this._postSet(6, var2, var1);
   }

   public WLDFNotificationBean[] getNotifications() {
      return this._Notifications;
   }

   public String getNotificationsAsString() {
      return this._getHelper()._serializeKeyList(this.getNotifications());
   }

   public boolean isNotificationsSet() {
      return this._isSet(7);
   }

   public void setNotificationsAsString(String var1) {
      if (var1 != null && var1.length() != 0) {
         String[] var13 = this._getHelper()._splitKeyList(var1);
         List var3 = this._getHelper()._getKeyList(this._Notifications);

         String var5;
         for(int var4 = 0; var4 < var13.length; ++var4) {
            var5 = var13[var4];
            var5 = var5 == null ? null : var5.trim();
            if (var3.contains(var5)) {
               var3.remove(var5);
            } else {
               this._getReferenceManager().registerUnresolvedReference(var5, WLDFNotificationBean.class, new ReferenceManager.Resolver(this, 7) {
                  public void resolveReference(Object var1) {
                     try {
                        WLDFWatchBeanImpl.this.addNotification((WLDFNotificationBean)var1);
                     } catch (RuntimeException var3) {
                        throw var3;
                     } catch (Exception var4) {
                        throw new AssertionError("Impossible exception: " + var4);
                     }
                  }
               });
            }
         }

         Iterator var14 = var3.iterator();

         while(true) {
            while(var14.hasNext()) {
               var5 = (String)var14.next();
               WLDFNotificationBean[] var6 = this._Notifications;
               int var7 = var6.length;

               for(int var8 = 0; var8 < var7; ++var8) {
                  WLDFNotificationBean var9 = var6[var8];
                  if (var5.equals(var9.getName())) {
                     try {
                        this.removeNotification(var9);
                        break;
                     } catch (RuntimeException var11) {
                        throw var11;
                     } catch (Exception var12) {
                        throw new AssertionError("Impossible exception: " + var12);
                     }
                  }
               }
            }

            return;
         }
      } else {
         WLDFNotificationBean[] var2 = this._Notifications;
         this._initializeProperty(7);
         this._postSet(7, var2, this._Notifications);
      }
   }

   public void setNotifications(WLDFNotificationBean[] var1) {
      Object var4 = var1 == null ? new WLDFNotificationBeanImpl[0] : var1;
      var1 = (WLDFNotificationBean[])((WLDFNotificationBean[])this._getHelper()._cleanAndValidateArray(var4, WLDFNotificationBean.class));

      for(int var2 = 0; var2 < var1.length; ++var2) {
         if (var1[var2] != null) {
            ResolvedReference var3 = new ResolvedReference(this, 7, (AbstractDescriptorBean)var1[var2]) {
               protected Object getPropertyValue() {
                  return WLDFWatchBeanImpl.this.getNotifications();
               }
            };
            this._getReferenceManager().registerResolvedReference((AbstractDescriptorBean)var1[var2], var3);
         }
      }

      WLDFNotificationBean[] var5 = this._Notifications;
      this._Notifications = var1;
      this._postSet(7, var5, var1);
   }

   public boolean addNotification(WLDFNotificationBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 7)) {
         WLDFNotificationBean[] var2;
         if (this._isSet(7)) {
            var2 = (WLDFNotificationBean[])((WLDFNotificationBean[])this._getHelper()._extendArray(this.getNotifications(), WLDFNotificationBean.class, var1));
         } else {
            var2 = new WLDFNotificationBean[]{var1};
         }

         try {
            this.setNotifications(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

      return true;
   }

   public boolean removeNotification(WLDFNotificationBean var1) {
      WLDFNotificationBean[] var2 = this.getNotifications();
      WLDFNotificationBean[] var3 = (WLDFNotificationBean[])((WLDFNotificationBean[])this._getHelper()._removeElement(var2, WLDFNotificationBean.class, var1));
      if (var3.length != var2.length) {
         try {
            this.setNotifications(var3);
            return true;
         } catch (Exception var5) {
            if (var5 instanceof RuntimeException) {
               throw (RuntimeException)var5;
            } else {
               throw new UndeclaredThrowableException(var5);
            }
         }
      } else {
         return false;
      }
   }

   public Object _getKey() {
      return super._getKey();
   }

   public void _validate() throws IllegalArgumentException {
      super._validate();
      WatchValidator.validateWatch(this);
      LegalChecks.checkIsSet("RuleExpression", this.isRuleExpressionSet());
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
         var1 = 6;
      }

      try {
         switch (var1) {
            case 6:
               this._AlarmResetPeriod = 60000;
               if (var2) {
                  break;
               }
            case 5:
               this._AlarmType = "None";
               if (var2) {
                  break;
               }
            case 7:
               this._Notifications = new WLDFNotificationBean[0];
               if (var2) {
                  break;
               }
            case 3:
               this._RuleExpression = null;
               if (var2) {
                  break;
               }
            case 2:
               this._RuleType = "Harvester";
               if (var2) {
                  break;
               }
            case 4:
               this._customizer.setSeverity("Notice");
               if (var2) {
                  break;
               }
            case 1:
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
      return "http://xmlns.oracle.com/weblogic/weblogic-diagnostics/1.0/weblogic-diagnostics.xsd";
   }

   protected String getTargetNamespace() {
      return "http://xmlns.oracle.com/weblogic/weblogic-diagnostics";
   }

   public SchemaHelper _getSchemaHelper2() {
      if (_schemaHelper == null) {
         _schemaHelper = new SchemaHelper2();
      }

      return _schemaHelper;
   }

   public static class SchemaHelper2 extends WLDFBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 7:
               if (var1.equals("enabled")) {
                  return 1;
               }
               break;
            case 8:
               if (var1.equals("severity")) {
                  return 4;
               }
               break;
            case 9:
               if (var1.equals("rule-type")) {
                  return 2;
               }
               break;
            case 10:
               if (var1.equals("alarm-type")) {
                  return 5;
               }
            case 11:
            case 13:
            case 14:
            case 16:
            case 17:
            default:
               break;
            case 12:
               if (var1.equals("notification")) {
                  return 7;
               }
               break;
            case 15:
               if (var1.equals("rule-expression")) {
                  return 3;
               }
               break;
            case 18:
               if (var1.equals("alarm-reset-period")) {
                  return 6;
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
            case 1:
               return "enabled";
            case 2:
               return "rule-type";
            case 3:
               return "rule-expression";
            case 4:
               return "severity";
            case 5:
               return "alarm-type";
            case 6:
               return "alarm-reset-period";
            case 7:
               return "notification";
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
            case 0:
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

   protected static class Helper extends WLDFBeanImpl.Helper {
      private WLDFWatchBeanImpl bean;

      protected Helper(WLDFWatchBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 1:
               return "Enabled";
            case 2:
               return "RuleType";
            case 3:
               return "RuleExpression";
            case 4:
               return "Severity";
            case 5:
               return "AlarmType";
            case 6:
               return "AlarmResetPeriod";
            case 7:
               return "Notifications";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("AlarmResetPeriod")) {
            return 6;
         } else if (var1.equals("AlarmType")) {
            return 5;
         } else if (var1.equals("Notifications")) {
            return 7;
         } else if (var1.equals("RuleExpression")) {
            return 3;
         } else if (var1.equals("RuleType")) {
            return 2;
         } else if (var1.equals("Severity")) {
            return 4;
         } else {
            return var1.equals("Enabled") ? 1 : super.getPropertyIndex(var1);
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
            if (this.bean.isAlarmResetPeriodSet()) {
               var2.append("AlarmResetPeriod");
               var2.append(String.valueOf(this.bean.getAlarmResetPeriod()));
            }

            if (this.bean.isAlarmTypeSet()) {
               var2.append("AlarmType");
               var2.append(String.valueOf(this.bean.getAlarmType()));
            }

            if (this.bean.isNotificationsSet()) {
               var2.append("Notifications");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getNotifications())));
            }

            if (this.bean.isRuleExpressionSet()) {
               var2.append("RuleExpression");
               var2.append(String.valueOf(this.bean.getRuleExpression()));
            }

            if (this.bean.isRuleTypeSet()) {
               var2.append("RuleType");
               var2.append(String.valueOf(this.bean.getRuleType()));
            }

            if (this.bean.isSeveritySet()) {
               var2.append("Severity");
               var2.append(String.valueOf(this.bean.getSeverity()));
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
            WLDFWatchBeanImpl var2 = (WLDFWatchBeanImpl)var1;
            this.computeDiff("AlarmResetPeriod", this.bean.getAlarmResetPeriod(), var2.getAlarmResetPeriod(), true);
            this.computeDiff("AlarmType", this.bean.getAlarmType(), var2.getAlarmType(), true);
            this.computeDiff("Notifications", this.bean.getNotifications(), var2.getNotifications(), true);
            this.computeDiff("RuleExpression", this.bean.getRuleExpression(), var2.getRuleExpression(), true);
            this.computeDiff("RuleType", this.bean.getRuleType(), var2.getRuleType(), true);
            this.computeDiff("Severity", this.bean.getSeverity(), var2.getSeverity(), true);
            this.computeDiff("Enabled", this.bean.isEnabled(), var2.isEnabled(), true);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            WLDFWatchBeanImpl var3 = (WLDFWatchBeanImpl)var1.getSourceBean();
            WLDFWatchBeanImpl var4 = (WLDFWatchBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("AlarmResetPeriod")) {
                  var3.setAlarmResetPeriod(var4.getAlarmResetPeriod());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 6);
               } else if (var5.equals("AlarmType")) {
                  var3.setAlarmType(var4.getAlarmType());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 5);
               } else if (var5.equals("Notifications")) {
                  var3.setNotificationsAsString(var4.getNotificationsAsString());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 7);
               } else if (var5.equals("RuleExpression")) {
                  var3.setRuleExpression(var4.getRuleExpression());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 3);
               } else if (var5.equals("RuleType")) {
                  var3.setRuleType(var4.getRuleType());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 2);
               } else if (var5.equals("Severity")) {
                  var3.setSeverity(var4.getSeverity());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 4);
               } else if (var5.equals("Enabled")) {
                  var3.setEnabled(var4.isEnabled());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 1);
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
            WLDFWatchBeanImpl var5 = (WLDFWatchBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("AlarmResetPeriod")) && this.bean.isAlarmResetPeriodSet()) {
               var5.setAlarmResetPeriod(this.bean.getAlarmResetPeriod());
            }

            if ((var3 == null || !var3.contains("AlarmType")) && this.bean.isAlarmTypeSet()) {
               var5.setAlarmType(this.bean.getAlarmType());
            }

            if ((var3 == null || !var3.contains("Notifications")) && this.bean.isNotificationsSet()) {
               var5._unSet(var5, 7);
               var5.setNotificationsAsString(this.bean.getNotificationsAsString());
            }

            if ((var3 == null || !var3.contains("RuleExpression")) && this.bean.isRuleExpressionSet()) {
               var5.setRuleExpression(this.bean.getRuleExpression());
            }

            if ((var3 == null || !var3.contains("RuleType")) && this.bean.isRuleTypeSet()) {
               var5.setRuleType(this.bean.getRuleType());
            }

            if ((var3 == null || !var3.contains("Severity")) && this.bean.isSeveritySet()) {
               var5.setSeverity(this.bean.getSeverity());
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
         this.inferSubTree(this.bean.getNotifications(), var1, var2);
      }
   }
}
