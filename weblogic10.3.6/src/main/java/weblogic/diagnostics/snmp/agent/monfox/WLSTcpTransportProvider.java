package weblogic.diagnostics.snmp.agent.monfox;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import monfox.toolkit.snmp.engine.SnmpBuffer;
import monfox.toolkit.snmp.engine.SnmpTransportException;
import monfox.toolkit.snmp.engine.TcpEntity;
import monfox.toolkit.snmp.engine.TransportEntity;
import monfox.toolkit.snmp.engine.TransportProvider;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.diagnostics.snmp.agent.SNMPTransportProvider;
import weblogic.diagnostics.snmp.i18n.SNMPLogger;
import weblogic.diagnostics.snmp.muxer.MuxableSocketSNMP;
import weblogic.socket.MuxableSocket;

public class WLSTcpTransportProvider extends TransportProvider implements SNMPTransportProvider {
   private boolean isActive = true;
   private static final DebugLogger DEBUG_LOGGER = DebugLogger.getDebugLogger("DebugSNMPToolkit");

   public boolean isPushProvider() {
      return true;
   }

   public void initialize(TransportProvider.Params var1) throws SnmpTransportException {
      super.initialize(var1);
   }

   public int getTransportType() {
      return 2;
   }

   public void initialize(InetAddress var1, int var2) throws SnmpTransportException {
      this.isActive = true;
   }

   public boolean isActive() {
      return this.isActive;
   }

   public void shutdown() throws SnmpTransportException {
      this.isActive = false;
   }

   public Object send(Object var1, TransportEntity var2) throws SnmpTransportException {
      if (!(var2 instanceof WLSTcpEntity)) {
         throw new SnmpTransportException("wrong transport provider type:" + var2);
      } else {
         WLSTcpEntity var3 = (WLSTcpEntity)var2;

         try {
            Object var4 = var3.getDestinationSocket();
            if (var4 == null) {
               if (DEBUG_LOGGER.isDebugEnabled()) {
                  DEBUG_LOGGER.debug("Creating outbound SNMP TCP connection to host " + var3.getAddress().getHostAddress() + " on port " + var3.getPort());
               }

               var4 = MuxableSocketSNMP.createConnection(var3.getAddress().getHostAddress(), var3.getPort());
            }

            if (!(var1 instanceof SnmpBuffer)) {
               throw new SnmpTransportException("unknown data class:" + var1.getClass().getName());
            }

            SnmpBuffer var5 = (SnmpBuffer)var1;
            if (DEBUG_LOGGER.isDebugEnabled()) {
               DEBUG_LOGGER.debug("Writing outgoing SNMP message, length " + var5.length);
            }

            OutputStream var6 = ((MuxableSocket)var4).getSocket().getOutputStream();
            var6.write(var5.data, var5.offset, var5.length);
         } catch (IOException var7) {
            SNMPLogger.logTCPProviderSendException(var7);
         }

         return var1;
      }
   }

   public TransportEntity receive(SnmpBuffer var1, boolean var2) throws SnmpTransportException {
      return null;
   }

   public static void log(String var0) {
      System.out.println((new Date(System.currentTimeMillis())).toString() + ": " + var0);
   }

   public void pushMessage(MuxableSocket var1, byte[] var2) {
      SnmpBuffer var3 = new SnmpBuffer(var2);
      WLSTcpEntity var4 = new WLSTcpEntity();
      var4.setProvider(this);
      var4.setDestinationSocket(var1);
      super.pushMessage(var4, var3);
   }

   public int getType() {
      return 1;
   }

   private class WLSTcpEntity extends TcpEntity {
      private MuxableSocket destinationSocket;

      public WLSTcpEntity() {
      }

      public WLSTcpEntity(MuxableSocket var2) {
         this.destinationSocket = var2;
      }

      public MuxableSocket getDestinationSocket() {
         return this.destinationSocket;
      }

      public void setDestinationSocket(MuxableSocket var1) {
         this.destinationSocket = var1;
      }
   }

   public static class Params extends TransportProvider.Params {
      public Params() {
      }

      public Params(String var1, int var2) throws UnknownHostException {
         super(var1 == null ? null : InetAddress.getByName(var1), var2);
      }

      public Params(InetAddress var1, int var2) {
         super(var1, var2);
      }

      public int getTransportType() {
         return 2;
      }
   }
}
