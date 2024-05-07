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

public class PasswordPolicyMBeanImpl extends ConfigurationMBeanImpl implements PasswordPolicyMBean, Serializable {
   private int _LockoutCacheSize;
   private int _LockoutDuration;
   private boolean _LockoutEnabled;
   private int _LockoutGCThreshold;
   private int _LockoutResetDuration;
   private int _LockoutThreshold;
   private int _MinimumPasswordLength;
   private static SchemaHelper2 _schemaHelper;

   public PasswordPolicyMBeanImpl() {
      this._initializeProperty(-1);
   }

   public PasswordPolicyMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public int getMinimumPasswordLength() {
      return this._MinimumPasswordLength;
   }

   public boolean isMinimumPasswordLengthSet() {
      return this._isSet(7);
   }

   public void setMinimumPasswordLength(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkMin("MinimumPasswordLength", var1, 0);
      int var2 = this._MinimumPasswordLength;
      this._MinimumPasswordLength = var1;
      this._postSet(7, var2, var1);
   }

   public boolean isLockoutEnabled() {
      return this._LockoutEnabled;
   }

   public boolean isLockoutEnabledSet() {
      return this._isSet(8);
   }

   public void setLockoutEnabled(boolean var1) {
      boolean var2 = this._LockoutEnabled;
      this._LockoutEnabled = var1;
      this._postSet(8, var2, var1);
   }

   public int getLockoutThreshold() {
      return this._LockoutThreshold;
   }

   public boolean isLockoutThresholdSet() {
      return this._isSet(9);
   }

   public void setLockoutThreshold(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("LockoutThreshold", (long)var1, 1L, 99999L);
      int var2 = this._LockoutThreshold;
      this._LockoutThreshold = var1;
      this._postSet(9, var2, var1);
   }

   public int getLockoutDuration() {
      return this._LockoutDuration;
   }

   public boolean isLockoutDurationSet() {
      return this._isSet(10);
   }

   public void setLockoutDuration(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("LockoutDuration", (long)var1, 0L, 999999L);
      int var2 = this._LockoutDuration;
      this._LockoutDuration = var1;
      this._postSet(10, var2, var1);
   }

   public int getLockoutResetDuration() {
      return this._LockoutResetDuration;
   }

   public boolean isLockoutResetDurationSet() {
      return this._isSet(11);
   }

   public void setLockoutResetDuration(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("LockoutResetDuration", (long)var1, 1L, 99999L);
      int var2 = this._LockoutResetDuration;
      this._LockoutResetDuration = var1;
      this._postSet(11, var2, var1);
   }

   public int getLockoutCacheSize() {
      return this._LockoutCacheSize;
   }

   public boolean isLockoutCacheSizeSet() {
      return this._isSet(12);
   }

   public void setLockoutCacheSize(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("LockoutCacheSize", (long)var1, 0L, 99999L);
      int var2 = this._LockoutCacheSize;
      this._LockoutCacheSize = var1;
      this._postSet(12, var2, var1);
   }

   public int getLockoutGCThreshold() {
      return this._LockoutGCThreshold;
   }

   public boolean isLockoutGCThresholdSet() {
      return this._isSet(13);
   }

   public void setLockoutGCThreshold(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("LockoutGCThreshold", (long)var1, 0L, 999999L);
      int var2 = this._LockoutGCThreshold;
      this._LockoutGCThreshold = var1;
      this._postSet(13, var2, var1);
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
         var1 = 12;
      }

      try {
         switch (var1) {
            case 12:
               this._LockoutCacheSize = 5;
               if (var2) {
                  break;
               }
            case 10:
               this._LockoutDuration = 30;
               if (var2) {
                  break;
               }
            case 13:
               this._LockoutGCThreshold = 400;
               if (var2) {
                  break;
               }
            case 11:
               this._LockoutResetDuration = 5;
               if (var2) {
                  break;
               }
            case 9:
               this._LockoutThreshold = 5;
               if (var2) {
                  break;
               }
            case 7:
               this._MinimumPasswordLength = 8;
               if (var2) {
                  break;
               }
            case 8:
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
      return "PasswordPolicy";
   }

   public void putValue(String var1, Object var2) {
      int var3;
      if (var1.equals("LockoutCacheSize")) {
         var3 = this._LockoutCacheSize;
         this._LockoutCacheSize = (Integer)var2;
         this._postSet(12, var3, this._LockoutCacheSize);
      } else if (var1.equals("LockoutDuration")) {
         var3 = this._LockoutDuration;
         this._LockoutDuration = (Integer)var2;
         this._postSet(10, var3, this._LockoutDuration);
      } else if (var1.equals("LockoutEnabled")) {
         boolean var4 = this._LockoutEnabled;
         this._LockoutEnabled = (Boolean)var2;
         this._postSet(8, var4, this._LockoutEnabled);
      } else if (var1.equals("LockoutGCThreshold")) {
         var3 = this._LockoutGCThreshold;
         this._LockoutGCThreshold = (Integer)var2;
         this._postSet(13, var3, this._LockoutGCThreshold);
      } else if (var1.equals("LockoutResetDuration")) {
         var3 = this._LockoutResetDuration;
         this._LockoutResetDuration = (Integer)var2;
         this._postSet(11, var3, this._LockoutResetDuration);
      } else if (var1.equals("LockoutThreshold")) {
         var3 = this._LockoutThreshold;
         this._LockoutThreshold = (Integer)var2;
         this._postSet(9, var3, this._LockoutThreshold);
      } else if (var1.equals("MinimumPasswordLength")) {
         var3 = this._MinimumPasswordLength;
         this._MinimumPasswordLength = (Integer)var2;
         this._postSet(7, var3, this._MinimumPasswordLength);
      } else {
         super.putValue(var1, var2);
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("LockoutCacheSize")) {
         return new Integer(this._LockoutCacheSize);
      } else if (var1.equals("LockoutDuration")) {
         return new Integer(this._LockoutDuration);
      } else if (var1.equals("LockoutEnabled")) {
         return new Boolean(this._LockoutEnabled);
      } else if (var1.equals("LockoutGCThreshold")) {
         return new Integer(this._LockoutGCThreshold);
      } else if (var1.equals("LockoutResetDuration")) {
         return new Integer(this._LockoutResetDuration);
      } else if (var1.equals("LockoutThreshold")) {
         return new Integer(this._LockoutThreshold);
      } else {
         return var1.equals("MinimumPasswordLength") ? new Integer(this._MinimumPasswordLength) : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends ConfigurationMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 15:
               if (var1.equals("lockout-enabled")) {
                  return 8;
               }
               break;
            case 16:
               if (var1.equals("lockout-duration")) {
                  return 10;
               }
               break;
            case 17:
               if (var1.equals("lockout-threshold")) {
                  return 9;
               }
               break;
            case 18:
               if (var1.equals("lockout-cache-size")) {
                  return 12;
               }
               break;
            case 19:
               if (var1.equals("lockoutgc-threshold")) {
                  return 13;
               }
            case 20:
            case 21:
            default:
               break;
            case 22:
               if (var1.equals("lockout-reset-duration")) {
                  return 11;
               }
               break;
            case 23:
               if (var1.equals("minimum-password-length")) {
                  return 7;
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
               return "minimum-password-length";
            case 8:
               return "lockout-enabled";
            case 9:
               return "lockout-threshold";
            case 10:
               return "lockout-duration";
            case 11:
               return "lockout-reset-duration";
            case 12:
               return "lockout-cache-size";
            case 13:
               return "lockoutgc-threshold";
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
      private PasswordPolicyMBeanImpl bean;

      protected Helper(PasswordPolicyMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 7:
               return "MinimumPasswordLength";
            case 8:
               return "LockoutEnabled";
            case 9:
               return "LockoutThreshold";
            case 10:
               return "LockoutDuration";
            case 11:
               return "LockoutResetDuration";
            case 12:
               return "LockoutCacheSize";
            case 13:
               return "LockoutGCThreshold";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("LockoutCacheSize")) {
            return 12;
         } else if (var1.equals("LockoutDuration")) {
            return 10;
         } else if (var1.equals("LockoutGCThreshold")) {
            return 13;
         } else if (var1.equals("LockoutResetDuration")) {
            return 11;
         } else if (var1.equals("LockoutThreshold")) {
            return 9;
         } else if (var1.equals("MinimumPasswordLength")) {
            return 7;
         } else {
            return var1.equals("LockoutEnabled") ? 8 : super.getPropertyIndex(var1);
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

            if (this.bean.isMinimumPasswordLengthSet()) {
               var2.append("MinimumPasswordLength");
               var2.append(String.valueOf(this.bean.getMinimumPasswordLength()));
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
            PasswordPolicyMBeanImpl var2 = (PasswordPolicyMBeanImpl)var1;
            this.computeDiff("LockoutCacheSize", this.bean.getLockoutCacheSize(), var2.getLockoutCacheSize(), false);
            this.computeDiff("LockoutDuration", this.bean.getLockoutDuration(), var2.getLockoutDuration(), false);
            this.computeDiff("LockoutGCThreshold", this.bean.getLockoutGCThreshold(), var2.getLockoutGCThreshold(), false);
            this.computeDiff("LockoutResetDuration", this.bean.getLockoutResetDuration(), var2.getLockoutResetDuration(), false);
            this.computeDiff("LockoutThreshold", this.bean.getLockoutThreshold(), var2.getLockoutThreshold(), false);
            this.computeDiff("MinimumPasswordLength", this.bean.getMinimumPasswordLength(), var2.getMinimumPasswordLength(), false);
            this.computeDiff("LockoutEnabled", this.bean.isLockoutEnabled(), var2.isLockoutEnabled(), false);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            PasswordPolicyMBeanImpl var3 = (PasswordPolicyMBeanImpl)var1.getSourceBean();
            PasswordPolicyMBeanImpl var4 = (PasswordPolicyMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("LockoutCacheSize")) {
                  var3.setLockoutCacheSize(var4.getLockoutCacheSize());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 12);
               } else if (var5.equals("LockoutDuration")) {
                  var3.setLockoutDuration(var4.getLockoutDuration());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 10);
               } else if (var5.equals("LockoutGCThreshold")) {
                  var3.setLockoutGCThreshold(var4.getLockoutGCThreshold());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 13);
               } else if (var5.equals("LockoutResetDuration")) {
                  var3.setLockoutResetDuration(var4.getLockoutResetDuration());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 11);
               } else if (var5.equals("LockoutThreshold")) {
                  var3.setLockoutThreshold(var4.getLockoutThreshold());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 9);
               } else if (var5.equals("MinimumPasswordLength")) {
                  var3.setMinimumPasswordLength(var4.getMinimumPasswordLength());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 7);
               } else if (var5.equals("LockoutEnabled")) {
                  var3.setLockoutEnabled(var4.isLockoutEnabled());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 8);
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
            PasswordPolicyMBeanImpl var5 = (PasswordPolicyMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
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

            if ((var3 == null || !var3.contains("MinimumPasswordLength")) && this.bean.isMinimumPasswordLengthSet()) {
               var5.setMinimumPasswordLength(this.bean.getMinimumPasswordLength());
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
      }
   }
}
