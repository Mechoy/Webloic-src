package weblogic.wsee.security.wst.helpers;

import java.util.Calendar;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.soap.SOAPFaultException;
import weblogic.wsee.security.configuration.TimestampConfiguration;
import weblogic.wsee.security.policy.WssPolicyContext;
import weblogic.wsee.security.wst.binding.RequestSecurityToken;
import weblogic.wsee.security.wst.binding.TokenType;
import weblogic.wsee.security.wst.faults.RequestFailedException;
import weblogic.wsee.security.wst.framework.TrustToken;
import weblogic.wsee.security.wst.framework.TrustTokenProvider;
import weblogic.wsee.security.wst.framework.TrustTokenProviderRegistry;
import weblogic.wsee.security.wst.framework.WSTContext;
import weblogic.wsee.util.Verbose;
import weblogic.xml.crypto.wss.SecurityTokenHelper;
import weblogic.xml.crypto.wss.WSSecurityContext;
import weblogic.xml.crypto.wss.api.KeyIdentifier;
import weblogic.xml.crypto.wss.provider.SecurityToken;
import weblogic.xml.crypto.wss.provider.SecurityTokenReference;

public class TrustTokenHelper {
   private static final boolean verbose = Verbose.isVerbose(TrustTokenHelper.class);

   public static boolean isExpired(MessageContext var0, Calendar var1, Calendar var2) {
      WssPolicyContext var4 = (WssPolicyContext)var0.getProperty("weblogic.weblogic.wsee.security.policy.WssPolicyCtx");
      TimestampConfiguration var3;
      if (var4 != null) {
         var3 = var4.getWssConfiguration().getTimestampConfig();
      } else {
         var3 = new TimestampConfiguration();
      }

      try {
         var3.checkExpiration(var1, var2);
         return false;
      } catch (SOAPFaultException var6) {
         return true;
      }
   }

   public static TrustTokenProvider resolveTrustProvider(String var0) throws RequestFailedException {
      TrustTokenProviderRegistry var1 = TrustTokenProviderRegistry.getInstance();
      TrustTokenProvider var2 = var1.getTrustTokenProvider(var0);
      if (var2 == null) {
         throw new RequestFailedException("Trust is unable to handle token type: " + var0);
      } else {
         if (verbose) {
            Verbose.log((Object)(" for tokenType='" + var0 + "', returning TTP='" + var2.getClass().getName() + "'"));
         }

         return var2;
      }
   }

   public static TrustTokenProvider resolveTrustProvider(RequestSecurityToken var0, SecurityTokenReference var1) throws RequestFailedException {
      String var2 = null;
      TokenType var3 = var0.getTokenType();
      if (var3 != null) {
         if (verbose) {
            Verbose.log((Object)(" resolveTrustProvider tokenType from RST= '" + var3 + "'"));
         }

         var2 = var3.getTokenType();
      } else {
         var2 = var1.getValueType();
         if (verbose) {
            Verbose.log((Object)(" resolveTrustProvider tokenType from STR= '" + var3 + "'"));
         }
      }

      return resolveTrustProvider(var2);
   }

   public static TrustToken getTrustCredentialFromSecurityContext(WSTContext var0, SecurityTokenReference var1) {
      MessageContext var2 = var0.getMessageContext();
      if (var2 == null) {
         return null;
      } else {
         WSSecurityContext var3 = WSSecurityContext.getSecurityContext(var2);
         if (var3 == null) {
            return null;
         } else {
            String var4 = var1.getReferenceURI();
            if (var4 == null) {
               KeyIdentifier var5 = var1.getKeyIdentifier();
               if (var5 != null) {
                  var4 = new String(var5.getIdentifier());
               }
            } else if (var4.startsWith("#")) {
               var4 = var4.substring(1);
            }

            if (var4 == null) {
               return null;
            } else {
               SecurityToken var6 = SecurityTokenHelper.findSecurityTokenByIdInContext(var3, var1.getValueType(), var4);
               return var6 instanceof TrustToken ? (TrustToken)var6 : null;
            }
         }
      }
   }

   public static boolean isWsscTokenType(String var0) {
      return null == var0 ? false : var0.endsWith("/sct");
   }
}
