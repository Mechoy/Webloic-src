package weblogic.wsee.security.wst.helpers;

import com.sun.xml.ws.util.DOMUtil;
import java.security.InvalidAlgorithmParameterException;
import java.security.Key;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.soap.SOAPException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import weblogic.wsee.message.soap.SoapMessageContext;
import weblogic.wsee.security.saml.SAMLCredential;
import weblogic.wsee.security.wss.plan.helper.SOAPSecurityHeaderHelper;
import weblogic.wsee.security.wst.faults.InvalidRequestException;
import weblogic.wsee.security.wst.faults.WSTFaultUtil;
import weblogic.wsee.security.wst.framework.WSTContext;
import weblogic.wsee.util.Verbose;
import weblogic.xml.crypto.api.KeySelector;
import weblogic.xml.crypto.api.KeySelectorResult;
import weblogic.xml.crypto.api.MarshalException;
import weblogic.xml.crypto.common.keyinfo.EncryptedKeyProvider;
import weblogic.xml.crypto.encrypt.api.EncryptionMethod;
import weblogic.xml.crypto.encrypt.api.XMLEncryptionException;
import weblogic.xml.crypto.encrypt.api.XMLEncryptionFactory;
import weblogic.xml.crypto.encrypt.api.dom.DOMDecryptContext;
import weblogic.xml.crypto.encrypt.api.keyinfo.EncryptedKey;
import weblogic.xml.crypto.encrypt.api.spec.EncryptionMethodParameterSpec;
import weblogic.xml.crypto.wss.SecurityImpl;
import weblogic.xml.crypto.wss.SecurityTokenContextHandler;
import weblogic.xml.crypto.wss.WSSConstants;
import weblogic.xml.crypto.wss.WSSecurityContext;
import weblogic.xml.crypto.wss.WSSecurityException;
import weblogic.xml.crypto.wss.provider.CredentialProvider;
import weblogic.xml.crypto.wss11.internal.STRType;
import weblogic.xml.crypto.wss11.internal.SecurityBuilderImpl;
import weblogic.xml.crypto.wss11.internal.WSS11Context;
import weblogic.xml.crypto.wss11.internal.enckey.EncryptedKeyToken;

public class EncryptedKeyInfoBuilder {
   private static final boolean verbose = Verbose.isVerbose(EncryptedKeyInfoBuilder.class);
   private static final boolean DEBUG = false;
   private WSS11Context securityCtx = null;
   private SecurityTokenContextHandler ctxHandler;
   private Element soapHeader = null;
   protected SecurityBuilderImpl securityBuilder = null;

   public EncryptedKeyInfoBuilder(WSSecurityContext var1, CredentialProvider var2) {
      this.createWSSecurityContext(var1, var2);
      this.ctxHandler = new SecurityTokenContextHandler(this.securityCtx);
      this.securityBuilder = new SecurityBuilderImpl(this.securityCtx);
   }

   private void createWSSecurityContext(WSSecurityContext var1, CredentialProvider var2) {
      try {
         SoapMessageContext var3 = SOAPHelper.createEmptyRSTBaseMsgContext(true);
         this.soapHeader = var3.getMessage().getSOAPHeader();
         this.securityCtx = new WSS11Context(this.soapHeader, (Node)null, (Set)null, (Map)null);
         transferCredntialProviders(var1, this.securityCtx);
         if (null != var2) {
            if (verbose) {
               Verbose.log((Object)("setting CredentialProvider =" + var2.toString()));
            }

            this.securityCtx.addCredentialProvider(var2);
         }
      } catch (SOAPException var4) {
         WSTFaultUtil.raiseFault(new InvalidRequestException("Failed to create WSSecurityContext in trust for building EncryptedKey element."));
      }

   }

   private static void transferCredntialProviders(WSSecurityContext var0, WSS11Context var1) {
      Map var2 = var0.getCredentialProviders();
      Iterator var3 = var2.values().iterator();

      while(var3.hasNext()) {
         var1.setCredentialProvider((CredentialProvider)var3.next());
      }

   }

   private static EncryptionMethod getEncryptionMethod(String var0) throws XMLEncryptionException, InvalidAlgorithmParameterException {
      XMLEncryptionFactory var1 = XMLEncryptionFactory.getInstance();
      return var1.newEncryptionMethod(var0, (Integer)null, (EncryptionMethodParameterSpec)null);
   }

   private static List getSTRTypes(QName var0, String var1) {
      ArrayList var2 = new ArrayList();
      var2.add(new STRType(var0, var1));
      return var2;
   }

   public Node getEncryptedKeyNode(WSTContext var1) throws WSSecurityException {
      Node var10;
      try {
         if (verbose) {
            Verbose.log((Object)"Getting Encrypted Key Node for SAML Issued Token .........");
         }

         EncryptionMethod var2 = getEncryptionMethod("http://www.w3.org/2001/04/xmlenc#rsa-oaep-mgf1p");
         EncryptionMethod var3 = getEncryptionMethod("http://www.w3.org/2001/04/xmlenc#aes256-cbc");
         List var4 = getSTRTypes(WSSConstants.KEY_IDENTIFIER_QNAME, "http://docs.oasis-open.org/wss/oasis-wss-soap-message-security-1.1#ThumbprintSHA1");
         String var5 = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v3";
         Object var6 = null;
         boolean var7 = true;
         Key var8 = this.securityBuilder.getEncryptedKeyToken(var2, var3, var5, var4, (String)var6, var7, this.ctxHandler);
         EncryptedKeyToken var9;
         if (null == var8) {
            if (verbose) {
               Verbose.log((Object)("Unable to get EncryptedKey for tokenType =" + var5));
            }

            var9 = null;
            return var9;
         }

         var9 = (EncryptedKeyToken)this.securityCtx.getSecurityTokens("http://docs.oasis-open.org/wss/oasis-wss-soap-message-security-1.1#EncryptedKey").get(0);
         if (null == var9) {
            if (verbose) {
               Verbose.log((Object)"Unable to build EncryptedKeyToken for tokenType =http://docs.oasis-open.org/wss/oasis-wss-soap-message-security-1.1#EncryptedKey");
            }

            var10 = null;
            return var10;
         }

         var1.setSymmetricKey(var8);
         debugKey(var8, "Created symmetricKey from EncryptedKey for wstCtx ");
         var10 = this.buildEncryptedKeyInfoNode();
      } catch (WSSecurityException var16) {
         throw var16;
      } catch (Exception var17) {
         throw new WSSecurityException(var17);
      } finally {
         this.securityCtx = null;
         this.securityBuilder = null;
         this.soapHeader = null;
         this.ctxHandler = null;
      }

      return var10;
   }

   public static void debugKey(Key var0, String var1) {
      if (verbose) {
         if (var0 == null) {
            Verbose.log((Object)(var1 + " key is null"));
            return;
         }

         Verbose.log((Object)("Key for " + var1 + " Algo =" + var0.getAlgorithm() + " Format = " + var0.getFormat()));
      }

   }

   private Node buildEncryptedKeyInfoNode() throws Exception {
      Element var1 = SOAPSecurityHeaderHelper.getFirstChildElement(this.soapHeader, SecurityImpl.ENCRYPTED_KEY_QNAME);
      Element var2 = DOMUtil.createDom().createElementNS("http://www.w3.org/2000/09/xmldsig#", "KeyInfo");
      var2.setPrefix("sig");
      if (null != var1) {
         var2.appendChild(var2.getOwnerDocument().importNode(var1, true));
      } else if (verbose) {
         Verbose.log((Object)"Unable to find EncryptedKey element!!!!!");
      }

      return var2;
   }

   public static void processEncryptedKey(SAMLCredential var0, MessageContext var1) throws WSSecurityException, MarshalException {
      if (null != var0 && null != var1) {
         Element var2 = var0.getEncryptedKey();
         WSSecurityContext var3 = (WSSecurityContext)var1.getProperty("weblogic.xml.crypto.wss.WSSecurityContext");
         if (null != var2 && null != var3) {
            if (verbose) {
               Verbose.log((Object)"Processing EncryptedKey element to get EncryptedKeyProvider ");
            }

            KeySelector var4 = var3.getKeySelector();
            DOMDecryptContext var5 = new DOMDecryptContext(var4, var2);
            var5.setProperty("javax.xml.rpc.handler.MessageContext", var1);
            var5.setProperty("weblogic.xml.crypto.wss.WSSecurityContext", var3);
            XMLEncryptionFactory var6 = var3.getEncryptionFactory();
            EncryptedKey var7 = (EncryptedKey)var6.unmarshalEncryptedType(var5);
            EncryptedKeyProvider var8 = null;

            try {
               var8 = new EncryptedKeyProvider(var7, var5);
            } catch (XMLEncryptionException var10) {
               if (verbose) {
                  Verbose.log("XMLEncryptionException  when getting EncryptedKeyProvider from EncryptedKey ", var10);
               }

               throw new WSSecurityException(var10, WSSConstants.FAILURE_INVALID);
            }

            var0.setEncryptedKeyProvider(var8);
            KeySelectorResult var9 = var8.getKey("http://www.w3.org/2000/09/xmldsig#hmac-sha1", KeySelector.Purpose.VERIFY);
            var0.setSymmetircKey(var9.getKey());
            if (verbose) {
               Verbose.log((Object)"set EncryptedKeyProvider in SAMLCrential");
            }

            debugKey(var9.getKey(), "Set symmetricKey from input EncryptedKey into SAML Credentail");
         } else {
            throw new IllegalArgumentException("Null EncryptedKey element = " + (null == var2) + " WSSecurityContext securityCtx = null  " + (null == var3));
         }
      } else {
         throw new IllegalArgumentException("Null SAMLCredential = " + (null == var0) + " MessageContext msgCtx = null  is " + (null == var1));
      }
   }
}
