package weblogic.cluster.replication;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.rmi.RemoteException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import javax.naming.NamingException;
import weblogic.cluster.ClusterLogger;
import weblogic.jndi.Environment;
import weblogic.rmi.extensions.PortableRemoteObject;
import weblogic.rmi.extensions.server.RemoteDomainSecurityHelper;
import weblogic.security.SubjectUtils;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityManager;
import weblogic.security.service.SecurityServiceManager;

public class SecureReplicationInvocationHandler implements InvocationHandler {
   private static final Class[] interfaces = new Class[]{ReplicationServicesInternal.class};
   private static final AuthenticatedSubject KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private final ReplicationServicesInternal delegate;
   private final boolean useSecuredChannel;
   private final String url;
   private AuthenticatedSubject cachedSubject;

   public static ReplicationServicesInternal lookupService(final String var0, final String var1, final int var2, final Class var3, boolean var4) throws NamingException {
      ReplicationServicesInternal var5 = null;
      AuthenticatedSubject var6 = getSubjectForReplicationCalls(var0, var4);

      try {
         var5 = (ReplicationServicesInternal)SecurityManager.runAs(KERNEL_ID, var6, new PrivilegedExceptionAction() {
            public Object run() throws Exception {
               Environment var1x = new Environment();
               var1x.setProviderUrl(var0);
               var1x.setProviderChannel(var1);
               if (var2 >= 0) {
                  var1x.setRequestTimeout((long)var2);
               }

               return (ReplicationServicesInternal)PortableRemoteObject.narrow(var1x.getInitialReference(var3), ReplicationServicesInternal.class);
            }
         });
         return makeSecureService(var5, var4, var0);
      } catch (PrivilegedActionException var9) {
         NamingException var8 = new NamingException(var9.getMessage());
         var8.setRootCause(var9.getCause());
         throw var8;
      }
   }

   public static void checkPriviledges(AuthenticatedSubject var0, boolean var1) throws SecurityException {
      if (var1) {
         checkPriviledgesForSecuredChannel(var0);
      } else {
         checkPriviledgesForUnsecuredChannel(var0);
      }

   }

   private static ReplicationServicesInternal makeSecureService(ReplicationServicesInternal var0, boolean var1, String var2) {
      return (ReplicationServicesInternal)Proxy.newProxyInstance(var0.getClass().getClassLoader(), interfaces, new SecureReplicationInvocationHandler(var0, var1, var2));
   }

   private static AuthenticatedSubject getSubjectForReplicationCalls(String var0, boolean var1) {
      try {
         AuthenticatedSubject var2 = RemoteDomainSecurityHelper.getSubject(var0);
         if (var2 != null) {
            return var2;
         }
      } catch (IOException var3) {
      }

      return var1 ? KERNEL_ID : getCurrentSubject();
   }

   private AuthenticatedSubject getSubjectForReplicationCalls() {
      AuthenticatedSubject var1 = this.cachedSubject;
      if (var1 == null) {
         try {
            var1 = RemoteDomainSecurityHelper.getSubject(this.url);
         } catch (IOException var3) {
         }

         if (var1 != null) {
            this.cachedSubject = var1;
         }
      }

      return var1 != null ? var1 : (this.useSecuredChannel ? KERNEL_ID : getCurrentSubject());
   }

   private static AuthenticatedSubject getCurrentSubject() {
      AuthenticatedSubject var0 = SecurityServiceManager.getCurrentSubject(KERNEL_ID);
      return SubjectUtils.isUserAnAdministrator(var0) ? SubjectUtils.getAnonymousSubject() : var0;
   }

   private static void checkPriviledgesForSecuredChannel(AuthenticatedSubject var0) throws SecurityException {
      if (!SubjectUtils.isUserAnAdministrator(var0)) {
         ClusterLogger.logWrongPriviledgesForReplicationCalls("users with Admin priviledges", "secured");
         SecurityException var1 = new SecurityException("Insufficient priviledges for doing replication.");
         throw var1;
      }
   }

   private static void checkPriviledgesForUnsecuredChannel(AuthenticatedSubject var0) throws SecurityException {
      if (SubjectUtils.isUserAnAdministrator(var0)) {
         ClusterLogger.logWrongPriviledgesForReplicationCalls("users without Admin priviledges", "unsecured");
         SecurityException var1 = new SecurityException("Insufficient priviledges for doing replication.");
         throw var1;
      }
   }

   private SecureReplicationInvocationHandler(ReplicationServicesInternal var1, boolean var2, String var3) {
      this.delegate = var1;
      this.useSecuredChannel = var2;
      this.url = var3;
   }

   public Object invoke(Object var1, Method var2, Object[] var3) throws Throwable {
      ReplicationServicesInvocationAction var4 = new ReplicationServicesInvocationAction(this.delegate, var2, var3);
      AuthenticatedSubject var5 = this.getSubjectForReplicationCalls();

      try {
         Object var6 = SecurityServiceManager.runAs(KERNEL_ID, var5, var4);
         if (var4.getInvocationException() != null) {
            throw var4.getInvocationException();
         } else {
            return var6;
         }
      } catch (InvocationTargetException var7) {
         if (var7.getCause() != null) {
            throw var7.getCause();
         } else {
            throw new RemoteException(var7.getMessage());
         }
      }
   }

   public class ReplicationServicesInvocationAction implements PrivilegedAction {
      private Object targetObject;
      private Method targetMethod;
      private Object[] targetArgs;
      private Throwable exception = null;

      public ReplicationServicesInvocationAction(Object var2, Method var3, Object[] var4) {
         this.targetObject = var2;
         this.targetMethod = var3;
         this.targetArgs = var4;
      }

      public Object run() {
         try {
            return this.targetMethod.invoke(this.targetObject, this.targetArgs);
         } catch (Throwable var2) {
            this.exception = var2;
            return null;
         }
      }

      public Throwable getInvocationException() {
         return this.exception;
      }
   }
}
