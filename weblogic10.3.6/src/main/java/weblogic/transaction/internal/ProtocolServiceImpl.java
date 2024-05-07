package weblogic.transaction.internal;

import java.rmi.UnknownHostException;
import weblogic.protocol.Protocol;
import weblogic.protocol.ProtocolManager;
import weblogic.protocol.ServerChannelManager;
import weblogic.protocol.ServerIdentity;
import weblogic.protocol.URLManager;
import weblogic.protocol.configuration.ChannelHelper;
import weblogic.rmi.spi.Channel;

public class ProtocolServiceImpl implements ProtocolService {
   private ProtocolService protocolService = null;

   public ProtocolService getProtocolService() {
      if (this.protocolService == null) {
         this.protocolService = new ProtocolServiceImpl();
      }

      return this.protocolService;
   }

   public void setProtocolService(ProtocolService var1) {
      this.protocolService = var1;
   }

   public Protocol getDefaultSecureProtocol() {
      return ProtocolManager.getDefaultSecureProtocol();
   }

   public Protocol getDefaultProtocol() {
      return ProtocolManager.getDefaultProtocol();
   }

   public String findURL(String var1, Protocol var2) throws UnknownHostException {
      return URLManager.findURL(var1, var2);
   }

   public String findAdministrationURL(String var1) throws UnknownHostException {
      return URLManager.findAdministrationURL(var1);
   }

   public Channel findServerChannel(ServerIdentity var1, Protocol var2) {
      return ServerChannelManager.findServerChannel(var1, var2);
   }

   public boolean isLocalAdminChannelEnabled() {
      return ChannelHelper.isLocalAdminChannelEnabled();
   }
}
