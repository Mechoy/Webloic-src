package weblogic.wtc.tbridge;

import com.bea.core.jatmi.common.ntrace;
import java.util.StringTokenizer;
import weblogic.logging.Loggable;
import weblogic.management.configuration.WTCServerMBean;
import weblogic.management.configuration.WTCtBridgeGlobalMBean;
import weblogic.management.configuration.WTCtBridgeRedirectMBean;
import weblogic.wtc.WTCLogger;
import weblogic.wtc.jatmi.TPException;

public final class tBparseArgs {
   public static tBstartArgs sArgs = new tBstartArgs();
   private String tempstr;
   private tBpvalueMap pv = new tBpvalueMap();
   private int state = 0;
   public static final int TUX2JMS = 1;
   public static final int JMS2TUX = 2;
   private static final int TUXQ2JMSQ = 1;
   private static final int JMSQ2TUXQ = 2;
   private static final int JMSQ2TUXS = 3;
   private static final int JMSQ2JMSQ = 4;

   public tBstartArgs parseMBeans(WTCServerMBean var1) throws TPException {
      boolean var2 = ntrace.isMixedTraceEnabled(17);
      if (var2) {
         ntrace.doTrace("[/tBparseArgs/parseMBeans");
      }

      WTCtBridgeRedirectMBean[] var3 = var1.gettBridgeRedirects();
      int var4 = var3.length;
      if (var4 == 0) {
         if (var2) {
            ntrace.doTrace("*]tBparseArgs/parseMBeans/10/no tbridge redirect info defined");
         }

         Loggable var8 = WTCLogger.logtBNOredirectsLoggable();
         var8.log();
         throw new TPException(4, var8.getMessage());
      } else {
         if (var2) {
            ntrace.doTrace("/tBparseArgs/parseMBeans/redirect=" + var4);
         }

         sArgs.redirect = var4;

         Loggable var6;
         for(int var5 = 0; var5 < var4; ++var5) {
            if (!this.setupTBRedirInfo(var3[var5], var5)) {
               var6 = WTCLogger.logErrorExecMBeanDefLoggable(var3[var5].getName());
               var6.log();
               throw new TPException(4, var6.getMessage());
            }

            if (var2) {
               ntrace.doTrace("/tBparseArgs/parseMBeans/20/setup tbridge redirect:" + var3[var5].getName());
            }
         }

         WTCtBridgeGlobalMBean var7 = var1.getWTCtBridgeGlobal();
         if (var7 == null) {
            var6 = WTCLogger.logUndefinedMBeanLoggable("tBridgeGlobal");
            var6.log();
            throw new TPException(4, var6.getMessage());
         } else if (!this.setupTBGlobalInfo(var7)) {
            var6 = WTCLogger.logErrorExecMBeanDefLoggable(var7.getName());
            var6.log();
            throw new TPException(4, var6.getMessage());
         } else {
            if (var2) {
               ntrace.doTrace("/tBparseArgs/parseMBeans/30/setup tbridge global:" + var7.getName());
            }

            for(int var9 = 0; var9 < var4; ++var9) {
               sArgs.operational[var9] = true;
            }

            sArgs.print_tBstartArgs();
            if (var2) {
               ntrace.doTrace("]tBparseArgs/parseMBeans/40/sArgs");
            }

            return sArgs;
         }
      }
   }

   public tBstartArgs parseGlobal(WTCtBridgeGlobalMBean var1) throws TPException {
      boolean var2 = ntrace.isMixedTraceEnabled(17);
      if (var2) {
         ntrace.doTrace("[/tBparseArgs/parseGlobal(tBridgeGlobal)");
      }

      Loggable var3;
      if (var1 != null) {
         if (!this.setupTBGlobalInfo(var1)) {
            if (var2) {
               ntrace.doTrace("*]/tBparseArgs/parseGlobal(10) failed to setup tbridge global: " + var1.getName());
            }

            var3 = WTCLogger.logErrorExecMBeanDefLoggable(var1.getName());
            var3.log();
            throw new TPException(4, var3.getMessage());
         } else {
            if (var2) {
               ntrace.doTrace("setup tbridge global:" + var1.getName());
            }

            if (var2) {
               ntrace.doTrace("]/tBparseArgs/parseGlobal(30) returns sArgs = " + sArgs);
            }

            return sArgs;
         }
      } else {
         if (var2) {
            ntrace.doTrace("*]/tBparseArgs/parseGlobal(20) null WTCtBridgeGlobalMBean");
         }

         var3 = WTCLogger.logUndefinedMBeanLoggable("tBridgeGlobal");
         var3.log();
         throw new TPException(4, var3.getMessage());
      }
   }

   public tBstartArgs parseRedirect(WTCtBridgeRedirectMBean var1) throws TPException {
      boolean var2 = ntrace.isMixedTraceEnabled(17);
      if (var2) {
         ntrace.doTrace("[/tBparseArgs/parseRedirect(WTCtBridgeRedirect=" + var1 + ")");
      }

      if (var1 == null) {
         if (var2) {
            ntrace.doTrace("*]/tBparseArgs/parseRedirect(10) null WTCtBridgeRedirectMBean");
         }

         Loggable var5 = WTCLogger.logtBNOredirectsLoggable();
         var5.log();
         throw new TPException(4, var5.getMessage());
      } else {
         int var3;
         if (sArgs.redirect == -1) {
            var3 = 0;
            sArgs.redirect = 1;
         } else {
            var3 = sArgs.redirect++;
         }

         if (!this.setupTBRedirInfo(var1, var3)) {
            if (var2) {
               ntrace.doTrace("*]/tBparseArgs/parseRedirect(20) failed to parse WTCtBridgeRedirectMBean");
            }

            Loggable var4 = WTCLogger.logErrorExecMBeanDefLoggable(var1.getName());
            var4.log();
            throw new TPException(4, var4.getMessage());
         } else {
            if (var2) {
               ntrace.doTrace("setup tbridge redirect:" + var1.getName());
            }

            sArgs.operational[var3] = true;
            if (var2) {
               ntrace.doTrace("]/tBparseArgs/parseRedirect(30) parse WTCtBridgeRedirectMBean successful");
            }

            return sArgs;
         }
      }
   }

   public tBstartArgs getParsedMBeans() {
      boolean var1 = ntrace.isMixedTraceEnabled(17);
      if (var1) {
         ntrace.doTrace("[/tBparseArgs/getParsedMBean()");
      }

      sArgs.print_tBstartArgs();
      if (var1) {
         ntrace.doTrace("]/tBparseArgs/getParsedMBean(10) returns sArgs = " + sArgs);
      }

      return sArgs;
   }

   private boolean setupTBRedirInfo(WTCtBridgeRedirectMBean var1, int var2) {
      boolean var3 = ntrace.isMixedTraceEnabled(17);
      if (var3) {
         ntrace.doTrace("[/tBparseArgs/setupTBRedirInfo/rindex " + var2);
      }

      String var4 = var1.getDirection();
      if (var4.equals("JmsQ2TuxQ")) {
         sArgs.direction[var2] = 2;
      } else if (var4.equals("TuxQ2JmsQ")) {
         sArgs.direction[var2] = 1;
      } else if (var4.equals("JmsQ2TuxS")) {
         sArgs.direction[var2] = 3;
      } else if (var4.equals("JmsQ2JmsQ")) {
         sArgs.direction[var2] = 4;
      }

      if (var3) {
         ntrace.doTrace("/tBparseArgs/setupTBRedirInfo/Direction=" + var4);
      }

      String var5 = var1.getSourceAccessPoint();
      String var6 = var1.getTargetAccessPoint();
      if (var5 != null && var5.length() != 0 || var6 != null && var6.length() != 0) {
         if (var5 != null && var5.length() != 0) {
            if (var6 != null && var6.length() != 0) {
               if (sArgs.direction[var2] == 1) {
                  if (var3) {
                     ntrace.doTrace("/tBparseArgs/setupTBRedirInfo/20/WARNING: source and target Access Point both set. Using source AccessPoint");
                  }

                  sArgs.AccessPoint[var2] = var5;
               } else {
                  if (var3) {
                     ntrace.doTrace("/tBparseArgs/setupTBRedirInfo/25/WARNING: source and target Access Point both set. Using target AccessPoint");
                  }

                  sArgs.AccessPoint[var2] = var6;
               }
            } else {
               sArgs.AccessPoint[var2] = var5;
            }
         } else {
            sArgs.AccessPoint[var2] = var6;
         }
      } else if (sArgs.direction[var2] != 4) {
         if (var3) {
            ntrace.doTrace("]/tBparseArgs/setupTBRedirInfo/10/false Source or target AccessPoint must be set.");
         }

         return false;
      }

      if (var3) {
         ntrace.doTrace("/tBparseArgs/setupTBRedirInfo/AccessPoint=" + sArgs.AccessPoint[var2]);
      }

      sArgs.sourceName[var2] = var1.getSourceName();
      if (var3) {
         ntrace.doTrace("/tBparseArgs/setupTBRedirInfo/SourceName=" + sArgs.sourceName[var2]);
      }

      sArgs.targetName[var2] = var1.getTargetName();
      if (var3) {
         ntrace.doTrace("/tBparseArgs/setupTBRedirInfo/TargetName=" + sArgs.targetName[var2]);
      }

      sArgs.sourceQspace[var2] = var1.getSourceQspace();
      if (var3) {
         ntrace.doTrace("/tBparseArgs/setupTBRedirInfo/SourceQspace=" + sArgs.sourceQspace[var2]);
      }

      sArgs.targetQspace[var2] = var1.getTargetQspace();
      if (var3) {
         ntrace.doTrace("/tBparseArgs/setupTBRedirInfo/TargetQspace=" + sArgs.targetQspace[var2]);
      }

      sArgs.translateFML[var2] = var1.getTranslateFML();
      if (var3) {
         ntrace.doTrace("/tBparseArgs/setupTBRedirInfo/TranslateFML=" + sArgs.translateFML[var2]);
      }

      sArgs.metadataFile[var2] = var1.getMetaDataFile();
      if (var3) {
         ntrace.doTrace("/tBparseArgs/setupTBRedirInfo/MetaDataFile=" + sArgs.metadataFile[var2]);
      }

      sArgs.replyQ[var2] = var1.getReplyQ();
      if (var3) {
         ntrace.doTrace("/tBparseArgs/setupTBRedirInfo/ReplyQ=" + sArgs.replyQ[var2]);
      }

      if (var3) {
         ntrace.doTrace("]/tBparseArgs/setupTBRedirInfo/35/true");
      }

      return true;
   }

   private boolean setupTBGlobalInfo(WTCtBridgeGlobalMBean var1) {
      boolean var2 = ntrace.isMixedTraceEnabled(17);
      if (var2) {
         ntrace.doTrace("[/tBparseArgs/setupTBGlobalInfo/");
      }

      sArgs.transactional = var1.getTransactional();
      if (var2) {
         ntrace.doTrace("/tBparseArgs/setupTBGlobalInfo/Transactional=" + sArgs.transactional);
      }

      sArgs.timeout = var1.getTimeout();
      if (var2) {
         ntrace.doTrace("/tBparseArgs/setupTBGlobalInfo/Timeout=" + sArgs.timeout);
      }

      sArgs.retries = var1.getRetries();
      if (var2) {
         ntrace.doTrace("/tBparseArgs/setupTBGlobalInfo/Retries=" + sArgs.retries);
      }

      sArgs.retryDelay = var1.getRetryDelay();
      if (var2) {
         ntrace.doTrace("/tBparseArgs/setupTBGlobalInfo/RetryDelay=" + sArgs.retryDelay);
      }

      sArgs.wlsErrorDestination = var1.getWlsErrorDestination();
      if (var2) {
         ntrace.doTrace("/tBparseArgs/setupTBGlobalInfo/WlsErrorDestination=" + sArgs.wlsErrorDestination);
      }

      sArgs.tuxErrorQueue = var1.getTuxErrorQueue();
      if (var2) {
         ntrace.doTrace("/tBparseArgs/setupTBGlobalInfo/TuxErrorQueue=" + sArgs.tuxErrorQueue);
      }

      sArgs.deliveryModeOverride = var1.getDeliveryModeOverride();
      if (var2) {
         ntrace.doTrace("/tBparseArgs/setupTBGlobalInfo/DeliveryModeOverride=" + sArgs.deliveryModeOverride);
      }

      sArgs.defaultReplyDeliveryMode = var1.getDefaultReplyDeliveryMode();
      if (var2) {
         ntrace.doTrace("/tBparseArgs/setupTBGlobalInfo/DefaultReplyDeliveryMode=" + sArgs.defaultReplyDeliveryMode);
      }

      sArgs.userID = var1.getUserId();
      if (var2) {
         ntrace.doTrace("/tBparseArgs/setupTBGlobalInfo/UserId=" + sArgs.userID);
      }

      sArgs.allowNonStandardTypes = var1.getAllowNonStandardTypes();
      if (var2) {
         ntrace.doTrace("/tBparseArgs/setupTBGlobalInfo/AllowNonStandardTypes=" + sArgs.allowNonStandardTypes);
      }

      sArgs.jndiFactory = var1.getJndiFactory();
      if (sArgs.jndiFactory == null || sArgs.jndiFactory.length() == 0) {
         sArgs.jndiFactory = "weblogic.jndi.WLInitialContextFactory";
         if (var2) {
            ntrace.doTrace("jndiFactory set to default value");
         }
      }

      if (var2) {
         ntrace.doTrace("/tBparseArgs/setupTBGlobalInfo/JndiFactory=" + sArgs.jndiFactory);
      }

      sArgs.jmsFactory = var1.getJmsFactory();
      if (sArgs.jmsFactory == null || sArgs.jmsFactory.length() == 0) {
         if (sArgs.transactional.equals("Yes")) {
            sArgs.jmsFactory = "weblogic.jms.XAConnectionFactory";
            if (var2) {
               ntrace.doTrace("jmsFactory set to default transactional value");
            }
         } else {
            sArgs.jmsFactory = "weblogic.jms.ConnectionFactory";
            if (var2) {
               ntrace.doTrace("jmsFactory set to default non transactional value");
            }
         }
      }

      if (var2) {
         ntrace.doTrace("/tBparseArgs/setupTBGlobalInfo/JmsFactory=" + sArgs.jmsFactory);
      }

      sArgs.tuxFactory = var1.getTuxFactory();
      if (sArgs.tuxFactory == null || sArgs.tuxFactory.length() == 0) {
         sArgs.tuxFactory = "tuxedo.services.TuxedoConnection";
         if (var2) {
            ntrace.doTrace("tuxFactory set to default value");
         }
      }

      if (var2) {
         ntrace.doTrace("/tBparseArgs/setupTBGlobalInfo/TuxFactory=" + sArgs.tuxFactory);
      }

      boolean var3 = true;
      boolean var4 = true;
      tBsetPriority var9 = new tBsetPriority();
      tBsetPriority var10 = new tBsetPriority();
      boolean var11 = true;

      for(int var12 = 1; var12 <= 2; ++var12) {
         String var5;
         if (var12 == 2) {
            var5 = var1.getJmsToTuxPriorityMap();
         } else {
            var5 = var1.getTuxToJmsPriorityMap();
         }

         if (var5 != null && var5.length() != 0) {
            if (var2) {
               ntrace.doTrace("/tBparseArgs/setupTBGlobalInfo/Pway=" + var12 + " PriorityMap=" + var5);
            }

            ++sArgs.priorityMapping;
            this.pv.Pway = var12;
            String var6;
            int var13;
            if (var5.indexOf(124) == -1) {
               var13 = var5.indexOf(58);
               if (var13 == -1) {
                  if (var2) {
                     ntrace.doTrace("/tBparseArgs/setupTBGlobalInfo/10/Error value:range pair missing separator=>" + var5);
                  }

                  var3 = false;
               } else {
                  var6 = var5.substring(0, var13);
                  if (var6.indexOf(45) != -1) {
                     if (var2) {
                        ntrace.doTrace("/tBparseArgs/setupTBGlobalInfo/15/Error range defined instead of value in value:range pair=>" + var6);
                     }

                     var3 = false;
                  } else {
                     this.pv.Pvalue = Integer.parseInt(var6, 10);
                     this.pv.Prange = var5.substring(var13 + 1, var5.length());
                     if (var12 == 2) {
                        var4 = var9.setPVmap(this.pv, sArgs);
                     } else {
                        var4 = var10.setPVmap(this.pv, sArgs);
                     }

                     if (!var4 && var2) {
                        ntrace.doTrace("/tBparseArgs/setupTBGlobalInfo/20/WARNING:problem setting priority map");
                     }
                  }
               }
            } else {
               StringTokenizer var8 = new StringTokenizer(var5, "|");

               while(var8.hasMoreTokens()) {
                  String var7 = var8.nextToken();
                  var13 = var7.indexOf(58);
                  if (var13 == -1) {
                     if (var2) {
                        ntrace.doTrace("/tBparseArgs/setupTBGlobalInfo/20/Error value:range pair missing separator=>" + var7);
                     }

                     var3 = false;
                  } else {
                     var6 = var7.substring(0, var13);
                     if (var6.indexOf(45) != -1) {
                        if (var2) {
                           ntrace.doTrace("/tBparseArgs/setupTBGlobalInfo/25/Error range defined instead of value in value:range pair=>" + var6);
                        }

                        var3 = false;
                     } else {
                        this.pv.Pvalue = Integer.parseInt(var6, 10);
                        this.pv.Prange = var7.substring(var13 + 1, var7.length());
                        if (var12 == 2) {
                           var4 = var9.setPVmap(this.pv, sArgs);
                        } else {
                           var4 = var10.setPVmap(this.pv, sArgs);
                        }

                        if (!var4 && var2) {
                           ntrace.doTrace("/tBparseArgs/setupTBGlobalInfo/45/WARNING:problem setting priority map");
                        }
                     }
                  }
               }
            }
         }
      }

      if (var2) {
         ntrace.doTrace("]/tBparseArgs/setupTBGlobalInfo/50/" + var3);
      }

      return var3;
   }
}
