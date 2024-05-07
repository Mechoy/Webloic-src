package weblogic.wsee.jaxws.framework.jaxrpc;

import com.sun.istack.Nullable;
import com.sun.xml.ws.api.WSBinding;
import com.sun.xml.ws.api.message.Packet;
import com.sun.xml.ws.api.model.wsdl.WSDLBoundFault;
import com.sun.xml.ws.api.model.wsdl.WSDLBoundOperation;
import com.sun.xml.ws.api.model.wsdl.WSDLBoundPortType;
import com.sun.xml.ws.api.model.wsdl.WSDLExtensible;
import com.sun.xml.ws.api.model.wsdl.WSDLFault;
import com.sun.xml.ws.api.model.wsdl.WSDLInput;
import com.sun.xml.ws.api.model.wsdl.WSDLMessage;
import com.sun.xml.ws.api.model.wsdl.WSDLModel;
import com.sun.xml.ws.api.model.wsdl.WSDLOperation;
import com.sun.xml.ws.api.model.wsdl.WSDLOutput;
import com.sun.xml.ws.api.model.wsdl.WSDLPort;
import com.sun.xml.ws.api.model.wsdl.WSDLPortType;
import com.sun.xml.ws.api.model.wsdl.WSDLService;
import com.sun.xml.ws.api.server.Container;
import com.sun.xml.ws.binding.BindingImpl;
import com.sun.xml.ws.client.HandlerConfiguration;
import com.sun.xml.ws.developer.MemberSubmissionAddressingFeature;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.xml.namespace.QName;
import javax.xml.soap.Name;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.soap.AddressingFeature;
import weblogic.j2ee.descriptor.wl.PortComponentBean;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.wsee.WebServiceType;
import weblogic.wsee.deploy.DeployInfo;
import weblogic.wsee.deploy.EJBDeployInfo;
import weblogic.wsee.deploy.ServletDeployInfo;
import weblogic.wsee.deploy.WsdlAddressInfo;
import weblogic.wsee.jaxws.framework.policy.PolicyReferencedByAttributeWsdlExtension;
import weblogic.wsee.jaxws.framework.policy.WSDLParserExtension;
import weblogic.wsee.jaxws.owsm.PropertyConverters;
import weblogic.wsee.jaxws.tubeline.TubelineAssemblerItem;
import weblogic.wsee.monitoring.OperationStats;
import weblogic.wsee.monitoring.WsspStats;
import weblogic.wsee.policy.deployment.PolicyRef;
import weblogic.wsee.policy.deployment.PolicyURIs;
import weblogic.wsee.policy.framework.NormalizedExpression;
import weblogic.wsee.policy.runtime.PolicyServer;
import weblogic.wsee.security.configuration.WssConfiguration;
import weblogic.wsee.security.configuration.WssConfigurationException;
import weblogic.wsee.security.policy.WssPolicyContext;
import weblogic.wsee.server.servlet.SecurityHelper;
import weblogic.wsee.ws.WsEndpoint;
import weblogic.wsee.ws.WsMethod;
import weblogic.wsee.ws.WsParameterType;
import weblogic.wsee.ws.WsPort;
import weblogic.wsee.ws.WsReturnType;
import weblogic.wsee.ws.WsService;
import weblogic.wsee.ws.init.WsDeploymentContext;
import weblogic.wsee.wsa.wsaddressing.WSAVersion;
import weblogic.wsee.wsdl.WsdlBinding;
import weblogic.wsee.wsdl.WsdlBindingMessage;
import weblogic.wsee.wsdl.WsdlBindingOperation;
import weblogic.wsee.wsdl.WsdlDefinitions;
import weblogic.wsee.wsdl.WsdlDocumentation;
import weblogic.wsee.wsdl.WsdlElement;
import weblogic.wsee.wsdl.WsdlException;
import weblogic.wsee.wsdl.WsdlExtensible;
import weblogic.wsee.wsdl.WsdlExtension;
import weblogic.wsee.wsdl.WsdlFilter;
import weblogic.wsee.wsdl.WsdlImport;
import weblogic.wsee.wsdl.WsdlMessage;
import weblogic.wsee.wsdl.WsdlMethod;
import weblogic.wsee.wsdl.WsdlOperation;
import weblogic.wsee.wsdl.WsdlPart;
import weblogic.wsee.wsdl.WsdlPort;
import weblogic.wsee.wsdl.WsdlPortType;
import weblogic.wsee.wsdl.WsdlSchema;
import weblogic.wsee.wsdl.WsdlService;
import weblogic.wsee.wsdl.WsdlTypes;

public abstract class EnvironmentFactory {
   public static final String KEY = "weblogic.wsee.jaxws.framework.jaxrpc.context";
   private static final Logger LOGGER = Logger.getLogger(EnvironmentFactory.class.getName());
   private WSBinding binding;
   private Set<String> roles;
   private WSDLPort port;
   private Container container;
   private SingletonService simulatedService;
   private WsDeploymentContext simulatedDepoymentContext;
   private WsdlDefinitions wsdlDef;
   private Map<String, PropertyConverter> jaxrpcConverters;

   protected EnvironmentFactory(WSBinding var1, WSDLPort var2, Container var3) {
      this.binding = var1;
      HandlerConfiguration var4 = ((BindingImpl)var1).getHandlerConfig();
      this.roles = new HashSet();
      if (var4 != null) {
         Set var5 = var4.getRoles();
         if (var5 != null) {
            this.roles.addAll(var5);
         }
      }

      this.port = var2;
      this.container = var3;
      if (var2 != null) {
         this.wsdlDef = new SimulatedWsdlDefinitions(var2.getOwner().getParent());
      }

   }

   private void initializeConverters() {
      this.jaxrpcConverters = new HashMap();
      this.jaxrpcConverters.put("javax.xml.rpc.service.endpoint.address", new BasicPropertyConverter("javax.xml.rpc.service.endpoint.address", "javax.xml.ws.service.endpoint.address"));
      this.jaxrpcConverters.put("javax.xml.rpc.security.auth.username", new BasicPropertyConverter("javax.xml.rpc.security.auth.username", "javax.xml.ws.security.auth.username"));
      this.jaxrpcConverters.put("javax.xml.rpc.security.auth.password", new BasicPropertyConverter("javax.xml.rpc.security.auth.password", "javax.xml.ws.security.auth.password"));
      this.jaxrpcConverters.put("javax.xml.rpc.session.maintain", new BasicPropertyConverter("javax.xml.rpc.session.maintain", "javax.xml.ws.session.maintain"));
      this.jaxrpcConverters.put("javax.xml.rpc.soap.http.soapaction.use", new BasicPropertyConverter("javax.xml.rpc.soap.http.soapaction.use", "javax.xml.ws.soap.http.soapaction.use"));
      this.jaxrpcConverters.put("javax.xml.rpc.soap.http.soapaction.uri", new BasicPropertyConverter("javax.xml.rpc.soap.http.soapaction.uri", "javax.xml.ws.soap.http.soapaction.uri"));
      this.jaxrpcConverters.put("weblogic.wsee.invoke_properties", new AbstractPropertyConverter() {
         public Object convertToJAXRPC(Packet var1, MessageContext var2) {
            return var1.proxy != null ? var1.proxy.getRequestContext() : null;
         }

         public Object convertToJAXWS(Packet var1, MessageContext var2, Object var3) {
            if (var3 != null && var1.proxy != null) {
               var1.proxy.getRequestContext().putAll((Map)var3);
            }

            return null;
         }
      });
      this.jaxrpcConverters.put("weblogic.wsee.connection.end_point_address", new BasicPropertyConverter("weblogic.wsee.connection.end_point_address", "com.sun.xml.ws.transport.http.servlet.requestURL"));
      this.jaxrpcConverters.put("weblogic.wsee.transport.servlet.request.secure", new AbstractPropertyConverter() {
         public Object convertToJAXRPC(Packet var1, MessageContext var2) {
            HttpServletRequest var3 = (HttpServletRequest)var2.get("javax.xml.ws.servlet.request");
            return var3 != null ? var3.isSecure() : null;
         }
      });
      this.jaxrpcConverters.put("weblogic.wsee.transport.client.cert.required", new AbstractPropertyConverter() {
         public Object convertToJAXRPC(Packet var1, MessageContext var2) {
            HttpServletRequest var3 = (HttpServletRequest)var2.get("javax.xml.ws.servlet.request");
            return var3 != null ? SecurityHelper.isClientCertPresent(var3) : null;
         }
      });
      this.jaxrpcConverters.put("weblogic.wsee.transport.headers", new AbstractPropertyConverter() {
         public Object convertToJAXRPC(Packet var1, MessageContext var2) {
            return ((javax.xml.ws.handler.soap.SOAPMessageContext)var2).getMessage().getMimeHeaders();
         }

         public Object convertToJAXWS(Packet var1, MessageContext var2, Object var3) {
            return null;
         }
      });
      this.jaxrpcConverters.put("weblogic.wsee.service_name", new AbstractPropertyConverter() {
         public Object convertToJAXRPC(Packet var1, MessageContext var2) {
            return EnvironmentFactory.this.getServiceName();
         }
      });
      this.jaxrpcConverters.put("weblogic.wsee.context_path", new AbstractPropertyConverter() {
         public Object convertToJAXRPC(Packet var1, MessageContext var2) {
            return EnvironmentFactory.this.getContextPath();
         }
      });
      this.jaxrpcConverters.put("weblogic.wsee.service_uri", new AbstractPropertyConverter() {
         public Object convertToJAXRPC(Packet var1, MessageContext var2) {
            return EnvironmentFactory.this.getServiceUris();
         }
      });
      this.jaxrpcConverters.put("weblogic.wsee.security_realm", new AbstractPropertyConverter() {
         public Object convertToJAXRPC(Packet var1, MessageContext var2) {
            return EnvironmentFactory.this.getSecurityRealmName();
         }
      });
      this.jaxrpcConverters.put("weblogic.wsee.application_id", new AbstractPropertyConverter() {
         public Object convertToJAXRPC(Packet var1, MessageContext var2) {
            return EnvironmentFactory.this.getApplication();
         }
      });
      this.jaxrpcConverters.put("weblogic.wsee.addressing.version", new AbstractPropertyConverter() {
         public Object convertToJAXRPC(Packet var1, MessageContext var2) {
            return EnvironmentFactory.this.getWSAVersion();
         }
      });
      this.jaxrpcConverters.putAll((new PropertyConverters(this)).getConverters());
   }

   public Map<String, PropertyConverter> getJAXRPCConverters() {
      if (this.jaxrpcConverters == null) {
         this.initializeConverters();
      }

      return this.jaxrpcConverters;
   }

   public WsdlDefinitions getWsdlDef() {
      return this.wsdlDef;
   }

   public Container getContainer() {
      return this.container;
   }

   public WSBinding getBinding() {
      return this.binding;
   }

   public WSDLPort getPort() {
      return this.port;
   }

   protected SimulatedWsMethod createSimulatedWsMethod(WsdlOperation var1) {
      return new SimulatedWsMethod(var1);
   }

   protected SimulatedWsEndpoint createSimulatedWsEndpoint(Map<String, WsMethod> var1, @Nullable QName var2) {
      return new SimulatedWsEndpoint(var1, var2);
   }

   protected SimulatedWsPort createSimulatedWsPort(WsEndpoint var1, @Nullable QName var2) {
      return new SimulatedWsPort(var1, var2);
   }

   protected abstract void initPolicyContext(SingletonService var1);

   public SingletonService getService() {
      if (this.simulatedService == null) {
         final QName var1 = this.port != null ? this.port.getName() : null;
         final QName var2 = this.port != null ? this.port.getOwner().getName() : null;
         HashMap var3 = new HashMap();
         WsdlDefinitions var4 = this.getWsdlDef();
         if (var4 != null) {
            Iterator var5 = ((WsdlPort)var4.getPorts().get(var1)).getPortType().getOperations().values().iterator();

            while(var5.hasNext()) {
               WsdlOperation var6 = (WsdlOperation)var5.next();
               var3.put(var6.getName().getLocalPart(), this.createSimulatedWsMethod(var6));
            }
         }

         final SimulatedWsEndpoint var8 = this.createSimulatedWsEndpoint(var3, var1);
         final SimulatedWsPort var9 = this.createSimulatedWsPort(var8, var1);
         if (this.getContainer() != null) {
            DeployInfo var7 = (DeployInfo)this.getContainer().getSPI(DeployInfo.class);
            if (var7 != null) {
               var9.setPortComponent(var7.getWlPortComp());
            }
         }

         this.simulatedService = new SingletonService() {
            private boolean isUsingPolicy = false;
            private WssPolicyContext wssPolicyContext = null;

            public WsPort getSingletonPort() {
               return var9;
            }

            public WsEndpoint getSingletonEndpoint() {
               return var8;
            }

            public void addEndpoint(QName var1x, WsEndpoint var2x) {
               throw new UnsupportedOperationException();
            }

            public WsPort addPort(String var1x, WsdlPort var2x, WsEndpoint var3) {
               throw new UnsupportedOperationException();
            }

            public WsEndpoint getEndpoint(QName var1x) {
               return EnvironmentFactory.this.getWsdlDef() != null && var1 != null && ((WsdlPort)EnvironmentFactory.this.getWsdlDef().getPorts().get(var1)).getPortType().getName().equals(var1x) ? var8 : null;
            }

            public Iterator<WsEndpoint> getEndpoints() {
               return new Iterator<WsEndpoint>() {
                  private boolean nextCalled = false;

                  public boolean hasNext() {
                     return !this.nextCalled;
                  }

                  public WsEndpoint next() {
                     if (this.nextCalled) {
                        throw new NoSuchElementException();
                     } else {
                        this.nextCalled = true;
                        return var8;
                     }
                  }

                  public void remove() {
                     throw new UnsupportedOperationException();
                  }
               };
            }

            public PolicyServer getPolicyServer() {
               return this.wssPolicyContext != null ? this.wssPolicyContext.getPolicyServer() : null;
            }

            public WsPort getPort(String var1x) {
               return var1x.equals(var1.getLocalPart()) ? var9 : null;
            }

            public Iterator<WsPort> getPorts() {
               return new Iterator<WsPort>() {
                  private boolean nextCalled = false;

                  public boolean hasNext() {
                     return !this.nextCalled;
                  }

                  public WsPort next() {
                     if (this.nextCalled) {
                        throw new NoSuchElementException();
                     } else {
                        this.nextCalled = true;
                        return var9;
                     }
                  }

                  public void remove() {
                     throw new UnsupportedOperationException();
                  }
               };
            }

            public WsdlService getWsdlService() {
               return EnvironmentFactory.this.getWsdlDef() == null ? null : (WsdlService)EnvironmentFactory.this.getWsdlDef().getServices().get(var2);
            }

            public WssConfiguration getWssConfiguration() {
               return this.wssPolicyContext.getWssConfiguration();
            }

            public WssPolicyContext getWssPolicyContext() {
               return this.wssPolicyContext;
            }

            public void initWssConfiguration() throws WssConfigurationException {
               this.wssPolicyContext.getWssConfiguration().init();
            }

            public boolean isUsingPolicy() {
               return this.isUsingPolicy;
            }

            public void setUsingPolicy(boolean var1x) {
               this.isUsingPolicy = var1x;
            }

            public void setWssPolicyContext(WssPolicyContext var1x) {
               this.wssPolicyContext = var1x;
            }
         };
         this.initPolicyContext(this.simulatedService);
      }

      return this.simulatedService;
   }

   public String[] getServiceUris() {
      DeployInfo var1 = this.container != null ? (DeployInfo)this.container.getSPI(DeployInfo.class) : null;
      if (var1 instanceof ServletDeployInfo) {
         ServletDeployInfo var3 = (ServletDeployInfo)var1;
         return var3.getServiceURIs();
      } else if (var1 instanceof EJBDeployInfo) {
         EJBDeployInfo var2 = (EJBDeployInfo)var1;
         return var2.getServiceURIs();
      } else {
         return null;
      }
   }

   public String getContextPath() {
      DeployInfo var1 = this.container != null ? (DeployInfo)this.container.getSPI(DeployInfo.class) : null;
      if (var1 instanceof ServletDeployInfo) {
         ServletDeployInfo var3 = (ServletDeployInfo)var1;
         return var3.getContextPath();
      } else if (var1 instanceof EJBDeployInfo) {
         EJBDeployInfo var2 = (EJBDeployInfo)var1;
         return var2.getContextPath();
      } else {
         return null;
      }
   }

   public String getSecurityRealmName() {
      DeployInfo var1 = this.container != null ? (DeployInfo)this.container.getSPI(DeployInfo.class) : null;
      return var1 != null ? var1.getSecurityRealmName() : null;
   }

   public String getApplication() {
      DeployInfo var1 = this.container != null ? (DeployInfo)this.container.getSPI(DeployInfo.class) : null;
      return var1 != null ? var1.getApplication() : null;
   }

   public WSAVersion getWSAVersion() {
      WebServiceFeature var1 = this.binding.getFeature(MemberSubmissionAddressingFeature.class);
      if (var1 != null && var1.isEnabled()) {
         return WSAVersion.MemberSubmission;
      } else {
         var1 = this.binding.getFeature(AddressingFeature.class);
         return var1 != null && var1.isEnabled() ? WSAVersion.WSA10 : null;
      }
   }

   public String getServiceName() {
      DeployInfo var1 = this.container != null ? (DeployInfo)this.container.getSPI(DeployInfo.class) : null;
      if (var1 instanceof ServletDeployInfo) {
         ServletDeployInfo var3 = (ServletDeployInfo)var1;
         return var3.getServiceName();
      } else if (var1 instanceof EJBDeployInfo) {
         EJBDeployInfo var2 = (EJBDeployInfo)var1;
         return var2.getServiceName();
      } else {
         return this.port != null ? this.port.getOwner().getName().getLocalPart() : null;
      }
   }

   public WsDeploymentContext getDeploymentContext() {
      if (this.simulatedDepoymentContext == null) {
         this.simulatedDepoymentContext = this.buildDeploymentContext(this.getServiceUris(), this.getContextPath(), this.getServiceName());
      }

      return this.simulatedDepoymentContext;
   }

   protected String getCurrentVersionId() {
      return null;
   }

   protected WsDeploymentContext newDeploymentContext() {
      return new WsDeploymentContext();
   }

   protected WsDeploymentContext buildDeploymentContext(String[] var1, String var2, String var3) {
      WsDeploymentContext var4 = this.newDeploymentContext();
      var4.setWsService(this.getService());
      var4.setServiceURIs(var1);
      String var5 = this.getCurrentVersionId();
      var4.setVersion(var5);
      var4.setContextPath(var2);
      var4.setServiceName(var3);
      var4.setType(WebServiceType.JAXWS);
      return var4;
   }

   public HandlerList buildServerHandlerList(Set<TubelineAssemblerItem> var1) {
      return new HandlerList(var1) {
         protected void initialize() {
            this.add("MESSAGE_CONTEXT_INIT_HANDLER", (TubelineAssemblerItem)null);
            this.add("CONNECTION_HANDLER", (TubelineAssemblerItem)null);
            this.add("WS_VERSION_REDIRECT_HANDLER", (TubelineAssemblerItem)null);
            this.add("DIRECT_INVOKE_HANDLER", (TubelineAssemblerItem)null);
            this.add("ADDRESSING_HANDLER", (TubelineAssemblerItem)null);
            this.add("OPERATION_LOOKUP_HANDLER", (TubelineAssemblerItem)null);
            this.add("ONE_WAY_HANDLER", (TubelineAssemblerItem)null);
            this.add("PRE_INVOKE_HANDLER", (TubelineAssemblerItem)null);
            this.add("CODEC_HANDLER", (TubelineAssemblerItem)null);
            this.add("AUTHORIZATION_HANDLER", (TubelineAssemblerItem)null);
            super.initialize();
            this.add("COMPONENT_HANDLER", (TubelineAssemblerItem)null);
         }
      };
   }

   public HandlerList buildClientHandlerList(Set<TubelineAssemblerItem> var1) {
      return new HandlerList(var1) {
         protected void initialize() {
            this.add("CODEC_HANDLER", (TubelineAssemblerItem)null);
            this.add("ADDRESSING_HANDLER", (TubelineAssemblerItem)null);
            this.add("MMEHEADER_HANDLER", (TubelineAssemblerItem)null);
            super.initialize();
            this.add("ASYNC_HANDLER", (TubelineAssemblerItem)null);
            this.add("CONNECTION_HANDLER", (TubelineAssemblerItem)null);
         }
      };
   }

   public SOAPMessageContext getContext(Packet var1) {
      Context var2 = (Context)var1.invocationProperties.get("weblogic.wsee.jaxws.framework.jaxrpc.context");
      if (var2 == null || !var2.isValid(var1)) {
         var2 = new Context(var1);
         var1.invocationProperties.put("weblogic.wsee.jaxws.framework.jaxrpc.context", var2);
      }

      return var2.getContext();
   }

   public static SOAPMessageContext getContext(MessageContext var0) {
      Context var1 = (Context)var0.get("weblogic.wsee.jaxws.framework.jaxrpc.context");
      return var1 == null ? null : var1.getContext();
   }

   public Context create(Packet var1) {
      return new Context(var1);
   }

   private class Context {
      private SOAPMessageContext context;

      public Context(Packet var2) {
         this.context = new SOAPMessageContext(EnvironmentFactory.this, EnvironmentFactory.this.binding, var2, EnvironmentFactory.this.roles);
      }

      public SOAPMessageContext getContext() {
         return this.context;
      }

      public boolean isValid(Packet var1) {
         return this.context.isValid(var1);
      }
   }

   protected class SimulatedWsPort extends WsPort {
      private WsEndpoint wse;
      @Nullable
      protected QName wsdlPort;
      private weblogic.wsee.handler.HandlerList hl;
      private Set<PolicyRef> inboundRefs = new HashSet();
      private Set<PolicyRef> outboundRefs = new HashSet();
      private Map<Name, QName> soapDispatchMap = new HashMap();
      private PortComponentBean portComp;
      private WsspStats wsspStats = null;

      protected SimulatedWsPort(WsEndpoint var2, @Nullable QName var3) {
         this.wse = var2;
         this.wsdlPort = var3;
      }

      public void addPolicyRef(PolicyRef var1, boolean var2, boolean var3) {
         if (var2) {
            this.inboundRefs.add(var1);
         }

         if (var3) {
            this.outboundRefs.add(var1);
         }

      }

      public WsEndpoint getEndpoint() {
         return this.wse;
      }

      public Set<PolicyRef> getInboundPolicyRefs() {
         return this.inboundRefs;
      }

      public weblogic.wsee.handler.HandlerList getInternalHandlerList() {
         return this.hl;
      }

      public Set<PolicyRef> getOutboundPolicyRefs() {
         return this.outboundRefs;
      }

      public RuntimeMBean getRuntimeMBean() {
         throw new UnsupportedOperationException();
      }

      public Map<Name, QName> getSoapDispatchMap() {
         return this.soapDispatchMap;
      }

      public Map<Name, QName> getActionDispatchMap(String var1) {
         return null;
      }

      @Nullable
      public WsdlPort getWsdlPort() {
         return EnvironmentFactory.this.getWsdlDef() != null && this.wsdlPort != null ? (WsdlPort)EnvironmentFactory.this.getWsdlDef().getPorts().get(this.wsdlPort) : null;
      }

      public void removePolicyRef(PolicyRef var1) {
         this.inboundRefs.remove(var1);
         this.outboundRefs.remove(var1);
      }

      public void resetPolicyRefs() {
         this.inboundRefs.clear();
         this.outboundRefs.clear();
      }

      public void setInternalHandlerList(weblogic.wsee.handler.HandlerList var1) {
         this.hl = var1;
      }

      public PortComponentBean getPortComponent() {
         return this.portComp;
      }

      public void setPortComponent(PortComponentBean var1) {
         this.portComp = var1;
      }

      public WsspStats getWsspStats() {
         return this.wsspStats;
      }

      public void setWsspStats(WsspStats var1) {
         this.wsspStats = var1;
      }
   }

   protected class SimulatedWsEndpoint extends WsEndpoint {
      private Map<String, WsMethod> methods;
      @Nullable
      private QName wsdlPort;

      protected SimulatedWsEndpoint(Map<String, WsMethod> var2, @Nullable QName var3) {
         this.methods = var2;
         this.wsdlPort = var3;
      }

      public Class getEndpointInterface() {
         throw new UnsupportedOperationException();
      }

      public Class getJwsClass() {
         throw new UnsupportedOperationException();
      }

      public WsMethod getMethod(String var1) {
         return (WsMethod)this.methods.get(var1);
      }

      public Iterator<WsMethod> getMethods() {
         return this.methods.values().iterator();
      }

      public WsdlPortType getPortType() {
         return EnvironmentFactory.this.getWsdlDef() != null && this.wsdlPort != null ? ((WsdlPort)EnvironmentFactory.this.getWsdlDef().getPorts().get(this.wsdlPort)).getPortType() : null;
      }

      public WsService getService() {
         return EnvironmentFactory.this.simulatedService;
      }
   }

   protected class SimulatedWsMethod extends WsMethod {
      private WsdlOperation op;
      private NormalizedExpression cip;
      private NormalizedExpression cop;
      private OperationStats os;

      public SimulatedWsMethod(WsdlOperation var2) {
         this.op = var2;
      }

      public NormalizedExpression getCachedEffectiveInboundPolicy() {
         return this.cip;
      }

      public NormalizedExpression getCachedEffectiveOutboundPolicy() {
         return this.cop;
      }

      public WsEndpoint getEndpoint() {
         return EnvironmentFactory.this.simulatedService.getSingletonEndpoint();
      }

      public Iterator getExceptions() {
         throw new UnsupportedOperationException();
      }

      public String getMethodName() {
         throw new UnsupportedOperationException();
      }

      public QName getOperationName() {
         return this.op.getName();
      }

      public WsParameterType getParameter(int var1) {
         throw new UnsupportedOperationException();
      }

      public int getParameterSize() {
         throw new UnsupportedOperationException();
      }

      public Iterator getParameters() {
         throw new UnsupportedOperationException();
      }

      public WsReturnType getReturnType() {
         throw new UnsupportedOperationException();
      }

      public QName getReturnWrapperElement() {
         throw new UnsupportedOperationException();
      }

      public OperationStats getStats() {
         return this.os;
      }

      public QName getWrapperElement() {
         throw new UnsupportedOperationException();
      }

      public boolean isWrapped() {
         throw new UnsupportedOperationException();
      }

      public void setCachedEffectiveInboundPolicy(NormalizedExpression var1) {
         this.cip = var1;
      }

      public void setCachedEffectiveOutboundPolicy(NormalizedExpression var1) {
         this.cop = var1;
      }

      public void setStats(OperationStats var1) {
         this.os = var1;
      }
   }

   protected class SimulatedWsdlExtensible extends SimulatedWsdlElement implements WsdlExtensible {
      private WSDLExtensible inner;

      public SimulatedWsdlExtensible(WSDLExtensible var2) {
         super();
         this.inner = var2;
      }

      private WsdlExtensible get() {
         return WsdlExtensibleHolder.get(this.inner);
      }

      public WsdlExtension getExtension(String var1) {
         return this.get().getExtension(var1);
      }

      public List<WsdlExtension> getExtensionList(String var1) {
         return this.get().getExtensionList(var1);
      }

      public Map<String, List<WsdlExtension>> getExtensions() {
         return this.get().getExtensions();
      }

      public void putExtension(WsdlExtension var1) {
         this.get().putExtension(var1);
      }
   }

   protected class SimulatedWsdlElement implements WsdlElement {
      public WsdlDocumentation getDocumentation() {
         throw new UnsupportedOperationException();
      }
   }

   protected class SimulatedWsdlMessage extends SimulatedWsdlExtensible implements WsdlMessage {
      private WSDLMessage message;
      private PolicyURIs policyURIs;

      public SimulatedWsdlMessage(WSDLMessage var2) {
         super(var2);
         this.message = var2;
      }

      public QName getName() {
         return this.message.getName();
      }

      public Map<String, ? extends WsdlPart> getParts() {
         throw new UnsupportedOperationException();
      }

      public PolicyURIs getPolicyUris() {
         return this.policyURIs;
      }

      public void setPolicyUris(PolicyURIs var1) {
         this.policyURIs = var1;
      }
   }

   protected class SimulatedWsdlOperation extends SimulatedWsdlExtensible implements WsdlOperation {
      private WSDLOperation operation;
      private PolicyURIs policyURIs;
      private PolicyURIs inputPolicy;
      private PolicyURIs outputPolicy;
      private Map<String, SimulatedWsdlMessage> faults = new HashMap();
      private SimulatedWsdlMessage input;
      private SimulatedWsdlMessage output;

      public SimulatedWsdlOperation(SimulatedWsdlDefinitions var2, WSDLOperation var3) {
         super(var3);
         this.operation = var3;
         Map var4 = var2.getMessages();
         Iterator var5 = var3.getFaults().iterator();

         while(var5.hasNext()) {
            WSDLFault var6 = (WSDLFault)var5.next();
            SimulatedWsdlMessage var7 = EnvironmentFactory.this.new SimulatedWsdlMessage(var6.getMessage());
            this.faults.put(var6.getName(), var7);
            var4.put(var7.getName(), var7);
         }

         this.input = EnvironmentFactory.this.new SimulatedWsdlMessage(var3.getInput().getMessage());
         if (var3.getOutput() != null) {
            this.output = EnvironmentFactory.this.new SimulatedWsdlMessage(var3.getOutput().getMessage());
         }

      }

      public PolicyURIs getFaultPolicyUris(String var1) {
         return ((SimulatedWsdlMessage)this.faults.get(var1)).getPolicyUris();
      }

      public Map<String, SimulatedWsdlMessage> getFaults() {
         return this.faults;
      }

      public SimulatedWsdlMessage getInput() {
         return this.input;
      }

      public String getInputAction() {
         return this.operation.getInput().getAction();
      }

      public String getInputName() {
         WSDLInput var1 = this.operation.getInput();
         return var1 == null ? null : var1.getName();
      }

      public String getOutputName() {
         WSDLOutput var1 = this.operation.getOutput();
         return var1 == null ? null : var1.getName();
      }

      public PolicyURIs getInputPolicyUris() {
         return this.inputPolicy;
      }

      public QName getName() {
         return this.operation.getName();
      }

      public SimulatedWsdlMessage getOutput() {
         return this.output;
      }

      public String getOutputAction() {
         return this.operation.getOutput().getAction();
      }

      public PolicyURIs getOutputPolicyUris() {
         return this.outputPolicy;
      }

      public PolicyURIs getPolicyUris() {
         return this.policyURIs;
      }

      public int getType() {
         return this.operation.isOneWay() ? 1 : 0;
      }

      public WsdlMethod getWsdlMethod() {
         throw new UnsupportedOperationException();
      }

      public WsdlMethod getWsdlMethod(boolean var1) {
         throw new UnsupportedOperationException();
      }

      public boolean isWrapped() {
         throw new UnsupportedOperationException();
      }

      public void setFaultPolicyUris(String var1, PolicyURIs var2) {
         ((SimulatedWsdlMessage)this.faults.get(var1)).setPolicyUris(var2);
      }

      public void setInputPolicyUris(PolicyURIs var1) {
         this.inputPolicy = var1;
      }

      public void setOutputPolicyUris(PolicyURIs var1) {
         this.outputPolicy = var1;
      }

      public void setPolicyUris(PolicyURIs var1) {
         this.policyURIs = var1;
      }
   }

   protected class SimulatedWsdlPortType extends SimulatedWsdlElement implements WsdlPortType {
      private WSDLPortType portType;
      private SimulatedWsdlDefinitions owner;
      private PolicyURIs policyURIs;
      private Map<QName, SimulatedWsdlOperation> operations = new HashMap();

      public SimulatedWsdlPortType(SimulatedWsdlDefinitions var2, WSDLPortType var3) {
         super();
         this.owner = var2;
         this.portType = var3;
         Iterator var4 = var3.getOperations().iterator();

         while(var4.hasNext()) {
            WSDLOperation var5 = (WSDLOperation)var4.next();
            this.operations.put(var5.getName(), EnvironmentFactory.this.new SimulatedWsdlOperation(var2, var5));
         }

         WsdlExtensibleHolder var7 = (WsdlExtensibleHolder)var3.getExtension(WsdlExtensibleHolder.class);
         PolicyReferencedByAttributeWsdlExtension var6 = var7 == null ? null : (PolicyReferencedByAttributeWsdlExtension)var7.getExtension("PolicyURIs");
         if (var6 != null) {
            this.policyURIs = var6.getPolicyUri();
         }

      }

      public QName getName() {
         return this.portType.getName();
      }

      public Map<QName, SimulatedWsdlOperation> getOperations() {
         return this.operations;
      }

      public PolicyURIs getPolicyUris() {
         return this.policyURIs;
      }

      public void setPolicyUris(PolicyURIs var1) {
         this.policyURIs = var1;
      }
   }

   protected class SimulatedWsdlPort extends SimulatedWsdlExtensible implements WsdlPort {
      private WSDLPort port;
      private SimulatedWsdlService service;
      private SimulatedWsdlBinding binding;
      private PolicyURIs policyURIs;

      public SimulatedWsdlPort(SimulatedWsdlService var2, SimulatedWsdlBinding var3, WSDLPort var4) {
         super(var4);
         this.service = var2;
         this.binding = var3;
         this.port = var4;
      }

      public SimulatedWsdlBinding getBinding() {
         return this.binding;
      }

      public SimulatedWsdlDefinitions getDefinitions() {
         return this.service.getDefinitions();
      }

      public QName getName() {
         return this.port.getName();
      }

      public PolicyURIs getPolicyUris() {
         return this.policyURIs;
      }

      public WsdlAddressInfo.PortAddress getPortAddress() {
         return null;
      }

      public SimulatedWsdlPortType getPortType() {
         return this.binding.getPortType();
      }

      public SimulatedWsdlService getService() {
         return this.service;
      }

      public String getTransport() {
         return null;
      }

      public void setPolicyUris(PolicyURIs var1) {
         this.policyURIs = var1;
      }

      public void setPortAddress(WsdlAddressInfo.PortAddress var1) {
      }
   }

   protected class SimulatedWsdlService extends SimulatedWsdlExtensible implements WsdlService {
      private WSDLService service;
      private SimulatedWsdlDefinitions owner;
      private PolicyURIs policyURIs;
      private Map<QName, SimulatedWsdlPort> ports = new HashMap();
      private List<SimulatedWsdlPortType> portTypes = new ArrayList();

      public SimulatedWsdlService(SimulatedWsdlDefinitions var2, WSDLService var3) {
         super(var3);
         this.owner = var2;
         this.service = var3;
         Iterator var4 = var3.getPorts().iterator();

         while(var4.hasNext()) {
            WSDLPort var5 = (WSDLPort)var4.next();
            SimulatedWsdlPort var6 = EnvironmentFactory.this.new SimulatedWsdlPort(this, (SimulatedWsdlBinding)var2.getBindings().get(var5.getBinding().getName()), var5);
            this.ports.put(var5.getName(), var6);
            this.portTypes.add(var6.getPortType());
         }

      }

      public SimulatedWsdlDefinitions getDefinitions() {
         return this.owner;
      }

      public QName getName() {
         return this.service.getName();
      }

      public PolicyURIs getPolicyUris() {
         return this.policyURIs;
      }

      public void setPolicyUris(PolicyURIs var1) {
         this.policyURIs = var1;
      }

      public List<SimulatedWsdlPortType> getPortTypes() {
         return this.portTypes;
      }

      public Map<QName, SimulatedWsdlPort> getPorts() {
         return this.ports;
      }

      public WsdlFilter getWsdlFilter() {
         throw new UnsupportedOperationException();
      }
   }

   protected class SimulatedWsdlBindingMessage extends SimulatedWsdlExtensible implements WsdlBindingMessage {
      private SimulatedWsdlBindingOperation op;
      private SimulatedWsdlMessage msg;
      private String name;
      private PolicyURIs policyURIs;

      public SimulatedWsdlBindingMessage(WSDLExtensible var2, String var3, SimulatedWsdlBindingOperation var4, SimulatedWsdlMessage var5) {
         super(var2);
         this.op = var4;
         this.msg = var5;
         this.name = var3;
      }

      public SimulatedWsdlBindingOperation getBindingOperation() {
         return this.op;
      }

      public SimulatedWsdlMessage getMessage() throws WsdlException {
         return this.msg;
      }

      public String getName() {
         return this.name;
      }

      public PolicyURIs getPolicyUris() {
         return this.policyURIs;
      }

      public int getType() {
         return 0;
      }

      public void setPolicyUris(PolicyURIs var1) {
         this.policyURIs = var1;
      }
   }

   protected class SimulatedWsdlBindingOperation extends SimulatedWsdlExtensible implements WsdlBindingOperation {
      private WSDLBoundOperation boundOp;
      private SimulatedWsdlBinding binding;
      private Map<String, SimulatedWsdlBindingMessage> faults = new HashMap();
      private PolicyURIs policyURIs;
      private SimulatedWsdlBindingMessage input;
      private SimulatedWsdlBindingMessage output;

      public SimulatedWsdlBindingOperation(SimulatedWsdlDefinitions var2, SimulatedWsdlBinding var3, WSDLBoundOperation var4) {
         super(var4);
         this.binding = var3;
         this.boundOp = var4;
         Iterator var5 = var4.getFaults().iterator();

         while(var5.hasNext()) {
            WSDLBoundFault var6 = (WSDLBoundFault)var5.next();
            this.faults.put(var6.getName(), EnvironmentFactory.this.new SimulatedWsdlBindingMessage(var6, var6.getName(), this, (SimulatedWsdlMessage)var2.getMessages().get(var6.getFault().getMessage().getName())));
         }

         WSDLOperation var8 = var4.getOperation();
         WSDLParserExtension.PseudoBoundInputExtensible var9 = (WSDLParserExtension.PseudoBoundInputExtensible)var4.getExtension(WSDLParserExtension.PseudoBoundInputExtensible.class);
         this.input = EnvironmentFactory.this.new SimulatedWsdlBindingMessage(var9, var8.getInput().getName(), this, (SimulatedWsdlMessage)var2.getMessages().get(var8.getInput().getMessage().getName()));
         if (var8.getOutput() != null) {
            WSDLParserExtension.PseudoBoundOutputExtensible var7 = (WSDLParserExtension.PseudoBoundOutputExtensible)var4.getExtension(WSDLParserExtension.PseudoBoundOutputExtensible.class);
            this.output = EnvironmentFactory.this.new SimulatedWsdlBindingMessage(var7, var8.getOutput().getName(), this, (SimulatedWsdlMessage)var2.getMessages().get(var8.getOutput().getMessage().getName()));
         }

      }

      public SimulatedWsdlBinding getBinding() {
         return this.binding;
      }

      public Map<String, SimulatedWsdlBindingMessage> getFaults() {
         return this.faults;
      }

      public SimulatedWsdlBindingMessage getInput() {
         return this.input;
      }

      public QName getName() {
         return this.boundOp.getName();
      }

      public SimulatedWsdlBindingMessage getOutput() {
         return this.output;
      }

      public PolicyURIs getPolicyUris() {
         return this.policyURIs;
      }

      public void setPolicyUris(PolicyURIs var1) {
         this.policyURIs = var1;
      }
   }

   protected class SimulatedWsdlBinding extends SimulatedWsdlExtensible implements WsdlBinding {
      private SimulatedWsdlPortType portType;
      private SimulatedWsdlDefinitions owner;
      private WSDLBoundPortType binding;
      private PolicyURIs policyURIs;
      private Map<QName, SimulatedWsdlBindingOperation> operations = new HashMap();

      public SimulatedWsdlBinding(SimulatedWsdlDefinitions var2, SimulatedWsdlPortType var3, WSDLBoundPortType var4) {
         super(var4);
         this.portType = var3;
         this.binding = var4;
         this.owner = var2;
         Iterator var5 = var4.getBindingOperations().iterator();

         while(var5.hasNext()) {
            WSDLBoundOperation var6 = (WSDLBoundOperation)var5.next();
            this.operations.put(var6.getName(), EnvironmentFactory.this.new SimulatedWsdlBindingOperation(var2, this, var6));
         }

      }

      public String getBindingType() {
         throw new UnsupportedOperationException();
      }

      public QName getName() {
         return this.binding.getName();
      }

      public Map<QName, SimulatedWsdlBindingOperation> getOperations() {
         return this.operations;
      }

      public PolicyURIs getPolicyUris() {
         return this.policyURIs;
      }

      public SimulatedWsdlPortType getPortType() {
         return this.portType;
      }

      public String getTransportProtocol() {
         return null;
      }

      public String getTransportURI() {
         return null;
      }

      public void setPolicyUris(PolicyURIs var1) {
         this.policyURIs = var1;
      }
   }

   protected class SimulatedWsdlDefinitions extends SimulatedWsdlExtensible implements WsdlDefinitions {
      private WSDLModel model;
      private Map<QName, SimulatedWsdlService> services = new HashMap();
      private Map<QName, SimulatedWsdlPortType> portTypes = new HashMap();
      private Map<QName, SimulatedWsdlBinding> bindings = new HashMap();
      private Map<QName, SimulatedWsdlPort> ports = new HashMap();
      private Map<QName, SimulatedWsdlMessage> messages = new HashMap();

      public SimulatedWsdlDefinitions(WSDLModel var2) {
         super(var2);
         this.model = var2;
         Iterator var3 = var2.getPortTypes().values().iterator();

         while(var3.hasNext()) {
            WSDLPortType var4 = (WSDLPortType)var3.next();
            this.portTypes.put(var4.getName(), EnvironmentFactory.this.new SimulatedWsdlPortType(this, var4));
         }

         var3 = var2.getBindings().values().iterator();

         while(var3.hasNext()) {
            WSDLBoundPortType var6 = (WSDLBoundPortType)var3.next();
            this.bindings.put(var6.getName(), EnvironmentFactory.this.new SimulatedWsdlBinding(this, (SimulatedWsdlPortType)this.portTypes.get(var6.getPortTypeName()), var6));
         }

         var3 = var2.getServices().values().iterator();

         while(var3.hasNext()) {
            WSDLService var7 = (WSDLService)var3.next();
            SimulatedWsdlService var5 = EnvironmentFactory.this.new SimulatedWsdlService(this, var7);
            this.services.put(var7.getName(), var5);
            this.ports.putAll(var5.getPorts());
         }

      }

      public Map<QName, SimulatedWsdlBinding> getBindings() {
         return this.bindings;
      }

      public String getEncoding() {
         throw new UnsupportedOperationException();
      }

      public List<? extends WsdlDefinitions> getImportedWsdlDefinitions() {
         return Collections.emptyList();
      }

      public List<? extends WsdlImport> getImports() {
         return Collections.emptyList();
      }

      public Set<String> getKnownImportedWsdlLocations() {
         return Collections.emptySet();
      }

      public Map<QName, SimulatedWsdlMessage> getMessages() {
         return this.messages;
      }

      public String getName() {
         throw new UnsupportedOperationException();
      }

      public Map<QName, SimulatedWsdlPortType> getPortTypes() {
         return this.portTypes;
      }

      public Map<QName, SimulatedWsdlPort> getPorts() {
         return this.ports;
      }

      public Map<QName, SimulatedWsdlService> getServices() {
         return this.services;
      }

      public String getTargetNamespace() {
         throw new UnsupportedOperationException();
      }

      public WsdlSchema getTheOnlySchema() {
         throw new UnsupportedOperationException();
      }

      public WsdlTypes getTypes() {
         throw new UnsupportedOperationException();
      }

      public String getWsdlLocation() {
         throw new UnsupportedOperationException();
      }
   }

   public interface SingletonService extends WsService {
      WsPort getSingletonPort();

      WsEndpoint getSingletonEndpoint();
   }
}
