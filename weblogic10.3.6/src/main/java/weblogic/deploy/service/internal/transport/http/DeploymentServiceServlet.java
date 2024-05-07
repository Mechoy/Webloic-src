package weblogic.deploy.service.internal.transport.http;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.FileLock;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.List;
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
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import weblogic.application.utils.ApplicationVersionUtils;
import weblogic.common.internal.PeerInfo;
import weblogic.common.internal.WLObjectOutputStream;
import weblogic.deploy.common.Debug;
import weblogic.deploy.common.DeploymentObjectInputStream;
import weblogic.deploy.service.DataTransferRequest;
import weblogic.deploy.service.MultiDataStream;
import weblogic.deploy.service.datatransferhandlers.DataHandlerManager;
import weblogic.deploy.service.datatransferhandlers.MultipartResponse;
import weblogic.deploy.service.internal.DeploymentService;
import weblogic.deploy.service.internal.DeploymentServiceLogger;
import weblogic.deploy.service.internal.transport.DeploymentServiceMessage;
import weblogic.deploy.service.internal.transport.MessageReceiver;
import weblogic.deploy.utils.DeploymentServletConstants;
import weblogic.logging.Loggable;
import weblogic.management.DomainDir;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.PropertyService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.management.servlet.ConnectionSigner;
import weblogic.rmi.utils.io.RemoteObjectReplacer;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.AdminResource;
import weblogic.security.service.AuthorizationManager;
import weblogic.security.service.ContextHandler;
import weblogic.security.service.PrincipalAuthenticator;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.security.service.ServerResource;
import weblogic.security.service.SecurityService.ServiceType;
import weblogic.utils.FileUtils;
import weblogic.utils.StackTraceUtils;
import weblogic.utils.jars.JarFileUtils;

public final class DeploymentServiceServlet extends HttpServlet implements PrivilegedExceptionAction, DeploymentServiceConstants, DeploymentServletConstants {
   private static final int MAXIMUM_UPLOAD_SIZE = Integer.MAX_VALUE;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static AdminResource FILEUPLOAD_RESOURCE = new AdminResource("FileUpload", (String)null, (String)null);
   private AuthorizationManager authorizer;
   private PrincipalAuthenticator authenticator;
   private ServletConfig config = null;
   private String serverName = null;
   private RuntimeAccess serverConfig = null;
   private MessageReceiver loopbackReceiver = null;

   private final boolean isDebugEnabled() {
      return Debug.isServiceHttpDebugEnabled();
   }

   private final void debug(String var1) {
      Debug.serviceHttpDebug("DeploymentServiceServlet:" + var1);
   }

   public String getServletInfo() {
      return "DeploymentService transport servlet";
   }

   public void init(ServletConfig var1) throws ServletException {
      this.config = var1;
      if (kernelId == null) {
         Loggable var2 = DeploymentServiceLogger.logServletFailedToInitLoggable();
         var2.log();
         if (this.isDebugEnabled()) {
            this.debug("DeploymentServiceServlet: init: Security Services unavailable");
         }

         throw new ServletException(var2.getMessage());
      } else {
         this.authenticator = (PrincipalAuthenticator)SecurityServiceManager.getSecurityService(kernelId, "weblogicDEFAULT", ServiceType.AUTHENTICATION);
         this.authorizer = (AuthorizationManager)SecurityServiceManager.getSecurityService(kernelId, "weblogicDEFAULT", ServiceType.AUTHORIZE);
         if (this.authenticator != null && this.authorizer != null) {
            try {
               SecurityServiceManager.runAs(kernelId, kernelId, this);
            } catch (PrivilegedActionException var5) {
               Loggable var4 = DeploymentServiceLogger.logServletInitFailedDueToPrivilegedActionViolationLoggable(var5.getException().getMessage());
               var4.log();
               if (this.isDebugEnabled()) {
                  this.debug("DeploymentServiceServlet: init: Privileged action violation: " + var4.getMessage());
               }

               throw (ServletException)var5.getException();
            }

            this.serverConfig = ManagementService.getRuntimeAccess(kernelId);
            this.serverName = this.serverConfig.getServerName();
            DeploymentService var6 = DeploymentService.getDeploymentService();
            this.loopbackReceiver = var6.getMessageReceiver().getDelegate();
            if (this.isDebugEnabled()) {
               this.debug("DeploymentServiceServlet initialized");
            }

         } else {
            Loggable var3 = DeploymentServiceLogger.logServletFailedToInitLoggable();
            var3.log();
            if (this.isDebugEnabled()) {
               this.debug("DeploymentServiceServlet: init: Security Services unavailable " + (this.authenticator == null ? "null authenticator" : "null authorizer"));
            }

            throw new ServletException(var3.getMessage());
         }
      }
   }

   public void doGet(HttpServletRequest var1, HttpServletResponse var2) throws ServletException, IOException {
   }

   public void doPost(final HttpServletRequest var1, final HttpServletResponse var2) throws ServletException, IOException {
      final AuthenticatedSubject var3 = this.authenticateRequest(var1, var2);
      if (var3 != null) {
         String var4 = mimeDecode(var1.getHeader("wl_request_type"));
         if (this.isDebugEnabled()) {
            this.debug("DeploymentServiceServlet: doPost: requestType: " + var4);
         }

         try {
            Object var5 = SecurityServiceManager.runAs(kernelId, var3, new PrivilegedExceptionAction() {
               public Object run() throws Exception {
                  DeploymentServiceServlet.this.internalDoPost(var1, var2, var3);
                  return null;
               }
            });
         } catch (PrivilegedActionException var7) {
            Loggable var6 = DeploymentServiceLogger.logUnautherizedRequestLoggable(var4, var3.getName());
            if (this.isDebugEnabled()) {
               this.debug("DeploymentServiceServlet: doPost: privileged action error - " + var6.getMessage());
            }

            logAndSendError(var2, 401, var6);
         }

      }
   }

   private final void internalDoPost(HttpServletRequest var1, HttpServletResponse var2, AuthenticatedSubject var3) throws IOException {
      String var4 = mimeDecode(var1.getHeader("wl_request_type"));

      try {
         if (var4.equals("deployment_svc_msg")) {
            this.handleDeploymentServiceMessage(var1, var2, var3);
            return;
         }

         if (var4.equals("data_transfer_request")) {
            this.handleDataTransferRequest(var1, var2, var3);
            return;
         }

         if (var4.equals("plan_upload") || var4.equals("app_upload")) {
            this.handlePlanOrApplicationUpload(var1, var2, var3);
            return;
         }
      } catch (IOException var7) {
         Loggable var6 = DeploymentServiceLogger.logExceptionInServletRequestLoggable(var4, var7.getMessage());
         logAndSendError(var2, 500, var6);
      }

   }

   private final void handlePlanOrApplicationUpload(HttpServletRequest var1, HttpServletResponse var2, AuthenticatedSubject var3) throws IOException {
      String var4 = mimeDecode(var1.getHeader("wl_upload_application_name"));
      String var5 = ApplicationVersionUtils.getApplicationName(var4);
      String var6 = ApplicationVersionUtils.getVersionId(var4);
      String var7 = mimeDecode(var1.getHeader("wl_request_type"));
      if (var5 == null) {
         Loggable var24 = DeploymentServiceLogger.logRequestWithNoAppNameLoggable(var7);
         logAndSendError(var2, 403, var24);
      } else {
         String var8 = var1.getContentType();
         if (var8 != null && var8.startsWith("multipart")) {
            boolean var25 = false;
            String var10 = var1.getHeader("wl_upload_delta");
            if (var10 != null && var10.equalsIgnoreCase("true")) {
               var25 = true;
            }

            boolean var11 = var7.equals("plan_upload");
            boolean var12 = "false".equals(var1.getHeader("archive"));
            if (this.isDebugEnabled()) {
               this.debug(var7 + " request for application " + var5 + " with archive: " + var12);
            }

            String var13 = null;
            if (var6 == null || var6.length() == 0) {
               var13 = this.getUploadDirName(var5, var6, var25, var11, var12);
            }

            if (var13 == null) {
               var13 = this.getDefaultUploadDirName();
               if (var13 == null) {
                  Loggable var26 = DeploymentServiceLogger.logNoUploadDirectoryLoggable(var7, var5);
                  logAndSendError(var2, 500, var26);
                  return;
               }

               var13 = var13 + var5 + File.separator;
               if (var6 != null) {
                  var13 = var13 + var6 + File.separator;
               }
            }

            if (this.isDebugEnabled()) {
               this.debug(" +++ Final uploadingDirName : " + var13);
            }

            boolean var14 = true;
            String var15 = null;

            try {
               var2.setContentType("text/plain");
               File var16 = null;
               Loggable var17;
               if (var1.getHeader("jspRefresh") != null && var1.getHeader("jspRefresh").equals("true")) {
                  var15 = var1.getHeader("adminAppPath");
                  if (var15 == null) {
                     var17 = DeploymentServiceLogger.logNoUploadFileRequestLoggable();
                     logAndSendError(var2, 400, var17);
                     return;
                  }

                  var15 = var1.getHeader("adminAppPath");
                  var16 = this.doUploadFile(var1, var15);
               } else {
                  var16 = this.doUploadFile(var1, var13);
               }

               if (var16 != null) {
                  var15 = var16.getPath();
                  var14 = false;
                  if (!var12) {
                     return;
                  }

                  if (this.isDebugEnabled()) {
                     this.debug("extracting " + var15 + " to " + var13);
                  }

                  var14 = !this.extractArchive(var15, var13);
                  var15 = var13;
                  return;
               }

               var17 = DeploymentServiceLogger.logFailedOnUploadingFileLoggable();
               logAndSendError(var2, 400, var17);
            } finally {
               if (!var14) {
                  PrintStream var20 = new PrintStream(var2.getOutputStream());
                  File var21 = new File(var15);
                  var20.println(mimeEncode(var21.getAbsolutePath()));
                  var20.close();
               }

            }

         } else {
            Loggable var9 = DeploymentServiceLogger.logBadContentTypeServletRequestLoggable(var7, var8);
            logAndSendError(var2, 400, var9);
         }
      }
   }

   private File doUploadFile(HttpServletRequest var1, String var2) {
      AuthenticatedSubject var3 = SecurityServiceManager.getCurrentSubject(kernelId);
      if (!this.authorizer.isAccessAllowed(var3, FILEUPLOAD_RESOURCE, (ContextHandler)null)) {
         throw new RuntimeException("User has no access to upload files");
      } else {
         File var4;
         if (var2 != null) {
            var4 = new File(var2);
            if (!var4.exists()) {
               boolean var5 = var4.mkdirs();
            }

            if (!var4.isDirectory()) {
               throw new IllegalArgumentException("Not a directory: " + var2);
            }

            if (!var4.canWrite()) {
               throw new IllegalArgumentException("Not writable: " + var2);
            }
         }

         try {
            var4 = File.createTempFile("wls_upload", "");
         } catch (Exception var14) {
            throw new IllegalArgumentException(var14.getMessage());
         }

         if (var4.exists()) {
            var4.delete();
         }

         var4.mkdirs();
         if (!var4.isDirectory()) {
            throw new IllegalArgumentException("Not a directory: " + var4);
         } else if (!var4.canWrite()) {
            throw new IllegalArgumentException("Not writable: " + var4);
         } else {
            File var17 = null;

            String var10;
            try {
               DiskFileItemFactory var6 = new DiskFileItemFactory();
               var6.setSizeThreshold(1048576);
               var6.setRepository(var4);
               ServletFileUpload var18 = new ServletFileUpload(var6);
               var18.setSizeMax(2147483647L);
               List var19 = var18.parseRequest(var1);
               FileItem var20 = (FileItem)var19.iterator().next();
               if (!var20.isFormField()) {
                  var10 = mimeDecode(var20.getName());
                  var17 = new File(var2 + var10);
                  var20.write(var17);
               }

               return var17;
            } catch (FileUploadException var15) {
               String var7 = var1.getMethod();
               String var8 = mimeDecode(var1.getHeader("wl_upload_application_name"));
               String var9 = ApplicationVersionUtils.getApplicationName(var8);
               var10 = ApplicationVersionUtils.getVersionId(var8);
               String var11 = mimeDecode(var1.getHeader("wl_request_type"));
               String var12 = var1.getContentType();
               String var13 = "\n" + var7 + ",\n" + "wl_request_type" + ":" + var11 + ",\n" + "wl_upload_application_name" + ": " + var9 + ",\n" + "Content-Type: " + var12 + ",\n";
               if (this.isDebugEnabled()) {
                  this.debug(" Exception while parsing multipart/form-data request " + var13 + "reason: " + StackTraceUtils.throwable2StackTrace(var15));
               }

               DeploymentServiceLogger.logExceptionOnUpload(var13, var15.getMessage());
               return null;
            } catch (Exception var16) {
               if (this.isDebugEnabled()) {
                  this.debug(" Exception while uploading file to directory : " + var2 + "reason: " + StackTraceUtils.throwable2StackTrace(var16));
               }

               DeploymentServiceLogger.logExceptionOnUpload(var2.toString(), var16.getMessage());
               return null;
            }
         }
      }
   }

   private final String getUploadDirName(String var1, String var2, boolean var3, boolean var4, boolean var5) {
      AppDeploymentMBean var6 = null;
      String var7 = null;
      if (var1 != null) {
         var6 = ApplicationVersionUtils.getAppDeployment(ManagementService.getRuntimeAccess(kernelId).getDomain(), var1, var2);
         if (var6 != null) {
            if (var3) {
               var7 = var6.getAbsoluteSourcePath();
            } else if (!var4) {
               File var8 = new File(var6.getAbsoluteSourcePath());
               var7 = var8.getParentFile().getParent();
            } else if (var5) {
               var7 = this.getUploadDirForPlanDir(var6);
            } else {
               var7 = this.getUploadDirForPlan(var6);
            }

            if (var7 != null) {
               var7 = var7 + File.separator;
            }
         }
      }

      return var7;
   }

   private String getUploadDirForPlan(AppDeploymentMBean var1) {
      String var2 = var1.getAbsolutePlanPath();
      if (var2 == null) {
         var2 = this.getOrCreatePlanDir(var1);
      } else {
         var2 = (new File(var2)).getParent();
      }

      return var2;
   }

   private String getOrCreatePlanDir(AppDeploymentMBean var1) {
      if (var1.getPlanDir() != null) {
         return var1.getAbsolutePlanDir();
      } else {
         File var2 = null;
         String var3 = null;
         if (var1.getAbsoluteInstallDir() != null) {
            var2 = new File(var1.getAbsoluteInstallDir());
            var2 = new File(var2, "plan");
            var2.mkdirs();
            var3 = var2.getPath();
         }

         return var3;
      }
   }

   private String getUploadDirForPlanDir(AppDeploymentMBean var1) {
      return this.getOrCreatePlanDir(var1);
   }

   private final String getDefaultUploadDirName() {
      String var1 = null;
      var1 = ManagementService.getRuntimeAccess(kernelId).getServer().getUploadDirectoryName() + File.separator;
      if (this.isDebugEnabled()) {
         this.debug("uploadingDirName is " + var1);
      }

      return var1;
   }

   private final boolean extractArchive(String var1, String var2) {
      File var3 = new File(var2);
      File var4 = new File(var1);
      if (this.isDebugEnabled()) {
         this.debug(" +++ toDir : " + var3.getAbsolutePath());
         this.debug(" +++ jar : " + var4.getAbsolutePath());
      }

      try {
         JarFileUtils.extract(var4, var3);
      } catch (IOException var6) {
         if (this.isDebugEnabled()) {
            this.debug(" Exception while extracting jar file to directory : " + var3 + "reason: " + StackTraceUtils.throwable2StackTrace(var6));
         }

         DeploymentServiceLogger.logExceptionOnExtract(var3.toString(), var6.getMessage());
         return false;
      }

      var4.delete();
      return true;
   }

   private AuthenticatedSubject authenticateRequest(HttpServletRequest var1, HttpServletResponse var2) throws IOException {
      long var3 = System.currentTimeMillis();

      try {
         String var6 = var1.getHeader("wls_salt");
         String var7;
         AuthenticatedSubject var9;
         if (var6 != null) {
            var7 = var1.getHeader("wls_signature");
            Loggable var20;
            if (var7 == null) {
               var20 = DeploymentServiceLogger.logUnautherizedRequestLoggable(mimeDecode(var1.getHeader("wl_request_type")), "No Signature");
               var20.log();
               if (this.isDebugEnabled()) {
                  this.debug("DeploymentServiceServlet: authenticateRequest: " + var20.getMessage());
               }
            }

            if (!ConnectionSigner.authenticate(var6, var7)) {
               var20 = DeploymentServiceLogger.logDomainWideSecretMismatchLoggable();
               if (this.isDebugEnabled()) {
                  this.debug("DeploymentServiceServlet: authenticateRequest: Domain wide secret mismatch between salt: " + var6 + " and signature: " + var7);
               }

               logAndSendError(var2, 401, var20);
               var9 = null;
               return var9;
            } else {
               AuthenticatedSubject var21 = kernelId;
               return var21;
            }
         } else {
            var7 = mimeDecode(var1.getHeader("username"));
            if (this.isDebugEnabled()) {
               this.debug("DeploymentServiceServlet: authenticateRequest: Received req.header username: " + var7);
            }

            String var8 = mimeDecode(var1.getHeader("password"));
            Loggable var10;
            if (var7 == null || var8 == null) {
               if (this.isDebugEnabled()) {
                  if (var7 == null) {
                     this.debug("DeploymentServiceServlet: authenticateRequest: error - User name not provided");
                  } else {
                     this.debug("DeploymentServiceServlet: authenticateRequest: error - User password not provided");
                  }
               }

               Loggable var22 = DeploymentServiceLogger.logNoUserNameOrPasswordLoggable();
               logAndSendError(var2, 401, var22);
               var10 = null;
               return var10;
            } else {
               AuthenticatedSubject var5;
               try {
                  var5 = this.authenticator.authenticate(new MyCallbackHandler(var7, var8));
               } catch (LoginException var18) {
                  var10 = DeploymentServiceLogger.logInvalidUserNameOrPasswordLoggable();
                  if (this.isDebugEnabled()) {
                     this.debug("DeploymentServiceServlet: authenticateRequest: error - User name not authorized");
                  }

                  logAndSendError(var2, 401, var10);
                  Object var11 = null;
                  return (AuthenticatedSubject)var11;
               }

               var9 = var5;
               return var9;
            }
         }
      } finally {
         long var14 = System.currentTimeMillis() - var3;
         if (this.isDebugEnabled()) {
            this.debug("DeploymentServiceServlet: authenticateRequest: TIME TOOK: " + var14);
         }

      }
   }

   private void handleDeploymentServiceMessage(HttpServletRequest var1, HttpServletResponse var2, AuthenticatedSubject var3) throws IOException {
      String var4 = mimeDecode(var1.getHeader("serverName"));
      if (this.isDebugEnabled()) {
         this.debug("Received req.header serverName: " + var4);
      }

      String var5 = var1.getHeader("deployment_request_id");
      long var6 = var5 != null ? Long.parseLong(var5) : -1L;
      String var8 = this.readOrConstructPeerVersion(var1);
      if (this.isDebugEnabled()) {
         this.debug("Peer Version : " + var8);
      }

      boolean var9 = Boolean.valueOf(mimeDecode(var1.getHeader("isSynchronous")));
      if (this.isDebugEnabled()) {
         this.debug("Received req.header isSynch: " + var9);
      }

      var2.addHeader("serverName", mimeEncode(this.serverName));
      DeploymentObjectInputStream var10 = null;
      WLObjectOutputStream var11 = null;
      ServletOutputStream var12 = null;

      try {
         Loggable var15;
         try {
            var10 = new DeploymentObjectInputStream(var1.getInputStream(), var8);
            DeploymentServiceMessage var13 = (DeploymentServiceMessage)var10.readObject();
            ServerResource var23 = new ServerResource((String)null, var4, "boot");
            if (this.authorizer.isAccessAllowed(var3, var23, (ContextHandler)null)) {
               if (this.isDebugEnabled()) {
                  this.debug("Received DeploymentService message '" + var13);
               }

               var2.setStatus(200);
               if (var9) {
                  if (this.isDebugEnabled()) {
                     this.debug("Handling synchronous deployment service message");
                  }

                  DeploymentServiceMessage var24 = this.loopbackReceiver.receiveSynchronousMessage(var13);
                  if (var24 != null && this.isDebugEnabled()) {
                     this.debug("Sending out synchronous response " + var24);
                  }

                  var2.addHeader("server_version", PeerInfo.getPeerInfo().getVersionAsString());
                  ByteArrayOutputStream var16 = new ByteArrayOutputStream();
                  var11 = new WLObjectOutputStream(var16);
                  var11.setReplacer(RemoteObjectReplacer.getReplacer());
                  var11.writeObject(var24);
                  var11.flush();
                  var2.setContentLength(var16.size());
                  var12 = var2.getOutputStream();
                  var16.writeTo(var12);
                  var12.flush();
               } else {
                  if (this.isDebugEnabled()) {
                     this.debug("Handling asynchronous deployment service message");
                  }

                  this.loopbackReceiver.receiveMessage(var13);
               }

               return;
            }

            var15 = DeploymentServiceLogger.logAccessNotAllowedLoggable(var4);
            if (this.isDebugEnabled()) {
               this.debug(var15.getMessage());
               this.debug("DeploymentServiceServlet error - access not allowed");
            }

            logAndSendError(var2, 401, var15);
         } catch (Throwable var21) {
            var2.addHeader("serverName", mimeEncode(this.serverName));
            String var14 = StackTraceUtils.throwable2StackTrace(var21);
            if (this.isDebugEnabled()) {
               this.debug("DeploymentServiceServlet error - " + var21.getMessage() + " " + var14);
            }

            var15 = DeploymentServiceLogger.logExceptionInServletRequestForDeploymentMsgLoggable(var6, var4, var14);
            var15.log();
            var15 = DeploymentServiceLogger.logExceptionInServletRequestForDeploymentMsgLoggable(var6, var4, var21.getMessage());
            sendError(var2, 500, var15.getMessage());
            return;
         }
      } finally {
         if (var10 != null) {
            var10.close();
         }

         if (var11 != null) {
            var11.close();
         }

         if (var12 != null) {
            var12.close();
         }

      }

   }

   private void handleDataTransferRequest(HttpServletRequest var1, HttpServletResponse var2, AuthenticatedSubject var3) throws IOException {
      if (this.isDebugEnabled()) {
         this.debug("Received DataTransferRequest : ");
      }

      String var4 = this.readOrConstructPeerVersion(var1);
      if (this.isDebugEnabled()) {
         this.debug("Peer Version : " + var4);
      }

      String var5 = var1.getHeader("deployment_request_id");
      long var6 = var5 != null ? Long.parseLong(var5) : -1L;
      String var8 = mimeDecode(var1.getHeader("serverName"));
      DeploymentObjectInputStream var9 = null;

      try {
         var9 = new DeploymentObjectInputStream(var1.getInputStream(), var4);
         DataTransferRequest var10 = (DataTransferRequest)var9.readObject();
         MultiDataStream var28 = DataHandlerManager.getInstance().getHttpDataTransferHandler().getDataAsStream(var10);
         String var29 = var10.getLockPath();
         FileLock var13 = null;

         try {
            if (var29 != null && var29.length() > 0) {
               var13 = this.lockFile(var29);
            }

            MultipartResponse var14 = new MultipartResponse(var2, var28);
            var14.write();
            return;
         } finally {
            this.unlockFile(var13);
         }
      } catch (Throwable var26) {
         String var11 = StackTraceUtils.throwable2StackTrace(var26);
         if (this.isDebugEnabled()) {
            this.debug("DeploymentServiceServlet error - " + var26.getMessage() + " " + var11);
         }

         Loggable var12 = DeploymentServiceLogger.logExceptionInServletRequestForDatatransferMsgLoggable(var6, var8, var11);
         var12.log();
         var12 = DeploymentServiceLogger.logExceptionInServletRequestForDatatransferMsgLoggable(var6, var8, var26.getMessage());
         sendError(var2, 500, var12.getMessage());
      } finally {
         if (var9 != null) {
            var9.close();
         }

      }

   }

   public Object run() throws ServletException {
      super.init(this.config);
      return null;
   }

   public static URL getURL() throws MalformedURLException {
      ManagementService.getPropertyService(kernelId);
      String var0 = PropertyService.getAdminHttpUrl();
      if (!var0.endsWith("/")) {
         var0 = var0 + "/";
      }

      return new URL(var0 + "bea_wls_deployment_internal/DeploymentService");
   }

   private static void sendError(HttpServletResponse var0, int var1, String var2) throws IOException {
      if (!var0.isCommitted()) {
         var0.sendError(var1, var2);
         var0.addHeader("ErrorMsg", var2);
      }

   }

   private static void logAndSendError(HttpServletResponse var0, int var1, Loggable var2) throws IOException {
      var2.log();
      sendError(var0, var1, var2.getMessage());
   }

   private FileLock lockFile(String var1) {
      if (var1 == null) {
         return null;
      } else {
         FileLock var2 = null;

         try {
            String var3 = DomainDir.getRootDir() + File.separator;
            String var4 = var3 + var1;
            FileOutputStream var5 = new FileOutputStream(var4);
            var2 = FileUtils.getFileLock(var5.getChannel(), 30000L);
         } catch (IOException var6) {
         }

         return var2;
      }
   }

   private void unlockFile(FileLock var1) {
      if (var1 != null) {
         try {
            var1.release();
            var1.channel().close();
         } catch (IOException var3) {
         }
      }

   }

   private static String mimeDecode(String var0) {
      String var1 = null;

      try {
         if (var0 != null) {
            var1 = MimeUtility.decodeText(var0);
         }
      } catch (UnsupportedEncodingException var3) {
         var1 = var0;
      }

      return var1;
   }

   private static String mimeEncode(String var0) {
      String var1 = null;

      try {
         var1 = MimeUtility.encodeText(var0, "UTF-8", (String)null);
      } catch (UnsupportedEncodingException var3) {
         var1 = var0;
      }

      return var1;
   }

   private String readOrConstructPeerVersion(HttpServletRequest var1) {
      String var2 = var1.getHeader("server_version");
      if (this.isDebugEnabled()) {
         this.debug("Received req.header server_version: " + var2);
      }

      String var3 = var2 != null && var2.length() != 0 ? var2 : PeerInfo.getPeerInfo().getVersionAsString();
      if (this.isDebugEnabled()) {
         this.debug("decided peerVersion: " + var3);
      }

      return var3;
   }

   static final class MyCallbackHandler implements CallbackHandler {
      private final String username;
      private final String password;

      public MyCallbackHandler(String var1, String var2) {
         this.username = var1;
         this.password = var2;
      }

      public void handle(Callback[] var1) throws UnsupportedCallbackException {
         for(int var2 = 0; var2 < var1.length; ++var2) {
            if (var1[var2] instanceof NameCallback) {
               NameCallback var3 = (NameCallback)var1[var2];
               var3.setName(this.username);
            } else {
               if (!(var1[var2] instanceof PasswordCallback)) {
                  String var5 = DeploymentServiceLogger.unrecognizedCallback();
                  throw new UnsupportedCallbackException(var1[var2], var5);
               }

               PasswordCallback var4 = (PasswordCallback)var1[var2];
               var4.setPassword(this.password.toCharArray());
            }
         }

      }
   }
}
