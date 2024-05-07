package weblogic.wsee.security.wss.plan;

import weblogic.wsee.policy.framework.PolicyAlternative;
import weblogic.wsee.security.policy.SecurityToken;
import weblogic.wsee.security.wss.policy.EncryptionPolicy;
import weblogic.wsee.security.wss.policy.GeneralPolicy;
import weblogic.wsee.security.wss.policy.IdentityPolicy;
import weblogic.wsee.security.wss.policy.SignaturePolicy;
import weblogic.wsee.security.wss.policy.TimestampPolicy;
import weblogic.wsee.security.wss.policy.wssp.GeneralPolicyImpl;
import weblogic.wsee.security.wss.policy.wssp.IdentityPolicyImpl;
import weblogic.wsee.security.wss.policy.wssp.TimestampPolicyImpl;
import weblogic.wsee.util.Verbose;
import weblogic.xml.crypto.wss.BSTUtils;

public class SecurityPolicyPlanImpl implements SecurityPolicyPlan {
   private static final boolean verbose = Verbose.isVerbose(SecurityPolicyPlanImpl.class);
   private static final boolean debug = false;
   private boolean isRequest;
   private boolean isSymmeticPlan = false;
   private boolean isEncryptedKeyRequired = false;
   private boolean hasSecurity = true;
   private boolean hasMessageSecurity;
   protected boolean isBodyEmpty = false;
   private int buildingPlan = 0;
   protected PolicyAlternative policyAlternative;
   protected EncryptionPolicy encryptionPolicy;
   protected SignaturePolicy signingPolicy;
   protected IdentityPolicy identityPolicy = new IdentityPolicyImpl();
   protected TimestampPolicy timestampPolicy = new TimestampPolicyImpl();
   protected GeneralPolicy generalPolicy = new GeneralPolicyImpl();
   protected SignaturePolicy endorsingPolicy;
   private SecurityToken policyIdToken;

   public boolean isRequest() {
      return this.isRequest;
   }

   public void setRequest(boolean var1) {
      this.isRequest = var1;
   }

   public boolean isHasSecurity() {
      return this.hasSecurity;
   }

   public void setHasSecurity(boolean var1) {
      this.hasSecurity = var1;
   }

   public boolean isHasMessageSecurity() {
      return this.hasMessageSecurity;
   }

   public void setHasMessageSecurity(boolean var1) {
      this.hasMessageSecurity = var1;
   }

   public EncryptionPolicy getEncryptionPolicy() {
      return this.encryptionPolicy;
   }

   public void setEncryptionPolicy(EncryptionPolicy var1) {
      this.encryptionPolicy = var1;
   }

   public SignaturePolicy getSigningPolicy() {
      return this.signingPolicy;
   }

   public void setSigningPolicy(SignaturePolicy var1) {
      this.signingPolicy = var1;
   }

   public SignaturePolicy getEndorsingPolicy() {
      return this.endorsingPolicy;
   }

   public void setEndorsingPolicy(SignaturePolicy var1) {
      this.endorsingPolicy = var1;
   }

   public int getBuildingPlan() {
      return this.buildingPlan;
   }

   public void setBuildingPlan(int var1) {
      this.buildingPlan = var1;
   }

   public void addActionToBuildingPlan(int var1) {
      this.buildingPlan |= var1;
   }

   public IdentityPolicy getIdentityPolicy() {
      return this.identityPolicy;
   }

   public void setIdentityPolicy(IdentityPolicy var1) {
      this.identityPolicy = var1;
   }

   public GeneralPolicy getGeneralPolicy() {
      return this.generalPolicy;
   }

   public void setGeneralPolicy(GeneralPolicy var1) {
      this.generalPolicy = var1;
   }

   public TimestampPolicy getTimestampPolicy() {
      return this.timestampPolicy;
   }

   public SecurityToken getPolicyIdToken() {
      return this.policyIdToken;
   }

   public void setPolicyIdToken(SecurityToken var1) {
      this.policyIdToken = var1;
   }

   public GeneralPolicy getGenrealPolicy() {
      return this.generalPolicy;
   }

   public void setGenrealPolicy(GeneralPolicy var1) {
      this.generalPolicy = var1;
   }

   public boolean isX509AuthConditional() {
      if (!this.signingPolicy.isX509AuthConditional()) {
         return false;
      } else {
         return this.policyIdToken == null || !BSTUtils.isX509Type(this.policyIdToken.getTokenTypeUri());
      }
   }

   public boolean isSymmeticPlan() {
      return this.isSymmeticPlan;
   }

   public void setSymmeticPlan(boolean var1) {
      this.isSymmeticPlan = var1;
   }

   public boolean isEncryptedKeyRequired() {
      return this.isEncryptedKeyRequired;
   }

   public void setEncryptedKeyRequired(boolean var1) {
      this.isEncryptedKeyRequired = var1;
   }

   public boolean isBodyEmpty() {
      return false;
   }

   public PolicyAlternative getPolicyAlternative() {
      return this.policyAlternative;
   }

   public void setPolicyAlternative(PolicyAlternative var1) {
      this.policyAlternative = var1;
   }

   public boolean hasTransportSecuirity() {
      return this.generalPolicy.isHTTPsRequired();
   }
}
