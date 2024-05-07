package weblogic.management.servlet;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
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
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.kernel.Kernel;
import weblogic.ldap.EmbeddedLDAP;
import weblogic.management.DomainDir;
import weblogic.management.ManagementLogger;
import weblogic.management.provider.PropertyService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.acl.internal.FileRealm;
import weblogic.security.service.AdminResource;
import weblogic.security.service.AuthorizationManager;
import weblogic.security.service.ContextHandler;
import weblogic.security.service.PrincipalAuthenticator;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.security.service.SecurityService.ServiceType;
import weblogic.servlet.security.Utils;
import weblogic.utils.io.StreamUtils;
import weblogic.xml.registry.XMLRegistryDir;
import weblogic.xml.registry.XMLRegistryException;

public class FileDistributionServlet extends HttpServlet {
   public static final String OAM_APPNAME = "bea_wls_management_internal2";
   public static final String NAME = "bea_wls_management_internal2/wl_management";
   public static final String REQUEST_TYPE = "wl_request_type";
   public static final String REQUEST_USERNAME = "username";
   public static final String REQUEST_PASSWORD = "password";
   public static final String FILE_REALM_REQUEST = "wl_file_realm_request";
   public static final String INIT_REPLICA_REQUEST = "wl_init_replica_request";
   public static final String INIT_REPLICA_SERVER_NAME = "init-replica_server-name";
   public static final String INIT_REPLICA_SERVER_URL = "init-replica_server-url";
   public static final String INIT_REPLICA_VALIDATE = "init-replica-validate";
   public static final String XML_ENTITY_REQUEST = "wl_xml_entity_request";
   public static final String XML_ENTITY_PATH = "xml-entity-path";
   public static final String XML_REGISTRY_NAME = "xml-registry-name";
   public static final String JSP_UPLOAD_REQUEST = "wl_jsp_upload_request";
   public static final String JSP_REFRESH_REQUEST = "wl_jsp_refresh_request";
   public static final String SERVER_NAME = "server_name";
   public static final String MSI_REQUEST = "wl_managed_server_independence_request";
   public static final String MSI_REQUEST_FILE = "wl_managed_server_independence_request_filename";
   public static final String MSI_REQUEST_DOMAIN = "wl_managed_server_independence_request_domain";
   public static final String FILE_REQUEST = "file";
   public static final String FILE_REQUESTED = "file_name";
   private static final long serialVersionUID = -8473442547994280015L;
   private static DebugLogger debugLogger = DebugLogger.getDebugLogger("DebugFileDistributionServlet");
   private AuthorizationManager am = null;
   private PrincipalAuthenticator pa = null;
   private static AuthenticatedSubject KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public static URL getURL() throws MalformedURLException {
      String var0 = PropertyService.getAdminHttpUrl();
      if (!var0.endsWith("/")) {
         var0 = var0 + "/";
      }

      return new URL(var0 + "bea_wls_management_internal2/wl_management");
   }

   private AuthenticatedSubject authenticateRequest(HttpServletRequest var1, HttpServletResponse var2) throws IOException {
      AuthenticatedSubject var3 = null;
      String var4 = var1.getHeader("wls_salt");
      String var5;
      if (var4 != null) {
         var5 = var1.getHeader("wls_signature");
         if (var5 == null) {
            ManagementLogger.logErrorFDSMissingCredentials();
         }

         if (!ConnectionSigner.authenticate(var4, var5)) {
            ManagementLogger.logErrorFDSAuthenticationFailedDueToDomainWideSecretMismatch(var4, var5);
            return null;
         } else {
            return KERNEL_ID;
         }
      } else {
         var5 = var1.getHeader("username");
         String var6 = var1.getHeader("password");
         if (var5 != null && var6 != null) {
            try {
               var3 = this.pa.authenticate(new MyCallbackHandler(var5, var6));
               return var3;
            } catch (LoginException var8) {
               ManagementLogger.logErrorFDSAuthenticationFailed(var5);
               var2.sendError(401);
               return null;
            }
         } else {
            ManagementLogger.logErrorFDSMissingCredentials();
            var2.sendError(401);
            return null;
         }
      }
   }

   public String getServletInfo() {
      return "Management files distribution servlet";
   }

   public void init(ServletConfig var1) throws ServletException {
      super.init(var1);
      KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
      if (KERNEL_ID == null) {
         throw new ServletException("Security Services Unavailable");
      } else {
         String var2 = "weblogicDEFAULT";
         this.pa = (PrincipalAuthenticator)SecurityServiceManager.getSecurityService(KERNEL_ID, var2, ServiceType.AUTHENTICATION);
         this.am = (AuthorizationManager)SecurityServiceManager.getSecurityService(KERNEL_ID, var2, ServiceType.AUTHORIZE);
         if (this.pa != null && this.am != null) {
            if (debugLogger.isDebugEnabled()) {
               debugLogger.debug("FileDistributionServlet initialized");
            }

         } else {
            throw new ServletException("Security Services Unavailable");
         }
      }
   }

   public void doPost(HttpServletRequest var1, HttpServletResponse var2) throws ServletException, IOException {
      AuthenticatedSubject var3 = this.authenticateRequest(var1, var2);
      if (var3 != null) {
         final HttpServletRequest var4 = var1;
         final HttpServletResponse var5 = var2;

         try {
            SecurityServiceManager.runAs(KERNEL_ID, var3, new PrivilegedExceptionAction() {
               public Object run() throws Exception {
                  FileDistributionServlet.this.internalDoPost(var4, var5);
                  return null;
               }
            });
         } catch (PrivilegedActionException var7) {
            ManagementLogger.logErrorFDSUnauthorizedUploadAttempt(var3.getName());
            var2.sendError(401);
         }

      }
   }

   private void internalDoPost(HttpServletRequest var1, HttpServletResponse var2) throws IOException {
   }

   public void doGet(final HttpServletRequest var1, final HttpServletResponse var2) throws ServletException, IOException {
      AuthenticatedSubject var3 = this.authenticateRequest(var1, var2);
      if (var3 != null) {
         final String var4 = var1.getHeader("wl_request_type");
         if (var3 != KERNEL_ID) {
            AdminResource var5 = new AdminResource("FileDownload", (String)null, var4);
            if (!this.am.isAccessAllowed(var3, var5, (ContextHandler)null)) {
               ManagementLogger.logErrorFDSUnauthorizedDownloadAttempt(var3.getName(), var4);
               var2.sendError(401);
               return;
            }
         }

         try {
            if (debugLogger.isDebugEnabled()) {
               debugLogger.debug("---- >doGet incoming request: " + var4);
            }

            if (var4.equals("wl_xml_entity_request")) {
               this.doGetXMLEntityRequest(var1, var2);
            } else if (var4.equals("wl_jsp_refresh_request")) {
               this.doGetJspRefreshRequest(var1, var2);
            } else if (var4.equals("file")) {
               this.doGetFile(var1, var2);
            } else if (!var4.equals("wl_init_replica_request") && !var4.equals("wl_file_realm_request") && !var4.equals("wl_managed_server_independence_request")) {
               var2.addHeader("ErrorMsg", "Bad request type");
               String var10 = Utils.encodeXSS(var4);
               var2.sendError(400, "Bad request type: " + var10);
               ManagementLogger.logBadRequestInFileDistributionServlet(var4);
            } else {
               try {
                  SecurityServiceManager.runAs(KERNEL_ID, var3, new PrivilegedExceptionAction() {
                     public Object run() throws IOException {
                        if (var4.equals("wl_init_replica_request")) {
                           FileDistributionServlet.this.doGetInitReplicaRequest(var1, var2);
                        } else if (var4.equals("wl_file_realm_request")) {
                           FileDistributionServlet.this.doGetFileRealmRequest(var2);
                        } else if (var4.equals("wl_managed_server_independence_request")) {
                           FileDistributionServlet.this.doGetMSIRequest(var1, var2);
                        }

                        return null;
                     }
                  });
               } catch (PrivilegedActionException var8) {
                  Exception var6 = var8.getException();
                  throw var6;
               }
            }
         } catch (Exception var9) {
            if (!Kernel.isInitialized()) {
               throw new AssertionError("kernel not initialized");
            }

            ManagementLogger.logErrorInFileDistributionServlet(var4, var9);
         }

      }
   }

   private void returnInputStream(InputStream var1, OutputStream var2) throws IOException {
      StreamUtils.writeTo(var1, var2);
   }

   private void returnFile(String var1, HttpServletResponse var2, boolean var3, FileNotFoundHandler var4) throws IOException {
      File var5 = new File(var1);
      if (!var5.exists()) {
         String var18 = var4.purpose() + " file not found at configured location";
         var2.addHeader("ErrorMsg", var18);
         var2.sendError(500, var18 + ": " + Utils.encodeXSS(var5.toString()));
         if (Kernel.isInitialized()) {
            var4.log(var5.toString());
         }

      } else {
         DataOutputStream var6 = new DataOutputStream(var2.getOutputStream());

         try {
            if (var3) {
               var6.writeUTF(Utils.encodeXSS(var1));
            }

            FileInputStream var7 = new FileInputStream(var5);

            try {
               this.returnInputStream(var7, var6);
            } finally {
               var7.close();
            }
         } finally {
            var6.close();
         }

      }
   }

   private void doGetFileRealmRequest(HttpServletResponse var1) throws IOException {
      this.returnFile(FileRealm.getPath(), var1, false, new FileNotFoundHandler() {
         public void log(String var1) {
            ManagementLogger.logFileNotFoundProcessingFileRealmRequest(var1);
         }

         public String purpose() {
            return "Security";
         }
      });
   }

   private void doGetXMLEntityRequest(HttpServletRequest var1, HttpServletResponse var2) throws IOException {
      String var3 = var1.getHeader("xml-registry-name");
      String var4 = var1.getHeader("xml-entity-path");
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("wl_xml_entity_request: registryName = " + var3 + ", entityPath = " + var4);
      }

      String var5;
      if (var3 != null && var3.length() != 0) {
         if (var4 != null && var4.length() != 0) {
            XMLRegistryDir var16 = new XMLRegistryDir(var3);
            InputStream var6 = null;
            BufferedOutputStream var7 = new BufferedOutputStream(var2.getOutputStream());

            label106: {
               try {
                  var6 = var16.getEntity(var4);
                  StreamUtils.writeTo(var6, var7);
                  break label106;
               } catch (XMLRegistryException var14) {
                  if (debugLogger.isDebugEnabled()) {
                     debugLogger.debug("Exception in FileDistributionServlet", var14);
                  }

                  String var9 = var14.getMessage();
                  var2.addHeader("ErrorMsg", var9);
                  var2.sendError(500, var9);
               } finally {
                  if (var6 != null) {
                     var6.close();
                  }

                  var7.close();
               }

               return;
            }

            if (debugLogger.isDebugEnabled()) {
               debugLogger.debug("entity written to servlet output stream");
            }

         } else {
            var5 = "Entity path not specified";
            var2.addHeader("ErrorMsg", var5);
            var2.sendError(500, var5);
         }
      } else {
         var5 = "Registry Name not specified";
         var2.addHeader("ErrorMsg", var5);
         var2.sendError(500, var5);
      }
   }

   private void doGetInitReplicaRequest(HttpServletRequest var1, HttpServletResponse var2) throws IOException {
      String var3 = mimeDecode(var1.getHeader("init-replica_server-name"));
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("wl_init_replica_request: serverName = " + var3);
      }

      String var4;
      if (var3 == null) {
         var4 = "Server Name not specified";
         ManagementLogger.logErrorProcessingInitReplicaRequest(var4);
         var2.addHeader("ErrorMsg", var4);
         var2.sendError(500, var4);
      } else {
         var4 = var1.getHeader("init-replica_server-url");
         String var5 = var1.getHeader("init-replica-validate");
         EmbeddedLDAP var6 = EmbeddedLDAP.getEmbeddedLDAP();
         String var7;
         if (var6 == null) {
            var7 = "Embedded LDAP not available";
            ManagementLogger.logErrorProcessingInitReplicaRequest(var7);
            var2.addHeader("ErrorMsg", var7);
            var2.sendError(500, var7);
         } else {
            String var8;
            try {
               if (var5 != null && var6.isValidReplica(var3, var4)) {
                  BufferedOutputStream var12 = new BufferedOutputStream(var2.getOutputStream());
                  var12.close();
                  return;
               }
            } catch (Exception var11) {
               if (debugLogger.isDebugEnabled()) {
                  debugLogger.debug("Exception in FileDistributionServlet", var11);
               }

               var8 = "" + var11.getMessage();
               ManagementLogger.logErrorProcessingInitReplicaRequest(var8);
               var2.addHeader("ErrorMsg", var8);
               var2.sendError(500, var8);
               return;
            }

            var7 = null;

            try {
               var7 = var6.initReplicaForNewServer(var3, var4);
               if (var7 == null) {
                  var8 = "Initial replica not available";
                  ManagementLogger.logErrorProcessingInitReplicaRequest(var8);
                  var2.addHeader("ErrorMsg", var8);
                  var2.sendError(500, var8);
                  return;
               }
            } catch (Exception var10) {
               if (debugLogger.isDebugEnabled()) {
                  debugLogger.debug("Exception in FileDistributionServlet", var10);
               }

               String var9 = "" + var10.getMessage();
               ManagementLogger.logErrorProcessingInitReplicaRequest(var9);
               var2.addHeader("ErrorMsg", var9);
               var2.sendError(500, var9);
               return;
            }

            this.returnFile(var7, var2, false, new FileNotFoundHandler() {
               public void log(String var1) {
                  ManagementLogger.logFileNotFoundProcessingInitReplicaRequest(var1);
               }

               public String purpose() {
                  return "Embedded LDAP initial replica";
               }
            });
         }
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

   private void doGetJspRefreshRequest(HttpServletRequest var1, HttpServletResponse var2) throws IOException {
      String var3 = var1.getHeader("adminPath");

      try {
         FileInputStream var4 = new FileInputStream(var3);

         try {
            var2.setContentType("text/plain");
            var2.setStatus(200);
            this.returnInputStream(var4, var2.getOutputStream());
         } finally {
            var4.close();
         }

      } catch (IOException var10) {
         String var5 = "I/O Exception getting resource: " + var10.getMessage();
         var2.addHeader("ErrorMsg", var5);
         var2.sendError(500, var5);
      }
   }

   private void doGetMSIRequest(HttpServletRequest var1, HttpServletResponse var2) throws IOException {
      String var3 = var1.getHeader("wl_managed_server_independence_request_filename");
      String var4 = File.separator;
      String var5 = DomainDir.getRootDir() + var4 + var3;
      File var6 = new File(var5);
      if (!var6.exists()) {
         String var16 = var6.getAbsolutePath() + " doesn't exist in AdminServer";
         var2.addHeader("ErrorMsg", var16);
         var2.sendError(404, var16);
      } else {
         try {
            FileInputStream var7 = null;

            try {
               var2.setContentType("text/plain");
               var2.setStatus(200);
               if (var6.isDirectory()) {
                  File[] var17 = var6.listFiles();
                  ObjectOutputStream var9 = new ObjectOutputStream(var2.getOutputStream());
                  var9.writeObject(var17);
               } else {
                  var7 = new FileInputStream(var6);
                  this.returnInputStream(var7, var2.getOutputStream());
               }
            } finally {
               if (var7 != null) {
                  var7.close();
               }

            }
         } catch (IOException var15) {
            this.log("Interal I/0 Exception on AdminServer getting resource " + (var3 == null ? "null" : var3), var15);
            String var8 = "I/O Exception getting resource: " + var15.getMessage();
            var2.addHeader("ErrorMsg", var8);
            var2.sendError(500, var8);
         }

      }
   }

   private void doGetFile(HttpServletRequest var1, HttpServletResponse var2) throws IOException {
      String var3 = var1.getHeader("file_name");
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug(" File requested : " + var3);
      }

      File var4 = new File(var3);
      if (!var4.exists()) {
         String var12 = var4.getAbsolutePath() + " doesn't exist";
         var2.addHeader("ErrorMsg", var12);
         var2.sendError(404, var12);
      } else {
         try {
            FileInputStream var5 = new FileInputStream(var4);

            try {
               var2.setContentType("text/plain");
               var2.setStatus(200);
               this.returnInputStream(var5, var2.getOutputStream());
            } finally {
               var5.close();
            }
         } catch (IOException var11) {
            this.log("Interal I/0 Exception on AdminServer getting resource " + (var3 == null ? "null" : var3), var11);
            String var6 = "I/O Exception getting resource: " + var11.getMessage();
            var2.addHeader("ErrorMsg", var6);
            var2.sendError(500, var6);
         }

      }
   }

   private interface FileNotFoundHandler {
      void log(String var1);

      String purpose();
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
               if (this.password != null) {
                  var4.setPassword(this.password.toCharArray());
               } else {
                  var4.setPassword((char[])null);
               }
            }
         }

      }
   }
}
