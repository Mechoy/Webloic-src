package weblogic.wsee.jaxws.spi;

import com.sun.istack.NotNull;
import com.sun.istack.Nullable;
import com.sun.xml.stream.buffer.XMLStreamBufferResult;
import com.sun.xml.ws.api.BindingID;
import com.sun.xml.ws.api.WSBinding;
import com.sun.xml.ws.api.server.Container;
import com.sun.xml.ws.api.server.ContainerResolver;
import com.sun.xml.ws.api.server.InstanceResolver;
import com.sun.xml.ws.api.server.SDDocumentSource;
import com.sun.xml.ws.api.server.WSEndpoint;
import com.sun.xml.ws.binding.BindingImpl;
import com.sun.xml.ws.binding.ImpliesWebServiceFeature;
import com.sun.xml.ws.binding.WebServiceFeatureList;
import com.sun.xml.ws.developer.MemberSubmissionAddressingFeature;
import com.sun.xml.ws.server.EndpointFactory;
import com.sun.xml.ws.server.ServerRtException;
import com.sun.xml.ws.server.WSEndpointImpl;
import com.sun.xml.ws.util.xml.XmlUtil;
import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.ws.Binding;
import javax.xml.ws.Endpoint;
import javax.xml.ws.EndpointReference;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.WebServicePermission;
import javax.xml.ws.soap.AddressingFeature;
import javax.xml.ws.wsaddressing.W3CEndpointReference;
import org.w3c.dom.Element;
import weblogic.application.ApplicationAccess;
import weblogic.application.ApplicationContextInternal;
import weblogic.application.ApplicationException;
import weblogic.application.ApplicationLifecycleEvent;
import weblogic.application.ApplicationLifecycleListener;
import weblogic.application.ModuleException;
import weblogic.application.UpdateListener;
import weblogic.deploy.api.shared.WebLogicModuleType;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.DescriptorManager;
import weblogic.j2ee.descriptor.ServletBean;
import weblogic.j2ee.descriptor.ServletMappingBean;
import weblogic.j2ee.descriptor.WebAppBean;
import weblogic.j2ee.descriptor.wl.DeploymentPlanBean;
import weblogic.j2ee.descriptor.wl.WeblogicWebAppBean;
import weblogic.jws.jaxws.client.async.AsyncClientFeatureListFeature;
import weblogic.kernel.KernelStatus;
import weblogic.management.configuration.NetworkAccessPointMBean;
import weblogic.management.configuration.SSLMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.servlet.ReferencedAttribute;
import weblogic.servlet.internal.HttpServer;
import weblogic.servlet.internal.ServletContextManager;
import weblogic.servlet.internal.WarSource;
import weblogic.servlet.internal.WebAppModule;
import weblogic.servlet.internal.WebAppParser;
import weblogic.servlet.internal.WebAppServletContext;
import weblogic.servlet.internal.WebService;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.collections.ConcurrentHashMap;
import weblogic.utils.jars.VirtualJarFile;
import weblogic.wsee.WseeCoreLogger;
import weblogic.wsee.deploy.DeployInfo;
import weblogic.wsee.deploy.WseeWebappParser;
import weblogic.wsee.jaxws.JAXWSPublishServlet;
import weblogic.wsee.jaxws.WLSContainer;
import weblogic.wsee.server.ServerUtil;
import weblogic.wsee.util.Guid;
import weblogic.wsee.wstx.wsat.config.DDHelper;

public class WLSEndpoint extends Endpoint {
   private static final Logger LOGGER = Logger.getLogger(WLSEndpoint.class.getName());
   private static final WebServicePermission ENDPOINT_PUBLISH_PERMISSION = new WebServicePermission("publishEndpoint");
   private static final AuthenticatedSubject KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private final BindingImpl binding;
   private final Object implementor;
   private List<Source> metadata;
   private Executor executor;
   private Map<String, Object> properties = Collections.emptyMap();
   private boolean published;
   private boolean stopped;
   private WLSProvider provider;
   private Endpoint proxy = null;
   private WSEndpoint wse;
   private String address;
   private String publicAddress;
   private String mapping;
   private PublishWseeModule module;
   private List<Listener> _listeners = new ArrayList();
   private ReentrantReadWriteLock _listenersLock = new ReentrantReadWriteLock(false);

   public WLSEndpoint(@NotNull WLSProvider var1, @NotNull BindingID var2, @NotNull Object var3, Executor var4) {
      this.provider = var1;
      WebServiceFeatureList var5 = new WebServiceFeatureList(var3.getClass());
      WLSProvider.parseAnnotations(var5, var3.getClass(), true);
      DDHelper.updateFeatureFromJWS((Class)null, var3.getClass(), var5);
      if (var3 instanceof ImpliesWebServiceFeature) {
         ((ImpliesWebServiceFeature)var3).implyFeatures(var5);
      }

      this.binding = BindingImpl.create(var2, var5.toArray());
      this.implementor = var3;
      this.executor = var4;
   }

   public void addFeature(WebServiceFeature var1) {
      this.binding.addFeature(var1);
   }

   public void addFeatures(WebServiceFeature[] var1) {
      WebServiceFeature[] var2 = var1;
      int var3 = var1.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         WebServiceFeature var5 = var2[var4];
         this.addFeature(var5);
      }

   }

   public WSEndpoint getWSEndpoint() {
      return this.wse;
   }

   public Binding getBinding() {
      return (Binding)(this.proxy != null ? this.proxy.getBinding() : this.binding);
   }

   public Object getImplementor() {
      return this.implementor;
   }

   public void publish(String var1) {
      this.canPublish();

      URL var2;
      try {
         var2 = new URL(var1);
      } catch (MalformedURLException var4) {
         throw new IllegalArgumentException("Cannot create URL for this address " + var1);
      }

      if (!var2.getProtocol().equals("http") && !var2.getProtocol().equals("https")) {
         throw new IllegalArgumentException(var2.getProtocol() + " protocol based address is not supported");
      } else if (!var2.getPath().startsWith("/")) {
         throw new IllegalArgumentException("Incorrect WebService address=" + var1 + ". The address's path should start with /");
      } else {
         this.address = var1;
         this.publicAddress = calculatePublicAddressFromEndpointAddress(var2);
         this.createAndPublishEndpoint(var2);
      }
   }

   public void publish(Object var1) {
      this.canPublish();
      if (!WebAppServletContext.class.isAssignableFrom(var1.getClass())) {
         throw new IllegalArgumentException(var1.getClass() + " is not a supported context.");
      } else {
         WebAppServletContext var2 = (WebAppServletContext)var1;
         String var3 = WLSContainer.calculateEndpointId((WSBinding)this.getBinding(), false);
         if (var3 == null) {
            var3 = Guid.generateGuid();
         }

         String var4 = "/" + var3;
         StringBuffer var5 = new StringBuffer();
         var5.append(getServerAddress());
         var5.append(var2.getContextPath());
         var5.append(var4);
         this.address = var5.toString();
         this.publicAddress = calculatePublicAddressFromEndpointAddress(this.address);
         this.createAndPublishEndpoint(var2.getServer(), var2, var4);
      }
   }

   public static String getServerAddress() {
      return getServerAddress("http");
   }

   public static String getServerAddressForSSL() {
      return getServerAddress("https");
   }

   public static String getServerAddress(String var0) {
      if (var0 != null && var0.length() != 0) {
         ServerMBean var1 = ManagementService.getRuntimeAccess(KERNEL_ID).getServer();
         String var2 = var1.getListenAddress();
         if (var2 == null || var2.equals("")) {
            try {
               var2 = InetAddress.getLocalHost().getCanonicalHostName();
            } catch (UnknownHostException var9) {
               throw new WebServiceException(var9);
            }
         }

         int var3 = -1;
         if (var0.equalsIgnoreCase("https")) {
            if (var1.getSSL() != null) {
               SSLMBean var4 = var1.getSSL();
               if (var4.isEnabled()) {
                  var3 = var4.getListenPort();
               }
            }
         } else if (var1.isListenPortEnabled()) {
            var3 = var1.getListenPort();
         }

         if (var3 == -1) {
            NetworkAccessPointMBean[] var10 = var1.getNetworkAccessPoints();
            if (var10 != null) {
               NetworkAccessPointMBean[] var5 = var10;
               int var6 = var10.length;

               for(int var7 = 0; var7 < var6; ++var7) {
                  NetworkAccessPointMBean var8 = var5[var7];
                  if (var0.equalsIgnoreCase(var8.getProtocol())) {
                     var3 = var8.getListenPort();
                     if (var8.getListenAddress() != null) {
                        var2 = var8.getListenAddress();
                     }
                  }
               }
            }
         }

         StringBuffer var11 = new StringBuffer();
         var11.append(var0 + "://");
         var11.append(var2);
         if (var3 != -1) {
            var11.append(':');
            var11.append(var3);
         }

         return var11.toString();
      } else {
         throw new IllegalArgumentException("Bad protocol: " + var0);
      }
   }

   private static String calculatePublicAddressFromEndpointAddress(String var0) {
      if (!KernelStatus.isServer()) {
         return var0;
      } else {
         URL var1;
         try {
            var1 = new URL(var0);
         } catch (MalformedURLException var3) {
            throw new IllegalArgumentException("Cannot create URL for this address " + var0);
         }

         return calculatePublicAddressFromEndpointAddress(var1);
      }
   }

   private static String calculatePublicAddressFromEndpointAddress(URL var0) {
      if (!KernelStatus.isServer()) {
         return var0.toExternalForm();
      } else {
         String var1 = ServerUtil.getServerURL(var0.getProtocol());
         var1 = var1 + var0.getPath();
         return var1;
      }
   }

   public void addListener(Listener var1) {
      try {
         this._listenersLock.writeLock().lock();
         if (!this._listeners.contains(var1)) {
            this._listeners.add(var1);
         }
      } finally {
         this._listenersLock.writeLock().unlock();
      }

   }

   public void removeListener(Listener var1) {
      try {
         this._listenersLock.writeLock().lock();
         this._listeners.remove(var1);
      } finally {
         this._listenersLock.writeLock().unlock();
      }

   }

   private Listener[] getListeners() {
      Listener[] var1;
      try {
         this._listenersLock.readLock().lock();
         var1 = (Listener[])this._listeners.toArray(new Listener[this._listeners.size()]);
      } finally {
         this._listenersLock.readLock().unlock();
      }

      return var1;
   }

   public void stop() {
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("Stopping endpoint: " + this.toString());
      }

      Listener[] var1 = this.getListeners();
      Listener[] var2 = var1;
      int var3 = var1.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Listener var5 = var2[var4];

         try {
            var5.endpointStopping(this);
         } catch (Exception var13) {
            WseeCoreLogger.logUnexpectedException(var13.toString(), var13);
         }
      }

      if (this.proxy != null) {
         this.proxy.stop();
      } else if (this.isPublished()) {
         if (this.module != null) {
            try {
               this.module.deactivate();
               this.module.unprepare();
            } catch (ModuleException var11) {
               throw new WebServiceException(var11);
            } finally {
               this.module = null;
            }
         }

         ((JAXWSPublishServlet.Configuration)((Map)((ServletContext)this.wse.getContainer().getSPI(ServletContext.class)).getAttribute("CONFIGURATION_KEY")).get(this.mapping)).stopped = true;
         this.wse.dispose();
         this.published = false;
      }

      this.stopped = true;
   }

   public boolean isPublished() {
      return this.proxy != null ? this.proxy.isPublished() : this.published;
   }

   public List<Source> getMetadata() {
      return this.proxy != null ? this.proxy.getMetadata() : this.metadata;
   }

   public void setMetadata(List<Source> var1) {
      if (this.proxy != null) {
         this.proxy.setMetadata(var1);
      } else if (this.isPublished()) {
         throw new IllegalStateException("Cannot set Metadata. Endpoint is already published");
      }

      this.metadata = var1;
   }

   public Executor getExecutor() {
      return this.proxy != null ? this.proxy.getExecutor() : this.executor;
   }

   public void setExecutor(Executor var1) {
      if (this.proxy != null) {
         this.proxy.setExecutor(var1);
      }

      this.executor = var1;
   }

   public Map<String, Object> getProperties() {
      return (Map)(this.proxy != null ? this.proxy.getProperties() : new HashMap(this.properties));
   }

   public void setProperties(Map<String, Object> var1) {
      if (this.proxy != null) {
         this.proxy.setProperties(var1);
      }

      this.properties = new HashMap(var1);
   }

   private Endpoint initializeProxy(URL var1) {
      Endpoint var2 = this.provider.createEndpointInternal(this.binding.getBindingId(), this.implementor);
      var2.setExecutor(this.executor);
      var2.setMetadata(this.metadata);
      var2.setProperties(this.properties);
      var2.publish(var1.toExternalForm());
      return var2;
   }

   private void createAndPublishEndpoint(URL var1) {
      this.addFeaturesFromClientBinding();
      HttpServer var2 = this.findServer(var1);
      if (var2 != null) {
         ServletContextManager var3 = var2.getServletContextManager();
         String var4 = var1.getPath() != null ? var1.getPath() : "/";
         String var5 = var4;
         int var6 = var4.lastIndexOf(47);
         if (var6 > 0) {
            var5 = var4.substring(0, var6);
         }

         WebAppServletContext var7 = var3.getContext(var5);
         String var8;
         if (var7 != null) {
            var8 = var4.substring(var7.getContextPath().length());
         } else {
            var8 = var4.substring(var5.length());
            if (var8.length() == 0) {
               var8 = "/*";
            }

            this.createModule(var5, var8);
         }

         this.createAndPublishEndpoint(var2, var7, var8);
      }

      if (!this.published) {
         this.proxy = this.initializeProxy(var1);
      }

   }

   private void createModule(String var1, String var2) {
      Container var3 = ContainerResolver.getInstance().getContainer();
      String var4 = "";
      if (var3 != null) {
         WebAppServletContext var5 = (WebAppServletContext)var3.getSPI(ServletContext.class);
         if (var5 != null) {
            WebAppModule var6 = var5.getWebAppModule();
            if (var6 != null) {
               var4 = var6.getModuleURI();
            }
         }
      }

      ApplicationContextInternal var9 = ApplicationAccess.getApplicationAccess().getCurrentApplicationContext();
      GenericClassLoader var10 = var9.getAppClassLoader();
      this.module = new PublishWseeModule(var4, var1, var2);

      try {
         this.module.initUsingLoader(var9, var10, UpdateListener.Registration.NOOP);
         this.module.prepare();
      } catch (ModuleException var8) {
         throw new WebServiceException(var8);
      }
   }

   private HttpServer findServer(URL var1) {
      HttpServer var2 = WebService.getVirtualHost(var1.getHost());
      if (var2 == null) {
         var2 = WebService.defaultHttpServer();
      }

      return var2;
   }

   private void createAndPublishEndpoint(HttpServer var1, WebAppServletContext var2, String var3) {
      this.addFeaturesFromClientBinding();
      if (this.module != null) {
         Iterator var4 = this.module.getAllContexts();

         while(var4.hasNext()) {
            WebAppServletContext var5 = (WebAppServletContext)var4.next();
            if (var5.getServer().equals(var1)) {
               var2 = var5;
               break;
            }
         }
      }

      SecurityManager var22 = System.getSecurityManager();
      if (var22 != null) {
         var22.checkPermission(ENDPOINT_PUBLISH_PERMISSION);
      }

      WLSContainer var23 = new WLSContainer(var2, (DeployInfo)null);
      QName var6 = (QName)this.getProperty(QName.class, "javax.xml.ws.wsdl.service");
      if (var6 == null) {
         var6 = WSEndpoint.getDefaultServiceName(this.implementor.getClass());
      }

      QName var7 = (QName)this.getProperty(QName.class, "javax.xml.ws.wsdl.port");
      if (var7 == null) {
         var7 = WSEndpoint.getDefaultPortName(var6, this.implementor.getClass());
      }

      var23.registerPendingBoundEndpoint(var6, var7, var3);
      var23.setCurrent();

      try {
         this.wse = WSEndpoint.create(this.implementor.getClass(), true, InstanceResolver.createSingleton(this.implementor).createInvoker(), var6, var7, var23, this.binding, this.getPrimaryWsdl(var2), this.buildDocList(), (URL)null);
         this.wse.setExecutor(this.executor);
         this.mapping = var3;
         var2.getApplicationContext().addApplicationListener(new ApplicationLifecycleListener() {
            public void preStop(ApplicationLifecycleEvent var1) throws ApplicationException {
               WLSEndpoint.this.stop();
            }
         });
         JAXWSPublishServlet.Configuration var8 = new JAXWSPublishServlet.Configuration(var23, this.wse);
         synchronized(var2) {
            Object var10 = (Map)var2.getAttribute("CONFIGURATION_KEY");
            if (var10 == null) {
               var10 = new ReferencedConcurrentHashMap();
               var2.setAttribute("CONFIGURATION_KEY", var10);
            }

            ((Map)var10).put(var3, var8);
         }

         if (this.module == null) {
            try {
               HashMap var9 = new HashMap();
               var9.put("SERVICEURI_KEY", var3);
               String var21 = this.implementor.getClass().getSimpleName() + ":" + var3;
               if (var2.getNamedDispatcher(var21) == null) {
                  var2.registerServlet(var21, JAXWSPublishServlet.class.getName(), new String[]{var3}, var9, 0);
               } else {
                  var2.swapServlet(var21, JAXWSPublishServlet.class.getName(), var9);
               }
            } catch (Exception var18) {
               throw new WebServiceException(var18);
            }
         } else {
            try {
               var2.setAttribute("SERVICEURI_KEY", var3);
               this.module.activate();
               this.module.start();
            } catch (ModuleException var17) {
               throw new WebServiceException(var17);
            }
         }
      } finally {
         var23.resetCurrent();
      }

      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("Published endpoint: " + this.toString());
      }

      this.published = true;
   }

   private void addFeaturesFromClientBinding() {
      if (this.binding.isFeatureEnabled(AsyncClientFeatureListFeature.class)) {
         AsyncClientFeatureListFeature var1 = (AsyncClientFeatureListFeature)this.binding.getFeature(AsyncClientFeatureListFeature.class);
         if (var1.getClientFeatures().isEnabled(AddressingFeature.class)) {
            this.binding.addFeature(var1.getClientFeatures().get(AddressingFeature.class));
         }

         if (var1.getClientFeatures().isEnabled(MemberSubmissionAddressingFeature.class)) {
            this.binding.addFeature(var1.getClientFeatures().get(MemberSubmissionAddressingFeature.class));
         }
      }

   }

   private <T> T getProperty(Class<T> var1, String var2) {
      Object var3 = this.properties.get(var2);
      if (var3 == null) {
         return null;
      } else if (var1.isInstance(var3)) {
         return var1.cast(var3);
      } else {
         throw new IllegalArgumentException("Property " + var2 + " has to be of type " + var1);
      }
   }

   private List<SDDocumentSource> buildDocList() {
      ArrayList var1 = new ArrayList();
      if (this.metadata != null) {
         Transformer var2 = XmlUtil.newTransformer();
         Iterator var3 = this.metadata.iterator();

         while(var3.hasNext()) {
            Source var4 = (Source)var3.next();
            var1.add(this.convert(var2, var4));
         }
      }

      return var1;
   }

   private SDDocumentSource convert(Source var1) {
      Transformer var2 = XmlUtil.newTransformer();
      return this.convert(var2, var1);
   }

   private SDDocumentSource convert(Transformer var1, Source var2) {
      try {
         XMLStreamBufferResult var3 = new XMLStreamBufferResult();
         var1.transform(var2, var3);
         String var4 = var2.getSystemId();
         return SDDocumentSource.create(new URL(var4), var3.getXMLStreamBuffer());
      } catch (TransformerException var5) {
         throw new ServerRtException("server.rt.err", new Object[]{var5});
      } catch (IOException var6) {
         throw new ServerRtException("server.rt.err", new Object[]{var6});
      }
   }

   @Nullable
   private SDDocumentSource getPrimaryWsdl(WebAppServletContext var1) {
      Class var2 = this.implementor.getClass();
      String var3 = EndpointFactory.getWsdlLocation(var2);
      if (var3 != null) {
         ClassLoader var4 = var2.getClassLoader();
         URL var5 = var4.getResource(var3);
         if (var5 != null) {
            return SDDocumentSource.create(var5);
         } else {
            WarSource var6 = var1.getResourceAsSource(var3);
            if (var6 != null) {
               return SDDocumentSource.create(var6.getURL());
            } else {
               throw new ServerRtException("cannot.load.wsdl", new Object[]{var3});
            }
         }
      } else {
         return null;
      }
   }

   private void canPublish() {
      if (this.isPublished()) {
         throw new IllegalStateException("Cannot publish endpoint (" + this.toString() + "). Endpoint has been already published.");
      } else if (this.stopped) {
         throw new IllegalStateException("Cannot publish this endpoint (" + this.toString() + "). Endpoint has been already stopped.");
      }
   }

   public EndpointReference getEndpointReference(Element... var1) {
      return this.proxy != null ? this.proxy.getEndpointReference(var1) : this.getEndpointReference(W3CEndpointReference.class, var1);
   }

   public <T extends EndpointReference> T getEndpointReference(Class<T> var1, Element... var2) {
      if (this.proxy != null) {
         return this.proxy.getEndpointReference(var1, var2);
      } else if (!this.isPublished()) {
         throw new WebServiceException("Endpoint is not published yet");
      } else {
         WSEndpointImpl var3 = (WSEndpointImpl)this.wse;
         return (EndpointReference)var1.cast(var3.getEndpointReference(var1, this.publicAddress, this.publicAddress + "?WSDL", var2));
      }
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append(super.toString());
      var1.append(" - Endpoint ID: ").append(this.wse != null ? this.wse.getEndpointId() : null);
      var1.append(" - address: ").append(this.address);
      var1.append(" - public address: ").append(this.publicAddress);
      return var1.toString();
   }

   private static class ReferencedConcurrentHashMap extends ConcurrentHashMap implements ReferencedAttribute {
      public ReferencedConcurrentHashMap() {
      }
   }

   private class PublishWseeModule extends WebAppModule {
      private WebAppParser parser;
      private ServletBean servletBean;

      public PublishWseeModule(String var2, String var3, String var4) {
         super(var2, var3);
         this.parser = this.createWebDD(var4);
      }

      public String getType() {
         return WebLogicModuleType.MODULETYPE_WSEE;
      }

      protected WebAppParser getWebAppParser(VirtualJarFile var1, DeploymentPlanBean var2) throws ModuleException {
         return this.parser;
      }

      public ServletBean getServletBean() {
         return this.servletBean;
      }

      private WebAppParser createWebDD(String var1) {
         String var2 = WLSEndpoint.this.implementor.getClass().getSimpleName() + "Servlethttp";
         DescriptorBean var3 = (new DescriptorManager()).createDescriptorRoot(WebAppBean.class).getRootBean();
         WebAppBean var4 = (WebAppBean)var3;
         this.servletBean = var4.createServlet();
         this.servletBean.setServletName(var2);
         this.servletBean.setServletClass(JAXWSPublishServlet.class.getName());
         this.servletBean.setLoadOnStartup("1");
         ServletMappingBean var5 = var4.createServletMapping();
         var5.setServletName(var2);
         var5.setUrlPatterns(new String[]{var1});
         if (!"/".equals(var1)) {
            var5 = var4.createServletMapping();
            var5.setServletName(var2);
            var5.setUrlPatterns(new String[]{"/"});
         }

         return new WseeWebappParser(var4, (WeblogicWebAppBean)null);
      }
   }

   public interface Listener {
      void endpointStopping(WLSEndpoint var1);
   }
}
