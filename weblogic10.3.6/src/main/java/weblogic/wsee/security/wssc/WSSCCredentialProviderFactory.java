package weblogic.wsee.security.wssc;

import java.util.HashMap;
import weblogic.xml.crypto.wss.provider.CredentialProvider;

public class WSSCCredentialProviderFactory {
   private HashMap<String, CredentialProvider> credProviders = new HashMap();
   private static final WSSCCredentialProviderFactory INSTANCE = new WSSCCredentialProviderFactory();
   private static final String SCT_VALUE_TYPE_V2005 = "http://schemas.xmlsoap.org/ws/2005/02/sc/sct";
   private static final String CLIENT_SCT_PROVIDER_V2005 = "weblogic.wsee.security.wssc.v200502.sct.ClientSCCredentialProvider";
   private static final String DK_VALUE_TYPE_V2005 = "http://schemas.xmlsoap.org/ws/2005/02/sc/dk";
   private static final String CLIENT_DK_PROVIDER_V2005 = "weblogic.wsee.security.wssc.v200502.dk.DKCredentialProvider";
   private static final String SCT_VALUE_TYPE_V13 = "http://docs.oasis-open.org/ws-sx/ws-secureconversation/200512/sct";
   private static final String CLIENT_SCT_PROVIDER_V13 = "weblogic.wsee.security.wssc.v13.sct.ClientSCCredentialProvider";
   private static final String DK_VALUE_TYPE_V13 = "http://docs.oasis-open.org/ws-sx/ws-secureconversation/200512/dk";
   private static final String CLIENT_DK_PROVIDER_V13 = "weblogic.wsee.security.wssc.v13.dk.DKCredentialProvider";

   private WSSCCredentialProviderFactory() {
      this.credProviders.put("http://schemas.xmlsoap.org/ws/2005/02/sc/sct", newInstance("weblogic.wsee.security.wssc.v200502.sct.ClientSCCredentialProvider"));
      this.credProviders.put("http://schemas.xmlsoap.org/ws/2005/02/sc/dk", newInstance("weblogic.wsee.security.wssc.v200502.dk.DKCredentialProvider"));
      this.credProviders.put("http://docs.oasis-open.org/ws-sx/ws-secureconversation/200512/sct", newInstance("weblogic.wsee.security.wssc.v13.sct.ClientSCCredentialProvider"));
      this.credProviders.put("http://docs.oasis-open.org/ws-sx/ws-secureconversation/200512/dk", newInstance("weblogic.wsee.security.wssc.v13.dk.DKCredentialProvider"));
   }

   public static final WSSCCredentialProviderFactory getInstance() {
      return INSTANCE;
   }

   public CredentialProvider getCredentialProvider(String var1) {
      return (CredentialProvider)this.credProviders.get(var1);
   }

   private static final CredentialProvider newInstance(String var0) {
      try {
         Class var1 = Class.forName(var0);
         return (CredentialProvider)var1.newInstance();
      } catch (ClassNotFoundException var2) {
         var2.printStackTrace();
      } catch (IllegalAccessException var3) {
         var3.printStackTrace();
      } catch (InstantiationException var4) {
         var4.printStackTrace();
      }

      return null;
   }
}
