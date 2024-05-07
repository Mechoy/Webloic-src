package weblogic.iiop;

import java.rmi.Remote;
import java.security.AccessController;
import java.util.concurrent.ConcurrentHashMap;
import javax.security.auth.login.LoginException;
import weblogic.common.internal.PeerInfo;
import weblogic.kernel.Kernel;
import weblogic.management.provider.ManagementService;
import weblogic.protocol.AsyncMessageSender;
import weblogic.protocol.ServerChannel;
import weblogic.rmi.spi.Channel;
import weblogic.security.SimpleCallbackHandler;
import weblogic.security.SubjectUtils;
import weblogic.security.acl.UserInfo;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.ContextHandler;
import weblogic.security.service.PrincipalAuthenticator;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.security.service.SecurityService.ServiceType;

public abstract class Connection implements AsyncMessageSender, EndPointFactory {
   public static final String CONN_BIDIR_KEYS = "weblogic.iiop.BiDirKeys";
   public static final int SENT_CODE_SET = 1;
   public static final int SENT_CODEBASE = 2;
   public static final int SENT_VERSION = 4;
   public static final int SENT_BIDIR = 8;
   public static final int CONN_NEGOTIATED = 16;
   public static final int RECV_CODE_SET = 32;
   public static final int RECV_BIDIR = 64;
   private int flags = 0;
   private Remote hbStub;
   private int minorVersion;
   private PeerInfo peerinfo = null;
   private int char_codeset = CodeSet.getDefaultCharCodeSet();
   private int wchar_codeset = CodeSet.getDefaultWcharCodeSet();
   private IOR remoteCodeBase = null;
   private static AuthenticatedSubject defaultSubject = null;
   private ConcurrentHashMap properties = new ConcurrentHashMap();

   public abstract void close();

   public abstract boolean isClosed();

   public boolean isDead() {
      return false;
   }

   public abstract ConnectionKey getConnectionKey();

   public abstract void setConnectionKey(ConnectionKey var1);

   final void setHeartbeatStub(Remote var1) {
      this.hbStub = var1;
   }

   protected final Remote getHeartbeatStub() {
      return this.hbStub;
   }

   public abstract AuthenticatedSubject getUser();

   public abstract void authenticate(UserInfo var1);

   public abstract Object getTxContext();

   public abstract void setTxContext(Object var1);

   public abstract ServerChannel getChannel();

   public abstract Channel getRemoteChannel();

   public boolean isStateful() {
      return true;
   }

   public int getMinorVersion() {
      return this.minorVersion;
   }

   public void setMinorVersion(int var1) {
      this.minorVersion = var1;
   }

   public void setFlag(int var1) {
      this.flags |= var1;
   }

   public boolean getFlag(int var1) {
      return (this.flags & var1) != 0;
   }

   public int getWcharCodeSet() {
      return this.wchar_codeset;
   }

   public int getCharCodeSet() {
      return this.char_codeset;
   }

   public void setCodeSets(int var1, int var2) {
      this.char_codeset = var1;
      this.wchar_codeset = var2;
   }

   public IOR getRemoteCodeBase() {
      return this.remoteCodeBase;
   }

   public void setRemoteCodeBase(IOR var1) {
      this.remoteCodeBase = var1;
   }

   public PeerInfo getPeerInfo() {
      return this.peerinfo;
   }

   public void setPeerInfo(PeerInfo var1) {
      this.peerinfo = var1;
   }

   public Object getProperty(String var1) {
      return this.properties.get(var1);
   }

   public void setProperty(String var1, Object var2) {
      this.properties.put(var1, var2);
   }

   protected abstract boolean isSecure();

   public ContextHandler getContextHandler() {
      return null;
   }

   public static AuthenticatedSubject getDefaultSubject() {
      if (defaultSubject != null) {
         return defaultSubject;
      } else if (!Kernel.isServer()) {
         return SubjectUtils.getAnonymousSubject();
      } else {
         Class var0 = MuxableSocketIIOP.class;
         synchronized(MuxableSocketIIOP.class) {
            if (defaultSubject == null) {
               AuthenticatedSubject var1 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
               if (ManagementService.getRuntimeAccess(var1).getServer().getDefaultIIOPUser() == null) {
                  defaultSubject = SubjectUtils.getAnonymousSubject();
               } else {
                  try {
                     SimpleCallbackHandler var2 = new SimpleCallbackHandler(ManagementService.getRuntimeAccess(var1).getServer().getDefaultIIOPUser(), ManagementService.getRuntimeAccess(var1).getServer().getDefaultIIOPPassword());
                     defaultSubject = getPrincipalAuthenticator().authenticate(var2);
                  } catch (LoginException var4) {
                     defaultSubject = SubjectUtils.getAnonymousSubject();
                  }
               }
            }
         }

         return defaultSubject;
      }
   }

   protected static PrincipalAuthenticator getPrincipalAuthenticator() {
      return Connection.PrincipalAuthenticatorMaker.AUTHENTICATOR;
   }

   public static boolean isValidDefaultUser() {
      return getDefaultSubject() != null;
   }

   private static class PrincipalAuthenticatorMaker {
      private static final PrincipalAuthenticator AUTHENTICATOR;

      static {
         AUTHENTICATOR = (PrincipalAuthenticator)SecurityServiceManager.getSecurityService((AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction()), "weblogicDEFAULT", ServiceType.AUTHENTICATION);
      }
   }
}
