package weblogic.protocol.configuration;

import java.security.AccessController;
import javax.management.InvalidAttributeValueException;
import weblogic.kernel.NetworkAccessPointMBeanStub;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.provider.ManagementService;
import weblogic.protocol.Protocol;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public class NetworkAccessPointDefaultMBean extends NetworkAccessPointMBeanStub {
   private final Protocol p;
   private final String name;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public NetworkAccessPointDefaultMBean(Protocol var1, ServerMBean var2) {
      super(var1.getProtocolName(), var2);
      this.p = var1;
      this.name = this.p.getQOS() == 103 ? "DefaultAdministration" : (this.p.isSecure() ? "DefaultSecure" : "Default");
   }

   private final ServerMBean getConfig() {
      return (ServerMBean)this.config;
   }

   public String getName() {
      return this.name;
   }

   public String getListenAddress() {
      return this.getConfig().getListenAddress();
   }

   public String getPublicAddress() {
      String var1 = this.getConfig().getExternalDNSName();
      return var1 == null ? this.getListenAddress() : var1;
   }

   public int getListenPort() {
      if (this.p.getQOS() == 103) {
         return this.getConfig().getAdministrationPort();
      } else {
         return this.p.isSecure() ? this.getConfig().getSSL().getListenPort() : this.getConfig().getListenPort();
      }
   }

   public int getPublicPort() {
      return this.getListenPort();
   }

   public boolean isHttpEnabledForThisProtocol() {
      return this.getConfig().isHttpdEnabled();
   }

   public int getAcceptBacklog() {
      return this.getConfig().getAcceptBacklog();
   }

   public int getMaxBackoffBetweenFailures() {
      return this.getConfig().getMaxBackoffBetweenFailures();
   }

   public int getLoginTimeoutMillis() {
      return (this.p.getQOS() == 103 || this.p.isSecure()) && this.getConfig().getSSL() != null ? this.getConfig().getSSL().getLoginTimeoutMillis() : this.getConfig().getLoginTimeoutMillis();
   }

   public int getTunnelingClientPingSecs() {
      return this.getConfig().getTunnelingClientPingSecs();
   }

   public int getTunnelingClientTimeoutSecs() {
      return this.getConfig().getTunnelingClientTimeoutSecs();
   }

   public boolean isTunnelingEnabled() {
      return this.getConfig().isTunnelingEnabled();
   }

   public boolean isOutboundEnabled() {
      return this.p.getQOS() == 103 ? true : this.getConfig().isOutboundEnabled();
   }

   public boolean isOutboundPrivateKeyEnabled() {
      return this.getConfig().isOutboundPrivateKeyEnabled();
   }

   public int getChannelWeight() {
      return 50;
   }

   public String getClusterAddress() {
      String var1 = this.getConfig().getCluster() != null ? this.getConfig().getCluster().getClusterAddress() : null;
      return var1 == null ? this.getPublicAddress() : var1;
   }

   public boolean isEnabled() {
      if (this.p.getQOS() == 103) {
         return ManagementService.getRuntimeAccess(kernelId).getDomain().isAdministrationPortEnabled();
      } else if (this.p.isSecure()) {
         if (this.getConfig().getSSL() == null) {
            return false;
         } else {
            return this.getConfig().getSSL().isEnabled() && this.getConfig().getSSL().isListenPortEnabled();
         }
      } else {
         return this.getConfig().isListenPortEnabled();
      }
   }

   public void setEnabled(boolean var1) throws InvalidAttributeValueException {
   }

   public int getMaxConnectedClients() {
      return Integer.MAX_VALUE;
   }

   public void setMaxConnectedClients(int var1) throws InvalidAttributeValueException {
   }

   public boolean isTwoWaySSLEnabled() {
      return this.p.isSecure() && this.getConfig().getSSL() != null ? this.getConfig().getSSL().isTwoWaySSLEnabled() : false;
   }

   public void setTwoWaySSLEnabled(boolean var1) {
   }

   public boolean isChannelIdentityCustomized() {
      return false;
   }

   public void setChannelIdentityCustomized(boolean var1) {
   }

   public String getCustomPrivateKeyAlias() {
      return this.p.isSecure() && this.getConfig().getSSL() != null ? this.getConfig().getSSL().getServerPrivateKeyAlias() : null;
   }

   public void setCustomPrivateKeyAlias(String var1) {
   }

   public String getCustomPrivateKeyPassPhrase() {
      return this.p.isSecure() && this.getConfig().getSSL() != null ? this.getConfig().getSSL().getServerPrivateKeyPassPhrase() : null;
   }

   public void setCustomPrivateKeyPassPhrase(String var1) {
   }

   public boolean isClientCertificateEnforced() {
      return this.p.isSecure() && this.getConfig().getSSL() != null ? this.getConfig().getSSL().isClientCertificateEnforced() : false;
   }

   public void setClientCertificateEnforced(boolean var1) {
   }

   public String getExternalDNSName() {
      return this.getPublicAddress();
   }

   public String getOutboundPrivateKeyAlias() {
      return this.p.isSecure() && this.getConfig().getSSL() != null ? this.getConfig().getSSL().getOutboundPrivateKeyAlias() : null;
   }

   public String getOutboundPrivateKeyPassPhrase() {
      return this.p.isSecure() && this.getConfig().getSSL() != null ? this.getConfig().getSSL().getOutboundPrivateKeyPassPhrase() : null;
   }
}
