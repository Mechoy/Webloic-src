package weblogic.xml.security.keyinfo;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.interfaces.DSAPublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import weblogic.xml.security.signature.DSIGConstants;
import weblogic.xml.security.utils.StreamUtils;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLOutputStream;
import weblogic.xml.stream.XMLStreamException;

public abstract class KeyValue implements DSIGConstants {
   public static KeyValue getKeyValue(PublicKey var0) throws KeyInfoException {
      if (var0 instanceof DSAPublicKey) {
         return new DSAKeyValue((DSAPublicKey)var0);
      } else if (var0 instanceof RSAPublicKey) {
         return new RSAKeyValue((RSAPublicKey)var0);
      } else {
         throw new KeyInfoException("Unknown PublicKey type: " + var0);
      }
   }

   protected PublicKey createFromKeySpec(String var1, KeySpec var2) throws KeyInfoValidationException {
      String var4;
      try {
         KeyFactory var3 = KeyFactory.getInstance(var1);
         return var3.generatePublic(var2);
      } catch (NoSuchAlgorithmException var5) {
         var4 = var1 + " KeyFactory implementation not registered";
         throw new KeyInfoValidationException(var4, var5);
      } catch (InvalidKeySpecException var6) {
         var4 = "Invalid DSA key parameters";
         throw new KeyInfoValidationException(var4, var6);
      }
   }

   public abstract PublicKey getPublicKey() throws KeyInfoValidationException;

   public abstract void validate() throws KeyInfoValidationException;

   public abstract void toXML(XMLOutputStream var1, String var2, int var3) throws XMLStreamException;

   public static KeyValue fromXML(XMLInputStream var0, String var1) throws XMLStreamException {
      Object var2 = null;
      if (StreamUtils.getElement(var0, var1, "KeyValue") != null) {
         if (StreamUtils.peekElement(var0, var1, "DSAKeyValue")) {
            var2 = new DSAKeyValue(var0, var1);
         } else if (StreamUtils.peekElement(var0, var1, "RSAKeyValue")) {
            var2 = new RSAKeyValue(var0, var1);
         }

         StreamUtils.closeScope(var0, var1, "KeyValue");
      }

      return (KeyValue)var2;
   }
}
