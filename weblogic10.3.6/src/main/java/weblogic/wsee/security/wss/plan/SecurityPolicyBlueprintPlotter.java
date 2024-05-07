package weblogic.wsee.security.wss.plan;

import java.security.InvalidAlgorithmParameterException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import weblogic.wsee.policy.util.PolicySelectionPreference;
import weblogic.wsee.security.policy.SecurityToken;
import weblogic.wsee.security.policy12.assertions.ContentEncryptedElements;
import weblogic.wsee.security.policy12.assertions.EncryptedElements;
import weblogic.wsee.security.policy12.assertions.Header;
import weblogic.wsee.security.policy12.assertions.IncludeTimestamp;
import weblogic.wsee.security.policy12.assertions.Layout;
import weblogic.wsee.security.policy12.assertions.RequiredElements;
import weblogic.wsee.security.policy12.assertions.RequiredParts;
import weblogic.wsee.security.policy12.assertions.SignedElements;
import weblogic.wsee.security.policy12.assertions.XPath;
import weblogic.wsee.security.policy12.internal.QNameExprImpl;
import weblogic.wsee.security.wss.plan.fact.SecurityTokenFactory;
import weblogic.wsee.security.wss.plan.helper.SecurityPolicyBlueprintHelper;
import weblogic.wsee.security.wss.plan.helper.TokenReferenceTypeHelper;
import weblogic.wsee.security.wss.policy.EncryptionPolicy;
import weblogic.wsee.security.wss.policy.GeneralPolicy;
import weblogic.wsee.security.wss.policy.SecurityPolicyArchitectureException;
import weblogic.wsee.security.wss.policy.SecurityPolicyBuilderConstants;
import weblogic.wsee.security.wss.policy.SignaturePolicy;
import weblogic.wsee.security.wss.policy.TimestampPolicy;
import weblogic.wsee.security.wssp.AlgorithmSuiteInfo;
import weblogic.wsee.security.wssp.AsymmetricBindingInfo;
import weblogic.wsee.security.wssp.ConfidentialityAssertion;
import weblogic.wsee.security.wssp.HttpsTokenAssertion;
import weblogic.wsee.security.wssp.IntegrityAssertion;
import weblogic.wsee.security.wssp.IssuedTokenAssertion;
import weblogic.wsee.security.wssp.KerberosTokenAssertion;
import weblogic.wsee.security.wssp.ProtectionAssertion;
import weblogic.wsee.security.wssp.QNameExpr;
import weblogic.wsee.security.wssp.SamlTokenAssertion;
import weblogic.wsee.security.wssp.SecureConversationTokenAssertion;
import weblogic.wsee.security.wssp.SecurityBindingPropertiesAssertion;
import weblogic.wsee.security.wssp.SecurityPolicyAssertionInfo;
import weblogic.wsee.security.wssp.SupportingTokensAssertion;
import weblogic.wsee.security.wssp.SymmetricBindingInfo;
import weblogic.wsee.security.wssp.TokenAssertion;
import weblogic.wsee.security.wssp.TransportBindingInfo;
import weblogic.wsee.security.wssp.UsernameTokenAssertion;
import weblogic.wsee.security.wssp.WsTrustOptions;
import weblogic.wsee.security.wssp.Wss10Options;
import weblogic.wsee.security.wssp.Wss11Options;
import weblogic.wsee.security.wssp.X509TokenAssertion;
import weblogic.wsee.util.Verbose;
import weblogic.xml.crypto.encrypt.api.EncryptionMethod;
import weblogic.xml.crypto.encrypt.api.spec.EncryptionMethodParameterSpec;
import weblogic.xml.crypto.wss11.internal.SecurityBuilder;
import weblogic.xml.crypto.wss11.internal.SecurityValidator;
import weblogic.xml.dom.DOMUtils;

public class SecurityPolicyBlueprintPlotter {
   private static final boolean verbose = Verbose.isVerbose(SecurityPolicyBlueprintPlotter.class);
   private static final boolean debug = false;
   private SecurityPolicyAssertionInfo policy = null;
   private SecurityPolicyBlueprint blueprint;
   private AlgorithmSuiteInfo algorithmSuiteInfo = null;
   public static boolean SUPPORT_DK_ENDORSING_WITH_DK = false;
   private boolean isCompatMSFT = false;
   private String policyNamespaceUri = null;
   private static final String SP_NAMESPACE_2005_07 = "http://schemas.xmlsoap.org/ws/2005/07/securitypolicy";

   public SecurityPolicyBlueprintPlotter(SecurityBuilder var1) {
      if (null == var1) {
         throw new IllegalArgumentException("Null security builder found");
      } else {
         this.blueprint = new SecurityPolicyBlueprint(var1);
      }
   }

   public SecurityPolicyBlueprintPlotter(SecurityValidator var1) {
      if (null == var1) {
         throw new IllegalArgumentException("Null security validator found");
      } else {
         this.blueprint = new SecurityPolicyBlueprint(var1);
         this.blueprint.setBuildingPlan(27);
      }
   }

   protected SecurityPolicyBlueprintPlotter(SecurityPolicyBlueprint var1) {
      this.blueprint = var1;
   }

   public void setPolicyInfo(SecurityPolicyAssertionInfo var1) {
      this.policy = var1;
   }

   protected SecurityPolicyBlueprint getBlueprint() {
      return this.blueprint;
   }

   protected AlgorithmSuiteInfo getAlgorithmSuiteInfo() {
      return this.algorithmSuiteInfo;
   }

   protected void setAlgorithmSuiteInfo(AlgorithmSuiteInfo var1) {
      this.algorithmSuiteInfo = var1;
   }

   protected void drawPolicySelectionPreference(PolicySelectionPreference var1) {
      if (null == var1) {
         var1 = new PolicySelectionPreference();
      }

      this.blueprint.getGeneralPolicy().setPreference(var1);
   }

   protected void drawPolicyCompatibilityPreference(String var1, String var2) {
      if (var1 != null && var1.equals("msft")) {
         this.isCompatMSFT = true;
      }

      this.policyNamespaceUri = var2;
      if (verbose) {
         Verbose.log((Object)("isCompatMSFT set to " + this.isCompatMSFT));
         Verbose.log((Object)("policyNamespaceUri is " + this.policyNamespaceUri));
      }

      this.blueprint.getGeneralPolicy().setCompatMSFT(this.isCompatMSFT);
   }

   protected void drawLayOut(Object var1) {
      GeneralPolicy var2 = this.blueprint.getGeneralPolicy();
      if (null == var1) {
         if (var2.getLayout() == null) {
            var2.setLayoutToLax();
         }

      } else if (var1 instanceof Layout) {
         Layout var3 = (Layout)var1;
         if (null != var3.getStrict()) {
            var2.setLayoutToStrict();
         } else if (null != var3.getLaxTsFirst()) {
            var2.setLayoutToLaxTimestampFirst();
         } else if (null != var3.getLaxTsLast()) {
            var2.setLayoutToLaxTimestampLast();
         } else {
            var2.setLayoutToLax();
         }

      } else {
         if (var1.equals(SecurityBindingPropertiesAssertion.Layout.STRICT)) {
            var2.setLayoutToStrict();
         } else if (var1.equals(SecurityBindingPropertiesAssertion.Layout.LAX_TIMESTAMP_FIRST)) {
            var2.setLayoutToLaxTimestampFirst();
         } else if (var1.equals(SecurityBindingPropertiesAssertion.Layout.LAX_TIMESTAMP_LAST)) {
            var2.setLayoutToLaxTimestampLast();
         } else {
            var2.setLayoutToLax();
         }

      }
   }

   protected void drawTimestamp(Object var1, Map<String, Object> var2) throws SecurityPolicyArchitectureException {
      TimestampPolicy var3 = this.blueprint.getTimestampPolicy();
      if (null != var1) {
         if (var2 != null) {
            Object var4 = var2.get("weblogic.wsee.security.message_age");
            if (var4 instanceof Integer) {
               var3.setMessageAgeSeconds(((Integer)var4).shortValue());
            }
         }

         if (!"LaxTimestampFirst".equals(this.blueprint.getGeneralPolicy().getLayout()) && !"LaxTimestampFirst".equals(this.blueprint.getGeneralPolicy().getLayout())) {
            if (var1 instanceof AsymmetricBindingInfo) {
               AsymmetricBindingInfo var7 = (AsymmetricBindingInfo)var1;
               if (var7.isTimestampRequired()) {
                  if (var7.isTimestampOptional() && !this.isSecurityFirst()) {
                     if (verbose) {
                        Verbose.say("Skip the Timesptamp assertion due to it is optional and security is not a preference");
                     }

                     return;
                  }

                  var3.setIncludeTimestamp(true);
               }

            } else if (var1 instanceof SymmetricBindingInfo) {
               SymmetricBindingInfo var6 = (SymmetricBindingInfo)var1;
               if (var6.isTimestampRequired()) {
                  if (var6.isTimestampOptional() && !this.isSecurityFirst()) {
                     if (verbose) {
                        Verbose.say("Skip the Timesptamp assertion due to it is optional and security is not a preference");
                     }

                     return;
                  }

                  var3.setIncludeTimestamp(true);
               }

            } else if (var1 instanceof TransportBindingInfo) {
               TransportBindingInfo var5 = (TransportBindingInfo)var1;
               if (var5.isTimestampRequired()) {
                  var3.setIncludeTimestamp(true);
               }

            } else if (var1 instanceof IncludeTimestamp) {
               var3.setIncludeTimestamp(true);
            } else {
               throw new SecurityPolicyArchitectureException("unknow object for Timestamp policy ");
            }
         } else {
            var3.setIncludeTimestamp(true);
         }
      }
   }

   protected void drawTransportToken(HttpsTokenAssertion var1) {
      GeneralPolicy var2 = this.blueprint.getGeneralPolicy();
      var2.setHttpsAssertion(var1);
   }

   protected void drawWss11Options(Wss11Options var1, boolean var2) throws SecurityPolicyArchitectureException {
      this.blueprint.setRequest(var2);
      if (null != var1) {
         GeneralPolicy var3 = this.blueprint.getGeneralPolicy();
         var3.setWss11On();
         var3.setWss11OptionsAssertion(var1);
         if (!var2 && var1.isSignatureConfirmationRequired()) {
            this.addBlueprintAction(128);
         } else {
            var3.setRequireSignatureConfirmation(false);
         }
      }

   }

   protected void addBlueprintAction(int var1) {
      this.blueprint.addActionToBuildingPlan(var1);
   }

   protected void drawEncyptedKeyAction() {
      this.blueprint.setEncryptedKeyRequired(true);
      if (this.blueprint.isRequest()) {
         this.addBlueprintAction(288);
      } else {
         this.addBlueprintAction(320);
      }

   }

   protected void drawWss10Options(Wss10Options var1) throws SecurityPolicyArchitectureException {
      if (null != var1) {
         GeneralPolicy var2 = this.blueprint.getGeneralPolicy();
         var2.setWss10OptionsAssertion(var1);
      }

   }

   protected void drawAsymmetricBindingAlgorithm(AlgorithmSuiteInfo var1) throws SecurityPolicyArchitectureException {
      SignaturePolicy var2 = this.blueprint.getSigningPolicy();
      var2.setSignatureMethod(var1.getAsymSigUri());
      var2.setCanonicalizationMethod(var1.getC14nAlgUri());
      var2.setDigestMethod(var1.getDigUri());
      EncryptionPolicy var3 = this.blueprint.getEncryptionPolicy();
      var3.setEncryptionMethod(var1.getEncUri());
      var3.setKeyWrapMethod(var1.getAsymKwUri());
      var3.setCanonicalizationMethod();
      this.setEndorsingAlgo(var1);
   }

   private void setEndorsingAlgo(AlgorithmSuiteInfo var1) throws SecurityPolicyArchitectureException {
      SignaturePolicy var2 = this.blueprint.getEndorsingPolicy();
      var2.setSignatureMethod(var1.getAsymSigUri());
      var2.setCanonicalizationMethod(var1.getC14nAlgUri());
      var2.setDigestMethod(var1.getDigUri());
   }

   public void setSymmetricEndorsingAlgo(AlgorithmSuiteInfo var1) throws SecurityPolicyArchitectureException {
      if (verbose) {
         Verbose.log((Object)"Setting the Endorsing signing algorithm to symmetric signing algorithm ");
      }

      SignaturePolicy var2 = this.blueprint.getEndorsingPolicy();
      var2.setSignatureMethod(var1.getSymSigUri());
      var2.setCanonicalizationMethod(var1.getC14nAlgUri());
      var2.setDigestMethod(var1.getDigUri());
   }

   protected void drawSymmetricBindingAlgorithm(AlgorithmSuiteInfo var1, boolean var2) throws SecurityPolicyArchitectureException {
      SignaturePolicy var3 = this.blueprint.getSigningPolicy();
      var3.setSignatureMethod(var1.getSymSigUri());
      var3.setCanonicalizationMethod(var1.getC14nAlgUri());
      var3.setDigestMethod(var1.getDigUri());
      EncryptionPolicy var4 = this.blueprint.getEncryptionPolicy();
      var4.setEncryptionMethod(var1.getEncUri());
      if (var2) {
         var4.setKeyWrapMethod(var1.getAsymKwUri());
      }

      var4.setCanonicalizationMethod();
      this.setEndorsingAlgo(var1);
      this.algorithmSuiteInfo = var1;
   }

   protected void drawOneSignatureItem(String var1) {
      if (!this.blueprint.hasTransportSecuirity()) {
         this.blueprint.getSigningPolicy().addSignatureNode(var1, (Node)null);
      }

   }

   protected void drawOneEndorseItem(String var1) {
      this.blueprint.getEndorsingPolicy().addSignatureNode(var1, (Node)null);
   }

   protected void drawEncryptBeforeSigning() {
      this.blueprint.getGeneralPolicy().setEncryptBeforeSigning(true);
   }

   protected void drawSignatureProtection(boolean var1) {
      if (var1 && !this.isSecurityFirst()) {
         if (verbose) {
            Verbose.say("Skip the TokenProtection assertion due to it is optional and security is not a preference");
         }
      } else {
         this.drawOneEncryptionItem("EncryptSignature");
      }

   }

   protected void drawOneEncryptionItem(String var1) {
      if (!this.blueprint.hasTransportSecuirity()) {
         this.blueprint.getEncryptionPolicy().addNode(var1, (Node)null);
      }

   }

   protected void drawIntegrity(List<IntegrityAssertion> var1) throws SecurityPolicyArchitectureException {
      if (!this.blueprint.hasTransportSecuirity()) {
         if (null != var1 && var1.size() != 0) {
            Iterator var2 = var1.iterator();

            while(true) {
               while(true) {
                  SignaturePolicy var4;
                  Iterator var7;
                  SignedElements var9;
                  Set var10;
                  do {
                     do {
                        IntegrityAssertion var3;
                        do {
                           if (!var2.hasNext()) {
                              return;
                           }

                           var3 = (IntegrityAssertion)var2.next();
                           var4 = this.blueprint.getSigningPolicy();
                           if (var3.isSignedBodyRequired()) {
                              if (var3.isSignedBodyOptional() && !this.isSecurityFirst()) {
                                 if (verbose) {
                                    Verbose.say("Skip the Body Signatre assertion due to it is optional and security is not a preference");
                                 }
                              } else {
                                 this.drawOneSignatureItem("Body");
                              }
                           }

                           if (null != var3.getSigningParts() && var3.getSigningParts().size() > 0) {
                              Map var5 = var4.getSigningNodeMap();
                              if (var5.containsKey("Header") && null == var5.get("Header")) {
                                 if (verbose) {
                                    Verbose.log((Object)"isEntireHeaderAndBodySignatureRequired() == true, all header elements will be signed");
                                 }
                              } else {
                                 List var6 = var3.getSigningParts();
                                 var7 = var6.iterator();

                                 while(var7.hasNext()) {
                                    QNameExpr var8 = (QNameExpr)var7.next();
                                    if (var8 != null) {
                                       var4.addQNameExprNode("Header", var8);
                                    }
                                 }
                              }
                           }
                        } while(var3.getSignedElementsPolicy() == null);

                        var9 = var3.getSignedElementsPolicy();
                        var10 = var9.getXPathExpressions();
                     } while(var10 == null);
                  } while(var10.size() <= 0);

                  if ("http://www.w3.org/2002/06/xmldsig-filter2".equals(var9.getXPathVersion())) {
                     var4.addXPathFilter2NodeList("Element", new ArrayList(var10));
                  } else {
                     var7 = var10.iterator();

                     while(var7.hasNext()) {
                        XPath var11 = (XPath)var7.next();
                        var4.addXPathNode("Element", var11);
                     }
                  }
               }
            }
         }
      }
   }

   protected void drawConfidentiality(List<ConfidentialityAssertion> var1) throws SecurityPolicyArchitectureException {
      if (!this.blueprint.hasTransportSecuirity()) {
         if (null != var1 && var1.size() != 0) {
            EncryptionPolicy var2 = this.blueprint.getEncryptionPolicy();
            Iterator var3 = var1.iterator();

            while(true) {
               ConfidentialityAssertion var4;
               Iterator var11;
               XPath var12;
               do {
                  if (!var3.hasNext()) {
                     return;
                  }

                  var4 = (ConfidentialityAssertion)var3.next();
                  if (var4.isEncryptedBodyRequired()) {
                     if (var4.isEncryptedBodyOptional() && !this.isSecurityFirst()) {
                        if (verbose) {
                           Verbose.say("Skip the Body encryption due to optional and security is not a preference");
                        }
                     } else {
                        this.drawOneEncryptionItem("Body");
                     }
                  }

                  if (var4.isEncryptedHeaderRequired()) {
                     boolean var5 = false;
                     if (null != var4.getEncryptingParts() && var4.getEncryptingParts().size() > 0) {
                        List var6 = var4.getEncryptingParts();
                        Iterator var7 = var6.iterator();

                        label69:
                        while(true) {
                           while(true) {
                              QNameExpr var8;
                              do {
                                 if (!var7.hasNext()) {
                                    break label69;
                                 }

                                 var8 = (QNameExpr)var7.next();
                              } while(var8 == null);

                              if (var8.isOptional() && !this.isSecurityFirst()) {
                                 if (verbose) {
                                    Verbose.say("Skip the Body encryption due to optional and security is not a preference");
                                 }
                              } else {
                                 var5 = true;
                                 var2.addQNameExprNode("Header", var8);
                              }
                           }
                        }
                     }

                     if (!var5) {
                        this.drawOneEncryptionItem("Header");
                     }
                  }

                  if (var4.getEncryptedElementsPolicy() != null) {
                     EncryptedElements var9 = var4.getEncryptedElementsPolicy();
                     var11 = var9.getXPathExpressions().iterator();

                     while(var11.hasNext()) {
                        var12 = (XPath)var11.next();
                        var2.addXPathNode("Element", var12);
                     }
                  }
               } while(var4.getContentEncryptedElementsPolicy() == null);

               ContentEncryptedElements var10 = var4.getContentEncryptedElementsPolicy();
               var11 = var10.getXPathExpressions().iterator();

               while(var11.hasNext()) {
                  var12 = (XPath)var11.next();
                  var2.addXPathNode("Element", var12);
               }
            }
         }
      }
   }

   protected void drawUsernameToken(UsernameTokenAssertion var1, int var2, boolean var3) throws SecurityPolicyArchitectureException {
      if (null != var1) {
         TokenAssertion.TokenInclusion var4 = var1.getTokenInclusion();
         boolean var5 = SecurityPolicyBlueprintHelper.shouldIncludeToken(var4, var3);
         if (var3 || var5) {
            SecurityToken var6 = SecurityTokenFactory.makeSecurityToken(var1, var5);
            if (var1.getIssuer() != null) {
               var6.setTokenIssuer(var1.getIssuer());
            } else {
               var6.setIssuerName(var1.getIssuerName());
            }

            boolean var7 = this.isSignedSupportingToken(var2);
            boolean var8 = this.isEncryptedSupportingToken(var2);
            boolean var9 = this.isEndorsingSupportingToken(var2);
            if (!var8 && var7 && (this.blueprint.getGeneralPolicy().isCompatMSFT() && "http://schemas.xmlsoap.org/ws/2005/07/securitypolicy".equals(this.policyNamespaceUri) || this.blueprint.getGeneralPolicy().hasTrustOptions() && !var1.isHashPasswordRequired() || "http://schemas.xmlsoap.org/ws/2005/07/securitypolicy".equals(this.policyNamespaceUri) && !var1.isHashPasswordRequired())) {
               if (verbose) {
                  Verbose.log((Object)"Forcing encryption for Signed Supporting Username token");
               }

               var8 = true;
            }

            if (var8) {
               if (verbose) {
                  Verbose.log((Object)"Supporting Username token is Encrypted");
               }

               this.drawOneEncryptionItem("UserNameToken");
            }

            Node var10;
            Element var11;
            if (!var1.isCreatedRequired() && !var1.isNonceRequired()) {
               if (!this.blueprint.isForValidator() && !var1.isHashPasswordRequired() && !var1.requireDerivedKey() && !var1.noPasswordRequried() && this.isSecurityFirst() && !var7 && !var9) {
                  var10 = var6.getClaims();
                  var11 = null;
                  if (null != var10) {
                     var11 = DOMUtils.getFirstElement(var10, SecurityPolicyBuilderConstants.POLICY_USE_PASSWD);
                  }

                  if (null != var11) {
                     weblogic.xml.crypto.utils.DOMUtils.addAttribute(var11, SecurityPolicyBuilderConstants.POLICY_PASSWD_ATTR, "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#NonceCreate");
                  }
               }
            } else {
               var10 = var6.getClaims();
               var11 = null;
               if (null != var10) {
                  var11 = DOMUtils.getFirstElement(var10, SecurityPolicyBuilderConstants.POLICY_USE_PASSWD);
               }

               if (null != var11) {
                  if (var1.isCreatedRequired() && var1.isNonceRequired()) {
                     weblogic.xml.crypto.utils.DOMUtils.addAttribute(var11, SecurityPolicyBuilderConstants.POLICY_PASSWD_ATTR, "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#NonceCreate");
                  } else if (var1.isCreatedRequired()) {
                     weblogic.xml.crypto.utils.DOMUtils.addAttribute(var11, SecurityPolicyBuilderConstants.POLICY_PASSWD_ATTR, "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#Create");
                  } else {
                     weblogic.xml.crypto.utils.DOMUtils.addAttribute(var11, SecurityPolicyBuilderConstants.POLICY_PASSWD_ATTR, "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#Nonce");
                  }
               }
            }

            if (var7) {
               if (verbose) {
                  Verbose.log((Object)"Supporting Username token is Signed");
               }

               this.drawOneSignatureItem("UserNameToken");
            }

            if (var9) {
               if (verbose) {
                  Verbose.log((Object)"Supporting Username token is Endorsing");
               }

               throw new SecurityPolicyArchitectureException("Endorsing Supporting Username Token not supported");
            } else {
               this.blueprint.getIdentityPolicy().addIdentityToken(var6);
            }
         }
      }
   }

   protected void drawX509Token(X509TokenAssertion var1, int var2, boolean var3) throws SecurityPolicyArchitectureException {
      if (null != var1) {
         TokenAssertion.TokenInclusion var4 = var1.getTokenInclusion();
         boolean var5 = SecurityPolicyBlueprintHelper.shouldIncludeToken(var4, var3);
         SecurityToken var6;
         if (var2 != 2 && var2 != 3) {
            var6 = SecurityTokenFactory.makeSecurityToken(var1, var5, this.blueprint.getGeneralPolicy());
         } else {
            var6 = SecurityTokenFactory.makeSecurityTokenForSignature(var1, var5, this.blueprint.getGeneralPolicy());
         }

         if (var1.getIssuer() != null) {
            var6.setTokenIssuer(var1.getIssuer());
         } else {
            var6.setIssuerName(var1.getIssuerName());
         }

         if (var1.requireDerivedKey() && SUPPORT_DK_ENDORSING_WITH_DK) {
            String[] var7 = var1.getDerivedKeyTokenType(this.blueprint.getGeneralPolicy().isWssc13());
            if (var7 != null) {
               var6.setTokenTypeUri(var7[0]);
            } else {
               Verbose.say("No token types from DK??");
            }

            var6.setDerivedFromTokenType("http://docs.oasis-open.org/wss/oasis-wss-soap-message-security-1.1#EncryptedKey");
            var6.setEncryptionMethod(this.blueprint.getEncryptionPolicy().getEncryptionMethod());
            EncryptionMethod var8 = this.blueprint.getEncryptionPolicy().getKeyWrapMethod();
            if (null == var8 && this.algorithmSuiteInfo != null) {
               try {
                  var8 = this.blueprint.getXmlEncryptionFactory().newEncryptionMethod(this.algorithmSuiteInfo.getAsymKwUri(), (Integer)null, (EncryptionMethodParameterSpec)null);
               } catch (InvalidAlgorithmParameterException var10) {
                  throw new SecurityPolicyArchitectureException(var10.getMessage(), var10);
               }
            }

            var6.setKeyWrapMethod(var8);
            var6.setStrTypes(TokenReferenceTypeHelper.getSTRTypesForDK(var6.getTokenTypeUri()));
            this.blueprint.getEndorsingPolicy().setSignatureMethod(this.algorithmSuiteInfo.getSymSigUri());
         }

         switch (var2) {
            case 0:
            case 5:
               if (var5) {
                  this.blueprint.getIdentityPolicy().addIdentityToken(var6);
               }

               return;
            case 1:
               if (var5) {
                  this.drawOneSignatureItem("X509Token");
                  this.blueprint.getIdentityPolicy().addIdentityToken(var6);
               }

               return;
            case 2:
               if (var5) {
                  this.drawOneEndorseItem("EndoseSignature");
                  this.blueprint.getEndorsingPolicy().addSignatureToken(var6);
               }

               return;
            case 3:
            case 7:
               this.drawOneSignatureItem("X509Token");
               if (var5) {
                  this.drawOneEndorseItem("EndoseSignature");
                  this.blueprint.getEndorsingPolicy().addSignatureToken(var6);
               }

               return;
            case 4:
            case 6:
            default:
         }
      }
   }

   protected void drawSamlToken(SamlTokenAssertion var1, int var2, boolean var3, AlgorithmSuiteInfo var4) throws SecurityPolicyArchitectureException {
      if (null != var1) {
         TokenAssertion.TokenInclusion var5 = var1.getTokenInclusion();
         boolean var6 = SecurityPolicyBlueprintHelper.shouldIncludeToken(var5, var3);
         if (var3 || var6) {
            SecurityToken var7 = SecurityTokenFactory.makeSecurityToken(var1, var6, this.getBlueprint().getGeneralPolicy());
            if (var1.getIssuer() != null) {
               var7.setTokenIssuer(var1.getIssuer());
            } else {
               var7.setIssuerName(var1.getIssuerName());
            }

            boolean var8 = false;
            switch (var2) {
               case 0:
                  var8 = true;
                  break;
               case 4:
                  if (verbose) {
                     Verbose.log((Object)"SAML token will be Encrypted");
                  }

                  this.drawOneEncryptionItem("SamlToken");
               case 1:
                  this.drawOneSignatureItem("SamlToken");
                  var8 = true;
               case 5:
               case 6:
               default:
                  break;
               case 7:
                  this.drawOneEncryptionItem("SamlToken");
               case 3:
                  this.drawOneSignatureItem("SamlToken");
               case 2:
                  if (this.blueprint.hasTransportSecuirity()) {
                     this.setEndorsingAlgo(var4);
                     this.drawOneEndorseItem("TimeStamp");
                  }

                  if (var6 && !this.getBlueprint().hasTransportSecuirity()) {
                     this.drawOneEndorseItem("EndoseSignature");
                  }

                  this.blueprint.getEndorsingPolicy().addSignatureToken(var7);
            }

            if (var8) {
               this.blueprint.getIdentityPolicy().addIdentityToken(var7);
            }

         }
      }
   }

   protected void drawWsscToken(SecureConversationTokenAssertion var1, int var2, boolean var3, AlgorithmSuiteInfo var4) throws SecurityPolicyArchitectureException {
      if (null != var1) {
         TokenAssertion.TokenInclusion var5 = var1.getTokenInclusion();
         boolean var6 = SecurityPolicyBlueprintHelper.shouldIncludeToken(var5, var3);
         if (var4 == null) {
            var4 = this.policy.getAlgorithmSuiteInfo();
         }

         this.setEndorsingAlgo(var4);
         if (this.blueprint.hasTransportSecuirity()) {
            this.blueprint.getEndorsingPolicy().setSignatureMethod(var4.getSymSigUri());
         }

         SecurityToken var7 = SecurityTokenFactory.makeSecurityToken(var1, var6, this.getBlueprint().getGeneralPolicy(), var4);
         if (var1.getIssuer() != null) {
            var7.setTokenIssuer(var1.getIssuer());
         } else {
            var7.setIssuerName(var1.getIssuerName());
         }

         switch (var2) {
            case 0:
               this.blueprint.getIdentityPolicy().addIdentityToken(var7);
               break;
            case 1:
               this.drawOneSignatureItem("SecureConversationTokenToken");
               this.blueprint.getIdentityPolicy().addIdentityToken(var7);
            case 4:
            case 5:
            case 6:
            default:
               break;
            case 7:
               if (var6) {
                  this.drawOneEncryptionItem("SecureConversationTokenToken");
               }
            case 3:
               if (var6) {
                  this.drawOneSignatureItem("SecureConversationTokenToken");
               }
            case 2:
               if (var6) {
                  this.blueprint.getEndorsingPolicy().addSignatureToken(var7);
                  this.drawOneEndorseItem("TimeStamp");
               }
         }

      }
   }

   protected void drawIssuedToken(IssuedTokenAssertion var1, int var2, boolean var3, AlgorithmSuiteInfo var4) throws SecurityPolicyArchitectureException {
      if (null != var1) {
         SamlTokenAssertion.ConfirmationMethod var5 = SamlTokenAssertion.ConfirmationMethod.SENDER_VOUCHES;
         TokenAssertion.TokenInclusion var6 = var1.getTokenInclusion();
         boolean var7 = SecurityPolicyBlueprintHelper.shouldIncludeToken(var6, var3);
         if (var3) {
            switch (var2) {
               case 0:
               case 4:
               case 5:
               case 6:
               default:
                  var5 = SamlTokenAssertion.ConfirmationMethod.BEARER;
                  break;
               case 1:
                  var5 = SamlTokenAssertion.ConfirmationMethod.SENDER_VOUCHES;
                  break;
               case 2:
               case 3:
               case 7:
                  var5 = SamlTokenAssertion.ConfirmationMethod.HOLDER_OF_KEY;
            }

            SecurityToken var8 = SecurityTokenFactory.makeSecurityToken(var1, var7, this.getBlueprint().getGeneralPolicy(), var4, var5);
            if (var1.getIssuer() != null) {
               var8.setTokenIssuer(var1.getIssuer());
            } else {
               var8.setIssuerName(var1.getIssuerName());
            }

            switch (var2) {
               case 0:
               case 5:
               case 6:
               default:
                  break;
               case 4:
                  if (verbose) {
                     Verbose.log((Object)"SAML token will be Encrypted");
                  }

                  this.drawOneEncryptionItem("SamlToken");
               case 1:
                  this.drawOneSignatureItem("SamlToken");
                  break;
               case 7:
                  this.drawOneEncryptionItem("SamlToken");
               case 3:
                  this.drawOneSignatureItem("SamlToken");
               case 2:
                  if (this.blueprint.hasTransportSecuirity() || var8.getDerivedFromTokenType() != null) {
                     this.setSymmetricEndorsingAlgo(var4);
                  }

                  if (var7) {
                     this.drawOneEndorseItem("EndoseSignature");
                     this.blueprint.getEndorsingPolicy().addSignatureToken(var8);
                  }
            }

            this.blueprint.getIdentityPolicy().addIdentityToken(var8);
         }
      }
   }

   protected void drawKerberosToken(KerberosTokenAssertion var1, int var2, boolean var3) throws SecurityPolicyArchitectureException {
      if (null != var1) {
         TokenAssertion.TokenInclusion var4 = var1.getTokenInclusion();
         boolean var5 = SecurityPolicyBlueprintHelper.shouldIncludeToken(var4, var3);
         SecurityToken var6 = SecurityTokenFactory.makeSecurityToken(var1, var5, this.getBlueprint().getGeneralPolicy());
         if (var1.getIssuer() != null) {
            var6.setTokenIssuer(var1.getIssuer());
         } else {
            var6.setIssuerName(var1.getIssuerName());
         }

         switch (var2) {
            case 0:
            case 4:
            case 5:
            case 6:
            default:
               break;
            case 1:
               this.drawOneSignatureItem("KerberosToken");
               break;
            case 2:
               this.drawOneEndorseItem("KerberosToken");
               break;
            case 7:
               this.drawOneEncryptionItem("KerberosToken");
            case 3:
               this.drawOneSignatureItem("KerberosToken");
         }

         this.blueprint.getIdentityPolicy().addIdentityToken(var6);
      }
   }

   protected void drawSupportingToken(List var1, SupportingTokensAssertion.SecurityInfo var2, int var3) throws SecurityPolicyArchitectureException {
      boolean var4 = false;
      Object var5 = null;
      boolean var6 = this.blueprint.isRequest();
      ListIterator var7 = var1.listIterator();

      while(true) {
         while(var7.hasNext()) {
            Object var8 = var7.next();
            if (!(var8 instanceof TokenAssertion)) {
               var5 = var8;
               Verbose.log((Object)("Found Unknown Token Assertion =" + var8.toString()));
            } else if (((TokenAssertion)var8).isOptional() && !this.isSecurityFirst()) {
               Verbose.log((Object)("Optional token will not getnerated without security preference set for Token Assertion =" + var8.toString()));
               var4 = true;
            } else if (var8 instanceof UsernameTokenAssertion) {
               this.drawUsernameToken((UsernameTokenAssertion)var8, var3, var6);
               var4 = true;
            } else if (var8 instanceof X509TokenAssertion) {
               this.drawX509Token((X509TokenAssertion)var8, var3, var6);
               var4 = true;
            } else if (var8 instanceof SamlTokenAssertion) {
               this.drawSamlToken((SamlTokenAssertion)var8, var3, var6, this.algorithmSuiteInfo);
               var4 = true;
            } else if (var8 instanceof SecureConversationTokenAssertion) {
               this.drawWsscToken((SecureConversationTokenAssertion)var8, var3, var6, this.algorithmSuiteInfo);
               var4 = true;
            } else if (var8 instanceof IssuedTokenAssertion) {
               this.drawIssuedToken((IssuedTokenAssertion)var8, var3, var6, this.algorithmSuiteInfo);
               var4 = true;
            } else if (var8 instanceof KerberosTokenAssertion) {
               this.drawKerberosToken((KerberosTokenAssertion)var8, var3, var6);
               var4 = true;
            } else {
               var5 = var8;
               Verbose.log((Object)("Found Unknown Token Assertion" + var8.toString()));
            }
         }

         if (!var4) {
            throw new SecurityPolicyArchitectureException("Unknown Token found -" + var5.toString());
         }

         SignaturePolicy var21 = this.blueprint.getSigningPolicy();
         EncryptionPolicy var9 = this.blueprint.getEncryptionPolicy();
         SignaturePolicy var10 = this.blueprint.getEndorsingPolicy();
         EncryptionPolicy var11 = this.blueprint.getEncryptionPolicy();
         String var12 = var2.getSignedXPathVersion();
         String var13 = var2.getEncryptedXPathVersion();
         List var14 = var2.getSignedParts();
         List var15 = var2.getSignedElements();
         List var16 = var2.getEncryptedParts();
         List var17 = var2.getEncryptedElements();
         if (var2.isSignedBodyRequired()) {
            if (var2.isSignedBodyOptional() && !this.isSecurityFirst()) {
               if (verbose) {
                  Verbose.say("For supporting tokens, skip the Body Signatre assertion due to it is optional and security is not a preference");
               }
            } else if (var3 != 2 && var3 != 6) {
               if (!this.blueprint.hasTransportSecuirity() && var21.hasSignatureToken()) {
                  var21.addSignatureNode("Body", (Node)null);
               }
            } else if (var10.hasSignatureToken()) {
               var10.addSignatureNode("Body", (Node)null);
            }
         }

         if (var14 != null && !var14.isEmpty()) {
            Map var18 = var21.getSigningNodeMap();
            if (var18.containsKey("Header") && null == var18.get("Header")) {
               if (verbose) {
                  Verbose.log((Object)"For supporting tokens, isEntireHeaderAndBodySignatureRequired() == true, all header elements will be signed");
               }
            } else {
               Iterator var19 = var14.iterator();

               label211:
               while(true) {
                  while(true) {
                     QNameExpr var20;
                     do {
                        if (!var19.hasNext()) {
                           break label211;
                        }

                        var20 = (QNameExpr)var19.next();
                     } while(var20 == null);

                     if (var3 != 2 && var3 != 6) {
                        if (!this.blueprint.hasTransportSecuirity() && var21.hasSignatureToken()) {
                           var21.addQNameExprNode("Header", var20);
                        }
                     } else if (var10.hasSignatureToken()) {
                        var10.addQNameExprNode("Header", var20);
                     }
                  }
               }
            }
         }

         Iterator var22;
         XPath var23;
         if (var15 != null && !var15.isEmpty()) {
            if ("http://www.w3.org/2002/06/xmldsig-filter2".equals(var12)) {
               var21.addXPathFilter2NodeList("Element", new ArrayList(var15));
            } else {
               var22 = var15.iterator();

               label192:
               while(true) {
                  while(true) {
                     if (!var22.hasNext()) {
                        break label192;
                     }

                     var23 = (XPath)var22.next();
                     if (var3 != 2 && var3 != 6) {
                        if (!this.blueprint.hasTransportSecuirity() && var21.hasSignatureToken()) {
                           var21.addXPathNode("Element", var23);
                        }
                     } else if (var10.hasSignatureToken()) {
                        var10.addXPathNode("Element", var23);
                     }
                  }
               }
            }
         }

         if (var2.isEncryptedBodyRequired()) {
            if (var2.isEncryptedBodyOptional() && !this.isSecurityFirst()) {
               if (verbose) {
                  Verbose.say("For supporting tokens, skip the Body encryption due to optional and security is not a preference");
               }
            } else if (var3 != 2 && var3 != 6) {
               if (!this.blueprint.hasTransportSecuirity() && var9.hasEncryptionToken()) {
                  var9.addNode("Body", (Node)null);
               }
            } else if (var11.hasEncryptionToken()) {
               var11.addNode("Body", (Node)null);
            }
         }

         if (var16 != null && !var16.isEmpty()) {
            var22 = var16.iterator();

            label168:
            while(true) {
               while(true) {
                  QNameExpr var24;
                  do {
                     if (!var22.hasNext()) {
                        break label168;
                     }

                     var24 = (QNameExpr)var22.next();
                  } while(var24 == null);

                  if (var24.isOptional() && !this.isSecurityFirst()) {
                     if (verbose) {
                        Verbose.say("For supporting tokens, skip the Body encryption due to optional and security is not a preference");
                     }
                  } else if (var3 != 2 && var3 != 6) {
                     if (!this.blueprint.hasTransportSecuirity() && var9.hasEncryptionToken()) {
                        var9.addQNameExprNode("Header", var24);
                     }
                  } else if (var11.hasEncryptionToken()) {
                     var11.addQNameExprNode("Header", var24);
                  }
               }
            }
         }

         if (var17 != null && !var17.isEmpty()) {
            var22 = var17.iterator();

            while(true) {
               while(var22.hasNext()) {
                  var23 = (XPath)var22.next();
                  if (var3 != 2 && var3 != 6) {
                     if (!this.blueprint.hasTransportSecuirity() && var9.hasEncryptionToken()) {
                        var9.addXPathNode("Element", var23);
                     }
                  } else if (var11.hasEncryptionToken()) {
                     var11.addXPathNode("Element", var23);
                  }
               }

               return;
            }
         }

         return;
      }
   }

   protected void drawTrustOptions(WsTrustOptions var1) {
      this.blueprint.getGeneralPolicy().setTrustOptions(var1);
   }

   protected void drawProtectionAssertion(List<ProtectionAssertion> var1) {
      if (var1 != null && var1.size() != 0) {
         Iterator var4 = var1.iterator();

         while(true) {
            RequiredElements var9;
            do {
               if (!var4.hasNext()) {
                  return;
               }

               ProtectionAssertion var5 = (ProtectionAssertion)var4.next();
               RequiredParts var6 = var5.getRequiredPartsPolicy();
               if (var6 != null) {
                  Iterator var7 = var6.getHeaders().iterator();

                  while(var7.hasNext()) {
                     Header var3 = (Header)var7.next();
                     this.blueprint.addRequiredPart(new QNameExprImpl(var3.getHeaderName(), var3.getHeaderNamespaceUri(), var3.isOptional() || var6.isOptional()));
                  }
               }

               var9 = var5.getRequiredElementsPolicy();
            } while(var9 == null);

            Iterator var8 = var9.getXPathExpressions().iterator();

            while(var8.hasNext()) {
               XPath var2 = (XPath)var8.next();
               this.blueprint.addRequiredElement(var2);
            }
         }
      }
   }

   protected boolean isSecurityFirst() {
      return null != this.blueprint.getGeneralPolicy().getPreference() && !this.blueprint.getGeneralPolicy().getPreference().isDefaut() ? this.blueprint.getGeneralPolicy().getPreference().isSecurityFirst() : true;
   }

   private boolean isSignedSupportingToken(int var1) {
      return var1 == 1 || var1 == 3 || var1 == 4 || var1 == 7;
   }

   private boolean isEncryptedSupportingToken(int var1) {
      return var1 == 5 || var1 == 4 || var1 == 6 || var1 == 7;
   }

   private boolean isEndorsingSupportingToken(int var1) {
      return var1 == 2 || var1 == 3 || var1 == 6 || var1 == 7;
   }
}
