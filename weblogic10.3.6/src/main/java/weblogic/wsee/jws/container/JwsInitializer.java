package weblogic.wsee.jws.container;

import com.bea.util.jam.JClass;
import com.bea.util.jam.JamClassLoader;
import com.bea.util.jam.JamServiceFactory;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import javax.xml.namespace.QName;
import javax.xml.rpc.JAXRPCException;
import javax.xml.rpc.ServiceException;
import javax.xml.rpc.Stub;
import weblogic.application.utils.ApplicationVersionUtils;
import weblogic.jws.AsyncFailure;
import weblogic.jws.AsyncResponse;
import weblogic.jws.CallbackMethod;
import weblogic.jws.ReliabilityErrorHandler;
import weblogic.jws.ServiceClient;
import weblogic.jws.security.CallbackRolesAllowed;
import weblogic.jws.wlw.UseWLW81BindingTypes;
import weblogic.protocol.LocalServerIdentity;
import weblogic.wsee.addressing.EndpointReference;
import weblogic.wsee.async.AsyncUtil;
import weblogic.wsee.callback.CallbackInfoHeader;
import weblogic.wsee.callback.CallbackStubImpl;
import weblogic.wsee.callback.CallbackUtils;
import weblogic.wsee.callback.Wlw81CallbackHeader;
import weblogic.wsee.conversation.ContinueHeader;
import weblogic.wsee.conversation.ConversationUtils;
import weblogic.wsee.conversation.StartHeader;
import weblogic.wsee.jaxrpc.ServiceImpl;
import weblogic.wsee.message.FreeStandingMsgHeaders;
import weblogic.wsee.message.MsgHeaders;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.reliability.ReliabilityErrorContext;
import weblogic.wsee.server.ServerUtil;
import weblogic.wsee.util.ClassUtil;
import weblogic.wsee.util.Guid;
import weblogic.wsee.util.JmsUtil;
import weblogic.wsee.util.StringUtil;
import weblogic.wsee.util.Verbose;
import weblogic.xml.schema.binding.internal.NameUtil;

class JwsInitializer {
   private final Object targetJWS;
   private final WlMessageContext initialMessageContext;
   private Container container = null;
   private static final boolean verbose = Verbose.isVerbose(JwsInitializer.class);
   private static final String WSDL_LOCATION = "weblogic.wsee.wsdl.location";
   private static final String SERVICE_CLASS_NAME = "weblogic.wsee.service.class.name";
   private static final String SERVICE_METHOD_NAME = "weblogic.wsee.service.method.name";

   JwsInitializer(Object var1, WlMessageContext var2, Container var3) {
      this.targetJWS = var1;
      this.initialMessageContext = var2;
      this.container = var3;
   }

   void initialize() {
      this.initializeFields();
      this.initializeStubs();
      if (this.isConversational()) {
         this.initializeConversationalStubs();
      }

   }

   private void initializeFields() {
      Field[] var1 = this.targetJWS.getClass().getDeclaredFields();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         Field var4 = var1[var3];
         if (FieldHelper.isCallback(var4) && var4.getType().isInterface()) {
            Object var5 = this.getInitialValueForCallback(var4);
            if (var5 != null) {
               FieldHelper.setFieldValue(var4, this.targetJWS, var5);
            }
         }
      }

   }

   private void initializeStubs() {
      Field[] var1 = this.targetJWS.getClass().getDeclaredFields();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         Field var4 = var1[var3];
         if (FieldHelper.isStub(var4)) {
            if (!var4.getType().isInterface()) {
               throw new JAXRPCException("ServiceClient annotation should only be used on an interface");
            }

            Object var5 = this.getInitialValueForStub(var4);
            if (var5 != null) {
               FieldHelper.setFieldValue(var4, this.targetJWS, var5);
            }
         }
      }

   }

   private Object getInitialValueForStub(Field var1) {
      try {
         Class var2 = var1.getType();
         String var3 = var1.getName();
         this.logInjectionStart(var2, "stub");
         ServiceClient var4 = (ServiceClient)var1.getAnnotation(ServiceClient.class);

         assert var4 != null : "ServiceClient annotation not found";

         String var5 = var4.wsdlLocation();
         String var6 = this.getServiceName(var2, var4.serviceName());
         Class var7 = null;

         try {
            logMessage("Loading class " + var6 + " wsdlLocaltion = " + var5);
            var7 = var2.getClassLoader().loadClass(var6);
         } catch (ClassNotFoundException var14) {
            var6 = this.getServiceName(var2, var4.serviceName() + "_Service");
            logMessage("Loading class " + var6 + " wsdlLocaltion = " + var5);
            var7 = var2.getClassLoader().loadClass(var6);
         }

         ServiceImpl var8 = this.createServiceInstance(var5, var7);
         if (var8 == null) {
            return null;
         } else {
            logMessage("ServiceInstance is " + var8.getClass().getName());
            String var9 = this.getPortName(var8, var4.portName());
            Method var10 = var7.getMethod("get" + NameUtil.getJAXRPCClassName(var9));
            logMessage("Invoking method " + var10);
            Stub var11 = (Stub)var10.invoke(var8);
            String var12 = var8._getPortTransport(var9);
            boolean var13 = "SOAP12".equals(var8._getPortBindingType(var9));
            this.setupStubProperties(var5, var11, var6, var10, var4.endpointAddress(), var8, var9);
            this.setupAsyncProperties(var11, var3, var12, var13);
            this.setupConversationStyle(var8, var11);
            if (var8.hasCallback()) {
               this.setupCallback(var11, var3, var8, var12, var9);
            } else if (var8.has81Callback()) {
               this.setup81Callback(var11, var3, var8, var12, var9);
               this.setupCallback(var11, var3, var8, var12, var9);
            }

            this.logInjectionEnd();
            return var11;
         }
      } catch (Exception var15) {
         if (verbose) {
            Verbose.logException(var15);
         }

         throw new InvokeException("Unable to Construct Stub", var15);
      }
   }

   private void setupConversationStyle(ServiceImpl var1, Stub var2) {
      if (var1._has81Conversation()) {
         ConversationUtils.setConversationVersionOne(var2);
      }

   }

   private void setupStubProperties(String var1, Stub var2, String var3, Method var4, String var5, ServiceImpl var6, String var7) {
      if (var1.length() > 0) {
         var2._setProperty("weblogic.wsee.wsdl.location", var1);
      }

      var2._setProperty("weblogic.wsee.service.class.name", var3);
      var2._setProperty("weblogic.wsee.service.method.name", var4.getName());
      if (var5.length() > 0) {
         var2._setProperty("javax.xml.rpc.service.endpoint.address", var5);
      } else if (var1.length() > 0) {
         var2._setProperty("javax.xml.rpc.service.endpoint.address", var6._getPortLocation(var7));
      }

      var2._setProperty("weblogic.wsee.invoke_properties", new HashMap());
      var2._setProperty("weblogic.wsee.complex", "true");
   }

   private ServiceImpl createServiceInstance(String var1, Class var2) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
      Object var3;
      Constructor var4;
      if (var1.length() == 0) {
         var4 = var2.getConstructor();
         var3 = var4.newInstance();
      } else {
         var4 = var2.getConstructor(String.class);
         var3 = var4.newInstance(var1);
      }

      return var3 instanceof ServiceImpl ? (ServiceImpl)var3 : null;
   }

   private String getPortName(ServiceImpl var1, String var2) throws ServiceException {
      String var3 = null;
      Iterator var4 = var1.getPorts();

      while(var4.hasNext()) {
         QName var5 = (QName)var4.next();
         if (!StringUtil.isEmpty(var2)) {
            if (var2.equals(var5.getLocalPart())) {
               return var5.getLocalPart();
            }
         } else {
            if (var3 != null) {
               throw new JAXRPCException("there are multiple ports, portname is required in annotation");
            }

            var3 = var5.getLocalPart();
         }
      }

      if (var3 == null) {
         throw new JAXRPCException("No suitable port found");
      } else {
         return var3;
      }
   }

   private void setupCallback(Stub var1, String var2, ServiceImpl var3, String var4, String var5) {
      EndpointReference var6 = new EndpointReference(this.getCallbackAddress(var3, var4, var5));
      FreeStandingMsgHeaders var7 = new FreeStandingMsgHeaders();
      var7.addHeader(this.buildCallbackInfoHeader(var2));
      var6.setReferenceParameters(var7);
      var1._setProperty("weblogic.wsee.addressing.CallbackTo", var6);
   }

   private void setup81Callback(Stub var1, String var2, ServiceImpl var3, String var4, String var5) {
      String var6 = this.getCallbackAddress(var3, var4, var5);
      if ("jms".equals(var4)) {
         var6 = JmsUtil.wls9UriToWlw81(var6);
      }

      CallbackInfoHeader var7 = this.buildCallbackInfoHeader(var2);
      StringBuffer var8 = new StringBuffer();
      String var9;
      if (this.isConversational()) {
         ContinueHeader var10 = new ContinueHeader(this.getConversationId(), LocalServerIdentity.getIdentity().getServerName());
         var9 = "[" + var10.convertToWlw81StringForm() + "]";
      } else {
         var9 = "[]";
      }

      var8.append(var9);
      var8.append(var7.convertToWlw81StringForm());
      var8.append(":");
      var8.append(Guid.generateGuidStandardChar());
      var1._setProperty("weblogic.wsee.conversation.ConversationId", var8.toString());
      var1._setProperty("weblogic.wsee.callback.loc", var6);
   }

   private CallbackInfoHeader buildCallbackInfoHeader(String var1) {
      CallbackInfoHeader var2 = new CallbackInfoHeader();
      var2.setServiceURI(this.initialMessageContext.getDispatcher().getConnection().getTransport().getServiceURI());
      if (this.container instanceof ConversationalContainer) {
         String var3 = ApplicationVersionUtils.getCurrentVersionId();
         if (var3 != null) {
            var2.setAppVersion(var3);
            if (verbose) {
               Verbose.log((Object)("conversational callback endpoint version = " + var3));
            }
         } else if (verbose) {
            Verbose.log((Object)"conversation callback endpoint is not versioned");
         }
      } else if (verbose) {
         Verbose.log((Object)"non-conversational callback - no versioning");
      }

      Method[] var11 = this.targetJWS.getClass().getDeclaredMethods();
      int var4 = var11.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Method var6 = var11[var5];
         CallbackMethod var7 = (CallbackMethod)var6.getAnnotation(CallbackMethod.class);
         if (var7 != null && var1.equals(var7.target())) {
            var2.setStubName(var1);
            CallbackRolesAllowed var8 = (CallbackRolesAllowed)var6.getAnnotation(CallbackRolesAllowed.class);
            if (var8 != null) {
               var2.setRoleRequired(true);
               break;
            }

            try {
               Field var9 = this.targetJWS.getClass().getDeclaredField(var1);
               var8 = (CallbackRolesAllowed)var9.getAnnotation(CallbackRolesAllowed.class);
               if (var8 != null) {
                  var2.setRoleRequired(true);
                  break;
               }
            } catch (NoSuchFieldException var10) {
               throw new JAXRPCException(var10);
            }
         }
      }

      return var2;
   }

   private String getCallbackAddress(ServiceImpl var1, String var2, String var3) {
      String var4 = (String)this.initialMessageContext.getProperty("weblogic.wsee.context_path");
      if (var4 == null) {
         throw new JAXRPCException("Can't find the current context path");
      } else {
         QName var5 = var1.getCallbackServiceName();
         String var6 = CallbackUtils.getCallbackPortName(var3);
         StringBuilder var7 = new StringBuilder();
         var7.append(ServerUtil.getServerURL(var2));
         if (!var4.startsWith("/")) {
            var7.append('/');
         }

         var7.append(var4);
         var7.append(CallbackUtils.getServiceUri(var5, var6));
         return !var2.equalsIgnoreCase("jms") ? var7.toString() : this.getJMSUrl(var7.toString(), ServerUtil.getCallbackQueueInfo().getQueueName());
      }
   }

   private String getJMSUrl(String var1, String var2) {
      return var1 + "?URI=" + var2 + "&FACTORY=" + ServerUtil.getJmsConnectionFactory();
   }

   private String getServiceName(Class var1, String var2) {
      String var3 = var1.getName();
      var2 = NameUtil.getJAXRPCClassName(var2);
      return var3.substring(0, var3.lastIndexOf(".") + 1) + var2 + "_Impl";
   }

   private void setupAsyncProperties(Stub var1, String var2, String var3, boolean var4) {
      HashMap var5 = new HashMap();
      HashMap var6 = new HashMap();
      Method[] var7 = this.targetJWS.getClass().getDeclaredMethods();
      int var8 = var7.length;

      for(int var9 = 0; var9 < var8; ++var9) {
         Method var10 = var7[var9];
         AsyncResponse var11 = (AsyncResponse)var10.getAnnotation(AsyncResponse.class);
         if (var11 != null) {
            if (var2.equals(var11.target())) {
               var5.put(var11.operation(), var10.getName());
            }
         } else {
            AsyncFailure var12 = (AsyncFailure)var10.getAnnotation(AsyncFailure.class);
            if (var12 != null) {
               if (var2.equals(var12.target())) {
                  var6.put(var12.operation(), var10.getName());
               }
            } else {
               ReliabilityErrorHandler var13 = (ReliabilityErrorHandler)var10.getAnnotation(ReliabilityErrorHandler.class);
               if (var13 != null && var2.equals(var13.target())) {
                  Class[] var14 = var10.getParameterTypes();
                  if (var14.length != 1 || var14[0] != ReliabilityErrorContext.class) {
                     throw new JAXRPCException("Error handler method must have a single parameter of type '" + ReliabilityErrorContext.class.getName() + "'");
                  }

                  var1._setProperty("weblogic.wsee.reliabile.errorhandler", var10.getName());
               }
            }
         }
      }

      if (var5.size() > 0) {
         var1._setProperty("weblogic.wsee.async.response.map", var5);
      }

      if (var6.size() > 0) {
         var1._setProperty("weblogic.wsee.async.failure.map", var6);
      }

      EndpointReference var15 = AsyncUtil.getDefaultAsyncResponseServiceEPR(var3, var4);
      var1._setProperty("weblogic.wsee.async.res.epr", var15);
      var1._setProperty("weblogic.wsee.stub.name", var2);
      var1._setProperty("weblogic.wsee.enclosing.classname", this.targetJWS.getClass().getName());
      var1._setProperty("weblogic.wsee.enclosing.jws.serviceuri", this.initialMessageContext.getDispatcher().getConnection().getTransport().getServiceURI());
   }

   private Object getInitialValueForCallback(Field var1) {
      try {
         Class var2 = var1.getType();
         this.logInjectionStart(var2, "callback");
         Class var3 = this.loadServiceClass(var2);
         Constructor var4 = var3.getConstructor();
         Object var5 = var4.newInstance();
         Method var6 = var3.getMethod(this.getCallbackPortMethodName());
         logMessage("Invoking method " + var6);
         Stub var7 = (Stub)var6.invoke(var5);
         this.setupCallbackEndpoint(var7);
         var7._setProperty("weblogic.wsee.complex", "true");
         var7._setProperty("weblogic.wsee.enclosing.jws.contextpath", this.initialMessageContext.getProperty("weblogic.wsee.context_path"));
         var7._setProperty("weblogic.wsee.enclosing.jws.servicename", this.initialMessageContext.getProperty("weblogic.wsee.service_name"));
         var7._setProperty("weblogic.wsee.enclosing.classname", this.targetJWS.getClass().getName());
         var7._setProperty("weblogic.wsee.enclosing.jws.serviceuri", this.initialMessageContext.getDispatcher().getConnection().getTransport().getServiceURI());
         if (this.targetJWS.getClass().getAnnotation(UseWLW81BindingTypes.class) != null) {
            var7._setProperty("weblogic.wsee.usewlw81bindingtypes", "true");
         }

         if (this.container != null && this.container instanceof ConversationalContainer) {
            var7._setProperty("weblogic.wsee.enclosing.container", this.container);
         }

         var7 = CallbackStubImpl.newInstance(var7, var2);
         this.logInjectionEnd();
         return var7;
      } catch (Exception var8) {
         if (verbose) {
            Verbose.logException(var8);
         }

         throw new InvokeException("Unable to Construct Stub", var8);
      }
   }

   private void setupCallbackEndpoint(Stub var1) {
      EndpointReference var2 = (EndpointReference)this.initialMessageContext.getProperty("weblogic.wsee.addressing.CallbackTo");
      if (var2 == null) {
         MsgHeaders var3 = this.initialMessageContext.getHeaders();

         assert var3 != null;

         StartHeader var4;
         if ((var4 = (StartHeader)var3.getHeader(StartHeader.TYPE)) != null) {
            String var5 = var4.getCallbackLocation();
            if (var5 != null) {
               String var6 = var4.getConversationId();
               var2 = new EndpointReference(var5);
               var2.getReferenceParameters().addHeader(new Wlw81CallbackHeader(var6));
            }
         }
      }

      if (var2 != null) {
         var1._setProperty("weblogic.wsee.addressing.Target", var2);
      }

   }

   private Class loadServiceClass(Class var1) throws ClassNotFoundException {
      String var2 = this.getCallbackServiceClassName(var1);
      logMessage("Loading class " + var2);
      Class var3 = var1.getClassLoader().loadClass(var2);
      return var3;
   }

   private String getCallbackServiceClassName(Class var1) {
      String var2 = var1.getPackage().getName() + ".callbackclient.";
      String var3 = this.initialMessageContext.getDispatcher().getWsdlPort().getService().getName().getLocalPart();
      String var4 = ClassUtil.getServiceName(this.loadJClass(var1), var3);
      String var5 = NameUtil.getJAXRPCClassName(var4) + "_Impl";
      return var2 + var5;
   }

   private JClass loadJClass(Class var1) {
      JamClassLoader var2 = JamServiceFactory.getInstance().createJamClassLoader(Thread.currentThread().getContextClassLoader());
      return var2.loadClass(var1.getName());
   }

   private String getCallbackPortMethodName() {
      String var1 = CallbackUtils.getCallbackPortName(this.initialMessageContext.getDispatcher().getWsdlPort().getName().getLocalPart());
      return "get" + NameUtil.getJAXRPCClassName(var1);
   }

   private boolean isConversational() {
      return !StringUtil.isEmpty(this.getConversationId());
   }

   private String getConversationId() {
      return (String)this.initialMessageContext.getProperty("weblogic.wsee.conversation.ConversationId");
   }

   private void initializeConversationalStubs() {
      String var1 = this.getConversationId();
      Field[] var2 = this.targetJWS.getClass().getDeclaredFields();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Field var5 = var2[var4];
         if (FieldHelper.isStub(var5)) {
            Object var6 = FieldHelper.getFieldValue(var5, this.targetJWS);
            if (var6 != null) {
               Stub var7 = (Stub)var6;
               this.setupContinueHeader(var7, "weblogic.wsee.addressing.CallbackTo", var1);
               this.setupContinueHeader(var7, "weblogic.wsee.async.res.epr", var1);
            }
         }
      }

   }

   private void setupContinueHeader(Stub var1, String var2, String var3) {
      EndpointReference var4 = (EndpointReference)var1._getProperty(var2);
      if (var4 != null) {
         var4.getReferenceParameters().addHeader(new ContinueHeader(var3, LocalServerIdentity.getIdentity().getServerName()));
      }

   }

   private void logInjectionStart(Class var1, String var2) {
      if (verbose) {
         Verbose.getOut().println("-------------------------");
         Verbose.log((Object)("Dependency injection for " + var2));
         Verbose.log((Object)("Instance class " + var1.getName() + " " + var1.isPrimitive() + " " + var1.isInterface()));
      }

   }

   private void logInjectionEnd() {
      if (verbose) {
         Verbose.getOut().println("-------------------------");
      }

   }

   private static void logMessage(String var0) {
      if (verbose) {
         Verbose.log((Object)var0);
      }

   }
}
