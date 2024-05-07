package weblogic.wsee.security.wst.helpers;

import java.io.IOException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.rpc.JAXRPCException;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.soap.Detail;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import org.w3c.dom.Node;
import weblogic.wsee.addressing.ClientAddressingHandler;
import weblogic.wsee.connection.Connection;
import weblogic.wsee.connection.ConnectionFactory;
import weblogic.wsee.message.soap.SoapMessageContext;
import weblogic.wsee.policy.framework.NormalizedExpression;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.policy.runtime.PolicyServer;
import weblogic.wsee.security.bst.StubPropertyBSTCredProv;
import weblogic.wsee.security.policy.assertions.SecurityPolicyAssertionFactory;
import weblogic.wsee.security.wssp.handlers.WssClientHandler;
import weblogic.wsee.security.wst.faults.InvalidRequestException;
import weblogic.wsee.security.wst.faults.RequestFailedException;
import weblogic.wsee.security.wst.faults.WSTFaultUtil;
import weblogic.wsee.security.wst.framework.WSTContext;
import weblogic.wsee.security.wst.framework.async.AsyncTrustClient;
import weblogic.wsee.security.wst.framework.async.AsyncTrustClientHelper;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.util.WLMessageFactory;
import weblogic.xml.crypto.utils.DOMUtils;
import weblogic.xml.crypto.wss.WSSecurityContext;
import weblogic.xml.crypto.wss11.internal.WSS11Context;

public class SOAPHelper {
   private static final String SCT_CANCEL = "/SCT/Cancel";
   private static final String SCT_RENEW = "/SCT/Renew";
   private static final boolean DEBUG = false;
   private static final boolean verbose = Verbose.isVerbose(SOAPHelper.class);

   public static SoapMessageContext createEmptyRSTBaseMsgContext(boolean var0) throws SOAPException {
      MessageFactory var1 = WLMessageFactory.getInstance().getMessageFactory(var0);
      SOAPMessage var2 = var1.createMessage();
      SoapMessageContext var3 = new SoapMessageContext(var0);
      var3.setMessage(var2);
      return var3;
   }

   public static SoapMessageContext createRSTBaseMsgContext(Node var0, WSTContext var1) throws SOAPException {
      return createRSTBaseMsgContext(var0, var1, createEmptyRSTBaseMsgContext(((SoapMessageContext)var1.getMessageContext()).isSoap12()));
   }

   public static SoapMessageContext createRSTBaseMsgContext(Node var0, WSTContext var1, SoapMessageContext var2) throws SOAPException {
      assert var2 != null;

      SOAPMessage var3 = var2.getMessage();
      SOAPEnvelope var4 = var3.getSOAPPart().getEnvelope();
      declareNamespacesToSOAPEnv(var4, var1);
      SOAPBody var5 = var3.getSOAPBody();
      var0 = var5.getOwnerDocument().importNode(var0, true);
      var5.appendChild(var0);
      return var2;
   }

   public static void updateCookies(MessageContext var0, SoapMessageContext var1) {
      if ((Boolean)var1.getProperty("javax.xml.rpc.session.maintain")) {
         Map var2 = (Map)var1.getProperty("weblogic.wsee.invoke_properties");
         if (var2 != null) {
            MimeHeaders var3 = (MimeHeaders)var2.get("weblogic.wsee.transport.headers");
            if (var3 != null) {
               var0.setProperty("weblogic.wsee.transport.headers", var3);
               var2 = (Map)var0.getProperty("weblogic.wsee.invoke_properties");
               if (var2 != null) {
                  var2.put("weblogic.wsee.transport.headers", var3);
               }
            }
         }
      }

   }

   public static void initTrustMsgCtxProperties(WSTContext var0, SoapMessageContext var1) {
      // $FF: Couldn't be decompiled
   }

   private static void handleFault(SoapMessageContext var0) {
      Throwable var1 = var0.getFault();
      if (var1 != null) {
         throw new JAXRPCException(var1);
      }
   }

   public static void invokeWsspHandler(SoapMessageContext var0, String var1, String var2) throws IOException {
      invokeWsspHandler(var0, var1, var2, false);
   }

   public static void invokeWsspHandler(SoapMessageContext var0, String var1, String var2, boolean var3) throws IOException {
      if (AsyncTrustClientHelper.isAsyncTrustRequired(var0)) {
         AsyncTrustClient.process(var0, var1, var2, var3);
      } else {
         ClientAddressingHandler var4 = new ClientAddressingHandler();
         if (!var4.handleRequest(var0)) {
            handleFault(var0);
         }

         WssClientHandler var5 = null;
         if (var3) {
            var5 = new WssClientHandler();
            if (!var5.handleRequest(var0)) {
               handleFault(var0);
            }
         }

         Connection var6 = ConnectionFactory.instance().createClientConnection(var1, var2);
         var6.send(var0);
         var6.receive(var0);
         if (var3 && !var5.handleResponse(var0)) {
            handleFault(var0);
         }

         if (!var4.handleResponse(var0)) {
            handleFault(var0);
         }

      }
   }

   public static void invokeHandlers(SoapMessageContext var0, String var1, String var2) throws IOException {
      ClientAddressingHandler var3 = new ClientAddressingHandler();
      if (!var3.handleRequest(var0)) {
         handleFault(var0);
      }

      weblogic.wsee.security.WssClientHandler var4 = new weblogic.wsee.security.WssClientHandler();
      NormalizedExpression var5 = (NormalizedExpression)var0.getProperty("weblogic.wsee.policy.effectiveRequestPolicy");
      boolean var6 = SecurityPolicyAssertionFactory.hasSecurityPolicy(var5);
      if (var6) {
         if (!var4.handleRequest(var0)) {
            handleFault(var0);
         }
      } else {
         var1 = "https";
      }

      Connection var7 = ConnectionFactory.instance().createClientConnection(var1, var2);
      var7.send(var0);
      var7.receive(var0);
      if (var6 && !var4.handleResponse(var0)) {
         handleFault(var0);
      }

      if (!var3.handleResponse(var0)) {
         handleFault(var0);
      }

   }

   public static Node getRSTBaseNode(SOAPMessage var0) throws RequestFailedException {
      SOAPBody var1;
      try {
         var1 = var0.getSOAPBody();
         SOAPFault var2 = var1.getFault();
         if (var2 != null) {
            throw new RequestFailedException(getFaultMessage(var2));
         }
      } catch (SOAPException var3) {
         throw new RequestFailedException(var3.getMessage());
      }

      for(Node var4 = var1.getFirstChild(); var4 != null; var4 = var4.getNextSibling()) {
         if (var4.getNodeType() == 1) {
            return var4;
         }
      }

      throw new RequestFailedException("SOAP Body does not contain any elements");
   }

   private static void declareNamespacesToSOAPEnv(SOAPEnvelope var0, WSTContext var1) {
      Set var2 = var1.getNamespaces().entrySet();
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         Map.Entry var4 = (Map.Entry)var3.next();
         DOMUtils.declareNamespace(var0, (String)var4.getKey(), (String)var4.getValue());
      }

   }

   private static String getFaultMessage(SOAPFault var0) {
      Detail var1 = var0.getDetail();
      if (var1 == null) {
         return "";
      } else {
         String var2 = var1.toString();

         for(Node var3 = var1.getFirstChild(); var3 != null; var3 = var3.getNextSibling()) {
            if (var3.getNodeType() == 3) {
               var2 = var3.getNodeValue();
            }
         }

         return var2;
      }
   }

   public static void insertTokenToTrustMessage(MessageContext var0, NormalizedExpression var1) {
      NormalizedExpression var2 = (NormalizedExpression)var0.getProperty("weblogic.wsee.policy.effectiveRequestPolicy");
      if (var1 != null) {
         var0.setProperty("weblogic.wsee.policy.effectiveRequestPolicy", var1);
         WSTContext var3 = WSTContext.getWSTContext(var0);
         boolean var4 = var3.isWssp();
         if (var4) {
            (new weblogic.wsee.security.WssClientHandler()).handleRequest(var0);
         } else {
            (new weblogic.wsee.security.WssClientHandler(false)).handleRequest(var0);
         }
      }

      var0.setProperty("weblogic.wsee.policy.effectiveRequestPolicy", var2);
   }

   public static void insertTokenToTrustMessage(MessageContext var0, String var1) {
      NormalizedExpression var2 = null;

      try {
         PolicyServer var3 = new PolicyServer();
         var2 = var3.getPolicy(var1).normalize();
      } catch (PolicyException var4) {
         throw new IllegalArgumentException("Fatal Error.  Unable to load policy '" + var1 + "'");
      }

      insertTokenToTrustMessage(var0, var2);
   }

   private static void initSecurityContext(MessageContext var0, SoapMessageContext var1, WSSecurityContext var2, boolean var3) {
      Map var5 = WSSecurityContext.getCredentialProviders(var0);
      Object var4;
      Iterator var6;
      if (var5 != null && var5.size() != 0) {
         var4 = new ArrayList();
         var6 = var5.entrySet().iterator();

         while(var6.hasNext()) {
            ((List)var4).add(((Map.Entry)var6.next()).getValue());
         }
      } else {
         var4 = (List)var0.getProperty("weblogic.wsee.security.wss.CredentialProviderList");
      }

      var6 = null;
      Object var9;
      if (var3) {
         var9 = var0.getProperty("weblogic.wsee.security.bst.serverEncryptCert");
      } else {
         var9 = var0.getProperty("weblogic.wsee.security.bst.stsEncryptCert");
         if (null == var9) {
            var9 = var0.getProperty("weblogic.wsee.security.bst.serverEncryptCert");
            if (null != var9 && verbose) {
               Verbose.say("weblogic.wsee.security.bst.serverEncryptCert is  used for the STS Server Certificate");
            }
         } else if (verbose) {
            Verbose.say("weblogic.wsee.security.bst.stsEncryptCert is  used for the STS Server Certificate");
         }
      }

      if (var9 != null) {
         var1.setProperty("weblogic.wsee.security.bst.serverEncryptCert", var9);
         if (verbose) {
            Verbose.say("The Server Encypt Certificate in Trust Msg Ctx is " + var9.toString());
         }

         if (var4 != null && !((List)var4).isEmpty()) {
            var4 = CredentialProviderHelper.replaceCredentialProviderWithNewCert((List)var4, (X509Certificate)var9);
         } else if (var0.getProperty("weblogic.wsee.security.bst.stsEncryptCert") != null) {
            var4 = new ArrayList();
            ((List)var4).add(new StubPropertyBSTCredProv((X509Certificate)var9, (X509Certificate)null));
            if (verbose) {
               Verbose.say("New StubPropertyBSTCredProv is added with Server Cert");
            }
         }
      }

      if (var4 != null) {
         var1.setProperty("weblogic.wsee.security.wss.CredentialProviderList", var4);
         var2.addCredentialProviders((List)var4);
         var1.setProperty("weblogic.xml.crypto.wss.WSSecurityContext", var2);
      }

      Object var7 = var0.getProperty("weblogic.wsee.security.bst.serverVerifyCert");
      if (var7 != null) {
         var1.setProperty("weblogic.wsee.security.bst.serverVerifyCert", var7);
      }

      Object var8 = var0.getProperty("weblogic.wsee.security.wss.TrustManager");
      if (var8 != null) {
         var1.setProperty("weblogic.wsee.security.wss.TrustManager", var8);
      }

   }

   private static WSSecurityContext createWSSecurityContext(SoapMessageContext var0, boolean var1) {
      try {
         if (var1) {
            SOAPHeader var2 = var0.getMessage().getSOAPHeader();
            return new WSS11Context(var2, (Node)null, (Set)null, (Map)null);
         } else {
            return new WSSecurityContext(var0);
         }
      } catch (SOAPException var3) {
         WSTFaultUtil.raiseFault(new InvalidRequestException("Failed to create WSSecurityContext in trust."));
         return null;
      }
   }
}
