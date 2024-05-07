package weblogic.protocol;

import java.net.MalformedURLException;
import java.security.AccessController;
import java.util.ArrayList;
import javax.security.auth.login.LoginException;
import weblogic.jndi.Environment;
import weblogic.jndi.internal.ThreadEnvironment;
import weblogic.management.configuration.ClusterMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.MachineMBean;
import weblogic.management.configuration.NodeManagerMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.provider.ManagementService;
import weblogic.security.SimpleCallbackHandler;
import weblogic.security.SubjectUtils;
import weblogic.security.acl.DefaultUserInfoImpl;
import weblogic.security.acl.UserInfo;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrincipalAuthenticator;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.security.service.SecurityService.ServiceType;
import weblogic.utils.StringUtils;

public class ClusterURLImpl implements ClusterURL {
   private static final String CLUSTER_URL_PREFIX = "cluster:";
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public String parseClusterURL(String var1) throws MalformedURLException {
      if (!var1.startsWith("cluster:")) {
         return var1;
      } else {
         String var2 = this.extractProtocol(var1);
         String var3 = this.extractClusterName(var1);
         ClusterMBean var4 = this.getClusterMBean(var3);
         if (var4 == null) {
            throw new MalformedURLException("No cluster named '" + var3 + "' found.");
         } else {
            ServerMBean[] var5 = var4.getServers();
            if (var5 != null && var5.length != 0) {
               String var6 = var4.getClusterAddress();
               if (var6 != null && var6.length() != 0) {
                  return this.constructClusterAddress(var2, var5, var6);
               } else {
                  ArrayList var7 = this.buildListOfAddresses(var3, var5, var2.endsWith("s"));
                  String var8 = this.buildClusterAddressURL(var2, var3, var7);
                  return var8;
               }
            } else {
               throw new MalformedURLException("No servers configured in cluster: " + var3);
            }
         }
      }
   }

   protected String constructClusterAddress(String var1, ServerMBean[] var2, String var3) {
      boolean var4 = var1.endsWith("s");
      String[] var5 = StringUtils.splitCompletely(var3, ",", false);
      String var6 = var1 != null ? var1 : "t3";
      String var7 = var6 + "://" + var3;
      if (var5.length == 1) {
         if (var3.contains(":")) {
            return var7;
         }

         int var8 = var2[0].getListenPort();
         if (var4) {
            DomainMBean var9 = ManagementService.getRuntimeAccess(kernelId).getDomain();
            if (var9.isAdministrationPortEnabled() && this.isUserAdministrator()) {
               var8 = var2[0].getAdministrationPort();
            } else if (var2[0].getSSL() != null && var2[0].getSSL().isListenPortEnabled()) {
               var8 = var2[0].getSSL().getListenPort();
            }
         }

         var7 = var7 + ":" + var8;
      }

      return var7;
   }

   protected ArrayList<String> buildListOfAddresses(String var1, ServerMBean[] var2, boolean var3) throws MalformedURLException {
      if (var2 != null && var2.length != 0) {
         DomainMBean var4 = ManagementService.getRuntimeAccess(kernelId).getDomain();
         ArrayList var5 = new ArrayList();

         for(int var6 = 0; var6 < var2.length; ++var6) {
            ServerMBean var7 = var2[var6];
            String var8 = null;
            var8 = var7.getListenAddress();
            if (var8 == null || var8.length() == 0) {
               MachineMBean var9 = var7.getMachine();
               if (var9 != null) {
                  NodeManagerMBean var10 = var9.getNodeManager();
                  if (var10 != null) {
                     var8 = var10.getListenAddress();
                  }
               }
            }

            if (var8 != null) {
               int var12 = var7.getListenPort();
               if (var3) {
                  if (var4.isAdministrationPortEnabled() && this.isUserAdministrator()) {
                     var12 = var7.getAdministrationPort();
                  } else {
                     if (var7.getSSL() == null || !var7.getSSL().isListenPortEnabled()) {
                        continue;
                     }

                     var12 = var7.getSSL().getListenPort();
                  }
               }

               StringBuilder var11 = new StringBuilder();
               var11.append(var8);
               var11.append(":");
               var11.append(var12);
               var5.add(var11.toString());
            }
         }

         return var5;
      } else {
         throw new MalformedURLException("No servers configured in cluster: " + var1);
      }
   }

   protected String buildClusterAddressURL(String var1, String var2, ArrayList<String> var3) throws MalformedURLException {
      if (var3 != null && !var3.isEmpty()) {
         StringBuilder var4 = new StringBuilder();
         var4.append(var1);
         var4.append("://");

         for(int var5 = 0; var5 < var3.size(); ++var5) {
            if (var5 > 0) {
               var4.append(',');
            }

            var4.append((String)var3.get(var5));
         }

         return var4.toString();
      } else {
         throw new MalformedURLException("Unable to construct proper cluster address URL since no listening addresses found for any configured server(s) in cluster: " + var2);
      }
   }

   protected String extractClusterName(String var1) {
      int var2 = var1.lastIndexOf("://");
      String var3 = var1.substring(var2 + 3, var1.length());
      return var3;
   }

   protected String extractProtocol(String var1) {
      int var2 = var1.indexOf(58);
      int var3 = var1.indexOf("://");
      String var4 = var1.substring(var2 + 1, var3);
      return var4;
   }

   protected ClusterMBean getClusterMBean(String var1) {
      DomainMBean var2 = ManagementService.getRuntimeAccess(kernelId).getDomain();
      ClusterMBean var3 = var2.lookupCluster(var1);
      return var3;
   }

   protected boolean isUserAdministrator() {
      Environment var1 = ThreadEnvironment.get();
      UserInfo var2 = var1.getSecurityUser();
      if (var2 != null) {
         try {
            AuthenticatedSubject var3 = authenticateLocally(var2);
            if (var3 != null) {
               return SubjectUtils.isUserAnAdministrator(var3);
            }
         } catch (LoginException var4) {
         }
      }

      return false;
   }

   private static AuthenticatedSubject authenticateLocally(UserInfo var0) throws LoginException {
      String var1 = "weblogicDEFAULT";
      PrincipalAuthenticator var2 = (PrincipalAuthenticator)SecurityServiceManager.getSecurityService(kernelId, var1, ServiceType.AUTHENTICATION);
      AuthenticatedSubject var3 = null;
      if (var0 instanceof DefaultUserInfoImpl) {
         DefaultUserInfoImpl var4 = (DefaultUserInfoImpl)var0;
         SimpleCallbackHandler var5 = new SimpleCallbackHandler(var4.getName(), var4.getPassword());
         var3 = var2.authenticate(var5);
      }

      return var3;
   }
}
