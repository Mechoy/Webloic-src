package weblogic.wsee.security.wss.plan;

import java.util.List;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import weblogic.wsee.security.policy.SigningReferencesFactory;
import weblogic.wsee.security.wss.policy.SecurityPolicyArchitectureException;
import weblogic.wsee.security.wss.policy.wssp.EncryptionPolicyOutlineImpl;
import weblogic.wsee.security.wss.policy.wssp.SigningPolicyOutlineImpl;
import weblogic.wsee.util.Verbose;
import weblogic.xml.crypto.dsig.api.XMLSignatureFactory;
import weblogic.xml.crypto.encrypt.api.XMLEncryptionFactory;
import weblogic.xml.crypto.wss11.internal.SecurityBuilder;

public class SecurityPolicyOutline extends SecurityPolicyPlanImpl implements SecurityPolicyPlan {
   private static final boolean verbose = Verbose.isVerbose(SecurityPolicyOutline.class);
   private static final boolean debug = false;
   protected List requiredElements;

   public SecurityPolicyOutline() {
      this.signingPolicy = new SigningPolicyOutlineImpl();
      this.encryptionPolicy = new EncryptionPolicyOutlineImpl();
      this.endorsingPolicy = new SigningPolicyOutlineImpl();
   }

   public SecurityBuilder getSecurityBuilder() {
      throw new UnsupportedOperationException("Not supported from outline");
   }

   public XMLEncryptionFactory getXmlEncryptionFactory() {
      throw new UnsupportedOperationException("Not supported from outline");
   }

   public XMLSignatureFactory getXmlSignatureFactory() {
      throw new UnsupportedOperationException("Not supported from outline");
   }

   public SigningReferencesFactory getSigningReferencesFactory() {
      throw new UnsupportedOperationException("Not supported from outline");
   }

   public void verifyPolicy(SOAPMessageContext var1) throws SecurityPolicyArchitectureException {
      if (this.getBuildingPlan() == 0) {
         throw new IllegalStateException("The outline is never been described.");
      } else {
         if (null != this.requiredElements && this.requiredElements.size() > 0) {
         }

      }
   }

   public void setRequiredElements(List var1) {
      this.requiredElements = var1;
   }

   public boolean isBodyEmpty() {
      return this.isBodyEmpty;
   }

   public void setBodyEmpty(boolean var1) {
      this.isBodyEmpty = var1;
   }
}
