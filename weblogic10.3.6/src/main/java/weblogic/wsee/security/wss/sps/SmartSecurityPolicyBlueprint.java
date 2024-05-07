package weblogic.wsee.security.wss.sps;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import weblogic.wsee.policy.framework.PolicyAlternative;
import weblogic.wsee.policy.util.PolicySelectionPreference;
import weblogic.wsee.security.policy.SecurityToken;
import weblogic.wsee.security.wss.plan.SecurityPolicyBlueprint;
import weblogic.wsee.security.wss.plan.SecurityPolicyOutline;
import weblogic.wsee.security.wss.plan.SecurityPolicyPlan;
import weblogic.wsee.security.wss.plan.helper.TokenReferenceTypeHelper;
import weblogic.wsee.util.Verbose;
import weblogic.xml.crypto.wss11.internal.STRType;
import weblogic.xml.crypto.wss11.internal.WSS11Context;

public class SmartSecurityPolicyBlueprint {
   private static final boolean verbose = Verbose.isVerbose(SmartSecurityPolicyBlueprint.class);
   private static final boolean debug = true;
   private SecurityPolicyPlan blueprint;
   private static final int SC_TRANS_SECURITY = 100;
   private static final int SC_USER_NAME_TOKEN = 200;
   private long outlook;
   private long outlookMask;
   private long optionalOutlookMask;
   private boolean isFromActualMessage = false;
   private List tokenList;
   private Map signatureMap;
   private Map encryptionMap;
   public static final long SIGNATURE = 1L;
   public static final long ENCRYPTION = 2L;
   public static final long SIGNATURE_CONFIRMATION = 4L;
   public static final long ENCRYTED_KEY = 8L;
   public static final long REQUIRED_ELEMENT = 16L;
   public static final long ENDORSE_TOKEN = 32L;
   public static final long AUTHENTICATION = 64L;
   public static final long USERNAME_TOKEN = 192L;
   public static final long SAML_TOKEN = 256L;
   public static final long X509_TOKEN_AUTH = 576L;
   public static final long TIME_STAMP = 1024L;
   public static final long BODY_SIGNATURE = 2049L;
   public static final long BODY_ENCRYPTION = 4098L;
   public static final long THUMBPRINT = 8192L;
   public static final long ENCRYPTED_HEADER = 16386L;
   public static final long ENCRYPTED_USERNAME_TOKEN = 32962L;
   public static final long ENCRYPTED_SIGNATURE = 65539L;
   public static final long SAML11_TOKEN = 131072L;
   public static final long SAML20_TOKEN = 262144L;
   public static final long KERBEROS_TOKEN = 524288L;
   public static final long ELEMENT_SIGN_ENCRPT = 1048576L;
   public static final long WSS11 = 2097152L;
   public static final long OPTIONAL = 4194304L;
   public static final long BODY_SIGNATURE_OPTIONAL = 4196353L;
   public static final long BODY_ENCRYPTION_OPTIONAL = 4198402L;
   public static final long SECURE_CONVERSATION = 8388608L;
   public static final long WSSC13 = 16777216L;
   public static final long TRUST13 = 33554432L;
   public static final long ISSUED_TOKEN = 67108864L;
   private static final int[][] POSITIVE_SCORING = new int[][]{{3, 1, 0}, {3, 1, 0}, {2, 0, 0}, {4, 0, 2}, {4, 0, 0}, {2, 0, 0}, {5, 0, 0}, {3, 2, 1}, {4, 0, 0}, {2, 1, 2}, {1, 4, 0}, {3, 1, 0}, {3, 1, 0}, {1, 2, 0}, {4, 0, 1}, {1, 0, 0}, {1, 0, 0}, {1, 4, 2}, {2, 3, 3}, {1, 0, 0}, {4, 1, 4}, {2, 0, 0}, {0, 0, 5}, {4, 1, 4}, {5, 0, 5}, {5, 0, 4}};
   private static final int[][] NEGATIVE_SCORING = new int[][]{{0, 2, 5}, {0, 2, 5}, {0, 4, 3}, {0, 1, 0}, {0, 3, 1}, {0, 3, 3}, {0, 3, 3}, {0, 0, 1}, {0, 3, 3}, {0, 0, 1}, {0, 0, 1}, {0, 1, 2}, {0, 0, 3}, {0, 0, 0}, {0, 1, 1}, {0, 1, 2}, {0, 1, 2}, {0, 2, 2}, {0, 3, 2}, {0, 5, 2}, {0, 3, 1}, {0, 4, 3}, {2, 1, 0}, {0, 5, 1}, {0, 5, 0}, {0, 5, 0}};
   private static final int[] MISSING_CODE = new int[]{3001, 4001, 6301, 4701, 2501, 7001, 1001, 1021, 1061, 1041, 6101, 3201, 4201, 2741, 4301, 4621, 4331, 2661, 2761, 1071, 3501, 6401};
   private static final int[] NOT_REQUIRED_CODE = new int[]{3008, 4008, 6308, 4702, 3508, 7008, 1008, 1028, 1068, 1048, 6108, 3208, 4208, 2748, 4308, 4028, 4338, 2668, 2768, 1078, 4508, 6408};
   private static final String[] FEATURES = new String[]{"Signature", "Encryption", "Signature confirmation", "Encryted key", "Required element", "Endorse token", "Authentication", "Username token", "Saml token SV", "X509 token auth", "Time stamp", "Body signature", "Body encryption", "Thumbprint", "Encrypted header", "Encrypted username token", "Encrypted signature", "Saml11 token", "Saml20 token", "Kerberos token", "Element sign encrpt", "Wss11", "Optional", "Secure conversation", "Wssc13", "Trust13"};
   private boolean usernameTokenAuth = false;
   private boolean x509TokenAuth = false;
   private boolean kerberosAuth = false;
   private boolean saml11TokenAuth = false;
   private boolean saml20TokenAuth = false;
   private boolean spnegoToken = false;
   private boolean relToken = false;
   private boolean issuedToken = false;
   WSS11Context securityContext = null;
   private int policyLocationIdx = 0;

   public SmartSecurityPolicyBlueprint(SecurityPolicyBlueprint var1, int var2, PolicyAlternative var3, WSS11Context var4) {
      this.blueprint = var1;
      this.policyLocationIdx = var2;
      this.isFromActualMessage = false;
      this.securityContext = var4;
      this.setPolicyAlternative(var3);
      this.init();
   }

   public SmartSecurityPolicyBlueprint(SecurityPolicyOutline var1) {
      this.blueprint = var1;
      this.isFromActualMessage = true;
      this.init();
   }

   public PolicyAlternative getPolicyAlternative() {
      return this.blueprint.getPolicyAlternative();
   }

   public void setPolicyAlternative(PolicyAlternative var1) {
      this.blueprint.setPolicyAlternative(var1);
   }

   public int getPolicyLocationIdx() {
      return this.policyLocationIdx;
   }

   public long getMessageOutlook() {
      return this.outlook;
   }

   public long getActualPolicyOutlook() {
      return this.outlook & ~this.outlookMask;
   }

   public long getPolicyOutlookWithMask(long var1) {
      return this.outlook & ~this.outlookMask & ~var1;
   }

   public long getMessageOutlookMask() {
      return this.outlookMask;
   }

   public boolean hasUnsupportedFeature(long var1) {
      return (this.outlook & var1) != 0L;
   }

   private void addOutlook(long var1) {
      if (this.blueprint.hasTransportSecuirity() && !this.isFromActualMessage) {
         if (var1 != 1L && var1 != 2L && var1 != 2049L && var1 != 4098L && var1 != 16386L && var1 != 32962L && var1 != 65539L && var1 != 1048576L && var1 != 4196353L && var1 != 4198402L) {
            if (var1 == 32L) {
               var1 = 1L;
            }
         } else {
            var1 = 0L;
         }
      }

      this.outlook |= var1;
   }

   private void addPolicyOutlookMask(long var1) {
      this.outlookMask |= var1;
   }

   private void init() {
      this.outlook = 0L;
      this.tokenList = new ArrayList();
      if (this.isAuthenticationRequired()) {
         this.tokenList.addAll(this.blueprint.getIdentityPolicy().getValidIdentityTokens());
         this.addOutlook(64L);
         this.setAuthToken();
      }

      if (this.isEncryptionRequired()) {
         this.tokenList.addAll(this.blueprint.getEncryptionPolicy().getValidEncryptionTokens());
         this.encryptionMap = this.blueprint.getEncryptionPolicy().getNodeMap();
         this.setSamlToken(this.blueprint.getEncryptionPolicy().getValidEncryptionTokens());
         if (!this.isFromActualMessage) {
            this.addOutlook(2L);
            if (this.isElementEncryption()) {
               this.addOutlook(1048576L);
               this.addPolicyOutlookMask(1048576L);
               if (this.getActualSize(this.encryptionMap) == 1) {
                  this.addPolicyOutlookMask(2L);
               }
            }
         } else if (this.blueprint.isBodyEmpty()) {
            this.addPolicyOutlookMask(4098L);
         } else {
            this.addOutlook(2L);
         }
      } else if (this.isFromActualMessage && this.blueprint.isBodyEmpty()) {
         this.addPolicyOutlookMask(4098L);
         this.addPolicyOutlookMask(2L);
         this.addPolicyOutlookMask(2097152L);
      }

      if (this.isSignatureRequired()) {
         if (!this.isSignatureEncryptionRequired()) {
            this.tokenList.addAll(this.blueprint.getSigningPolicy().getValidSignatureTokens());
            this.addOutlook(1L);
            this.signatureMap = this.blueprint.getSigningPolicy().getSigningNodeMap();
         }

         this.setSamlToken(this.blueprint.getSigningPolicy().getValidSignatureTokens());
      }

      if (this.isEndorsingRequired()) {
         this.tokenList.addAll(this.blueprint.getEndorsingPolicy().getValidSignatureTokens());
         this.addOutlook(32L);
      }

      if (this.isWss11()) {
         this.addOutlook(2097152L);
         if (!this.isFromActualMessage) {
            this.addPolicyOutlookMask(2097152L);
         }
      }

      if (this.isSignatureConfirmationRequired()) {
         this.addOutlook(4L);
      }

      if (this.isTimestampRequired()) {
         this.addOutlook(1024L);
      }

      if (this.isBodyEncrypted()) {
         this.addOutlook(4098L);
      }

      if (this.isBodySigned()) {
         this.addOutlook(2049L);
      }

      if (this.isSignatureEncryptionRequired()) {
         this.addOutlook(65539L);
         if (!this.isFromActualMessage) {
            this.addPolicyOutlookMask(65539L);
            this.addPolicyOutlookMask(1L);
            if (this.isSignatureConfirmationRequired()) {
               this.addPolicyOutlookMask(4L);
            }

            if (this.isWss11()) {
               this.addPolicyOutlookMask(2097152L);
            }

            Map var1 = this.blueprint.getSigningPolicy().getSigningNodeMap();
            if (var1.containsKey("Body")) {
               this.addPolicyOutlookMask(2049L);
            }

            if (this.isElementSignature()) {
               this.addOutlook(1048576L);
               this.addPolicyOutlookMask(1048576L);
               if (this.getActualSize(var1) == 1) {
                  this.addPolicyOutlookMask(1L);
               }
            }

            if (this.isEndorsedTokenRequired()) {
               this.addPolicyOutlookMask(32L);
            }
         }
      }

      if (!this.isFromActualMessage && this.isUsernameTokenEncryptionRequired()) {
         this.addPolicyOutlookMask(192L);
      }

      if (!this.isFromActualMessage && this.isX509TokenAuth() && this.isSignatureRequired()) {
         this.addPolicyOutlookMask(576L);
      }

      if (this.isSecureConversation()) {
         this.addOutlook(8388608L);
         this.addPolicyOutlookMask(8388608L);
      }

      if (this.isWssc13()) {
         this.addOutlook(16777216L);
         this.addPolicyOutlookMask(16777216L);
      }

      if (!this.isFromActualMessage && this.isSamlTokenEncryptionRequired()) {
         this.addPolicyOutlookMask(256L);
         this.addPolicyOutlookMask(64L);
         if (this.isSaml11TokenAuth()) {
            this.addPolicyOutlookMask(131072L);
         }

         if (this.isSaml20TokenAuth()) {
            this.addPolicyOutlookMask(262144L);
         }

         if (this.isWss11()) {
            this.addPolicyOutlookMask(2097152L);
         }
      }

   }

   private int getActualSize(Map var1) {
      ArrayList var2 = new ArrayList(var1.values());
      var2.remove((Object)null);
      return var2.size();
   }

   public int getErrorCode(long var1) {
      if (this.getActualPolicyOutlook() == var1) {
         return 0;
      } else {
         long var3 = this.getActualPolicyOutlook() & ~var1;
         if (var3 != 0L) {
            return getMissingRc(var3);
         } else {
            long var5 = ~this.outlook & var1;
            return var5 != 0L ? getNotRequiredRc(var5) : 9999;
         }
      }
   }

   public String tellMeWhy(long var1) {
      if (this.getActualPolicyOutlook() == var1) {
         return "0";
      } else {
         StringBuffer var3 = new StringBuffer(" Error codes:");
         long var4 = this.getActualPolicyOutlook() & ~var1;
         if (var4 != 0L) {
            StringBuffer var6 = findMissingCodes(var4);
            if (verbose) {
               Verbose.log((Object)("Incoming message missing code =" + var6));
            }

            var3.append(var6);
         }

         long var9 = ~this.getActualPolicyOutlook() & var1;
         if (var9 != 0L) {
            StringBuffer var8 = findNotRequiredCodes(var9);
            if (verbose) {
               Verbose.log((Object)("Incoming message error code =" + var8));
            }

            var3.append(var8);
         }

         return var3.toString();
      }
   }

   public String tellMeWhyNotSupported(long var1) {
      StringBuffer var3 = new StringBuffer("Unsupported codes:");
      long var4 = this.outlook & var1;
      if (var4 != 0L) {
         StringBuffer var6 = findNotRequiredCodes(var4);
         if (verbose) {
            Verbose.log((Object)("Incoming message error code =" + var6));
         }

         var3.append(var6);
      }

      return var3.toString();
   }

   private static int getMissingRc(long var0) {
      for(int var2 = 0; var2 < MISSING_CODE.length; ++var2) {
         if ((var0 & 1L) == 1L) {
            return MISSING_CODE[var2];
         }

         var0 >>= 1;
      }

      return 9999;
   }

   private static int getNotRequiredRc(long var0) {
      for(int var2 = 0; var2 < NOT_REQUIRED_CODE.length; ++var2) {
         if ((var0 & 1L) == 1L) {
            return NOT_REQUIRED_CODE[var2];
         }

         var0 >>= 1;
      }

      return 9999;
   }

   private static StringBuffer findMissingCodes(long var0) {
      StringBuffer var2 = new StringBuffer();

      for(int var3 = 0; var3 < MISSING_CODE.length; ++var3) {
         if ((var0 & 1L) == 1L) {
            var2.append(" " + MISSING_CODE[var3]);
         }

         var0 >>= 1;
      }

      return var2;
   }

   private static StringBuffer findNotRequiredCodes(long var0) {
      StringBuffer var2 = new StringBuffer();

      for(int var3 = 0; var3 < NOT_REQUIRED_CODE.length; ++var3) {
         if ((var0 & 1L) == 1L) {
            var2.append(" " + NOT_REQUIRED_CODE[var3]);
         }

         var0 >>= 1;
      }

      return var2;
   }

   public int getScore(PolicySelectionPreference var1) {
      if (null != var1 && !var1.isDefaut()) {
         int[] var2 = this.securityInteropPerformanceScoring();
         return var1.calculateScore(var2);
      } else {
         return 1;
      }
   }

   public SecurityPolicyBlueprint getSecurityPolicyBlueprint() {
      if (this.blueprint instanceof SecurityPolicyBlueprint) {
         return (SecurityPolicyBlueprint)this.blueprint;
      } else {
         throw new UnsupportedOperationException("Unable to get blueprint from outline");
      }
   }

   public boolean hasDescribed() {
      if (this.blueprint != null && this.blueprint.getGeneralPolicy() != null) {
         return this.blueprint.getBuildingPlan() != 0;
      } else {
         return false;
      }
   }

   public boolean isSignatureRequired() {
      return this.blueprint.getSigningPolicy().isSignatureRequired();
   }

   public boolean isEncryptionRequired() {
      return this.blueprint.getEncryptionPolicy().isEncryptionRequired();
   }

   public boolean isAuthenticationRequired() {
      return this.blueprint.getIdentityPolicy().isAuthenticationRequired();
   }

   private void setAuthToken() {
      List var1 = this.blueprint.getIdentityPolicy().getValidIdentityTokens();

      for(int var2 = 0; var2 < var1.size(); ++var2) {
         SecurityToken var3 = (SecurityToken)var1.get(var2);
         String var4 = var3.getTokenTypeUri();
         if ("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#UsernameToken".equals(var4)) {
            this.usernameTokenAuth = true;
            this.addOutlook(192L);
         } else if (!"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v3".equals(var4) && !"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v1".equals(var4) && !"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#PKCS7".equals(var4) && !"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509PKIPathv1".equals(var4)) {
            if (!"http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.0#SAMLAssertionID".equals(var4) && !"http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.1#SAMLID".equals(var4) && !"http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.1#SAMLV1.1".equals(var4)) {
               if ("http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.1#SAMLV2.0".equals(var4)) {
                  this.saml20TokenAuth = true;
                  this.addOutlook(256L);
                  this.addOutlook(262144L);
               } else if ("http://docs.oasis-open.org/wss/oasis-wss-kerberos-token-profile-1.1".equals(var4)) {
                  this.kerberosAuth = true;
                  this.addOutlook(524288L);
               }
            } else {
               this.saml11TokenAuth = true;
               this.addOutlook(256L);
               this.addOutlook(131072L);
            }
         } else {
            this.x509TokenAuth = true;
            this.addOutlook(576L);
         }
      }

   }

   private void setSamlToken(List var1) {
      for(int var2 = 0; var2 < var1.size(); ++var2) {
         SecurityToken var3 = (SecurityToken)var1.get(var2);
         String var4 = var3.getTokenTypeUri();
         if (!"http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.0#SAMLAssertionID".equals(var4) && !"http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.1#SAMLID".equals(var4) && !"http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.1#SAMLV1.1".equals(var4)) {
            if ("http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.1#SAMLV2.0".equals(var4)) {
               this.saml20TokenAuth = true;
               this.addOutlook(262144L);
            }
         } else {
            this.saml11TokenAuth = true;
            this.addOutlook(131072L);
         }
      }

   }

   public boolean isSaml11TokenAuth() {
      return this.saml11TokenAuth;
   }

   public boolean isSaml20TokenAuth() {
      return this.saml20TokenAuth;
   }

   public boolean isSamlTokenAuth() {
      return this.saml11TokenAuth || this.saml20TokenAuth;
   }

   public boolean isIssuedToken() {
      return this.issuedToken;
   }

   public boolean isRelToken() {
      return this.relToken;
   }

   public boolean isSpnegoToken() {
      return this.spnegoToken;
   }

   public boolean isKerberosAuth() {
      return this.kerberosAuth;
   }

   public boolean isUsernameTokenAuth() {
      return this.usernameTokenAuth;
   }

   public boolean isX509TokenAuth() {
      return this.x509TokenAuth;
   }

   public boolean isEndorsingRequired() {
      return this.blueprint.getEndorsingPolicy().isSignatureRequired();
   }

   public boolean isSignatureConfirmationRequired() {
      return this.blueprint.getGeneralPolicy().isRequireSignatureConfirmation();
   }

   public boolean isTimestampRequired() {
      return this.blueprint.getTimestampPolicy().isSignTimestampRequired();
   }

   public boolean isEndorsedTokenRequired() {
      return this.blueprint.getEndorsingPolicy().isSignatureRequired();
   }

   public boolean isBodySigned() {
      return this.isSignatureRequired() && null != this.signatureMap ? this.signatureMap.containsKey("Body") : false;
   }

   public boolean isBodyEncrypted() {
      return this.isEncryptionRequired() && null != this.encryptionMap ? this.encryptionMap.containsKey("Body") : false;
   }

   public boolean isElementEncryption() {
      return this.isEncryptionRequired() && null != this.encryptionMap ? this.encryptionMap.containsKey("Element") : false;
   }

   public boolean isWssc13() {
      return this.blueprint.getGeneralPolicy().isWssc13();
   }

   public boolean isElementSignature() {
      return this.isSignatureRequired() && null != this.signatureMap ? this.signatureMap.containsKey("Element") : false;
   }

   public String[] getTokenTypes() {
      if (this.tokenList != null && this.tokenList.size() != 0) {
         ArrayList var1 = new ArrayList();

         for(int var2 = 0; var2 < this.tokenList.size(); ++var2) {
            SecurityToken var3 = (SecurityToken)this.tokenList.get(var2);
            if (!var3.isOptional()) {
               var1.add(var3.getTokenTypeUri());
            }
         }

         if (var1.size() == 0) {
            return null;
         } else {
            String[] var4 = new String[var1.size()];

            for(int var5 = 0; var5 < var1.size(); ++var5) {
               var4[var5] = (String)var1.get(var5);
            }

            return var4;
         }
      } else {
         return null;
      }
   }

   protected boolean isThumbprintRequired() {
      if (this.tokenList != null && this.tokenList.size() != 0) {
         for(int var1 = 0; var1 < this.tokenList.size(); ++var1) {
            SecurityToken var2 = (SecurityToken)this.tokenList.get(var1);
            List var3 = var2.getStrTypes();
            if (null != var3 && !var3.isEmpty()) {
               Iterator var4 = var3.iterator();

               while(var4.hasNext()) {
                  STRType var5 = (STRType)var4.next();
                  if (TokenReferenceTypeHelper.isThrumbprintSTRType(var5)) {
                     return true;
                  }
               }
            }
         }

         return false;
      } else {
         return false;
      }
   }

   protected boolean isUsernameTokenEncryptionRequired() {
      return this.isEncryptionRequired() && null != this.encryptionMap ? this.encryptionMap.containsKey("UserNameToken") : false;
   }

   protected boolean isSamlTokenEncryptionRequired() {
      return this.isEncryptionRequired() && null != this.encryptionMap ? this.encryptionMap.containsKey("SamlToken") : false;
   }

   protected boolean isSignatureEncryptionRequired() {
      return this.isEncryptionRequired() && null != this.encryptionMap ? this.encryptionMap.containsKey("EncryptSignature") : false;
   }

   public boolean isCertRequired() {
      if (this.tokenList != null && this.tokenList.size() != 0) {
         for(int var1 = 0; var1 < this.tokenList.size(); ++var1) {
            SecurityToken var2 = (SecurityToken)this.tokenList.get(var1);
            String var3 = var2.getTokenTypeUri();
            if ("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v3".equals(var3) || "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v1".equals(var3) || "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509PKIPathv1".equals(var3) || "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#PKCS7".equals(var3)) {
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   public boolean isSecureConversation() {
      if (this.tokenList != null && this.tokenList.size() != 0) {
         for(int var1 = 0; var1 < this.tokenList.size(); ++var1) {
            SecurityToken var2 = (SecurityToken)this.tokenList.get(var1);
            String var3 = var2.getTokenTypeUri();
            if ("http://schemas.xmlsoap.org/ws/2005/02/sc/dk".equals(var3) || "http://docs.oasis-open.org/ws-sx/ws-secureconversation/200512/dk".equals(var3)) {
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   public boolean isWss11() {
      return this.blueprint.getGeneralPolicy().isWss11();
   }

   private int[] securityInteropPerformanceScoring() {
      StringBuffer var1 = new StringBuffer();
      int[] var2 = new int[3];

      for(int var3 = 0; var3 < var2.length; ++var3) {
         var2[var3] = 0;
      }

      long var7 = this.outlook;

      for(int var5 = 0; var5 < POSITIVE_SCORING.length; ++var5) {
         int var6;
         if ((var7 & 1L) != 1L) {
            for(var6 = 0; var6 < var2.length; ++var6) {
               var2[var6] += NEGATIVE_SCORING[var5][var6];
            }
         } else {
            for(var6 = 0; var6 < var2.length; ++var6) {
               var2[var6] += POSITIVE_SCORING[var5][var6];
            }

            if (!verbose) {
            }

            var1.append(FEATURES[var5] + " ");
         }

         var7 >>= 1;
      }

      if (verbose) {
         Verbose.say(var1.toString() + " are considered for scoring");
      }

      System.out.println("Policy Key =" + this.outlook + " Counted --" + var1.toString());
      return var2;
   }

   private void checkTransportSecurity() {
   }

   private void checkAuthentication() {
   }

   public String getPolicyScoringKey() {
      StringBuffer var1 = new StringBuffer();
      var1.append("Policy Key =" + this.outlook + " --");
      long var2 = this.outlook;

      for(int var4 = 0; var4 < POSITIVE_SCORING.length; ++var4) {
         if ((var2 & 1L) == 1L) {
            var1.append(FEATURES[var4] + " ");
         }

         var2 >>= 1;
      }

      return var1.toString();
   }
}
