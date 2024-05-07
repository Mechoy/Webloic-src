package weblogic.wsee.security.wssc.base.dk;

import java.util.List;
import javax.xml.namespace.QName;
import weblogic.security.service.ContextHandler;
import weblogic.wsee.security.saml.SAMLUtils;
import weblogic.wsee.security.wss.plan.helper.TokenReferenceTypeHelper;
import weblogic.wsee.security.wssc.base.faults.WSCFaultException;
import weblogic.wsee.security.wssc.dk.DKClaims;
import weblogic.wsee.security.wssc.dk.DKCredential;
import weblogic.wsee.security.wst.faults.WSTFaultUtil;
import weblogic.wsee.util.Verbose;
import weblogic.xml.crypto.utils.KeyUtils;
import weblogic.xml.crypto.wss.WSSConstants;
import weblogic.xml.crypto.wss.WSSecurityContext;
import weblogic.xml.crypto.wss.WSSecurityException;
import weblogic.xml.crypto.wss.provider.CredentialProvider;
import weblogic.xml.crypto.wss.provider.Purpose;
import weblogic.xml.crypto.wss.provider.SecurityToken;
import weblogic.xml.crypto.wss.provider.SecurityTokenHandler;
import weblogic.xml.crypto.wss.provider.SecurityTokenReference;
import weblogic.xml.crypto.wss11.internal.STRType;

public abstract class DKCredentialProviderBase implements CredentialProvider {
   public static final boolean verbose = Verbose.isVerbose(DKCredentialProviderBase.class);

   protected DKCredentialProviderBase() {
   }

   public abstract String[] getValueTypes();

   protected abstract String getURI_P_SHA1();

   protected abstract WSCFaultException newUnknownDerivationSourceException(String var1);

   public Object getCredential(String var1, String var2, ContextHandler var3, Purpose var4) {
      return var4 != null && !var4.equals(Purpose.IDENTITY) && !var4.equals(Purpose.ENCRYPT_RESPONSE) ? this.createNewDK(var3, var4) : null;
   }

   protected Object createNewDK(ContextHandler var1, Purpose var2) {
      try {
         SecurityToken var3 = (SecurityToken)var1.getValue("weblogic.wsee.wsc.derived_from_token");
         if (var3 == null) {
            return null;
         } else {
            WSSecurityContext var4 = (WSSecurityContext)var1.getValue("com.bea.contextelement.xml.SecurityInfo");
            String var5 = var3.getValueType();
            SecurityTokenHandler var6 = var4.getRequiredTokenHandler(var5);
            QName var7 = (QName)var1.getValue("weblogic.wsee.dk.referece_type");
            if (SAMLUtils.isSamlTokenType(var5)) {
               List var8 = TokenReferenceTypeHelper.getSTRTypesForSAMLIssuedToken(var5);
               if (var8 != null && var8.size() > 0) {
                  var7 = ((STRType)var8.get(0)).getTopLevelElement();
               }
            }

            if (var7 == null) {
               var7 = WSSConstants.REFERENCE_QNAME;
            }

            SecurityTokenReference var11 = var6.getSTR(var7, var5, var3);
            DKCredential var9 = new DKCredential();
            var9.setSecurityToken(var3);
            var9.setTokenReference(var11);
            var9.setAlgorithm(this.getURI_P_SHA1());
            var9.setLabel(DKClaims.getLabelFromContextHandler(var1));
            var9.setLength(DKClaims.getLengthFromContextHandler(var1));
            var9.setGeneration(0);
            var9.setNonce(KeyUtils.createNonce());
            if (verbose) {
               Verbose.banner("Returning DK Credential");
               Verbose.log((Object)("DKLabel: " + var9.getLabel()));
               Verbose.log((Object)("DKLength: " + var9.getLength()));
            }

            if (SAMLUtils.isSamlTokenType(var5)) {
               var9.setSecretKey(var3.getSecretKey());
            }

            return var9;
         }
      } catch (WSSecurityException var10) {
         WSTFaultUtil.raiseFault(this.newUnknownDerivationSourceException("Can not create DerivedKey Token"));
         return null;
      }
   }
}
