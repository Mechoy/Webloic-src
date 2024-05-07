package weblogic.xml.security.wsse.v200207;

import java.util.List;
import java.util.Map;
import weblogic.xml.security.utils.StreamUtils;
import weblogic.xml.security.wsse.KeyIdentifier;
import weblogic.xml.stream.StartElement;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLStreamException;

public class KeyIdentifierImpl extends EncodedString implements KeyIdentifier, WSSEConstants {
   private byte[] identifier;
   private String valueType;

   private KeyIdentifierImpl(byte[] var1, String var2, String var3) {
      super(var2);
      this.valueType = null;
      if (var1 == null) {
         throw new IllegalArgumentException("identifier cannot be null");
      } else {
         this.identifier = var1;
         this.valueType = var3;
      }
   }

   public KeyIdentifierImpl(byte[] var1, String var2) {
      this(var1, ENCODING_BASE64, var2);
   }

   public KeyIdentifierImpl(String var1, String var2) {
      this(var1.getBytes(), (String)null, var2);
   }

   public KeyIdentifierImpl(XMLInputStream var1, String var2) throws XMLStreamException {
      super(var1, var2);
      this.valueType = null;
   }

   public String getIdentifierString() {
      return new String(this.getIdentifier());
   }

   public byte[] getIdentifier() {
      if (this.identifier == null) {
         this.identifier = this.getDecodedValue();
      }

      return this.identifier;
   }

   protected final String getElementName() {
      return "KeyIdentifier";
   }

   protected final byte[] getValue() {
      return this.identifier;
   }

   public final String getValueType() {
      return this.valueType;
   }

   protected final void writeAttributes(List var1) {
      if (this.valueType != null) {
         var1.add(StreamUtils.createAttribute("ValueType", this.valueType));
      }

   }

   protected final void readAttributes(StartElement var1, Map var2) {
      String var3 = StreamUtils.getAttribute(var1, "ValueType");
      if (var3 != null) {
         this.valueType = var3;
      }

   }
}
