package weblogic.wsee.jaxws;

import com.ctc.wstx.stax.WstxInputFactory;
import com.ctc.wstx.stax.WstxOutputFactory;
import com.sun.istack.Nullable;
import com.sun.xml.ws.api.SAAJFactory;
import com.sun.xml.ws.api.WSBinding;
import com.sun.xml.ws.api.WSDLLocator;
import com.sun.xml.ws.api.addressing.AddressingVersion;
import com.sun.xml.ws.api.message.Packet;
import com.sun.xml.ws.api.model.SEIModel;
import com.sun.xml.ws.api.model.wsdl.WSDLPort;
import com.sun.xml.ws.api.pipe.Fiber;
import com.sun.xml.ws.api.pipe.ServerTubeAssemblerContext;
import com.sun.xml.ws.api.pipe.Tube;
import com.sun.xml.ws.api.pipe.TubelineAssemblerFactory;
import com.sun.xml.ws.api.server.BoundEndpoint;
import com.sun.xml.ws.api.server.Container;
import com.sun.xml.ws.api.server.ContainerResolver;
import com.sun.xml.ws.api.server.Invoker;
import com.sun.xml.ws.api.server.WSEndpoint;
import com.sun.xml.ws.api.streaming.XMLStreamReaderFactory;
import com.sun.xml.ws.api.streaming.XMLStreamWriterFactory;
import com.sun.xml.ws.client.WSServiceDelegate;
import com.sun.xml.ws.fault.SOAPFaultBuilder;
import com.sun.xml.ws.message.BinaryNodePropertyBag;
import com.sun.xml.ws.message.DOMWriter;
import com.sun.xml.ws.message.DOMWriterFactory;
import com.sun.xml.ws.message.SAX2DOMWriterEx;
import com.sun.xml.ws.message.SAX2DOMWriterExFactory;
import com.sun.xml.ws.model.AbstractSEIModelImpl;
import com.sun.xml.ws.model.wsdl.WSDLServiceImpl;
import com.sun.xml.ws.server.EndpointAwareTube;
import com.sun.xml.ws.server.EndpointFactory;
import com.sun.xml.ws.server.ServiceDefinitionImpl;
import com.sun.xml.ws.server.WSEndpointImpl;
import com.sun.xml.ws.server.provider.ProviderInvokerTube;
import com.sun.xml.ws.server.sei.SEIInvokerTube;
import com.sun.xml.ws.transport.http.ResourceLoader;
import com.sun.xml.ws.transport.http.client.HttpTransportPipe;
import com.sun.xml.ws.transport.http.servlet.ServletAdapter;
import com.sun.xml.ws.transport.http.servlet.ServletAdapterList;
import com.sun.xml.ws.transport.http.servlet.ServletModule;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import javax.activation.DataHandler;
import javax.servlet.ServletContext;
import javax.xml.namespace.QName;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPMessage;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Source;
import javax.xml.ws.Service;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceProvider;
import javax.xml.ws.soap.SOAPBinding;
import org.jvnet.staxex.XMLStreamWriterEx;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import weblogic.application.ApplicationContextInternal;
import weblogic.diagnostics.instrumentation.DelegatingMonitor;
import weblogic.diagnostics.instrumentation.DiagnosticAction;
import weblogic.diagnostics.instrumentation.DiagnosticActionState;
import weblogic.diagnostics.instrumentation.DynamicJoinPoint;
import weblogic.diagnostics.instrumentation.InstrumentationSupport;
import weblogic.diagnostics.instrumentation.JoinPoint;
import weblogic.diagnostics.instrumentation.PointcutHandlingInfo;
import weblogic.diagnostics.instrumentation.ValueHandlingInfo;
import weblogic.jws.jaxws.client.ClientIdentityFeature;
import weblogic.jws.jaxws.client.async.AsyncClientFeatureListFeature;
import weblogic.jws.jaxws.client.async.AsyncClientTransportFeature;
import weblogic.jws.jaxws.client.async.AsyncTransportProvider;
import weblogic.jws.jaxws.client.async.AsyncTransportProviderFactory;
import weblogic.kernel.KernelStatus;
import weblogic.logging.LoggingHelper;
import weblogic.management.provider.ManagementService;
import weblogic.management.runtime.WseeBaseRuntimeMBean;
import weblogic.management.runtime.WseeRuntimeMBean;
import weblogic.management.runtime.WseeV2RuntimeMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.servlet.internal.WebAppServletContext;
import weblogic.utils.collections.ConcurrentHashMap;
import weblogic.wsee.deploy.DeployInfo;
import weblogic.wsee.jaxws.framework.jaxrpc.EnvironmentFactory;
import weblogic.wsee.jaxws.framework.jaxrpc.JAXRPCEnvironmentFeature;
import weblogic.wsee.jaxws.framework.policy.WSDLPatchFilter;
import weblogic.wsee.jaxws.persistence.StandardPersistentPropertyRegister;
import weblogic.wsee.jaxws.spi.WLSProvider;
import weblogic.wsee.jaxws.tubeline.AbstractTubeFactory;
import weblogic.wsee.jaxws.tubeline.standard.WseeMemberSubmissionWsaServerTube;
import weblogic.wsee.jaxws.tubeline.standard.WseeWsaServerTube;
import weblogic.wsee.policy.runtime.PolicyServer;
import weblogic.wsee.util.JAXWSClassLoaderFactory;
import weblogic.wsee.util.StringUtil;
import weblogic.wsee.util.Verbose;

public class WLSContainer extends Container {
   private static Handler _logHandler;
   private static final WLSContainerResolver theResolver;
   private static final WLSAsyncTransportProviderFactory asyncFactory;
   private static final Container basic;
   private static final Logger LOGGER;
   private ServletContext servletContext;
   private WLSModule module;
   private WLSTubelineAssemblerFactory taFactory;
   private WLSEndpointFactory endpointFactory;
   private DeployInfo deployInfo;
   private volatile XMLStreamReaderFactory readerFactory;
   private volatile XMLStreamWriterFactory writerFactory;
   private volatile WLSSAAJFactory saajFac;
   private volatile WLSSAX2DOMWriterExFactory sax2DOMFac;
   private volatile WLSDOMWriterFactory domFac;
   private volatile WLSWSDLLocator locator;
   private volatile PolicyServer policyServer;
   private final ResourceLoader loader = new ResourceLoader() {
      static final long serialVersionUID = 1202159300286155441L;
      public static final String _WLDF$INST_VERSION = "9.0.0";
      // $FF: synthetic field
      static Class _WLDF$INST_FLD_class = Class.forName("weblogic.wsee.jaxws.WLSContainer$1");
      public static final DelegatingMonitor _WLDF$INST_FLD_Webservices_JAXWS_Diagnostic_Resource_After_Low;
      public static final JoinPoint _WLDF$INST_JPFLD_0;

      public URL getResource(String var1) throws MalformedURLException {
         boolean var9 = false;

         URL var10000;
         URL var4;
         try {
            var9 = true;
            var10000 = WLSContainer.this.servletContext.getResource("/WEB-INF/" + var1);
            var9 = false;
         } finally {
            if (var9) {
               var4 = null;
               if (_WLDF$INST_FLD_Webservices_JAXWS_Diagnostic_Resource_After_Low.isEnabledAndNotDyeFiltered()) {
                  DynamicJoinPoint var10002 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_0, (Object[])null, var4);
                  DelegatingMonitor var10003 = _WLDF$INST_FLD_Webservices_JAXWS_Diagnostic_Resource_After_Low;
                  InstrumentationSupport.process(var10002, var10003, var10003.getActions());
               }

            }
         }

         var4 = var10000;
         if (_WLDF$INST_FLD_Webservices_JAXWS_Diagnostic_Resource_After_Low.isEnabledAndNotDyeFiltered()) {
            DynamicJoinPoint var10001 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_0, (Object[])null, var4);
            DelegatingMonitor var11 = _WLDF$INST_FLD_Webservices_JAXWS_Diagnostic_Resource_After_Low;
            InstrumentationSupport.process(var10001, var11, var11.getActions());
         }

         return var10000;
      }

      public URL getCatalogFile() throws MalformedURLException {
         return WLSContainer.this.deployInfo != null ? WLSContainer.this.deployInfo.getCatalog() : WLSContainer.this.servletContext.getResource("/jax-ws-catalog.xml");
      }

      public Set<String> getResourcePaths(String var1) {
         return WLSContainer.this.servletContext.getResourcePaths("/WEB-INF/" + var1);
      }

      static {
         _WLDF$INST_FLD_Webservices_JAXWS_Diagnostic_Resource_After_Low = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "Webservices_JAXWS_Diagnostic_Resource_After_Low");
         _WLDF$INST_JPFLD_0 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "WLSContainer.java", "weblogic.wsee.jaxws.WLSContainer$1", "getResource", "(Ljava/lang/String;)Ljava/net/URL;", 239, InstrumentationSupport.makeMap(new String[]{"Webservices_JAXWS_Diagnostic_Resource_After_Low"}, new PointcutHandlingInfo[]{InstrumentationSupport.createPointcutHandlingInfo((ValueHandlingInfo)null, InstrumentationSupport.createValueHandlingInfo("uri", "weblogic.diagnostics.instrumentation.gathering.WebservicesJAXWSUriStringRenderer", false, true), (ValueHandlingInfo[])null)}), (boolean)0);
      }
   };
   private Map<PendingBoundEndpoint, String> pendingBE = new ConcurrentHashMap();
   static final long serialVersionUID = 8731714859264023229L;
   public static final String _WLDF$INST_VERSION = "9.0.0";
   // $FF: synthetic field
   static Class _WLDF$INST_FLD_class = Class.forName("weblogic.wsee.jaxws.WLSContainer");
   public static final DelegatingMonitor _WLDF$INST_FLD_Webservices_JAXWS_Diagnostic_Endpoint_Around_Low;
   public static final JoinPoint _WLDF$INST_JPFLD_0;

   public static void setupLogging() {
      try {
         Class.forName(HttpTransportPipe.class.getName());
      } catch (Exception var3) {
         var3.printStackTrace();
      }

      if (Verbose.isVerboseOn()) {
         Verbose.setupLogging();
         Handler var0 = _logHandler;
         Logger var1 = Logger.getLogger("com.sun.xml.ws");
         var1.setLevel(Level.INFO);
         addHandler(var0, var1);
         Logger var2 = Logger.getLogger("weblogic.wsee");
         var2.setLevel(Level.INFO);
         addHandler(var0, var2);
         Verbose.setLoggingLevelsAndAddHandler(var0);
      }

   }

   private static void addHandler(Handler var0, Logger var1) {
      Handler[] var2 = var1.getHandlers();
      if (!Arrays.asList(var2).contains(var0)) {
         var1.addHandler(var0);
      }

   }

   public static void setContainerResolver() {
      ContainerResolver.setInstance(theResolver);
   }

   public WLSContainer(ServletContext var1, DeployInfo var2) {
      this.servletContext = var1;
      this.module = new WLSModule(var1.getContextPath());
      this.taFactory = new WLSTubelineAssemblerFactory();
      this.endpointFactory = new WLSEndpointFactory();
      this.deployInfo = var2;
   }

   private DeployInfo getDeployInfo() {
      return this.deployInfo;
   }

   private PolicyServer getPolicyServer() {
      if (this.policyServer == null) {
         synchronized(this) {
            if (this.policyServer == null) {
               this.policyServer = this.deployInfo != null ? this.deployInfo.getWssPolicyContext().getPolicyServer() : new PolicyServer();
            }
         }
      }

      return this.policyServer;
   }

   public <T> T getSPI(Class<T> var1) {
      if (var1 == XMLStreamReaderFactory.class) {
         if (var1 == XMLStreamReaderFactory.class) {
            if (this.readerFactory == null) {
               synchronized(this) {
                  if (this.readerFactory == null) {
                     WstxInputFactory var3 = new WstxInputFactory();
                     var3.setProperty("javax.xml.stream.supportDTD", Boolean.FALSE);
                     var3.setProperty("javax.xml.stream.isSupportingExternalEntities", Boolean.FALSE);
                     var3.setProperty("javax.xml.stream.isReplacingEntityReferences", Boolean.FALSE);
                     this.readerFactory = new XMLStreamReaderFactory.Woodstox(var3);
                  }
               }
            }

            return var1.cast(this.readerFactory);
         } else {
            return var1.cast(this.readerFactory);
         }
      } else if (var1 == XMLStreamWriterFactory.class) {
         if (this.writerFactory == null) {
            synchronized(this) {
               if (this.writerFactory == null) {
                  this.writerFactory = new XMLStreamWriterFactory.NoLock(new WstxOutputFactory());
               }
            }
         }

         return var1.cast(this.writerFactory);
      } else if (var1 == SAAJFactory.class) {
         if (this.saajFac == null) {
            synchronized(this) {
               if (this.saajFac == null) {
                  this.saajFac = new WLSSAAJFactory();
               }
            }
         }

         return var1.cast(this.saajFac);
      } else if (var1 == SAX2DOMWriterExFactory.class) {
         if (this.sax2DOMFac == null) {
            synchronized(this) {
               if (this.sax2DOMFac == null) {
                  this.sax2DOMFac = new WLSSAX2DOMWriterExFactory();
               }
            }
         }

         return var1.cast(this.sax2DOMFac);
      } else if (var1 == DOMWriterFactory.class) {
         if (this.domFac == null) {
            synchronized(this) {
               if (this.domFac == null) {
                  this.domFac = new WLSDOMWriterFactory();
               }
            }
         }

         return var1.cast(this.domFac);
      } else if (var1.isAssignableFrom(ServletContext.class)) {
         return var1.cast(this.servletContext);
      } else if (var1.isAssignableFrom(ServletModule.class)) {
         return var1.cast(this.module);
      } else if (var1.isAssignableFrom(TubelineAssemblerFactory.class)) {
         return var1.cast(this.taFactory);
      } else if (var1.isAssignableFrom(DeployInfo.class)) {
         return var1.cast(this.getDeployInfo());
      } else if (var1 == PolicyServer.class) {
         return var1.cast(this.getPolicyServer());
      } else if (var1.isAssignableFrom(ResourceLoader.class)) {
         return var1.cast(this.loader);
      } else if (var1 == WSDLLocator.class) {
         if (this.locator == null) {
            synchronized(this) {
               if (this.locator == null) {
                  this.locator = new WLSWSDLLocator();
               }
            }
         }

         return var1.cast(this.locator);
      } else if (var1.isAssignableFrom(EndpointFactory.class)) {
         return var1.cast(this.endpointFactory);
      } else {
         return var1 == AsyncTransportProviderFactory.class ? var1.cast(asyncFactory) : null;
      }
   }

   public BoundEndpoint getBoundEndpoint(WSEndpoint var1) {
      Iterator var2 = this.module.endpoints.iterator();

      BoundEndpoint var3;
      do {
         if (!var2.hasNext()) {
            String var4 = (String)this.pendingBE.get(new PendingBoundEndpoint(var1.getServiceName(), var1.getPortName()));
            if (var4 != null) {
               return this.registerBoundEndpoint(var1, var4);
            }

            return null;
         }

         var3 = (BoundEndpoint)var2.next();
      } while(!var3.getEndpoint().equals(var1));

      return var3;
   }

   public void registerPendingBoundEndpoint(QName var1, QName var2, String var3) {
      this.pendingBE.put(new PendingBoundEndpoint(var1, var2), var3);
   }

   public BoundEndpoint registerBoundEndpoint(WSEndpoint var1, String var2) {
      boolean var11;
      boolean var10000 = var11 = _WLDF$INST_FLD_Webservices_JAXWS_Diagnostic_Endpoint_Around_Low.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var12 = null;
      DiagnosticActionState[] var13 = null;
      Object var10 = null;
      DelegatingMonitor var10001;
      if (var10000) {
         Object[] var6 = null;
         if (_WLDF$INST_FLD_Webservices_JAXWS_Diagnostic_Endpoint_Around_Low.isArgumentsCaptureNeeded()) {
            var6 = new Object[]{this, var1, var2};
         }

         DynamicJoinPoint var18 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_0, var6, (Object)null);
         var10001 = _WLDF$INST_FLD_Webservices_JAXWS_Diagnostic_Endpoint_Around_Low;
         DiagnosticAction[] var10002 = var12 = var10001.getActions();
         InstrumentationSupport.preProcess(var18, var10001, var10002, var13 = InstrumentationSupport.getActionStates(var10002));
      }

      boolean var15 = false;

      ServletAdapter var20;
      label104: {
         BoundEndpoint var19;
         try {
            var15 = true;
            Iterator var3 = this.module.endpoints.iterator();

            BoundEndpoint var4;
            do {
               if (!var3.hasNext()) {
                  ServletAdapter var17 = this.createAdapter(var1, var2);
                  this.pendingBE.remove(new PendingBoundEndpoint(var1.getServiceName(), var1.getPortName()));
                  var20 = var17;
                  var15 = false;
                  break label104;
               }

               var4 = (BoundEndpoint)var3.next();
            } while(!var4.getEndpoint().equals(var1));

            var19 = var4;
            var15 = false;
         } finally {
            if (var15) {
               var10001 = null;
               if (var11) {
                  InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_0, _WLDF$INST_FLD_Webservices_JAXWS_Diagnostic_Endpoint_Around_Low, var12, var13);
               }

            }
         }

         if (var11) {
            InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_0, _WLDF$INST_FLD_Webservices_JAXWS_Diagnostic_Endpoint_Around_Low, var12, var13);
         }

         return var19;
      }

      if (var11) {
         InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_0, _WLDF$INST_FLD_Webservices_JAXWS_Diagnostic_Endpoint_Around_Low, var12, var13);
      }

      return var20;
   }

   private ServletAdapter createAdapter(WSEndpoint var1, String var2) {
      String var3 = var2.startsWith("/") ? var2 : "/" + var2;
      Object var4 = this.deployInfo != null && this.deployInfo.getAdpaterList() != null ? this.deployInfo.getAdpaterList() : new WLSServletAdapterList();
      ServletAdapter var5 = (ServletAdapter)((ServletAdapterList)var4).createAdapter(this.deployInfo != null ? this.deployInfo.getPortComp().getPortComponentName() : null, var3, var1);
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("JAXWS Servlet Adapter:\n  name: " + var5.getName() + "\n  portName: " + var5.getPortName() + "\n  validPath: " + var5.getValidPath());
      }

      return var5;
   }

   public void setCurrent() {
      theResolver.set(this);
   }

   public void resetCurrent() {
      theResolver.remove();
   }

   public static String calculateEndpointId(WSBinding var0, boolean var1) {
      String var2 = null;
      AsyncClientFeatureListFeature var3 = (AsyncClientFeatureListFeature)var0.getFeature(AsyncClientFeatureListFeature.class);
      if (var3 != null) {
         ClientIdentityFeature var4 = (ClientIdentityFeature)var3.getClientFeatures().get(ClientIdentityFeature.class);
         if (var4 != null) {
            if (var1) {
               var2 = var4.getClientId();
            } else {
               var2 = var4.getRawClientId();
            }

            var2 = var2 + "-AsyncResponse";
         }
      }

      return var2;
   }

   static {
      _WLDF$INST_FLD_Webservices_JAXWS_Diagnostic_Endpoint_Around_Low = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "Webservices_JAXWS_Diagnostic_Endpoint_Around_Low");
      _WLDF$INST_JPFLD_0 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "WLSContainer.java", "weblogic.wsee.jaxws.WLSContainer", "registerBoundEndpoint", "(Lcom/sun/xml/ws/api/server/WSEndpoint;Ljava/lang/String;)Lcom/sun/xml/ws/api/server/BoundEndpoint;", 412, InstrumentationSupport.makeMap(new String[]{"Webservices_JAXWS_Diagnostic_Endpoint_Around_Low"}, new PointcutHandlingInfo[]{InstrumentationSupport.createPointcutHandlingInfo((ValueHandlingInfo)null, (ValueHandlingInfo)null, new ValueHandlingInfo[]{null, InstrumentationSupport.createValueHandlingInfo("uri", "weblogic.diagnostics.instrumentation.gathering.WebservicesJAXWSUriStringRenderer", false, true)})}), (boolean)0);
      System.setProperty(XMLStreamWriterFactory.class.getName() + ".woodstox", "true");
      System.setProperty(XMLStreamReaderFactory.class.getName() + ".woodstox", "true");
      if (KernelStatus.isServer() && ManagementService.getRuntimeAccess((AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction())).getDomain().isProductionModeEnabled()) {
         System.setProperty(SOAPFaultBuilder.class.getName() + ".disableCaptureStackTrace", "true");
      }

      _logHandler = new MyHandler();
      setupLogging();
      theResolver = new WLSContainerResolver();
      asyncFactory = new WLSAsyncTransportProviderFactory();
      basic = new BasicContainer();
      setContainerResolver();
      LOGGER = Logger.getLogger(WLSContainer.class.getName());
   }

   private static class WLSStandardPersistentPropertyRegister implements StandardPersistentPropertyRegister {
      private Set<String> props;
      private Set<String> classNames;

      private WLSStandardPersistentPropertyRegister() {
         this.props = new HashSet();
         this.classNames = new HashSet();
      }

      public Set<String> getStandardProperties() {
         return this.props;
      }

      public Set<String> getStandardPropertyBagClassNames() {
         return this.classNames;
      }

      // $FF: synthetic method
      WLSStandardPersistentPropertyRegister(Object var1) {
         this();
      }
   }

   private static class WLSAsyncTransportProviderFactory implements AsyncTransportProviderFactory {
      private WLSAsyncTransportProviderFactory() {
      }

      public AsyncTransportProvider create(AsyncClientTransportFeature var1) {
         return new weblogic.wsee.jaxws.client.async.AsyncTransportProvider(var1);
      }

      // $FF: synthetic method
      WLSAsyncTransportProviderFactory(Object var1) {
         this();
      }
   }

   private static class WLSDOMWriterFactory extends DOMWriterFactory {
      private Class binaryTextClass = null;
      private Method dataHandlerAccessor = null;

      public WLSDOMWriterFactory() {
         ClassLoader var1 = Thread.currentThread().getContextClassLoader();

         try {
            this.binaryTextClass = Class.forName("oracle.j2ee.ws.saaj.soap.BinaryTextImpl", true, var1);
            this.dataHandlerAccessor = this.binaryTextClass.getMethod("getDataHandler");
         } catch (ClassNotFoundException var3) {
         } catch (SecurityException var4) {
         } catch (NoSuchMethodException var5) {
         }

      }

      public DOMWriter create() {
         return new WLSDOMWriter();
      }

      private class WLSDOMWriter extends DOMWriter {
         private WLSDOMWriter() {
         }

         protected void writeTextNode(Node var1, XMLStreamWriter var2, BinaryNodePropertyBag var3) throws XMLStreamException {
            if (WLSDOMWriterFactory.this.binaryTextClass != null && WLSDOMWriterFactory.this.binaryTextClass.isAssignableFrom(var1.getClass()) && var2 instanceof XMLStreamWriterEx) {
               try {
                  ((XMLStreamWriterEx)var2).writeBinary((DataHandler)WLSDOMWriterFactory.this.dataHandlerAccessor.invoke(var1));
               } catch (IllegalArgumentException var5) {
                  throw new WebServiceException(var5);
               } catch (IllegalAccessException var6) {
                  throw new WebServiceException(var6);
               } catch (InvocationTargetException var7) {
                  throw new WebServiceException(var7.getCause());
               }
            } else {
               super.writeTextNode(var1, var2, var3);
            }
         }

         // $FF: synthetic method
         WLSDOMWriter(Object var2) {
            this();
         }
      }
   }

   private static class WLSSAX2DOMWriterExFactory extends SAX2DOMWriterExFactory {
      private Class soapDocClass = null;
      private Class binaryTextClass = null;
      private Constructor binaryTextConstructor = null;

      public WLSSAX2DOMWriterExFactory() {
         ClassLoader var1 = Thread.currentThread().getContextClassLoader();

         try {
            this.soapDocClass = Class.forName("oracle.j2ee.ws.saaj.soap.SOAPDoc", true, var1);
            this.binaryTextClass = Class.forName("oracle.j2ee.ws.saaj.soap.BinaryTextImpl", true, var1);
            Class var2 = Class.forName("oracle.j2ee.ws.saaj.soap.MessageImpl", true, var1);
            this.binaryTextConstructor = this.binaryTextClass.getConstructor(var2, DataHandler.class, Document.class);
         } catch (ClassNotFoundException var3) {
         } catch (SecurityException var4) {
         } catch (NoSuchMethodException var5) {
         }

      }

      public SAX2DOMWriterEx create(SOAPMessage var1) throws ParserConfigurationException {
         return new WLSSAX2DOMWriterEx(var1);
      }

      public SAX2DOMWriterEx create(SOAPMessage var1, BinaryNodePropertyBag var2) throws ParserConfigurationException {
         return new WLSSAX2DOMWriterEx(var1, var2);
      }

      public SAX2DOMWriterEx create(SOAPMessage var1, Node var2, BinaryNodePropertyBag var3) {
         return new WLSSAX2DOMWriterEx(var1, var2, var3);
      }

      public SAX2DOMWriterEx create(SOAPMessage var1, Node var2, BinaryNodePropertyBag var3, boolean var4) {
         return new WLSSAX2DOMWriterEx(var1, var2, var3, var4);
      }

      public SAX2DOMWriterEx create(SOAPMessage var1, Node var2) {
         return new WLSSAX2DOMWriterEx(var1, var2);
      }

      private class WLSSAX2DOMWriterEx extends SAX2DOMWriterEx {
         private SOAPMessage msg;

         public WLSSAX2DOMWriterEx(SOAPMessage var2) throws ParserConfigurationException {
            this(var2, (BinaryNodePropertyBag)((BinaryNodePropertyBag)null));
         }

         public WLSSAX2DOMWriterEx(SOAPMessage var2, BinaryNodePropertyBag var3) throws ParserConfigurationException {
            super(var3);
            this.msg = var2;
         }

         public WLSSAX2DOMWriterEx(SOAPMessage var2, Node var3) {
            this(var2, var3, (BinaryNodePropertyBag)null);
         }

         public WLSSAX2DOMWriterEx(SOAPMessage var2, Node var3, BinaryNodePropertyBag var4) {
            this(var2, var3, var4, false);
         }

         public WLSSAX2DOMWriterEx(SOAPMessage var2, Node var3, BinaryNodePropertyBag var4, boolean var5) {
            super(var3, var4, var5);
            this.msg = var2;
         }

         public void writeBinary(DataHandler var1) throws XMLStreamException {
            if (WLSSAX2DOMWriterExFactory.this.soapDocClass != null && WLSSAX2DOMWriterExFactory.this.soapDocClass.isAssignableFrom(this.document.getClass())) {
               Node var2 = (Node)this.nodeStack.peek();

               Text var3;
               try {
                  var3 = (Text)WLSSAX2DOMWriterExFactory.this.binaryTextConstructor.newInstance(this.msg, var1, this.document);
               } catch (IllegalArgumentException var5) {
                  throw new WebServiceException(var5);
               } catch (InstantiationException var6) {
                  throw new WebServiceException(var6);
               } catch (IllegalAccessException var7) {
                  throw new WebServiceException(var7);
               } catch (InvocationTargetException var8) {
                  throw new WebServiceException(var8.getCause());
               }

               var2.appendChild(var3);
            } else {
               super.writeBinary(var1);
            }
         }
      }
   }

   private static class WLSSAAJFactory extends SAAJFactory {
      private Map<String, MessageFactory> msgFacCache;
      private Map<String, SOAPFactory> soapFacCache;
      private static final JAXWSClassLoaderFactory fac = JAXWSClassLoaderFactory.getInstance();

      private WLSSAAJFactory() {
         this.msgFacCache = new ConcurrentHashMap();
         this.soapFacCache = new ConcurrentHashMap();
      }

      public MessageFactory createMessageFactory(String var1) throws SOAPException {
         MessageFactory var2 = (MessageFactory)this.msgFacCache.get(var1);
         if (var2 == null) {
            ClassLoader var3 = Thread.currentThread().getContextClassLoader();
            fac.setContextLoader(var3);

            try {
               var2 = super.createMessageFactory(var1);
               this.msgFacCache.put(var1, var2);
            } finally {
               Thread.currentThread().setContextClassLoader(var3);
            }
         }

         return var2;
      }

      public SOAPFactory createSOAPFactory(String var1) throws SOAPException {
         SOAPFactory var2 = (SOAPFactory)this.soapFacCache.get(var1);
         if (var2 == null) {
            ClassLoader var3 = Thread.currentThread().getContextClassLoader();
            fac.setContextLoader(var3);

            try {
               var2 = super.createSOAPFactory(var1);
               this.soapFacCache.put(var1, var2);
            } finally {
               Thread.currentThread().setContextClassLoader(var3);
            }
         }

         return var2;
      }

      // $FF: synthetic method
      WLSSAAJFactory(Object var1) {
         this();
      }
   }

   static class WLSEndpointFactory extends EndpointFactory {
      protected <T> WSEndpoint<T> createEndpoint(QName var1, QName var2, WSBinding var3, Container var4, SEIModel var5, WSDLPort var6, Class<T> var7, ServiceDefinitionImpl var8, EndpointAwareTube var9, boolean var10) {
         WLSEndpointImpl var11 = new WLSEndpointImpl(var1, var2, var3, var4, this, var5, var6, var7, var8, var9, var10);
         DeployInfo var12 = var4 != null ? (DeployInfo)var4.getSPI(DeployInfo.class) : null;
         boolean var13 = var12 != null && var12.getWsdlDef() != null;
         if (!var13) {
            var13 = !StringUtil.isEmpty(EndpointFactory.getWsdlLocation(var7));
            if (!var13 && var11.getServiceDefinition() != null && this.isProviderClass(var7)) {
               EnvironmentFactory var14 = JAXRPCEnvironmentFeature.getFactory((WSEndpoint)var11);
               var13 = var14.getWsdlDef() != null;
            }
         }

         if (var8 != null && var13) {
            var8.addFilter(new WSDLPatchFilter(var11));
         }

         return var11;
      }

      public <T> WSEndpoint<T> createSpliceEndpoint(QName var1, QName var2, WSBinding var3, Container var4, SEIModel var5, WSDLPort var6, Tube var7, Tube var8) {
         return new WLSEndpointImpl(var1, var2, var3, var4, this, var5, var6, var7, var8);
      }

      public <T> WSEndpoint<T> createSpliceEndpoint(WSEndpoint<T> var1, SEIModel var2, Tube var3, Tube var4) {
         return new WLSEndpointImpl(var1, this, var2, var3, var4);
      }

      protected EndpointAwareTube createSEIInvokerTube(final AbstractSEIModelImpl var1, final Invoker var2, final WSBinding var3) {
         return new EndpointAwareLateInitTube(var3, new AbstractTubeFactory() {
            public Tube createServer(Tube var1x, ServerTubeAssemblerContext var2x) {
               return new SEIInvokerTube(var1, var2, var3);
            }
         });
      }

      protected <T> EndpointAwareTube createProviderInvokerTube(final Class<T> var1, final WSBinding var2, final Invoker var3) {
         return new EndpointAwareLateInitTube(var2, new AbstractTubeFactory() {
            public Tube createServer(Tube var1x, ServerTubeAssemblerContext var2x) {
               return ProviderInvokerTube.create(var1, var2, var3);
            }
         });
      }

      private boolean isProviderClass(Class<?> var1) {
         return var1.getAnnotation(WebServiceProvider.class) != null;
      }

      static class WLSEndpointImpl<T> extends WSEndpointImpl<T> {
         private WseeBaseRuntimeMBean _wseeRuntimeMBean;
         private WLSStandardPersistentPropertyRegister sppr = new WLSStandardPersistentPropertyRegister();
         private String _cachedEndpointId = null;

         public WLSEndpointImpl(QName var1, QName var2, WSBinding var3, Container var4, EndpointFactory var5, SEIModel var6, WSDLPort var7, Class var8, ServiceDefinitionImpl var9, EndpointAwareTube var10, boolean var11) {
            super(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11);
         }

         public WLSEndpointImpl(QName var1, QName var2, WSBinding var3, Container var4, EndpointFactory var5, SEIModel var6, WSDLPort var7, Tube var8) {
            super(var1, var2, var3, var4, var5, var6, var7, var8);
         }

         public WLSEndpointImpl(QName var1, QName var2, WSBinding var3, Container var4, EndpointFactory var5, SEIModel var6, WSDLPort var7, Tube var8, Tube var9) {
            super(var1, var2, var3, var4, var5, var6, var7, var8, var9);
         }

         public WLSEndpointImpl(WSEndpoint var1, EndpointFactory var2, Tube var3) {
            super(var1, var2, var3);
         }

         public WLSEndpointImpl(WSEndpoint var1, EndpointFactory var2, SEIModel var3, Tube var4, Tube var5) {
            super(var1, var2, var3, var4, var5);
         }

         protected WSServiceDelegate createDelegate(SEIModel var1) {
            return WLSProvider.getInstance().createServiceDelegate((Source)null, (WSDLServiceImpl)((WSDLServiceImpl)(var1 != null ? var1.getPort().getOwner() : null)), (QName)this.getServiceName(), (Class)Service.class);
         }

         public String getEndpointId() {
            String var1 = super.getEndpointId();
            if (this.getContainer() instanceof WLSContainer) {
               if (this._cachedEndpointId == null) {
                  WLSContainer var2 = (WLSContainer)this.getContainer();
                  this._cachedEndpointId = this.calculateEndpointId(var2);
               }

               var1 = this._cachedEndpointId;
            }

            return var1;
         }

         protected ServerTubeAssemblerContext createServerTubeAssemblerContext(SEIModel var1, EndpointAwareTube var2, boolean var3) {
            return new ServerTubeAssemblerContext(var1, this.getPort(), this, var2, var3) {
               public Tube createWsaTube(Tube var1) {
                  WSBinding var2 = WLSEndpointImpl.this.getBinding();
                  if (var2 instanceof SOAPBinding && AddressingVersion.isEnabled(var2)) {
                     return (Tube)(AddressingVersion.fromBinding(var2) == AddressingVersion.MEMBER ? new WseeMemberSubmissionWsaServerTube(WLSEndpointImpl.this, WLSEndpointImpl.this.getPort(), var2, var1) : new WseeWsaServerTube(WLSEndpointImpl.this, WLSEndpointImpl.this.getPort(), WLSEndpointImpl.this.getBinding(), var1));
                  } else {
                     return var1;
                  }
               }
            };
         }

         private String calculateEndpointId(WLSContainer var1) {
            DeployInfo var3 = (DeployInfo)var1.getSPI(DeployInfo.class);
            String var2;
            if (var3 != null) {
               var2 = this.calculateEndpointId(var1, var3);
            } else {
               WSBinding var4 = this.getBinding();
               var2 = WLSContainer.calculateEndpointId(var4, true);
               if (var2 == null) {
                  var2 = this.calculateEndpointId(var1, var3);
               }
            }

            return var2;
         }

         private String calculateEndpointId(WLSContainer var1, @Nullable DeployInfo var2) {
            WebAppServletContext var4 = (WebAppServletContext)var1.getSPI(ServletContext.class);
            ApplicationContextInternal var5 = var4.getApplicationContext();
            String var6 = var2 != null ? var2.getApplication() : var5.getApplicationId();
            String var7 = "1";
            int var8 = var6.indexOf(35);
            if (var8 >= 0) {
               var7 = var6.substring(var8 + 1);
               var6 = var6.substring(0, var8);
            }

            String var9 = var4.getContextPath();
            String var10 = var2 != null ? var2.getModuleName() : var9;
            String var11 = this.getServiceName().getLocalPart();
            StringBuilder var12 = new StringBuilder(var6);
            if (var7 != null) {
               var12.append('#').append(var7);
            }

            var12.append('!').append(var10);
            var12.append('!').append(var11);
            var12.append('!').append(this.getPortName().getLocalPart());
            String var3 = var12.toString();
            return var3;
         }

         public WseeBaseRuntimeMBean getWseeRuntimeMBean() {
            return this._wseeRuntimeMBean;
         }

         public void setWseeRuntimeMBean(WseeBaseRuntimeMBean var1) {
            this._wseeRuntimeMBean = var1;
         }

         public <T> T getSPI(Class<T> var1) {
            if (var1 == WseeRuntimeMBean.class) {
               return var1.cast(this._wseeRuntimeMBean);
            } else if (var1 == WseeV2RuntimeMBean.class) {
               return var1.cast(this._wseeRuntimeMBean);
            } else {
               return var1 == StandardPersistentPropertyRegister.class ? var1.cast(this.sppr) : super.getSPI(var1);
            }
         }
      }
   }

   private static class WLSWSDLLocator extends WSDLLocator {
      private WLSWSDLLocator() {
      }

      public URL locateWSDL(Class<Service> var1, String var2) throws MalformedURLException {
         return WLSProvider.locateWsdl(var1, var2);
      }

      // $FF: synthetic method
      WLSWSDLLocator(Object var1) {
         this();
      }
   }

   private static class BasicContainer extends Container {
      private volatile PolicyServer policyServer;
      private volatile XMLStreamReaderFactory readerFactory;
      private volatile XMLStreamWriterFactory writerFactory;
      private volatile WLSSAAJFactory saajFac;
      private volatile WLSWSDLLocator locator;

      private BasicContainer() {
      }

      public <T> T getSPI(Class<T> var1) {
         if (var1 == XMLStreamReaderFactory.class) {
            if (this.readerFactory == null) {
               synchronized(this) {
                  if (this.readerFactory == null) {
                     this.readerFactory = new XMLStreamReaderFactory.Woodstox(new WstxInputFactory());
                  }
               }
            }

            return var1.cast(this.readerFactory);
         } else if (var1 == XMLStreamWriterFactory.class) {
            if (this.writerFactory == null) {
               synchronized(this) {
                  if (this.writerFactory == null) {
                     this.writerFactory = new XMLStreamWriterFactory.NoLock(new WstxOutputFactory());
                  }
               }
            }

            return var1.cast(this.writerFactory);
         } else if (var1 == SAAJFactory.class) {
            if (this.saajFac == null) {
               synchronized(this) {
                  if (this.saajFac == null) {
                     this.saajFac = new WLSSAAJFactory();
                  }
               }
            }

            return var1.cast(this.saajFac);
         } else if (var1 == PolicyServer.class) {
            if (this.policyServer == null) {
               synchronized(this) {
                  if (this.policyServer == null) {
                     this.policyServer = new PolicyServer();
                  }
               }
            }

            return var1.cast(this.policyServer);
         } else if (var1 == WSDLLocator.class) {
            if (this.locator == null) {
               synchronized(this) {
                  if (this.locator == null) {
                     this.locator = new WLSWSDLLocator();
                  }
               }
            }

            return var1.cast(this.locator);
         } else {
            return var1 == AsyncTransportProviderFactory.class ? var1.cast(WLSContainer.asyncFactory) : null;
         }
      }

      // $FF: synthetic method
      BasicContainer(Object var1) {
         this();
      }
   }

   private static class WLSContainerResolver extends ContainerResolver {
      private ThreadLocal<List<WLSContainer>> containers;

      private WLSContainerResolver() {
         this.containers = new ThreadLocal<List<WLSContainer>>() {
            protected List<WLSContainer> initialValue() {
               return new ArrayList();
            }
         };
      }

      public Container getContainer() {
         Fiber var1 = Fiber.getCurrentIfSet();
         if (var1 != null) {
            Packet var2 = var1.getPacket();
            if (var2 != null) {
               WSEndpoint var3 = var2.endpoint;
               if (var3 != null) {
                  return var3.getContainer();
               }
            }
         }

         List var4 = (List)this.containers.get();
         return var4.size() > 0 ? (Container)var4.get(var4.size() - 1) : WLSContainer.basic;
      }

      public void set(WLSContainer var1) {
         ((List)this.containers.get()).add(var1);
      }

      public void remove() {
         List var1 = (List)this.containers.get();
         if (var1.size() > 0) {
            var1.remove(var1.size() - 1);
         }

      }

      // $FF: synthetic method
      WLSContainerResolver(Object var1) {
         this();
      }
   }

   public class WLSModule extends ServletModule {
      private List<BoundEndpoint> endpoints = new ArrayList();
      private String cp;

      public WLSModule(String var2) {
         this.cp = var2;
      }

      public List<BoundEndpoint> getBoundEndpoints() {
         return this.endpoints;
      }

      public String getContextPath() {
         return this.cp;
      }
   }

   private static class PendingBoundEndpoint {
      public QName service;
      public QName port;

      public PendingBoundEndpoint(QName var1, QName var2) {
         this.service = var1;
         this.port = var2;
      }

      public int hashCode() {
         int var1 = 17;
         var1 = 37 * var1 + this.service.hashCode();
         var1 = 37 * var1 + this.port.hashCode();
         return var1;
      }

      public boolean equals(Object var1) {
         if (!(var1 instanceof PendingBoundEndpoint)) {
            return false;
         } else {
            PendingBoundEndpoint var2 = (PendingBoundEndpoint)var1;
            return this.service.equals(var2.service) && this.port.equals(var2.port);
         }
      }
   }

   private static class MyHandler extends Handler {
      private Logger wlsLogger = null;

      public MyHandler() {
         if (KernelStatus.isServer()) {
            this.wlsLogger = LoggingHelper.getServerLogger();
         }

      }

      public void close() throws SecurityException {
      }

      public void flush() {
      }

      public void publish(LogRecord var1) {
         if (this.wlsLogger != null && Verbose.isVerbose(var1.getSourceClassName())) {
            this.wlsLogger.log(var1);
         }

      }
   }
}
