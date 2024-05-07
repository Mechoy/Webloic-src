package weblogic.iiop;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSocket;
import javax.security.auth.login.LoginException;
import weblogic.kernel.Kernel;
import weblogic.kernel.KernelStatus;
import weblogic.protocol.ProtocolHandlerAdmin;
import weblogic.protocol.ProtocolManager;
import weblogic.protocol.ServerChannel;
import weblogic.protocol.ServerChannelManager;
import weblogic.security.acl.UserInfo;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.acl.internal.AuthenticatedUser;
import weblogic.security.service.PrincipalAuthenticator;
import weblogic.security.service.SecurityServiceManager;
import weblogic.security.utils.SSLCertUtility;
import weblogic.security.utils.SSLIOContext;
import weblogic.security.utils.SSLIOContextTable;
import weblogic.socket.JSSESocket;
import weblogic.socket.SSLFilter;
import weblogic.socket.utils.JSSEUtils;
import weblogic.utils.io.Chunk;

final class MuxableSocketIIOPS extends MuxableSocketIIOP {
   public MuxableSocketIIOPS(Chunk var1, Socket var2, ServerChannel var3) throws IOException {
      super(var1, var2, var3);
   }

   protected MuxableSocketIIOPS(ServerChannel var1) {
      super(var1);
   }

   static MuxableSocketIIOP createConnection(InetAddress var0, int var1, String var2) throws IOException {
      ServerChannel var3 = null;
      if (KernelStatus.isServer() && kernelId.getQOS() == 103 && SecurityServiceManager.getCurrentSubject(kernelId) == kernelId) {
         if (ProtocolManager.getDefaultAdminProtocol().toByte() != ProtocolHandlerIIOPS.PROTOCOL_IIOPS.toByte()) {
            throw new IOException("Attempted to use IIOPS as the admin protocol");
         }

         var3 = ServerChannelManager.findOutboundServerChannel(ProtocolHandlerAdmin.PROTOCOL_ADMIN, var2);
      } else {
         var3 = ServerChannelManager.findOutboundServerChannel(ProtocolHandlerIIOPS.PROTOCOL_IIOPS, var2);
      }

      MuxableSocketIIOPS var4 = new MuxableSocketIIOPS(var3);
      var4.connect(var0, var1);
      var4.register(var4.getSocket(), true);
      return var4;
   }

   public void register(Socket var1, boolean var2) throws IOException {
      if (var2) {
         SSLSocket var3 = (SSLSocket)var1;
         JSSESocket var4 = JSSEUtils.getJSSESocket(var3);
         if (var4 != null) {
            JSSEUtils.registerJSSEFilter(var4, this);
            JSSEUtils.activate(var4, this);
         } else {
            SSLIOContext var5 = SSLIOContextTable.findContext(var3);
            SSLFilter var6 = (SSLFilter)var5.getFilter();
            this.setSocketFilter(var6);

            try {
               var3.startHandshake();
            } catch (SSLException var10) {
               if (!var3.isClosed()) {
                  try {
                     var3.close();
                  } catch (IOException var9) {
                  }
               }

               throw var10;
            }

            var6.setDelegate(this);
            var6.activate();
            if (!Kernel.isServer()) {
               return;
            }
         }
      }

   }

   public byte getQOS() {
      return 102;
   }

   public void authenticate(UserInfo var1) {
      if (var1 == null || var1 instanceof AuthenticatedUser || !this.authenticate()) {
         super.authenticate(var1);
      }
   }

   public AuthenticatedSubject getUser() {
      if (this.getSubject() == null && Kernel.isServer()) {
         this.authenticate();
      }

      return super.getUser();
   }

   private boolean authenticate() {
      X509Certificate[] var1 = null;
      SSLSocket var2 = (SSLSocket)this.getSocket();

      try {
         var1 = SSLCertUtility.getPeerCertChain(var2);
      } catch (Exception var7) {
      }

      if (var1 != null) {
         String var3 = "X.509";
         PrincipalAuthenticator var4 = Connection.getPrincipalAuthenticator();

         try {
            AuthenticatedSubject var5 = var4.assertIdentity(var3, var1, this);
            if (var5 != null) {
               this.setSubject(var5);
               return true;
            }
         } catch (LoginException var6) {
         }
      }

      return false;
   }

   protected final boolean isSecure() {
      return true;
   }
}
