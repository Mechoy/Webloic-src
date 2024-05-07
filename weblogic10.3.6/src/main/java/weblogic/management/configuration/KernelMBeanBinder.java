package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanAlreadyExistsException;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.management.DistributedManagementException;
import weblogic.management.internal.mbean.ReadOnlyMBeanBinder;
import weblogic.management.internal.mbean.SecurityReadOnlyMBeanBinder;
import weblogic.utils.codegen.AttributeBinder;

public class KernelMBeanBinder extends ConfigurationMBeanBinder implements AttributeBinder {
   private KernelMBeanImpl bean;

   protected KernelMBeanBinder(DescriptorBean var1) {
      super(var1);
      this.bean = (KernelMBeanImpl)var1;
   }

   public KernelMBeanBinder() {
      super(new KernelMBeanImpl());
      this.bean = (KernelMBeanImpl)this.getBean();
   }

   public AttributeBinder bindAttribute(String var1, Object var2) throws IllegalArgumentException {
      try {
         Object var3 = this;
         if (!(this instanceof SecurityReadOnlyMBeanBinder) && var2 != null && var2.toString().trim().length() == 0) {
            return this;
         } else {
            if (var1 != null) {
               if (var1.equals("AdministrationProtocol")) {
                  try {
                     this.bean.setAdministrationProtocol((String)var2);
                  } catch (BeanAlreadyExistsException var73) {
                     System.out.println("Warning: multiple definitions with same name: " + var73.getMessage());
                  }
               } else if (var1.equals("CompleteCOMMessageTimeout")) {
                  this.handleDeprecatedProperty("CompleteCOMMessageTimeout", "8.1.0.0 use {@link NetworkAccessPointMBean#getCompleteMessageTimeout()}");

                  try {
                     this.bean.setCompleteCOMMessageTimeout(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var72) {
                     System.out.println("Warning: multiple definitions with same name: " + var72.getMessage());
                  }
               } else if (var1.equals("CompleteHTTPMessageTimeout")) {
                  this.handleDeprecatedProperty("CompleteHTTPMessageTimeout", "8.1.0.0 use {@link NetworkAccessPointMBean#getCompleteMessageTimeout()}");

                  try {
                     this.bean.setCompleteHTTPMessageTimeout(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var71) {
                     System.out.println("Warning: multiple definitions with same name: " + var71.getMessage());
                  }
               } else if (var1.equals("CompleteIIOPMessageTimeout")) {
                  this.handleDeprecatedProperty("CompleteIIOPMessageTimeout", "8.1.0.0 use {@link NetworkAccessPointMBean#getCompleteMessageTimeout()}");

                  try {
                     this.bean.setCompleteIIOPMessageTimeout(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var70) {
                     System.out.println("Warning: multiple definitions with same name: " + var70.getMessage());
                  }
               } else if (var1.equals("CompleteMessageTimeout")) {
                  try {
                     this.bean.setCompleteMessageTimeout(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var69) {
                     System.out.println("Warning: multiple definitions with same name: " + var69.getMessage());
                  }
               } else if (var1.equals("CompleteT3MessageTimeout")) {
                  this.handleDeprecatedProperty("CompleteT3MessageTimeout", "8.1.0.0 use {@link NetworkAccessPointMBean#getCompleteMessageTimeout()}");

                  try {
                     this.bean.setCompleteT3MessageTimeout(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var68) {
                     System.out.println("Warning: multiple definitions with same name: " + var68.getMessage());
                  }
               } else if (var1.equals("ConnectTimeout")) {
                  try {
                     this.bean.setConnectTimeout(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var67) {
                     System.out.println("Warning: multiple definitions with same name: " + var67.getMessage());
                  }
               } else if (var1.equals("DGCIdlePeriodsUntilTimeout")) {
                  try {
                     this.bean.setDGCIdlePeriodsUntilTimeout(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var66) {
                     System.out.println("Warning: multiple definitions with same name: " + var66.getMessage());
                  }
               } else if (var1.equals("DefaultGIOPMinorVersion")) {
                  this.handleDeprecatedProperty("DefaultGIOPMinorVersion", "<unknown>");

                  try {
                     this.bean.setDefaultGIOPMinorVersion(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var65) {
                     System.out.println("Warning: multiple definitions with same name: " + var65.getMessage());
                  }
               } else if (var1.equals("DefaultProtocol")) {
                  try {
                     this.bean.setDefaultProtocol((String)var2);
                  } catch (BeanAlreadyExistsException var64) {
                     System.out.println("Warning: multiple definitions with same name: " + var64.getMessage());
                  }
               } else if (var1.equals("DefaultSecureProtocol")) {
                  try {
                     this.bean.setDefaultSecureProtocol((String)var2);
                  } catch (BeanAlreadyExistsException var63) {
                     System.out.println("Warning: multiple definitions with same name: " + var63.getMessage());
                  }
               } else if (var1.equals("ExecuteQueue")) {
                  try {
                     this.bean.addExecuteQueue((ExecuteQueueMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var62) {
                     System.out.println("Warning: multiple definitions with same name: " + var62.getMessage());
                     this.bean.removeExecuteQueue((ExecuteQueueMBean)var62.getExistingBean());
                     this.bean.addExecuteQueue((ExecuteQueueMBean)((AbstractDescriptorBean)((ExecuteQueueMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                  }
               } else if (var1.equals("IIOP")) {
                  try {
                     this.bean.setIIOP((IIOPMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var61) {
                     System.out.println("Warning: multiple definitions with same name: " + var61.getMessage());
                  }
               } else if (var1.equals("IIOPLocationForwardPolicy")) {
                  this.handleDeprecatedProperty("IIOPLocationForwardPolicy", "<unknown>");

                  try {
                     this.bean.setIIOPLocationForwardPolicy((String)var2);
                  } catch (BeanAlreadyExistsException var60) {
                     System.out.println("Warning: multiple definitions with same name: " + var60.getMessage());
                  }
               } else if (var1.equals("IIOPTxMechanism")) {
                  this.handleDeprecatedProperty("IIOPTxMechanism", "8.1.0.0 use {@link IIOPMBean#getTxMechanism()}");

                  try {
                     this.bean.setIIOPTxMechanism((String)var2);
                  } catch (BeanAlreadyExistsException var59) {
                     System.out.println("Warning: multiple definitions with same name: " + var59.getMessage());
                  }
               } else if (var1.equals("IdleConnectionTimeout")) {
                  try {
                     this.bean.setIdleConnectionTimeout(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var58) {
                     System.out.println("Warning: multiple definitions with same name: " + var58.getMessage());
                  }
               } else if (var1.equals("IdleIIOPConnectionTimeout")) {
                  this.handleDeprecatedProperty("IdleIIOPConnectionTimeout", "8.1.0.0 use {@link NetworkAccessPointMBean#getIdleConnectionTimeout()}");

                  try {
                     this.bean.setIdleIIOPConnectionTimeout(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var57) {
                     System.out.println("Warning: multiple definitions with same name: " + var57.getMessage());
                  }
               } else if (var1.equals("IdlePeriodsUntilTimeout")) {
                  try {
                     this.bean.setIdlePeriodsUntilTimeout(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var56) {
                     System.out.println("Warning: multiple definitions with same name: " + var56.getMessage());
                  }
               } else if (var1.equals("JMSThreadPoolSize")) {
                  try {
                     this.bean.setJMSThreadPoolSize(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var55) {
                     System.out.println("Warning: multiple definitions with same name: " + var55.getMessage());
                  }
               } else if (var1.equals("LoadStubUsingContextClassLoader")) {
                  try {
                     this.bean.setLoadStubUsingContextClassLoader(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var54) {
                     System.out.println("Warning: multiple definitions with same name: " + var54.getMessage());
                  }
               } else if (var1.equals("Log")) {
                  try {
                     this.bean.setLog((LogMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var53) {
                     System.out.println("Warning: multiple definitions with same name: " + var53.getMessage());
                  }
               } else if (var1.equals("MTUSize")) {
                  try {
                     this.bean.setMTUSize(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var52) {
                     System.out.println("Warning: multiple definitions with same name: " + var52.getMessage());
                  }
               } else if (var1.equals("MaxCOMMessageSize")) {
                  this.handleDeprecatedProperty("MaxCOMMessageSize", "8.1.0.0 use {@link NetworkAccessPointMBean#getMaxMessageSize()}");

                  try {
                     this.bean.setMaxCOMMessageSize(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var51) {
                     System.out.println("Warning: multiple definitions with same name: " + var51.getMessage());
                  }
               } else if (var1.equals("MaxHTTPMessageSize")) {
                  this.handleDeprecatedProperty("MaxHTTPMessageSize", "8.1.0.0 use {@link NetworkAccessPointMBean#getMaxMessageSize()}");

                  try {
                     this.bean.setMaxHTTPMessageSize(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var50) {
                     System.out.println("Warning: multiple definitions with same name: " + var50.getMessage());
                  }
               } else if (var1.equals("MaxIIOPMessageSize")) {
                  this.handleDeprecatedProperty("MaxIIOPMessageSize", "8.1.0.0 use {@link NetworkAccessPointMBean#getMaxMessageSize()}");

                  try {
                     this.bean.setMaxIIOPMessageSize(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var49) {
                     System.out.println("Warning: multiple definitions with same name: " + var49.getMessage());
                  }
               } else if (var1.equals("MaxMessageSize")) {
                  try {
                     this.bean.setMaxMessageSize(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var48) {
                     System.out.println("Warning: multiple definitions with same name: " + var48.getMessage());
                  }
               } else if (var1.equals("MaxOpenSockCount")) {
                  try {
                     this.bean.setMaxOpenSockCount(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var47) {
                     System.out.println("Warning: multiple definitions with same name: " + var47.getMessage());
                  }
               } else if (var1.equals("MaxT3MessageSize")) {
                  this.handleDeprecatedProperty("MaxT3MessageSize", "8.1.0.0 use {@link NetworkAccessPointMBean#getMaxMessageSize()}");

                  try {
                     this.bean.setMaxT3MessageSize(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var46) {
                     System.out.println("Warning: multiple definitions with same name: " + var46.getMessage());
                  }
               } else if (var1.equals("MessagingBridgeThreadPoolSize")) {
                  this.handleDeprecatedProperty("MessagingBridgeThreadPoolSize", "9.0.0.0 replaced by a Work Manager named weblogic.jms.MessagingBridge");

                  try {
                     this.bean.setMessagingBridgeThreadPoolSize(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var45) {
                     System.out.println("Warning: multiple definitions with same name: " + var45.getMessage());
                  }
               } else if (var1.equals("MuxerClass")) {
                  try {
                     this.bean.setMuxerClass((String)var2);
                  } catch (BeanAlreadyExistsException var44) {
                     System.out.println("Warning: multiple definitions with same name: " + var44.getMessage());
                  }
               } else if (var1.equals("Name")) {
                  try {
                     this.bean.setName((String)var2);
                  } catch (BeanAlreadyExistsException var43) {
                     System.out.println("Warning: multiple definitions with same name: " + var43.getMessage());
                  }
               } else if (var1.equals("PeriodLength")) {
                  try {
                     this.bean.setPeriodLength(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var42) {
                     System.out.println("Warning: multiple definitions with same name: " + var42.getMessage());
                  }
               } else if (var1.equals("RefreshClientRuntimeDescriptor")) {
                  try {
                     this.bean.setRefreshClientRuntimeDescriptor(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var41) {
                     System.out.println("Warning: multiple definitions with same name: " + var41.getMessage());
                  }
               } else if (var1.equals("ResponseTimeout")) {
                  try {
                     this.bean.setResponseTimeout(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var40) {
                     System.out.println("Warning: multiple definitions with same name: " + var40.getMessage());
                  }
               } else if (var1.equals("RjvmIdleTimeout")) {
                  try {
                     this.bean.setRjvmIdleTimeout(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var39) {
                     System.out.println("Warning: multiple definitions with same name: " + var39.getMessage());
                  }
               } else if (var1.equals("SSL")) {
                  try {
                     this.bean.setSSL((SSLMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var38) {
                     System.out.println("Warning: multiple definitions with same name: " + var38.getMessage());
                  }
               } else if (var1.equals("SelfTuningThreadPoolSizeMax")) {
                  try {
                     this.bean.setSelfTuningThreadPoolSizeMax(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var37) {
                     System.out.println("Warning: multiple definitions with same name: " + var37.getMessage());
                  }
               } else if (var1.equals("SelfTuningThreadPoolSizeMin")) {
                  try {
                     this.bean.setSelfTuningThreadPoolSizeMin(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var36) {
                     System.out.println("Warning: multiple definitions with same name: " + var36.getMessage());
                  }
               } else if (var1.equals("SocketReaderTimeoutMaxMillis")) {
                  try {
                     this.bean.setSocketReaderTimeoutMaxMillis(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var35) {
                     System.out.println("Warning: multiple definitions with same name: " + var35.getMessage());
                  }
               } else if (var1.equals("SocketReaderTimeoutMinMillis")) {
                  try {
                     this.bean.setSocketReaderTimeoutMinMillis(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var34) {
                     System.out.println("Warning: multiple definitions with same name: " + var34.getMessage());
                  }
               } else if (var1.equals("SocketReaders")) {
                  try {
                     this.bean.setSocketReaders(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var33) {
                     System.out.println("Warning: multiple definitions with same name: " + var33.getMessage());
                  }
               } else if (var1.equals("StdoutFormat")) {
                  this.handleDeprecatedProperty("StdoutFormat", "<unknown>");

                  try {
                     this.bean.setStdoutFormat((String)var2);
                  } catch (BeanAlreadyExistsException var32) {
                     System.out.println("Warning: multiple definitions with same name: " + var32.getMessage());
                  }
               } else if (var1.equals("StdoutSeverityLevel")) {
                  this.handleDeprecatedProperty("StdoutSeverityLevel", "9.0.0.0 Replaced by LogMBean.StdoutSeverity.  For backward compatibility the changes to this attribute will be  propagated to the LogMBean.");

                  try {
                     this.bean.setStdoutSeverityLevel(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var31) {
                     System.out.println("Warning: multiple definitions with same name: " + var31.getMessage());
                  }
               } else if (var1.equals("StuckThreadMaxTime")) {
                  this.handleDeprecatedProperty("StuckThreadMaxTime", "9.0.0.0 replaced by");

                  try {
                     this.bean.setStuckThreadMaxTime(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var30) {
                     System.out.println("Warning: multiple definitions with same name: " + var30.getMessage());
                  }
               } else if (var1.equals("StuckThreadTimerInterval")) {
                  try {
                     this.bean.setStuckThreadTimerInterval(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var29) {
                     System.out.println("Warning: multiple definitions with same name: " + var29.getMessage());
                  }
               } else if (var1.equals("SystemThreadPoolSize")) {
                  try {
                     this.bean.setSystemThreadPoolSize(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var28) {
                     System.out.println("Warning: multiple definitions with same name: " + var28.getMessage());
                  }
               } else if (var1.equals("T3ClientAbbrevTableSize")) {
                  try {
                     this.bean.setT3ClientAbbrevTableSize(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var27) {
                     System.out.println("Warning: multiple definitions with same name: " + var27.getMessage());
                  }
               } else if (var1.equals("T3ServerAbbrevTableSize")) {
                  try {
                     this.bean.setT3ServerAbbrevTableSize(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var26) {
                     System.out.println("Warning: multiple definitions with same name: " + var26.getMessage());
                  }
               } else if (var1.equals("ThreadPoolPercentSocketReaders")) {
                  try {
                     this.bean.setThreadPoolPercentSocketReaders(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var25) {
                     System.out.println("Warning: multiple definitions with same name: " + var25.getMessage());
                  }
               } else if (var1.equals("ThreadPoolSize")) {
                  this.handleDeprecatedProperty("ThreadPoolSize", "9.0.0.0 replaced with SelfTuningMBean");

                  try {
                     this.bean.setThreadPoolSize(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var24) {
                     System.out.println("Warning: multiple definitions with same name: " + var24.getMessage());
                  }
               } else if (var1.equals("TimedOutRefIsolationTime")) {
                  try {
                     this.bean.setTimedOutRefIsolationTime(Long.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var23) {
                     System.out.println("Warning: multiple definitions with same name: " + var23.getMessage());
                  }
               } else if (var1.equals("TracingEnabled")) {
                  try {
                     this.bean.setTracingEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var22) {
                     System.out.println("Warning: multiple definitions with same name: " + var22.getMessage());
                  }
               } else if (var1.equals("Use81StyleExecuteQueues")) {
                  try {
                     this.bean.setUse81StyleExecuteQueues(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var21) {
                     System.out.println("Warning: multiple definitions with same name: " + var21.getMessage());
                  }
               } else if (var1.equals("UseIIOPLocateRequest")) {
                  this.handleDeprecatedProperty("UseIIOPLocateRequest", "<unknown>");

                  try {
                     this.bean.setUseIIOPLocateRequest(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var20) {
                     System.out.println("Warning: multiple definitions with same name: " + var20.getMessage());
                  }
               } else {
                  if (var1.equals("ValidProtocols")) {
                     throw new AssertionError("can't set read-only property ValidProtocols");
                  }

                  if (var1.equals("AddWorkManagerThreadsByCpuCount")) {
                     try {
                        this.bean.setAddWorkManagerThreadsByCpuCount(Boolean.valueOf((String)var2));
                     } catch (BeanAlreadyExistsException var19) {
                        System.out.println("Warning: multiple definitions with same name: " + var19.getMessage());
                     }
                  } else if (var1.equals("DevPollDisabled")) {
                     try {
                        this.bean.setDevPollDisabled(Boolean.valueOf((String)var2));
                     } catch (BeanAlreadyExistsException var18) {
                        System.out.println("Warning: multiple definitions with same name: " + var18.getMessage());
                     }
                  } else if (var1.equals("GatheredWritesEnabled")) {
                     try {
                        this.bean.setGatheredWritesEnabled(Boolean.valueOf((String)var2));
                     } catch (BeanAlreadyExistsException var17) {
                        System.out.println("Warning: multiple definitions with same name: " + var17.getMessage());
                     }
                  } else if (var1.equals("InstrumentStackTraceEnabled")) {
                     try {
                        this.bean.setInstrumentStackTraceEnabled(Boolean.valueOf((String)var2));
                     } catch (BeanAlreadyExistsException var16) {
                        System.out.println("Warning: multiple definitions with same name: " + var16.getMessage());
                     }
                  } else if (var1.equals("LogRemoteExceptionsEnabled")) {
                     try {
                        this.bean.setLogRemoteExceptionsEnabled(Boolean.valueOf((String)var2));
                     } catch (BeanAlreadyExistsException var15) {
                        System.out.println("Warning: multiple definitions with same name: " + var15.getMessage());
                     }
                  } else if (var1.equals("NativeIOEnabled")) {
                     try {
                        this.bean.setNativeIOEnabled(Boolean.valueOf((String)var2));
                     } catch (BeanAlreadyExistsException var14) {
                        System.out.println("Warning: multiple definitions with same name: " + var14.getMessage());
                     }
                  } else if (var1.equals("OutboundEnabled")) {
                     try {
                        this.bean.setOutboundEnabled(Boolean.valueOf((String)var2));
                     } catch (BeanAlreadyExistsException var13) {
                        System.out.println("Warning: multiple definitions with same name: " + var13.getMessage());
                     }
                  } else if (var1.equals("OutboundPrivateKeyEnabled")) {
                     try {
                        this.bean.setOutboundPrivateKeyEnabled(Boolean.valueOf((String)var2));
                     } catch (BeanAlreadyExistsException var12) {
                        System.out.println("Warning: multiple definitions with same name: " + var12.getMessage());
                     }
                  } else if (var1.equals("ReverseDNSAllowed")) {
                     try {
                        this.bean.setReverseDNSAllowed(Boolean.valueOf((String)var2));
                     } catch (BeanAlreadyExistsException var11) {
                        System.out.println("Warning: multiple definitions with same name: " + var11.getMessage());
                     }
                  } else if (var1.equals("ScatteredReadsEnabled")) {
                     try {
                        this.bean.setScatteredReadsEnabled(Boolean.valueOf((String)var2));
                     } catch (BeanAlreadyExistsException var10) {
                        System.out.println("Warning: multiple definitions with same name: " + var10.getMessage());
                     }
                  } else if (var1.equals("SocketBufferSizeAsChunkSize")) {
                     try {
                        this.bean.setSocketBufferSizeAsChunkSize(Boolean.valueOf((String)var2));
                     } catch (BeanAlreadyExistsException var9) {
                        System.out.println("Warning: multiple definitions with same name: " + var9.getMessage());
                     }
                  } else if (var1.equals("StdoutDebugEnabled")) {
                     this.handleDeprecatedProperty("StdoutDebugEnabled", "9.0.0.0 replaced by LogMBean.StdoutSeverity For backward compatibility the changes to this attribute will be propagated to the LogMBean.");

                     try {
                        this.bean.setStdoutDebugEnabled(Boolean.valueOf((String)var2));
                     } catch (BeanAlreadyExistsException var8) {
                        System.out.println("Warning: multiple definitions with same name: " + var8.getMessage());
                     }
                  } else if (var1.equals("StdoutEnabled")) {
                     this.handleDeprecatedProperty("StdoutEnabled", "9.0.0.0 replaced by LogMBean.StdoutSeverity, for backward compatibility the changes to this attribute will be propagated to the LogMBean.");

                     try {
                        this.bean.setStdoutEnabled(Boolean.valueOf((String)var2));
                     } catch (BeanAlreadyExistsException var7) {
                        System.out.println("Warning: multiple definitions with same name: " + var7.getMessage());
                     }
                  } else if (var1.equals("StdoutLogStack")) {
                     this.handleDeprecatedProperty("StdoutLogStack", "9.0.0.0");

                     try {
                        this.bean.setStdoutLogStack(Boolean.valueOf((String)var2));
                     } catch (BeanAlreadyExistsException var6) {
                        System.out.println("Warning: multiple definitions with same name: " + var6.getMessage());
                     }
                  } else if (var1.equals("UseConcurrentQueueForRequestManager")) {
                     try {
                        this.bean.setUseConcurrentQueueForRequestManager(Boolean.valueOf((String)var2));
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
      } catch (ClassCastException var74) {
         System.out.println(var74 + " name: " + var1 + " class: " + var2.getClass().getName());
         throw var74;
      } catch (RuntimeException var75) {
         throw var75;
      } catch (Exception var76) {
         if (var76 instanceof DistributedManagementException) {
            throw new AssertionError("impossible exception: " + var76);
         } else if (var76 instanceof InvalidAttributeValueException) {
            throw new IllegalArgumentException(var76.getMessage());
         } else {
            throw new AssertionError("unexpected exception: " + var76);
         }
      }
   }
}
