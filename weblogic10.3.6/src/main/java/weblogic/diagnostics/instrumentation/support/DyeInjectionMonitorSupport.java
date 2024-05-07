package weblogic.diagnostics.instrumentation.support;

import java.lang.reflect.Method;
import java.security.Principal;
import javax.servlet.http.Cookie;
import weblogic.diagnostics.context.DiagnosticContext;
import weblogic.diagnostics.context.DiagnosticContextConstants;
import weblogic.diagnostics.context.DiagnosticContextFactory;
import weblogic.diagnostics.instrumentation.DiagnosticMonitor;
import weblogic.diagnostics.instrumentation.DiagnosticMonitorControl;
import weblogic.diagnostics.instrumentation.InstrumentationConstants;
import weblogic.diagnostics.instrumentation.InstrumentationDebug;
import weblogic.diagnostics.instrumentation.InstrumentationLibrary;
import weblogic.diagnostics.type.UnexpectedExceptionHandler;
import weblogic.rmi.spi.Channel;
import weblogic.rmi.spi.InboundRequest;
import weblogic.security.SubjectUtils;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.servlet.internal.ServletRequestImpl;

public final class DyeInjectionMonitorSupport implements DiagnosticContextConstants, InstrumentationConstants {
   private static DiagnosticMonitorControl dyeInjectionMonitor = null;
   private static boolean computeDyes;
   private static final long ADDR_USER_MASK = 255L;
   private static final String[] dyeInjectionPointcutClasses = new String[]{"weblogic.rmi.internal.BasicServerRef", "weblogic.servlet.internal.WebAppServletContext"};
   private static final String ENABLE_WLDF_DYE_INJECTION_METHOD_NAME = "enableWLDFDyeInjection";
   private static final String ENABLE_DYE_INJECTION_METHOD_NAME = "enableDyeInjection";
   private static long throttleInterval = -1L;
   private static long throttleRate = -1L;
   private static long reqNumber;
   private static long lastReqTime;
   private static boolean throttlingEnabled;
   private static String propADDR1;
   private static String propADDR2;
   private static String propADDR3;
   private static String propADDR4;
   private static boolean checkADDR;
   private static String propUSER1;
   private static String propUSER2;
   private static String propUSER3;
   private static String propUSER4;
   private static boolean checkUSER;
   private static String propCOOKIE1;
   private static String propCOOKIE2;
   private static String propCOOKIE3;
   private static String propCOOKIE4;
   private static boolean checkCOOKIE;

   public static synchronized void setDyeInjectionMonitor(DiagnosticMonitorControl var0) {
      dyeInjectionMonitor = var0;
      captureProperties(var0);
      Class[] var1 = new Class[]{Boolean.class};
      Object[] var2 = new Object[]{new Boolean(var0 != null)};

      for(int var3 = 0; var3 < dyeInjectionPointcutClasses.length; ++var3) {
         try {
            Class var4 = Class.forName(dyeInjectionPointcutClasses[var3]);
            Method var5 = var4.getMethod("enableWLDFDyeInjection", var1);
            var5.invoke((Object)null, var2);
         } catch (Throwable var9) {
            UnexpectedExceptionHandler.handle("Unexpected exception in setDyeInjectionMonitor", var9);
         }
      }

      var1[0] = DiagnosticMonitor.class;
      var2[0] = var0;
      String[] var10 = InstrumentationLibrary.getInstrumentationLibrary().getInstrumentationEngineConfiguration().getEntryClasses();
      int var11 = var10 != null ? var10.length : 0;

      for(int var12 = 0; var12 < var11; ++var12) {
         try {
            Class var6 = Class.forName(var10[var12]);
            Method var7 = var6.getMethod("enableDyeInjection", var1);
            var7.invoke((Object)null, var2);
         } catch (Throwable var8) {
            UnexpectedExceptionHandler.handle("Unexpected exception in setDyeInjectionMonitor", var8);
         }
      }

   }

   public static boolean isThrottlingEnabled() {
      return throttlingEnabled;
   }

   private static void captureProperties(DiagnosticMonitorControl var0) {
      computeDyes = var0 != null ? var0.isEnabled() : false;
      throttleInterval = getLongAttribute(var0, "THROTTLE_INTERVAL");
      throttleRate = getLongAttribute(var0, "THROTTLE_RATE");
      throttlingEnabled = throttleInterval > 0L || throttleRate > 0L;
      propADDR1 = getAttribute(var0, "ADDR1");
      propADDR2 = getAttribute(var0, "ADDR2");
      propADDR3 = getAttribute(var0, "ADDR3");
      propADDR4 = getAttribute(var0, "ADDR4");
      checkADDR = propADDR1 != null || propADDR2 != null || propADDR3 != null || propADDR4 != null;
      propUSER1 = getAttribute(var0, "USER1");
      propUSER2 = getAttribute(var0, "USER2");
      propUSER3 = getAttribute(var0, "USER3");
      propUSER4 = getAttribute(var0, "USER4");
      checkUSER = propUSER1 != null || propUSER2 != null || propUSER3 != null || propUSER4 != null;
      propCOOKIE1 = getAttribute(var0, "COOKIE1");
      propCOOKIE2 = getAttribute(var0, "COOKIE2");
      propCOOKIE3 = getAttribute(var0, "COOKIE3");
      propCOOKIE4 = getAttribute(var0, "COOKIE4");
      checkCOOKIE = propCOOKIE1 != null || propCOOKIE2 != null || propCOOKIE3 != null || propCOOKIE4 != null;
   }

   private static long getLongAttribute(DiagnosticMonitorControl var0, String var1) {
      long var2 = -1L;

      try {
         String var4 = getAttribute(var0, var1);
         var2 = Long.parseLong(var4);
      } catch (Exception var5) {
      }

      if (var2 <= 0L) {
         var2 = -1L;
      }

      return var2;
   }

   private static String getAttribute(DiagnosticMonitorControl var0, String var1) {
      String var2 = var0 != null ? var0.getAttribute(var1) : null;
      if (var2 != null) {
         var2 = var2.trim();
         if (var2.length() == 0) {
            var2 = null;
         }
      }

      return var2;
   }

   private static long computeThrottleDye() {
      if (throttleInterval <= 0L && throttleRate <= 0L) {
         return 4294967296L;
      } else {
         long var0 = System.currentTimeMillis();
         long var2 = 0L;
         if (throttleInterval > 0L && var0 - lastReqTime >= throttleInterval) {
            var2 = 4294967296L;
         }

         if (throttleRate > 0L && reqNumber >= throttleRate) {
            var2 = 4294967296L;
         }

         ++reqNumber;
         if (var2 != 0L) {
            reqNumber = 0L;
            lastReqTime = var0;
         }

         return var2;
      }
   }

   private static void dyeWebAppRequest(Object var0, DiagnosticMonitorControl var1) {
      try {
         DiagnosticContext var2 = getDiagnosticContext();
         if (var2 == null) {
            return;
         }

         if (!computeDyes) {
            return;
         }

         ServletRequestImpl var3 = (ServletRequestImpl)var0;
         long var4 = var2.getDyeVector();
         var4 |= computeThrottleDye();
         var4 |= 2097152L;
         if (var3.isSecure()) {
            var4 |= 67108864L;
         }

         if (checkADDR) {
            String var6 = var3.getRemoteAddr();
            var4 |= computeAddressDye(var6);
         }

         if (checkUSER) {
            Principal var9 = var3.getUserPrincipal();
            String var7 = var9 != null ? var9.getName() : null;
            var4 |= computeUsernameDye(var7);
         }

         if (checkCOOKIE) {
            var4 |= computeCookiesDye(var3.getCookies());
         }

         setDye(var2, var4);
      } catch (Throwable var8) {
         UnexpectedExceptionHandler.handle("Unexpected exception in DyeInjectionMonitorSupport", var8);
      }

   }

   public static void dyeWebAppRequest(Object var0) {
      if (dyeInjectionMonitor != null) {
         dyeWebAppRequest(var0, dyeInjectionMonitor);
      }

   }

   private static void dyeRMIRequest(Object var0, DiagnosticMonitorControl var1) {
      try {
         DiagnosticContext var2 = getDiagnosticContext();
         if (var2 == null) {
            return;
         }

         if (!computeDyes) {
            return;
         }

         InboundRequest var3 = (InboundRequest)var0;
         long var4 = var2.getDyeVector();
         var4 |= computeThrottleDye();
         Channel var6 = var3.getEndPoint().getRemoteChannel();
         String var7;
         if (checkADDR) {
            var7 = var6.getInetAddress().getHostAddress();
            var4 |= computeAddressDye(var7);
         }

         if (checkUSER) {
            AuthenticatedSubject var10 = (AuthenticatedSubject)var3.getSubject();
            if (var10 != null) {
               String var8 = SubjectUtils.getUsername(var10);
               var4 |= computeUsernameDye(var8);
            }
         }

         var7 = var6.getProtocolPrefix();
         var4 |= computeProtocolDye(var7);
         var4 |= 4194304L;
         setDye(var2, var4);
      } catch (Throwable var9) {
         UnexpectedExceptionHandler.handle("Unexpected exception in DyeInjectionMonitorSupport", var9);
      }

   }

   public static void dyeRMIRequest(Object var0) {
      if (dyeInjectionMonitor != null) {
         dyeRMIRequest(var0, dyeInjectionMonitor);
      }

   }

   private static void setDye(DiagnosticContext var0, long var1) {
      var1 |= var0.getDyeVector();
      var0.setDyeVector(var1);
      if (InstrumentationDebug.DEBUG_ACTIONS.isDebugEnabled()) {
         InstrumentationDebug.DEBUG_ACTIONS.debug("dyeWebAppRequest: setting request dye to: " + var1);
      }

   }

   private static long computeAddressDye(String var0) {
      long var1 = 0L;
      if (var0 != null) {
         if (var0.equals(propADDR1)) {
            var1 |= 1L;
         }

         if (var0.equals(propADDR2)) {
            var1 |= 2L;
         }

         if (var0.equals(propADDR3)) {
            var1 |= 4L;
         }

         if (var0.equals(propADDR4)) {
            var1 |= 8L;
         }
      }

      return var1;
   }

   private static long computeUsernameDye(String var0) {
      long var1 = 0L;
      if (var0 != null) {
         if (var0.equals(propUSER1)) {
            var1 |= 16L;
         }

         if (var0.equals(propUSER2)) {
            var1 |= 32L;
         }

         if (var0.equals(propUSER3)) {
            var1 |= 64L;
         }

         if (var0.equals(propUSER4)) {
            var1 |= 128L;
         }
      }

      return var1;
   }

   private static long computeCookiesDye(Cookie[] var0) {
      int var1 = var0 != null ? var0.length : 0;

      for(int var2 = 0; var2 < var1; ++var2) {
         Cookie var3 = var0[var2];
         if (var3.getName().equals("weblogic.diagnostics.dye")) {
            String var4 = var3.getValue();
            long var5 = 0L;
            if (var4 != null) {
               if (var4.equals(propCOOKIE1)) {
                  var5 |= 256L;
               }

               if (var4.equals(propCOOKIE2)) {
                  var5 |= 512L;
               }

               if (var4.equals(propCOOKIE3)) {
                  var5 |= 1024L;
               }

               if (var4.equals(propCOOKIE4)) {
                  var5 |= 2048L;
               }
            }

            return var5;
         }
      }

      return 0L;
   }

   private static long computeProtocolDye(String var0) {
      var0 = var0.toLowerCase();
      if (var0.equals("t3")) {
         return 1048576L;
      } else if (var0.equals("t3s")) {
         return 68157440L;
      } else if (var0.equals("http")) {
         return 2097152L;
      } else if (var0.equals("https")) {
         return 69206016L;
      } else if (var0.equals("iiop")) {
         return 16777216L;
      } else if (var0.equals("iiops")) {
         return 83886080L;
      } else {
         return var0.equals("jrmp") ? 33554432L : 0L;
      }
   }

   private static DiagnosticContext getDiagnosticContext() {
      return DiagnosticContextFactory.findOrCreateDiagnosticContext(true);
   }

   public static void setDyes(long var0, long var2, String var4, String var5) {
      DiagnosticContext var6 = getDiagnosticContext();
      var2 |= 255L;
      var0 |= computeUsernameDye(var5);
      var0 |= computeAddressDye(var4);
      long var7 = var6.getDyeVector();
      var0 = var7 & ~var2 | var0 & var2;
      var6.setDyeVector(var0);
   }
}
