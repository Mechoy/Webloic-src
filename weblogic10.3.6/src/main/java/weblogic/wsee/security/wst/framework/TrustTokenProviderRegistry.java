package weblogic.wsee.security.wst.framework;

import java.util.concurrent.ConcurrentHashMap;
import weblogic.wsee.util.Verbose;

public class TrustTokenProviderRegistry {
   private static final boolean verbose = Verbose.isVerbose(TrustTokenProviderRegistry.class);
   private static TrustTokenProviderRegistry instance = new TrustTokenProviderRegistry();
   private ConcurrentHashMap<String, TrustTokenProvider> providers = new ConcurrentHashMap();

   private TrustTokenProviderRegistry() {
      this.registerProvider("http://docs.oasis-open.org/ws-sx/ws-secureconversation/200512/sct", "weblogic.wsee.security.wssc.v13.sct.ServerSCCredentialProvider");
      this.registerProvider("http://schemas.xmlsoap.org/ws/2005/02/sc/sct", "weblogic.wsee.security.wssc.v200502.sct.ServerSCCredentialProvider");
   }

   public static TrustTokenProviderRegistry getInstance() {
      return instance;
   }

   public TrustTokenProvider registerProvider(String var1, String var2) throws IllegalArgumentException {
      TrustTokenProvider var3 = createTrustProvider(var2);
      this.registerProvider(var1, var3);
      return var3;
   }

   public void registerProvider(String var1, TrustTokenProvider var2) {
      this.providers.put(var1, var2);
   }

   public TrustTokenProvider getTrustTokenProvider(String var1) {
      if (verbose) {
         Verbose.log((Object)("looking up TokenProvider for tokenType='" + var1 + "'"));
      }

      return (TrustTokenProvider)this.providers.get(var1);
   }

   private static final TrustTokenProvider createTrustProvider(String var0) throws IllegalArgumentException {
      try {
         Class var1 = Thread.currentThread().getContextClassLoader().loadClass(var0);
         return (TrustTokenProvider)var1.newInstance();
      } catch (ClassNotFoundException var2) {
         throw new IllegalArgumentException("Can not find trust token provider: " + var0);
      } catch (IllegalAccessException var3) {
         throw new IllegalArgumentException("Can not access trust token provider: " + var0);
      } catch (InstantiationException var4) {
         throw new IllegalArgumentException("Can not instantiate trust token provider: " + var0);
      } catch (ClassCastException var5) {
         throw new IllegalArgumentException(var0 + " is not an instance of TrustTokenProvider");
      }
   }
}
