package weblogic.diagnostics.accessor;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.net.URLDecoder;
import java.security.AccessController;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.security.auth.login.LoginException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLStreamException;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.diagnostics.i18n.DiagnosticsLogger;
import weblogic.diagnostics.i18n.DiagnosticsTextTextFormatter;
import weblogic.diagnostics.image.ImageManager;
import weblogic.diagnostics.instrumentation.DelegatingMonitor;
import weblogic.diagnostics.instrumentation.DiagnosticAction;
import weblogic.diagnostics.instrumentation.DiagnosticActionState;
import weblogic.diagnostics.instrumentation.DynamicJoinPoint;
import weblogic.diagnostics.instrumentation.InstrumentationSupport;
import weblogic.diagnostics.instrumentation.JoinPoint;
import weblogic.diagnostics.instrumentation.PointcutHandlingInfo;
import weblogic.diagnostics.instrumentation.ValueHandlingInfo;
import weblogic.logging.Loggable;
import weblogic.management.ManagementException;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.management.runtime.WLDFDataAccessRuntimeMBean;
import weblogic.security.SimpleCallbackHandler;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrincipalAuthenticator;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.security.service.SecurityService.ServiceType;
import weblogic.utils.io.StreamUtils;

public final class AccessorServlet extends HttpServlet implements AccessorClientConstants {
   private static final DebugLogger ACCESSOR_DEBUG;
   private static final AuthenticatedSubject kernelId;
   private static final RuntimeAccess runtimeAccess;
   private static final String SERVLET_INFO = "Accessor Servlet";
   static final long serialVersionUID = -2951340701982146842L;
   public static final String _WLDF$INST_VERSION = "9.0.0";
   // $FF: synthetic field
   static Class _WLDF$INST_FLD_class = Class.forName("weblogic.diagnostics.accessor.AccessorServlet");
   public static final DelegatingMonitor _WLDF$INST_FLD_Servlet_Request_Action_Around_Medium;
   public static final JoinPoint _WLDF$INST_JPFLD_0;

   public String getServletInfo() {
      return "Accessor Servlet";
   }

   public void init(ServletConfig var1) throws ServletException {
      super.init(var1);
   }

   public void doGet(HttpServletRequest var1, HttpServletResponse var2) throws ServletException, IOException {
      boolean var17;
      boolean var10000 = var17 = _WLDF$INST_FLD_Servlet_Request_Action_Around_Medium.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var18 = null;
      DiagnosticActionState[] var19 = null;
      Object var16 = null;
      if (var10000) {
         Object[] var12 = null;
         if (_WLDF$INST_FLD_Servlet_Request_Action_Around_Medium.isArgumentsCaptureNeeded()) {
            var12 = new Object[]{this, var1, var2};
         }

         DynamicJoinPoint var37 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_0, var12, (Object)null);
         DelegatingMonitor var10001 = _WLDF$INST_FLD_Servlet_Request_Action_Around_Medium;
         DiagnosticAction[] var10002 = var18 = var10001.getActions();
         InstrumentationSupport.preProcess(var37, var10001, var10002, var19 = InstrumentationSupport.getActionStates(var10002));
      }

      try {
         var1.setCharacterEncoding("UTF-8");
         if (ACCESSOR_DEBUG.isDebugEnabled()) {
            ACCESSOR_DEBUG.debug("Secure request = " + var1.isSecure());
         }

         if (!var1.isSecure() && runtimeAccess.getDomain().isProductionModeEnabled()) {
            Loggable var35 = DiagnosticsLogger.logNonSecureAttemptToAccessDiagnosticDataLoggable();
            var35.log();
            var2.sendError(401, var35.getMessage());
            return;
         }

         String var3 = var1.getHeader("username");
         String var4 = var1.getHeader("password");
         if (var3 != null && var4 != null) {
            String var5 = URLDecoder.decode(var3, "UTF-8");
            String var6 = URLDecoder.decode(var4, "UTF-8");
            if (ACCESSOR_DEBUG.isDebugEnabled()) {
               ACCESSOR_DEBUG.debug("User = " + var5);
            }

            AuthenticatedSubject var7 = null;

            try {
               String var8 = "weblogicDEFAULT";
               PrincipalAuthenticator var9 = (PrincipalAuthenticator)SecurityServiceManager.getSecurityService(kernelId, var8, ServiceType.AUTHENTICATION);
               var7 = var9.authenticate(new SimpleCallbackHandler(var5, var6));
            } catch (LoginException var33) {
               var2.sendError(401);
               return;
            }

            if (var7 == null) {
               return;
            }

            SecurityServiceManager.pushSubject(kernelId, var7);

            try {
               AccessorUtils.ensureUserAuthorized();
            } catch (Exception var32) {
               if (ACCESSOR_DEBUG.isDebugEnabled()) {
                  ACCESSOR_DEBUG.debug(var32.getMessage());
               }

               var2.sendError(401);
               SecurityServiceManager.popSubject(kernelId);
               return;
            }

            try {
               queryType var36 = this.getQueryType(var1);
               if (var36 == AccessorServlet.queryType.ACCESSOR) {
                  this.handleAccessorQuery(var1, var2);
                  return;
               } else if (var36 == AccessorServlet.queryType.LISTIMAGES) {
                  this.handleListImages(var1, var2);
                  return;
               } else if (var36 == AccessorServlet.queryType.GETIMAGE) {
                  this.handleGetImage(var1, var2);
                  return;
               } else {
                  if (var36 == AccessorServlet.queryType.GETIMAGEENTRY) {
                     this.handleGetImageEntry(var1, var2);
                  } else {
                     if (ACCESSOR_DEBUG.isDebugEnabled()) {
                        ACCESSOR_DEBUG.debug("invalid query type returned");
                     }

                     var2.sendError(500);
                  }

                  return;
               }
            } catch (Exception var30) {
               var2.sendError(500);
               return;
            } finally {
               SecurityServiceManager.popSubject(kernelId);
            }
         }

         var2.sendError(401);
      } finally {
         if (var17) {
            InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_0, _WLDF$INST_FLD_Servlet_Request_Action_Around_Medium, var18, var19);
         }

      }

   }

   private queryType getQueryType(HttpServletRequest var1) {
      if (var1.getParameter("listAvailableImages") != null) {
         return AccessorServlet.queryType.LISTIMAGES;
      } else if (var1.getParameter("getImage") != null) {
         return var1.getParameter("getImageEntry") != null ? AccessorServlet.queryType.GETIMAGEENTRY : AccessorServlet.queryType.GETIMAGE;
      } else {
         return AccessorServlet.queryType.ACCESSOR;
      }
   }

   private void handleAccessorQuery(HttpServletRequest var1, HttpServletResponse var2) throws ServletException, IOException, ManagementException, XMLStreamException {
      var2.setContentType("text/xml");
      String var3 = var1.getParameter("logicalName");
      if (var3 == null || var3.length() == 0) {
         var3 = "ServerLog";
      }

      if (ACCESSOR_DEBUG.isDebugEnabled()) {
         ACCESSOR_DEBUG.debug("Logical name = " + var3);
      }

      String var4 = var1.getParameter("query");
      if (var4 == null) {
         var4 = "";
      }

      if (ACCESSOR_DEBUG.isDebugEnabled()) {
         ACCESSOR_DEBUG.debug("Query = " + var4);
      }

      long var5 = 0L;
      String var7 = var1.getParameter("beginTimestamp");
      if (var7 != null && var7.length() >= 0) {
         var5 = Long.parseLong(var7);
      }

      if (ACCESSOR_DEBUG.isDebugEnabled()) {
         ACCESSOR_DEBUG.debug("Begin timestamp = " + var5);
      }

      long var8 = Long.MAX_VALUE;
      var7 = var1.getParameter("endTimestamp");
      if (var7 != null && var7.length() >= 0) {
         var8 = Long.parseLong(var7);
      }

      if (ACCESSOR_DEBUG.isDebugEnabled()) {
         ACCESSOR_DEBUG.debug("End timestamp = " + var8);
      }

      DiagnosticAccessRuntime var10 = DiagnosticAccessRuntime.getInstance();
      WLDFDataAccessRuntimeMBean var11 = var10.lookupWLDFDataAccessRuntime(var3);
      XMLDataWriter var12 = new XMLDataWriter(new BufferedOutputStream(var2.getOutputStream()), false);
      var12.exportDiagnosticDataToXML(var11.getColumns(), var11.retrieveDataRecords(var5, var8, var4));
   }

   private void handleListImages(HttpServletRequest var1, HttpServletResponse var2) throws ServletException, IOException {
      if (ACCESSOR_DEBUG.isDebugEnabled()) {
         ACCESSOR_DEBUG.debug("handleListImages");
      }

      String[] var3 = ImageManager.getInstance().getAvailableCapturedImages();
      ServletOutputStream var4 = var2.getOutputStream();
      ObjectOutputStream var5 = new ObjectOutputStream(new BufferedOutputStream(var4));
      var5.writeObject(var3);
      var5.flush();
   }

   private void handleGetImage(HttpServletRequest var1, HttpServletResponse var2) throws ServletException, IOException {
      if (ACCESSOR_DEBUG.isDebugEnabled()) {
         ACCESSOR_DEBUG.debug("handleGetImage");
      }

      String var3 = var1.getParameter("getImage");
      if (var3 == null) {
         throw new ServletException(DiagnosticsTextTextFormatter.getInstance().getAccessorClientInvalidImageName(var3));
      } else {
         ImageManager var4 = ImageManager.getInstance();
         File var5 = var4.findImageFile(var3);
         FileInputStream var6 = new FileInputStream(var5);
         ServletOutputStream var7 = var2.getOutputStream();

         try {
            StreamUtils.writeTo(var6, var7);
            var7.flush();
         } finally {
            if (var6 != null) {
               var6.close();
            }

         }

      }
   }

   private void handleGetImageEntry(HttpServletRequest var1, HttpServletResponse var2) throws ServletException, IOException {
      if (ACCESSOR_DEBUG.isDebugEnabled()) {
         ACCESSOR_DEBUG.debug("handleGetImageEntry");
      }

      String var3 = var1.getParameter("getImage");
      if (var3 == null) {
         throw new ServletException(DiagnosticsTextTextFormatter.getInstance().getAccessorClientInvalidImageName(var3));
      } else {
         String var4 = var1.getParameter("getImageEntry");
         if (var4 == null) {
            throw new ServletException(DiagnosticsTextTextFormatter.getInstance().getAccessorClientInvalidImageEntryName(var3, var4));
         } else {
            ImageManager var5 = ImageManager.getInstance();
            ZipFile var6 = null;
            ZipEntry var7 = null;
            InputStream var8 = null;

            try {
               File var9 = var5.findImageFile(var3);
               var6 = new ZipFile(var9);
               var7 = var6.getEntry(var4);
               if (var7 == null) {
                  throw new ServletException(DiagnosticsTextTextFormatter.getInstance().getAccessorClientImageEntryNotFound(var3, var4));
               }

               ServletOutputStream var10 = var2.getOutputStream();
               var8 = var6.getInputStream(var7);
               StreamUtils.writeTo(var8, var10);
               var10.flush();
            } finally {
               if (var8 != null) {
                  var8.close();
               }

               if (var6 != null) {
                  var6.close();
               }

            }

         }
      }
   }

   static {
      _WLDF$INST_FLD_Servlet_Request_Action_Around_Medium = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "Servlet_Request_Action_Around_Medium");
      _WLDF$INST_JPFLD_0 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "AccessorServlet.java", "weblogic.diagnostics.accessor.AccessorServlet", "doGet", "(Ljavax/servlet/http/HttpServletRequest;Ljavax/servlet/http/HttpServletResponse;)V", 92, InstrumentationSupport.makeMap(new String[]{"Servlet_Request_Action_Around_Medium"}, new PointcutHandlingInfo[]{InstrumentationSupport.createPointcutHandlingInfo((ValueHandlingInfo)null, (ValueHandlingInfo)null, new ValueHandlingInfo[]{InstrumentationSupport.createValueHandlingInfo("req", "weblogic.diagnostics.instrumentation.gathering.ServletRequestRenderer", false, true), null})}), (boolean)0);
      ACCESSOR_DEBUG = DebugLogger.getDebugLogger("DebugDiagnosticAccessor");
      kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
      runtimeAccess = ManagementService.getRuntimeAccess(kernelId);
   }

   private static enum queryType {
      ACCESSOR,
      LISTIMAGES,
      GETIMAGE,
      GETIMAGEENTRY;
   }
}
