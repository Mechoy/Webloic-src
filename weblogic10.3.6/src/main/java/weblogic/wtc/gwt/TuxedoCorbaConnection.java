package weblogic.wtc.gwt;

import com.bea.core.jatmi.common.ntrace;
import com.bea.core.jatmi.internal.TCRouteEntry;
import com.bea.core.jatmi.internal.TCTransactionHelper;
import com.bea.core.jatmi.internal.TuxedoXA;
import java.util.ArrayList;
import java.util.HashMap;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.xa.XAException;
import javax.transaction.xa.Xid;
import weblogic.wtc.jatmi.CallDescriptor;
import weblogic.wtc.jatmi.CorbaAtmi;
import weblogic.wtc.jatmi.Objinfo;
import weblogic.wtc.jatmi.Reply;
import weblogic.wtc.jatmi.ReqOid;
import weblogic.wtc.jatmi.TPException;
import weblogic.wtc.jatmi.TPReplyException;
import weblogic.wtc.jatmi.TuxRply;
import weblogic.wtc.jatmi.TypedBuffer;

public final class TuxedoCorbaConnection extends TuxedoConnection implements TxEnd {
   private HashMap txInfoMap = new HashMap();

   public TuxedoCorbaConnection() throws TPException {
      super(8);
   }

   public CallDescriptor tpMethodReq(TypedBuffer var1, Objinfo var2, MethodParameters var3, int var4) throws TPException {
      boolean var5 = ntrace.isTraceEnabled(2);
      if (var5) {
         ntrace.doTrace("[/TuxedoCorbaConnection/tpMethodReq/0");
      }

      CorbaAtmi var8 = null;
      int var12 = 0;
      Xid var13 = null;
      TuxedoXA var14 = null;
      Transaction var15 = null;
      boolean var16 = false;
      Object[] var17 = null;
      byte[] var18 = null;
      UID var19 = null;
      if ((var4 & -16430) != 0) {
         if (var5) {
            ntrace.doTrace("*]/TuxedoCorbaConnection/tpMethodReq/10");
         }

         throw new TPException(4);
      } else if (var1 != null && var2 != null) {
         String var20 = new String("//" + var2.getDomainId());
         if (var5) {
            ntrace.doTrace("/TuxedoCorbaConnection/tpMethodReq/30" + var20);
         }

         TuxRply var10;
         if ((var4 & 4) == 0 && (var4 & 16384) == 0) {
            var10 = this.myRplyObj;
         } else {
            var10 = null;
         }

         if ((var15 = TCTransactionHelper.getTransaction()) != null) {
            var13 = TCTransactionHelper.getXidFromTransaction(var15);
            var18 = var13.getGlobalTransactionId();
            var19 = TuxedoCorbaConnection.UID.attach(var18);
         }

         if (!this.is_term) {
            if (var15 != null) {
               synchronized(this.txInfoMap) {
                  if ((var17 = (Object[])((Object[])this.txInfoMap.get(var19))) == null) {
                     var14 = new TuxedoXA(this.tos, this);

                     try {
                        var15.enlistResource(var14);
                     } catch (SystemException var25) {
                        throw new TPException(12, "ERROR: Could not enlist in transaction");
                     } catch (RollbackException var26) {
                        var16 = true;
                     } catch (IllegalStateException var27) {
                        throw new TPException(12, "ERROR: Transaction already prepared");
                     }

                     if (!var16) {
                        var17 = new Object[]{var14, new Boolean(var16), var13};
                        if (var5) {
                           ntrace.doTrace("Adding currXid = " + var13 + " to txInfoMap");
                        }

                        this.txInfoMap.put(var19, var17);
                     }
                  } else {
                     var14 = (TuxedoXA)var17[0];
                     var16 = (Boolean)var17[1];
                  }
               }

               if ((var4 & 8) == 0) {
                  if (var16) {
                     if (var5) {
                        ntrace.doTrace("*]/TuxedoCorbaConnection/tpMethodReq/60/TPEINVAL");
                     }

                     throw new TPException(4, "Transaction rolled back but TPNOTRAN not specified");
                  }

                  var12 = var14.getRealTransactionTimeout();
                  var4 &= -33;
               }
            }

            ArrayList var6 = this.getProviderRoute(var20, var1, var13, var4);
            TCRouteEntry var7 = (TCRouteEntry)var6.get(0);
            var8 = (CorbaAtmi)var7.getSessionGroup();
            CallDescriptor var9 = var8.tpMethodReq(var1, var2, var3, this, var4, var10, var13, var12, this);
            ReqOid var11 = new ReqOid(var9, var8, var13);
            if (var5) {
               ntrace.doTrace("]/TuxedoCorbaConnection/tpMethodReq/70" + var11);
            }

            return var11;
         } else {
            if (var15 != null) {
               try {
                  var15.setRollbackOnly();
               } catch (SystemException var29) {
                  if (var5) {
                     ntrace.doTrace("/TuxedoCorbaConnection/tpMethodReq/40 SystemException:" + var29);
                  }
               }

               var16 = true;
               synchronized(this.txInfoMap) {
                  if ((var17 = (Object[])((Object[])this.txInfoMap.get(var18))) != null) {
                     var17[1] = new Boolean(var16);
                     this.txInfoMap.put(var19, var17);
                  }
               }
            }

            if (var5) {
               ntrace.doTrace("*]/TuxedoCorbaConnection/tpMethodReq/50");
            }

            throw new TPException(9, "Session terminated");
         }
      } else {
         if (var5) {
            ntrace.doTrace("*]/TuxedoCorbaConnection/tpMethodReq/20");
         }

         throw new TPException(4);
      }
   }

   public Reply tpgetrply(CallDescriptor var1, int var2) throws TPException, TPReplyException {
      Xid var4 = null;
      boolean var5 = false;
      Transaction var6 = null;
      Object[] var7 = new Object[3];
      boolean var8 = ntrace.isTraceEnabled(2);
      if (var8) {
         ntrace.doTrace("[/TuxedoCorbaConnection/tpgetrply/0" + var1 + "/" + var2);
      }

      Reply var3;
      try {
         var3 = super.tpgetrply(var1, var2);
      } catch (TPException var16) {
         if (var1 != null) {
            var4 = ((ReqOid)var1).getXID();
         }

         if (var8) {
            ntrace.doTrace("currXid = " + var4);
         }

         if (var4 != null) {
            if ((var6 = TCTransactionHelper.getTransaction()) != null) {
               if (var4 != TCTransactionHelper.getXidFromTransaction(var6)) {
                  if (var8) {
                     ntrace.doTrace("*]/TuxedoCorbaConnection/tpgetrply/10");
                  }

                  throw new TPException(9, "Internal Error: Xid stored in Reply Handle doesn't match current Transaction");
               }
            } else if ((var6 = TCTransactionHelper.getTransaction(var4)) == null) {
               if (var8) {
                  ntrace.doTrace("*]/TuxedoCorbaConnection/tpgetrply/20");
               }

               throw new TPException(12, "Internal Error: Could not find Transaction matching Xid in Reply Handle");
            }

            try {
               var6.setRollbackOnly();
            } catch (SystemException var15) {
               if (var8) {
                  ntrace.doTrace("SystemException:" + var15);
               }
            }

            var5 = true;
            synchronized(this.txInfoMap) {
               byte[] var11 = var4.getGlobalTransactionId();
               UID var12 = TuxedoCorbaConnection.UID.attach(var11);
               if ((var7 = (Object[])((Object[])this.txInfoMap.get(var12))) == null) {
                  if (var8) {
                     ntrace.doTrace("*]/TuxedoCorbaConnection/tpgetrply/30");
                  }

                  throw new TPException(12, "Internal Error: Unknown Xid(" + var4 + ")");
               }

               var7[1] = new Boolean(var5);
               this.txInfoMap.put(var12, var7);
            }
         }

         if (var8) {
            ntrace.doTrace("*]/TuxedoCorbaConnection/tpgetrply/40");
         }

         throw var16;
      }

      if (var8) {
         ntrace.doTrace("]/TuxedoCorbaConnection/tpgetrply/50");
      }

      return var3;
   }

   public boolean getRollbackOnly() throws TPException {
      throw new TPException(9);
   }

   public void end(Xid var1, int var2) throws XAException {
      boolean var3 = ntrace.isTraceEnabled(2);
      if (var3) {
         ntrace.doTrace("[/TuxedoCorbaConnection/end/currXid = " + var1 + ", flags: " + flagsToString(var2));
      }

      if (var2 == 33554432) {
         if (var3) {
            ntrace.doTrace("]/TuxedoCorbaConnection/end/5");
         }
      } else {
         synchronized(this.txInfoMap) {
            byte[] var5 = var1.getGlobalTransactionId();
            UID var6 = TuxedoCorbaConnection.UID.attach(var5);
            if (this.txInfoMap.get(var6) == null) {
               if (var3) {
                  ntrace.doTrace("*]/TuxedoCorbaConnection/end/20");
               }

               throw new XAException(-3);
            }

            this.txInfoMap.remove(var6);
            if (var3) {
               ntrace.doTrace("]/TuxedoCorbaConnection/end/10");
            }
         }
      }

   }

   public HashMap getTxInfoMap() {
      return this.txInfoMap;
   }

   private static String flagsToString(int var0) {
      switch (var0) {
         case 0:
            return "TMNOFLAGS";
         case 2097152:
            return "TMJOIN";
         case 33554432:
            return "TMSUSPEND";
         case 67108864:
            return "TMSUCCESS";
         case 134217728:
            return "TMRESUME";
         case 536870912:
            return "TMFAIL";
         case 1073741824:
            return "TMONEPHASE";
         default:
            return Integer.toHexString(var0).toUpperCase();
      }
   }

   private static class UID {
      public static final int UIDLEN = 6;
      private byte[] uid = new byte[6];

      public static UID attach(byte[] var0) {
         UID var1 = null;
         if (var0 != null && var0.length >= 6) {
            var1 = new UID();
            System.arraycopy(var0, 0, var1.uid, 0, 6);
         }

         return var1;
      }

      public byte[] value() {
         return this.uid;
      }

      public int hashCode() {
         int var1 = 0;

         for(int var2 = 0; var2 < this.uid.length; ++var2) {
            var1 += this.uid[var2];
         }

         return var1;
      }

      public boolean equals(Object var1) {
         if (var1 != null && var1 instanceof UID) {
            byte[] var2 = ((UID)var1).value();

            for(int var3 = 0; var3 < this.uid.length; ++var3) {
               if (var2[var3] != this.uid[var3]) {
                  return false;
               }
            }

            return true;
         } else {
            return false;
         }
      }
   }
}
