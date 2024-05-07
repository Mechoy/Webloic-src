package weblogic.wsee.jaxws;

import com.sun.xml.ws.api.server.Container;
import com.sun.xml.ws.api.server.ContainerResolver;
import com.sun.xml.ws.api.streaming.XMLStreamReaderFactory;
import com.sun.xml.ws.api.streaming.XMLStreamWriterFactory;
import com.sun.xml.ws.api.wsdl.parser.WSDLParserExtension;
import com.sun.xml.ws.binding.WebServiceFeatureList;
import com.sun.xml.ws.model.RuntimeModeler;
import com.sun.xml.ws.model.wsdl.WSDLModelImpl;
import com.sun.xml.ws.util.ServiceFinder;
import com.sun.xml.ws.wsdl.parser.RuntimeWSDLParser;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.Iterator;
import java.util.Set;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.Service;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceRef;
import org.xml.sax.EntityResolver;
import weblogic.deployment.ServiceRefProcessor;
import weblogic.deployment.ServiceRefProcessorException;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.EditableDescriptorManager;
import weblogic.j2ee.descriptor.InjectionTargetBean;
import weblogic.j2ee.descriptor.ServiceRefBean;
import weblogic.j2ee.descriptor.wl.PortInfoBean;
import weblogic.j2ee.descriptor.wl.ServiceReferenceDescriptionBean;
import weblogic.j2ee.descriptor.wl.WeblogicWebAppBean;
import weblogic.j2ee.injection.PitchforkContext;
import weblogic.wsee.deploy.DeployInfo;
import weblogic.wsee.jaxws.handler.ClientHandlerChainsResolver;
import weblogic.wsee.jaxws.injection.WSEEClientComponentContributor;
import weblogic.wsee.jaxws.spi.WLSProvider;
import weblogic.wsee.jaxws.spi.WLSServiceDelegate;
import weblogic.wsee.tools.xcatalog.XCatalogUtil;
import weblogic.wsee.util.ClassUtil;
import weblogic.wsee.util.StringUtil;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.ws.WsException;
import weblogic.wsee.wsdl.WsdlException;
import weblogic.wsee.wstx.wsat.Transactional;
import weblogic.wsee.wstx.wsat.config.DDHelper;

public class ServiceRefProcessorImpl implements ServiceRefProcessor {
   private static final boolean verbose;
   private ServiceRefBean serviceRefBean;
   private ServiceReferenceDescriptionBean serviceReferenceDescriptionBean;
   private Object targetRef;
   private WebServiceFeatureList features = new WebServiceFeatureList();

   public ServiceRefProcessorImpl(ServiceRefBean var1, ServiceReferenceDescriptionBean var2, ServletContext var3) throws WsException {
      this.serviceRefBean = var1;
      this.serviceReferenceDescriptionBean = var2;
      if (this.serviceReferenceDescriptionBean == null) {
         EditableDescriptorManager var4 = new EditableDescriptorManager();
         WeblogicWebAppBean var5 = (WeblogicWebAppBean)var4.createDescriptorRoot(WeblogicWebAppBean.class).getRootBean();
         this.serviceReferenceDescriptionBean = var5.createServiceReferenceDescription();
      }

      try {
         ClassLoader var9 = Thread.currentThread().getContextClassLoader();
         Class var10 = null;
         String var6 = var1.getServiceRefType();
         if (!StringUtil.isEmpty(var6)) {
            var10 = var9.loadClass(var6);
         }

         this.parseAnnotations();
         if (var10 != null && Service.class.isAssignableFrom(var10)) {
            this.targetRef = this.createService(var10, var3);
            this.parseAnnotations(true);
         } else {
            Service var7 = this.createServiceInterface(var10, var9, var3);
            this.parseAnnotations(true);
            if (var10 == null) {
               this.targetRef = var7;
            } else {
               this.targetRef = var7.getPort(var10);
            }
         }

      } catch (Throwable var8) {
         throw new WsException("Error setting up WebServiceRef: " + var8, var8);
      }
   }

   private void parseAnnotations() throws Exception {
      this.parseAnnotations(false);
   }

   private void parseAnnotations(boolean var1) throws Exception {
      InjectionTargetBean[] var2 = this.serviceRefBean.getInjectionTargets();
      int var4 = var2.length;
      byte var5 = 0;
      if (var5 < var4) {
         InjectionTargetBean var6 = var2[var5];
         String var7 = var6.getInjectionTargetClass();
         String var8 = var6.getInjectionTargetName();
         Class var9 = ClassUtil.loadClass(var7);
         Class var10 = ClassUtil.loadClass(this.serviceRefBean.getServiceRefType());
         Object var11 = null;

         try {
            var11 = var9.getDeclaredField(var8);
         } catch (NoSuchFieldException var17) {
         }

         if (var11 == null || ((AccessibleObject)var11).getAnnotation(WebServiceRef.class) == null) {
            if (verbose) {
               Verbose.log((Object)(" no field  named " + var8 + "annotated with @WebServiceRef!"));
            }

            try {
               var11 = var9.getDeclaredMethod("set" + RuntimeModeler.capitalize(var8), var10);
            } catch (NoSuchMethodException var16) {
               throw new WebServiceException("can't find injection target");
            }
         }

         if (var11 != null) {
            if (var1) {
               Transactional var12 = (Transactional)((AccessibleObject)var11).getAnnotation(Transactional.class);
               if (var12 == null) {
                  return;
               }

               PortInfoBean var15;
               if (this.targetRef instanceof Service) {
                  for(Iterator var13 = ((Service)this.targetRef).getPorts(); var13.hasNext(); DDHelper.populateServiceRefDDFromAnnotation(var12, var15)) {
                     QName var14 = (QName)var13.next();
                     var15 = this.serviceReferenceDescriptionBean.lookupPortInfo(var14.getLocalPart());
                     if (var15 == null) {
                        var15 = this.serviceReferenceDescriptionBean.createPortInfo();
                        var15.setPortName(var14.getLocalPart());
                     }
                  }
               } else {
                  PortInfoBean var18 = this.serviceReferenceDescriptionBean.createPortInfo();
                  var18.setPortName(":");
                  DDHelper.populateServiceRefDDFromAnnotation(var12, var18);
               }
            } else {
               WLSProvider.parseAnnotations(this.features, (AnnotatedElement)var11, true);
            }
         }

      }
   }

   protected boolean isSet(String var1, Object var2) {
      return ((DescriptorBean)var2).isSet(var1);
   }

   private void configureHandlers(Service var1) throws Throwable {
      PitchforkContext var2 = new PitchforkContext((String)null);
      WSEEClientComponentContributor var3 = new WSEEClientComponentContributor(this.serviceRefBean.getHandlerChains(), var2);
      var3.init();
      ClientHandlerChainsResolver var4 = new ClientHandlerChainsResolver(var1.getServiceName(), var1.getPorts(), this.serviceRefBean.getHandlerChains(), var3);
      var1.setHandlerResolver(var4);
   }

   private Service createServiceInterface(Class var1, ClassLoader var2, ServletContext var3) throws Throwable {
      String var4 = this.serviceRefBean.getServiceInterface();
      if (StringUtil.isEmpty(var4)) {
         if (var1 == null) {
            throw new IllegalArgumentException("A ServiceInterface must be specified if no service-ref-type is specified");
         } else {
            throw new IllegalArgumentException("A ServiceInterface must be specified if using a SEI reference " + var1.getName());
         }
      } else {
         Class var5 = var2.loadClass(var4);
         if (!Service.class.isAssignableFrom(var5)) {
            throw new IllegalArgumentException("ServiceInterface " + var5.getName() + " must extend " + Service.class.getName());
         } else {
            return this.createService(var5, var3);
         }
      }
   }

   private Service createService(Class var1, ServletContext var2) throws Throwable {
      URL var3 = this.getWsdlURL(var1, var2);
      QName var4 = this.getServiceQName(var1, var3);
      WLSServiceDelegate.serviceRefLocal.set(this.serviceRefBean);
      WLSServiceDelegate.serviceReferenceDescriptionLocal.set(this.serviceReferenceDescriptionBean);
      WLSServiceDelegate.featuresLocal.set(this.features);

      Service var5;
      try {
         if (Service.class.equals(var1)) {
            var5 = Service.create(var3, var4);
         } else {
            Constructor var6 = var1.getConstructor(URL.class, QName.class);
            var5 = (Service)var6.newInstance(var3, var4);
         }
      } finally {
         WLSServiceDelegate.serviceRefLocal.set((Object)null);
         WLSServiceDelegate.serviceReferenceDescriptionLocal.set((Object)null);
         WLSServiceDelegate.featuresLocal.set((Object)null);
      }

      this.configureHandlers(var5);
      return var5;
   }

   private URL getWsdlURL(Class var1, ServletContext var2) throws Exception {
      if (this.serviceReferenceDescriptionBean != null && this.serviceReferenceDescriptionBean.getWsdlUrl() != null && var2 != null) {
         return getResource(this.serviceReferenceDescriptionBean.getWsdlUrl(), var2);
      } else {
         Container var3 = ContainerResolver.getInstance().getContainer();
         WLSContainer var4 = null;
         URL var5 = null;

         try {
            if (var2 != null && !var2.equals(var3.getSPI(ServletContext.class))) {
               var4 = new WLSContainer(var2, (DeployInfo)null);
               var4.setCurrent();
            }

            if (this.serviceReferenceDescriptionBean != null && this.serviceReferenceDescriptionBean.getWsdlUrl() != null) {
               var5 = WLSProvider.locateWsdl(var1, this.serviceReferenceDescriptionBean.getWsdlUrl());
            }

            if (var5 == null) {
               var5 = WLSProvider.locateWsdl(var1, this.serviceRefBean.getWsdlFile());
            }
         } finally {
            if (var4 != null) {
               var4.resetCurrent();
            }

         }

         if (var5 == null) {
            if (Service.class.equals(var1)) {
               throw new IllegalArgumentException("A wsdlLocation must be specified if using " + Service.class.getName());
            }

            WebServiceClient var6 = (WebServiceClient)var1.getAnnotation(WebServiceClient.class);
            if (var6 == null) {
               throw new IllegalArgumentException("Service class " + var1.getName() + " does not have required WebServiceClient annotation.");
            }

            if (StringUtil.isEmpty(var6.wsdlLocation())) {
               throw new IllegalArgumentException("wsdlLocation not specidied on class " + var1.getName() + " WebServiceClient annotation.");
            }
         }

         return var5;
      }
   }

   private static URL getResource(String var0, ServletContext var1) throws Exception {
      URL var2 = null;
      if (var0 != null && var0.startsWith("/")) {
         var2 = var1.getResource(var0);
      }

      if (var2 == null) {
         var2 = Thread.currentThread().getContextClassLoader().getResource(var0);
      }

      if (var2 == null) {
         var2 = new URL(var0);
      }

      return var2;
   }

   private QName getServiceQName(Class var1, URL var2) throws WsdlException {
      QName var3 = this.serviceRefBean.getServiceQname();
      if (var3 == null) {
         if (var2 == null) {
            String var6 = this.serviceReferenceDescriptionBean != null ? this.serviceReferenceDescriptionBean.getWsdlUrl() : this.serviceRefBean.getWsdlFile();
            if (var6 == null) {
               WebServiceClient var5 = (WebServiceClient)var1.getAnnotation(WebServiceClient.class);
               if (var5 != null) {
                  var6 = var5.wsdlLocation();
               }
            }

            throw new IllegalArgumentException("WSDL not found at " + var6);
         }

         Set var4 = this.getServiceQNameByRI(var2, var1);
         if (var4 != null) {
            if (var4.size() == 1) {
               var3 = (QName)var4.iterator().next();
            } else if (var4.size() > 1) {
               throw new IllegalArgumentException("A serviceQName must be specified when referencing " + Service.class.getName() + ", more than one service was found in the wsdl.");
            }
         }

         if (var3 == null) {
            throw new IllegalArgumentException("A serviceQName must be specified when referencing " + Service.class.getName() + ", no service was found in the wsdl.");
         }
      }

      return var3;
   }

   public void unbindServiceRef(Context var1) throws NamingException {
      var1.unbind(this.serviceRefBean.getServiceRefName());
   }

   public void bindServiceRef(Context var1, Context var2, String var3) throws NamingException, ServiceRefProcessorException {
      var2.bind(this.serviceRefBean.getServiceRefName(), this.targetRef);
      if (verbose) {
         Verbose.log((Object)("Bound JAXWS service-ref under name " + this.serviceRefBean.getServiceRefName()));
      }

   }

   private Set<QName> getServiceQNameByRI(URL var1, Class var2) throws WsdlException {
      try {
         EntityResolver var3 = XCatalogUtil.createRuntimeCatalogResolver(2);
         Container var4 = ContainerResolver.getInstance().getContainer();
         WSDLModelImpl var5 = RuntimeWSDLParser.parse(var1, new StreamSource(var1.toExternalForm()), var3, false, var4, var2, (WSDLParserExtension[])ServiceFinder.find(WSDLParserExtension.class).toArray());
         return var5.getServices().keySet();
      } catch (Exception var6) {
         throw new WsdlException("Failed to parse wsdl from " + var1, var6);
      }
   }

   static {
      System.setProperty(XMLStreamWriterFactory.class.getName() + ".woodstox", "true");
      System.setProperty(XMLStreamReaderFactory.class.getName() + ".woodstox", "true");
      WLSContainer.setContainerResolver();
      verbose = Verbose.isVerbose(ServiceRefProcessorImpl.class);
   }
}
