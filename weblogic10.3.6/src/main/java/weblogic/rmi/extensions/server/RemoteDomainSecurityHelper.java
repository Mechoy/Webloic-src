package weblogic.rmi.extensions.server;

import java.io.IOException;
import java.rmi.RemoteException;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import javax.resource.spi.security.PasswordCredential;
import weblogic.management.configuration.SecurityConfigurationMBean;
import weblogic.management.provider.ManagementService;
import weblogic.protocol.LocalServerIdentity;
import weblogic.protocol.ProtocolManager;
import weblogic.protocol.ServerIdentity;
import weblogic.rjvm.RJVM;
import weblogic.rmi.extensions.RemoteHelper;
import weblogic.rmi.spi.EndPoint;
import weblogic.rmi.spi.HostID;
import weblogic.rmi.spi.RMIRuntime;
import weblogic.security.SubjectUtils;
import weblogic.security.acl.DefaultUserInfoImpl;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.acl.internal.AuthenticatedUser;
import weblogic.security.acl.internal.Security;
import weblogic.security.service.AdminResource;
import weblogic.security.service.AuthorizationManager;
import weblogic.security.service.ContextHandler;
import weblogic.security.service.CredentialManager;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.RemoteResource;
import weblogic.security.service.SecurityServiceManager;
import weblogic.security.service.SecurityService.ServiceType;

public final class RemoteDomainSecurityHelper {
   private static final boolean DEBUG = Boolean.getBoolean("weblogic.debug.DebugCrossDomainSecurity");
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final String CROSS_DOMAIN_PROTOCOL = "cross-domain-protocol";
   private static final String CROSS_DOMAIN_USER = "cross-domain";
   private static final String CROSS_DOMAIN_ADMIN_RESOURCE = "CrossDomain";
   public static final int ACCEPT_CALL = 0;
   public static final int REJECT_CALL = 1;
   public static final int UNDETERMINABLE = 2;

   private static boolean isCDSEnabled() {
      return RemoteDomainSecurityHelper.SINGLETON.secConfig.isCrossDomainSecurityEnabled();
   }

   public static AuthenticatedSubject getSubject(String var0) throws IOException, RemoteException {
      if (isCDSEnabled() && isRemoteDomain(var0)) {
         final String var1 = convertAdminProtocol(var0);
         EndPoint var2 = null;
         if (RMIRuntime.supportServerURL(var1)) {
            try {
               var2 = (EndPoint)SecurityServiceManager.runAs(kernelId, SubjectUtils.getAnonymousSubject(), new PrivilegedExceptionAction() {
                  public Object run() throws IOException {
                     return RMIRuntime.findOrCreateEndPoint(var1);
                  }
               });
               return getSubjectInternal(var2);
            } catch (Exception var4) {
            }
         }

         return null;
      } else {
         return null;
      }
   }

   private static String convertAdminProtocol(String var0) {
      String var1 = var0;

      try {
         int var2 = var0.indexOf(58);
         String var3 = var0.substring(0, var2);
         if ("admin".equals(var3)) {
            var1 = ProtocolManager.getDefaultAdminProtocol().getProtocolName() + var0.substring(var2);
         }

         return var1;
      } catch (IndexOutOfBoundsException var4) {
         throw new AssertionError("unsupported protocol " + var0);
      }
   }

   public static AuthenticatedSubject getSubject(Object var0) throws RemoteException, IllegalArgumentException {
      if (!isCDSEnabled()) {
         return null;
      } else {
         AuthenticatedSubject var1 = getSubjectInternal(RemoteHelper.getEndPoint(var0));
         if (DEBUG) {
            debug("getSubject for  " + var0 + " returned:" + var1);
         }

         return var1;
      }
   }

   public static AuthenticatedSubject getSubject(EndPoint var0) throws RemoteException {
      if (!isCDSEnabled()) {
         return null;
      } else {
         AuthenticatedSubject var1 = getSubjectInternal(var0);
         if (DEBUG) {
            debug("getSubject for  " + var0 + " returned:" + var1);
         }

         return var1;
      }
   }

   private static AuthenticatedSubject getSubjectInternal(EndPoint var0) throws RemoteException {
      HostID var1 = var0.getHostID();
      if (!(var1 instanceof ServerIdentity)) {
         return null;
      } else {
         String var2 = ((ServerIdentity)var1).getDomainName();
         if (var2 != null && !isDomainExcluded(var2)) {
            PasswordCredential var3 = getCredentials(var2);
            if (DEBUG) {
               debug("getCredentials() returned " + var3);
            }

            if (var3 == null) {
               return null;
            } else {
               AuthenticatedSubject var4 = SecurityServiceManager.getASFromAU(authenticate(var0, var3));
               if (DEBUG) {
                  debug("authenticate returned " + var4);
               }

               return var4;
            }
         } else {
            return null;
         }
      }
   }

   private static AuthenticatedUser authenticate(EndPoint var0, PasswordCredential var1) throws RemoteException {
      if (var1 == null) {
         return null;
      } else {
         DefaultUserInfoImpl var2 = new DefaultUserInfoImpl(var1.getUserName(), new String(var1.getPassword()));
         if (DEBUG) {
            debug(var1.getUserName() + " - " + new String(var1.getPassword()));
         }

         return Security.authenticate(var2, (RJVM)var0, ProtocolManager.getDefaultProtocol(), (String)null);
      }
   }

   private static PasswordCredential getCredentials(String var0) {
      CredentialManager var1 = (CredentialManager)SecurityServiceManager.getSecurityService(kernelId, "weblogicDEFAULT", ServiceType.CREDENTIALMANAGER);
      AuthenticatedSubject var2 = SecurityServiceManager.getCurrentSubject(kernelId);
      if (DEBUG) {
         debug("current subject=" + var2 + ", domainName=" + var0);
      }

      RemoteResource var3 = new RemoteResource("cross-domain-protocol", var0, (String)null, (String)null, (String)null);
      Object[] var4 = var1.getCredentials(kernelId, "cross-domain", var3, (ContextHandler)null, "weblogic.UserPassword");
      if (DEBUG) {
         debug("got mappings=" + var4);
      }

      if (var4 == null) {
         return null;
      } else {
         if (DEBUG) {
            debug("got mappings length=" + var4.length);
         }

         for(int var5 = 0; var5 < var4.length; ++var5) {
            Object var6 = var4[var5];
            if (var6 instanceof PasswordCredential) {
               if (DEBUG) {
                  debug("cred=" + var6);
               }

               return (PasswordCredential)var6;
            }
         }

         if (DEBUG) {
            debug("found no password credential !");
         }

         return null;
      }
   }

   public static int acceptRemoteDomainCall(HostID var0, AuthenticatedSubject var1) {
      if (!isCDSEnabled()) {
         if (DEBUG) {
            debug("acceptRemoteDomainCall for " + var1 + "= No CDS");
         }

         return 2;
      } else if (!(var0 instanceof ServerIdentity)) {
         if (DEBUG) {
            debug("acceptRemoteDomainCall for " + var1 + "= Not ServerIdentity" + var0);
         }

         return 2;
      } else {
         String var2 = ((ServerIdentity)var0).getDomainName();
         if (var2 != null && !LocalServerIdentity.getIdentity().getDomainName().equals(var2) && !isDomainExcluded(var2)) {
            AdminResource var3 = new AdminResource("CrossDomain", (String)null, (String)null);
            String var4 = "weblogicDEFAULT";
            AuthorizationManager var5 = (AuthorizationManager)SecurityServiceManager.getSecurityService(kernelId, var4, ServiceType.AUTHORIZE);
            boolean var6 = var5.isAccessAllowed(var1, var3, (ContextHandler)null);
            if (DEBUG) {
               debug("acceptRemoteDomainCall for " + var1 + "=" + var6);
            }

            return var6 ? 0 : 1;
         } else {
            if (DEBUG) {
               debug("acceptRemoteDomainCall for " + var1 + "= UNDETERMINABLE");
            }

            return 2;
         }
      }
   }

   public static boolean isRemoteDomain(String var0) throws IOException, RemoteException {
      if (DEBUG) {
         debug("[RemoteDomainSecurityHelper] isRemoteDomain: url= " + var0);
      }

      if (var0 != null && var0.length() != 0) {
         String var1 = convertAdminProtocol(var0);
         boolean var2 = false;
         if (RMIRuntime.supportServerURL(var1)) {
            var2 = isRemoteDomain(RMIRuntime.findOrCreateEndPoint(var1));
         }

         if (DEBUG) {
            debug("[RemoteDomainSecurityHelper] isRemoteDomain: url= " + var1 + " isRemote==" + var2);
         }

         return var2;
      } else {
         return false;
      }
   }

   public static boolean isRemoteDomain(EndPoint var0) {
      HostID var1 = var0.getHostID();
      if (!(var1 instanceof ServerIdentity)) {
         return false;
      } else {
         String var2 = ((ServerIdentity)var1).getDomainName();
         if (var2 != null && !RemoteDomainSecurityHelper.SINGLETON.localName.equals(var2)) {
            if (DEBUG) {
               debug("[RemoteDomainSecurityHelper] isRemoteDomain: TRUE remote domainName= " + var2 + "!=" + RemoteDomainSecurityHelper.SINGLETON.localName);
            }

            return true;
         } else {
            if (DEBUG) {
               debug("[RemoteDomainSecurityHelper] isRemoteDomain: FALSE remote domainName= " + var2 + "==" + RemoteDomainSecurityHelper.SINGLETON.localName);
            }

            return false;
         }
      }
   }

   private static boolean isDomainExcluded(String var0) {
      if (var0 == null) {
         return false;
      } else {
         String[] var1 = RemoteDomainSecurityHelper.SINGLETON.secConfig.getExcludedDomainNames();
         if (var1 == null) {
            return false;
         } else {
            for(int var2 = 0; var2 < var1.length; ++var2) {
               if (var0.equals(var1[var2])) {
                  return true;
               }
            }

            return false;
         }
      }
   }

   private static void debug(String var0) {
      if (DEBUG) {
         System.out.println("[RemoteDomainSecurityHelper] " + var0);
      }

   }

   private static class SINGLETON {
      static SecurityConfigurationMBean secConfig;
      static String localName;

      static {
         secConfig = ManagementService.getRuntimeAccess(RemoteDomainSecurityHelper.kernelId).getDomain().getSecurityConfiguration();
         localName = LocalServerIdentity.getIdentity().getDomainName();
      }
   }
}
