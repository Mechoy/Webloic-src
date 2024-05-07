package weblogic.wsee.security.serviceref;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.Principal;
import java.util.Iterator;
import java.util.Set;
import javax.xml.rpc.handler.MessageContext;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.ContextHandler;
import weblogic.security.service.CredentialManager;
import weblogic.security.service.RemoteResource;
import weblogic.security.service.SecurityService;
import weblogic.security.service.SecurityServiceManager;
import weblogic.security.service.SecurityService.ServiceType;
import weblogic.security.spi.Resource;
import weblogic.security.spi.WLSUser;
import weblogic.xml.crypto.utils.LogUtils;
import weblogic.xml.crypto.wss.SecurityImpl;
import weblogic.xml.crypto.wss.WSSecurityContext;

public class ServiceRefUtils {
   private static boolean VERBOSE;
   public static final String END_POINT_ADDRESS = "weblogic.wsee.connection.end_point_address";

   public static Object getCredential(AuthenticatedSubject var0, String var1, String var2, ContextHandler var3) {
      String var4 = getPrincipalName(var0, var3, var1);
      if (var4 == null) {
         return null;
      } else {
         Resource var5 = getResource(var3);
         if (var5 == null) {
            return null;
         } else {
            CredentialManager var6 = getCredentialManager(var0);
            Object[] var7 = var6.getCredentials(var0, var4, var5, (ContextHandler)null, var1);
            if (var7.length < 1) {
               AuthenticatedSubject var8 = SecurityServiceManager.getCurrentSubject(var0);
               var7 = var6.getCredentials(var0, var8, var5, (ContextHandler)null, var1);
               if (var7.length < 1) {
                  LogUtils.logWss("No credentials found for principal name " + var4 + " and remote resource " + var5);
                  return null;
               }
            }

            LogUtils.logWss("Got credentials for principal name " + var4 + " and remote resource " + var5);
            return var7[0];
         }
      }
   }

   private static CredentialManager getCredentialManager(AuthenticatedSubject var0) {
      SecurityService.ServiceType var1 = ServiceType.CREDENTIALMANAGER;
      String var2 = SecurityServiceManager.getDefaultRealmName();
      CredentialManager var3 = (CredentialManager)SecurityServiceManager.getSecurityService(var0, var2, var1);
      return var3;
   }

   private static String getPrincipal(AuthenticatedSubject var0) {
      AuthenticatedSubject var1 = SecurityServiceManager.getCurrentSubject(var0);
      return getName(var1);
   }

   private static String getName(AuthenticatedSubject var0) {
      Set var1 = var0.getPrincipals(WLSUser.class);
      String var2 = null;
      Iterator var3 = var1.iterator();
      if (var3.hasNext()) {
         Principal var4 = (Principal)var3.next();
         var2 = var4.getName();
      }

      return var2;
   }

   private static Resource getResource(ContextHandler var0) {
      WSSecurityContext var1 = (WSSecurityContext)var0.getValue("com.bea.contextelement.xml.SecurityInfo");
      MessageContext var2 = var1.getMessageContext();
      String var3 = (String)var2.getProperty("weblogic.wsee.security.wss.end_point_url");
      if (var3 != null) {
         LogUtils.logWss("Endpoint address from message context property weblogic.wsee.security.wss.end_point_url : " + var3);
      }

      if (var3 == null) {
         var3 = (String)var2.getProperty("weblogic.wsee.connection.end_point_address");
         if (var3 != null) {
            LogUtils.logWss("Endpoint address from message context property weblogic.wsee.connection.end_point_address : " + var3);
         }
      }

      URL var4 = null;

      try {
         var4 = new URL(var3);
      } catch (MalformedURLException var6) {
         LogUtils.logWss("Could not create RemoteResource, endpoint address: " + var3 + ", " + var6);
         return null;
      }

      return new RemoteResource(var4.getProtocol(), var4.getHost(), String.valueOf(var4.getPort()), var4.getPath(), (String)null);
   }

   private static String getPrincipalName(AuthenticatedSubject var0, ContextHandler var1, String var2) {
      String var3 = null;
      if ("weblogic.pki.Keypair".equals(var2) || "weblogic.pki.TrustedCertificate".equals(var2)) {
         var3 = (String)var1.getValue("weblogic.xml.crypto.wss.PKI_Initiator");
      }

      LogUtils.logWss("Principal name from context handler: " + var3);
      if (var3 == null) {
         var3 = getPrincipal(var0);
         LogUtils.logWss("Principal name from subject on thread:" + var3);
      }

      if (var3 == null) {
         AuthenticatedSubject var4 = (AuthenticatedSubject)var1.getValue("weblogic.wsee.wss.subject");
         if (var4 != null) {
            var3 = getName(var4);
         }

         LogUtils.logWss("Principal name from security context:" + var3);
      }

      return var3;
   }

   static {
      VERBOSE = SecurityImpl.VERBOSE;
   }
}
