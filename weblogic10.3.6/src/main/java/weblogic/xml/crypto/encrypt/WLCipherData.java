package weblogic.xml.crypto.encrypt;

import java.io.InputStream;
import java.util.List;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import weblogic.xml.crypto.api.MarshalException;
import weblogic.xml.crypto.api.URIReferenceException;
import weblogic.xml.crypto.encrypt.api.CipherData;
import weblogic.xml.crypto.encrypt.api.XMLDecryptContext;
import weblogic.xml.crypto.encrypt.api.XMLEncryptionException;
import weblogic.xml.crypto.utils.StaxUtils;

public abstract class WLCipherData implements CipherData {
   public static final String TAG_CIPHER_DATA = "CipherData";

   static WLCipherData newInstance() {
      return new WLCipherValue();
   }

   static WLCipherData newInstance(byte[] var0) {
      return new WLCipherValue(var0);
   }

   static WLCipherData newInstance(WLCipherReference var0) {
      return var0;
   }

   static WLCipherData newInstance(String var0, List var1) {
      return new WLCipherReference(var0, var1);
   }

   static WLCipherData newInstance(XMLStreamReader var0) throws MarshalException {
      try {
         StaxUtils.readStart(var0, "http://www.w3.org/2001/04/xmlenc#", "CipherData");
         WLCipherData var1 = readDataSource(var0);
         StaxUtils.readEnd(var0, "http://www.w3.org/2001/04/xmlenc#", "CipherData");
         return var1;
      } catch (XMLStreamException var3) {
         throw new MarshalException("Unable to read CipherData", var3);
      }
   }

   public abstract void read(XMLStreamReader var1) throws MarshalException;

   public final void write(XMLStreamWriter var1) throws MarshalException {
      try {
         var1.writeStartElement("http://www.w3.org/2001/04/xmlenc#", "CipherData");
         this.writeDataSource(var1);
         var1.writeEndElement();
      } catch (XMLStreamException var3) {
         throw new MarshalException("Unable to write CipherData", var3);
      }
   }

   protected abstract void writeDataSource(XMLStreamWriter var1) throws MarshalException;

   private static WLCipherData readDataSource(XMLStreamReader var0) throws XMLStreamException, MarshalException {
      Object var1;
      if (StaxUtils.findStart(var0, "http://www.w3.org/2001/04/xmlenc#", "CipherValue")) {
         var1 = new WLCipherValue();
      } else {
         if (!StaxUtils.findStart(var0, "http://www.w3.org/2001/04/xmlenc#", "CipherReference")) {
            String var2 = var0.getLocalName();
            throw new MarshalException("Unknown child of CipherData: " + var2);
         }

         var1 = new WLCipherReference();
      }

      ((WLCipherData)var1).read(var0);
      return (WLCipherData)var1;
   }

   public abstract InputStream getCipherText();

   public abstract byte[] getCipherBytes() throws XMLEncryptionException;

   public abstract void setCipherText(InputStream var1) throws XMLEncryptionException;

   public abstract void clear();

   public abstract InputStream getCipherTextInternal();

   public abstract InputStream getCipherTextInternal(XMLDecryptContext var1) throws URIReferenceException;
}
