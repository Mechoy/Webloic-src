package weblogic.servlet.internal;

import java.io.IOException;
import java.io.PrintStream;
import java.net.SocketTimeoutException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServletRequest;
import weblogic.j2ee.descriptor.ErrorPageBean;
import weblogic.j2ee.descriptor.WebAppBean;
import weblogic.security.SubjectUtils;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.SecurityServiceManager;
import weblogic.servlet.HTTPLogger;
import weblogic.servlet.internal.session.SessionData;
import weblogic.servlet.security.Utils;
import weblogic.servlet.security.internal.SecurityModule;
import weblogic.utils.NestedRuntimeException;
import weblogic.utils.collections.ConcurrentHashMap;
import weblogic.utils.http.HttpParsing;
import weblogic.utils.io.UnsyncByteArrayOutputStream;

public final class ErrorManager {
   public static final String ERROR_PAGE = "weblogic.servlet.errorPage";
   private final WebAppServletContext context;
   private final String[] statusErrors = new String[110];
   private final ConcurrentHashMap exceptionMap = new ConcurrentHashMap();

   ErrorManager(WebAppServletContext var1) {
      this.context = var1;
   }

   String[] getStatusErrors() {
      return this.statusErrors;
   }

   private void registerError(int var1, String var2) {
      try {
         int var3 = var1 - 400;
         if (!WebAppServletContext.isAbsoluteURL(var2)) {
            var2 = HttpParsing.ensureStartingSlash(var2);
         }

         this.statusErrors[var3] = var2;
      } catch (ArrayIndexOutOfBoundsException var4) {
         HTTPLogger.logUnsupportedErrorCode(this.context.getLogContext(), var4);
      }

   }

   private void registerException(String var1, String var2) {
      try {
         Class var3 = this.context.getServletClassLoader().loadClass(var1);
         if (!WebAppServletContext.isAbsoluteURL(var2)) {
            var2 = HttpParsing.ensureStartingSlash(var2);
         }

         this.exceptionMap.put(var3, var2);
      } catch (ClassNotFoundException var4) {
         HTTPLogger.logInvalidExceptionType(var1, var4);
      }

   }

   void registerErrorPages(WebAppBean var1) {
      if (var1 != null) {
         ErrorPageBean[] var2 = var1.getErrorPages();
         if (var2 != null) {
            for(int var3 = 0; var3 < var2.length; ++var3) {
               ErrorPageBean var4 = var2[var3];
               int var5 = var4.getErrorCode();
               String var6 = var4.getExceptionType();
               if (var5 > 0) {
                  this.registerError(var5, var4.getLocation());
               } else if (var6 != null) {
                  synchronized(this) {
                     this.registerException(var6, var4.getLocation());
                  }
               }
            }
         }

      }
   }

   public String getErrorLocation(String var1) {
      try {
         int var2 = Integer.parseInt(var1) - 400;
         return this.statusErrors[var2];
      } catch (NumberFormatException var3) {
         return null;
      } catch (ArrayIndexOutOfBoundsException var4) {
         return null;
      }
   }

   public String getExceptionLocation(Throwable var1) {
      if (var1 == null) {
         return null;
      } else if (this.exceptionMap.isEmpty()) {
         return null;
      } else {
         String var2 = this.findExceptionLocation(var1);
         if (var2 != null) {
            return var2;
         } else if (var1 instanceof ServletException) {
            var1 = ((ServletException)var1).getRootCause();
            return this.findExceptionLocation(var1);
         } else {
            return null;
         }
      }
   }

   void handleException(ServletRequestImpl var1, ServletResponseImpl var2, Throwable var3) throws IOException {
      if (var3 instanceof NestedRuntimeException) {
         var3 = ((NestedRuntimeException)var3).getNestedException();
      }

      String var4 = this.getExceptionLocation(var3);
      if (var4 != null) {
         if (WebAppServletContext.isAbsoluteURL(var4)) {
            var2.sendRedirect(var4);
            return;
         }

         var2.reset();
         RequestDispatcher var5 = this.context.getRequestDispatcher(var4, 3);
         if (var5 != null) {
            var2.setStatus(500);
            this.setErrorAttributes(var1, var1.getInputHelper().getNormalizedURI(), var3);
            AuthenticatedSubject var16 = SecurityModule.getCurrentUser(this.context.getServer(), var1);
            if (var16 == null) {
               var16 = SubjectUtils.getAnonymousSubject();
            }

            ForwardAction var17 = new ForwardAction(var5, var1, var2);
            Throwable var8 = (Throwable)SecurityServiceManager.runAs(WebAppConfigManager.KERNEL_ID, var16, var17);
            if (var8 != null) {
               if (var8 instanceof IOException) {
                  throw (IOException)var8;
               }

               var2.sendError(500);
               HTTPLogger.logSendError(this.context.getLogContext(), var8);
            }

            return;
         }
      }

      short var14;
      try {
         throw var3;
      } catch (UnavailableException var9) {
         var14 = 503;
      } catch (ServletException var10) {
         var14 = 500;
      } catch (SocketTimeoutException var11) {
         var14 = 408;
      } catch (ThreadDeath var12) {
         throw var12;
      } catch (Throwable var13) {
         if (!"Internal Servlet Session Process Error Found!".equals(var13.getMessage())) {
            var14 = 500;
         } else {
            var14 = -900;
         }
      }

      if ((!HttpServer.isProductionModeEnabled() || HTTPDebugLogger.isEnabled()) && var14 != -900) {
         Throwable var15 = (Throwable)var1.getAttribute("javax.servlet.error.exception");
         if (var15 == null) {
            var15 = var3;
         }

         String var7 = this.throwable2StackTraceWithXSSEncodedMessage(var15);
         var7 = "<pre>" + var7 + "</pre>";
         var7 = ErrorMessages.getErrorPage(var14, var7);
         var2.sendError(var14, var7);
      } else if (var14 != -900) {
         var2.sendError(var14);
      } else {
         String var6 = "<H3>This server only licensed to concurrently serve " + SessionData.getCurrProcessedSessionsCount() + " sessions. Please try again later!</H3>";
         var6 = ErrorMessages.getErrorPage(var14, var6);
         var2.sendError(500, var6);
      }

   }

   public void setErrorAttributes(HttpServletRequest var1, String var2, Throwable var3) {
      ServletStubImpl var4 = null;
      String var6 = this.context.getContextPath();
      String var5;
      if (var6 != null && !this.context.isDefaultContext() && var2.startsWith(var6)) {
         var5 = var2.substring(var6.length());
      } else {
         var5 = var2;
      }

      if (var5 != null) {
         var4 = this.context.getServletStub(var5);
      }

      if (var4 != null) {
         var1.setAttribute("javax.servlet.error.servlet_name", var4.getServletName());
      }

      Throwable var7 = (Throwable)var1.getAttribute("javax.servlet.error.exception");
      if (var7 != null) {
         var3 = var7;
      }

      var1.setAttribute("javax.servlet.error.exception_type", var3.getClass());
      var1.setAttribute("javax.servlet.error.exception", var3);
      var1.setAttribute("javax.servlet.error.message", var3.getMessage());
      if (var1.getAttribute("javax.servlet.error.request_uri") == null) {
         var1.setAttribute("javax.servlet.error.request_uri", var2);
      }

      if (var1.getAttribute("javax.servlet.jsp.jspException") == null) {
         var1.setAttribute("javax.servlet.jsp.jspException", var3);
      }

      var1.setAttribute("javax.servlet.error.status_code", new Integer(500));
   }

   private String throwable2StackTraceWithXSSEncodedMessage(Throwable var1) {
      if (var1 == null) {
         var1 = new Throwable("[Null exception passed, creating stack trace for offending caller]");
      }

      UnsyncByteArrayOutputStream var2 = new UnsyncByteArrayOutputStream();
      PrintStream var3 = new PrintStream(var2);
      var3.println(Utils.encodeXSS(var1.toString()));
      StackTraceElement[] var4 = var1.getStackTrace();

      for(int var5 = 0; var5 < var4.length; ++var5) {
         var3.println("\tat " + var4[var5]);
      }

      Throwable var6 = var1.getCause();
      if (var6 != null) {
         this.printStackTraceAsCause(var6, var3, var4);
      }

      return var2.toString();
   }

   private void printStackTraceAsCause(Throwable var1, PrintStream var2, StackTraceElement[] var3) {
      StackTraceElement[] var4 = var1.getStackTrace();
      int var5 = var4.length - 1;

      for(int var6 = var3.length - 1; var5 >= 0 && var6 >= 0 && var4[var5].equals(var3[var6]); --var6) {
         --var5;
      }

      int var7 = var4.length - 1 - var5;
      var2.println("Caused by: " + Utils.encodeXSS(var1.toString()));

      for(int var8 = 0; var8 <= var5; ++var8) {
         var2.println("\tat " + var4[var8]);
      }

      if (var7 != 0) {
         var2.println("\t... " + var7 + " more");
      }

      Throwable var9 = var1.getCause();
      if (var9 != null) {
         this.printStackTraceAsCause(var9, var2, var4);
      }

   }

   private String findExceptionLocation(Throwable var1) {
      if (var1 == null) {
         return null;
      } else {
         String var2 = null;

         for(Class var3 = var1.getClass(); var3 != null; var3 = var3.getSuperclass()) {
            var2 = (String)this.exceptionMap.get(var3);
            if (var2 != null) {
               break;
            }
         }

         return var2;
      }
   }
}
