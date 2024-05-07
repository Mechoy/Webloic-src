package weblogic.xml.security.specs;

import java.util.ArrayList;
import weblogic.xml.security.utils.NSOutputStream;
import weblogic.xml.security.utils.NamespaceAwareXOS;
import weblogic.xml.security.utils.StreamUtils;
import weblogic.xml.security.wsse.v200207.WSSEConstants;
import weblogic.xml.stream.Attribute;
import weblogic.xml.stream.StartElement;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLOutputStream;
import weblogic.xml.stream.XMLStreamException;

/** @deprecated */
public class BinarySecurityTokenSpec extends SpecBase {
   private String valueType;
   private String encoding;
   public static final BinarySecurityTokenSpec DEFAULT_SPEC = new BinarySecurityTokenSpec();

   public BinarySecurityTokenSpec() {
      this(WSSEConstants.VALUETYPE_X509V3, WSSEConstants.ENCODING_BASE64);
   }

   public BinarySecurityTokenSpec(String var1, String var2) {
      this.valueType = null;
      this.encoding = null;
      if (!WSSEConstants.VALUETYPE_X509V3.equals(var1)) {
         throw new AssertionError("Unsupported valueType: " + var1);
      } else if (!WSSEConstants.ENCODING_BASE64.equals(var2)) {
         throw new AssertionError("Unsupported encoding: " + var2);
      } else {
         this.valueType = var1;
         this.encoding = var2;
      }
   }

   public BinarySecurityTokenSpec(XMLInputStream var1, String var2) throws XMLStreamException {
      this.valueType = null;
      this.encoding = null;
      this.fromXMLInternal(var1, var2);
   }

   public String getEncoding() {
      return this.encoding;
   }

   public String getValueType() {
      return this.valueType;
   }

   protected void fromXMLInternal(XMLInputStream var1, String var2) throws XMLStreamException {
      StartElement var3 = (StartElement)StreamUtils.getElement(var1, var2, "BinarySecurityTokenSpec");
      if (var3 == null) {
         throw new XMLStreamException("Expected BinarySecurityTokenSpec, got " + var3);
      } else {
         this.encoding = StreamUtils.getAttribute(var3, "EncodingType");
         StreamUtils.requiredAttr(this.encoding, "BinarySecurityTokenSpec", "EncodingType");
         this.valueType = StreamUtils.getAttribute(var3, "ValueType");
         StreamUtils.requiredAttr(this.valueType, "BinarySecurityTokenSpec", "ValueType");
         if (!WSSEConstants.ENCODING_BASE64.equals(this.encoding)) {
            throw new AssertionError("Unsupported encoding \"" + this.encoding + "\"");
         } else if (!WSSEConstants.VALUETYPE_X509V3.equals(this.valueType)) {
            throw new AssertionError("Unsupported value type");
         } else {
            StreamUtils.closeScope(var1, var2, "BinarySecurityTokenSpec");
         }
      }
   }

   public void toXML(XMLOutputStream var1, String var2, int var3) throws XMLStreamException {
      ArrayList var4 = new ArrayList(2);
      ArrayList var5 = new ArrayList(2);
      if (var1 instanceof NSOutputStream) {
         NSOutputStream var6 = (NSOutputStream)var1;
      } else {
         NamespaceAwareXOS var8;
         var1 = var8 = new NamespaceAwareXOS((XMLOutputStream)var1);
         var8.addPrefix("http://www.openuri.org/2002/11/wsse/spec", "spec");
         var8.addPrefix(WSSEConstants.WSSE_URI, "wsse");
      }

      if (this.valueType != null) {
         var5.add(StreamUtils.createAttribute("ValueType", this.valueType));
      }

      if (this.encoding != null) {
         var5.add(StreamUtils.createAttribute("EncodingType", this.encoding));
      }

      Attribute[] var9 = new Attribute[var5.size()];
      var5.toArray(var9);
      Attribute[] var7 = new Attribute[var4.size()];
      var4.toArray(var7);
      StreamUtils.addStart((XMLOutputStream)var1, var2, "BinarySecurityTokenSpec", var9, var7, var3);
      StreamUtils.addEnd((XMLOutputStream)var1, var2, "BinarySecurityTokenSpec", var3);
   }

   public static BinarySecurityTokenSpec getDefaultSpec() {
      return DEFAULT_SPEC;
   }

   public String toString() {
      return "weblogic.xml.security.specs.BinarySecurityTokenSpec{valueType=" + this.valueType + ", encoding=" + this.encoding + "}";
   }
}
