package weblogic.xml.security.wsse.internal;

import java.security.Key;
import weblogic.xml.security.NamedKey;
import weblogic.xml.security.SecurityProcessingException;
import weblogic.xml.security.encryption.DataReference;
import weblogic.xml.security.encryption.EncryptXMLOutputStream;
import weblogic.xml.security.encryption.EncryptedData;
import weblogic.xml.security.encryption.EncryptedKey;
import weblogic.xml.security.encryption.EncryptionException;
import weblogic.xml.security.encryption.EncryptionMethod;
import weblogic.xml.security.encryption.ReferenceList;
import weblogic.xml.security.keyinfo.KeyInfo;
import weblogic.xml.security.utils.Preprocessor;
import weblogic.xml.stream.EndElement;
import weblogic.xml.stream.StartElement;
import weblogic.xml.stream.XMLOutputStream;
import weblogic.xml.stream.XMLStreamException;

public class EncryptingPreprocessor implements Preprocessor {
   private final EncryptXMLOutputStream enXos;
   private final EncryptionMethod encryptionMethod;
   private final String keyName;
   private final ReferenceList refs;
   private int encryptionDepth = 0;
   private final Key key;

   public EncryptingPreprocessor(EncryptedKey var1, String var2, EncryptXMLOutputStream var3) throws EncryptionException {
      ReferenceList var4 = var1.getReferenceList();
      if (var4 == null) {
         var4 = new ReferenceList();
         var1.setReferenceList(var4);
      }

      this.enXos = var3;
      this.encryptionMethod = EncryptionMethod.get(var2);
      this.key = var1.getWrappedKey(this.encryptionMethod);
      this.keyName = var1.getCarriedKeyName();
      this.refs = var4;
   }

   public EncryptingPreprocessor(NamedKey var1, String var2, EncryptXMLOutputStream var3) throws EncryptionException {
      this.enXos = var3;
      this.encryptionMethod = EncryptionMethod.get(var2);
      this.key = var1.getKey();
      this.refs = var1.getReferenceList();
      this.keyName = var1.getName();
   }

   public void begin(StartElement var1, XMLOutputStream var2) throws XMLStreamException {
      if (this.encryptionDepth != 0) {
         ++this.encryptionDepth;
         var2.add(var1);
      } else {
         String var3 = weblogic.xml.security.utils.Utils.generateId("EncryptedData");
         EncryptedData var4 = new EncryptedData();
         var4.setEncryptionMethod(this.encryptionMethod);
         var4.setId(var3);
         var4.setKey(this.key);
         if (this.keyName != null) {
            var4.setKeyInfo(new KeyInfo(this.keyName));
         }

         try {
            if ("Body".equals(var1.getName().getLocalName())) {
               var2.add(var1);
               this.enXos.beginEncrypt(var4);
            } else {
               this.enXos.beginEncrypt(var4);
               var2.add(var1);
            }
         } catch (ClassCastException var6) {
            throw new SecurityProcessingException("Attempted encryption operation on a non-encrypting stream", var6);
         }

         this.refs.addReference(new DataReference(var3));
         ++this.encryptionDepth;
      }
   }

   public void end(EndElement var1, XMLOutputStream var2) throws XMLStreamException {
      --this.encryptionDepth;
      switch (this.encryptionDepth) {
         case -1:
            throw new XMLStreamException("Unbalanced start/end tags on encrypted element");
         case 0:
            if ("Body".equals(var1.getName().getLocalName())) {
               var2.flush();

               try {
                  this.enXos.endEncrypt();
               } catch (ClassCastException var5) {
                  throw new SecurityProcessingException("Attempted encryption operation on a non-encrypting stream", var5);
               }

               var2.add(var1);
            } else {
               var2.add(var1);
               var2.flush();

               try {
                  this.enXos.endEncrypt();
               } catch (ClassCastException var4) {
                  throw new SecurityProcessingException("Attempted encryption operation on a non-encrypting stream", var4);
               }
            }
         default:
      }
   }
}
