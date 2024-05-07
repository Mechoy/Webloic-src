package weblogic.jms.common;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessControlException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import weblogic.security.subject.AbstractSubject;
import weblogic.security.subject.SubjectManager;

public final class CrossDomainSecurityManager {
   private static final String THICK_SUBJECT_MANAGER = "weblogic.security.service.SubjectManagerImpl";
   private static final String IIOPCLIENT_SUBJECT_MANAGER = "weblogic.corba.client.security.SubjectManagerImpl";
   private static final String T3CLIENT_SUBJECT_MANAGER = "weblogic.security.subject.SubjectManager";
   private static boolean initialized;
   private static final AbstractSubject kernelID = getKernelIdentity();
   public static CrossDomainSecurityUtil util = new ClientCrossDomainSecurityUtil();

   private static final AbstractSubject getKernelIdentity() {
      try {
         ensureSubjectManagerInitialized();
         return (AbstractSubject)AccessController.doPrivileged(SubjectManager.getKernelIdentityAction());
      } catch (AccessControlException var1) {
         return null;
      }
   }

   public static CrossDomainSecurityUtil getCrossDomainSecurityUtil() {
      return util;
   }

   public static void setCrossDomainSecurityUtil(CrossDomainSecurityUtil var0) {
      util = var0;
   }

   public static void ensureSubjectManagerInitialized() {
      if (!initialized) {
         Class var0;
         try {
            var0 = Class.forName("weblogic.security.service.SubjectManagerImpl", true, ClassLoader.getSystemClassLoader());
         } catch (ClassNotFoundException var9) {
            try {
               var0 = Class.forName("weblogic.corba.client.security.SubjectManagerImpl", true, ClassLoader.getSystemClassLoader());
            } catch (ClassNotFoundException var8) {
               try {
                  var0 = Class.forName("weblogic.security.subject.SubjectManager", true, ClassLoader.getSystemClassLoader());
               } catch (ClassNotFoundException var7) {
                  if (SubjectManager.getSubjectManager() == null) {
                     throw new AssertionError(var7);
                  }

                  var0 = SubjectManager.getSubjectManager().getClass();
               }
            }
         }

         Method var1;
         try {
            var1 = var0.getMethod("ensureInitialized");
         } catch (NoSuchMethodException var6) {
            throw new AssertionError(var6);
         }

         try {
            var1.invoke((Object)null, (Object[])null);
         } catch (IllegalAccessException var4) {
            throw new AssertionError(var4);
         } catch (InvocationTargetException var5) {
            throw new AssertionError(var5);
         }

         initialized = true;
      }
   }

   public static final AbstractSubject getCurrentSubject() {
      return SubjectManager.getSubjectManager().getCurrentSubject(kernelID);
   }

   public static final void doAs(AbstractSubject var0, PrivilegedExceptionAction var1) throws javax.jms.JMSException {
      try {
         var0.doAs(kernelID, var1);
      } catch (PrivilegedActionException var4) {
         Exception var3 = var4.getException();
         if (var3 instanceof javax.jms.JMSException) {
            throw (javax.jms.JMSException)var3;
         }
      }

   }

   public static final void doAsWithWrappedException(AbstractSubject var0, PrivilegedExceptionAction var1) throws PrivilegedActionException {
      var0.doAs(kernelID, var1);
   }

   public static final Object runAs(AbstractSubject var0, PrivilegedExceptionAction var1) throws PrivilegedActionException {
      return var0.doAs(kernelID, var1);
   }

   public static final Object runAs(AbstractSubject var0, PrivilegedAction var1) {
      return var0.doAs(kernelID, var1);
   }
}
