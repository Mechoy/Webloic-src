package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanAlreadyExistsException;
import weblogic.descriptor.DescriptorBean;
import weblogic.management.DistributedManagementException;
import weblogic.management.internal.mbean.SecurityReadOnlyMBeanBinder;
import weblogic.utils.codegen.AttributeBinder;

public class SSLMBeanBinder extends ConfigurationMBeanBinder implements AttributeBinder {
   private SSLMBeanImpl bean;

   protected SSLMBeanBinder(DescriptorBean var1) {
      super(var1);
      this.bean = (SSLMBeanImpl)var1;
   }

   public SSLMBeanBinder() {
      super(new SSLMBeanImpl());
      this.bean = (SSLMBeanImpl)this.getBean();
   }

   public AttributeBinder bindAttribute(String var1, Object var2) throws IllegalArgumentException {
      try {
         Object var3 = this;
         if (!(this instanceof SecurityReadOnlyMBeanBinder) && var2 != null && var2.toString().trim().length() == 0) {
            return this;
         } else {
            if (var1 != null) {
               if (var1.equals("CertAuthenticator")) {
                  try {
                     this.bean.setCertAuthenticator((String)var2);
                  } catch (BeanAlreadyExistsException var35) {
                     System.out.println("Warning: multiple definitions with same name: " + var35.getMessage());
                  }
               } else if (var1.equals("CertificateCacheSize")) {
                  try {
                     this.bean.setCertificateCacheSize(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var34) {
                     System.out.println("Warning: multiple definitions with same name: " + var34.getMessage());
                  }
               } else if (var1.equals("Ciphersuites")) {
                  try {
                     this.bean.setCiphersuites(this.parseStringArrayInitializer((String)var2));
                  } catch (BeanAlreadyExistsException var33) {
                     System.out.println("Warning: multiple definitions with same name: " + var33.getMessage());
                  }
               } else if (var1.equals("ClientCertAlias")) {
                  try {
                     this.bean.setClientCertAlias((String)var2);
                  } catch (BeanAlreadyExistsException var32) {
                     System.out.println("Warning: multiple definitions with same name: " + var32.getMessage());
                  }
               } else if (var1.equals("ClientCertPrivateKeyPassPhrase")) {
                  try {
                     if (this.bean.isClientCertPrivateKeyPassPhraseEncryptedSet()) {
                        throw new IllegalArgumentException("Encrypted attribute corresponding to ClientCertPrivateKeyPassPhrase [ SSLMBean ] is also present in the config file. For encrypted properties either encrypted or unencrypted attribute can be present in 8.x config.");
                     }

                     this.bean.setClientCertPrivateKeyPassPhrase((String)var2);
                  } catch (BeanAlreadyExistsException var31) {
                     System.out.println("Warning: multiple definitions with same name: " + var31.getMessage());
                  }
               } else if (var1.equals("ClientCertPrivateKeyPassPhraseEncrypted")) {
                  if (this.bean.isClientCertPrivateKeyPassPhraseEncryptedSet()) {
                     throw new IllegalArgumentException("Unencrypted attribute corresponding to ClientCertPrivateKeyPassPhraseEncrypted [ SSLMBean ] is also present in the config file. For encrypted properties either encrypted or unencrypted attribute can be present in 8.x config.");
                  }

                  this.bean.setClientCertPrivateKeyPassPhraseEncryptedAsString((String)var2);
               } else if (var1.equals("ExportKeyLifespan")) {
                  try {
                     this.bean.setExportKeyLifespan(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var30) {
                     System.out.println("Warning: multiple definitions with same name: " + var30.getMessage());
                  }
               } else if (var1.equals("HostnameVerifier")) {
                  try {
                     this.bean.setHostnameVerifier((String)var2);
                  } catch (BeanAlreadyExistsException var29) {
                     System.out.println("Warning: multiple definitions with same name: " + var29.getMessage());
                  }
               } else if (var1.equals("IdentityAndTrustLocations")) {
                  try {
                     this.bean.setIdentityAndTrustLocations((String)var2);
                  } catch (BeanAlreadyExistsException var28) {
                     System.out.println("Warning: multiple definitions with same name: " + var28.getMessage());
                  }
               } else if (var1.equals("InboundCertificateValidation")) {
                  try {
                     this.bean.setInboundCertificateValidation((String)var2);
                  } catch (BeanAlreadyExistsException var27) {
                     System.out.println("Warning: multiple definitions with same name: " + var27.getMessage());
                  }
               } else if (var1.equals("ListenPort")) {
                  try {
                     this.bean.setListenPort(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var26) {
                     System.out.println("Warning: multiple definitions with same name: " + var26.getMessage());
                  }
               } else if (var1.equals("LoginTimeoutMillis")) {
                  try {
                     this.bean.setLoginTimeoutMillis(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var25) {
                     System.out.println("Warning: multiple definitions with same name: " + var25.getMessage());
                  }
               } else if (var1.equals("Name")) {
                  try {
                     this.bean.setName((String)var2);
                  } catch (BeanAlreadyExistsException var24) {
                     System.out.println("Warning: multiple definitions with same name: " + var24.getMessage());
                  }
               } else if (var1.equals("OutboundCertificateValidation")) {
                  try {
                     this.bean.setOutboundCertificateValidation((String)var2);
                  } catch (BeanAlreadyExistsException var23) {
                     System.out.println("Warning: multiple definitions with same name: " + var23.getMessage());
                  }
               } else {
                  if (var1.equals("OutboundPrivateKeyAlias")) {
                     throw new AssertionError("can't set read-only property OutboundPrivateKeyAlias");
                  }

                  if (var1.equals("OutboundPrivateKeyPassPhrase")) {
                     throw new AssertionError("can't set read-only property OutboundPrivateKeyPassPhrase");
                  }

                  if (var1.equals("ServerCertificateChainFileName")) {
                     this.handleDeprecatedProperty("ServerCertificateChainFileName", "7.0.0.0 server certificates (and chains) should be stored in keystores.");

                     try {
                        this.bean.setServerCertificateChainFileName((String)var2);
                     } catch (BeanAlreadyExistsException var22) {
                        System.out.println("Warning: multiple definitions with same name: " + var22.getMessage());
                     }
                  } else if (var1.equals("ServerCertificateFileName")) {
                     this.handleDeprecatedProperty("ServerCertificateFileName", "8.1.0.0 server certificates (and chains) should be stored in keystores.");

                     try {
                        this.bean.setServerCertificateFileName((String)var2);
                     } catch (BeanAlreadyExistsException var21) {
                        System.out.println("Warning: multiple definitions with same name: " + var21.getMessage());
                     }
                  } else if (var1.equals("ServerKeyFileName")) {
                     this.handleDeprecatedProperty("ServerKeyFileName", "8.1.0.0 private keys should be stored in keystores.");

                     try {
                        this.bean.setServerKeyFileName((String)var2);
                     } catch (BeanAlreadyExistsException var20) {
                        System.out.println("Warning: multiple definitions with same name: " + var20.getMessage());
                     }
                  } else if (var1.equals("ServerPrivateKeyAlias")) {
                     try {
                        this.bean.setServerPrivateKeyAlias((String)var2);
                     } catch (BeanAlreadyExistsException var19) {
                        System.out.println("Warning: multiple definitions with same name: " + var19.getMessage());
                     }
                  } else if (var1.equals("ServerPrivateKeyPassPhrase")) {
                     try {
                        if (this.bean.isServerPrivateKeyPassPhraseEncryptedSet()) {
                           throw new IllegalArgumentException("Encrypted attribute corresponding to ServerPrivateKeyPassPhrase [ SSLMBean ] is also present in the config file. For encrypted properties either encrypted or unencrypted attribute can be present in 8.x config.");
                        }

                        this.bean.setServerPrivateKeyPassPhrase((String)var2);
                     } catch (BeanAlreadyExistsException var18) {
                        System.out.println("Warning: multiple definitions with same name: " + var18.getMessage());
                     }
                  } else if (var1.equals("ServerPrivateKeyPassPhraseEncrypted")) {
                     if (this.bean.isServerPrivateKeyPassPhraseEncryptedSet()) {
                        throw new IllegalArgumentException("Unencrypted attribute corresponding to ServerPrivateKeyPassPhraseEncrypted [ SSLMBean ] is also present in the config file. For encrypted properties either encrypted or unencrypted attribute can be present in 8.x config.");
                     }

                     this.bean.setServerPrivateKeyPassPhraseEncryptedAsString((String)var2);
                  } else if (var1.equals("TrustedCAFileName")) {
                     this.handleDeprecatedProperty("TrustedCAFileName", "8.1.0.0 trusted CAs should be stored in keystores.");

                     try {
                        this.bean.setTrustedCAFileName((String)var2);
                     } catch (BeanAlreadyExistsException var17) {
                        System.out.println("Warning: multiple definitions with same name: " + var17.getMessage());
                     }
                  } else if (var1.equals("AllowUnencryptedNullCipher")) {
                     try {
                        this.bean.setAllowUnencryptedNullCipher(Boolean.valueOf((String)var2));
                     } catch (BeanAlreadyExistsException var16) {
                        System.out.println("Warning: multiple definitions with same name: " + var16.getMessage());
                     }
                  } else if (var1.equals("ClientCertificateEnforced")) {
                     try {
                        this.bean.setClientCertificateEnforced(Boolean.valueOf((String)var2));
                     } catch (BeanAlreadyExistsException var15) {
                        System.out.println("Warning: multiple definitions with same name: " + var15.getMessage());
                     }
                  } else if (var1.equals("Enabled")) {
                     try {
                        this.bean.setEnabled(Boolean.valueOf((String)var2));
                     } catch (BeanAlreadyExistsException var14) {
                        System.out.println("Warning: multiple definitions with same name: " + var14.getMessage());
                     }
                  } else if (var1.equals("HandlerEnabled")) {
                     try {
                        this.bean.setHandlerEnabled(Boolean.valueOf((String)var2));
                     } catch (BeanAlreadyExistsException var13) {
                        System.out.println("Warning: multiple definitions with same name: " + var13.getMessage());
                     }
                  } else if (var1.equals("HostnameVerificationIgnored")) {
                     try {
                        this.bean.setHostnameVerificationIgnored(Boolean.valueOf((String)var2));
                     } catch (BeanAlreadyExistsException var12) {
                        System.out.println("Warning: multiple definitions with same name: " + var12.getMessage());
                     }
                  } else if (var1.equals("JSSEEnabled")) {
                     try {
                        this.bean.setJSSEEnabled(Boolean.valueOf((String)var2));
                     } catch (BeanAlreadyExistsException var11) {
                        System.out.println("Warning: multiple definitions with same name: " + var11.getMessage());
                     }
                  } else if (var1.equals("KeyEncrypted")) {
                     try {
                        this.bean.setKeyEncrypted(Boolean.valueOf((String)var2));
                     } catch (BeanAlreadyExistsException var10) {
                        System.out.println("Warning: multiple definitions with same name: " + var10.getMessage());
                     }
                  } else if (var1.equals("SSLRejectionLoggingEnabled")) {
                     try {
                        this.bean.setSSLRejectionLoggingEnabled(Boolean.valueOf((String)var2));
                     } catch (BeanAlreadyExistsException var9) {
                        System.out.println("Warning: multiple definitions with same name: " + var9.getMessage());
                     }
                  } else if (var1.equals("TwoWaySSLEnabled")) {
                     try {
                        this.bean.setTwoWaySSLEnabled(Boolean.valueOf((String)var2));
                     } catch (BeanAlreadyExistsException var8) {
                        System.out.println("Warning: multiple definitions with same name: " + var8.getMessage());
                     }
                  } else if (var1.equals("UseClientCertForOutbound")) {
                     try {
                        this.bean.setUseClientCertForOutbound(Boolean.valueOf((String)var2));
                     } catch (BeanAlreadyExistsException var7) {
                        System.out.println("Warning: multiple definitions with same name: " + var7.getMessage());
                     }
                  } else if (var1.equals("UseJava")) {
                     try {
                        this.bean.setUseJava(Boolean.valueOf((String)var2));
                     } catch (BeanAlreadyExistsException var6) {
                        System.out.println("Warning: multiple definitions with same name: " + var6.getMessage());
                     }
                  } else if (var1.equals("UseServerCerts")) {
                     try {
                        this.bean.setUseServerCerts(Boolean.valueOf((String)var2));
                     } catch (BeanAlreadyExistsException var5) {
                        System.out.println("Warning: multiple definitions with same name: " + var5.getMessage());
                     }
                  } else {
                     var3 = super.bindAttribute(var1, var2);
                  }
               }
            }

            return (AttributeBinder)var3;
         }
      } catch (ClassCastException var36) {
         System.out.println(var36 + " name: " + var1 + " class: " + var2.getClass().getName());
         throw var36;
      } catch (RuntimeException var37) {
         throw var37;
      } catch (Exception var38) {
         if (var38 instanceof DistributedManagementException) {
            throw new AssertionError("impossible exception: " + var38);
         } else if (var38 instanceof InvalidAttributeValueException) {
            throw new IllegalArgumentException(var38.getMessage());
         } else {
            throw new AssertionError("unexpected exception: " + var38);
         }
      }
   }
}
