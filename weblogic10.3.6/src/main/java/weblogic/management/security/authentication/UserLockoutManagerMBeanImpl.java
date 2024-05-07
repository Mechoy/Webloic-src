package weblogic.management.security.authentication;

import java.io.Serializable;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.CRC32;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanException;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.beangen.LegalChecks;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBeanHelper;
import weblogic.descriptor.internal.AbstractSchemaHelper2;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.management.commo.AbstractCommoConfigurationBean;
import weblogic.management.commo.RequiredModelMBeanWrapper;
import weblogic.management.security.RealmMBean;
import weblogic.utils.collections.CombinedIterator;

public class UserLockoutManagerMBeanImpl extends AbstractCommoConfigurationBean implements UserLockoutManagerMBean, Serializable {
   private String _CompatibilityObjectName;
   private long _InvalidLoginAttemptsTotalCount;
   private long _InvalidLoginUsersHighCount;
   private long _LockedUsersCurrentCount;
   private long _LockoutCacheSize;
   private long _LockoutDuration;
   private boolean _LockoutEnabled;
   private long _LockoutGCThreshold;
   private long _LockoutResetDuration;
   private long _LockoutThreshold;
   private long _LoginAttemptsWhileLockedTotalCount;
   private String _Name;
   private RealmMBean _Realm;
   private long _UnlockedUsersTotalCount;
   private long _UserLockoutTotalCount;
   private UserLockoutManagerImpl _customizer;
   private static SchemaHelper2 _schemaHelper;

   public UserLockoutManagerMBeanImpl() {
      try {
         this._customizer = new UserLockoutManagerImpl(new RequiredModelMBeanWrapper(this));
      } catch (Exception var2) {
         if (var2 instanceof RuntimeException) {
            throw (RuntimeException)var2;
         }

         throw new UndeclaredThrowableException(var2);
      }

      this._initializeProperty(-1);
   }

   public UserLockoutManagerMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);

      try {
         this._customizer = new UserLockoutManagerImpl(new RequiredModelMBeanWrapper(this));
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         }

         throw new UndeclaredThrowableException(var4);
      }

      this._initializeProperty(-1);
   }

   public long getUserLockoutTotalCount() {
      try {
         return this._customizer.getUserLockoutTotalCount();
      } catch (MBeanException var2) {
         throw new UndeclaredThrowableException(var2);
      }
   }

   public boolean isUserLockoutTotalCountSet() {
      return this._isSet(2);
   }

   public void setUserLockoutTotalCount(long var1) throws InvalidAttributeValueException {
      this._UserLockoutTotalCount = var1;
   }

   public long getInvalidLoginAttemptsTotalCount() {
      try {
         return this._customizer.getInvalidLoginAttemptsTotalCount();
      } catch (MBeanException var2) {
         throw new UndeclaredThrowableException(var2);
      }
   }

   public boolean isInvalidLoginAttemptsTotalCountSet() {
      return this._isSet(3);
   }

   public void setInvalidLoginAttemptsTotalCount(long var1) throws InvalidAttributeValueException {
      this._InvalidLoginAttemptsTotalCount = var1;
   }

   public long getLoginAttemptsWhileLockedTotalCount() {
      try {
         return this._customizer.getLoginAttemptsWhileLockedTotalCount();
      } catch (MBeanException var2) {
         throw new UndeclaredThrowableException(var2);
      }
   }

   public boolean isLoginAttemptsWhileLockedTotalCountSet() {
      return this._isSet(4);
   }

   public void setLoginAttemptsWhileLockedTotalCount(long var1) throws InvalidAttributeValueException {
      this._LoginAttemptsWhileLockedTotalCount = var1;
   }

   public long getInvalidLoginUsersHighCount() {
      try {
         return this._customizer.getInvalidLoginUsersHighCount();
      } catch (MBeanException var2) {
         throw new UndeclaredThrowableException(var2);
      }
   }

   public boolean isInvalidLoginUsersHighCountSet() {
      return this._isSet(5);
   }

   public void setInvalidLoginUsersHighCount(long var1) throws InvalidAttributeValueException {
      this._InvalidLoginUsersHighCount = var1;
   }

   public long getUnlockedUsersTotalCount() {
      try {
         return this._customizer.getUnlockedUsersTotalCount();
      } catch (MBeanException var2) {
         throw new UndeclaredThrowableException(var2);
      }
   }

   public boolean isUnlockedUsersTotalCountSet() {
      return this._isSet(6);
   }

   public void setUnlockedUsersTotalCount(long var1) throws InvalidAttributeValueException {
      this._UnlockedUsersTotalCount = var1;
   }

   public long getLockedUsersCurrentCount() {
      try {
         return this._customizer.getLockedUsersCurrentCount();
      } catch (MBeanException var2) {
         throw new UndeclaredThrowableException(var2);
      }
   }

   public boolean isLockedUsersCurrentCountSet() {
      return this._isSet(7);
   }

   public void setLockedUsersCurrentCount(long var1) throws InvalidAttributeValueException {
      this._LockedUsersCurrentCount = var1;
   }

   public RealmMBean getRealm() {
      return this._customizer.getRealm();
   }

   public boolean isRealmSet() {
      return this._isSet(8);
   }

   public void setRealm(RealmMBean var1) throws InvalidAttributeValueException {
      this._Realm = var1;
   }

   public boolean isLockoutEnabled() {
      return this._LockoutEnabled;
   }

   public boolean isLockoutEnabledSet() {
      return this._isSet(9);
   }

   public void setLockoutEnabled(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this._LockoutEnabled;
      this._LockoutEnabled = var1;
      this._postSet(9, var2, var1);
   }

   public long getLockoutThreshold() {
      return this._LockoutThreshold;
   }

   public boolean isLockoutThresholdSet() {
      return this._isSet(10);
   }

   public void setLockoutThreshold(long var1) throws InvalidAttributeValueException {
      LegalChecks.checkMin("LockoutThreshold", var1, 1L);
      long var3 = this._LockoutThreshold;
      this._LockoutThreshold = var1;
      this._postSet(10, var3, var1);
   }

   public long getLockoutDuration() {
      return this._LockoutDuration;
   }

   public boolean isLockoutDurationSet() {
      return this._isSet(11);
   }

   public void setLockoutDuration(long var1) throws InvalidAttributeValueException {
      LegalChecks.checkMin("LockoutDuration", var1, 0L);
      long var3 = this._LockoutDuration;
      this._LockoutDuration = var1;
      this._postSet(11, var3, var1);
   }

   public long getLockoutResetDuration() {
      return this._LockoutResetDuration;
   }

   public boolean isLockoutResetDurationSet() {
      return this._isSet(12);
   }

   public void setLockoutResetDuration(long var1) throws InvalidAttributeValueException {
      LegalChecks.checkMin("LockoutResetDuration", var1, 1L);
      long var3 = this._LockoutResetDuration;
      this._LockoutResetDuration = var1;
      this._postSet(12, var3, var1);
   }

   public long getLockoutCacheSize() {
      return this._LockoutCacheSize;
   }

   public boolean isLockoutCacheSizeSet() {
      return this._isSet(13);
   }

   public void setLockoutCacheSize(long var1) throws InvalidAttributeValueException {
      LegalChecks.checkMin("LockoutCacheSize", var1, 0L);
      long var3 = this._LockoutCacheSize;
      this._LockoutCacheSize = var1;
      this._postSet(13, var3, var1);
   }

   public long getLockoutGCThreshold() {
      return this._LockoutGCThreshold;
   }

   public boolean isLockoutGCThresholdSet() {
      return this._isSet(14);
   }

   public void setLockoutGCThreshold(long var1) throws InvalidAttributeValueException {
      LegalChecks.checkMin("LockoutGCThreshold", var1, 0L);
      long var3 = this._LockoutGCThreshold;
      this._LockoutGCThreshold = var1;
      this._postSet(14, var3, var1);
   }

   public boolean isLockedOut(String var1) {
      try {
         return this._customizer.isLockedOut(var1);
      } catch (MBeanException var3) {
         throw new UndeclaredThrowableException(var3);
      }
   }

   public void clearLockout(String var1) {
      try {
         this._customizer.clearLockout(var1);
      } catch (MBeanException var3) {
         throw new UndeclaredThrowableException(var3);
      }
   }

   public long getLastLoginFailure(String var1) {
      try {
         return this._customizer.getLastLoginFailure(var1);
      } catch (MBeanException var3) {
         throw new UndeclaredThrowableException(var3);
      }
   }

   public long getLoginFailureCount(String var1) {
      try {
         return this._customizer.getLoginFailureCount(var1);
      } catch (MBeanException var3) {
         throw new UndeclaredThrowableException(var3);
      }
   }

   public String getName() {
      return this._Name;
   }

   public boolean isNameSet() {
      return this._isSet(15);
   }

   public void setName(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._Name;
      this._Name = var1;
      this._postSet(15, var2, var1);
   }

   public String getCompatibilityObjectName() {
      return this._customizer.getCompatibilityObjectName();
   }

   public boolean isCompatibilityObjectNameSet() {
      return this._isSet(16);
   }

   public void setCompatibilityObjectName(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._CompatibilityObjectName;
      this._CompatibilityObjectName = var1;
      this._postSet(16, var2, var1);
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
         var1 = 16;
      }

      try {
         switch (var1) {
            case 16:
               this._CompatibilityObjectName = null;
               if (var2) {
                  break;
               }
            case 3:
               this._InvalidLoginAttemptsTotalCount = 0L;
               if (var2) {
                  break;
               }
            case 5:
               this._InvalidLoginUsersHighCount = 0L;
               if (var2) {
                  break;
               }
            case 7:
               this._LockedUsersCurrentCount = 0L;
               if (var2) {
                  break;
               }
            case 13:
               this._LockoutCacheSize = 5L;
               if (var2) {
                  break;
               }
            case 11:
               this._LockoutDuration = 30L;
               if (var2) {
                  break;
               }
            case 14:
               this._LockoutGCThreshold = 400L;
               if (var2) {
                  break;
               }
            case 12:
               this._LockoutResetDuration = 5L;
               if (var2) {
                  break;
               }
            case 10:
               this._LockoutThreshold = 5L;
               if (var2) {
                  break;
               }
            case 4:
               this._LoginAttemptsWhileLockedTotalCount = 0L;
               if (var2) {
                  break;
               }
            case 15:
               this._Name = "UserLockoutManager";
               if (var2) {
                  break;
               }
            case 8:
               this._Realm = null;
               if (var2) {
                  break;
               }
            case 6:
               this._UnlockedUsersTotalCount = 0L;
               if (var2) {
                  break;
               }
            case 2:
               this._UserLockoutTotalCount = 0L;
               if (var2) {
                  break;
               }
            case 9:
               this._LockoutEnabled = true;
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
      return "http://xmlns.oracle.com/weblogic/1.0/security.xsd";
   }

   protected String getTargetNamespace() {
      return "http://xmlns.oracle.com/weblogic/security";
   }

   public SchemaHelper _getSchemaHelper2() {
      if (_schemaHelper == null) {
         _schemaHelper = new SchemaHelper2();
      }

      return _schemaHelper;
   }

   public String wls_getInterfaceClassName() {
      return "weblogic.management.security.authentication.UserLockoutManagerMBean";
   }

   public static class SchemaHelper2 extends AbstractSchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 4:
               if (var1.equals("name")) {
                  return 15;
               }
               break;
            case 5:
               if (var1.equals("realm")) {
                  return 8;
               }
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 20:
            case 21:
            case 23:
            case 27:
            case 28:
            case 29:
            case 31:
            case 32:
            case 33:
            case 35:
            case 36:
            case 37:
            case 38:
            default:
               break;
            case 15:
               if (var1.equals("lockout-enabled")) {
                  return 9;
               }
               break;
            case 16:
               if (var1.equals("lockout-duration")) {
                  return 11;
               }
               break;
            case 17:
               if (var1.equals("lockout-threshold")) {
                  return 10;
               }
               break;
            case 18:
               if (var1.equals("lockout-cache-size")) {
                  return 13;
               }
               break;
            case 19:
               if (var1.equals("lockoutgc-threshold")) {
                  return 14;
               }
               break;
            case 22:
               if (var1.equals("lockout-reset-duration")) {
                  return 12;
               }
               break;
            case 24:
               if (var1.equals("user-lockout-total-count")) {
                  return 2;
               }
               break;
            case 25:
               if (var1.equals("compatibility-object-name")) {
                  return 16;
               }
               break;
            case 26:
               if (var1.equals("locked-users-current-count")) {
                  return 7;
               }

               if (var1.equals("unlocked-users-total-count")) {
                  return 6;
               }
               break;
            case 30:
               if (var1.equals("invalid-login-users-high-count")) {
                  return 5;
               }
               break;
            case 34:
               if (var1.equals("invalid-login-attempts-total-count")) {
                  return 3;
               }
               break;
            case 39:
               if (var1.equals("login-attempts-while-locked-total-count")) {
                  return 4;
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
               return "user-lockout-total-count";
            case 3:
               return "invalid-login-attempts-total-count";
            case 4:
               return "login-attempts-while-locked-total-count";
            case 5:
               return "invalid-login-users-high-count";
            case 6:
               return "unlocked-users-total-count";
            case 7:
               return "locked-users-current-count";
            case 8:
               return "realm";
            case 9:
               return "lockout-enabled";
            case 10:
               return "lockout-threshold";
            case 11:
               return "lockout-duration";
            case 12:
               return "lockout-reset-duration";
            case 13:
               return "lockout-cache-size";
            case 14:
               return "lockoutgc-threshold";
            case 15:
               return "name";
            case 16:
               return "compatibility-object-name";
            default:
               return super.getElementName(var1);
         }
      }

      public boolean isBean(int var1) {
         switch (var1) {
            default:
               return super.isBean(var1);
         }
      }
   }

   protected static class Helper extends AbstractCommoConfigurationBean.Helper {
      private UserLockoutManagerMBeanImpl bean;

      protected Helper(UserLockoutManagerMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 2:
               return "UserLockoutTotalCount";
            case 3:
               return "InvalidLoginAttemptsTotalCount";
            case 4:
               return "LoginAttemptsWhileLockedTotalCount";
            case 5:
               return "InvalidLoginUsersHighCount";
            case 6:
               return "UnlockedUsersTotalCount";
            case 7:
               return "LockedUsersCurrentCount";
            case 8:
               return "Realm";
            case 9:
               return "LockoutEnabled";
            case 10:
               return "LockoutThreshold";
            case 11:
               return "LockoutDuration";
            case 12:
               return "LockoutResetDuration";
            case 13:
               return "LockoutCacheSize";
            case 14:
               return "LockoutGCThreshold";
            case 15:
               return "Name";
            case 16:
               return "CompatibilityObjectName";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("CompatibilityObjectName")) {
            return 16;
         } else if (var1.equals("InvalidLoginAttemptsTotalCount")) {
            return 3;
         } else if (var1.equals("InvalidLoginUsersHighCount")) {
            return 5;
         } else if (var1.equals("LockedUsersCurrentCount")) {
            return 7;
         } else if (var1.equals("LockoutCacheSize")) {
            return 13;
         } else if (var1.equals("LockoutDuration")) {
            return 11;
         } else if (var1.equals("LockoutGCThreshold")) {
            return 14;
         } else if (var1.equals("LockoutResetDuration")) {
            return 12;
         } else if (var1.equals("LockoutThreshold")) {
            return 10;
         } else if (var1.equals("LoginAttemptsWhileLockedTotalCount")) {
            return 4;
         } else if (var1.equals("Name")) {
            return 15;
         } else if (var1.equals("Realm")) {
            return 8;
         } else if (var1.equals("UnlockedUsersTotalCount")) {
            return 6;
         } else if (var1.equals("UserLockoutTotalCount")) {
            return 2;
         } else {
            return var1.equals("LockoutEnabled") ? 9 : super.getPropertyIndex(var1);
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
            if (this.bean.isCompatibilityObjectNameSet()) {
               var2.append("CompatibilityObjectName");
               var2.append(String.valueOf(this.bean.getCompatibilityObjectName()));
            }

            if (this.bean.isInvalidLoginAttemptsTotalCountSet()) {
               var2.append("InvalidLoginAttemptsTotalCount");
               var2.append(String.valueOf(this.bean.getInvalidLoginAttemptsTotalCount()));
            }

            if (this.bean.isInvalidLoginUsersHighCountSet()) {
               var2.append("InvalidLoginUsersHighCount");
               var2.append(String.valueOf(this.bean.getInvalidLoginUsersHighCount()));
            }

            if (this.bean.isLockedUsersCurrentCountSet()) {
               var2.append("LockedUsersCurrentCount");
               var2.append(String.valueOf(this.bean.getLockedUsersCurrentCount()));
            }

            if (this.bean.isLockoutCacheSizeSet()) {
               var2.append("LockoutCacheSize");
               var2.append(String.valueOf(this.bean.getLockoutCacheSize()));
            }

            if (this.bean.isLockoutDurationSet()) {
               var2.append("LockoutDuration");
               var2.append(String.valueOf(this.bean.getLockoutDuration()));
            }

            if (this.bean.isLockoutGCThresholdSet()) {
               var2.append("LockoutGCThreshold");
               var2.append(String.valueOf(this.bean.getLockoutGCThreshold()));
            }

            if (this.bean.isLockoutResetDurationSet()) {
               var2.append("LockoutResetDuration");
               var2.append(String.valueOf(this.bean.getLockoutResetDuration()));
            }

            if (this.bean.isLockoutThresholdSet()) {
               var2.append("LockoutThreshold");
               var2.append(String.valueOf(this.bean.getLockoutThreshold()));
            }

            if (this.bean.isLoginAttemptsWhileLockedTotalCountSet()) {
               var2.append("LoginAttemptsWhileLockedTotalCount");
               var2.append(String.valueOf(this.bean.getLoginAttemptsWhileLockedTotalCount()));
            }

            if (this.bean.isNameSet()) {
               var2.append("Name");
               var2.append(String.valueOf(this.bean.getName()));
            }

            if (this.bean.isRealmSet()) {
               var2.append("Realm");
               var2.append(String.valueOf(this.bean.getRealm()));
            }

            if (this.bean.isUnlockedUsersTotalCountSet()) {
               var2.append("UnlockedUsersTotalCount");
               var2.append(String.valueOf(this.bean.getUnlockedUsersTotalCount()));
            }

            if (this.bean.isUserLockoutTotalCountSet()) {
               var2.append("UserLockoutTotalCount");
               var2.append(String.valueOf(this.bean.getUserLockoutTotalCount()));
            }

            if (this.bean.isLockoutEnabledSet()) {
               var2.append("LockoutEnabled");
               var2.append(String.valueOf(this.bean.isLockoutEnabled()));
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
            UserLockoutManagerMBeanImpl var2 = (UserLockoutManagerMBeanImpl)var1;
            this.computeDiff("CompatibilityObjectName", this.bean.getCompatibilityObjectName(), var2.getCompatibilityObjectName(), false);
            this.computeDiff("LockoutCacheSize", this.bean.getLockoutCacheSize(), var2.getLockoutCacheSize(), false);
            this.computeDiff("LockoutDuration", this.bean.getLockoutDuration(), var2.getLockoutDuration(), false);
            this.computeDiff("LockoutGCThreshold", this.bean.getLockoutGCThreshold(), var2.getLockoutGCThreshold(), false);
            this.computeDiff("LockoutResetDuration", this.bean.getLockoutResetDuration(), var2.getLockoutResetDuration(), false);
            this.computeDiff("LockoutThreshold", this.bean.getLockoutThreshold(), var2.getLockoutThreshold(), false);
            this.computeDiff("Name", this.bean.getName(), var2.getName(), false);
            this.computeDiff("LockoutEnabled", this.bean.isLockoutEnabled(), var2.isLockoutEnabled(), false);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            UserLockoutManagerMBeanImpl var3 = (UserLockoutManagerMBeanImpl)var1.getSourceBean();
            UserLockoutManagerMBeanImpl var4 = (UserLockoutManagerMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("CompatibilityObjectName")) {
                  var3._conditionalUnset(var2.isUnsetUpdate(), 16);
               } else if (!var5.equals("InvalidLoginAttemptsTotalCount") && !var5.equals("InvalidLoginUsersHighCount") && !var5.equals("LockedUsersCurrentCount")) {
                  if (var5.equals("LockoutCacheSize")) {
                     var3.setLockoutCacheSize(var4.getLockoutCacheSize());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 13);
                  } else if (var5.equals("LockoutDuration")) {
                     var3.setLockoutDuration(var4.getLockoutDuration());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 11);
                  } else if (var5.equals("LockoutGCThreshold")) {
                     var3.setLockoutGCThreshold(var4.getLockoutGCThreshold());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 14);
                  } else if (var5.equals("LockoutResetDuration")) {
                     var3.setLockoutResetDuration(var4.getLockoutResetDuration());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 12);
                  } else if (var5.equals("LockoutThreshold")) {
                     var3.setLockoutThreshold(var4.getLockoutThreshold());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 10);
                  } else if (!var5.equals("LoginAttemptsWhileLockedTotalCount")) {
                     if (var5.equals("Name")) {
                        var3.setName(var4.getName());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 15);
                     } else if (!var5.equals("Realm") && !var5.equals("UnlockedUsersTotalCount") && !var5.equals("UserLockoutTotalCount")) {
                        if (var5.equals("LockoutEnabled")) {
                           var3.setLockoutEnabled(var4.isLockoutEnabled());
                           var3._conditionalUnset(var2.isUnsetUpdate(), 9);
                        } else {
                           super.applyPropertyUpdate(var1, var2);
                        }
                     }
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
            UserLockoutManagerMBeanImpl var5 = (UserLockoutManagerMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("CompatibilityObjectName")) && this.bean.isCompatibilityObjectNameSet()) {
            }

            if ((var3 == null || !var3.contains("LockoutCacheSize")) && this.bean.isLockoutCacheSizeSet()) {
               var5.setLockoutCacheSize(this.bean.getLockoutCacheSize());
            }

            if ((var3 == null || !var3.contains("LockoutDuration")) && this.bean.isLockoutDurationSet()) {
               var5.setLockoutDuration(this.bean.getLockoutDuration());
            }

            if ((var3 == null || !var3.contains("LockoutGCThreshold")) && this.bean.isLockoutGCThresholdSet()) {
               var5.setLockoutGCThreshold(this.bean.getLockoutGCThreshold());
            }

            if ((var3 == null || !var3.contains("LockoutResetDuration")) && this.bean.isLockoutResetDurationSet()) {
               var5.setLockoutResetDuration(this.bean.getLockoutResetDuration());
            }

            if ((var3 == null || !var3.contains("LockoutThreshold")) && this.bean.isLockoutThresholdSet()) {
               var5.setLockoutThreshold(this.bean.getLockoutThreshold());
            }

            if ((var3 == null || !var3.contains("Name")) && this.bean.isNameSet()) {
               var5.setName(this.bean.getName());
            }

            if ((var3 == null || !var3.contains("LockoutEnabled")) && this.bean.isLockoutEnabledSet()) {
               var5.setLockoutEnabled(this.bean.isLockoutEnabled());
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
         this.inferSubTree(this.bean.getRealm(), var1, var2);
      }
   }
}
