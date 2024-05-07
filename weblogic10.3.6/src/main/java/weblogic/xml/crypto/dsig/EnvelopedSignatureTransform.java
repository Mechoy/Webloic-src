package weblogic.xml.crypto.dsig;

import java.security.InvalidAlgorithmParameterException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import org.w3c.dom.Node;
import weblogic.xml.crypto.NodeSetDataImpl;
import weblogic.xml.crypto.api.Data;
import weblogic.xml.crypto.api.NodeSetData;
import weblogic.xml.crypto.api.XMLCryptoContext;
import weblogic.xml.crypto.dom.WLDOMSignContext;
import weblogic.xml.crypto.dom.WLDOMValidateContext;
import weblogic.xml.crypto.dsig.api.Transform;
import weblogic.xml.crypto.dsig.api.XMLSignContext;
import weblogic.xml.crypto.dsig.api.XMLSignatureException;
import weblogic.xml.crypto.dsig.api.XMLValidateContext;
import weblogic.xml.crypto.dsig.api.spec.TransformParameterSpec;
import weblogic.xml.crypto.utils.DOMUtils;

public class EnvelopedSignatureTransform extends TransformImpl implements NodeTransform, TransformFactory {
   private static final TransformFactory factory = new EnvelopedSignatureTransform();
   private TransformParameterSpec params;

   protected EnvelopedSignatureTransform() {
   }

   protected EnvelopedSignatureTransform(TransformParameterSpec var1) {
      this.params = var1;
   }

   public String getAlgorithm() {
      return "http://www.w3.org/2000/09/xmldsig#enveloped-signature";
   }

   public AlgorithmParameterSpec getParameterSpec() {
      return this.params;
   }

   public Data transform(Data var1, XMLCryptoContext var2) throws XMLSignatureException {
      Object var3 = null;
      if (var2 instanceof XMLValidateContext) {
         var3 = ((WLDOMValidateContext)var2).getSignatureNode();
      } else {
         if (!(var2 instanceof XMLSignContext)) {
            return var1;
         }

         var3 = ((WLDOMSignContext)var2).getXMLSignature().getSignatureNode();
      }

      Set var4 = DOMUtils.getNodeSet((Node)var3, true);
      NodeSetData var5 = (NodeSetData)var1;
      LinkedHashSet var6 = new LinkedHashSet();
      Iterator var7 = var5.iterator();

      while(var7.hasNext()) {
         Object var8 = var7.next();
         if (!var4.contains(var8)) {
            var6.add(var8);
         }
      }

      return new NodeSetDataImpl(var6);
   }

   protected void writeParameters(XMLStreamWriter var1) {
   }

   protected void readParameters(XMLStreamReader var1) {
   }

   public Transform newTransform(TransformParameterSpec var1) throws InvalidAlgorithmParameterException {
      return new EnvelopedSignatureTransform(var1);
   }

   public String getURI() {
      return "http://www.w3.org/2000/09/xmldsig#enveloped-signature";
   }

   public static void init() {
      register(factory);
   }
}
