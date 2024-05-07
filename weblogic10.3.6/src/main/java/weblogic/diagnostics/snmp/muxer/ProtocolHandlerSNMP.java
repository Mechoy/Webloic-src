package weblogic.diagnostics.snmp.muxer;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.Socket;
import java.security.AccessController;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.diagnostics.snmp.agent.SNMPV3Agent;
import weblogic.diagnostics.snmp.i18n.SNMPLogger;
import weblogic.management.configuration.NetworkAccessPointMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.protocol.Protocol;
import weblogic.protocol.ProtocolHandler;
import weblogic.protocol.ProtocolManager;
import weblogic.protocol.ServerChannel;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.server.channels.ServerChannelImpl;
import weblogic.socket.MuxableSocket;
import weblogic.utils.ArrayUtils;
import weblogic.utils.io.Chunk;

public class ProtocolHandlerSNMP implements ProtocolHandler, PropertyChangeListener, ArrayUtils.DiffHandler {
   private static final AuthenticatedSubject KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final String PROTOCOL_NAME = "snmp";
   static final byte ASN1_SEQUENCE_DEFINITE_LENGTH = 48;
   static final byte BER_ENCODED_INTEGER = 2;
   static final int MULTIBYTE_LENGTH_TAG = 128;
   static final int MULTIBYTE_LENGTH_MASK = 127;
   private static final int SNMP_V1_TAG = 0;
   private static final int SNMP_V2_TAG = 1;
   private static final int SNMP_V3_TAG = 3;
   private static final DebugLogger DEBUG = DebugLogger.getDebugLogger("DebugSNMPProtocolTCP");
   static final int DISCRIMINATION_LENGTH = 9;
   private static final ProtocolHandlerSNMP theOne = new ProtocolHandlerSNMP();
   public static final Protocol PROTOCOL_SNMP = ProtocolManager.createProtocol((byte)17, "snmp", "snmp", false, getProtocolHandler());
   protected SNMPV3Agent snmpAgent;
   private boolean usingDedicatedSnmpChannel;
   private static int SNMP_PRIORITY = 2147483646;
   private ServerMBean server = this.getServerRuntimeConfiguration();

   public static ProtocolHandler getProtocolHandler() {
      if (DEBUG.isDebugEnabled()) {
         Utils.debug(DEBUG, "ProtocolHandlerSNMP", "getSNMPProtocolHandler", "");
      }

      return getSNMPProtocolHandler();
   }

   public static ProtocolHandlerSNMP getSNMPProtocolHandler() {
      if (DEBUG.isDebugEnabled()) {
         Utils.debug(DEBUG, "ProtocolHandlerSNMP", "getSNMPProtocolHandler", "");
      }

      return theOne;
   }

   private ProtocolHandlerSNMP() {
      this.server.addPropertyChangeListener(this);
      if (DEBUG.isDebugEnabled()) {
         DEBUG.debug("Initializing, checking for dedicated SNMP channel.");
      }

      this.usingDedicatedSnmpChannel = this.checkForDedicatedSNMPChannel(this.server.getNetworkAccessPoints());
      NetworkAccessPointMBean[] var1 = this.server.getNetworkAccessPoints();
      if (var1 != null) {
         for(int var2 = 0; var2 < var1.length; ++var2) {
            if (var1[var2] != null) {
               var1[var2].addPropertyChangeListener(this);
            }
         }
      }

   }

   private ServerMBean getServerRuntimeConfiguration() {
      RuntimeAccess var1 = ManagementService.getRuntimeAccess(KERNEL_ID);
      ServerMBean var2 = var1.getServer();
      return var2;
   }

   public ServerChannel getDefaultServerChannel() {
      return ProtocolHandlerSNMP.ChannelInitializer.DEFAULT_SNMP_CHANNEL;
   }

   public int getHeaderLength() {
      return 9;
   }

   public int getPriority() {
      return SNMP_PRIORITY;
   }

   public Protocol getProtocol() {
      return PROTOCOL_SNMP;
   }

   public boolean claimSocket(Chunk var1) {
      if (this.snmpAgent != null && this.snmpAgent.isSNMPAgentInitialized()) {
         if (var1.end < 9) {
            return false;
         } else {
            byte[] var2 = var1.buf;
            int var3 = 0;
            if (var2[var3++] != 48) {
               return false;
            } else {
               byte var4 = var2[var3++];
               if ((var4 & 128) == 128) {
                  int var5 = var4 & 127;
                  if (var5 > MuxableSocketSNMP.MSG_LENGTH_MULTIPLIERS.length) {
                     if (DEBUG.isDebugEnabled()) {
                        this.debug("claimSocket", "multi-byte length field greater than 4");
                     }

                     return false;
                  }

                  var3 += var5;
               }

               if (var2[var3++] != 2) {
                  return false;
               } else if ((var2[var3] & 128) == 128) {
                  return false;
               } else {
                  byte var7 = var2[var3++];
                  if (var7 > 1) {
                     return false;
                  } else {
                     byte var6 = var2[var3++];
                     switch (var6) {
                        case 0:
                        case 1:
                        case 3:
                           if (DEBUG.isDebugEnabled()) {
                              this.debug("claimSocket", "Valid SNMP packet, version: " + var6);
                           }

                           return true;
                        case 2:
                        default:
                           if (DEBUG.isDebugEnabled()) {
                              this.debug("claimSocket", "Invalid SNMP packet version: " + var6);
                           }

                           return false;
                     }
                  }
               }
            }
         }
      } else {
         return false;
      }
   }

   public MuxableSocket createSocket(Chunk var1, Socket var2, ServerChannel var3) throws IOException {
      if (this.usingDedicatedSnmpChannel && var3.getChannelName().startsWith("Default")) {
         String var5 = SNMPLogger.logUseOfDefaultSnmpChannelDetectedLoggable().getMessage();
         throw new InvalidSNMPNetworkChannelException(var5);
      } else {
         MuxableSocketSNMP var4 = new MuxableSocketSNMP(var1, var2, var3, this.snmpAgent);
         return var4;
      }
   }

   public void propertyChange(PropertyChangeEvent var1) {
      Object var2 = var1.getSource();
      if (var2 instanceof ServerMBean) {
         if (var1.getPropertyName().equals("NetworkAccessPoints")) {
            if (DEBUG.isDebugEnabled()) {
               DEBUG.debug("NetworkAccessPoints changed, processing changes");
            }

            NetworkAccessPointMBean[] var3 = (NetworkAccessPointMBean[])((NetworkAccessPointMBean[])var1.getOldValue());
            NetworkAccessPointMBean[] var4 = (NetworkAccessPointMBean[])((NetworkAccessPointMBean[])var1.getNewValue());
            ArrayUtils.computeDiff(var3, var4, this);
            this.usingDedicatedSnmpChannel = this.checkForDedicatedSNMPChannel(var4);
         }
      } else if (var2 instanceof NetworkAccessPointMBean) {
         if (DEBUG.isDebugEnabled()) {
            DEBUG.debug("NetworkAccessPoints changed, processing changes");
         }

         if (var1.getPropertyName().equals("Enabled")) {
            this.usingDedicatedSnmpChannel = this.checkForDedicatedSNMPChannel(this.server.getNetworkAccessPoints());
         }
      }

   }

   private boolean checkForDedicatedSNMPChannel(NetworkAccessPointMBean[] var1) {
      if (var1 != null) {
         for(int var2 = 0; var2 < var1.length; ++var2) {
            NetworkAccessPointMBean var3 = var1[var2];
            if (var3 != null && var3.isEnabled() && var3.getProtocol().equals("snmp") && !var3.getName().startsWith("Default")) {
               if (DEBUG.isDebugEnabled()) {
                  DEBUG.debug("Found dedicated SNMP channel, channel name: " + var3.getName());
               }

               return true;
            }
         }
      }

      if (DEBUG.isDebugEnabled()) {
         DEBUG.debug("Using default channel for SNMP traffic.");
      }

      return false;
   }

   public void setAgent(SNMPV3Agent var1) {
      this.snmpAgent = var1;
   }

   public SNMPV3Agent getAgent() {
      return this.snmpAgent;
   }

   private void debug(String var1, String var2) {
      Utils.debug(DEBUG, "ProtocolHandlerSNMP", var1, var2);
   }

   public void addObject(Object var1) {
      if (var1 instanceof NetworkAccessPointMBean) {
         NetworkAccessPointMBean var2 = (NetworkAccessPointMBean)var1;
         if (var2.getProtocol().equals("snmp")) {
            var2.addPropertyChangeListener(this);
            if (DEBUG.isDebugEnabled()) {
               DEBUG.debug("Registering protocol handler for property change notifications with NAP named " + var2.getName());
            }
         }
      }

   }

   public void removeObject(Object var1) {
      if (var1 instanceof NetworkAccessPointMBean) {
         NetworkAccessPointMBean var2 = (NetworkAccessPointMBean)var1;
         if (var2.getProtocol().equals("snmp")) {
            if (DEBUG.isDebugEnabled()) {
               DEBUG.debug("NAP " + var2.getName() + " removed, unregistering for property change notifications");
            }

            var2.removePropertyChangeListener(this);
         }
      }

   }

   private static final class ChannelInitializer {
      private static final ServerChannel DEFAULT_SNMP_CHANNEL;

      static {
         DEFAULT_SNMP_CHANNEL = ServerChannelImpl.createDefaultServerChannel(ProtocolHandlerSNMP.PROTOCOL_SNMP);
      }
   }
}
