package weblogic.rjvm.wls;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.rmi.RemoteException;
import weblogic.common.internal.PassivationUtils;
import weblogic.common.internal.ProxyClassResolver;
import weblogic.kernel.Kernel;
import weblogic.kernel.KernelStatus;
import weblogic.net.http.HttpURLConnection;
import weblogic.protocol.Protocol;
import weblogic.protocol.ProtocolHandlerAdmin;
import weblogic.protocol.ServerChannel;
import weblogic.protocol.configuration.ChannelHelper;
import weblogic.rjvm.MsgAbbrevInputStream;
import weblogic.rjvm.MsgAbbrevJVMConnection;
import weblogic.rjvm.RJVMConnectionFactory;
import weblogic.rjvm.RJVMEnvironment;
import weblogic.rjvm.RJVMManager;
import weblogic.rjvm.RemoteInvokable;
import weblogic.rjvm.http.HTTPClientConnectionFactory;
import weblogic.rjvm.http.HTTPSClientConnectionFactory;
import weblogic.rjvm.t3.ConnectionFactoryT3;
import weblogic.rjvm.t3.ConnectionFactoryT3S;
import weblogic.rjvm.t3.ProtocolHandlerT3;
import weblogic.rjvm.t3.ProtocolHandlerT3S;
import weblogic.security.SubjectUtils;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.server.channels.ChannelService;
import weblogic.server.channels.ServerChannelImpl;
import weblogic.servlet.internal.ProtocolHandlerHTTP;
import weblogic.servlet.internal.ProtocolHandlerHTTPS;
import weblogic.servlet.internal.WebService;
import weblogic.socket.ChannelSocketFactory;
import weblogic.t3.srvr.BootServicesImpl;

public final class WLSRJVMEnvironment extends RJVMEnvironment {
   public void ensureInitialized() {
      Kernel.ensureInitialized();
   }

   public int getHeartbeatIdlePeriodsUntilTimeout() {
      return Kernel.getConfig().getIdlePeriodsUntilTimeout();
   }

   public int getHeartbeatPeriodLengthMillis() {
      return Kernel.getConfig().getPeriodLength();
   }

   public int getAbbrevTableSize() {
      return KernelStatus.isServer() ? Kernel.getConfig().getT3ServerAbbrevTableSize() : Kernel.getConfig().getT3ClientAbbrevTableSize();
   }

   public boolean isTracingEnabled() {
      return Kernel.isTracingEnabled();
   }

   public int getRjvmIdleTimeout() {
      return Kernel.getConfig().getRjvmIdleTimeout();
   }

   public String getDefaultProtocolName() {
      return Kernel.getConfig().getDefaultProtocol();
   }

   public String getDefaultSecureProtocolName() {
      return Kernel.getConfig().getDefaultSecureProtocol();
   }

   public String getAdminProtocolName() {
      return Kernel.getConfig().getAdministrationProtocol();
   }

   public Class resolveProxyClass(String[] var1, String var2, String var3) throws IOException, ClassNotFoundException {
      return ProxyClassResolver.resolveProxyClass(var1, var2, var3);
   }

   public Object copyObject(Object var1) throws IOException, ClassNotFoundException {
      return PassivationUtils.copy(var1);
   }

   public void registerRJVMProtocols() {
      RJVMManager.registerRJVMProtocol((byte)0, ProtocolHandlerT3.getProtocolHandler(), new ConnectionFactoryT3());
      RJVMManager.registerRJVMProtocol((byte)2, ProtocolHandlerT3S.getProtocolHandler(), new ConnectionFactoryT3S());
      RJVMManager.registerRJVMProtocol((byte)1, ProtocolHandlerHTTP.getProtocolHandler(), new HTTPClientConnectionFactory());
      RJVMManager.registerRJVMProtocol((byte)3, ProtocolHandlerHTTPS.getProtocolHandler(), new HTTPSClientConnectionFactory());
      RJVMManager.registerRJVMProtocol((byte)6, ProtocolHandlerAdmin.getProtocolHandler(), (RJVMConnectionFactory)null);
   }

   public String getInternalWebAppContextPath() {
      return WebService.getInternalWebAppContextPath();
   }

   public ServerChannel createDefaultChannel(Protocol var1) {
      return ServerChannelImpl.createDefaultServerChannel(var1);
   }

   public boolean isLocalChannel(InetAddress var1, int var2) {
      return ChannelService.isLocalChannel(var1, var2);
   }

   public String createClusterURL(ServerChannel var1) {
      return ChannelHelper.createClusterURL(var1);
   }

   public void invokeBootService(RemoteInvokable var1, MsgAbbrevInputStream var2) throws RemoteException {
      BootServicesImpl var3 = (BootServicesImpl)var1;
      synchronized(var3) {
         MsgAbbrevJVMConnection var5 = var2.getConnection();
         var3.setConnectionInfo(var5);
         var3.invoke(var2);
      }
   }

   public boolean isServerClusteringSupported() {
      return true;
   }

   public ClassLoader getConnectionManagerClassLoader() {
      return KernelStatus.class.getClassLoader();
   }

   public boolean isServer() {
      return KernelStatus.isServer();
   }

   public boolean isUserAnonymous(AuthenticatedSubject var1) {
      return SubjectUtils.isUserAnonymous(var1);
   }

   public ClassLoader getContextClassLoader() {
      return Thread.currentThread().getContextClassLoader();
   }

   public Object getSSLContext() {
      return null;
   }

   public void setSSLContext(Object var1) {
   }

   public URLConnection createURLConnection(URL var1, ServerChannel var2) throws IOException {
      if (KernelStatus.isServer()) {
         HttpURLConnection var4 = new HttpURLConnection(var1);
         var4.setSocketFactory(new ChannelSocketFactory(var2));
         var4.u11();
         return var4;
      } else {
         URLConnection var3 = var1.openConnection();
         if (var3 instanceof HttpURLConnection) {
            ((HttpURLConnection)var3).u11();
         }

         return var3;
      }
   }
}
