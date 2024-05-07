package weblogic.xml.security.encryption;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import weblogic.utils.io.UnsyncByteArrayInputStream;
import weblogic.xml.security.keyinfo.KeyPurpose;
import weblogic.xml.security.keyinfo.KeyResolverException;
import weblogic.xml.security.keyinfo.KeyResult;
import weblogic.xml.security.utils.StreamUtils;
import weblogic.xml.security.utils.TestUtils;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLInputStreamFactory;
import weblogic.xml.stream.XMLOutputStream;
import weblogic.xml.stream.XMLOutputStreamFactory;
import weblogic.xml.stream.XMLStreamException;

public class EncryptedData extends EncryptedType implements XMLEncConstants {
   private static final XMLInputStreamFactory factory = XMLInputStreamFactory.newInstance();
   private EncryptionAlgorithm encryptionMethod;

   public EncryptedData() {
   }

   EncryptedData(XMLInputStream var1, String var2) throws XMLStreamException {
      this.fromXMLInternal(var1, var2);
   }

   public XMLInputStream getXMLInputStream(String var1) throws XMLStreamException {
      InputStream var2 = this.getInputStream();
      InputStreamReader var3 = this.getReader(var2, var1);
      return factory.newFragmentInputStream(var3, this.nsMap);
   }

   private InputStreamReader getReader(InputStream var1, String var2) {
      try {
         return var2 != null ? new InputStreamReader(var1, var2) : new InputStreamReader(var1, "UTF-8");
      } catch (UnsupportedEncodingException var4) {
         return new InputStreamReader(var1);
      }
   }

   public InputStream getInputStream() throws EncryptionException {
      Key var1 = this.getKey();
      if (var1 == null && this.encryptionMethod != null) {
         KeyResult var2 = null;

         try {
            var2 = this.resolveKey(KeyPurpose.DECRYPT, this.encryptionMethod.getURI(), this.getKeyInfo());
         } catch (KeyResolverException var5) {
            throw new EncryptionException("Unable to resolve decryption key", var5);
         }

         if (var2 != null) {
            var1 = var2.getKey();
         }
      }

      if (var1 == null) {
         throw new EncryptionException("Cannot find key to perform decryption");
      } else if (this.encryptionMethod == null) {
         throw new EncryptionException("Cannot determine EncryptionMethod");
      } else {
         InputStream var6 = this.encryptionMethod.decrypt(var1, this.getCipherData().getInputStream());
         if (VERBOSE) {
            try {
               var6 = this.debugInput(var6);
            } catch (IOException var4) {
               var4.printStackTrace();
               var6 = this.encryptionMethod.decrypt(var1, this.getCipherData().getInputStream());
            }
         }

         return var6;
      }
   }

   private InputStream debugInput(InputStream var1) throws IOException {
      ByteArrayOutputStream var2 = new ByteArrayOutputStream();
      byte[] var3 = new byte[1024];

      int var4;
      while((var4 = var1.read(var3)) != -1) {
         var2.write(var3, 0, var4);
      }

      System.out.println("<!-- -- Begin decrypted data from EncryptedData (id=" + this.id + ") ----->");
      byte[] var5 = var2.toByteArray();
      System.out.print(new String(var5));
      UnsyncByteArrayInputStream var6 = new UnsyncByteArrayInputStream(var5);
      System.out.println("\n<!----- End decrypted data from EncryptedData (id=" + this.id + ")   -- -->");
      return var6;
   }

   public XMLOutputStream getXMLOutputStream(String var1) throws XMLStreamException {
      try {
         return XMLOutputStreamFactory.newInstance().newOutputStream(new OutputStreamWriter(this.getOutputStream(), var1));
      } catch (Exception var3) {
         return XMLOutputStreamFactory.newInstance().newOutputStream(this.getOutputStream());
      }
   }

   public OutputStream getOutputStream() throws EncryptionException {
      Key var1 = this.getKey();
      if (var1 == null && this.encryptionMethod != null) {
         KeyResult var2 = null;

         try {
            var2 = this.resolveKey(KeyPurpose.ENCRYPT, this.encryptionMethod.getURI(), this.getKeyInfo());
         } catch (KeyResolverException var4) {
            throw new EncryptionException("failed to resolve encryption key", var4);
         }

         var1 = var2.getKey();
      }

      if (var1 == null) {
         throw new EncryptionException("Must set key to perform encryption");
      } else if (this.encryptionMethod == null) {
         throw new EncryptionException("Must set EncryptionMethod to perform encryption");
      } else {
         return this.encryptionMethod.encrypt(var1, this.getCipherData().getOutputStream());
      }
   }

   public void setEncryptionMethod(EncryptionMethod var1) throws EncryptionException {
      if (!(var1 instanceof EncryptionAlgorithm)) {
         throw new EncryptionException(var1.getURI() + " cannot be used as a block cipher");
      } else {
         this.encryptionMethod = (EncryptionAlgorithm)var1;
      }
   }

   EncryptionMethod getEncryptionMethodInternal() {
      return this.encryptionMethod;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("EncryptedData").append(": \n");
      super.toString(var1);
      return var1.toString();
   }

   public void toXML(XMLOutputStream var1, String var2, int var3) throws XMLStreamException {
      super.writeCommon(var2, "EncryptedData", var1, var3);
      StreamUtils.addEnd(var1, var2, "EncryptedData", var3);
      this.clear();
   }

   void fromXMLInternal(XMLInputStream var1, String var2) throws XMLStreamException {
      super.readCommon(var1, var2);
      StreamUtils.closeScope(var1, var2, "EncryptedData");
   }

   public static void main(String[] var0) throws Exception {
      XMLInputStream var1 = var0.length > 0 ? TestUtils.createXMLInputStreamFromFile(var0[0]) : TestUtils.createXMLInputStreamFromString("<EncryptedData Id=\"ED\" Type=\"http://www.w3.org/2001/04/xmlenc#Element\" xmlns=\"http://www.w3.org/2001/04/xmlenc#\">\n  <EncryptionMethod Algorithm=\"http://www.w3.org/2001/04/xmlenc#tripledes-cbc\"/>\n  <ds:KeyInfo xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\">\n    <EncryptedKey Id=\"EK\" xmlns=\"http://www.w3.org/2001/04/xmlenc#\">\n       <EncryptionMethod Algorithm=\"http://www.w3.org/2001/04/xmlenc#kw-tripledes\"/>\n      <ds:KeyInfo xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\">\n        <ds:KeyName>my-tripledes-key</ds:KeyName>\n      </ds:KeyInfo>\n      <CipherData>\n        <CipherValue>HgVuHoXxBQWD9fvi0gt9TanywZ5lJokM/12fcMG6gRoMjsCPulH+4A==</CipherValue>\n      </CipherData>\n      <ReferenceList>\n        <DataReference URI=\"#ED\"/>\n      </ReferenceList>\n     </EncryptedKey>\n  </ds:KeyInfo>\n  <CipherData>\n    <CipherValue>\n      yUMIHkj5EETckjZ59fpda4+m4YLCrkJsnuRz+Q3e5sP+VvHKRH1kdeGkXw3kYURVJM3nQjGl2egW80oUxSykQD2F9iDoIjNhLSgIbyuse64oo/5/v9IiaUpSvrAocwLPAzFIUmOrxmIagAkRGDOeMR8tdHLD6g84dQj4O/aGfwhL/2wUo/l+7onrbmsd6pVIfjNyvXm+eITuyUnkDTHrCR+dfb2sHaQ3g3McgyfP6ZjI/L50SPJZ/w==\n    </CipherValue>\n  </CipherData>\n</EncryptedData>\n");
      EncryptedData var2 = (EncryptedData)XMLEncReader.read(var1);
      var2.setKeyResolver(TestUtils.getDESKeyResolver());
      System.err.println("Deserialized:\n");
      System.out.println(var2);
      System.err.println("\nReserialized:\n");
      XMLOutputStream var3 = TestUtils.createXMLOutputStream(System.out);
      var2.toXML(var3, "http://www.w3.org/2001/04/xmlenc#", 0);
      var3.flush();
      System.out.println("\n\nClear text:\n");
      XMLInputStream var4 = var2.getXMLInputStream("UTF-8");
      XMLOutputStream var5 = XMLOutputStreamFactory.newInstance().newOutputStream(System.out);
      var5.add(var4);
      var5.close();
   }
}
