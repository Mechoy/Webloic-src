package weblogic.wtc.gwt;

import com.bea.core.jatmi.common.ntrace;
import com.bea.core.jatmi.internal.TuxedoXid;
import java.io.Serializable;
import java.util.TimerTask;
import weblogic.wtc.jatmi.InvokeInfo;
import weblogic.wtc.jatmi.Reply;
import weblogic.wtc.jatmi.SessionAcallDescriptor;
import weblogic.wtc.jatmi.TPException;
import weblogic.wtc.jatmi.TPReplyException;
import weblogic.wtc.jatmi.TPRequestAsyncReply;
import weblogic.wtc.jatmi.TdomTcb;
import weblogic.wtc.jatmi.TypedBuffer;
import weblogic.wtc.jatmi.UserTcb;
import weblogic.wtc.jatmi.dsession;
import weblogic.wtc.jatmi.rdsession;
import weblogic.wtc.jatmi.tcm;
import weblogic.wtc.jatmi.tfmh;

public class TPRequestAsyncReplyImpl implements TPRequestAsyncReply {
   static final long serialVersionUID = 9166408416488128781L;
   private ServiceParameters _params;
   private boolean _called;
   private TuxedoXid _xid;
   private OatmialServices _services;
   private TDMRemote _remoteDomain;
   private boolean _done = false;
   private TimerTask _task = null;

   protected TPRequestAsyncReplyImpl(ServiceParameters var1, TDMRemote var2, TuxedoXid var3) {
      this._params = var1;
      this._remoteDomain = var2;
      this._xid = var3;
      this._services = WTCService.getOatmialServices();
      this._called = false;
   }

   private void internalReply(Reply var1, TPException var2) {
      this._done = true;
      if (this._task != null) {
         this._task.cancel();
      }

      boolean var3 = ntrace.isTraceEnabled(2);
      if (var3) {
         ntrace.doTrace("[/TPRequestAsyncReplyImpl/internalReply/" + Thread.currentThread());
      }

      synchronized(this) {
         if (this.getCalled()) {
            if (var3) {
               ntrace.doTrace("]/TPRequestAsyncReplyImpl/internalReply/05");
            }

            return;
         }
      }

      rdsession var6 = null;
      TypedBuffer var10 = null;
      int var11 = 0;
      int var12 = 0;
      int var13 = -1;
      SessionAcallDescriptor var15 = null;
      if (this._params == null) {
         if (var3) {
            ntrace.doTrace("]/TPRequestAsyncReplyImpl/internalReply/10");
         }

      } else {
         InvokeInfo var4;
         if ((var4 = this._params.get_invokeInfo()) == null) {
            if (var3) {
               ntrace.doTrace("]/TPRequestAsyncReplyImpl/internalReply/20");
            }

         } else {
            dsession var5;
            if ((var5 = (dsession)this._params.get_gwatmi()) == null) {
               if (var3) {
                  ntrace.doTrace("]/TPRequestAsyncReplyImpl/internalReply/30");
               }

            } else {
               Serializable var14;
               if ((var14 = var4.getReqid()) == null) {
                  if (var3) {
                     ntrace.doTrace("]/TPRequestAsyncReplyImpl/internalReply/40");
                  }

               } else {
                  tfmh var8;
                  if ((var8 = var4.getServiceMessage()) == null) {
                     synchronized(this) {
                        var5.send_failure_return(var14, new TPException(4), var13);
                        this.setCalled(true);
                     }

                     if (var3) {
                        ntrace.doTrace("]/TPRequestAsyncReplyImpl/internalReply/50");
                     }

                  } else {
                     TdomTcb var9;
                     if (var8.tdom != null && (var9 = (TdomTcb)var8.tdom.body) != null) {
                        if ((var13 = var9.get_convid()) != -1) {
                           var15 = new SessionAcallDescriptor(var13, true);
                           var6 = var5.get_rcv_place();
                        }

                        if (var1 != null) {
                           var10 = var1.getReplyBuffer();
                           var11 = var1.gettpurcode();
                        }

                        if (var2 != null) {
                           if (var3) {
                              ntrace.doTrace("/TPRequestAsyncReplyImpl/internalRely/tpReplyerro " + var2);
                           }

                           var12 = var2.gettperrno();
                           if (var2 instanceof TPReplyException) {
                              Reply var16 = ((TPReplyException)var2).getExceptionReply();
                              if (var16 != null) {
                                 var10 = var16.getReplyBuffer();
                                 var11 = var16.gettpurcode();
                              }
                           }
                        }

                        if ((var9.get_flag() & 4) != 0) {
                           if (var3) {
                              ntrace.doTrace("]/TPRequestAsyncReplyImpl/internalReply/TPNOREPLY set");
                           }

                        } else {
                           tfmh var7;
                           if (var10 == null) {
                              var7 = new tfmh(1);
                           } else {
                              tcm var27 = new tcm((short)0, new UserTcb(var10));
                              var7 = new tfmh(var10.getHintIndex(), var27, 1);
                           }

                           try {
                              if (var3) {
                                 ntrace.doTrace("]/TPRequestAsyncReplyImpl/internalReply/sending success " + var14);
                              }

                              synchronized(this) {
                                 var5.send_success_return(var14, var7, var12, var11, var13);
                                 this.setCalled(true);
                              }
                           } catch (TPException var25) {
                              TPException var28 = var25;
                              if (var13 == -1) {
                                 synchronized(this) {
                                    var5.send_failure_return(var14, var28, var13);
                                    this.setCalled(true);
                                 }
                              } else {
                                 var6.remove_rplyObj(var15);
                              }

                              this._params.removeUser();
                              if (var3) {
                                 ntrace.doTrace("]/TPRequestAsyncReplyImpl/internalReply/70/" + var25);
                              }

                              return;
                           }

                           if (var13 != -1) {
                              var6.remove_rplyObj(var15);
                           }

                           this._params.removeUser();
                           if (var3) {
                              ntrace.doTrace("]/TPRequestAsyncReplyImpl/internalReply/80");
                           }

                        }
                     } else {
                        synchronized(this) {
                           var5.send_failure_return(var14, new TPException(4), var13);
                           this.setCalled(true);
                        }

                        if (var3) {
                           ntrace.doTrace("]/TPRequestAsyncReplyImpl/internalReply/60");
                        }

                     }
                  }
               }
            }
         }
      }
   }

   public void success(Reply var1) {
      this.internalReply(var1, (TPException)null);
   }

   public void failure(TPException var1) {
      this.internalReply((Reply)null, var1);
   }

   protected void setCalled(boolean var1) {
      this._called = var1;
   }

   protected boolean getCalled() {
      return this._called;
   }

   protected void setTimeoutTask(TimerTask var1) {
      this._task = var1;
   }

   protected boolean isDone() {
      return this._done;
   }
}
