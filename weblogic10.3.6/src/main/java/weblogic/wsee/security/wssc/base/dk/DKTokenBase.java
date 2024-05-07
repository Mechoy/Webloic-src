package weblogic.wsee.security.wssc.base.dk;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.MessageContext;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import weblogic.wsee.security.saml.SAMLToken;
import weblogic.wsee.security.wssc.SecurityTokenBase;
import weblogic.wsee.security.wssc.base.WSCConstantsBase;
import weblogic.wsee.security.wssc.base.faults.WSCFaultException;
import weblogic.wsee.security.wssc.dk.DKCredential;
import weblogic.wsee.security.wst.faults.WSTFaultUtil;
import weblogic.wsee.security.wst.helpers.EncryptedKeyInfoBuilder;
import weblogic.wsee.util.Verbose;
import weblogic.xml.crypto.utils.DOMUtils;
import weblogic.xml.crypto.utils.KeyUtils;
import weblogic.xml.crypto.wss.Base64Encoding;
import weblogic.xml.crypto.wss.SecurityTokenReferenceImpl;
import weblogic.xml.crypto.wss.WSSecurityContext;
import weblogic.xml.crypto.wss.WSSecurityException;
import weblogic.xml.crypto.wss.provider.SecurityToken;
import weblogic.xml.crypto.wss.provider.SecurityTokenHandler;
import weblogic.xml.crypto.wss.provider.SecurityTokenReference;
import weblogic.xml.dom.DOMProcessingException;
import weblogic.xml.dom.marshal.MarshalException;

public abstract class DKTokenBase extends SecurityTokenBase {
   private static final boolean verbose = Verbose.isVerbose(DKTokenBase.class);
   private static final boolean DEBUG = false;
   private DKCredential credential;

   public DKTokenBase() {
   }

   public DKTokenBase(DKCredential var1) {
      this.credential = var1;
   }

   protected abstract QName getDK_ALGORITHM_QNAME();

   protected abstract QName getDK_QNAME();

   protected abstract QName getDK_OFFSET_QNAME();

   protected abstract QName getDK_LENGTH_QNAME();

   protected abstract QName getDK_LABEL_QNAME();

   protected abstract QName getDK_NONCE_QNAME();

   protected abstract QName getDK_GENERATION_QNAME();

   protected abstract String getDK_VALUE_TYPE();

   protected abstract String getURI_P_SHA1();

   protected abstract String getXMLNS_WSS();

   protected abstract String getXMLNS_WSC();

   protected abstract WSCFaultException newBadContextTokenException(String var1);

   protected abstract WSCFaultException newUnknownDerivationSourceException(String var1);

   protected abstract WSCFaultException newUnsupportedContextTokenException(String var1);

   protected Element marshalInternal(Element var1, Node var2, Map var3) throws MarshalException {
      Element var4 = DOMUtils.createElement(var1, this.getDK_QNAME(), "wsc");
      if (this.credential.getAlgorithm() != null) {
         DOMUtils.addAttribute(var4, WSCConstantsBase.DK_ALGORITHM_QNAME, this.credential.getAlgorithm());
      }

      if (this.credential.getTokenReference() != null) {
         this.credential.getTokenReference().marshal(var4, (Node)null, var3);
      }

      Element var5;
      if (this.credential.getGeneration() != -1) {
         var5 = DOMUtils.createAndAddElement(var4, this.getDK_GENERATION_QNAME(), "wsc");
         DOMUtils.addText(var5, Integer.toString(this.credential.getGeneration()));
      }

      if (this.credential.getOffset() != -1) {
         var5 = DOMUtils.createAndAddElement(var4, this.getDK_OFFSET_QNAME(), "wsc");
         DOMUtils.addText(var5, Integer.toString(this.credential.getOffset()));
      }

      if (verbose) {
         Verbose.log((Object)("Marshall DK Credential: Length is: " + this.credential.getLength()));
      }

      if (this.credential.getLength() != -1) {
         var5 = DOMUtils.createAndAddElement(var4, this.getDK_LENGTH_QNAME(), "wsc");
         DOMUtils.addText(var5, Integer.toString(this.credential.getLength()));
      }

      if (this.credential.getLabel() != null) {
         var5 = DOMUtils.createAndAddElement(var4, this.getDK_LABEL_QNAME(), "wsc");
         DOMUtils.addText(var5, this.credential.getLabel());
      }

      if (this.credential.getNonce() != null) {
         var5 = DOMUtils.createAndAddElement(var4, this.getDK_NONCE_QNAME(), "wsc");
         DOMUtils.addText(var5, (new Base64Encoding()).encode(this.credential.getNonce()));
      }

      if (var2 == null) {
         var1.appendChild(var4);
      } else {
         var1.insertBefore(var4, var2);
      }

      return var4;
   }

   protected Element unmarshalInternal(Node var1) throws MarshalException {
      Element var2 = (Element)var1;
      this.credential = new DKCredential();
      this.credential.setAlgorithm(DOMUtils.getAttributeValue(var2, this.getDK_ALGORITHM_QNAME()));
      Element var3 = getElement(var2, this.getXMLNS_WSS(), "SecurityTokenReference");
      if (var3 != null) {
         SecurityTokenReference var4 = SecurityTokenReferenceImpl.createAndUnmarshal(var3);
         this.credential.setTokenReference(var4);
      }

      this.credential.setGeneration(getElementAsInt(var2, this.getXMLNS_WSC(), "Generation"));
      this.credential.setOffset(getElementAsInt(var2, this.getXMLNS_WSC(), "Offset"));
      this.credential.setLength(getElementAsInt(var2, this.getXMLNS_WSC(), "Length"));
      this.credential.setLabel(getElementAsString(var2, this.getXMLNS_WSC(), "Label"));
      if (this.credential.getLabel() == null) {
         this.credential.setLabel("WS-SecureConversationWS-SecureConversation");
      }

      String var5 = getElementAsString(var2, this.getXMLNS_WSC(), "Nonce");
      if (var5 != null) {
         this.credential.setNonce((new Base64Encoding()).decode(var5));
      }

      return var2;
   }

   public Key getSecretKey() {
      return this.credential.getSecretKey();
   }

   public String getValueType() {
      return this.getDK_VALUE_TYPE();
   }

   public Object getCredential() {
      return this.credential;
   }

   public Key getSecretKey(MessageContext var1) {
      if (this.credential == null) {
         return null;
      } else {
         Key var2 = this.credential.getSecretKey();
         if (var2 != null) {
            return var2;
         } else if (this.credential.getAlgorithm() != null && !this.getURI_P_SHA1().equals(this.credential.getAlgorithm())) {
            return null;
         } else {
            WSSecurityContext var3 = WSSecurityContext.getSecurityContext(var1);
            if (var3 == null) {
               return null;
            } else {
               SecurityTokenReference var4 = this.credential.getTokenReference();
               if (var4 == null) {
                  return null;
               } else {
                  SecurityToken var5 = this.credential.getSecurityToken();
                  if (var5 == null) {
                     try {
                        SecurityTokenHandler var6 = var3.getRequiredTokenHandler(var4.getValueType());
                        var5 = var6.getSecurityToken(var4, var1);
                        this.credential.setSecurityToken(var5);
                     } catch (WSSecurityException var14) {
                        Verbose.log("Could not resolve Referenced Token in Derived Key Token", var14);
                        WSTFaultUtil.raiseFault(this.newBadContextTokenException("Could not resolve Referenced Token in Derived Key Token"));
                     }
                  }

                  if (var5 == null) {
                     Verbose.log((Object)"Could not resolve Referenced Token in Derived Key Token");
                     WSTFaultUtil.raiseFault(this.newBadContextTokenException("Could not resolve Referenced Token in Derived Key Token"));
                  }

                  Key var16 = var5.getSecretKey();
                  if (var16 == null) {
                     Verbose.log((Object)"Context token does not have a shared secret that is required for deriving secret keys");
                     WSTFaultUtil.raiseFault(this.newUnknownDerivationSourceException("Context token does not have a shared secret that is required for deriving secret keys"));
                  }

                  EncryptedKeyInfoBuilder.debugKey(var16, "DK Token got sharedSecret from Referenced Token");
                  if (var5 instanceof SAMLToken) {
                     if (verbose) {
                        Verbose.log((Object)"Return with Symetric Key from SAML Token ");
                     }

                     return var16;
                  } else {
                     int var7 = this.credential.getLength() != -1 ? this.credential.getLength() : 32;
                     String var8 = this.credential.getLabel();
                     if (var8 == null) {
                        var8 = (String)var1.getProperty("weblogic.wsee.wssc.dk.label");
                        if (var8 == null) {
                           var8 = "WS-SecureConversation";
                        }
                     }

                     try {
                        byte[] var9 = concatenate(var8.getBytes("UTF-8"), this.credential.getNonce());
                        byte[] var10 = KeyUtils.P_SHA1(var16, var9, var7);
                        SecretKeySpec var15 = new SecretKeySpec(var10, "AES");
                        this.credential.setSecretKey(var15);
                        if (verbose) {
                        }

                        EncryptedKeyInfoBuilder.debugKey(var15, "DK Token set symetric Key onto new credential");
                        return var15;
                     } catch (NoSuchAlgorithmException var11) {
                        WSTFaultUtil.raiseFault(this.newUnsupportedContextTokenException(var11.getMessage()));
                     } catch (UnsupportedEncodingException var12) {
                        WSTFaultUtil.raiseFault(this.newUnsupportedContextTokenException(var12.getMessage()));
                     } catch (InvalidKeyException var13) {
                        WSTFaultUtil.raiseFault(this.newUnsupportedContextTokenException(var13.getMessage()));
                     }

                     return null;
                  }
               }
            }
         }
      }
   }

   private static Element getElement(Element var0, String var1, String var2) {
      try {
         return weblogic.xml.dom.DOMUtils.getOptionalElementByTagNameNS(var0, var1, var2);
      } catch (DOMProcessingException var4) {
         return null;
      }
   }

   private static int getElementAsInt(Element var0, String var1, String var2) {
      Element var3 = getElement(var0, var1, var2);
      return var3 != null ? Integer.parseInt(weblogic.xml.dom.DOMUtils.getTextContent(var3, true)) : -1;
   }

   private static String getElementAsString(Element var0, String var1, String var2) {
      Element var3 = getElement(var0, var1, var2);
      return var3 != null ? weblogic.xml.dom.DOMUtils.getTextContent(var3, true) : null;
   }

   private static final byte[] concatenate(byte[] var0, byte[] var1) {
      if (var0 == null && var1 == null) {
         return null;
      } else if (var0 == null && var1 != null) {
         return var1;
      } else if (var0 != null && var1 == null) {
         return var0;
      } else {
         byte[] var2 = new byte[var0.length + var1.length];
         System.arraycopy(var0, 0, var2, 0, var0.length);
         System.arraycopy(var1, 0, var2, var0.length, var1.length);
         return var2;
      }
   }
}
