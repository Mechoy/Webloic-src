package weblogic.kernel;

import java.util.Properties;
import javax.management.InvalidAttributeValueException;
import weblogic.management.DistributedManagementException;
import weblogic.management.configuration.KernelMBean;
import weblogic.management.configuration.NetworkAccessPointMBean;

public class NetworkAccessPointMBeanStub extends MBeanStub implements NetworkAccessPointMBean {
   private String protocol;
   private String address;
   private String name;
   private boolean timeoutConnectionWithPendingResponses;
   private boolean sdpEnabled;
   private boolean t3SenderQueueEnabled;
   protected KernelMBean config;
   private int port;
   private boolean httpEnabledForThisProtocol;
   private boolean outboundEnabled;
   private boolean fastSerialization;

   NetworkAccessPointMBeanStub() {
      this.timeoutConnectionWithPendingResponses = false;
      this.port = -1;
      this.httpEnabledForThisProtocol = false;
      this.outboundEnabled = false;
      this.fastSerialization = false;
      this.initializeFromSystemProperties("weblogic.channels.");
   }

   public NetworkAccessPointMBeanStub(String var1) {
      this(var1, getKernelConfig());
   }

   private static KernelMBean getKernelConfig() {
      Kernel.ensureInitialized();
      return Kernel.getConfig();
   }

   public NetworkAccessPointMBeanStub(String var1, KernelMBean var2) {
      this.timeoutConnectionWithPendingResponses = false;
      this.port = -1;
      this.httpEnabledForThisProtocol = false;
      this.outboundEnabled = false;
      this.fastSerialization = false;
      this.initializeFromSystemProperties("weblogic." + var1 + ".");
      this.protocol = var1;
      this.config = var2;
      this.name = "Default";
   }

   public static NetworkAccessPointMBean createBootstrapStub() {
      NetworkAccessPointMBeanStub var0 = new NetworkAccessPointMBeanStub("ADMIN".toLowerCase(), new KernelMBeanStub());
      var0.httpEnabledForThisProtocol = true;
      var0.outboundEnabled = true;
      var0.name = " Bootstrap";
      return var0;
   }

   public String getName() {
      return this.name;
   }

   public String getProtocol() {
      return this.protocol;
   }

   public void setProtocol(String var1) throws InvalidAttributeValueException {
      this.protocol = var1;
   }

   public String getListenAddress() {
      return this.address;
   }

   public void setListenAddress(String var1) throws InvalidAttributeValueException {
      this.address = var1;
   }

   public String getPublicAddress() {
      return this.getListenAddress();
   }

   public void setPublicAddress(String var1) throws InvalidAttributeValueException {
   }

   public int getListenPort() {
      return this.port;
   }

   public void setListenPort(int var1) {
      this.port = var1;
   }

   public int getPublicPort() {
      return this.getListenPort();
   }

   public void setPublicPort(int var1) {
   }

   public String getProxyAddress() {
      return null;
   }

   public void setProxyAddress(String var1) {
   }

   public int getProxyPort() {
      return -1;
   }

   public void setProxyPort(int var1) {
   }

   public boolean isHttpEnabledForThisProtocol() {
      return this.httpEnabledForThisProtocol;
   }

   public void setHttpEnabledForThisProtocol(boolean var1) throws InvalidAttributeValueException {
   }

   public int getAcceptBacklog() {
      return 50;
   }

   public void setAcceptBacklog(int var1) {
   }

   public int getMaxBackoffBetweenFailures() {
      return 10000;
   }

   public void setMaxBackoffBetweenFailures(int var1) {
   }

   public int getLoginTimeoutMillis() {
      return 5000;
   }

   public void setLoginTimeoutMillis(int var1) {
   }

   public int getConnectTimeout() {
      return this.config.getConnectTimeout();
   }

   public void setConnectTimeout(int var1) throws InvalidAttributeValueException, DistributedManagementException {
      this.config.setConnectTimeout(var1);
   }

   public int getTunnelingClientPingSecs() {
      return 0;
   }

   public void setTunnelingClientPingSecs(int var1) throws InvalidAttributeValueException {
   }

   public int getTunnelingClientTimeoutSecs() {
      return 0;
   }

   public void setTunnelingClientTimeoutSecs(int var1) throws InvalidAttributeValueException {
   }

   public boolean isTunnelingEnabled() {
      return false;
   }

   public void setTunnelingEnabled(boolean var1) throws InvalidAttributeValueException {
   }

   public int getCompleteMessageTimeout() {
      return this.config.getCompleteMessageTimeout();
   }

   public void setCompleteMessageTimeout(int var1) throws InvalidAttributeValueException, DistributedManagementException {
      this.config.setCompleteMessageTimeout(var1);
   }

   public int getIdleConnectionTimeout() {
      return this.config.getIdleConnectionTimeout();
   }

   public void setIdleConnectionTimeout(int var1) throws InvalidAttributeValueException, DistributedManagementException {
      this.config.setIdleConnectionTimeout(var1);
   }

   public int getMaxMessageSize() {
      int var1 = this.config.getMaxMessageSize();
      if (var1 < 0) {
         var1 = Integer.MAX_VALUE;
      }

      return var1;
   }

   public void setMaxMessageSize(int var1) throws InvalidAttributeValueException, DistributedManagementException {
      this.config.setMaxMessageSize(var1);
   }

   public boolean isOutboundEnabled() {
      return this.outboundEnabled;
   }

   public void setOutboundEnabled(boolean var1) throws InvalidAttributeValueException {
   }

   public int getChannelWeight() {
      return 0;
   }

   public void setChannelWeight(int var1) throws InvalidAttributeValueException {
   }

   public String getClusterAddress() {
      return null;
   }

   public void setClusterAddress(String var1) throws InvalidAttributeValueException {
   }

   public boolean isEnabled() {
      return true;
   }

   public void setEnabled(boolean var1) throws InvalidAttributeValueException {
   }

   public int getMaxConnectedClients() {
      return Integer.MAX_VALUE;
   }

   public void setMaxConnectedClients(int var1) throws InvalidAttributeValueException {
   }

   public boolean isTwoWaySSLEnabled() {
      return false;
   }

   public void setTwoWaySSLEnabled(boolean var1) {
   }

   public boolean isChannelIdentityCustomized() {
      return false;
   }

   public void setChannelIdentityCustomized(boolean var1) {
   }

   public String getCustomPrivateKeyAlias() {
      return null;
   }

   public void setCustomPrivateKeyAlias(String var1) {
   }

   public String getPrivateKeyAlias() {
      return this.getCustomPrivateKeyAlias();
   }

   public boolean isOutboundPrivateKeyEnabled() {
      return false;
   }

   public void setOutboundPrivateKeyEnabled(boolean var1) {
   }

   public boolean getUseFastSerialization() {
      return "iiop".equals(this.protocol) ? Kernel.getConfig().getIIOP().getUseJavaSerialization() : this.fastSerialization;
   }

   public void setUseFastSerialization(boolean var1) throws InvalidAttributeValueException {
      this.fastSerialization = var1;
   }

   public String getCustomPrivateKeyPassPhrase() {
      return null;
   }

   public void setCustomPrivateKeyPassPhrase(String var1) {
   }

   public String getPrivateKeyPassPhrase() {
      return this.getCustomPrivateKeyPassPhrase();
   }

   public byte[] getCustomPrivateKeyPassPhraseEncrypted() {
      return null;
   }

   public void setCustomPrivateKeyPassPhraseEncrypted(byte[] var1) {
   }

   public boolean isClientCertificateEnforced() {
      return false;
   }

   public void setClientCertificateEnforced(boolean var1) {
   }

   public boolean getTimeoutConnectionWithPendingResponses() {
      return this.timeoutConnectionWithPendingResponses;
   }

   public void setTimeoutConnectionWithPendingResponses(boolean var1) throws InvalidAttributeValueException, DistributedManagementException {
      this.timeoutConnectionWithPendingResponses = var1;
   }

   public int getIdleIIOPConnectionTimeout() {
      return 0;
   }

   public void setIdleIIOPConnectionTimeout(int var1) throws InvalidAttributeValueException, DistributedManagementException {
   }

   public int getSSLListenPort() {
      return -1;
   }

   public void setSSLListenPort(int var1) throws InvalidAttributeValueException {
   }

   public String getExternalDNSName() {
      return null;
   }

   public void setExternalDNSName(String var1) throws InvalidAttributeValueException {
   }

   public int getLoginTimeoutMillisSSL() {
      return 0;
   }

   public void setLoginTimeoutMillisSSL(int var1) throws InvalidAttributeValueException {
   }

   public int getCompleteT3MessageTimeout() {
      return 0;
   }

   public void setCompleteT3MessageTimeout(int var1) throws InvalidAttributeValueException, DistributedManagementException {
   }

   public int getCompleteHTTPMessageTimeout() {
      return 0;
   }

   public void setCompleteHTTPMessageTimeout(int var1) throws InvalidAttributeValueException, DistributedManagementException {
   }

   public int getCompleteCOMMessageTimeout() {
      return 0;
   }

   public void setCompleteCOMMessageTimeout(int var1) throws InvalidAttributeValueException, DistributedManagementException {
   }

   public int getCompleteIIOPMessageTimeout() {
      return 0;
   }

   public void setCompleteIIOPMessageTimeout(int var1) throws InvalidAttributeValueException, DistributedManagementException {
   }

   public void setCustomProperties(Properties var1) {
   }

   public Properties getCustomProperties() {
      return null;
   }

   public boolean isSDPEnabled() {
      return this.sdpEnabled;
   }

   public void setSDPEnabled(boolean var1) {
      this.sdpEnabled = var1;
   }

   public String getOutboundPrivateKeyAlias() {
      return null;
   }

   public String getOutboundPrivateKeyPassPhrase() {
      return null;
   }
}
