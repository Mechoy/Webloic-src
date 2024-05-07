package weblogic.jndi.security.internal.client;

import java.security.AccessControlException;
import java.security.AccessController;
import java.util.HashSet;
import java.util.Hashtable;
import javax.naming.Context;
import javax.net.ssl.SSLContext;
import javax.security.auth.Subject;
import weblogic.corba.j2ee.naming.ContextImpl;
import weblogic.jndi.security.SubjectPusher;
import weblogic.security.auth.login.PasswordCredential;
import weblogic.security.subject.AbstractSubject;
import weblogic.security.subject.SubjectManager;
import weblogic.security.subject.SubjectProxy;

public final class ClientSubjectPusher implements SubjectPusher {
   private static final boolean DEBUG = false;
   private static AbstractSubject kernelId = null;

   public ClientSubjectPusher() {
      try {
         kernelId = (AbstractSubject)AccessController.doPrivileged(SubjectManager.getKernelIdentityAction());
      } catch (AccessControlException var2) {
      }

   }

   public final void pushSubject(Hashtable var1, Context var2) {
      PasswordCredential var3 = getEnvSecurityUser(var1);
      if (var3 != null) {
         HashSet var4 = new HashSet();
         var4.add(var3);
         SubjectProxy var5 = new SubjectProxy(new Subject(false, new HashSet(), var4, new HashSet()));
         SubjectManager.getSubjectManager().pushSubject(kernelId, var5);
         if (var2 instanceof ContextImpl) {
            ((ContextImpl)var2).enableLogoutOnClose();
         }
      }

   }

   public final void popSubject() {
      SubjectManager.getSubjectManager().popSubject(kernelId);
   }

   public static final PasswordCredential getEnvSecurityUser(Hashtable var0) throws IllegalArgumentException {
      try {
         Object var1 = var0.get("java.naming.security.credentials");
         String var2 = (String)var0.get("java.naming.security.principal");
         PasswordCredential var3 = null;
         if (var3 != null) {
            return var3;
         } else if (var1 instanceof PasswordCredential) {
            return (PasswordCredential)var1;
         } else {
            if (var1 instanceof String) {
               if (var2 == null) {
                  throw new IllegalArgumentException("The 'java.naming.security.principal' property has not been specified");
               }

               var3 = new PasswordCredential(var2, (String)var1);
            } else if (var1 == null) {
               if (var2 != null) {
                  var3 = new PasswordCredential(var2, (String)null);
               }
            } else if (var1 != null && !(var1 instanceof SSLContext)) {
               throw new IllegalArgumentException("The 'java.naming.security.credentials' property must be either a password String, an instance of PasswordCredential or an instance of SSLContext.");
            }

            if (var3 != null) {
               var0.put("java.naming.security.credentials", var3);
            }

            return var3;
         }
      } catch (ClassCastException var4) {
         throw new IllegalArgumentException("The 'java.naming.security.credentials' property must be either a password String or an instance of PasswordCredential.");
      }
   }

   static void p(String var0) {
      System.err.println("<ClientSecurityManager> " + var0);
   }
}
