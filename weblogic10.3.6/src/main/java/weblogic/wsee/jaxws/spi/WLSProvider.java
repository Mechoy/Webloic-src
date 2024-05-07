package weblogic.wsee.jaxws.spi;

import com.sun.istack.NotNull;
import com.sun.xml.ws.Closeable;
import com.sun.xml.ws.api.BindingID;
import com.sun.xml.ws.api.FeatureConstructor;
import com.sun.xml.ws.api.addressing.WSEndpointReference;
import com.sun.xml.ws.api.server.AsyncProvider;
import com.sun.xml.ws.api.server.AsyncProviderCallback;
import com.sun.xml.ws.api.server.Container;
import com.sun.xml.ws.api.server.ContainerResolver;
import com.sun.xml.ws.api.streaming.XMLStreamReaderFactory;
import com.sun.xml.ws.api.streaming.XMLStreamWriterFactory;
import com.sun.xml.ws.binding.WebServiceFeatureList;
import com.sun.xml.ws.client.PortInfo;
import com.sun.xml.ws.client.SEIPortInfo;
import com.sun.xml.ws.model.AbstractSEIModelImpl;
import com.sun.xml.ws.model.wsdl.WSDLPortImpl;
import com.sun.xml.ws.model.wsdl.WSDLServiceImpl;
import com.sun.xml.ws.resources.ModelerMessages;
import com.sun.xml.ws.spi.ProviderImpl;
import com.sun.xml.ws.transport.http.server.EndpointImpl;
import java.lang.annotation.Annotation;
import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.servlet.ServletContext;
import javax.xml.bind.JAXBContext;
import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Endpoint;
import javax.xml.ws.Provider;
import javax.xml.ws.Service;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.soap.MTOM;
import javax.xml.ws.spi.WebServiceFeatureAnnotation;
import weblogic.j2ee.descriptor.wl.PortInfoBean;
import weblogic.jws.Policy.Direction;
import weblogic.jws.jaxws.AggregatePolicyFeature;
import weblogic.jws.jaxws.AggregateWebServiceFeature;
import weblogic.jws.jaxws.AggregateWebServiceFeatureAnnotation;
import weblogic.jws.jaxws.PolicyFeature;
import weblogic.jws.jaxws.client.ClientIdentityFeature;
import weblogic.kernel.KernelStatus;
import weblogic.servlet.internal.WebAppServletContext;
import weblogic.timers.NakedTimerListener;
import weblogic.timers.Timer;
import weblogic.timers.TimerManager;
import weblogic.timers.TimerManagerFactory;
import weblogic.utils.collections.ConcurrentHashMap;
import weblogic.utils.collections.WeakConcurrentHashMap;
import weblogic.work.WorkManagerFactory;
import weblogic.wsee.jaxws.WLSContainer;
import weblogic.wsee.jaxws.framework.ConfigUtil;
import weblogic.wsee.jaxws.owsm.WsdlDefinitionFeature;
import weblogic.wsee.jaxws.persistence.ClientInstancePoolFeature;
import weblogic.wsee.jaxws.persistence.ConversationalClientInstanceFeature;
import weblogic.wsee.jaxws.proxy.ClientProxyFeature;
import weblogic.wsee.persistence.StoreException;
import weblogic.wsee.util.ClassLoaderUtil;
import weblogic.wsee.util.Pair;

public class WLSProvider extends ProviderImpl {
   private static final int WSEE_REFERENCE_CLOSE_INTERVAL = 10000;
   private static final String CLOSE_TIMER_MANAGER_NAME = "weblogic.wsee.jaxws.WLSProvider.CloseTimer";
   private static final Map<Pair<Class, URL>, Map<Pair<QName, QName>, Reference<AbstractSEIModelImpl>>> runtimeMap;
   private static final WLSProvider instance;

   public static WLSProvider getInstance() {
      return instance;
   }

   public ServiceDelegate createServiceDelegate(URL var1, QName var2, Class var3) {
      return this.createServiceDelegate(var1, var2, var3, (WebServiceFeature[])null);
   }

   public ServiceDelegate createServiceDelegate(URL var1, QName var2, Class var3, WebServiceFeature... var4) {
      Object var5 = null;
      if (WsdlDefinitionFeature.required()) {
         try {
            var5 = WsdlDefinitionFeature.readDefinition(var1, new StreamSource(var1.toExternalForm()), var3, WLSServiceDelegate.staticCreateCatalogResolver());
         } catch (Exception var8) {
            Logger var7 = Logger.getLogger(var3.getName());
            var7.warning("Could not read WSDL Definition from URL wsdlDocumentLocation: " + var8.getMessage());
         }
      }

      return var5 == null ? new ServiceDelegate(var1, var2, var3, var4) : new ServiceDelegate(var5, var2, var3, var4);
   }

   public URL locateWsdl(Class<? extends Service> var1) {
      return locateWsdl(var1, (String)null);
   }

   public static URL locateWsdl(Class<? extends Service> var0, String var1) {
      URL var2 = null;
      String var3 = var1;
      if (var1 == null || var1.length() == 0) {
         WebServiceClient var4 = (WebServiceClient)var0.getAnnotation(WebServiceClient.class);
         if (var4 == null) {
            throw new WebServiceException("Service class " + var0.getName() + " does not have required WebServiceClient annotation.");
         }

         var3 = var4.wsdlLocation();
      }

      if (var3 != null && var3.length() > 0) {
         String var14 = var3.startsWith("/") ? null : "/" + var3;
         String var5 = var3.toUpperCase(Locale.ENGLISH).startsWith("/WEB-INF") ? null : (var14 != null ? "/WEB-INF" + var14 : "/WEB-INF/" + var3);
         String var6 = var3.toUpperCase(Locale.ENGLISH).startsWith("/META-INF") ? null : (var14 != null ? "/META-INF" + var14 : "/META-INF/" + var3);
         if (var3 != null && var3.length() > 0) {
            Logger var8;
            try {
               URI var7 = new URI(var3.replace('\\', '/'));
               if (var7.isAbsolute()) {
                  return var7.toURL();
               }

               ClassLoader var15 = Thread.currentThread().getContextClassLoader();
               if (var15 != null) {
                  var2 = var15.getResource(var3);
                  if (var2 == null && var14 != null) {
                     var2 = var15.getResource(var14);
                  }

                  if (var2 == null && var6 != null) {
                     var2 = var15.getResource(var6);
                  }
               }

               if (var2 == null) {
                  var2 = var0.getResource(var3);
                  if (var2 == null && var14 != null) {
                     var2 = var0.getResource(var14);
                  }

                  if (var2 == null && var6 != null) {
                     var2 = var0.getResource(var6);
                  }
               }

               if (var2 == null) {
                  Container var9 = ContainerResolver.getInstance().getContainer();
                  if (var9 != null) {
                     ServletContext var10 = (ServletContext)var9.getSPI(ServletContext.class);
                     if (var10 != null) {
                        if (var14 == null) {
                           var2 = var10.getResource(var3);
                        }

                        if (var2 == null && var14 != null) {
                           var2 = var10.getResource(var14);
                        }

                        if (var2 == null && var5 != null) {
                           var2 = var10.getResource(var5);
                        }

                        if (var2 == null && var6 != null) {
                           var2 = var10.getResource(var6);
                        }

                        if (var2 == null && var10 instanceof WebAppServletContext) {
                           ClassLoader var11 = ((WebAppServletContext)var10).getServletClassLoader();
                           var2 = var11.getResource(var3);
                           if (var2 == null && var14 != null) {
                              var2 = var11.getResource(var14);
                           }

                           if (var2 == null && var5 != null) {
                              var2 = var11.getResource(var5);
                           }

                           if (var2 == null && var6 != null) {
                              var2 = var11.getResource(var6);
                           }
                        }
                     }
                  }
               }
            } catch (URISyntaxException var12) {
               var8 = Logger.getLogger(var0.getName());
               var8.warning("Failed to create URL for the wsdl Location: '" + var3 + "', retrying as a local file");
               var8.warning(var12.getMessage());
            } catch (MalformedURLException var13) {
               var8 = Logger.getLogger(var0.getName());
               var8.warning("Failed to create URL for the wsdl Location: '" + var3 + "', retrying as a local file");
               var8.warning(var13.getMessage());
            }
         }
      }

      return var2;
   }

   public ServiceDelegate createServiceDelegate(Source var1, QName var2, Class var3) {
      return this.createServiceDelegate((Source)var1, (WSDLServiceImpl)null, (QName)var2, (Class)var3);
   }

   public ServiceDelegate createServiceDelegate(Source var1, WSDLServiceImpl var2, QName var3, Class var4) {
      return new ServiceDelegate(var1, var2, var3, var4);
   }

   public ServiceDelegate createServiceDelegate(Object var1, QName var2, Class var3) {
      return new ServiceDelegate(var1, var2, var3, new WebServiceFeature[0]);
   }

   public Endpoint createAndPublishEndpoint(String var1, Object var2) {
      if (!KernelStatus.isServer()) {
         return super.createAndPublishEndpoint(var1, var2);
      } else {
         WLSEndpoint var3 = new WLSEndpoint(this, BindingID.parse(var2.getClass()), var2, WorkManagerExecutor.getExecutor());
         var3.publish(var1);
         return var3;
      }
   }

   public Endpoint createEndpoint(String var1, Object var2) {
      return (Endpoint)(!KernelStatus.isServer() ? super.createEndpoint(var1, var2) : new WLSEndpoint(this, var1 != null ? BindingID.parse(var1) : BindingID.parse(var2.getClass()), var2, WorkManagerExecutor.getExecutor()));
   }

   Endpoint createEndpointInternal(@NotNull BindingID var1, @NotNull Object var2) {
      return new EndpointImpl(var1, var2);
   }

   public static void parseAnnotations(WebServiceFeatureList var0, AnnotatedElement var1, boolean var2) {
      Annotation[] var3 = var1.getAnnotations();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Annotation var6 = var3[var5];
         boolean var7 = isMtomAnnotation(var6);
         if (var7 || !var2 || var6.annotationType().isAnnotationPresent(AggregateWebServiceFeatureAnnotation.class)) {
            Object var8 = null;
            if (var7) {
               var8 = new AggregatePolicyFeature();
               HashMap var9 = new HashMap();
               var9.put(var1, new PolicyFeature("policy:Mtom.xml", Direction.both));
               ((AggregatePolicyFeature)var8).combine(var9);
            } else {
               var8 = getWebServiceFeatureBean(var6);
            }

            if (var8 != null) {
               addFeature(var0, checkForAggregate((WebServiceFeature)var8, var6, var1));
            }
         }
      }

      if (var1 instanceof Class) {
         Class var21 = (Class)var1;
         int var34;
         int var36;
         if (var21.getAnnotation(WebService.class) != null) {
            WebService var22 = (WebService)var21.getAnnotation(WebService.class);
            boolean var24 = false;
            Class var25 = var21;
            if (!"".equals(var22.endpointInterface())) {
               String var29 = var22.endpointInterface();
               ClassLoader var33 = Thread.currentThread().getContextClassLoader();
               if (var33 == null) {
                  var33 = WLSProvider.class.getClassLoader();
               }

               try {
                  var21 = ClassLoaderUtil.loadClass(var25.getClassLoader(), var33, var29);
                  var24 = true;
               } catch (Exception var20) {
                  throw new RuntimeException(var20.toString(), var20);
               }
            }

            Method[] var30 = var21.getMethods();
            var36 = var30.length;

            for(var34 = 0; var34 < var36; ++var34) {
               Method var10 = var30[var34];
               if (var10.getDeclaringClass() != Object.class && Modifier.isPublic(var10.getModifiers())) {
                  WebMethod var11 = (WebMethod)var10.getAnnotation(WebMethod.class);
                  boolean var12 = var11 != null && var11.exclude();
                  if (!var12) {
                     if (var24) {
                        try {
                           var10 = var25.getMethod(var10.getName(), var10.getParameterTypes());
                        } catch (NoSuchMethodException var19) {
                           throw new WebServiceException(var25.getName() + " doesn't implement " + var10);
                        }
                     }

                     Annotation[] var13 = var10.getAnnotations();
                     int var14 = var13.length;

                     for(int var15 = 0; var15 < var14; ++var15) {
                        Annotation var16 = var13[var15];
                        if (var16.annotationType().isAnnotationPresent(AggregateWebServiceFeatureAnnotation.class)) {
                           addFeature(var0, checkForAggregate(getWebServiceFeatureBean(var16), var16, var10));
                        }
                     }
                  }
               }
            }
         } else {
            Type[] var23 = var21.getGenericInterfaces();
            Type[] var26 = null;
            if (var23 != null) {
               Type[] var27 = var23;
               int var31 = var23.length;

               for(var36 = 0; var36 < var31; ++var36) {
                  Type var35 = var27[var36];
                  if (var35 instanceof ParameterizedType) {
                     ParameterizedType var37 = (ParameterizedType)var35;
                     if (Provider.class.equals(var37.getRawType()) || AsyncProvider.class.equals(var37.getRawType())) {
                        var26 = var37.getActualTypeArguments();
                        break;
                     }
                  }
               }
            }

            if (var26 != null && var26.length == 1) {
               Method var28 = null;

               try {
                  var28 = var21.getMethod("invoke", (Class)var26[0]);
               } catch (NoSuchMethodException var18) {
               }

               if (var28 == null) {
                  try {
                     var28 = var21.getMethod("invoke", (Class)var26[0], AsyncProviderCallback.class, WebServiceContext.class);
                  } catch (NoSuchMethodException var17) {
                  }
               }

               if (var28 != null) {
                  Annotation[] var32 = var28.getAnnotations();
                  var36 = var32.length;

                  for(var34 = 0; var34 < var36; ++var34) {
                     Annotation var38 = var32[var34];
                     if (var38.annotationType().isAnnotationPresent(AggregateWebServiceFeatureAnnotation.class)) {
                        addFeature(var0, checkForAggregate(getWebServiceFeatureBean(var38), var38, var21));
                     }
                  }
               }
            }
         }
      }

   }

   private static boolean isMtomAnnotation(Annotation var0) {
      return var0.annotationType().isAssignableFrom(MTOM.class);
   }

   private static void addFeature(WebServiceFeatureList var0, WebServiceFeature var1) {
      if (var1 instanceof AggregateWebServiceFeature) {
         AggregateWebServiceFeature var2 = (AggregateWebServiceFeature)var0.get(var1.getClass());
         if (var2 != null) {
            var2.combine(((AggregateWebServiceFeature)var1).getMap());
            return;
         }
      }

      var0.add(var1);
   }

   private static WebServiceFeature getWebServiceFeatureBean(Annotation var0) {
      AggregateWebServiceFeatureAnnotation var1 = (AggregateWebServiceFeatureAnnotation)var0.annotationType().getAnnotation(AggregateWebServiceFeatureAnnotation.class);
      Class var2 = null;
      if (var1 != null) {
         var2 = var1.featureBean();
      }

      if (var2 == null) {
         WebServiceFeatureAnnotation var3 = (WebServiceFeatureAnnotation)var0.annotationType().getAnnotation(WebServiceFeatureAnnotation.class);
         if (var3 != null) {
            var2 = var3.bean();
         }
      }

      if (var2 == null) {
         return null;
      } else {
         Constructor var4 = null;
         String[] var5 = null;
         Constructor[] var6 = var2.getConstructors();
         int var7 = var6.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            Constructor var9 = var6[var8];
            FeatureConstructor var10 = (FeatureConstructor)var9.getAnnotation(FeatureConstructor.class);
            if (var10 != null) {
               if (var4 != null) {
                  throw new WebServiceException(ModelerMessages.RUNTIME_MODELER_WSFEATURE_MORETHANONE_FTRCONSTRUCTOR(var0, var2));
               }

               var4 = var9;
               var5 = var10.value();
            }
         }

         if (var4 == null) {
            throw new WebServiceException(ModelerMessages.RUNTIME_MODELER_WSFEATURE_NO_FTRCONSTRUCTOR(var0, var2));
         } else if (var4.getParameterTypes().length != var5.length) {
            throw new WebServiceException(ModelerMessages.RUNTIME_MODELER_WSFEATURE_ILLEGAL_FTRCONSTRUCTOR(var0, var2));
         } else {
            try {
               Object[] var13 = new Object[var5.length];

               for(var7 = 0; var7 < var5.length; ++var7) {
                  Method var14 = var0.annotationType().getDeclaredMethod(var5[var7]);
                  var13[var7] = var14.invoke(var0);
               }

               WebServiceFeature var12 = (WebServiceFeature)var4.newInstance(var13);
               return var12;
            } catch (Exception var11) {
               throw new WebServiceException(var11);
            }
         }
      }
   }

   private static WebServiceFeature checkForAggregate(WebServiceFeature var0, Annotation var1, AnnotatedElement var2) {
      AggregateWebServiceFeatureAnnotation var3 = (AggregateWebServiceFeatureAnnotation)var1.annotationType().getAnnotation(AggregateWebServiceFeatureAnnotation.class);
      if (var3 != null) {
         Class var5 = var3.aggregateBean();

         AggregateWebServiceFeature var4;
         try {
            var4 = (AggregateWebServiceFeature)var5.newInstance();
         } catch (Exception var7) {
            throw new WebServiceException(var7);
         }

         var4.getMap().put(var2, var0);
         return var4;
      } else {
         return var0;
      }
   }

   static {
      System.setProperty(XMLStreamWriterFactory.class.getName() + ".woodstox", "true");
      System.setProperty(XMLStreamReaderFactory.class.getName() + ".woodstox", "true");
      WLSContainer.setContainerResolver();
      runtimeMap = new WeakConcurrentHashMap();
      instance = new WLSProvider();
   }

   private class ServiceDelegate extends WLSServiceDelegate {
      private ClientProxyFeature proxyFeature;
      private ReferenceCleaner cleaner;

      public ServiceDelegate(URL var2, QName var3, Class<? extends Service> var4) {
         this((URL)var2, (QName)var3, (Class)var4, (WebServiceFeature[])((WebServiceFeature[])null));
      }

      public ServiceDelegate(URL var2, QName var3, Class<? extends Service> var4, WebServiceFeature... var5) {
         super(var2, var3, var4);
         this.proxyFeature = null;
         this.cleaner = new ReferenceCleaner(this);
         this.addFeatures(var5);
         this.proxyFeature = (ClientProxyFeature)this.features.get(ClientProxyFeature.class);
         if (KernelStatus.isServer()) {
            this.setExecutor(WorkManagerExecutor.getExecutor());
         }

      }

      public ServiceDelegate(Source var2, WSDLServiceImpl var3, QName var4, Class<? extends Service> var5) {
         this(var2, var3, var4, var5, (WebServiceFeature[])null);
      }

      public ServiceDelegate(Source var2, WSDLServiceImpl var3, QName var4, Class<? extends Service> var5, WebServiceFeature... var6) {
         super(var2, var3, var4, var5);
         this.proxyFeature = null;
         this.cleaner = new ReferenceCleaner(this);
         this.addFeatures(var6);
         this.proxyFeature = (ClientProxyFeature)this.features.get(ClientProxyFeature.class);
         if (KernelStatus.isServer()) {
            this.setExecutor(WorkManagerExecutor.getExecutor());
         }

      }

      public ServiceDelegate(Object var2, QName var3, Class<? extends Service> var4, WebServiceFeature... var5) {
         super(var2, var3, var4);
         this.proxyFeature = null;
         this.cleaner = new ReferenceCleaner(this);
         this.addFeatures(var5);
         this.proxyFeature = (ClientProxyFeature)this.features.get(ClientProxyFeature.class);
         if (KernelStatus.isServer()) {
            this.setExecutor(WorkManagerExecutor.getExecutor());
         }

      }

      private void addFeatures(WebServiceFeature... var1) {
         if (var1 != null) {
            WebServiceFeature[] var2 = var1;
            int var3 = var1.length;

            for(int var4 = 0; var4 < var3; ++var4) {
               WebServiceFeature var5 = var2[var4];
               if (this.features.contains(var5.getClass())) {
                  throw new WebServiceException("Duplicate feature: " + var5.getClass());
               }

               this.features.add(var5);
            }
         }

      }

      public Dispatch<Object> createDispatch(QName var1, WSEndpointReference var2, JAXBContext var3, Service.Mode var4, WebServiceFeatureList var5) {
         return this.createDispatch(var1, var2, Object.class, var3, var4, var5);
      }

      public <T> Dispatch<T> createDispatch(QName var1, WSEndpointReference var2, Class<T> var3, Service.Mode var4, WebServiceFeatureList var5) {
         return this.createDispatch(var1, var2, var3, (JAXBContext)null, var4, var5);
      }

      private <T> Dispatch<T> createDispatch(QName var1, WSEndpointReference var2, Class<T> var3, JAXBContext var4, Service.Mode var5, WebServiceFeatureList var6) {
         ClientIdentityFeature var7 = ClientIdentityRegistry.initClientIdentityFeature(var1, this, var2, var3, this.getServiceRefUniqueKey(), var6);
         this.addFeatures(var1, var6);
         if (this.features != null) {
            var6.addAll(this.features);
         }

         ClientInstance.CreationInfo var8 = new ClientInstance.CreationInfo(var1, var2, var3, var4, var5, var6);
         ClientInstancePool var9 = this.verifyDispatchClientInstancePool(var8);
         ClientInstanceIdentity var10 = ClientIdentityRegistry.generateSimpleClientInstanceIdentity(var7.getClientId());
         ClientInstance var11 = var9.take(var10, var8);
         Dispatch var12 = (Dispatch)var11.createProxyInstance(this.cleaner);
         WeakReference var13 = new WeakReference(var11);
         var12.getRequestContext().put("weblogic.wsee.jaxws.spi.ClientInstanceWeakRef", var13);
         return var12;
      }

      private ClientInstancePool verifyDispatchClientInstancePool(ClientInstance.CreationInfo var1) {
         WebServiceFeatureList var2 = var1.getFeatures();
         ClientIdentityFeature var3 = (ClientIdentityFeature)var2.get(ClientIdentityFeature.class);
         ClientIdentityRegistry.ClientInfo var4 = ClientIdentityRegistry.getClientInfo(var3.getClientId());
         ClientInstancePool var5 = var4 != null ? (ClientInstancePool)var4.getClientInstancePools().get(ClientIdentityRegistry.getPoolKey(var1.getInstanceType(), true)) : null;
         if (var5 == null || !var5.isInitialized()) {
            ClientInstancePoolFeature var6 = (ClientInstancePoolFeature)var2.get(ClientInstancePoolFeature.class);
            DispatchClientInstanceFactory var7 = new DispatchClientInstanceFactory(var1);
            PortInfoBean var8 = null;
            if (var1.getPortName() != null) {
               var8 = ConfigUtil.getPortInfoBeanForPort(this, var1.getPortName().getLocalPart());
            }

            try {
               var5 = ClientIdentityRegistry.initClientIdentity(var3, var6, var8, var7, true, var1.getInstanceType());
            } catch (StoreException var10) {
               throw new IllegalStateException(var10.toString(), var10);
            }
         }

         return var5;
      }

      public <T> ClientInstance<Dispatch<T>> internalCreateDispatch(ClientInstanceIdentity var1, Class var2, ClientInstance.InstanceReleaser<T> var3, ClientInstance.CreationInfo var4) {
         WebServiceFeatureList var5 = var4.getFeatures();
         var5.add(new ClientInstanceIdentityFeature(var1));
         Dispatch var6;
         if (var4.getJaxbContext() != null) {
            var6 = super.createDispatch(var4.getPortName(), var4.getWsepr(), var4.getJaxbContext(), var4.getMode(), var5);
         } else {
            var6 = super.createDispatch(var4.getPortName(), var4.getWsepr(), var2, var4.getMode(), var5);
         }

         ClientProxyFeature var7 = (ClientProxyFeature)var5.get(ClientProxyFeature.class);
         if (var7 != null) {
            var7.attachsPort(var6);
         } else if (this.proxyFeature != null && this.proxyFeature.isUsedInPort()) {
            this.proxyFeature.attachsPort(var6);
         }

         return new ClientInstance(var1, var3, var4, var6);
      }

      public <T> T getPort(QName var1, Class<T> var2, WebServiceFeature... var3) {
         WebServiceFeatureList var4 = new WebServiceFeatureList();
         if (var3 != null) {
            WebServiceFeature[] var5 = var3;
            int var6 = var3.length;

            for(int var7 = 0; var7 < var6; ++var7) {
               WebServiceFeature var8 = var5[var7];
               if (var4.contains(var8.getClass())) {
                  throw new WebServiceException("Duplicate feature: " + var8.getClass());
               }

               var4.add(var8);
            }
         }

         return super.getPort(var1, var2, var3);
      }

      protected <T> T getPort(WSEndpointReference var1, QName var2, Class<T> var3, WebServiceFeatureList var4) {
         ClientIdentityFeature var5 = ClientIdentityRegistry.initClientIdentityFeature(var2, this, var1, var3, this.getServiceRefUniqueKey(), var4);
         this.addFeatures(var2, var4);
         if (this.features != null) {
            var4.addAll(this.features);
         }

         ConversationalClientInstanceFeature var6 = (ConversationalClientInstanceFeature)var4.get(ConversationalClientInstanceFeature.class);
         ClientInstancePoolFeature var7 = (ClientInstancePoolFeature)var4.get(ClientInstancePoolFeature.class);
         if (var7 != null) {
            var7.setClientIdentityFeature(var5);
         }

         ClientInstance.CreationInfo var8 = new ClientInstance.CreationInfo(var2, var1, var3, (JAXBContext)null, (Service.Mode)null, var4);
         ClientInstancePool var9 = this.verifyPortClientInstancePool(var8);
         ClientInstanceIdentity var10;
         if (var6 != null) {
            var10 = new ClientInstanceIdentity(var5.getClientId(), ClientInstanceIdentity.Type.CONVERSATIONAL, var6.getCorrelationId());
         } else {
            var10 = ClientIdentityRegistry.generateSimpleClientInstanceIdentity(var5.getClientId());
         }

         ClientInstance var11 = var9.take(var10, var8);
         Object var12 = var11.createProxyInstance(this.cleaner);
         WeakReference var13 = new WeakReference(var11);
         ((BindingProvider)var12).getRequestContext().put("weblogic.wsee.jaxws.spi.ClientInstanceWeakRef", var13);
         return var12;
      }

      ClientInstancePool verifyPortClientInstancePool(ClientInstance.CreationInfo var1) {
         WebServiceFeatureList var2 = var1.getFeatures();
         ClientIdentityFeature var3 = (ClientIdentityFeature)var2.get(ClientIdentityFeature.class);
         ClientInstancePoolFeature var4 = (ClientInstancePoolFeature)var2.get(ClientInstancePoolFeature.class);
         ClientIdentityRegistry.ClientInfo var5 = ClientIdentityRegistry.getClientInfo(var3.getClientId());
         ClientInstancePool var6 = var5 != null ? (ClientInstancePool)var5.getClientInstancePools().get(ClientIdentityRegistry.getPoolKey(var1.getInstanceType(), true)) : null;
         if (var6 == null || !var6.isInitialized()) {
            PortClientInstanceFactory var7 = new PortClientInstanceFactory(var1);
            PortInfoBean var8 = null;
            if (var1.getPortName() != null) {
               var8 = ConfigUtil.getPortInfoBeanForPort(this, var1.getPortName().getLocalPart());
            }

            try {
               var6 = ClientIdentityRegistry.initClientIdentity(var3, var4, var8, var7, false, var1.getInstanceType());
            } catch (StoreException var10) {
               throw new IllegalStateException(var10.toString(), var10);
            }
         }

         return var6;
      }

      protected <T> ClientInstance<T> internalGetPort(ClientInstanceIdentity var1, ClientInstance.InstanceReleaser var2, ClientInstance.CreationInfo var3) {
         WebServiceFeatureList var4 = var3.getFeatures();
         var4.add(new ClientInstanceIdentityFeature(var1));
         Object var5 = super.getPort(var3.getWsepr(), var3.getPortName(), var3.getInstanceType(), var4);
         this.updatePort(var3.getPortName(), var5, var3.getInstanceType());
         ClientProxyFeature var6 = (ClientProxyFeature)var4.get(ClientProxyFeature.class);
         if (var6 != null) {
            var6.attachsPort(var5);
         } else if (this.proxyFeature != null && this.proxyFeature.isUsedInPort()) {
            this.proxyFeature.attachsPort(var5);
         }

         return new ClientInstance(var1, var2, var3, var5);
      }

      protected AbstractSEIModelImpl buildRuntimeModel(QName var1, QName var2, Class var3, WSDLPortImpl var4, WebServiceFeatureList var5) throws WebServiceException {
         Pair var6 = new Pair(var3, var4.getAddress().getURL());
         Pair var7 = new Pair(var1, var2);
         Object var8 = (Map)WLSProvider.runtimeMap.get(var6);
         if (var8 != null) {
            Reference var9 = (Reference)((Map)var8).get(var7);
            if (var9 != null) {
               AbstractSEIModelImpl var10 = (AbstractSEIModelImpl)var9.get();
               if (var10 != null) {
                  return var10;
               }
            }
         } else {
            PortInfo var11 = this.safeGetPort(var2);
            if (var11 != null && var11 instanceof SEIPortInfo) {
               return ((SEIPortInfo)var11).model;
            }
         }

         if (var3 == null) {
            return null;
         } else {
            AbstractSEIModelImpl var12 = super.buildRuntimeModel(var1, var2, var3, var4, var5);
            if (var8 == null) {
               var8 = new ConcurrentHashMap();
               WLSProvider.runtimeMap.put(var6, var8);
            }

            ((Map)var8).put(var7, new WeakReference(var12));
            return var12;
         }
      }

      private class PortClientInstanceFactory implements ClientInstancePool.InstanceFactory {
         private ClientInstance.CreationInfo _creationInfo;

         public PortClientInstanceFactory(ClientInstance.CreationInfo var2) {
            this._creationInfo = var2;
         }

         public <T2> ClientInstance<T2> createClientInstance(ClientInstanceIdentity var1, Class<T2> var2, ClientInstance.InstanceReleaser<T2> var3) {
            return this.createClientInstance(var1, var2, var3, this._creationInfo);
         }

         public <T2> ClientInstance<T2> createClientInstance(ClientInstanceIdentity var1, Class<T2> var2, ClientInstance.InstanceReleaser<T2> var3, ClientInstance.CreationInfo var4) {
            return ServiceDelegate.this.internalGetPort(var1, var3, var4);
         }
      }

      private class DispatchClientInstanceFactory implements ClientInstancePool.InstanceFactory {
         private ClientInstance.CreationInfo _creationInfo;

         public DispatchClientInstanceFactory(ClientInstance.CreationInfo var2) {
            this._creationInfo = var2;
         }

         public <T2> ClientInstance<T2> createClientInstance(ClientInstanceIdentity var1, Class<T2> var2, ClientInstance.InstanceReleaser<T2> var3) {
            return this.createClientInstance(var1, var2, var3, this._creationInfo);
         }

         public <T2> ClientInstance<T2> createClientInstance(ClientInstanceIdentity var1, Class<T2> var2, ClientInstance.InstanceReleaser<T2> var3, ClientInstance.CreationInfo var4) {
            return ServiceDelegate.this.internalCreateDispatch(var1, var2, var3, var4);
         }
      }
   }

   private static class ReferenceCleaner implements ClientInstance.ReferenceHolderFactory {
      private ReferenceQueue<?> queue = new ReferenceQueue();
      private PhantomReference<?> ref;
      private WeakReference<ServiceDelegate> wsd;

      public ReferenceCleaner(ServiceDelegate var1) {
         TimerManager var2 = TimerManagerFactory.getTimerManagerFactory().getTimerManager("weblogic.wsee.jaxws.WLSProvider.CloseTimer", WorkManagerFactory.getInstance().getSystem());
         final Timer var3 = var2.schedule(new CleanerTimer(), 10000L, 10000L);
         this.wsd = new WeakReference(var1);
         this.ref = new BasePhantomCleaner(this.queue, var1, new Closeable() {
            public void close() throws WebServiceException {
               var3.cancel();
            }
         });
      }

      public PhantomReference<?> create(Object var1, Closeable var2) {
         return new PhantomCleaner(this.queue, var1, var2, this.wsd);
      }

      private class CleanerTimer implements NakedTimerListener {
         private CleanerTimer() {
         }

         public void timerExpired(Timer var1) {
            Reference var2 = ReferenceCleaner.this.queue.poll();
            if (var2 != null) {
               do {
                  ((Closeable)var2).close();
                  var2 = ReferenceCleaner.this.queue.poll();
               } while(var2 != null);
            }

         }

         // $FF: synthetic method
         CleanerTimer(Object var2) {
            this();
         }
      }

      private static class PhantomCleaner extends BasePhantomCleaner {
         private ServiceDelegate delegate;

         public PhantomCleaner(ReferenceQueue<?> var1, Object var2, Closeable var3, Reference<ServiceDelegate> var4) {
            super(var1, var2, var3);
            this.delegate = (ServiceDelegate)var4.get();
         }
      }

      private static class BasePhantomCleaner extends PhantomReference implements Closeable {
         private Closeable c;

         public BasePhantomCleaner(ReferenceQueue<?> var1, Object var2, Closeable var3) {
            super(var2, var1);
            this.c = var3;
         }

         public void close() throws WebServiceException {
            this.c.close();
         }
      }
   }
}
