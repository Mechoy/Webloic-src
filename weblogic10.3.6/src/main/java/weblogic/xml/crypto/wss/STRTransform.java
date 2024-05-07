package weblogic.xml.crypto.wss;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import weblogic.utils.io.UnsyncByteArrayOutputStream;
import weblogic.xml.crypto.NodeSetDataImpl;
import weblogic.xml.crypto.OctetData;
import weblogic.xml.crypto.api.Data;
import weblogic.xml.crypto.api.NodeSetData;
import weblogic.xml.crypto.api.XMLCryptoContext;
import weblogic.xml.crypto.dom.WLDOMSignContext;
import weblogic.xml.crypto.dsig.CanonicalizationMethodImpl;
import weblogic.xml.crypto.dsig.OctetTransform;
import weblogic.xml.crypto.dsig.TransformFactory;
import weblogic.xml.crypto.dsig.TransformImpl;
import weblogic.xml.crypto.dsig.WLCanonicalizationMethod;
import weblogic.xml.crypto.dsig.WLXMLStructure;
import weblogic.xml.crypto.dsig.api.CanonicalizationMethod;
import weblogic.xml.crypto.dsig.api.Transform;
import weblogic.xml.crypto.dsig.api.XMLSignatureException;
import weblogic.xml.crypto.dsig.api.spec.TransformParameterSpec;
import weblogic.xml.crypto.utils.DOMUtils;
import weblogic.xml.crypto.utils.DataUtils;
import weblogic.xml.crypto.utils.LogUtils;
import weblogic.xml.crypto.utils.StaxUtils;
import weblogic.xml.crypto.wss.api.BinarySecurityToken;
import weblogic.xml.crypto.wss.provider.SecurityToken;
import weblogic.xml.crypto.wss.provider.SecurityTokenHandler;
import weblogic.xml.crypto.wss.provider.SecurityTokenReference;
import weblogic.xml.dom.marshal.MarshalException;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLOutputStream;
import weblogic.xml.stream.XMLStreamException;

public class STRTransform extends TransformImpl implements OctetTransform, TransformFactory {
   private static STRTransform exclC14nStrTransform = new STRTransform("http://www.w3.org/2001/10/xml-exc-c14n#");
   private static STRTransform inclC14nStrTransform = new STRTransform("http://www.w3.org/TR/2001/REC-xml-c14n-20010315");
   private CanonicalizationMethod c14nMethod;

   private STRTransform(String var1) {
      try {
         this.c14nMethod = CanonicalizationMethodImpl.newCanonicalizationMethod(var1 + "_augmented");
      } catch (NoSuchAlgorithmException var3) {
         throw new IllegalArgumentException(var3);
      }
   }

   public static void init() {
      register(exclC14nStrTransform);
   }

   public static STRTransform getInstance() {
      return new STRTransform("http://www.w3.org/2001/10/xml-exc-c14n#");
   }

   public String getAlgorithm() {
      return "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#STR-Transform";
   }

   public Data transform(Data var1, XMLCryptoContext var2) throws XMLSignatureException {
      Object var3 = DataUtils.extractNodeSetData(var1);
      Node var4 = null;
      Iterator var5 = ((NodeSetData)var3).iterator();
      if (var5.hasNext()) {
         var4 = (Node)var5.next();
      }

      if (null == var4) {
         throw new XMLSignatureException("Null input STR node found. ");
      } else {
         boolean var6 = false;
         if (var2 instanceof WLDOMSignContext && var4.getParentNode() == null) {
            Element var7 = ((WLDOMSignContext)var2).getXMLSignature().getSignatureNode();

            try {
               Node var8 = var7.getOwnerDocument().adoptNode(var4);
               if (null != var8) {
                  LogUtils.logDsig("Good output STR node found after adoptNode into signatureNode: " + var8);
                  var4 = var8;
               } else {
                  LogUtils.logDsig("Try again, after Null Node found during signatureNode adoptNode process. Input was", var4);
                  var4 = var7.getOwnerDocument().importNode(var4, true);
                  var3 = new NodeSetDataImpl(DOMUtils.getNodeSet(var4, false));
               }
            } catch (Throwable var28) {
               LogUtils.logDsig("Error during signatureNode adoptNode process. Exception =", var28);
               var4 = var7.getOwnerDocument().importNode(var4, true);
               var3 = new NodeSetDataImpl(DOMUtils.getNodeSet(var4, false));
            }

            if (var4 == null) {
               throw new XMLSignatureException("Problem on import STR node to signature node. STR node was: " + var4);
            }

            var7.appendChild(var4);
            var6 = true;
         }

         boolean var32 = false;
         LinkedHashMap var33 = new LinkedHashMap();
         Iterator var9 = ((NodeSetData)var3).iterator();

         while(var9.hasNext()) {
            Node var10 = (Node)var9.next();
            if (var10.getNodeType() == 1) {
               if (!var32 && "http://www.w3.org/2000/09/xmldsig#".equals(var10.getNamespaceURI()) && "KeyInfo".equals(var10.getLocalName())) {
                  var32 = true;
               }

               if ("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd".equals(var10.getNamespaceURI()) && "SecurityTokenReference".equals(var10.getLocalName())) {
                  var33.put(var10, (Object)null);
               }
            }
         }

         if (var33.isEmpty()) {
            throw new XMLSignatureException("Input node set for STR-Transform can not be empty.");
         } else {
            WSSecurityContext var34 = (WSSecurityContext)var2.getProperty("weblogic.xml.crypto.wss.WSSecurityContext");
            if (var34 == null) {
               throw new IllegalArgumentException("WSSecurityContext must be set on XMLCryptoContext");
            } else {
               MessageContext var35 = (MessageContext)var34.getProperty("javax.xml.rpc.handler.MessageContext");
               if (var35 == null) {
                  throw new IllegalArgumentException("MessageContext must be set on WSSecurityContext");
               } else {
                  try {
                     Iterator var11 = var33.keySet().iterator();

                     while(var11.hasNext()) {
                        Element var12 = (Element)var11.next();
                        SecurityToken var13 = null;
                        String var14 = DOMUtils.getExistingId(var12, var34.getIdQNames());
                        SecurityTokenReference var15 = var34.getSTR(var14);
                        if (var15 != null) {
                           var13 = var15.getSecurityToken();
                        } else {
                           var15 = SecurityTokenReferenceImpl.createAndUnmarshal(var12);
                           SecurityTokenHandler var16 = var34.getRequiredTokenHandler(var15.getValueType());
                           var13 = var16.getSecurityToken(var15, var35);
                        }

                        if (var32) {
                           String var42 = DOMUtils.getExistingId((Element)var4, var34.getIdQNames());
                           var34.addKeyInfo(var42, var13);
                        } else {
                           var34.addSTR(var15, var13);
                        }

                        Node var43 = var34.getNode(var13);
                        Node var45;
                        if (var43 == null) {
                           var13.setId((String)null);
                           HashMap var17 = new HashMap();
                           var17.put(var12.getNamespaceURI(), var12.getPrefix());
                           if (var13 instanceof BinarySecurityToken) {
                              BSTUtils.marshalToken((BinarySecurityToken)var13, (Element)var12.getParentNode(), var17, var12, false);
                           } else {
                              var13.marshal((Element)var12.getParentNode(), var12, (Map)null);
                           }
                        } else {
                           var45 = var12.getParentNode().insertBefore(var43.cloneNode(true), var12);
                           Set var18 = DOMUtils.getNodeSet(var43, false);
                           Set var19 = DOMUtils.getNodeSet(var45, false);
                           Iterator var20 = var18.iterator();
                           HashSet var21 = new HashSet();
                           Iterator var22 = var19.iterator();

                           label174:
                           while(true) {
                              Node var23;
                              Node var24;
                              String var25;
                              do {
                                 do {
                                    do {
                                       if (!var22.hasNext()) {
                                          break label174;
                                       }

                                       var23 = (Node)var20.next();
                                       var24 = (Node)var22.next();
                                    } while(var24.getNodeType() != 1 && var24.getNodeType() != 2);

                                    var25 = var24.getPrefix();
                                 } while(var24.getNodeType() == 2 && ("xmlns".equals(var24.getNodeName()) || "xmlns".equals(var25) || var25 == null || "".equals(var25)));
                              } while(var21.contains(var25));

                              String var26 = var23.lookupNamespaceURI(var25);
                              String var27 = var24.lookupNamespaceURI(var25);
                              if ((var26 != null || var27 != null) && var26 != null && !var26.equals(var27)) {
                                 if (var25 == null || "".equals(var25)) {
                                    ((Element)var45).setAttribute("xmlns", var26);
                                 }

                                 DOMUtils.declareNamespace((Element)var45, var26, var25);
                              }

                              var21.add(var25);
                           }
                        }

                        var45 = var12.getPreviousSibling();
                        var12.getParentNode().removeChild(var12);
                        var33.put(var12, var45);
                     }

                     HashSet var36 = new HashSet();
                     Iterator var37 = var33.values().iterator();

                     while(var37.hasNext()) {
                        var36.add(((Node)var37.next()).getNodeName());
                     }

                     ((WLCanonicalizationMethod)this.c14nMethod).setAugmentedElementTracks(var36);
                     Node var38 = var4;
                     if (var33.keySet().contains(var4)) {
                        var38 = (Node)var33.get(var4);
                     }

                     XMLInputStream var40 = DOMUtils.getXMLInputStream(var38);
                     Map var39 = DOMUtils.getNamespaceMap(var38);
                     OctetData var41 = new OctetData(this.c14n(var40, var39));
                     Iterator var44 = var33.entrySet().iterator();

                     while(var44.hasNext()) {
                        Map.Entry var47 = (Map.Entry)var44.next();
                        Node var48 = (Node)var47.getKey();
                        Node var49 = (Node)var47.getValue();
                        var49.getParentNode().insertBefore(var48, var49);
                        var49.getParentNode().removeChild(var49);
                     }

                     if (var6) {
                        Element var46 = ((WLDOMSignContext)var2).getXMLSignature().getSignatureNode();
                        var46.removeChild(var4);
                     }

                     return var41;
                  } catch (MarshalException var29) {
                     throw new XMLSignatureException(var29);
                  } catch (WSSecurityException var30) {
                     throw new XMLSignatureException(var30);
                  } catch (XMLStreamException var31) {
                     throw new XMLSignatureException("Failed to obtain InputStream of STR-Transform result.", var31);
                  }
               }
            }
         }
      }
   }

   public void readParameters(XMLStreamReader var1) throws weblogic.xml.crypto.api.MarshalException {
      try {
         var1.nextTag();
         var1.require(1, "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", "TransformationParameters");
         var1.nextTag();
         String var2 = StaxUtils.getAttributeValue("http://www.w3.org/2000/09/xmldsig#", "Algorithm", var1);
         this.c14nMethod = CanonicalizationMethodImpl.newCanonicalizationMethod(var2 + "_augmented");
         ((WLXMLStructure)this.c14nMethod).read(var1);
         var1.nextTag();
         StaxUtils.findEnd(var1, "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", "TransformationParameters");
      } catch (javax.xml.stream.XMLStreamException var3) {
         throw new weblogic.xml.crypto.api.MarshalException("Failed to read Transform " + this.getAlgorithm() + ".", var3);
      } catch (NoSuchAlgorithmException var4) {
         throw new weblogic.xml.crypto.api.MarshalException("Failed to read CanonicalizationMethod Transform " + this.getAlgorithm() + ".", var4);
      }
   }

   public void writeParameters(XMLStreamWriter var1) throws weblogic.xml.crypto.api.MarshalException {
      try {
         var1.writeStartElement("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", "TransformationParameters");
         ((WLXMLStructure)this.c14nMethod).write(var1);
         var1.writeEndElement();
      } catch (javax.xml.stream.XMLStreamException var3) {
         throw new weblogic.xml.crypto.api.MarshalException("Failed to write element Transform", var3);
      }
   }

   private byte[] c14n(XMLInputStream var1, Map var2) throws XMLSignatureException {
      UnsyncByteArrayOutputStream var3 = new UnsyncByteArrayOutputStream();
      XMLOutputStream var4 = ((WLCanonicalizationMethod)this.c14nMethod).canonicalize(var3, var2);

      try {
         var4.add(var1);
         var4.close(true);
      } catch (XMLStreamException var6) {
         throw new XMLSignatureException("canonicalization error", var6);
      }

      byte[] var5 = var3.toByteArray();
      return var5;
   }

   public Transform newTransform(TransformParameterSpec var1) throws InvalidAlgorithmParameterException {
      return new STRTransform("http://www.w3.org/2001/10/xml-exc-c14n#");
   }

   public String getURI() {
      return "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#STR-Transform";
   }
}
