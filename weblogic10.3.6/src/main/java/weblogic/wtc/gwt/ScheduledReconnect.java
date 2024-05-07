package weblogic.wtc.gwt;

import com.bea.core.jatmi.common.ntrace;
import com.bea.core.jatmi.internal.TCTaskHelper;
import java.util.TimerTask;
import weblogic.logging.Loggable;
import weblogic.management.configuration.WTCRemoteTuxDomMBean;
import weblogic.wtc.WTCLogger;
import weblogic.wtc.jatmi.TPException;

class ScheduledReconnect extends TimerTask {
   private TDMRemoteTDomain rdom;
   private long retryLeft;
   private ConnectingWork conn_work = null;
   private int state;
   private boolean connected = false;
   private static int CONN_ST_INIT = 0;
   private static int CONN_ST_ACTIVE = 1;
   private static int CONN_ST_IDLE = 2;
   private static int CONN_ST_SHUTDOWN = 3;

   ScheduledReconnect(TDMRemoteTDomain var1, long var2) {
      this.rdom = var1;
      this.retryLeft = var2;
   }

   public void connectingFailure() {
      synchronized(this) {
         this.conn_work = null;
      }

      --this.retryLeft;
      if (this.retryLeft <= 0L) {
         this.rdom.removeConnectingTaskReference();
         this.cancel();
      }

   }

   public void connectingSuccess() {
      synchronized(this) {
         this.conn_work = null;
         this.connected = true;
      }

      this.rdom.removeConnectingTaskReference();
      this.cancel();
   }

   public void connectingTerminate() {
      synchronized(this) {
         this.conn_work = null;
         this.connected = false;
      }

      this.rdom.removeConnectingTaskReference();
      this.cancel();
   }

   public void run() {
      boolean var1 = ntrace.isTraceEnabled(2);
      if (var1) {
         ntrace.doTrace("[/ScheduledReconnect/run/" + this.rdom.getAccessPoint());
      }

      boolean var2;
      synchronized(this) {
         var2 = this.conn_work == null;
      }

      if (var2) {
         this.conn_work = new ConnectingWork(this, this.rdom);
         TCTaskHelper.schedule(this.conn_work);
      }

      if (var1) {
         ntrace.doTrace("]/ScheduledReconnect/run/10");
      }

   }

   public static TDMRemoteTDomain create(WTCRemoteTuxDomMBean var0) throws TPException {
      boolean var1 = ntrace.isMixedTraceEnabled(18);
      if (var1) {
         ntrace.doTrace("[/TDMRemoteTDomain/create/");
      }

      WTCService var2 = WTCService.getWTCService();
      String var3 = var0.getAccessPoint();
      if (var3 == null) {
         Loggable var17 = WTCLogger.logUndefinedMBeanAttrLoggable("AccessPoint", var0.getName());
         var17.log();
         if (var1) {
            ntrace.doTrace("*]/TDMRemoteTDomain/create/10/no AP");
         }

         throw new TPException(4, var17.getMessage());
      } else {
         if (var1) {
            ntrace.doTrace("AccessPoint: " + var3);
         }

         String var4 = var0.getAccessPointId();
         if (var4 == null) {
            Loggable var18 = WTCLogger.logUndefinedMBeanAttrLoggable("AccessPointId", var0.getName());
            var18.log();
            if (var1) {
               ntrace.doTrace("*]/TDMRemoteTDomain/create/20/no APId");
            }

            throw new TPException(4, var18.getMessage());
         } else {
            if (var1) {
               ntrace.doTrace("AccessPointId:" + var4);
            }

            String var5 = var0.getLocalAccessPoint();
            if (var5 == null) {
               Loggable var19 = WTCLogger.logUndefinedMBeanAttrLoggable("LocalAccessPoint", var0.getName());
               var19.log();
               if (var1) {
                  ntrace.doTrace("*]/TDMRemoteTDomain/create/30/no LAP");
               }

               throw new TPException(4, var19.getMessage());
            } else {
               if (var1) {
                  ntrace.doTrace("LocalAccessPoint:" + var5);
               }

               String var6 = var0.getNWAddr();
               if (var6 == null) {
                  Loggable var20 = WTCLogger.logUndefinedMBeanAttrLoggable("NWAddr", var0.getName());
                  var20.log();
                  if (var1) {
                     ntrace.doTrace("*]/TDMRemoteTDomain/create/40/no NWAddr");
                  }

                  throw new TPException(4, var20.getMessage());
               } else {
                  if (var1) {
                     ntrace.doTrace("NWAddr:" + var6);
                  }

                  if (var1) {
                     ntrace.doTrace("create rtd from " + var3);
                  }

                  TDMRemoteTDomain var7;
                  Loggable var9;
                  try {
                     var7 = new TDMRemoteTDomain(var3, var2.getUnknownTxidRply(), WTCService.getTimerService());
                  } catch (Exception var15) {
                     var9 = WTCLogger.logUEconstructTDMRemoteTDLoggable(var15.getMessage());
                     var9.log();
                     if (var1) {
                        ntrace.doTrace("*]/TDMRemoteTDomain/create/50/create failed");
                     }

                     throw new TPException(4, var9.getMessage());
                  }

                  TDMLocalTDomain var8 = var2.getLocalDomain(var5);
                  if (null == var8) {
                     var9 = WTCLogger.logErrorBadTDMRemoteLTDLoggable(var5);
                     var9.log();
                     if (var1) {
                        ntrace.doTrace("*]/TDMRemoteTDomain/create/60/no LDOM");
                     }

                     throw new TPException(4, var9.getMessage());
                  } else {
                     if (var1) {
                        ntrace.doTrace("valid LocalAccessPoint");
                     }

                     var7.setLocalAccessPoint(var5);
                     var7.setAccessPointId(var4);
                     var7.setAclPolicy(var0.getAclPolicy());
                     var7.setCredentialPolicy(var0.getCredentialPolicy());
                     var7.setTpUsrFile(var0.getTpUsrFile());

                     Loggable var10;
                     try {
                        var7.setNWAddr(var6);
                     } catch (TPException var14) {
                        var10 = WTCLogger.logInvalidMBeanAttrLoggable("NWAddr", var0.getName());
                        var10.log();
                        if (var1) {
                           ntrace.doTrace("*]/TDMRemoteTDomain/create/70/" + var14.getMessage());
                        }

                        throw new TPException(4, var10.getMessage());
                     }

                     var7.setFederationURL(var0.getFederationURL());
                     var7.setFederationName(var0.getFederationName());

                     try {
                        var7.setCmpLimit(var0.getCmpLimit());
                     } catch (TPException var16) {
                        var10 = WTCLogger.logInvalidMBeanAttrLoggable("CmpLimit", var0.getName());
                        var10.log();
                        if (var1) {
                           ntrace.doTrace("*]/TDMRemoteTDomain/create/80/" + var16.getMessage());
                        }

                        throw new TPException(4, var10.getMessage());
                     }

                     String var21 = var0.getMinEncryptBits();
                     if (var21 != null) {
                        var7.setMinEncryptBits(Integer.parseInt(var21, 10));
                     }

                     var21 = var0.getMaxEncryptBits();
                     if (var21 != null) {
                        var7.setMaxEncryptBits(Integer.parseInt(var21, 10));
                     }

                     var7.setConnectionPolicy(var0.getConnectionPolicy());
                     var7.setRetryInterval(var0.getRetryInterval());
                     var7.setMaxRetries(var0.getMaxRetries());
                     var7.setKeepAlive(var0.getKeepAlive());
                     var7.setKeepAliveWait(var0.getKeepAliveWait());
                     String var22 = var0.getAppKey();
                     if (var22 == null && var0.getTpUsrFile() != null) {
                        var22 = new String("TpUsrFile");
                        if (var1) {
                           ntrace.doTrace("Use dflt AppKey Generator");
                        }
                     }

                     var7.setAppKey(var22);
                     var7.setAllowAnonymous(var0.getAllowAnonymous());
                     var7.setDefaultAppKey(var0.getDefaultAppKey());
                     if (var22 != null) {
                        if (var22.equals("LDAP")) {
                           var7.setTuxedoUidKw(var0.getTuxedoUidKw());
                           var7.setTuxedoGidKw(var0.getTuxedoGidKw());
                           if (var1) {
                              ntrace.doTrace("LDAP, allow=" + var0.getAllowAnonymous() + ",Dflt AppKey=" + var0.getDefaultAppKey() + ",UID KW=" + var0.getTuxedoUidKw() + ", GID KW=" + var0.getTuxedoGidKw());
                           }
                        } else if (var22.equals("Custom")) {
                           String var11 = var0.getCustomAppKeyClass();
                           String var12 = var0.getCustomAppKeyClassParam();
                           if (var11 == null) {
                              Loggable var13 = WTCLogger.logUndefinedMBeanAttrLoggable("CustomAppKeyClass", var0.getName());
                              var13.log();
                              if (var1) {
                                 ntrace.doTrace("*]/TDMRemoteTDomain/create/90/no custom class defined");
                              }

                              throw new TPException(4, var13.getMessage());
                           }

                           if (var1) {
                              ntrace.doTrace("Custom, allow=" + var0.getAllowAnonymous() + ",Dflt AppKey=" + var0.getDefaultAppKey() + ",Class=" + var11 + ", Parm =" + var12);
                           }

                           var7.setCustomAppKeyClass(var11);
                           var7.setCustomAppKeyClassParam(var12);
                        } else {
                           if (!var22.equals("TpUsrFile")) {
                              Loggable var23 = WTCLogger.logInvalidMBeanAttrLoggable("AppKey", var0.getName());
                              var23.log();
                              if (var1) {
                                 ntrace.doTrace("*]/TDMRemoteTDomain/create/100/unsupported appkey");
                              }

                              throw new TPException(4, var23.getMessage());
                           }

                           if (var1) {
                              ntrace.doTrace("TpUsrFile, allow=" + var0.getAllowAnonymous() + ",Dflt AppKey=" + var0.getDefaultAppKey() + ",File=" + var0.getTpUsrFile());
                           }
                        }
                     }

                     var7.setMBean(var0);
                     if (var1) {
                        ntrace.doTrace("]/TDMRemoteTDomain/create/140/success");
                     }

                     return var7;
                  }
               }
            }
         }
      }
   }
}
