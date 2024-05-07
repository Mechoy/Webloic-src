package weblogic.wsee.callback;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.rmi.Remote;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.jws.Oneway;
import javax.xml.rpc.Stub;
import javax.xml.rpc.handler.MessageContext;
import org.w3c.dom.Element;
import weblogic.jws.MessageBuffer;
import weblogic.wsee.addressing.EndpointReference;
import weblogic.wsee.connection.transport.https.HttpsTransportInfo;
import weblogic.wsee.conversation.ContinueHeader;
import weblogic.wsee.jaxrpc.StubImpl;
import weblogic.wsee.jws.CallbackInterface;
import weblogic.wsee.jws.Protocol;
import weblogic.wsee.message.MsgHeaders;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.util.ControlAPIUtil;
import weblogic.wsee.ws.dispatch.server.ServletEndpointContextImpl;

public class CallbackStubImpl implements CallbackInterface, InvocationHandler, Serializable {
   static final long serialVersionUID = 1L;
   String stubId = null;
   private Stub stub = null;
   private Class callbackInterface;
   static Map<Key, Converter> converters = new HashMap();
   private static final String JAX_RPC_ENDPOINT_ADDRESS_PROPERTY = "javax.xml.rpc.service.endpoint.address";
   private static final String TRANSPORT_INFO_PROPERTY = "weblogic.wsee.connection.transportinfo";
   CallbackCredentials _credentials = null;
   String _originalEndpointAddress = null;
   String _endpointAddress = null;
   boolean _originalEndpointAddresSet = false;
   Integer _originalTimeout = null;
   boolean _originalTimeoutSet = false;
   private static final String DEFAULT_KeyStoreType = "JKS";
   private static final String UPGRADE_81_PROPERTY = "weblogic.wsee.WLW81Upgrade";

   public static Stub newInstance(Stub var0, Class var1) {
      Class[] var2 = new Class[]{Stub.class, var1};
      CallbackStubImpl var3 = new CallbackStubImpl(var0, var1);
      return (Stub)Proxy.newProxyInstance(var0.getClass().getClassLoader(), var2, var3);
   }

   public CallbackStubImpl(Stub var1, Class var2) {
      this.stub = var1;
      this.stubId = "Stub Created At: " + new Date();
      this.callbackInterface = var2;
   }

   public Object invoke(Object var1, Method var2, Object[] var3) throws Throwable {
      if (var2.getDeclaringClass().isAssignableFrom(CallbackStubImpl.class)) {
         try {
            Method var4 = CallbackStubImpl.class.getDeclaredMethod(var2.getName(), var2.getParameterTypes());
            if (var4 != null) {
               return var4.invoke(this, var3);
            }
         } catch (NoSuchMethodException var5) {
         } catch (InvocationTargetException var6) {
            throw var6.getTargetException();
         }
      }

      this.invokePrep();
      return this.invokeStub(var1, var2, var3);
   }

   private Object invokeStub(Object var1, Method var2, Object[] var3) throws Throwable {
      if (!var2.getDeclaringClass().isAssignableFrom(this.callbackInterface)) {
         try {
            return var2.invoke(this.stub, var3);
         } catch (InvocationTargetException var17) {
            throw var17.getTargetException();
         }
      } else {
         Class var4 = this.getPortTypeInterface();

         assert var4 != null;

         Method var5 = this.getCallBackMethod(var4, var2);
         if (var5 == null) {
            throw new NoSuchMethodException(var2.toString() + " is not found in the " + this.callbackInterface);
         } else {
            Class[] var6 = var2.getParameterTypes();
            Class[] var7 = var5.getParameterTypes();
            Object[] var8 = null;

            assert var6.length == var7.length;

            for(int var9 = 0; var9 < var7.length; ++var9) {
               if (!var7[var9].isAssignableFrom(var6[var9])) {
                  int var10 = 0;

                  int var11;
                  for(var11 = 0; var7[var9].isArray(); ++var10) {
                     var7[var9] = var7[var9].getComponentType();
                  }

                  while(var6[var9].isArray()) {
                     var6[var9] = var6[var9].getComponentType();
                     ++var11;
                  }

                  if (var10 != var11) {
                     throw new NoSuchMethodException(var2.toString() + " is not found in the " + this.callbackInterface);
                  }

                  Object var12 = (Converter)converters.get(new Key(var6[var9], var7[var9]));
                  if (var12 == null) {
                     throw new NoSuchMethodException(var2.toString() + " is not found in the " + this.callbackInterface);
                  }

                  if (var11 > 0) {
                     var12 = new ArrayConverter((Converter)var12, var5.getParameterTypes()[var9]);
                  }

                  if (var8 == null) {
                     var8 = new Object[var3.length];
                     System.arraycopy(var3, 0, var8, 0, var3.length);
                  }

                  var8[var9] = ((Converter)var12).getValue(var8[var9]);
               }
            }

            if (var8 == null) {
               var8 = var3;
            }

            if (this.thereIsNoTargetAndFromNonConversationalJws()) {
               String var20 = "JWS is not conversational, but tried to perform a callback. If asynchronous callbacks are needed, you will need to make this a conversational web service with start, continue, and finish methods.";
               throw new RuntimeException(var20);
            } else {
               if (this.potentialDeadlock(var2)) {
                  this.printDeadlockWarning();
               }

               this.processBuffering(this.stub, var2, var3);

               Object var19;
               try {
                  var19 = var5.invoke(this.stub, var8);
               } finally {
                  if (this.stub instanceof StubImpl) {
                     ((StubImpl)this.stub)._removeProperty("weblogic.wsee.callback.client.buffering");
                  }

                  ((StubImpl)this.stub)._removeProperty("weblogic.wsee.callback.client.methodname");
                  ((StubImpl)this.stub)._removeProperty("weblogic.wsee.callback.client.args");
               }

               return var19;
            }
         }
      }
   }

   private Method getCallBackMethod(Class var1, Method var2) {
      Method[] var3 = var1.getDeclaredMethods();
      Method[] var4 = var3;
      int var5 = var3.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         Method var7 = var4[var6];
         if (var7.getName().equals(var2.getName())) {
            return var7;
         }
      }

      return null;
   }

   private Class getPortTypeInterface() {
      Class var1 = null;
      Class[] var2 = this.stub.getClass().getInterfaces();
      Class[] var3 = var2;
      int var4 = var2.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Class var6 = var3[var5];
         if (Remote.class.isAssignableFrom(var6)) {
            var1 = var6;
            break;
         }
      }

      return var1;
   }

   private void processBuffering(Stub var1, Method var2, Object[] var3) {
      MessageBuffer var4 = this.getMessageBuffer(var2);
      if (var4 != null) {
         boolean var5 = true;
         if (var3 != null) {
            Object[] var6 = var3;
            int var7 = var3.length;

            for(int var8 = 0; var8 < var7; ++var8) {
               Object var9 = var6[var8];
               if (!(var9 instanceof Serializable)) {
                  System.err.println("Error reporting will not work for callback MessageBuffer: not all arguments are serializable");
                  var5 = false;
                  break;
               }
            }
         }

         if (var5) {
            var1._setProperty("weblogic.wsee.callback.client.methodname", var2.getName());
            var1._setProperty("weblogic.wsee.callback.client.args", var3);
         }

         CallbackClientBufferingData var10 = new CallbackClientBufferingData();
         var10.setRetryCount(var4.retryCount());
         var10.setRetryDelay(var4.retryDelay());
         var10.setTargetURI((String)var1._getProperty("weblogic.wsee.enclosing.jws.contextpath") + "/" + (String)var1._getProperty("weblogic.wsee.enclosing.jws.servicename") + ":/" + var2.getDeclaringClass().getName().replace(".", "/"));
         var1._setProperty("weblogic.wsee.callback.client.buffering", var10);
      }
   }

   private MessageBuffer getMessageBuffer(Method var1) {
      MessageBuffer var2 = (MessageBuffer)var1.getAnnotation(MessageBuffer.class);
      if (var2 == null) {
         var2 = (MessageBuffer)var1.getDeclaringClass().getAnnotation(MessageBuffer.class);
      }

      return var2;
   }

   private boolean thereIsNoTargetAndFromNonConversationalJws() {
      if (null == this.stub._getProperty("weblogic.wsee.addressing.Target")) {
         MessageContext var1 = ServletEndpointContextImpl.getMessageContextStatic();
         WlMessageContext var2 = WlMessageContext.narrow(var1);
         String var3 = (String)var2.getProperty("javax.xml.rpc.service.endpoint.address");
         return null == var3;
      } else {
         return false;
      }
   }

   private void printDeadlockWarning() {
      System.err.println("===========================");
      System.err.println("Potential deadlock detected: you should not invoke a synchronous, conversational callback within a blocking jws method.  You can fix this by making either the jws method or the callback buffered.");
      System.err.println("===========================");
   }

   private boolean potentialDeadlock(Method var1) {
      Oneway var2 = (Oneway)var1.getAnnotation(Oneway.class);
      MessageBuffer var3 = this.getMessageBuffer(var1);
      if (var2 == null && var3 == null) {
         MessageContext var4 = ServletEndpointContextImpl.getMessageContextStatic();
         if (var4 == null) {
            return false;
         }

         WlMessageContext var5 = WlMessageContext.narrow(var4);
         if (var5.getDispatcher().getOperation().getType() == 1 || var5.getDispatcher().getOperation().getType() == 3 || var5.getProperty("weblogic.wsee.reply.anonymous") == null || var5.containsProperty("weblogic.wsee.handler.wlw81BufferCompatFlat")) {
            return false;
         }

         EndpointReference var6 = (EndpointReference)this.stub._getProperty("weblogic.wsee.addressing.Target");
         if (var6 != null) {
            Iterator var7;
            if (var6.getReferenceParameters() != null) {
               var7 = var6.getReferenceParameters().listHeaders();

               while(var7.hasNext()) {
                  if (var7.next() instanceof ContinueHeader) {
                     return true;
                  }
               }
            }

            if (var6.getReferenceProperties() != null) {
               var7 = var6.getReferenceProperties().listHeaders();

               while(var7.hasNext()) {
                  if (var7.next() instanceof ContinueHeader) {
                     return true;
                  }
               }
            }
         }
      }

      return false;
   }

   public void invokePrep() {
      if (this._credentials != null) {
         String var1 = this._credentials.getUsername();
         if (var1 != null) {
            this.stub._setProperty("javax.xml.rpc.security.auth.username", var1);
         }

         String var2 = this._credentials.getPassword();
         if (var2 != null) {
            this.stub._setProperty("javax.xml.rpc.security.auth.password", var2);
         }

         HttpsTransportInfo var3 = this._credentials.getHttpsTransportInfo();
         if (var3 != null) {
            this.stub._setProperty("weblogic.wsee.connection.transportinfo", var3);
         }
      }

      this.stub._setProperty("weblogic.wsee.WLW81Upgrade", "true");
   }

   public Element[] getInputHeaders() {
      return ControlAPIUtil.getInputHeaders(this.stub);
   }

   public void setOutputHeaders(Element[] var1) {
      ControlAPIUtil.setOutputHeaders(this.stub, var1);
   }

   public void setConversationID(String var1) {
      this.stub._setProperty("weblogic.wsee.conversation.ConversationId", var1);
   }

   public String getConversationID() {
      String var1 = (String)this.stub._getProperty("weblogic.wsee.conversation.ConversationId");
      if (var1 == null) {
         Map var2 = (Map)this.stub._getProperty("weblogic.wsee.invoke_properties");
         if (var2 == null) {
            return null;
         }

         EndpointReference var3 = (EndpointReference)var2.get("weblogic.wsee.addressing.Target");
         if (var3 == null) {
            return null;
         }

         MsgHeaders var4 = var3.getReferenceParameters();
         if (var4 == null) {
            return null;
         }

         ContinueHeader var5 = (ContinueHeader)var4.getHeader(ContinueHeader.TYPE);
         if (var5 == null) {
            return null;
         }

         var1 = var5.getConversationId();
      }

      return var1;
   }

   public void setTimeout(int var1) {
      if (var1 < 0) {
         var1 = 0;
      }

      if (!this._originalTimeoutSet) {
         this._originalTimeoutSet = true;
         this._originalTimeout = (Integer)this.stub._getProperty("weblogic.wsee.transport.read.timeout");
      }

      this.stub._setProperty("weblogic.wsee.transport.read.timeout", new Integer(var1));
   }

   public int getTimeout() {
      Integer var1 = (Integer)this.stub._getProperty("weblogic.wsee.transport.read.timeout");
      return var1 != null ? var1 : -1;
   }

   /** @deprecated */
   public void setEndPoint(URL var1) {
      if (var1 != null) {
         String var2 = var1.toExternalForm();
         this.setEndpointAddress(var2);
      } else {
         this.setEndpointAddress((String)null);
      }

   }

   /** @deprecated */
   public URL getEndPoint() {
      String var1 = this.getEndpointAddress();
      if (var1 == null) {
         return null;
      } else {
         try {
            return new URL(var1);
         } catch (MalformedURLException var3) {
            throw new RuntimeException("Invalid URL returned from stub!", var3);
         }
      }
   }

   public String getEndpointAddress() {
      String var1 = (String)this.stub._getProperty("javax.xml.rpc.service.endpoint.address");
      if (var1 == null) {
         EndpointReference var2 = (EndpointReference)this.stub._getProperty("weblogic.wsee.addressing.Target");
         if (var2 != null) {
            var1 = var2.getAddress();
         }
      }

      return var1;
   }

   public void setEndpointAddress(String var1) {
      if (!this._originalEndpointAddresSet) {
         this._originalEndpointAddress = this.getEndpointAddress();
         this._originalEndpointAddresSet = true;
      }

      this.stub._setProperty("javax.xml.rpc.service.endpoint.address", var1);
      EndpointReference var2 = (EndpointReference)this.stub._getProperty("weblogic.wsee.addressing.Target");
      if (var2 != null) {
         var2.setAddress(var1);
      }

   }

   public void setUsername(String var1) {
      if (this._credentials == null) {
         this._credentials = new CallbackCredentials();
      }

      this._credentials.setUsername(var1);
   }

   public void setPassword(String var1) {
      if (this._credentials == null) {
         this._credentials = new CallbackCredentials();
      }

      this._credentials.setPassword(var1);
   }

   public String getUsername() {
      return this._credentials != null ? this._credentials.getUsername() : null;
   }

   public String getPassword() {
      return this._credentials != null ? this._credentials.getPassword() : null;
   }

   /** @deprecated */
   public void setProtocol(Protocol var1) {
   }

   /** @deprecated */
   public Protocol getProtocol() {
      String var1 = this.getEndpointAddress();
      if (var1 == null) {
         return null;
      } else if (!var1.startsWith("http") && !var1.startsWith("https")) {
         return var1.startsWith("jms") ? Protocol.JMS_SOAP : null;
      } else {
         return Protocol.HTTP_SOAP;
      }
   }

   public void useClientKeySSL(boolean var1) {
      if (this._credentials == null) {
         this._credentials = new CallbackCredentials();
      }

      this._credentials.useClientKeySSL(var1);
   }

   public void setKeystore(String var1, String var2) {
      this.setKeystore(var1, var2, (String)null);
   }

   public void setKeystore(String var1, String var2, String var3) {
      if (this._credentials == null) {
         this._credentials = new CallbackCredentials();
      }

      this._credentials.setKeystore(var1, var2, var3);
   }

   public void setClientCert(String var1, String var2) {
      if (this._credentials == null) {
         this._credentials = new CallbackCredentials();
      }

      this._credentials.setClientCert(var1, var2);
   }

   public void reset() {
      this._credentials = null;
      this.stub._setProperty("javax.xml.rpc.security.auth.username", (Object)null);
      this.stub._setProperty("javax.xml.rpc.security.auth.password", (Object)null);
      this.stub._setProperty("weblogic.wsee.connection.transportinfo", (Object)null);
      if (this._originalEndpointAddresSet) {
         this.stub._setProperty("javax.xml.rpc.service.endpoint.address", this._originalEndpointAddress);
         EndpointReference var1 = (EndpointReference)this.stub._getProperty("weblogic.wsee.addressing.Target");
         if (var1 != null) {
            var1.setAddress(this._originalEndpointAddress);
         }
      }

      this._originalEndpointAddress = null;
      this._originalEndpointAddresSet = false;
      if (this._originalTimeoutSet) {
         this.stub._setProperty("weblogic.wsee.transport.read.timeout", this._originalTimeout);
      }

      this._originalTimeout = null;
      this._originalTimeoutSet = false;
      ControlAPIUtil.unsetOutputHeaders(this.stub);
   }

   public void setTruststore(String var1, String var2) {
      this.setTruststore(var1, var2, (String)null);
   }

   public void setTruststore(String var1, String var2, String var3) {
      if (this._credentials == null) {
         this._credentials = new CallbackCredentials();
      }

      this._credentials.setTruststore(var1, var2, var3);
   }

   public CallbackCredentials getCallbackCredentials() {
      return this._credentials;
   }

   static {
      Converter[] var0 = new Converter[]{new DateConverter(Calendar.class), new DateConverter(GregorianCalendar.class), new GregorianCalendarConverter(Calendar.class), new Obj2StringConverter(Character.class), new Obj2StringConverter(Character.TYPE), new Obj2StringConverter(URI.class), new AutoBoxingConverter(Byte.class, Byte.TYPE, (byte)0), new AutoBoxingConverter(Short.class, Short.TYPE, Short.valueOf((short)0)), new AutoBoxingConverter(Integer.class, Integer.TYPE), new AutoBoxingConverter(Long.class, Long.TYPE), new AutoBoxingConverter(Float.class, Float.TYPE), new AutoBoxingConverter(Double.class, Double.TYPE), new AutoBoxingConverter(Boolean.class, Boolean.TYPE, false)};
      Converter[] var1 = var0;
      int var2 = var0.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         Converter var4 = var1[var3];
         converters.put(var4.getKey(), var4);
      }

   }

   static class Key {
      Class from;
      Class to;

      public Key(Class var1, Class var2) {
         this.from = var1;
         this.to = var2;
      }

      public boolean equals(Object var1) {
         if (this == var1) {
            return true;
         } else if (!(var1 instanceof Key)) {
            return false;
         } else {
            Key var2 = (Key)var1;
            return this.from == this.from && this.to == this.to;
         }
      }

      public int hashCode() {
         int var1 = this.from != null ? this.from.hashCode() : 0;
         var1 = 29 * var1 + (this.to != null ? this.to.hashCode() : 0);
         return var1;
      }
   }

   static class ArrayConverter implements Converter {
      private Converter _delegate;
      private Class _to;

      public ArrayConverter(Converter var1, Class var2) {
         this._delegate = var1;
         this._to = var2;
      }

      public Object getValue(Object var1) {
         return this.convert(var1, this._to);
      }

      public Key getKey() {
         return null;
      }

      private Object convert(Object var1, Class var2) {
         if (var1 == null) {
            return null;
         } else {
            var2 = var2.getComponentType();
            if (var2 == null) {
               return this._delegate.getValue(var1);
            } else {
               Object[] var3 = (Object[])((Object[])Array.newInstance(var2, Array.getLength(var1)));

               for(int var4 = 0; var4 < var3.length; ++var4) {
                  var3[var4] = this.convert(Array.get(var1, var4), var2);
               }

               return var3;
            }
         }
      }
   }

   static class AutoBoxingConverter implements Converter {
      private Class from;
      private Class to;
      private Object defaultValue;

      AutoBoxingConverter(Class var1, Class var2) {
         this(var1, var2, 0);
      }

      AutoBoxingConverter(Class var1, Class var2, Object var3) {
         this.from = var1;
         this.to = var2;
         this.defaultValue = var3;
      }

      public Object getValue(Object var1) {
         return var1 == null ? this.defaultValue : var1;
      }

      public Key getKey() {
         return new Key(this.from, this.to);
      }
   }

   static class GregorianCalendarConverter implements Converter {
      private Class from;

      public GregorianCalendarConverter(Class var1) {
         this.from = var1;
      }

      public GregorianCalendar getValue(Object var1) {
         return (GregorianCalendar)GregorianCalendar.class.cast(var1);
      }

      public Key getKey() {
         return new Key(this.from, GregorianCalendar.class);
      }
   }

   static class DateConverter implements Converter {
      private Class to;

      public DateConverter(Class var1) {
         this.to = var1;
      }

      public Calendar getValue(Object var1) {
         if (var1 == null) {
            return null;
         } else {
            assert var1 instanceof Date;

            GregorianCalendar var2 = new GregorianCalendar();
            var2.setTime((Date)var1);
            return var2;
         }
      }

      public Key getKey() {
         return new Key(Date.class, this.to);
      }
   }

   static class Obj2StringConverter implements Converter {
      private Class from;

      Obj2StringConverter(Class var1) {
         this.from = var1;
      }

      public String getValue(Object var1) {
         return var1 == null ? null : var1.toString();
      }

      public Key getKey() {
         return new Key(this.from, String.class);
      }
   }

   interface Converter {
      Object getValue(Object var1);

      Key getKey();
   }
}
