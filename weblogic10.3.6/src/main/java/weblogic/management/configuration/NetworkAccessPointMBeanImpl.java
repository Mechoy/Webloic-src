package weblogic.management.configuration;

import java.io.Serializable;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.zip.CRC32;
import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.beangen.StringHelper;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBeanHelper;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.management.DistributedManagementException;
import weblogic.management.mbeans.custom.NetworkAccessPoint;
import weblogic.utils.ArrayUtils;
import weblogic.utils.collections.CombinedIterator;

public class NetworkAccessPointMBeanImpl extends ConfigurationMBeanImpl implements NetworkAccessPointMBean, Serializable {
   private int _AcceptBacklog;
   private boolean _ChannelIdentityCustomized;
   private int _ChannelWeight;
   private boolean _ClientCertificateEnforced;
   private String _ClusterAddress;
   private int _CompleteCOMMessageTimeout;
   private int _CompleteHTTPMessageTimeout;
   private int _CompleteIIOPMessageTimeout;
   private int _CompleteMessageTimeout;
   private int _CompleteT3MessageTimeout;
   private int _ConnectTimeout;
   private String _CustomPrivateKeyAlias;
   private String _CustomPrivateKeyPassPhrase;
   private byte[] _CustomPrivateKeyPassPhraseEncrypted;
   private Properties _CustomProperties;
   private boolean _Enabled;
   private String _ExternalDNSName;
   private boolean _HttpEnabledForThisProtocol;
   private int _IdleConnectionTimeout;
   private int _IdleIIOPConnectionTimeout;
   private String _ListenAddress;
   private int _ListenPort;
   private int _LoginTimeoutMillis;
   private int _LoginTimeoutMillisSSL;
   private int _MaxBackoffBetweenFailures;
   private int _MaxConnectedClients;
   private int _MaxMessageSize;
   private String _Name;
   private boolean _OutboundEnabled;
   private String _OutboundPrivateKeyAlias;
   private boolean _OutboundPrivateKeyEnabled;
   private String _OutboundPrivateKeyPassPhrase;
   private String _PrivateKeyAlias;
   private String _PrivateKeyPassPhrase;
   private String _Protocol;
   private String _ProxyAddress;
   private int _ProxyPort;
   private String _PublicAddress;
   private int _PublicPort;
   private boolean _SDPEnabled;
   private int _SSLListenPort;
   private boolean _TimeoutConnectionWithPendingResponses;
   private int _TunnelingClientPingSecs;
   private int _TunnelingClientTimeoutSecs;
   private boolean _TunnelingEnabled;
   private boolean _TwoWaySSLEnabled;
   private boolean _UseFastSerialization;
   private static SchemaHelper2 _schemaHelper;

   public NetworkAccessPointMBeanImpl() {
      this._initializeProperty(-1);
   }

   public NetworkAccessPointMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public String getName() {
      return this._Name;
   }

   public boolean isNameSet() {
      return this._isSet(2);
   }

   public void setName(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      ConfigurationValidator.validateName(var1);
      String var2 = this._Name;
      this._Name = var1;
      this._postSet(2, var2, var1);
   }

   public String getProtocol() {
      return this._Protocol;
   }

   public boolean isProtocolSet() {
      return this._isSet(7);
   }

   public void setProtocol(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._Protocol;
      this._Protocol = var1;
      this._postSet(7, var2, var1);
   }

   public String getListenAddress() {
      if (!this._isSet(8)) {
         try {
            return ((ServerMBean)this.getParent()).getListenAddress();
         } catch (NullPointerException var2) {
         }
      }

      return this._ListenAddress;
   }

   public boolean isListenAddressSet() {
      return this._isSet(8);
   }

   public void setListenAddress(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._ListenAddress;
      this._ListenAddress = var1;
      this._postSet(8, var2, var1);
   }

   public String getPublicAddress() {
      if (!this._isSet(9)) {
         try {
            return NetworkAccessPoint.getPublicAddress(this);
         } catch (NullPointerException var2) {
         }
      }

      return this._PublicAddress;
   }

   public boolean isPublicAddressSet() {
      return this._isSet(9);
   }

   public void setPublicAddress(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._PublicAddress;
      this._PublicAddress = var1;
      this._postSet(9, var2, var1);
   }

   public int getListenPort() {
      if (!this._isSet(10)) {
         try {
            return NetworkAccessPoint.isSecure(this) ? ((ServerMBean)this.getParent()).getSSL().getListenPort() : ((ServerMBean)this.getParent()).getListenPort();
         } catch (NullPointerException var2) {
         }
      }

      return this._ListenPort;
   }

   public boolean isListenPortSet() {
      return this._isSet(10);
   }

   public void setListenPort(int var1) throws InvalidAttributeValueException {
      NetworkAccessPointValidator.validateListenPort(var1);
      int var2 = this._ListenPort;
      this._ListenPort = var1;
      this._postSet(10, var2, var1);
   }

   public int getPublicPort() {
      if (!this._isSet(11)) {
         try {
            return this.getListenPort();
         } catch (NullPointerException var2) {
         }
      }

      return this._PublicPort;
   }

   public boolean isPublicPortSet() {
      return this._isSet(11);
   }

   public void setPublicPort(int var1) throws InvalidAttributeValueException {
      NetworkAccessPointValidator.validatePublicPort(var1);
      int var2 = this._PublicPort;
      this._PublicPort = var1;
      this._postSet(11, var2, var1);
   }

   public String getProxyAddress() {
      return this._ProxyAddress;
   }

   public boolean isProxyAddressSet() {
      return this._isSet(12);
   }

   public void setProxyAddress(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._ProxyAddress;
      this._ProxyAddress = var1;
      this._postSet(12, var2, var1);
   }

   public int getProxyPort() {
      return this._ProxyPort;
   }

   public boolean isProxyPortSet() {
      return this._isSet(13);
   }

   public void setProxyPort(int var1) throws InvalidAttributeValueException {
      int var2 = this._ProxyPort;
      this._ProxyPort = var1;
      this._postSet(13, var2, var1);
   }

   public boolean isHttpEnabledForThisProtocol() {
      return this._HttpEnabledForThisProtocol;
   }

   public boolean isHttpEnabledForThisProtocolSet() {
      return this._isSet(14);
   }

   public void setHttpEnabledForThisProtocol(boolean var1) throws InvalidAttributeValueException {
      NetworkAccessPointValidator.validateHttpEnabledForThisProtocol(this, var1);
      boolean var2 = this._HttpEnabledForThisProtocol;
      this._HttpEnabledForThisProtocol = var1;
      this._postSet(14, var2, var1);
   }

   public int getAcceptBacklog() {
      if (!this._isSet(15)) {
         try {
            return ((ServerMBean)this.getParent()).getAcceptBacklog();
         } catch (NullPointerException var2) {
         }
      }

      return this._AcceptBacklog;
   }

   public boolean isAcceptBacklogSet() {
      return this._isSet(15);
   }

   public void setAcceptBacklog(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkMin("AcceptBacklog", var1, 0);
      int var2 = this._AcceptBacklog;
      this._AcceptBacklog = var1;
      this._postSet(15, var2, var1);
   }

   public int getMaxBackoffBetweenFailures() {
      if (!this._isSet(16)) {
         try {
            return ((ServerMBean)this.getParent()).getMaxBackoffBetweenFailures();
         } catch (NullPointerException var2) {
         }
      }

      return this._MaxBackoffBetweenFailures;
   }

   public boolean isMaxBackoffBetweenFailuresSet() {
      return this._isSet(16);
   }

   public void setMaxBackoffBetweenFailures(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkMin("MaxBackoffBetweenFailures", var1, 0);
      int var2 = this._MaxBackoffBetweenFailures;
      this._MaxBackoffBetweenFailures = var1;
      this._postSet(16, var2, var1);
   }

   public int getLoginTimeoutMillis() {
      if (!this._isSet(17)) {
         try {
            return NetworkAccessPoint.isSecure(this) ? ((ServerMBean)this.getParent()).getSSL().getLoginTimeoutMillis() : ((ServerMBean)this.getParent()).getLoginTimeoutMillis();
         } catch (NullPointerException var2) {
         }
      }

      return this._LoginTimeoutMillis;
   }

   public boolean isLoginTimeoutMillisSet() {
      return this._isSet(17);
   }

   public void setLoginTimeoutMillis(int var1) throws InvalidAttributeValueException, DistributedManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("LoginTimeoutMillis", (long)var1, 0L, 100000L);
      int var2 = this._LoginTimeoutMillis;
      this._LoginTimeoutMillis = var1;
      this._postSet(17, var2, var1);
   }

   public int getTunnelingClientPingSecs() {
      if (!this._isSet(18)) {
         try {
            return ((ServerMBean)this.getParent()).getTunnelingClientPingSecs();
         } catch (NullPointerException var2) {
         }
      }

      return this._TunnelingClientPingSecs;
   }

   public boolean isTunnelingClientPingSecsSet() {
      return this._isSet(18);
   }

   public void setTunnelingClientPingSecs(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkMin("TunnelingClientPingSecs", var1, 1);
      int var2 = this._TunnelingClientPingSecs;
      this._TunnelingClientPingSecs = var1;
      this._postSet(18, var2, var1);
   }

   public int getTunnelingClientTimeoutSecs() {
      if (!this._isSet(19)) {
         try {
            return ((ServerMBean)this.getParent()).getTunnelingClientTimeoutSecs();
         } catch (NullPointerException var2) {
         }
      }

      return this._TunnelingClientTimeoutSecs;
   }

   public boolean isTunnelingClientTimeoutSecsSet() {
      return this._isSet(19);
   }

   public void setTunnelingClientTimeoutSecs(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkMin("TunnelingClientTimeoutSecs", var1, 1);
      int var2 = this._TunnelingClientTimeoutSecs;
      this._TunnelingClientTimeoutSecs = var1;
      this._postSet(19, var2, var1);
   }

   public boolean isTunnelingEnabled() {
      return this._TunnelingEnabled;
   }

   public boolean isTunnelingEnabledSet() {
      return this._isSet(20);
   }

   public void setTunnelingEnabled(boolean var1) throws InvalidAttributeValueException {
      NetworkAccessPointValidator.validateTunnelingEnabled(this, var1);
      boolean var2 = this._TunnelingEnabled;
      this._TunnelingEnabled = var1;
      this._postSet(20, var2, var1);
   }

   public int getCompleteMessageTimeout() {
      if (!this._isSet(21)) {
         try {
            return ((ServerMBean)this.getParent()).getCompleteMessageTimeout();
         } catch (NullPointerException var2) {
         }
      }

      return this._CompleteMessageTimeout;
   }

   public boolean isCompleteMessageTimeoutSet() {
      return this._isSet(21);
   }

   public void setCompleteMessageTimeout(int var1) throws InvalidAttributeValueException, DistributedManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("CompleteMessageTimeout", (long)var1, 0L, 480L);
      int var2 = this._CompleteMessageTimeout;
      this._CompleteMessageTimeout = var1;
      this._postSet(21, var2, var1);
   }

   public boolean getTimeoutConnectionWithPendingResponses() {
      return this._TimeoutConnectionWithPendingResponses;
   }

   public boolean isTimeoutConnectionWithPendingResponsesSet() {
      return this._isSet(22);
   }

   public void setTimeoutConnectionWithPendingResponses(boolean var1) throws InvalidAttributeValueException, DistributedManagementException {
      boolean var2 = this._TimeoutConnectionWithPendingResponses;
      this._TimeoutConnectionWithPendingResponses = var1;
      this._postSet(22, var2, var1);
   }

   public int getIdleConnectionTimeout() {
      if (!this._isSet(23)) {
         try {
            return ((ServerMBean)this.getParent()).getIdleConnectionTimeout();
         } catch (NullPointerException var2) {
         }
      }

      return this._IdleConnectionTimeout;
   }

   public boolean isIdleConnectionTimeoutSet() {
      return this._isSet(23);
   }

   public void setIdleConnectionTimeout(int var1) throws InvalidAttributeValueException, DistributedManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkMin("IdleConnectionTimeout", var1, 0);
      int var2 = this._IdleConnectionTimeout;
      this._IdleConnectionTimeout = var1;
      this._postSet(23, var2, var1);
   }

   public int getConnectTimeout() {
      if (!this._isSet(24)) {
         try {
            return ((ServerMBean)this.getParent()).getConnectTimeout();
         } catch (NullPointerException var2) {
         }
      }

      return this._ConnectTimeout;
   }

   public boolean isConnectTimeoutSet() {
      return this._isSet(24);
   }

   public void setConnectTimeout(int var1) throws InvalidAttributeValueException, DistributedManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("ConnectTimeout", (long)var1, 0L, 240L);
      int var2 = this._ConnectTimeout;
      this._ConnectTimeout = var1;
      this._postSet(24, var2, var1);
   }

   public int getMaxMessageSize() {
      if (!this._isSet(25)) {
         try {
            return NetworkAccessPoint.getMaxMessageSize(this);
         } catch (NullPointerException var2) {
         }
      }

      return this._MaxMessageSize;
   }

   public boolean isMaxMessageSizeSet() {
      return this._isSet(25);
   }

   public void setMaxMessageSize(int var1) throws InvalidAttributeValueException, DistributedManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("MaxMessageSize", (long)var1, 4096L, 100000000L);
      int var2 = this._MaxMessageSize;
      this._MaxMessageSize = var1;
      this._postSet(25, var2, var1);
   }

   public boolean isOutboundEnabled() {
      if (!this._isSet(26)) {
         try {
            return this.getProtocol().toLowerCase().equals("admin");
         } catch (NullPointerException var2) {
         }
      }

      return this._OutboundEnabled;
   }

   public boolean isOutboundEnabledSet() {
      return this._isSet(26);
   }

   public void setOutboundEnabled(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this._OutboundEnabled;
      this._OutboundEnabled = var1;
      this._postSet(26, var2, var1);
   }

   public int getChannelWeight() {
      return this._ChannelWeight;
   }

   public boolean isChannelWeightSet() {
      return this._isSet(27);
   }

   public void setChannelWeight(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("ChannelWeight", (long)var1, 1L, 100L);
      int var2 = this._ChannelWeight;
      this._ChannelWeight = var1;
      this._postSet(27, var2, var1);
   }

   public String getClusterAddress() {
      if (!this._isSet(28)) {
         try {
            return NetworkAccessPoint.getClusterAddress(this);
         } catch (NullPointerException var2) {
         }
      }

      return this._ClusterAddress;
   }

   public boolean isClusterAddressSet() {
      return this._isSet(28);
   }

   public void setClusterAddress(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._ClusterAddress;
      this._ClusterAddress = var1;
      this._postSet(28, var2, var1);
   }

   public boolean isEnabled() {
      return this._Enabled;
   }

   public boolean isEnabledSet() {
      return this._isSet(29);
   }

   public void setEnabled(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this._Enabled;
      this._Enabled = var1;
      this._postSet(29, var2, var1);
   }

   public int getMaxConnectedClients() {
      return this._MaxConnectedClients;
   }

   public boolean isMaxConnectedClientsSet() {
      return this._isSet(30);
   }

   public void setMaxConnectedClients(int var1) throws InvalidAttributeValueException {
      int var2 = this._MaxConnectedClients;
      this._MaxConnectedClients = var1;
      this._postSet(30, var2, var1);
   }

   public boolean isTwoWaySSLEnabled() {
      return this._TwoWaySSLEnabled;
   }

   public boolean isTwoWaySSLEnabledSet() {
      return this._isSet(31);
   }

   public void setTwoWaySSLEnabled(boolean var1) {
      boolean var2 = this._TwoWaySSLEnabled;
      this._TwoWaySSLEnabled = var1;
      this._postSet(31, var2, var1);
   }

   public boolean isChannelIdentityCustomized() {
      return this._ChannelIdentityCustomized;
   }

   public boolean isChannelIdentityCustomizedSet() {
      return this._isSet(32);
   }

   public void setChannelIdentityCustomized(boolean var1) {
      boolean var2 = this._ChannelIdentityCustomized;
      this._ChannelIdentityCustomized = var1;
      this._postSet(32, var2, var1);
   }

   public String getCustomPrivateKeyAlias() {
      if (!this._isSet(33)) {
         try {
            return ((ServerMBean)this.getParent()).getSSL().getServerPrivateKeyAlias();
         } catch (NullPointerException var2) {
         }
      }

      return this._CustomPrivateKeyAlias;
   }

   public boolean isCustomPrivateKeyAliasSet() {
      return this._isSet(33);
   }

   public void setCustomPrivateKeyAlias(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._CustomPrivateKeyAlias;
      this._CustomPrivateKeyAlias = var1;
      this._postSet(33, var2, var1);
   }

   public String getPrivateKeyAlias() {
      if (!this._isSet(34)) {
         try {
            return this.isChannelIdentityCustomized() ? this.getCustomPrivateKeyAlias() : ((ServerMBean)this.getParent()).getSSL().getServerPrivateKeyAlias();
         } catch (NullPointerException var2) {
         }
      }

      return this._PrivateKeyAlias;
   }

   public boolean isPrivateKeyAliasSet() {
      return this._isSet(34);
   }

   public void setPrivateKeyAlias(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._PrivateKeyAlias;
      this._PrivateKeyAlias = var1;
      this._postSet(34, var2, var1);
   }

   public String getCustomPrivateKeyPassPhrase() {
      byte[] var1 = this.getCustomPrivateKeyPassPhraseEncrypted();
      return var1 == null ? null : this._decrypt("CustomPrivateKeyPassPhrase", var1);
   }

   public boolean isCustomPrivateKeyPassPhraseSet() {
      return this.isCustomPrivateKeyPassPhraseEncryptedSet();
   }

   public void setCustomPrivateKeyPassPhrase(String var1) {
      var1 = var1 == null ? null : var1.trim();
      this.setCustomPrivateKeyPassPhraseEncrypted(var1 == null ? null : this._encrypt("CustomPrivateKeyPassPhrase", var1));
   }

   public String getPrivateKeyPassPhrase() {
      if (!this._isSet(36)) {
         try {
            return this.isChannelIdentityCustomized() ? this.getCustomPrivateKeyPassPhrase() : ((ServerMBean)this.getParent()).getSSL().getServerPrivateKeyPassPhrase();
         } catch (NullPointerException var2) {
         }
      }

      return this._PrivateKeyPassPhrase;
   }

   public boolean isPrivateKeyPassPhraseSet() {
      return this._isSet(36);
   }

   public void setPrivateKeyPassPhrase(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._PrivateKeyPassPhrase;
      this._PrivateKeyPassPhrase = var1;
      this._postSet(36, var2, var1);
   }

   public byte[] getCustomPrivateKeyPassPhraseEncrypted() {
      if (!this._isSet(37)) {
         try {
            return ((ServerMBean)this.getParent()).getSSL().getServerPrivateKeyPassPhraseEncrypted();
         } catch (NullPointerException var2) {
         }
      }

      return this._getHelper()._cloneArray(this._CustomPrivateKeyPassPhraseEncrypted);
   }

   public String getCustomPrivateKeyPassPhraseEncryptedAsString() {
      byte[] var1 = this.getCustomPrivateKeyPassPhraseEncrypted();
      return var1 == null ? null : new String(var1);
   }

   public boolean isCustomPrivateKeyPassPhraseEncryptedSet() {
      return this._isSet(37);
   }

   public void setCustomPrivateKeyPassPhraseEncryptedAsString(String var1) {
      try {
         byte[] var2 = var1 == null ? null : var1.getBytes();
         this.setCustomPrivateKeyPassPhraseEncrypted(var2);
      } catch (Exception var3) {
         if (var3 instanceof RuntimeException) {
            throw (RuntimeException)var3;
         } else {
            throw new UndeclaredThrowableException(var3);
         }
      }
   }

   public boolean isClientCertificateEnforced() {
      return this._ClientCertificateEnforced;
   }

   public boolean isClientCertificateEnforcedSet() {
      return this._isSet(38);
   }

   public void setClientCertificateEnforced(boolean var1) {
      boolean var2 = this._ClientCertificateEnforced;
      this._ClientCertificateEnforced = var1;
      this._postSet(38, var2, var1);
   }

   public boolean isOutboundPrivateKeyEnabled() {
      if (!this._isSet(39)) {
         try {
            return ((ServerMBean)this.getParent()).isOutboundPrivateKeyEnabled();
         } catch (NullPointerException var2) {
         }
      }

      return this._OutboundPrivateKeyEnabled;
   }

   public boolean isOutboundPrivateKeyEnabledSet() {
      return this._isSet(39);
   }

   public void setOutboundPrivateKeyEnabled(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this._OutboundPrivateKeyEnabled;
      this._OutboundPrivateKeyEnabled = var1;
      this._postSet(39, var2, var1);
   }

   public boolean getUseFastSerialization() {
      if (!this._isSet(40)) {
         try {
            return this.getProtocol().toLowerCase().startsWith("iiop") ? ((ServerMBean)this.getParent()).getIIOP().getUseJavaSerialization() : false;
         } catch (NullPointerException var2) {
         }
      }

      return this._UseFastSerialization;
   }

   public boolean isUseFastSerializationSet() {
      return this._isSet(40);
   }

   public void setUseFastSerialization(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this._UseFastSerialization;
      this._UseFastSerialization = var1;
      this._postSet(40, var2, var1);
   }

   public int getIdleIIOPConnectionTimeout() {
      return this._IdleIIOPConnectionTimeout;
   }

   public boolean isIdleIIOPConnectionTimeoutSet() {
      return this._isSet(41);
   }

   public void setIdleIIOPConnectionTimeout(int var1) throws InvalidAttributeValueException, DistributedManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkMin("IdleIIOPConnectionTimeout", var1, -1);
      int var2 = this._IdleIIOPConnectionTimeout;
      this._IdleIIOPConnectionTimeout = var1;
      this._postSet(41, var2, var1);
   }

   public int getSSLListenPort() {
      return this._SSLListenPort;
   }

   public boolean isSSLListenPortSet() {
      return this._isSet(42);
   }

   public void setSSLListenPort(int var1) throws InvalidAttributeValueException {
      NetworkAccessPointValidator.validateMaxMessageSize(var1);
      int var2 = this._SSLListenPort;
      this._SSLListenPort = var1;
      this._postSet(42, var2, var1);
   }

   public String getExternalDNSName() {
      return this._ExternalDNSName;
   }

   public boolean isExternalDNSNameSet() {
      return this._isSet(43);
   }

   public void setExternalDNSName(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._ExternalDNSName;
      this._ExternalDNSName = var1;
      this._postSet(43, var2, var1);
   }

   public int getLoginTimeoutMillisSSL() {
      return this._LoginTimeoutMillisSSL;
   }

   public boolean isLoginTimeoutMillisSSLSet() {
      return this._isSet(44);
   }

   public void setLoginTimeoutMillisSSL(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("LoginTimeoutMillisSSL", (long)var1, -1L, 2147483647L);
      int var2 = this._LoginTimeoutMillisSSL;
      this._LoginTimeoutMillisSSL = var1;
      this._postSet(44, var2, var1);
   }

   public int getCompleteT3MessageTimeout() {
      return this._CompleteT3MessageTimeout;
   }

   public boolean isCompleteT3MessageTimeoutSet() {
      return this._isSet(45);
   }

   public void setCompleteT3MessageTimeout(int var1) throws InvalidAttributeValueException, DistributedManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("CompleteT3MessageTimeout", (long)var1, -1L, 480L);
      int var2 = this._CompleteT3MessageTimeout;
      this._CompleteT3MessageTimeout = var1;
      this._postSet(45, var2, var1);
   }

   public int getCompleteHTTPMessageTimeout() {
      return this._CompleteHTTPMessageTimeout;
   }

   public boolean isCompleteHTTPMessageTimeoutSet() {
      return this._isSet(46);
   }

   public void setCompleteHTTPMessageTimeout(int var1) throws InvalidAttributeValueException, DistributedManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("CompleteHTTPMessageTimeout", (long)var1, -1L, 480L);
      int var2 = this._CompleteHTTPMessageTimeout;
      this._CompleteHTTPMessageTimeout = var1;
      this._postSet(46, var2, var1);
   }

   public int getCompleteCOMMessageTimeout() {
      return this._CompleteCOMMessageTimeout;
   }

   public boolean isCompleteCOMMessageTimeoutSet() {
      return this._isSet(47);
   }

   public void setCompleteCOMMessageTimeout(int var1) throws InvalidAttributeValueException, DistributedManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("CompleteCOMMessageTimeout", (long)var1, -1L, 480L);
      int var2 = this._CompleteCOMMessageTimeout;
      this._CompleteCOMMessageTimeout = var1;
      this._postSet(47, var2, var1);
   }

   public int getCompleteIIOPMessageTimeout() {
      return this._CompleteIIOPMessageTimeout;
   }

   public boolean isCompleteIIOPMessageTimeoutSet() {
      return this._isSet(48);
   }

   public void setCompleteIIOPMessageTimeout(int var1) throws InvalidAttributeValueException, DistributedManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("CompleteIIOPMessageTimeout", (long)var1, -1L, 480L);
      int var2 = this._CompleteIIOPMessageTimeout;
      this._CompleteIIOPMessageTimeout = var1;
      this._postSet(48, var2, var1);
   }

   public void setCustomProperties(Properties var1) {
      Properties var2 = this._CustomProperties;
      this._CustomProperties = var1;
      this._postSet(49, var2, var1);
   }

   public Properties getCustomProperties() {
      return this._CustomProperties;
   }

   public String getCustomPropertiesAsString() {
      return StringHelper.objectToString(this.getCustomProperties());
   }

   public boolean isCustomPropertiesSet() {
      return this._isSet(49);
   }

   public void setCustomPropertiesAsString(String var1) {
      try {
         this.setCustomProperties(StringHelper.stringToProperties(var1));
      } catch (Exception var3) {
         if (var3 instanceof RuntimeException) {
            throw (RuntimeException)var3;
         } else {
            throw new UndeclaredThrowableException(var3);
         }
      }
   }

   public boolean isSDPEnabled() {
      return this._SDPEnabled;
   }

   public boolean isSDPEnabledSet() {
      return this._isSet(50);
   }

   public void setSDPEnabled(boolean var1) {
      boolean var2 = this._SDPEnabled;
      this._SDPEnabled = var1;
      this._postSet(50, var2, var1);
   }

   public String getOutboundPrivateKeyAlias() {
      if (!this._isSet(51)) {
         try {
            return this.isOutboundPrivateKeyEnabled() && this.isChannelIdentityCustomized() ? this.getCustomPrivateKeyAlias() : ((ServerMBean)this.getParent()).getSSL().getOutboundPrivateKeyAlias();
         } catch (NullPointerException var2) {
         }
      }

      return this._OutboundPrivateKeyAlias;
   }

   public boolean isOutboundPrivateKeyAliasSet() {
      return this._isSet(51);
   }

   public void setOutboundPrivateKeyAlias(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._OutboundPrivateKeyAlias;
      this._OutboundPrivateKeyAlias = var1;
      this._postSet(51, var2, var1);
   }

   public String getOutboundPrivateKeyPassPhrase() {
      if (!this._isSet(52)) {
         try {
            return this.isOutboundPrivateKeyEnabled() && this.isChannelIdentityCustomized() ? this.getCustomPrivateKeyPassPhrase() : ((ServerMBean)this.getParent()).getSSL().getOutboundPrivateKeyPassPhrase();
         } catch (NullPointerException var2) {
         }
      }

      return this._OutboundPrivateKeyPassPhrase;
   }

   public boolean isOutboundPrivateKeyPassPhraseSet() {
      return this._isSet(52);
   }

   public void setOutboundPrivateKeyPassPhrase(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._OutboundPrivateKeyPassPhrase;
      this._OutboundPrivateKeyPassPhrase = var1;
      this._postSet(52, var2, var1);
   }

   public Object _getKey() {
      return this.getName();
   }

   public void _validate() throws IllegalArgumentException {
      super._validate();
   }

   public void setCustomPrivateKeyPassPhraseEncrypted(byte[] var1) {
      byte[] var2 = this._CustomPrivateKeyPassPhraseEncrypted;
      if (this._isProductionModeEnabled() && var1 != null && !this._isEncrypted(var1)) {
         throw new IllegalArgumentException("In production mode, it's not allowed to set a clear text value to the property: CustomPrivateKeyPassPhraseEncrypted of NetworkAccessPointMBean");
      } else {
         this._getHelper()._clearArray(this._CustomPrivateKeyPassPhraseEncrypted);
         this._CustomPrivateKeyPassPhraseEncrypted = this._getHelper()._cloneArray(var1);
         this._postSet(37, var2, var1);
      }
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
         if (var1 == 35) {
            this._markSet(37, false);
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
         var1 = 15;
      }

      try {
         switch (var1) {
            case 15:
               this._AcceptBacklog = 0;
               if (var2) {
                  break;
               }
            case 27:
               this._ChannelWeight = 50;
               if (var2) {
                  break;
               }
            case 28:
               this._ClusterAddress = null;
               if (var2) {
                  break;
               }
            case 47:
               this._CompleteCOMMessageTimeout = -1;
               if (var2) {
                  break;
               }
            case 46:
               this._CompleteHTTPMessageTimeout = -1;
               if (var2) {
                  break;
               }
            case 48:
               this._CompleteIIOPMessageTimeout = -1;
               if (var2) {
                  break;
               }
            case 21:
               this._CompleteMessageTimeout = 0;
               if (var2) {
                  break;
               }
            case 45:
               this._CompleteT3MessageTimeout = -1;
               if (var2) {
                  break;
               }
            case 24:
               this._ConnectTimeout = 0;
               if (var2) {
                  break;
               }
            case 33:
               this._CustomPrivateKeyAlias = null;
               if (var2) {
                  break;
               }
            case 35:
               this._CustomPrivateKeyPassPhraseEncrypted = null;
               if (var2) {
                  break;
               }
            case 37:
               this._CustomPrivateKeyPassPhraseEncrypted = null;
               if (var2) {
                  break;
               }
            case 49:
               this._CustomProperties = null;
               if (var2) {
                  break;
               }
            case 43:
               this._ExternalDNSName = null;
               if (var2) {
                  break;
               }
            case 23:
               this._IdleConnectionTimeout = 0;
               if (var2) {
                  break;
               }
            case 41:
               this._IdleIIOPConnectionTimeout = -1;
               if (var2) {
                  break;
               }
            case 8:
               this._ListenAddress = null;
               if (var2) {
                  break;
               }
            case 10:
               this._ListenPort = 0;
               if (var2) {
                  break;
               }
            case 17:
               this._LoginTimeoutMillis = 0;
               if (var2) {
                  break;
               }
            case 44:
               this._LoginTimeoutMillisSSL = -1;
               if (var2) {
                  break;
               }
            case 16:
               this._MaxBackoffBetweenFailures = 0;
               if (var2) {
                  break;
               }
            case 30:
               this._MaxConnectedClients = Integer.MAX_VALUE;
               if (var2) {
                  break;
               }
            case 25:
               this._MaxMessageSize = 0;
               if (var2) {
                  break;
               }
            case 2:
               this._Name = "<unknown>";
               if (var2) {
                  break;
               }
            case 51:
               this._OutboundPrivateKeyAlias = null;
               if (var2) {
                  break;
               }
            case 52:
               this._OutboundPrivateKeyPassPhrase = null;
               if (var2) {
                  break;
               }
            case 34:
               this._PrivateKeyAlias = null;
               if (var2) {
                  break;
               }
            case 36:
               this._PrivateKeyPassPhrase = null;
               if (var2) {
                  break;
               }
            case 7:
               this._Protocol = "t3";
               if (var2) {
                  break;
               }
            case 12:
               this._ProxyAddress = null;
               if (var2) {
                  break;
               }
            case 13:
               this._ProxyPort = 80;
               if (var2) {
                  break;
               }
            case 9:
               this._PublicAddress = null;
               if (var2) {
                  break;
               }
            case 11:
               this._PublicPort = 0;
               if (var2) {
                  break;
               }
            case 42:
               this._SSLListenPort = -1;
               if (var2) {
                  break;
               }
            case 22:
               this._TimeoutConnectionWithPendingResponses = false;
               if (var2) {
                  break;
               }
            case 18:
               this._TunnelingClientPingSecs = 0;
               if (var2) {
                  break;
               }
            case 19:
               this._TunnelingClientTimeoutSecs = 0;
               if (var2) {
                  break;
               }
            case 40:
               this._UseFastSerialization = false;
               if (var2) {
                  break;
               }
            case 32:
               this._ChannelIdentityCustomized = false;
               if (var2) {
                  break;
               }
            case 38:
               this._ClientCertificateEnforced = false;
               if (var2) {
                  break;
               }
            case 29:
               this._Enabled = true;
               if (var2) {
                  break;
               }
            case 14:
               this._HttpEnabledForThisProtocol = true;
               if (var2) {
                  break;
               }
            case 26:
               this._OutboundEnabled = false;
               if (var2) {
                  break;
               }
            case 39:
               this._OutboundPrivateKeyEnabled = false;
               if (var2) {
                  break;
               }
            case 50:
               this._SDPEnabled = false;
               if (var2) {
                  break;
               }
            case 20:
               this._TunnelingEnabled = false;
               if (var2) {
                  break;
               }
            case 31:
               this._TwoWaySSLEnabled = false;
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
      return "NetworkAccessPoint";
   }

   public void putValue(String var1, Object var2) {
      int var4;
      if (var1.equals("AcceptBacklog")) {
         var4 = this._AcceptBacklog;
         this._AcceptBacklog = (Integer)var2;
         this._postSet(15, var4, this._AcceptBacklog);
      } else {
         boolean var3;
         if (var1.equals("ChannelIdentityCustomized")) {
            var3 = this._ChannelIdentityCustomized;
            this._ChannelIdentityCustomized = (Boolean)var2;
            this._postSet(32, var3, this._ChannelIdentityCustomized);
         } else if (var1.equals("ChannelWeight")) {
            var4 = this._ChannelWeight;
            this._ChannelWeight = (Integer)var2;
            this._postSet(27, var4, this._ChannelWeight);
         } else if (var1.equals("ClientCertificateEnforced")) {
            var3 = this._ClientCertificateEnforced;
            this._ClientCertificateEnforced = (Boolean)var2;
            this._postSet(38, var3, this._ClientCertificateEnforced);
         } else {
            String var5;
            if (var1.equals("ClusterAddress")) {
               var5 = this._ClusterAddress;
               this._ClusterAddress = (String)var2;
               this._postSet(28, var5, this._ClusterAddress);
            } else if (var1.equals("CompleteCOMMessageTimeout")) {
               var4 = this._CompleteCOMMessageTimeout;
               this._CompleteCOMMessageTimeout = (Integer)var2;
               this._postSet(47, var4, this._CompleteCOMMessageTimeout);
            } else if (var1.equals("CompleteHTTPMessageTimeout")) {
               var4 = this._CompleteHTTPMessageTimeout;
               this._CompleteHTTPMessageTimeout = (Integer)var2;
               this._postSet(46, var4, this._CompleteHTTPMessageTimeout);
            } else if (var1.equals("CompleteIIOPMessageTimeout")) {
               var4 = this._CompleteIIOPMessageTimeout;
               this._CompleteIIOPMessageTimeout = (Integer)var2;
               this._postSet(48, var4, this._CompleteIIOPMessageTimeout);
            } else if (var1.equals("CompleteMessageTimeout")) {
               var4 = this._CompleteMessageTimeout;
               this._CompleteMessageTimeout = (Integer)var2;
               this._postSet(21, var4, this._CompleteMessageTimeout);
            } else if (var1.equals("CompleteT3MessageTimeout")) {
               var4 = this._CompleteT3MessageTimeout;
               this._CompleteT3MessageTimeout = (Integer)var2;
               this._postSet(45, var4, this._CompleteT3MessageTimeout);
            } else if (var1.equals("ConnectTimeout")) {
               var4 = this._ConnectTimeout;
               this._ConnectTimeout = (Integer)var2;
               this._postSet(24, var4, this._ConnectTimeout);
            } else if (var1.equals("CustomPrivateKeyAlias")) {
               var5 = this._CustomPrivateKeyAlias;
               this._CustomPrivateKeyAlias = (String)var2;
               this._postSet(33, var5, this._CustomPrivateKeyAlias);
            } else if (var1.equals("CustomPrivateKeyPassPhrase")) {
               var5 = this._CustomPrivateKeyPassPhrase;
               this._CustomPrivateKeyPassPhrase = (String)var2;
               this._postSet(35, var5, this._CustomPrivateKeyPassPhrase);
            } else if (var1.equals("CustomPrivateKeyPassPhraseEncrypted")) {
               byte[] var7 = this._CustomPrivateKeyPassPhraseEncrypted;
               this._CustomPrivateKeyPassPhraseEncrypted = (byte[])((byte[])var2);
               this._postSet(37, var7, this._CustomPrivateKeyPassPhraseEncrypted);
            } else if (var1.equals("CustomProperties")) {
               Properties var6 = this._CustomProperties;
               this._CustomProperties = (Properties)var2;
               this._postSet(49, var6, this._CustomProperties);
            } else if (var1.equals("Enabled")) {
               var3 = this._Enabled;
               this._Enabled = (Boolean)var2;
               this._postSet(29, var3, this._Enabled);
            } else if (var1.equals("ExternalDNSName")) {
               var5 = this._ExternalDNSName;
               this._ExternalDNSName = (String)var2;
               this._postSet(43, var5, this._ExternalDNSName);
            } else if (var1.equals("HttpEnabledForThisProtocol")) {
               var3 = this._HttpEnabledForThisProtocol;
               this._HttpEnabledForThisProtocol = (Boolean)var2;
               this._postSet(14, var3, this._HttpEnabledForThisProtocol);
            } else if (var1.equals("IdleConnectionTimeout")) {
               var4 = this._IdleConnectionTimeout;
               this._IdleConnectionTimeout = (Integer)var2;
               this._postSet(23, var4, this._IdleConnectionTimeout);
            } else if (var1.equals("IdleIIOPConnectionTimeout")) {
               var4 = this._IdleIIOPConnectionTimeout;
               this._IdleIIOPConnectionTimeout = (Integer)var2;
               this._postSet(41, var4, this._IdleIIOPConnectionTimeout);
            } else if (var1.equals("ListenAddress")) {
               var5 = this._ListenAddress;
               this._ListenAddress = (String)var2;
               this._postSet(8, var5, this._ListenAddress);
            } else if (var1.equals("ListenPort")) {
               var4 = this._ListenPort;
               this._ListenPort = (Integer)var2;
               this._postSet(10, var4, this._ListenPort);
            } else if (var1.equals("LoginTimeoutMillis")) {
               var4 = this._LoginTimeoutMillis;
               this._LoginTimeoutMillis = (Integer)var2;
               this._postSet(17, var4, this._LoginTimeoutMillis);
            } else if (var1.equals("LoginTimeoutMillisSSL")) {
               var4 = this._LoginTimeoutMillisSSL;
               this._LoginTimeoutMillisSSL = (Integer)var2;
               this._postSet(44, var4, this._LoginTimeoutMillisSSL);
            } else if (var1.equals("MaxBackoffBetweenFailures")) {
               var4 = this._MaxBackoffBetweenFailures;
               this._MaxBackoffBetweenFailures = (Integer)var2;
               this._postSet(16, var4, this._MaxBackoffBetweenFailures);
            } else if (var1.equals("MaxConnectedClients")) {
               var4 = this._MaxConnectedClients;
               this._MaxConnectedClients = (Integer)var2;
               this._postSet(30, var4, this._MaxConnectedClients);
            } else if (var1.equals("MaxMessageSize")) {
               var4 = this._MaxMessageSize;
               this._MaxMessageSize = (Integer)var2;
               this._postSet(25, var4, this._MaxMessageSize);
            } else if (var1.equals("Name")) {
               var5 = this._Name;
               this._Name = (String)var2;
               this._postSet(2, var5, this._Name);
            } else if (var1.equals("OutboundEnabled")) {
               var3 = this._OutboundEnabled;
               this._OutboundEnabled = (Boolean)var2;
               this._postSet(26, var3, this._OutboundEnabled);
            } else if (var1.equals("OutboundPrivateKeyAlias")) {
               var5 = this._OutboundPrivateKeyAlias;
               this._OutboundPrivateKeyAlias = (String)var2;
               this._postSet(51, var5, this._OutboundPrivateKeyAlias);
            } else if (var1.equals("OutboundPrivateKeyEnabled")) {
               var3 = this._OutboundPrivateKeyEnabled;
               this._OutboundPrivateKeyEnabled = (Boolean)var2;
               this._postSet(39, var3, this._OutboundPrivateKeyEnabled);
            } else if (var1.equals("OutboundPrivateKeyPassPhrase")) {
               var5 = this._OutboundPrivateKeyPassPhrase;
               this._OutboundPrivateKeyPassPhrase = (String)var2;
               this._postSet(52, var5, this._OutboundPrivateKeyPassPhrase);
            } else if (var1.equals("PrivateKeyAlias")) {
               var5 = this._PrivateKeyAlias;
               this._PrivateKeyAlias = (String)var2;
               this._postSet(34, var5, this._PrivateKeyAlias);
            } else if (var1.equals("PrivateKeyPassPhrase")) {
               var5 = this._PrivateKeyPassPhrase;
               this._PrivateKeyPassPhrase = (String)var2;
               this._postSet(36, var5, this._PrivateKeyPassPhrase);
            } else if (var1.equals("Protocol")) {
               var5 = this._Protocol;
               this._Protocol = (String)var2;
               this._postSet(7, var5, this._Protocol);
            } else if (var1.equals("ProxyAddress")) {
               var5 = this._ProxyAddress;
               this._ProxyAddress = (String)var2;
               this._postSet(12, var5, this._ProxyAddress);
            } else if (var1.equals("ProxyPort")) {
               var4 = this._ProxyPort;
               this._ProxyPort = (Integer)var2;
               this._postSet(13, var4, this._ProxyPort);
            } else if (var1.equals("PublicAddress")) {
               var5 = this._PublicAddress;
               this._PublicAddress = (String)var2;
               this._postSet(9, var5, this._PublicAddress);
            } else if (var1.equals("PublicPort")) {
               var4 = this._PublicPort;
               this._PublicPort = (Integer)var2;
               this._postSet(11, var4, this._PublicPort);
            } else if (var1.equals("SDPEnabled")) {
               var3 = this._SDPEnabled;
               this._SDPEnabled = (Boolean)var2;
               this._postSet(50, var3, this._SDPEnabled);
            } else if (var1.equals("SSLListenPort")) {
               var4 = this._SSLListenPort;
               this._SSLListenPort = (Integer)var2;
               this._postSet(42, var4, this._SSLListenPort);
            } else if (var1.equals("TimeoutConnectionWithPendingResponses")) {
               var3 = this._TimeoutConnectionWithPendingResponses;
               this._TimeoutConnectionWithPendingResponses = (Boolean)var2;
               this._postSet(22, var3, this._TimeoutConnectionWithPendingResponses);
            } else if (var1.equals("TunnelingClientPingSecs")) {
               var4 = this._TunnelingClientPingSecs;
               this._TunnelingClientPingSecs = (Integer)var2;
               this._postSet(18, var4, this._TunnelingClientPingSecs);
            } else if (var1.equals("TunnelingClientTimeoutSecs")) {
               var4 = this._TunnelingClientTimeoutSecs;
               this._TunnelingClientTimeoutSecs = (Integer)var2;
               this._postSet(19, var4, this._TunnelingClientTimeoutSecs);
            } else if (var1.equals("TunnelingEnabled")) {
               var3 = this._TunnelingEnabled;
               this._TunnelingEnabled = (Boolean)var2;
               this._postSet(20, var3, this._TunnelingEnabled);
            } else if (var1.equals("TwoWaySSLEnabled")) {
               var3 = this._TwoWaySSLEnabled;
               this._TwoWaySSLEnabled = (Boolean)var2;
               this._postSet(31, var3, this._TwoWaySSLEnabled);
            } else if (var1.equals("UseFastSerialization")) {
               var3 = this._UseFastSerialization;
               this._UseFastSerialization = (Boolean)var2;
               this._postSet(40, var3, this._UseFastSerialization);
            } else {
               super.putValue(var1, var2);
            }
         }
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("AcceptBacklog")) {
         return new Integer(this._AcceptBacklog);
      } else if (var1.equals("ChannelIdentityCustomized")) {
         return new Boolean(this._ChannelIdentityCustomized);
      } else if (var1.equals("ChannelWeight")) {
         return new Integer(this._ChannelWeight);
      } else if (var1.equals("ClientCertificateEnforced")) {
         return new Boolean(this._ClientCertificateEnforced);
      } else if (var1.equals("ClusterAddress")) {
         return this._ClusterAddress;
      } else if (var1.equals("CompleteCOMMessageTimeout")) {
         return new Integer(this._CompleteCOMMessageTimeout);
      } else if (var1.equals("CompleteHTTPMessageTimeout")) {
         return new Integer(this._CompleteHTTPMessageTimeout);
      } else if (var1.equals("CompleteIIOPMessageTimeout")) {
         return new Integer(this._CompleteIIOPMessageTimeout);
      } else if (var1.equals("CompleteMessageTimeout")) {
         return new Integer(this._CompleteMessageTimeout);
      } else if (var1.equals("CompleteT3MessageTimeout")) {
         return new Integer(this._CompleteT3MessageTimeout);
      } else if (var1.equals("ConnectTimeout")) {
         return new Integer(this._ConnectTimeout);
      } else if (var1.equals("CustomPrivateKeyAlias")) {
         return this._CustomPrivateKeyAlias;
      } else if (var1.equals("CustomPrivateKeyPassPhrase")) {
         return this._CustomPrivateKeyPassPhrase;
      } else if (var1.equals("CustomPrivateKeyPassPhraseEncrypted")) {
         return this._CustomPrivateKeyPassPhraseEncrypted;
      } else if (var1.equals("CustomProperties")) {
         return this._CustomProperties;
      } else if (var1.equals("Enabled")) {
         return new Boolean(this._Enabled);
      } else if (var1.equals("ExternalDNSName")) {
         return this._ExternalDNSName;
      } else if (var1.equals("HttpEnabledForThisProtocol")) {
         return new Boolean(this._HttpEnabledForThisProtocol);
      } else if (var1.equals("IdleConnectionTimeout")) {
         return new Integer(this._IdleConnectionTimeout);
      } else if (var1.equals("IdleIIOPConnectionTimeout")) {
         return new Integer(this._IdleIIOPConnectionTimeout);
      } else if (var1.equals("ListenAddress")) {
         return this._ListenAddress;
      } else if (var1.equals("ListenPort")) {
         return new Integer(this._ListenPort);
      } else if (var1.equals("LoginTimeoutMillis")) {
         return new Integer(this._LoginTimeoutMillis);
      } else if (var1.equals("LoginTimeoutMillisSSL")) {
         return new Integer(this._LoginTimeoutMillisSSL);
      } else if (var1.equals("MaxBackoffBetweenFailures")) {
         return new Integer(this._MaxBackoffBetweenFailures);
      } else if (var1.equals("MaxConnectedClients")) {
         return new Integer(this._MaxConnectedClients);
      } else if (var1.equals("MaxMessageSize")) {
         return new Integer(this._MaxMessageSize);
      } else if (var1.equals("Name")) {
         return this._Name;
      } else if (var1.equals("OutboundEnabled")) {
         return new Boolean(this._OutboundEnabled);
      } else if (var1.equals("OutboundPrivateKeyAlias")) {
         return this._OutboundPrivateKeyAlias;
      } else if (var1.equals("OutboundPrivateKeyEnabled")) {
         return new Boolean(this._OutboundPrivateKeyEnabled);
      } else if (var1.equals("OutboundPrivateKeyPassPhrase")) {
         return this._OutboundPrivateKeyPassPhrase;
      } else if (var1.equals("PrivateKeyAlias")) {
         return this._PrivateKeyAlias;
      } else if (var1.equals("PrivateKeyPassPhrase")) {
         return this._PrivateKeyPassPhrase;
      } else if (var1.equals("Protocol")) {
         return this._Protocol;
      } else if (var1.equals("ProxyAddress")) {
         return this._ProxyAddress;
      } else if (var1.equals("ProxyPort")) {
         return new Integer(this._ProxyPort);
      } else if (var1.equals("PublicAddress")) {
         return this._PublicAddress;
      } else if (var1.equals("PublicPort")) {
         return new Integer(this._PublicPort);
      } else if (var1.equals("SDPEnabled")) {
         return new Boolean(this._SDPEnabled);
      } else if (var1.equals("SSLListenPort")) {
         return new Integer(this._SSLListenPort);
      } else if (var1.equals("TimeoutConnectionWithPendingResponses")) {
         return new Boolean(this._TimeoutConnectionWithPendingResponses);
      } else if (var1.equals("TunnelingClientPingSecs")) {
         return new Integer(this._TunnelingClientPingSecs);
      } else if (var1.equals("TunnelingClientTimeoutSecs")) {
         return new Integer(this._TunnelingClientTimeoutSecs);
      } else if (var1.equals("TunnelingEnabled")) {
         return new Boolean(this._TunnelingEnabled);
      } else if (var1.equals("TwoWaySSLEnabled")) {
         return new Boolean(this._TwoWaySSLEnabled);
      } else {
         return var1.equals("UseFastSerialization") ? new Boolean(this._UseFastSerialization) : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends ConfigurationMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 4:
               if (var1.equals("name")) {
                  return 2;
               }
            case 5:
            case 6:
            case 9:
            case 12:
            case 19:
            case 25:
            case 31:
            case 33:
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
            case 39:
            default:
               break;
            case 7:
               if (var1.equals("enabled")) {
                  return 29;
               }
               break;
            case 8:
               if (var1.equals("protocol")) {
                  return 7;
               }
               break;
            case 10:
               if (var1.equals("proxy-port")) {
                  return 13;
               }
               break;
            case 11:
               if (var1.equals("listen-port")) {
                  return 10;
               }

               if (var1.equals("public-port")) {
                  return 11;
               }

               if (var1.equals("sdp-enabled")) {
                  return 50;
               }
               break;
            case 13:
               if (var1.equals("proxy-address")) {
                  return 12;
               }
               break;
            case 14:
               if (var1.equals("accept-backlog")) {
                  return 15;
               }

               if (var1.equals("channel-weight")) {
                  return 27;
               }

               if (var1.equals("listen-address")) {
                  return 8;
               }

               if (var1.equals("public-address")) {
                  return 9;
               }
               break;
            case 15:
               if (var1.equals("cluster-address")) {
                  return 28;
               }

               if (var1.equals("connect-timeout")) {
                  return 24;
               }

               if (var1.equals("ssl-listen-port")) {
                  return 42;
               }
               break;
            case 16:
               if (var1.equals("externaldns-name")) {
                  return 43;
               }

               if (var1.equals("max-message-size")) {
                  return 25;
               }

               if (var1.equals("outbound-enabled")) {
                  return 26;
               }
               break;
            case 17:
               if (var1.equals("custom-properties")) {
                  return 49;
               }

               if (var1.equals("private-key-alias")) {
                  return 34;
               }

               if (var1.equals("tunneling-enabled")) {
                  return 20;
               }
               break;
            case 18:
               if (var1.equals("two-wayssl-enabled")) {
                  return 31;
               }
               break;
            case 20:
               if (var1.equals("login-timeout-millis")) {
                  return 17;
               }
               break;
            case 21:
               if (var1.equals("max-connected-clients")) {
                  return 30;
               }
               break;
            case 22:
               if (var1.equals("use-fast-serialization")) {
                  return 40;
               }
               break;
            case 23:
               if (var1.equals("idle-connection-timeout")) {
                  return 23;
               }

               if (var1.equals("login-timeout-millisssl")) {
                  return 44;
               }

               if (var1.equals("private-key-pass-phrase")) {
                  return 36;
               }
               break;
            case 24:
               if (var1.equals("complete-message-timeout")) {
                  return 21;
               }

               if (var1.equals("custom-private-key-alias")) {
                  return 33;
               }
               break;
            case 26:
               if (var1.equals("completet3-message-timeout")) {
                  return 45;
               }

               if (var1.equals("outbound-private-key-alias")) {
                  return 51;
               }

               if (var1.equals("tunneling-client-ping-secs")) {
                  return 18;
               }
               break;
            case 27:
               if (var1.equals("completecom-message-timeout")) {
                  return 47;
               }

               if (var1.equals("idleiiop-connection-timeout")) {
                  return 41;
               }

               if (var1.equals("channel-identity-customized")) {
                  return 32;
               }

               if (var1.equals("client-certificate-enforced")) {
                  return 38;
               }
               break;
            case 28:
               if (var1.equals("completehttp-message-timeout")) {
                  return 46;
               }

               if (var1.equals("completeiiop-message-timeout")) {
                  return 48;
               }

               if (var1.equals("max-backoff-between-failures")) {
                  return 16;
               }

               if (var1.equals("outbound-private-key-enabled")) {
                  return 39;
               }
               break;
            case 29:
               if (var1.equals("tunneling-client-timeout-secs")) {
                  return 19;
               }
               break;
            case 30:
               if (var1.equals("custom-private-key-pass-phrase")) {
                  return 35;
               }

               if (var1.equals("http-enabled-for-this-protocol")) {
                  return 14;
               }
               break;
            case 32:
               if (var1.equals("outbound-private-key-pass-phrase")) {
                  return 52;
               }
               break;
            case 40:
               if (var1.equals("custom-private-key-pass-phrase-encrypted")) {
                  return 37;
               }
               break;
            case 41:
               if (var1.equals("timeout-connection-with-pending-responses")) {
                  return 22;
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
               return "protocol";
            case 8:
               return "listen-address";
            case 9:
               return "public-address";
            case 10:
               return "listen-port";
            case 11:
               return "public-port";
            case 12:
               return "proxy-address";
            case 13:
               return "proxy-port";
            case 14:
               return "http-enabled-for-this-protocol";
            case 15:
               return "accept-backlog";
            case 16:
               return "max-backoff-between-failures";
            case 17:
               return "login-timeout-millis";
            case 18:
               return "tunneling-client-ping-secs";
            case 19:
               return "tunneling-client-timeout-secs";
            case 20:
               return "tunneling-enabled";
            case 21:
               return "complete-message-timeout";
            case 22:
               return "timeout-connection-with-pending-responses";
            case 23:
               return "idle-connection-timeout";
            case 24:
               return "connect-timeout";
            case 25:
               return "max-message-size";
            case 26:
               return "outbound-enabled";
            case 27:
               return "channel-weight";
            case 28:
               return "cluster-address";
            case 29:
               return "enabled";
            case 30:
               return "max-connected-clients";
            case 31:
               return "two-wayssl-enabled";
            case 32:
               return "channel-identity-customized";
            case 33:
               return "custom-private-key-alias";
            case 34:
               return "private-key-alias";
            case 35:
               return "custom-private-key-pass-phrase";
            case 36:
               return "private-key-pass-phrase";
            case 37:
               return "custom-private-key-pass-phrase-encrypted";
            case 38:
               return "client-certificate-enforced";
            case 39:
               return "outbound-private-key-enabled";
            case 40:
               return "use-fast-serialization";
            case 41:
               return "idleiiop-connection-timeout";
            case 42:
               return "ssl-listen-port";
            case 43:
               return "externaldns-name";
            case 44:
               return "login-timeout-millisssl";
            case 45:
               return "completet3-message-timeout";
            case 46:
               return "completehttp-message-timeout";
            case 47:
               return "completecom-message-timeout";
            case 48:
               return "completeiiop-message-timeout";
            case 49:
               return "custom-properties";
            case 50:
               return "sdp-enabled";
            case 51:
               return "outbound-private-key-alias";
            case 52:
               return "outbound-private-key-pass-phrase";
         }
      }

      public boolean isArray(int var1) {
         switch (var1) {
            default:
               return super.isArray(var1);
         }
      }

      public boolean isConfigurable(int var1) {
         switch (var1) {
            case 9:
               return true;
            case 10:
            case 12:
            case 13:
            case 14:
            case 20:
            case 22:
            case 27:
            case 29:
            case 30:
            case 36:
            case 37:
            case 42:
            case 43:
            default:
               return super.isConfigurable(var1);
            case 11:
               return true;
            case 15:
               return true;
            case 16:
               return true;
            case 17:
               return true;
            case 18:
               return true;
            case 19:
               return true;
            case 21:
               return true;
            case 23:
               return true;
            case 24:
               return true;
            case 25:
               return true;
            case 26:
               return true;
            case 28:
               return true;
            case 31:
               return true;
            case 32:
               return true;
            case 33:
               return true;
            case 34:
               return true;
            case 35:
               return true;
            case 38:
               return true;
            case 39:
               return true;
            case 40:
               return true;
            case 41:
               return true;
            case 44:
               return true;
            case 45:
               return true;
            case 46:
               return true;
            case 47:
               return true;
            case 48:
               return true;
            case 49:
               return true;
            case 50:
               return true;
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
      private NetworkAccessPointMBeanImpl bean;

      protected Helper(NetworkAccessPointMBeanImpl var1) {
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
               return "Protocol";
            case 8:
               return "ListenAddress";
            case 9:
               return "PublicAddress";
            case 10:
               return "ListenPort";
            case 11:
               return "PublicPort";
            case 12:
               return "ProxyAddress";
            case 13:
               return "ProxyPort";
            case 14:
               return "HttpEnabledForThisProtocol";
            case 15:
               return "AcceptBacklog";
            case 16:
               return "MaxBackoffBetweenFailures";
            case 17:
               return "LoginTimeoutMillis";
            case 18:
               return "TunnelingClientPingSecs";
            case 19:
               return "TunnelingClientTimeoutSecs";
            case 20:
               return "TunnelingEnabled";
            case 21:
               return "CompleteMessageTimeout";
            case 22:
               return "TimeoutConnectionWithPendingResponses";
            case 23:
               return "IdleConnectionTimeout";
            case 24:
               return "ConnectTimeout";
            case 25:
               return "MaxMessageSize";
            case 26:
               return "OutboundEnabled";
            case 27:
               return "ChannelWeight";
            case 28:
               return "ClusterAddress";
            case 29:
               return "Enabled";
            case 30:
               return "MaxConnectedClients";
            case 31:
               return "TwoWaySSLEnabled";
            case 32:
               return "ChannelIdentityCustomized";
            case 33:
               return "CustomPrivateKeyAlias";
            case 34:
               return "PrivateKeyAlias";
            case 35:
               return "CustomPrivateKeyPassPhrase";
            case 36:
               return "PrivateKeyPassPhrase";
            case 37:
               return "CustomPrivateKeyPassPhraseEncrypted";
            case 38:
               return "ClientCertificateEnforced";
            case 39:
               return "OutboundPrivateKeyEnabled";
            case 40:
               return "UseFastSerialization";
            case 41:
               return "IdleIIOPConnectionTimeout";
            case 42:
               return "SSLListenPort";
            case 43:
               return "ExternalDNSName";
            case 44:
               return "LoginTimeoutMillisSSL";
            case 45:
               return "CompleteT3MessageTimeout";
            case 46:
               return "CompleteHTTPMessageTimeout";
            case 47:
               return "CompleteCOMMessageTimeout";
            case 48:
               return "CompleteIIOPMessageTimeout";
            case 49:
               return "CustomProperties";
            case 50:
               return "SDPEnabled";
            case 51:
               return "OutboundPrivateKeyAlias";
            case 52:
               return "OutboundPrivateKeyPassPhrase";
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("AcceptBacklog")) {
            return 15;
         } else if (var1.equals("ChannelWeight")) {
            return 27;
         } else if (var1.equals("ClusterAddress")) {
            return 28;
         } else if (var1.equals("CompleteCOMMessageTimeout")) {
            return 47;
         } else if (var1.equals("CompleteHTTPMessageTimeout")) {
            return 46;
         } else if (var1.equals("CompleteIIOPMessageTimeout")) {
            return 48;
         } else if (var1.equals("CompleteMessageTimeout")) {
            return 21;
         } else if (var1.equals("CompleteT3MessageTimeout")) {
            return 45;
         } else if (var1.equals("ConnectTimeout")) {
            return 24;
         } else if (var1.equals("CustomPrivateKeyAlias")) {
            return 33;
         } else if (var1.equals("CustomPrivateKeyPassPhrase")) {
            return 35;
         } else if (var1.equals("CustomPrivateKeyPassPhraseEncrypted")) {
            return 37;
         } else if (var1.equals("CustomProperties")) {
            return 49;
         } else if (var1.equals("ExternalDNSName")) {
            return 43;
         } else if (var1.equals("IdleConnectionTimeout")) {
            return 23;
         } else if (var1.equals("IdleIIOPConnectionTimeout")) {
            return 41;
         } else if (var1.equals("ListenAddress")) {
            return 8;
         } else if (var1.equals("ListenPort")) {
            return 10;
         } else if (var1.equals("LoginTimeoutMillis")) {
            return 17;
         } else if (var1.equals("LoginTimeoutMillisSSL")) {
            return 44;
         } else if (var1.equals("MaxBackoffBetweenFailures")) {
            return 16;
         } else if (var1.equals("MaxConnectedClients")) {
            return 30;
         } else if (var1.equals("MaxMessageSize")) {
            return 25;
         } else if (var1.equals("Name")) {
            return 2;
         } else if (var1.equals("OutboundPrivateKeyAlias")) {
            return 51;
         } else if (var1.equals("OutboundPrivateKeyPassPhrase")) {
            return 52;
         } else if (var1.equals("PrivateKeyAlias")) {
            return 34;
         } else if (var1.equals("PrivateKeyPassPhrase")) {
            return 36;
         } else if (var1.equals("Protocol")) {
            return 7;
         } else if (var1.equals("ProxyAddress")) {
            return 12;
         } else if (var1.equals("ProxyPort")) {
            return 13;
         } else if (var1.equals("PublicAddress")) {
            return 9;
         } else if (var1.equals("PublicPort")) {
            return 11;
         } else if (var1.equals("SSLListenPort")) {
            return 42;
         } else if (var1.equals("TimeoutConnectionWithPendingResponses")) {
            return 22;
         } else if (var1.equals("TunnelingClientPingSecs")) {
            return 18;
         } else if (var1.equals("TunnelingClientTimeoutSecs")) {
            return 19;
         } else if (var1.equals("UseFastSerialization")) {
            return 40;
         } else if (var1.equals("ChannelIdentityCustomized")) {
            return 32;
         } else if (var1.equals("ClientCertificateEnforced")) {
            return 38;
         } else if (var1.equals("Enabled")) {
            return 29;
         } else if (var1.equals("HttpEnabledForThisProtocol")) {
            return 14;
         } else if (var1.equals("OutboundEnabled")) {
            return 26;
         } else if (var1.equals("OutboundPrivateKeyEnabled")) {
            return 39;
         } else if (var1.equals("SDPEnabled")) {
            return 50;
         } else if (var1.equals("TunnelingEnabled")) {
            return 20;
         } else {
            return var1.equals("TwoWaySSLEnabled") ? 31 : super.getPropertyIndex(var1);
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
            if (this.bean.isAcceptBacklogSet()) {
               var2.append("AcceptBacklog");
               var2.append(String.valueOf(this.bean.getAcceptBacklog()));
            }

            if (this.bean.isChannelWeightSet()) {
               var2.append("ChannelWeight");
               var2.append(String.valueOf(this.bean.getChannelWeight()));
            }

            if (this.bean.isClusterAddressSet()) {
               var2.append("ClusterAddress");
               var2.append(String.valueOf(this.bean.getClusterAddress()));
            }

            if (this.bean.isCompleteCOMMessageTimeoutSet()) {
               var2.append("CompleteCOMMessageTimeout");
               var2.append(String.valueOf(this.bean.getCompleteCOMMessageTimeout()));
            }

            if (this.bean.isCompleteHTTPMessageTimeoutSet()) {
               var2.append("CompleteHTTPMessageTimeout");
               var2.append(String.valueOf(this.bean.getCompleteHTTPMessageTimeout()));
            }

            if (this.bean.isCompleteIIOPMessageTimeoutSet()) {
               var2.append("CompleteIIOPMessageTimeout");
               var2.append(String.valueOf(this.bean.getCompleteIIOPMessageTimeout()));
            }

            if (this.bean.isCompleteMessageTimeoutSet()) {
               var2.append("CompleteMessageTimeout");
               var2.append(String.valueOf(this.bean.getCompleteMessageTimeout()));
            }

            if (this.bean.isCompleteT3MessageTimeoutSet()) {
               var2.append("CompleteT3MessageTimeout");
               var2.append(String.valueOf(this.bean.getCompleteT3MessageTimeout()));
            }

            if (this.bean.isConnectTimeoutSet()) {
               var2.append("ConnectTimeout");
               var2.append(String.valueOf(this.bean.getConnectTimeout()));
            }

            if (this.bean.isCustomPrivateKeyAliasSet()) {
               var2.append("CustomPrivateKeyAlias");
               var2.append(String.valueOf(this.bean.getCustomPrivateKeyAlias()));
            }

            if (this.bean.isCustomPrivateKeyPassPhraseSet()) {
               var2.append("CustomPrivateKeyPassPhrase");
               var2.append(String.valueOf(this.bean.getCustomPrivateKeyPassPhrase()));
            }

            if (this.bean.isCustomPrivateKeyPassPhraseEncryptedSet()) {
               var2.append("CustomPrivateKeyPassPhraseEncrypted");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getCustomPrivateKeyPassPhraseEncrypted())));
            }

            if (this.bean.isCustomPropertiesSet()) {
               var2.append("CustomProperties");
               var2.append(String.valueOf(this.bean.getCustomProperties()));
            }

            if (this.bean.isExternalDNSNameSet()) {
               var2.append("ExternalDNSName");
               var2.append(String.valueOf(this.bean.getExternalDNSName()));
            }

            if (this.bean.isIdleConnectionTimeoutSet()) {
               var2.append("IdleConnectionTimeout");
               var2.append(String.valueOf(this.bean.getIdleConnectionTimeout()));
            }

            if (this.bean.isIdleIIOPConnectionTimeoutSet()) {
               var2.append("IdleIIOPConnectionTimeout");
               var2.append(String.valueOf(this.bean.getIdleIIOPConnectionTimeout()));
            }

            if (this.bean.isListenAddressSet()) {
               var2.append("ListenAddress");
               var2.append(String.valueOf(this.bean.getListenAddress()));
            }

            if (this.bean.isListenPortSet()) {
               var2.append("ListenPort");
               var2.append(String.valueOf(this.bean.getListenPort()));
            }

            if (this.bean.isLoginTimeoutMillisSet()) {
               var2.append("LoginTimeoutMillis");
               var2.append(String.valueOf(this.bean.getLoginTimeoutMillis()));
            }

            if (this.bean.isLoginTimeoutMillisSSLSet()) {
               var2.append("LoginTimeoutMillisSSL");
               var2.append(String.valueOf(this.bean.getLoginTimeoutMillisSSL()));
            }

            if (this.bean.isMaxBackoffBetweenFailuresSet()) {
               var2.append("MaxBackoffBetweenFailures");
               var2.append(String.valueOf(this.bean.getMaxBackoffBetweenFailures()));
            }

            if (this.bean.isMaxConnectedClientsSet()) {
               var2.append("MaxConnectedClients");
               var2.append(String.valueOf(this.bean.getMaxConnectedClients()));
            }

            if (this.bean.isMaxMessageSizeSet()) {
               var2.append("MaxMessageSize");
               var2.append(String.valueOf(this.bean.getMaxMessageSize()));
            }

            if (this.bean.isNameSet()) {
               var2.append("Name");
               var2.append(String.valueOf(this.bean.getName()));
            }

            if (this.bean.isOutboundPrivateKeyAliasSet()) {
               var2.append("OutboundPrivateKeyAlias");
               var2.append(String.valueOf(this.bean.getOutboundPrivateKeyAlias()));
            }

            if (this.bean.isOutboundPrivateKeyPassPhraseSet()) {
               var2.append("OutboundPrivateKeyPassPhrase");
               var2.append(String.valueOf(this.bean.getOutboundPrivateKeyPassPhrase()));
            }

            if (this.bean.isPrivateKeyAliasSet()) {
               var2.append("PrivateKeyAlias");
               var2.append(String.valueOf(this.bean.getPrivateKeyAlias()));
            }

            if (this.bean.isPrivateKeyPassPhraseSet()) {
               var2.append("PrivateKeyPassPhrase");
               var2.append(String.valueOf(this.bean.getPrivateKeyPassPhrase()));
            }

            if (this.bean.isProtocolSet()) {
               var2.append("Protocol");
               var2.append(String.valueOf(this.bean.getProtocol()));
            }

            if (this.bean.isProxyAddressSet()) {
               var2.append("ProxyAddress");
               var2.append(String.valueOf(this.bean.getProxyAddress()));
            }

            if (this.bean.isProxyPortSet()) {
               var2.append("ProxyPort");
               var2.append(String.valueOf(this.bean.getProxyPort()));
            }

            if (this.bean.isPublicAddressSet()) {
               var2.append("PublicAddress");
               var2.append(String.valueOf(this.bean.getPublicAddress()));
            }

            if (this.bean.isPublicPortSet()) {
               var2.append("PublicPort");
               var2.append(String.valueOf(this.bean.getPublicPort()));
            }

            if (this.bean.isSSLListenPortSet()) {
               var2.append("SSLListenPort");
               var2.append(String.valueOf(this.bean.getSSLListenPort()));
            }

            if (this.bean.isTimeoutConnectionWithPendingResponsesSet()) {
               var2.append("TimeoutConnectionWithPendingResponses");
               var2.append(String.valueOf(this.bean.getTimeoutConnectionWithPendingResponses()));
            }

            if (this.bean.isTunnelingClientPingSecsSet()) {
               var2.append("TunnelingClientPingSecs");
               var2.append(String.valueOf(this.bean.getTunnelingClientPingSecs()));
            }

            if (this.bean.isTunnelingClientTimeoutSecsSet()) {
               var2.append("TunnelingClientTimeoutSecs");
               var2.append(String.valueOf(this.bean.getTunnelingClientTimeoutSecs()));
            }

            if (this.bean.isUseFastSerializationSet()) {
               var2.append("UseFastSerialization");
               var2.append(String.valueOf(this.bean.getUseFastSerialization()));
            }

            if (this.bean.isChannelIdentityCustomizedSet()) {
               var2.append("ChannelIdentityCustomized");
               var2.append(String.valueOf(this.bean.isChannelIdentityCustomized()));
            }

            if (this.bean.isClientCertificateEnforcedSet()) {
               var2.append("ClientCertificateEnforced");
               var2.append(String.valueOf(this.bean.isClientCertificateEnforced()));
            }

            if (this.bean.isEnabledSet()) {
               var2.append("Enabled");
               var2.append(String.valueOf(this.bean.isEnabled()));
            }

            if (this.bean.isHttpEnabledForThisProtocolSet()) {
               var2.append("HttpEnabledForThisProtocol");
               var2.append(String.valueOf(this.bean.isHttpEnabledForThisProtocol()));
            }

            if (this.bean.isOutboundEnabledSet()) {
               var2.append("OutboundEnabled");
               var2.append(String.valueOf(this.bean.isOutboundEnabled()));
            }

            if (this.bean.isOutboundPrivateKeyEnabledSet()) {
               var2.append("OutboundPrivateKeyEnabled");
               var2.append(String.valueOf(this.bean.isOutboundPrivateKeyEnabled()));
            }

            if (this.bean.isSDPEnabledSet()) {
               var2.append("SDPEnabled");
               var2.append(String.valueOf(this.bean.isSDPEnabled()));
            }

            if (this.bean.isTunnelingEnabledSet()) {
               var2.append("TunnelingEnabled");
               var2.append(String.valueOf(this.bean.isTunnelingEnabled()));
            }

            if (this.bean.isTwoWaySSLEnabledSet()) {
               var2.append("TwoWaySSLEnabled");
               var2.append(String.valueOf(this.bean.isTwoWaySSLEnabled()));
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
            NetworkAccessPointMBeanImpl var2 = (NetworkAccessPointMBeanImpl)var1;
            this.computeDiff("AcceptBacklog", this.bean.getAcceptBacklog(), var2.getAcceptBacklog(), true);
            this.computeDiff("ChannelWeight", this.bean.getChannelWeight(), var2.getChannelWeight(), true);
            this.computeDiff("ClusterAddress", this.bean.getClusterAddress(), var2.getClusterAddress(), true);
            this.computeDiff("CompleteCOMMessageTimeout", this.bean.getCompleteCOMMessageTimeout(), var2.getCompleteCOMMessageTimeout(), true);
            this.computeDiff("CompleteHTTPMessageTimeout", this.bean.getCompleteHTTPMessageTimeout(), var2.getCompleteHTTPMessageTimeout(), true);
            this.computeDiff("CompleteIIOPMessageTimeout", this.bean.getCompleteIIOPMessageTimeout(), var2.getCompleteIIOPMessageTimeout(), true);
            this.computeDiff("CompleteMessageTimeout", this.bean.getCompleteMessageTimeout(), var2.getCompleteMessageTimeout(), true);
            this.computeDiff("CompleteT3MessageTimeout", this.bean.getCompleteT3MessageTimeout(), var2.getCompleteT3MessageTimeout(), true);
            this.computeDiff("ConnectTimeout", this.bean.getConnectTimeout(), var2.getConnectTimeout(), true);
            this.computeDiff("CustomPrivateKeyAlias", this.bean.getCustomPrivateKeyAlias(), var2.getCustomPrivateKeyAlias(), true);
            this.computeDiff("CustomPrivateKeyPassPhraseEncrypted", this.bean.getCustomPrivateKeyPassPhraseEncrypted(), var2.getCustomPrivateKeyPassPhraseEncrypted(), false);
            this.computeDiff("CustomProperties", this.bean.getCustomProperties(), var2.getCustomProperties(), false);
            this.computeDiff("ExternalDNSName", this.bean.getExternalDNSName(), var2.getExternalDNSName(), false);
            this.computeDiff("IdleConnectionTimeout", this.bean.getIdleConnectionTimeout(), var2.getIdleConnectionTimeout(), true);
            this.computeDiff("IdleIIOPConnectionTimeout", this.bean.getIdleIIOPConnectionTimeout(), var2.getIdleIIOPConnectionTimeout(), true);
            this.computeDiff("ListenAddress", this.bean.getListenAddress(), var2.getListenAddress(), false);
            this.computeDiff("ListenPort", this.bean.getListenPort(), var2.getListenPort(), false);
            this.computeDiff("LoginTimeoutMillis", this.bean.getLoginTimeoutMillis(), var2.getLoginTimeoutMillis(), true);
            this.computeDiff("LoginTimeoutMillisSSL", this.bean.getLoginTimeoutMillisSSL(), var2.getLoginTimeoutMillisSSL(), true);
            this.computeDiff("MaxBackoffBetweenFailures", this.bean.getMaxBackoffBetweenFailures(), var2.getMaxBackoffBetweenFailures(), true);
            this.computeDiff("MaxConnectedClients", this.bean.getMaxConnectedClients(), var2.getMaxConnectedClients(), true);
            this.computeDiff("MaxMessageSize", this.bean.getMaxMessageSize(), var2.getMaxMessageSize(), true);
            this.computeDiff("Name", this.bean.getName(), var2.getName(), false);
            this.computeDiff("OutboundPrivateKeyAlias", this.bean.getOutboundPrivateKeyAlias(), var2.getOutboundPrivateKeyAlias(), true);
            this.computeDiff("OutboundPrivateKeyPassPhrase", this.bean.getOutboundPrivateKeyPassPhrase(), var2.getOutboundPrivateKeyPassPhrase(), true);
            this.computeDiff("PrivateKeyAlias", this.bean.getPrivateKeyAlias(), var2.getPrivateKeyAlias(), true);
            this.computeDiff("PrivateKeyPassPhrase", this.bean.getPrivateKeyPassPhrase(), var2.getPrivateKeyPassPhrase(), true);
            this.computeDiff("Protocol", this.bean.getProtocol(), var2.getProtocol(), false);
            this.computeDiff("ProxyAddress", this.bean.getProxyAddress(), var2.getProxyAddress(), true);
            this.computeDiff("ProxyPort", this.bean.getProxyPort(), var2.getProxyPort(), true);
            this.computeDiff("PublicAddress", this.bean.getPublicAddress(), var2.getPublicAddress(), true);
            this.computeDiff("PublicPort", this.bean.getPublicPort(), var2.getPublicPort(), true);
            this.computeDiff("SSLListenPort", this.bean.getSSLListenPort(), var2.getSSLListenPort(), false);
            this.computeDiff("TimeoutConnectionWithPendingResponses", this.bean.getTimeoutConnectionWithPendingResponses(), var2.getTimeoutConnectionWithPendingResponses(), true);
            this.computeDiff("TunnelingClientPingSecs", this.bean.getTunnelingClientPingSecs(), var2.getTunnelingClientPingSecs(), true);
            this.computeDiff("TunnelingClientTimeoutSecs", this.bean.getTunnelingClientTimeoutSecs(), var2.getTunnelingClientTimeoutSecs(), true);
            this.computeDiff("UseFastSerialization", this.bean.getUseFastSerialization(), var2.getUseFastSerialization(), true);
            this.computeDiff("ChannelIdentityCustomized", this.bean.isChannelIdentityCustomized(), var2.isChannelIdentityCustomized(), true);
            this.computeDiff("ClientCertificateEnforced", this.bean.isClientCertificateEnforced(), var2.isClientCertificateEnforced(), true);
            this.computeDiff("Enabled", this.bean.isEnabled(), var2.isEnabled(), true);
            this.computeDiff("HttpEnabledForThisProtocol", this.bean.isHttpEnabledForThisProtocol(), var2.isHttpEnabledForThisProtocol(), true);
            this.computeDiff("OutboundEnabled", this.bean.isOutboundEnabled(), var2.isOutboundEnabled(), true);
            this.computeDiff("OutboundPrivateKeyEnabled", this.bean.isOutboundPrivateKeyEnabled(), var2.isOutboundPrivateKeyEnabled(), true);
            this.computeDiff("SDPEnabled", this.bean.isSDPEnabled(), var2.isSDPEnabled(), false);
            this.computeDiff("TunnelingEnabled", this.bean.isTunnelingEnabled(), var2.isTunnelingEnabled(), true);
            this.computeDiff("TwoWaySSLEnabled", this.bean.isTwoWaySSLEnabled(), var2.isTwoWaySSLEnabled(), true);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            NetworkAccessPointMBeanImpl var3 = (NetworkAccessPointMBeanImpl)var1.getSourceBean();
            NetworkAccessPointMBeanImpl var4 = (NetworkAccessPointMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("AcceptBacklog")) {
                  var3.setAcceptBacklog(var4.getAcceptBacklog());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 15);
               } else if (var5.equals("ChannelWeight")) {
                  var3.setChannelWeight(var4.getChannelWeight());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 27);
               } else if (var5.equals("ClusterAddress")) {
                  var3.setClusterAddress(var4.getClusterAddress());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 28);
               } else if (var5.equals("CompleteCOMMessageTimeout")) {
                  var3.setCompleteCOMMessageTimeout(var4.getCompleteCOMMessageTimeout());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 47);
               } else if (var5.equals("CompleteHTTPMessageTimeout")) {
                  var3.setCompleteHTTPMessageTimeout(var4.getCompleteHTTPMessageTimeout());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 46);
               } else if (var5.equals("CompleteIIOPMessageTimeout")) {
                  var3.setCompleteIIOPMessageTimeout(var4.getCompleteIIOPMessageTimeout());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 48);
               } else if (var5.equals("CompleteMessageTimeout")) {
                  var3.setCompleteMessageTimeout(var4.getCompleteMessageTimeout());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 21);
               } else if (var5.equals("CompleteT3MessageTimeout")) {
                  var3.setCompleteT3MessageTimeout(var4.getCompleteT3MessageTimeout());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 45);
               } else if (var5.equals("ConnectTimeout")) {
                  var3.setConnectTimeout(var4.getConnectTimeout());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 24);
               } else if (var5.equals("CustomPrivateKeyAlias")) {
                  var3.setCustomPrivateKeyAlias(var4.getCustomPrivateKeyAlias());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 33);
               } else if (!var5.equals("CustomPrivateKeyPassPhrase")) {
                  if (var5.equals("CustomPrivateKeyPassPhraseEncrypted")) {
                     var3.setCustomPrivateKeyPassPhraseEncrypted(var4.getCustomPrivateKeyPassPhraseEncrypted());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 37);
                  } else if (var5.equals("CustomProperties")) {
                     var3.setCustomProperties(var4.getCustomProperties() == null ? null : (Properties)var4.getCustomProperties().clone());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 49);
                  } else if (var5.equals("ExternalDNSName")) {
                     var3.setExternalDNSName(var4.getExternalDNSName());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 43);
                  } else if (var5.equals("IdleConnectionTimeout")) {
                     var3.setIdleConnectionTimeout(var4.getIdleConnectionTimeout());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 23);
                  } else if (var5.equals("IdleIIOPConnectionTimeout")) {
                     var3.setIdleIIOPConnectionTimeout(var4.getIdleIIOPConnectionTimeout());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 41);
                  } else if (var5.equals("ListenAddress")) {
                     var3.setListenAddress(var4.getListenAddress());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 8);
                  } else if (var5.equals("ListenPort")) {
                     var3.setListenPort(var4.getListenPort());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 10);
                  } else if (var5.equals("LoginTimeoutMillis")) {
                     var3.setLoginTimeoutMillis(var4.getLoginTimeoutMillis());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 17);
                  } else if (var5.equals("LoginTimeoutMillisSSL")) {
                     var3.setLoginTimeoutMillisSSL(var4.getLoginTimeoutMillisSSL());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 44);
                  } else if (var5.equals("MaxBackoffBetweenFailures")) {
                     var3.setMaxBackoffBetweenFailures(var4.getMaxBackoffBetweenFailures());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 16);
                  } else if (var5.equals("MaxConnectedClients")) {
                     var3.setMaxConnectedClients(var4.getMaxConnectedClients());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 30);
                  } else if (var5.equals("MaxMessageSize")) {
                     var3.setMaxMessageSize(var4.getMaxMessageSize());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 25);
                  } else if (var5.equals("Name")) {
                     var3.setName(var4.getName());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 2);
                  } else if (var5.equals("OutboundPrivateKeyAlias")) {
                     var3._conditionalUnset(var2.isUnsetUpdate(), 51);
                  } else if (var5.equals("OutboundPrivateKeyPassPhrase")) {
                     var3._conditionalUnset(var2.isUnsetUpdate(), 52);
                  } else if (var5.equals("PrivateKeyAlias")) {
                     var3._conditionalUnset(var2.isUnsetUpdate(), 34);
                  } else if (var5.equals("PrivateKeyPassPhrase")) {
                     var3._conditionalUnset(var2.isUnsetUpdate(), 36);
                  } else if (var5.equals("Protocol")) {
                     var3.setProtocol(var4.getProtocol());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 7);
                  } else if (var5.equals("ProxyAddress")) {
                     var3.setProxyAddress(var4.getProxyAddress());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 12);
                  } else if (var5.equals("ProxyPort")) {
                     var3.setProxyPort(var4.getProxyPort());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 13);
                  } else if (var5.equals("PublicAddress")) {
                     var3.setPublicAddress(var4.getPublicAddress());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 9);
                  } else if (var5.equals("PublicPort")) {
                     var3.setPublicPort(var4.getPublicPort());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 11);
                  } else if (var5.equals("SSLListenPort")) {
                     var3.setSSLListenPort(var4.getSSLListenPort());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 42);
                  } else if (var5.equals("TimeoutConnectionWithPendingResponses")) {
                     var3.setTimeoutConnectionWithPendingResponses(var4.getTimeoutConnectionWithPendingResponses());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 22);
                  } else if (var5.equals("TunnelingClientPingSecs")) {
                     var3.setTunnelingClientPingSecs(var4.getTunnelingClientPingSecs());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 18);
                  } else if (var5.equals("TunnelingClientTimeoutSecs")) {
                     var3.setTunnelingClientTimeoutSecs(var4.getTunnelingClientTimeoutSecs());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 19);
                  } else if (var5.equals("UseFastSerialization")) {
                     var3.setUseFastSerialization(var4.getUseFastSerialization());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 40);
                  } else if (var5.equals("ChannelIdentityCustomized")) {
                     var3.setChannelIdentityCustomized(var4.isChannelIdentityCustomized());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 32);
                  } else if (var5.equals("ClientCertificateEnforced")) {
                     var3.setClientCertificateEnforced(var4.isClientCertificateEnforced());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 38);
                  } else if (var5.equals("Enabled")) {
                     var3.setEnabled(var4.isEnabled());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 29);
                  } else if (var5.equals("HttpEnabledForThisProtocol")) {
                     var3.setHttpEnabledForThisProtocol(var4.isHttpEnabledForThisProtocol());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 14);
                  } else if (var5.equals("OutboundEnabled")) {
                     var3.setOutboundEnabled(var4.isOutboundEnabled());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 26);
                  } else if (var5.equals("OutboundPrivateKeyEnabled")) {
                     var3.setOutboundPrivateKeyEnabled(var4.isOutboundPrivateKeyEnabled());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 39);
                  } else if (var5.equals("SDPEnabled")) {
                     var3.setSDPEnabled(var4.isSDPEnabled());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 50);
                  } else if (var5.equals("TunnelingEnabled")) {
                     var3.setTunnelingEnabled(var4.isTunnelingEnabled());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 20);
                  } else if (var5.equals("TwoWaySSLEnabled")) {
                     var3.setTwoWaySSLEnabled(var4.isTwoWaySSLEnabled());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 31);
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
            NetworkAccessPointMBeanImpl var5 = (NetworkAccessPointMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("AcceptBacklog")) && this.bean.isAcceptBacklogSet()) {
               var5.setAcceptBacklog(this.bean.getAcceptBacklog());
            }

            if ((var3 == null || !var3.contains("ChannelWeight")) && this.bean.isChannelWeightSet()) {
               var5.setChannelWeight(this.bean.getChannelWeight());
            }

            if ((var3 == null || !var3.contains("ClusterAddress")) && this.bean.isClusterAddressSet()) {
               var5.setClusterAddress(this.bean.getClusterAddress());
            }

            if ((var3 == null || !var3.contains("CompleteCOMMessageTimeout")) && this.bean.isCompleteCOMMessageTimeoutSet()) {
               var5.setCompleteCOMMessageTimeout(this.bean.getCompleteCOMMessageTimeout());
            }

            if ((var3 == null || !var3.contains("CompleteHTTPMessageTimeout")) && this.bean.isCompleteHTTPMessageTimeoutSet()) {
               var5.setCompleteHTTPMessageTimeout(this.bean.getCompleteHTTPMessageTimeout());
            }

            if ((var3 == null || !var3.contains("CompleteIIOPMessageTimeout")) && this.bean.isCompleteIIOPMessageTimeoutSet()) {
               var5.setCompleteIIOPMessageTimeout(this.bean.getCompleteIIOPMessageTimeout());
            }

            if ((var3 == null || !var3.contains("CompleteMessageTimeout")) && this.bean.isCompleteMessageTimeoutSet()) {
               var5.setCompleteMessageTimeout(this.bean.getCompleteMessageTimeout());
            }

            if ((var3 == null || !var3.contains("CompleteT3MessageTimeout")) && this.bean.isCompleteT3MessageTimeoutSet()) {
               var5.setCompleteT3MessageTimeout(this.bean.getCompleteT3MessageTimeout());
            }

            if ((var3 == null || !var3.contains("ConnectTimeout")) && this.bean.isConnectTimeoutSet()) {
               var5.setConnectTimeout(this.bean.getConnectTimeout());
            }

            if ((var3 == null || !var3.contains("CustomPrivateKeyAlias")) && this.bean.isCustomPrivateKeyAliasSet()) {
               var5.setCustomPrivateKeyAlias(this.bean.getCustomPrivateKeyAlias());
            }

            if ((var3 == null || !var3.contains("CustomPrivateKeyPassPhraseEncrypted")) && this.bean.isCustomPrivateKeyPassPhraseEncryptedSet()) {
               byte[] var4 = this.bean.getCustomPrivateKeyPassPhraseEncrypted();
               var5.setCustomPrivateKeyPassPhraseEncrypted(var4 == null ? null : (byte[])((byte[])((byte[])((byte[])var4)).clone()));
            }

            if ((var3 == null || !var3.contains("CustomProperties")) && this.bean.isCustomPropertiesSet()) {
               var5.setCustomProperties(this.bean.getCustomProperties());
            }

            if ((var3 == null || !var3.contains("ExternalDNSName")) && this.bean.isExternalDNSNameSet()) {
               var5.setExternalDNSName(this.bean.getExternalDNSName());
            }

            if ((var3 == null || !var3.contains("IdleConnectionTimeout")) && this.bean.isIdleConnectionTimeoutSet()) {
               var5.setIdleConnectionTimeout(this.bean.getIdleConnectionTimeout());
            }

            if ((var3 == null || !var3.contains("IdleIIOPConnectionTimeout")) && this.bean.isIdleIIOPConnectionTimeoutSet()) {
               var5.setIdleIIOPConnectionTimeout(this.bean.getIdleIIOPConnectionTimeout());
            }

            if ((var3 == null || !var3.contains("ListenAddress")) && this.bean.isListenAddressSet()) {
               var5.setListenAddress(this.bean.getListenAddress());
            }

            if ((var3 == null || !var3.contains("ListenPort")) && this.bean.isListenPortSet()) {
               var5.setListenPort(this.bean.getListenPort());
            }

            if ((var3 == null || !var3.contains("LoginTimeoutMillis")) && this.bean.isLoginTimeoutMillisSet()) {
               var5.setLoginTimeoutMillis(this.bean.getLoginTimeoutMillis());
            }

            if ((var3 == null || !var3.contains("LoginTimeoutMillisSSL")) && this.bean.isLoginTimeoutMillisSSLSet()) {
               var5.setLoginTimeoutMillisSSL(this.bean.getLoginTimeoutMillisSSL());
            }

            if ((var3 == null || !var3.contains("MaxBackoffBetweenFailures")) && this.bean.isMaxBackoffBetweenFailuresSet()) {
               var5.setMaxBackoffBetweenFailures(this.bean.getMaxBackoffBetweenFailures());
            }

            if ((var3 == null || !var3.contains("MaxConnectedClients")) && this.bean.isMaxConnectedClientsSet()) {
               var5.setMaxConnectedClients(this.bean.getMaxConnectedClients());
            }

            if ((var3 == null || !var3.contains("MaxMessageSize")) && this.bean.isMaxMessageSizeSet()) {
               var5.setMaxMessageSize(this.bean.getMaxMessageSize());
            }

            if ((var3 == null || !var3.contains("Name")) && this.bean.isNameSet()) {
               var5.setName(this.bean.getName());
            }

            if ((var3 == null || !var3.contains("OutboundPrivateKeyAlias")) && this.bean.isOutboundPrivateKeyAliasSet()) {
            }

            if ((var3 == null || !var3.contains("OutboundPrivateKeyPassPhrase")) && this.bean.isOutboundPrivateKeyPassPhraseSet()) {
            }

            if ((var3 == null || !var3.contains("PrivateKeyAlias")) && this.bean.isPrivateKeyAliasSet()) {
            }

            if ((var3 == null || !var3.contains("PrivateKeyPassPhrase")) && this.bean.isPrivateKeyPassPhraseSet()) {
            }

            if ((var3 == null || !var3.contains("Protocol")) && this.bean.isProtocolSet()) {
               var5.setProtocol(this.bean.getProtocol());
            }

            if ((var3 == null || !var3.contains("ProxyAddress")) && this.bean.isProxyAddressSet()) {
               var5.setProxyAddress(this.bean.getProxyAddress());
            }

            if ((var3 == null || !var3.contains("ProxyPort")) && this.bean.isProxyPortSet()) {
               var5.setProxyPort(this.bean.getProxyPort());
            }

            if ((var3 == null || !var3.contains("PublicAddress")) && this.bean.isPublicAddressSet()) {
               var5.setPublicAddress(this.bean.getPublicAddress());
            }

            if ((var3 == null || !var3.contains("PublicPort")) && this.bean.isPublicPortSet()) {
               var5.setPublicPort(this.bean.getPublicPort());
            }

            if ((var3 == null || !var3.contains("SSLListenPort")) && this.bean.isSSLListenPortSet()) {
               var5.setSSLListenPort(this.bean.getSSLListenPort());
            }

            if ((var3 == null || !var3.contains("TimeoutConnectionWithPendingResponses")) && this.bean.isTimeoutConnectionWithPendingResponsesSet()) {
               var5.setTimeoutConnectionWithPendingResponses(this.bean.getTimeoutConnectionWithPendingResponses());
            }

            if ((var3 == null || !var3.contains("TunnelingClientPingSecs")) && this.bean.isTunnelingClientPingSecsSet()) {
               var5.setTunnelingClientPingSecs(this.bean.getTunnelingClientPingSecs());
            }

            if ((var3 == null || !var3.contains("TunnelingClientTimeoutSecs")) && this.bean.isTunnelingClientTimeoutSecsSet()) {
               var5.setTunnelingClientTimeoutSecs(this.bean.getTunnelingClientTimeoutSecs());
            }

            if ((var3 == null || !var3.contains("UseFastSerialization")) && this.bean.isUseFastSerializationSet()) {
               var5.setUseFastSerialization(this.bean.getUseFastSerialization());
            }

            if ((var3 == null || !var3.contains("ChannelIdentityCustomized")) && this.bean.isChannelIdentityCustomizedSet()) {
               var5.setChannelIdentityCustomized(this.bean.isChannelIdentityCustomized());
            }

            if ((var3 == null || !var3.contains("ClientCertificateEnforced")) && this.bean.isClientCertificateEnforcedSet()) {
               var5.setClientCertificateEnforced(this.bean.isClientCertificateEnforced());
            }

            if ((var3 == null || !var3.contains("Enabled")) && this.bean.isEnabledSet()) {
               var5.setEnabled(this.bean.isEnabled());
            }

            if ((var3 == null || !var3.contains("HttpEnabledForThisProtocol")) && this.bean.isHttpEnabledForThisProtocolSet()) {
               var5.setHttpEnabledForThisProtocol(this.bean.isHttpEnabledForThisProtocol());
            }

            if ((var3 == null || !var3.contains("OutboundEnabled")) && this.bean.isOutboundEnabledSet()) {
               var5.setOutboundEnabled(this.bean.isOutboundEnabled());
            }

            if ((var3 == null || !var3.contains("OutboundPrivateKeyEnabled")) && this.bean.isOutboundPrivateKeyEnabledSet()) {
               var5.setOutboundPrivateKeyEnabled(this.bean.isOutboundPrivateKeyEnabled());
            }

            if ((var3 == null || !var3.contains("SDPEnabled")) && this.bean.isSDPEnabledSet()) {
               var5.setSDPEnabled(this.bean.isSDPEnabled());
            }

            if ((var3 == null || !var3.contains("TunnelingEnabled")) && this.bean.isTunnelingEnabledSet()) {
               var5.setTunnelingEnabled(this.bean.isTunnelingEnabled());
            }

            if ((var3 == null || !var3.contains("TwoWaySSLEnabled")) && this.bean.isTwoWaySSLEnabledSet()) {
               var5.setTwoWaySSLEnabled(this.bean.isTwoWaySSLEnabled());
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
