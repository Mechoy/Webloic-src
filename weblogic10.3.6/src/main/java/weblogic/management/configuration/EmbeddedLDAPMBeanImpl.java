package weblogic.management.configuration;

import java.io.Serializable;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.Arrays;
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
import weblogic.utils.ArrayUtils;
import weblogic.utils.collections.CombinedIterator;

public class EmbeddedLDAPMBeanImpl extends ConfigurationMBeanImpl implements EmbeddedLDAPMBean, Serializable {
   private boolean _AnonymousBindAllowed;
   private int _BackupCopies;
   private int _BackupHour;
   private int _BackupMinute;
   private boolean _CacheEnabled;
   private int _CacheSize;
   private int _CacheTTL;
   private String _Credential;
   private byte[] _CredentialEncrypted;
   private boolean _MasterFirst;
   private boolean _RefreshReplicaAtStartup;
   private int _Timeout;
   private static SchemaHelper2 _schemaHelper;

   public EmbeddedLDAPMBeanImpl() {
      this._initializeProperty(-1);
   }

   public EmbeddedLDAPMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public String getCredential() {
      byte[] var1 = this.getCredentialEncrypted();
      return var1 == null ? null : this._decrypt("Credential", var1);
   }

   public boolean isCredentialSet() {
      return this.isCredentialEncryptedSet();
   }

   public void setCredential(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      this.setCredentialEncrypted(var1 == null ? null : this._encrypt("Credential", var1));
   }

   public byte[] getCredentialEncrypted() {
      return this._getHelper()._cloneArray(this._CredentialEncrypted);
   }

   public String getCredentialEncryptedAsString() {
      byte[] var1 = this.getCredentialEncrypted();
      return var1 == null ? null : new String(var1);
   }

   public boolean isCredentialEncryptedSet() {
      return this._isSet(8);
   }

   public void setCredentialEncryptedAsString(String var1) {
      try {
         byte[] var2 = var1 == null ? null : var1.getBytes();
         this.setCredentialEncrypted(var2);
      } catch (Exception var3) {
         if (var3 instanceof RuntimeException) {
            throw (RuntimeException)var3;
         } else {
            throw new UndeclaredThrowableException(var3);
         }
      }
   }

   public int getBackupHour() {
      return this._BackupHour;
   }

   public boolean isBackupHourSet() {
      return this._isSet(9);
   }

   public void setBackupHour(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("BackupHour", (long)var1, 0L, 23L);
      int var2 = this._BackupHour;
      this._BackupHour = var1;
      this._postSet(9, var2, var1);
   }

   public int getBackupMinute() {
      return this._BackupMinute;
   }

   public boolean isBackupMinuteSet() {
      return this._isSet(10);
   }

   public void setBackupMinute(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("BackupMinute", (long)var1, 0L, 59L);
      int var2 = this._BackupMinute;
      this._BackupMinute = var1;
      this._postSet(10, var2, var1);
   }

   public int getBackupCopies() {
      return this._BackupCopies;
   }

   public boolean isBackupCopiesSet() {
      return this._isSet(11);
   }

   public void setBackupCopies(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("BackupCopies", (long)var1, 0L, 65534L);
      int var2 = this._BackupCopies;
      this._BackupCopies = var1;
      this._postSet(11, var2, var1);
   }

   public boolean isCacheEnabled() {
      return this._CacheEnabled;
   }

   public boolean isCacheEnabledSet() {
      return this._isSet(12);
   }

   public void setCacheEnabled(boolean var1) {
      boolean var2 = this._CacheEnabled;
      this._CacheEnabled = var1;
      this._postSet(12, var2, var1);
   }

   public int getCacheSize() {
      return this._CacheSize;
   }

   public boolean isCacheSizeSet() {
      return this._isSet(13);
   }

   public void setCacheSize(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkMin("CacheSize", var1, 0);
      int var2 = this._CacheSize;
      this._CacheSize = var1;
      this._postSet(13, var2, var1);
   }

   public int getCacheTTL() {
      return this._CacheTTL;
   }

   public boolean isCacheTTLSet() {
      return this._isSet(14);
   }

   public void setCacheTTL(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkMin("CacheTTL", var1, 0);
      int var2 = this._CacheTTL;
      this._CacheTTL = var1;
      this._postSet(14, var2, var1);
   }

   public boolean isRefreshReplicaAtStartup() {
      return this._RefreshReplicaAtStartup;
   }

   public boolean isRefreshReplicaAtStartupSet() {
      return this._isSet(15);
   }

   public void setRefreshReplicaAtStartup(boolean var1) {
      boolean var2 = this._RefreshReplicaAtStartup;
      this._RefreshReplicaAtStartup = var1;
      this._postSet(15, var2, var1);
   }

   public boolean isMasterFirst() {
      return this._MasterFirst;
   }

   public boolean isMasterFirstSet() {
      return this._isSet(16);
   }

   public void setMasterFirst(boolean var1) {
      boolean var2 = this._MasterFirst;
      this._MasterFirst = var1;
      this._postSet(16, var2, var1);
   }

   public int getTimeout() {
      return this._Timeout;
   }

   public boolean isTimeoutSet() {
      return this._isSet(17);
   }

   public void setTimeout(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkMin("Timeout", var1, 0);
      int var2 = this._Timeout;
      this._Timeout = var1;
      this._postSet(17, var2, var1);
   }

   public boolean isAnonymousBindAllowed() {
      return this._AnonymousBindAllowed;
   }

   public boolean isAnonymousBindAllowedSet() {
      return this._isSet(18);
   }

   public void setAnonymousBindAllowed(boolean var1) {
      boolean var2 = this._AnonymousBindAllowed;
      this._AnonymousBindAllowed = var1;
      this._postSet(18, var2, var1);
   }

   public Object _getKey() {
      return super._getKey();
   }

   public void _validate() throws IllegalArgumentException {
      super._validate();
   }

   public void setCredentialEncrypted(byte[] var1) {
      byte[] var2 = this._CredentialEncrypted;
      if (this._isProductionModeEnabled() && var1 != null && !this._isEncrypted(var1)) {
         throw new IllegalArgumentException("In production mode, it's not allowed to set a clear text value to the property: CredentialEncrypted of EmbeddedLDAPMBean");
      } else {
         this._getHelper()._clearArray(this._CredentialEncrypted);
         this._CredentialEncrypted = this._getHelper()._cloneArray(var1);
         this._postSet(8, var2, var1);
      }
   }

   protected void _unSet(int var1) {
      if (!this._initializeProperty(var1)) {
         super._unSet(var1);
      } else {
         this._markSet(var1, false);
         if (var1 == 7) {
            this._markSet(8, false);
         }
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
         var1 = 11;
      }

      try {
         switch (var1) {
            case 11:
               this._BackupCopies = 7;
               if (var2) {
                  break;
               }
            case 9:
               this._BackupHour = 23;
               if (var2) {
                  break;
               }
            case 10:
               this._BackupMinute = 5;
               if (var2) {
                  break;
               }
            case 13:
               this._CacheSize = 32;
               if (var2) {
                  break;
               }
            case 14:
               this._CacheTTL = 60;
               if (var2) {
                  break;
               }
            case 7:
               this._CredentialEncrypted = null;
               if (var2) {
                  break;
               }
            case 8:
               this._CredentialEncrypted = null;
               if (var2) {
                  break;
               }
            case 17:
               this._Timeout = 0;
               if (var2) {
                  break;
               }
            case 18:
               this._AnonymousBindAllowed = false;
               if (var2) {
                  break;
               }
            case 12:
               this._CacheEnabled = true;
               if (var2) {
                  break;
               }
            case 16:
               this._MasterFirst = false;
               if (var2) {
                  break;
               }
            case 15:
               this._RefreshReplicaAtStartup = false;
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
      return "EmbeddedLDAP";
   }

   public void putValue(String var1, Object var2) {
      boolean var4;
      if (var1.equals("AnonymousBindAllowed")) {
         var4 = this._AnonymousBindAllowed;
         this._AnonymousBindAllowed = (Boolean)var2;
         this._postSet(18, var4, this._AnonymousBindAllowed);
      } else {
         int var3;
         if (var1.equals("BackupCopies")) {
            var3 = this._BackupCopies;
            this._BackupCopies = (Integer)var2;
            this._postSet(11, var3, this._BackupCopies);
         } else if (var1.equals("BackupHour")) {
            var3 = this._BackupHour;
            this._BackupHour = (Integer)var2;
            this._postSet(9, var3, this._BackupHour);
         } else if (var1.equals("BackupMinute")) {
            var3 = this._BackupMinute;
            this._BackupMinute = (Integer)var2;
            this._postSet(10, var3, this._BackupMinute);
         } else if (var1.equals("CacheEnabled")) {
            var4 = this._CacheEnabled;
            this._CacheEnabled = (Boolean)var2;
            this._postSet(12, var4, this._CacheEnabled);
         } else if (var1.equals("CacheSize")) {
            var3 = this._CacheSize;
            this._CacheSize = (Integer)var2;
            this._postSet(13, var3, this._CacheSize);
         } else if (var1.equals("CacheTTL")) {
            var3 = this._CacheTTL;
            this._CacheTTL = (Integer)var2;
            this._postSet(14, var3, this._CacheTTL);
         } else if (var1.equals("Credential")) {
            String var6 = this._Credential;
            this._Credential = (String)var2;
            this._postSet(7, var6, this._Credential);
         } else if (var1.equals("CredentialEncrypted")) {
            byte[] var5 = this._CredentialEncrypted;
            this._CredentialEncrypted = (byte[])((byte[])var2);
            this._postSet(8, var5, this._CredentialEncrypted);
         } else if (var1.equals("MasterFirst")) {
            var4 = this._MasterFirst;
            this._MasterFirst = (Boolean)var2;
            this._postSet(16, var4, this._MasterFirst);
         } else if (var1.equals("RefreshReplicaAtStartup")) {
            var4 = this._RefreshReplicaAtStartup;
            this._RefreshReplicaAtStartup = (Boolean)var2;
            this._postSet(15, var4, this._RefreshReplicaAtStartup);
         } else if (var1.equals("Timeout")) {
            var3 = this._Timeout;
            this._Timeout = (Integer)var2;
            this._postSet(17, var3, this._Timeout);
         } else {
            super.putValue(var1, var2);
         }
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("AnonymousBindAllowed")) {
         return new Boolean(this._AnonymousBindAllowed);
      } else if (var1.equals("BackupCopies")) {
         return new Integer(this._BackupCopies);
      } else if (var1.equals("BackupHour")) {
         return new Integer(this._BackupHour);
      } else if (var1.equals("BackupMinute")) {
         return new Integer(this._BackupMinute);
      } else if (var1.equals("CacheEnabled")) {
         return new Boolean(this._CacheEnabled);
      } else if (var1.equals("CacheSize")) {
         return new Integer(this._CacheSize);
      } else if (var1.equals("CacheTTL")) {
         return new Integer(this._CacheTTL);
      } else if (var1.equals("Credential")) {
         return this._Credential;
      } else if (var1.equals("CredentialEncrypted")) {
         return this._CredentialEncrypted;
      } else if (var1.equals("MasterFirst")) {
         return new Boolean(this._MasterFirst);
      } else if (var1.equals("RefreshReplicaAtStartup")) {
         return new Boolean(this._RefreshReplicaAtStartup);
      } else {
         return var1.equals("Timeout") ? new Integer(this._Timeout) : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends ConfigurationMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 7:
               if (var1.equals("timeout")) {
                  return 17;
               }
               break;
            case 8:
               if (var1.equals("cachettl")) {
                  return 14;
               }
            case 9:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 21:
            case 23:
            case 24:
            case 25:
            default:
               break;
            case 10:
               if (var1.equals("cache-size")) {
                  return 13;
               }

               if (var1.equals("credential")) {
                  return 7;
               }
               break;
            case 11:
               if (var1.equals("backup-hour")) {
                  return 9;
               }
               break;
            case 12:
               if (var1.equals("master-first")) {
                  return 16;
               }
               break;
            case 13:
               if (var1.equals("backup-copies")) {
                  return 11;
               }

               if (var1.equals("backup-minute")) {
                  return 10;
               }

               if (var1.equals("cache-enabled")) {
                  return 12;
               }
               break;
            case 20:
               if (var1.equals("credential-encrypted")) {
                  return 8;
               }
               break;
            case 22:
               if (var1.equals("anonymous-bind-allowed")) {
                  return 18;
               }
               break;
            case 26:
               if (var1.equals("refresh-replica-at-startup")) {
                  return 15;
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
               return "credential";
            case 8:
               return "credential-encrypted";
            case 9:
               return "backup-hour";
            case 10:
               return "backup-minute";
            case 11:
               return "backup-copies";
            case 12:
               return "cache-enabled";
            case 13:
               return "cache-size";
            case 14:
               return "cachettl";
            case 15:
               return "refresh-replica-at-startup";
            case 16:
               return "master-first";
            case 17:
               return "timeout";
            case 18:
               return "anonymous-bind-allowed";
            default:
               return super.getElementName(var1);
         }
      }

      public boolean isArray(int var1) {
         switch (var1) {
            default:
               return super.isArray(var1);
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
      private EmbeddedLDAPMBeanImpl bean;

      protected Helper(EmbeddedLDAPMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 7:
               return "Credential";
            case 8:
               return "CredentialEncrypted";
            case 9:
               return "BackupHour";
            case 10:
               return "BackupMinute";
            case 11:
               return "BackupCopies";
            case 12:
               return "CacheEnabled";
            case 13:
               return "CacheSize";
            case 14:
               return "CacheTTL";
            case 15:
               return "RefreshReplicaAtStartup";
            case 16:
               return "MasterFirst";
            case 17:
               return "Timeout";
            case 18:
               return "AnonymousBindAllowed";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("BackupCopies")) {
            return 11;
         } else if (var1.equals("BackupHour")) {
            return 9;
         } else if (var1.equals("BackupMinute")) {
            return 10;
         } else if (var1.equals("CacheSize")) {
            return 13;
         } else if (var1.equals("CacheTTL")) {
            return 14;
         } else if (var1.equals("Credential")) {
            return 7;
         } else if (var1.equals("CredentialEncrypted")) {
            return 8;
         } else if (var1.equals("Timeout")) {
            return 17;
         } else if (var1.equals("AnonymousBindAllowed")) {
            return 18;
         } else if (var1.equals("CacheEnabled")) {
            return 12;
         } else if (var1.equals("MasterFirst")) {
            return 16;
         } else {
            return var1.equals("RefreshReplicaAtStartup") ? 15 : super.getPropertyIndex(var1);
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
            if (this.bean.isBackupCopiesSet()) {
               var2.append("BackupCopies");
               var2.append(String.valueOf(this.bean.getBackupCopies()));
            }

            if (this.bean.isBackupHourSet()) {
               var2.append("BackupHour");
               var2.append(String.valueOf(this.bean.getBackupHour()));
            }

            if (this.bean.isBackupMinuteSet()) {
               var2.append("BackupMinute");
               var2.append(String.valueOf(this.bean.getBackupMinute()));
            }

            if (this.bean.isCacheSizeSet()) {
               var2.append("CacheSize");
               var2.append(String.valueOf(this.bean.getCacheSize()));
            }

            if (this.bean.isCacheTTLSet()) {
               var2.append("CacheTTL");
               var2.append(String.valueOf(this.bean.getCacheTTL()));
            }

            if (this.bean.isCredentialSet()) {
               var2.append("Credential");
               var2.append(String.valueOf(this.bean.getCredential()));
            }

            if (this.bean.isCredentialEncryptedSet()) {
               var2.append("CredentialEncrypted");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getCredentialEncrypted())));
            }

            if (this.bean.isTimeoutSet()) {
               var2.append("Timeout");
               var2.append(String.valueOf(this.bean.getTimeout()));
            }

            if (this.bean.isAnonymousBindAllowedSet()) {
               var2.append("AnonymousBindAllowed");
               var2.append(String.valueOf(this.bean.isAnonymousBindAllowed()));
            }

            if (this.bean.isCacheEnabledSet()) {
               var2.append("CacheEnabled");
               var2.append(String.valueOf(this.bean.isCacheEnabled()));
            }

            if (this.bean.isMasterFirstSet()) {
               var2.append("MasterFirst");
               var2.append(String.valueOf(this.bean.isMasterFirst()));
            }

            if (this.bean.isRefreshReplicaAtStartupSet()) {
               var2.append("RefreshReplicaAtStartup");
               var2.append(String.valueOf(this.bean.isRefreshReplicaAtStartup()));
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
            EmbeddedLDAPMBeanImpl var2 = (EmbeddedLDAPMBeanImpl)var1;
            this.computeDiff("BackupCopies", this.bean.getBackupCopies(), var2.getBackupCopies(), false);
            this.computeDiff("BackupHour", this.bean.getBackupHour(), var2.getBackupHour(), false);
            this.computeDiff("BackupMinute", this.bean.getBackupMinute(), var2.getBackupMinute(), false);
            this.computeDiff("CacheSize", this.bean.getCacheSize(), var2.getCacheSize(), false);
            this.computeDiff("CacheTTL", this.bean.getCacheTTL(), var2.getCacheTTL(), false);
            this.computeDiff("CredentialEncrypted", this.bean.getCredentialEncrypted(), var2.getCredentialEncrypted(), false);
            this.computeDiff("Timeout", this.bean.getTimeout(), var2.getTimeout(), false);
            this.computeDiff("AnonymousBindAllowed", this.bean.isAnonymousBindAllowed(), var2.isAnonymousBindAllowed(), false);
            this.computeDiff("CacheEnabled", this.bean.isCacheEnabled(), var2.isCacheEnabled(), false);
            this.computeDiff("MasterFirst", this.bean.isMasterFirst(), var2.isMasterFirst(), false);
            this.computeDiff("RefreshReplicaAtStartup", this.bean.isRefreshReplicaAtStartup(), var2.isRefreshReplicaAtStartup(), false);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            EmbeddedLDAPMBeanImpl var3 = (EmbeddedLDAPMBeanImpl)var1.getSourceBean();
            EmbeddedLDAPMBeanImpl var4 = (EmbeddedLDAPMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("BackupCopies")) {
                  var3.setBackupCopies(var4.getBackupCopies());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 11);
               } else if (var5.equals("BackupHour")) {
                  var3.setBackupHour(var4.getBackupHour());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 9);
               } else if (var5.equals("BackupMinute")) {
                  var3.setBackupMinute(var4.getBackupMinute());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 10);
               } else if (var5.equals("CacheSize")) {
                  var3.setCacheSize(var4.getCacheSize());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 13);
               } else if (var5.equals("CacheTTL")) {
                  var3.setCacheTTL(var4.getCacheTTL());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 14);
               } else if (!var5.equals("Credential")) {
                  if (var5.equals("CredentialEncrypted")) {
                     var3.setCredentialEncrypted(var4.getCredentialEncrypted());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 8);
                  } else if (var5.equals("Timeout")) {
                     var3.setTimeout(var4.getTimeout());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 17);
                  } else if (var5.equals("AnonymousBindAllowed")) {
                     var3.setAnonymousBindAllowed(var4.isAnonymousBindAllowed());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 18);
                  } else if (var5.equals("CacheEnabled")) {
                     var3.setCacheEnabled(var4.isCacheEnabled());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 12);
                  } else if (var5.equals("MasterFirst")) {
                     var3.setMasterFirst(var4.isMasterFirst());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 16);
                  } else if (var5.equals("RefreshReplicaAtStartup")) {
                     var3.setRefreshReplicaAtStartup(var4.isRefreshReplicaAtStartup());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 15);
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
            EmbeddedLDAPMBeanImpl var5 = (EmbeddedLDAPMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("BackupCopies")) && this.bean.isBackupCopiesSet()) {
               var5.setBackupCopies(this.bean.getBackupCopies());
            }

            if ((var3 == null || !var3.contains("BackupHour")) && this.bean.isBackupHourSet()) {
               var5.setBackupHour(this.bean.getBackupHour());
            }

            if ((var3 == null || !var3.contains("BackupMinute")) && this.bean.isBackupMinuteSet()) {
               var5.setBackupMinute(this.bean.getBackupMinute());
            }

            if ((var3 == null || !var3.contains("CacheSize")) && this.bean.isCacheSizeSet()) {
               var5.setCacheSize(this.bean.getCacheSize());
            }

            if ((var3 == null || !var3.contains("CacheTTL")) && this.bean.isCacheTTLSet()) {
               var5.setCacheTTL(this.bean.getCacheTTL());
            }

            if ((var3 == null || !var3.contains("CredentialEncrypted")) && this.bean.isCredentialEncryptedSet()) {
               byte[] var4 = this.bean.getCredentialEncrypted();
               var5.setCredentialEncrypted(var4 == null ? null : (byte[])((byte[])((byte[])((byte[])var4)).clone()));
            }

            if ((var3 == null || !var3.contains("Timeout")) && this.bean.isTimeoutSet()) {
               var5.setTimeout(this.bean.getTimeout());
            }

            if ((var3 == null || !var3.contains("AnonymousBindAllowed")) && this.bean.isAnonymousBindAllowedSet()) {
               var5.setAnonymousBindAllowed(this.bean.isAnonymousBindAllowed());
            }

            if ((var3 == null || !var3.contains("CacheEnabled")) && this.bean.isCacheEnabledSet()) {
               var5.setCacheEnabled(this.bean.isCacheEnabled());
            }

            if ((var3 == null || !var3.contains("MasterFirst")) && this.bean.isMasterFirstSet()) {
               var5.setMasterFirst(this.bean.isMasterFirst());
            }

            if ((var3 == null || !var3.contains("RefreshReplicaAtStartup")) && this.bean.isRefreshReplicaAtStartupSet()) {
               var5.setRefreshReplicaAtStartup(this.bean.isRefreshReplicaAtStartup());
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
