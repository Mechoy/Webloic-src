package weblogic.xml.crypto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.w3c.dom.Node;
import weblogic.xml.crypto.api.Data;
import weblogic.xml.crypto.api.MarshalException;
import weblogic.xml.crypto.api.URIReference;
import weblogic.xml.crypto.api.URIReferenceException;
import weblogic.xml.crypto.api.XMLCryptoContext;
import weblogic.xml.crypto.api.dom.DOMSignContext;
import weblogic.xml.crypto.api.dom.DOMStructure;
import weblogic.xml.crypto.dom.WLDOMSignContext;
import weblogic.xml.crypto.dsig.api.XMLObject;
import weblogic.xml.crypto.dsig.api.keyinfo.KeyInfo;
import weblogic.xml.crypto.utils.DOMUtils;
import weblogic.xml.crypto.utils.LogUtils;
import weblogic.xml.crypto.wss.SecurityTokenReferenceImpl;
import weblogic.xml.crypto.wss.WSSecurityContext;
import weblogic.xml.crypto.wss.provider.SecurityTokenReference;
import weblogic.xml.dom.ElementNode;

public class NodeURIDereferencer extends URIDereferencerBase {
   private Node node;
   private Node contextNode;
   private final HashMap hmAttrNode;
   private List nodeList;
   private boolean parsedFlag;

   public NodeURIDereferencer(Node var1, Map var2) {
      this.hmAttrNode = new HashMap();
      this.nodeList = new ArrayList();
      this.parsedFlag = false;
      this.node = var1;
      this.contextNode = var1;
   }

   public NodeURIDereferencer(Node var1) {
      this(var1, (Map)null);
   }

   private void traverseOnlyOnce(Set var1) {
      if (!this.parsedFlag) {
         this.hmAttrNode.clear();
         this.nodeList.clear();
         this.nodeList = DOMUtils.getNodeListAndIdAttrNodeMap(this.node, this.hmAttrNode, var1, false);
         this.parsedFlag = true;
      }

   }

   private Set getNodeSet(List var1, int var2, int var3) {
      LinkedHashSet var4 = new LinkedHashSet();
      if (var1.size() > var2 && var3 <= var1.size()) {
         for(int var5 = var2; var5 < var3; ++var5) {
            var4.add(var1.get(var5));
         }
      }

      return var4;
   }

   public Data dereference(URIReference var1, XMLCryptoContext var2) throws URIReferenceException {
      Set var3 = (Set)var2.getProperty("weblogic.xml.crypto.idqnames");
      String var4 = var1.getURI();
      if (var4 != null && var4.length() != 0) {
         if (!var4.startsWith("#")) {
            return super.dereference(var1, var2);
         } else {
            try {
               String var5 = var4.substring(1);
               List var9;
               if (var2 instanceof WLDOMSignContext) {
                  List var6 = ((WLDOMSignContext)var2).getXMLSignature().getObjects();
                  if (var6 != null) {
                     Iterator var7 = var6.iterator();

                     while(var7.hasNext()) {
                        XMLObject var8 = (XMLObject)var7.next();
                        if (var8.getId().equals(var5)) {
                           LogUtils.logDsig("Resolved uri ref: " + var4 + "in Object list.");
                           var9 = var8.getContent();
                           Node var10 = this.getNode(var8, var2);
                           Iterator var11 = var9.iterator();

                           while(var11.hasNext()) {
                              DOMStructure var12 = (DOMStructure)var11.next();
                              Node var13 = var12.getNode();
                              var10.appendChild(var13);
                           }

                           this.contextNode = var10;
                           return new NodeSetDataImpl(DOMUtils.getNodeSet(var10, false));
                        }
                     }
                  }

                  KeyInfo var16 = ((WLDOMSignContext)var2).getXMLSignature().getKeyInfo();
                  if (var16 != null) {
                     List var18 = var16.getContent();
                     Iterator var20 = var18.iterator();

                     while(var20.hasNext()) {
                        Object var21 = var20.next();
                        if (var21 instanceof SecurityTokenReference) {
                           SecurityTokenReference var22 = (SecurityTokenReference)var21;
                           if (var5.equals(var22.getId())) {
                              Node var23 = SecurityTokenReferenceImpl.getStrNode(var22);
                              return new NodeSetDataImpl(DOMUtils.getNodeSet(var23, false));
                           }
                        }
                     }
                  }
               }

               this.traverseOnlyOnce(var3);
               int[] var15 = (int[])((int[])this.hmAttrNode.get(var5));
               if (var15 != null) {
                  return new NodeSetDataImpl(this.getNodeSet(this.nodeList, var15[0], var15[1]));
               } else {
                  Node var17 = (Node)var2.getProperty("weblogic.wsee.security.signature_node");
                  if (null != var17) {
                     HashMap var19 = new HashMap();
                     var9 = DOMUtils.getNodeListAndIdAttrNodeMap(var17, var19, var3, false);
                     var15 = (int[])((int[])var19.get(var5));
                     if (var15 != null) {
                        return new NodeSetDataImpl(this.getNodeSet(var9, var15[0], var15[1]));
                     }
                  }

                  throw new URIReferenceException("Failed to dereference URI " + var4);
               }
            } catch (MarshalException var14) {
               throw new URIReferenceException(var14);
            }
         }
      } else {
         this.contextNode = this.node;
         this.traverseOnlyOnce(var3);
         return new NodeSetDataImpl(this.getNodeSet(this.nodeList, 0, this.nodeList.size()));
      }
   }

   private Node getNode(XMLObject var1, XMLCryptoContext var2) {
      String var3 = ((DOMSignContext)var2).getNamespacePrefix("http://www.w3.org/2000/09/xmldsig#", "dsig");
      ElementNode var4 = new ElementNode("http://www.w3.org/2000/09/xmldsig#", "Object", var3);
      var4.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:dsig", "http://www.w3.org/2000/09/xmldsig#");
      var4.setAttributeNS((String)null, "Id", var1.getId());
      return var4;
   }

   public Node getNode() {
      return this.node;
   }

   public Node getContextNode() {
      return this.contextNode;
   }

   public static final void resetParsedFlag(WSSecurityContext var0) {
      if (var0 != null && var0.getURIDereferencer() instanceof NodeURIDereferencer) {
         NodeURIDereferencer var1 = (NodeURIDereferencer)var0.getURIDereferencer();
         var1.parsedFlag = false;
         LogUtils.logWss("refresh nodeList once!");
      }

   }
}
