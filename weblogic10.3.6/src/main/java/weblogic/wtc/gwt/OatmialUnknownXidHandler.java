package weblogic.wtc.gwt;

import com.bea.core.jatmi.common.ntrace;
import com.bea.core.jatmi.config.TuxedoConnectorRAP;
import com.bea.core.jatmi.internal.TCTransactionHelper;
import com.bea.core.jatmi.internal.TuxedoXid;
import com.bea.core.jatmi.intf.TCTask;
import com.bea.core.jatmi.intf.TuxedoLoggable;
import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import weblogic.wtc.WTCLogger;
import weblogic.wtc.jatmi.ReqXidMsg;
import weblogic.wtc.jatmi.ReqXidOid;
import weblogic.wtc.jatmi.TPException;
import weblogic.wtc.jatmi.TdomTcb;
import weblogic.wtc.jatmi.TdomTranTcb;
import weblogic.wtc.jatmi.TuxXidRply;
import weblogic.wtc.jatmi.Txid;
import weblogic.wtc.jatmi.gwatmi;
import weblogic.wtc.jatmi.tcm;
import weblogic.wtc.jatmi.tfmh;

class OatmialUnknownXidHandler implements TCTask {
   private TuxXidRply myRplyXidObj;
   private String myName;

   public OatmialUnknownXidHandler(TuxXidRply var1) {
      this.myRplyXidObj = var1;
   }

   public int execute() {
      boolean var1 = ntrace.isTraceEnabled(2);
      if (var1) {
         ntrace.doTrace("[/OatmialUnknownXidHandler/execute/" + Thread.currentThread());
      }

      ReqXidMsg var2 = null;
      tfmh var3 = null;
      TdomTcb var4 = null;
      TdomTranTcb var5 = null;
      tfmh var6 = null;
      TdomTcb var7 = null;
      TdomTranTcb var8 = null;
      boolean var10 = false;
      Txid var11 = null;
      gwatmi var12 = null;
      TuxedoXid var13 = null;
      OatmialServices var15 = WTCService.getOatmialServices();
      XAResource var16 = null;
      byte var17 = 0;
      TuxedoLoggable var18 = null;
      ReqXidOid var20 = null;
      var2 = this.myRplyXidObj.get_reply(true);
      if ((var3 = var2.getReply()) == null) {
         if (var1) {
            ntrace.doTrace("]/OatmialUnknownXidHandler/10/");
         }

         return 0;
      } else if (var3.tdom == null) {
         if (var1) {
            ntrace.doTrace("]/OatmialUnknownXidHandler/20/Invalid reply, no TDOM TCM/");
         }

         return 0;
      } else if (var3.tdomtran == null) {
         if (var1) {
            ntrace.doTrace("]/OatmialUnknownXidHandler/30/Invalid reply, no TDOM_TRAN TCM/");
         }

         return 0;
      } else {
         var20 = var2.getReqXidOid();
         if (var20 != null && (var11 = var20.getReqXidReturn()) != null) {
            if ((var12 = var20.getAtmiObject()) == null) {
               if (var1) {
                  ntrace.doTrace("]/OatmialUnknownXidHandler/50/Invalid reply, no reply domain/");
               }

               return 0;
            } else {
               var4 = (TdomTcb)var3.tdom.body;
               var5 = (TdomTranTcb)var3.tdomtran.body;
               int var9 = var4.get_opcode();
               if (var1) {
                  ntrace.doTrace("/OatmialUnknownXidHandler/opcode=" + TdomTcb.print_opcode(var9));
               }

               if (var5 == null) {
                  if (var1) {
                     ntrace.doTrace("]/OatmialUnknownXidHandler/60.1/null transaction");
                  }

                  return 0;
               } else {
                  try {
                     var13 = new TuxedoXid(var5);
                  } catch (TPException var26) {
                     if (var1) {
                        ntrace.doTrace("]/OatmialUnknownXidHandler/60/" + var26);
                     }

                     return 0;
                  }

                  TuxedoConnectorRAP[] var14;
                  if ((var14 = var15.getInboundRdomsAssociatedWithXid(var13)) != null && (var16 = TCTransactionHelper.getXAResource()) == null) {
                     if (var1) {
                        ntrace.doTrace("Could not get interposed mamager");
                     }

                     return 0;
                  } else {
                     byte var30;
                     switch (var9) {
                        case 7:
                           if (var14 == null) {
                              var30 = 12;
                              if (var1) {
                                 ntrace.doTrace("new opcode ROLLBACK");
                              }
                           } else {
                              try {
                                 if (var1) {
                                    ntrace.doTrace("issue PREPARE, tuxedoXid = " + var13);
                                 }

                                 var16.prepare(var13);
                              } catch (XAException var29) {
                                 var30 = 12;
                                 if (var1) {
                                    ntrace.doTrace("opcode ROLLBACK");
                                 }
                                 break;
                              }

                              var30 = 8;
                              if (var1) {
                                 ntrace.doTrace("opcode READY");
                              }

                              var18 = TCTransactionHelper.createTuxedoLoggable(var13, 3);
                              var18.write();
                              var18.waitForDisk();
                              if (ntrace.getTraceLevel() == 1000372) {
                                 if (var1) {
                                    ntrace.doTrace("After prepared log, sleeping 30 seconds");
                                 }

                                 try {
                                    Thread.sleep(30000L);
                                 } catch (InterruptedException var25) {
                                 }

                                 if (var1) {
                                    ntrace.doTrace("Finished sleeping");
                                 }
                              }
                           }
                           break;
                        case 8:
                           var30 = 9;
                           if (var1) {
                              ntrace.doTrace("opcode COMMIT");
                           }
                           break;
                        case 9:
                           var30 = 10;
                           if (var1) {
                              ntrace.doTrace("opcode DONE");
                           }

                           if (var14 != null) {
                              boolean var21 = !var15.isXidInReadyMap(var13);
                              if (var1) {
                                 ntrace.doTrace("ONE PC =" + var21);
                              }

                              if (!var21) {
                                 var18 = TCTransactionHelper.createTuxedoLoggable(var13, 4);
                                 var18.write();
                              }

                              try {
                                 if (var1) {
                                    ntrace.doTrace("tuxedoXid = " + var13);
                                    ntrace.doTrace("issue COMMIT");
                                 }

                                 var16.commit(var13, var21);
                              } catch (XAException var27) {
                                 if (var1) {
                                    ntrace.doTrace("commit failed:" + var27);
                                 }

                                 switch (var27.errorCode) {
                                    case -7:
                                    case -6:
                                    case -5:
                                    case -4:
                                    case -3:
                                    case -2:
                                    case -1:
                                    case 0:
                                    case 1:
                                    case 2:
                                    case 3:
                                    case 4:
                                    case 7:
                                    case 8:
                                    default:
                                       var17 = 16;
                                       if (var1) {
                                          ntrace.doTrace("flag HEURISTIC_HAZARD");
                                       }
                                       break;
                                    case 5:
                                    case 6:
                                       var17 = 8;
                                       if (var1) {
                                          ntrace.doTrace("flag HEURISTIC_MIX");
                                       }
                                 }
                              }

                              if (!var21) {
                                 var18.waitForDisk();
                              }

                              if (ntrace.getTraceLevel() == 1000372) {
                                 ntrace.doTrace("After commit log, sleeping 30 seconds");

                                 try {
                                    Thread.sleep(30000L);
                                 } catch (InterruptedException var24) {
                                 }

                                 ntrace.doTrace("/OatmialUnknownXidHandler/execute/Finished sleeping");
                              }
                           }
                           break;
                        case 10:
                        case 11:
                        case 13:
                        default:
                           if (var1) {
                              ntrace.doTrace("]/OatmialUnknownXidHandler/70/Opcode does not need a reply");
                           }

                           return 0;
                        case 12:
                           var30 = 10;
                           if (var1) {
                              ntrace.doTrace("opcode DONE");
                           }

                           if (var14 != null) {
                              var18 = TCTransactionHelper.createTuxedoLoggable(var13, 3);

                              try {
                                 if (var1) {
                                    ntrace.doTrace("aborting tuxedoXid = " + var13);
                                    ntrace.doTrace("issue ROLLBACK");
                                 }

                                 var16.rollback(var13);
                              } catch (XAException var28) {
                                 if (var1) {
                                    ntrace.doTrace("/OatmialUnknownXidHandler/abort failed/" + var28);
                                 }

                                 switch (var28.errorCode) {
                                    case -7:
                                    case -6:
                                    case -5:
                                    case -3:
                                    case -2:
                                    case -1:
                                    case 0:
                                    case 1:
                                    case 2:
                                    case 3:
                                    case 4:
                                    case 6:
                                    case 8:
                                    default:
                                       var17 = 16;
                                       if (var1) {
                                          ntrace.doTrace("flag HEURISTIC_HAZARD");
                                       }
                                    case -4:
                                       break;
                                    case 5:
                                    case 7:
                                       var17 = 8;
                                 }
                              }
                           }
                     }

                     var6 = new tfmh(1);
                     var7 = new TdomTcb(var30, var4.get_reqid(), 0, (String)null);
                     var7.set_info(32 | var17);
                     var6.tdom = new tcm((short)7, var7);
                     var8 = new TdomTranTcb(var11);
                     var8.setNwtranidparent(var5.getNwtranidparent());
                     var6.tdomtran = new tcm((short)10, var8);

                     try {
                        var12.send_transaction_reply(var6);
                        if (var1) {
                           ntrace.doTrace("/OatmialUnknownXidHandler/reply to unknown transaction request sent/");
                        }
                     } catch (TPException var23) {
                        WTCLogger.logTPEsendTran(var23);
                     }

                     if (var14 != null) {
                        switch (var30) {
                           case 8:
                              var15.addXidToReadyMap(var13);
                              break;
                           case 10:
                              if (var18 != null) {
                                 var18.forget();
                              }

                              var15.deleteXidFromReadyMap(var13);
                              var15.deleteInboundRdomsAssociatedWithXid(var13);
                        }
                     }

                     if (var1) {
                        ntrace.doTrace("]/OatmialUnknownXidHandler/80/");
                     }

                     return 0;
                  }
               }
            }
         } else {
            if (var1) {
               ntrace.doTrace("]/OatmialUnknownXidHandler/40/Invalid reply, no Txid/");
            }

            return 0;
         }
      }
   }

   public void setTaskName(String var1) {
      this.myName = new String("OatmialUnknownXidHandler$" + var1);
   }

   public String getTaskName() {
      return this.myName == null ? "OatmialUnknownXidHandler$unknown" : this.myName;
   }
}
