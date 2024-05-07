package weblogic.xml.crypto.dsig;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Map;
import java.util.Set;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import weblogic.utils.StringUtils;
import weblogic.xml.babel.stream.CanonicalWriter;
import weblogic.xml.babel.stream.ExclusiveCanonicalWriter;
import weblogic.xml.babel.stream.XMLOutputStreamBase;
import weblogic.xml.babel.stream.XMLWriter;
import weblogic.xml.crypto.api.MarshalException;
import weblogic.xml.crypto.api.XMLCryptoContext;
import weblogic.xml.crypto.dsig.api.CanonicalizationMethod;
import weblogic.xml.crypto.dsig.api.spec.C14NMethodParameterSpec;
import weblogic.xml.crypto.utils.DOMUtils;
import weblogic.xml.crypto.utils.StaxUtils;
import weblogic.xml.schema.types.XSDNMTokens;
import weblogic.xml.stream.XMLOutputStream;

class CanonicalizationMethodW3C extends CanonicalizationMethodImpl implements CanonicalizationMethodFactory, CanonicalizationMethod, WLCanonicalizationMethod, WLXMLStructure {
   private final String algorithmURI;
   private final String algorithm;
   private final boolean withComments;
   private final boolean exclusive;
   private final boolean augmented;
   private Set<String> augmentedElementTracks;
   private C14NMethodParameterSpec c14nMethodParameterSpec;
   private String[] inclusiveNSPrefixList;

   private CanonicalizationMethodW3C(String var1, boolean var2, boolean var3) {
      this(var1, var2, var3, false);
   }

   private CanonicalizationMethodW3C(String var1, boolean var2, boolean var3, boolean var4) {
      this.algorithmURI = var1;
      this.withComments = var2;
      this.exclusive = var3;
      this.augmented = var4;
      if (var4) {
         this.algorithm = var1 + "_augmented";
      } else {
         this.algorithm = var1;
      }

   }

   static void init() {
      register(new CanonicalizationMethodW3C("http://www.w3.org/TR/2001/REC-xml-c14n-20010315", false, false));
      register(new CanonicalizationMethodW3C("http://www.w3.org/TR/2001/REC-xml-c14n-20010315#WithComments", true, false));
      register(new CanonicalizationMethodW3C("http://www.w3.org/2001/10/xml-exc-c14n#", false, true));
      register(new CanonicalizationMethodW3C("http://www.w3.org/TR/2001/REC-xml-c14n-20010315", false, false, true));
      register(new CanonicalizationMethodW3C("http://www.w3.org/TR/2001/REC-xml-c14n-20010315#WithComments", true, false, true));
      register(new CanonicalizationMethodW3C("http://www.w3.org/2001/10/xml-exc-c14n#", false, true, true));
   }

   public String getURI() {
      return this.algorithm;
   }

   public CanonicalizationMethod newCanonicalizationMethod() {
      if ("http://www.w3.org/2001/10/xml-exc-c14n#".equals(this.algorithmURI)) {
         return !this.augmented ? new CanonicalizationMethodW3C("http://www.w3.org/2001/10/xml-exc-c14n#", false, true) : new CanonicalizationMethodW3C("http://www.w3.org/2001/10/xml-exc-c14n#", false, true, true);
      } else {
         return this;
      }
   }

   public AlgorithmParameterSpec getParameterSpec() {
      return this.c14nMethodParameterSpec;
   }

   public String getAlgorithm() {
      return this.algorithmURI;
   }

   public boolean isFeatureSupported(String var1) {
      return false;
   }

   public XMLOutputStream canonicalize(OutputStream var1, Map var2) {
      OutputStreamWriter var3;
      try {
         var3 = new OutputStreamWriter(var1, "UTF-8");
      } catch (UnsupportedEncodingException var5) {
         throw new AssertionError(var5);
      }

      Object var4;
      if (this.exclusive) {
         if (this.inclusiveNSPrefixList != null) {
            var4 = new ExclusiveCanonicalWriter(var3, var2, this.getPrefixList());
         } else {
            var4 = new ExclusiveCanonicalWriter(var3, this.getPrefixList());
         }
      } else {
         var4 = new CanonicalWriter(var3, var2);
         if (this.withComments) {
            ((CanonicalWriter)var4).setWriteComments(this.withComments);
         }
      }

      if (this.augmented) {
         ((CanonicalWriter)var4).setWriteAugmented(this.augmented);
         ((CanonicalWriter)var4).setAugmentedElementTracks(this.augmentedElementTracks);
      }

      return new XMLOutputStreamBase((XMLWriter)var4);
   }

   public void write(XMLStreamWriter var1) throws MarshalException {
      try {
         var1.writeStartElement("http://www.w3.org/2000/09/xmldsig#", "CanonicalizationMethod");
         var1.writeAttribute("Algorithm", this.algorithmURI);
         var1.writeEndElement();
      } catch (XMLStreamException var3) {
         throw new MarshalException("Failed to write CanonicalizationMethod " + this.algorithmURI + ".", var3);
      }
   }

   private void writeInclusiveNamespaces(XMLStreamWriter var1) throws XMLStreamException {
      var1.setPrefix("exc14n", "http://www.w3.org/2001/10/xml-exc-c14n#");
      var1.writeStartElement("http://www.w3.org/2001/10/xml-exc-c14n#", "InclusiveNamespaces");
      var1.writeNamespace("exc14n", "http://www.w3.org/2001/10/xml-exc-c14n#");
      String[] var2 = this.getPrefixList();
      if (var2 != null && var2.length > 0) {
         var1.writeAttribute("PrefixList", this.getPrefixes());
      }

      var1.writeEndElement();
   }

   private String[] getPrefixList() {
      if (this.inclusiveNSPrefixList != null && this.inclusiveNSPrefixList.length > 0) {
         for(int var1 = 0; var1 < this.inclusiveNSPrefixList.length; ++var1) {
            if (this.inclusiveNSPrefixList[var1].compareTo("xmlns") == 0) {
               this.inclusiveNSPrefixList[var1] = "#default";
            }
         }
      }

      return this.inclusiveNSPrefixList;
   }

   public String getPrefixes() {
      return this.inclusiveNSPrefixList != null ? StringUtils.join(this.getPrefixList(), " ") : "";
   }

   public void setPrefixes(String var1) {
      if (var1 != null) {
         var1.replace("#default", "xmlns");
         this.inclusiveNSPrefixList = XSDNMTokens.convertXml(var1, "#default");
      }

   }

   public void setupNonVisiblyUsed(Node var1, Map var2, XMLCryptoContext var3) {
      if ("http://www.w3.org/2001/10/xml-exc-c14n#".equals(this.algorithmURI) || "http://www.w3.org/2001/10/xml-exc-c14n#WithComments".equals(this.algorithmURI)) {
         String[] var4 = InclusiveUtils.getNonVisiblyUsed(var1, var2, var3);
         if (var4 != null && var4.length > 0) {
            Node var5 = DOMUtils.findNode(var1, "CanonicalizationMethod");
            if (var5 != null && var5.getNodeType() == 1) {
               Element var6 = DOMUtils.createAndAddElement((Element)var5, new QName("http://www.w3.org/2001/10/xml-exc-c14n#", "InclusiveNamespaces", "exc14n"), "exc14n");
               DOMUtils.addAttribute(var6, new QName("PrefixList"), StringUtils.join(var4, " "));
               this.inclusiveNSPrefixList = var4;
            }
         }

      }
   }

   public void read(XMLStreamReader var1) throws MarshalException {
      try {
         if (this.algorithmURI.equals("http://www.w3.org/2001/10/xml-exc-c14n#")) {
            this.readInclusiveNamespaces(var1);
         }

         StaxUtils.forwardToEndElement("http://www.w3.org/2000/09/xmldsig#", "CanonicalizationMethod", var1);
      } catch (XMLStreamException var3) {
         throw new MarshalException("Failed to read CanonicalizationMethod " + this.getAlgorithm() + ".", var3);
      }
   }

   private void readInclusiveNamespaces(XMLStreamReader var1) throws XMLStreamException {
      var1.nextTag();
      if (StaxUtils.findStart(var1, "http://www.w3.org/2001/10/xml-exc-c14n#", "InclusiveNamespaces", false)) {
         String var2 = StaxUtils.getAttributeValue("http://www.w3.org/2001/10/xml-exc-c14n#", "PrefixList", var1);
         this.setPrefixes(var2);
      }

   }

   public void setAugmentedElementTracks(Set<String> var1) {
      if (!"http://www.w3.org/2001/10/xml-exc-c14n#".equals(this.algorithmURI)) {
         throw new IllegalArgumentException("Augmented element tracks not allowed to be set into the one other than EXC-C14N without comments");
      } else {
         this.augmentedElementTracks = var1;
      }
   }
}
