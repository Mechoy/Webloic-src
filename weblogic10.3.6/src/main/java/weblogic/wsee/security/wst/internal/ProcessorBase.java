package weblogic.wsee.security.wst.internal;

import javax.xml.rpc.handler.MessageContext;
import weblogic.wsee.security.configuration.WssConfigurationException;
import weblogic.wsee.security.policy.WssPolicyContext;
import weblogic.wsee.security.wst.binding.AppliesTo;
import weblogic.wsee.security.wst.binding.KeySize;
import weblogic.wsee.security.wst.binding.KeyType;
import weblogic.wsee.security.wst.binding.Lifetime;
import weblogic.wsee.security.wst.binding.RequestSecurityToken;
import weblogic.wsee.security.wst.binding.RequestSecurityTokenResponse;
import weblogic.wsee.security.wst.binding.RequestType;
import weblogic.wsee.security.wst.binding.TokenType;
import weblogic.wsee.security.wst.faults.BadRequestException;
import weblogic.wsee.security.wst.framework.TrustProcessor;
import weblogic.wsee.security.wst.framework.WSTContext;
import weblogic.xml.crypto.wss.provider.SecurityTokenHandler;

public abstract class ProcessorBase implements TrustProcessor {
   protected static SecurityTokenHandler getSecurityTokenHandler(WSTContext var0) {
      MessageContext var1 = var0.getMessageContext();
      if (var1 != null) {
         WssPolicyContext var2 = (WssPolicyContext)var1.getProperty("weblogic.weblogic.wsee.security.policy.WssPolicyCtx");
         if (var2 != null) {
            try {
               SecurityTokenHandler var3 = var2.getWssConfiguration().getTokenHandler(var0.getTokenType(), (String)null);
               return var3;
            } catch (WssConfigurationException var4) {
            }
         }
      }

      return null;
   }

   protected static RequestSecurityTokenResponse createRSTR(RequestSecurityToken var0, WSTContext var1) {
      String var2 = var1.getWstNamespaceURI();
      RequestSecurityTokenResponse var3 = new RequestSecurityTokenResponse(var2);
      return var3;
   }

   protected static void setRequestType(RequestSecurityToken var0, RequestSecurityTokenResponse var1, WSTContext var2) throws BadRequestException {
      RequestType var3 = var0.getRequestType();
      if (var3 == null) {
         throw new BadRequestException("RequestType is missing in RST");
      } else {
         String var4 = var3.getRequestType();
         var3 = new RequestType(var2.getWstNamespaceURI());
         var3.setRequestType(var4);
         var1.setRequestType(var3);
      }
   }

   protected static String setTokenType(RequestSecurityToken var0, RequestSecurityTokenResponse var1, WSTContext var2) throws BadRequestException {
      String var3 = null;
      TokenType var4 = var0.getTokenType();
      if (var4 == null) {
         var3 = var2.getTokenType();
      } else {
         var3 = var4.getTokenType();
         var2.setTokenType(var3);
      }

      if (var3 == null) {
         throw new BadRequestException("Can not determine requested token type from RST or WSTContext.");
      } else {
         var4 = new TokenType(var1.getNamespaceURI());
         var4.setTokenType(var3);
         var1.setTokenType(var4);
         return var3;
      }
   }

   protected void setAppliesTo(RequestSecurityToken var1, RequestSecurityTokenResponse var2, WSTContext var3) {
      AppliesTo var4 = var1.getAppliesTo();
      AppliesTo var5;
      if (var4 != null) {
         var5 = new AppliesTo(var4.getNamespaceURI());
         if (var4.getElement() != null) {
            var5.setElement(var4.getElement());
            var3.setAppliesToElement(var4.getElement());
         } else {
            String var6 = var4.getEndpointReference();
            var5.setEndpointReference(var3.getWsaNamespaceURI(), var6);
            var3.setAppliesTo(var6);
         }

         var2.setAppliesTo(var5);
      } else if (var3.getAppliesToElement() != null) {
         var5 = new AppliesTo(var3.getWspNamespaceURI());
         var5.setElement(var3.getAppliesToElement());
         var2.setAppliesTo(var5);
      } else if (var3.getAppliesTo() != null) {
         var5 = new AppliesTo(var3.getWspNamespaceURI());
         var5.setEndpointReference(var3.getWsaNamespaceURI(), var3.getAppliesTo());
         var2.setAppliesTo(var5);
      }

   }

   protected void setLifetime(RequestSecurityToken var1, RequestSecurityTokenResponse var2, WSTContext var3, boolean var4) {
      Lifetime var5 = var1.getLifetime();
      if (var5 == null) {
         var5 = new Lifetime(var3.getWstNamespaceURI());
         var5.setPeriod(var3.getLifetimePeriod(), var3.getWsuNamespaceURI());
      }

      if (var4) {
         long var6 = var5.getExpires().getTimeInMillis() - var5.getCreated().getTimeInMillis();
         var5.setPeriod(var6, var3.getWsuNamespaceURI());
      }

      var2.setLifetime(var5);
      var3.setCreated(var5.getCreated());
      var3.setExpires(var5.getExpires());
   }

   protected void setKeySize(RequestSecurityToken var1, RequestSecurityTokenResponse var2, WSTContext var3) {
      KeySize var5 = var1.getKeySize();
      int var4;
      if (var5 == null) {
         var4 = var3.getKeySize();
      } else {
         var4 = var5.getSize();
      }

      var5 = new KeySize(var2.getNamespaceURI());
      var5.setSize(var4);
      var2.setKeySize(var5);
      var3.setKeySize(var4);
   }

   protected void setKeyType(RequestSecurityToken var1, String var2, WSTContext var3) {
      KeyType var4 = var1.getKeyType();
      if (var4 != null) {
         var3.setKeyType(var4.getKeyType());
      }

   }
}
