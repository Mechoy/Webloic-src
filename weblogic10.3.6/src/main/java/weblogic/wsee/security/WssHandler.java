package weblogic.wsee.security;

import java.util.Iterator;
import java.util.List;
import javax.xml.namespace.QName;
import javax.xml.rpc.JAXRPCException;
import javax.xml.rpc.handler.GenericHandler;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.rpc.soap.SOAPFaultException;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import org.w3c.dom.NodeList;
import weblogic.utils.Debug;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.message.soap.SoapMessageContext;
import weblogic.wsee.policy.framework.NormalizedExpression;
import weblogic.wsee.policy.framework.PolicyAlternative;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.policy.runtime.PolicyContext;
import weblogic.wsee.policy.util.PolicySelectionPreference;
import weblogic.wsee.security.configuration.TimestampConfiguration;
import weblogic.wsee.security.configuration.WssConfigurationException;
import weblogic.wsee.security.policy.WssPolicyContext;
import weblogic.wsee.security.util.FaultUtils;
import weblogic.wsee.security.wss.SecurityPolicyDriver;
import weblogic.wsee.security.wss.SecurityPolicyException;
import weblogic.wsee.security.wss.SecurityPolicyValidator;
import weblogic.wsee.security.wss.policy.SecurityPolicyInspectionException;
import weblogic.wsee.ws.WsService;
import weblogic.xml.crypto.wss.SecurityBuilder;
import weblogic.xml.crypto.wss.SecurityBuilderFactory;
import weblogic.xml.crypto.wss.SecurityValidator;
import weblogic.xml.crypto.wss.SecurityValidatorFactory;
import weblogic.xml.crypto.wss.WSSConstants;
import weblogic.xml.crypto.wss.WSSecurityContext;
import weblogic.xml.crypto.wss.WSSecurityException;
import weblogic.xml.crypto.wss.provider.CredentialProvider;
import weblogic.xml.crypto.wss.provider.SecurityTokenHandler;

public abstract class WssHandler extends GenericHandler {
   public static final String VERBOSE_PROPERTY = "weblogic.wsee.security.WssHandler";
   public static final boolean VERBOSE = Boolean.getBoolean("weblogic.wsee.security.WssHandler");
   private static QName[] headers;

   public QName[] getHeaders() {
      return headers;
   }

   public boolean handleRequest(MessageContext var1) throws JAXRPCException, SOAPFaultException {
      if (VERBOSE) {
         Debug.say(this.getClass() + ".handleRequest");
      }

      if (!(var1 instanceof SOAPMessageContext)) {
         return true;
      } else {
         SOAPMessageContext var2 = (SOAPMessageContext)var1;
         this.copyEndpointAddress(var2);
         Object var3 = null;

         try {
            try {
               this.processRequest(var2);
               return true;
            } catch (SecurityPolicyException var5) {
               FaultUtils.setSOAPFault(var5, (SoapMessageContext)var2);
               return false;
            } catch (PolicyException var6) {
               FaultUtils.setSOAPFault(var6, (SoapMessageContext)var2);
               return false;
            } catch (SecurityPolicyInspectionException var7) {
               FaultUtils.setSOAPFault(var7, (SoapMessageContext)var2);
               return false;
            } catch (WSSecurityException var8) {
               WSSecurityException var4 = var8;
               if (!FaultUtils.isDebug()) {
                  var4 = new WSSecurityException("Unknown exception, internal system processing error.");
               }

               FaultUtils.setSOAPFault(var4, (SoapMessageContext)var2);
               return false;
            } catch (SOAPException var9) {
               throw new JAXRPCException(var9);
            }
         } catch (SOAPException var10) {
            throw new JAXRPCException((Throwable)var3);
         }
      }
   }

   protected void copyEndpointAddress(SOAPMessageContext var1) {
      String var2 = (String)var1.getProperty("javax.xml.rpc.service.endpoint.address");
      var1.setProperty("weblogic.wsee.security.wss.end_point_url", var2);
   }

   public boolean handleResponse(MessageContext var1) throws JAXRPCException {
      if (VERBOSE) {
         Debug.say(this.getClass() + ".handleResponse");
      }

      if (!(var1 instanceof SOAPMessageContext)) {
         return true;
      } else {
         SOAPMessageContext var2 = (SOAPMessageContext)var1;
         Object var3 = null;

         try {
            try {
               this.processResponse(var2);
               return true;
            } catch (SecurityPolicyException var5) {
               setFault(var5, var2);
               return false;
            } catch (PolicyException var6) {
               setFault(var6, var2);
               return false;
            } catch (WSSecurityException var7) {
               setFault(var7, (SoapMessageContext)var2);
               return false;
            } catch (SOAPException var8) {
               throw new JAXRPCException(var8);
            }
         } catch (SOAPException var9) {
            throw new JAXRPCException((Throwable)var3);
         }
      }
   }

   private static void setFault(Exception var0, SOAPMessageContext var1) throws SOAPException {
      FaultUtils.setSOAPFault(var0, (SoapMessageContext)var1);
      var1.setProperty("weblogic.wsee.security.fault", true);
   }

   protected SecurityPolicyDriver getSecurityPolicyDriver(SOAPMessageContext var1, PolicyAlternative var2) throws SOAPException, WSSecurityException {
      WSSecurityContext var3 = this.setupSecurityContext(var1, var2);
      SecurityBuilder var4 = SecurityBuilderFactory.newSecurityBuilder(var3);
      return new SecurityPolicyDriver(var4, var3);
   }

   protected SecurityPolicyValidator getSecurityPolicyValidator(SOAPMessageContext var1) throws SOAPException, WSSecurityException {
      WSSecurityContext var2 = this.setupSecurityContext(var1, (PolicyAlternative)null);
      SecurityValidator var3 = SecurityValidatorFactory.newSecurityValidator(var2);
      WssPolicyContext var4 = (WssPolicyContext)var1.getProperty("weblogic.weblogic.wsee.security.policy.WssPolicyCtx");
      SecurityPolicyValidator var5;
      if (var4 == null) {
         var5 = new SecurityPolicyValidator(var3);
      } else {
         TimestampConfiguration var6 = var4.getWssConfiguration().getTimestampConfig();
         var5 = new SecurityPolicyValidator(var3, var6);
      }

      return var5;
   }

   protected static PolicyAlternative getResponsePolicyAlternative(MessageContext var0) throws PolicyException {
      NormalizedExpression var1 = PolicyContext.getResponseEffectivePolicy(var0);
      return var1.getPolicyAlternative();
   }

   protected static PolicyAlternative getRequestPolicyAlternative(MessageContext var0) throws PolicyException {
      NormalizedExpression var1 = PolicyContext.getRequestEffectivePolicy(var0);
      return var1.getPolicyAlternative();
   }

   protected static boolean hasSecurityHeader(SOAPMessageContext var0) throws SOAPException {
      SOAPHeader var1 = var0.getMessage().getSOAPHeader();
      if (var1 == null) {
         return false;
      } else {
         NodeList var2 = var1.getElementsByTagNameNS("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", "Security");
         return var2.getLength() != 0;
      }
   }

   protected WSSecurityContext setupSecurityContext(SOAPMessageContext var1, PolicyAlternative var2) throws SOAPException, WSSecurityException {
      WSSecurityContext var3 = WSSecurityContext.getSecurityContext(var1);
      if (var3 == null) {
         var3 = new WSSecurityContext(var1);
      } else {
         var3.init(var1);
      }

      this.fillTokenHandlers(var1, var3);
      this.fillCredentialProviders(var1, var3, var2);
      return var3;
   }

   protected void fillCredentialProviders(SOAPMessageContext var1, WSSecurityContext var2, PolicyAlternative var3) throws WSSecurityException {
      WssPolicyContext var4 = this.getPolicyContext(var1);
      List var5 = var4.getWssConfiguration().getCredentialProviders();
      Iterator var6 = var5.iterator();

      while(var6.hasNext()) {
         CredentialProvider var7 = (CredentialProvider)var6.next();
         var2.setCredentialProvider(var7);
      }

   }

   protected void fillTokenHandlers(SOAPMessageContext var1, WSSecurityContext var2) throws WssConfigurationException {
      WssPolicyContext var3 = this.getPolicyContext(var1);
      List var4 = var3.getWssConfiguration().getTokenHandlers();
      Iterator var5 = var4.iterator();

      while(var5.hasNext()) {
         SecurityTokenHandler var6 = (SecurityTokenHandler)var5.next();
         var2.setTokenHandler(var6);
      }

      TimestampConfiguration var7 = var3.getWssConfiguration().getTimestampConfig();
      if (var7 == null) {
         var7 = new TimestampConfiguration();
      }

      var2.setTimestampHandler(var7);
      PolicySelectionPreference var8 = var3.getWssConfiguration().getPolicySelectionPreference();
      if (null != var8 && null == var1.getProperty("weblogic.wsee.policy.selection.preference")) {
         var1.setProperty("weblogic.wsee.policy.selection.preference", var8);
      }

   }

   private WssPolicyContext getPolicyContext(SOAPMessageContext var1) {
      WssPolicyContext var2 = (WssPolicyContext)var1.getProperty("weblogic.weblogic.wsee.security.policy.WssPolicyCtx");
      if (var2 == null) {
         WlMessageContext var3 = WlMessageContext.narrow(var1);
         WsService var4 = var3.getDispatcher().getWsPort().getEndpoint().getService();
         var2 = var4.getWssPolicyContext();
         var1.setProperty("weblogic.weblogic.wsee.security.policy.WssPolicyCtx", var2);
      }

      return var2;
   }

   protected abstract boolean processRequest(SOAPMessageContext var1) throws SecurityPolicyException, PolicyException, SOAPException, WSSecurityException;

   protected abstract boolean processResponse(SOAPMessageContext var1) throws SecurityPolicyException, PolicyException, SOAPException, WSSecurityException;

   static {
      headers = new QName[]{WSSConstants.SECURITY_QNAME};
   }
}
