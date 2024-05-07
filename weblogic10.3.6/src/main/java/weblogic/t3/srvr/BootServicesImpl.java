package weblogic.t3.srvr;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.UnmarshalException;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import java.security.cert.X509Certificate;
import javax.security.auth.login.LoginException;
import weblogic.common.T3Exception;
import weblogic.common.internal.BootServices;
import weblogic.common.internal.PeerInfo;
import weblogic.common.internal.T3ClientParams;
import weblogic.kernel.Kernel;
import weblogic.management.provider.ManagementService;
import weblogic.protocol.ServerChannel;
import weblogic.protocol.configuration.ChannelHelper;
import weblogic.rjvm.MsgAbbrevJVMConnection;
import weblogic.rjvm.RJVMLogger;
import weblogic.rjvm.RJVMManager;
import weblogic.rjvm.RemoteInvokable;
import weblogic.rjvm.RemoteRequest;
import weblogic.rjvm.ReplyStream;
import weblogic.security.SimpleCallbackHandler;
import weblogic.security.SubjectUtils;
import weblogic.security.acl.DefaultUserInfoImpl;
import weblogic.security.acl.UserInfo;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.acl.internal.AuthenticatedUser;
import weblogic.security.service.PrincipalAuthenticator;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.security.service.SecurityService.ServiceType;
import weblogic.utils.AssertionError;
import weblogic.utils.Debug;
import weblogic.work.WorkAdapter;
import weblogic.work.WorkManager;
import weblogic.work.WorkManagerFactory;

public final class BootServicesImpl implements BootServices, RemoteInvokable {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private String realmName = "weblogicDEFAULT";
   private PrincipalAuthenticator pa;
   private byte qos;
   private int port;
   private ServerChannel channel;
   private X509Certificate[] peerCertChain = null;
   private static final String OLDBOOTSTRAPREQUEST_QUEUE = "wl_oldBootStrap";
   private WorkManager workMgr;

   private BootServicesImpl() {
      this.pa = (PrincipalAuthenticator)SecurityServiceManager.getSecurityService(kernelId, this.realmName, ServiceType.AUTHENTICATION);
      Debug.assertion(this.pa != null, "Security system not initialized");
      this.workMgr = WorkManagerFactory.getInstance().findOrCreate("wl_oldBootStrap", -1, 8);
   }

   public static void initialize() {
      RJVMManager.getLocalRJVM().getFinder().put(1, new BootServicesImpl());
   }

   public void setConnectionInfo(MsgAbbrevJVMConnection var1) {
      this.qos = var1.getQOS();
      this.port = var1.getLocalPort();
      this.channel = var1.getChannel();
      this.peerCertChain = var1.getJavaCertChain();
   }

   public AuthenticatedUser authenticate(UserInfo var1, PeerInfo var2) throws RemoteException {
      Object var3 = null;
      AuthenticatedSubject var4 = null;
      X509Certificate[] var5 = this.peerCertChain;
      this.peerCertChain = null;
      if (var1 instanceof AuthenticatedUser) {
         var3 = (AuthenticatedUser)var1;
      } else {
         if (var5 != null) {
            try {
               String var6 = "X.509";
               var4 = this.pa.assertIdentity(var6, var5);
            } catch (LoginException var11) {
            }
         }

         if (var4 == null) {
            if (!(var1 instanceof DefaultUserInfoImpl)) {
               throw new SecurityException("Received bad UserInfo: " + var1.getClass().getName());
            }

            DefaultUserInfoImpl var12 = (DefaultUserInfoImpl)var1;
            String var7 = var12.getName();
            String var8 = var12.getPassword();
            if (var7 != null && var7.length() != 0) {
               try {
                  SimpleCallbackHandler var9 = new SimpleCallbackHandler(var7, var8);
                  var4 = this.pa.authenticate(var9);
               } catch (LoginException var10) {
                  throw new SecurityException(var10.getMessage());
               }
            } else {
               var4 = SubjectUtils.getAnonymousSubject();
            }
         }

         this.checkAdminPort(var4, var1);
         var3 = var4;
      }

      ((AuthenticatedUser)var3).setQOS(this.qos);
      return (AuthenticatedUser)var3;
   }

   private void checkAdminPort(AuthenticatedSubject var1, UserInfo var2) {
      if (ChannelHelper.isLocalAdminChannelEnabled() && SubjectUtils.isUserAnAdministrator(var1) && ManagementService.getRuntimeAccess(kernelId).getServer().getAdministrationPort() != this.port && !ChannelHelper.isAdminChannel(this.channel)) {
         throw new SecurityException("All administrative tasks must go through an Administration Port.");
      }
   }

   public T3ClientParams findOrCreateClientContext(String var1, UserInfo var2, int var3) throws RemoteException {
      throw new InternalError("should never be called");
   }

   public void invoke(RemoteRequest var1) throws RemoteException {
      try {
         byte var2 = var1.readByte();
         switch (var2) {
            case 1:
               this.authenticate(var1);
               break;
            case 2:
               this.findOrCreateClientContext(var1);
               break;
            default:
               throw new AssertionError("Unknown OPCODE: " + var2);
         }

      } catch (RemoteException var3) {
         throw var3;
      } catch (IOException var4) {
         throw new UnmarshalException("While providing boot service", var4);
      } catch (ClassNotFoundException var5) {
         throw new UnmarshalException("While providing boot service", var5);
      }
   }

   private void authenticate(RemoteRequest var1) throws IOException, ClassNotFoundException {
      UserInfo var2 = (UserInfo)var1.readObject();
      PeerInfo var3 = var1.getPeerInfo();
      BootServicesAuthenticateRequest var4 = new BootServicesAuthenticateRequest(this, var1, var2, var3);
      this.workMgr.schedule(var4);
   }

   private void findOrCreateClientContext(RemoteRequest var1) throws IOException, ClassNotFoundException {
      String var2 = var1.readString();
      UserInfo var3 = (UserInfo)var1.readObjectWL();
      int var4 = var1.readInt();
      byte var5 = var1.readByte();
      BootServicesClientContextRequest var6 = new BootServicesClientContextRequest(this, var1, var2, var3, var4, var5);
      this.workMgr.schedule(var6);
   }

   private void checkServerLock(AuthenticatedUser var1) {
      if (!SubjectUtils.isUserAnAdministrator((AuthenticatedSubject)var1)) {
         T3Srvr.getT3Srvr().getLockoutManager().checkServerLock();
      }
   }

   public int hashCode() {
      return 1;
   }

   public boolean equals(Object var1) {
      return var1 == this;
   }

   private static class BootServicesClientContextRequest extends WorkAdapter {
      BootServicesImpl bootServicesImpl;
      RemoteRequest remoteRequest;
      String workSpace;
      UserInfo userInfo;
      int idleCallbackID;
      byte qos;

      public BootServicesClientContextRequest(BootServicesImpl var1, RemoteRequest var2, String var3, UserInfo var4, int var5, byte var6) {
         this.bootServicesImpl = var1;
         this.remoteRequest = var2;
         this.workSpace = var3;
         this.userInfo = var4;
         this.idleCallbackID = var5;
         this.qos = var6;
      }

      public void run() {
         try {
            final AuthenticatedUser var5 = this.bootServicesImpl.authenticate(this.userInfo, this.remoteRequest.getPeerInfo());
            AuthenticatedSubject var2 = SecurityServiceManager.getASFromAU(var5);
            SecurityServiceManager.runAs(BootServicesImpl.kernelId, var2, new PrivilegedExceptionAction() {
               public Object run() throws IOException {
                  T3ClientParams var1;
                  try {
                     BootServicesClientContextRequest.this.bootServicesImpl.checkServerLock(var5);
                     ClientContext var2 = ClientContext.getClientContext(BootServicesClientContextRequest.this.remoteRequest.getOrigin(), BootServicesClientContextRequest.this.workSpace, var5, BootServicesClientContextRequest.this.idleCallbackID, BootServicesClientContextRequest.this.qos);
                     var1 = var2.getParams();
                  } catch (T3Exception var3) {
                     throw new RemoteException("Failed to create client context", var3);
                  }

                  ReplyStream var4 = BootServicesClientContextRequest.this.remoteRequest.getResponseStream();
                  var4.writeObjectWL(var1);
                  var4.send();
                  return null;
               }
            });
         } catch (Throwable var4) {
            Throwable var1 = var4;
            if (Kernel.DEBUG && Kernel.getDebug().getDebugMessaging()) {
               RJVMLogger.logDebug2("Execute problem", var4);
            }

            try {
               this.remoteRequest.getResponseStream().sendThrowable(var1);
            } catch (IOException var3) {
               RJVMLogger.logDebug2("Failed to deliver error response " + var4 + " to client", var3);
            }

            if (var4 instanceof Error) {
               throw (Error)var4;
            }
         }

      }
   }

   private static class BootServicesAuthenticateRequest extends WorkAdapter {
      BootServicesImpl bootServicesImpl;
      RemoteRequest remoteRequest;
      UserInfo userInfo;
      PeerInfo peerInfo;

      public BootServicesAuthenticateRequest(BootServicesImpl var1, RemoteRequest var2, UserInfo var3, PeerInfo var4) {
         this.bootServicesImpl = var1;
         this.remoteRequest = var2;
         this.userInfo = var3;
         this.peerInfo = var4;
      }

      public void run() {
         try {
            AuthenticatedUser var5 = this.bootServicesImpl.authenticate(this.userInfo, this.peerInfo);
            ReplyStream var2 = this.remoteRequest.getResponseStream();
            var2.writeObject(var5);
            var2.send();
         } catch (Throwable var4) {
            Throwable var1 = var4;
            if (Kernel.DEBUG && Kernel.getDebug().getDebugMessaging()) {
               RJVMLogger.logDebug2("Execute problem ", var4);
            }

            try {
               this.remoteRequest.getResponseStream().sendThrowable(var1);
            } catch (IOException var3) {
               RJVMLogger.logDebug2("Failed to deliver error response " + var4 + " to client", var3);
            }

            if (var4 instanceof Error) {
               throw (Error)var4;
            }
         }

      }
   }
}
