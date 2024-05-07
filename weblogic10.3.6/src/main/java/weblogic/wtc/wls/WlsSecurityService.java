package weblogic.wtc.wls;

import com.bea.core.jatmi.common.ntrace;
import com.bea.core.jatmi.intf.TCAppKey;
import com.bea.core.jatmi.intf.TCAuthenticatedUser;
import com.bea.core.jatmi.intf.TCSecurityService;
import java.io.Serializable;
import java.security.AccessController;
import javax.security.auth.login.LoginException;
import weblogic.security.WLSPrincipals;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrincipalAuthenticator;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.security.service.SecurityService.ServiceType;
import weblogic.wtc.jatmi.AppKey;
import weblogic.wtc.jatmi.TPException;

public final class WlsSecurityService implements TCSecurityService, Serializable {
   static final long serialVersionUID = -2563145107053158113L;
   private static final AuthenticatedSubject myKid = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final String SEL_LDAP = "LDAP";
   private static final String SEL_CUSTOM = "Custom";
   private ClassLoader _loader = null;
   private String _anon_username = null;
   private PrincipalAuthenticator _pa = null;

   public WlsSecurityService() {
      if (ntrace.isTraceEnabled(4)) {
         ntrace.doTrace("[ WlsSecurityService()");
         this._anon_username = WLSPrincipals.getAnonymousUsername();
         this._pa = (PrincipalAuthenticator)SecurityServiceManager.getSecurityService(myKid, "weblogicDEFAULT", ServiceType.AUTHENTICATION);
         ntrace.doTrace("] WlsSecurityService/10");
      } else {
         this._anon_username = WLSPrincipals.getAnonymousUsername();
         this._pa = (PrincipalAuthenticator)SecurityServiceManager.getSecurityService(myKid, "weblogicDEFAULT", ServiceType.AUTHENTICATION);
      }

   }

   static AuthenticatedSubject getKernelUser() {
      if (ntrace.isTraceEnabled(4)) {
         ntrace.doTrace("[/WlsSecurityService/getKernelUser()");
         ntrace.doTrace("] WlsSecurityService/getKernelUser/10/" + myKid);
      }

      return myKid;
   }

   public void shutdown(int var1) {
   }

   public void pushUser(TCAuthenticatedUser var1) {
      if (ntrace.isTraceEnabled(4)) {
         ntrace.doTrace("[/WlsSecurityService/pushUser(" + var1 + ")");
         var1.setAsCurrentUser();
         ntrace.doTrace("] WlsSecurityService/pushUser/10/");
      } else {
         var1.setAsCurrentUser();
      }

   }

   public TCAuthenticatedUser getUser() {
      AuthenticatedSubject var1;
      if (ntrace.isTraceEnabled(4)) {
         ntrace.doTrace("[/WlsSecurityService/getUser()");
         var1 = SecurityServiceManager.getCurrentSubject(myKid);
         WlsAuthenticatedUser var2 = new WlsAuthenticatedUser(var1);
         ntrace.doTrace("] WlsSecurityService/getUser/10/" + var2);
         return var2;
      } else {
         var1 = SecurityServiceManager.getCurrentSubject(myKid);
         return new WlsAuthenticatedUser(var1);
      }
   }

   public void popUser() {
      if (ntrace.isTraceEnabled(4)) {
         ntrace.doTrace("[/WlsSecurityService/popUser()");
         SecurityServiceManager.popSubject(myKid);
         ntrace.doTrace("] WlsSecurityService/popUser/10/");
      } else {
         SecurityServiceManager.popSubject(myKid);
      }

   }

   public String getAnonymousUserName() {
      if (ntrace.isTraceEnabled(4)) {
         ntrace.doTrace("[/WlsSecurityService/getAnonymousUserName()");
         ntrace.doTrace("]/WlsSecurityService/getAnonymousUserName/10/" + this._anon_username);
      }

      return this._anon_username;
   }

   public TCAuthenticatedUser impersonate(String var1) throws LoginException {
      AuthenticatedSubject var2;
      if (ntrace.isTraceEnabled(4)) {
         ntrace.doTrace("[/WlsSecurityService/impersonate(" + var1 + ")");
         var2 = this._pa.impersonateIdentity(var1);
         WlsAuthenticatedUser var3 = new WlsAuthenticatedUser(var2);
         ntrace.doTrace("]/WlsSecurityService/impersonate/10/" + var3);
         return var3;
      } else {
         var2 = this._pa.impersonateIdentity(var1);
         return new WlsAuthenticatedUser(var2);
      }
   }

   public TCAppKey getAppKeyGenerator(String var1, String var2, String var3, boolean var4, int var5) {
      boolean var6 = ntrace.isTraceEnabled(4);
      Object var7 = null;
      boolean var10 = false;
      if (var6) {
         ntrace.doTrace("[/WlsSecurityService/getAppKeyGenerator/" + var1 + ", p1 = " + var2 + ", p2 = " + var3 + ", p3 = " + var4 + ", p4 = " + var5);
      }

      if (var1 != null) {
         String var8;
         String var9;
         if (var1.compareToIgnoreCase("LDAP") != 0) {
            if (var1.compareToIgnoreCase("Custom") != 0) {
               if (var6) {
                  ntrace.doTrace("unsupported AppKey Generator type: " + var1);
                  ntrace.doTrace("]/WlsSecurityService/getAppKeyGenerator/10/null");
               }

               return null;
            }

            var10 = true;
            var8 = var2;
            var9 = var3;
         } else {
            var8 = new String("weblogic.wtc.wls.ldapAppKey");
            StringBuffer var11;
            if (var2 != null && var2.length() != 0) {
               var11 = new StringBuffer(var2);
            } else {
               var11 = new StringBuffer("TUXEDO_UID");
            }

            var11.append(" ");
            if (var3 != null && var3.length() != 0) {
               var11.append(var3);
            } else {
               var11.append("TUXEDO_GID");
            }

            var9 = new String(var11);
         }

         try {
            Class var12;
            if (this._loader == null) {
               var12 = this.getClass();
               this._loader = var12.getClassLoader();
            }

            var12 = this._loader.loadClass(var8);
            Object var16 = var12.newInstance();
            if (var16 instanceof AppKey) {
               var7 = new WlsAppKeyDelegate((AppKey)var16);
            } else {
               var7 = (TCAppKey)var16;
            }

            ((TCAppKey)var7).init(var9, var4, var5);
            if (!var10) {
               if (var6) {
                  ntrace.doTrace("Cache enabled");
               }

               ((TCAppKey)var7).doCache(true);
            } else {
               ((TCAppKey)var7).doCache(false);
            }
         } catch (ClassNotFoundException var13) {
            if (var6) {
               ntrace.doTrace("]/WlsSecurityService/getAppKeyGenerator/10/null");
            }

            return null;
         } catch (TPException var14) {
            if (var6) {
               ntrace.doTrace("]/WlsSecurityService/getAppKeyGenerator/20/null");
            }

            return null;
         } catch (Exception var15) {
            if (var6) {
               ntrace.doTrace("]/WlsSecurityService/getAppKeyGenerator/30/null");
            }

            return null;
         }
      }

      if (var6) {
         ntrace.doTrace("]/WlsSecurityService/getAppKeyGenerator/40/success");
      }

      return (TCAppKey)var7;
   }

   public int getSecProviderId() {
      return 0;
   }

   public String getSecProviderName() {
      return "WLS Security Service for TC";
   }
}
