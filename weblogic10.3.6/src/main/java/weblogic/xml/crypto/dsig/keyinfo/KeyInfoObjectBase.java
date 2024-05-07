package weblogic.xml.crypto.dsig.keyinfo;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;
import weblogic.utils.collections.ConcurrentHashMap;
import weblogic.xml.crypto.api.MarshalException;
import weblogic.xml.crypto.dsig.KeyInfoObjectFactory;
import weblogic.xml.crypto.encrypt.WLEncryptedKey;
import weblogic.xml.crypto.wss.SecurityTokenReferenceImpl;

public abstract class KeyInfoObjectBase {
   private static final ConcurrentHashMap factories = new ConcurrentHashMap();

   private static final void initFactories() {
      KeyNameImpl.init();
      KeyValueImpl.init();
      RetrievalMethodImpl.init();
      X509DataImpl.init();
      WLEncryptedKey.init();
      register(new SecurityTokenReferenceImpl());
   }

   public static void register(KeyInfoObjectFactory var0) {
      factories.put(var0.getQName(), var0);
   }

   public static Object readKeyInfoObject(XMLStreamReader var0) throws MarshalException {
      QName var1 = var0.getName();
      KeyInfoObjectFactory var2 = (KeyInfoObjectFactory)factories.get(var1);
      if (var2 == null) {
         throw new MarshalException("KeyInfo child element " + var1 + " not supported.");
      } else {
         Object var3 = var2.newKeyInfoObject(var0);
         return var3;
      }
   }

   static {
      initFactories();
   }
}
