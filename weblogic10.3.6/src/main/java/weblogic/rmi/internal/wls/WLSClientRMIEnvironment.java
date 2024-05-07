package weblogic.rmi.internal.wls;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Parser;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import weblogic.common.internal.VersionInfoFactory;
import weblogic.jndi.Environment;
import weblogic.kernel.Kernel;
import weblogic.rmi.cluster.ReplicaList;
import weblogic.rmi.extensions.server.CBVInputStream;
import weblogic.rmi.extensions.server.CBVOutputStream;
import weblogic.rmi.extensions.server.InvokableServerReference;
import weblogic.rmi.extensions.server.ReferenceHelper;
import weblogic.rmi.extensions.server.RuntimeMethodDescriptor;
import weblogic.rmi.internal.BasicServerRef;
import weblogic.rmi.internal.CBVInput;
import weblogic.rmi.internal.CBVOutput;
import weblogic.rmi.internal.ClusterAwareServerReference;
import weblogic.rmi.internal.DefaultCBVInput;
import weblogic.rmi.internal.DefaultCBVOutput;
import weblogic.rmi.internal.DefaultExecuteRequest;
import weblogic.rmi.internal.RMIEnvironment;
import weblogic.rmi.spi.HostID;
import weblogic.rmi.spi.InboundRequest;
import weblogic.rmi.spi.InvokeHandler;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.SecurityServiceManager;
import weblogic.work.Work;

public class WLSClientRMIEnvironment extends RMIEnvironment {
   private static final String WLS_STUB_VERSION = "_" + VersionInfoFactory.getPeerInfo().getMajor() + VersionInfoFactory.getPeerInfo().getMinor() + VersionInfoFactory.getPeerInfo().getServicePack() + "_WLStub";
   private static final String DEFAULT_STUB_VERSION = "_1036_WLStub";
   private static final String DEFAULT_SYSTEM_SECURITY = "supported";
   private static final boolean DEFAULT_NETWORK_CLASS_LOADING = false;
   private static final String NETWORK_CLASS_LOADING_PROP = "weblogic.rmi.networkclassloadingenabled";
   private static final int DEFAULT_TRAN_TIMEOUT = 30000;

   public WLSClientRMIEnvironment() {
      ReferenceHelper.setReferenceHelper(new CEReferenceHelperImpl());
   }

   public long getTimedOutRefIsolationTime() {
      return Kernel.getConfig().getTimedOutRefIsolationTime();
   }

   public boolean isTracingEnabled() {
      return Kernel.isTracingEnabled();
   }

   public String getIIOPSystemSecurity() {
      return "supported";
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

   public boolean isNetworkClassLoadingEnabled() {
      return System.getProperty("weblogic.rmi.networkclassloadingenabled") != null ? Boolean.getBoolean("weblogic.rmi.networkclassloadingenabled") : false;
   }

   public int getTransactionTimeoutMillis() {
      return 30000;
   }

   public String getStubVersion() {
      return "_1036_WLStub";
   }

   public CBVInput getCBVInput(CBVInputStream var1, InputStream var2) throws IOException {
      return new DefaultCBVInput(var1, var2);
   }

   public CBVOutput getCBVOutput(CBVOutputStream var1, OutputStream var2) throws IOException {
      return new DefaultCBVOutput(var1, var2);
   }

   public Parser getSAXParser() throws SAXException, ParserConfigurationException {
      SAXParserFactory var1 = SAXParserFactory.newInstance();
      var1.setValidating(true);
      SAXParser var2 = var1.newSAXParser();
      Parser var3 = var2.getParser();
      var3.setEntityResolver(new DefaultRMIEntityResolver());
      var3.setErrorHandler(new DefaultErrorHandler());
      return var3;
   }

   public Work createExecuteRequest(BasicServerRef var1, InboundRequest var2, RuntimeMethodDescriptor var3, InvokeHandler var4, AuthenticatedSubject var5) {
      return new DefaultExecuteRequest(var1, var2, var3, var4, var5);
   }

   public ClusterAwareServerReference createClusteredServerRef(InvokableServerReference var1) {
      throw new AssertionError("Clustering not supported");
   }

   public String getIIOPMangledName(Method var1, Class var2) {
      throw new AssertionError("RMI over IIOP not supported");
   }

   public boolean isIIOPResponse(Object var1) {
      return false;
   }

   public Object replaceSpecialCBVObject(Object var1) {
      return null;
   }

   public ClassLoader getDescriptorClassLoader() {
      return Thread.currentThread().getContextClassLoader();
   }

   public boolean isIIOPHostID(HostID var1) {
      return false;
   }

   public Context getContext(Object var1) throws NamingException {
      return ((Environment)var1).getContext((String)null);
   }

   public boolean isIIOPVendorInfoCluster(ReplicaList var1) {
      return false;
   }

   public boolean isIIOPInboundRequest(InboundRequest var1) {
      return false;
   }

   public Object newEnvironment() {
      try {
         Class var1 = Class.forName("weblogic.jndi.Environment");
         Constructor var2 = var1.getConstructor();
         return var2.newInstance((Object[])null);
      } catch (ClassNotFoundException var3) {
         throw new AssertionError(var3);
      } catch (SecurityException var4) {
         throw new AssertionError(var4);
      } catch (NoSuchMethodException var5) {
         throw new AssertionError(var5);
      } catch (IllegalArgumentException var6) {
         throw new AssertionError(var6);
      } catch (InstantiationException var7) {
         throw new AssertionError(var7);
      } catch (IllegalAccessException var8) {
         throw new AssertionError(var8);
      } catch (InvocationTargetException var9) {
         throw new AssertionError(var9);
      }
   }

   public Object threadEnvironmentGet() {
      try {
         Class var1 = Class.forName("weblogic.jndi.internal.ThreadEnvironment");
         Method var2 = var1.getMethod("get", (Class[])null);
         return var2.invoke((Object)null, (Object[])null);
      } catch (ClassNotFoundException var3) {
         throw new AssertionError(var3);
      } catch (SecurityException var4) {
         throw new AssertionError(var4);
      } catch (NoSuchMethodException var5) {
         throw new AssertionError(var5);
      } catch (IllegalArgumentException var6) {
         throw new AssertionError(var6);
      } catch (IllegalAccessException var7) {
         throw new AssertionError(var7);
      } catch (InvocationTargetException var8) {
         throw new AssertionError(var8);
      }
   }

   public Object threadEnvironmentPop() {
      try {
         Class var1 = Class.forName("weblogic.jndi.internal.ThreadEnvironment");
         Method var2 = var1.getMethod("pop", (Class[])null);
         return var2.invoke((Object)null, (Object[])null);
      } catch (ClassNotFoundException var3) {
         throw new AssertionError(var3);
      } catch (SecurityException var4) {
         throw new AssertionError(var4);
      } catch (NoSuchMethodException var5) {
         throw new AssertionError(var5);
      } catch (IllegalArgumentException var6) {
         throw new AssertionError(var6);
      } catch (IllegalAccessException var7) {
         throw new AssertionError(var7);
      } catch (InvocationTargetException var8) {
         throw new AssertionError(var8);
      }
   }

   public void threadEnvironmentPush(Object var1) {
      try {
         Class var2 = Class.forName("weblogic.jndi.internal.ThreadEnvironment");
         Class var3 = Class.forName("weblogic.jndi.Environment");
         Class[] var4 = new Class[]{var3};
         Method var5 = var2.getMethod("push", var4);
         Object[] var6 = new Object[]{var1};
         var5.invoke((Object)null, var6);
      } catch (ClassNotFoundException var7) {
         throw new AssertionError(var7);
      } catch (SecurityException var8) {
         throw new AssertionError(var8);
      } catch (NoSuchMethodException var9) {
         throw new AssertionError(var9);
      } catch (IllegalArgumentException var10) {
         throw new AssertionError(var10);
      } catch (IllegalAccessException var11) {
         throw new AssertionError(var11);
      } catch (InvocationTargetException var12) {
         throw new AssertionError(var12);
      }
   }

   public Hashtable getProperties(Object var1) {
      return ((Environment)var1).getProperties();
   }

   public boolean isAdminModeAccessException(NamingException var1) {
      return false;
   }

   public String getClusterDefaultLoadAlgorithm() {
      return "round-robin";
   }

   public boolean isMigratableActivatingException(RemoteException var1) {
      return false;
   }

   public boolean isMigratableInactiveException(RemoteException var1) {
      return false;
   }

   public boolean isServerInCluster() {
      return false;
   }

   public AuthenticatedSubject getCurrentSubjectForWire(AuthenticatedSubject var1) {
      return SecurityServiceManager.getCurrentSubjectForWire(var1);
   }

   public boolean rmiShutdownAcceptRequest(int var1, AuthenticatedSubject var2) {
      return true;
   }

   public boolean nonTxRmiShutdownAcceptRequest(int var1, AuthenticatedSubject var2, Object var3) {
      return true;
   }

   private static final class DefaultErrorHandler implements ErrorHandler {
      private DefaultErrorHandler() {
      }

      public void error(SAXParseException var1) throws SAXException {
         throw var1;
      }

      public void fatalError(SAXParseException var1) throws SAXException {
         throw var1;
      }

      public void warning(SAXParseException var1) {
      }

      // $FF: synthetic method
      DefaultErrorHandler(Object var1) {
         this();
      }
   }

   private static final class DefaultRMIEntityResolver implements EntityResolver {
      private DefaultRMIEntityResolver() {
      }

      public InputSource resolveEntity(String var1, String var2) {
         int var3 = var2.lastIndexOf(47);
         String var4;
         if (var3 >= 0) {
            var4 = var2.substring(var3 + 1);
         } else {
            var4 = var2;
         }

         if (var4 != null && var4.equals("rmi.dtd")) {
            InputStream var5 = this.getClass().getClassLoader().getResourceAsStream("weblogic/rmi/internal/rmi.dtd");
            if (var5 != null) {
               return new InputSource(var5);
            }
         }

         return null;
      }

      // $FF: synthetic method
      DefaultRMIEntityResolver(Object var1) {
         this();
      }
   }
}
