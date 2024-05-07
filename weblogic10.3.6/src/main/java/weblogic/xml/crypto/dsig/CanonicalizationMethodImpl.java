package weblogic.xml.crypto.dsig;

import java.security.NoSuchAlgorithmException;
import weblogic.utils.collections.ConcurrentHashMap;
import weblogic.xml.crypto.dsig.api.CanonicalizationMethod;
import weblogic.xml.crypto.dsig.api.spec.C14NMethodParameterSpec;

public abstract class CanonicalizationMethodImpl {
   public static final String C14NMETHOD_ELEMENT = "CanonicalizationMethod";
   public static final String ALGORITHM_ATTRIBUTE = "Algorithm";
   private static final ConcurrentHashMap factories = new ConcurrentHashMap();

   private static final void initFactories() {
      CanonicalizationMethodW3C.init();
   }

   public static void register(CanonicalizationMethodFactory var0) {
      factories.put(var0.getURI(), var0);
   }

   public static CanonicalizationMethod newCanonicalizationMethod(String var0) throws NoSuchAlgorithmException {
      return newCanonicalizationMethod(var0, (C14NMethodParameterSpec)null);
   }

   public static CanonicalizationMethod newCanonicalizationMethod(String var0, C14NMethodParameterSpec var1) throws NoSuchAlgorithmException {
      CanonicalizationMethodFactory var2 = (CanonicalizationMethodFactory)factories.get(var0);
      if (var2 == null) {
         throw new NoSuchAlgorithmException(var0 + " not supported");
      } else {
         return var2.newCanonicalizationMethod();
      }
   }

   static {
      initFactories();
   }
}
