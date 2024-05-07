package weblogic.wsee.message;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.xml.rpc.handler.MessageContext;
import weblogic.wsee.ws.dispatch.Dispatcher;

public abstract class WlMessageContext implements MessageContext {
   private MsgHeaders headers;
   private Dispatcher dispatcher;
   protected Throwable fault;
   protected final Map propertyMap = this.createPropertyMap();
   public static final String END_POINT_URI = "weblogic.wsee.connection.end_point_uri";
   public static final String END_POINT_ADDRESS = "weblogic.wsee.connection.end_point_address";
   public static final String TRANSPORT_HEADERS = "weblogic.wsee.transport.headers";
   public static final String SERVLET_REQUEST = "weblogic.wsee.transport.servlet.request";
   public static final String IS_CLIENT_CERT_REQUIRED = "weblogic.wsee.transport.client.cert.required";
   public static final String SERVLET_RESPONSE = "weblogic.wsee.transport.servlet.response";
   public static final String IS_SECURE_SERVLET_REQUEST = "weblogic.wsee.transport.servlet.request.secure";
   public static final String SECURITY_CONTEXT_CREDENTIAL = "weblogic.wsee.wssc.sct";
   public static final String SAML_CREDENTIAL = "weblogic.wsee.saml.credential";
   /** @deprecated */
   public static final String STS_ENDPOINT_ADDRESS_PROPERTY = "weblogic.wsee.wst.sts_endpoint_uri";
   public static final String WST_BOOT_STRAP_POLICY = "weblogic.wsee.security.wst_bootstrap_policy";
   public static final String SCT_LIFETIME_PROPERTY = "weblogic.wsee.wssc.sct.lifetime";
   public static final String DK_LABEL_PROPERTY = "weblogic.wsee.wssc.dk.label";
   public static final String DK_LENGTH_PROPERTY = "weblogic.wsee.wssc.dk.length";
   public static final String WSS_MESSAGE_AGE = "weblogic.wsee.security.message_age";
   public static final String INCOMING_FAULT_MSG = "weblogic.wsee.ignore.fault";
   public static final String MTOM_MESSAGE_RECVD = "weblogic.wsee.mtom_message_recvd";
   public static final String SERVICE_NAME = "weblogic.wsee.service_name";
   public static final String CONTEXT_PATH = "weblogic.wsee.context_path";
   public static final String SERVICE_URI = "weblogic.wsee.service_uri";
   public static final String SECURITY_REALM = "weblogic.wsee.security_realm";
   public static final String APPLICATION_ID = "weblogic.wsee.application_id";
   public static final String STREAM_ATTACHMENTS = "weblogic.wsee.stream_attachments";
   public static final String VALIDATE_REQUEST = "weblogic.wsee.validate_request";
   public static final String IS_MTOM_ENABLE_IN_JAXWS = "weblogic.wsee.mtom.enable";
   public static final String MTOM_THRESHOLD = "weblogic.wsee.mtom.threshold";

   protected Map createPropertyMap() {
      return new HashMap();
   }

   public Dispatcher getDispatcher() {
      return this.dispatcher;
   }

   public void setDispatcher(Dispatcher var1) {
      this.dispatcher = var1;
   }

   public static WlMessageContext narrow(MessageContext var0) {
      if (var0 == null) {
         throw new IllegalArgumentException("Internal error, the context provided is 'null'");
      } else if (var0 instanceof WlMessageContext) {
         return (WlMessageContext)var0;
      } else {
         throw new IllegalArgumentException("Internal error, the context provided is not WlMessageContext, make sure that you are usingWLS runtime");
      }
   }

   public Throwable getFault() {
      return this.fault;
   }

   public void setFault(Throwable var1) {
      this.fault = var1;
   }

   public boolean hasFault() {
      return this.fault != null;
   }

   public void setProperty(String var1, Object var2) {
      this.propertyMap.put(var1, var2);
   }

   public Object getProperty(String var1) {
      return this.propertyMap.get(var1);
   }

   public void removeProperty(String var1) {
      this.propertyMap.remove(var1);
   }

   public boolean containsProperty(String var1) {
      return this.propertyMap.containsKey(var1);
   }

   public Iterator getPropertyNames() {
      return this.propertyMap.keySet().iterator();
   }

   public MsgHeaders getHeaders() {
      return this.headers;
   }

   public void setHeaders(MsgHeaders var1) {
      this.headers = var1;
   }
}
