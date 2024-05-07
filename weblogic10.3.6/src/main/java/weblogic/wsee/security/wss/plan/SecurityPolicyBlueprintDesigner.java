package weblogic.wsee.security.wss.plan;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import weblogic.wsee.policy.util.PolicySelectionPreference;
import weblogic.wsee.security.policy.SecurityToken;
import weblogic.wsee.security.wss.plan.fact.SecurityTokenFactory;
import weblogic.wsee.security.wss.plan.helper.SecurityPolicyBlueprintHelper;
import weblogic.wsee.security.wss.plan.helper.TokenReferenceTypeHelper;
import weblogic.wsee.security.wss.policy.SecurityPolicyArchitectureException;
import weblogic.wsee.security.wssc.dk.DKClaims;
import weblogic.wsee.security.wssp.AsymmetricBindingInfo;
import weblogic.wsee.security.wssp.EncryptionTokenAssertion;
import weblogic.wsee.security.wssp.InitiatorTokenAssertion;
import weblogic.wsee.security.wssp.IntegrityAssertion;
import weblogic.wsee.security.wssp.ProtectionTokenAssertion;
import weblogic.wsee.security.wssp.RecipientTokenAssertion;
import weblogic.wsee.security.wssp.SamlTokenAssertion;
import weblogic.wsee.security.wssp.SecureConversationTokenAssertion;
import weblogic.wsee.security.wssp.SecurityPolicyAssertionInfo;
import weblogic.wsee.security.wssp.SignatureTokenAssertion;
import weblogic.wsee.security.wssp.SupportingTokensAssertion;
import weblogic.wsee.security.wssp.SymmetricBindingInfo;
import weblogic.wsee.security.wssp.TransportBindingInfo;
import weblogic.wsee.security.wssp.X509TokenAssertion;
import weblogic.wsee.util.Verbose;
import weblogic.xml.crypto.encrypt.api.EncryptionMethod;
import weblogic.xml.crypto.wss.WSSecurityException;
import weblogic.xml.crypto.wss11.internal.SecurityBuilder;
import weblogic.xml.crypto.wss11.internal.SecurityValidator;

public class SecurityPolicyBlueprintDesigner {
   private static final boolean verbose = Verbose.isVerbose(SecurityPolicyBlueprintDesigner.class);
   private static final boolean debug = false;
   private static final int ITA = 3;
   private static final int IETA = 2;
   private static final int ISTA = 1;
   private static final int RTA = 12;
   private static final int RETA = 4;
   private static final int RSTA = 8;
   private SecurityPolicyBlueprintPlotter blueprintPlotter;

   public SecurityPolicyBlueprintDesigner(SecurityBuilder var1) {
      if (null == var1) {
         throw new IllegalArgumentException("Null security builder found");
      } else {
         this.blueprintPlotter = new SecurityPolicyBlueprintPlotter(var1);
      }
   }

   public SecurityPolicyBlueprintDesigner(SecurityValidator var1) {
      if (null == var1) {
         throw new IllegalArgumentException("Null security builder found");
      } else {
         this.blueprintPlotter = new SecurityPolicyBlueprintPlotter(var1);
      }
   }

   public SecurityPolicyBlueprint getBlueprint() {
      return this.blueprintPlotter.getBlueprint();
   }

   public void designOutboundBlueprint(SecurityPolicyAssertionInfo var1, Map<String, Object> var2, boolean var3) throws WSSecurityException, SecurityPolicyArchitectureException {
      if (null == var1) {
         throw new IllegalArgumentException("Null SecurityPolicyAssertionInfo found");
      } else {
         this.blueprintPlotter.drawPolicySelectionPreference((PolicySelectionPreference)var2.get("weblogic.wsee.policy.selection.preference"));
         this.blueprintPlotter.drawPolicyCompatibilityPreference((String)var2.get("weblogic.wsee.policy.compat.preference"), var1.getNamespaceUri());
         this.blueprintPlotter.drawWss10Options(var1.getWss10Options());
         this.blueprintPlotter.drawWss11Options(var1.getWss11Options(), var3);
         this.blueprintPlotter.drawTrustOptions(var1.getWsTrustOptions());
         if (null != var1.getTransportBindingInfo()) {
            this.processTransportBindingInfoPolicy(var1, var1.getTransportBindingInfo(), var2);
         }

         if (null != var1.getAsymmetricBindingInfo()) {
            this.processAsymmetricBindingPolicy(var1, var2, var3);
         }

         if (null != var1.getSymmetricBindingInfo()) {
            this.processSymmetricBindingPolicy(var1, var2, var3);
         }

         this.blueprintPlotter.drawIntegrity(var1.getIntegrityAssertions());
         this.blueprintPlotter.drawConfidentiality(var1.getConfidentialityAssertions());
         if (null != var1.getSupportingTokensAssertion()) {
            this.processSupportingTokensAssertionPolicy(var1);
         }

         this.blueprintPlotter.drawProtectionAssertion(var1.getProtectionAssertions());
         this.verifyPolicy(var1, var3);
      }
   }

   private void processAsymmetricBindingPolicy(SecurityPolicyAssertionInfo var1, Map<String, Object> var2, boolean var3) throws WSSecurityException, SecurityPolicyArchitectureException {
      AsymmetricBindingInfo var4 = var1.getAsymmetricBindingInfo();
      if (null != var4) {
         if (null != var4.getAlgorithm()) {
            this.blueprintPlotter.drawAsymmetricBindingAlgorithm(var1.getAlgorithmSuiteInfo());
         }

         if (var4.isTokenProtectionRequired()) {
            if (var4.isTokenProtectionOptional() && !this.blueprintPlotter.isSecurityFirst()) {
               if (verbose) {
                  Verbose.say("\"The TokenProtection is off  due to it is optional and security is not a preference");
               }

               this.blueprintPlotter.getBlueprint().getSigningPolicy().setTokenProtection(false);
               this.blueprintPlotter.getBlueprint().getEndorsingPolicy().setTokenProtection(false);
            } else {
               this.blueprintPlotter.getBlueprint().getSigningPolicy().setTokenProtection(true);
               this.blueprintPlotter.getBlueprint().getEndorsingPolicy().setTokenProtection(true);
            }
         }

         if (var4.isSignatureProtectionRequired()) {
            this.blueprintPlotter.drawSignatureProtection(var4.isSignatureProtectionOptional());
         }

         if (var4.isEncryptBeforeSigning()) {
            if (var4.isEncryptBeforeSigningOptional() && !this.blueprintPlotter.isSecurityFirst()) {
               if (verbose) {
                  Verbose.say("The Encrypt Before Signning is off due to it is optional and security is not a preference");
               }
            } else {
               this.blueprintPlotter.drawEncryptBeforeSigning();
            }
         }

         this.blueprintPlotter.drawLayOut(var4.getLayout());
         this.blueprintPlotter.drawTimestamp(var4, var2);
         this.blueprintPlotter.setAlgorithmSuiteInfo(var1.getAlgorithmSuiteInfo());
         X509TokenAssertion var5 = null;
         SamlTokenAssertion var6 = null;
         X509TokenAssertion var7 = null;
         int var8 = 0;
         if (null != var4.getInitiatorTokenAssertion()) {
            var8 |= 3;
            var5 = var4.getInitiatorTokenAssertion().getX509TokenAssertion();
            var6 = var4.getInitiatorTokenAssertion().getSamlTokenAssertion();
         }

         if (null != var4.getRecipientTokenAssertion()) {
            var8 |= 12;
            var7 = var4.getRecipientTokenAssertion().getX509TokenAssertion();
         }

         if (null != var4.getInitiatorEncryptionTokenAssertion()) {
            var8 |= 2;
            if (var5 != null) {
               throw new SecurityPolicyArchitectureException("Duplicate X509TokenAssertion in InitiatorTokenAssertion.");
            }

            var5 = var4.getInitiatorEncryptionTokenAssertion().getX509TokenAssertion();
            if (var6 != null) {
               throw new SecurityPolicyArchitectureException("Duplicate SamlTokenAssertion in InitiatorTokenAssertion.");
            }

            var6 = var4.getInitiatorEncryptionTokenAssertion().getSamlTokenAssertion();
         }

         if (null != var4.getRecipientEncryptionTokenAssertion()) {
            var8 |= 4;
            if (var7 != null) {
               throw new SecurityPolicyArchitectureException("Duplicate X509TokenAssertion in RecipientTokenAssertion.");
            }

            var7 = var4.getRecipientEncryptionTokenAssertion().getX509TokenAssertion();
         }

         if (null != var4.getInitiatorSignatureTokenAssertion()) {
            var8 |= 1;
            if (var5 != null) {
               throw new SecurityPolicyArchitectureException("Duplicate X509TokenAssertion in InitiatorTokenAssertion.");
            }

            var5 = var4.getInitiatorSignatureTokenAssertion().getX509TokenAssertion();
            if (var6 != null) {
               throw new SecurityPolicyArchitectureException("Duplicate SamlTokenAssertion in InitiatorTokenAssertion.");
            }

            var6 = var4.getInitiatorSignatureTokenAssertion().getSamlTokenAssertion();
         }

         if (null != var4.getRecipientSignatureTokenAssertion()) {
            var8 |= 8;
            if (var7 != null) {
               throw new SecurityPolicyArchitectureException("Duplicate X509TokenAssertion in RecipientTokenAssertion.");
            }

            var7 = var4.getRecipientSignatureTokenAssertion().getX509TokenAssertion();
         }

         this.processInitiatorAndRecipientToken(var5, var6, var7, var8, var3);
         if (var4.isEntireHeaderAndBodySignatureRequired() && this.blueprintPlotter.getBlueprint().getSigningPolicy().hasSignatureToken()) {
            if (var4.isEntireHeaderAndBodySignatureOptional() && !this.blueprintPlotter.isSecurityFirst()) {
               if (verbose) {
                  Verbose.say("Skip the OnlySignEntrieHeaderAndBody assertion due to it is optional and security is not a preference");
               }
            } else {
               this.blueprintPlotter.drawOneSignatureItem("Body");
               this.blueprintPlotter.drawOneSignatureItem("Header");
            }
         }

         if (var3) {
         }

      }
   }

   private void processInitiatorAndRecipientToken(X509TokenAssertion var1, SamlTokenAssertion var2, X509TokenAssertion var3, int var4, boolean var5) throws SecurityPolicyArchitectureException {
      if (var3 == null) {
         if (verbose) {
            Verbose.say("No X509TokenAssertion in RecipientTokenAssertion.");
         }

         if ((var4 & 2) == 2 || (var4 & 8) == 8) {
            throw new SecurityPolicyArchitectureException("Unable to find X509TokenAssertion in RecipientTokenAssertion.");
         }
      }

      if (var1 == null && var2 == null) {
         throw new SecurityPolicyArchitectureException("Unable to find X509TokenAssertion or SamlTokenAssertion in InitiatorTokenAssertion.");
      } else {
         SecurityToken var6 = null;
         SecurityToken var7 = null;
         if (var5) {
            if ((var4 & 1) == 1) {
               if (var1 != null) {
                  var6 = SecurityTokenFactory.makeSecurityTokenForSignature(var1, SecurityPolicyBlueprintHelper.shouldIncludeToken(var1.getTokenInclusion(), true), this.getBlueprint().getGeneralPolicy());
               } else {
                  var6 = SecurityTokenFactory.makeSecurityToken(var2, SecurityPolicyBlueprintHelper.shouldIncludeToken(var2.getTokenInclusion(), true), this.getBlueprint().getGeneralPolicy());
               }
            }

            if ((var4 & 2) == 2) {
               var7 = SecurityTokenFactory.makeSecurityToken(var3, SecurityPolicyBlueprintHelper.shouldIncludeToken(var3.getTokenInclusion(), true), this.getBlueprint().getGeneralPolicy());
            }
         } else {
            if ((var4 & 8) == 8) {
               var6 = SecurityTokenFactory.makeSecurityTokenForSignatureResponse(var3, SecurityPolicyBlueprintHelper.shouldIncludeToken(var3.getTokenInclusion(), false), this.getBlueprint().getGeneralPolicy(), this.getBlueprint().isForValidator());
            }

            if ((var4 & 4) == 4) {
               if (var1 != null) {
                  var7 = SecurityTokenFactory.makeSecurityToken(var1, SecurityPolicyBlueprintHelper.shouldIncludeToken(var1.getTokenInclusion(), false), this.getBlueprint().getGeneralPolicy());
               } else {
                  var7 = SecurityTokenFactory.makeSecurityToken(var2, SecurityPolicyBlueprintHelper.shouldIncludeToken(var2.getTokenInclusion(), false), this.getBlueprint().getGeneralPolicy());
               }
            }
         }

         if (null != var6) {
            this.blueprintPlotter.getBlueprint().getSigningPolicy().addSignatureToken(var6);
         }

         if (null != var7) {
            this.blueprintPlotter.getBlueprint().getEncryptionPolicy().addEncryptionToken(var7);
         }

      }
   }

   private boolean processInitiatorAndRecipientToken(AsymmetricBindingInfo var1, boolean var2) throws WSSecurityException, SecurityPolicyArchitectureException {
      InitiatorTokenAssertion var3 = var1.getInitiatorTokenAssertion();
      RecipientTokenAssertion var4 = var1.getRecipientTokenAssertion();
      if (var3 != null && var4 != null) {
         X509TokenAssertion var5 = var3.getX509TokenAssertion();
         SamlTokenAssertion var6 = var3.getSamlTokenAssertion();
         X509TokenAssertion var7 = var4.getX509TokenAssertion();
         if (var7 == null) {
            throw new SecurityPolicyArchitectureException("Unable to find X509TokenAssertion in RecipientTokenAssertion.");
         } else {
            SecurityToken var8;
            SecurityToken var9;
            if (var2) {
               if (var5 != null) {
                  var8 = SecurityTokenFactory.makeSecurityTokenForSignature(var5, SecurityPolicyBlueprintHelper.shouldIncludeToken(var5.getTokenInclusion(), true), this.getBlueprint().getGeneralPolicy());
               } else {
                  if (var6 == null) {
                     throw new SecurityPolicyArchitectureException("Unable to find X509TokenAssertion or SamlTokenAssertion in InitiatorTokenAssertion.");
                  }

                  var8 = SecurityTokenFactory.makeSecurityToken(var6, SecurityPolicyBlueprintHelper.shouldIncludeToken(var6.getTokenInclusion(), true), this.getBlueprint().getGeneralPolicy());
               }

               var9 = SecurityTokenFactory.makeSecurityToken(var7, SecurityPolicyBlueprintHelper.shouldIncludeToken(var7.getTokenInclusion(), true), this.getBlueprint().getGeneralPolicy());
            } else {
               var8 = SecurityTokenFactory.makeSecurityTokenForSignatureResponse(var7, SecurityPolicyBlueprintHelper.shouldIncludeToken(var7.getTokenInclusion(), false), this.getBlueprint().getGeneralPolicy(), this.getBlueprint().isForValidator());
               if (var5 != null) {
                  var9 = SecurityTokenFactory.makeSecurityToken(var5, SecurityPolicyBlueprintHelper.shouldIncludeToken(var5.getTokenInclusion(), false), this.getBlueprint().getGeneralPolicy());
               } else {
                  if (var6 == null) {
                     throw new SecurityPolicyArchitectureException("Unable to find X509TokenAssertion or SamlTokenAssertion in InitiatorTokenAssertion.");
                  }

                  var9 = SecurityTokenFactory.makeSecurityToken(var6, SecurityPolicyBlueprintHelper.shouldIncludeToken(var6.getTokenInclusion(), false), this.getBlueprint().getGeneralPolicy());
               }
            }

            this.blueprintPlotter.getBlueprint().getSigningPolicy().addSignatureToken(var8);
            this.blueprintPlotter.getBlueprint().getEncryptionPolicy().addEncryptionToken(var9);
            return true;
         }
      } else {
         return false;
      }
   }

   private void processSymmetricBindingPolicy(SecurityPolicyAssertionInfo var1, Map<String, Object> var2, boolean var3) throws WSSecurityException, SecurityPolicyArchitectureException {
      SymmetricBindingInfo var4 = var1.getSymmetricBindingInfo();
      if (null != var4) {
         this.getBlueprint().setSymmeticPlan(true);
         this.blueprintPlotter.setAlgorithmSuiteInfo(var1.getAlgorithmSuiteInfo());
         X509TokenAssertion var6;
         boolean var7;
         SecurityToken var8;
         if (null != var4.getProtectionTokenAssertion()) {
            ProtectionTokenAssertion var5 = var4.getProtectionTokenAssertion();
            var6 = var5.getX509TokenAssertion();
            boolean var9;
            if (null != var6) {
               var7 = SecurityPolicyBlueprintHelper.shouldIncludeToken(var6.getTokenInclusion(), var3);
               if (null == var6.getX509TokenType()) {
                  throw new SecurityPolicyArchitectureException("Missing TokenType assertion");
               }

               var8 = SecurityTokenFactory.makeSecurityToken(var6, var7, this.getBlueprint().getGeneralPolicy());
               this.blueprintPlotter.getBlueprint().getSigningPolicy().addSignatureToken(var8);
               this.blueprintPlotter.getBlueprint().getEncryptionPolicy().addEncryptionToken(var8);
               var9 = false;
               if (var6.requireDerivedKey()) {
                  boolean var10 = true;
                  if (var6.isDerivedKeyOptional()) {
                     if (!var3 && var2.get("DerivedKeysToken") == null) {
                        var10 = false;
                        if (verbose) {
                           Verbose.say("Skip the DK assertion due to it is optional with <sp:RequireDerivedKeys wsp:Optional=\"true\" /> and the DK token is not in the request");
                        }
                     } else if (!this.blueprintPlotter.isSecurityFirst()) {
                        var10 = false;
                        if (verbose) {
                           Verbose.say("Skip the DK assertion due to it is optional with <sp:RequireDerivedKeys wsp:Optional=\"true\" /> and security is not a preference");
                        }
                     }
                  }

                  if (var10) {
                     var8.setTokenTypeUri(var6.getDerivedKeyTokenType(this.blueprintPlotter.getBlueprint().getGeneralPolicy().isWssc13())[0]);
                     var8.setDerivedFromTokenType("http://docs.oasis-open.org/wss/oasis-wss-soap-message-security-1.1#EncryptedKey");
                     var8.setClaims(DKClaims.makeDKClaimsNode(this.blueprintPlotter.getBlueprint().getGeneralPolicy(), (String)null, var1.getAlgorithmSuiteInfo()));
                     this.blueprintPlotter.addBlueprintAction(512);
                     this.blueprintPlotter.drawSymmetricBindingAlgorithm(var1.getAlgorithmSuiteInfo(), true);
                     var8.setEncryptionMethod(this.blueprintPlotter.getBlueprint().getEncryptionPolicy().getEncryptionMethod());
                     var8.setKeyWrapMethod(this.blueprintPlotter.getBlueprint().getEncryptionPolicy().getKeyWrapMethod());
                     this.blueprintPlotter.getBlueprint().getEncryptionPolicy().setKeyWrapMethod((EncryptionMethod)null);
                     var8.setStrTypesForDKBaseToken(var8.getStrTypes());
                     var8.setStrTypes(TokenReferenceTypeHelper.getSTRTypesForDK(var8.getTokenTypeUri()));
                     var8.setIncludeInMessage(true);
                     var9 = true;
                  }
               }

               if (!var9) {
                  if (var4.isEncryptedKeyRequired()) {
                     this.blueprintPlotter.drawEncyptedKeyAction();
                  }

                  if (null != var4.getAlgorithm()) {
                     this.blueprintPlotter.drawSymmetricBindingAlgorithm(var1.getAlgorithmSuiteInfo(), true);
                  }
               }
            }

            SamlTokenAssertion var13 = var5.getSamlTokenAssertion();
            if (var13 != null) {
               var8 = SecurityTokenFactory.makeSecurityToken(var13, true, this.getBlueprint().getGeneralPolicy());
               this.blueprintPlotter.getBlueprint().getSigningPolicy().addSignatureToken(var8);
               this.blueprintPlotter.getBlueprint().getEncryptionPolicy().addEncryptionToken(var8);
               if (null != var4.getAlgorithm()) {
                  this.blueprintPlotter.drawSymmetricBindingAlgorithm(var1.getAlgorithmSuiteInfo(), true);
               }
            }

            SecureConversationTokenAssertion var14 = var5.getSecureConversationTokenAssertion();
            if (var14 != null) {
               var9 = SecurityPolicyBlueprintHelper.shouldIncludeToken(var14.getTokenInclusion(), var3);
               SecurityToken var15 = SecurityTokenFactory.makeSecurityToken(var14, var9, this.getBlueprint().getGeneralPolicy(), var1.getAlgorithmSuiteInfo());
               this.blueprintPlotter.getBlueprint().getSigningPolicy().addSignatureToken(var15);
               this.blueprintPlotter.getBlueprint().getEncryptionPolicy().addEncryptionToken(var15);
               if (null != var4.getAlgorithm()) {
                  this.blueprintPlotter.drawSymmetricBindingAlgorithm(var1.getAlgorithmSuiteInfo(), false);
               }
            }
         } else {
            if (null != var4.getEncryptionTokenAssertion()) {
               EncryptionTokenAssertion var11 = var4.getEncryptionTokenAssertion();
               var6 = var11.getX509TokenAssertion();
               if (null != var6) {
                  var7 = SecurityPolicyBlueprintHelper.shouldIncludeToken(var6.getTokenInclusion(), var3);
                  if (null == var6.getX509TokenType()) {
                     throw new SecurityPolicyArchitectureException("Missing TokenType assertion");
                  }

                  var8 = SecurityTokenFactory.makeSecurityToken(var6, var7, this.getBlueprint().getGeneralPolicy());
                  this.blueprintPlotter.getBlueprint().getEncryptionPolicy().addEncryptionToken(var8);
               }
            }

            if (null != var4.getSignatureTokenAssertion()) {
               SignatureTokenAssertion var12 = var4.getSignatureTokenAssertion();
               var6 = var12.getX509TokenAssertion();
               if (null != var6) {
                  var7 = SecurityPolicyBlueprintHelper.shouldIncludeToken(var6.getTokenInclusion(), var3);
                  if (null == var6.getX509TokenType()) {
                     throw new SecurityPolicyArchitectureException("Missing TokenType assertion");
                  }

                  var8 = SecurityTokenFactory.makeSecurityToken(var6, var7, this.getBlueprint().getGeneralPolicy());
                  this.blueprintPlotter.getBlueprint().getEncryptionPolicy().addEncryptionToken(var8);
               }
            }
         }

         if (var4.isEntireHeaderAndBodySignatureRequired()) {
            if (var4.isEntireHeaderAndBodySignatureOptional() && !this.blueprintPlotter.isSecurityFirst()) {
               if (verbose) {
                  Verbose.say("Skip the OnlySignEntrieHeaderAndBody assertion due to it is optional and security is not a preference");
               }
            } else {
               this.blueprintPlotter.drawOneSignatureItem("Body");
               this.blueprintPlotter.drawOneSignatureItem("Header");
            }
         }

         if (var4.isTokenProtectionRequired()) {
            if (var4.isTokenProtectionOptional() && !this.blueprintPlotter.isSecurityFirst()) {
               if (verbose) {
                  Verbose.say("The TokenProtection is off due to it is optional and security is not a preference");
               }

               this.blueprintPlotter.getBlueprint().getSigningPolicy().setTokenProtection(false);
               this.blueprintPlotter.getBlueprint().getEndorsingPolicy().setTokenProtection(false);
            } else {
               this.blueprintPlotter.getBlueprint().getSigningPolicy().setTokenProtection(true);
               this.blueprintPlotter.getBlueprint().getEndorsingPolicy().setTokenProtection(true);
            }
         }

         if (var4.isSignatureProtectionRequired()) {
            if (var4.isSignatureProtectionOptional() && !this.blueprintPlotter.isSecurityFirst()) {
               if (verbose) {
                  Verbose.say("The SignatureProtection is off due to it is optional and security is not a preference");
               }
            } else {
               this.blueprintPlotter.drawSignatureProtection(var4.isSignatureProtectionOptional());
               this.blueprintPlotter.addBlueprintAction(8192);
            }
         }

         if (var4.isEncryptBeforeSigning()) {
            if (var4.isEncryptBeforeSigningOptional() && !this.blueprintPlotter.isSecurityFirst()) {
               if (verbose) {
                  Verbose.say("The Encrypt Before Signning is off due to it is optional and security is not a preference");
               }
            } else {
               this.blueprintPlotter.drawEncryptBeforeSigning();
               this.blueprintPlotter.addBlueprintAction(4096);
            }
         }

         this.blueprintPlotter.drawLayOut(var4.getLayout());
         this.blueprintPlotter.drawTimestamp(var4, var2);
      }
   }

   private void processSupportingTokensAssertionPolicy(SecurityPolicyAssertionInfo var1) throws WSSecurityException, SecurityPolicyArchitectureException {
      this.blueprintPlotter.setPolicyInfo(var1);
      SupportingTokensAssertion var2 = var1.getSupportingTokensAssertion();
      if (var2.hasSupportingTokens()) {
         this.blueprintPlotter.drawSupportingToken(var2.getSupportingTokens(), var2.getSecurityInfoOfSupportingTokens(), 0);
      }

      if (var2.hasEncryptedSupportingTokens()) {
         this.blueprintPlotter.drawSupportingToken(var2.getEncryptedSupportingTokens(), var2.getSecurityInfoOfSignedEncryptedSupportingTokens(), 5);
      }

      if (var2.hasSignedSupportingTokens()) {
         this.blueprintPlotter.drawSupportingToken(var2.getSignedSupportingTokens(), var2.getSecurityInfoOfSignedSupportingTokens(), 1);
      }

      if (var2.hasEndorsingSupportingTokens()) {
         this.blueprintPlotter.drawSupportingToken(var2.getEndorsingSupportingTokens(), var2.getSecurityInfoOfEndorsingSupportingTokens(), 2);
         this.blueprintPlotter.addBlueprintAction(1024);
      }

      if (var2.hasSignedEndorsingSupportingTokens()) {
         this.blueprintPlotter.drawSupportingToken(var2.getSignedEndorsingSupportingTokens(), var2.getSecurityInfoOfSignedEndorsingSupportingTokens(), 3);
         this.blueprintPlotter.addBlueprintAction(3072);
      }

      if (var2.hasSignedEncryptedSupportingTokens()) {
         this.blueprintPlotter.drawSupportingToken(var2.getSignedEncryptedSupportingTokens(), var2.getSecurityInfoOfSignedEncryptedSupportingTokens(), 4);
      }

   }

   private void processTransportBindingInfoPolicy(SecurityPolicyAssertionInfo var1, TransportBindingInfo var2, Map<String, Object> var3) throws WSSecurityException, SecurityPolicyArchitectureException {
      this.blueprintPlotter.drawTimestamp(var2, var3);
      this.blueprintPlotter.drawLayOut(var2.getLayout());
      this.blueprintPlotter.drawTransportToken(var2.getHttpsTokenAssertion());
      this.blueprintPlotter.setAlgorithmSuiteInfo(var1.getAlgorithmSuiteInfo());
   }

   private void verifyPolicy(SecurityPolicyAssertionInfo var1, boolean var2) throws SecurityPolicyArchitectureException {
      if (!this.getBlueprint().getEncryptionPolicy().isPolicyValid()) {
         throw new SecurityPolicyArchitectureException("Invalid encryption policy");
      } else if (!this.getBlueprint().getEndorsingPolicy().isPolicyValid()) {
         throw new SecurityPolicyArchitectureException("Invalid endorsing policy");
      } else {
         if (!this.getBlueprint().getSigningPolicy().isPolicyValid()) {
            List var3 = var1.getIntegrityAssertions();
            if (var3 != null && var3.size() > 0) {
               if (null != var1.getAsymmetricBindingInfo() && null != var1.getAsymmetricBindingInfo().getInitiatorSignatureTokenAssertion() && (null == var1.getAsymmetricBindingInfo().getRecipientTokenAssertion() || null == var1.getAsymmetricBindingInfo().getRecipientSignatureTokenAssertion()) && !var2) {
                  if (verbose) {
                     Verbose.say("The Signature policy is not verified, due to this is a response on InitiatorSignatureTokenAssertion only");
                  }

                  return;
               }

               Iterator var5 = var3.iterator();

               while(var5.hasNext()) {
                  IntegrityAssertion var4 = (IntegrityAssertion)var5.next();
                  if (var4 != null && (var4.isSignedBodyRequired() || var4.isSignedWsaHeadersRequired() || var4.getSigningElements() != null || var4.getSigningParts() != null)) {
                     throw new SecurityPolicyArchitectureException("Invalid signing policy");
                  }
               }
            }
         }

      }
   }
}
