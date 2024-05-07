package weblogic.server;

import java.rmi.RemoteException;
import java.rmi.UnexpectedException;
import javax.rmi.CORBA.Stub;
import javax.rmi.CORBA.Util;
import org.omg.CORBA.SystemException;
import org.omg.CORBA.portable.ApplicationException;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.RemarshalException;
import org.omg.CORBA_2_3.portable.InputStream;

public final class _RemoteLifeCycleOperationsImpl_Stub extends Stub implements RemoteLifeCycleOperations {
   private static String[] _type_ids = new String[]{"RMI:weblogic.server.RemoteLifeCycleOperationsImpl:0000000000000000", "RMI:weblogic.server.RemoteLifeCycleOperations:0000000000000000"};
   // $FF: synthetic field
   private static Class class$weblogic$server$ServerLifecycleException;
   // $FF: synthetic field
   private static Class class$java$lang$String;

   public String[] _ids() {
      return _type_ids;
   }

   public final void shutdown() throws RemoteException {
      try {
         InputStream var1 = null;

         try {
            OutputStream var2 = this._request("shutdown__", true);
            this._invoke(var2);
         } catch (ApplicationException var10) {
            var1 = (InputStream)var10.getInputStream();
            String var4 = var1.read_string();
            if (var4.equals("IDL:weblogic/server/ServerLifecycleEx:1.0")) {
               throw (ServerLifecycleException)((ServerLifecycleException)var1.read_value(class$weblogic$server$ServerLifecycleException == null ? (class$weblogic$server$ServerLifecycleException = class$("weblogic.server.ServerLifecycleException")) : class$weblogic$server$ServerLifecycleException));
            }

            throw new UnexpectedException(var4);
         } catch (RemarshalException var11) {
            this.shutdown();
         } finally {
            this._releaseReply(var1);
         }

      } catch (SystemException var13) {
         throw Util.mapSystemException(var13);
      }
   }

   // $FF: synthetic method
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   public final void shutdown(int var1, boolean var2) throws RemoteException {
      try {
         InputStream var3 = null;

         try {
            OutputStream var4 = this._request("shutdown__long__boolean", true);
            var4.write_long(var1);
            var4.write_boolean(var2);
            this._invoke(var4);
         } catch (ApplicationException var12) {
            var3 = (InputStream)var12.getInputStream();
            String var6 = var3.read_string();
            if (var6.equals("IDL:weblogic/server/ServerLifecycleEx:1.0")) {
               throw (ServerLifecycleException)((ServerLifecycleException)var3.read_value(class$weblogic$server$ServerLifecycleException == null ? (class$weblogic$server$ServerLifecycleException = class$("weblogic.server.ServerLifecycleException")) : class$weblogic$server$ServerLifecycleException));
            }

            throw new UnexpectedException(var6);
         } catch (RemarshalException var13) {
            this.shutdown(var1, var2);
         } finally {
            this._releaseReply(var3);
         }

      } catch (SystemException var15) {
         throw Util.mapSystemException(var15);
      }
   }

   public final void forceShutdown() throws RemoteException {
      try {
         InputStream var1 = null;

         try {
            OutputStream var2 = this._request("forceShutdown", true);
            this._invoke(var2);
         } catch (ApplicationException var10) {
            var1 = (InputStream)var10.getInputStream();
            String var4 = var1.read_string();
            if (var4.equals("IDL:weblogic/server/ServerLifecycleEx:1.0")) {
               throw (ServerLifecycleException)((ServerLifecycleException)var1.read_value(class$weblogic$server$ServerLifecycleException == null ? (class$weblogic$server$ServerLifecycleException = class$("weblogic.server.ServerLifecycleException")) : class$weblogic$server$ServerLifecycleException));
            }

            throw new UnexpectedException(var4);
         } catch (RemarshalException var11) {
            this.forceShutdown();
         } finally {
            this._releaseReply(var1);
         }

      } catch (SystemException var13) {
         throw Util.mapSystemException(var13);
      }
   }

   public final void suspend() throws RemoteException {
      try {
         InputStream var1 = null;

         try {
            OutputStream var2 = this._request("suspend__", true);
            this._invoke(var2);
         } catch (ApplicationException var10) {
            var1 = (InputStream)var10.getInputStream();
            String var4 = var1.read_string();
            if (var4.equals("IDL:weblogic/server/ServerLifecycleEx:1.0")) {
               throw (ServerLifecycleException)((ServerLifecycleException)var1.read_value(class$weblogic$server$ServerLifecycleException == null ? (class$weblogic$server$ServerLifecycleException = class$("weblogic.server.ServerLifecycleException")) : class$weblogic$server$ServerLifecycleException));
            }

            throw new UnexpectedException(var4);
         } catch (RemarshalException var11) {
            this.suspend();
         } finally {
            this._releaseReply(var1);
         }

      } catch (SystemException var13) {
         throw Util.mapSystemException(var13);
      }
   }

   public final void suspend(int var1, boolean var2) throws RemoteException {
      try {
         InputStream var3 = null;

         try {
            OutputStream var4 = this._request("suspend__long__boolean", true);
            var4.write_long(var1);
            var4.write_boolean(var2);
            this._invoke(var4);
         } catch (ApplicationException var12) {
            var3 = (InputStream)var12.getInputStream();
            String var6 = var3.read_string();
            if (var6.equals("IDL:weblogic/server/ServerLifecycleEx:1.0")) {
               throw (ServerLifecycleException)((ServerLifecycleException)var3.read_value(class$weblogic$server$ServerLifecycleException == null ? (class$weblogic$server$ServerLifecycleException = class$("weblogic.server.ServerLifecycleException")) : class$weblogic$server$ServerLifecycleException));
            }

            throw new UnexpectedException(var6);
         } catch (RemarshalException var13) {
            this.suspend(var1, var2);
         } finally {
            this._releaseReply(var3);
         }

      } catch (SystemException var15) {
         throw Util.mapSystemException(var15);
      }
   }

   public final void forceSuspend() throws RemoteException {
      try {
         InputStream var1 = null;

         try {
            OutputStream var2 = this._request("forceSuspend", true);
            this._invoke(var2);
         } catch (ApplicationException var10) {
            var1 = (InputStream)var10.getInputStream();
            String var4 = var1.read_string();
            if (var4.equals("IDL:weblogic/server/ServerLifecycleEx:1.0")) {
               throw (ServerLifecycleException)((ServerLifecycleException)var1.read_value(class$weblogic$server$ServerLifecycleException == null ? (class$weblogic$server$ServerLifecycleException = class$("weblogic.server.ServerLifecycleException")) : class$weblogic$server$ServerLifecycleException));
            }

            throw new UnexpectedException(var4);
         } catch (RemarshalException var11) {
            this.forceSuspend();
         } finally {
            this._releaseReply(var1);
         }

      } catch (SystemException var13) {
         throw Util.mapSystemException(var13);
      }
   }

   public final void resume() throws RemoteException {
      try {
         InputStream var1 = null;

         try {
            OutputStream var2 = this._request("resume", true);
            this._invoke(var2);
         } catch (ApplicationException var10) {
            var1 = (InputStream)var10.getInputStream();
            String var4 = var1.read_string();
            if (var4.equals("IDL:weblogic/server/ServerLifecycleEx:1.0")) {
               throw (ServerLifecycleException)((ServerLifecycleException)var1.read_value(class$weblogic$server$ServerLifecycleException == null ? (class$weblogic$server$ServerLifecycleException = class$("weblogic.server.ServerLifecycleException")) : class$weblogic$server$ServerLifecycleException));
            }

            throw new UnexpectedException(var4);
         } catch (RemarshalException var11) {
            this.resume();
         } finally {
            this._releaseReply(var1);
         }

      } catch (SystemException var13) {
         throw Util.mapSystemException(var13);
      }
   }

   public final String getState() throws RemoteException {
      try {
         InputStream var1 = null;

         String var5;
         try {
            OutputStream var2 = this._request("_get_state", true);
            var1 = (InputStream)this._invoke(var2);
            String var3 = (String)var1.read_value(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            return var3;
         } catch (ApplicationException var12) {
            var1 = (InputStream)var12.getInputStream();
            String var4 = var1.read_string();
            throw new UnexpectedException(var4);
         } catch (RemarshalException var13) {
            var5 = this.getState();
         } finally {
            this._releaseReply(var1);
         }

         return var5;
      } catch (SystemException var15) {
         throw Util.mapSystemException(var15);
      }
   }

   public final void setState(String var1, String var2) throws RemoteException {
      try {
         InputStream var3 = null;

         try {
            org.omg.CORBA_2_3.portable.OutputStream var4 = (org.omg.CORBA_2_3.portable.OutputStream)this._request("setState", false);
            var4.write_value(var1, class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            var4.write_value(var2, class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            this._invoke(var4);
         } catch (ApplicationException var12) {
            var3 = (InputStream)var12.getInputStream();
            String var6 = var3.read_string();
            throw new UnexpectedException(var6);
         } catch (RemarshalException var13) {
            this.setState(var1, var2);
         } finally {
            this._releaseReply(var3);
         }

      } catch (SystemException var15) {
         throw Util.mapSystemException(var15);
      }
   }

   public final String getWeblogicHome() throws RemoteException {
      try {
         InputStream var1 = null;

         String var5;
         try {
            OutputStream var2 = this._request("_get_weblogicHome", true);
            var1 = (InputStream)this._invoke(var2);
            String var3 = (String)var1.read_value(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            return var3;
         } catch (ApplicationException var12) {
            var1 = (InputStream)var12.getInputStream();
            String var4 = var1.read_string();
            throw new UnexpectedException(var4);
         } catch (RemarshalException var13) {
            var5 = this.getWeblogicHome();
         } finally {
            this._releaseReply(var1);
         }

         return var5;
      } catch (SystemException var15) {
         throw Util.mapSystemException(var15);
      }
   }

   public final String getMiddlewareHome() throws RemoteException {
      try {
         InputStream var1 = null;

         String var5;
         try {
            OutputStream var2 = this._request("_get_middlewareHome", true);
            var1 = (InputStream)this._invoke(var2);
            String var3 = (String)var1.read_value(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            return var3;
         } catch (ApplicationException var12) {
            var1 = (InputStream)var12.getInputStream();
            String var4 = var1.read_string();
            throw new UnexpectedException(var4);
         } catch (RemarshalException var13) {
            var5 = this.getMiddlewareHome();
         } finally {
            this._releaseReply(var1);
         }

         return var5;
      } catch (SystemException var15) {
         throw Util.mapSystemException(var15);
      }
   }
}
