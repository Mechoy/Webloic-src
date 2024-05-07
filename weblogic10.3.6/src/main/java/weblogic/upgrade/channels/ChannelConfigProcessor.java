package weblogic.upgrade.channels;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.NetworkAccessPointMBean;
import weblogic.management.configuration.NetworkChannelMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.configuration.TargetMBean;
import weblogic.management.provider.ConfigurationProcessor;
import weblogic.management.provider.UpdateException;

public class ChannelConfigProcessor implements ConfigurationProcessor {
   private final boolean DEBUG = false;

   public void updateConfiguration(DomainMBean var1) throws UpdateException {
      try {
         this.changeChannels(var1);
      } catch (Exception var3) {
         throw new UpdateException(var3);
      }
   }

   private void changeChannels(DomainMBean var1) throws Exception {
      ServerMBean[] var2 = var1.getServers();
      HashMap var3 = new HashMap();

      for(int var4 = 0; var4 < var2.length; ++var4) {
         var3.put(var2[var4].getName(), var2[var4]);
      }

      NetworkChannelMBean[] var15 = var1.getNetworkChannels();

      for(int var5 = 0; var5 < var15.length; ++var5) {
         NetworkChannelMBean var6 = var15[var5];
         TargetMBean[] var7 = var6.getTargets();

         for(int var8 = 0; var8 < var7.length; ++var8) {
            Set var9 = var7[var8].getServerNames();

            ServerMBean var11;
            NetworkAccessPointMBean var13;
            for(Iterator var10 = var9.iterator(); var10.hasNext(); var11.destroyNetworkAccessPoint(var13)) {
               var11 = (ServerMBean)var3.get((String)var10.next());
               NetworkAccessPointMBean[] var12 = var11.getNetworkAccessPoints();
               var13 = null;

               for(int var14 = 0; var14 < var12.length; ++var14) {
                  if (var12[var14].getName().equals(var6.getName())) {
                     var13 = var12[var14];
                     break;
                  }
               }

               if (var13 == null) {
                  var13 = var11.createNetworkAccessPoint(var6.getName());
               }

               NetworkAccessPointMBean var16;
               if (var6.isT3Enabled()) {
                  var16 = var11.createNetworkAccessPoint(var6.getName() + "_t3");
                  var16.setProtocol("t3");
                  var16.setCompleteMessageTimeout(var6.getCompleteT3MessageTimeout());
                  var16.setMaxMessageSize(var6.getMaxT3MessageSize());
                  var16.setHttpEnabledForThisProtocol(var6.isHTTPEnabled());
                  this.copyNCSettings(var16, var6);
               }

               if (var6.isT3SEnabled()) {
                  var16 = var11.createNetworkAccessPoint(var6.getName() + "_t3s");
                  var16.setProtocol("t3s");
                  var16.setCompleteMessageTimeout(var6.getCompleteT3MessageTimeout());
                  var16.setMaxMessageSize(var6.getMaxT3MessageSize());
                  var16.setHttpEnabledForThisProtocol(var6.isHTTPSEnabled());
                  this.copyNCSettings(var16, var6);
               }

               if (var6.isIIOPEnabled()) {
                  var16 = var11.createNetworkAccessPoint(var6.getName() + "_iiop");
                  var16.setProtocol("iiop");
                  var16.setCompleteMessageTimeout(var6.getCompleteIIOPMessageTimeout());
                  var16.setMaxMessageSize(var6.getMaxIIOPMessageSize());
                  var16.setIdleConnectionTimeout(var6.getIdleIIOPConnectionTimeout());
                  var16.setHttpEnabledForThisProtocol(var6.isHTTPEnabled());
                  this.copyNCSettings(var16, var6);
               }

               if (var6.isIIOPSEnabled()) {
                  var16 = var11.createNetworkAccessPoint(var6.getName() + "_iiops");
                  var16.setProtocol("iiops");
                  var16.setCompleteMessageTimeout(var6.getCompleteIIOPMessageTimeout());
                  var16.setMaxMessageSize(var6.getMaxIIOPMessageSize());
                  var16.setIdleConnectionTimeout(var6.getIdleIIOPConnectionTimeout());
                  var16.setHttpEnabledForThisProtocol(var6.isHTTPSEnabled());
                  this.copyNCSettings(var16, var6);
               }

               if (var6.isCOMEnabled()) {
                  var16 = var11.createNetworkAccessPoint(var6.getName() + "_com");
                  var16.setProtocol("com");
                  var16.setCompleteMessageTimeout(var6.getCompleteCOMMessageTimeout());
                  var16.setMaxMessageSize(var6.getMaxCOMMessageSize());
                  this.copyNCSettings(var16, var6);
               }

               if (var6.isHTTPEnabled()) {
                  var16 = var11.createNetworkAccessPoint(var6.getName() + "_http");
                  var16.setProtocol("http");
                  var16.setCompleteMessageTimeout(var6.getCompleteHTTPMessageTimeout());
                  var16.setMaxMessageSize(var6.getMaxHTTPMessageSize());
                  this.copyNCSettings(var16, var6);
               }

               if (var6.isHTTPSEnabled()) {
                  var16 = var11.createNetworkAccessPoint(var6.getName() + "_https");
                  var16.setProtocol("https");
                  var16.setCompleteMessageTimeout(var6.getCompleteHTTPMessageTimeout());
                  var16.setMaxMessageSize(var6.getMaxHTTPMessageSize());
                  this.copyNCSettings(var16, var6);
               }
            }
         }

         var1.destroyNetworkChannel(var6);
      }

   }

   private void copyNCSettings(NetworkAccessPointMBean var1, NetworkChannelMBean var2) throws Exception {
      var1.setListenPort(var2.getListenPort());
      var1.setEnabled(var2.isListenPortEnabled());
      var1.setOutboundEnabled(var2.isOutgoingEnabled());
      var1.setChannelWeight(var2.getChannelWeight());
      var1.setAcceptBacklog(var2.getAcceptBacklog());
      var1.setLoginTimeoutMillis(var2.getLoginTimeoutMillis());
      var1.setTunnelingEnabled(var2.isTunnelingEnabled());
      var1.setTunnelingClientPingSecs(var2.getTunnelingClientPingSecs());
      var1.setTunnelingClientTimeoutSecs(var2.getTunnelingClientTimeoutSecs());
   }

   private static final void p(String var0) {
      System.out.println("<ChannelConfigProcessor>: " + var0);
   }
}
