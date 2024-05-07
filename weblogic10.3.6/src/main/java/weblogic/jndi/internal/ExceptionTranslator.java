package weblogic.jndi.internal;

import java.io.IOException;
import java.io.NotSerializableException;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.ConnectException;
import java.rmi.ConnectIOException;
import java.rmi.MarshalException;
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.ServerError;
import java.rmi.ServerException;
import java.rmi.StubNotFoundException;
import java.rmi.UnmarshalException;
import javax.naming.AuthenticationException;
import javax.naming.CommunicationException;
import javax.naming.ConfigurationException;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import javax.naming.ServiceUnavailableException;
import javax.security.auth.login.LoginException;
import weblogic.rmi.extensions.RemoteRuntimeException;
import weblogic.rmi.extensions.RequestTimeoutException;
import weblogic.utils.Debug;
import weblogic.utils.StackTraceUtils;

public final class ExceptionTranslator {
   public static NamingException toNamingException(Throwable var0) {
      if (var0 instanceof RemoteRuntimeException) {
         var0 = ((RemoteRuntimeException)var0).getNested();
         Debug.assertion(var0 instanceof RemoteException, "RemoteRuntimeException must nest a RemoteException");
      }

      Object var1;
      if (var0 instanceof NamingException) {
         var1 = (NamingException)var0;
      } else {
         if (var0 instanceof RemoteException) {
            return toNamingException((RemoteException)var0);
         }

         if (var0 instanceof UnknownHostException) {
            var1 = new ServiceUnavailableException();
         } else if (var0 instanceof MalformedURLException) {
            var1 = new ConfigurationException();
         } else if (var0 instanceof NotSerializableException) {
            var1 = new ConfigurationException();
         } else if (var0 instanceof IOException) {
            var1 = new CommunicationException();
         } else if (var0 instanceof SecurityException) {
            var1 = new AuthenticationException();
         } else if (var0 instanceof LoginException) {
            var1 = new AuthenticationException();
         } else {
            if (var0 instanceof Error) {
               throw (Error)var0;
            }

            if (var0 instanceof RuntimeException) {
               throw (RuntimeException)var0;
            }

            var1 = new NamingException("Unexpected exception: " + StackTraceUtils.throwable2StackTrace(var0));
         }
      }

      ((NamingException)var1).setRootCause(var0);
      return (NamingException)var1;
   }

   public static NamingException toNamingException(RemoteException var0) {
      Object var1;
      if (var0 instanceof java.rmi.UnknownHostException) {
         var1 = new ServiceUnavailableException();
      } else if (var0 instanceof ConnectException) {
         var1 = new CommunicationException();
      } else if (var0 instanceof ConnectIOException) {
         var1 = new CommunicationException();
      } else if (var0 instanceof MarshalException) {
         var1 = toNamingException(var0.detail);
      } else if (var0 instanceof NoSuchObjectException) {
         var1 = new ServiceUnavailableException();
      } else if (var0 instanceof StubNotFoundException) {
         var1 = new ConfigurationException();
      } else if (var0 instanceof UnmarshalException) {
         var1 = new CommunicationException();
      } else if (var0 instanceof ServerError) {
         var1 = new CommunicationException();
      } else {
         if (var0 instanceof ServerException) {
            return toNamingException((ServerException)var0);
         }

         if (var0 instanceof RequestTimeoutException) {
            var1 = new ServiceUnavailableException();
         } else if (var0 instanceof NameAlreadyUnboundException) {
            var1 = new NameNotFoundException();
         } else {
            var1 = new NamingException();
         }
      }

      ((NamingException)var1).setRootCause(var0);
      return (NamingException)var1;
   }

   public static NamingException toNamingException(ServerException var0) {
      Object var2 = var0;
      Throwable var3 = var0.detail;
      Debug.assertion(var3 instanceof RemoteException, "ServerException must nest a RemoteException");
      Object var1;
      if (var3 instanceof UnmarshalException) {
         var1 = new CommunicationException();
      } else if (var3 instanceof MarshalException) {
         var1 = new ConfigurationException();
      } else if (var3 instanceof StubNotFoundException) {
         var1 = new ConfigurationException();
         var2 = var3;
      } else {
         var1 = new NamingException();
      }

      ((NamingException)var1).setRootCause((Throwable)var2);
      return (NamingException)var1;
   }
}
