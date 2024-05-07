package weblogic.wsee.jws.container;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.security.Principal;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.xml.rpc.ServiceException;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.server.ServiceLifecycle;
import javax.xml.rpc.server.ServletEndpointContext;
import org.w3c.dom.Element;
import weblogic.jws.Context;
import weblogic.jws.ServiceClient;
import weblogic.utils.collections.WeakConcurrentHashMap;
import weblogic.wsee.conversation.ConversationUtils;
import weblogic.wsee.jws.JwsContext;
import weblogic.wsee.jws.Protocol;
import weblogic.wsee.jws.ServiceHandle;
import weblogic.wsee.jws.ServiceHandleImpl;
import weblogic.wsee.jws.context.JwsSecurityContext;
import weblogic.wsee.jws.context.WebSecurityContext;
import weblogic.wsee.jws.util.ApplicationLogger;
import weblogic.wsee.jws.util.Logger;
import weblogic.wsee.message.FilteredMessageContext;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.message.soap.SoapMessageContext;
import weblogic.wsee.util.ControlAPIUtil;
import weblogic.wsee.util.HeaderUtil;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.ws.WsPort;
import weblogic.wsee.ws.dispatch.server.ServletEndpointContextImpl;

public class Container implements JwsContext, ContainerMarker {
   private static final long serialVersionUID = 557998558456986952L;
   private static final String CONTROL_ANNOTATION = "org.apache.beehive.controls.api.bean.Control";
   private static final String CONTEXT_ANNOTATION = "weblogic.controls.jws.Common.Context";
   private static Set<String> complexAnnotationSet = null;
   private static Map<Class, WeakReference<Class>> controlClientInitClassCache = new WeakConcurrentHashMap();
   private static final String UNDERSTOOD_HEADERS = "weblogic.wsee.addressing.UnderstoodHeaders";
   private Object _targetJWS;
   private CompositeListener listeners = new CompositeListener();
   transient WlMessageContext messageContext = null;
   private transient JwsSecurityContext jwsSecurityContext = null;
   private boolean _understoodInputHeaders = false;
   private Map<String, Object> properties = new HashMap();
   private transient ServiceHandleImpl serviceHandle = null;
   private static final boolean verbose;

   protected Container(Object var1, WlMessageContext var2) {
      this.setMessageContext(var2);
      this.init(var1);
   }

   public void setMessageContext(WlMessageContext var1) {
      this.messageContext = var1;
      var1.setProperty("weblogic.wsee.jws.container", this);
   }

   public void setServletContext(ServletEndpointContext var1) {
      if (this._targetJWS instanceof ServiceLifecycle) {
         ServiceLifecycle var2 = (ServiceLifecycle)this._targetJWS;

         try {
            var2.init(var1);
         } catch (ServiceException var4) {
            if (verbose) {
               Verbose.logException(var4);
            }

            throw new InvokeException(var4.getMessage(), var4);
         }

         if (var1 != null) {
            ServletEndpointContextImpl var3 = (ServletEndpointContextImpl)var1;
            var3.setMessageContext(this.messageContext);
            var3.setSecurityContext((WebSecurityContext)this.getSecurityContext());
         }
      }

   }

   private void init(Object var1) {
      try {
         this._targetJWS = var1;
         this.registerListeners();
         JwsInitializer var2 = new JwsInitializer(this._targetJWS, this.messageContext, this);
         var2.initialize();
         this.initializeContextFields();
         this.listeners.onCreate();
      } catch (InvocationTargetException var3) {
         throw new InvokeException("Unable to Load JWS", var3.getTargetException());
      } catch (RuntimeException var4) {
         throw var4;
      } catch (Exception var5) {
         throw new InvokeException("Unable to Load JWS", var5);
      }
   }

   private static Field[] getAllVisibleFieldsInClass(Class var0) {
      if (var0 == null) {
         return new Field[0];
      } else {
         Field[] var1 = var0.getDeclaredFields();
         Class var2 = var0.getSuperclass();
         if (var2 == null) {
            return var1;
         } else {
            HashSet var3 = new HashSet();
            Field[] var4 = var1;
            int var5 = var1.length;

            int var6;
            for(var6 = 0; var6 < var5; ++var6) {
               Field var7 = var4[var6];
               var3.add(var7);
            }

            for(var4 = null; var2 != null; var2 = var2.getSuperclass()) {
               var4 = var2.getDeclaredFields();
               Field[] var9 = var4;
               var6 = var4.length;

               for(int var10 = 0; var10 < var6; ++var10) {
                  Field var8 = var9[var10];
                  if (Modifier.isProtected(var8.getModifiers()) || Modifier.isPublic(var8.getModifiers())) {
                     var3.add(var8);
                  }
               }
            }

            return (Field[])var3.toArray(new Field[var3.size()]);
         }
      }
   }

   private void initializeContextFields() {
      Field[] var1 = getAllVisibleFieldsInClass(this._targetJWS.getClass());
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         Field var4 = var1[var3];
         if (FieldHelper.isContext(var4)) {
            FieldHelper.setFieldValue(var4, this._targetJWS, this);
         }
      }

   }

   private ContainerListener getControlListener() throws Exception {
      if (this.getControlClientInitClass() != null) {
         Class var1 = this.getContextClassLoader().loadClass("weblogic.controls.jws.ControlListener");
         Constructor var2 = var1.getConstructor(Container.class);
         return (ContainerListener)var2.newInstance(this);
      } else {
         return null;
      }
   }

   private Class getControlClientInitClass() throws Exception {
      Class var1 = null;
      Class var2 = this._targetJWS.getClass();

      try {
         WeakReference var3 = (WeakReference)controlClientInitClassCache.get(var2);
         if (var3 != null) {
            var1 = (Class)var3.get();
            if (var1 != null) {
               return var1 != Void.class ? var1 : null;
            }
         }

         String var4 = var2.getName() + "ClientInitializer";
         var1 = var2.getClassLoader().loadClass(var4);
         controlClientInitClassCache.put(var2, new WeakReference(var1 != null ? var1 : Void.class));
      } catch (ClassNotFoundException var5) {
         controlClientInitClassCache.put(var2, new WeakReference(Void.class));
      }

      return var1;
   }

   private ContainerListener getContextCallbackListener() {
      return this._targetJWS instanceof JwsContext.Callback ? new CallbackEventListener((JwsContext.Callback)this._targetJWS) : null;
   }

   private void registerListeners() throws Exception {
      ContainerListener var1 = this.getControlListener();
      if (var1 != null) {
         this.listeners.addListener(var1);
      }

      ContainerListener var2 = this.getContextCallbackListener();
      if (var2 != null) {
         this.listeners.addListener(var2);
      }

      Field[] var3 = getAllVisibleFieldsInClass(this._targetJWS.getClass());
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Field var6 = var3[var5];
         if (FieldHelper.isContext(var6)) {
            this.listeners.addListener(new ContextEventListener(var6.getName(), this._targetJWS));
         }
      }

   }

   private void failNotConversational(String var1) throws IllegalStateException {
      throw new IllegalStateException(var1 + "\nOperation requires a conversational service.");
   }

   public MessageContext getMessageContext() {
      return new FilteredMessageContext(this.messageContext);
   }

   public WlMessageContext getUnfilteredMessageContext() {
      return this.messageContext;
   }

   public Element[] getInputHeaders() {
      MessageContext var1 = (MessageContext)this.messageContext.getProperty("weblogic.wsee.local.transport.prior.context");
      return var1 != null ? ControlAPIUtil.getInputHeaders(var1) : ControlAPIUtil.getInputHeaders((MessageContext)this.messageContext);
   }

   public void setUnderstoodInputHeaders(boolean var1) {
      this._understoodInputHeaders = var1;
      this.messageContext.setProperty("weblogic.wsee.addressing.UnderstoodHeaders", new Boolean(this._understoodInputHeaders));
      if (!var1) {
         HeaderUtil.throwMustUnderstand(this.messageContext);
      }

   }

   public boolean getUnderstoodInputHeaders() {
      return this._understoodInputHeaders;
   }

   public void setOutputHeaders(Element[] var1) {
      MessageContext var2 = (MessageContext)this.messageContext.getProperty("weblogic.wsee.local.transport.prior.context");
      if (var2 != null) {
         ControlAPIUtil.setOutputHeaders(var2, var1);
      } else {
         ControlAPIUtil.setOutputHeaders((MessageContext)this.messageContext, var1);
      }

   }

   public Protocol getProtocol() {
      String var1 = this.getEndpointAddress();
      String var2 = "jms";
      int var3 = var1.indexOf(58);
      if (var3 > 0) {
         var2 = var1.substring(0, var3);
      }

      boolean var4 = false;
      if (this.messageContext instanceof SoapMessageContext) {
         var4 = ((SoapMessageContext)this.messageContext).isSoap12();
      }

      return Protocol.getProtocol(var4, var2);
   }

   public boolean isFinished() {
      return false;
   }

   public void finishConversation() {
      this.failNotConversational("Cannot finish conversation.");
   }

   public void setMaxAge(Date var1) throws IllegalStateException, IllegalArgumentException {
      this.failNotConversational("Cannot set maximum age.");
   }

   public void setMaxAge(String var1) throws IllegalStateException, IllegalArgumentException {
      this.failNotConversational("Cannot set maximum age.");
   }

   public long getMaxAge() throws IllegalStateException {
      return 0L;
   }

   public long getCurrentAge() throws IllegalStateException {
      return 0L;
   }

   public void resetIdleTime() throws IllegalStateException {
      this.failNotConversational("Cannot reset idle time.");
   }

   public void setMaxIdleTime(long var1) throws IllegalStateException, IllegalArgumentException {
      this.failNotConversational("Cannot set maxium idle time.");
   }

   public void setMaxIdleTime(String var1) throws IllegalStateException, IllegalArgumentException {
      this.failNotConversational("Cannot set maxium idle time.");
   }

   public long getMaxIdleTime() throws IllegalStateException {
      return 0L;
   }

   public long getCurrentIdleTime() throws IllegalStateException {
      return 0L;
   }

   public Principal getCallerPrincipal() {
      return this.getSecurityContext().getCallerPrincipal();
   }

   public boolean isCallerInRole(String var1) {
      return this.getSecurityContext().isCallerInRole(var1);
   }

   public ServiceHandle getService() {
      if (this.serviceHandle == null) {
         this.serviceHandle = new ServiceHandleImpl(this.getEndpointAddress(), this.getContextPath(), this.getURI(), (String)null);
      }

      return this.serviceHandle;
   }

   public Logger getLogger(String var1) {
      return new ApplicationLogger(var1);
   }

   private ClassLoader getContextClassLoader() {
      return Thread.currentThread().getContextClassLoader();
   }

   private void writeObject(ObjectOutputStream var1) throws IOException {
      var1.writeUTF("9.0");
      var1.defaultWriteObject();
   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      String var2 = var1.readUTF();
      if (!var2.equals("9.0")) {
         throw new IOException("Wrong version, expected: 9.0 actual: " + var2);
      } else {
         var1.defaultReadObject();
      }
   }

   CompositeListener getListeners() {
      return this.listeners;
   }

   String getEndpointAddress() {
      return (String)this.messageContext.getProperty("weblogic.wsee.connection.end_point_address");
   }

   String getContextPath() {
      return (String)this.messageContext.getProperty("weblogic.wsee.context_path");
   }

   String getURI() {
      String var1 = ControlAPIUtil.getURI(this.messageContext);
      return var1 != null ? var1 : this.messageContext.getDispatcher().getConnection().getTransport().getServiceURI();
   }

   public static boolean isContainerRequired(WsPort var0) {
      return isComplexFieldsPresent(var0.getEndpoint().getJwsClass()) || ConversationUtils.isConversational(var0);
   }

   private static boolean isComplexFieldsPresent(Class var0) {
      Field[] var1 = getAllVisibleFieldsInClass(var0);
      Field[] var2 = var1;
      int var3 = var1.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Field var5 = var2[var4];
         var5.setAccessible(true);
         Annotation[] var6 = var5.getAnnotations();
         Annotation[] var7 = var6;
         int var8 = var6.length;

         for(int var9 = 0; var9 < var8; ++var9) {
            Annotation var10 = var7[var9];
            if (complexAnnotationSet.contains(var10.annotationType().getName())) {
               return true;
            }
         }
      }

      return false;
   }

   public <V extends Serializable> V setProperty(String var1, V var2) {
      return (Serializable)this.properties.put(var1, var2);
   }

   public <V extends Serializable> V getProperty(String var1) {
      return (Serializable)this.properties.get(var1);
   }

   public <V extends Serializable> V removeProperty(String var1) {
      return (Serializable)this.properties.remove(var1);
   }

   public Object getTargetJWS() {
      return this._targetJWS;
   }

   private JwsSecurityContext getSecurityContext() {
      if (this.jwsSecurityContext == null) {
         this.jwsSecurityContext = new WebSecurityContext(this.messageContext, this._targetJWS.getClass());
      }

      return this.jwsSecurityContext;
   }

   public void destroy() {
      if (ServiceLifecycle.class.isInstance(this._targetJWS)) {
         ServiceLifecycle var1 = (ServiceLifecycle)this._targetJWS;
         var1.destroy();
      }

   }

   public String getId() {
      return null;
   }

   static {
      complexAnnotationSet = new HashSet();
      complexAnnotationSet.add("weblogic.controls.jws.Common.Context");
      complexAnnotationSet.add("org.apache.beehive.controls.api.bean.Control");
      complexAnnotationSet.add(Context.class.getName());
      complexAnnotationSet.add(weblogic.jws.Callback.class.getName());
      complexAnnotationSet.add(ServiceClient.class.getName());
      verbose = Verbose.isVerbose(Container.class);
   }
}
