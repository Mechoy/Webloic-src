package weblogic.wsee.security.wssc.base.sct;

import java.security.Key;
import weblogic.wsee.security.wssc.sct.SCCredential;
import weblogic.wsee.security.wst.binding.AppliesTo;
import weblogic.wsee.security.wst.binding.Lifetime;
import weblogic.wsee.security.wst.binding.RequestSecurityTokenResponse;
import weblogic.wsee.security.wst.binding.RequestedAttachedReference;
import weblogic.wsee.security.wst.binding.RequestedSecurityToken;
import weblogic.wsee.security.wst.binding.RequestedUnattachedReference;
import weblogic.wsee.security.wst.binding.TokenType;
import weblogic.wsee.security.wst.faults.RequestFailedException;
import weblogic.wsee.security.wst.faults.WSTFaultException;
import weblogic.wsee.security.wst.framework.WSTContext;
import weblogic.xml.crypto.wss.provider.SecurityToken;
import weblogic.xml.crypto.wss.provider.SecurityTokenReference;

public class SCTHelper {
   public static SCCredential getSCCredentialFromRSTR(WSTContext var0, Key var1, RequestSecurityTokenResponse var2, SCTokenHandlerBase var3) throws WSTFaultException {
      SCCredential var4 = var3.newSCCredential();
      TokenType var5 = var2.getTokenType();
      if (var5 != null && !var5.getTokenType().equals(var3.getSCT_VALUE_TYPE())) {
         throw new RequestFailedException("Unexpected token type in RSTR: " + var5.getTokenType());
      } else {
         RequestedSecurityToken var6 = var2.getRequestedSecurityToken();
         if (var6 == null) {
            throw new RequestFailedException("RequestedSecurityToken must be specified");
         } else {
            SecurityToken var7 = var6.getSecurityToken();
            if (!(var7 instanceof SCTokenBase)) {
               throw new RequestFailedException(var7.getValueType() + " is not a SCT");
            } else {
               SCTokenBase var8 = (SCTokenBase)var7;
               SCCredential var9 = var8.getCredential();
               SCCredential.copy(var9, var4);
               var4.setSecret(var1);
               Lifetime var10 = var2.getLifetime();
               var4.setCreated(var10.getCreated());
               var4.setExpires(var10.getExpires());
               AppliesTo var11 = var2.getAppliesTo();
               if (var11 != null) {
                  var4.setAppliesTo(var11.getEndpointReference());
                  var4.setAppliesToElement(var11.getElement());
               } else {
                  var4.setAppliesTo(var0.getAppliesTo());
                  var4.setAppliesToElement(var0.getAppliesToElement());
               }

               RequestedAttachedReference var12 = var2.getRequestedAttachedReference();
               if (var12 != null) {
                  SecurityTokenReference var13 = var12.getSecurityTokenReference();
                  SCCredential.SecurityTokenReferenceInfo var14 = var4.newAttachedSecurityTokenReferenceInfo();
                  SCCredential.copyFromSTRToInfo(var13, var14);
               }

               RequestedUnattachedReference var16 = var2.getRequestedUnattachedReference();
               if (var16 != null) {
                  SecurityTokenReference var17 = var16.getSecurityTokenReference();
                  SCCredential.SecurityTokenReferenceInfo var15 = var4.newUnattachedSecurityTokenReferenceInfo();
                  SCCredential.copyFromSTRToInfo(var17, var15);
               }

               return var4;
            }
         }
      }
   }
}
