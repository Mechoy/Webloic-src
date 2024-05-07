package weblogic.management.utils;

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
import weblogic.descriptor.beangen.LegalChecks;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBeanHelper;
import weblogic.descriptor.internal.AbstractSchemaHelper2;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.management.commo.AbstractCommoConfigurationBean;
import weblogic.utils.ArrayUtils;
import weblogic.utils.collections.CombinedIterator;

public class LDAPServerMBeanImpl extends AbstractCommoConfigurationBean implements LDAPServerMBean, Serializable {
   private boolean _BindAnonymouslyOnReferrals;
   private boolean _CacheEnabled;
   private int _CacheSize;
   private int _CacheTTL;
   private int _ConnectTimeout;
   private int _ConnectionPoolSize;
   private int _ConnectionRetryLimit;
   private String _Credential;
   private byte[] _CredentialEncrypted;
   private boolean _FollowReferrals;
   private String _Host;
   private int _ParallelConnectDelay;
   private int _Port;
   private String _Principal;
   private int _ResultsTimeLimit;
   private boolean _SSLEnabled;
   private static SchemaHelper2 _schemaHelper;

   public LDAPServerMBeanImpl() {
      this._initializeProperty(-1);
   }

   public LDAPServerMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public String getHost() {
      return this._Host;
   }

   public boolean isHostSet() {
      return this._isSet(2);
   }

   public void setHost(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._Host;
      this._Host = var1;
      this._postSet(2, var2, var1);
   }

   public int getPort() {
      return this._Port;
   }

   public boolean isPortSet() {
      return this._isSet(3);
   }

   public void setPort(int var1) throws InvalidAttributeValueException {
      LegalChecks.checkInRange("Port", (long)var1, 1L, 65534L);
      int var2 = this._Port;
      this._Port = var1;
      this._postSet(3, var2, var1);
   }

   public boolean isSSLEnabled() {
      return this._SSLEnabled;
   }

   public boolean isSSLEnabledSet() {
      return this._isSet(4);
   }

   public void setSSLEnabled(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this._SSLEnabled;
      this._SSLEnabled = var1;
      this._postSet(4, var2, var1);
   }

   public String getPrincipal() {
      return this._Principal;
   }

   public boolean isPrincipalSet() {
      return this._isSet(5);
   }

   public void setPrincipal(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._Principal;
      this._Principal = var1;
      this._postSet(5, var2, var1);
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
      return this._isSet(7);
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

   public boolean isCacheEnabled() {
      return this._CacheEnabled;
   }

   public boolean isCacheEnabledSet() {
      return this._isSet(8);
   }

   public void setCacheEnabled(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this._CacheEnabled;
      this._CacheEnabled = var1;
      this._postSet(8, var2, var1);
   }

   public int getCacheSize() {
      return this._CacheSize;
   }

   public boolean isCacheSizeSet() {
      return this._isSet(9);
   }

   public void setCacheSize(int var1) throws InvalidAttributeValueException {
      LegalChecks.checkMin("CacheSize", var1, 0);
      int var2 = this._CacheSize;
      this._CacheSize = var1;
      this._postSet(9, var2, var1);
   }

   public int getCacheTTL() {
      return this._CacheTTL;
   }

   public boolean isCacheTTLSet() {
      return this._isSet(10);
   }

   public void setCacheTTL(int var1) throws InvalidAttributeValueException {
      LegalChecks.checkMin("CacheTTL", var1, 0);
      int var2 = this._CacheTTL;
      this._CacheTTL = var1;
      this._postSet(10, var2, var1);
   }

   public boolean isFollowReferrals() {
      return this._FollowReferrals;
   }

   public boolean isFollowReferralsSet() {
      return this._isSet(11);
   }

   public void setFollowReferrals(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this._FollowReferrals;
      this._FollowReferrals = var1;
      this._postSet(11, var2, var1);
   }

   public boolean isBindAnonymouslyOnReferrals() {
      return this._BindAnonymouslyOnReferrals;
   }

   public boolean isBindAnonymouslyOnReferralsSet() {
      return this._isSet(12);
   }

   public void setBindAnonymouslyOnReferrals(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this._BindAnonymouslyOnReferrals;
      this._BindAnonymouslyOnReferrals = var1;
      this._postSet(12, var2, var1);
   }

   public int getResultsTimeLimit() {
      return this._ResultsTimeLimit;
   }

   public boolean isResultsTimeLimitSet() {
      return this._isSet(13);
   }

   public void setResultsTimeLimit(int var1) throws InvalidAttributeValueException {
      int var2 = this._ResultsTimeLimit;
      this._ResultsTimeLimit = var1;
      this._postSet(13, var2, var1);
   }

   public int getConnectTimeout() {
      return this._ConnectTimeout;
   }

   public boolean isConnectTimeoutSet() {
      return this._isSet(14);
   }

   public void setConnectTimeout(int var1) throws InvalidAttributeValueException {
      int var2 = this._ConnectTimeout;
      this._ConnectTimeout = var1;
      this._postSet(14, var2, var1);
   }

   public int getParallelConnectDelay() {
      return this._ParallelConnectDelay;
   }

   public boolean isParallelConnectDelaySet() {
      return this._isSet(15);
   }

   public void setParallelConnectDelay(int var1) throws InvalidAttributeValueException {
      int var2 = this._ParallelConnectDelay;
      this._ParallelConnectDelay = var1;
      this._postSet(15, var2, var1);
   }

   public int getConnectionRetryLimit() {
      return this._ConnectionRetryLimit;
   }

   public boolean isConnectionRetryLimitSet() {
      return this._isSet(16);
   }

   public void setConnectionRetryLimit(int var1) throws InvalidAttributeValueException {
      int var2 = this._ConnectionRetryLimit;
      this._ConnectionRetryLimit = var1;
      this._postSet(16, var2, var1);
   }

   public int getConnectionPoolSize() {
      return this._ConnectionPoolSize;
   }

   public boolean isConnectionPoolSizeSet() {
      return this._isSet(17);
   }

   public void setConnectionPoolSize(int var1) throws InvalidAttributeValueException {
      int var2 = this._ConnectionPoolSize;
      this._ConnectionPoolSize = var1;
      this._postSet(17, var2, var1);
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
         throw new IllegalArgumentException("In production mode, it's not allowed to set a clear text value to the property: CredentialEncrypted of LDAPServerMBean");
      } else {
         this._getHelper()._clearArray(this._CredentialEncrypted);
         this._CredentialEncrypted = this._getHelper()._cloneArray(var1);
         this._postSet(7, var2, var1);
      }
   }

   protected void _unSet(int var1) {
      if (!this._initializeProperty(var1)) {
         super._unSet(var1);
      } else {
         this._markSet(var1, false);
         if (var1 == 6) {
            this._markSet(7, false);
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
         var1 = 9;
      }

      try {
         switch (var1) {
            case 9:
               this._CacheSize = 32;
               if (var2) {
                  break;
               }
            case 10:
               this._CacheTTL = 60;
               if (var2) {
                  break;
               }
            case 14:
               this._ConnectTimeout = 0;
               if (var2) {
                  break;
               }
            case 17:
               this._ConnectionPoolSize = 6;
               if (var2) {
                  break;
               }
            case 16:
               this._ConnectionRetryLimit = 1;
               if (var2) {
                  break;
               }
            case 6:
               this._CredentialEncrypted = null;
               if (var2) {
                  break;
               }
            case 7:
               this._CredentialEncrypted = null;
               if (var2) {
                  break;
               }
            case 2:
               this._Host = "localhost";
               if (var2) {
                  break;
               }
            case 15:
               this._ParallelConnectDelay = 0;
               if (var2) {
                  break;
               }
            case 3:
               this._Port = 389;
               if (var2) {
                  break;
               }
            case 5:
               this._Principal = null;
               if (var2) {
                  break;
               }
            case 13:
               this._ResultsTimeLimit = 0;
               if (var2) {
                  break;
               }
            case 12:
               this._BindAnonymouslyOnReferrals = false;
               if (var2) {
                  break;
               }
            case 8:
               this._CacheEnabled = true;
               if (var2) {
                  break;
               }
            case 11:
               this._FollowReferrals = true;
               if (var2) {
                  break;
               }
            case 4:
               this._SSLEnabled = false;
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
      return "weblogic.management.utils.LDAPServerMBean";
   }

   public static class SchemaHelper2 extends AbstractSchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 4:
               if (var1.equals("host")) {
                  return 2;
               }

               if (var1.equals("port")) {
                  return 3;
               }
            case 5:
            case 6:
            case 7:
            case 12:
            case 14:
            case 17:
            case 19:
            case 21:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            default:
               break;
            case 8:
               if (var1.equals("cachettl")) {
                  return 10;
               }
               break;
            case 9:
               if (var1.equals("principal")) {
                  return 5;
               }
               break;
            case 10:
               if (var1.equals("cache-size")) {
                  return 9;
               }

               if (var1.equals("credential")) {
                  return 6;
               }
               break;
            case 11:
               if (var1.equals("ssl-enabled")) {
                  return 4;
               }
               break;
            case 13:
               if (var1.equals("cache-enabled")) {
                  return 8;
               }
               break;
            case 15:
               if (var1.equals("connect-timeout")) {
                  return 14;
               }
               break;
            case 16:
               if (var1.equals("follow-referrals")) {
                  return 11;
               }
               break;
            case 18:
               if (var1.equals("results-time-limit")) {
                  return 13;
               }
               break;
            case 20:
               if (var1.equals("connection-pool-size")) {
                  return 17;
               }

               if (var1.equals("credential-encrypted")) {
                  return 7;
               }
               break;
            case 22:
               if (var1.equals("connection-retry-limit")) {
                  return 16;
               }

               if (var1.equals("parallel-connect-delay")) {
                  return 15;
               }
               break;
            case 29:
               if (var1.equals("bind-anonymously-on-referrals")) {
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
               return "host";
            case 3:
               return "port";
            case 4:
               return "ssl-enabled";
            case 5:
               return "principal";
            case 6:
               return "credential";
            case 7:
               return "credential-encrypted";
            case 8:
               return "cache-enabled";
            case 9:
               return "cache-size";
            case 10:
               return "cachettl";
            case 11:
               return "follow-referrals";
            case 12:
               return "bind-anonymously-on-referrals";
            case 13:
               return "results-time-limit";
            case 14:
               return "connect-timeout";
            case 15:
               return "parallel-connect-delay";
            case 16:
               return "connection-retry-limit";
            case 17:
               return "connection-pool-size";
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
   }

   protected static class Helper extends AbstractCommoConfigurationBean.Helper {
      private LDAPServerMBeanImpl bean;

      protected Helper(LDAPServerMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 2:
               return "Host";
            case 3:
               return "Port";
            case 4:
               return "SSLEnabled";
            case 5:
               return "Principal";
            case 6:
               return "Credential";
            case 7:
               return "CredentialEncrypted";
            case 8:
               return "CacheEnabled";
            case 9:
               return "CacheSize";
            case 10:
               return "CacheTTL";
            case 11:
               return "FollowReferrals";
            case 12:
               return "BindAnonymouslyOnReferrals";
            case 13:
               return "ResultsTimeLimit";
            case 14:
               return "ConnectTimeout";
            case 15:
               return "ParallelConnectDelay";
            case 16:
               return "ConnectionRetryLimit";
            case 17:
               return "ConnectionPoolSize";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("CacheSize")) {
            return 9;
         } else if (var1.equals("CacheTTL")) {
            return 10;
         } else if (var1.equals("ConnectTimeout")) {
            return 14;
         } else if (var1.equals("ConnectionPoolSize")) {
            return 17;
         } else if (var1.equals("ConnectionRetryLimit")) {
            return 16;
         } else if (var1.equals("Credential")) {
            return 6;
         } else if (var1.equals("CredentialEncrypted")) {
            return 7;
         } else if (var1.equals("Host")) {
            return 2;
         } else if (var1.equals("ParallelConnectDelay")) {
            return 15;
         } else if (var1.equals("Port")) {
            return 3;
         } else if (var1.equals("Principal")) {
            return 5;
         } else if (var1.equals("ResultsTimeLimit")) {
            return 13;
         } else if (var1.equals("BindAnonymouslyOnReferrals")) {
            return 12;
         } else if (var1.equals("CacheEnabled")) {
            return 8;
         } else if (var1.equals("FollowReferrals")) {
            return 11;
         } else {
            return var1.equals("SSLEnabled") ? 4 : super.getPropertyIndex(var1);
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
            if (this.bean.isCacheSizeSet()) {
               var2.append("CacheSize");
               var2.append(String.valueOf(this.bean.getCacheSize()));
            }

            if (this.bean.isCacheTTLSet()) {
               var2.append("CacheTTL");
               var2.append(String.valueOf(this.bean.getCacheTTL()));
            }

            if (this.bean.isConnectTimeoutSet()) {
               var2.append("ConnectTimeout");
               var2.append(String.valueOf(this.bean.getConnectTimeout()));
            }

            if (this.bean.isConnectionPoolSizeSet()) {
               var2.append("ConnectionPoolSize");
               var2.append(String.valueOf(this.bean.getConnectionPoolSize()));
            }

            if (this.bean.isConnectionRetryLimitSet()) {
               var2.append("ConnectionRetryLimit");
               var2.append(String.valueOf(this.bean.getConnectionRetryLimit()));
            }

            if (this.bean.isCredentialSet()) {
               var2.append("Credential");
               var2.append(String.valueOf(this.bean.getCredential()));
            }

            if (this.bean.isCredentialEncryptedSet()) {
               var2.append("CredentialEncrypted");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getCredentialEncrypted())));
            }

            if (this.bean.isHostSet()) {
               var2.append("Host");
               var2.append(String.valueOf(this.bean.getHost()));
            }

            if (this.bean.isParallelConnectDelaySet()) {
               var2.append("ParallelConnectDelay");
               var2.append(String.valueOf(this.bean.getParallelConnectDelay()));
            }

            if (this.bean.isPortSet()) {
               var2.append("Port");
               var2.append(String.valueOf(this.bean.getPort()));
            }

            if (this.bean.isPrincipalSet()) {
               var2.append("Principal");
               var2.append(String.valueOf(this.bean.getPrincipal()));
            }

            if (this.bean.isResultsTimeLimitSet()) {
               var2.append("ResultsTimeLimit");
               var2.append(String.valueOf(this.bean.getResultsTimeLimit()));
            }

            if (this.bean.isBindAnonymouslyOnReferralsSet()) {
               var2.append("BindAnonymouslyOnReferrals");
               var2.append(String.valueOf(this.bean.isBindAnonymouslyOnReferrals()));
            }

            if (this.bean.isCacheEnabledSet()) {
               var2.append("CacheEnabled");
               var2.append(String.valueOf(this.bean.isCacheEnabled()));
            }

            if (this.bean.isFollowReferralsSet()) {
               var2.append("FollowReferrals");
               var2.append(String.valueOf(this.bean.isFollowReferrals()));
            }

            if (this.bean.isSSLEnabledSet()) {
               var2.append("SSLEnabled");
               var2.append(String.valueOf(this.bean.isSSLEnabled()));
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
            LDAPServerMBeanImpl var2 = (LDAPServerMBeanImpl)var1;
            this.computeDiff("CacheSize", this.bean.getCacheSize(), var2.getCacheSize(), false);
            this.computeDiff("CacheTTL", this.bean.getCacheTTL(), var2.getCacheTTL(), false);
            this.computeDiff("ConnectTimeout", this.bean.getConnectTimeout(), var2.getConnectTimeout(), false);
            this.computeDiff("ConnectionPoolSize", this.bean.getConnectionPoolSize(), var2.getConnectionPoolSize(), true);
            this.computeDiff("ConnectionRetryLimit", this.bean.getConnectionRetryLimit(), var2.getConnectionRetryLimit(), false);
            this.computeDiff("CredentialEncrypted", this.bean.getCredentialEncrypted(), var2.getCredentialEncrypted(), false);
            this.computeDiff("Host", this.bean.getHost(), var2.getHost(), false);
            this.computeDiff("ParallelConnectDelay", this.bean.getParallelConnectDelay(), var2.getParallelConnectDelay(), false);
            this.computeDiff("Port", this.bean.getPort(), var2.getPort(), false);
            this.computeDiff("Principal", this.bean.getPrincipal(), var2.getPrincipal(), false);
            this.computeDiff("ResultsTimeLimit", this.bean.getResultsTimeLimit(), var2.getResultsTimeLimit(), false);
            this.computeDiff("BindAnonymouslyOnReferrals", this.bean.isBindAnonymouslyOnReferrals(), var2.isBindAnonymouslyOnReferrals(), false);
            this.computeDiff("CacheEnabled", this.bean.isCacheEnabled(), var2.isCacheEnabled(), false);
            this.computeDiff("FollowReferrals", this.bean.isFollowReferrals(), var2.isFollowReferrals(), false);
            this.computeDiff("SSLEnabled", this.bean.isSSLEnabled(), var2.isSSLEnabled(), false);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            LDAPServerMBeanImpl var3 = (LDAPServerMBeanImpl)var1.getSourceBean();
            LDAPServerMBeanImpl var4 = (LDAPServerMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("CacheSize")) {
                  var3.setCacheSize(var4.getCacheSize());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 9);
               } else if (var5.equals("CacheTTL")) {
                  var3.setCacheTTL(var4.getCacheTTL());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 10);
               } else if (var5.equals("ConnectTimeout")) {
                  var3.setConnectTimeout(var4.getConnectTimeout());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 14);
               } else if (var5.equals("ConnectionPoolSize")) {
                  var3.setConnectionPoolSize(var4.getConnectionPoolSize());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 17);
               } else if (var5.equals("ConnectionRetryLimit")) {
                  var3.setConnectionRetryLimit(var4.getConnectionRetryLimit());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 16);
               } else if (!var5.equals("Credential")) {
                  if (var5.equals("CredentialEncrypted")) {
                     var3.setCredentialEncrypted(var4.getCredentialEncrypted());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 7);
                  } else if (var5.equals("Host")) {
                     var3.setHost(var4.getHost());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 2);
                  } else if (var5.equals("ParallelConnectDelay")) {
                     var3.setParallelConnectDelay(var4.getParallelConnectDelay());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 15);
                  } else if (var5.equals("Port")) {
                     var3.setPort(var4.getPort());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 3);
                  } else if (var5.equals("Principal")) {
                     var3.setPrincipal(var4.getPrincipal());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 5);
                  } else if (var5.equals("ResultsTimeLimit")) {
                     var3.setResultsTimeLimit(var4.getResultsTimeLimit());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 13);
                  } else if (var5.equals("BindAnonymouslyOnReferrals")) {
                     var3.setBindAnonymouslyOnReferrals(var4.isBindAnonymouslyOnReferrals());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 12);
                  } else if (var5.equals("CacheEnabled")) {
                     var3.setCacheEnabled(var4.isCacheEnabled());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 8);
                  } else if (var5.equals("FollowReferrals")) {
                     var3.setFollowReferrals(var4.isFollowReferrals());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 11);
                  } else if (var5.equals("SSLEnabled")) {
                     var3.setSSLEnabled(var4.isSSLEnabled());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 4);
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
            LDAPServerMBeanImpl var5 = (LDAPServerMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("CacheSize")) && this.bean.isCacheSizeSet()) {
               var5.setCacheSize(this.bean.getCacheSize());
            }

            if ((var3 == null || !var3.contains("CacheTTL")) && this.bean.isCacheTTLSet()) {
               var5.setCacheTTL(this.bean.getCacheTTL());
            }

            if ((var3 == null || !var3.contains("ConnectTimeout")) && this.bean.isConnectTimeoutSet()) {
               var5.setConnectTimeout(this.bean.getConnectTimeout());
            }

            if ((var3 == null || !var3.contains("ConnectionPoolSize")) && this.bean.isConnectionPoolSizeSet()) {
               var5.setConnectionPoolSize(this.bean.getConnectionPoolSize());
            }

            if ((var3 == null || !var3.contains("ConnectionRetryLimit")) && this.bean.isConnectionRetryLimitSet()) {
               var5.setConnectionRetryLimit(this.bean.getConnectionRetryLimit());
            }

            if ((var3 == null || !var3.contains("CredentialEncrypted")) && this.bean.isCredentialEncryptedSet()) {
               byte[] var4 = this.bean.getCredentialEncrypted();
               var5.setCredentialEncrypted(var4 == null ? null : (byte[])((byte[])((byte[])((byte[])var4)).clone()));
            }

            if ((var3 == null || !var3.contains("Host")) && this.bean.isHostSet()) {
               var5.setHost(this.bean.getHost());
            }

            if ((var3 == null || !var3.contains("ParallelConnectDelay")) && this.bean.isParallelConnectDelaySet()) {
               var5.setParallelConnectDelay(this.bean.getParallelConnectDelay());
            }

            if ((var3 == null || !var3.contains("Port")) && this.bean.isPortSet()) {
               var5.setPort(this.bean.getPort());
            }

            if ((var3 == null || !var3.contains("Principal")) && this.bean.isPrincipalSet()) {
               var5.setPrincipal(this.bean.getPrincipal());
            }

            if ((var3 == null || !var3.contains("ResultsTimeLimit")) && this.bean.isResultsTimeLimitSet()) {
               var5.setResultsTimeLimit(this.bean.getResultsTimeLimit());
            }

            if ((var3 == null || !var3.contains("BindAnonymouslyOnReferrals")) && this.bean.isBindAnonymouslyOnReferralsSet()) {
               var5.setBindAnonymouslyOnReferrals(this.bean.isBindAnonymouslyOnReferrals());
            }

            if ((var3 == null || !var3.contains("CacheEnabled")) && this.bean.isCacheEnabledSet()) {
               var5.setCacheEnabled(this.bean.isCacheEnabled());
            }

            if ((var3 == null || !var3.contains("FollowReferrals")) && this.bean.isFollowReferralsSet()) {
               var5.setFollowReferrals(this.bean.isFollowReferrals());
            }

            if ((var3 == null || !var3.contains("SSLEnabled")) && this.bean.isSSLEnabledSet()) {
               var5.setSSLEnabled(this.bean.isSSLEnabled());
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
