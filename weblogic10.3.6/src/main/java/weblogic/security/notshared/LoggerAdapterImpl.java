package weblogic.security.notshared;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.logging.Logger;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.logging.WLLevel;
import weblogic.security.SecurityEnvironment;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.security.shared.LoggerAdapter;

public class LoggerAdapterImpl implements LoggerAdapter {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final String WLS_PACKAGE = "weblogic.security.providers.";
   private static final int WLS_PACKAGE_OFFSET = "weblogic.security.providers.".length();
   private static final String WLES_PACKAGE = "com.bea.security.providers.";
   private static final int WLES_PACKAGE_OFFSET = "com.bea.security.providers.".length();
   private HashMap loggers = null;
   private boolean appletEnvironment = false;

   public LoggerAdapterImpl() {
      this.convertLegacyDebugSystemProperties();
      this.loggers = new HashMap();
   }

   private void run(PrivilegedAction var1) {
      SecurityServiceManager.runAs(kernelId, kernelId, var1);
   }

   public boolean isDebugEnabled(Object var1) {
      DebugLogger var2 = this.getDebugLogger(var1);
      if (var2 == null) {
         Logger var3 = this.getLogger(var1);
         return var3 == null ? false : var3.isLoggable(WLLevel.DEBUG);
      } else {
         return var2.isDebugEnabled();
      }
   }

   private void convertLegacyDebugSystemProperties() {
      this.mapOldSecurityDebugProperty("ssl.debug", "DebugSecuritySSL");
      this.mapOldSecurityDebugProperty("weblogic.security.SSL.verbose", "DebugSecuritySSL");
      this.mapOldSecurityDebugProperty("weblogic.security.ssl.verbose", "DebugSecuritySSL");
      this.mapOldSecurityDebugProperty("ssl.debugEaten", "DebugSecuritySSLEaten");
      this.mapOldSecurityDebugProperty("weblogic.security.SSL.debugEaten", "DebugSecuritySSLEaten");
      this.mapOldSecurityDebugProperty("weblogic.security.ssl.debugEaten", "DebugSecuritySSLEaten");
      this.mapOldSecurityDebugProperty("weblogic.security.DebugSecurityAtn", "DebugSecurityAtn");
      this.mapOldSecurityDebugProperty("weblogic.security.providers.authentication.WIndowsNTAuthenticator.debug", "DebugSecurityAtn");
      this.mapOldSecurityDebugProperty("weblogic.security.providers.authentication.DebugSecurityAtn", "DebugSecurityAtn");
      this.mapOldSecurityDebugProperty("weblogic.security.realm.debug", "DebugSecurityRealm");
   }

   private void mapOldSecurityDebugProperty(String var1, String var2) {
      try {
         String var3 = System.getProperty(var1);
         if (var3 == null) {
            return;
         }

         System.setProperty("weblogic.debug." + var2, var3);
      } catch (SecurityException var4) {
         this.appletEnvironment = true;
      }

   }

   public synchronized Object getLogger(String var1) {
      LoggerHolder var2 = (LoggerHolder)this.loggers.get(var1);
      if (var2 != null) {
         return var2;
      } else {
         Logger var3 = null;
         DebugLogger var4 = null;
         var3 = this.getServerLogger();
         if (var1 != null) {
            if (var1.startsWith("Security")) {
               var4 = this.getDebugLogger("Debug" + var1);
            } else if (var1.startsWith("Debug")) {
               var4 = this.getDebugLogger(var1);
            } else {
               String var5;
               if (var1.startsWith("weblogic.security.providers.")) {
                  var5 = var1.substring(WLS_PACKAGE_OFFSET);
                  if (var5 != null) {
                     if (var5.startsWith("authentication")) {
                        var4 = this.getDebugLogger("DebugSecurityAtn");
                     } else if (var5.startsWith("authorization")) {
                        var4 = this.getDebugLogger("DebugSecurityAtz");
                     } else if (var5.startsWith("audit")) {
                        var4 = this.getDebugLogger("DebugSecurityAuditor");
                     } else if (var5.startsWith("credentials")) {
                        var4 = this.getDebugLogger("DebugSecurityCredMap");
                     } else if (var5.startsWith("pk")) {
                        var4 = this.getDebugLogger("DebugSecurityKeyStore");
                     } else if (var5.startsWith("profiles")) {
                        var4 = this.getDebugLogger("DebugSecurityProfiler");
                     } else if (var5.startsWith("realmadapter")) {
                        var4 = this.getDebugLogger("DebugSecurityRealm");
                     } else if (var5.startsWith("saml")) {
                        var4 = this.getDebugLogger("DebugSecurityAtn");
                     }
                  }
               } else if (var1.startsWith("com.bea.security.providers.")) {
                  var5 = var1.substring(WLES_PACKAGE_OFFSET);
                  if (var5 != null) {
                     if (var5.startsWith("authentication")) {
                        var4 = this.getDebugLogger("DebugSecurityAtn");
                     } else if (var5.startsWith("authorization")) {
                        var4 = this.getDebugLogger("DebugSecurityAtz");
                     } else if (var5.startsWith("audit")) {
                        var4 = this.getDebugLogger("DebugSecurityAuditor");
                     } else if (var5.startsWith("credentials")) {
                        var4 = this.getDebugLogger("DebugSecurityCredMap");
                     } else if (var5.startsWith("pk")) {
                        var4 = this.getDebugLogger("DebugSecurityKeyStore");
                     } else if (var5.startsWith("profiles")) {
                        var4 = this.getDebugLogger("DebugSecurityProfiler");
                     } else if (var5.startsWith("realmadapter")) {
                        var4 = this.getDebugLogger("DebugSecurityRealm");
                     } else if (var5.startsWith("saml")) {
                        var4 = this.getDebugLogger("DebugSecurityAtn");
                     }
                  }
               } else if (var1.startsWith("weblogic.entitlement")) {
                  var4 = this.getDebugLogger("DebugSecurityEEngine");
               } else {
                  var4 = this.getDebugLogger(var1);
               }
            }
         }

         if (var3 != null || var4 != null) {
            var2 = new LoggerHolder(var3, var4);
            this.loggers.put(var1, var2);
         }

         return var2;
      }
   }

   private Logger getServerLogger() {
      if (this.appletEnvironment) {
         return null;
      } else {
         try {
            return SecurityEnvironment.getSecurityEnvironment().getServerLogger();
         } catch (SecurityException var2) {
            return null;
         }
      }
   }

   private DebugLogger getDebugLogger(String var1) {
      if (this.appletEnvironment) {
         return null;
      } else {
         try {
            return DebugLogger.getDebugLogger(var1);
         } catch (SecurityException var3) {
            return null;
         }
      }
   }

   public void debug(Object var1, final Object var2) {
      final DebugLogger var3 = this.getDebugLogger(var1);
      if (var3 == null) {
         final Logger var4 = this.getLogger(var1);
         if (var4 != null) {
            this.run(new PrivilegedAction() {
               public Object run() {
                  var4.log(WLLevel.DEBUG, var2.toString());
                  return null;
               }
            });
         }

      } else {
         this.run(new PrivilegedAction() {
            public Object run() {
               var3.debug(var2.toString());
               return null;
            }
         });
      }
   }

   public void debug(Object var1, final Object var2, final Throwable var3) {
      final DebugLogger var4 = this.getDebugLogger(var1);
      if (var4 == null) {
         final Logger var5 = this.getLogger(var1);
         if (var5 != null) {
            this.run(new PrivilegedAction() {
               public Object run() {
                  var5.log(WLLevel.DEBUG, var2.toString(), var3);
                  return null;
               }
            });
         }

      } else {
         this.run(new PrivilegedAction() {
            public Object run() {
               var4.debug(var2.toString(), var3);
               return null;
            }
         });
      }
   }

   public void info(Object var1, final Object var2) {
      final Logger var3 = this.getLogger(var1);
      if (var3 != null) {
         this.run(new PrivilegedAction() {
            public Object run() {
               var3.log(WLLevel.INFO, var2.toString());
               return null;
            }
         });
      }

   }

   public void info(Object var1, final Object var2, final Throwable var3) {
      final Logger var4 = this.getLogger(var1);
      if (var4 != null) {
         this.run(new PrivilegedAction() {
            public Object run() {
               var4.log(WLLevel.INFO, var2.toString(), var3);
               return null;
            }
         });
      }

   }

   public void warn(Object var1, final Object var2) {
      final Logger var3 = this.getLogger(var1);
      if (var3 != null) {
         this.run(new PrivilegedAction() {
            public Object run() {
               var3.log(WLLevel.WARNING, var2.toString());
               return null;
            }
         });
      }

   }

   public void warn(Object var1, final Object var2, final Throwable var3) {
      final Logger var4 = this.getLogger(var1);
      if (var4 != null) {
         this.run(new PrivilegedAction() {
            public Object run() {
               var4.log(WLLevel.WARNING, var2.toString(), var3);
               return null;
            }
         });
      }

   }

   public void error(Object var1, final Object var2) {
      final Logger var3 = this.getLogger(var1);
      if (var3 != null) {
         this.run(new PrivilegedAction() {
            public Object run() {
               var3.log(WLLevel.ERROR, var2.toString());
               return null;
            }
         });
      }

   }

   public void error(Object var1, final Object var2, final Throwable var3) {
      final Logger var4 = this.getLogger(var1);
      if (var4 != null) {
         this.run(new PrivilegedAction() {
            public Object run() {
               var4.log(WLLevel.ERROR, var2.toString(), var3);
               return null;
            }
         });
      }

   }

   public void severe(Object var1, final Object var2) {
      final Logger var3 = this.getLogger(var1);
      if (var3 != null) {
         this.run(new PrivilegedAction() {
            public Object run() {
               var3.log(WLLevel.CRITICAL, var2.toString());
               return null;
            }
         });
      }

   }

   public void severe(Object var1, final Object var2, final Throwable var3) {
      final Logger var4 = this.getLogger(var1);
      if (var4 != null) {
         this.run(new PrivilegedAction() {
            public Object run() {
               var4.log(WLLevel.CRITICAL, var2.toString(), var3);
               return null;
            }
         });
      }

   }

   private final DebugLogger getDebugLogger(Object var1) {
      return var1 == null ? null : ((LoggerHolder)((LoggerHolder)var1)).getDebugLogger();
   }

   private final Logger getLogger(Object var1) {
      return var1 == null ? null : ((LoggerHolder)((LoggerHolder)var1)).getLogger();
   }

   private class LoggerHolder {
      private Logger logger = null;
      private DebugLogger debugLogger = null;

      private LoggerHolder() {
      }

      public LoggerHolder(Logger var2, DebugLogger var3) {
         this.logger = var2;
         this.debugLogger = var3;
      }

      public final Logger getLogger() {
         return this.logger;
      }

      public final DebugLogger getDebugLogger() {
         return this.debugLogger;
      }
   }
}
