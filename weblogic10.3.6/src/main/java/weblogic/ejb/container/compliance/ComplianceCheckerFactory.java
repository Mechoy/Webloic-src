package weblogic.ejb.container.compliance;

public final class ComplianceCheckerFactory {
   public static ComplianceChecker getComplianceChecker() {
      return new EJBComplianceChecker();
   }
}
