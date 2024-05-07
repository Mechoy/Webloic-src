package weblogic.wsee.ws.dispatch.server;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.xml.namespace.QName;
import javax.xml.rpc.JAXRPCException;
import javax.xml.rpc.handler.GenericHandler;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPMessage;
import org.w3c.dom.Element;
import weblogic.diagnostics.instrumentation.DelegatingMonitor;
import weblogic.diagnostics.instrumentation.DiagnosticAction;
import weblogic.diagnostics.instrumentation.DiagnosticActionState;
import weblogic.diagnostics.instrumentation.DynamicJoinPoint;
import weblogic.diagnostics.instrumentation.InstrumentationSupport;
import weblogic.diagnostics.instrumentation.JoinPoint;
import weblogic.security.utils.KeyStoreConfigurationHelper;
import weblogic.security.utils.KeyStoreInfo;
import weblogic.security.utils.MBeanKeyStoreConfiguration;
import weblogic.wsee.addressing.AddressingProvider;
import weblogic.wsee.addressing.AddressingProviderFactory;
import weblogic.wsee.addressing.EndpointReference;
import weblogic.wsee.connection.Connection;
import weblogic.wsee.connection.ConnectionException;
import weblogic.wsee.connection.ConnectionFactory;
import weblogic.wsee.connection.transport.Transport;
import weblogic.wsee.connection.transport.https.WlsSSLAdapter;
import weblogic.wsee.connection.transport.jms.JmsServerQueueTransport;
import weblogic.wsee.connection.transport.servlet.HttpServerTransport;
import weblogic.wsee.handler.InvocationException;
import weblogic.wsee.handler.WLHandler;
import weblogic.wsee.jaxrpc.soapfault.SOAPFaultUtil;
import weblogic.wsee.message.UnknownMsgHeader;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.message.soap.SoapMessageContext;
import weblogic.wsee.reliability.WsrmClientHandler;
import weblogic.wsee.server.WsLifeCycleEvent;
import weblogic.wsee.server.WsLifeCycleListenerRegistry;
import weblogic.wsee.util.ControlAPIUtil;
import weblogic.wsee.util.ToStringWriter;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.ws.dispatch.Dispatcher;
import weblogic.xml.saaj.VersionMismatchException;

public class ConnectionHandler extends GenericHandler implements WLHandler {
   private static final boolean verbose;
   static final long serialVersionUID = -3051027421965256288L;
   public static final String _WLDF$INST_VERSION = "9.0.0";
   // $FF: synthetic field
   static Class _WLDF$INST_FLD_class = Class.forName("weblogic.wsee.ws.dispatch.server.ConnectionHandler");
   public static final DelegatingMonitor _WLDF$INST_FLD_Webservices_JAXRPC_Diagnostic_Request_Action_Around_Low;
   public static final JoinPoint _WLDF$INST_JPFLD_0;

   public boolean handleResponse(MessageContext var1) {
      if (verbose) {
         Verbose.log((Object)("Sending response: " + var1));
      }

      WlMessageContext var2 = WlMessageContext.narrow(var1);
      this.writeOutputHeaders(var2);
      if (var1.getProperty("weblogic.wsee.addressing.ReplyTo") != null && var1.getProperty("weblogic.wsee.reply.anonymous") == null && var1.getProperty("weblogic.wsee.reliable.oneway.reply") == null) {
         return this.handleAsync(var1);
      } else {
         this.send(var1);
         return true;
      }
   }

   private void writeOutputHeaders(WlMessageContext var1) {
      List var2 = (List)var1.getProperty("weblogic.wsee.OutputHeaders");
      if (var2 != null) {
         Iterator var3 = var2.iterator();

         while(var3.hasNext()) {
            Element var4 = (Element)var3.next();
            var1.getHeaders().addHeader(new UnknownMsgHeader(var4));
         }

         ControlAPIUtil.unsetOutputHeaders((MessageContext)var1);
      }

   }

   private boolean handleAsync(MessageContext var1) {
      long var2 = System.nanoTime();
      if (verbose) {
         Verbose.log((Object)(var2 + " :Entering ConnectionHandler.handleAsync(MessageContext)"));
      }

      if (var1.getProperty("weblogic.wsee.oneway.confirmed") == null) {
         try {
            ((WlMessageContext)var1).getDispatcher().getConnection().getTransport().confirmOneway();
         } catch (IOException var17) {
            throw new InvocationException("Failed to confirm oneway", var17);
         }

         var1.setProperty("weblogic.wsee.oneway.confirmed", "true");
      }

      if (!((WlMessageContext)var1).hasFault() && var1.getProperty("weblogic.wsee.queued.invoke") != null) {
         return true;
      } else {
         String var4;
         if ((var4 = (String)var1.getProperty("javax.xml.rpc.service.endpoint.address")) == null) {
            var4 = ((EndpointReference)var1.getProperty("weblogic.wsee.addressing.ReplyTo")).getAddress();
            var1.setProperty("javax.xml.rpc.service.endpoint.address", var4);
         }

         boolean var5 = false;
         String var6;
         if (((var6 = (String)var1.getProperty("weblogic.wsee.async.res.sequence.id")) != null || var1.getProperty("weblogic.wsee.conversation.AsyncConvId") != null && (var6 = (String)var1.getProperty("weblogic.wsee.convid.sequence.id")) != null) && var1.getProperty("weblogic.wsee.reliability.asyncfault") == null) {
            var5 = true;
         }

         if (var5) {
            var1.setProperty("weblogic.wsee.sequenceid", var6);
            var1.setProperty("weblogic.wsee.async.res", "true");
            var1.removeProperty("weblogic.wsee.handler.jaxrpcHandlerChain");
            var1.removeProperty("weblogic.wsee.conversation.Lock");
            if (verbose) {
               Verbose.say(var2 + " :Inside ConnectionHandler.handleAsync(MessageContext) == 1");
            }

            WsLifeCycleListenerRegistry.getInstance().onEvent(WsLifeCycleEvent.WSRM_RECV_BEFORE_RES_TO_SAF);
            if (verbose) {
               Verbose.say(var2 + " :Inside ConnectionHandler.handleAsync(MessageContext) == 2");
            }

            (new WsrmClientHandler()).handleRequest(var1);
         } else {
            Dispatcher var7 = ((WlMessageContext)var1).getDispatcher();
            int var9 = var4.indexOf(58);
            String var8;
            if (var9 < 0) {
               var8 = "http";
            } else {
               var8 = var4.substring(0, var9);
            }

            verifySSLAdapterOnMessageContextIfNeeded(var1);

            Connection var10;
            try {
               var10 = ConnectionFactory.instance().createClientConnection(var8, var7.getWsdlPort().getBinding().getBindingType());
            } catch (ConnectionException var16) {
               throw new InvocationException("Failed to create a connection", var16);
            }

            Transport var11 = var7.getConnection().getTransport();
            if (var11 instanceof JmsServerQueueTransport && ((JmsServerQueueTransport)var11).isBytesMessage()) {
               var1.setProperty("weblogic.wsee.transport.jms.messagetype", "BytesMessage");
            }

            try {
               SOAPMessage var12 = ((SoapMessageContext)var1).getMessage();
               MimeHeaders var13 = var12.getMimeHeaders();
               var13.removeHeader("Content-Length");
               var10.send(var1);
            } catch (IOException var15) {
               throw new InvocationException("Failed to send message using connection:" + var10, var15);
            }

            try {
               var10.getTransport().confirmOneway();
            } catch (IOException var14) {
               throw new InvocationException("Oneway failed", var14);
            }

            var1.setProperty("weblogic.wsee.oneway.confirmed", "true");
         }

         if (verbose) {
            Verbose.log((Object)(var2 + " :Exiting ConnectionHandler.handleAsync(MessageContext)"));
         }

         return true;
      }
   }

   public static void verifySSLAdapterOnMessageContextIfNeeded(MessageContext var0) {
      String var1 = (String)var0.getProperty("javax.xml.rpc.service.endpoint.address");
      if (var1 != null) {
         if (var1.startsWith("https:")) {
            if (verbose) {
               Verbose.log((Object)"Server ConnectionHandler verifying we have an SSLAdapter to handle an HTTPS connection for delivering a message (async response or RM protocol message");
            }

            if (var0.getProperty("weblogic.wsee.client.ssladapter") != null) {
               if (verbose) {
                  Verbose.log((Object)"Server ConnectionHandler found an existing SSLAdapter to handle an HTTPS connection for delivering a message (async response or RM protocol message)");
               }

               return;
            }

            if (verbose) {
               Verbose.log((Object)"Server ConnectionHandler will *ADD* a new SSLAdapter to handle an HTTPS connection for delivering a message (async response or RM protocol message)");
            }

            KeyStoreConfigurationHelper var2 = new KeyStoreConfigurationHelper(MBeanKeyStoreConfiguration.getInstance());
            KeyStoreInfo var3 = var2.getIdentityKeyStore();
            if (var3 == null) {
               throw new JAXRPCException("Couldn't get KeyStoreConfigurationHelper");
            }

            String var4 = var3.getFileName();
            if (var4 == null || var4.length() == 0) {
               throw new JAXRPCException("KeyStoreFilename not supplied");
            }

            String var5 = var3.getType();
            char[] var6 = var3.getPassPhrase();
            String var7 = var2.getIdentityAlias();
            char[] var8 = var2.getIdentityPrivateKeyPassPhrase();
            if (var7 == null || var7.length() == 0) {
               throw new JAXRPCException("Certificate Alias not supplied");
            }

            if (var8 == null) {
               throw new JAXRPCException("PassPhrase not supplied");
            }

            if (verbose) {
               Verbose.log((Object)("KeyStore File:  " + var4));
               Verbose.log((Object)("KeyStore Type:  " + var5));
               Verbose.log((Object)("KeyStore Alias: " + var7));
            }

            WlsSSLAdapter var9 = new WlsSSLAdapter();

            try {
               var9.setKeystore(var4, var6, var5);
               var9.setClientCert(var7, var8);
            } catch (Exception var11) {
               throw new JAXRPCException(var11);
            }

            var0.setProperty("weblogic.wsee.client.ssladapter", var9);
         }

      }
   }

   public boolean handleClosure(MessageContext var1) {
      if (var1.containsProperty("weblogic.wsee.conversation.id.msg")) {
         return this.handleResponse(var1);
      } else {
         if (var1.getProperty("weblogic.wsee.reliable.oneway.reply") != null) {
            this.send(var1);
         }

         WlMessageContext var2 = (WlMessageContext)var1;
         if (var1.getProperty("weblogic.wsee.security.fault") != null && Boolean.TRUE.equals(var1.getProperty("weblogic.wsee.security.fault")) && (var1.getProperty("weblogic.wsee.addressing.ReplyTo") == null || var1.getProperty("weblogic.wsee.reply.anonymous") != null) && var2.getDispatcher().getOperation().getType() != 1 && var2.getDispatcher().getOperation().getType() != 3) {
            this.send(var1);
         }

         return true;
      }
   }

   public boolean handleFault(MessageContext var1) {
      WlMessageContext var2 = WlMessageContext.narrow(var1);
      if (verbose && var2.getFault() != null) {
         Verbose.log("Webservice invoke failed", var2.getFault());
      }

      if (var1.getProperty("weblogic.wsee.reliable.oneway.reply") != null) {
         this.send(var1);
         return true;
      } else {
         EndpointReference var3 = (EndpointReference)var1.getProperty("weblogic.wsee.faultto.override");
         if (var3 == null) {
            var3 = (EndpointReference)var1.getProperty("weblogic.wsee.addressing.FaultTo");
         }

         AddressingProvider var4 = AddressingProviderFactory.getInstance().getAddressingProvider((MessageContext)var2);
         if (var3 != null && !var4.isAnonymousReferenceURI(var3.getAddress())) {
            return this.handleAsync(var1);
         } else {
            this.send(var1);
            return true;
         }
      }
   }

   public boolean handleRequest(MessageContext var1) {
      boolean var13;
      boolean var10000 = var13 = _WLDF$INST_FLD_Webservices_JAXRPC_Diagnostic_Request_Action_Around_Low.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var14 = null;
      DiagnosticActionState[] var15 = null;
      Object var12 = null;
      if (var10000) {
         Object[] var8 = null;
         if (_WLDF$INST_FLD_Webservices_JAXRPC_Diagnostic_Request_Action_Around_Low.isArgumentsCaptureNeeded()) {
            var8 = InstrumentationSupport.toSensitive(2);
         }

         DynamicJoinPoint var22 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_0, var8, (Object)null);
         DelegatingMonitor var10001 = _WLDF$INST_FLD_Webservices_JAXRPC_Diagnostic_Request_Action_Around_Low;
         DiagnosticAction[] var10002 = var14 = var10001.getActions();
         InstrumentationSupport.preProcess(var22, var10001, var10002, var15 = InstrumentationSupport.getActionStates(var10002));
      }

      boolean var18 = false;

      try {
         var18 = true;
         WlMessageContext var2 = WlMessageContext.narrow(var1);
         Dispatcher var3 = var2.getDispatcher();

         try {
            var3.getConnection().receive(var1);
         } catch (IOException var19) {
            Throwable var5 = var19.getCause();
            if (var5 == null || !(var5 instanceof VersionMismatchException)) {
               throw SOAPFaultUtil.newWLSOAPFaultException(var1, (String)"Client", "Sender", "Unable to parse the incoming request", (String)null, var19, SOAPFaultUtil.SOAP12_FC_CLIENT_QNAME);
            }

            VersionMismatchException var6 = (VersionMismatchException)var5;
            SOAPFaultUtil.throwVersionMismatchException(var6.getMessage());
         }

         if (((SoapMessageContext)var1).hasFault()) {
            var1.setProperty("weblogic.wsee.ignore.fault", "true");
         }

         String var4 = var3.getConnection().getTransport().getServiceURI();
         if (var4 != null) {
            var2.setProperty("weblogic.wsee.connection.end_point_uri", var4);
         }

         String var21 = var3.getConnection().getTransport().getEndpointAddress();
         if (var21 != null) {
            var2.setProperty("weblogic.wsee.connection.end_point_address", var21);
         }

         this.populateTransportInfo(var2, var3.getConnection().getTransport());
         if (verbose) {
            Verbose.log((Object)("Processing request " + var1));
         }

         var10000 = true;
         var18 = false;
      } finally {
         if (var18) {
            boolean var23 = false;
            if (var13) {
               InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_0, _WLDF$INST_FLD_Webservices_JAXRPC_Diagnostic_Request_Action_Around_Low, var14, var15);
            }

         }
      }

      if (var13) {
         InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_0, _WLDF$INST_FLD_Webservices_JAXRPC_Diagnostic_Request_Action_Around_Low, var14, var15);
      }

      return var10000;
   }

   private void populateTransportInfo(WlMessageContext var1, Transport var2) {
      if (var2 instanceof HttpServerTransport) {
         HttpServerTransport var3 = (HttpServerTransport)var2;
         HttpServletRequest var4 = var3.getRequest();
         if (var4 != null) {
            var1.setProperty("weblogic.wsee.transport.servlet.request.secure", var4.isSecure());
            var1.setProperty("weblogic.wsee.transport.client.cert.required", weblogic.wsee.server.servlet.SecurityHelper.isClientCertPresent(var4));
         }
      }

   }

   public QName[] getHeaders() {
      return new QName[0];
   }

   private void send(MessageContext var1) {
      WlMessageContext var2 = WlMessageContext.narrow(var1);
      Dispatcher var3 = var2.getDispatcher();

      try {
         var3.getConnection().send(var1);
      } catch (IOException var5) {
         throw new InvocationException("Failed to send message", var5);
      }
   }

   public String toString() {
      ToStringWriter var1 = new ToStringWriter();
      this.toString(var1);
      return var1.toString();
   }

   public void toString(ToStringWriter var1) {
      var1.start(this);
      var1.end();
   }

   static {
      _WLDF$INST_FLD_Webservices_JAXRPC_Diagnostic_Request_Action_Around_Low = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "Webservices_JAXRPC_Diagnostic_Request_Action_Around_Low");
      _WLDF$INST_JPFLD_0 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "ConnectionHandler.java", "weblogic.wsee.ws.dispatch.server.ConnectionHandler", "handleRequest", "(Ljavax/xml/rpc/handler/MessageContext;)Z", 384, (Map)null, (boolean)0);
      verbose = Verbose.isVerbose(ConnectionHandler.class);
   }
}
