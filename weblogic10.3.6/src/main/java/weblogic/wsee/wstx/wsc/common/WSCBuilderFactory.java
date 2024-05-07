package weblogic.wsee.wstx.wsc.common;

import com.sun.xml.ws.api.message.Header;
import com.sun.xml.ws.api.message.HeaderList;
import weblogic.wsee.wstx.wsat.Transactional;
import weblogic.wsee.wstx.wsat.Transactional.Version;
import weblogic.wsee.wstx.wsat.security.ClientPolicyFeatureBuilder;
import weblogic.wsee.wstx.wsat.security.IssuedTokenBuilder;
import weblogic.wsee.wstx.wsc.common.client.RegistrationMessageBuilder;
import weblogic.wsee.wstx.wsc.common.client.RegistrationProxyBuilder;
import weblogic.wsee.wstx.wsc.v11.WSCBuilderFactoryImpl;

public abstract class WSCBuilderFactory {
   public static WSCBuilderFactory newInstance(Transactional.Version var0) {
      if (Version.WSAT10 != var0 && Version.DEFAULT != var0) {
         if (Version.WSAT11 != var0 && Version.WSAT12 != var0) {
            throw new IllegalArgumentException(var0 + "is not a supported ws-at version");
         } else {
            return new WSCBuilderFactoryImpl();
         }
      } else {
         return new weblogic.wsee.wstx.wsc.v10.WSCBuilderFactoryImpl();
      }
   }

   public static WSCBuilderFactory fromHeaders(HeaderList var0) {
      Object var1 = null;

      for(int var2 = 0; var2 < var0.size(); ++var2) {
         Header var3 = var0.get(var2);
         if (var3.getLocalPart().equals("CoordinationContext")) {
            if ("http://schemas.xmlsoap.org/ws/2004/10/wsat".equals(var3.getNamespaceURI())) {
               var1 = new weblogic.wsee.wstx.wsc.v10.WSCBuilderFactoryImpl();
            } else if ("http://docs.oasis-open.org/ws-tx/wsat/2006/06".equals(var3.getNamespaceURI())) {
               var1 = new WSCBuilderFactoryImpl();
            }

            if (var1 != null) {
               var0.understood(var2);
               return (WSCBuilderFactory)var1;
            }
         }
      }

      return null;
   }

   public abstract WSATCoordinationContextBuilder newWSATCoordinationContextBuilder();

   public abstract RegistrationProxyBuilder newRegistrationProxyBuilder();

   public abstract RegistrationMessageBuilder newWSATRegistrationRequestBuilder();

   public abstract IssuedTokenBuilder newIssuedTokenBuilder();

   public abstract ClientPolicyFeatureBuilder newWSATReqistrationClientPolicyFeatureBuilder();
}
