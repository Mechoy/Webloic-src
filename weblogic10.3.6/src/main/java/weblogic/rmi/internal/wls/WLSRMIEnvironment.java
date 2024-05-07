package weblogic.rmi.internal.wls;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.security.AccessController;
import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import org.xml.sax.Parser;
import org.xml.sax.SAXException;
import weblogic.cluster.migration.MigratableActivatingException;
import weblogic.cluster.migration.MigratableInactiveException;
import weblogic.common.internal.VersionInfoFactory;
import weblogic.corba.rmic.IDLMangler;
import weblogic.ejb.EJBObjectEnum;
import weblogic.iiop.HostIDImpl;
import weblogic.iiop.OutboundResponse;
import weblogic.iiop.VendorInfoCluster;
import weblogic.jndi.Environment;
import weblogic.jndi.internal.AdminModeAccessException;
import weblogic.jndi.internal.ThreadEnvironment;
import weblogic.kernel.Kernel;
import weblogic.kernel.KernelStatus;
import weblogic.management.configuration.ClusterMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.JTAMBean;
import weblogic.management.provider.ManagementService;
import weblogic.rmi.cluster.ClusterableServerRef;
import weblogic.rmi.cluster.ReplicaList;
import weblogic.rmi.extensions.server.CBVInputStream;
import weblogic.rmi.extensions.server.CBVOutputStream;
import weblogic.rmi.extensions.server.InvokableServerReference;
import weblogic.rmi.extensions.server.RuntimeMethodDescriptor;
import weblogic.rmi.internal.BasicServerRef;
import weblogic.rmi.internal.CBVInput;
import weblogic.rmi.internal.CBVOutput;
import weblogic.rmi.internal.ClusterAwareServerReference;
import weblogic.rmi.internal.NonTxRMIShutdownService;
import weblogic.rmi.internal.RMIEntityResolver;
import weblogic.rmi.internal.RMIEnvironment;
import weblogic.rmi.internal.RMIShutdownService;
import weblogic.rmi.spi.HostID;
import weblogic.rmi.spi.InboundRequest;
import weblogic.rmi.spi.InvokeHandler;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.work.Work;
import weblogic.xml.jaxp.WebLogicSAXParserFactory;

public final class WLSRMIEnvironment extends RMIEnvironment {
   private static final String WLS_STUB_VERSION = "_" + VersionInfoFactory.getPeerInfo().getMajor() + VersionInfoFactory.getPeerInfo().getMinor() + VersionInfoFactory.getPeerInfo().getServicePack() + "_WLStub";
   private final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private ClusterMBean clusterMBean;

   public boolean isTracingEnabled() {
      return Kernel.isTracingEnabled();
   }

   public long getTimedOutRefIsolationTime() {
      return Kernel.getConfig().getTimedOutRefIsolationTime();
   }

   public String getIIOPSystemSecurity() {
      return Kernel.getConfig().getIIOP().getSystemSecurity();
   }

   public boolean isLogRemoteExceptions() {
      return Kernel.getConfig().isLogRemoteExceptionsEnabled();
   }

   public int getHeartbeatPeriodLength() {
      return Kernel.getConfig().getPeriodLength();
   }

   public boolean isRefreshClientRuntimeDescriptor() {
      return Kernel.getConfig().getRefreshClientRuntimeDescriptor();
   }

   public int getDGCIdlePeriodsUntilTimeout() {
      return Kernel.getConfig().getDGCIdlePeriodsUntilTimeout();
   }

   public boolean isInstrumentStackTrace() {
      return Kernel.getConfig().isInstrumentStackTraceEnabled();
   }

   public int getTransactionTimeoutMillis() {
      DomainMBean var1 = ManagementService.getRuntimeAccess(this.kernelId).getDomain();
      JTAMBean var2 = var1.getJTA();
      return var2.getTimeoutSeconds() * 1000;
   }

   public boolean isNetworkClassLoadingEnabled() {
      return !KernelStatus.isServer() || ManagementService.getRuntimeAccess(this.kernelId).getServer().isNetworkClassLoadingEnabled();
   }

   public String getStubVersion() {
      return WLS_STUB_VERSION;
   }

   public CBVInput getCBVInput(CBVInputStream var1, InputStream var2) throws IOException {
      return new WLSCBVInput(var1, var2);
   }

   public CBVOutput getCBVOutput(CBVOutputStream var1, OutputStream var2) throws IOException {
      return new WLSCBVOutput(var1, var2);
   }

   public Parser getSAXParser() throws SAXException, ParserConfigurationException {
      WebLogicSAXParserFactory var1 = new WebLogicSAXParserFactory();
      var1.setValidating(false);
      var1.setDisabledEntityResolutionRegistry(true);
      SAXParser var2 = var1.newSAXParser();
      Parser var3 = var2.getParser();
      var3.setEntityResolver(new RMIEntityResolver());
      return var3;
   }

   public Work createExecuteRequest(BasicServerRef var1, InboundRequest var2, RuntimeMethodDescriptor var3, InvokeHandler var4, AuthenticatedSubject var5) {
      return new WLSExecuteRequest(var1, var2, var3, var4, var5);
   }

   public ClusterAwareServerReference createClusteredServerRef(InvokableServerReference var1) {
      return new ClusterableServerRef(var1);
   }

   public String getIIOPMangledName(Method var1, Class var2) {
      return IDLMangler.getMangledMethodName(var1, var2);
   }

   public boolean isIIOPResponse(Object var1) {
      return var1 instanceof OutboundResponse;
   }

   public Object replaceSpecialCBVObject(Object var1) {
      return var1 instanceof EJBObjectEnum ? ((EJBObjectEnum)var1).clone() : null;
   }

   public boolean isIIOPHostID(HostID var1) {
      return var1 instanceof HostIDImpl;
   }

   public Context getContext(Object var1) throws NamingException {
      return ((Environment)var1).getContext((String)null);
   }

   public boolean isIIOPVendorInfoCluster(ReplicaList var1) {
      return var1 instanceof VendorInfoCluster;
   }

   public boolean isIIOPInboundRequest(InboundRequest var1) {
      return var1 instanceof weblogic.iiop.InboundRequest;
   }

   public Object newEnvironment() {
      return new Environment();
   }

   public Object threadEnvironmentGet() {
      return ThreadEnvironment.get();
   }

   public Object threadEnvironmentPop() {
      return ThreadEnvironment.pop();
   }

   public void threadEnvironmentPush(Object var1) {
      ThreadEnvironment.push((Environment)var1);
   }

   public Hashtable getProperties(Object var1) {
      return ((Environment)var1).getProperties();
   }

   public boolean isAdminModeAccessException(NamingException var1) {
      return var1 instanceof AdminModeAccessException;
   }

   public ClassLoader getDescriptorClassLoader() {
      return Thread.currentThread().getContextClassLoader();
   }

   public String getClusterDefaultLoadAlgorithm() {
      if (this.clusterMBean == null) {
         this.clusterMBean = ManagementService.getRuntimeAccess(this.kernelId).getServer().getCluster();
      }

      return this.clusterMBean != null ? this.clusterMBean.getDefaultLoadAlgorithm() : null;
   }

   public boolean isServerInCluster() {
      return this.clusterMBean != null;
   }

   public AuthenticatedSubject getCurrentSubjectForWire(AuthenticatedSubject var1) {
      return SecurityServiceManager.getCurrentSubjectForWire(var1);
   }

   public boolean rmiShutdownAcceptRequest(int var1, AuthenticatedSubject var2) {
      return RMIShutdownService.acceptRequest(var1, var2);
   }

   public boolean nonTxRmiShutdownAcceptRequest(int var1, AuthenticatedSubject var2, Object var3) {
      return NonTxRMIShutdownService.acceptRequest(var1, var2, var3);
   }

   public boolean isMigratableActivatingException(RemoteException var1) {
      return var1 instanceof MigratableActivatingException;
   }

   public boolean isMigratableInactiveException(RemoteException var1) {
      return var1 instanceof MigratableInactiveException;
   }
}
