package weblogic.wsee.security.policy;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import weblogic.wsee.policy.framework.NormalizedExpression;
import weblogic.wsee.policy.framework.PolicyAlternative;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.security.policy.assertions.ConfidentialityAssertion;
import weblogic.wsee.security.policy.assertions.IdentityAssertion;
import weblogic.wsee.security.policy.assertions.IntegrityAssertion;
import weblogic.wsee.security.policy.assertions.xbeans.ConfidentialityDocument;
import weblogic.wsee.security.policy.assertions.xbeans.IdentityDocument;
import weblogic.wsee.security.policy.assertions.xbeans.IntegrityDocument;
import weblogic.wsee.security.policy.assertions.xbeans.KeyInfoType;
import weblogic.wsee.security.policy.assertions.xbeans.SecurityTokenType;
import weblogic.wsee.security.policy.assertions.xbeans.SupportedTokensType;
import weblogic.wsee.security.policy12.assertions.AsymmetricBinding;
import weblogic.wsee.security.policy12.assertions.EncryptedElements;
import weblogic.wsee.security.policy12.assertions.EncryptedParts;
import weblogic.wsee.security.policy12.assertions.RequireDerivedKeys;
import weblogic.wsee.security.policy12.assertions.RequiredParts;
import weblogic.wsee.security.policy12.assertions.SignedElements;
import weblogic.wsee.security.policy12.assertions.SignedParts;
import weblogic.wsee.security.policy12.assertions.SupportingTokens;
import weblogic.wsee.security.policy12.assertions.SymmetricBinding;
import weblogic.wsee.security.policy12.assertions.X509Token;
import weblogic.wsee.security.wssp.AsymmetricBindingInfo;
import weblogic.wsee.security.wssp.InitiatorTokenAssertion;
import weblogic.wsee.security.wssp.SamlTokenAssertion;
import weblogic.wsee.security.wssp.SecurityPolicyAssertionInfo;
import weblogic.wsee.security.wssp.SecurityPolicyAssertionInfoFactory;
import weblogic.wsee.security.wssp.SupportingTokensAssertion;
import weblogic.wsee.security.wssp.SymmetricBindingInfo;
import weblogic.wsee.security.wssp.TokenAssertion;
import weblogic.wsee.security.wssp.UsernameTokenAssertion;
import weblogic.wsee.util.Verbose;

public class SecurityPolicyAssertionHelper {
   private static final boolean verbose = Verbose.isVerbose(SecurityPolicyAssertionHelper.class);
   private static final boolean DEBUG = true;

   public static List<String> getAllSupportedTokenTypes(PolicyAlternative var0) {
      ArrayList var1 = new ArrayList();
      if (var0 == null) {
         return var1;
      } else {
         Set var2 = var0.getAssertions(IdentityAssertion.class);
         Iterator var3 = var2.iterator();

         SupportedTokensType var6;
         while(var3.hasNext()) {
            IdentityAssertion var4 = (IdentityAssertion)var3.next();
            IdentityDocument.Identity var5 = var4.getXbean().getIdentity();
            var6 = var5.getSupportedTokens();
            if (var6 != null) {
               addTokenType(var1, var6.getSecurityTokenArray());
            }
         }

         var2 = var0.getAssertions(ConfidentialityAssertion.class);
         var3 = var2.iterator();

         while(var3.hasNext()) {
            ConfidentialityAssertion var7 = (ConfidentialityAssertion)var3.next();
            ConfidentialityDocument.Confidentiality var9 = var7.getXbean().getConfidentiality();
            KeyInfoType var11 = var9.getKeyInfo();
            addTokenType(var1, var11.getSecurityTokenArray());
         }

         var2 = var0.getAssertions(IntegrityAssertion.class);
         var3 = var2.iterator();

         while(var3.hasNext()) {
            IntegrityAssertion var8 = (IntegrityAssertion)var3.next();
            IntegrityDocument.Integrity var10 = var8.getXbean().getIntegrity();
            var6 = var10.getSupportedTokens();
            if (var6 != null) {
               addTokenType(var1, var6.getSecurityTokenArray());
            }
         }

         return var1;
      }
   }

   private static void addTokenType(List<String> var0, SecurityTokenType[] var1) {
      SecurityTokenType[] var2 = var1;
      int var3 = var1.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         SecurityTokenType var5 = var2[var4];
         String var6 = var5.getTokenType();
         if (!var0.contains(var6)) {
            var0.add(var6);
         }

         if (var5.isSetDerivedFromTokenType()) {
            String var7 = var5.getDerivedFromTokenType();
            if (!var0.contains(var7)) {
               var0.add(var7);
            }
         }
      }

   }

   public static void checkLogicalError(NormalizedExpression var0) throws PolicyException {
      if (var0.getPolicyAlternatives() != null) {
         int var1 = 0;

         int var4;
         for(Iterator var2 = var0.getPolicyAlternatives().iterator(); var2.hasNext(); var1 = var4) {
            PolicyAlternative var3 = (PolicyAlternative)var2.next();
            var4 = policyLogicalErrorDetector(var3);
            if (var4 == 0) {
               return;
            }
         }

         if (!verbose) {
         }

         Verbose.say("Policy Error code = " + var1 + " Policy :\n" + var0);
         System.err.println("Policy Error code = " + var1 + " Error Policy :\n" + var0);
         throw new PolicyException("Policy logical error found,  error code =" + var1);
      }
   }

   public static int policyLogicalErrorDetector(PolicyAlternative var0) {
      return policyLogicalErrorDetector(var0, false);
   }

   public static int policyLogicalErrorDetectorRuntime(PolicyAlternative var0) {
      return policyLogicalErrorDetector(var0, true);
   }

   private static int policyLogicalErrorDetector(PolicyAlternative var0, boolean var1) {
      if (null == var0) {
         throw new IllegalArgumentException("Null Policy Alternative");
      } else if (var0.isEmpty()) {
         return 0;
      } else {
         boolean var2 = false;
         int var4;
         if (var1) {
            var4 = checkPartsError(var0);
            if (var4 != 0) {
               return var4;
            }
         }

         SecurityPolicyAssertionInfo var3 = SecurityPolicyAssertionInfoFactory.getSecurityPolicyAssertionInfo(var0);
         if (var3 != null && var3.isMessageSecurityEnabled()) {
            if (var1) {
               var4 = checkTokenInclusion(var3.getAsymmetricBindingInfo());
               if (var4 != 0) {
                  return var4;
               }
            }

            return checkSamlError(var3);
         } else {
            return 0;
         }
      }
   }

   private static int checkTokenInclusion(AsymmetricBindingInfo var0) {
      if (null == var0) {
         return 0;
      } else {
         TokenAssertion.TokenInclusion var1 = null;
         TokenAssertion.TokenInclusion var2 = null;
         if (var0.getRecipientTokenAssertion() != null) {
            if (var0.getRecipientTokenAssertion().getX509TokenAssertion() != null) {
               var2 = var0.getRecipientTokenAssertion().getX509TokenAssertion().getTokenInclusion();
            }
         } else if (var0.getRecipientSignatureTokenAssertion() != null) {
            if (var0.getRecipientSignatureTokenAssertion().getX509TokenAssertion() != null) {
               var2 = var0.getRecipientSignatureTokenAssertion().getX509TokenAssertion().getTokenInclusion();
            }
         } else if (var0.getRecipientEncryptionTokenAssertion() != null && var0.getRecipientEncryptionTokenAssertion().getX509TokenAssertion() != null) {
            var2 = var0.getRecipientEncryptionTokenAssertion().getX509TokenAssertion().getTokenInclusion();
         }

         if (!TokenAssertion.TokenInclusion.TO_INITIATOR_ONLY.equals(var2) && !TokenAssertion.TokenInclusion.TO_RECIPIENT_ONLY.equals(var2)) {
            return 0;
         } else {
            if (var0.getInitiatorTokenAssertion() != null) {
               if (var0.getInitiatorTokenAssertion().getX509TokenAssertion() != null) {
                  var1 = var0.getInitiatorTokenAssertion().getX509TokenAssertion().getTokenInclusion();
               } else if (var0.getInitiatorTokenAssertion().getSamlTokenAssertion() != null) {
                  var1 = var0.getInitiatorTokenAssertion().getSamlTokenAssertion().getTokenInclusion();
               }
            } else if (var0.getInitiatorSignatureTokenAssertion() != null) {
               if (var0.getInitiatorSignatureTokenAssertion().getX509TokenAssertion() != null) {
                  var1 = var0.getInitiatorSignatureTokenAssertion().getX509TokenAssertion().getTokenInclusion();
               } else if (var0.getInitiatorSignatureTokenAssertion().getSamlTokenAssertion() != null) {
                  var1 = var0.getInitiatorSignatureTokenAssertion().getSamlTokenAssertion().getTokenInclusion();
               }
            } else if (var0.getInitiatorEncryptionTokenAssertion() != null) {
               if (var0.getInitiatorEncryptionTokenAssertion().getX509TokenAssertion() != null) {
                  var1 = var0.getInitiatorEncryptionTokenAssertion().getX509TokenAssertion().getTokenInclusion();
               } else if (var0.getInitiatorEncryptionTokenAssertion().getSamlTokenAssertion() != null) {
                  var1 = var0.getInitiatorEncryptionTokenAssertion().getSamlTokenAssertion().getTokenInclusion();
               }
            }

            if (var1.equals(var2)) {
               if (!verbose) {
               }

               Verbose.say("Inititiator Token = " + var1 + " and Reciptient Token = " + var2);
               return 8642;
            } else {
               return 0;
            }
         }
      }
   }

   private static int checkSamlError(SecurityPolicyAssertionInfo var0) {
      boolean var1 = false;
      SupportingTokensAssertion var2 = var0.getSupportingTokensAssertion();
      if (null == var2) {
         return 0;
      } else {
         AsymmetricBindingInfo var3 = var0.getAsymmetricBindingInfo();
         SamlTokenAssertion var4 = null;
         SamlTokenAssertion var5 = null;
         UsernameTokenAssertion var6 = null;
         if (var3 == null) {
            SymmetricBindingInfo var7 = var0.getSymmetricBindingInfo();
            if (var7 != null) {
               if (var7.getProtectionTokenAssertion() != null) {
                  var4 = var7.getProtectionTokenAssertion().getSamlTokenAssertion();
               } else if (var7.getSignatureTokenAssertion() != null) {
                  var4 = var7.getSignatureTokenAssertion().getSamlTokenAssertion();
               }
            }
         } else {
            InitiatorTokenAssertion var9 = var3.getInitiatorTokenAssertion();
            if (var9 != null) {
               var4 = var9.getSamlTokenAssertion();
            } else if (var3.getInitiatorSignatureTokenAssertion() != null) {
               var4 = var3.getInitiatorSignatureTokenAssertion().getSamlTokenAssertion();
            }
         }

         ArrayList var10 = new ArrayList();
         var10.addAll(var2.getSignedSupportingTokens());
         var10.addAll(var2.getSupportingTokens());
         var10.addAll(var2.getSignedEncryptedSupportingTokens());
         var10.addAll(var2.getEncryptedSupportingTokens());
         if (var10.isEmpty()) {
            return 0;
         } else {
            for(int var8 = 0; var8 < var10.size(); ++var8) {
               if (var10.get(var8) instanceof SamlTokenAssertion) {
                  var5 = (SamlTokenAssertion)var10.get(var8);
               }

               if (var10.get(var8) instanceof UsernameTokenAssertion) {
                  var6 = (UsernameTokenAssertion)var10.get(var8);
               }
            }

            if (var4 != null && var5 != null) {
               return 8665;
            } else {
               return (var4 != null || var5 != null) && var6 != null ? 8628 : 0;
            }
         }
      }
   }

   private static int checkPartsError(PolicyAlternative var0) {
      short var1 = 0;
      Iterator var2 = null;
      Set var3 = var0.getAssertions(RequiredParts.class);
      if (!var3.isEmpty()) {
         var2 = var3.iterator();

         while(var2.hasNext()) {
            RequiredParts var4 = (RequiredParts)var2.next();
            if (var4.getBody() != null) {
               if (verbose) {
                  Verbose.log((Object)"Invalid policy for required body part");
               }

               var1 = 8416;
            }

            if (var4.getUnknown() != null) {
               if (verbose) {
                  Verbose.log((Object)"Invalid policy for unknown required parts");
               }

               var1 = 8418;
            }
         }
      }

      var3 = var0.getAssertions(SignedParts.class);
      if (!var3.isEmpty()) {
         var2 = var3.iterator();

         while(var2.hasNext()) {
            SignedParts var5 = (SignedParts)var2.next();
            if (var5.getUnknown() != null) {
               if (verbose) {
                  Verbose.log((Object)"Invalid policy for unkown signature part");
               }

               var1 = 8438;
            }
         }
      }

      var3 = var0.getAssertions(EncryptedParts.class);
      if (!var3.isEmpty()) {
         var2 = var3.iterator();

         while(var2.hasNext()) {
            EncryptedParts var6 = (EncryptedParts)var2.next();
            if (var6.getUnknown() != null) {
               if (verbose) {
                  Verbose.log((Object)"Invalid policy for encryption unknow parts");
               }

               var1 = 8458;
            }
         }
      }

      return var1;
   }

   public static boolean hasOptionalAttribute(PolicyAlternative var0) {
      if (null == var0) {
         throw new IllegalArgumentException("Null Policy Alternative");
      } else if (var0.isEmpty()) {
         return false;
      } else {
         Set var1 = var0.getAssertions();
         if (var1 != null && !var1.isEmpty()) {
            Iterator var2 = var1.iterator();

            while(var2.hasNext()) {
               Object var3 = var2.next();
               if (var3 instanceof SymmetricBinding) {
                  SymmetricBinding var4 = (SymmetricBinding)var3;
                  if (var4.getProtectionToken() != null) {
                     X509Token var5 = var4.getProtectionToken().getX509Token();
                     if (var5 != null && var5.getRequireDerivedKeys() != null) {
                        RequireDerivedKeys var6 = var5.getRequireDerivedKeys();
                        if (var6.isOptional()) {
                           return true;
                        }
                     }
                  }
               } else if (var3 instanceof SupportingTokens) {
                  SupportingTokens var7 = (SupportingTokens)var3;
                  if (var7.getX509Token() != null && var7.getX509Token().getRequireDerivedKeys() != null && var7.getX509Token().getRequireDerivedKeys().isOptional()) {
                     return true;
                  }
               } else if (var3 instanceof EncryptedParts) {
                  EncryptedParts var8 = (EncryptedParts)var3;
               } else if (var3 instanceof SignedParts) {
                  SignedParts var9 = (SignedParts)var3;
               }
            }

            return false;
         } else {
            return false;
         }
      }
   }

   public static PolicyAlternative getPolicyAlternativeWithoutOption(PolicyAlternative var0) {
      if (!hasOptionalAttribute(var0)) {
         return null;
      } else {
         PolicyAlternative var1 = var0.clone();
         if (null == var1) {
            return null;
         } else {
            Set var2 = var1.getAssertions();
            if (var2 != null && !var2.isEmpty()) {
               Iterator var3 = var2.iterator();

               while(true) {
                  while(var3.hasNext()) {
                     Object var4 = var3.next();
                     if (var4 instanceof SymmetricBinding) {
                        SymmetricBinding var12 = (SymmetricBinding)var4;
                        if (var12.getProtectionToken() != null) {
                           X509Token var6 = var12.getProtectionToken().getX509Token();
                           if (var6 != null && var6.getRequireDerivedKeys() != null) {
                              var6.setOptional(false);
                              var6.getRequireDerivedKeys().setOptional(false);
                           }
                        }
                     } else if (var4 instanceof AsymmetricBinding) {
                        AsymmetricBinding var11 = (AsymmetricBinding)var4;
                        if (var11.getInitiatorToken() != null && var11.getInitiatorToken().getX509Token() != null && var11.getInitiatorToken().getX509Token().getRequireDerivedKeys() != null) {
                           var11.getInitiatorToken().getX509Token().setOptional(false);
                           var11.getInitiatorToken().getX509Token().getRequireDerivedKeys().setOptional(false);
                        }
                     } else if (var4 instanceof SupportingTokens) {
                        SupportingTokens var10 = (SupportingTokens)var4;
                        var10.setOptional(false);
                        if (var10.getX509Token() != null && var10.getX509Token().getRequireDerivedKeys() != null) {
                           var10.getX509Token().setOptional(false);
                           var10.getX509Token().getRequireDerivedKeys().setOptional(false);
                        } else if (var10.getUsernameToken() != null) {
                           var10.getUsernameToken().setOptional(false);
                        } else if (var10.getSamlToken() != null) {
                           var10.getSamlToken().setOptional(false);
                        } else if (var10.getSecureConversationToken() != null) {
                           var10.getSecureConversationToken().setOptional(false);
                        }
                     } else if (var4 instanceof EncryptedParts) {
                        EncryptedParts var5 = (EncryptedParts)var4;
                        var5.setOptional(false);
                        var5.setBodyOptional(false);
                     } else if (var4 instanceof SignedParts) {
                        SignedParts var7 = (SignedParts)var4;
                        var7.setBodyOptional(false);
                        var7.setOptional(false);
                     } else if (var4 instanceof EncryptedElements) {
                        EncryptedElements var8 = (EncryptedElements)var4;
                        var8.setOptional(false);
                     } else if (var4 instanceof SignedElements) {
                        SignedElements var9 = (SignedElements)var4;
                        var9.setOptional(false);
                     }
                  }

                  return var1;
               }
            } else {
               return null;
            }
         }
      }
   }
}
