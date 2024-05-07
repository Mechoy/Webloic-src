package weblogic.wsee.jaxws.framework.jaxrpc;

import com.sun.xml.ws.api.SOAPVersion;
import com.sun.xml.ws.api.WSBinding;
import com.sun.xml.ws.api.WSService;
import com.sun.xml.ws.api.message.Message;
import com.sun.xml.ws.api.message.Packet;
import com.sun.xml.ws.api.model.wsdl.WSDLBoundOperation;
import com.sun.xml.ws.api.model.wsdl.WSDLPort;
import com.sun.xml.ws.handler.SOAPMessageContextImpl;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.soap.MTOMFeature;
import weblogic.jws.jaxws.client.async.AsyncClientTransportFeature;
import weblogic.utils.collections.CombinedIterator;
import weblogic.wsee.connection.Connection;
import weblogic.wsee.connection.transport.Transport;
import weblogic.wsee.jaxws.framework.jaxrpc.client.ClientEnvironmentFactory;
import weblogic.wsee.jaxws.owsm.WsdlDefinitionFeature;
import weblogic.wsee.mc.api.McFeature;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.message.soap.SoapMessageContext;
import weblogic.wsee.message.soap.SoapMsgHeaders;
import weblogic.wsee.ws.WsMethod;
import weblogic.wsee.ws.WsPort;
import weblogic.wsee.ws.dispatch.Dispatcher;
import weblogic.wsee.wsdl.WsdlBindingOperation;
import weblogic.wsee.wsdl.WsdlOperation;
import weblogic.wsee.wsdl.WsdlPort;

public class SOAPMessageContext extends SoapMessageContext {
   public static final String JAX_WS_RUNTIME = "weblogic.wsee.jaxws.framework.jaxrpc.SOAPMessageContext.JAX_WS_RUNTIME";
   private static final String OPERATION_NAME_KEY = "weblogic.wsee.jaxws.framework.jaxrpc.OPERATION_NAME";
   public static final String ASYNC_CLIENT_FEATURE = "weblogic.wsee.jaxws.framework.jaxrpc.SOAPMessageContext.ASYNC_CLIENT_FEATURE";
   public static final String SERVICE = "weblogic.wsee.jaxws.framework.jaxrpc.SOAPMessageContext.SERVICE";
   private SOAPMessageContextImpl smc;
   private EnvironmentFactory factory;
   private Packet p;

   public SOAPMessageContext(final EnvironmentFactory var1, WSBinding var2, final Packet var3, Set<String> var4) {
      super(SOAPVersion.SOAP_12.equals(var2.getSOAPVersion()));
      this.smc = new SOAPMessageContextImpl(var2, var3, var4);
      this.factory = var1;
      this.p = var3;
      this.setProperty("weblogic.wsee.jaxws.framework.jaxrpc.SOAPMessageContext.JAX_WS_RUNTIME", "true");
      if (var2.isFeatureEnabled(MTOMFeature.class)) {
         this.setProperty("weblogic.wsee.mtom.enable", "true");
         this.setProperty("weblogic.wsee.mtom.threshold", ((MTOMFeature)var2.getFeature(MTOMFeature.class)).getThreshold());
      }

      if (var2.isFeatureEnabled(AsyncClientTransportFeature.class)) {
         this.setProperty("weblogic.wsee.jaxws.framework.jaxrpc.SOAPMessageContext.ASYNC_CLIENT_FEATURE", var2.getFeature(AsyncClientTransportFeature.class));
      } else if (var2.isFeatureEnabled(McFeature.class)) {
         this.setProperty("weblogic.wsee.jaxws.framework.jaxrpc.SOAPMessageContext.ASYNC_CLIENT_FEATURE", var2.getFeature(McFeature.class));
      }

      if (var1 instanceof ClientEnvironmentFactory) {
         WSService var5 = ((ClientEnvironmentFactory)var1).getWSService();
         this.setProperty("weblogic.wsee.jaxws.framework.jaxrpc.SOAPMessageContext.SERVICE", var5);
      }

      if (var2.isFeatureEnabled(WsdlDefinitionFeature.class)) {
         this.setProperty("oracle.webservices.wsdldefinition", ((WsdlDefinitionFeature)var2.getFeature(WsdlDefinitionFeature.class)).getDefinition());
      }

      this.setDispatcher(new Dispatcher() {
         private Map inParams;
         private Map outParams;

         public WsdlBindingOperation getBindingOperation() {
            return (WsdlBindingOperation)this.getWsdlPort().getBinding().getOperations().get(this.getOperationName());
         }

         public Connection getConnection() {
            return new Connection() {
               public Transport getTransport() {
                  return new Transport() {
                     public void confirmOneway() throws IOException {
                        if (var3.transportBackChannel != null) {
                           var3.transportBackChannel.close();
                        }

                     }

                     public String getEndpointAddress() {
                        throw new UnsupportedOperationException();
                     }

                     public String getName() {
                        throw new UnsupportedOperationException();
                     }

                     public String getServiceURI() {
                        throw new UnsupportedOperationException();
                     }

                     public InputStream receive(MimeHeaders var1x) throws IOException {
                        throw new UnsupportedOperationException();
                     }

                     public OutputStream send(MimeHeaders var1x) throws IOException {
                        throw new UnsupportedOperationException();
                     }
                  };
               }

               public void receive(MessageContext var1x) throws IOException {
                  throw new UnsupportedOperationException();
               }

               public void send(MessageContext var1x) throws IOException {
                  throw new UnsupportedOperationException();
               }

               public void setTransport(Transport var1x) {
                  throw new UnsupportedOperationException();
               }
            };
         }

         public WlMessageContext getContext() {
            return SOAPMessageContext.this;
         }

         public Map getInParams() {
            return this.inParams;
         }

         public WsdlOperation getOperation() {
            return this.getWsMethod() == null ? null : (WsdlOperation)this.getWsdlPort().getPortType().getOperations().get(this.getWsMethod().getOperationName());
         }

         public QName getOperationName() {
            QName var1x = (QName)SOAPMessageContext.this.smc.get("weblogic.wsee.jaxws.framework.jaxrpc.OPERATION_NAME");
            if (var1x == null) {
               Message var2 = var3.getMessage();
               WSDLPort var3x = var1.getPort();
               if (var2 != null && var3x != null) {
                  WSDLBoundOperation var4 = var2.getOperation(var3x);
                  if (var4 != null) {
                     var1x = var4.getName();
                     SOAPMessageContext.this.smc.put("weblogic.wsee.jaxws.framework.jaxrpc.OPERATION_NAME", var1x);
                  }
               }
            }

            return var1x;
         }

         public Map getOutParams() {
            return this.outParams;
         }

         public WsMethod getWsMethod() {
            return this.getOperationName() == null ? null : this.getWsPort().getEndpoint().getMethod(this.getOperationName().getLocalPart());
         }

         public WsPort getWsPort() {
            return var1.getService().getSingletonPort();
         }

         public void setWsPort(WsPort var1x) {
            throw new UnsupportedOperationException();
         }

         public WsdlPort getWsdlPort() {
            return this.getWsPort().getWsdlPort();
         }

         public QName getPortName() {
            return this.getWsdlPort().getName();
         }

         public QName getServiceName() {
            return this.getWsdlPort().getService().getName();
         }

         public boolean isSOAP12() {
            return SOAPMessageContext.this.isSoap12();
         }

         public void setInParams(Map var1x) {
            this.inParams = var1x;
         }

         public void setOutParams(Map var1x) {
            this.outParams = var1x;
         }
      });
   }

   boolean isValid(Packet var1) {
      return this.p == var1;
   }

   protected Map createPropertyMap() {
      return new InvocationPropertyMap();
   }

   public final void updatePacket() {
      this.smc.updatePacket();
      this.msg = null;
   }

   public SOAPMessage getMessage() {
      if (this.msg == null && this.p.getMessage() != null) {
         super.setMessage(this.smc.getMessage());
      }

      return this.msg;
   }

   public void setMessage(SOAPMessage var1) {
      super.setMessage(var1);
      this.smc.setMessage(var1);
   }

   public void setFault(Throwable var1, boolean var2) {
      if (var2) {
         super.setFault(var1);
      } else {
         this.fault = var1;
      }

   }

   public boolean isClient() {
      return this.factory instanceof ClientEnvironmentFactory;
   }

   public WSService getWSService() {
      return this.factory instanceof ClientEnvironmentFactory ? ((ClientEnvironmentFactory)this.factory).getWSService() : null;
   }

   public SoapMsgHeaders getHeaders() {
      this.getMessage();
      return (SoapMsgHeaders)super.getHeaders();
   }

   private class InvocationPropertyMap implements Map<String, Object> {
      private InvocationPropertyMap() {
      }

      public Object get(Object var1) {
         PropertyConverter var2 = (PropertyConverter)SOAPMessageContext.this.factory.getJAXRPCConverters().get(var1);
         return var2 != null ? var2.convertToJAXRPC(SOAPMessageContext.this.p, SOAPMessageContext.this.smc) : SOAPMessageContext.this.smc.get(var1);
      }

      public Object put(String var1, Object var2) {
         PropertyConverter var3 = (PropertyConverter)SOAPMessageContext.this.factory.getJAXRPCConverters().get(var1);
         return var3 != null ? var3.convertToJAXWS(SOAPMessageContext.this.p, SOAPMessageContext.this.smc, var2) : SOAPMessageContext.this.smc.put(var1, var2);
      }

      public boolean containsValue(Object var1) {
         throw new UnsupportedOperationException();
      }

      public Set<Map.Entry<String, Object>> entrySet() {
         return new Set<Map.Entry<String, Object>>() {
            public boolean add(Map.Entry<String, Object> var1) {
               return InvocationPropertyMap.this.put((String)var1.getKey(), var1.getValue()) == null;
            }

            public boolean addAll(Collection<? extends Map.Entry<String, Object>> var1) {
               boolean var2 = false;

               Map.Entry var4;
               for(Iterator var3 = var1.iterator(); var3.hasNext(); var2 = this.add(var4) || var2) {
                  var4 = (Map.Entry)var3.next();
               }

               return var2;
            }

            public void clear() {
               InvocationPropertyMap.this.clear();
            }

            public boolean contains(Object var1) {
               if (!(var1 instanceof Map.Entry)) {
                  return false;
               } else {
                  Map.Entry var2 = (Map.Entry)var1;
                  Object var3 = InvocationPropertyMap.this.get(var2.getKey());
                  return var3 == var1 || var3 != null && var3.equals(var1);
               }
            }

            public boolean containsAll(Collection<?> var1) {
               Iterator var2 = var1.iterator();

               Object var3;
               do {
                  if (!var2.hasNext()) {
                     return true;
                  }

                  var3 = var2.next();
               } while(this.contains(var3));

               return false;
            }

            public boolean isEmpty() {
               return InvocationPropertyMap.this.isEmpty();
            }

            public Iterator<Map.Entry<String, Object>> iterator() {
               return new Iterator<Map.Entry<String, Object>>() {
                  private Iterator<String> it = InvocationPropertyMap.this.keySet().iterator();

                  public boolean hasNext() {
                     return this.it.hasNext();
                  }

                  public Map.Entry<String, Object> next() {
                     return new Map.Entry<String, Object>() {
                        String key;

                        {
                           this.key = (String)it.next();
                        }

                        public String getKey() {
                           return this.key;
                        }

                        public Object getValue() {
                           return InvocationPropertyMap.this.get(this.key);
                        }

                        public Object setValue(Object var1) {
                           return InvocationPropertyMap.this.put(this.key, var1);
                        }
                     };
                  }

                  public void remove() {
                     this.it.remove();
                  }
               };
            }

            public boolean remove(Object var1) {
               if (!(var1 instanceof Map.Entry)) {
                  return false;
               } else {
                  Map.Entry var2 = (Map.Entry)var1;
                  Object var3 = InvocationPropertyMap.this.get(var2.getKey());
                  if (var3 != var1 && (var3 == null || !var3.equals(var1))) {
                     return false;
                  } else {
                     InvocationPropertyMap.this.remove(var2.getKey());
                     return true;
                  }
               }
            }

            public boolean removeAll(Collection<?> var1) {
               boolean var2 = false;

               Object var4;
               for(Iterator var3 = var1.iterator(); var3.hasNext(); var2 = this.remove(var4) || var2) {
                  var4 = var3.next();
               }

               return var2;
            }

            public boolean retainAll(Collection<?> var1) {
               boolean var2 = false;
               Iterator var3 = this.iterator();

               while(var3.hasNext()) {
                  if (!var1.contains(var3.next())) {
                     var3.remove();
                     var2 = true;
                  }
               }

               return var2;
            }

            public int size() {
               return InvocationPropertyMap.this.size();
            }

            public Object[] toArray() {
               Object[] var1 = new Object[this.size()];
               Iterator var2 = this.iterator();

               for(int var3 = 0; var2.hasNext(); ++var3) {
                  var1[var3] = var2.next();
               }

               return var1;
            }

            public <T> T[] toArray(T[] var1) {
               int var2 = this.size();
               if (var1.length < var2) {
                  var1 = (Object[])((Object[])Array.newInstance(var1.getClass().getComponentType(), var2));
               }

               Iterator var3 = this.iterator();
               Object[] var4 = var1;

               for(int var5 = 0; var5 < var2; ++var5) {
                  var4[var5] = var3.next();
               }

               if (var1.length > var2) {
                  var1[var2] = null;
               }

               return var1;
            }
         };
      }

      public Set<String> keySet() {
         Map var1 = SOAPMessageContext.this.factory.getJAXRPCConverters();
         if (var1 != null && !var1.isEmpty()) {
            final Set var2 = SOAPMessageContext.this.smc.keySet();
            if (var2 != null && !var2.isEmpty()) {
               final Set var3 = var1.keySet();
               return new AbstractSet<String>() {
                  public Iterator<String> iterator() {
                     return new CombinedIterator(var2.iterator(), var3.iterator());
                  }

                  public int size() {
                     return var2.size() + var3.size();
                  }
               };
            } else {
               return var1.keySet();
            }
         } else {
            return SOAPMessageContext.this.smc.keySet();
         }
      }

      public void putAll(Map<? extends String, ? extends Object> var1) {
         Iterator var2 = var1.entrySet().iterator();

         while(var2.hasNext()) {
            Map.Entry var3 = (Map.Entry)var2.next();
            this.put((String)var3.getKey(), var3.getValue());
         }

      }

      public Object remove(Object var1) {
         PropertyConverter var2 = (PropertyConverter)SOAPMessageContext.this.factory.getJAXRPCConverters().get(var1);
         if (var2 != null) {
            var2.remove(SOAPMessageContext.this.p, SOAPMessageContext.this.smc);
         }

         return SOAPMessageContext.this.smc.remove(var1);
      }

      public Collection<Object> values() {
         return new AbstractCollection<Object>() {
            public Iterator<Object> iterator() {
               final Iterator var1 = InvocationPropertyMap.this.entrySet().iterator();
               return new Iterator<Object>() {
                  public boolean hasNext() {
                     return var1.hasNext();
                  }

                  public Object next() {
                     return ((Map.Entry)var1.next()).getValue();
                  }

                  public void remove() {
                     var1.remove();
                  }
               };
            }

            public int size() {
               return InvocationPropertyMap.this.size();
            }
         };
      }

      public boolean containsKey(Object var1) {
         PropertyConverter var2 = (PropertyConverter)SOAPMessageContext.this.factory.getJAXRPCConverters().get(var1);
         return var2 != null ? var2.containsKey(SOAPMessageContext.this.p, SOAPMessageContext.this.smc) : SOAPMessageContext.this.smc.containsKey(var1);
      }

      public void clear() {
         SOAPMessageContext.this.smc.clear();
      }

      public boolean isEmpty() {
         return SOAPMessageContext.this.smc.isEmpty();
      }

      public int size() {
         return SOAPMessageContext.this.smc.size();
      }

      // $FF: synthetic method
      InvocationPropertyMap(Object var2) {
         this();
      }
   }
}
