package weblogic.xml.security.specs;

import java.util.Iterator;
import weblogic.xml.security.utils.ElementFactory;
import weblogic.xml.security.utils.NSOutputStream;
import weblogic.xml.security.utils.NamespaceAwareXOS;
import weblogic.xml.security.utils.StreamUtils;
import weblogic.xml.security.wsu.v200207.WSUConstants;
import weblogic.xml.stream.Attribute;
import weblogic.xml.stream.StartElement;
import weblogic.xml.stream.XMLEvent;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLOutputStream;
import weblogic.xml.stream.XMLStreamException;

/** @deprecated */
public class SignatureSpec extends OperationSpec {
   private String c14nMethodURI;
   private String sigMethodURI;
   private static final SignatureSpec DEFAULT_SPEC = new SignatureSpec("http://www.w3.org/2001/10/xml-exc-c14n#", "http://www.w3.org/2000/09/xmldsig#rsa-sha1", true);

   public SignatureSpec(String var1, String var2) {
      this(var1, var2, false);
   }

   public SignatureSpec(String var1, String var2, boolean var3) {
      this.c14nMethodURI = null;
      this.sigMethodURI = null;
      this.c14nMethodURI = var1;
      this.sigMethodURI = var2;
      this.setEntireBody(var3);
   }

   public SignatureSpec(XMLInputStream var1, String var2) throws XMLStreamException {
      this.c14nMethodURI = null;
      this.sigMethodURI = null;
      this.fromXMLInternal(var1, var2);
   }

   public String getSignatureMethod() {
      return this.sigMethodURI;
   }

   public String getCanonicalizationMethod() {
      return this.c14nMethodURI;
   }

   protected void fromXMLInternal(XMLInputStream var1, String var2) throws XMLStreamException {
      StartElement var3 = (StartElement)StreamUtils.getElement(var1, var2, "SignatureSpec");
      if (var3 == null) {
         throw new XMLStreamException("Expected SignatureSpec, got " + var3);
      } else {
         this.sigMethodURI = StreamUtils.getAttribute(var3, "SignatureMethod");
         StreamUtils.requiredAttr(this.sigMethodURI, "SignatureSpec", "SignatureMethod");
         this.c14nMethodURI = StreamUtils.getAttribute(var3, "CanonicalizationMethod");
         StreamUtils.requiredAttr(this.sigMethodURI, "SignatureSpec", "CanonicalizationMethod");
         this.setEntireBody("true".equals(StreamUtils.getAttribute(var3, "SignBody")));
         XMLEvent var4 = StreamUtils.peekElement(var1);

         while(true) {
            while(!var4.isEndElement()) {
               if (var4.isStartElement() && StreamUtils.matches(var4, "ElementIdentifier", var2)) {
                  ElementIdentifier var5 = new ElementIdentifier(var1, var2);
                  String var6 = var5.getRestriction();
                  if (var6 == null) {
                     this.addUnrestrictedType(var5);
                  } else if ("body".equals(var6)) {
                     this.addBodyElement(var5);
                  } else {
                     this.addHeaderElement(var5);
                  }

                  var4 = StreamUtils.peekElement(var1);
               } else {
                  StreamUtils.discard(var1);
                  var4 = StreamUtils.peekElement(var1);
               }
            }

            StreamUtils.closeScope(var1, var2, "SignatureSpec");
            return;
         }
      }
   }

   public void toXML(XMLOutputStream var1, String var2, int var3) throws XMLStreamException {
      if (!(var1 instanceof NSOutputStream)) {
         NamespaceAwareXOS var4;
         var1 = var4 = new NamespaceAwareXOS((XMLOutputStream)var1);
         var4.addPrefix("http://www.openuri.org/2002/11/wsse/spec", "spec");
      }

      Attribute[] var8 = new Attribute[]{ElementFactory.createAttribute(var2, "SignatureMethod", this.sigMethodURI), ElementFactory.createAttribute(var2, "CanonicalizationMethod", this.c14nMethodURI), ElementFactory.createAttribute(var2, "SignBody", this.entireBody() ? "true" : "false")};
      StreamUtils.addStart((XMLOutputStream)var1, var2, "SignatureSpec", var8, var3);
      int var5 = var3 + 2;
      Iterator var6 = this.headerList.iterator();

      ElementIdentifier var7;
      while(var6.hasNext()) {
         var7 = (ElementIdentifier)var6.next();
         var7.toXML((XMLOutputStream)var1, var2, var5);
      }

      var6 = this.bodyList.iterator();

      while(var6.hasNext()) {
         var7 = (ElementIdentifier)var6.next();
         var7.toXML((XMLOutputStream)var1, var2, var5);
      }

      var6 = this.unrestrictedList.iterator();

      while(var6.hasNext()) {
         var7 = (ElementIdentifier)var6.next();
         var7.toXML((XMLOutputStream)var1, var2, var5);
      }

      StreamUtils.addEnd((XMLOutputStream)var1, var2, "SignatureSpec", var3);
   }

   public static final SignatureSpec getDefaultSpec() {
      return DEFAULT_SPEC;
   }

   static {
      DEFAULT_SPEC.addElement(WSUConstants.WSU_URI, "Timestamp", (String)null);
   }
}
