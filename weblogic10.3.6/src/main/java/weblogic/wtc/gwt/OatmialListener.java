package weblogic.wtc.gwt;

import com.bea.core.jatmi.common.ntrace;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Timer;
import javax.net.ssl.HandshakeCompletedEvent;
import javax.net.ssl.HandshakeCompletedListener;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLSocket;
import weblogic.security.utils.MBeanKeyStoreConfiguration;
import weblogic.socket.utils.SDPSocketUtils;
import weblogic.wtc.WTCLogger;
import weblogic.wtc.jatmi.TPException;
import weblogic.wtc.jatmi.TuxXidRply;
import weblogic.wtc.jatmi.TuxedoSSLSocketFactory;
import weblogic.wtc.jatmi.atn;

public final class OatmialListener extends Thread {
   private TDMLocalTDomain myLocalDomain;
   private boolean goon = true;
   private WTCService myMommy;
   private Timer myTimeService;
   private TuxXidRply myXidRply;

   public OatmialListener(Timer var1, TDMLocalTDomain var2, WTCService var3, TuxXidRply var4) {
      this.myLocalDomain = var2;
      this.myMommy = var3;
      this.myTimeService = var1;
      this.myXidRply = var4;
   }

   public void shutdown() {
      this.goon = false;
   }

   public void run() {
      boolean var1 = ntrace.isTraceEnabled(2);
      if (var1) {
         ntrace.doTrace("[/WTCService/OatmialListener/run/");
      }

      ServerSocket var6 = null;
      boolean var14 = false;
      String[] var9;
      if ((var9 = this.myLocalDomain.get_ipaddress()) != null && var9.length != 0) {
         boolean[] var15;
         if ((var15 = this.myLocalDomain.get_useSDP()) != null && var15.length != 0) {
            int[] var10 = this.myLocalDomain.get_port();
            int var13 = var9.length;
            InetAddress[] var5 = new InetAddress[var13];
            if (this.myLocalDomain.getMBean().getUseSSL().equals("TwoWay") || this.myLocalDomain.getMBean().getUseSSL().equals("OneWay")) {
               if (var1) {
                  ntrace.doTrace(this.myLocalDomain.getMBean().getUseSSL() + " SSL turned on for access point " + this.myLocalDomain.getMBean().getAccessPoint());
               }

               var14 = true;
            }

            int var12 = 0;

            int var11;
            for(var11 = 0; var11 < var13; ++var11) {
               try {
                  var5[var11] = InetAddress.getByName(var9[var11]);
               } catch (UnknownHostException var38) {
                  var5[var11] = null;
                  if (var1) {
                     ntrace.doTrace("unknown host(" + var9[var11] + ") skip it.");
                  }

                  ++var12;
               }
            }

            if (var12 == var13) {
               WTCLogger.logWarnNoValidListeningAddress(this.myLocalDomain.getAccessPointId());
               if (var1) {
                  ntrace.doTrace("]/WTCService/OatmialListener/run/08");
               }

            } else {
               boolean var39 = false;

               for(var11 = 0; var11 < var13 && !var39; ++var11) {
                  if (var5[var11] == null) {
                     if (var11 >= var13 - 1) {
                        WTCLogger.logWarnNoMoreValidListeningAddress(this.myLocalDomain.getAccessPointId());
                        break;
                     }
                  } else {
                     try {
                        if (var15[var11] && var14) {
                           var14 = false;
                           WTCLogger.logWarnIgnoreSSLwithSDP(this.myLocalDomain.getAccessPointId());
                        }

                        Method var17;
                        if (var14) {
                           var17 = null;
                           TuxedoSSLSocketFactory var41;
                           String var43;
                           if (this.myLocalDomain.getMBean().getKeyStoresLocation().equals("WLS Stores")) {
                              MBeanKeyStoreConfiguration var18 = MBeanKeyStoreConfiguration.getInstance();
                              String var19 = var18.getKeyStores();
                              if (var1) {
                                 ntrace.doTrace("key store info = " + var19);
                              }

                              String var23 = null;
                              String var24 = null;
                              String var25 = null;
                              String var26 = var18.getCustomIdentityPrivateKeyPassPhrase();
                              if ((var43 = var18.getCustomIdentityAlias()) == null && (var43 = this.myLocalDomain.getConnPrincipalName()) == null) {
                                 var43 = this.myLocalDomain.getAccessPointId();
                              }

                              String var20 = var18.getCustomIdentityKeyStoreType();
                              String var21 = var18.getCustomIdentityKeyStoreFileName();
                              String var22 = var18.getCustomIdentityKeyStorePassPhrase();
                              if (var1) {
                                 ntrace.doTrace("idKSType = " + var20 + ", idKSLoc = " + var21 + ", idKSPwd = " + var22);
                              }

                              if ("CustomIdentityAndCustomTrust".equals(var19)) {
                                 var23 = var18.getCustomTrustKeyStoreType();
                                 var24 = var18.getCustomTrustKeyStoreFileName();
                                 var25 = var18.getCustomTrustKeyStorePassPhrase();
                                 if (var1) {
                                    ntrace.doTrace("trustKSType = " + var23 + ", trustKSLoc = " + var24 + ", trustKSPwd = " + var25);
                                 }
                              }

                              var41 = new TuxedoSSLSocketFactory(var20, var21, var22, var43, var26, var23, var24, var25);
                           } else {
                              if (this.myLocalDomain.getMBean().getPrivateKeyAlias() == null && this.myLocalDomain.getConnPrincipalName() == null) {
                                 var43 = this.myLocalDomain.getAccessPointId();
                              }

                              var41 = new TuxedoSSLSocketFactory("jks", this.myLocalDomain.getMBean().getIdentityKeyStoreFileName(), this.myLocalDomain.getMBean().getIdentityKeyStorePassPhrase(), this.myLocalDomain.getMBean().getPrivateKeyAlias(), this.myLocalDomain.getMBean().getPrivateKeyPassPhrase(), "jks", this.myLocalDomain.getMBean().getTrustKeyStoreFileName(), this.myLocalDomain.getMBean().getTrustKeyStorePassPhrase());
                           }

                           var6 = var41.createServerSocket(var10[var11], 50, var5[var11]);
                           if (this.myLocalDomain.getMBean().getUseSSL().equals("TwoWay")) {
                              if (var1) {
                                 ntrace.doTrace("Server set to TwoWay SSL");
                              }

                              ((SSLServerSocket)var6).setNeedClientAuth(true);
                           } else if (var1) {
                              ntrace.doTrace("Server set to OneWay SSL");
                           }
                        } else if (!var15[var11]) {
                           var6 = new ServerSocket(var10[var11], 50, var5[var11]);
                        } else {
                           try {
                              Class var16 = Class.forName("com.oracle.net.Sdp");
                              SDPSocketUtils.ensureEnvironment();
                              var17 = var16.getDeclaredMethod("openServerSocket", (Class[])null);
                              var6 = (ServerSocket)var17.invoke((Object)null, (Object[])null);
                           } catch (ClassNotFoundException var35) {
                              WTCLogger.logErrorSdpClassNotFound();
                           } catch (Exception var36) {
                              if (var1) {
                                 ntrace.doTrace("]/WTCService/OatmialListener/run/" + var36.toString() + "80");
                              }
                           }

                           InetSocketAddress var40 = new InetSocketAddress(var5[var11], var10[var11]);
                           var6.bind(var40, 50);
                        }

                        var39 = true;
                     } catch (IOException var37) {
                        if (var11 < var13 - 1) {
                           WTCLogger.logInfoTryNextListeningAddress(this.myLocalDomain.getAccessPointId(), var9[var11], var10[var11]);
                        } else {
                           WTCLogger.logWarnNoMoreListeningAddressToTry(this.myLocalDomain.getAccessPointId(), var9[var11], var10[var11]);
                        }
                     }
                  }
               }

               if (!var39) {
                  if (var1) {
                     ntrace.doTrace("]/WTCService/OatmialListener/run/20");
                  }

               } else {
                  try {
                     var6.setSoTimeout(1000);
                  } catch (SocketException var30) {
                     ntrace.doTrace("SocketException: " + var30);
                  }

                  while(this.goon) {
                     WLSInvoke var2 = null;
                     gwdsession var8 = null;
                     Socket var7 = null;
                     TDMRemote var4 = null;
                     TDMRemote[] var3 = null;

                     try {
                        var7 = var6.accept();
                        if (var14) {
                           ((SSLSocket)var7).addHandshakeCompletedListener(new MyListener());
                           String[] var42 = new String[]{"TLSv1"};
                           ((SSLSocket)var7).setEnabledProtocols(var42);
                           ((SSLSocket)var7).setEnabledCipherSuites(TuxedoSSLSocketFactory.getCiphers(this.myLocalDomain.getMinEncryptBits(), this.myLocalDomain.getMaxEncryptBits()));
                        }

                        var2 = new WLSInvoke(this.myLocalDomain);
                        var8 = new gwdsession(this.myTimeService, var7, (atn)null, var2, WTCService.getUniqueGwdsessionId(), var14, this.myXidRply);
                        var8.set_BlockTime(this.myLocalDomain.getBlockTime());
                        var8.set_compression_threshold(this.myLocalDomain.getCmpLimit());
                        var8.set_sess_sec(this.myLocalDomain.getSecurity());
                        var8.setDesiredName(this.myLocalDomain.getConnPrincipalName());
                        var8.set_local_domain_name(this.myLocalDomain.getAccessPointId());
                        var3 = this.myLocalDomain.get_remote_domains();
                        var4 = var8.tpinit(var3);
                        var2.setRemoteDomain(var4);
                        var8.setTerminationHandler((TDMRemoteTDomain)var4);
                     } catch (SocketTimeoutException var31) {
                     } catch (TPException var32) {
                        if (var8 != null) {
                           var8._dom_drop();
                        } else {
                           try {
                              if (var7 != null) {
                                 var7.close();
                              }
                           } catch (IOException var29) {
                           }
                        }

                        if (var1) {
                           ntrace.doTrace("/WTCService/OatmialListener/run/30/" + var32);
                        }
                     } catch (IOException var33) {
                        if (var8 != null) {
                           var8._dom_drop();
                        } else {
                           try {
                              if (var7 != null) {
                                 var7.close();
                              }
                           } catch (IOException var28) {
                           }
                        }

                        if (var1) {
                           ntrace.doTrace("/WTCService/OatmialListener/run/40/" + var33);
                        }
                     } catch (Exception var34) {
                        if (var1) {
                           ntrace.doTrace("/WTCService/OatmialListener/run/50/" + var34);
                        }
                     }
                  }

                  if (var6 != null) {
                     try {
                        if (var1) {
                           ntrace.doTrace("/WTCService/OatmialListener/close server socket");
                        }

                        var6.close();
                     } catch (IOException var27) {
                     }
                  }

                  if (var1) {
                     ntrace.doTrace("]/WTCService/OatmialListener/run/60/thread end");
                  }

               }
            }
         } else {
            if (var1) {
               ntrace.doTrace("]/WTCService/OatmialListener/run/15");
            }

         }
      } else {
         if (var1) {
            ntrace.doTrace("]/WTCService/OatmialListener/run/05");
         }

      }
   }

   private class MyListener implements HandshakeCompletedListener {
      private MyListener() {
      }

      public void handshakeCompleted(HandshakeCompletedEvent var1) {
         boolean var2 = ntrace.isTraceEnabled(2);
         if (var2) {
            ntrace.doTrace("/WTCService/OatmialListener/Server handshake done. Cipher used: " + var1.getCipherSuite());
         }

      }

      // $FF: synthetic method
      MyListener(Object var2) {
         this();
      }
   }
}
