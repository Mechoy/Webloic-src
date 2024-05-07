package weblogic.xml.crypto.dsig;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.security.InvalidAlgorithmParameterException;
import java.util.Map;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.xml.babel.stream.CanonicalWriter;
import weblogic.xml.crypto.NodeURIDereferencer;
import weblogic.xml.crypto.OctetData;
import weblogic.xml.crypto.api.Data;
import weblogic.xml.crypto.api.NodeSetData;
import weblogic.xml.crypto.api.XMLCryptoContext;
import weblogic.xml.crypto.dsig.api.Transform;
import weblogic.xml.crypto.dsig.api.XMLSignatureException;
import weblogic.xml.crypto.dsig.api.spec.TransformParameterSpec;
import weblogic.xml.crypto.utils.C14NDOMAdapter;
import weblogic.xml.crypto.utils.DOMUtils;
import weblogic.xml.crypto.utils.DataUtils;
import weblogic.xml.stream.XMLStreamException;

public class DOMC14NTransform extends TransformImpl implements OctetTransform, TransformFactory {
   private static TransformFactory withoutCommentsFactory = new DOMC14NTransform(false);
   private static TransformFactory withCommentsFactory = new DOMC14NTransform(true);
   private boolean withComments;

   public DOMC14NTransform(boolean var1) {
      this.withComments = var1;
   }

   public String getAlgorithm() {
      return this.withComments ? "http://www.w3.org/TR/2001/REC-xml-c14n-20010315#WithComments" : "http://www.w3.org/TR/2001/REC-xml-c14n-20010315";
   }

   public Data transform(Data var1, XMLCryptoContext var2) throws XMLSignatureException {
      NodeSetData var3 = DataUtils.extractNodeSetData(var1);
      Node var4 = ((NodeURIDereferencer)var2.getURIDereferencer()).getContextNode();
      return this.transform(var4, var3, var2);
   }

   public Data transform(Node var1, NodeSetData var2, XMLCryptoContext var3) throws XMLSignatureException {
      ByteArrayOutputStream var4 = new ByteArrayOutputStream();

      OutputStreamWriter var5;
      try {
         var5 = new OutputStreamWriter(var4, "UTF-8");
      } catch (UnsupportedEncodingException var9) {
         throw new AssertionError(var9);
      }

      Map var6 = DOMUtils.getNamespaceMap((Node)var2.iterator().next());

      try {
         this.setupNonVisiblyUsed(var2, var6, var3);
         CanonicalWriter var7 = this.getWriter(var5, var6);
         if (this.withComments) {
            var7.setWriteComments(this.withComments);
         }

         C14NDOMAdapter var8 = new C14NDOMAdapter(var7, var6);
         traverseNode(var1, var2, var8);
         var7.flush();
      } catch (XMLStreamException var10) {
         throw new XMLSignatureException("Could not create xml stream for " + this.getAlgorithm() + " transform.", var10);
      }

      return new OctetData(var4.toByteArray());
   }

   private static void traverseNode(Node var0, NodeSetData var1, C14NDOMAdapter var2) throws XMLStreamException {
      if (var1.contains(var0)) {
         var2.adaptNodeStart(var0);
      }

      int var4;
      int var5;
      Node var6;
      if (var0.getNodeType() == 1) {
         NamedNodeMap var3 = ((Element)var0).getAttributes();
         var4 = var3.getLength();

         for(var5 = 0; var5 < var4; ++var5) {
            var6 = var3.item(var5);
            if (var1.contains(var6)) {
               var2.adaptNodeStart(var6);
            }
         }
      }

      if (var0.hasChildNodes()) {
         NodeList var7 = var0.getChildNodes();
         var4 = var7.getLength();

         for(var5 = 0; var5 < var4; ++var5) {
            var6 = var7.item(var5);
            traverseNode(var6, var1, var2);
         }
      }

      if (var1.contains(var0)) {
         var2.adaptNodeEnd(var0);
      }

   }

   public Transform newTransform(TransformParameterSpec var1) throws InvalidAlgorithmParameterException {
      return this;
   }

   public String getURI() {
      return this.getAlgorithm();
   }

   public static void init() {
      register(withoutCommentsFactory);
      register(withCommentsFactory);
   }

   protected CanonicalWriter getWriter(Writer var1, Map var2) {
      return new CanonicalWriter(var1, var2);
   }

   protected void setupNonVisiblyUsed(NodeSetData var1, Map var2, XMLCryptoContext var3) {
   }
}
