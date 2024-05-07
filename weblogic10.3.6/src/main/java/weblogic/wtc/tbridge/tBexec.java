package weblogic.wtc.tbridge;

import com.bea.core.jatmi.common.ntrace;
import java.util.Date;
import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageEOFException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import weblogic.logging.Loggable;
import weblogic.management.configuration.WTCServerMBean;
import weblogic.management.configuration.WTCtBridgeGlobalMBean;
import weblogic.management.configuration.WTCtBridgeRedirectMBean;
import weblogic.transaction.TransactionManager;
import weblogic.transaction.TxHelper;
import weblogic.wtc.WTCLogger;
import weblogic.wtc.gwt.TuxedoConnection;
import weblogic.wtc.gwt.TuxedoConnectionFactory;
import weblogic.wtc.gwt.WTCService;
import weblogic.wtc.gwt.XmlFmlCnv;
import weblogic.wtc.jatmi.DequeueReply;
import weblogic.wtc.jatmi.EnqueueRequest;
import weblogic.wtc.jatmi.FldTbl;
import weblogic.wtc.jatmi.QueueTimeField;
import weblogic.wtc.jatmi.Reply;
import weblogic.wtc.jatmi.TPException;
import weblogic.wtc.jatmi.TypedCArray;
import weblogic.wtc.jatmi.TypedFML;
import weblogic.wtc.jatmi.TypedFML32;
import weblogic.wtc.jatmi.TypedString;
import weblogic.wtc.jatmi.TypedXML;

public final class tBexec extends Thread {
   private QueueConnectionFactory qconFactory;
   private QueueConnection qcon;
   private QueueSession sendSession;
   private QueueSession recvSession;
   private QueueSession errorSession;
   private QueueReceiver qreceiver;
   private QueueSender qsender;
   private QueueSender qerror;
   private Queue queue;
   private TextMessage jmsSendMsg;
   private BytesMessage jmsSendBytes;
   private Message jmsRecvMsg;
   private Message jmsErrorMsg;
   private static int threadCount = 0;
   private int threadNumber;
   private static tBstartArgs sArgs;
   private static tBparseArgs x = null;

   public tBexec() {
      this.threadNumber = ++threadCount;
   }

   public static void tBmain(WTCServerMBean var0) throws TPException {
      boolean var3 = ntrace.isTraceEnabled(1);
      if (var3) {
         ntrace.doTrace("[tBexec/");
      }

      if (x == null) {
         x = new tBparseArgs();
      }

      try {
         sArgs = x.parseMBeans(var0);
      } catch (TPException var6) {
         WTCLogger.logtBparsefailed(var6.getMessage());
         if (var3) {
            ntrace.doTrace("*]/tBexec/05/TPException: could not parse MBeans");
         }

         throw var6;
      }

      if (var3) {
         ntrace.doTrace("/tBexec/configuration parse complete");
      }

      if (sArgs.redirect == -1) {
         Loggable var4 = WTCLogger.logtBNOredirectsLoggable();
         var4.log();
         throw new TPException(4, var4.getMessage());
      } else {
         try {
            sleep(1000L);
         } catch (InterruptedException var5) {
         }

         for(int var1 = 1; var1 <= sArgs.redirect; ++var1) {
            if (var3) {
               ntrace.doTrace("/tBexec/t#" + var1 + " starting...");
            }

            (new tBexec()).start();
         }

         if (var3) {
            ntrace.doTrace("]/tBexec/10/");
         }

      }
   }

   public static void tBupdateGlobal(WTCtBridgeGlobalMBean var0) throws TPException {
      boolean var3 = ntrace.isTraceEnabled(1);
      if (var3) {
         ntrace.doTrace("[/tBexec/tBupdateGlobal(WTCtBridgeGlobal " + var0 + ")");
      }

      if (x == null) {
         x = new tBparseArgs();
      }

      try {
         sArgs = x.parseGlobal(var0);
      } catch (TPException var5) {
         if (var3) {
            ntrace.doTrace("*]/tBexec/tBupdateGlobal(10) TPException: could not parse MBeans");
         }

         WTCLogger.logtBparsefailed(var5.getMessage());
         throw var5;
      }

      if (var3) {
         ntrace.doTrace("global configuration parse complete");
      }

      if (var3) {
         ntrace.doTrace("]/tBexec/tBupdateGlobal(20) return success");
      }

   }

   public static void tBupdateRedirect(WTCtBridgeRedirectMBean var0) throws TPException {
      boolean var3 = ntrace.isTraceEnabled(1);
      if (var3) {
         ntrace.doTrace("[/tBexec/tBupdateRedirect(redirect " + var0 + ")");
      }

      if (x == null) {
         x = new tBparseArgs();
      }

      try {
         sArgs = x.parseRedirect(var0);
      } catch (TPException var5) {
         if (var3) {
            ntrace.doTrace("*]/tBexec/tBupdateRedirect(10) TPException: could not parse WTCtBridgeRedirectMBeans");
         }

         WTCLogger.logtBparsefailed(var5.getMessage());
         throw var5;
      }

      if (var3) {
         ntrace.doTrace("redirect configuration parse complete");
      }

      if (var3) {
         ntrace.doTrace("]/tBexec/tBupdateRedirect(20) return success");
      }

   }

   public static boolean tBactivate() {
      boolean var2 = ntrace.isTraceEnabled(1);
      if (var2) {
         ntrace.doTrace("[/tBexec/tBactivate()");
      }

      if (x == null) {
         if (var2) {
            ntrace.doTrace("]/tBexec/tBactivate(10) returns false");
         }

         return false;
      } else {
         sArgs = x.getParsedMBeans();
         if (sArgs.redirect == -1) {
            Loggable var3 = WTCLogger.logtBNOredirectsLoggable();
            var3.log();
            if (var2) {
               ntrace.doTrace("]/tBexec/tBactivate(20) returns false");
            }

            return false;
         } else {
            try {
               sleep(1000L);
            } catch (InterruptedException var4) {
            }

            for(int var0 = 1; var0 <= sArgs.redirect; ++var0) {
               if (var2) {
                  ntrace.doTrace("tBactivate: t#" + var0 + " starting...");
               }

               (new tBexec()).start();
            }

            if (var2) {
               ntrace.doTrace("]/tBexec/tBactivate(30) return true");
            }

            return true;
         }
      }
   }

   public void run() {
      boolean var1 = ntrace.isTraceEnabled(1);
      if (sArgs.direction[this.threadNumber - 1] == 1) {
         if (var1) {
            ntrace.doTrace("/tBexec>run/t#" + this.threadNumber + ": TuxQ2JmsQ");
         }

         try {
            this.tuxQ2jmsQ(this.threadNumber - 1);
         } catch (Exception var6) {
            var6.printStackTrace();
            if (var1) {
               ntrace.doTrace("/tBexec>run/t#" + this.threadNumber + ": TuxQ2JmsQ " + var6);
            }
         }
      } else if (sArgs.direction[this.threadNumber - 1] == 2) {
         if (var1) {
            ntrace.doTrace("/tBexec>run/t#" + this.threadNumber + ": JmsQ2TuxQ");
         }

         try {
            this.jmsQ2tuxQ(this.threadNumber - 1);
         } catch (Exception var5) {
            var5.printStackTrace();
            if (var1) {
               ntrace.doTrace("/tBexec>run/t#" + this.threadNumber + ": JmsQ2TuxQ " + var5);
            }
         }
      } else if (sArgs.direction[this.threadNumber - 1] == 3) {
         if (var1) {
            ntrace.doTrace("/tBexec>run/t#" + this.threadNumber + ": JmsQ2TuxS");
         }

         try {
            this.jmsQ2tuxS(this.threadNumber - 1);
         } catch (Exception var4) {
            var4.printStackTrace();
            if (var1) {
               ntrace.doTrace("/tBexec>run/t#" + this.threadNumber + ": JmsQ2TuxS " + var4);
            }
         }
      } else if (sArgs.direction[this.threadNumber - 1] == 4) {
         if (var1) {
            ntrace.doTrace("/tBexec>run/t#" + this.threadNumber + ": JmsQ2JmsQ");
         }

         try {
            this.jmsQ2jmsQ(this.threadNumber - 1);
         } catch (Exception var3) {
            var3.printStackTrace();
            if (var1) {
               ntrace.doTrace("/tBexec>run/t#" + this.threadNumber + ": JmsQ2JmsQ " + var3);
            }
         }
      } else if (var1) {
         ntrace.doTrace("/tBexec>run/t#" + this.threadNumber + ": BadLogic/10/");
      }

      if (var1) {
         ntrace.doTrace("]tBexec<run/t#" + this.threadNumber + ": exited.");
      }

   }

   private void tuxQ2jmsQ(int var1) throws Exception, NamingException {
      boolean var2 = ntrace.isTraceEnabled(1);
      if (var2) {
         ntrace.doTrace("[tBexec/tuxQ2jmsQ/");
      }

      TypedString var4 = null;
      TypedFML32 var5 = null;
      TypedFML var6 = null;
      TypedCArray var7 = null;
      TypedXML var8 = null;
      Object var9 = null;
      String var10 = null;
      int var11 = var1 + 1;
      TuxedoConnection var16 = null;
      DequeueReply var17 = null;
      Object var18 = null;
      Object var19 = null;
      boolean var21 = true;
      boolean var22 = true;
      boolean var23 = false;
      boolean var24 = false;
      byte[] var20 = new byte[32];

      for(int var26 = 0; var26 < 32; ++var26) {
         var20[var26] = 0;
      }

      InitialContext var54 = new InitialContext();

      try {
         this.jmsSendInit(var54, sArgs.targetName[var1], sArgs.jmsFactory);
      } catch (JMSException var53) {
         if (var2) {
            ntrace.doTrace("]/tBexec/tuxQ2jmsQ/JMS targetName failed: " + sArgs.targetName[var1]);
         }

         WTCLogger.logtBJMStargetNamefailed();
         sArgs.operational[var1] = false;
      }

      InitialContext var27 = new InitialContext();

      try {
         this.jmsErrorInit(var27, sArgs.wlsErrorDestination, sArgs.jmsFactory);
         var24 = true;
      } catch (Exception var52) {
         WTCLogger.logtBJMSerrorDestinationfailed();
         if (var2) {
            ntrace.doTrace("]/tBexec/tuxQ2jmsQ/JMS Error Destination failed: " + sArgs.wlsErrorDestination);
         }
      }

      InitialContext var28 = new InitialContext();

      try {
         TuxedoConnectionFactory var15 = (TuxedoConnectionFactory)var28.lookup(sArgs.tuxFactory);
         var16 = var15.getTuxedoConnection();
      } catch (NamingException var42) {
         WTCLogger.logtBNOTuxedoConnectionFactory();
         throw new TPException(6, ">tBexec/tuxQ2jmsQ/t#" + var11 + " Could not get " + sArgs.tuxFactory + ": " + var42);
      }

      if (sArgs.operational[var1] && var16 != null) {
         this.qcon.start();
         if (var2) {
            ntrace.doTrace("/tBexec/tuxQ2jmsQ/t#" + var11 + " ready.");
         }
      } else {
         sArgs.operational[var1] = false;
         if (var2) {
            ntrace.doTrace("/tBexec/tuxQ2jmsQ/t#" + var11 + " SHUTDOWN THREAD.");
         }
      }

      while(true) {
         while(true) {
            TransactionManager var25;
            while(true) {
               if (!sArgs.operational[var1]) {
                  if (var16 != null) {
                     var16.tpterm();
                  }

                  try {
                     this.jmsSendClose();
                  } catch (JMSException var33) {
                  }

                  try {
                     this.jmsErrorClose();
                  } catch (JMSException var32) {
                  }

                  if (this.qcon != null) {
                     this.qcon.close();
                  }

                  if (var2) {
                     ntrace.doTrace("]/tBexec/tuxQ2jmsQ/t#" + var11 + " shutdown.");
                  }

                  return;
               }

               var25 = TxHelper.getTransactionManager();
               var25.setTransactionTimeout(sArgs.timeout);
               var25.begin("WTC");
               if (var2) {
                  ntrace.doTrace("/tBexec/tuxQ2jmsQ/t#" + var11 + " transaction started.");
               }

               byte var29 = 32;

               try {
                  if (var2) {
                     ntrace.doTrace("/tBexec/tuxQ2jmsQ/t#" + var11 + " about to try tpdequeue");
                  }

                  var17 = var16.tpdequeue(sArgs.sourceQspace[var1], sArgs.sourceName[var1], (byte[])var18, (byte[])var19, var22, var23, var29);
                  if (var17 == null) {
                     if (var2) {
                        ntrace.doTrace("/tBexec/tuxQ2jmsQ/t#" + var11 + " Invalid NULL tpdequeue...retry");
                     }

                     var25.rollback();
                  } else {
                     if (var17.getReplyBuffer() != null) {
                        var10 = var17.getReplyBuffer().getType();
                     } else {
                        var10 = "STRING";
                     }

                     if (var2) {
                        ntrace.doTrace("/tBexec/tuxQ2jmsQ/t#" + var11 + " tuxType:" + var10);
                     }

                     if (var10.equalsIgnoreCase("STRING")) {
                        var4 = (TypedString)var17.getReplyBuffer();
                        break;
                     }

                     if (var10.equalsIgnoreCase("FML")) {
                        var6 = (TypedFML)var17.getReplyBuffer();
                        break;
                     }

                     if (var10.equalsIgnoreCase("FML32")) {
                        var5 = (TypedFML32)var17.getReplyBuffer();
                        break;
                     }

                     if (var10.equalsIgnoreCase("CARRAY")) {
                        var7 = (TypedCArray)var17.getReplyBuffer();
                        break;
                     }

                     if (var10.equalsIgnoreCase("XML")) {
                        var8 = (TypedXML)var17.getReplyBuffer();
                        break;
                     }

                     if (var2) {
                        ntrace.doTrace("/tBexec/tuxQ2jmsQ/t#" + var11 + " Unknown buffer type ingnored: " + var10);
                     }

                     var25.rollback();

                     try {
                        sleep((long)sArgs.retryDelay);
                     } catch (InterruptedException var41) {
                     }
                  }
               } catch (TPException var44) {
                  if (var2) {
                     ntrace.doTrace("/tBexec/tuxQ2jmsQ/TPException " + var44);
                  }

                  if (var44.gettperrno() == 13) {
                     if (var2) {
                        ntrace.doTrace("/tBexec/tuxQ2jmsQ/timeout on: " + sArgs.sourceQspace[var1]);
                     }
                  } else if (var2) {
                     ntrace.doTrace("/tBexec/tuxQ2jmsQ/failed: " + sArgs.sourceQspace[var1]);
                  }

                  try {
                     var25.rollback();
                  } catch (Exception var43) {
                     if (var2) {
                        ntrace.doTrace("/tBexec/tuxQ2jmsQ/TPException Rollback" + var43);
                     }
                  }

                  try {
                     sleep((long)sArgs.retryDelay);
                  } catch (InterruptedException var40) {
                  }
               } catch (Exception var45) {
                  if (var2) {
                     ntrace.doTrace("/tBexec/tuxQ2jmsQ/Not TPException" + var45);
                  }

                  try {
                     sleep((long)sArgs.retryDelay);
                  } catch (InterruptedException var39) {
                  }
               }
            }

            if (var2) {
               ntrace.doTrace("/tBexec/tuxQ2jmsQ/t#" + var11 + " Tux Msg received.");
            }

            this.showTuxMsg(var17);
            int var12;
            Integer var14;
            if ((var14 = var17.getpriority()) == null) {
               var12 = 1;
            } else {
               var12 = var14;
            }

            int var13 = sArgs.pMapTuxToJms[var12];
            if (var2) {
               ntrace.doTrace("/tBexec/tuxQ2jmsQ/t#" + var11 + " TPriority:" + var12 + " JPriority:" + var13);
            }

            if (var17.getcorrid() != null) {
               this.jmsSendMsg.setJMSCorrelationIDAsBytes(var17.getcorrid());
            } else {
               this.jmsSendMsg.setJMSCorrelationIDAsBytes(var20);
            }

            if (var10.equalsIgnoreCase("STRING")) {
               if (var17.getReplyBuffer() != null) {
                  this.jmsSendMsg.setText(var4.toString());
               } else {
                  this.jmsSendMsg.setText((String)null);
               }
            } else {
               TextMessage var30;
               if (var10.equalsIgnoreCase("FML")) {
                  var30 = this.FML2jms(var6, this.jmsSendMsg);
                  if (var30 == null) {
                     WTCLogger.logtBSlashQFML2XMLFailed();
                     if (var2) {
                        ntrace.doTrace("/tBexec/tuxQ2jmsQ/t#" + var11 + " translation to FML failed");
                     }

                     try {
                        var25.rollback();
                     } catch (Exception var51) {
                        if (var2) {
                           ntrace.doTrace("/tBexec/tuxQ2jmsQ/FML Rollback" + var51);
                        }
                     }

                     try {
                        sleep((long)sArgs.retryDelay);
                     } catch (InterruptedException var38) {
                     }
                     continue;
                  }

                  if (var2) {
                     ntrace.doTrace(">tBexec/tuxQ2jmsQ/t#" + var11 + "translation from FML complete: " + this.jmsSendMsg.getText());
                  }
               } else if (var10.equalsIgnoreCase("FML32")) {
                  var30 = this.FML322jms(var5, this.jmsSendMsg);
                  if (var30 == null) {
                     WTCLogger.logtBSlashQFML322XMLFailed();
                     if (var2) {
                        ntrace.doTrace("/tBexec/tuxQ2jmsQ/t#" + var11 + " translation to FML32 failed");
                     }

                     try {
                        var25.rollback();
                     } catch (Exception var50) {
                        if (var2) {
                           ntrace.doTrace("/tBexec/tuxQ2jmsQ/FML32 Rollback" + var50);
                        }
                     }

                     try {
                        sleep((long)sArgs.retryDelay);
                     } catch (InterruptedException var37) {
                     }
                     continue;
                  }

                  if (var2) {
                     ntrace.doTrace("/tBexec/tuxQ2jmsQ/t#" + var11 + "translation from FML32 complete: " + this.jmsSendMsg.getText());
                  }
               } else if (var10.equalsIgnoreCase("CARRAY")) {
                  this.jmsSendBytes.clearBody();
                  if (var17.getcorrid() != null) {
                     this.jmsSendBytes.setJMSCorrelationIDAsBytes(var17.getcorrid());
                  } else {
                     this.jmsSendBytes.setJMSCorrelationIDAsBytes(var20);
                  }

                  this.jmsSendBytes.writeBytes(var7.carray);
               } else if (var10.equalsIgnoreCase("XML")) {
                  this.jmsSendMsg.setText(var8.toString());
               }
            }

            try {
               if (var10.equalsIgnoreCase("CARRAY")) {
                  var21 = this.jmsSend(this.jmsSendBytes, var13);
               } else {
                  var21 = this.jmsSend(this.jmsSendMsg, var13);
               }

               if (var21) {
                  var25.commit();
               } else {
                  var25.rollback();

                  try {
                     sleep((long)sArgs.retryDelay);
                  } catch (InterruptedException var36) {
                  }
               }
            } catch (JMSException var48) {
               if (var2) {
                  ntrace.doTrace("/tBexec/tuxQ2jmsQ/JMS send failed: " + sArgs.targetName[var1]);
               }

               try {
                  var25.rollback();
               } catch (Exception var46) {
                  if (var2) {
                     ntrace.doTrace("/tBexec/tuxQ2jmsQ/Rollback failed in JMSException " + var46);
                  }
               }

               try {
                  sleep((long)sArgs.retryDelay);
               } catch (InterruptedException var35) {
               }
            } catch (Exception var49) {
               var49.printStackTrace();
               if (var2) {
                  ntrace.doTrace("/tBexec/tuxQ2jmsQ/JMS send exception: " + sArgs.targetName[var1]);
               }

               try {
                  var25.rollback();
               } catch (Exception var47) {
                  if (var2) {
                     ntrace.doTrace("/tBexec/tuxQ2jmsQ/Rollback failed Exception of JMSException " + var47);
                  }
               }

               try {
                  sleep((long)sArgs.retryDelay);
               } catch (InterruptedException var34) {
               }
            }
         }
      }
   }

   private void jmsQ2tuxQ(int var1) throws Exception, NamingException {
      boolean var2 = ntrace.isTraceEnabled(1);
      if (var2) {
         ntrace.doTrace("[/tBexec/jmsQ2tuxQ/");
      }

      TypedString var4 = null;
      TypedFML32 var5 = null;
      TypedCArray var6 = null;
      int var7 = var1 + 1;
      boolean var13 = false;
      InitialContext var14 = new InitialContext();

      try {
         this.jmsRecvInit(var14, sArgs.sourceName[var1], sArgs.jmsFactory);
      } catch (Exception var42) {
         if (var2) {
            ntrace.doTrace("/tBexec/jmsQ2tuxQ/JMS recv initialization failed: " + sArgs.sourceName[var1]);
         }

         WTCLogger.logtBJMSsourceNamefailed();
         sArgs.operational[var1] = false;
      }

      InitialContext var15 = new InitialContext();

      try {
         this.jmsErrorInit(var15, sArgs.wlsErrorDestination, sArgs.jmsFactory);
         var13 = true;
      } catch (Exception var41) {
         WTCLogger.logtBJMSerrorDestinationfailed();
         if (var2) {
            ntrace.doTrace("]/tBexec/jmsQ2tuxQ/JMS Error Destination failed: " + sArgs.wlsErrorDestination);
         }
      }

      InitialContext var16 = new InitialContext();

      TuxedoConnection var11;
      try {
         TuxedoConnectionFactory var10 = (TuxedoConnectionFactory)var16.lookup(sArgs.tuxFactory);
         var11 = var10.getTuxedoConnection();
      } catch (NamingException var37) {
         WTCLogger.logtBNOTuxedoConnectionFactory();
         throw new TPException(6, ">tBexec/jmsQ2tuxQ/t#" + var7 + " Could not get " + sArgs.tuxFactory + ": " + var37);
      }

      if (sArgs.operational[var1] && var11 != null) {
         this.qcon.start();
         if (var2) {
            ntrace.doTrace("/tBexec/jmsQ2tuxQ/t#" + var7 + " ready.");
         }
      } else {
         sArgs.operational[var1] = false;
         if (var2) {
            ntrace.doTrace("/tBexec/jmsQ2tuxQ/t#" + var7 + " SHUTDOWN THREAD.");
         }
      }

      while(sArgs.operational[var1]) {
         try {
            this.jmsRecvMsg = this.jmsReceive();
         } catch (Exception var40) {
            if (var2) {
               ntrace.doTrace("/tBexec/jmsQ2tuxQ/JMS recv failed: " + sArgs.sourceName[var1]);
            }

            try {
               sleep((long)sArgs.retryDelay);
            } catch (InterruptedException var36) {
            }
            continue;
         }

         if (var2) {
            ntrace.doTrace("/tBexec/jmsQ2tuxQ/t#" + var7 + " Got JmsRecvMsg: " + this.jmsRecvMsg);
         }

         if (sArgs.translateFML[var1].equalsIgnoreCase("FLAT")) {
            var5 = this.jms2FML32(this.jmsRecvMsg);
            if (var5 == null) {
               WTCLogger.logtBInternalTranslationFailed();
               if (var2) {
                  ntrace.doTrace("/tBexec/jmsQ2tuxQ/t#" + var7 + " translation to FML32 failed");
               }

               if (var13) {
                  if (this.jmsError(this.jmsRecvMsg, this.jmsRecvMsg.getJMSPriority())) {
                     this.jmsRecvMsg.acknowledge();
                     WTCLogger.logtBsent2errorDestination();
                     if (var2) {
                        ntrace.doTrace("/tBexec/jmsQ2tuxQ/t#" + var7 + " Msg sent to error queue.");
                     }
                  } else {
                     WTCLogger.logtBsent2errorDestinationfailed();
                     if (var2) {
                        ntrace.doTrace("/tBexec/jmsQ2tuxQ/t#" + var7 + " Failed to sent to error queue.");
                     }
                  }
               }

               try {
                  sleep((long)sArgs.retryDelay);
               } catch (InterruptedException var35) {
               }
               continue;
            }

            if (var2) {
               ntrace.doTrace("/tBexec/jmsQ2tuxQ/t#" + var7 + " translation to FML32 complete");
            }
         } else {
            if (sArgs.translateFML[var1].equalsIgnoreCase("WLXT")) {
               WTCLogger.logtBNOWLXToptionAvailable();
               if (var2) {
                  ntrace.doTrace("/tBexec/jmsQ2jmsQ/WLXT option unavailable.");
               }

               try {
                  sleep((long)sArgs.retryDelay);
               } catch (InterruptedException var34) {
               }
               continue;
            }

            if (this.jmsRecvMsg instanceof TextMessage) {
               var4 = this.jms2tuxString(this.jmsRecvMsg);
            } else {
               if (!(this.jmsRecvMsg instanceof BytesMessage)) {
                  if (var2) {
                     ntrace.doTrace("/tBexec/jmsQ2tuxQ/Unsupported JMS Message Type.");
                  }

                  WTCLogger.logtBunsupportedJMSmsgtype();

                  try {
                     sleep((long)sArgs.retryDelay);
                  } catch (InterruptedException var33) {
                  }
                  continue;
               }

               var6 = this.jms2tuxCArray(this.jmsRecvMsg);
            }
         }

         int var8 = this.jmsRecvMsg.getJMSPriority();
         int var9 = sArgs.pMapJmsToTux[var8];
         Integer var17 = new Integer(var9);
         Object var18 = null;
         Object var19 = null;
         byte var20 = 1;
         byte var21 = 1;
         byte var22 = 0;
         Object var23 = null;
         byte[] var24 = this.jmsRecvMsg.getJMSCorrelationIDAsBytes();
         String var25 = null;
         var25 = sArgs.replyQ[var1];
         EnqueueRequest var26 = new EnqueueRequest((QueueTimeField)var18, var17, (QueueTimeField)var19, var20, var21, (byte[])var23, var24, var25, sArgs.tuxErrorQueue, false, false, var22);
         if (var2) {
            ntrace.doTrace("/tBexec/jmsQ2tuxQ/t#" + var7 + " about to try tpenqueue");
         }

         try {
            if (sArgs.translateFML[var1].equalsIgnoreCase("NO")) {
               if (this.jmsRecvMsg instanceof TextMessage) {
                  var11.tpenqueue(sArgs.targetQspace[var1], sArgs.targetName[var1], var26, var4, 0);
               } else {
                  var11.tpenqueue(sArgs.targetQspace[var1], sArgs.targetName[var1], var26, var6, 0);
               }
            } else {
               var11.tpenqueue(sArgs.targetQspace[var1], sArgs.targetName[var1], var26, var5, 0);
            }

            this.jmsRecvMsg.acknowledge();
            if (var2) {
               ntrace.doTrace("/tBexec/jmsQ2tuxQ/t#" + var7 + " Tux Msg queued.");
            }
         } catch (TPException var38) {
            if (var2) {
               ntrace.doTrace("/tBexec/jmsQ2tuxQ/t#" + var7 + " TPException explanation: " + var38);
            }

            if (var13) {
               if (this.jmsError(this.jmsRecvMsg, this.jmsRecvMsg.getJMSPriority())) {
                  this.jmsRecvMsg.acknowledge();
                  if (var38.gettperrno() == 6) {
                     WTCLogger.logErrorFail2FindImportedQSpace(sArgs.targetQspace[var1]);
                  } else {
                     WTCLogger.logtBsent2errorDestination();
                  }

                  if (var2) {
                     ntrace.doTrace("/tBexec/jmsQ2tuxQ/t#" + var7 + " Msg sent to error queue.");
                  }
               } else {
                  WTCLogger.logtBsent2errorDestinationfailed();
                  if (var2) {
                     ntrace.doTrace("/tBexec/jmsQ2tuxQ/t#" + var7 + " Failed to sent to error queue.");
                  }
               }
            }

            try {
               sleep((long)sArgs.retryDelay);
            } catch (InterruptedException var32) {
            }

            this.recvSession.recover();
         } catch (Exception var39) {
            if (var2) {
               ntrace.doTrace("/tBexec/jmsQ2tuxQ/t#" + var7 + " Exception " + var39);
            }

            try {
               sleep((long)sArgs.retryDelay);
            } catch (InterruptedException var31) {
            }

            this.recvSession.recover();
         }
      }

      if (var11 != null) {
         var11.tpterm();
      }

      try {
         this.jmsRecvClose();
      } catch (JMSException var30) {
      }

      try {
         this.jmsErrorClose();
      } catch (JMSException var29) {
      }

      if (this.qcon != null) {
         this.qcon.close();
      }

      if (var2) {
         ntrace.doTrace("]/tBexec/jmsQ2tuxQ/t#" + var7 + " shutdown.");
      }

   }

   private void jmsQ2tuxS(int var1) throws Exception, NamingException {
      boolean var2 = ntrace.isTraceEnabled(1);
      if (var2) {
         ntrace.doTrace("[/tBexec/jmsQ2tuxS/redirectIndex = " + var1);
      }

      int var4 = var1 + 1;
      boolean var5 = true;
      String var8 = null;
      TypedString var10 = null;
      TypedFML32 var11 = null;
      TypedFML var12 = null;
      TypedCArray var13 = null;
      Reply var15 = null;
      boolean var16 = false;
      boolean var17 = true;
      InitialContext var18 = new InitialContext();

      try {
         this.jmsRecvInit(var18, sArgs.sourceName[var1], sArgs.jmsFactory);
      } catch (JMSException var44) {
         if (var2) {
            ntrace.doTrace("/tBexec/jmsQ2tuxS/sourceName failed: " + sArgs.sourceName[var1]);
         }

         WTCLogger.logtBJMStargetNamefailed();
         sArgs.operational[var1] = false;
      }

      InitialContext var19 = new InitialContext();

      try {
         this.jmsSendInit(var19, sArgs.replyQ[var1], sArgs.jmsFactory);
      } catch (JMSException var43) {
         var43.printStackTrace();
         if (var2) {
            ntrace.doTrace("/tBexec/jmsQ2tuxS/replyQ failed: " + sArgs.replyQ[var1]);
         }

         WTCLogger.logtBJMStargetNamefailed();
         sArgs.operational[var1] = false;
      }

      InitialContext var20 = new InitialContext();

      try {
         this.jmsErrorInit(var20, sArgs.wlsErrorDestination, sArgs.jmsFactory);
         var16 = true;
      } catch (Exception var42) {
         WTCLogger.logtBJMSerrorDestinationfailed();
         if (var2) {
            ntrace.doTrace("]/tBexec/jmsQ2tuxS/JMS Error Destination failed: " + sArgs.wlsErrorDestination);
         }
      }

      InitialContext var21 = new InitialContext();

      TuxedoConnection var7;
      try {
         TuxedoConnectionFactory var6 = (TuxedoConnectionFactory)var21.lookup(sArgs.tuxFactory);
         var7 = var6.getTuxedoConnection();
      } catch (NamingException var38) {
         WTCLogger.logtBNOTuxedoConnectionFactory();
         throw new TPException(6, ">tBexec/jmsQ2tuxS/t#" + var4 + " Could not get " + sArgs.tuxFactory + ": " + var38);
      }

      if (sArgs.operational[var1] && var7 != null) {
         this.qcon.start();
         if (var2) {
            ntrace.doTrace("/tBexec/jmsQ2tuxS/t#" + var4 + " ready.");
         }
      } else {
         sArgs.operational[var1] = false;
         if (var2) {
            ntrace.doTrace("/tBexec/jmsQ2tuxS/t#" + var4 + " SHUTDOWN THREAD.");
         }
      }

      while(sArgs.operational[var1]) {
         int var22 = 0;

         try {
            this.jmsRecvMsg = this.jmsReceive();
         } catch (Exception var39) {
            if (var2) {
               ntrace.doTrace("/tBexec/jmsQ2tuxS/JMS receive failed: " + sArgs.sourceName[var1]);
            }

            try {
               sleep((long)sArgs.retryDelay);
            } catch (InterruptedException var37) {
            }
            continue;
         }

         int var45 = this.jmsRecvMsg.getJMSPriority();
         if (var2) {
            ntrace.doTrace("/tBexec/jmsQ2tuxS/t#" + var4 + " Got JmsRecvMsg: " + this.jmsRecvMsg);
         }

         if (sArgs.translateFML[var1].equalsIgnoreCase("FLAT")) {
            var11 = this.jms2FML32(this.jmsRecvMsg);
            if (var11 == null) {
               WTCLogger.logtBInternalTranslationFailed();
               if (var2) {
                  ntrace.doTrace("/tBexec/jmsQ2tuxS/t#" + var4 + " translation to FML32 failed.");
               }

               if (var16) {
                  if (this.jmsError(this.jmsRecvMsg, this.jmsRecvMsg.getJMSPriority())) {
                     this.jmsRecvMsg.acknowledge();
                     WTCLogger.logtBsent2errorDestination();
                     if (var2) {
                        ntrace.doTrace("/tBexec/jmsQ2tuxS/t#" + var4 + " Msg sent to error queue.");
                     }
                  } else {
                     WTCLogger.logtBsent2errorDestinationfailed();
                     if (var2) {
                        ntrace.doTrace("/tBexec/jmsQ2tuxS/t#" + var4 + " Failed to sent to error queue.");
                     }
                  }
               }

               try {
                  sleep((long)sArgs.retryDelay);
               } catch (InterruptedException var36) {
               }
               continue;
            }

            if (var2) {
               ntrace.doTrace("/tBexec/jmsQ2tuxS/t#" + var4 + " translation to FML32 complete.");
            }
         } else {
            if (sArgs.translateFML[var1].equalsIgnoreCase("WLXT")) {
               WTCLogger.logtBNOWLXToptionAvailable();
               if (var2) {
                  ntrace.doTrace("/tBexec/jmsQ2tuxS/WLXT option unavailable ");
               }

               try {
                  sleep((long)sArgs.retryDelay);
               } catch (InterruptedException var35) {
               }
               continue;
            }

            if (this.jmsRecvMsg instanceof TextMessage) {
               var10 = this.jms2tuxString(this.jmsRecvMsg);
            } else {
               if (!(this.jmsRecvMsg instanceof BytesMessage)) {
                  if (var2) {
                     ntrace.doTrace("/tBexec/jmsQ2jmsQ/Unsupported JMS Message Type.");
                  }

                  WTCLogger.logtBunsupportedJMSmsgtype();

                  try {
                     sleep((long)sArgs.retryDelay);
                  } catch (InterruptedException var34) {
                  }
                  continue;
               }

               var13 = this.jms2tuxCArray(this.jmsRecvMsg);
            }
         }

         if (var2) {
            ntrace.doTrace("/tBexec/jmsQ2tuxS/t#" + var4 + " About to call tpcall[" + sArgs.targetName[var1] + "]");
         }

         int var9 = 0;

         do {
            try {
               if (sArgs.translateFML[var1].equalsIgnoreCase("NO")) {
                  if (this.jmsRecvMsg instanceof TextMessage) {
                     var15 = var7.tpcall(sArgs.targetName[var1], var10, 0);
                  } else {
                     var15 = var7.tpcall(sArgs.targetName[var1], var13, 0);
                  }
               } else {
                  var15 = var7.tpcall(sArgs.targetName[var1], var11, 0);
               }
               break;
            } catch (TPException var40) {
               ++var9;
               var22 = var40.gettperrno();
               if (var2) {
                  ntrace.doTrace("/tBexec/jmsQ2tuxS/t#" + var4 + " tpcall threw TPException " + var40);
                  ntrace.doTrace("/tBexec/jmsQ2tuxS/t#" + var4 + " retry#: " + var9);
               }
            } catch (Exception var41) {
               ++var9;
               if (var2) {
                  ntrace.doTrace("*]/tBexec/jmsQ2tuxS/t#" + var4 + " tpcall threw Exception " + var41);
               }
            }

            try {
               sleep((long)sArgs.retryDelay);
            } catch (InterruptedException var33) {
            }
         } while(var9 <= sArgs.retries);

         if (var9 > sArgs.retries) {
            if (var2) {
               ntrace.doTrace("*]/tBexec/jmsQ2tuxS/t#" + var4 + " retry count exhausted.");
            }

            if (var16) {
               if (this.jmsError(this.jmsRecvMsg, this.jmsRecvMsg.getJMSPriority())) {
                  this.jmsRecvMsg.acknowledge();
                  if (var22 != 6) {
                     WTCLogger.logtBsent2errorDestination();
                  } else {
                     WTCLogger.logErrorTbNoSuchImport(sArgs.targetName[var1]);
                  }

                  if (var2) {
                     ntrace.doTrace("/tBexec/jmsQ2tuxS/t#" + var4 + " Msg sent to error queue.");
                  }
               } else {
                  WTCLogger.logtBsent2errorDestinationfailed();
                  if (var2) {
                     ntrace.doTrace("/tBexec/jmsQ2tuxS/t#" + var4 + " Failed to sent to error queue.");
                  }
               }
            }

            try {
               sleep((long)sArgs.retryDelay);
            } catch (InterruptedException var32) {
            }
         } else {
            if (var15.getReplyBuffer() != null) {
               var8 = var15.getReplyBuffer().getType();
            } else {
               var8 = "STRING";
            }

            if (var2) {
               ntrace.doTrace("/tBexec/jmsQ2tuxS/t#" + var4 + " tuxType:" + var8);
            }

            if (var8.equalsIgnoreCase("STRING")) {
               var10 = (TypedString)var15.getReplyBuffer();
            } else if (var8.equalsIgnoreCase("FML")) {
               var12 = (TypedFML)var15.getReplyBuffer();
            } else if (var8.equalsIgnoreCase("FML32")) {
               var11 = (TypedFML32)var15.getReplyBuffer();
            } else {
               if (!var8.equalsIgnoreCase("CARRAY")) {
                  if (var2) {
                     ntrace.doTrace("/tBexec/jmsQ2tuxS/t#" + var4 + " Tux returned unknown buffer type: " + var8);
                  }

                  if (var16) {
                     if (this.jmsError(this.jmsRecvMsg, this.jmsRecvMsg.getJMSPriority())) {
                        this.jmsRecvMsg.acknowledge();
                        WTCLogger.logErrorTbUnsupportedBufferType(var8);
                        if (var2) {
                           ntrace.doTrace("/tBexec/jmsQ2tuxS/t#" + var4 + " Msg sent to error queue.");
                        }
                     } else {
                        WTCLogger.logtBsent2errorDestinationfailed();
                        if (var2) {
                           ntrace.doTrace("/tBexec/jmsQ2tuxS/t#" + var4 + " Failed to sent to error queue.");
                        }
                     }
                  }

                  try {
                     sleep((long)sArgs.retryDelay);
                  } catch (InterruptedException var31) {
                  }
                  continue;
               }

               var13 = (TypedCArray)var15.getReplyBuffer();
            }

            this.jmsSendMsg.setJMSCorrelationIDAsBytes(this.jmsRecvMsg.getJMSCorrelationIDAsBytes());
            if (var8.equalsIgnoreCase("STRING")) {
               if (var15.getReplyBuffer() != null) {
                  this.jmsSendMsg.setText(var10.toString());
               } else {
                  this.jmsSendMsg.setText((String)null);
               }
            } else if (var8.equalsIgnoreCase("FML")) {
               this.jmsSendMsg = this.FML2jms(var12, this.jmsSendMsg);
               if (this.jmsSendMsg == null) {
                  WTCLogger.logtBInternalFML2XMLFailed();
                  if (var2) {
                     ntrace.doTrace("/tBexec/jmsQ2tuxS/t#" + var4 + " translation to FML failed");
                  }

                  if (var16) {
                     if (this.jmsError(this.jmsRecvMsg, this.jmsRecvMsg.getJMSPriority())) {
                        this.jmsRecvMsg.acknowledge();
                        WTCLogger.logtBsent2errorDestination();
                        if (var2) {
                           ntrace.doTrace("/tBexec/jmsQ2tuxS/t#" + var4 + " Msg sent to error queue.");
                        }
                     } else {
                        WTCLogger.logtBsent2errorDestinationfailed();
                        if (var2) {
                           ntrace.doTrace("/tBexec/jmsQ2tuxS/t#" + var4 + " Failed to sent to error queue.");
                        }
                     }
                  }

                  try {
                     sleep((long)sArgs.retryDelay);
                  } catch (InterruptedException var30) {
                  }
                  continue;
               }

               if (var2) {
                  ntrace.doTrace("/tBexec/jmsQ2tuxS/t#" + var4 + " translation from FML complete: " + this.jmsSendMsg.getText());
               }
            } else if (var8.equalsIgnoreCase("FML32")) {
               this.jmsSendMsg = this.FML322jms(var11, this.jmsSendMsg);
               if (this.jmsSendMsg == null) {
                  WTCLogger.logtBInternalFML322XMLFailed();
                  if (var2) {
                     ntrace.doTrace("/tBexec/jmsQ2tuxS/t#" + var4 + " translation to FML32 failed");
                  }

                  if (var16) {
                     if (this.jmsError(this.jmsRecvMsg, this.jmsRecvMsg.getJMSPriority())) {
                        this.jmsRecvMsg.acknowledge();
                        WTCLogger.logtBsent2errorDestination();
                        if (var2) {
                           ntrace.doTrace("/tBexec/jmsQ2tuxS/t#" + var4 + " Msg sent to error queue.");
                        }
                     } else {
                        WTCLogger.logtBsent2errorDestinationfailed();
                        if (var2) {
                           ntrace.doTrace("/tBexec/jmsQ2tuxS/t#" + var4 + " Failed to sent to error queue.");
                        }
                     }
                  }

                  try {
                     sleep((long)sArgs.retryDelay);
                  } catch (InterruptedException var29) {
                  }
                  continue;
               }

               if (var2) {
                  ntrace.doTrace("/tBexec/jmsQ2tuxS/t#" + var4 + "translation from FML32 complete: " + this.jmsSendMsg.getText());
               }
            } else if (var8.equalsIgnoreCase("CARRAY")) {
               this.jmsSendBytes.clearBody();
               this.jmsSendBytes.writeBytes(var13.carray);
            }

            if (var8.equalsIgnoreCase("CARRAY")) {
               var17 = this.jmsSend(this.jmsSendBytes, var45);
            } else {
               var17 = this.jmsSend(this.jmsSendMsg, var45);
            }

            if (var17) {
               this.jmsRecvMsg.acknowledge();
            } else {
               if (var16) {
                  if (this.jmsError(this.jmsRecvMsg, this.jmsRecvMsg.getJMSPriority())) {
                     this.jmsRecvMsg.acknowledge();
                     WTCLogger.logErrorTbJmsSendFailure();
                     if (var2) {
                        ntrace.doTrace("/tBexec/jmsQ2tuxS/t#" + var4 + " Msg sent to error queue.");
                     }
                  } else {
                     WTCLogger.logtBsent2errorDestinationfailed();
                     if (var2) {
                        ntrace.doTrace("/tBexec/jmsQ2tuxS/t#" + var4 + " Failed to sent to error queue.");
                     }
                  }
               }

               try {
                  sleep((long)sArgs.retryDelay);
               } catch (InterruptedException var28) {
               }
            }
         }
      }

      if (var7 != null) {
         var7.tpterm();
      }

      try {
         this.jmsErrorClose();
      } catch (JMSException var27) {
      }

      try {
         this.jmsSendClose();
      } catch (JMSException var26) {
      }

      try {
         this.jmsRecvClose();
      } catch (JMSException var25) {
      }

      if (this.qcon != null) {
         this.qcon.close();
      }

      if (var2) {
         ntrace.doTrace("]/tBexec/jmsQ2tuxS/t#" + var4 + " shutdown.");
      }

   }

   private void jmsQ2jmsQ(int var1) throws Exception, NamingException {
      boolean var2 = ntrace.isTraceEnabled(1);
      if (var2) {
         ntrace.doTrace("[/tBexec/jmsQ2jmsQ/");
      }

      int var4 = var1 + 1;
      byte var5 = 1;
      InitialContext var9 = new InitialContext();

      try {
         this.jmsRecvInit(var9, sArgs.sourceName[var1], sArgs.jmsFactory);
      } catch (JMSException var20) {
         if (var2) {
            ntrace.doTrace("/tBexec/jmsQ2jmsQ/sourceName failed: " + sArgs.sourceName[var1]);
         }

         WTCLogger.logtBJMStargetNamefailed();
         sArgs.operational[var1] = false;
      }

      InitialContext var10 = new InitialContext();

      try {
         this.jmsSendInit(var10, sArgs.replyQ[var1], sArgs.jmsFactory);
      } catch (JMSException var19) {
         var19.printStackTrace();
         if (var2) {
            ntrace.doTrace("/tBexec/jmsQ2jmsQ/replyQ failed: " + sArgs.replyQ[var1]);
         }

         WTCLogger.logtBJMStargetNamefailed();
         sArgs.operational[var1] = false;
      }

      if (var2) {
         ntrace.doTrace("/tBexec/jmsQ2jmsQ/t#" + var4 + " ready.");
      }

      if (sArgs.operational[var1]) {
         this.qcon.start();
      }

      while(sArgs.operational[var1]) {
         try {
            this.jmsRecvMsg = this.jmsReceive();
         } catch (Exception var18) {
            var18.printStackTrace();
            if (var2) {
               ntrace.doTrace("/tBexec/jmsQ2jmsQ/t#" + var4 + " Exception " + var18);
            }
         }

         if (sArgs.translateFML[var1].equalsIgnoreCase("FLAT")) {
            TypedFML32 var7 = this.jms2FML32(this.jmsRecvMsg);
            if (var7 == null) {
               WTCLogger.logtBInternalTranslationFailed();
               if (var2) {
                  ntrace.doTrace("/tBexec/jmsQ2jmsQ/t#" + var4 + " translation to FML32 failed");
               }

               try {
                  sleep((long)sArgs.retryDelay);
               } catch (InterruptedException var17) {
               }
               continue;
            }

            if (var2) {
               ntrace.doTrace("/tBexec/jmsQ2jmsQ/t#" + var4 + " translation to FML32 complete");
            }

            this.jmsSendMsg = this.FML322jms(var7, (TextMessage)this.jmsRecvMsg);
            if (this.jmsSendMsg == null) {
               WTCLogger.logtBInternalTranslationFailed();
               if (var2) {
                  ntrace.doTrace("/tBexec/jmsQ2jmsQ/t#" + var4 + " translation to FML32 failed");
               }

               try {
                  sleep((long)sArgs.retryDelay);
               } catch (InterruptedException var16) {
               }
               continue;
            }

            if (var2) {
               ntrace.doTrace("/tBexec/jmsQ2jmsQ/t#" + var4 + " translation from FML32 complete: " + var7.toString());
            }
         } else {
            if (sArgs.translateFML[var1].equalsIgnoreCase("WLXT")) {
               WTCLogger.logtBNOWLXToptionAvailable();
               if (var2) {
                  ntrace.doTrace("/tBexec/jmsQ2jmsQ/WLXT option unavailable ");
               }

               try {
                  sleep((long)sArgs.retryDelay);
               } catch (InterruptedException var15) {
               }
               continue;
            }

            this.jmsSendMsg = (TextMessage)this.jmsRecvMsg;
         }

         if (this.jmsSend(this.jmsSendMsg, var5)) {
            this.jmsRecvMsg.acknowledge();
         } else {
            try {
               sleep((long)sArgs.retryDelay);
            } catch (InterruptedException var14) {
            }
         }
      }

      try {
         this.jmsSendClose();
      } catch (JMSException var13) {
      }

      try {
         this.jmsRecvClose();
      } catch (JMSException var12) {
      }

      if (this.qcon != null) {
         this.qcon.close();
      }

      if (var2) {
         ntrace.doTrace("]/tBexec/jmsQ2jmsQ/t#" + var4 + " shutdown.");
      }

   }

   public void jmsSendInit(Context var1, String var2, String var3) throws NamingException, JMSException {
      boolean var4 = ntrace.isTraceEnabled(1);
      if (var4) {
         ntrace.doTrace("[/tBexec/jmsSendInit/");
      }

      if (this.sendSession == null) {
         if (this.qcon == null) {
            this.qconFactory = (QueueConnectionFactory)var1.lookup(var3);
            this.qcon = this.qconFactory.createQueueConnection();
         }

         this.sendSession = this.qcon.createQueueSession(false, 1);
      }

      try {
         this.queue = (Queue)var1.lookup(var2);
         if (var4) {
            ntrace.doTrace("/tBexec/jmsSendInit/queue (" + var2 + ") located");
         }
      } catch (NamingException var6) {
         this.queue = this.sendSession.createQueue(var2);
         var1.bind(var2, this.queue);
         if (var4) {
            ntrace.doTrace("/tBexec/jmsSendInit/queue (" + var2 + ") created");
         }
      }

      this.qsender = this.sendSession.createSender(this.queue);
      this.jmsSendMsg = this.sendSession.createTextMessage();
      this.jmsSendBytes = this.sendSession.createBytesMessage();
      if (var4) {
         ntrace.doTrace("]/tBexec/jmsSendInit/10/");
      }

   }

   public boolean jmsSend(Message var1, int var2) throws JMSException {
      boolean var3 = ntrace.isTraceEnabled(1);
      if (var3) {
         ntrace.doTrace("[/tBexec/jmsSend/");
      }

      if (var3) {
         if (var1 instanceof TextMessage) {
            ntrace.doTrace("/tBexec/jmsSend/Jms Msg: " + ((TextMessage)var1).getText());
         } else {
            ntrace.doTrace("/tBexec/jmsSend/BytesMessage: ");
         }
      }

      int var4 = 0;

      while(true) {
         try {
            this.qsender.setPriority(var2);
            this.qsender.send(var1);
            break;
         } catch (Exception var8) {
            ++var4;
            if (var3) {
               ntrace.doTrace("/tBexec/jmsSend/Error - retry#: " + var4);
            }

            try {
               sleep((long)sArgs.retryDelay);
            } catch (InterruptedException var7) {
            }

            if (var4 > sArgs.retries) {
               break;
            }
         }
      }

      if (var4 > sArgs.retries) {
         if (var3) {
            ntrace.doTrace("*]/tBexec/jmsSend/JMS retry count exhausted.");
         }

         return false;
      } else {
         if (var3) {
            ntrace.doTrace("]/tBexec/jmsSend/10/");
         }

         return true;
      }
   }

   public void jmsSendClose() throws JMSException {
      boolean var1 = ntrace.isTraceEnabled(1);
      if (var1) {
         ntrace.doTrace("[/tBexec/jmsSendClose/");
      }

      if (this.qsender != null) {
         this.qsender.close();
      }

      if (this.sendSession != null) {
         this.sendSession.close();
      }

      if (var1) {
         ntrace.doTrace("]/tBexec/jmsSendClose/10/");
      }

   }

   public void jmsRecvInit(Context var1, String var2, String var3) throws NamingException, JMSException {
      boolean var4 = ntrace.isTraceEnabled(1);
      if (var4) {
         ntrace.doTrace("[/tBexec/jmsRecvInit/queueName = " + var2 + ", jmsFac = " + var3);
      }

      if (this.recvSession == null) {
         if (this.qcon == null) {
            this.qconFactory = (QueueConnectionFactory)var1.lookup(var3);
            this.qcon = this.qconFactory.createQueueConnection();
         }

         this.recvSession = this.qcon.createQueueSession(false, 2);
      }

      int var5;
      for(var5 = 0; var5 < 10; ++var5) {
         if (var4) {
            ntrace.doTrace("thread(" + this.threadNumber + "), thread id(" + this.getId() + "), lookup #" + var5);
         }

         try {
            this.queue = (Queue)var1.lookup(var2);
            if (var4) {
               ntrace.doTrace("/tBexec/jmsRecvInit/queue (" + var2 + ") located");
            }
            break;
         } catch (NamingException var8) {
            if (var4) {
               ntrace.doTrace("NamingException: " + var8);
            }
         } catch (Exception var9) {
            if (var4) {
               ntrace.doTrace("Exception: " + var9);
            }
         }

         try {
            sleep(1000L);
         } catch (InterruptedException var7) {
            if (var4) {
               ntrace.doTrace("InterruptedException: " + var7);
            }
         }
      }

      if (var5 >= 10) {
         this.queue = this.recvSession.createQueue(var2);
         var1.bind(var2, this.queue);
         if (var4) {
            ntrace.doTrace("/tBexec/jmsRecvInit/queue (" + var2 + ") created");
         }
      }

      this.qreceiver = this.recvSession.createReceiver(this.queue);
      if (var4) {
         ntrace.doTrace("]/tBexec/jmsRecvInit/10/");
      }

   }

   public Message jmsReceive() throws JMSException {
      boolean var2 = ntrace.isTraceEnabled(1);
      if (var2) {
         ntrace.doTrace("[/tBexec/jmsReceive/");
      }

      Message var1;
      try {
         var1 = this.qreceiver.receive();
         this.showJmsMsg(var1);
         synchronized(this) {
            this.notifyAll();
         }
      } catch (Exception var6) {
         var6.printStackTrace();
         var1 = null;
      }

      if (var2) {
         ntrace.doTrace("]/tBexec/jmsReceive/10/");
      }

      return var1;
   }

   public void jmsRecvClose() throws JMSException {
      boolean var1 = ntrace.isTraceEnabled(1);
      if (var1) {
         ntrace.doTrace("[/tBexec/jmsRecvClose/");
      }

      if (this.qreceiver != null) {
         this.qreceiver.close();
      }

      if (this.recvSession != null) {
         this.recvSession.close();
      }

      if (var1) {
         ntrace.doTrace("]/tBexec/jmsRecvClose/10/");
      }

   }

   public void jmsErrorInit(Context var1, String var2, String var3) throws NamingException, JMSException {
      boolean var4 = ntrace.isTraceEnabled(1);
      if (var4) {
         ntrace.doTrace("[/tBexec/jmsErrorInit/");
      }

      if (this.errorSession == null) {
         if (this.qcon == null) {
            this.qconFactory = (QueueConnectionFactory)var1.lookup(var3);
            this.qcon = this.qconFactory.createQueueConnection();
         }

         this.errorSession = this.qcon.createQueueSession(false, 1);
      }

      try {
         this.queue = (Queue)var1.lookup(var2);
         if (var4) {
            ntrace.doTrace("/tBexec/jmsErrorInit/queue (" + var2 + ") located");
         }
      } catch (NamingException var6) {
         this.queue = this.errorSession.createQueue(var2);
         var1.bind(var2, this.queue);
         if (var4) {
            ntrace.doTrace("/tBexec/jmsErrorInit/queue (" + var2 + ") created");
         }
      }

      this.qerror = this.errorSession.createSender(this.queue);
      this.jmsErrorMsg = this.errorSession.createMessage();
      if (var4) {
         ntrace.doTrace("]/tBexec/jmsErrorInit/10/");
      }

   }

   public boolean jmsError(Message var1, int var2) throws JMSException {
      boolean var3 = ntrace.isTraceEnabled(1);
      if (var3) {
         ntrace.doTrace("[/tBexec/jmsError/");
      }

      int var4 = 0;

      while(true) {
         try {
            this.qerror.setPriority(var2);
            this.qerror.send(var1);
            break;
         } catch (Exception var8) {
            ++var4;
            if (var3) {
               ntrace.doTrace("/tBexec/jmsError/Error - retry#: " + var4);
            }

            try {
               sleep((long)sArgs.retryDelay);
            } catch (InterruptedException var7) {
            }

            if (var4 > sArgs.retries) {
               break;
            }
         }
      }

      if (var4 > sArgs.retries) {
         if (var3) {
            ntrace.doTrace("*]/tBexec/jmsError/JMS retry count exhausted.");
         }

         return false;
      } else {
         if (var3) {
            ntrace.doTrace("]/tBexec/jmsError/10/");
         }

         return true;
      }
   }

   public void jmsErrorClose() throws JMSException {
      boolean var1 = ntrace.isTraceEnabled(1);
      if (var1) {
         ntrace.doTrace("[/tBexec/jmsErrorClose/");
      }

      if (this.qerror != null) {
         this.qerror.close();
      }

      if (this.errorSession != null) {
         this.errorSession.close();
      }

      if (var1) {
         ntrace.doTrace("]/tBexec/jmsErrorClose/10/");
      }

   }

   public void showJmsMsg(Message var1) throws JMSException {
      boolean var2 = ntrace.isTraceEnabled(1);
      if (var2) {
         ntrace.doTrace("[/tBexec/showJmsMsg/");
      }

      if (var2) {
         ntrace.doTrace("    Message ID:" + var1.getJMSMessageID());
         ntrace.doTrace(" Delivery Time:" + new Date(var1.getJMSTimestamp()));
         ntrace.doTrace("            To:" + var1.getJMSDestination());
         if (var1.getJMSExpiration() > 0L) {
            ntrace.doTrace("       Expires:" + new Date(var1.getJMSExpiration()));
         } else {
            ntrace.doTrace("       Expires:Never");
         }

         ntrace.doTrace("      Priority:" + var1.getJMSPriority());
         ntrace.doTrace("          Mode:" + (var1.getJMSDeliveryMode() == 2 ? "PERSISTENT" : "NON_PERSISTENT"));
         ntrace.doTrace("Correlation ID:" + var1.getJMSCorrelationID());
         ntrace.doTrace("      Reply to:" + var1.getJMSReplyTo());
         ntrace.doTrace("  Message type:" + var1.getJMSType());
         if (var1 instanceof TextMessage) {
            ntrace.doTrace("   TextMessage:" + ((TextMessage)var1).getText());
         } else if (var1 instanceof BytesMessage) {
            ntrace.doTrace("  BytesMessage:");
         } else {
            ntrace.doTrace("  NotSupported:");
         }
      }

      if (var2) {
         ntrace.doTrace("]/tBexec/showJmsMsg/10/");
      }

   }

   public void showTuxMsg(DequeueReply var1) {
      boolean var2 = ntrace.isTraceEnabled(1);
      if (var2) {
         ntrace.doTrace("[/tBexec/showTuxMsg/");
      }

      if (var2) {
         Integer var5;
         if ((var5 = var1.getpriority()) != null) {
            ntrace.doTrace("      Priority:" + var5);
         } else {
            ntrace.doTrace("      Priority:null");
         }

         ntrace.doTrace("Correlation ID:" + var1.getcorrid());
         ntrace.doTrace("     Failure Q:" + var1.getfailurequeue());
         ntrace.doTrace("      Reply to:" + var1.getreplyqueue());
         String var4;
         if (var1.getReplyBuffer() != null) {
            var4 = var1.getReplyBuffer().getType();
         } else {
            var4 = "STRING";
         }

         ntrace.doTrace("   MessageType:" + var4);
         if (var4.equals("STRING")) {
            TypedString var3 = (TypedString)var1.getReplyBuffer();
            if (var3 != null) {
               ntrace.doTrace("   TextMessage:" + var3.toString());
            } else {
               ntrace.doTrace("   TextMessage:null");
            }
         }
      }

      if (var2) {
         ntrace.doTrace("]/tBexec/showTuxMsg/10/");
      }

   }

   public static void stop(int var0) {
      boolean var1 = ntrace.isTraceEnabled(1);
      if (var1) {
         ntrace.doTrace("[/tBexec>stop/t#" + var0);
      }

      String var2 = null;
      if (sArgs.direction[var0 - 1] == 1) {
         var2 = "TuxQ2JmsQ";
      } else if (sArgs.direction[var0 - 1] == 2) {
         var2 = "JmsQ2TuxQ";
      } else if (sArgs.direction[var0 - 1] == 3) {
         var2 = "JmsQ2TuxS";
      } else if (sArgs.direction[var0 - 1] == 4) {
         var2 = "JmsQ2JmsQ";
      } else {
         var2 = "BadLogic/10/";
      }

      sArgs.operational[var0 - 1] = false;
      if (var1) {
         ntrace.doTrace("/tBexec>stop/direction:" + var2);
      }

      if (var1) {
         ntrace.doTrace("]/tBexec<stop/t#" + var0 + ": shutdown in progress");
      }

   }

   public static void tBcancel() {
      boolean var0 = ntrace.isTraceEnabled(1);
      if (var0) {
         ntrace.doTrace("[tBcancel/");
      }

      for(int var2 = 1; var2 <= sArgs.redirect; ++var2) {
         if (var0) {
            ntrace.doTrace("/tBcancel/t#" + var2 + " stoping...");
         }

         stop(var2);
      }

      threadCount = 0;
      if (var0) {
         ntrace.doTrace("]/tBcancel/10/");
      }

   }

   public TypedString jms2tuxString(Message var1) throws JMSException {
      boolean var2 = ntrace.isTraceEnabled(1);
      if (var2) {
         ntrace.doTrace("[/tBexec/jms2tuxString/");
      }

      TypedString var3 = null;
      if (((TextMessage)var1).getText() != null) {
         var3 = new TypedString(((TextMessage)var1).getText());
      }

      if (var2) {
         ntrace.doTrace("]/tBexec/jms2tuxString/10");
      }

      return var3;
   }

   public TypedCArray jms2tuxCArray(Message var1) throws JMSException {
      boolean var2 = ntrace.isTraceEnabled(1);
      if (var2) {
         ntrace.doTrace("[/tBexec/jms2tuxCArray/");
      }

      TypedCArray var3 = new TypedCArray(100000);
      int var4 = 0;

      while(true) {
         try {
            var3.carray[var4] = ((BytesMessage)var1).readByte();
         } catch (MessageEOFException var6) {
            var3.setSendSize(var4);
            if (var2) {
               ntrace.doTrace("]/tBexec/jms2tuxCArray/moved " + var4 + " bytes.");
               ntrace.doTrace("]/tBexec/jms2tuxCArray/10");
            }

            return var3;
         }

         ++var4;
      }
   }

   public TypedFML32 jms2FML32(Message var1) throws Exception {
      boolean var2 = ntrace.isTraceEnabled(1);
      if (var2) {
         ntrace.doTrace("[/tBexec/jms2FML32/");
      }

      XmlFmlCnv var4 = new XmlFmlCnv();
      TypedFML32 var5 = null;
      WTCService.getWTCService();
      FldTbl[] var3 = WTCService.getFldTbls("fml32");
      String var6 = ((TextMessage)var1).getText();
      var5 = var4.XMLtoFML32(var6, var3);
      if (var2) {
         if (var5 == null) {
            ntrace.doTrace("]/tBexec/jms2FML32/failed");
         } else {
            ntrace.doTrace("]/tBexec/jms2FML32/10");
         }
      }

      return var5;
   }

   public TextMessage FML2jms(TypedFML var1, TextMessage var2) {
      boolean var3 = ntrace.isTraceEnabled(1);
      if (var3) {
         ntrace.doTrace("[/tBexec/FML2jms/");
      }

      WTCService.getWTCService();
      var1.setFieldTables(WTCService.getFldTbls("fml16"));
      XmlFmlCnv var5 = new XmlFmlCnv();
      String var6 = var5.FMLtoXML(var1);
      if (var6 != null) {
         try {
            var2.setText(var6);
         } catch (JMSException var8) {
            var2 = null;
         }
      } else {
         var2 = null;
      }

      if (var3) {
         ntrace.doTrace("]/tBexec/FML2jms/10");
      }

      return var2;
   }

   public TextMessage FML322jms(TypedFML32 var1, TextMessage var2) {
      boolean var3 = ntrace.isTraceEnabled(1);
      if (var3) {
         ntrace.doTrace("[/tBexec/FML322jms/");
      }

      WTCService.getWTCService();
      var1.setFieldTables(WTCService.getFldTbls("fml32"));
      XmlFmlCnv var5 = new XmlFmlCnv();
      String var6 = var5.FML32toXML(var1);
      if (var6 != null) {
         try {
            var2.setText(var6);
         } catch (JMSException var8) {
            var2 = null;
         }
      } else {
         var2 = null;
      }

      if (var3) {
         ntrace.doTrace("]/tBexec/FML322jms/10");
      }

      return var2;
   }
}
