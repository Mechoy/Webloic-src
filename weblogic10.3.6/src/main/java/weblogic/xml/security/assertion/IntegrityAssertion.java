package weblogic.xml.security.assertion;

import java.security.cert.X509Certificate;
import javax.security.auth.Subject;
import weblogic.xml.security.SecurityAssertion;

/** @deprecated */
public class IntegrityAssertion implements SecurityAssertion {
   private static final String type = "http://www.bea.com/servers/xml/security/assertion/Integrity";
   private final boolean repudiable;
   private final String signatureMethod;
   private final X509Certificate cert;
   protected final String id;
   private Object serverSubject = null;

   public IntegrityAssertion(String var1, String var2, X509Certificate var3) {
      this.signatureMethod = var1;
      this.id = var2;
      this.cert = var3;
      this.repudiable = var3 == null;
   }

   public String getId() {
      return this.id;
   }

   public String getSignatureMethod() {
      return this.signatureMethod;
   }

   public X509Certificate getCertificate() {
      return this.cert;
   }

   public static String getType() {
      return "http://www.bea.com/servers/xml/security/assertion/Integrity";
   }

   public String getAssertionType() {
      return "http://www.bea.com/servers/xml/security/assertion/Integrity";
   }

   public int getAssertionTypeCode() {
      return 1;
   }

   public boolean isAssertionType(String var1) {
      return var1 == "http://www.bea.com/servers/xml/security/assertion/Integrity" || "http://www.bea.com/servers/xml/security/assertion/Integrity".equals(var1);
   }

   public boolean repudiable() {
      return this.repudiable;
   }

   final Object getSubject() {
      return this.serverSubject;
   }

   final void setSubject(Subject var1) {
      this.serverSubject = var1;
   }

   public String getPolicyString() {
      return "Integrity{id:" + this.id + "}";
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("(").append("IntegrityAssertion");
      var1.append(" :id ").append(this.id);
      var1.append("\n    :signatureMethod \"").append(this.getSignatureMethod()).append("\"");
      var1.append("\n    :repudiable \"").append(this.repudiable()).append("\"");
      if (this.cert != null) {
         var1.append("\n    :certificate \"").append(this.cert.getSubjectDN()).append("\"");
      }

      var1.append(")");
      return var1.toString();
   }
}
