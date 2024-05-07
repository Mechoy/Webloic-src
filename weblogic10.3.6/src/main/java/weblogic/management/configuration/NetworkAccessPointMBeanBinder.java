package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanAlreadyExistsException;
import weblogic.descriptor.DescriptorBean;
import weblogic.management.DistributedManagementException;
import weblogic.management.internal.mbean.SecurityReadOnlyMBeanBinder;
import weblogic.utils.codegen.AttributeBinder;

public class NetworkAccessPointMBeanBinder extends ConfigurationMBeanBinder implements AttributeBinder {
   private NetworkAccessPointMBeanImpl bean;

   protected NetworkAccessPointMBeanBinder(DescriptorBean var1) {
      super(var1);
      this.bean = (NetworkAccessPointMBeanImpl)var1;
   }

   public NetworkAccessPointMBeanBinder() {
      super(new NetworkAccessPointMBeanImpl());
      this.bean = (NetworkAccessPointMBeanImpl)this.getBean();
   }

   public AttributeBinder bindAttribute(String var1, Object var2) throws IllegalArgumentException {
      try {
         Object var3 = this;
         if (!(this instanceof SecurityReadOnlyMBeanBinder) && var2 != null && var2.toString().trim().length() == 0) {
            return this;
         } else {
            if (var1 != null) {
               if (var1.equals("AcceptBacklog")) {
                  try {
                     this.bean.setAcceptBacklog(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var45) {
                     System.out.println("Warning: multiple definitions with same name: " + var45.getMessage());
                  }
               } else if (var1.equals("ChannelWeight")) {
                  try {
                     this.bean.setChannelWeight(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var44) {
                     System.out.println("Warning: multiple definitions with same name: " + var44.getMessage());
                  }
               } else if (var1.equals("ClusterAddress")) {
                  try {
                     this.bean.setClusterAddress((String)var2);
                  } catch (BeanAlreadyExistsException var43) {
                     System.out.println("Warning: multiple definitions with same name: " + var43.getMessage());
                  }
               } else if (var1.equals("CompleteCOMMessageTimeout")) {
                  this.handleDeprecatedProperty("CompleteCOMMessageTimeout", "<unknown>");

                  try {
                     this.bean.setCompleteCOMMessageTimeout(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var42) {
                     System.out.println("Warning: multiple definitions with same name: " + var42.getMessage());
                  }
               } else if (var1.equals("CompleteHTTPMessageTimeout")) {
                  this.handleDeprecatedProperty("CompleteHTTPMessageTimeout", "<unknown>");

                  try {
                     this.bean.setCompleteHTTPMessageTimeout(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var41) {
                     System.out.println("Warning: multiple definitions with same name: " + var41.getMessage());
                  }
               } else if (var1.equals("CompleteIIOPMessageTimeout")) {
                  this.handleDeprecatedProperty("CompleteIIOPMessageTimeout", "<unknown>");

                  try {
                     this.bean.setCompleteIIOPMessageTimeout(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var40) {
                     System.out.println("Warning: multiple definitions with same name: " + var40.getMessage());
                  }
               } else if (var1.equals("CompleteMessageTimeout")) {
                  try {
                     this.bean.setCompleteMessageTimeout(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var39) {
                     System.out.println("Warning: multiple definitions with same name: " + var39.getMessage());
                  }
               } else if (var1.equals("CompleteT3MessageTimeout")) {
                  this.handleDeprecatedProperty("CompleteT3MessageTimeout", "<unknown>");

                  try {
                     this.bean.setCompleteT3MessageTimeout(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var38) {
                     System.out.println("Warning: multiple definitions with same name: " + var38.getMessage());
                  }
               } else if (var1.equals("ConnectTimeout")) {
                  try {
                     this.bean.setConnectTimeout(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var37) {
                     System.out.println("Warning: multiple definitions with same name: " + var37.getMessage());
                  }
               } else if (var1.equals("CustomPrivateKeyAlias")) {
                  try {
                     this.bean.setCustomPrivateKeyAlias((String)var2);
                  } catch (BeanAlreadyExistsException var36) {
                     System.out.println("Warning: multiple definitions with same name: " + var36.getMessage());
                  }
               } else if (var1.equals("CustomPrivateKeyPassPhrase")) {
                  try {
                     if (this.bean.isCustomPrivateKeyPassPhraseEncryptedSet()) {
                        throw new IllegalArgumentException("Encrypted attribute corresponding to CustomPrivateKeyPassPhrase [ NetworkAccessPointMBean ] is also present in the config file. For encrypted properties either encrypted or unencrypted attribute can be present in 8.x config.");
                     }

                     this.bean.setCustomPrivateKeyPassPhrase((String)var2);
                  } catch (BeanAlreadyExistsException var35) {
                     System.out.println("Warning: multiple definitions with same name: " + var35.getMessage());
                  }
               } else if (var1.equals("CustomPrivateKeyPassPhraseEncrypted")) {
                  if (this.bean.isCustomPrivateKeyPassPhraseEncryptedSet()) {
                     throw new IllegalArgumentException("Unencrypted attribute corresponding to CustomPrivateKeyPassPhraseEncrypted [ NetworkAccessPointMBean ] is also present in the config file. For encrypted properties either encrypted or unencrypted attribute can be present in 8.x config.");
                  }

                  this.bean.setCustomPrivateKeyPassPhraseEncryptedAsString((String)var2);
               } else if (var1.equals("CustomProperties")) {
                  this.bean.setCustomPropertiesAsString((String)var2);
               } else if (var1.equals("ExternalDNSName")) {
                  this.handleDeprecatedProperty("ExternalDNSName", "<unknown>");

                  try {
                     this.bean.setExternalDNSName((String)var2);
                  } catch (BeanAlreadyExistsException var34) {
                     System.out.println("Warning: multiple definitions with same name: " + var34.getMessage());
                  }
               } else if (var1.equals("IdleConnectionTimeout")) {
                  try {
                     this.bean.setIdleConnectionTimeout(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var33) {
                     System.out.println("Warning: multiple definitions with same name: " + var33.getMessage());
                  }
               } else if (var1.equals("IdleIIOPConnectionTimeout")) {
                  this.handleDeprecatedProperty("IdleIIOPConnectionTimeout", "<unknown>");

                  try {
                     this.bean.setIdleIIOPConnectionTimeout(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var32) {
                     System.out.println("Warning: multiple definitions with same name: " + var32.getMessage());
                  }
               } else if (var1.equals("ListenAddress")) {
                  try {
                     this.bean.setListenAddress((String)var2);
                  } catch (BeanAlreadyExistsException var31) {
                     System.out.println("Warning: multiple definitions with same name: " + var31.getMessage());
                  }
               } else if (var1.equals("ListenPort")) {
                  try {
                     this.bean.setListenPort(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var30) {
                     System.out.println("Warning: multiple definitions with same name: " + var30.getMessage());
                  }
               } else if (var1.equals("LoginTimeoutMillis")) {
                  try {
                     this.bean.setLoginTimeoutMillis(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var29) {
                     System.out.println("Warning: multiple definitions with same name: " + var29.getMessage());
                  }
               } else if (var1.equals("LoginTimeoutMillisSSL")) {
                  this.handleDeprecatedProperty("LoginTimeoutMillisSSL", "<unknown>");

                  try {
                     this.bean.setLoginTimeoutMillisSSL(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var28) {
                     System.out.println("Warning: multiple definitions with same name: " + var28.getMessage());
                  }
               } else if (var1.equals("MaxBackoffBetweenFailures")) {
                  try {
                     this.bean.setMaxBackoffBetweenFailures(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var27) {
                     System.out.println("Warning: multiple definitions with same name: " + var27.getMessage());
                  }
               } else if (var1.equals("MaxConnectedClients")) {
                  try {
                     this.bean.setMaxConnectedClients(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var26) {
                     System.out.println("Warning: multiple definitions with same name: " + var26.getMessage());
                  }
               } else if (var1.equals("MaxMessageSize")) {
                  try {
                     this.bean.setMaxMessageSize(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var25) {
                     System.out.println("Warning: multiple definitions with same name: " + var25.getMessage());
                  }
               } else if (var1.equals("Name")) {
                  try {
                     this.bean.setName((String)var2);
                  } catch (BeanAlreadyExistsException var24) {
                     System.out.println("Warning: multiple definitions with same name: " + var24.getMessage());
                  }
               } else {
                  if (var1.equals("OutboundPrivateKeyAlias")) {
                     throw new AssertionError("can't set read-only property OutboundPrivateKeyAlias");
                  }

                  if (var1.equals("OutboundPrivateKeyPassPhrase")) {
                     throw new AssertionError("can't set read-only property OutboundPrivateKeyPassPhrase");
                  }

                  if (var1.equals("PrivateKeyAlias")) {
                     throw new AssertionError("can't set read-only property PrivateKeyAlias");
                  }

                  if (var1.equals("PrivateKeyPassPhrase")) {
                     throw new AssertionError("can't set read-only property PrivateKeyPassPhrase");
                  }

                  if (var1.equals("Protocol")) {
                     try {
                        this.bean.setProtocol((String)var2);
                     } catch (BeanAlreadyExistsException var23) {
                        System.out.println("Warning: multiple definitions with same name: " + var23.getMessage());
                     }
                  } else if (var1.equals("ProxyAddress")) {
                     try {
                        this.bean.setProxyAddress((String)var2);
                     } catch (BeanAlreadyExistsException var22) {
                        System.out.println("Warning: multiple definitions with same name: " + var22.getMessage());
                     }
                  } else if (var1.equals("ProxyPort")) {
                     try {
                        this.bean.setProxyPort(Integer.valueOf((String)var2));
                     } catch (BeanAlreadyExistsException var21) {
                        System.out.println("Warning: multiple definitions with same name: " + var21.getMessage());
                     }
                  } else if (var1.equals("PublicAddress")) {
                     try {
                        this.bean.setPublicAddress((String)var2);
                     } catch (BeanAlreadyExistsException var20) {
                        System.out.println("Warning: multiple definitions with same name: " + var20.getMessage());
                     }
                  } else if (var1.equals("PublicPort")) {
                     try {
                        this.bean.setPublicPort(Integer.valueOf((String)var2));
                     } catch (BeanAlreadyExistsException var19) {
                        System.out.println("Warning: multiple definitions with same name: " + var19.getMessage());
                     }
                  } else if (var1.equals("SSLListenPort")) {
                     this.handleDeprecatedProperty("SSLListenPort", "<unknown>");

                     try {
                        this.bean.setSSLListenPort(Integer.valueOf((String)var2));
                     } catch (BeanAlreadyExistsException var18) {
                        System.out.println("Warning: multiple definitions with same name: " + var18.getMessage());
                     }
                  } else if (var1.equals("TimeoutConnectionWithPendingResponses")) {
                     try {
                        this.bean.setTimeoutConnectionWithPendingResponses(Boolean.valueOf((String)var2));
                     } catch (BeanAlreadyExistsException var17) {
                        System.out.println("Warning: multiple definitions with same name: " + var17.getMessage());
                     }
                  } else if (var1.equals("TunnelingClientPingSecs")) {
                     try {
                        this.bean.setTunnelingClientPingSecs(Integer.valueOf((String)var2));
                     } catch (BeanAlreadyExistsException var16) {
                        System.out.println("Warning: multiple definitions with same name: " + var16.getMessage());
                     }
                  } else if (var1.equals("TunnelingClientTimeoutSecs")) {
                     try {
                        this.bean.setTunnelingClientTimeoutSecs(Integer.valueOf((String)var2));
                     } catch (BeanAlreadyExistsException var15) {
                        System.out.println("Warning: multiple definitions with same name: " + var15.getMessage());
                     }
                  } else if (var1.equals("UseFastSerialization")) {
                     try {
                        this.bean.setUseFastSerialization(Boolean.valueOf((String)var2));
                     } catch (BeanAlreadyExistsException var14) {
                        System.out.println("Warning: multiple definitions with same name: " + var14.getMessage());
                     }
                  } else if (var1.equals("ChannelIdentityCustomized")) {
                     try {
                        this.bean.setChannelIdentityCustomized(Boolean.valueOf((String)var2));
                     } catch (BeanAlreadyExistsException var13) {
                        System.out.println("Warning: multiple definitions with same name: " + var13.getMessage());
                     }
                  } else if (var1.equals("ClientCertificateEnforced")) {
                     try {
                        this.bean.setClientCertificateEnforced(Boolean.valueOf((String)var2));
                     } catch (BeanAlreadyExistsException var12) {
                        System.out.println("Warning: multiple definitions with same name: " + var12.getMessage());
                     }
                  } else if (var1.equals("Enabled")) {
                     try {
                        this.bean.setEnabled(Boolean.valueOf((String)var2));
                     } catch (BeanAlreadyExistsException var11) {
                        System.out.println("Warning: multiple definitions with same name: " + var11.getMessage());
                     }
                  } else if (var1.equals("HttpEnabledForThisProtocol")) {
                     try {
                        this.bean.setHttpEnabledForThisProtocol(Boolean.valueOf((String)var2));
                     } catch (BeanAlreadyExistsException var10) {
                        System.out.println("Warning: multiple definitions with same name: " + var10.getMessage());
                     }
                  } else if (var1.equals("OutboundEnabled")) {
                     try {
                        this.bean.setOutboundEnabled(Boolean.valueOf((String)var2));
                     } catch (BeanAlreadyExistsException var9) {
                        System.out.println("Warning: multiple definitions with same name: " + var9.getMessage());
                     }
                  } else if (var1.equals("OutboundPrivateKeyEnabled")) {
                     try {
                        this.bean.setOutboundPrivateKeyEnabled(Boolean.valueOf((String)var2));
                     } catch (BeanAlreadyExistsException var8) {
                        System.out.println("Warning: multiple definitions with same name: " + var8.getMessage());
                     }
                  } else if (var1.equals("SDPEnabled")) {
                     try {
                        this.bean.setSDPEnabled(Boolean.valueOf((String)var2));
                     } catch (BeanAlreadyExistsException var7) {
                        System.out.println("Warning: multiple definitions with same name: " + var7.getMessage());
                     }
                  } else if (var1.equals("TunnelingEnabled")) {
                     try {
                        this.bean.setTunnelingEnabled(Boolean.valueOf((String)var2));
                     } catch (BeanAlreadyExistsException var6) {
                        System.out.println("Warning: multiple definitions with same name: " + var6.getMessage());
                     }
                  } else if (var1.equals("TwoWaySSLEnabled")) {
                     try {
                        this.bean.setTwoWaySSLEnabled(Boolean.valueOf((String)var2));
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
      } catch (ClassCastException var46) {
         System.out.println(var46 + " name: " + var1 + " class: " + var2.getClass().getName());
         throw var46;
      } catch (RuntimeException var47) {
         throw var47;
      } catch (Exception var48) {
         if (var48 instanceof DistributedManagementException) {
            throw new AssertionError("impossible exception: " + var48);
         } else if (var48 instanceof InvalidAttributeValueException) {
            throw new IllegalArgumentException(var48.getMessage());
         } else {
            throw new AssertionError("unexpected exception: " + var48);
         }
      }
   }
}
