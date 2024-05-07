package weblogic.wtc.corba.internal;

import com.bea.core.jatmi.common.ntrace;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.server.RMISocketFactory;
import weblogic.iiop.ProtocolHandlerIIOP;
import weblogic.protocol.ServerChannel;
import weblogic.protocol.ServerChannelManager;

public final class ORBSocketFactory extends RMISocketFactory {
   boolean isWTCObject;

   public ORBSocketFactory() {
      boolean var1 = ntrace.isTraceEnabled(8);
      if (var1) {
         ntrace.doTrace("[/ORBSocketFactory");
      }

      this.isWTCObject = true;
      if (var1) {
         ntrace.doTrace("]/ORBSocketFactory");
      }

   }

   public Socket createSocket(String var1, int var2) throws IOException {
      boolean var3 = ntrace.isTraceEnabled(8);
      if (var3) {
         ntrace.doTrace("[/ORBSocketFactory/createSocket/" + var1 + "/" + var2);
         ntrace.doTrace("]/ORBSocketFactory/createSocket");
      }

      try {
         ServerChannel var4 = ServerChannelManager.findLocalServerChannel(ProtocolHandlerIIOP.PROTOCOL_IIOP);
         String var5 = var4.getPublicAddress();
         int var6 = var4.getPublicPort();
         if (var6 == var2 && var5 != null && var1 != null && var5.equals(var1)) {
            this.isWTCObject = false;
         }
      } catch (Exception var7) {
      }

      return (Socket)(!this.isWTCObject ? new Socket(var1, var2) : new ORBSocket(var1, var2));
   }

   public ServerSocket createServerSocket(int var1) throws IOException {
      return new ServerSocket(var1);
   }
}
