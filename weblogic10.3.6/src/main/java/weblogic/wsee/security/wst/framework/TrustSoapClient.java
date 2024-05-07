package weblogic.wsee.security.wst.framework;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import org.w3c.dom.Node;
import weblogic.wsee.connection.ConnectionException;
import weblogic.wsee.message.soap.SoapMessageContext;
import weblogic.wsee.policy.framework.NormalizedExpression;
import weblogic.wsee.security.policy12.assertions.TransportBinding;
import weblogic.wsee.security.policy12.assertions.TransportToken;
import weblogic.wsee.security.saml.SAMLUtils;
import weblogic.wsee.security.wst.faults.InvalidRequestException;
import weblogic.wsee.security.wst.faults.WSTFaultException;
import weblogic.wsee.security.wst.faults.WSTFaultUtil;
import weblogic.wsee.security.wst.helpers.SOAPHelper;
import weblogic.wsee.util.Verbose;
import weblogic.xml.crypto.wss.SecurityTokenHelper;
import weblogic.xml.crypto.wss.WSSecurityContext;
import weblogic.xml.crypto.wss.provider.SecurityToken;

public class TrustSoapClient {
   private static final String TRANSPORT_JMS = "jms";
   private static final String TRANSPORT_HTTPS = "https";
   private static final boolean verbose = Verbose.isVerbose(TrustSoapClient.class);
   private WSTContext wstCtx;
   private TrustRequestor trustRequestor;
   private String transport = "http";
   private String binding = "SOAP11";
   private boolean isWsspEnabled;
   private static final boolean DEBUG = false;

   public TrustSoapClient(WSTContext var1) throws InvalidRequestException {
      this.isWsspEnabled = var1.isWssp();
      this.wstCtx = var1;
      if (this.isSoap12()) {
         this.binding = "SOAP12";
      }

      TrustRequestorFactory var2 = TrustRequestorFactory.getInstance();
      this.trustRequestor = var2.createTrustRequestor(var1.getTrustVersion());
   }

   private boolean isSoap12() {
      if (this.wstCtx.getSoapVersion() != null) {
         return "http://www.w3.org/2003/05/soap-envelope".equals(this.wstCtx.getSoapVersion());
      } else {
         return ((SoapMessageContext)this.wstCtx.getMessageContext()).isSoap12();
      }
   }

   public void setTransport(String var1) {
      this.transport = var1;
   }

   public void setBinding(String var1) {
      this.binding = var1;
   }

   public SOAPMessage cancelTrustToken(SoapMessageContext var1, String var2, String var3) throws WSTFaultException {
      TrustToken var4 = this.generateTrustToken(var1, var2, var3);
      Node var5 = this.trustRequestor.cancelRequestSecurityToken(var4, this.wstCtx);
      return this.invoke(var1, var5);
   }

   public SOAPMessage renewTrustToken(SoapMessageContext var1, String var2, String var3) throws WSTFaultException {
      TrustToken var4 = this.generateTrustToken(var1, var2, var3);
      Node var5 = this.trustRequestor.renewRequestSecurityToken(var4, this.wstCtx);
      return this.invoke(var1, var5);
   }

   public SOAPMessage requestTrustToken() throws WSTFaultException {
      try {
         SoapMessageContext var1 = SOAPHelper.createEmptyRSTBaseMsgContext(this.isSoap12());
         SOAPHelper.initTrustMsgCtxProperties(this.wstCtx, var1);
         this.updateTransport();
         Node var2 = this.trustRequestor.newRequestSecurityToken(this.wstCtx);
         SOAPMessage var3 = this.invoke(var1, var2);
         SOAPHelper.updateCookies(this.wstCtx.getMessageContext(), var1);
         return var3;
      } catch (SOAPException var4) {
         WSTFaultUtil.raiseFault(new InvalidRequestException(var4.getMessage()));
         return null;
      }
   }

   private SOAPMessage invoke(SoapMessageContext var1, Node var2) {
      try {
         SOAPHelper.createRSTBaseMsgContext(var2, this.wstCtx, var1);
         if (this.isWsspEnabled) {
            SOAPHelper.invokeWsspHandler(var1, this.transport, this.binding, this.wstCtx.getBootstrapPolicy() != null);
         } else {
            SOAPHelper.invokeHandlers(var1, this.transport, this.binding);
         }

         return var1.getMessage();
      } catch (ConnectionException var5) {
         var5.printStackTrace(System.out);
         WSTFaultUtil.raiseFault(new InvalidRequestException(var5.getMessage()));
      } catch (SOAPException var6) {
         var6.printStackTrace(System.out);
         WSTFaultUtil.raiseFault(new InvalidRequestException(var6.getMessage()));
      } catch (IOException var7) {
         var7.printStackTrace(System.out);
         String var4 = var7.getMessage();
         if (var4.indexOf("unknown protocol: jms") > 0) {
            var4 = "STS endpoint must be set on the stub when using jms transport";
         }

         WSTFaultUtil.raiseFault(new InvalidRequestException(var4));
      }

      return null;
   }

   private void updateTransportFromAppliesTo() {
      String var1 = this.wstCtx.getAppliesTo();
      if (var1 != null) {
         if (var1.startsWith("jms")) {
            this.setTransport("jms");
         } else if (var1.startsWith("https")) {
            this.setTransport("https");
         }
      }

   }

   private boolean updateTransportFromBootstrapPolicy() {
      NormalizedExpression var1 = this.wstCtx.getBootstrapPolicy();
      if (var1 != null) {
         TransportBinding var2 = (TransportBinding)var1.getPolicyAssertion(TransportBinding.class);
         if (var2 != null) {
            TransportToken var3 = var2.getTransportToken();
            if (var3 != null) {
               if (var3.getHttpsToken() != null) {
                  if (verbose) {
                     Verbose.log((Object)" setting Transport to HTTPS from Bootstrap Policy.");
                  }

                  this.setTransport("https");
                  return true;
               }

               if (verbose) {
                  Verbose.log((Object)"TransportToken contains NO HTTPS Token");
               }
            } else if (verbose) {
               Verbose.log((Object)"TransportBinding contains no TransportToken");
            }
         } else if (verbose) {
            Verbose.log((Object)"TransportBinding from bootstrap Policy is NULL.");
         }
      } else if (verbose) {
         Verbose.log((Object)"bootstrap Policy is NULL.");
      }

      return false;
   }

   private void updateTransport() {
      if (!this.updateTransportFromBootstrapPolicy()) {
         this.updateTransportFromAppliesTo();
         if (verbose) {
            Verbose.log((Object)(" set Transport to " + this.transport + " from 'AppliesTo'."));
         }
      }

      if (verbose) {
         Verbose.log((Object)(" Bootstrap Transport value is '" + this.transport + "'"));
      }

   }

   public SecurityToken generateTrustToken(SoapMessageContext var1, NormalizedExpression var2, String var3) {
      SOAPHelper.initTrustMsgCtxProperties(this.wstCtx, var1);
      this.updateTransport();
      SOAPHelper.insertTokenToTrustMessage(var1, (NormalizedExpression)var2);
      WSSecurityContext var4 = WSSecurityContext.getSecurityContext(var1);
      List var5 = this.getEquivalentSecurityTokens(var4, var3);
      if (var5.size() == 1) {
         return (SecurityToken)var5.get(0);
      } else {
         WSTFaultUtil.raiseFault(new InvalidRequestException("Unable to generte Trust Token for token type: " + var3));
         return null;
      }
   }

   public TrustToken generateTrustToken(SoapMessageContext var1, String var2, String var3) {
      SOAPHelper.initTrustMsgCtxProperties(this.wstCtx, var1);
      this.updateTransport();
      SOAPHelper.insertTokenToTrustMessage(var1, (String)var2);
      WSSecurityContext var4 = WSSecurityContext.getSecurityContext(var1);
      SecurityToken[] var5 = SecurityTokenHelper.findSecurityTokenByType(var4, var3);
      if (var5.length == 1 && var5[0] instanceof TrustToken) {
         return (TrustToken)var5[0];
      } else {
         WSTFaultUtil.raiseFault(new InvalidRequestException("Unable to generte Trust Token for token type: " + var3));
         return null;
      }
   }

   private List getEquivalentSecurityTokens(WSSecurityContext var1, String var2) {
      ArrayList var3 = new ArrayList();
      List var4 = var1.getSecurityTokens();
      Iterator var5 = var4.iterator();

      while(var5.hasNext()) {
         SecurityToken var6 = (SecurityToken)var5.next();
         if (this.isEquivalentTokenType(var6.getValueType(), var2)) {
            var3.add(var6);
         }
      }

      return var3;
   }

   private boolean isEquivalentTokenType(String var1, String var2) {
      if (var1 != null && var1.equals(var2)) {
         return true;
      } else {
         return this.isSameKindOfX509ValueType(var1, var2) ? true : SAMLUtils.isEquivalentSamlTokenType(var1, var2);
      }
   }

   private boolean isSameKindOfX509ValueType(String var1, String var2) {
      if (null != var1 && null != var2) {
         int var3 = var1.indexOf("#");
         int var4 = var2.indexOf("#");
         if (var3 != -1 && var4 != -1 && var3 == var4 && var1.length() >= var3 + 6) {
            return var1.substring(0, var3 - 1).equals(var2.substring(0, var4 - 1)) && var1.indexOf("x509-token") != -1 && "#X509".equals(var1.substring(var3, var3 + 5));
         } else {
            return false;
         }
      } else {
         return false;
      }
   }
}
