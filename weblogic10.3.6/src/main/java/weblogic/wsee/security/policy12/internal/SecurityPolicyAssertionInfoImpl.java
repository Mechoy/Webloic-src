package weblogic.wsee.security.policy12.internal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import weblogic.wsee.policy.framework.NormalizedExpression;
import weblogic.wsee.policy.framework.PolicyAlternative;
import weblogic.wsee.policy.framework.PolicyAssertion;
import weblogic.wsee.security.policy12.assertions.AsymmetricBinding;
import weblogic.wsee.security.policy12.assertions.EncryptedElements;
import weblogic.wsee.security.policy12.assertions.EncryptedParts;
import weblogic.wsee.security.policy12.assertions.EncryptedSupportingTokens;
import weblogic.wsee.security.policy12.assertions.EndorsingSupportingTokens;
import weblogic.wsee.security.policy12.assertions.RequiredElements;
import weblogic.wsee.security.policy12.assertions.RequiredParts;
import weblogic.wsee.security.policy12.assertions.SignedElements;
import weblogic.wsee.security.policy12.assertions.SignedEncryptedSupportingTokens;
import weblogic.wsee.security.policy12.assertions.SignedEndorsingSupportingTokens;
import weblogic.wsee.security.policy12.assertions.SignedParts;
import weblogic.wsee.security.policy12.assertions.SignedSupportingTokens;
import weblogic.wsee.security.policy12.assertions.SupportingTokens;
import weblogic.wsee.security.policy12.assertions.SymmetricBinding;
import weblogic.wsee.security.policy12.assertions.TransportBinding;
import weblogic.wsee.security.policy12.assertions.Trust10;
import weblogic.wsee.security.policy12.assertions.Trust13;
import weblogic.wsee.security.policy12.assertions.Wss10;
import weblogic.wsee.security.policy12.assertions.Wss11;
import weblogic.wsee.security.wssp.AlgorithmSuiteInfo;
import weblogic.wsee.security.wssp.AsymmetricBindingInfo;
import weblogic.wsee.security.wssp.ConfidentialityAssertion;
import weblogic.wsee.security.wssp.HttpsTokenAssertion;
import weblogic.wsee.security.wssp.IntegrityAssertion;
import weblogic.wsee.security.wssp.ProtectionAssertion;
import weblogic.wsee.security.wssp.ProtectionTokenAssertion;
import weblogic.wsee.security.wssp.SecureConversationTokenAssertion;
import weblogic.wsee.security.wssp.SecurityBindingPropertiesAssertion;
import weblogic.wsee.security.wssp.SecurityPolicyAssertionInfo;
import weblogic.wsee.security.wssp.SupportingTokensAssertion;
import weblogic.wsee.security.wssp.SymmetricBindingInfo;
import weblogic.wsee.security.wssp.TokenAssertion;
import weblogic.wsee.security.wssp.TransportBindingInfo;
import weblogic.wsee.security.wssp.WsTrustOptions;
import weblogic.wsee.security.wssp.Wss10Options;
import weblogic.wsee.security.wssp.Wss11Options;
import weblogic.wsee.security.wssp.X509TokenAssertion;
import weblogic.wsee.util.Verbose;

public class SecurityPolicyAssertionInfoImpl implements SecurityPolicyAssertionInfo {
   private static final boolean verbose = Verbose.isVerbose(SecurityPolicyAssertionInfo.class) || Verbose.isVerbose(SecurityPolicyAssertionInfoImpl.class);
   private boolean hasMessageLevelSecurity = false;
   private TransportBindingInfoImpl transportBinding = null;
   private SymmetricBindingInfoImpl symmetricBinding = null;
   private AsymmetricBindingInfoImpl asymmetricBiding = null;
   private SupportingTokensAssertionImpl supportingTokens = null;
   private WsTrustOptionsImpl trustOptions = null;
   private Wss10OptionsImpl wss10Options = null;
   private Wss11OptionsImpl wss11Options = null;
   private List<ProtectionAssertion> protectionAssts = null;
   private List<ConfidentialityAssertion> confidentialityAssts = null;
   private List<IntegrityAssertion> integrityAssts = null;
   private AlgorithmSuiteInfo algSuiteInfoImpl;
   private List ufo = null;
   private String namespaceUri = null;
   private boolean needFurtherSamlConfirmVerification = false;

   public void init(PolicyAlternative var1) {
      Iterator var2 = var1.getAssertions().iterator();

      while(true) {
         while(var2.hasNext()) {
            PolicyAssertion var3 = (PolicyAssertion)var2.next();
            if (null == var3.getName()) {
               if (verbose) {
                  Verbose.log((Object)("Found an unknown assertion " + var3));
               }

               this.reportUfo(var3);
            } else {
               String var4 = var3.getName().getNamespaceURI();
               if (!"http://docs.oasis-open.org/ws-sx/ws-securitypolicy/200702".equals(var4) && !"http://docs.oasis-open.org/ws-sx/ws-securitypolicy/200512".equals(var4) && !"http://schemas.xmlsoap.org/ws/2005/07/securitypolicy".equals(var4)) {
                  if (verbose) {
                     Verbose.log((Object)("Bypass non-security assertion " + var3.getName()));
                  }

                  this.reportUfo(var3);
               } else if (var3 instanceof SymmetricBinding) {
                  this.symmetricBinding = new SymmetricBindingInfoImpl((SymmetricBinding)var3);
                  this.algSuiteInfoImpl = getAlgorithmSuiteInfo(this.symmetricBinding.getAlgorithm());
                  this.hasMessageLevelSecurity = true;
                  this.namespaceUri = var4;
               } else if (var3 instanceof AsymmetricBinding) {
                  this.asymmetricBiding = new AsymmetricBindingInfoImpl((AsymmetricBinding)var3);
                  this.algSuiteInfoImpl = getAlgorithmSuiteInfo(this.asymmetricBiding.getAlgorithm());
                  this.hasMessageLevelSecurity = true;
                  this.namespaceUri = var4;
               } else if (var3 instanceof Wss11) {
                  this.wss11Options = new Wss11OptionsImpl((Wss11)var3);
                  if (null == this.namespaceUri) {
                     this.namespaceUri = var4;
                  }
               } else if (var3 instanceof Wss10) {
                  this.wss10Options = new Wss10OptionsImpl((Wss10)var3);
               } else {
                  IntegrityAssertionImpl var5;
                  if (var3 instanceof SignedParts) {
                     var5 = this.getIntegrityAssertionImplInstance();
                     var5.init((SignedParts)var3);
                     if (null == this.namespaceUri) {
                        this.namespaceUri = var4;
                     }

                     if (this.integrityAssts == null) {
                        this.integrityAssts = new ArrayList();
                     }

                     this.integrityAssts.add(var5);
                  } else {
                     ConfidentialityAssertionImpl var6;
                     if (var3 instanceof EncryptedParts) {
                        var6 = this.getConfidentialityAssertionImplInstance();
                        var6.init((EncryptedParts)var3);
                        if (this.confidentialityAssts == null) {
                           this.confidentialityAssts = new ArrayList();
                        }

                        this.confidentialityAssts.add(var6);
                     } else if (var3 instanceof EncryptedElements) {
                        var6 = this.getConfidentialityAssertionImplInstance();
                        var6.init((EncryptedElements)var3);
                        if (this.confidentialityAssts == null) {
                           this.confidentialityAssts = new ArrayList();
                        }

                        this.confidentialityAssts.add(var6);
                     } else if (var3 instanceof SupportingTokens) {
                        this.getSupportingTokensAssertionInstance().initSupportingTokens((SupportingTokens)var3);
                        this.hasMessageLevelSecurity = true;
                     } else if (var3 instanceof SignedSupportingTokens) {
                        this.getSupportingTokensAssertionInstance().initSignedSupportingTokens((SignedSupportingTokens)var3);
                        this.needFurtherSamlConfirmVerification = this.supportingTokens.isVerifySamlConfirmation();
                     } else if (var3 instanceof EncryptedSupportingTokens) {
                        this.getSupportingTokensAssertionInstance().initEncryptedSupportingTokens((EncryptedSupportingTokens)var3);
                     } else if (var3 instanceof SignedEncryptedSupportingTokens) {
                        this.getSupportingTokensAssertionInstance().initSignedEncryptedSupportingTokens((SignedEncryptedSupportingTokens)var3);
                        this.needFurtherSamlConfirmVerification = this.supportingTokens.isVerifySamlConfirmation();
                     } else if (var3 instanceof EndorsingSupportingTokens) {
                        this.getSupportingTokensAssertionInstance().initEndorsingSupportingTokens((EndorsingSupportingTokens)var3);
                     } else if (var3 instanceof SignedEndorsingSupportingTokens) {
                        this.getSupportingTokensAssertionInstance().initSignedEndorsingSupportingTokens((SignedEndorsingSupportingTokens)var3);
                     } else {
                        ProtectionAssertionImpl var7;
                        if (var3 instanceof RequiredParts) {
                           var7 = new ProtectionAssertionImpl((RequiredParts)var3);
                           this.hasMessageLevelSecurity = true;
                           if (this.protectionAssts == null) {
                              this.protectionAssts = new ArrayList();
                           }

                           this.protectionAssts.add(var7);
                        } else if (var3 instanceof RequiredElements) {
                           var7 = new ProtectionAssertionImpl((RequiredElements)var3);
                           this.hasMessageLevelSecurity = true;
                           if (this.protectionAssts == null) {
                              this.protectionAssts = new ArrayList();
                           }

                           this.protectionAssts.add(var7);
                        } else if (var3 instanceof SignedElements) {
                           var5 = this.getIntegrityAssertionImplInstance();
                           var5.init((SignedElements)var3);
                           if (this.integrityAssts == null) {
                              this.integrityAssts = new ArrayList();
                           }

                           this.integrityAssts.add(var5);
                        } else if (var3 instanceof TransportBinding) {
                           if (null == this.transportBinding) {
                              this.transportBinding = new TransportBindingInfoImpl((TransportBinding)var3);
                              this.algSuiteInfoImpl = getAlgorithmSuiteInfo(this.transportBinding.getAlgorithm());
                           } else {
                              TransportBindingInfoImpl var8 = new TransportBindingInfoImpl((TransportBinding)var3);
                              if (this.countHttpsTokenAssertion(var8) > this.countHttpsTokenAssertion(this.transportBinding)) {
                                 this.transportBinding = var8;
                                 this.algSuiteInfoImpl = getAlgorithmSuiteInfo(var8.getAlgorithm());
                              }
                           }
                        } else if (var3 instanceof Trust13) {
                           this.trustOptions = new WsTrustOptionsImpl((Trust13)var3);
                        } else if (var3 instanceof Trust10) {
                           this.trustOptions = new WsTrustOptionsImpl((Trust10)var3);
                        } else {
                           if (verbose) {
                              Verbose.log((Object)("Unsported security assertion " + var3.getName()));
                           }

                           this.reportUfo(var3);
                        }
                     }
                  }
               }
            }
         }

         this.postInit();
         return;
      }
   }

   private int countHttpsTokenAssertion(TransportBindingInfoImpl var1) {
      if (null == var1) {
         return -2;
      } else {
         int var2 = 0;
         HttpsTokenAssertion var3 = var1.getHttpsTokenAssertion();
         if (var3 == null) {
            return -1;
         } else {
            ++var2;
            if (var3.isClientCertificateRequired()) {
               var2 += 2;
            }

            if (var3.isHttpBasicAuthenticationRequired()) {
               ++var2;
            }

            return var2;
         }
      }
   }

   private void reportUfo(PolicyAssertion var1) {
      if (null == var1) {
         if (verbose) {
            Verbose.log((Object)"Found an unidentified null security assertion ");
         }

      } else {
         if (this.ufo == null) {
            this.ufo = new ArrayList();
         }

         this.ufo.add(var1);
         if (verbose) {
            if (null == var1.getPolicySubject()) {
               Verbose.log((Object)("Found an unidentified security assertion " + var1 + " Name =" + var1.getName() + " Subject = NULL"));
            } else {
               Verbose.log((Object)("Found an unidentified security assertion " + var1 + " Name =" + var1.getName() + " Subject " + var1.getPolicySubject().name()));
            }
         }

      }
   }

   private void postInit() {
      if (this.confidentialityAssts != null && this.wss11Options != null) {
         Iterator var1 = this.confidentialityAssts.iterator();

         label67:
         while(true) {
            ConfidentialityAssertionImpl var2;
            do {
               if (!var1.hasNext()) {
                  break label67;
               }

               var2 = (ConfidentialityAssertionImpl)var1.next();
            } while(var2.getEncryptingElements().size() <= 0 && var2.getEncryptingParts().size() <= 0);

            var2.setEncryptedHeaderRequired(true);
         }
      }

      if (this.symmetricBinding != null && this.wss11Options != null) {
         ProtectionTokenAssertion var4 = this.symmetricBinding.getProtectionTokenAssertion();
         if (var4 != null) {
            X509TokenAssertion var6 = var4.getX509TokenAssertion();
            if (var6 != null && TokenAssertion.TokenInclusion.NEVER.equals(var6.getTokenInclusion())) {
               this.symmetricBinding.setEncryptedKeyRequired(true);
            }
         }
      }

      if (this.symmetricBinding != null && this.needFurtherSamlConfirmVerification && !this.supportingTokens.hasEndorsingSupportingTokens() && !this.supportingTokens.hasSignedEndorsingSupportingTokens()) {
         List var5 = null;
         if (this.supportingTokens.hasSignedSupportingTokens()) {
            var5 = this.supportingTokens.getSignedSupportingTokens();
         }

         if (this.supportingTokens.hasSignedEncryptedSupportingTokens()) {
            var5 = this.supportingTokens.getSignedEncryptedSupportingTokens();
         }

         if (null != var5 && var5.size() > 0) {
            for(int var7 = 0; var7 < var5.size(); ++var7) {
               if (var5.get(var7) instanceof SamlTokenAssertionImpl) {
                  SamlTokenAssertionImpl var3 = (SamlTokenAssertionImpl)var5.get(var7);
                  var3.setConfirmationMethodBearer();
               }
            }
         }
      }

   }

   private static AlgorithmSuiteInfo getAlgorithmSuiteInfo(SecurityBindingPropertiesAssertion.AlgorithmSuite var0) {
      if (var0.equals(SecurityBindingPropertiesAssertion.AlgorithmSuite.BASIC256)) {
         return new Basic256AlgorithmSuiteInfoImpl();
      } else if (var0.equals(SecurityBindingPropertiesAssertion.AlgorithmSuite.TRIPLEDES_RSA15)) {
         return new TripleDesRsa15AlgorithmSuiteInfoImpl();
      } else if (var0.equals(SecurityBindingPropertiesAssertion.AlgorithmSuite.BASIC192)) {
         return new Basic192AlgorithmSuiteInfoImpl();
      } else if (var0.equals(SecurityBindingPropertiesAssertion.AlgorithmSuite.BASIC128)) {
         return new Basic128AlgorithmSuiteInfoImpl();
      } else if (var0.equals(SecurityBindingPropertiesAssertion.AlgorithmSuite.TRIPLEDES)) {
         return new TripleDesAlgorithmSuiteInfoImpl();
      } else if (var0.equals(SecurityBindingPropertiesAssertion.AlgorithmSuite.BASIC256_RSA15)) {
         return new Basic256Rsa15AlgorithmSuiteInfoImpl();
      } else if (var0.equals(SecurityBindingPropertiesAssertion.AlgorithmSuite.BASIC192_RSA15)) {
         return new Basic192Rsa15AlgorithmSuiteInfoImpl();
      } else if (var0.equals(SecurityBindingPropertiesAssertion.AlgorithmSuite.BASIC128_RSA15)) {
         return new Basic128Rsa15AlgorithmSuiteInfoImpl();
      } else if (var0.equals(SecurityBindingPropertiesAssertion.AlgorithmSuite.BASIC256_SHA256)) {
         return new Basic256Sha256AlgorithmSuiteInfoImpl();
      } else if (var0.equals(SecurityBindingPropertiesAssertion.AlgorithmSuite.BASIC192_SHA256)) {
         return new Basic192Sha256AlgorithmSuiteInfoImpl();
      } else if (var0.equals(SecurityBindingPropertiesAssertion.AlgorithmSuite.BASIC128_SHA256)) {
         return new Basic128Sha256AlgorithmSuiteInfoImpl();
      } else if (var0.equals(SecurityBindingPropertiesAssertion.AlgorithmSuite.TRIPLEDES_SHA256)) {
         return new TripleDesSha256AlgorithmSuiteInfoImpl();
      } else if (var0.equals(SecurityBindingPropertiesAssertion.AlgorithmSuite.BASIC256_SHA256_RSA15)) {
         return new Basic256Sha256Rsa15AlgorithmSuiteInfoImpl();
      } else if (var0.equals(SecurityBindingPropertiesAssertion.AlgorithmSuite.BASIC192_SHA256_RSA15)) {
         return new Basic192Sha256Rsa15AlgorithmSuiteInfoImpl();
      } else if (var0.equals(SecurityBindingPropertiesAssertion.AlgorithmSuite.BASIC128_SHA256_RSA15)) {
         return new Basic128Sha256Rsa15AlgorithmSuiteInfoImpl();
      } else if (var0.equals(SecurityBindingPropertiesAssertion.AlgorithmSuite.TRIPLEDES_SHA256_RSA15)) {
         return new TripleDesSha256Rsa15AlgorithmSuiteInfoImpl();
      } else {
         if (verbose) {
            Verbose.log((Object)("Found an unidentified AlgorithmSuite " + var0 + " Name =" + var0.name()));
         }

         return new Basic256AlgorithmSuiteInfoImpl();
      }
   }

   private IntegrityAssertionImpl getIntegrityAssertionImplInstance() {
      return new IntegrityAssertionImpl();
   }

   private ConfidentialityAssertionImpl getConfidentialityAssertionImplInstance() {
      return new ConfidentialityAssertionImpl();
   }

   private SupportingTokensAssertionImpl getSupportingTokensAssertionInstance() {
      if (this.supportingTokens == null) {
         this.supportingTokens = new SupportingTokensAssertionImpl();
      }

      return this.supportingTokens;
   }

   public SymmetricBindingInfo getSymmetricBindingInfo() {
      return this.symmetricBinding;
   }

   public AsymmetricBindingInfo getAsymmetricBindingInfo() {
      return this.asymmetricBiding;
   }

   public SupportingTokensAssertion getSupportingTokensAssertion() {
      return this.supportingTokens;
   }

   public WsTrustOptions getWsTrustOptions() {
      return this.trustOptions;
   }

   public NormalizedExpression getWsTrustBootstrapPolicy() {
      if (this.symmetricBinding != null) {
         ProtectionTokenAssertion var1 = this.symmetricBinding.getProtectionTokenAssertion();
         if (var1 != null) {
            SecureConversationTokenAssertion var2 = var1.getSecureConversationTokenAssertion();
            if (var2 != null) {
               return var2.getNormalizedBootstrapPolicy();
            }
         }
      }

      if (this.supportingTokens != null) {
         ArrayList var4 = new ArrayList();
         var4.addAll(this.supportingTokens.getSupportingTokens());
         var4.addAll(this.supportingTokens.getSignedSupportingTokens());
         var4.addAll(this.supportingTokens.getEncryptedSupportingTokens());
         var4.addAll(this.supportingTokens.getSignedEncryptedSupportingTokens());
         var4.addAll(this.supportingTokens.getEndorsingSupportingTokens());
         var4.addAll(this.supportingTokens.getSignedEndorsingSupportingTokens());
         Iterator var5 = var4.iterator();

         while(var5.hasNext()) {
            TokenAssertion var3 = (TokenAssertion)var5.next();
            if (var3 instanceof SecureConversationTokenAssertion) {
               return ((SecureConversationTokenAssertion)var3).getNormalizedBootstrapPolicy();
            }
         }
      }

      return null;
   }

   public Wss10Options getWss10Options() {
      return this.wss10Options;
   }

   public Wss11Options getWss11Options() {
      return this.wss11Options;
   }

   public List<IntegrityAssertion> getIntegrityAssertions() {
      return this.integrityAssts;
   }

   public IntegrityAssertion getIntegrityAssertion() {
      return this.integrityAssts != null && this.integrityAssts.size() == 1 ? (IntegrityAssertion)this.integrityAssts.get(0) : null;
   }

   public ConfidentialityAssertion getConfidentialityAssertion() {
      return this.confidentialityAssts != null && this.confidentialityAssts.size() == 1 ? (ConfidentialityAssertion)this.confidentialityAssts.get(0) : null;
   }

   public List<ConfidentialityAssertion> getConfidentialityAssertions() {
      return this.confidentialityAssts;
   }

   public List<ProtectionAssertion> getProtectionAssertions() {
      return this.protectionAssts;
   }

   public AlgorithmSuiteInfo getAlgorithmSuiteInfo() {
      return this.algSuiteInfoImpl;
   }

   public boolean isMessageSecurityEnabled() {
      return this.hasMessageLevelSecurity;
   }

   public TransportBindingInfo getTransportBindingInfo() {
      return this.transportBinding;
   }

   public boolean hasUnidentifiedAssertion() {
      return this.ufo != null && this.ufo.size() > 0;
   }

   public List getUnidentifiedAssertions() {
      return this.ufo;
   }

   public String getNamespaceUri() {
      return this.namespaceUri;
   }
}
