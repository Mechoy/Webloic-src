package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanAlreadyExistsException;
import weblogic.descriptor.DescriptorBean;
import weblogic.management.DistributedManagementException;
import weblogic.management.internal.mbean.SecurityReadOnlyMBeanBinder;
import weblogic.utils.codegen.AttributeBinder;

public class NetworkChannelMBeanBinder extends DeploymentMBeanBinder implements AttributeBinder {
   private NetworkChannelMBeanImpl bean;

   protected NetworkChannelMBeanBinder(DescriptorBean var1) {
      super(var1);
      this.bean = (NetworkChannelMBeanImpl)var1;
   }

   public NetworkChannelMBeanBinder() {
      super(new NetworkChannelMBeanImpl());
      this.bean = (NetworkChannelMBeanImpl)this.getBean();
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
                  } catch (BeanAlreadyExistsException var37) {
                     System.out.println("Warning: multiple definitions with same name: " + var37.getMessage());
                  }
               } else if (var1.equals("ChannelWeight")) {
                  try {
                     this.bean.setChannelWeight(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var36) {
                     System.out.println("Warning: multiple definitions with same name: " + var36.getMessage());
                  }
               } else if (var1.equals("ClusterAddress")) {
                  try {
                     this.bean.setClusterAddress((String)var2);
                  } catch (BeanAlreadyExistsException var35) {
                     System.out.println("Warning: multiple definitions with same name: " + var35.getMessage());
                  }
               } else if (var1.equals("CompleteCOMMessageTimeout")) {
                  try {
                     this.bean.setCompleteCOMMessageTimeout(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var34) {
                     System.out.println("Warning: multiple definitions with same name: " + var34.getMessage());
                  }
               } else if (var1.equals("CompleteHTTPMessageTimeout")) {
                  try {
                     this.bean.setCompleteHTTPMessageTimeout(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var33) {
                     System.out.println("Warning: multiple definitions with same name: " + var33.getMessage());
                  }
               } else if (var1.equals("CompleteIIOPMessageTimeout")) {
                  try {
                     this.bean.setCompleteIIOPMessageTimeout(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var32) {
                     System.out.println("Warning: multiple definitions with same name: " + var32.getMessage());
                  }
               } else if (var1.equals("CompleteT3MessageTimeout")) {
                  try {
                     this.bean.setCompleteT3MessageTimeout(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var31) {
                     System.out.println("Warning: multiple definitions with same name: " + var31.getMessage());
                  }
               } else if (var1.equals("DefaultIIOPPassword")) {
                  try {
                     if (this.bean.isDefaultIIOPPasswordEncryptedSet()) {
                        throw new IllegalArgumentException("Encrypted attribute corresponding to DefaultIIOPPassword [ NetworkChannelMBean ] is also present in the config file. For encrypted properties either encrypted or unencrypted attribute can be present in 8.x config.");
                     }

                     this.bean.setDefaultIIOPPassword((String)var2);
                  } catch (BeanAlreadyExistsException var30) {
                     System.out.println("Warning: multiple definitions with same name: " + var30.getMessage());
                  }
               } else if (var1.equals("DefaultIIOPPasswordEncrypted")) {
                  if (this.bean.isDefaultIIOPPasswordEncryptedSet()) {
                     throw new IllegalArgumentException("Unencrypted attribute corresponding to DefaultIIOPPasswordEncrypted [ NetworkChannelMBean ] is also present in the config file. For encrypted properties either encrypted or unencrypted attribute can be present in 8.x config.");
                  }

                  this.bean.setDefaultIIOPPasswordEncryptedAsString((String)var2);
               } else if (var1.equals("DefaultIIOPUser")) {
                  try {
                     this.bean.setDefaultIIOPUser((String)var2);
                  } catch (BeanAlreadyExistsException var29) {
                     System.out.println("Warning: multiple definitions with same name: " + var29.getMessage());
                  }
               } else if (var1.equals("Description")) {
                  try {
                     this.bean.setDescription((String)var2);
                  } catch (BeanAlreadyExistsException var28) {
                     System.out.println("Warning: multiple definitions with same name: " + var28.getMessage());
                  }
               } else if (var1.equals("IdleIIOPConnectionTimeout")) {
                  try {
                     this.bean.setIdleIIOPConnectionTimeout(Integer.valueOf((String)var2));
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
               } else if (var1.equals("LoginTimeoutMillisSSL")) {
                  try {
                     this.bean.setLoginTimeoutMillisSSL(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var24) {
                     System.out.println("Warning: multiple definitions with same name: " + var24.getMessage());
                  }
               } else if (var1.equals("MaxCOMMessageSize")) {
                  try {
                     this.bean.setMaxCOMMessageSize(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var23) {
                     System.out.println("Warning: multiple definitions with same name: " + var23.getMessage());
                  }
               } else if (var1.equals("MaxHTTPMessageSize")) {
                  try {
                     this.bean.setMaxHTTPMessageSize(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var22) {
                     System.out.println("Warning: multiple definitions with same name: " + var22.getMessage());
                  }
               } else if (var1.equals("MaxIIOPMessageSize")) {
                  try {
                     this.bean.setMaxIIOPMessageSize(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var21) {
                     System.out.println("Warning: multiple definitions with same name: " + var21.getMessage());
                  }
               } else if (var1.equals("MaxT3MessageSize")) {
                  try {
                     this.bean.setMaxT3MessageSize(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var20) {
                     System.out.println("Warning: multiple definitions with same name: " + var20.getMessage());
                  }
               } else if (var1.equals("Name")) {
                  try {
                     this.bean.setName((String)var2);
                  } catch (BeanAlreadyExistsException var19) {
                     System.out.println("Warning: multiple definitions with same name: " + var19.getMessage());
                  }
               } else if (var1.equals("SSLListenPort")) {
                  try {
                     this.bean.setSSLListenPort(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var18) {
                     System.out.println("Warning: multiple definitions with same name: " + var18.getMessage());
                  }
               } else if (var1.equals("TunnelingClientPingSecs")) {
                  try {
                     this.bean.setTunnelingClientPingSecs(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var17) {
                     System.out.println("Warning: multiple definitions with same name: " + var17.getMessage());
                  }
               } else if (var1.equals("TunnelingClientTimeoutSecs")) {
                  try {
                     this.bean.setTunnelingClientTimeoutSecs(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var16) {
                     System.out.println("Warning: multiple definitions with same name: " + var16.getMessage());
                  }
               } else {
                  if (var1.equals("BoundOutgoingEnabled")) {
                     throw new AssertionError("can't set read-only property BoundOutgoingEnabled");
                  }

                  if (var1.equals("COMEnabled")) {
                     try {
                        this.bean.setCOMEnabled(Boolean.valueOf((String)var2));
                     } catch (BeanAlreadyExistsException var15) {
                        System.out.println("Warning: multiple definitions with same name: " + var15.getMessage());
                     }
                  } else if (var1.equals("HTTPEnabled")) {
                     try {
                        this.bean.setHTTPEnabled(Boolean.valueOf((String)var2));
                     } catch (BeanAlreadyExistsException var14) {
                        System.out.println("Warning: multiple definitions with same name: " + var14.getMessage());
                     }
                  } else if (var1.equals("HTTPSEnabled")) {
                     try {
                        this.bean.setHTTPSEnabled(Boolean.valueOf((String)var2));
                     } catch (BeanAlreadyExistsException var13) {
                        System.out.println("Warning: multiple definitions with same name: " + var13.getMessage());
                     }
                  } else if (var1.equals("IIOPEnabled")) {
                     try {
                        this.bean.setIIOPEnabled(Boolean.valueOf((String)var2));
                     } catch (BeanAlreadyExistsException var12) {
                        System.out.println("Warning: multiple definitions with same name: " + var12.getMessage());
                     }
                  } else if (var1.equals("IIOPSEnabled")) {
                     try {
                        this.bean.setIIOPSEnabled(Boolean.valueOf((String)var2));
                     } catch (BeanAlreadyExistsException var11) {
                        System.out.println("Warning: multiple definitions with same name: " + var11.getMessage());
                     }
                  } else if (var1.equals("ListenPortEnabled")) {
                     try {
                        this.bean.setListenPortEnabled(Boolean.valueOf((String)var2));
                     } catch (BeanAlreadyExistsException var10) {
                        System.out.println("Warning: multiple definitions with same name: " + var10.getMessage());
                     }
                  } else if (var1.equals("OutgoingEnabled")) {
                     try {
                        this.bean.setOutgoingEnabled(Boolean.valueOf((String)var2));
                     } catch (BeanAlreadyExistsException var9) {
                        System.out.println("Warning: multiple definitions with same name: " + var9.getMessage());
                     }
                  } else if (var1.equals("SSLListenPortEnabled")) {
                     try {
                        this.bean.setSSLListenPortEnabled(Boolean.valueOf((String)var2));
                     } catch (BeanAlreadyExistsException var8) {
                        System.out.println("Warning: multiple definitions with same name: " + var8.getMessage());
                     }
                  } else if (var1.equals("T3Enabled")) {
                     try {
                        this.bean.setT3Enabled(Boolean.valueOf((String)var2));
                     } catch (BeanAlreadyExistsException var7) {
                        System.out.println("Warning: multiple definitions with same name: " + var7.getMessage());
                     }
                  } else if (var1.equals("T3SEnabled")) {
                     try {
                        this.bean.setT3SEnabled(Boolean.valueOf((String)var2));
                     } catch (BeanAlreadyExistsException var6) {
                        System.out.println("Warning: multiple definitions with same name: " + var6.getMessage());
                     }
                  } else if (var1.equals("TunnelingEnabled")) {
                     try {
                        this.bean.setTunnelingEnabled(Boolean.valueOf((String)var2));
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
      } catch (ClassCastException var38) {
         System.out.println(var38 + " name: " + var1 + " class: " + var2.getClass().getName());
         throw var38;
      } catch (RuntimeException var39) {
         throw var39;
      } catch (Exception var40) {
         if (var40 instanceof DistributedManagementException) {
            throw new AssertionError("impossible exception: " + var40);
         } else if (var40 instanceof InvalidAttributeValueException) {
            throw new IllegalArgumentException(var40.getMessage());
         } else {
            throw new AssertionError("unexpected exception: " + var40);
         }
      }
   }
}
