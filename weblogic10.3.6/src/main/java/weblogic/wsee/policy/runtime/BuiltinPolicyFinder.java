package weblogic.wsee.policy.runtime;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.policy.framework.PolicyStatement;
import weblogic.wsee.policy.runtime.schema.BuiltinPolicyList;
import weblogic.wsee.policy.runtime.schema.BuiltinPolicyListFactory;
import weblogic.wsee.util.Verbose;

public class BuiltinPolicyFinder extends PolicyFinder {
   private static final boolean verbose = Verbose.isVerbose(BuiltinPolicyFinder.class);
   private static final boolean debug = false;
   public static final String WLS92_POLICY = "WLS 9.2 Policy";
   public static final String WSSP12_POLICY = "WSSP12 Policy 2005/12";
   public static final String WSSP12_POLICY_2007 = "WSSP12 Policy 2007/02";
   public static final String RM_POLICY = "WS_RM";
   public static final String WSRM_POLICY = "WS_RM";
   public static final String SAML_POLICY = "Saml";
   public static final String MTOM_POLICY = "Mtom";
   public static final String MC_POLICY = "Make Connection";
   public static final String WSSC_POLICY = "WS-SecureConversation";
   public static final String WSSC13_POLICY = "WS-SecureConversation 1.3";
   private static final String[] CANNED_POLICY_TYPES = new String[]{"WSSP12 Policy 2007/02", "WLS 9.2 Policy", "WSSP12 Policy 2005/12", "WS_RM", "Saml", "Mtom", "Make Connection", "WS-SecureConversation", "WS-SecureConversation 1.3"};
   private static BuiltinPolicyFinder singleton = new BuiltinPolicyFinder();
   private static final String DEFAULT_RELIABILITY_XML = "DefaultReliability.xml";
   private static final String LONG_RUNNING_RELIABILITY_XML = "LongRunningReliability.xml";
   public static final boolean IS_FARALLON = false;

   private BuiltinPolicyFinder() {
   }

   public static PolicyFinder getInstance() {
      return singleton;
   }

   public PolicyStatement findPolicy(String var1, String var2) throws PolicyException {
      PolicyStatement var3 = null;
      var1 = checkFileExtension(var1);
      BuiltinPolicyList var4 = BuiltinPolicyListFactory.getBuiltinPolicyList();
      if (!var4.containsPolicy(var1)) {
         if (verbose) {
            Verbose.log((Object)("Unable to find a built-in policy with name = " + var1));
         }

         return null;
      } else {
         String var5 = '/' + this.getClass().getPackage().getName().replace('.', '/') + '/' + var1;
         if (verbose) {
            Verbose.log((Object)("Looking for a built-in policy with name = " + var5));
         }

         InputStream var6 = this.getClass().getResourceAsStream(var5);
         if (var6 != null) {
            var3 = readPolicyFromStream(var1, var6);
         }

         return var3;
      }
   }

   public static String[] getAllCannedPolicyNames() {
      BuiltinPolicyList var0 = BuiltinPolicyListFactory.getBuiltinPolicyList();
      return var0.getNonInternalCannedPolicyNames();
   }

   public static String[] getAllNon92CannedPolicyNames() {
      BuiltinPolicyList var0 = BuiltinPolicyListFactory.getBuiltinPolicyList();
      return var0.getAllNon92CannedPolicyNames();
   }

   public static boolean is92SecurityPolicy(String var0) {
      if (!"DefaultReliability.xml".equals(var0) && !"LongRunningReliability.xml".equals(var0)) {
         BuiltinPolicyList var1 = BuiltinPolicyListFactory.getBuiltinPolicyList();
         String[] var2 = var1.get92CannedPolicyNames();

         for(int var3 = 0; var3 < var2.length; ++var3) {
            if (var2[var3].equals(var0)) {
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   public static boolean isWsrmPolicy(String var0) {
      BuiltinPolicyList var1 = BuiltinPolicyListFactory.getBuiltinPolicyList();
      String[] var2 = var1.getWsrmCannedPolicyNames();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         if (var2[var3].equals(var0)) {
            return true;
         }
      }

      return false;
   }

   /** @deprecated */
   public static String[] getCannedPolicyNames() {
      BuiltinPolicyList var0 = BuiltinPolicyListFactory.getBuiltinPolicyList();
      return var0.get92CannedPolicyNames();
   }

   public static String[] getCannedPolicyTypes() {
      return CANNED_POLICY_TYPES;
   }

   public static String[] getCannedPolicyNames(String var0) {
      if (null == var0) {
         BuiltinPolicyList var1 = BuiltinPolicyListFactory.getBuiltinPolicyList();
         return var1.getAllPolices();
      } else if ("WSSP12 Policy 2005/12".equals(var0)) {
         return getWssp12CannedPolicyNames();
      } else if ("WLS 9.2 Policy".equals(var0)) {
         return get92CannedPolicyNames();
      } else if ("Saml".equals(var0)) {
         return getSamlCannedPolicyNames();
      } else if ("Mtom".equals(var0)) {
         return getMtomCannedPolicyNames();
      } else if ("Make Connection".equals(var0)) {
         return getMcCannedPolicyNames();
      } else if ("WS-SecureConversation".equals(var0)) {
         return getWsscCannedPolicyNames();
      } else if ("WS-SecureConversation 1.3".equals(var0)) {
         return getWssc13CannedPolicyNames();
      } else {
         return "WS_RM".equals(var0) ? getWsrmCannedPolicyNames() : getCannedPolicyLike(var0);
      }
   }

   public static String[] get92CannedPolicyNames() {
      BuiltinPolicyList var0 = BuiltinPolicyListFactory.getBuiltinPolicyList();
      return var0.get92CannedPolicyNames();
   }

   public static String[] getWssp12CannedPolicyNames() {
      BuiltinPolicyList var0 = BuiltinPolicyListFactory.getBuiltinPolicyList();
      return var0.getWssp12CannedPolicyNames();
   }

   public static String[] getWssp12_2007_CannedPolicyNames() {
      BuiltinPolicyList var0 = BuiltinPolicyListFactory.getBuiltinPolicyList();
      return var0.getWssp12_2007_CannedPolicyNames();
   }

   public static String[] getSamlCannedPolicyNames() {
      BuiltinPolicyList var0 = BuiltinPolicyListFactory.getBuiltinPolicyList();
      return var0.getSamlCannedPolicyNames();
   }

   public static String[] getMcCannedPolicyNames() {
      BuiltinPolicyList var0 = BuiltinPolicyListFactory.getBuiltinPolicyList();
      return var0.getMcCannedPolicyNames();
   }

   public static String[] getMtomCannedPolicyNames() {
      BuiltinPolicyList var0 = BuiltinPolicyListFactory.getBuiltinPolicyList();
      return var0.getMtomCannedPolicyNames();
   }

   public static String[] getProtectionCannedPolicyNames() {
      BuiltinPolicyList var0 = BuiltinPolicyListFactory.getBuiltinPolicyList();
      return var0.getProtectionCannedPolicyNames();
   }

   public static String[] getWsscCannedPolicyNames() {
      BuiltinPolicyList var0 = BuiltinPolicyListFactory.getBuiltinPolicyList();
      return var0.getWsscCannedPolicyNames();
   }

   public static String[] getWssc13CannedPolicyNames() {
      BuiltinPolicyList var0 = BuiltinPolicyListFactory.getBuiltinPolicyList();
      return var0.getWssc13CannedPolicyNames();
   }

   public static String[] getWssc14CannedPolicyNames() {
      BuiltinPolicyList var0 = BuiltinPolicyListFactory.getBuiltinPolicyList();
      return var0.getWssc14CannedPolicyNames();
   }

   public static String[] getWsrmCannedPolicyNames() {
      BuiltinPolicyList var0 = BuiltinPolicyListFactory.getBuiltinPolicyList();
      return var0.getWsrmCannedPolicyNames();
   }

   public static String[] getWsp15CannedPolicyNames() {
      BuiltinPolicyList var0 = BuiltinPolicyListFactory.getBuiltinPolicyList();
      return var0.getWsp15CannedPolicyNames();
   }

   public static String[] getWss10CannedPolicyNames() {
      BuiltinPolicyList var0 = BuiltinPolicyListFactory.getBuiltinPolicyList();
      return var0.getWss10CannedPolicyNames();
   }

   public static String[] getWss11CannedPolicyNames() {
      BuiltinPolicyList var0 = BuiltinPolicyListFactory.getBuiltinPolicyList();
      return var0.getWss11CannedPolicyNames();
   }

   public static String[] getTransportCannedPolicyNames() {
      BuiltinPolicyList var0 = BuiltinPolicyListFactory.getBuiltinPolicyList();
      return var0.getTransportCannedPolicyNames();
   }

   public static String[] getCannedPolicyLike(String var0) {
      BuiltinPolicyList var1 = BuiltinPolicyListFactory.getBuiltinPolicyList();
      return null != var0 && var0.length() != 0 ? getCannedPolicyLike(var1.getAllPolices(), var0) : var1.getAllPolices();
   }

   private static String[] getCannedPolicyLike(String[] var0, String var1) {
      var1 = var1.toLowerCase(Locale.ENGLISH);
      ArrayList var2 = new ArrayList();

      for(int var3 = 0; var3 < var0.length; ++var3) {
         if (-1 != var0[var3].toLowerCase(Locale.ENGLISH).indexOf(var1)) {
            var2.add(var0[var3]);
         }
      }

      if (var2.size() == 0) {
         return null;
      } else {
         String[] var6 = new String[var2.size()];
         Iterator var4 = var2.iterator();

         for(int var5 = 0; var4.hasNext(); var6[var5++] = (String)var4.next()) {
         }

         return var6;
      }
   }
}
