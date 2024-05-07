package weblogic.wsee.policy.util;

import java.io.Serializable;
import javax.xml.rpc.handler.MessageContext;
import weblogic.wsee.util.Verbose;

public class PolicySelectionPreference implements Serializable {
   private static final long serialVersionUID = 0L;
   private static final boolean verbose = Verbose.isVerbose(PolicySelectionPreference.class);
   private static final boolean debug = true;
   public static final String VALIDATION_ONLY = "ValidationOnly";
   public static final String DEFAULT = "NONE";
   private static final String[] VALID_OPTIONS = new String[]{"CPS", "CSP", "PCS", "PSC", "SCP", "SPC", "ValidationOnly", "NONE"};
   private String policySelectionPrefernceString;
   public static final int SECURITY_IDX = 0;
   public static final int INTEROP_IDX = 1;
   public static final int PERFORMANCE_IDX = 2;

   public PolicySelectionPreference() {
      this.policySelectionPrefernceString = "NONE";
   }

   public PolicySelectionPreference(String var1) {
      if (!isValidPreference(var1)) {
         throw new IllegalArgumentException("Input String is invalid for " + var1);
      } else {
         this.policySelectionPrefernceString = var1;
      }
   }

   public boolean isDefaut() {
      return "NONE".equals(this.policySelectionPrefernceString);
   }

   public boolean isSecurityFirst() {
      return this.policySelectionPrefernceString.startsWith("S");
   }

   public boolean isPreformanceFirst() {
      return this.policySelectionPrefernceString.startsWith("P");
   }

   public boolean isInteropFirst() {
      return this.policySelectionPrefernceString.startsWith("C");
   }

   public boolean isCompatibiltyFirst() {
      return this.policySelectionPrefernceString.startsWith("C");
   }

   public String getPolicySelectionPrefernceString() {
      return this.policySelectionPrefernceString;
   }

   public static boolean isValidPreference(String var0) {
      if (null == var0) {
         return false;
      } else {
         for(int var1 = 0; var1 < VALID_OPTIONS.length; ++var1) {
            if (VALID_OPTIONS[var1].equals(var0)) {
               return true;
            }
         }

         return false;
      }
   }

   public static PolicySelectionPreference getPolicySelectionPreference(MessageContext var0) {
      if (null == var0) {
         return new PolicySelectionPreference();
      } else {
         Object var1 = var0.getProperty("weblogic.wsee.policy.selection.preference");
         if (null == var1) {
            return new PolicySelectionPreference();
         } else {
            return var1 instanceof PolicySelectionPreference ? (PolicySelectionPreference)var1 : new PolicySelectionPreference((String)var1);
         }
      }
   }

   private int factor(int var1) {
      switch (var1) {
         case 0:
            return 90000;
         case 1:
            return 300;
         case 2:
            return 1;
         default:
            return 0;
      }
   }

   public int calculateScore(int[] var1) {
      if (this.isDefaut()) {
         return 1;
      } else {
         int var2 = var1[0] * this.factor(this.policySelectionPrefernceString.indexOf("S")) + var1[1] * this.factor(this.policySelectionPrefernceString.indexOf("C")) + var1[2] * this.factor(this.policySelectionPrefernceString.indexOf("P"));
         String var3 = null;
         if (!verbose) {
         }

         var3 = "Preferece =" + this.policySelectionPrefernceString + " sec points=" + var1[0] + " interop points=" + var1[1] + " performance points=" + var1[2] + " total = " + var2;
         if (verbose) {
            Verbose.log((Object)var3);
         }

         System.err.println(var3);
         return var2;
      }
   }

   public PolicySelectionPreference clone() {
      return new PolicySelectionPreference(this.getPolicySelectionPrefernceString());
   }

   public String toString() {
      return this.policySelectionPrefernceString;
   }
}
