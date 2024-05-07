package weblogic.diagnostics.snmp.muxer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.diagnostics.snmp.agent.SNMPTransportProvider;
import weblogic.diagnostics.snmp.agent.SNMPV3Agent;
import weblogic.diagnostics.snmp.agent.SNMPV3AgentToolkit;
import weblogic.protocol.ServerChannel;
import weblogic.protocol.ServerChannelManager;
import weblogic.socket.AbstractMuxableSocket;
import weblogic.socket.SocketMuxer;
import weblogic.utils.io.Chunk;
import weblogic.utils.io.ChunkedInputStream;
import weblogic.work.WorkManager;

public final class MuxableSocketSNMP extends AbstractMuxableSocket {
   private static final DebugLogger DEBUG_LOGGER = DebugLogger.getDebugLogger("DebugSNMPProtocolTCP");
   static final int[] MSG_LENGTH_MULTIPLIERS = new int[]{1, 256, 65536, 16777216};
   private SNMPV3Agent snmpAgent;
   private WorkManager snmpWorkManager;

   MuxableSocketSNMP(Chunk var1, Socket var2, ServerChannel var3, SNMPV3Agent var4) throws IOException {
      super(var1, var2, var3);
      this.snmpAgent = var4;
      if (this.snmpAgent != null) {
         this.snmpWorkManager = this.snmpAgent.getSnmpWorkManagerInstance();
      }

      if (DEBUG_LOGGER.isDebugEnabled() && this.snmpWorkManager != null) {
         DEBUG_LOGGER.debug("Workmanager instance for SNMP traffic: " + this.snmpWorkManager.getName());
      }

   }

   MuxableSocketSNMP(InetAddress var1, int var2, ServerChannel var3) throws IOException {
      super(var3);
      this.connect(var1, var2);
   }

   public static MuxableSocketSNMP createConnection(String var0, int var1) throws IOException {
      ServerChannel var2 = ServerChannelManager.findOutboundServerChannel(ProtocolHandlerSNMP.PROTOCOL_SNMP);
      MuxableSocketSNMP var3 = new MuxableSocketSNMP(InetAddress.getByName(var0), var1, var2);
      SocketMuxer.getMuxer().register(var3);
      SocketMuxer.getMuxer().read(var3);
      return var3;
   }

   protected int getHeaderLength() {
      return 9;
   }

   protected int getMessageLength() {
      if (super.msgLength < 0) {
         int var1 = 1;
         int var2 = 2;
         int var3 = this.getHeaderByte(var1++);
         if ((var3 & 128) == 128) {
            int var4 = var3 & 127;
            var2 += var4;
            var3 = 0;

            for(int var5 = 0; var5 < var4; ++var5) {
               int var6 = MSG_LENGTH_MULTIPLIERS[var4 - var5 - 1];
               byte var7 = this.getHeaderByte(var1++);
               var3 += (var7 & 255) * var6;
            }
         }

         super.msgLength = var2 + var3;
         if (DEBUG_LOGGER.isDebugEnabled()) {
            this.debug("getMessageLength", "msgLength == " + super.msgLength);
         }
      }

      return super.msgLength;
   }

   public void dispatch(Chunk var1) {
      if (this.snmpWorkManager != null) {
         this.snmpWorkManager.schedule(new SnmpProtocolReader(var1));
      }

   }

   public void hasException(Throwable var1) {
      this.close();
   }

   public void endOfStream() {
      this.close();
   }

   public boolean timeout() {
      this.close();
      return true;
   }

   private void debug(String var1, String var2) {
      Utils.debug(DEBUG_LOGGER, "MuxableSocketSNMP", var1, var2);
   }

   private class SnmpProtocolReader implements Runnable {
      private Chunk dataChunks;

      public SnmpProtocolReader(Chunk var2) {
         this.dataChunks = var2;
      }

      public void run() {
         if (MuxableSocketSNMP.DEBUG_LOGGER.isDebugEnabled()) {
            this.debug("run", "processing message");
         }

         try {
            if (MuxableSocketSNMP.this.snmpAgent != null && MuxableSocketSNMP.this.snmpAgent.isSNMPAgentInitialized()) {
               SNMPV3AgentToolkit var1 = (SNMPV3AgentToolkit)MuxableSocketSNMP.this.snmpAgent.getSNMPAgentToolkit();
               SNMPTransportProvider var2 = var1.getTransportProvider(1);
               if (var2 != null) {
                  ChunkedInputStream var3 = new ChunkedInputStream(this.dataChunks, 0);
                  int var4 = Chunk.size(this.dataChunks);
                  if (var4 > 0) {
                     byte[] var5 = new byte[var4];
                     int var6 = var3.available();

                     for(int var7 = 0; var6 > 0 && var7 < var4; var6 = var3.available()) {
                        var7 += var3.read(var5, var7, var6);
                     }

                     if (MuxableSocketSNMP.DEBUG_LOGGER.isDebugEnabled()) {
                        this.debug("run", "pushing message into agent");
                     }

                     var2.pushMessage(MuxableSocketSNMP.this, var5);
                  }
               }
            }
         } catch (Throwable var8) {
            this.debug("run", "Caught exception: " + var8.getMessage());
            SocketMuxer.getMuxer().deliverHasException(MuxableSocketSNMP.this.getSocketFilter(), var8);
            MuxableSocketSNMP.this.close();
         }

      }

      private void debug(String var1, String var2) {
         if (MuxableSocketSNMP.DEBUG_LOGGER.isDebugEnabled()) {
            Utils.debug(MuxableSocketSNMP.DEBUG_LOGGER, "MuxableSocketSNMP.SnmpProtocolReader:", var1, var2);
         }

      }
   }
}
