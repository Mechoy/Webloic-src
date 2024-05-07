package weblogic.wtc.jatmi;

import com.bea.core.jatmi.common.ntrace;
import com.bea.core.jatmi.internal.TCSecurityManager;
import com.bea.core.jatmi.internal.TCTransactionHelper;
import com.bea.core.jatmi.intf.TCAuthenticatedUser;
import com.bea.core.jatmi.intf.TCTask;
import java.io.IOException;
import java.util.HashMap;
import javax.security.auth.login.LoginException;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.xa.Xid;
import weblogic.iiop.ConnectionManager;
import weblogic.iiop.SequencedRequestMessage;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.tgiop.TGIOPConnection;
import weblogic.tgiop.TGIOPEndPointImpl;
import weblogic.wtc.gwt.MethodParameters;
import weblogic.wtc.gwt.ServiceParameters;
import weblogic.wtc.gwt.TuxedoCorbaConnection;

public final class RMIReplyRequest implements TCTask {
   tfmh myTmmsg;
   HashMap myConnMap;
   dsession myDsession;
   TCAuthenticatedUser mySubject = null;
   Object[] myRequestInfo;
   private String myName;

   public RMIReplyRequest(tfmh var1, Object[] var2, dsession var3) {
      boolean var4 = ntrace.isTraceEnabled(4);
      if (var4) {
         ntrace.doTrace("[/RMIReplyRequest/");
      }

      this.myTmmsg = var1;
      this.myRequestInfo = var2;
      this.myConnMap = ((TuxedoCorbaConnection)var2[0]).getTxInfoMap();
      if (var2[1] != null) {
         try {
            this.mySubject = TCSecurityManager.impersonate((String)var2[1]);
         } catch (LoginException var6) {
            if (var4) {
               ntrace.doTrace("/RMIReplyRequest/Failed to get user identity: " + var6);
            }
         }
      }

      this.myDsession = var3;
      if (var4) {
         ntrace.doTrace("]/RMIReplyRequest/10");
      }

   }

   public int execute() {
      TdomTranTcb var1 = null;
      Txid var2 = null;
      Xid var3 = null;
      Transaction var4 = null;
      boolean var6 = ntrace.isMixedTraceEnabled(12);
      if (var6) {
         ntrace.doTrace("[/RMIReplyRequest/execute/0");
      }

      MethodParameters var7 = new MethodParameters((ServiceParameters)null, (Objrecv)null, (Object[])null, this.myDsession);
      TdomTcb var8 = (TdomTcb)this.myTmmsg.tdom.body;
      int var9 = var8.get_opcode();
      int var10 = var8.get_diagnostic();
      if (var9 == 3 && var10 == 0) {
         var10 = 12;
      }

      if (var9 == 12) {
         var10 = 1;
      }

      if (11 == var10 && null != this.myTmmsg.user) {
         var10 = 0;
      }

      if (var10 == 0 && this.myTmmsg.user == null) {
         var10 = 12;
         if (var6) {
            ntrace.doTrace("/RMIReplyRequest/execute/Receive a reply message without user data and error code");
         }
      }

      if (var10 != 0) {
         if (var6) {
            ntrace.doTrace("/RMIReplyRequest/execute/1/diagnostic = " + var10);
         }

         Exception var11 = TGIOPUtil.mapTPError(var10);
         int var12 = var8.get_reqid();

         TGIOPConnection var13;
         try {
            var13 = new TGIOPConnection(var7);
         } catch (IOException var23) {
            throw new RuntimeException(var23);
         }

         TGIOPEndPointImpl var14 = new TGIOPEndPointImpl(var13, ConnectionManager.getConnectionManager(), (AuthenticatedSubject)null);
         Integer var15 = (Integer)this.myRequestInfo[2];
         SequencedRequestMessage var16 = var14.removePendingResponse(var15);
         if (var16 != null) {
            var16.notify((Throwable)var11);
         } else if (var6) {
            ntrace.doTrace("RMIReplyRequest/execute/2/request not found");
         }
      }

      if (this.myTmmsg.user == null) {
         if (var6) {
            ntrace.doTrace("*]/RMIReplyRequest/execute/4");
         }

         return 0;
      } else {
         if (this.mySubject != null) {
            if (var6) {
               ntrace.doTrace("/RMIReplyRequest/execute/5");
            }

            TCSecurityManager.setAsCurrentUser(this.mySubject);
         }

         if (this.myTmmsg.tdomtran != null) {
            var1 = (TdomTranTcb)this.myTmmsg.tdomtran.body;
            var2 = new Txid(var1.getGlobalTransactionId());
            if (var2 != null) {
               if (var6) {
                  ntrace.doTrace("/RMIReplyRequest/execute/10 + currTxid = " + var2);
               }

               Object[] var5 = new Object[3];
               synchronized(this.myConnMap) {
                  if ((var5 = (Object[])((Object[])this.myConnMap.get(var2))) == null) {
                     if (var6) {
                        ntrace.doTrace("*/RMIReplyRequest/dispatch/12");
                     }
                  } else {
                     var3 = (Xid)var5[2];
                  }
               }

               if (var3 != null) {
                  if (var6) {
                     ntrace.doTrace("/RMIReplyRequest/execute/14 + currXid = " + var3);
                  }

                  if ((var4 = TCTransactionHelper.getTransaction(var3)) == null) {
                     if (var6) {
                        ntrace.doTrace("*/RMIReplyRequest/execute/20");
                     }
                  } else {
                     try {
                        TCTransactionHelper.resumeTransaction(var4);
                     } catch (Exception var21) {
                        throw new RuntimeException(var21);
                     }
                  }

                  if (var9 == 3 && var10 != 11 && var10 != 10) {
                     if (var6) {
                        ntrace.doTrace("/RMIReplyRequest/execute/30");
                     }

                     var10 = 1;
                  } else if (var10 != 11 && var10 != 10) {
                     if (var6) {
                        ntrace.doTrace("/RMIReplyRequest/execute/40");
                     }

                     var10 = 0;
                  }

                  if (var10 != 0) {
                     if (var6) {
                        ntrace.doTrace("/RMIReplyRequest/execute/45");
                     }

                     var5 = new Object[3];
                     synchronized(this.myConnMap) {
                        if ((var5 = (Object[])((Object[])this.myConnMap.get(var3.getGlobalTransactionId()))) == null) {
                           if (var6) {
                              ntrace.doTrace("*/RMIReplyRequest/dispatch/50");
                           }
                        } else {
                           var5[1] = new Boolean(true);
                           this.myConnMap.put(var3.getGlobalTransactionId(), var5);
                        }
                     }

                     try {
                        if (var4 != null) {
                           var4.setRollbackOnly();
                        }
                     } catch (SystemException var24) {
                        if (var6) {
                           ntrace.doTrace("*/RMIReplyRequest/dispatch/60/SystemException:" + var24);
                        }
                     }
                  }

                  if (var6) {
                     ntrace.doTrace("/RMIReplyRequest/dispatch/70");
                  }
               }
            }
         }

         if (var10 == 0) {
            try {
               TGIOPUtil.injectMsgIntoRMI(this.myTmmsg, var7);
            } catch (IOException var19) {
               throw new RuntimeException(var19);
            }
         }

         if (var6) {
            ntrace.doTrace("]/RMIReplyRequest/execute/80");
         }

         return 0;
      }
   }

   public void setTaskName(String var1) {
      this.myName = new String("RMIReplyRequest$" + var1);
   }

   public String getTaskName() {
      return this.myName == null ? "RMIReplyRequest$unknown" : this.myName;
   }
}
