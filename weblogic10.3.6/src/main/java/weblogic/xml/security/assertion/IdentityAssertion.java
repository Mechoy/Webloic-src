package weblogic.xml.security.assertion;

import java.security.cert.X509Certificate;
import weblogic.xml.security.SecurityAssertion;
import weblogic.xml.security.UserInfo;

/** @deprecated */
public class IdentityAssertion implements SecurityAssertion {
   private static final String type = "http://www.bea.com/servers/xml/security/assertion/Identity";
   private final UserInfo ui;
   private final X509Certificate cert;
   private final int proof;
   private final boolean repudiable;
   public static final int NO_PROOF = 0;
   public static final String STRING_NO_PROOF = "No Proof";
   public static final int POSSESSION_PROOF = 1;
   public static final String STRING_POSSESSION_PROOF = "Possession Proof";
   public static final int SHARED_SECRET_PROOF = 2;
   public static final String STRING_SHARED_SECRET_PROOF = "Shared Secret Proof";

   public IdentityAssertion(UserInfo var1) {
      this.ui = var1;
      this.cert = null;
      if (this.ui.getPassword() != null) {
         this.proof = 2;
      } else if (this.ui.getPasswordDigest() != null) {
         this.proof = 1;
      } else {
         this.proof = 0;
      }

      this.repudiable = true;
   }

   public IdentityAssertion(X509Certificate var1, boolean var2) {
      this.ui = null;
      this.cert = var1;
      this.proof = var2 ? 1 : 0;
      this.repudiable = var2;
   }

   public String getAssertionType() {
      return "http://www.bea.com/servers/xml/security/assertion/Identity";
   }

   public int getAssertionTypeCode() {
      return 0;
   }

   public boolean isAssertionType(String var1) {
      return var1 == "http://www.bea.com/servers/xml/security/assertion/Identity" || "http://www.bea.com/servers/xml/security/assertion/Identity".equals(var1);
   }

   public boolean repudiable() {
      return this.repudiable;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("(").append("IdentityAssertion");
      if (this.ui != null) {
         var1.append(" ").append(this.ui.getUsername());
      } else if (this.cert != null) {
         var1.append(" \"").append(this.cert.getSubjectDN()).append("\"");
      }

      var1.append("\n    :proof \"").append(this.proofAsString()).append("\"");
      var1.append("\n    :repudiable \"").append(this.repudiable).append("\")");
      return var1.toString();
   }

   public String proofAsString() {
      switch (this.proof) {
         case 0:
            return "No Proof";
         case 1:
            return "Possession Proof";
         case 2:
            return "Shared Secret Proof";
         default:
            return "Unknown";
      }
   }

   public int getProofType() {
      return this.proof;
   }

   public UserInfo getUserInfo() {
      return this.ui;
   }

   public X509Certificate getX509Certificate() {
      return this.cert;
   }
}
