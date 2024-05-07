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
import weblogic.management.DistributedManagementException;
import weblogic.management.ManagementException;
import weblogic.utils.ArrayUtils;
import weblogic.utils.collections.CombinedIterator;

public class NetworkChannelMBeanImpl extends DeploymentMBeanImpl implements NetworkChannelMBean, Serializable {
   private int _AcceptBacklog;
   private boolean _BoundOutgoingEnabled;
   private boolean _COMEnabled;
   private int _ChannelWeight;
   private String _ClusterAddress;
   private int _CompleteCOMMessageTimeout;
   private int _CompleteHTTPMessageTimeout;
   private int _CompleteIIOPMessageTimeout;
   private int _CompleteT3MessageTimeout;
   private String _DefaultIIOPPassword;
   private byte[] _DefaultIIOPPasswordEncrypted;
   private String _DefaultIIOPUser;
   private String _Description;
   private boolean _HTTPEnabled;
   private boolean _HTTPSEnabled;
   private boolean _IIOPEnabled;
   private boolean _IIOPSEnabled;
   private int _IdleIIOPConnectionTimeout;
   private int _ListenPort;
   private boolean _ListenPortEnabled;
   private int _LoginTimeoutMillis;
   private int _LoginTimeoutMillisSSL;
   private int _MaxCOMMessageSize;
   private int _MaxHTTPMessageSize;
   private int _MaxIIOPMessageSize;
   private int _MaxT3MessageSize;
   private String _Name;
   private boolean _OutgoingEnabled;
   private int _SSLListenPort;
   private boolean _SSLListenPortEnabled;
   private boolean _T3Enabled;
   private boolean _T3SEnabled;
   private int _TunnelingClientPingSecs;
   private int _TunnelingClientTimeoutSecs;
   private boolean _TunnelingEnabled;
   private static SchemaHelper2 _schemaHelper;

   public NetworkChannelMBeanImpl() {
      this._initializeProperty(-1);
   }

   public NetworkChannelMBeanImpl(DescriptorBean var1, int var2) {
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
      NetworkChannelValidator.validateName(var1);
      String var2 = this._Name;
      this._Name = var1;
      this._postSet(2, var2, var1);
   }

   public String getDescription() {
      return this._Description;
   }

   public boolean isDescriptionSet() {
      return this._isSet(9);
   }

   public void setDescription(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._Description;
      this._Description = var1;
      this._postSet(9, var2, var1);
   }

   public int getListenPort() {
      return this._ListenPort;
   }

   public boolean isListenPortSet() {
      return this._isSet(10);
   }

   public void setListenPort(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("ListenPort", (long)var1, 1L, 65535L);
      int var2 = this._ListenPort;
      this._ListenPort = var1;
      this._postSet(10, var2, var1);
   }

   public boolean isListenPortEnabled() {
      return this._ListenPortEnabled;
   }

   public boolean isListenPortEnabledSet() {
      return this._isSet(11);
   }

   public void setListenPortEnabled(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this._ListenPortEnabled;
      this._ListenPortEnabled = var1;
      this._postSet(11, var2, var1);
   }

   public int getSSLListenPort() {
      return this._SSLListenPort;
   }

   public boolean isSSLListenPortSet() {
      return this._isSet(12);
   }

   public void setSSLListenPort(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("SSLListenPort", (long)var1, 1L, 65535L);
      int var2 = this._SSLListenPort;
      this._SSLListenPort = var1;
      this._postSet(12, var2, var1);
   }

   public boolean isSSLListenPortEnabled() {
      return this._SSLListenPortEnabled;
   }

   public boolean isSSLListenPortEnabledSet() {
      return this._isSet(13);
   }

   public void setSSLListenPortEnabled(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this._SSLListenPortEnabled;
      this._SSLListenPortEnabled = var1;
      this._postSet(13, var2, var1);
   }

   public String getClusterAddress() {
      return this._ClusterAddress;
   }

   public boolean isClusterAddressSet() {
      return this._isSet(14);
   }

   public void setClusterAddress(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._ClusterAddress;
      this._ClusterAddress = var1;
      this._postSet(14, var2, var1);
   }

   public boolean isT3Enabled() {
      return this._T3Enabled;
   }

   public boolean isT3EnabledSet() {
      return this._isSet(15);
   }

   public void setT3Enabled(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this._T3Enabled;
      this._T3Enabled = var1;
      this._postSet(15, var2, var1);
   }

   public boolean isT3SEnabled() {
      return this._T3SEnabled;
   }

   public boolean isT3SEnabledSet() {
      return this._isSet(16);
   }

   public void setT3SEnabled(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this._T3SEnabled;
      this._T3SEnabled = var1;
      this._postSet(16, var2, var1);
   }

   public boolean isHTTPEnabled() {
      return this._HTTPEnabled;
   }

   public boolean isHTTPEnabledSet() {
      return this._isSet(17);
   }

   public void setHTTPEnabled(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this._HTTPEnabled;
      this._HTTPEnabled = var1;
      this._postSet(17, var2, var1);
   }

   public boolean isHTTPSEnabled() {
      return this._HTTPSEnabled;
   }

   public boolean isHTTPSEnabledSet() {
      return this._isSet(18);
   }

   public void setHTTPSEnabled(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this._HTTPSEnabled;
      this._HTTPSEnabled = var1;
      this._postSet(18, var2, var1);
   }

   public boolean isCOMEnabled() {
      return this._COMEnabled;
   }

   public boolean isCOMEnabledSet() {
      return this._isSet(19);
   }

   public void setCOMEnabled(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this._COMEnabled;
      this._COMEnabled = var1;
      this._postSet(19, var2, var1);
   }

   public boolean isOutgoingEnabled() {
      return this._OutgoingEnabled;
   }

   public boolean isOutgoingEnabledSet() {
      return this._isSet(20);
   }

   public void setOutgoingEnabled(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this._OutgoingEnabled;
      this._OutgoingEnabled = var1;
      this._postSet(20, var2, var1);
   }

   public boolean isBoundOutgoingEnabled() {
      return this._BoundOutgoingEnabled;
   }

   public boolean isBoundOutgoingEnabledSet() {
      return this._isSet(21);
   }

   public void setBoundOutgoingEnabled(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this._BoundOutgoingEnabled;
      this._BoundOutgoingEnabled = var1;
      this._postSet(21, var2, var1);
   }

   public int getChannelWeight() {
      return this._ChannelWeight;
   }

   public boolean isChannelWeightSet() {
      return this._isSet(22);
   }

   public void setChannelWeight(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("ChannelWeight", (long)var1, 1L, 100L);
      int var2 = this._ChannelWeight;
      this._ChannelWeight = var1;
      this._postSet(22, var2, var1);
   }

   public int getAcceptBacklog() {
      return this._AcceptBacklog;
   }

   public boolean isAcceptBacklogSet() {
      return this._isSet(23);
   }

   public void setAcceptBacklog(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkMin("AcceptBacklog", var1, 0);
      int var2 = this._AcceptBacklog;
      this._AcceptBacklog = var1;
      this._postSet(23, var2, var1);
   }

   public int getLoginTimeoutMillis() {
      return this._LoginTimeoutMillis;
   }

   public boolean isLoginTimeoutMillisSet() {
      return this._isSet(24);
   }

   public void setLoginTimeoutMillis(int var1) throws InvalidAttributeValueException, DistributedManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("LoginTimeoutMillis", (long)var1, 0L, 100000L);
      int var2 = this._LoginTimeoutMillis;
      this._LoginTimeoutMillis = var1;
      this._postSet(24, var2, var1);
   }

   public int getLoginTimeoutMillisSSL() {
      return this._LoginTimeoutMillisSSL;
   }

   public boolean isLoginTimeoutMillisSSLSet() {
      return this._isSet(25);
   }

   public void setLoginTimeoutMillisSSL(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("LoginTimeoutMillisSSL", (long)var1, 0L, 2147483647L);
      int var2 = this._LoginTimeoutMillisSSL;
      this._LoginTimeoutMillisSSL = var1;
      this._postSet(25, var2, var1);
   }

   public boolean isTunnelingEnabled() {
      return this._TunnelingEnabled;
   }

   public boolean isTunnelingEnabledSet() {
      return this._isSet(26);
   }

   public void setTunnelingEnabled(boolean var1) throws DistributedManagementException {
      boolean var2 = this._TunnelingEnabled;
      this._TunnelingEnabled = var1;
      this._postSet(26, var2, var1);
   }

   public int getTunnelingClientPingSecs() {
      return this._TunnelingClientPingSecs;
   }

   public boolean isTunnelingClientPingSecsSet() {
      return this._isSet(27);
   }

   public void setTunnelingClientPingSecs(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkMin("TunnelingClientPingSecs", var1, 1);
      int var2 = this._TunnelingClientPingSecs;
      this._TunnelingClientPingSecs = var1;
      this._postSet(27, var2, var1);
   }

   public int getTunnelingClientTimeoutSecs() {
      return this._TunnelingClientTimeoutSecs;
   }

   public boolean isTunnelingClientTimeoutSecsSet() {
      return this._isSet(28);
   }

   public void setTunnelingClientTimeoutSecs(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkMin("TunnelingClientTimeoutSecs", var1, 1);
      int var2 = this._TunnelingClientTimeoutSecs;
      this._TunnelingClientTimeoutSecs = var1;
      this._postSet(28, var2, var1);
   }

   public int getCompleteT3MessageTimeout() {
      return this._CompleteT3MessageTimeout;
   }

   public boolean isCompleteT3MessageTimeoutSet() {
      return this._isSet(29);
   }

   public void setCompleteT3MessageTimeout(int var1) throws InvalidAttributeValueException, DistributedManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("CompleteT3MessageTimeout", (long)var1, 0L, 480L);
      int var2 = this._CompleteT3MessageTimeout;
      this._CompleteT3MessageTimeout = var1;
      this._postSet(29, var2, var1);
   }

   public int getCompleteHTTPMessageTimeout() {
      return this._CompleteHTTPMessageTimeout;
   }

   public boolean isCompleteHTTPMessageTimeoutSet() {
      return this._isSet(30);
   }

   public void setCompleteHTTPMessageTimeout(int var1) throws InvalidAttributeValueException, DistributedManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("CompleteHTTPMessageTimeout", (long)var1, 0L, 480L);
      int var2 = this._CompleteHTTPMessageTimeout;
      this._CompleteHTTPMessageTimeout = var1;
      this._postSet(30, var2, var1);
   }

   public int getCompleteCOMMessageTimeout() {
      return this._CompleteCOMMessageTimeout;
   }

   public boolean isCompleteCOMMessageTimeoutSet() {
      return this._isSet(31);
   }

   public void setCompleteCOMMessageTimeout(int var1) throws InvalidAttributeValueException, DistributedManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("CompleteCOMMessageTimeout", (long)var1, 0L, 480L);
      int var2 = this._CompleteCOMMessageTimeout;
      this._CompleteCOMMessageTimeout = var1;
      this._postSet(31, var2, var1);
   }

   public int getMaxT3MessageSize() {
      return this._MaxT3MessageSize;
   }

   public boolean isMaxT3MessageSizeSet() {
      return this._isSet(32);
   }

   public void setMaxT3MessageSize(int var1) throws InvalidAttributeValueException, DistributedManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("MaxT3MessageSize", (long)var1, 4096L, 2000000000L);
      int var2 = this._MaxT3MessageSize;
      this._MaxT3MessageSize = var1;
      this._postSet(32, var2, var1);
   }

   public int getMaxHTTPMessageSize() {
      return this._MaxHTTPMessageSize;
   }

   public boolean isMaxHTTPMessageSizeSet() {
      return this._isSet(33);
   }

   public void setMaxHTTPMessageSize(int var1) throws InvalidAttributeValueException, DistributedManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("MaxHTTPMessageSize", (long)var1, 4096L, 2000000000L);
      int var2 = this._MaxHTTPMessageSize;
      this._MaxHTTPMessageSize = var1;
      this._postSet(33, var2, var1);
   }

   public int getMaxCOMMessageSize() {
      return this._MaxCOMMessageSize;
   }

   public boolean isMaxCOMMessageSizeSet() {
      return this._isSet(34);
   }

   public void setMaxCOMMessageSize(int var1) throws InvalidAttributeValueException, DistributedManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("MaxCOMMessageSize", (long)var1, 4096L, 2000000000L);
      int var2 = this._MaxCOMMessageSize;
      this._MaxCOMMessageSize = var1;
      this._postSet(34, var2, var1);
   }

   public boolean isIIOPEnabled() {
      return this._IIOPEnabled;
   }

   public boolean isIIOPEnabledSet() {
      return this._isSet(35);
   }

   public void setIIOPEnabled(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this._IIOPEnabled;
      this._IIOPEnabled = var1;
      this._postSet(35, var2, var1);
   }

   public boolean isIIOPSEnabled() {
      return this._IIOPSEnabled;
   }

   public boolean isIIOPSEnabledSet() {
      return this._isSet(36);
   }

   public void setIIOPSEnabled(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this._IIOPSEnabled;
      this._IIOPSEnabled = var1;
      this._postSet(36, var2, var1);
   }

   public int getCompleteIIOPMessageTimeout() {
      return this._CompleteIIOPMessageTimeout;
   }

   public boolean isCompleteIIOPMessageTimeoutSet() {
      return this._isSet(37);
   }

   public void setCompleteIIOPMessageTimeout(int var1) throws InvalidAttributeValueException, DistributedManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("CompleteIIOPMessageTimeout", (long)var1, 0L, 480L);
      int var2 = this._CompleteIIOPMessageTimeout;
      this._CompleteIIOPMessageTimeout = var1;
      this._postSet(37, var2, var1);
   }

   public int getMaxIIOPMessageSize() {
      return this._MaxIIOPMessageSize;
   }

   public boolean isMaxIIOPMessageSizeSet() {
      return this._isSet(38);
   }

   public void setMaxIIOPMessageSize(int var1) throws InvalidAttributeValueException, DistributedManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("MaxIIOPMessageSize", (long)var1, 4096L, 2000000000L);
      int var2 = this._MaxIIOPMessageSize;
      this._MaxIIOPMessageSize = var1;
      this._postSet(38, var2, var1);
   }

   public int getIdleIIOPConnectionTimeout() {
      return this._IdleIIOPConnectionTimeout;
   }

   public boolean isIdleIIOPConnectionTimeoutSet() {
      return this._isSet(39);
   }

   public void setIdleIIOPConnectionTimeout(int var1) throws InvalidAttributeValueException, DistributedManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkMin("IdleIIOPConnectionTimeout", var1, 0);
      int var2 = this._IdleIIOPConnectionTimeout;
      this._IdleIIOPConnectionTimeout = var1;
      this._postSet(39, var2, var1);
   }

   public String getDefaultIIOPUser() {
      return this._DefaultIIOPUser;
   }

   public boolean isDefaultIIOPUserSet() {
      return this._isSet(40);
   }

   public void setDefaultIIOPUser(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._DefaultIIOPUser;
      this._DefaultIIOPUser = var1;
      this._postSet(40, var2, var1);
   }

   public String getDefaultIIOPPassword() {
      byte[] var1 = this.getDefaultIIOPPasswordEncrypted();
      return var1 == null ? null : this._decrypt("DefaultIIOPPassword", var1);
   }

   public boolean isDefaultIIOPPasswordSet() {
      return this.isDefaultIIOPPasswordEncryptedSet();
   }

   public void setDefaultIIOPPassword(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      this.setDefaultIIOPPasswordEncrypted(var1 == null ? null : this._encrypt("DefaultIIOPPassword", var1));
   }

   public byte[] getDefaultIIOPPasswordEncrypted() {
      return this._getHelper()._cloneArray(this._DefaultIIOPPasswordEncrypted);
   }

   public String getDefaultIIOPPasswordEncryptedAsString() {
      byte[] var1 = this.getDefaultIIOPPasswordEncrypted();
      return var1 == null ? null : new String(var1);
   }

   public boolean isDefaultIIOPPasswordEncryptedSet() {
      return this._isSet(42);
   }

   public void setDefaultIIOPPasswordEncryptedAsString(String var1) {
      try {
         byte[] var2 = var1 == null ? null : var1.getBytes();
         this.setDefaultIIOPPasswordEncrypted(var2);
      } catch (Exception var3) {
         if (var3 instanceof RuntimeException) {
            throw (RuntimeException)var3;
         } else {
            throw new UndeclaredThrowableException(var3);
         }
      }
   }

   public Object _getKey() {
      return this.getName();
   }

   public void _validate() throws IllegalArgumentException {
      super._validate();
   }

   public void setDefaultIIOPPasswordEncrypted(byte[] var1) {
      byte[] var2 = this._DefaultIIOPPasswordEncrypted;
      if (this._isProductionModeEnabled() && var1 != null && !this._isEncrypted(var1)) {
         throw new IllegalArgumentException("In production mode, it's not allowed to set a clear text value to the property: DefaultIIOPPasswordEncrypted of NetworkChannelMBean");
      } else {
         this._getHelper()._clearArray(this._DefaultIIOPPasswordEncrypted);
         this._DefaultIIOPPasswordEncrypted = this._getHelper()._cloneArray(var1);
         this._postSet(42, var2, var1);
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
         if (var1 == 41) {
            this._markSet(42, false);
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
         var1 = 23;
      }

      try {
         switch (var1) {
            case 23:
               this._AcceptBacklog = 50;
               if (var2) {
                  break;
               }
            case 22:
               this._ChannelWeight = 50;
               if (var2) {
                  break;
               }
            case 14:
               this._ClusterAddress = null;
               if (var2) {
                  break;
               }
            case 31:
               this._CompleteCOMMessageTimeout = 60;
               if (var2) {
                  break;
               }
            case 30:
               this._CompleteHTTPMessageTimeout = 60;
               if (var2) {
                  break;
               }
            case 37:
               this._CompleteIIOPMessageTimeout = 60;
               if (var2) {
                  break;
               }
            case 29:
               this._CompleteT3MessageTimeout = 60;
               if (var2) {
                  break;
               }
            case 41:
               this._DefaultIIOPPasswordEncrypted = null;
               if (var2) {
                  break;
               }
            case 42:
               this._DefaultIIOPPasswordEncrypted = null;
               if (var2) {
                  break;
               }
            case 40:
               this._DefaultIIOPUser = null;
               if (var2) {
                  break;
               }
            case 9:
               this._Description = null;
               if (var2) {
                  break;
               }
            case 39:
               this._IdleIIOPConnectionTimeout = 60;
               if (var2) {
                  break;
               }
            case 10:
               this._ListenPort = 8001;
               if (var2) {
                  break;
               }
            case 24:
               this._LoginTimeoutMillis = 5000;
               if (var2) {
                  break;
               }
            case 25:
               this._LoginTimeoutMillisSSL = 25000;
               if (var2) {
                  break;
               }
            case 34:
               this._MaxCOMMessageSize = 10000000;
               if (var2) {
                  break;
               }
            case 33:
               this._MaxHTTPMessageSize = 10000000;
               if (var2) {
                  break;
               }
            case 38:
               this._MaxIIOPMessageSize = 10000000;
               if (var2) {
                  break;
               }
            case 32:
               this._MaxT3MessageSize = 10000000;
               if (var2) {
                  break;
               }
            case 2:
               this._Name = null;
               if (var2) {
                  break;
               }
            case 12:
               this._SSLListenPort = 8002;
               if (var2) {
                  break;
               }
            case 27:
               this._TunnelingClientPingSecs = 45;
               if (var2) {
                  break;
               }
            case 28:
               this._TunnelingClientTimeoutSecs = 40;
               if (var2) {
                  break;
               }
            case 21:
               this._BoundOutgoingEnabled = false;
               if (var2) {
                  break;
               }
            case 19:
               this._COMEnabled = false;
               if (var2) {
                  break;
               }
            case 17:
               this._HTTPEnabled = false;
               if (var2) {
                  break;
               }
            case 18:
               this._HTTPSEnabled = false;
               if (var2) {
                  break;
               }
            case 35:
               this._IIOPEnabled = false;
               if (var2) {
                  break;
               }
            case 36:
               this._IIOPSEnabled = false;
               if (var2) {
                  break;
               }
            case 11:
               this._ListenPortEnabled = false;
               if (var2) {
                  break;
               }
            case 20:
               this._OutgoingEnabled = false;
               if (var2) {
                  break;
               }
            case 13:
               this._SSLListenPortEnabled = false;
               if (var2) {
                  break;
               }
            case 15:
               this._T3Enabled = false;
               if (var2) {
                  break;
               }
            case 16:
               this._T3SEnabled = false;
               if (var2) {
                  break;
               }
            case 26:
               this._TunnelingEnabled = false;
               if (var2) {
                  break;
               }
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
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
      return "NetworkChannel";
   }

   public void putValue(String var1, Object var2) {
      int var4;
      if (var1.equals("AcceptBacklog")) {
         var4 = this._AcceptBacklog;
         this._AcceptBacklog = (Integer)var2;
         this._postSet(23, var4, this._AcceptBacklog);
      } else {
         boolean var3;
         if (var1.equals("BoundOutgoingEnabled")) {
            var3 = this._BoundOutgoingEnabled;
            this._BoundOutgoingEnabled = (Boolean)var2;
            this._postSet(21, var3, this._BoundOutgoingEnabled);
         } else if (var1.equals("COMEnabled")) {
            var3 = this._COMEnabled;
            this._COMEnabled = (Boolean)var2;
            this._postSet(19, var3, this._COMEnabled);
         } else if (var1.equals("ChannelWeight")) {
            var4 = this._ChannelWeight;
            this._ChannelWeight = (Integer)var2;
            this._postSet(22, var4, this._ChannelWeight);
         } else {
            String var5;
            if (var1.equals("ClusterAddress")) {
               var5 = this._ClusterAddress;
               this._ClusterAddress = (String)var2;
               this._postSet(14, var5, this._ClusterAddress);
            } else if (var1.equals("CompleteCOMMessageTimeout")) {
               var4 = this._CompleteCOMMessageTimeout;
               this._CompleteCOMMessageTimeout = (Integer)var2;
               this._postSet(31, var4, this._CompleteCOMMessageTimeout);
            } else if (var1.equals("CompleteHTTPMessageTimeout")) {
               var4 = this._CompleteHTTPMessageTimeout;
               this._CompleteHTTPMessageTimeout = (Integer)var2;
               this._postSet(30, var4, this._CompleteHTTPMessageTimeout);
            } else if (var1.equals("CompleteIIOPMessageTimeout")) {
               var4 = this._CompleteIIOPMessageTimeout;
               this._CompleteIIOPMessageTimeout = (Integer)var2;
               this._postSet(37, var4, this._CompleteIIOPMessageTimeout);
            } else if (var1.equals("CompleteT3MessageTimeout")) {
               var4 = this._CompleteT3MessageTimeout;
               this._CompleteT3MessageTimeout = (Integer)var2;
               this._postSet(29, var4, this._CompleteT3MessageTimeout);
            } else if (var1.equals("DefaultIIOPPassword")) {
               var5 = this._DefaultIIOPPassword;
               this._DefaultIIOPPassword = (String)var2;
               this._postSet(41, var5, this._DefaultIIOPPassword);
            } else if (var1.equals("DefaultIIOPPasswordEncrypted")) {
               byte[] var6 = this._DefaultIIOPPasswordEncrypted;
               this._DefaultIIOPPasswordEncrypted = (byte[])((byte[])var2);
               this._postSet(42, var6, this._DefaultIIOPPasswordEncrypted);
            } else if (var1.equals("DefaultIIOPUser")) {
               var5 = this._DefaultIIOPUser;
               this._DefaultIIOPUser = (String)var2;
               this._postSet(40, var5, this._DefaultIIOPUser);
            } else if (var1.equals("Description")) {
               var5 = this._Description;
               this._Description = (String)var2;
               this._postSet(9, var5, this._Description);
            } else if (var1.equals("HTTPEnabled")) {
               var3 = this._HTTPEnabled;
               this._HTTPEnabled = (Boolean)var2;
               this._postSet(17, var3, this._HTTPEnabled);
            } else if (var1.equals("HTTPSEnabled")) {
               var3 = this._HTTPSEnabled;
               this._HTTPSEnabled = (Boolean)var2;
               this._postSet(18, var3, this._HTTPSEnabled);
            } else if (var1.equals("IIOPEnabled")) {
               var3 = this._IIOPEnabled;
               this._IIOPEnabled = (Boolean)var2;
               this._postSet(35, var3, this._IIOPEnabled);
            } else if (var1.equals("IIOPSEnabled")) {
               var3 = this._IIOPSEnabled;
               this._IIOPSEnabled = (Boolean)var2;
               this._postSet(36, var3, this._IIOPSEnabled);
            } else if (var1.equals("IdleIIOPConnectionTimeout")) {
               var4 = this._IdleIIOPConnectionTimeout;
               this._IdleIIOPConnectionTimeout = (Integer)var2;
               this._postSet(39, var4, this._IdleIIOPConnectionTimeout);
            } else if (var1.equals("ListenPort")) {
               var4 = this._ListenPort;
               this._ListenPort = (Integer)var2;
               this._postSet(10, var4, this._ListenPort);
            } else if (var1.equals("ListenPortEnabled")) {
               var3 = this._ListenPortEnabled;
               this._ListenPortEnabled = (Boolean)var2;
               this._postSet(11, var3, this._ListenPortEnabled);
            } else if (var1.equals("LoginTimeoutMillis")) {
               var4 = this._LoginTimeoutMillis;
               this._LoginTimeoutMillis = (Integer)var2;
               this._postSet(24, var4, this._LoginTimeoutMillis);
            } else if (var1.equals("LoginTimeoutMillisSSL")) {
               var4 = this._LoginTimeoutMillisSSL;
               this._LoginTimeoutMillisSSL = (Integer)var2;
               this._postSet(25, var4, this._LoginTimeoutMillisSSL);
            } else if (var1.equals("MaxCOMMessageSize")) {
               var4 = this._MaxCOMMessageSize;
               this._MaxCOMMessageSize = (Integer)var2;
               this._postSet(34, var4, this._MaxCOMMessageSize);
            } else if (var1.equals("MaxHTTPMessageSize")) {
               var4 = this._MaxHTTPMessageSize;
               this._MaxHTTPMessageSize = (Integer)var2;
               this._postSet(33, var4, this._MaxHTTPMessageSize);
            } else if (var1.equals("MaxIIOPMessageSize")) {
               var4 = this._MaxIIOPMessageSize;
               this._MaxIIOPMessageSize = (Integer)var2;
               this._postSet(38, var4, this._MaxIIOPMessageSize);
            } else if (var1.equals("MaxT3MessageSize")) {
               var4 = this._MaxT3MessageSize;
               this._MaxT3MessageSize = (Integer)var2;
               this._postSet(32, var4, this._MaxT3MessageSize);
            } else if (var1.equals("Name")) {
               var5 = this._Name;
               this._Name = (String)var2;
               this._postSet(2, var5, this._Name);
            } else if (var1.equals("OutgoingEnabled")) {
               var3 = this._OutgoingEnabled;
               this._OutgoingEnabled = (Boolean)var2;
               this._postSet(20, var3, this._OutgoingEnabled);
            } else if (var1.equals("SSLListenPort")) {
               var4 = this._SSLListenPort;
               this._SSLListenPort = (Integer)var2;
               this._postSet(12, var4, this._SSLListenPort);
            } else if (var1.equals("SSLListenPortEnabled")) {
               var3 = this._SSLListenPortEnabled;
               this._SSLListenPortEnabled = (Boolean)var2;
               this._postSet(13, var3, this._SSLListenPortEnabled);
            } else if (var1.equals("T3Enabled")) {
               var3 = this._T3Enabled;
               this._T3Enabled = (Boolean)var2;
               this._postSet(15, var3, this._T3Enabled);
            } else if (var1.equals("T3SEnabled")) {
               var3 = this._T3SEnabled;
               this._T3SEnabled = (Boolean)var2;
               this._postSet(16, var3, this._T3SEnabled);
            } else if (var1.equals("TunnelingClientPingSecs")) {
               var4 = this._TunnelingClientPingSecs;
               this._TunnelingClientPingSecs = (Integer)var2;
               this._postSet(27, var4, this._TunnelingClientPingSecs);
            } else if (var1.equals("TunnelingClientTimeoutSecs")) {
               var4 = this._TunnelingClientTimeoutSecs;
               this._TunnelingClientTimeoutSecs = (Integer)var2;
               this._postSet(28, var4, this._TunnelingClientTimeoutSecs);
            } else if (var1.equals("TunnelingEnabled")) {
               var3 = this._TunnelingEnabled;
               this._TunnelingEnabled = (Boolean)var2;
               this._postSet(26, var3, this._TunnelingEnabled);
            } else {
               super.putValue(var1, var2);
            }
         }
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("AcceptBacklog")) {
         return new Integer(this._AcceptBacklog);
      } else if (var1.equals("BoundOutgoingEnabled")) {
         return new Boolean(this._BoundOutgoingEnabled);
      } else if (var1.equals("COMEnabled")) {
         return new Boolean(this._COMEnabled);
      } else if (var1.equals("ChannelWeight")) {
         return new Integer(this._ChannelWeight);
      } else if (var1.equals("ClusterAddress")) {
         return this._ClusterAddress;
      } else if (var1.equals("CompleteCOMMessageTimeout")) {
         return new Integer(this._CompleteCOMMessageTimeout);
      } else if (var1.equals("CompleteHTTPMessageTimeout")) {
         return new Integer(this._CompleteHTTPMessageTimeout);
      } else if (var1.equals("CompleteIIOPMessageTimeout")) {
         return new Integer(this._CompleteIIOPMessageTimeout);
      } else if (var1.equals("CompleteT3MessageTimeout")) {
         return new Integer(this._CompleteT3MessageTimeout);
      } else if (var1.equals("DefaultIIOPPassword")) {
         return this._DefaultIIOPPassword;
      } else if (var1.equals("DefaultIIOPPasswordEncrypted")) {
         return this._DefaultIIOPPasswordEncrypted;
      } else if (var1.equals("DefaultIIOPUser")) {
         return this._DefaultIIOPUser;
      } else if (var1.equals("Description")) {
         return this._Description;
      } else if (var1.equals("HTTPEnabled")) {
         return new Boolean(this._HTTPEnabled);
      } else if (var1.equals("HTTPSEnabled")) {
         return new Boolean(this._HTTPSEnabled);
      } else if (var1.equals("IIOPEnabled")) {
         return new Boolean(this._IIOPEnabled);
      } else if (var1.equals("IIOPSEnabled")) {
         return new Boolean(this._IIOPSEnabled);
      } else if (var1.equals("IdleIIOPConnectionTimeout")) {
         return new Integer(this._IdleIIOPConnectionTimeout);
      } else if (var1.equals("ListenPort")) {
         return new Integer(this._ListenPort);
      } else if (var1.equals("ListenPortEnabled")) {
         return new Boolean(this._ListenPortEnabled);
      } else if (var1.equals("LoginTimeoutMillis")) {
         return new Integer(this._LoginTimeoutMillis);
      } else if (var1.equals("LoginTimeoutMillisSSL")) {
         return new Integer(this._LoginTimeoutMillisSSL);
      } else if (var1.equals("MaxCOMMessageSize")) {
         return new Integer(this._MaxCOMMessageSize);
      } else if (var1.equals("MaxHTTPMessageSize")) {
         return new Integer(this._MaxHTTPMessageSize);
      } else if (var1.equals("MaxIIOPMessageSize")) {
         return new Integer(this._MaxIIOPMessageSize);
      } else if (var1.equals("MaxT3MessageSize")) {
         return new Integer(this._MaxT3MessageSize);
      } else if (var1.equals("Name")) {
         return this._Name;
      } else if (var1.equals("OutgoingEnabled")) {
         return new Boolean(this._OutgoingEnabled);
      } else if (var1.equals("SSLListenPort")) {
         return new Integer(this._SSLListenPort);
      } else if (var1.equals("SSLListenPortEnabled")) {
         return new Boolean(this._SSLListenPortEnabled);
      } else if (var1.equals("T3Enabled")) {
         return new Boolean(this._T3Enabled);
      } else if (var1.equals("T3SEnabled")) {
         return new Boolean(this._T3SEnabled);
      } else if (var1.equals("TunnelingClientPingSecs")) {
         return new Integer(this._TunnelingClientPingSecs);
      } else if (var1.equals("TunnelingClientTimeoutSecs")) {
         return new Integer(this._TunnelingClientTimeoutSecs);
      } else {
         return var1.equals("TunnelingEnabled") ? new Boolean(this._TunnelingEnabled) : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends DeploymentMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 4:
               if (var1.equals("name")) {
                  return 2;
               }
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 21:
            case 24:
            case 25:
            default:
               break;
            case 10:
               if (var1.equals("t3-enabled")) {
                  return 15;
               }
               break;
            case 11:
               if (var1.equals("description")) {
                  return 9;
               }

               if (var1.equals("listen-port")) {
                  return 10;
               }

               if (var1.equals("com-enabled")) {
                  return 19;
               }

               if (var1.equals("t3s-enabled")) {
                  return 16;
               }
               break;
            case 12:
               if (var1.equals("http-enabled")) {
                  return 17;
               }

               if (var1.equals("iiop-enabled")) {
                  return 35;
               }
               break;
            case 13:
               if (var1.equals("https-enabled")) {
                  return 18;
               }

               if (var1.equals("iiops-enabled")) {
                  return 36;
               }
               break;
            case 14:
               if (var1.equals("accept-backlog")) {
                  return 23;
               }

               if (var1.equals("channel-weight")) {
                  return 22;
               }
               break;
            case 15:
               if (var1.equals("cluster-address")) {
                  return 14;
               }

               if (var1.equals("ssl-listen-port")) {
                  return 12;
               }
               break;
            case 16:
               if (var1.equals("defaultiiop-user")) {
                  return 40;
               }

               if (var1.equals("outgoing-enabled")) {
                  return 20;
               }
               break;
            case 17:
               if (var1.equals("tunneling-enabled")) {
                  return 26;
               }
               break;
            case 18:
               if (var1.equals("maxt3-message-size")) {
                  return 32;
               }
               break;
            case 19:
               if (var1.equals("maxcom-message-size")) {
                  return 34;
               }

               if (var1.equals("listen-port-enabled")) {
                  return 11;
               }
               break;
            case 20:
               if (var1.equals("defaultiiop-password")) {
                  return 41;
               }

               if (var1.equals("login-timeout-millis")) {
                  return 24;
               }

               if (var1.equals("maxhttp-message-size")) {
                  return 33;
               }

               if (var1.equals("maxiiop-message-size")) {
                  return 38;
               }
               break;
            case 22:
               if (var1.equals("bound-outgoing-enabled")) {
                  return 21;
               }
               break;
            case 23:
               if (var1.equals("login-timeout-millisssl")) {
                  return 25;
               }

               if (var1.equals("ssl-listen-port-enabled")) {
                  return 13;
               }
               break;
            case 26:
               if (var1.equals("completet3-message-timeout")) {
                  return 29;
               }

               if (var1.equals("tunneling-client-ping-secs")) {
                  return 27;
               }
               break;
            case 27:
               if (var1.equals("completecom-message-timeout")) {
                  return 31;
               }

               if (var1.equals("idleiiop-connection-timeout")) {
                  return 39;
               }
               break;
            case 28:
               if (var1.equals("completehttp-message-timeout")) {
                  return 30;
               }

               if (var1.equals("completeiiop-message-timeout")) {
                  return 37;
               }
               break;
            case 29:
               if (var1.equals("tunneling-client-timeout-secs")) {
                  return 28;
               }
               break;
            case 30:
               if (var1.equals("defaultiiop-password-encrypted")) {
                  return 42;
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
            case 7:
            case 8:
            default:
               return super.getElementName(var1);
            case 9:
               return "description";
            case 10:
               return "listen-port";
            case 11:
               return "listen-port-enabled";
            case 12:
               return "ssl-listen-port";
            case 13:
               return "ssl-listen-port-enabled";
            case 14:
               return "cluster-address";
            case 15:
               return "t3-enabled";
            case 16:
               return "t3s-enabled";
            case 17:
               return "http-enabled";
            case 18:
               return "https-enabled";
            case 19:
               return "com-enabled";
            case 20:
               return "outgoing-enabled";
            case 21:
               return "bound-outgoing-enabled";
            case 22:
               return "channel-weight";
            case 23:
               return "accept-backlog";
            case 24:
               return "login-timeout-millis";
            case 25:
               return "login-timeout-millisssl";
            case 26:
               return "tunneling-enabled";
            case 27:
               return "tunneling-client-ping-secs";
            case 28:
               return "tunneling-client-timeout-secs";
            case 29:
               return "completet3-message-timeout";
            case 30:
               return "completehttp-message-timeout";
            case 31:
               return "completecom-message-timeout";
            case 32:
               return "maxt3-message-size";
            case 33:
               return "maxhttp-message-size";
            case 34:
               return "maxcom-message-size";
            case 35:
               return "iiop-enabled";
            case 36:
               return "iiops-enabled";
            case 37:
               return "completeiiop-message-timeout";
            case 38:
               return "maxiiop-message-size";
            case 39:
               return "idleiiop-connection-timeout";
            case 40:
               return "defaultiiop-user";
            case 41:
               return "defaultiiop-password";
            case 42:
               return "defaultiiop-password-encrypted";
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

      public boolean isConfigurable(int var1) {
         switch (var1) {
            case 25:
               return true;
            case 26:
            case 27:
            case 28:
            case 35:
            case 36:
            default:
               return super.isConfigurable(var1);
            case 29:
               return true;
            case 30:
               return true;
            case 31:
               return true;
            case 32:
               return true;
            case 33:
               return true;
            case 34:
               return true;
            case 37:
               return true;
            case 38:
               return true;
            case 39:
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

   protected static class Helper extends DeploymentMBeanImpl.Helper {
      private NetworkChannelMBeanImpl bean;

      protected Helper(NetworkChannelMBeanImpl var1) {
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
            case 7:
            case 8:
            default:
               return super.getPropertyName(var1);
            case 9:
               return "Description";
            case 10:
               return "ListenPort";
            case 11:
               return "ListenPortEnabled";
            case 12:
               return "SSLListenPort";
            case 13:
               return "SSLListenPortEnabled";
            case 14:
               return "ClusterAddress";
            case 15:
               return "T3Enabled";
            case 16:
               return "T3SEnabled";
            case 17:
               return "HTTPEnabled";
            case 18:
               return "HTTPSEnabled";
            case 19:
               return "COMEnabled";
            case 20:
               return "OutgoingEnabled";
            case 21:
               return "BoundOutgoingEnabled";
            case 22:
               return "ChannelWeight";
            case 23:
               return "AcceptBacklog";
            case 24:
               return "LoginTimeoutMillis";
            case 25:
               return "LoginTimeoutMillisSSL";
            case 26:
               return "TunnelingEnabled";
            case 27:
               return "TunnelingClientPingSecs";
            case 28:
               return "TunnelingClientTimeoutSecs";
            case 29:
               return "CompleteT3MessageTimeout";
            case 30:
               return "CompleteHTTPMessageTimeout";
            case 31:
               return "CompleteCOMMessageTimeout";
            case 32:
               return "MaxT3MessageSize";
            case 33:
               return "MaxHTTPMessageSize";
            case 34:
               return "MaxCOMMessageSize";
            case 35:
               return "IIOPEnabled";
            case 36:
               return "IIOPSEnabled";
            case 37:
               return "CompleteIIOPMessageTimeout";
            case 38:
               return "MaxIIOPMessageSize";
            case 39:
               return "IdleIIOPConnectionTimeout";
            case 40:
               return "DefaultIIOPUser";
            case 41:
               return "DefaultIIOPPassword";
            case 42:
               return "DefaultIIOPPasswordEncrypted";
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("AcceptBacklog")) {
            return 23;
         } else if (var1.equals("ChannelWeight")) {
            return 22;
         } else if (var1.equals("ClusterAddress")) {
            return 14;
         } else if (var1.equals("CompleteCOMMessageTimeout")) {
            return 31;
         } else if (var1.equals("CompleteHTTPMessageTimeout")) {
            return 30;
         } else if (var1.equals("CompleteIIOPMessageTimeout")) {
            return 37;
         } else if (var1.equals("CompleteT3MessageTimeout")) {
            return 29;
         } else if (var1.equals("DefaultIIOPPassword")) {
            return 41;
         } else if (var1.equals("DefaultIIOPPasswordEncrypted")) {
            return 42;
         } else if (var1.equals("DefaultIIOPUser")) {
            return 40;
         } else if (var1.equals("Description")) {
            return 9;
         } else if (var1.equals("IdleIIOPConnectionTimeout")) {
            return 39;
         } else if (var1.equals("ListenPort")) {
            return 10;
         } else if (var1.equals("LoginTimeoutMillis")) {
            return 24;
         } else if (var1.equals("LoginTimeoutMillisSSL")) {
            return 25;
         } else if (var1.equals("MaxCOMMessageSize")) {
            return 34;
         } else if (var1.equals("MaxHTTPMessageSize")) {
            return 33;
         } else if (var1.equals("MaxIIOPMessageSize")) {
            return 38;
         } else if (var1.equals("MaxT3MessageSize")) {
            return 32;
         } else if (var1.equals("Name")) {
            return 2;
         } else if (var1.equals("SSLListenPort")) {
            return 12;
         } else if (var1.equals("TunnelingClientPingSecs")) {
            return 27;
         } else if (var1.equals("TunnelingClientTimeoutSecs")) {
            return 28;
         } else if (var1.equals("BoundOutgoingEnabled")) {
            return 21;
         } else if (var1.equals("COMEnabled")) {
            return 19;
         } else if (var1.equals("HTTPEnabled")) {
            return 17;
         } else if (var1.equals("HTTPSEnabled")) {
            return 18;
         } else if (var1.equals("IIOPEnabled")) {
            return 35;
         } else if (var1.equals("IIOPSEnabled")) {
            return 36;
         } else if (var1.equals("ListenPortEnabled")) {
            return 11;
         } else if (var1.equals("OutgoingEnabled")) {
            return 20;
         } else if (var1.equals("SSLListenPortEnabled")) {
            return 13;
         } else if (var1.equals("T3Enabled")) {
            return 15;
         } else if (var1.equals("T3SEnabled")) {
            return 16;
         } else {
            return var1.equals("TunnelingEnabled") ? 26 : super.getPropertyIndex(var1);
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

            if (this.bean.isCompleteT3MessageTimeoutSet()) {
               var2.append("CompleteT3MessageTimeout");
               var2.append(String.valueOf(this.bean.getCompleteT3MessageTimeout()));
            }

            if (this.bean.isDefaultIIOPPasswordSet()) {
               var2.append("DefaultIIOPPassword");
               var2.append(String.valueOf(this.bean.getDefaultIIOPPassword()));
            }

            if (this.bean.isDefaultIIOPPasswordEncryptedSet()) {
               var2.append("DefaultIIOPPasswordEncrypted");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getDefaultIIOPPasswordEncrypted())));
            }

            if (this.bean.isDefaultIIOPUserSet()) {
               var2.append("DefaultIIOPUser");
               var2.append(String.valueOf(this.bean.getDefaultIIOPUser()));
            }

            if (this.bean.isDescriptionSet()) {
               var2.append("Description");
               var2.append(String.valueOf(this.bean.getDescription()));
            }

            if (this.bean.isIdleIIOPConnectionTimeoutSet()) {
               var2.append("IdleIIOPConnectionTimeout");
               var2.append(String.valueOf(this.bean.getIdleIIOPConnectionTimeout()));
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

            if (this.bean.isMaxCOMMessageSizeSet()) {
               var2.append("MaxCOMMessageSize");
               var2.append(String.valueOf(this.bean.getMaxCOMMessageSize()));
            }

            if (this.bean.isMaxHTTPMessageSizeSet()) {
               var2.append("MaxHTTPMessageSize");
               var2.append(String.valueOf(this.bean.getMaxHTTPMessageSize()));
            }

            if (this.bean.isMaxIIOPMessageSizeSet()) {
               var2.append("MaxIIOPMessageSize");
               var2.append(String.valueOf(this.bean.getMaxIIOPMessageSize()));
            }

            if (this.bean.isMaxT3MessageSizeSet()) {
               var2.append("MaxT3MessageSize");
               var2.append(String.valueOf(this.bean.getMaxT3MessageSize()));
            }

            if (this.bean.isNameSet()) {
               var2.append("Name");
               var2.append(String.valueOf(this.bean.getName()));
            }

            if (this.bean.isSSLListenPortSet()) {
               var2.append("SSLListenPort");
               var2.append(String.valueOf(this.bean.getSSLListenPort()));
            }

            if (this.bean.isTunnelingClientPingSecsSet()) {
               var2.append("TunnelingClientPingSecs");
               var2.append(String.valueOf(this.bean.getTunnelingClientPingSecs()));
            }

            if (this.bean.isTunnelingClientTimeoutSecsSet()) {
               var2.append("TunnelingClientTimeoutSecs");
               var2.append(String.valueOf(this.bean.getTunnelingClientTimeoutSecs()));
            }

            if (this.bean.isBoundOutgoingEnabledSet()) {
               var2.append("BoundOutgoingEnabled");
               var2.append(String.valueOf(this.bean.isBoundOutgoingEnabled()));
            }

            if (this.bean.isCOMEnabledSet()) {
               var2.append("COMEnabled");
               var2.append(String.valueOf(this.bean.isCOMEnabled()));
            }

            if (this.bean.isHTTPEnabledSet()) {
               var2.append("HTTPEnabled");
               var2.append(String.valueOf(this.bean.isHTTPEnabled()));
            }

            if (this.bean.isHTTPSEnabledSet()) {
               var2.append("HTTPSEnabled");
               var2.append(String.valueOf(this.bean.isHTTPSEnabled()));
            }

            if (this.bean.isIIOPEnabledSet()) {
               var2.append("IIOPEnabled");
               var2.append(String.valueOf(this.bean.isIIOPEnabled()));
            }

            if (this.bean.isIIOPSEnabledSet()) {
               var2.append("IIOPSEnabled");
               var2.append(String.valueOf(this.bean.isIIOPSEnabled()));
            }

            if (this.bean.isListenPortEnabledSet()) {
               var2.append("ListenPortEnabled");
               var2.append(String.valueOf(this.bean.isListenPortEnabled()));
            }

            if (this.bean.isOutgoingEnabledSet()) {
               var2.append("OutgoingEnabled");
               var2.append(String.valueOf(this.bean.isOutgoingEnabled()));
            }

            if (this.bean.isSSLListenPortEnabledSet()) {
               var2.append("SSLListenPortEnabled");
               var2.append(String.valueOf(this.bean.isSSLListenPortEnabled()));
            }

            if (this.bean.isT3EnabledSet()) {
               var2.append("T3Enabled");
               var2.append(String.valueOf(this.bean.isT3Enabled()));
            }

            if (this.bean.isT3SEnabledSet()) {
               var2.append("T3SEnabled");
               var2.append(String.valueOf(this.bean.isT3SEnabled()));
            }

            if (this.bean.isTunnelingEnabledSet()) {
               var2.append("TunnelingEnabled");
               var2.append(String.valueOf(this.bean.isTunnelingEnabled()));
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
            NetworkChannelMBeanImpl var2 = (NetworkChannelMBeanImpl)var1;
            this.computeDiff("AcceptBacklog", this.bean.getAcceptBacklog(), var2.getAcceptBacklog(), false);
            this.computeDiff("ChannelWeight", this.bean.getChannelWeight(), var2.getChannelWeight(), false);
            this.computeDiff("ClusterAddress", this.bean.getClusterAddress(), var2.getClusterAddress(), false);
            this.computeDiff("CompleteCOMMessageTimeout", this.bean.getCompleteCOMMessageTimeout(), var2.getCompleteCOMMessageTimeout(), true);
            this.computeDiff("CompleteHTTPMessageTimeout", this.bean.getCompleteHTTPMessageTimeout(), var2.getCompleteHTTPMessageTimeout(), true);
            this.computeDiff("CompleteIIOPMessageTimeout", this.bean.getCompleteIIOPMessageTimeout(), var2.getCompleteIIOPMessageTimeout(), true);
            this.computeDiff("CompleteT3MessageTimeout", this.bean.getCompleteT3MessageTimeout(), var2.getCompleteT3MessageTimeout(), true);
            this.computeDiff("DefaultIIOPPasswordEncrypted", this.bean.getDefaultIIOPPasswordEncrypted(), var2.getDefaultIIOPPasswordEncrypted(), false);
            this.computeDiff("DefaultIIOPUser", this.bean.getDefaultIIOPUser(), var2.getDefaultIIOPUser(), false);
            this.computeDiff("Description", this.bean.getDescription(), var2.getDescription(), false);
            this.computeDiff("IdleIIOPConnectionTimeout", this.bean.getIdleIIOPConnectionTimeout(), var2.getIdleIIOPConnectionTimeout(), true);
            this.computeDiff("ListenPort", this.bean.getListenPort(), var2.getListenPort(), false);
            this.computeDiff("LoginTimeoutMillis", this.bean.getLoginTimeoutMillis(), var2.getLoginTimeoutMillis(), true);
            this.computeDiff("LoginTimeoutMillisSSL", this.bean.getLoginTimeoutMillisSSL(), var2.getLoginTimeoutMillisSSL(), true);
            this.computeDiff("MaxCOMMessageSize", this.bean.getMaxCOMMessageSize(), var2.getMaxCOMMessageSize(), true);
            this.computeDiff("MaxHTTPMessageSize", this.bean.getMaxHTTPMessageSize(), var2.getMaxHTTPMessageSize(), true);
            this.computeDiff("MaxIIOPMessageSize", this.bean.getMaxIIOPMessageSize(), var2.getMaxIIOPMessageSize(), true);
            this.computeDiff("MaxT3MessageSize", this.bean.getMaxT3MessageSize(), var2.getMaxT3MessageSize(), true);
            this.computeDiff("Name", this.bean.getName(), var2.getName(), false);
            this.computeDiff("SSLListenPort", this.bean.getSSLListenPort(), var2.getSSLListenPort(), false);
            this.computeDiff("TunnelingClientPingSecs", this.bean.getTunnelingClientPingSecs(), var2.getTunnelingClientPingSecs(), false);
            this.computeDiff("TunnelingClientTimeoutSecs", this.bean.getTunnelingClientTimeoutSecs(), var2.getTunnelingClientTimeoutSecs(), false);
            this.computeDiff("BoundOutgoingEnabled", this.bean.isBoundOutgoingEnabled(), var2.isBoundOutgoingEnabled(), false);
            this.computeDiff("COMEnabled", this.bean.isCOMEnabled(), var2.isCOMEnabled(), false);
            this.computeDiff("HTTPEnabled", this.bean.isHTTPEnabled(), var2.isHTTPEnabled(), false);
            this.computeDiff("HTTPSEnabled", this.bean.isHTTPSEnabled(), var2.isHTTPSEnabled(), false);
            this.computeDiff("IIOPEnabled", this.bean.isIIOPEnabled(), var2.isIIOPEnabled(), false);
            this.computeDiff("IIOPSEnabled", this.bean.isIIOPSEnabled(), var2.isIIOPSEnabled(), false);
            this.computeDiff("ListenPortEnabled", this.bean.isListenPortEnabled(), var2.isListenPortEnabled(), false);
            this.computeDiff("OutgoingEnabled", this.bean.isOutgoingEnabled(), var2.isOutgoingEnabled(), false);
            this.computeDiff("SSLListenPortEnabled", this.bean.isSSLListenPortEnabled(), var2.isSSLListenPortEnabled(), false);
            this.computeDiff("T3Enabled", this.bean.isT3Enabled(), var2.isT3Enabled(), false);
            this.computeDiff("T3SEnabled", this.bean.isT3SEnabled(), var2.isT3SEnabled(), false);
            this.computeDiff("TunnelingEnabled", this.bean.isTunnelingEnabled(), var2.isTunnelingEnabled(), true);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            NetworkChannelMBeanImpl var3 = (NetworkChannelMBeanImpl)var1.getSourceBean();
            NetworkChannelMBeanImpl var4 = (NetworkChannelMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("AcceptBacklog")) {
                  var3.setAcceptBacklog(var4.getAcceptBacklog());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 23);
               } else if (var5.equals("ChannelWeight")) {
                  var3.setChannelWeight(var4.getChannelWeight());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 22);
               } else if (var5.equals("ClusterAddress")) {
                  var3.setClusterAddress(var4.getClusterAddress());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 14);
               } else if (var5.equals("CompleteCOMMessageTimeout")) {
                  var3.setCompleteCOMMessageTimeout(var4.getCompleteCOMMessageTimeout());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 31);
               } else if (var5.equals("CompleteHTTPMessageTimeout")) {
                  var3.setCompleteHTTPMessageTimeout(var4.getCompleteHTTPMessageTimeout());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 30);
               } else if (var5.equals("CompleteIIOPMessageTimeout")) {
                  var3.setCompleteIIOPMessageTimeout(var4.getCompleteIIOPMessageTimeout());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 37);
               } else if (var5.equals("CompleteT3MessageTimeout")) {
                  var3.setCompleteT3MessageTimeout(var4.getCompleteT3MessageTimeout());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 29);
               } else if (!var5.equals("DefaultIIOPPassword")) {
                  if (var5.equals("DefaultIIOPPasswordEncrypted")) {
                     var3.setDefaultIIOPPasswordEncrypted(var4.getDefaultIIOPPasswordEncrypted());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 42);
                  } else if (var5.equals("DefaultIIOPUser")) {
                     var3.setDefaultIIOPUser(var4.getDefaultIIOPUser());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 40);
                  } else if (var5.equals("Description")) {
                     var3.setDescription(var4.getDescription());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 9);
                  } else if (var5.equals("IdleIIOPConnectionTimeout")) {
                     var3.setIdleIIOPConnectionTimeout(var4.getIdleIIOPConnectionTimeout());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 39);
                  } else if (var5.equals("ListenPort")) {
                     var3.setListenPort(var4.getListenPort());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 10);
                  } else if (var5.equals("LoginTimeoutMillis")) {
                     var3.setLoginTimeoutMillis(var4.getLoginTimeoutMillis());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 24);
                  } else if (var5.equals("LoginTimeoutMillisSSL")) {
                     var3.setLoginTimeoutMillisSSL(var4.getLoginTimeoutMillisSSL());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 25);
                  } else if (var5.equals("MaxCOMMessageSize")) {
                     var3.setMaxCOMMessageSize(var4.getMaxCOMMessageSize());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 34);
                  } else if (var5.equals("MaxHTTPMessageSize")) {
                     var3.setMaxHTTPMessageSize(var4.getMaxHTTPMessageSize());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 33);
                  } else if (var5.equals("MaxIIOPMessageSize")) {
                     var3.setMaxIIOPMessageSize(var4.getMaxIIOPMessageSize());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 38);
                  } else if (var5.equals("MaxT3MessageSize")) {
                     var3.setMaxT3MessageSize(var4.getMaxT3MessageSize());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 32);
                  } else if (var5.equals("Name")) {
                     var3.setName(var4.getName());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 2);
                  } else if (var5.equals("SSLListenPort")) {
                     var3.setSSLListenPort(var4.getSSLListenPort());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 12);
                  } else if (var5.equals("TunnelingClientPingSecs")) {
                     var3.setTunnelingClientPingSecs(var4.getTunnelingClientPingSecs());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 27);
                  } else if (var5.equals("TunnelingClientTimeoutSecs")) {
                     var3.setTunnelingClientTimeoutSecs(var4.getTunnelingClientTimeoutSecs());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 28);
                  } else if (var5.equals("BoundOutgoingEnabled")) {
                     var3._conditionalUnset(var2.isUnsetUpdate(), 21);
                  } else if (var5.equals("COMEnabled")) {
                     var3.setCOMEnabled(var4.isCOMEnabled());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 19);
                  } else if (var5.equals("HTTPEnabled")) {
                     var3.setHTTPEnabled(var4.isHTTPEnabled());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 17);
                  } else if (var5.equals("HTTPSEnabled")) {
                     var3.setHTTPSEnabled(var4.isHTTPSEnabled());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 18);
                  } else if (var5.equals("IIOPEnabled")) {
                     var3.setIIOPEnabled(var4.isIIOPEnabled());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 35);
                  } else if (var5.equals("IIOPSEnabled")) {
                     var3.setIIOPSEnabled(var4.isIIOPSEnabled());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 36);
                  } else if (var5.equals("ListenPortEnabled")) {
                     var3.setListenPortEnabled(var4.isListenPortEnabled());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 11);
                  } else if (var5.equals("OutgoingEnabled")) {
                     var3.setOutgoingEnabled(var4.isOutgoingEnabled());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 20);
                  } else if (var5.equals("SSLListenPortEnabled")) {
                     var3.setSSLListenPortEnabled(var4.isSSLListenPortEnabled());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 13);
                  } else if (var5.equals("T3Enabled")) {
                     var3.setT3Enabled(var4.isT3Enabled());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 15);
                  } else if (var5.equals("T3SEnabled")) {
                     var3.setT3SEnabled(var4.isT3SEnabled());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 16);
                  } else if (var5.equals("TunnelingEnabled")) {
                     var3.setTunnelingEnabled(var4.isTunnelingEnabled());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 26);
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
            NetworkChannelMBeanImpl var5 = (NetworkChannelMBeanImpl)var1;
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

            if ((var3 == null || !var3.contains("CompleteT3MessageTimeout")) && this.bean.isCompleteT3MessageTimeoutSet()) {
               var5.setCompleteT3MessageTimeout(this.bean.getCompleteT3MessageTimeout());
            }

            if ((var3 == null || !var3.contains("DefaultIIOPPasswordEncrypted")) && this.bean.isDefaultIIOPPasswordEncryptedSet()) {
               byte[] var4 = this.bean.getDefaultIIOPPasswordEncrypted();
               var5.setDefaultIIOPPasswordEncrypted(var4 == null ? null : (byte[])((byte[])((byte[])((byte[])var4)).clone()));
            }

            if ((var3 == null || !var3.contains("DefaultIIOPUser")) && this.bean.isDefaultIIOPUserSet()) {
               var5.setDefaultIIOPUser(this.bean.getDefaultIIOPUser());
            }

            if ((var3 == null || !var3.contains("Description")) && this.bean.isDescriptionSet()) {
               var5.setDescription(this.bean.getDescription());
            }

            if ((var3 == null || !var3.contains("IdleIIOPConnectionTimeout")) && this.bean.isIdleIIOPConnectionTimeoutSet()) {
               var5.setIdleIIOPConnectionTimeout(this.bean.getIdleIIOPConnectionTimeout());
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

            if ((var3 == null || !var3.contains("MaxCOMMessageSize")) && this.bean.isMaxCOMMessageSizeSet()) {
               var5.setMaxCOMMessageSize(this.bean.getMaxCOMMessageSize());
            }

            if ((var3 == null || !var3.contains("MaxHTTPMessageSize")) && this.bean.isMaxHTTPMessageSizeSet()) {
               var5.setMaxHTTPMessageSize(this.bean.getMaxHTTPMessageSize());
            }

            if ((var3 == null || !var3.contains("MaxIIOPMessageSize")) && this.bean.isMaxIIOPMessageSizeSet()) {
               var5.setMaxIIOPMessageSize(this.bean.getMaxIIOPMessageSize());
            }

            if ((var3 == null || !var3.contains("MaxT3MessageSize")) && this.bean.isMaxT3MessageSizeSet()) {
               var5.setMaxT3MessageSize(this.bean.getMaxT3MessageSize());
            }

            if ((var3 == null || !var3.contains("Name")) && this.bean.isNameSet()) {
               var5.setName(this.bean.getName());
            }

            if ((var3 == null || !var3.contains("SSLListenPort")) && this.bean.isSSLListenPortSet()) {
               var5.setSSLListenPort(this.bean.getSSLListenPort());
            }

            if ((var3 == null || !var3.contains("TunnelingClientPingSecs")) && this.bean.isTunnelingClientPingSecsSet()) {
               var5.setTunnelingClientPingSecs(this.bean.getTunnelingClientPingSecs());
            }

            if ((var3 == null || !var3.contains("TunnelingClientTimeoutSecs")) && this.bean.isTunnelingClientTimeoutSecsSet()) {
               var5.setTunnelingClientTimeoutSecs(this.bean.getTunnelingClientTimeoutSecs());
            }

            if ((var3 == null || !var3.contains("BoundOutgoingEnabled")) && this.bean.isBoundOutgoingEnabledSet()) {
            }

            if ((var3 == null || !var3.contains("COMEnabled")) && this.bean.isCOMEnabledSet()) {
               var5.setCOMEnabled(this.bean.isCOMEnabled());
            }

            if ((var3 == null || !var3.contains("HTTPEnabled")) && this.bean.isHTTPEnabledSet()) {
               var5.setHTTPEnabled(this.bean.isHTTPEnabled());
            }

            if ((var3 == null || !var3.contains("HTTPSEnabled")) && this.bean.isHTTPSEnabledSet()) {
               var5.setHTTPSEnabled(this.bean.isHTTPSEnabled());
            }

            if ((var3 == null || !var3.contains("IIOPEnabled")) && this.bean.isIIOPEnabledSet()) {
               var5.setIIOPEnabled(this.bean.isIIOPEnabled());
            }

            if ((var3 == null || !var3.contains("IIOPSEnabled")) && this.bean.isIIOPSEnabledSet()) {
               var5.setIIOPSEnabled(this.bean.isIIOPSEnabled());
            }

            if ((var3 == null || !var3.contains("ListenPortEnabled")) && this.bean.isListenPortEnabledSet()) {
               var5.setListenPortEnabled(this.bean.isListenPortEnabled());
            }

            if ((var3 == null || !var3.contains("OutgoingEnabled")) && this.bean.isOutgoingEnabledSet()) {
               var5.setOutgoingEnabled(this.bean.isOutgoingEnabled());
            }

            if ((var3 == null || !var3.contains("SSLListenPortEnabled")) && this.bean.isSSLListenPortEnabledSet()) {
               var5.setSSLListenPortEnabled(this.bean.isSSLListenPortEnabled());
            }

            if ((var3 == null || !var3.contains("T3Enabled")) && this.bean.isT3EnabledSet()) {
               var5.setT3Enabled(this.bean.isT3Enabled());
            }

            if ((var3 == null || !var3.contains("T3SEnabled")) && this.bean.isT3SEnabledSet()) {
               var5.setT3SEnabled(this.bean.isT3SEnabled());
            }

            if ((var3 == null || !var3.contains("TunnelingEnabled")) && this.bean.isTunnelingEnabledSet()) {
               var5.setTunnelingEnabled(this.bean.isTunnelingEnabled());
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
