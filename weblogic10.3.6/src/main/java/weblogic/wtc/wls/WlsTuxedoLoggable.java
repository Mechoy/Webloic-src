package weblogic.wtc.wls;

import com.bea.core.jatmi.common.ntrace;
import com.bea.core.jatmi.config.TuxedoConnectorRAP;
import com.bea.core.jatmi.internal.TuxedoXid;
import com.bea.core.jatmi.intf.TuxedoLoggable;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.InvalidObjectException;
import javax.transaction.xa.Xid;
import weblogic.transaction.ServerTransactionManager;
import weblogic.transaction.TransactionLoggable;
import weblogic.transaction.TransactionLogger;
import weblogic.transaction.TxHelper;
import weblogic.transaction.internal.LogDataInput;
import weblogic.transaction.internal.LogDataOutput;
import weblogic.wtc.gwt.OatmialServices;
import weblogic.wtc.gwt.WTCService;
import weblogic.wtc.jatmi.TPException;
import weblogic.wtc.jatmi.Txid;

public class WlsTuxedoLoggable implements TransactionLoggable, TuxedoLoggable {
   private static final int VERSION = 1;
   private Txid myTxid;
   private Xid myXid;
   private int myType;
   private Object onDiskReply;
   private boolean gotDiskReply = false;
   private String[] remoteDomains;
   private TransactionLogger tlg = null;

   public WlsTuxedoLoggable() {
      this.myType = 0;
      this.onDiskReply = new Object();
   }

   public WlsTuxedoLoggable(Xid var1, int var2) {
      this.myXid = var1;
      this.myTxid = new Txid(var1.getGlobalTransactionId());
      if (var2 >= 0 && var2 <= 4) {
         this.myType = var2;
      } else {
         this.myType = 0;
      }

      this.onDiskReply = new Object();
   }

   public int getType() {
      return this.myType;
   }

   public void readExternal(DataInput var1) throws IOException {
      LogDataInput var2 = (LogDataInput)var1;
      boolean var3 = ntrace.isTraceEnabled(2);
      if (var3) {
         ntrace.doTrace("[/WlsTuxedoLoggable/readExternal/");
      }

      Object var8 = null;
      Object var9 = null;
      int var11 = var2.readNonNegativeInt();
      if (var11 != 1) {
         if (var3) {
            ntrace.doTrace("*]/WlsTuxedoLoggable/readExternal/10");
         }

         throw new InvalidObjectException("WTC log record: unrecognized versionnumber " + var11);
      } else {
         this.myType = var2.readNonNegativeInt();
         byte[] var14 = var2.readByteArray();
         byte[] var15 = var2.readByteArray();
         switch (this.myType) {
            case 1:
            case 2:
               this.myXid = TxHelper.createXid(var14, var15);
               this.myTxid = new Txid(this.myXid.getGlobalTransactionId());
               break;
            case 3:
            case 4:
               try {
                  this.myXid = new TuxedoXid(var14, var15);
               } catch (TPException var13) {
                  if (var3) {
                     ntrace.doTrace("*]/WlsTuxedoLoggable/readExternal/20");
                  }

                  throw new InvalidObjectException("WTC log record: Invalid Tuxeod Xid " + var13);
               }

               this.myTxid = new Txid(this.myXid.getGlobalTransactionId());
               break;
            default:
               if (var3) {
                  ntrace.doTrace("*]/WlsTuxedoLoggable/readExternal/30");
               }

               throw new InvalidObjectException("WTC log record: unrecognized type " + this.myType);
         }

         int var6;
         if ((var6 = var2.readNonNegativeInt()) == 0) {
            if (var3) {
               ntrace.doTrace("]/WlsTuxedoLoggable/readExternal/40");
            }

         } else {
            this.remoteDomains = new String[var6];

            for(int var10 = 0; var10 < var6; ++var10) {
               this.remoteDomains[var10] = var2.readAbbrevString();
            }

            if (var3) {
               ntrace.doTrace("]/WlsTuxedoLoggable/readExternal/50");
            }

         }
      }
   }

   public void writeExternal(DataOutput var1) throws IOException {
      boolean var2 = ntrace.isTraceEnabled(2);
      if (var2) {
         ntrace.doTrace("[/WlsTuxedoLoggable/writeExternal/");
      }

      LogDataOutput var3 = (LogDataOutput)var1;
      var3.writeNonNegativeInt(1);
      var3.writeNonNegativeInt(this.myType);
      if (this.myXid == null) {
         var3.writeNonNegativeInt(0);
         var3.writeNonNegativeInt(0);
         var3.writeNonNegativeInt(0);
         if (var2) {
            ntrace.doTrace("]/WlsTuxedoLoggable/writeExternal/10");
         }

      } else {
         byte[] var4 = this.myXid.getGlobalTransactionId();
         var3.writeByteArray(var4);
         var4 = this.myXid.getBranchQualifier();
         var3.writeByteArray(var4);
         OatmialServices var5 = WTCService.getOatmialServices();
         TuxedoConnectorRAP[] var6;
         switch (this.myType) {
            case 1:
            case 2:
            default:
               var6 = var5.getOutboundRdomsAssociatedWithXid(this.myXid);
               break;
            case 3:
            case 4:
               var6 = var5.getInboundRdomsAssociatedWithXid(this.myXid);
         }

         if (var6 != null && var6.length != 0) {
            var3.writeNonNegativeInt(var6.length);

            for(int var7 = 0; var7 < var6.length; ++var7) {
               var3.writeAbbrevString(var6[var7].getAccessPoint());
            }
         } else {
            if (var2) {
               ntrace.doTrace("no rdom");
            }

            var3.writeNonNegativeInt(0);
         }

         if (var2) {
            ntrace.doTrace("]/WlsTuxedoLoggable/writeExternal/20");
         }

      }
   }

   public void onDisk(TransactionLogger var1) {
      boolean var2 = ntrace.isTraceEnabled(2);
      if (var2) {
         ntrace.doTrace("[/WlsTuxedoLoggable/onDisk/");
      }

      if (this.myType == 4) {
         if (this.tlg == null) {
            this.tlg = ((ServerTransactionManager)((ServerTransactionManager)TxHelper.getTransactionManager())).getTransactionLogger();
         }

         WlsTuxedoLoggable var3 = new WlsTuxedoLoggable(this.myXid, 3);
         if (ntrace.getTraceLevel() == 1000372) {
            if (var2) {
               ntrace.doTrace("Committing on disk, prepared not removed, sleep 30 seconds");
            }

            try {
               Thread.sleep(30000L);
            } catch (InterruptedException var7) {
            }

            if (var2) {
               ntrace.doTrace("Finished sleeping");
            }
         }

         var3.forget();
      }

      synchronized(this.onDiskReply) {
         this.gotDiskReply = true;
         this.onDiskReply.notify();
      }

      if (var2) {
         ntrace.doTrace("]/WlsTuxedoLoggable/onDisk/10");
      }

   }

   public void waitForDisk() {
      synchronized(this.onDiskReply) {
         while(!this.gotDiskReply) {
            try {
               this.onDiskReply.wait();
            } catch (InterruptedException var4) {
               this.gotDiskReply = false;
               return;
            }
         }

         this.gotDiskReply = false;
      }
   }

   public void onError(TransactionLogger var1) {
   }

   public void onRecovery(TransactionLogger var1) {
      boolean var2 = ntrace.isTraceEnabled(2);
      if (var2) {
         ntrace.doTrace("[/WlsTuxedoLoggable/onRecovery/" + var1);
      }

      switch (this.myType) {
         case 0:
            if (var2) {
               ntrace.doTrace("IS_NONE");
            }
            break;
         case 1:
            if (var2) {
               ntrace.doTrace("IS_READY");
            }

            if (this.myXid != null && this.remoteDomains != null && this.remoteDomains.length != 0) {
               WTCService.addRecoveredXid(this.myXid, this.remoteDomains);
               WTCService.AddXidTLogMap(this.myXid, this);
            }
            break;
         case 2:
            if (var2) {
               ntrace.doTrace("IS_COMMIT");
            }

            if (this.myXid != null && this.remoteDomains != null && this.remoteDomains.length != 0) {
               WTCService.addCommittedXid(this.myXid, this.remoteDomains);
            }
            break;
         case 3:
            if (var2) {
               ntrace.doTrace("IS_PREPARED");
            }

            if (this.myXid != null && this.remoteDomains != null && this.remoteDomains.length != 0) {
               WTCService.addPreparedXid(this.myXid, this.remoteDomains[0], this);
            }
            break;
         case 4:
            if (var2) {
               ntrace.doTrace("IS_COMMITTING");
            }

            if (this.myXid != null && this.remoteDomains != null && this.remoteDomains.length != 0) {
               WTCService.addCommittingXid(this.myXid, this.remoteDomains[0], this);
            }
            break;
         default:
            if (var2) {
               ntrace.doTrace("Unknown type: " + this.myType);
            }
      }

      if (var2) {
         ntrace.doTrace("]/WlsTuxedoLoggable/onRecovery/10");
      }

   }

   public Txid getTxid() {
      return this.myTxid;
   }

   public boolean equals(Object var1) {
      if (this.myTxid == null && var1 != null) {
         return false;
      } else if (this.myTxid != null && var1 == null) {
         return false;
      } else if (var1 == null) {
         return this.myTxid == null && this.myType == 0;
      } else if (!(var1 instanceof WlsTuxedoLoggable)) {
         return false;
      } else {
         WlsTuxedoLoggable var2 = (WlsTuxedoLoggable)var1;
         int var3 = var2.getType();
         if (var3 != this.myType) {
            return false;
         } else {
            Txid var4 = var2.getTxid();
            return this.myTxid.equals(var4);
         }
      }
   }

   public int hashCode() {
      return this.myTxid == null ? this.myType : this.myTxid.hashCode() + this.myType;
   }

   public void write() {
      if (this.tlg == null) {
         this.tlg = ((ServerTransactionManager)((ServerTransactionManager)TxHelper.getTransactionManager())).getTransactionLogger();
      }

      this.tlg.store(this);
   }

   public void forget() {
      if (this.tlg == null) {
         this.tlg = ((ServerTransactionManager)((ServerTransactionManager)TxHelper.getTransactionManager())).getTransactionLogger();
      }

      this.tlg.release(this);
   }
}
