package weblogic.xml.crypto.encrypt;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import weblogic.utils.io.UnsyncByteArrayOutputStream;
import weblogic.xml.crypto.api.KeySelector;
import weblogic.xml.crypto.api.MarshalException;
import weblogic.xml.crypto.api.URIReferenceException;
import weblogic.xml.crypto.dsig.api.keyinfo.KeyInfo;
import weblogic.xml.crypto.encrypt.api.CipherReference;
import weblogic.xml.crypto.encrypt.api.EncryptedData;
import weblogic.xml.crypto.encrypt.api.EncryptionMethod;
import weblogic.xml.crypto.encrypt.api.EncryptionProperties;
import weblogic.xml.crypto.encrypt.api.TBE;
import weblogic.xml.crypto.encrypt.api.TBEData;
import weblogic.xml.crypto.encrypt.api.TBEXML;
import weblogic.xml.crypto.encrypt.api.XMLDecryptContext;
import weblogic.xml.crypto.encrypt.api.XMLEncryptContext;
import weblogic.xml.crypto.encrypt.api.XMLEncryptionException;
import weblogic.xml.crypto.encrypt.api.dom.DOMDecryptContext;
import weblogic.xml.crypto.encrypt.api.dom.DOMTBEXML;
import weblogic.xml.crypto.utils.DOMUtils;
import weblogic.xml.crypto.utils.LogUtils;

public class WLEncryptedData extends WLEncryptedType implements EncryptedData {
   public static final String VERBOSE_PROPERTY = "weblogic.xml.crypto.encrypt.verbose";
   public static final boolean VERBOSE = Boolean.getBoolean("weblogic.xml.crypto.encrypt.verbose");
   public static final String DEFAULT_ENCODING = "UTF-8";
   public static final String TAG_ENCRYPTED_DATA = "EncryptedData";
   private EncryptionAlgorithm encryptionMethod;
   private TBE tbe;

   WLEncryptedData(TBE var1, EncryptionMethod var2, KeyInfo var3, EncryptionProperties var4, String var5, CipherReference var6) {
      super(var3, var4, var5, (WLCipherReference)var6);
      this.tbe = var1;
      if (var2 instanceof EncryptionAlgorithm) {
         this.encryptionMethod = (EncryptionAlgorithm)var2;
      }

   }

   WLEncryptedData() {
   }

   public boolean isFeatureSupported(String var1) {
      return false;
   }

   public EncryptionMethod getEncryptionMethod() {
      return this.encryptionMethod;
   }

   protected void setEncryptionMethod(WLEncryptionMethod var1) throws XMLEncryptionException {
      if (var1 instanceof EncryptionAlgorithm) {
         this.encryptionMethod = (EncryptionAlgorithm)var1;
      } else {
         throw new IllegalArgumentException("EncryptionMethod's algorithm (" + var1 + ") cannot be used for data encryption.");
      }
   }

   public InputStream decrypt(XMLDecryptContext var1) throws XMLEncryptionException {
      WLCipherData var2 = (WLCipherData)this.getCipherData();

      try {
         return this.encryptionMethod.decrypt(this.getKey(KeySelector.Purpose.DECRYPT, var1), var2.getCipherTextInternal(var1));
      } catch (URIReferenceException var4) {
         throw new XMLEncryptionException(var4);
      }
   }

   public void decryptAndReplace(XMLDecryptContext var1) throws XMLEncryptionException {
      if (!(var1 instanceof DOMDecryptContext)) {
         throw new IllegalArgumentException("decryptAndReplace() can only be used with DOMDecryptContext");
      } else {
         DOMDecryptContext var2 = (DOMDecryptContext)var1;
         if (VERBOSE) {
            InputStream var4 = this.decrypt(var1);
            short var5 = 8192;
            byte[] var6 = new byte[var5];
            int var7 = 0;
            int var8 = 0;

            do {
               try {
                  if (var5 - var8 >= 1024) {
                     var7 = var4.read(var6, var8, 1024);
                  } else {
                     var7 = var4.read(var6, var8, var5 - var8);
                  }

                  var8 += var7;
               } catch (IOException var11) {
                  var11.printStackTrace();
               }
            } while(var7 > -1 && var8 < var5);

            if (var8 == -1) {
               LogUtils.logEncrypt("Partial Decrypted CipherData is empty String");
            } else {
               String var9 = new String(var6, 0, var8);
               LogUtils.logEncrypt("Partial Decrypted CipherData is " + var9);
            }
         }

         InputStream var3 = this.decrypt(var1);
         Element var12 = var2.getNode();
         BufferedInputStream var13 = new BufferedInputStream(var3);
         if (!this.noEncryptedData(var12, var13)) {
            try {
               String var14 = this.getEncoding();
               if (var14 == null) {
                  var14 = "UTF-8";
               }

               DOMUtils.replace(var12, var13, var14);
            } catch (XMLStreamException var10) {
               throw new XMLEncryptionException(var10);
            }
         }
      }
   }

   private boolean noEncryptedData(Node var1, BufferedInputStream var2) throws XMLEncryptionException {
      var2.mark(1);

      try {
         if (var2.read() == -1) {
            var1.getParentNode().removeChild(var1);
            return true;
         } else {
            var2.reset();
            return false;
         }
      } catch (IOException var4) {
         throw new XMLEncryptionException(var4);
      }
   }

   public void encrypt(XMLEncryptContext var1) throws XMLEncryptionException, MarshalException {
      this.setEncoding(this.tbe.getEncoding());
      this.setMimeType(this.tbe.getMimeType());
      this.setType(this.tbe.getType());
      if (this.tbe == null) {
         throw new IllegalStateException("Cannot encrypt -- a plaintext source has not been provided");
      } else {
         UnsyncByteArrayOutputStream var2 = new UnsyncByteArrayOutputStream();
         OutputStream var3 = this.encryptionMethod.encrypt(this.getKey(KeySelector.Purpose.ENCRYPT, var1), var2);
         if (this.tbe instanceof TBEXML) {
            this.writeTBE((TBEXML)this.tbe, var3);
         } else if (this.tbe instanceof TBEData) {
            this.writeTBE((TBEData)this.tbe, var3);
         }

         try {
            var3.close();
         } catch (IOException var5) {
            throw new XMLEncryptionException(var5);
         }

         WLCipherData var4 = (WLCipherData)this.getCipherData();
         var4.setCipherText(new ByteArrayInputStream(var2.toByteArray()));
         this.marshal(var1);
         this.tbe = null;
      }
   }

   private void writeTBE(TBEData var1, OutputStream var2) throws XMLEncryptionException {
      InputStream var3 = var1.getInputStream();
      byte[] var4 = new byte[1024];
      boolean var5 = false;

      try {
         int var8;
         while((var8 = var3.read(var4)) >= 0) {
            var2.write(var4, 0, var8);
         }

      } catch (IOException var7) {
         throw new XMLEncryptionException(var7);
      }
   }

   private void writeTBE(TBEXML var1, OutputStream var2) throws XMLEncryptionException {
      try {
         DOMTBEXML var3 = (DOMTBEXML)var1;
         String var4 = var3.getType();
         if ("http://www.w3.org/2001/04/xmlenc#Element".equals(var4)) {
            DOMUtils.writeNode(var3.getNodeList().item(0), var2, this.getEncodingInternal());
         } else {
            if (!"http://www.w3.org/2001/04/xmlenc#Content".equals(var4)) {
               throw new XMLEncryptionException("Unrecognized Type " + this.tbe.getType() + " for TBE");
            }

            DOMUtils.writeNodeList(var3.getNodeList(), var2, this.getEncodingInternal());
         }

      } catch (XMLStreamException var5) {
         throw new XMLEncryptionException(var5);
      } catch (UnsupportedEncodingException var6) {
         throw new XMLEncryptionException(var6);
      }
   }

   private String getEncodingInternal() {
      String var1 = this.getEncoding();
      return var1 != null ? var1 : "UTF-8";
   }

   public TBE getTBE() {
      return this.tbe;
   }

   WLEncryptionMethod getEncryptionMethodInternal() {
      return this.encryptionMethod;
   }

   public String getLocalName() {
      return "EncryptedData";
   }

   public String getNamespace() {
      return "http://www.w3.org/2001/04/xmlenc#";
   }

   protected void readAttributes(XMLStreamReader var1) {
   }

   protected void readChildren(XMLStreamReader var1) {
   }

   protected void writeAttributes(XMLStreamWriter var1) {
   }

   protected void writeChildren(XMLStreamWriter var1) {
   }

   public String childrenToString() {
      return ", encryptionMethod=" + this.encryptionMethod;
   }
}
