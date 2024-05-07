package weblogic.connector.outbound;

import java.lang.ref.Reference;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.UndeclaredThrowableException;
import java.security.AccessController;
import javax.resource.ResourceException;
import weblogic.connector.common.Debug;
import weblogic.connector.common.Utils;
import weblogic.connector.transaction.outbound.TxConnectionHandler;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.PlatformConstants;
import weblogic.utils.StackTraceUtils;

public final class ConnectionWrapper implements InvocationHandler {
   private ConnectionPool connPool = null;
   private ConnectionInfo connInfo = null;
   private Object connInstance = null;
   private Reference ref = null;
   private int hash;

   private ConnectionWrapper(ConnectionPool var1, ConnectionInfo var2, Object var3) {
      this.connInstance = var3;
      this.connPool = var1;
      this.connInfo = var2;
      this.connInfo.setLastUsedTime(System.currentTimeMillis());
      this.connInfo.setUsed(true);
      if (var3 != null) {
         this.hash = var3.hashCode();
      } else {
         this.hash = super.hashCode();
      }

      if (var1 != null && var1.getConnectionProfilingEnabled()) {
         String var4 = Debug.getExceptionStackTrace() + PlatformConstants.EOL;
         Throwable var5 = new Throwable(var4);
         String var6 = StackTraceUtils.throwable2StackTrace(var5);

         try {
            String var7 = ":";
            var6 = var6.substring(var6.indexOf(var7) + var7.length());
         } catch (Exception var8) {
         }

         this.connInfo.setAllocationCallStack(var6);
      }

   }

   private ConnectionWrapper(ConnectionPool var1, Object var2) {
      this.connInstance = var2;
      this.connPool = var1;
      this.connInfo = new ConnectionInfo();
      this.connInfo.setLastUsedTime(System.currentTimeMillis());
      this.connInfo.setUsed(true);
      if (var2 != null) {
         this.hash = var2.hashCode();
      } else {
         this.hash = super.hashCode();
      }

   }

   public static Object createConnectionWrapper(ConnectionPool var0, ConnectionInfo var1, Object var2) {
      Class var3 = var2.getClass();
      Class[] var4 = Utils.getInterfaces(var3);
      return Proxy.newProxyInstance(var3.getClassLoader(), var4, new ConnectionWrapper(var0, var1, var2));
   }

   public static Object createProxyTestConnectionWrapper(ConnectionPool var0, Object var1) {
      Class var2 = var1.getClass();
      Class[] var3 = Utils.getInterfaces(var2);
      return Proxy.newProxyInstance(var2.getClassLoader(), var3, new ConnectionWrapper(var0, var1));
   }

   public void setWeakReference(Reference var1) {
      this.ref = var1;
   }

   public int hashCode() {
      return this.hash;
   }

   public Object invoke(Object var1, Method var2, Object[] var3) throws ResourceException, Throwable {
      this.connInfo.setLastUsedTime(System.currentTimeMillis());
      this.connInfo.setUsed(true);
      ConnectionHandler var4 = this.connInfo.getConnectionHandler();
      if (var4 != null && var4 instanceof TxConnectionHandler && !var2.getName().equals("close")) {
         ((TxConnectionHandler)var4).enListResource();
      }

      AuthenticatedSubject var5 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

      Throwable var7;
      try {
         return this.connPool.getRAInstanceManager().getAdapterLayer().invoke(var2, this.connInstance, var3, var5);
      } catch (IllegalAccessException var8) {
         throw var8;
      } catch (InvocationTargetException var9) {
         var7 = var9.getCause();
         if (var7 != null) {
            if (var7 instanceof ResourceException) {
               throw (ResourceException)var7;
            } else {
               throw var7;
            }
         } else {
            throw var9;
         }
      } catch (UndeclaredThrowableException var10) {
         var7 = var10.getUndeclaredThrowable();
         if (var7 != null) {
            if (var7 instanceof ResourceException) {
               throw (ResourceException)var7;
            } else {
               throw var7;
            }
         } else {
            throw var10;
         }
      }
   }

   protected synchronized void finalize() throws Throwable {
      Debug.enter(this, "finalize() for " + this.connInfo);

      try {
         this.connInfo.getConnectionHandler().connectionFinalized(this.ref);
      } finally {
         super.finalize();
         Debug.exit(this, "finalize() for " + this.connInfo);
      }

   }

   protected Object getConnectionInstance() {
      return this.connInstance;
   }
}
