package weblogic.xml.security.wsse.v200207;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import weblogic.utils.AssertionError;
import weblogic.xml.security.utils.ElementFactory;
import weblogic.xml.security.utils.NSOutputStream;
import weblogic.xml.security.utils.NamespaceAwareXOS;
import weblogic.xml.security.utils.StreamUtils;
import weblogic.xml.security.utils.Utils;
import weblogic.xml.security.wsu.v200207.WSUConstants;
import weblogic.xml.stream.Attribute;
import weblogic.xml.stream.StartElement;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLOutputStream;
import weblogic.xml.stream.XMLStreamException;

public abstract class EncodedString implements WSSEConstants {
   private String encodingType = null;
   private String encodedValue = null;
   private String id = null;

   protected EncodedString(String var1) {
      this.encodingType = var1;
   }

   protected EncodedString(XMLInputStream var1, String var2) throws XMLStreamException {
      this.fromXMLInternal(var1, var2);
   }

   protected abstract String getElementName();

   protected abstract byte[] getValue();

   protected abstract void readAttributes(StartElement var1, Map var2) throws XMLStreamException;

   protected abstract void writeAttributes(List var1) throws XMLStreamException;

   public final void setId(String var1) {
      if (var1 != null) {
         throw new AssertionError("Id for " + this + " already set");
      } else {
         this.id = var1;
      }
   }

   public final String getId() {
      if (this.id == null) {
         this.id = Utils.generateId(this.getElementName());
      }

      return this.id;
   }

   public final byte[] getDecodedValue() {
      byte[] var1;
      if (this.encodedValue != null) {
         if (this.encodingType != null && !ENCODING_BASE64.equals(this.encodingType)) {
            throw new AssertionError("Unsupported encoding type: " + this.encodingType);
         }

         var1 = Utils.base64(this.encodedValue);
      } else {
         var1 = null;
      }

      return var1;
   }

   public final String getEncodedValue() {
      if (this.encodedValue == null) {
         if (this.encodingType != null && !ENCODING_BASE64.equals(this.encodingType)) {
            throw new AssertionError("Unsupported encoding type: " + this.encodingType);
         }

         String var1 = Utils.toBase64(this.getValue());
         this.encodedValue = var1;
      }

      return this.encodedValue;
   }

   public String getEncodingType() {
      return this.encodingType != null ? this.encodingType : ENCODING_BASE64;
   }

   public final void toXML(XMLOutputStream var1, String var2, int var3) throws XMLStreamException {
      String var4 = this.getElementName();
      Object var5 = null;
      if (var1 instanceof NSOutputStream) {
         var5 = (NSOutputStream)var1;
         String var6 = (String)((NSOutputStream)var5).getNamespaces().get(WSSE_URI);
         if (var6 == null) {
            ((NSOutputStream)var5).addPrefix(WSSE_URI, "wsse");
         }
      } else {
         var5 = new NamespaceAwareXOS(var1);
         ((NSOutputStream)var5).addPrefix(WSSE_URI, "wsse");
      }

      ArrayList var8 = new ArrayList();
      this.writeAttributes(var8);
      if (this.encodingType != null) {
         var8.add(StreamUtils.createAttribute("EncodingType", this.encodingType));
      }

      var8.add(ElementFactory.createAttribute(WSUConstants.WSU_URI, "Id", this.getId()));
      Attribute[] var7 = new Attribute[var8.size()];
      var8.toArray(var7);
      StreamUtils.addElement((XMLOutputStream)var5, var2, var4, this.getEncodedValue(), var7, 0, 0);
   }

   public final void fromXMLInternal(XMLInputStream var1, String var2) throws XMLStreamException {
      String var3 = this.getElementName();
      StartElement var4 = (StartElement)StreamUtils.getElement(var1, var2, var3);
      if (var4 == null) {
         throw new XMLStreamException("Did not receive expected BinarySecurityToken");
      } else {
         Map var5 = var4.getNamespaceMap();
         this.encodingType = StreamUtils.getAttribute(var4, "EncodingType");
         this.readAttributes(var4, var5);
         this.id = StreamUtils.getAttribute(var4, "Id");
         if (this.encodingType != null && !ENCODING_BASE64.equals(this.encodingType)) {
            throw new AssertionError("unsupported encodingType \"" + this.encodingType + "\"");
         } else {
            this.encodedValue = StreamUtils.getData(var1, var3);
            StreamUtils.closeScope(var1, var2, var3);
         }
      }
   }
}
