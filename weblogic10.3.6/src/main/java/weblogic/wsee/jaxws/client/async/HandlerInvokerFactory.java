package weblogic.wsee.jaxws.client.async;

import com.sun.istack.Nullable;
import com.sun.xml.bind.api.JAXBRIContext;
import com.sun.xml.ws.api.WSBinding;
import com.sun.xml.ws.api.message.Message;
import com.sun.xml.ws.api.message.Packet;
import com.sun.xml.ws.api.model.SEIModel;
import com.sun.xml.ws.api.model.wsdl.WSDLBoundOperation;
import com.sun.xml.ws.api.pipe.Fiber;
import com.sun.xml.ws.client.AsyncInvoker;
import com.sun.xml.ws.client.AsyncResponseImpl;
import com.sun.xml.ws.client.ResponseContext;
import com.sun.xml.ws.client.Stub;
import com.sun.xml.ws.client.sei.ResponseBuilder;
import com.sun.xml.ws.client.sei.ValueSetter;
import com.sun.xml.ws.client.sei.ValueSetterFactory;
import com.sun.xml.ws.client.sei.ResponseBuilder.AttachmentBuilder;
import com.sun.xml.ws.fault.SOAPFaultBuilder;
import com.sun.xml.ws.model.CheckedExceptionImpl;
import com.sun.xml.ws.model.JavaMethodImpl;
import com.sun.xml.ws.model.ParameterImpl;
import com.sun.xml.ws.model.SOAPSEIModel;
import com.sun.xml.ws.model.WrapperParameter;
import com.sun.xml.ws.server.sei.DispatchException;
import com.sun.xml.ws.server.sei.EndpointMethodDispatcher;
import com.sun.xml.ws.server.sei.EndpointMethodDispatcherGetter;
import com.sun.xml.ws.server.sei.EndpointMethodHandler;
import com.sun.xml.ws.server.sei.EndpointMethodHandlerFactory;
import com.sun.xml.ws.server.sei.Invoker;
import com.sun.xml.ws.server.sei.InvokerSource;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.soap.SOAPBinding.Style;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.Source;
import javax.xml.ws.AsyncHandler;
import javax.xml.ws.Response;
import javax.xml.ws.Service;
import javax.xml.ws.ServiceMode;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.Service.Mode;
import weblogic.jws.jaxws.client.async.AsyncClientHandlerFeature;
import weblogic.wsee.jaxws.framework.WsUtil;

public class HandlerInvokerFactory {
   private static final Logger LOGGER = Logger.getLogger(HandlerInvokerFactory.class.getName());

   private HandlerInvokerFactory() {
   }

   public static HandlerInvoker getInvoker(AsyncClientHandlerFeature var0, WSBinding var1, Stub var2) {
      if (var0.getHandler() instanceof AsyncHandler) {
         return new DispatchHandlerInvokerImpl(var0, var1, var2);
      } else {
         HandlerInvokerImpl var3 = new HandlerInvokerImpl(var0, var1, var2);
         return var3.dispatcherList == null ? null : var3;
      }
   }

   private static class MessageReaderImpl implements MessageReader {
      private MessageReaderImpl() {
      }

      public Object readAsObject(Message var1) {
         return var1;
      }

      // $FF: synthetic method
      MessageReaderImpl(Object var1) {
         this();
      }
   }

   private static class SOAPMessageReaderImpl implements MessageReader {
      private SOAPMessageReaderImpl() {
      }

      public Object readAsObject(Message var1) {
         try {
            return var1.readAsSOAPMessage(Fiber.current().getPacket(), true);
         } catch (SOAPException var3) {
            throw new WebServiceException(var3);
         }
      }

      // $FF: synthetic method
      SOAPMessageReaderImpl(Object var1) {
         this();
      }
   }

   private static class EnvelopeAsSourceReaderImpl implements MessageReader {
      private EnvelopeAsSourceReaderImpl() {
      }

      public Object readAsObject(Message var1) {
         return var1.readEnvelopeAsSource();
      }

      // $FF: synthetic method
      EnvelopeAsSourceReaderImpl(Object var1) {
         this();
      }
   }

   private static class PayloadReaderImpl implements MessageReader {
      private PayloadReaderImpl() {
      }

      public Object readAsObject(Message var1) {
         return var1.readPayloadAsSource();
      }

      // $FF: synthetic method
      PayloadReaderImpl(Object var1) {
         this();
      }
   }

   private interface MessageReader {
      Object readAsObject(Message var1);
   }

   static class AsyncDispatchMethodHandler extends BaseEndpointMethodHandler implements EndpointMethodHandler {
      private AsyncHandler handler;
      private Class dataType;
      private static Method invokeMethod;
      private Service.Mode mode;
      private MessageReader reader;

      protected AsyncDispatchMethodHandler(AsyncHandler var1, Map<QName, CheckedExceptionImpl> var2, WSBinding var3) {
         super(var2, var3);
         this.handler = var1;
         Type var4 = JAXBRIContext.getBaseType(var1.getClass(), AsyncHandler.class);
         if (var4 != null && var4 instanceof ParameterizedType) {
            ParameterizedType var5 = (ParameterizedType)var4;
            Type[] var6 = var5.getActualTypeArguments();
            if (!(var6[0] instanceof Class)) {
               throw new WebServiceException(var1.getClass().getName() + "'s paramterized type '" + var6[0] + "' is not supported");
            } else {
               this.dataType = (Class)var6[0];
               ServiceMode var7 = (ServiceMode)var1.getClass().getAnnotation(ServiceMode.class);
               this.mode = var7 == null ? Mode.PAYLOAD : var7.value();
               if (this.mode == Mode.PAYLOAD) {
                  if (this.dataType == Source.class) {
                     this.reader = new PayloadReaderImpl();
                  }
               } else if (this.dataType == Source.class) {
                  this.reader = new EnvelopeAsSourceReaderImpl();
               } else if (this.dataType == SOAPMessage.class) {
                  this.reader = new SOAPMessageReaderImpl();
               } else {
                  if (this.dataType != Message.class) {
                     throw new WebServiceException("Illeagal combination of service mode '" + this.mode + "' and parameterized type '" + this.dataType + "' on " + var1.getClass().getName());
                  }

                  this.reader = new MessageReaderImpl();
               }

            }
         } else {
            throw new WebServiceException("provider is not parameterized");
         }
      }

      protected Object getResponse(Message var1) throws Exception {
         return this.reader.readAsObject(var1);
      }

      public void handleResponse(Packet var1, Response var2) {
         try {
            invokeMethod.invoke(this.handler, var2);
         } catch (InvocationTargetException var4) {
            throw new WebServiceException(var4);
         } catch (IllegalAccessException var5) {
            throw new WebServiceException(var5);
         }
      }

      static {
         try {
            invokeMethod = AsyncHandler.class.getDeclaredMethod("handleResponse", Response.class);
         } catch (NoSuchMethodException var1) {
            throw new WebServiceException(var1);
         }
      }
   }

   static class AsyncEndpointMethodHandler extends BaseEndpointMethodHandler implements EndpointMethodHandler {
      private final ResponseBuilder responseBuilder;
      @Nullable
      private final Class asyncBeanClass;
      private InvokerSource owner;
      private WSBinding binding;
      private Method method;

      public AsyncEndpointMethodHandler(InvokerSource var1, JavaMethodImpl var2, JavaMethodImpl var3, WSBinding var4, EndpointMethodDispatcherGetter var5) {
         super(new HashMap(), var4);
         this.owner = var1;
         this.binding = var4;
         this.method = var5.getHandlerMethod(var2);
         Iterator var6 = var2.getCheckedExceptions().iterator();

         while(var6.hasNext()) {
            CheckedExceptionImpl var7 = (CheckedExceptionImpl)var6.next();
            this.checkedExceptions.put(var7.getBridge().getTypeReference().tagName, var7);
         }

         List var14 = var3.getResponseParameters();
         int var15 = 0;
         Iterator var8 = var14.iterator();

         while(var8.hasNext()) {
            ParameterImpl var9 = (ParameterImpl)var8.next();
            if (var9.isWrapperStyle()) {
               WrapperParameter var10 = (WrapperParameter)var9;
               var15 += var10.getWrapperChildren().size();
               if (var3.getBinding().getStyle() == Style.DOCUMENT) {
                  var15 += 2;
               }
            } else {
               ++var15;
            }
         }

         Class var16 = null;
         if (var15 > 1) {
            var14 = var2.getResponseParameters();
            Iterator var17 = var14.iterator();

            label60:
            do {
               ParameterImpl var18;
               do {
                  if (!var17.hasNext()) {
                     break label60;
                  }

                  var18 = (ParameterImpl)var17.next();
                  if (var18.isWrapperStyle()) {
                     WrapperParameter var11 = (WrapperParameter)var18;
                     if (var3.getBinding().getStyle() == Style.DOCUMENT) {
                        var16 = (Class)var11.getTypeReference().type;
                        break label60;
                     }

                     Iterator var12 = var11.getWrapperChildren().iterator();

                     while(var12.hasNext()) {
                        ParameterImpl var13 = (ParameterImpl)var12.next();
                        if (var13.getIndex() == -1) {
                           var16 = (Class)var13.getTypeReference().type;
                           continue label60;
                        }
                     }
                     continue label60;
                  }
               } while(var18.getIndex() != -1);

               var16 = (Class)var18.getTypeReference().type;
               break;
            } while(var16 == null);
         }

         this.asyncBeanClass = var16;
         switch (var15) {
            case 0:
               this.responseBuilder = this.buildResponseBuilder(var3, ValueSetterFactory.NONE);
               break;
            case 1:
               this.responseBuilder = this.buildResponseBuilder(var3, ValueSetterFactory.SINGLE);
               break;
            default:
               this.responseBuilder = this.buildResponseBuilder(var3, new ValueSetterFactory.AsyncBeanValueSetterFactory(this.asyncBeanClass));
         }

      }

      ResponseBuilder buildResponseBuilder(JavaMethodImpl var1, ValueSetterFactory var2) {
         List var3 = var1.getResponseParameters();
         ArrayList var4 = new ArrayList();
         Iterator var5 = var3.iterator();

         while(var5.hasNext()) {
            ParameterImpl var6 = (ParameterImpl)var5.next();
            ValueSetter var7;
            switch (var6.getOutBinding().kind) {
               case BODY:
                  if (var6.isWrapperStyle()) {
                     if (var6.getParent().getBinding().isRpcLit()) {
                        var4.add(new ResponseBuilder.RpcLit((WrapperParameter)var6, var2));
                     } else {
                        var4.add(new ResponseBuilder.DocLit((WrapperParameter)var6, var2));
                     }
                  } else {
                     var7 = var2.get(var6);
                     var4.add(new ResponseBuilder.Body(var6.getBridge(), var7));
                  }
                  break;
               case HEADER:
                  var7 = var2.get(var6);
                  var4.add(new ResponseBuilder.Header(this.binding.getSOAPVersion(), var6, var7));
                  break;
               case ATTACHMENT:
                  var7 = var2.get(var6);
                  var4.add(AttachmentBuilder.createAttachmentBuilder(var6, var7));
                  break;
               case UNBOUND:
                  var7 = var2.get(var6);
                  var4.add(new ResponseBuilder.NullSetter(var7, ResponseBuilder.getVMUninitializedValue(var6.getTypeReference().type)));
                  break;
               default:
                  throw new AssertionError();
            }
         }

         Object var8;
         switch (var4.size()) {
            case 0:
               var8 = ResponseBuilder.NONE;
               break;
            case 1:
               var8 = (ResponseBuilder)var4.get(0);
               break;
            default:
               var8 = new ResponseBuilder.Composite(var4);
         }

         return (ResponseBuilder)var8;
      }

      protected Object getResponse(Message var1) throws Exception {
         Object[] var2 = new Object[1];
         if (this.asyncBeanClass != null) {
            var2[0] = this.asyncBeanClass.newInstance();
         }

         this.responseBuilder.readResponse(var1, var2);
         return var2[0];
      }

      public void handleResponse(Packet var1, Response var2) {
         try {
            this.owner.getInvoker(var1).invoke(var1, this.method, new Object[]{var2});
         } catch (InvocationTargetException var4) {
            throw new WebServiceException(var4);
         } catch (IllegalAccessException var5) {
            throw new WebServiceException(var5);
         }
      }
   }

   private abstract static class BaseEndpointMethodHandler implements EndpointMethodHandler {
      protected final Map<QName, CheckedExceptionImpl> checkedExceptions;
      private WSBinding binding;

      protected BaseEndpointMethodHandler(Map<QName, CheckedExceptionImpl> var1, WSBinding var2) {
         this.checkedExceptions = var1;
         this.binding = var2;
      }

      protected abstract Object getResponse(Message var1) throws Exception;

      public abstract void handleResponse(Packet var1, Response var2);

      public Packet invoke(Throwable var1) {
         Packet var2 = Fiber.current().getPacket();
         return this.invoke(var2, var1);
      }

      public Packet invoke(Packet var1) {
         return this.invoke(var1, (Throwable)null);
      }

      public Packet invoke(final Packet var1, final Throwable var2) {
         AsyncInvoker var3 = new AsyncInvoker() {
            public void do_run() {
               this.responseImpl.setResponseContext(new ResponseContext(var1));
               Message var1x = var1.getMessage();
               if (var1x != null) {
                  if (HandlerInvokerFactory.LOGGER.isLoggable(Level.FINE)) {
                     String var2x = var1.getMessage().getHeaders().getMessageID(BaseEndpointMethodHandler.this.binding.getAddressingVersion(), BaseEndpointMethodHandler.this.binding.getSOAPVersion());
                     String var3 = AsyncTransportProvider.dumpPersistentContextContextProps(var1.persistentContext);
                     String var4 = AsyncTransportProvider.dumpPersistentContextContextProps(this.responseImpl.getContext());
                     HandlerInvokerFactory.LOGGER.fine("In AsyncEndpointMethodHandler for msg " + var2x + " and Packet.persistentContext contents '" + var3 + "'. Set new ResponseContext contents: " + var4);
                  }

                  Throwable var6 = var2;
                  if (var6 == null) {
                     try {
                        if (var1x.isFault()) {
                           SOAPFaultBuilder var7 = SOAPFaultBuilder.create(var1x);
                           throw var7.createException(BaseEndpointMethodHandler.this.checkedExceptions);
                        }

                        this.responseImpl.set(BaseEndpointMethodHandler.this.getResponse(var1x), (Throwable)null);
                     } catch (Throwable var5) {
                        var6 = var5;
                     }
                  }

                  if (var6 != null) {
                     if (var6 instanceof RuntimeException) {
                        if (var2 instanceof WebServiceException) {
                           this.responseImpl.set((Object)null, var6);
                           return;
                        }
                     } else if (var6 instanceof Exception) {
                        this.responseImpl.set((Object)null, var6);
                        return;
                     }

                     this.responseImpl.set((Object)null, new WebServiceException(var6));
                  }

               }
            }
         };
         AsyncResponseImpl var4 = new AsyncResponseImpl(var3, new AsyncHandler() {
            public void handleResponse(Response var1x) {
               BaseEndpointMethodHandler.this.handleResponse(var1, var1x);
            }
         });
         var3.setReceiver(var4);
         var3.run();
         var1.setMessage((Message)null);
         return var1;
      }
   }

   static class AsyncEndpointMethodHandlerFactory extends EndpointMethodHandlerFactory {
      private Map<WSDLBoundOperation, JavaMethodImpl> syncs = new HashMap();

      public AsyncEndpointMethodHandlerFactory(SEIModel var1) {
         Iterator var2 = ((SOAPSEIModel)var1).getJavaMethods().iterator();

         while(var2.hasNext()) {
            JavaMethodImpl var3 = (JavaMethodImpl)var2.next();
            if (!var3.getMEP().isAsync) {
               this.syncs.put(var3.getOperation(), var3);
            }
         }

      }

      public EndpointMethodHandler create(InvokerSource var1, JavaMethodImpl var2, WSBinding var3, EndpointMethodDispatcherGetter var4) {
         JavaMethodImpl var5 = (JavaMethodImpl)this.syncs.get(var2.getOperation());
         return new AsyncEndpointMethodHandler(var1, var2, var5, var3, var4);
      }
   }

   private static class HandlerInvokerImpl implements HandlerInvoker {
      private final List<EndpointMethodDispatcher> dispatcherList;
      private WSBinding binding;
      private Stub stub;

      private HandlerInvokerImpl(final AsyncClientHandlerFeature var1, WSBinding var2, final Stub var3) {
         this.binding = var2;
         this.stub = var3;
         SEIModel var4 = var3.getRuntimeModel();
         if (var4 == null) {
            this.dispatcherList = null;
         } else {
            InvokerSource var5 = new InvokerSource() {
               public Invoker getInvoker(Packet var1x) {
                  return new Invoker() {
                     public Object invoke(Packet var1x, Method var2, Object... var3) throws InvocationTargetException, IllegalAccessException {
                        try {
                           Object var4 = var1.getHandler();
                           if (var4 == null) {
                              throw new NullPointerException("AsyncClientHandlerFeature contained a null handler impl");
                           } else {
                              return var2.invoke(var4, var3);
                           }
                        } catch (Throwable var5) {
                           var5.printStackTrace();
                           throw new WebServiceException(var5);
                        }
                     }
                  };
               }
            };
            EndpointMethodDispatcherGetter var6 = new EndpointMethodDispatcherGetter(var4, var2, var5, true, new AsyncEndpointMethodHandlerFactory(var4)) {
               public Method getHandlerMethod(JavaMethodImpl var1) {
                  try {
                     Method var2 = var1.getMethod();
                     String var3x = var2.getName();
                     String var4 = "on" + var3x.substring(0, 1).toUpperCase(Locale.ENGLISH) + var3x.substring(1, var3x.length() - 5) + "Response";
                     Class var5 = var3.getPortInterface();
                     Class var6 = Class.forName(var5.getName() + "AsyncHandler", true, var5.getClassLoader());
                     return var6.getMethod(var4, Response.class);
                  } catch (NoSuchMethodException var7) {
                     throw new WebServiceException(var7);
                  } catch (ClassNotFoundException var8) {
                     throw new WebServiceException(var8);
                  }
               }
            };
            this.dispatcherList = var6.getDispatcherList();
         }
      }

      private EndpointMethodHandler findHandler(Packet var1) throws DispatchException {
         Iterator var2 = this.dispatcherList.iterator();

         EndpointMethodHandler var4;
         do {
            if (!var2.hasNext()) {
               return null;
            }

            EndpointMethodDispatcher var3 = (EndpointMethodDispatcher)var2.next();
            var4 = var3.getEndpointMethodHandler(var1);
         } while(var4 == null);

         return var4;
      }

      public Packet createNoHandlerError(Packet var1) {
         String var2 = MessageFormat.format("Request=[SOAPAction={0},Payload='{'{1}'}'{2}]", var1.soapAction, var1.getMessage().getPayloadNamespaceURI(), var1.getMessage().getPayloadLocalPart());
         String var3 = "Dispatch cannot find method: " + var2;
         Message var4 = SOAPFaultBuilder.createSOAPFaultMessage(this.binding.getSOAPVersion(), var3, this.binding.getSOAPVersion().faultCodeClient);
         SEIModel var5 = this.stub.getRuntimeModel();
         return var1.createServerResponse(var4, var5 != null ? var5.getPort() : null, (SEIModel)null, this.binding);
      }

      public Packet handleResponse(Packet var1) {
         EndpointMethodHandler var2;
         try {
            var2 = this.findHandler(var1);
         } catch (DispatchException var4) {
            if (var1.getMessage() != null && var1.getMessage().isFault()) {
               return var1;
            }

            return var1.createServerResponse(var4.fault, this.stub.getRuntimeModel().getPort(), (SEIModel)null, this.binding);
         }

         if (var2 != null) {
            Packet var3 = var2.invoke(var1);
            return var3;
         } else {
            return this.createNoHandlerError(var1);
         }
      }

      public Packet handleException(Throwable var1) {
         Packet var2 = Fiber.current().getPacket();

         EndpointMethodHandler var3;
         try {
            var3 = this.findHandler(var2);
         } catch (DispatchException var5) {
            if (!WsUtil.isPermanentSendFailure(var1) && !WsUtil.hasRootCause(var1, IOException.class)) {
               return var2.createServerResponse(var5.fault, this.stub.getRuntimeModel().getPort(), (SEIModel)null, this.binding);
            }

            return null;
         }

         if (var3 != null) {
            Packet var4 = var3.invoke(var1);
            return var4;
         } else {
            return this.createNoHandlerError(var2);
         }
      }

      // $FF: synthetic method
      HandlerInvokerImpl(AsyncClientHandlerFeature var1, WSBinding var2, Stub var3, Object var4) {
         this(var1, var2, var3);
      }
   }

   private static class DispatchHandlerInvokerImpl implements HandlerInvoker {
      AsyncDispatchMethodHandler handler;

      private DispatchHandlerInvokerImpl(AsyncClientHandlerFeature var1, WSBinding var2, Stub var3) {
         this.handler = new AsyncDispatchMethodHandler((AsyncHandler)var1.getHandler(), (Map)null, var2);
      }

      public Packet handleResponse(Packet var1) {
         return this.handler.invoke(var1);
      }

      public Packet handleException(Throwable var1) {
         Packet var2 = Fiber.current().getPacket();
         return this.handler.invoke(var2, var1);
      }

      // $FF: synthetic method
      DispatchHandlerInvokerImpl(AsyncClientHandlerFeature var1, WSBinding var2, Stub var3, Object var4) {
         this(var1, var2, var3);
      }
   }
}
