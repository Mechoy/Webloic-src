package weblogic.messaging.common;

import java.io.StreamCorruptedException;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import javax.naming.Context;
import javax.naming.NamingException;
import weblogic.jndi.Aggregatable;
import weblogic.jndi.WLContext;
import weblogic.management.ManagementException;
import weblogic.management.runtime.RuntimeMBeanDelegate;
import weblogic.messaging.MessagingLogger;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.SecurityServiceManager;

public class PrivilegedActionUtilities {
   public static void register(RuntimeMBeanDelegate var0, AuthenticatedSubject var1) throws ManagementException {
      final RuntimeMBeanDelegate var2 = var0;

      try {
         SecurityServiceManager.runAs(var1, var1, new PrivilegedExceptionAction() {
            public Object run() throws ManagementException {
               var2.register();
               return null;
            }
         });
      } catch (PrivilegedActionException var4) {
         throw (ManagementException)var4.getException();
      }
   }

   public static void unregister(RuntimeMBeanDelegate var0, AuthenticatedSubject var1) throws ManagementException {
      final RuntimeMBeanDelegate var2 = var0;

      try {
         SecurityServiceManager.runAs(var1, var1, new PrivilegedExceptionAction() {
            public Object run() throws ManagementException {
               var2.unregister();
               return null;
            }
         });
      } catch (PrivilegedActionException var4) {
         throw (ManagementException)var4.getException();
      }
   }

   public static void bindAsSU(Context var0, String var1, Object var2, AuthenticatedSubject var3) throws NamingException {
      final Context var4 = var0;
      final String var5 = var1;
      final Object var6 = var2;

      try {
         SecurityServiceManager.runAs(var3, var3, new PrivilegedExceptionAction() {
            public Object run() throws NamingException {
               var4.bind(var5, var6);
               return null;
            }
         });
      } catch (PrivilegedActionException var9) {
         Throwable var8 = var9.getCause();
         if (var8 instanceof NamingException) {
            throw (NamingException)var8;
         } else {
            throw new NamingException(var9.toString());
         }
      }
   }

   public static void unbindAsSU(Context var0, String var1, AuthenticatedSubject var2) throws NamingException {
      final Context var3 = var0;
      final String var4 = var1;

      try {
         SecurityServiceManager.runAs(var2, var2, new PrivilegedExceptionAction() {
            public Object run() throws NamingException {
               var3.unbind(var4);
               return null;
            }
         });
      } catch (PrivilegedActionException var7) {
         Throwable var6 = var7.getCause();
         if (var6 instanceof NamingException) {
            throw (NamingException)var6;
         } else {
            throw new NamingException(var7.toString());
         }
      }
   }

   public static void unbindAsSU(Context var0, String var1, Object var2, AuthenticatedSubject var3) throws NamingException {
      final Context var4 = var0;
      final String var5 = var1;
      final Object var6 = var2;

      try {
         SecurityServiceManager.runAs(var3, var3, new PrivilegedExceptionAction() {
            public Object run() throws NamingException {
               ((WLContext)var4).unbind(var5, var6);
               return null;
            }
         });
      } catch (PrivilegedActionException var8) {
         throw new NamingException(var8.toString());
      }
   }

   public static void rebindAsSU(Context var0, String var1, Object var2, AuthenticatedSubject var3) throws NamingException {
      final Context var4 = var0;
      final String var5 = var1;
      final Object var6 = var2;

      try {
         SecurityServiceManager.runAs(var3, var3, new PrivilegedExceptionAction() {
            public Object run() throws NamingException {
               if (var6 instanceof Aggregatable) {
                  ((WLContext)var4).rebind(var5, var6, var6);
               } else {
                  var4.rebind(var5, var6);
               }

               return null;
            }
         });
      } catch (PrivilegedActionException var8) {
         throw new NamingException(var8.toString());
      }
   }

   public static StreamCorruptedException versionIOException(int var0, int var1, int var2) {
      return new StreamCorruptedException(MessagingLogger.logUnsupportedClassVersionLoggable(var0, var1, var2).getMessage());
   }

   public static final int calcObjectSize(Object var0) {
      if (var0 == null) {
         return 2;
      } else if (var0 instanceof Integer) {
         return 6;
      } else if (var0 instanceof String) {
         return 4 + (((String)var0).length() << 2);
      } else if (var0 instanceof Long) {
         return 10;
      } else if (var0 instanceof Boolean) {
         return 3;
      } else if (var0 instanceof Byte) {
         return 3;
      } else if (var0 instanceof Short) {
         return 4;
      } else if (var0 instanceof Float) {
         return 6;
      } else if (var0 instanceof Double) {
         return 10;
      } else {
         return var0 instanceof byte[] ? ((byte[])((byte[])var0)).length + 6 : 0;
      }
   }
}
