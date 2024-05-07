package weblogic.jndi;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.rmi.NoSuchObjectException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Hashtable;
import java.util.Map;
import javax.naming.CommunicationException;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.NoInitialContextException;
import javax.naming.spi.InitialContextFactory;
import javax.naming.spi.NamingManager;
import javax.security.auth.login.LoginException;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.jndi.internal.ExceptionTranslator;
import weblogic.jndi.internal.JNDIEnvironment;
import weblogic.jndi.internal.NamingDebugLogger;
import weblogic.jndi.internal.NamingNode;
import weblogic.jndi.internal.NamingNodeReplicaHandler;
import weblogic.jndi.internal.WLContextImpl;
import weblogic.jndi.internal.WLEventContextImpl;
import weblogic.jndi.internal.WLInternalContext;
import weblogic.jndi.spi.EnvironmentFactory;
import weblogic.kernel.KernelStatus;
import weblogic.protocol.LocalServerIdentity;
import weblogic.protocol.Protocol;
import weblogic.protocol.ProtocolManager;
import weblogic.protocol.ProtocolStack;
import weblogic.protocol.ServerIdentity;
import weblogic.rjvm.JVMID;
import weblogic.rjvm.RJVM;
import weblogic.rjvm.RJVMManager;
import weblogic.rjvm.ServerURL;
import weblogic.rmi.cluster.ClusterableRemoteRef;
import weblogic.rmi.cluster.ReplicaAwareInfo;
import weblogic.rmi.extensions.RemoteHelper;
import weblogic.rmi.extensions.StubFactory;
import weblogic.rmi.extensions.server.RemoteReference;
import weblogic.rmi.extensions.server.ServerHelper;
import weblogic.rmi.internal.BasicRemoteRef;
import weblogic.rmi.internal.ClientMethodDescriptor;
import weblogic.rmi.internal.ClientRuntimeDescriptor;
import weblogic.rmi.internal.StubInfo;
import weblogic.rmi.internal.StubInfoIntf;
import weblogic.rmi.spi.HostID;
import weblogic.rmi.utils.Utilities;
import weblogic.security.SimpleCallbackHandler;
import weblogic.security.SubjectUtils;
import weblogic.security.acl.DefaultUserInfoImpl;
import weblogic.security.acl.UserInfo;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.acl.internal.AuthenticatedUser;
import weblogic.security.acl.internal.RemoteAuthenticate;
import weblogic.security.service.PrincipalAuthenticator;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityManager;
import weblogic.security.service.SecurityServiceManager;
import weblogic.security.service.SecurityService.ServiceType;
import weblogic.utils.Debug;
import weblogic.utils.StackTraceUtils;

public class WLInitialContextFactoryDelegate implements InitialContextFactory, EnvironmentFactory {
   private static final DebugLogger debugConnection;
   private static final AuthenticatedSubject kernelId;
   private static Class preload;
   private static final String ROOT_NAMING_NODE_CLASS_NAME = "weblogic.jndi.internal.ServerNamingNode";
   private static NamingNode rootNode;
   private static final String[] ROOT_NODE_INTERFACES;
   private static final ClientMethodDescriptor DESC;
   private static final Class[] STUB_INFO_CLASS;
   private static final ClientRuntimeDescriptor ROOT_NODE_DESCRIPTOR;
   private static final ReplicaAwareInfo NAMING_NODE_RA_INFO;
   private static boolean keepEnvironmentUntilContextClose;

   public static final WLInitialContextFactoryDelegate theOne() {
      return WLInitialContextFactoryDelegate.SingletonMaker.singleton;
   }

   public WLInitialContextFactoryDelegate() {
      JNDIEnvironment.getJNDIEnvironment().prepareKernel();
      JNDIEnvironment.getJNDIEnvironment().prepReferenceHelper();
   }

   public final Context getInitialContext(Hashtable var1) throws NamingException {
      Environment var2 = new Environment(var1);

      try {
         return this.getInitialContext(var2, (String)null);
      } catch (RuntimeException var4) {
         throw var4;
      }
   }

   public final Context getInitialContext(final Environment var1, String var2, final HostID var3) throws NamingException {
      CommunicationException var4 = null;
      String var5 = var1.getProviderUrl();
      ServerIdentity var6 = var1.getProviderIdentity();
      byte var7;
      if (var6 != null) {
         var7 = 0;
      } else {
         var7 = 1;
      }

      int var8 = 0;

      while(var8 <= var7) {
         Object var9 = null;
         if (var6 != null) {
            var9 = var6;
         } else if (var5 == "local://") {
            var9 = LocalServerIdentity.getIdentity();
         } else {
            JNDIEnvironment.getJNDIEnvironment().pushThreadEnvironment(var1);

            try {
               final ServerURL var10 = new ServerURL(var1.getProviderUrl());
               if (var1.getSecurityPrincipal() == null && var1.getSecurityCredentials() == null) {
                  var9 = var10.findOrCreateRJVM(var1.getEnableServerAffinity(), var1.getProviderChannel(), var3, (int)var1.getRequestTimeout(), var1.getForceResolveDNSName()).getID();
               } else {
                  var9 = (ServerIdentity)SecurityManager.runAs(kernelId, SubjectUtils.getAnonymousSubject(), new PrivilegedExceptionAction() {
                     public Object run() throws Exception {
                        return var10.findOrCreateRJVM(var1.getEnableServerAffinity(), var1.getProviderChannel(), var3, (int)var1.getRequestTimeout(), var1.getForceResolveDNSName()).getID();
                     }
                  });
               }

               if (NamingDebugLogger.isDebugEnabled()) {
                  boolean var11 = debugConnection.isDebugEnabled();
                  if (var11) {
                     NamingDebugLogger.debug("Bootstrapping context from DNS " + var5);
                  }
               }
            } catch (PrivilegedActionException var18) {
               throw this.toNamingException(var18.getException());
            } catch (Exception var19) {
               throw this.toNamingException(var19);
            } finally {
               JNDIEnvironment.getJNDIEnvironment().popThreadEnvironment();
            }
         }

         try {
            return this.newContext((ServerIdentity)var9, var1, var2);
         } catch (CommunicationException var21) {
            var4 = var21;
            ++var8;
         }
      }

      throw var4;
   }

   public Remote getInitialReference(Environment var1, Class var2) throws NamingException {
      String var3 = var1.getProviderUrl();
      Object var4 = var1.getProviderIdentity();
      if (var4 == null) {
         if (var3 == "local://") {
            var4 = LocalServerIdentity.getIdentity();
         } else {
            JNDIEnvironment.getJNDIEnvironment().pushThreadEnvironment(var1);

            try {
               var4 = (new ServerURL(var3)).findOrCreateRJVM(var1.getEnableServerAffinity(), var1.getProviderChannel(), (HostID)null, (int)var1.getRequestTimeout(), var1.getForceResolveDNSName()).getID();
               if (NamingDebugLogger.isDebugEnabled()) {
                  boolean var5 = debugConnection.isDebugEnabled();
                  if (var5) {
                     NamingDebugLogger.debug("Bootstrapping reference from DNS " + var3);
                  }
               }
            } catch (IOException var12) {
               throw this.toNamingException(var12);
            } finally {
               JNDIEnvironment.getJNDIEnvironment().popThreadEnvironment();
            }
         }
      }

      try {
         return var1.getRequestTimeout() > 0L ? (Remote)StubFactory.getStub(var2, (HostID)var4, var1.getProviderChannel(), this.getClientRuntimeDescriptor(var2, var1.getRequestTimeout())) : (Remote)StubFactory.getStub(var2, (HostID)var4, var1.getProviderChannel());
      } catch (RemoteException var11) {
         throw this.toNamingException(var11);
      }
   }

   public Context getInitialContext(Environment var1, String var2) throws NamingException {
      return this.getInitialContext(var1, var2, (HostID)null);
   }

   private ClientRuntimeDescriptor getClientRuntimeDescriptor(Class var1, long var2) {
      String[] var4 = Utilities.getRemoteInterfaceNames(var1);
      ClientMethodDescriptor var5 = new ClientMethodDescriptor("*", false, false, false, false, (int)var2);
      return (new ClientRuntimeDescriptor(var4, (String)null, (Map)null, var5, ServerHelper.getStubClassName(var1.getName()))).intern();
   }

   private final Context newContext(ServerIdentity var1, Environment var2, String var3) throws NamingException {
      Context var4 = null;
      JNDIEnvironment.getJNDIEnvironment().pushThreadEnvironment(var2);

      try {
         this.pushSubject(var2, var1);
         JNDIEnvironment.getJNDIEnvironment().activateTransactionHelper();

         try {
            if (var1.isLocal()) {
               var4 = this.newLocalContext(var2, var3);
            } else {
               var4 = this.newRemoteContext(var1, var2, var3);
            }

            if (var2.getSecurityUser() != null || var2.isClientCertAvailable() || var2.isLocalIdentitySet()) {
               ((WLInternalContext)var4).enableLogoutOnClose();
            }

            long var5 = var2.getRMIClientTimeout();
            if (var5 > 0L) {
               RemoteHelper.setClientTimeout(var5);
            }
         } catch (RemoteException var19) {
            throw this.toNamingException(var19);
         } catch (NamingException var20) {
            throw var20;
         } finally {
            if (var4 == null) {
               this.popSubject(var2);
               JNDIEnvironment.getJNDIEnvironment().deactivateTransactionHelper();
            }

         }
      } finally {
         if (!keepEnvironmentUntilContextClose) {
            JNDIEnvironment.getJNDIEnvironment().popThreadEnvironment();
         }

      }

      return var4;
   }

   private final Context newLocalContext(Environment var1, String var2) throws NamingException {
      Debug.assertion(Thread.currentThread().getContextClassLoader() != null, "ContextClassLoader == null");
      Hashtable var3 = var1.getDelegateEnvironment();
      if (var3 != null) {
         this.popSubject(var1);
         return NamingManager.getInitialContext(var3);
      } else {
         try {
            NamingNode var4 = this.getRootNode();
            WLEventContextImpl var5 = null;
            Hashtable var6 = var1.getProperties();
            var5 = new WLEventContextImpl(var6, var4);
            return (Context)(var2 != null && var2.length() != 0 ? (Context)var5.lookup(var2) : var5);
         } catch (NoSuchObjectException var7) {
            throw new NoInitialContextException("JNDI subsystem is not ready for use ");
         }
      }
   }

   private NamingNode getRootNode() throws NoSuchObjectException {
      if (rootNode == null) {
         rootNode = (NamingNode)ServerHelper.getRemoteObject(9);
      }

      return rootNode;
   }

   private final Context newRemoteContext(ServerIdentity var1, Environment var2, String var3) throws NamingException, RemoteException {
      Hashtable var4 = var2.getDelegateEnvironment();
      if (var4 != null) {
         return JNDIEnvironment.getJNDIEnvironment().getDelegateContext(var1, var2, var3);
      } else {
         BasicRemoteRef var5 = new BasicRemoteRef(9, var1, var2.getProviderChannel());
         NamingNode var6 = this.newRootNamingNodeStub(var5, (int)var2.getRequestTimeout());
         WLContextImpl var7 = new WLContextImpl(var2.getRemoteProperties(), var6);
         return (Context)(var3 != null && var3.length() != 0 ? (Context)var7.lookup(var3) : var7);
      }
   }

   private NamingNode newRootNamingNodeStub(RemoteReference var1, int var2) {
      ClientRuntimeDescriptor var3 = var2 == 0 ? ROOT_NODE_DESCRIPTOR : this.getClientRuntimeDescriptorWithTimeout(var2);
      ClusterableRemoteRef var4 = new ClusterableRemoteRef(var1);
      var4.initialize(NAMING_NODE_RA_INFO);
      StubInfo var5 = new StubInfo(var4, var3, ServerHelper.getStubClassName("weblogic.jndi.internal.ServerNamingNode"));

      try {
         Class var6 = Class.forName(ServerHelper.getStubClassName("weblogic.jndi.internal.ServerNamingNode"));
         Constructor var7 = var6.getConstructor(STUB_INFO_CLASS);
         return (NamingNode)var7.newInstance(var5);
      } catch (ClassNotFoundException var8) {
         throw new AssertionError(var8);
      } catch (Exception var9) {
         throw new AssertionError(var9);
      }
   }

   private ClientRuntimeDescriptor getClientRuntimeDescriptorWithTimeout(int var1) {
      ClientMethodDescriptor var2 = new ClientMethodDescriptor("*", false, false, false, false, var1);
      return new ClientRuntimeDescriptor(ROOT_NODE_INTERFACES, (String)null, (Map)null, var2, ServerHelper.getStubClassName("weblogic.jndi.internal.ServerNamingNode"));
   }

   private final void pushSubject(Environment var1, ServerIdentity var2) throws NamingException {
      Protocol var3 = null;

      try {
         var3 = this.getProtocol(var1);
         pushProtocol(var1, var3);
      } catch (MalformedURLException var12) {
         throw new AssertionError(var12);
      }

      Object var4 = var1.getSecurityUser();
      if (var4 == null && var3.isSecure() && (var1.isClientCertAvailable() || var1.isLocalIdentitySet())) {
         var4 = new DefaultUserInfoImpl((String)null, (Object)null);
      }

      Object var5 = null;
      boolean var6 = var2.isLocal();
      AuthenticatedSubject var7 = null;
      if (var4 != null) {
         if (var1.getSecuritySubject() != null) {
            var7 = var1.getSecuritySubject();
         } else if (var6) {
            try {
               var7 = this.authenticateLocally((UserInfo)var4);
            } catch (LoginException var11) {
               popProtocol(var1);
               throw this.toNamingException(var11);
            }
         } else {
            try {
               var7 = this.authenticateRemotely(var3, var1, (UserInfo)var4, var2);
            } catch (SecurityException var9) {
               popProtocol(var1);
               throw this.toNamingException(var9);
            } catch (RemoteException var10) {
               popProtocol(var1);
               throw this.toNamingException(var10);
            }
         }
      }

      if (var7 != null) {
         var1.setSecuritySubject(var7);
         JNDIEnvironment.getJNDIEnvironment().pushSubject(kernelId, var7);
      }

   }

   private AuthenticatedSubject authenticateLocally(UserInfo var1) throws LoginException {
      String var2 = "weblogicDEFAULT";
      PrincipalAuthenticator var3 = (PrincipalAuthenticator)SecurityServiceManager.getSecurityService(kernelId, var2, ServiceType.AUTHENTICATION);
      AuthenticatedSubject var4 = null;
      if (var1 instanceof DefaultUserInfoImpl) {
         DefaultUserInfoImpl var5 = (DefaultUserInfoImpl)var1;
         SimpleCallbackHandler var6 = new SimpleCallbackHandler(var5.getName(), var5.getPassword());
         var4 = var3.authenticate(var6);
      }

      return var4;
   }

   private Protocol getProtocol(Environment var1) throws MalformedURLException {
      String var2 = var1.getProviderUrl();
      Protocol var3 = null;
      if (var2 == "local://") {
         var3 = ProtocolManager.getProtocolByName("t3");
      } else {
         var3 = ProtocolManager.getProtocolByName((new ServerURL(var2)).getProtocol());
      }

      if (var3.isUnknown()) {
         throw new MalformedURLException("No support for protocol: " + var2);
      } else {
         return var3;
      }
   }

   private AuthenticatedSubject authenticateRemotely(Protocol var1, Environment var2, UserInfo var3, ServerIdentity var4) throws RemoteException {
      RJVM var5 = RJVMManager.getRJVMManager().findOrCreate((JVMID)var4);
      AuthenticatedUser var6 = RemoteAuthenticate.authenticate(var3, var5, var1, var2.getProviderChannel(), var2.getRequestTimeout(), var2.getEnableDefaultUser());
      var5.setUser(var6);
      return JNDIEnvironment.getJNDIEnvironment().getASFromAU(var6);
   }

   private void popSubject(Environment var1) {
      popProtocol(var1);
      if (var1.getSecuritySubject() != null) {
         SecurityServiceManager.popSubject(kernelId);
         JNDIEnvironment.getJNDIEnvironment().popSubject(kernelId);
      }

   }

   private static void pushProtocol(Environment var0, Protocol var1) {
      if (var0.getProviderUrl() != "local://") {
         ProtocolStack.push(var1);
      }

   }

   private static void popProtocol(Environment var0) {
      if (var0.getProviderUrl() != "local://") {
         ProtocolStack.pop();
      }

   }

   private final NamingException toNamingException(Throwable var1) {
      NamingException var2 = ExceptionTranslator.toNamingException(var1);
      if (NamingDebugLogger.isDebugEnabled()) {
         boolean var3 = debugConnection.isDebugEnabled();
         if (var3 && var2 instanceof CommunicationException) {
            String var4 = StackTraceUtils.throwable2StackTrace(var2.getRootCause());
            NamingDebugLogger.debug("Failed to create initial context due to: " + var4);
         }
      }

      return var2;
   }

   static {
      JNDIEnvironment.getJNDIEnvironment().prepareSubjectManager();
      debugConnection = DebugLogger.getDebugLogger("DebugConnection");
      kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
      preload = ExceptionTranslator.class;
      ROOT_NODE_INTERFACES = new String[]{NamingNode.class.getName(), StubInfoIntf.class.getName()};
      DESC = new ClientMethodDescriptor("*", false, false, false, false, 0);
      STUB_INFO_CLASS = new Class[]{StubInfo.class};
      ROOT_NODE_DESCRIPTOR = (new ClientRuntimeDescriptor(ROOT_NODE_INTERFACES, (String)null, (Map)null, DESC, ServerHelper.getStubClassName("weblogic.jndi.internal.ServerNamingNode"))).intern();
      NAMING_NODE_RA_INFO = new ReplicaAwareInfo("", NamingNodeReplicaHandler.class);
      keepEnvironmentUntilContextClose = false;
      if (KernelStatus.isServer()) {
         String var0 = System.getProperty("weblogic.jndi.retainenvironment");
         if (var0 != null) {
            keepEnvironmentUntilContextClose = true;
         }
      }

   }

   static class SingletonMaker {
      static final WLInitialContextFactoryDelegate singleton = new WLInitialContextFactoryDelegate();
   }
}
