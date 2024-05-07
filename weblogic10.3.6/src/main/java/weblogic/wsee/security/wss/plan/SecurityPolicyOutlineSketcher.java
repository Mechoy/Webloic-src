package weblogic.wsee.security.wss.plan;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPBody;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import weblogic.wsee.security.policy.SecurityToken;
import weblogic.wsee.security.saml.SAML2Constants;
import weblogic.wsee.security.saml.SAMLConstants;
import weblogic.wsee.security.saml.SAMLUtils;
import weblogic.wsee.security.wss.plan.helper.TokenReferenceTypeHelper;
import weblogic.wsee.security.wss.plan.helper.TokenTypeHelper;
import weblogic.wsee.security.wss.policy.EncryptionPolicy;
import weblogic.wsee.security.wss.policy.IdentityPolicy;
import weblogic.wsee.security.wss.policy.SecurityPolicyInspectionException;
import weblogic.wsee.security.wss.policy.SignaturePolicy;
import weblogic.wsee.security.wss.policy.TimestampPolicy;
import weblogic.wsee.util.Verbose;
import weblogic.xml.crypto.api.MarshalException;
import weblogic.xml.crypto.dsig.SignedInfoImpl;
import weblogic.xml.crypto.dsig.WLXMLStructure;
import weblogic.xml.crypto.dsig.api.Reference;
import weblogic.xml.crypto.dsig.keyinfo.KeyInfoImpl;
import weblogic.xml.crypto.dsig.keyinfo.KeyInfoObjectBase;
import weblogic.xml.crypto.encrypt.ReferenceList;
import weblogic.xml.crypto.encrypt.WLEncryptedKey;
import weblogic.xml.crypto.encrypt.api.DataReference;
import weblogic.xml.crypto.encrypt.api.ReferenceType;
import weblogic.xml.crypto.utils.DOMUtils;
import weblogic.xml.crypto.wss.SecurityImpl;
import weblogic.xml.crypto.wss.WSSConstants;
import weblogic.xml.crypto.wss.provider.SecurityTokenReference;
import weblogic.xml.crypto.wss11.internal.WSS11Constants;
import weblogic.xml.dom.DOMStreamReader;

public class SecurityPolicyOutlineSketcher {
   private static final boolean verbose = Verbose.isVerbose(SecurityPolicyOutlineSketcher.class);
   private static final boolean debug = false;
   private SecurityPolicyPlan outline;
   private int duplicateFlag = 0;
   public static final int ELM_TIMESTAMP = 1;
   public static final int ELM_USERNAME_TOKEN = 2;
   public static final int ELM_SAML_TOKEN = 4;
   public static final int ELM_ENCRYPTED_KEY = 8;
   public static final int ELM_ENCRYPTED_HEADER = 16;
   public static final int ELM_SIGNATURE = 32;
   public static final int ELM_SIGNATURE_CONFRIM = 64;
   public static final int ELM_REFERCE_LIST = 128;
   public static final int ELM_DERIVED_KEY_TOKEN = 256;
   public static final int ELM_DERIVED_KEY_TOKENS = 768;
   public static final int LAYOUT_TS_FIRST = 1024;
   public static final int LAYOUT_TS_LAST = 2048;
   public static final int LAYOUT_STRICT = 4096;
   public static final int ELM_ENCRYPTED_KEYS = 8200;
   public static final int ELM_NONCE = 16384;
   public static final int ELM_CREATED = 32768;
   private boolean isServiceProvider;
   private Map<String, Reference> signatureReference = new HashMap();
   private Map<String, Reference> endoseSignatureReference = new HashMap();
   private Map<String, DataReference> encryptionReference = new HashMap();
   private Map<String, ReferenceType> encryptionReferenceList = new HashMap();
   private Map<String, Element> bstReferenceList = new HashMap();

   public boolean isServiceProvider() {
      return this.isServiceProvider;
   }

   public void setServiceProvider(boolean var1) {
      this.isServiceProvider = var1;
   }

   private boolean isDuplicateElement(int var1) {
      if ((this.duplicateFlag & var1) == var1) {
         return true;
      } else {
         this.duplicateFlag |= var1;
         return false;
      }
   }

   public SecurityPolicyOutlineSketcher() {
      this.outline = new SecurityPolicyOutline();
   }

   protected SecurityPolicyOutlineSketcher(SecurityPolicyPlan var1) {
      this.outline = var1;
   }

   protected SecurityPolicyPlan getOutline() {
      return this.outline;
   }

   protected void sketchSecurity(boolean var1) {
      if (var1 && !this.outline.isRequest()) {
         this.outline.setBuildingPlan(27);
      }

      this.outline.setHasSecurity(var1);
      this.outline.setHasMessageSecurity(var1);
   }

   protected void sketchOuline(Element var1) throws SecurityPolicyInspectionException {
      if (null != var1) {
         QName var2 = new QName(var1.getNamespaceURI(), var1.getLocalName());
         if (WSSConstants.UNT_QNAME.equals(var2)) {
            this.sketchUsernameToken(var1);
         } else if (WSSConstants.TIMESTAMP_QNAME.equals(var2)) {
            this.sketchTimestamp(var1);
         } else if (WSSConstants.BST_QNAME.equals(var2)) {
            this.sketchBinarySecurityToken(var1);
         } else if (!SAML2Constants.SAML2_ASST_QNAME.equals(var2) && !SAMLConstants.SAML_ASST_QNAME.equals(var2)) {
            if (WSS11Constants.SIG_CONF_QNAME.equals(var2)) {
               this.sketchSignatureConfirmation(var1);
            } else if (WSS11Constants.ENC_HEADER_QNAME.equals(var2)) {
               this.sketchEncryptedHeader(var1);
            } else if (SecurityImpl.REFERENCE_LIST_QNAME.equals(var2)) {
               this.sketchReferenceList(var1);
            }
         } else {
            this.sketchSamlToken(var1);
         }
      }
   }

   protected void sketchEncryptedKeyList(List<Element> var1, List<Element> var2) throws SecurityPolicyInspectionException, MarshalException {
      int var3;
      if (null != var2 && var2.size() > 0) {
         for(var3 = 0; var3 < var2.size(); ++var3) {
            this.sketchReferenceList((Element)var2.get(var3));
         }
      }

      if (null != var1 && var1.size() != 0) {
         for(var3 = 0; var3 < var1.size(); ++var3) {
            this.sketchEncryptedKey((Element)var1.get(var3));
         }

      }
   }

   public static boolean isSignatureElement(Element var0) {
      String var1 = getExistingId(var0);
      if (null != var1 && !"".equals(var1)) {
         try {
            List var2 = getKeyInfoList(var0);
            if (null == var2) {
               return true;
            }

            Iterator var3 = var2.iterator();

            while(var3.hasNext()) {
               Object var4 = var3.next();
               if (var4 instanceof SecurityTokenReference) {
                  SecurityTokenReference var5 = (SecurityTokenReference)var4;
                  if (var5.getValueType() != null && !"http://docs.oasis-open.org/wss/oasis-wss-soap-message-security-1.1#EncryptedKey".equals(var5.getValueType()) && !"http://docs.oasis-open.org/wss/oasis-wss-soap-message-security-1.1#EncryptedKeySHA1".equals(var5.getValueType()) && !"http://docs.oasis-open.org/ws-sx/ws-secureconversation/200512/dk".equals(var5.getValueType()) && !"http://docs.oasis-open.org/ws-sx/ws-secureconversation/200512/sct".equals(var5.getValueType()) && !"http://schemas.xmlsoap.org/ws/2005/02/sc/dk".equals(var5.getValueType()) && !"http://schemas.xmlsoap.org/ws/2005/02/sc/sct".equals(var5.getValueType())) {
                     return false;
                  }

                  return true;
               }
            }
         } catch (MarshalException var6) {
            Verbose.log("Error on parsing Signature's key info element, while checking it is endorse signature or not", var6);
         }

         return true;
      } else {
         return false;
      }
   }

   protected void sketchEndorseElement(Element var1) throws SecurityPolicyInspectionException, MarshalException {
      SignaturePolicy var2 = this.outline.getEndorsingPolicy();
      this.sketchOneEndorseItem("EndoseSignature");
      this.sketchSignatureElement(var1, var2, true);
   }

   protected void sketchSignatureElelment(Element var1) throws SecurityPolicyInspectionException, MarshalException {
      SignaturePolicy var2 = this.outline.getSigningPolicy();
      this.sketchSignatureElement(var1, var2, false);
   }

   private void sketchSignatureElement(Element var1, SignaturePolicy var2, boolean var3) throws SecurityPolicyInspectionException, MarshalException {
      Element var4 = DOMUtils.getFirstElement(var1);
      if (var4 == null) {
         throw new SecurityPolicyInspectionException(3006);
      } else if (DOMUtils.is(var4, SecurityImpl.ENCRYPTED_DATA_QNAME)) {
         boolean var15 = this.isElementInEncryptedDataReferenceList(var4);
         if (!var15) {
            if (!this.isElementInReferenceList(var4)) {
               throw new SecurityPolicyInspectionException(4206);
            }

            SecurityToken var16 = new SecurityToken((Node)null, (String)null, "http://schemas.xmlsoap.org/ws/2005/02/sc/dk", true);
            this.outline.getEncryptionPolicy().addEncryptionToken(var16);
         }

         this.sketchOneEncryptionItem("EncryptSignature");
      } else {
         Element var5 = weblogic.xml.dom.DOMUtils.getFirstElement(var1, new QName("http://www.w3.org/2000/09/xmldsig#", "SignedInfo"));
         if (var5 == null) {
            throw new SecurityPolicyInspectionException(3006);
         } else {
            SignedInfoImpl var6 = new SignedInfoImpl();
            ((WLXMLStructure)var6).read(getXMLStreamReader(var5));
            var2.setSignatureMethod(var6.getSignatureMethod());
            var2.setCanonicalizationMethod(var6.getCanonicalizationMethod());
            List var7 = var6.getReferences();
            Iterator var8 = var7.iterator();

            while(var8.hasNext()) {
               Reference var9 = (Reference)var8.next();
               String var10 = var9.getURI();
               if (null != var10 && var10.length() >= 2) {
                  String var11 = var10.substring(1);
                  if (var3) {
                     this.endoseSignatureReference.put(var11, var9);
                  } else {
                     this.signatureReference.put(var11, var9);
                  }
               }
            }

            List var17 = getKeyInfoList(var1);
            if (null != var17) {
               Iterator var18 = var17.iterator();
               if (var18.hasNext()) {
                  Object var19 = var18.next();
                  if (var19 instanceof SecurityTokenReference) {
                     SecurityTokenReference var20 = (SecurityTokenReference)var19;
                     String var12 = var20.getId();
                     var2.setIncludeSigningTokens(true);
                     if (this.signatureReference.get(var12) != null) {
                        if (var3) {
                           this.sketchOneSignatureItem("X509Token");
                        } else {
                           var2.setIncludeSigningTokens(true);
                        }

                        this.signatureReference.remove(var12);
                     }

                     SecurityToken var13;
                     if (var20.getValueType() != null) {
                        if (var20.getValueType().indexOf("x509") != -1) {
                           var13 = new SecurityToken((Node)null, (String)null, var20.getValueType(), true);
                           var2.addSignatureToken(var13);
                        } else if (!"http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.1#SAMLV2.0".equals(var20.getValueType()) && !"http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.0#SAMLAssertionID".equals(var20.getValueType())) {
                           if (!"http://docs.oasis-open.org/wss/oasis-wss-soap-message-security-1.1#EncryptedKey".equals(var20.getValueType()) && !"http://docs.oasis-open.org/wss/oasis-wss-soap-message-security-1.1#EncryptedKeySHA1".equals(var20.getValueType())) {
                              if ("http://docs.oasis-open.org/wss/oasis-wss-soap-message-security-1.1#ThumbprintSHA1".equals(var20.getValueType())) {
                                 var13 = new SecurityToken((Node)null, (String)null, "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v3", true);
                                 List var14 = TokenReferenceTypeHelper.getSTRTypeList(var13.getTokenTypeUri(), 1);
                                 var13.setStrTypes(var14);
                                 var2.addSignatureToken(var13);
                              } else if ("http://schemas.xmlsoap.org/ws/2005/02/sc/dk".equals(var20.getValueType())) {
                                 var13 = new SecurityToken((Node)null, (String)null, "http://schemas.xmlsoap.org/ws/2005/02/sc/dk", true);
                                 var2.addSignatureToken(var13);
                              } else if ("http://schemas.xmlsoap.org/ws/2005/02/sc/sct".equals(var20.getValueType())) {
                                 var13 = new SecurityToken((Node)null, (String)null, "http://schemas.xmlsoap.org/ws/2005/02/sc/sct", true);
                                 var2.addSignatureToken(var13);
                              } else if ("http://docs.oasis-open.org/ws-sx/ws-secureconversation/200512/dk".equals(var20.getValueType())) {
                                 var13 = new SecurityToken((Node)null, (String)null, "http://docs.oasis-open.org/ws-sx/ws-secureconversation/200512/dk", true);
                                 var2.addSignatureToken(var13);
                              } else {
                                 if (!"http://docs.oasis-open.org/ws-sx/ws-secureconversation/200512/sct".equals(var20.getValueType())) {
                                    throw new SecurityPolicyInspectionException(3609, "Value Type = " + var20.getValueType() + " is not supported, ");
                                 }

                                 var13 = new SecurityToken((Node)null, (String)null, "http://docs.oasis-open.org/ws-sx/ws-secureconversation/200512/sct", true);
                                 var2.addSignatureToken(var13);
                              }
                           } else {
                              this.sketchEncyptedKeyAction();
                              var13 = new SecurityToken((Node)null, (String)null, var20.getValueType(), true);
                              var2.addSignatureToken(var13);
                           }
                        } else {
                           var13 = new SecurityToken((Node)null, (String)null, var20.getValueType(), true);
                           var2.addSignatureToken(var13);
                        }
                     } else {
                        var13 = new SecurityToken((Node)null, (String)null, "http://schemas.xmlsoap.org/ws/2005/02/sc/sct", true);
                        var2.addSignatureToken(var13);
                     }

                     var2.setSigningNodeMap(this.signatureReference);
                  } else {
                     throw new SecurityPolicyInspectionException(3709);
                  }
               }
            }
         }
      }
   }

   private static List getKeyInfoList(Element var0) throws MarshalException {
      Element var1 = weblogic.xml.dom.DOMUtils.getFirstElement(var0, new QName("http://www.w3.org/2000/09/xmldsig#", "KeyInfo"));
      if (var1 == null) {
         return null;
      } else {
         KeyInfoImpl var2 = new KeyInfoImpl();
         ((KeyInfoImpl)var2).read(getXMLStreamReader(var1));
         return null == var2 ? null : var2.getContent();
      }
   }

   protected void sketchEncryptedKey(Element var1) throws SecurityPolicyInspectionException, MarshalException {
      Element var2 = DOMUtils.getFirstElement(var1);
      if (var2 == null) {
         throw new SecurityPolicyInspectionException(4706);
      } else {
         Object var3 = KeyInfoObjectBase.readKeyInfoObject(getXMLStreamReader(var1));
         WLEncryptedKey var4 = (WLEncryptedKey)var3;
         EncryptionPolicy var5 = this.outline.getEncryptionPolicy();
         var5.setKeyWrapMethod(var4.getEncryptionMethod());
         List var6 = var4.getReferenceList();
         if (null != var6) {
            for(int var7 = 0; var7 < var6.size(); ++var7) {
               DataReference var8 = (DataReference)var6.get(var7);
               String var9 = var8.getURI();
               if (null != var9 && var9.length() > 1) {
                  this.sketchOneEncryptionItem(var9.substring(1));
                  this.encryptionReference.put(var9.substring(1), var8);
               }
            }
         }

         List var13 = getKeyInfoList(var1);
         if (null != var13) {
            Iterator var14 = var13.iterator();
            if (var14.hasNext()) {
               Object var15 = var14.next();
               if (var15 instanceof SecurityTokenReference) {
                  SecurityTokenReference var10 = (SecurityTokenReference)var15;
                  if (var10.getValueType() != null) {
                     SecurityToken var11;
                     if (var10.getValueType().indexOf("x509") != -1) {
                        var11 = new SecurityToken((Node)null, (String)null, var10.getValueType(), true);
                        var5.addEncryptionToken(var11);
                     } else if ("http://docs.oasis-open.org/wss/oasis-wss-soap-message-security-1.1#ThumbprintSHA1".equals(var10.getValueType())) {
                        var11 = new SecurityToken((Node)null, (String)null, "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v3", true);
                        List var12 = TokenReferenceTypeHelper.getSTRTypeList(var11.getTokenTypeUri(), 1);
                        var11.setStrTypes(var12);
                        var5.addEncryptionToken(var11);
                     } else {
                        if (!TokenTypeHelper.isSamlValueType(var10.getValueType())) {
                           throw new SecurityPolicyInspectionException(4609);
                        }

                        var11 = new SecurityToken((Node)null, (String)null, var10.getValueType(), true);
                        var5.addEncryptionToken(var11);
                     }
                  } else if (null != var10.getReferenceURI()) {
                     String var16 = var10.getReferenceURI().substring(1);
                     if (this.bstReferenceList.get(var16) != null) {
                        SecurityToken var17 = new SecurityToken((Node)null, (String)null, "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v3", true);
                        var5.addEncryptionToken(var17);
                     } else {
                        this.bstReferenceList.put(var16, var1);
                     }
                  }

               } else {
                  throw new SecurityPolicyInspectionException(4709);
               }
            }
         }
      }
   }

   protected void sketchUsernameToken(Element var1) throws SecurityPolicyInspectionException {
      if (this.isDuplicateElement(2)) {
         throw new SecurityPolicyInspectionException(1025);
      } else {
         Element var2 = DOMUtils.getFirstElement(var1);
         if (var2 == null) {
            throw new SecurityPolicyInspectionException(1026);
         } else {
            IdentityPolicy var3 = this.outline.getIdentityPolicy();
            SecurityToken var4 = new SecurityToken((Node)null, (String)null, "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#UsernameToken", true);
            var3.addIdentityToken(var4);
            if ("EncryptedData".equals(var2.getLocalName())) {
               this.sketchOneEncryptionItem("UserNameToken");
            }

            if (this.isElementInSignatureReferenceList(var1)) {
               this.sketchOneSignatureItem("UserNameToken");
            }

         }
      }
   }

   protected void sketchSamlToken(Element var1) throws SecurityPolicyInspectionException {
      if (this.isDuplicateElement(4)) {
         throw new SecurityPolicyInspectionException(1065);
      } else {
         Element var2 = DOMUtils.getFirstElement(var1);
         if (var2 == null) {
            throw new SecurityPolicyInspectionException(1066);
         } else {
            IdentityPolicy var3 = this.outline.getIdentityPolicy();
            String var4 = SAMLUtils.getTokenTypeFromAssertionElement(var1);
            SecurityToken var5 = new SecurityToken((Node)null, (String)null, var4, true);
            var3.addIdentityToken(var5);
            if ("EncryptedData".equals(var2.getLocalName())) {
               this.sketchOneEncryptionItem("SamlToken");
            }

            if (this.isElementInSignatureReferenceList(var1)) {
               this.sketchOneSignatureItem("SamlToken");
            }

         }
      }
   }

   protected void sketchTimestamp(Element var1) throws SecurityPolicyInspectionException {
      if (this.isDuplicateElement(1)) {
         throw new SecurityPolicyInspectionException(6105);
      } else {
         Element var2 = DOMUtils.getFirstElement(var1);
         if (var2 == null) {
            throw new SecurityPolicyInspectionException(6106);
         } else {
            TimestampPolicy var3 = this.outline.getTimestampPolicy();
            var3.setIncludeTimestamp(true);
         }
      }
   }

   protected void sketchSignatureConfirmation(Element var1) throws SecurityPolicyInspectionException {
      if (this.isDuplicateElement(64) && verbose) {
         Verbose.log((Object)"Multiple SignatureConfirmation elements found.");
      }

      Element var2 = DOMUtils.getFirstElement(var1);
      if (var2 != null) {
         throw new SecurityPolicyInspectionException(6306);
      } else {
         this.outline.getGeneralPolicy().setWss11On();
         this.outline.getGeneralPolicy().setRequireSignatureConfirmation(true);
         this.addBlueprintAction(128);
      }
   }

   protected void sketchEncryptedHeader(Element var1) throws SecurityPolicyInspectionException {
      Element var2 = DOMUtils.getFirstElement(var1);
      if (var2 == null) {
         throw new SecurityPolicyInspectionException(4306);
      } else {
         this.outline.getGeneralPolicy().setWss11On();
         this.sketchOneEncryptionItem("Header");
         if (this.isElementInSignatureReferenceList(var1)) {
            this.sketchOneSignatureItem("Header");
         }

      }
   }

   protected void sketchSoapBody(SOAPBody var1) throws SecurityPolicyInspectionException {
      Element var2 = DOMUtils.getFirstElement(var1);
      if (null == var2) {
         String var5 = var1.getAttributeValue(WSSConstants.WSU_ID_QNAME);
         if (var5 != null && this.signatureReference.get(var5) != null) {
            this.outline.getSigningPolicy().addSignatureNode("Body", var1);
         }

         ((SecurityPolicyOutline)this.outline).setBodyEmpty(true);
      } else {
         if (DOMUtils.is(var2, SecurityImpl.ENCRYPTED_DATA_QNAME)) {
            boolean var3 = this.isElementInEncryptedDataReferenceList(var2);
            if (!var3) {
               if (!this.isElementInReferenceList(var2)) {
                  throw new SecurityPolicyInspectionException(4206);
               }

               SecurityToken var4 = new SecurityToken((Node)null, (String)null, "http://schemas.xmlsoap.org/ws/2005/02/sc/dk", true);
               this.outline.getEncryptionPolicy().addEncryptionToken(var4);
            }

            this.sketchOneEncryptionItem("Body");
         }

         if (this.isElementInSignatureReferenceList((Element)var2.getParentNode())) {
            this.outline.getSigningPolicy().addSignatureNode("Body", (Element)var2.getParentNode());
         }

      }
   }

   protected void sketchReferenceList(Element var1) throws SecurityPolicyInspectionException {
      Element var2 = DOMUtils.getFirstElement(var1);
      if (var2 == null) {
         throw new SecurityPolicyInspectionException(4806);
      } else {
         Node var3 = var1.getParentNode();
         if (!DOMUtils.is(var3, SecurityImpl.ENCRYPTED_KEY_QNAME)) {
            try {
               List var4 = this.unmarshalReferenceList(var1);
               if (null != var4 && var4.size() != 0) {
                  this.outline.getGeneralPolicy().setWss11On();
                  if (this.isServiceProvider) {
                     this.addBlueprintAction(288);
                  } else {
                     this.addBlueprintAction(320);
                  }

                  Iterator var5 = var4.iterator();

                  while(var5.hasNext()) {
                     ReferenceType var6 = (ReferenceType)var5.next();
                     String var7 = var6.getURI();
                     if (var7 != null && var7.length() > 1) {
                        this.sketchOneEncryptionItem(var7.substring(1));
                        this.encryptionReferenceList.put(var7.substring(1), var6);
                     }
                  }
               } else {
                  Verbose.log((Object)"Empyty refernce list found in the header");
               }
            } catch (MarshalException var8) {
               Verbose.log((Object)"Unknow refernce list found in the header");
            }
         }

      }
   }

   private List unmarshalReferenceList(Node var1) throws MarshalException {
      try {
         DOMStreamReader var2 = new DOMStreamReader(var1);
         return ReferenceList.read(var2, false);
      } catch (XMLStreamException var3) {
         throw new MarshalException(var3);
      }
   }

   protected void sketchBinarySecurityToken(Element var1) throws SecurityPolicyInspectionException {
      Element var2 = DOMUtils.getFirstElement(var1);
      if (var2 != null) {
         throw new SecurityPolicyInspectionException(2546);
      } else {
         String var3 = DOMUtils.getExistingId(var1, WSSConstants.BUILTIN_ID_QNAMES);
         if (this.bstReferenceList.get(var3) != null) {
            SecurityToken var4 = new SecurityToken((Node)null, (String)null, "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v3", true);
            this.outline.getEncryptionPolicy().addEncryptionToken(var4);
         }

         this.bstReferenceList.put(var3, var1);
         if (this.isElementInSignatureReferenceList(var1)) {
            this.sketchOneSignatureItem("X509Token");
         }

      }
   }

   private boolean isElementInSignatureReferenceList(Element var1) {
      String var2 = DOMUtils.getExistingId(var1, WSSConstants.BUILTIN_ID_QNAMES);
      return this.signatureReference.get(var2) != null;
   }

   private boolean isElementInEncryptedDataReferenceList(Element var1) {
      String var2 = getExistingId(var1);
      return this.encryptionReference.get(var2) != null;
   }

   private boolean isElementInReferenceList(Element var1) {
      String var2 = getExistingId(var1);
      return this.encryptionReferenceList.get(var2) != null;
   }

   private static String getExistingId(Element var0) {
      String var1 = var0.getAttribute("Id");
      return var1 != null && var1.length() > 0 ? var1 : DOMUtils.getExistingId(var0, WSSConstants.BUILTIN_ID_QNAMES);
   }

   protected void addBlueprintAction(int var1) {
      this.outline.addActionToBuildingPlan(var1);
   }

   protected void sketchEncyptedKeyAction() {
      this.outline.setEncryptedKeyRequired(true);
      if (this.outline.isRequest()) {
         this.addBlueprintAction(288);
      } else {
         this.addBlueprintAction(320);
      }

   }

   protected void sketchOneSignatureItem(String var1) {
      this.outline.getSigningPolicy().addSignatureNode(var1, (Node)null);
   }

   protected void sketchOneEndorseItem(String var1) {
      this.outline.getEndorsingPolicy().addSignatureNode(var1, (Node)null);
   }

   protected void sketchSignatureProtection() {
      this.sketchOneEncryptionItem("EncryptSignature");
   }

   protected void sketchOneEncryptionItem(String var1) {
      this.outline.getEncryptionPolicy().addNode(var1, (Node)null);
   }

   private static XMLStreamReader getXMLStreamReader(Node var0) throws MarshalException {
      if (null == var0) {
         return null;
      } else {
         DOMStreamReader var1 = null;

         try {
            var1 = new DOMStreamReader(var0);
            return var1;
         } catch (XMLStreamException var3) {
            throw new MarshalException("Failed to create XMLStreamReader from " + var0.getNodeName(), var3);
         }
      }
   }
}
