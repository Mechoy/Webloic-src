package weblogic.servlet.security.internal;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.AccessController;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Map;
import javax.security.auth.login.LoginException;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.security.SubjectUtils;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrincipalAuthenticator;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.security.service.SecurityService.ServiceType;
import weblogic.servlet.HTTPLogger;
import weblogic.servlet.internal.ServletRequestImpl;
import weblogic.servlet.internal.VirtualConnection;
import weblogic.servlet.internal.WebAppServletContext;
import weblogic.servlet.internal.session.SessionInternal;
import weblogic.utils.encoders.BASE64Decoder;

public final class CertSecurityModule extends SecurityModule {
   private static final String CERT_RESERVED_IP = "IP";
   private static final String CERT_RESERVED_KEYSIZE = "Keysize";
   private static final String CERT_RESERVED_SECRETKEYSIZE = "SecretKeysize";
   private static final boolean protectResourceIfUnspecifiedConstraint = Boolean.getBoolean("weblogic.http.security.cert.protectResourceIfUnspecifiedConstraint");
   protected static final DebugLogger DEBUG_IA = DebugLogger.getDebugLogger("DebugWebAppIdentityAssertion");
   private static final AuthenticatedSubject KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private final PrincipalAuthenticator pa;
   private final boolean alwaysAssert;

   public CertSecurityModule(WebAppServletContext var1, WebAppSecurity var2, boolean var3, boolean var4) {
      super(var1, var2, var3);
      this.alwaysAssert = var4;
      this.pa = (PrincipalAuthenticator)SecurityServiceManager.getSecurityService(KERNEL_ID, var1.getSecurityRealmName(), ServiceType.AUTHENTICATION);
   }

   boolean checkUserPerm(HttpServletRequest var1, HttpServletResponse var2, SessionInternal var3, ResourceConstraint var4, AuthenticatedSubject var5, boolean var6) throws IOException, ServletException {
      ServletRequestImpl var7 = ServletRequestImpl.getOriginalRequest(var1);
      boolean var8 = false;
      if (this.alwaysAssert || var5 == null || SubjectUtils.isUserAnonymous(var5)) {
         AuthenticatedSubject var9 = this.assertIdentity(var1, var2, var7.getConnection(), this.getServletContext());
         if (var9 != null && var5 != var9) {
            var5 = var9;
            var8 = true;
         }
      }

      boolean var10 = false;
      if (protectResourceIfUnspecifiedConstraint) {
         var10 = var4 == null && !this.webAppSecurity.isFullSecurityDelegationRequired() || var5 != null && this.webAppSecurity.hasPermission(var1, var2, var5, var4);
      } else {
         var10 = this.webAppSecurity.hasPermission(var1, var2, var5, var4);
      }

      if (!var10) {
         if (DEBUG_IA.isDebugEnabled()) {
            DEBUG_IA.debug("Permission check failed for " + var1.toString());
         }

         if (this.isForbidden(var4) || var5 != null && !this.isReloginEnabled()) {
            this.sendForbiddenResponse(var1, var2);
         } else {
            if (var6 && this.webAppSecurity.hasAuthFilters()) {
               this.invokeAuthFilterChain(var1, var2);
               return false;
            }

            this.sendUnauthorizedResponse(var1, var2);
         }

         return false;
      } else if (!this.checkAuthCookie(this.getHttpServer(), var1, var3)) {
         if (DEBUG_SEC.isDebugEnabled()) {
            DEBUG_SEC.debug("AuthCookie not found - permission denied for " + var1);
         }

         this.sendUnauthorizedResponse(var1, var2);
         return false;
      } else if (!var8) {
         return true;
      } else {
         this.login(var1, var5, var3);
         if (DEBUG_SEC.isDebugEnabled()) {
            DEBUG_SEC.debug(this.webAppSecurity.getContextLog() + ": user: " + getUsername(var5) + " has permissions to access " + var1);
         }

         return true;
      }
   }

   protected AuthenticatedSubject assertIdentity(HttpServletRequest var1, HttpServletResponse var2, VirtualConnection var3, WebAppServletContext var4) {
      try {
         if (DEBUG_IA.isDebugEnabled()) {
            DEBUG_IA.debug("Trying to find identity assertion tokens for " + var1);
         }

         Token var5 = findToken(var1, var3, var4, this.pa);
         if (var5 == null) {
            if (DEBUG_IA.isDebugEnabled()) {
               DEBUG_IA.debug("Didn't find any token!");
            }

            return null;
         }

         if (DEBUG_IA.isDebugEnabled()) {
            DEBUG_IA.debug("assertIdentity with tokem.type: " + var5.type + " token.value: " + var5.value);
         }

         return this.pa.assertIdentity(var5.type, var5.value, WebAppSecurity.getContextHandler(var1, var2));
      } catch (LoginException var6) {
         if (DEBUG_SEC.isDebugEnabled()) {
            DEBUG_SEC.debug("Login failed for request: " + var1.toString(), var6);
         }
      } catch (SecurityException var7) {
         if (DEBUG_IA.isDebugEnabled()) {
            DEBUG_IA.debug("Indentity assertion failed", var7);
         }

         HTTPLogger.logCertAuthenticationError(var1.getRequestURI(), var7);
      }

      return null;
   }

   public static Token findToken(HttpServletRequest var0, VirtualConnection var1, WebAppServletContext var2, PrincipalAuthenticator var3) {
      Map var4 = var3.getAssertionsEncodingMap();
      if (var4 != null && !var4.isEmpty()) {
         if (DEBUG_IA.isDebugEnabled()) {
            DEBUG_IA.debug("AssertionsEncodingMap size: " + var4.size());
         }

         X509Certificate[] var5 = (X509Certificate[])((X509Certificate[])var0.getAttribute("javax.servlet.request.X509Certificate"));
         if (var5 != null && var5.length > 0 && var4.containsKey("X.509")) {
            return new Token("X.509", var5);
         } else {
            ArrayList var6 = var1.getPerimeterAuthClientCertType();
            int var7 = var6.size();
            if (var7 > 0) {
               ArrayList var8 = var1.getPerimeterAuthClientCert();

               for(int var9 = var7 - 1; var9 >= 0; --var9) {
                  String var10 = (String)var6.get(var9);
                  if (var4.containsKey(var10) && !isForbiddenTokenType(var10)) {
                     byte[] var11 = decodeCert(var10, (byte[])((byte[])var8.get(var9)));
                     if (var11 != null) {
                        return new Token(var10, var11);
                     }
                  }
               }
            }

            Enumeration var19 = var0.getHeaderNames();
            String var20 = null;
            ServletRequestImpl var21 = null;
            boolean var22 = true;
            if (var0 instanceof ServletRequestImpl) {
               var22 = false;
               var21 = (ServletRequestImpl)var0;
            }

            byte[] var13;
            do {
               Object var12;
               do {
                  do {
                     String var14;
                     do {
                        do {
                           if (!var19.hasMoreElements()) {
                              Cookie[] var23 = var0.getCookies();
                              if (var23 == null) {
                                 return null;
                              }

                              String var24 = null;
                              var14 = null;

                              for(int var15 = 0; var15 < var23.length; ++var15) {
                                 var24 = var23[var15].getName();
                                 var14 = var23[var15].getValue();
                                 if (var14 != null && var14.length() >= 1) {
                                    byte[] var17;
                                    if (var24.length() > 16 && "WL-Proxy-Client-".regionMatches(true, 0, var23[var15].getName(), 0, 16)) {
                                       String var25 = var24.substring(16);
                                       if (var4.containsKey(var25)) {
                                          var17 = decodeCert(var25, var14.getBytes());
                                          if (var17 != null) {
                                             return new Token(var25, var17);
                                          }
                                       }
                                    } else {
                                       Object var16 = var4.get(var24);
                                       if (var16 != null) {
                                          if (!var3.doesTokenRequireBase64Decoding(var16)) {
                                             return new Token(var24, var14.getBytes());
                                          }

                                          var17 = decodeCert(var24, var14.getBytes());
                                          if (var17 != null) {
                                             return new Token(var24, var17);
                                          }
                                       }
                                    }
                                 }
                              }

                              return null;
                           }

                           var20 = (String)var19.nextElement();
                        } while("Cookie".equalsIgnoreCase(var20));

                        var12 = var4.get(var20);
                     } while(var12 == null);

                     var13 = null;
                     if (!var22) {
                        var13 = var21.getRequestHeaders().getHeaderAsBytes(var20);
                     } else {
                        var14 = var0.getHeader(var20);
                        if (var14 != null) {
                           try {
                              var13 = var14.getBytes(getInputEncoding(var0, var2));
                           } catch (UnsupportedEncodingException var18) {
                           }
                        }
                     }
                  } while(var13 == null);
               } while(var13.length < 1);

               if (!var3.doesTokenRequireBase64Decoding(var12)) {
                  break;
               }

               var13 = decodeCert(var20, var13);
            } while(var13 == null);

            return new Token(var20, var13);
         }
      } else {
         if (DEBUG_IA.isDebugEnabled()) {
            DEBUG_IA.debug("AssertionsEncodingMap for active token types was null!!");
         }

         return null;
      }
   }

   private static String getInputEncoding(HttpServletRequest var0, WebAppServletContext var1) {
      String var2 = var0.getCharacterEncoding();
      return var2 != null ? var2 : var1.getConfigManager().getDefaultEncoding();
   }

   private static byte[] decodeCert(String var0, byte[] var1) {
      try {
         ByteArrayInputStream var2 = new ByteArrayInputStream(var1);
         byte[] var3 = (new BASE64Decoder()).decodeBuffer(var2);
         return var3 != null && var3.length >= 1 ? var3 : null;
      } catch (Exception var4) {
         HTTPLogger.logIgnoringClientCert(var0, var4);
         return null;
      }
   }

   private static boolean isForbiddenTokenType(String var0) {
      return var0.equalsIgnoreCase("IP") || var0.equalsIgnoreCase("Keysize") || var0.equalsIgnoreCase("SecretKeysize");
   }

   public static class Token {
      public final String type;
      public final Object value;

      Token(String var1, Object var2) {
         this.type = var1;
         this.value = var2;
      }
   }
}
