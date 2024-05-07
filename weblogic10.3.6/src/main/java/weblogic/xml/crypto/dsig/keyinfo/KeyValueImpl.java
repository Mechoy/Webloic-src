package weblogic.xml.crypto.dsig.keyinfo;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import weblogic.xml.crypto.api.MarshalException;
import weblogic.xml.crypto.dsig.DsigConstants;
import weblogic.xml.crypto.dsig.KeyInfoObjectFactory;
import weblogic.xml.crypto.dsig.WLXMLStructure;
import weblogic.xml.crypto.utils.StaxUtils;

public class KeyValueImpl extends KeyInfoObjectBase implements KeyInfoObjectFactory {
   KeyValueImpl() {
   }

   public static void init() {
      KeyValueImpl var0 = new KeyValueImpl();
      register(var0);
   }

   protected PublicKey createFromKeySpec(String var1, KeySpec var2) throws MarshalException {
      String var4;
      try {
         KeyFactory var3 = KeyFactory.getInstance(var1);
         return var3.generatePublic(var2);
      } catch (NoSuchAlgorithmException var5) {
         var4 = var1 + " KeyFactory implementation not registered";
         throw new MarshalException(var4, var5);
      } catch (InvalidKeySpecException var6) {
         var4 = "Invalid key parameters";
         throw new MarshalException(var4, var6);
      }
   }

   public QName getQName() {
      return DsigConstants.KEYVALUE_QNAME;
   }

   public Object newKeyInfoObject(XMLStreamReader var1) throws MarshalException {
      Object var2 = null;

      try {
         var1.nextTag();
         String var3 = var1.getLocalName();
         if ("DSAKeyValue".equals(var3)) {
            var2 = new DSAKeyValue();
         } else if ("RSAKeyValue".equals(var3)) {
            var2 = new RSAKeyValue();
         }

         ((WLXMLStructure)var2).read(var1);
         StaxUtils.forwardToEndElement("http://www.w3.org/2000/09/xmldsig#", "KeyValue", var1);
         return var2;
      } catch (XMLStreamException var4) {
         throw new MarshalException("Failed to read KeyValue element.", var4);
      }
   }
}
