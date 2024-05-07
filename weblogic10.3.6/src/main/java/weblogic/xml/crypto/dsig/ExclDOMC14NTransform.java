package weblogic.xml.crypto.dsig;

import java.io.Writer;
import java.security.InvalidAlgorithmParameterException;
import java.util.Map;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import weblogic.utils.StringUtils;
import weblogic.xml.babel.stream.CanonicalWriter;
import weblogic.xml.babel.stream.ExclusiveCanonicalWriter;
import weblogic.xml.crypto.api.MarshalException;
import weblogic.xml.crypto.api.NodeSetData;
import weblogic.xml.crypto.api.XMLCryptoContext;
import weblogic.xml.crypto.dsig.api.Transform;
import weblogic.xml.crypto.dsig.api.spec.TransformParameterSpec;
import weblogic.xml.crypto.utils.StaxUtils;
import weblogic.xml.schema.types.XSDNMTokens;

public class ExclDOMC14NTransform extends DOMC14NTransform {
   private static TransformFactory withoutCommentsExclFactory = new ExclDOMC14NTransform(false);
   private String[] inclusiveNSPrefixList;
   private boolean nonVisiblyUsedSet;

   public ExclDOMC14NTransform(boolean var1) {
      super(var1);
      this.inclusiveNSPrefixList = null;
      this.nonVisiblyUsedSet = false;
   }

   public ExclDOMC14NTransform(boolean var1, TransformParameterSpec var2) {
      this(var1);
   }

   public String getAlgorithm() {
      return "http://www.w3.org/2001/10/xml-exc-c14n#";
   }

   protected CanonicalWriter getWriter(Writer var1, Map var2) {
      return new ExclusiveCanonicalWriter(var1, var2, this.getPrefixList());
   }

   protected void readParameters(XMLStreamReader var1) throws MarshalException {
      this.readInclusiveNamespaces(var1);
   }

   protected void writeParameters(XMLStreamWriter var1) throws MarshalException {
      String[] var2 = this.getPrefixList();
      if (var2 != null && var2.length > 0) {
         this.writeInclusiveNamespaces(var1);
      }

   }

   private void readInclusiveNamespaces(XMLStreamReader var1) throws MarshalException {
      try {
         var1.nextTag();
         if (StaxUtils.findStart(var1, "http://www.w3.org/2001/10/xml-exc-c14n#", "InclusiveNamespaces", false)) {
            String var2 = StaxUtils.getAttributeValue("http://www.w3.org/2001/10/xml-exc-c14n#", "PrefixList", var1);
            this.setPrefixes(var2);
         }

         this.nonVisiblyUsedSet = true;
      } catch (XMLStreamException var3) {
         throw new MarshalException(var3);
      }
   }

   private void writeInclusiveNamespaces(XMLStreamWriter var1) throws MarshalException {
      try {
         var1.setPrefix("exc14n", "http://www.w3.org/2001/10/xml-exc-c14n#");
         var1.writeStartElement("http://www.w3.org/2001/10/xml-exc-c14n#", "InclusiveNamespaces");
         var1.writeNamespace("exc14n", "http://www.w3.org/2001/10/xml-exc-c14n#");
         String[] var2 = this.getPrefixList();
         if (var2 != null && var2.length > 0) {
            var1.writeAttribute("PrefixList", this.getPrefixes());
         }

         var1.writeEndElement();
      } catch (XMLStreamException var3) {
         throw new MarshalException(var3);
      }
   }

   public String[] getPrefixList() {
      if (this.inclusiveNSPrefixList != null && this.inclusiveNSPrefixList.length > 0) {
         for(int var1 = 0; var1 < this.inclusiveNSPrefixList.length; ++var1) {
            if (this.inclusiveNSPrefixList[var1].compareTo("xmlns") == 0) {
               this.inclusiveNSPrefixList[var1] = "#default";
            }
         }
      }

      return this.inclusiveNSPrefixList;
   }

   public void setPrefixList(String[] var1) {
      this.setNonVisiblyUsed(var1);
   }

   private String getPrefixes() {
      return this.inclusiveNSPrefixList != null ? StringUtils.join(this.getPrefixList(), " ") : "";
   }

   private void setPrefixes(String var1) {
      String[] var2 = null;
      if (var1 != null) {
         var1.replace("#default", "xmlns");
         var2 = XSDNMTokens.convertXml(var1, "#default");
      }

      this.setNonVisiblyUsed(var2);
   }

   public Transform newTransform(TransformParameterSpec var1) throws InvalidAlgorithmParameterException {
      return new ExclDOMC14NTransform(false, var1);
   }

   private void setNonVisiblyUsed(String[] var1) {
      this.nonVisiblyUsedSet = true;
      this.inclusiveNSPrefixList = var1;
   }

   protected void setupNonVisiblyUsed(NodeSetData var1, Map var2, XMLCryptoContext var3) {
      if (!this.isNonVisiblyUsedSet()) {
         String[] var4 = InclusiveUtils.getNonVisiblyUsed(var1, var2, var3);
         this.setNonVisiblyUsed(var4);
      }
   }

   private boolean isNonVisiblyUsedSet() {
      return this.nonVisiblyUsedSet;
   }

   public static void init() {
      register(withoutCommentsExclFactory);
   }
}
