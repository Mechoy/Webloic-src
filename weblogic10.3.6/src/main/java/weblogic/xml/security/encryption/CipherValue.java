package weblogic.xml.security.encryption;

import java.io.InputStream;
import java.io.OutputStream;
import weblogic.utils.io.UnsyncByteArrayInputStream;
import weblogic.utils.io.UnsyncByteArrayOutputStream;
import weblogic.xml.security.utils.StreamUtils;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLOutputStream;
import weblogic.xml.stream.XMLStreamException;

public class CipherValue extends CipherData implements XMLEncConstants {
   private String cipherValue;
   private UnsyncByteArrayOutputStream baos;

   public CipherValue() {
   }

   public CipherValue(XMLInputStream var1, String var2) throws XMLStreamException {
      this.fromXMLInternal(var1, var2);
   }

   public byte[] getCipherBytes() {
      return Utils.base64(this.cipherValue);
   }

   public InputStream getInputStream() {
      return new UnsyncByteArrayInputStream(this.getCipherBytes());
   }

   public void setCipherBytes(byte[] var1) {
      this.cipherValue = Utils.base64(var1);
   }

   public OutputStream getOutputStream() {
      if (this.baos != null) {
         return this.baos;
      } else {
         this.baos = new UnsyncByteArrayOutputStream();
         return this.baos;
      }
   }

   public void clear() {
      this.cipherValue = null;
      this.baos.reset();
   }

   public String toString() {
      return "CipherValue: " + this.cipherValue;
   }

   public void toXML(XMLOutputStream var1, String var2, int var3) throws XMLStreamException {
      StreamUtils.addStart(var1, var2, "CipherData", var3);
      if (this.baos != null) {
         this.cipherValue = Utils.base64(this.baos.toByteArray());
      }

      StreamUtils.addElement(var1, var2, "CipherValue", (String)this.cipherValue, var3 + 2, 2);
      StreamUtils.addEnd(var1, var2, "CipherData", var3);
   }

   static CipherData fromXML(XMLInputStream var0, String var1) throws XMLStreamException {
      return StreamUtils.peekElement(var0, var1, "CipherValue") ? new CipherValue(var0, var1) : null;
   }

   public void fromXMLInternal(XMLInputStream var1, String var2) throws XMLStreamException {
      this.cipherValue = StreamUtils.getValue(var1, var2, "CipherValue");
   }
}
