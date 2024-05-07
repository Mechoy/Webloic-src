package weblogic.security.SSL;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.AccessController;
import java.security.cert.CertificateException;
import javax.net.SocketFactory;
import javax.net.ssl.SSLSocket;
import weblogic.management.configuration.ConfigurationException;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.security.utils.SSLContextManager;
import weblogic.security.utils.SSLIOContextTable;

public class ServerSSLSocketFactory extends SSLSocketFactory {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public ServerSSLSocketFactory() throws CertificateException, ConfigurationException {
      super(SSLContextManager.getDefaultSSLSocketFactory(SecurityServiceManager.getCurrentSubject(kernelId)));
   }

   public static SocketFactory getDefault() {
      try {
         return new ServerSSLSocketFactory();
      } catch (CertificateException var1) {
         throw new RuntimeException("Failed to initialize ServerSSLSocketFactory", var1);
      } catch (ConfigurationException var2) {
         throw new RuntimeException("Failed to initialize ServerSSLSocketFactory", var2);
      }
   }

   public Socket createSocket(Socket var1, String var2, int var3, boolean var4) throws UnknownHostException, IOException {
      SSLSocket var5 = (SSLSocket)super.createSocket(var1, var2, var3, var4);
      SSLIOContextTable.removeContext(var5);
      return var5;
   }

   public Socket createSocket(String var1, int var2) throws UnknownHostException, IOException {
      SSLSocket var3 = (SSLSocket)super.createSocket(var1, var2);
      SSLIOContextTable.removeContext(var3);
      return var3;
   }

   public Socket createSocket(InetAddress var1, int var2) throws UnknownHostException, IOException {
      SSLSocket var3 = (SSLSocket)super.createSocket(var1, var2);
      SSLIOContextTable.removeContext(var3);
      return var3;
   }

   public Socket createSocket(String var1, int var2, InetAddress var3, int var4) throws UnknownHostException, IOException {
      SSLSocket var5 = (SSLSocket)super.createSocket(var1, var2, var3, var4);
      SSLIOContextTable.removeContext(var5);
      return var5;
   }

   public Socket createSocket(InetAddress var1, int var2, InetAddress var3, int var4) throws UnknownHostException, IOException {
      SSLSocket var5 = (SSLSocket)super.createSocket(var1, var2, var3, var4);
      SSLIOContextTable.removeContext(var5);
      return var5;
   }
}
