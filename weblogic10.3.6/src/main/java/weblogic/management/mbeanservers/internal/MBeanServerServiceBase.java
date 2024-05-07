package weblogic.management.mbeanservers.internal;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.UnknownHostException;
import java.security.AccessController;
import java.util.Hashtable;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MBeanServerDelegate;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.OperationsException;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.management.jmx.JMXLogger;
import weblogic.management.jmx.mbeanserver.WLSMBeanServer;
import weblogic.management.jmx.mbeanserver.WLSMBeanServerBuilder;
import weblogic.management.jmx.modelmbean.WLSModelMBeanContext;
import weblogic.management.jmx.modelmbean.WLSModelMBeanFactory;
import weblogic.management.mbeanservers.MBeanTypeService;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.protocol.ServerURL;
import weblogic.protocol.URLManager;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.server.AbstractServerService;
import weblogic.server.ServiceFailureException;

public class MBeanServerServiceBase extends AbstractServerService {
   private static DebugLogger debug = DebugLogger.getDebugLogger("DebugJMXCore");
   protected static final String CURRENT_VERSION = "12.0.0.0";
   private static final String PROTOCOL_IIOP = "iiop";
   private static final String JNDI = "/jndi/";
   private static final ObjectName MBEANTYPESERVICE;
   private static AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private MBeanServer mbeanServer;
   private JMXServiceURL serviceURL;
   private JMXConnectorServer connectorServer = null;

   protected MBeanServer getMBeanServer() {
      return this.mbeanServer;
   }

   protected synchronized void initialize(String var1, MBeanServer var2) throws ServiceFailureException {
      this.initialize(var1, var2, (MBeanServer)null);
   }

   protected void registerTypeService(WLSModelMBeanContext var1) throws OperationsException, MBeanRegistrationException {
      MBeanTypeServiceImpl var2 = new MBeanTypeServiceImpl(var1);
      WLSModelMBeanFactory.registerWLSModelMBean(var2, MBEANTYPESERVICE, var1);
   }

   protected synchronized void initialize(String var1, MBeanServer var2, MBeanServer var3) throws ServiceFailureException {
      RuntimeAccess var4 = ManagementService.getRuntimeAccess(kernelId);
      this.mbeanServer = var2;
      if (var3 != null && var3 instanceof WLSMBeanServer) {
         this.mbeanServer = var3;
      } else if (this.mbeanServer == null) {
         WLSMBeanServerBuilder var5 = new WLSMBeanServerBuilder();
         String var6 = var4.getDomainName();
         this.mbeanServer = var5.newMBeanServer(var6, (MBeanServer)null, (MBeanServerDelegate)null, var3);
      }

      try {
         String var10 = URLManager.findAdministrationURL(var4.getServerName());
         ServerURL var12 = new ServerURL(var10);
         this.serviceURL = new JMXServiceURL("iiop", var12.getHost(), var12.getPort(), "/jndi/" + var1);
      } catch (MalformedURLException var8) {
         throw new AssertionError(" Malformed URL" + var8);
      } catch (UnknownHostException var9) {
         if (debug.isDebugEnabled()) {
            debug.debug("Unable to obtain URL for managed server " + var4.getServerName());
         }

         return;
      }

      if (debug.isDebugEnabled()) {
         debug.debug("Registering JMXConnectorServer at " + this.serviceURL);
      }

      try {
         Hashtable var11 = new Hashtable();
         var11.put("weblogic.jndi.replicateBindings", Boolean.FALSE.toString());
         var11.put("weblogic.jndi.createIntermediateContexts", Boolean.TRUE.toString());
         var11.put("java.naming.factory.initial", "weblogic.jndi.WLInitialContextFactory");
         var11.put("jmx.remote.x.server.connection.timeout", new Long(Long.MAX_VALUE));
         var11.put("jmx.remote.protocol.provider.pkgs", "weblogic.management.remote");
         var11.put("jmx.remote.authenticator", new JMXAuthenticator());
         this.connectorServer = JMXConnectorServerFactory.newJMXConnectorServer(this.serviceURL, var11, this.mbeanServer);
         this.connectorServer.setMBeanServerForwarder(new JMXConnectorSubjectForwarder());
      } catch (IOException var7) {
         throw new AssertionError(" Failed to start JMXConnector Server" + var7);
      }

      if (debug.isDebugEnabled()) {
         debug.debug("JMXConnectorServer registered at " + this.serviceURL);
      }

   }

   private String getServiceURLString() {
      return this.serviceURL == null ? "<not established>" : this.serviceURL.toString();
   }

   public void start() throws ServiceFailureException {
      try {
         this.connectorServer.start();
         JMXLogger.logStartedJMXConnectorServer(this.getServiceURLString());
      } catch (IOException var2) {
         throw new ServiceFailureException("Unable to start JMX Connector Server", var2);
      }
   }

   public void stop() throws ServiceFailureException {
      try {
         if (this.connectorServer != null) {
            if (debug.isDebugEnabled()) {
               debug.debug("Stopping connectionServer: ConnectionIds: " + this.connectorServer.getConnectionIds());
            }

            this.connectorServer.stop();
            JMXLogger.logStoppedJMXConnectorServer(this.getServiceURLString());
         }
      } catch (IOException var2) {
         throw new ServiceFailureException("Unable to stop JMX Connector Server", var2);
      }
   }

   public void halt() throws ServiceFailureException {
      this.stop();
   }

   static {
      try {
         MBEANTYPESERVICE = new ObjectName(MBeanTypeService.OBJECT_NAME);
      } catch (MalformedObjectNameException var1) {
         throw new Error(var1);
      }
   }
}
