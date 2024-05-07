package weblogic.management.servlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.rmi.RemoteException;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import javax.mail.internet.MimeUtility;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import weblogic.common.internal.PeerInfo;
import weblogic.common.internal.WLObjectOutputStream;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.logging.Loggable;
import weblogic.management.internal.BootStrapStruct;
import weblogic.management.internal.ConfigLogger;
import weblogic.management.provider.ManagementService;
import weblogic.protocol.ServerChannelManager;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.AuthorizationManager;
import weblogic.security.service.ContextHandler;
import weblogic.security.service.PrincipalAuthenticator;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.security.service.ServerResource;
import weblogic.security.service.SecurityService.ServiceType;

public final class BootstrapServlet extends HttpServlet implements PrivilegedExceptionAction {
   private static DebugLogger debugLogger = DebugLogger.getDebugLogger("DebugBootstrapServlet");
   private AuthorizationManager authorizer;
   private PrincipalAuthenticator authenticator;
   private AuthenticatedSubject kernelId;
   private ServletConfig config = null;

   public String getServletInfo() {
      return "Managed server bootstrap servlet";
   }

   public void init(ServletConfig var1) throws ServletException {
      this.config = var1;
      this.kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
      if (this.kernelId == null) {
         throw new ServletException("Security Services Unavailable");
      } else {
         String var2 = "weblogicDEFAULT";
         this.authenticator = (PrincipalAuthenticator)SecurityServiceManager.getSecurityService(this.kernelId, var2, ServiceType.AUTHENTICATION);
         this.authorizer = (AuthorizationManager)SecurityServiceManager.getSecurityService(this.kernelId, var2, ServiceType.AUTHORIZE);
         if (this.authenticator != null && this.authorizer != null) {
            try {
               SecurityServiceManager.runAs(this.kernelId, this.kernelId, this);
            } catch (PrivilegedActionException var4) {
               throw (ServletException)var4.getException();
            }

            if (debugLogger.isDebugEnabled()) {
               debugLogger.debug("BootstrapServlet initialized");
            }

         } else {
            throw new ServletException("Security Services Unavailable");
         }
      }
   }

   public void doPost(HttpServletRequest var1, HttpServletResponse var2) throws ServletException, IOException {
      this.doGet(var1, var2);
   }

   public void doGet(HttpServletRequest var1, HttpServletResponse var2) throws ServletException, IOException {
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("BootstrapServlet invoked");
      }

      this.processGet(var1, var2);
   }

   public void processGet(final HttpServletRequest var1, final HttpServletResponse var2) throws ServletException, IOException {
      try {
         SecurityServiceManager.runAs(this.kernelId, this.kernelId, new PrivilegedExceptionAction() {
            public Object run() throws IOException {
               WLObjectOutputStream var1x = null;
               ServletOutputStream var2x = null;
               PeerInfo var3 = BootstrapServlet.this.checkClientVersion(var1, var2);
               if (var3 == null) {
                  return null;
               } else {
                  String var4 = BootstrapServlet.mimeDecode(var1.getHeader("username"));
                  String var5 = BootstrapServlet.mimeDecode(var1.getHeader("password"));
                  String var6 = BootstrapServlet.mimeDecode(var1.getHeader("servername"));
                  Loggable var18;
                  if (var6.equals(ManagementService.getRuntimeAccess(BootstrapServlet.this.kernelId).getServerName())) {
                     var18 = ConfigLogger.logServerNameSameAsAdminLoggable(var6);
                     var18.log();
                     var2.addHeader("MatchMsg", var18.getMessageText());
                     var2.sendError(404);
                     return null;
                  } else if (ManagementService.getRuntimeAccess(BootstrapServlet.this.kernelId).getDomain().lookupServer(var6) == null) {
                     var18 = ConfigLogger.logServerNameDoesNotExistLoggable(var6);
                     var18.log();
                     var2.addHeader("UnkSvrMsg", var18.getMessageText());
                     var2.sendError(404);
                     return null;
                  } else {
                     ByteArrayOutputStream var7 = new ByteArrayOutputStream();
                     var1x = new WLObjectOutputStream(var7);
                     var1x.setServerChannel(ServerChannelManager.findDefaultLocalServerChannel());

                     String var10;
                     try {
                        AuthenticatedSubject var8;
                        if (var4 == null || var5 == null) {
                           ConfigLogger.logBootstrapMissingCredentials(var6);
                           var2.sendError(401);
                           var8 = null;
                           return var8;
                        }

                        var8 = null;

                        try {
                           var8 = BootstrapServlet.this.authenticator.authenticate(BootstrapServlet.this.new MyCallbackHandler(var4, var5));
                        } catch (LoginException var15) {
                           ConfigLogger.logBootstrapInvalidCredentials(var6, var4);
                           var2.sendError(401);
                           var10 = null;
                           return var10;
                        }

                        ServerResource var19 = new ServerResource((String)null, var6, "boot");
                        if (!BootstrapServlet.this.authorizer.isAccessAllowed(var8, var19, (ContextHandler)null)) {
                           ConfigLogger.logBootstrapUnauthorizedUser(var6, var4);
                           var2.sendError(401);
                           var10 = null;
                           return var10;
                        }

                        var10 = ManagementService.getRuntimeAccess(BootstrapServlet.this.kernelId).getDomain().getDomainVersion();
                        if (var10 != null) {
                           var2.addHeader("DomainVersion", var10);
                        }

                        BootstrapServlet.this.writeStructToStream(var1x);
                        var1x.flush();
                        var2.setContentLength(var7.size());
                        var2x = var2.getOutputStream();
                        var7.writeTo(var2x);
                        var2x.flush();
                        ConfigLogger.logManagedServerConfigWritten(var6);
                        return null;
                     } catch (Exception var16) {
                        Loggable var9 = ConfigLogger.logBootStrapExceptionLoggable(var16);
                        var9.log();
                        var2.addHeader("ErrorMsg", var9.getMessageText());
                        var2.sendError(500, var9.getMessage());
                        var10 = null;
                     } finally {
                        if (var1x != null) {
                           var1x.close();
                        }

                        if (var2x != null) {
                           var2x.close();
                        }

                     }

                     return var10;
                  }
               }
            }
         });
      } catch (PrivilegedActionException var5) {
         Object var4 = var5.getException();
         if (var4 == null) {
            var4 = var5;
         }

         ConfigLogger.logBootStrapException((Exception)var4);
         if (var4 instanceof IOException) {
            throw (IOException)var4;
         } else {
            throw new ServletException((Throwable)var4);
         }
      }
   }

   public Object run() throws ServletException {
      super.init(this.config);
      return null;
   }

   private void writeStructToStream(ObjectOutputStream var1) throws RemoteException, IOException {
      String var2 = ManagementService.getRuntimeAccess(this.kernelId).getAdminServerName();
      BootStrapStruct var3 = new BootStrapStruct(var2);
      var1.writeObject(var3);
   }

   private PeerInfo checkClientVersion(HttpServletRequest var1, HttpServletResponse var2) throws IOException {
      String var3 = var1.getHeader("Version");
      String var4 = var1.getHeader("servername");
      if (var3 != null && var3.length() != 0) {
         PeerInfo var9 = PeerInfo.getPeerInfo(var3);
         PeerInfo var6 = PeerInfo.getPeerInfo();
         if (var9 != null && var6 != null && var6.getMajor() <= var9.getMajor()) {
            return var9;
         } else {
            String var7 = var6 == null ? "null" : var6.getVersionAsString();
            Loggable var8 = ConfigLogger.logInvalidReleaseLevelLoggable(var4, var3, var7);
            var8.log();
            var2.addHeader("ErrorMsg", var8.getMessageText());
            var2.sendError(409);
            return null;
         }
      } else {
         Loggable var5 = ConfigLogger.logUnknownReleaseLevelLoggable();
         var5.log();
         var2.addHeader("ErrorMsg", var5.getMessageText());
         var2.sendError(409);
         return null;
      }
   }

   private static String mimeDecode(String var0) {
      String var1 = null;

      try {
         var1 = MimeUtility.decodeText(var0);
      } catch (UnsupportedEncodingException var3) {
         var1 = var0;
      }

      return var1;
   }

   class MyCallbackHandler implements CallbackHandler {
      private String username;
      private String password;

      public MyCallbackHandler(String var2, String var3) {
         this.username = var2;
         this.password = var3;
      }

      public void handle(Callback[] var1) throws UnsupportedCallbackException {
         for(int var2 = 0; var2 < var1.length; ++var2) {
            if (var1[var2] instanceof NameCallback) {
               NameCallback var3 = (NameCallback)var1[var2];
               var3.setName(this.username);
            } else {
               if (!(var1[var2] instanceof PasswordCallback)) {
                  throw new UnsupportedCallbackException(var1[var2], "Unrecognized Callback");
               }

               PasswordCallback var4 = (PasswordCallback)var1[var2];
               var4.setPassword(this.password.toCharArray());
            }
         }

      }
   }
}
