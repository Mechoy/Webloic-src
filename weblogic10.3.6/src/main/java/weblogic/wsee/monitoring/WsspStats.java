package weblogic.wsee.monitoring;

public interface WsspStats {
   void reportAuthenticationViolation();

   void reportAuthorizationViolation();

   void reportConfidentialityViolation();

   void reportIntegrityViolation();

   void reportAuthenticationSuccess();

   void reportAuthorizationSuccess();

   void reportConfidentialitySuccess();

   void reportIntegritySuccess();

   void reportPolicyFault();

   void reportGeneralFault();
}
