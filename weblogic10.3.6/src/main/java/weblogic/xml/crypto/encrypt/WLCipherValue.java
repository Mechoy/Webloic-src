package weblogic.xml.crypto.encrypt;

import java.io.IOException;
import java.io.InputStream;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import weblogic.utils.io.UnsyncByteArrayInputStream;
import weblogic.utils.io.UnsyncByteArrayOutputStream;
import weblogic.xml.crypto.api.MarshalException;
import weblogic.xml.crypto.encrypt.api.CipherValue;
import weblogic.xml.crypto.encrypt.api.XMLDecryptContext;
import weblogic.xml.crypto.encrypt.api.XMLEncryptionException;
import weblogic.xml.crypto.utils.StaxUtils;

public class WLCipherValue extends WLCipherData implements CipherValue {
   private String cipherValue;
   public static final String TAG_CIPHER_VALUE = "CipherValue";

   WLCipherValue() {
   }

   WLCipherValue(byte[] var1) {
      this.cipherValue = Utils.base64(var1);
   }

   public InputStream getCipherTextInternal() {
      return new UnsyncByteArrayInputStream(this.getCipherBytes());
   }

   public InputStream getCipherTextInternal(XMLDecryptContext var1) {
      return this.getCipherTextInternal();
   }

   public InputStream getCipherText() {
      throw new IllegalStateException("Cannot throw getCipherText on EncryptedData without CipherReference ");
   }

   public void setCipherText(InputStream var1) throws XMLEncryptionException {
      UnsyncByteArrayOutputStream var2 = new UnsyncByteArrayOutputStream();
      byte[] var3 = new byte[1024];
      boolean var4 = false;

      int var7;
      try {
         while((var7 = var1.read(var3)) >= 0) {
            var2.write(var3, 0, var7);
         }
      } catch (IOException var6) {
         throw new XMLEncryptionException(var6);
      }

      this.setCipherBytes(var2.toByteArray());
   }

   public byte[] getCipherBytes() {
      return this.cipherValue != null ? Utils.base64(this.cipherValue) : null;
   }

   public void setCipherBytes(byte[] var1) {
      this.cipherValue = Utils.base64(var1);
   }

   public void clear() {
   }

   public String toString() {
      return "CipherValue: " + this.cipherValue;
   }

   private String getCipherValue() {
      return this.cipherValue;
   }

   protected void writeDataSource(XMLStreamWriter var1) throws MarshalException {
      try {
         String var2 = this.getCipherValue();
         StaxUtils.writeElement(var1, "http://www.w3.org/2001/04/xmlenc#", "CipherValue", var2);
      } catch (XMLStreamException var3) {
         throw new MarshalException("Error writing CipherValue", var3);
      }
   }

   public void read(XMLStreamReader var1) throws MarshalException {
      try {
         this.cipherValue = StaxUtils.readElement(var1, "http://www.w3.org/2001/04/xmlenc#", "CipherValue");
      } catch (XMLStreamException var3) {
         throw new MarshalException("Error reading CipherValue", var3);
      }
   }

   public boolean isFeatureSupported(String var1) {
      return false;
   }

   public byte[] getValue() {
      return this.getCipherBytes();
   }
}
