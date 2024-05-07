package weblogic.wsee.jaxws.spi;

import com.sun.xml.ws.api.WSService;
import com.sun.xml.ws.binding.WebServiceFeatureList;
import com.sun.xml.ws.client.WSServiceDelegate;
import com.sun.xml.ws.model.wsdl.WSDLServiceImpl;
import com.sun.xml.ws.util.JAXWSUtils;
import com.sun.xml.ws.util.ServiceFinder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.soap.SOAPBinding;
import org.xml.sax.EntityResolver;
import weblogic.descriptor.DescriptorBean;
import weblogic.j2ee.descriptor.PortComponentRefBean;
import weblogic.j2ee.descriptor.ServiceRefBean;
import weblogic.j2ee.descriptor.SessionBeanBean;
import weblogic.j2ee.descriptor.wl.PortInfoBean;
import weblogic.j2ee.descriptor.wl.PropertyNamevalueBean;
import weblogic.j2ee.descriptor.wl.ServiceReferenceDescriptionBean;
import weblogic.kernel.KernelStatus;
import weblogic.management.ManagementException;
import weblogic.wsee.jaxws.owsm.WsdlDefinitionFeature;
import weblogic.wsee.jaxws.persistence.StandardPersistentPropertyRegister;
import weblogic.wsee.jaxws.tubeline.ServiceInitialization;
import weblogic.wsee.jaxws.tubeline.TubelineDeploymentListener;
import weblogic.wsee.jaxws.tubeline.TubelineDeploymentListenerRepository;
import weblogic.wsee.monitoring.WseeClientConfigurationRuntimeMBeanImpl;
import weblogic.wsee.monitoring.WseeRuntimeMBeanManager;
import weblogic.wsee.runtime.owsm.PolicySubjectUtil;
import weblogic.wsee.tools.xcatalog.XCatalogUtil;
import weblogic.wsee.util.StringUtil;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.wstx.wsat.TransactionalFeature;
import weblogic.wsee.wstx.wsat.config.DDHelper;

public abstract class WLSServiceDelegate extends WSServiceDelegate {
   private static final boolean verbose = Verbose.isVerbose(WLSServiceDelegate.class);
   public static ThreadLocal<ServiceRefBean> serviceRefLocal = new ThreadLocal();
   public static ThreadLocal<ServiceReferenceDescriptionBean> serviceReferenceDescriptionLocal = new ThreadLocal();
   public static ThreadLocal<WebServiceFeatureList> featuresLocal = new ThreadLocal();
   public static final String SPECIAL_PORTNAME = ":";
   private ServiceRefBean serviceRef = null;
   private ServiceReferenceDescriptionBean serviceReferenceDescription = null;
   private Map<String, PortComponentRefBean> portMap = new HashMap();
   private WseeClientConfigurationRuntimeMBeanImpl serviceRefMBean = null;
   private Set<String> portConfigurationSet = new HashSet();
   private final TubelineDeploymentListenerRepository tdlr;
   protected WebServiceFeatureList features;
   private WLSStandardPersistentPropertyRegister sppr = new WLSStandardPersistentPropertyRegister();
   private static final Logger logger = Logger.getLogger(WLSServiceDelegate.class.getName());

   public WLSServiceDelegate(URL var1, QName var2, Class<? extends Service> var3) {
      super(var1, var2, var3);
      this.init();
      this.tdlr = new WLSTubelineDeploymentListenerRepository(this, var3);
      this.addWsdlDefinitionFeature(var1, (Source)null, var2, var3);
   }

   public WLSServiceDelegate(Source var1, WSDLServiceImpl var2, QName var3, Class<? extends Service> var4) {
      super(var1, var2, var3, var4);
      this.init();
      this.tdlr = new WLSTubelineDeploymentListenerRepository(this, var4);
      this.addWsdlDefinitionFeature((URL)null, var1, var3, var4);
   }

   public WLSServiceDelegate(Object var1, QName var2, Class<? extends Service> var3) {
      super(convertWsdlDefinitionToSource(var1), var2, var3);
      this.init();
      this.tdlr = new WLSTubelineDeploymentListenerRepository(this, var3);
      this.addWsdlDefinitionFeature(var1, var2, var3);
   }

   private void init() {
      WebServiceFeatureList var1 = (WebServiceFeatureList)featuresLocal.get();
      this.features = var1 != null ? new WebServiceFeatureList(var1.toArray()) : new WebServiceFeatureList();
      this.serviceRef = (ServiceRefBean)serviceRefLocal.get();
      if (this.serviceRef != null) {
         this.portMap = this.processComponentLinks(this.serviceRef.getPortComponentRefs());
      }

      this.serviceReferenceDescription = (ServiceReferenceDescriptionBean)serviceReferenceDescriptionLocal.get();
      this.setupClientConfigurationRuntimeMBean();
   }

   private static Source convertWsdlDefinitionToSource(Object var0) {
      ByteArrayOutputStream var1 = new ByteArrayOutputStream();

      try {
         WsdlDefinitionFeature.writeWSDL(var0, var1);
      } catch (IOException var3) {
         return null;
      }

      StreamSource var2 = new StreamSource(new ByteArrayInputStream(var1.toByteArray()), WsdlDefinitionFeature.getDocumentBaseUriFromWSDL(var0));
      return var2;
   }

   private void addWsdlDefinitionFeature(Object var1, QName var2, Class<? extends Service> var3) {
      WsdlDefinitionFeature var4 = WsdlDefinitionFeature.create(var1, var3, this.createCatalogResolver());
      if (var4 != null) {
         if (this.features == null) {
            this.features = new WebServiceFeatureList();
         }

         this.features.add(var4);
      }

   }

   private void addWsdlDefinitionFeature(URL var1, Source var2, QName var3, final Class<? extends Service> var4) {
      Object var5 = var2;
      if (var2 == null && var1 != null) {
         var5 = new StreamSource(var1.toExternalForm());
      }

      if (var5 == null && var4 != Service.class) {
         WebServiceClient var6 = (WebServiceClient)AccessController.doPrivileged(new PrivilegedAction<WebServiceClient>() {
            public WebServiceClient run() {
               return (WebServiceClient)var4.getAnnotation(WebServiceClient.class);
            }
         });
         String var7 = var6.wsdlLocation();
         var7 = JAXWSUtils.absolutize(JAXWSUtils.getFileOrURLName(var7));
         var5 = new StreamSource(var7);
      }

      if (var5 != null) {
         try {
            URL var9 = ((Source)var5).getSystemId() == null ? null : JAXWSUtils.getEncodedURL(((Source)var5).getSystemId());
            WsdlDefinitionFeature var10 = WsdlDefinitionFeature.create(var9, (Source)var5, var4, this.createCatalogResolver());
            if (var10 != null) {
               if (this.features == null) {
                  this.features = new WebServiceFeatureList();
               }

               this.features.add(var10);
            }
         } catch (Exception var8) {
            logger.severe("Failed to create WsdlDefinitionFeature for wsdl location: " + ((Source)var5).getSystemId() + ", error: " + var8.getClass().getName() + ", message: " + var8.getMessage());
         }
      }

   }

   protected EntityResolver createCatalogResolver() {
      return staticCreateCatalogResolver();
   }

   protected static EntityResolver staticCreateCatalogResolver() {
      return XCatalogUtil.createRuntimeCatalogResolver(2);
   }

   public ServiceReferenceDescriptionBean getServiceReferenceDescription() {
      return this.serviceReferenceDescription;
   }

   private Map<String, PortComponentRefBean> processComponentLinks(PortComponentRefBean[] var1) {
      HashMap var2 = new HashMap();
      if (var1 != null) {
         PortComponentRefBean[] var3 = var1;
         int var4 = var1.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            PortComponentRefBean var6 = var3[var5];
            String var7 = var6.getServiceEndpointInterface();
            if (!StringUtil.isEmpty(var7)) {
               var2.put(var7, var6);
            }
         }
      }

      return var2;
   }

   protected <S> void updatePort(QName var1, Object var2, Class<S> var3) {
      if (var2 instanceof BindingProvider) {
         if (this.serviceRef != null) {
            PortComponentRefBean var4 = (PortComponentRefBean)this.portMap.get(var3.getName());
            if (var4 != null && var4.isEnableMtom() && var2 instanceof BindingProvider) {
               BindingProvider var5 = (BindingProvider)var2;
               Binding var6 = var5.getBinding();
               if (var6 instanceof SOAPBinding) {
                  ((SOAPBinding)var6).setMTOMEnabled(true);
               }
            }
         }

         if (this.serviceReferenceDescription != null) {
            BindingProvider var14 = (BindingProvider)var2;
            Map var15 = var14.getRequestContext();
            PortInfoBean[] var16 = this.serviceReferenceDescription.getPortInfos();
            int var7 = var16.length;

            for(int var8 = 0; var8 < var7; ++var8) {
               PortInfoBean var9 = var16[var8];
               PropertyNamevalueBean[] var10 = var9.getStubProperties();
               int var11 = var10.length;

               for(int var12 = 0; var12 < var11; ++var12) {
                  PropertyNamevalueBean var13 = var10[var12];
                  if (var1 == null || var1.getLocalPart().equals(var9.getPortName())) {
                     var15.put(var13.getName(), var13.getValue());
                  }
               }
            }
         }

      }
   }

   protected String getServiceReferenceName() {
      if (this.serviceRefMBean != null) {
         String var1 = this.serviceRefMBean.getServiceReferenceName();
         DescriptorBean var2 = ((DescriptorBean)this.serviceRef).getParentBean();
         if (var2 instanceof SessionBeanBean) {
            var1 = ((SessionBeanBean)var2).getEjbName() + "/" + var1;
         }

         return var1;
      } else {
         return this.serviceRef != null ? this.serviceRef.getServiceRefName() : null;
      }
   }

   protected String getServiceRefUniqueKey() {
      String var1 = this.getServiceReferenceName();
      if (var1 != null) {
         StringBuffer var2 = new StringBuffer();
         var2.append(var1);
         if (this.serviceRef != null) {
            String var3 = this.serviceRef.getMappedName();
            if (var3 != null && var3.length() > 0) {
               var2.append(':');
               var2.append(var3);
            }

            String var4 = this.serviceRef.getWsdlFile();
            if (var4 != null && var4.length() > 0) {
               var2.append(':');
               var2.append(var4);
            }
         }

         return var2.toString();
      } else {
         return null;
      }
   }

   protected void addFeatures(QName var1, WebServiceFeatureList var2) {
      if (this.serviceReferenceDescription != null) {
         PortInfoBean[] var3 = this.serviceReferenceDescription.getPortInfos();
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            PortInfoBean var6 = var3[var5];
            if (var1 != null && var1.getLocalPart().equals(var6.getPortName()) && this.addTransactionalFeature(var2, var6)) {
               return;
            }
         }

         PortInfoBean var7 = this.serviceReferenceDescription.lookupPortInfo(":");
         this.addTransactionalFeature(var2, var7);
      }

   }

   private boolean addTransactionalFeature(WebServiceFeatureList var1, PortInfoBean var2) {
      TransactionalFeature var3 = DDHelper.buildFeatureFromServiceRefDD(var2);
      if (var3 != null) {
         var1.add(var3);
         return true;
      } else {
         return false;
      }
   }

   private void setupClientConfigurationRuntimeMBean() {
      if (this.serviceRef != null && KernelStatus.isServer()) {
         try {
            this.serviceRefMBean = WseeRuntimeMBeanManager.createClientConfigurationMBean(this.serviceRef.getServiceRefName());
            this.serviceRefMBean.register();
            String var1 = this.getServiceReferenceName();
            Iterator var2 = super.getPorts();

            while(var2.hasNext()) {
               QName var3 = (QName)var2.next();
               String var4 = PolicySubjectUtil.formatReferencedPortResourcePattern(var3.getLocalPart(), var1);
               if (!this.portConfigurationSet.contains(var4)) {
                  WseeRuntimeMBeanManager.createPortConfigurationMBean(this.getWsdlService().get(var3), var4, this.serviceRefMBean);
                  this.portConfigurationSet.add(var4);
               }
            }
         } catch (ManagementException var6) {
            if (verbose) {
               Verbose.logException(var6);
            }
         }
      }

   }

   public <T> T getSPI(Class<T> var1) {
      if (var1 == TubelineDeploymentListenerRepository.class) {
         return var1.cast(this.tdlr);
      } else if (var1 == StandardPersistentPropertyRegister.class) {
         return var1.cast(this.sppr);
      } else {
         return var1 == WseeClientConfigurationRuntimeMBeanImpl.class ? var1.cast(this.serviceRefMBean) : null;
      }
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

   private static class WLSTubelineDeploymentListenerRepository implements TubelineDeploymentListenerRepository {
      private final Collection<TubelineDeploymentListener> listeners = new ArrayList();

      public WLSTubelineDeploymentListenerRepository(WSService var1, Class<? extends Service> var2) {
         TubelineDeploymentListener var4;
         for(Iterator var3 = ServiceFinder.find(TubelineDeploymentListener.class, var1.getClass().getClassLoader()).iterator(); var3.hasNext(); this.listeners.add(var4)) {
            var4 = (TubelineDeploymentListener)var3.next();
            if (var4 instanceof ServiceInitialization) {
               ((ServiceInitialization)var4).init(var1, var2);
            }
         }

      }

      public Collection<TubelineDeploymentListener> getListeners() {
         return this.listeners;
      }
   }
}
