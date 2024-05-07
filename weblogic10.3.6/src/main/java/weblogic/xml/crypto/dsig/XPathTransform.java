package weblogic.xml.crypto.dsig;

import java.security.InvalidAlgorithmParameterException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Iterator;
import java.util.LinkedHashSet;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import org.w3c.dom.Node;
import weblogic.xml.crypto.NodeSetDataImpl;
import weblogic.xml.crypto.api.Data;
import weblogic.xml.crypto.api.MarshalException;
import weblogic.xml.crypto.api.NodeSetData;
import weblogic.xml.crypto.api.XMLCryptoContext;
import weblogic.xml.crypto.dsig.api.Transform;
import weblogic.xml.crypto.dsig.api.XMLSignatureException;
import weblogic.xml.crypto.dsig.api.spec.TransformParameterSpec;
import weblogic.xml.crypto.dsig.api.spec.XPathFilterParameterSpec;
import weblogic.xml.crypto.utils.DataUtils;
import weblogic.xml.crypto.utils.LogUtils;
import weblogic.xml.xpath.DOMXPath;
import weblogic.xml.xpath.XPathException;

public class XPathTransform extends TransformImpl implements NodeTransform, TransformFactory {
   private static final String XPATH_ELEMENT = "XPath";
   private static final TransformFactory factory = new XPathTransform();
   private XPathFilterParameterSpec params;

   protected XPathTransform() {
   }

   protected XPathTransform(TransformParameterSpec var1) throws InvalidAlgorithmParameterException {
      if (var1 != null) {
         if (!(var1 instanceof XPathFilterParameterSpec)) {
            throw new InvalidAlgorithmParameterException();
         }

         this.params = (XPathFilterParameterSpec)var1;
      }

   }

   public static void init() {
      register(factory);
   }

   public String getAlgorithm() {
      return "http://www.w3.org/TR/1999/REC-xpath-19991116";
   }

   public AlgorithmParameterSpec getParameterSpec() {
      return this.params;
   }

   public boolean isFeatureSupported(String var1) {
      return false;
   }

   public Data transform(Data var1, XMLCryptoContext var2) throws XMLSignatureException {
      if (this.params == null) {
         throw new XMLSignatureException("No XPath parameter.");
      } else {
         LogUtils.logDsig("applying xpath: " + this.params.getXPath());

         try {
            NodeSetData var3 = DataUtils.extractNodeSetData(var1);
            DOMXPath var4 = new DOMXPath(this.params.getXPath());
            LinkedHashSet var5 = new LinkedHashSet();
            int var6 = 0;

            for(Iterator var7 = var3.iterator(); var7.hasNext(); ++var6) {
               Node var8 = (Node)var7.next();
               if (var4.evaluateAsBoolean(var8)) {
                  var5.add(var8);
               }
            }

            LogUtils.logDsig(var6 + " nodes in input set, " + var5.size() + " nodes in result set");
            return new NodeSetDataImpl(var5);
         } catch (XPathException var9) {
            throw new XMLSignatureException("Failed to evaluate xpath.", var9);
         }
      }
   }

   protected void writeParameters(XMLStreamWriter var1) throws MarshalException {
      try {
         var1.writeStartElement("http://www.w3.org/2000/09/xmldsig#", "XPath");
         var1.writeCharacters(this.params.getXPath());
         var1.writeEndElement();
      } catch (XMLStreamException var3) {
         throw new MarshalException("Failed to write XPath transform element.", var3);
      }
   }

   protected void readParameters(XMLStreamReader var1) throws MarshalException {
      try {
         var1.nextTag();
         var1.require(1, "http://www.w3.org/2000/09/xmldsig#", "XPath");
         this.params = new XPathFilterParameterSpec(var1.getElementText());
         if (this.params == null) {
            throw new MarshalException("No parameter for XPath transform found.");
         }
      } catch (XMLStreamException var3) {
         throw new MarshalException("Failed to read XPath transform element.", var3);
      }
   }

   public Transform newTransform(TransformParameterSpec var1) throws InvalidAlgorithmParameterException {
      return new XPathTransform(var1);
   }

   public String getURI() {
      return "http://www.w3.org/TR/1999/REC-xpath-19991116";
   }
}
