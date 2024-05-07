package weblogic.tgiop;

import com.bea.core.jatmi.common.ntrace;
import com.bea.core.jatmi.internal.TCRouteEntry;
import com.bea.core.jatmi.internal.TuxedoXid;
import java.io.IOException;
import java.security.AccessController;
import java.util.ArrayList;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;
import weblogic.iiop.Connection;
import weblogic.iiop.ConnectionKey;
import weblogic.iiop.ConnectionManager;
import weblogic.iiop.EndPoint;
import weblogic.iiop.IIOPInputStream;
import weblogic.iiop.IIOPOutputStream;
import weblogic.iiop.IIOPService;
import weblogic.iiop.IOR;
import weblogic.iiop.Message;
import weblogic.iiop.MessageHeader;
import weblogic.iiop.ProtocolHandlerIIOP;
import weblogic.iiop.ReplyMessage;
import weblogic.logging.LogOutputStream;
import weblogic.protocol.AsyncOutgoingMessage;
import weblogic.protocol.ChannelImpl;
import weblogic.protocol.ServerChannel;
import weblogic.rmi.spi.Channel;
import weblogic.security.acl.UserInfo;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.utils.Debug;
import weblogic.utils.DebugCategory;
import weblogic.wtc.WTCLogger;
import weblogic.wtc.gwt.MethodParameters;
import weblogic.wtc.gwt.OatmialServices;
import weblogic.wtc.gwt.TDMRemote;
import weblogic.wtc.gwt.TuxedoCorbaConnection;
import weblogic.wtc.gwt.TuxedoCorbaConnectionFactory;
import weblogic.wtc.gwt.WTCService;
import weblogic.wtc.jatmi.BEAObjectKey;
import weblogic.wtc.jatmi.BindInfo;
import weblogic.wtc.jatmi.ClientInfo;
import weblogic.wtc.jatmi.ObjinfoImpl;
import weblogic.wtc.jatmi.SessionAcallDescriptor;
import weblogic.wtc.jatmi.TPException;
import weblogic.wtc.jatmi.TdomTcb;
import weblogic.wtc.jatmi.TypedBuffer;
import weblogic.wtc.jatmi.TypedTGIOP;
import weblogic.wtc.jatmi.UserTcb;
import weblogic.wtc.jatmi.dsession;
import weblogic.wtc.jatmi.rdsession;
import weblogic.wtc.jatmi.tcm;
import weblogic.wtc.jatmi.tfmh;

public final class TGIOPConnection extends Connection {
   private static boolean initialized = false;
   private static boolean closed = false;
   private static TuxedoCorbaConnectionFactory tuxedoCorbaConnectionFactory;
   private EndPoint endPoint;
   private static final DebugCategory debugSecurity = Debug.getCategory("weblogic.tgiop.security");
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   protected static LogOutputStream log;
   private TuxedoCorbaConnection tuxedoCorbaConnection;
   private MethodParameters methodParameters;
   private AuthenticatedSubject user;
   private dsession session;
   private Channel channel;
   private static final ConnectionKey notused = new ConnectionKey("notused", 0);
   private static final int TGIOP_FLAG_MASK = -2;

   protected static void p(String var0) {
      System.err.println("<TGIOPConnection> " + var0);
   }

   public static LogOutputStream getLog() {
      if (log == null) {
         Class var0 = TGIOPConnection.class;
         synchronized(TGIOPConnection.class) {
            if (log == null) {
               log = new LogOutputStream("IIOPSocket");
            }
         }
      }

      return log;
   }

   public void authenticate(UserInfo var1) {
   }

   public AuthenticatedSubject getUser() {
      return null;
   }

   public void setTxContext(Object var1) {
   }

   public Object getTxContext() {
      return null;
   }

   public ServerChannel getChannel() {
      return ProtocolHandlerIIOP.getProtocolHandler().getDefaultServerChannel();
   }

   public Channel getRemoteChannel() {
      return this.channel;
   }

   private void initTuxedoCorbaConnectionFactory() throws IOException {
      try {
         InitialContext var1 = new InitialContext();
         tuxedoCorbaConnectionFactory = (TuxedoCorbaConnectionFactory)var1.lookup("tuxedo.services.TuxedoCorbaConnection");
      } catch (NamingException var2) {
         IIOPService.setTGIOPEnabled(false);
         throw new IOException("Problem getting TuxedoCorbaConnectionFactory: " + var2);
      }
   }

   private TGIOPConnection() throws IOException {
      if (!IIOPService.isTGIOPEnabled()) {
         throw new IOException("TGIOP is disabled");
      } else {
         if (!initialized) {
            initialized = true;
            this.initTuxedoCorbaConnectionFactory();
         }

      }
   }

   public TGIOPConnection(MethodParameters var1) throws IOException {
      this();
      this.methodParameters = var1;
      if (this.methodParameters != null) {
         this.session = (dsession)var1.get_gwatmi();
      }

      if (this.methodParameters != null && this.methodParameters.getServiceParameters() != null) {
         this.user = var1.getAuthenticatedSubject();
      } else {
         this.user = SecurityServiceManager.getCurrentSubject(kernelId);
      }

      if (debugSecurity.isEnabled()) {
         WTCLogger.logDebugSecurity("inbound request user set to: " + this.user);
      }

   }

   public TGIOPConnection(String var1, String var2, int var3) throws IOException {
      this();
      this.channel = new ChannelImpl(var2, var3, "tgiop");

      try {
         this.tuxedoCorbaConnection = tuxedoCorbaConnectionFactory.getTuxedoCorbaConnection();
         StringBuffer var4 = new StringBuffer("//");
         var4.append(var1);
         ArrayList var5 = this.tuxedoCorbaConnection.getProviderRoute(var4.toString(), (TypedBuffer)null, (Xid)null, 0);
         TCRouteEntry var6 = (TCRouteEntry)var5.get(0);
         this.session = (dsession)var6.getSessionGroup();
      } catch (TPException var7) {
         throw new IOException("Could not get TuxedoCorbaConnection : " + var7);
      } catch (Exception var8) {
         throw new IOException("Could not get TuxedoCorbaConnection : " + var8);
      }
   }

   public final synchronized void send(AsyncOutgoingMessage var1) throws IOException {
      IIOPOutputStream var2 = (IIOPOutputStream)var1;
      Message var3 = (Message)var2.getMessage();
      int var4 = var3.getRequestID();
      byte[] var5 = var2.getBufferToWrite();
      if (debugSecurity.isEnabled()) {
         WTCLogger.logDebugSecurity("sending tgiop message, user set to " + SecurityServiceManager.getCurrentSubject(kernelId));
      }

      if (this.tuxedoCorbaConnection != null) {
         this.methodParameters = new MethodParameters(var4, this.session);
         this.sendUsingTuxedoCorbaConnection(var5, var5.length);
      } else {
         this.sendUsingMethodParameters(var5, var5.length);
      }
   }

   private void sendUsingTuxedoCorbaConnection(byte[] var1, int var2) throws IOException {
      boolean var3 = ntrace.isTraceEnabled(65000);
      if (var3) {
         ntrace.doTrace("[/TGIOPConnection/sendUsingTuxedoCorbaConnection/");
      }

      IOException var5;
      try {
         TypedTGIOP var4 = new TypedTGIOP(var1, var2);
         BEAObjectKey var9 = new BEAObjectKey(var4);
         ObjinfoImpl var6 = new ObjinfoImpl(var9, (ClientInfo)null, (BindInfo)null, 0);
         this.tuxedoCorbaConnection.tpMethodReq(var4, var6, this.methodParameters, 16384);
      } catch (TPException var7) {
         if (var3) {
            ntrace.doTrace("*]/TGIOPConnection/sendUsingTuxedoCorbaConnection/10/exception: " + var7);
         }

         var5 = new IOException("Could not send message via WTC : " + var7);
         var5.initCause(var7);
         throw var5;
      } catch (Exception var8) {
         if (var3) {
            ntrace.doTrace("*]/TGIOPConnection/sendUsingTuxedoCorbaConnection/20/exception: " + var8);
         }

         var5 = new IOException("Could not send message via WTC : " + var8);
         var5.initCause(var8);
         throw var5;
      }

      if (var3) {
         ntrace.doTrace("]/TGIOPConnection/sendUsingTuxedoCorbaConnection/30");
      }

   }

   private void sendUsingMethodParameters(byte[] var1, int var2) throws IOException {
      TypedTGIOP var3 = new TypedTGIOP(var1, var2);
      tcm var4 = new tcm((short)0, new UserTcb(var3));
      tfmh var5 = new tfmh(var3.getHintIndex(), var4, 1);
      dsession var6 = (dsession)this.methodParameters.get_gwatmi();
      TdomTcb var7 = (TdomTcb)this.methodParameters.get_invokeInfo().getServiceMessage().tdom.body;
      byte var8 = 0;
      byte var9 = 0;
      Object var10 = null;
      boolean var11 = true;
      SessionAcallDescriptor var12 = null;
      rdsession var13 = null;
      int var24;
      if ((var24 = var7.get_convid()) != -1) {
         var12 = new SessionAcallDescriptor(var24, true);
         var13 = var6.get_rcv_place();
      }

      Object[] var14 = this.methodParameters.getTxInfo();
      TuxedoXid var15 = null;
      TDMRemote var16 = null;
      OatmialServices var17 = null;
      XAResource var18 = null;
      if (var14 != null) {
         var15 = (TuxedoXid)var14[0];
         var18 = (XAResource)var14[1];
         var16 = (TDMRemote)var14[2];
         var17 = WTCService.getOatmialServices();
      }

      try {
         var6.send_success_return(this.methodParameters.get_invokeInfo().getReqid(), var5, var8, var9, var24);
      } catch (TPException var23) {
         if (var18 != null) {
            try {
               var18.end(var15, 536870912);
               var17.removeInboundRdomFromXid(var16, var15);
               var18.rollback(var15);
            } catch (XAException var21) {
            }
         }

         if (var24 == -1) {
            var6.send_failure_return(this.methodParameters.get_invokeInfo().getReqid(), var23, var24);
         } else {
            var13.remove_rplyObj(var12);
         }

         throw new IOException("Got TPException: " + var23);
      }

      if (var18 != null) {
         try {
            if (var10 == null) {
               var18.end(var15, 67108864);
            } else {
               var18.end(var15, 536870912);
               var17.removeInboundRdomFromXid(var16, var15);
               var18.rollback(var15);
            }
         } catch (XAException var22) {
         }
      }

      if (var24 != -1) {
         var13.remove_rplyObj(var12);
      }

   }

   private TPException getTPExceptionFromReply(byte[] var1) {
      int var2 = MessageHeader.getMsgType(var1);
      if (var2 != 1) {
         return null;
      } else {
         IIOPInputStream var3 = new IIOPInputStream(var1);
         MessageHeader var4 = new MessageHeader(var3);
         ReplyMessage var5 = new ReplyMessage(this.getEndPoint(), var4, var3);
         return var5.getReplyStatus() == 2 ? new TPException(11) : null;
      }
   }

   public final synchronized void close() {
      if (!this.isClosed()) {
         if (this.tuxedoCorbaConnection != null) {
            this.tuxedoCorbaConnection.tpterm();
         }

         closed = true;
      }
   }

   public final synchronized boolean isClosed() {
      return closed;
   }

   public boolean isStateful() {
      return false;
   }

   public ConnectionKey getConnectionKey() {
      return notused;
   }

   public void setConnectionKey(ConnectionKey var1) {
   }

   public final int getMinorVersion() {
      return this.session.getMinorVersion();
   }

   public final void setMinorVersion(int var1) {
      this.session.setMinorVersion(var1);
   }

   public final void setCodeSets(int var1, int var2) {
      this.session.setCodeSets(var1, var2);
   }

   public final int getWcharCodeSet() {
      return this.session.getWcharCodeSet();
   }

   public final int getCharCodeSet() {
      return this.session.getCharCodeSet();
   }

   public void setFlag(int var1) {
      this.session.setFlag(var1 & -2);
   }

   public boolean getFlag(int var1) {
      return this.session.getFlag(var1);
   }

   public final IOR getRemoteCodeBase() {
      return this.session.getRemoteCodeBase();
   }

   public final void setRemoteCodeBase(IOR var1) {
      this.session.setRemoteCodeBase(var1);
   }

   public EndPoint getEndPoint() {
      if (this.endPoint == null) {
         synchronized(this) {
            if (this.endPoint == null) {
               this.endPoint = new TGIOPEndPointImpl(this, ConnectionManager.getConnectionManager(), this.user);
            }
         }
      }

      return this.endPoint;
   }

   protected final boolean isSecure() {
      return false;
   }
}
