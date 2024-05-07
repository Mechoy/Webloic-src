package weblogic.xml.crypto.encrypt;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.util.List;
import java.util.Map;
import javax.xml.stream.XMLStreamException;
import org.w3c.dom.Element;
import weblogic.xml.crypto.api.MarshalException;
import weblogic.xml.crypto.api.XMLStructure;
import weblogic.xml.crypto.dsig.CanonicalizationMethodImpl;
import weblogic.xml.crypto.dsig.api.CanonicalizationMethod;
import weblogic.xml.crypto.dsig.api.keyinfo.KeyInfo;
import weblogic.xml.crypto.dsig.api.spec.C14NMethodParameterSpec;
import weblogic.xml.crypto.encrypt.api.CipherReference;
import weblogic.xml.crypto.encrypt.api.DataReference;
import weblogic.xml.crypto.encrypt.api.EncryptedData;
import weblogic.xml.crypto.encrypt.api.EncryptedType;
import weblogic.xml.crypto.encrypt.api.EncryptionMethod;
import weblogic.xml.crypto.encrypt.api.EncryptionProperties;
import weblogic.xml.crypto.encrypt.api.EncryptionProperty;
import weblogic.xml.crypto.encrypt.api.KeyReference;
import weblogic.xml.crypto.encrypt.api.TBE;
import weblogic.xml.crypto.encrypt.api.TBEKey;
import weblogic.xml.crypto.encrypt.api.XMLDecryptContext;
import weblogic.xml.crypto.encrypt.api.XMLEncryptionException;
import weblogic.xml.crypto.encrypt.api.XMLEncryptionFactory;
import weblogic.xml.crypto.encrypt.api.dom.DOMDecryptContext;
import weblogic.xml.crypto.encrypt.api.keyinfo.AgreementMethod;
import weblogic.xml.crypto.encrypt.api.keyinfo.EncryptedKey;
import weblogic.xml.crypto.encrypt.api.spec.EncryptionMethodParameterSpec;
import weblogic.xml.dom.DOMStreamReader;

public class WLXMLEncryptionFactory extends XMLEncryptionFactory {
   public static final boolean VERBOSE = false;

   public WLXMLEncryptionFactory() {
      super("DOM", (Provider)null);
   }

   public AgreementMethod newAgreementMethod(String var1, byte[] var2, XMLStructure var3, XMLStructure var4, List var5) {
      return null;
   }

   public CanonicalizationMethod newCanonicalizationMethod(String var1, C14NMethodParameterSpec var2) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
      return CanonicalizationMethodImpl.newCanonicalizationMethod(var1, var2);
   }

   public CipherReference newCipherReference(String var1, List var2) {
      return new WLCipherReference(var1, var2);
   }

   public DataReference newDataReference(String var1, List var2) {
      return new WLDataReference(var1, var2);
   }

   public EncryptedData newEncryptedData(TBE var1, EncryptionMethod var2, KeyInfo var3, EncryptionProperties var4, String var5, CipherReference var6) {
      return new WLEncryptedData(var1, var2, var3, var4, var5, var6);
   }

   public EncryptedKey newEncryptedKey(TBEKey var1, EncryptionMethod var2, KeyInfo var3, EncryptionProperties var4, List var5, String var6, String var7, String var8, CipherReference var9) {
      return new WLEncryptedKey(var1, var2, var3, var4, var5, var6, var7, var8, var9);
   }

   public EncryptionMethod newEncryptionMethod(String var1, Integer var2, EncryptionMethodParameterSpec var3) {
      WLEncryptionMethod var4 = null;

      try {
         var4 = WLEncryptionMethod.get(var1, var2, var3);
      } catch (XMLEncryptionException var6) {
      }

      return var4;
   }

   public EncryptionProperties newEncryptionProperties(List var1, String var2) {
      return null;
   }

   public EncryptionProperty newEncryptionProperty(List var1, String var2, String var3, Map var4) {
      return null;
   }

   public KeyReference newKeyReference(String var1, List var2) {
      return null;
   }

   public EncryptedType unmarshalEncryptedType(XMLDecryptContext var1) throws MarshalException {
      if (!(var1 instanceof DOMDecryptContext)) {
         throw new IllegalArgumentException("Unsupported context class");
      } else {
         DOMDecryptContext var2 = (DOMDecryptContext)var1;
         Element var3 = var2.getNode();

         try {
            DOMStreamReader var4 = new DOMStreamReader(var3);
            return WLEncryptedType.newInstance(var4);
         } catch (XMLStreamException var5) {
            throw new MarshalException(var5);
         }
      }
   }
}
