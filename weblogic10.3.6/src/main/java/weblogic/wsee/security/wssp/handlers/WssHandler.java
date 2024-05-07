package weblogic.wsee.security.wssp.handlers;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javax.xml.namespace.QName;
import javax.xml.rpc.JAXRPCException;
import javax.xml.rpc.handler.GenericHandler;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.utils.Debug;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.message.soap.SoapMessageContext;
import weblogic.wsee.monitoring.WsspStats;
import weblogic.wsee.policy.framework.NormalizedExpression;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.policy.util.PolicySelectionPreference;
import weblogic.wsee.security.configuration.TimestampConfiguration;
import weblogic.wsee.security.configuration.WssConfigurationException;
import weblogic.wsee.security.policy.WssPolicyContext;
import weblogic.wsee.security.util.FaultUtils;
import weblogic.wsee.security.wss.SecurityPolicyArchitect;
import weblogic.wsee.security.wss.SecurityPolicyException;
import weblogic.wsee.security.wss.SecurityPolicyInspector;
import weblogic.wsee.security.wss.plan.SecurityPolicyOutlineDescriber;
import weblogic.wsee.security.wss.plan.SecurityPolicyPlan;
import weblogic.wsee.security.wss.policy.SecurityPolicyInspectionException;
import weblogic.wsee.util.StringUtil;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.ws.WsService;
import weblogic.xml.crypto.api.MarshalException;
import weblogic.xml.crypto.encrypt.api.XMLEncryptionException;
import weblogic.xml.crypto.wss.BinarySecurityTokenHandler;
import weblogic.xml.crypto.wss.WSSConstants;
import weblogic.xml.crypto.wss.WSSecurityContext;
import weblogic.xml.crypto.wss.WSSecurityException;
import weblogic.xml.crypto.wss.api.NonceValidator;
import weblogic.xml.crypto.wss.nonce.NonceValidatorFactory;
import weblogic.xml.crypto.wss.provider.CredentialProvider;
import weblogic.xml.crypto.wss.provider.SecurityTokenHandler;
import weblogic.xml.crypto.wss11.internal.SecurityBuilderImpl;
import weblogic.xml.crypto.wss11.internal.SecurityValidator;
import weblogic.xml.crypto.wss11.internal.SecurityValidatorFactory;
import weblogic.xml.crypto.wss11.internal.WSS11Constants;
import weblogic.xml.crypto.wss11.internal.WSS11Context;
import weblogic.xml.crypto.wss11.internal.bst.BSTHandler;

public abstract class WssHandler extends GenericHandler {
   private static final boolean verbose = Verbose.isVerbose(WssHandler.class);
   public static final String VERBOSE_PROPERTY = "weblogic.wsee.security.WssHandler";
   public static final boolean VERBOSE = Boolean.getBoolean("weblogic.wsee.security.WssHandler");
   private static QName[] headers;

   public QName[] getHeaders() {
      return headers;
   }

   protected abstract boolean processRequest(SOAPMessageContext var1) throws SecurityPolicyException, PolicyException, SOAPException, WSSecurityException;

   protected abstract boolean processResponse(SOAPMessageContext var1) throws SecurityPolicyException, PolicyException, SOAPException, WSSecurityException;

   public boolean handleRequest(MessageContext var1) {
      List var2 = WssHandlerListener.retreive(var1);
      WssHandlerListener var4;
      if (var2 != null) {
         Iterator var3 = var2.iterator();

         while(var3.hasNext()) {
            var4 = (WssHandlerListener)var3.next();
            if (var4.isDisposed()) {
               var3.remove();
            } else {
               try {
                  var4.preHandlingRequest(var1);
               } catch (Exception var10) {
               }
            }
         }
      }

      if (VERBOSE) {
         Debug.say(this.getClass() + ".handleRequest");
      }

      if (!(var1 instanceof SOAPMessageContext)) {
         return true;
      } else {
         SOAPMessageContext var17 = (SOAPMessageContext)var1;
         this.copyEndpointAddress(var17);
         var4 = null;
         WsspStats var5 = this.getWsspStats(var17);

         try {
            try {
               this.processRequest(var17);
            } catch (SecurityPolicyException var11) {
               var5.reportPolicyFault();
               FaultUtils.setSOAPFault(var11, (SoapMessageContext)var17);
               return false;
            } catch (PolicyException var12) {
               var5.reportPolicyFault();
               FaultUtils.setSOAPFault(var12, (SoapMessageContext)var17);
               return false;
            } catch (SecurityPolicyInspectionException var13) {
               this.reportWSSErrorToWsspStats(var5, var17, var13);
               FaultUtils.setSOAPFault(var13, (SoapMessageContext)var17);
               return false;
            } catch (WSSecurityException var14) {
               WSSecurityException var6 = var14;
               this.reportWSSErrorToWsspStats(var5, var17, var14);
               if (!FaultUtils.isDebug()) {
                  var6 = new WSSecurityException("Unknown exception, internal system processing error.");
               }

               FaultUtils.setSOAPFault(var6, (SoapMessageContext)var17);
               return false;
            } catch (SOAPException var15) {
               var5.reportGeneralFault();
               throw new JAXRPCException(var15);
            }
         } catch (SOAPException var16) {
            throw new JAXRPCException(var4);
         }

         var2 = WssHandlerListener.retreive(var1);
         if (var2 != null) {
            Iterator var18 = var2.iterator();

            while(var18.hasNext()) {
               WssHandlerListener var7 = (WssHandlerListener)var18.next();
               if (var7.isDisposed()) {
                  var18.remove();
               } else {
                  try {
                     var7.postHandlingRequest(var1);
                  } catch (Exception var9) {
                  }
               }
            }
         }

         return true;
      }
   }

   public boolean handleResponse(MessageContext var1) {
      List var2 = WssHandlerListener.retreive(var1);
      WssHandlerListener var4;
      if (var2 != null) {
         Iterator var3 = var2.iterator();

         while(var3.hasNext()) {
            var4 = (WssHandlerListener)var3.next();
            if (var4.isDisposed()) {
               var3.remove();
            } else {
               try {
                  var4.preHandlingResponse(var1);
               } catch (Exception var15) {
               }
            }
         }
      }

      var1.setProperty("weblogic.wsee.security.fault", true);
      if (VERBOSE) {
         Debug.say(this.getClass() + ".handleResponse");
      }

      if (!(var1 instanceof SOAPMessageContext)) {
         return true;
      } else {
         SOAPMessageContext var16 = (SOAPMessageContext)var1;
         var4 = null;
         WsspStats var5 = this.getWsspStats(var16);

         try {
            try {
               this.processResponse(var16);
            } catch (SecurityPolicyException var10) {
               var5.reportPolicyFault();
               setFault(var10, var16);
            } catch (PolicyException var11) {
               var5.reportPolicyFault();
               setFault(var11, var16);
               return false;
            } catch (WSSecurityException var12) {
               this.reportWSSErrorToWsspStats(var5, var16, var12);
               setFault(var12, (SoapMessageContext)var16);
               return false;
            } catch (SOAPException var13) {
               var5.reportGeneralFault();
               throw new JAXRPCException(var13);
            }
         } catch (SOAPException var14) {
            throw new JAXRPCException(var4);
         }

         var2 = WssHandlerListener.retreive(var1);
         if (var2 != null) {
            Iterator var6 = var2.iterator();

            while(var6.hasNext()) {
               WssHandlerListener var7 = (WssHandlerListener)var6.next();
               if (var7.isDisposed()) {
                  var6.remove();
               } else {
                  try {
                     var7.postHandlingResponse(var1);
                  } catch (Exception var9) {
                  }
               }
            }
         }

         return true;
      }
   }

   protected void copyEndpointAddress(SOAPMessageContext var1) {
      String var2 = (String)var1.getProperty("javax.xml.rpc.service.endpoint.address");
      var1.setProperty("weblogic.wsee.security.wss.end_point_url", var2);
   }

   private static void setFault(Exception var0, SOAPMessageContext var1) throws SOAPException {
      FaultUtils.setSOAPFault(var0, (SoapMessageContext)var1);
      var1.setProperty("weblogic.wsee.security.fault", true);
   }

   protected SecurityPolicyArchitect getSecurityPolicyDriver(SOAPMessageContext var1) throws SOAPException, WSSecurityException {
      WSS11Context var2 = this.setupSecurityContext(var1);
      SecurityBuilderImpl var3 = new SecurityBuilderImpl(var2);
      return new SecurityPolicyArchitect(var3, var2);
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

   protected WSS11Context setupSecurityContext(SOAPMessageContext var1) throws SOAPException, WSSecurityException {
      WSS11Context var2 = this.getSecurityContext(var1);
      this.fillTokenHandlers(var1, var2);
      this.fillCredentialProviders(var1, var2);
      this.initWsscConfigParams(var1);
      return var2;
   }

   private void initWsscConfigParams(SOAPMessageContext var1) {
      WssPolicyContext var2 = this.getPolicyContext(var1);
      PolicySelectionPreference var3 = var2.getWssConfiguration().getPolicySelectionPreference();
      if (null != var3 && null == var1.getProperty("weblogic.wsee.policy.selection.preference")) {
         var1.setProperty("weblogic.wsee.policy.selection.preference", var3);
      }

   }

   private static final void updateProperty(SOAPMessageContext var0, String var1, Object var2) {
      if (var0.getProperty(var1) == null) {
         var0.setProperty(var1, var2);
      }

   }

   protected WSS11Context getSecurityContext(SOAPMessageContext var1) throws SOAPException {
      WSS11Context var2 = (WSS11Context)WSSecurityContext.getSecurityContext(var1);
      Object var3 = var1.getMessage().getSOAPHeader();
      if (var3 == null) {
         var3 = createSOAPHeader(var1);
      }

      if (var2 == null) {
         var2 = new WSS11Context((Node)var3, (Node)null, (Set)null, (Map)null);
      } else {
         var2.init((Node)var3, (Node)null, (Set)null, (Map)null);
      }

      var1.setProperty("weblogic.xml.crypto.wss.WSSecurityContext", var2);
      var2.setProperty("javax.xml.rpc.handler.MessageContext", var1);
      return var2;
   }

   private static Node createSOAPHeader(SOAPMessageContext var0) throws SOAPException {
      return var0.getMessage().getSOAPPart().getEnvelope().addHeader();
   }

   protected void fillCredentialProviders(SOAPMessageContext var1, WSSecurityContext var2) throws WSSecurityException {
      WssPolicyContext var3 = this.getPolicyContext(var1);
      List var4 = var3.getWssConfiguration().getCredentialProviders();
      Iterator var5 = var4.iterator();

      while(var5.hasNext()) {
         CredentialProvider var6 = (CredentialProvider)var5.next();
         var2.setCredentialProvider(var6);
      }

   }

   protected void fillTokenHandlers(SOAPMessageContext var1, WSSecurityContext var2) throws WssConfigurationException {
      WssPolicyContext var3 = this.getPolicyContext(var1);
      List var4 = var3.getWssConfiguration().getTokenHandlers();
      if (null != var4) {
         Iterator var5 = var4.iterator();

         while(var5.hasNext()) {
            Object var6 = (SecurityTokenHandler)var5.next();
            if (null != var6) {
               if (var2 instanceof WSS11Context && var6.getClass() == BinarySecurityTokenHandler.class) {
                  BinarySecurityTokenHandler var7 = (BinarySecurityTokenHandler)var6;
                  boolean var8 = var7.isAuthorizationToken();
                  BSTHandler var11 = new BSTHandler();
                  var11.setAuthorizationToken(var8);
                  var6 = var11;
               }

               var2.setTokenHandler((SecurityTokenHandler)var6);
            } else if (VERBOSE) {
               Debug.say("Found a null Security Token from weblogic.xml.crypto.wss.provider.SecurityTokenHandler");
            }
         }
      }

      TimestampConfiguration var9 = var3.getWssConfiguration().getTimestampConfig();
      if (var9 == null) {
         var9 = new TimestampConfiguration();
      }

      var2.setTimestampHandler(var9);
      NonceValidator var10 = NonceValidatorFactory.getInstance(var9.getMessageAge());
      Integer var12 = (Integer)var1.getProperty("weblogic.wsee.security.message_age");
      if (var12 == null) {
         var1.setProperty("weblogic.wsee.security.message_age", new Integer(var9.getMessageAge()));
      } else {
         var10.setExpirationTime(var12);
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

   protected void preValidate(SOAPMessageContext var1, boolean var2) throws SOAPException, MarshalException, WSSecurityException {
      WSS11Context var3 = this.getSecurityContext(var1);
      SecurityValidator var4 = SecurityValidatorFactory.getSecurityValidator(var3);
      HashMap var5 = new HashMap();
      SecurityPolicyOutlineDescriber var6 = new SecurityPolicyOutlineDescriber(var4);
      var6.sketchPolicyOutline(var1.getMessage(), var5, var2, var3);
      SecurityPolicyPlan var7 = var6.getPolicyOutline();
      var3.setPolicyOutline(var7);
   }

   protected void postValidate(NormalizedExpression var1, SOAPMessageContext var2, boolean var3) throws SOAPException, MarshalException, XMLEncryptionException, SecurityPolicyException, WSSecurityException, PolicyException {
      WSS11Context var4 = this.getSecurityContext(var2);
      SecurityValidator var5 = SecurityValidatorFactory.getSecurityValidator(var4);
      SecurityPolicyInspector var6 = new SecurityPolicyInspector(var5, var4);
      if (var3) {
         var6.processRequestOutbound(var1, var2);
      } else {
         var6.processResponseOutbound(var1, var2);
      }

   }

   protected WsspStats getWsspStats(SOAPMessageContext var1) {
      WlMessageContext var2 = WlMessageContext.narrow(var1);
      return var2 != null && var2.getDispatcher() != null && var2.getDispatcher().getWsPort().getWsspStats() != null ? var2.getDispatcher().getWsPort().getWsspStats() : new WsspStats() {
         public void reportAuthenticationSuccess() {
         }

         public void reportAuthenticationViolation() {
         }

         public void reportAuthorizationSuccess() {
         }

         public void reportAuthorizationViolation() {
         }

         public void reportConfidentialitySuccess() {
         }

         public void reportConfidentialityViolation() {
         }

         public void reportIntegritySuccess() {
         }

         public void reportIntegrityViolation() {
         }

         public void reportPolicyFault() {
         }

         public void reportGeneralFault() {
         }
      };
   }

   protected void reportWSSErrorToWsspStats(WsspStats var1, SOAPMessageContext var2, WSSecurityException var3) {
      assert var1 != null;

      if (var3 instanceof SecurityPolicyInspectionException) {
         SecurityPolicyInspectionException var7 = (SecurityPolicyInspectionException)var3;
         int var8 = var7.getErrorCode();
         if (var8 >= 1000 && var8 < 2000) {
            var1.reportAuthenticationViolation();
         } else if ((var8 < 3000 || var8 >= 4000) && (var8 < 6300 || var8 >= 4000) && (var8 < 7000 || var8 >= 8000)) {
            if (var8 >= 4000 && var8 < 6000) {
               var1.reportConfidentialityViolation();
            } else {
               var1.reportPolicyFault();
            }
         } else {
            var1.reportIntegrityViolation();
         }

      } else {
         if (var3.getFaultCode() != null) {
            QName var4 = var3.getFaultCode();
            if (WSSConstants.FAILURE_AUTH.equals(var4)) {
               var1.reportAuthenticationViolation();
            } else if (!WSSConstants.FAILURE_INVALID.equals(var4) && !WSSConstants.FAILURE_VERIFY_OR_DECRYPT.equals(var4)) {
               if (!WSSConstants.FAILURE_TOKEN_INVALID.equals(var4) && !WSSConstants.FAILURE_TOKEN_UNAVAILABLE.equals(var4)) {
                  var1.reportGeneralFault();
               } else {
                  var1.reportGeneralFault();
               }
            } else if (var3.getCause() instanceof XMLEncryptionException) {
               var1.reportConfidentialityViolation();
            } else if (!StringUtil.isEmpty(var3.getMessage())) {
               String var5 = var3.getMessage().toUpperCase(Locale.ENGLISH);
               if (var5.indexOf("SIGNATURE") > 0) {
                  var1.reportIntegrityViolation();
               } else if (var5.indexOf("ENCRYPTED") <= 0 && var5.indexOf("ENCRYPT") <= 0) {
                  var1.reportGeneralFault();
               } else {
                  var1.reportConfidentialityViolation();
               }
            } else {
               var1.reportGeneralFault();
            }
         } else if (!StringUtil.isEmpty(var3.getMessage())) {
            String var6 = var3.getMessage().toUpperCase(Locale.ENGLISH);
            if (var6.indexOf("SIGNATURE") > 0) {
               var1.reportIntegrityViolation();
            } else if (var6.indexOf("ENCRYPTED") <= 0 && var6.indexOf("ENCRYPT") <= 0) {
               if (var3.getCause() instanceof SecurityPolicyException) {
                  var1.reportPolicyFault();
               } else {
                  var1.reportGeneralFault();
               }
            } else {
               var1.reportConfidentialityViolation();
            }
         } else {
            var1.reportGeneralFault();
         }

      }
   }

   protected void reportInboundWSSSuccessToWsspStats(WsspStats var1, SOAPMessageContext var2) {
      assert var1 != null;

      WSS11Context var3 = (WSS11Context)var2.getProperty("weblogic.xml.crypto.wss.WSSecurityContext");

      assert var3 != null;

      SecurityPolicyPlan var4 = (SecurityPolicyPlan)var3.getPolicyOutline();
      if (var4 != null && var4.getBuildingPlan() > 0) {
         if (var4.getEncryptionPolicy() != null && var4.getEncryptionPolicy().isEncryptionRequired()) {
            var1.reportConfidentialitySuccess();
         }

         if (var4.getSigningPolicy() != null && var4.getSigningPolicy().isSignatureRequired()) {
            var1.reportIntegritySuccess();
         }
      }

   }

   protected void reportOutboundWSSSuccessToWsspStats(WsspStats var1, SOAPMessageContext var2) {
      assert var1 != null;

      WSS11Context var3 = (WSS11Context)var2.getProperty("weblogic.xml.crypto.wss.WSSecurityContext");

      assert var3 != null;

      SecurityPolicyPlan var4 = (SecurityPolicyPlan)var3.getPolicyOutline();
      if (var4 != null && var4.getBuildingPlan() > 0) {
         if (var4.getEncryptionPolicy() != null && var4.getEncryptionPolicy().isEncryptionRequired()) {
            var1.reportConfidentialitySuccess();
         }

         if (var4.getSigningPolicy() != null && var4.getSigningPolicy().isSignatureRequired()) {
            var1.reportIntegritySuccess();
         }
      }

   }

   static {
      headers = new QName[]{WSSConstants.SECURITY_QNAME, WSS11Constants.ENC_HEADER_QNAME};
   }
}
