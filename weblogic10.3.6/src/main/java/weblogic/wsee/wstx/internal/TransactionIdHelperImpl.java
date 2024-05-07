package weblogic.wsee.wstx.internal;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import javax.transaction.xa.Xid;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.transaction.WLXid;
import weblogic.transaction.XIDFactory;
import weblogic.transaction.internal.XidImpl;
import weblogic.wsee.wstx.TransactionIdHelper;
import weblogic.wsee.wstx.wsat.WSATHelper;

public class TransactionIdHelperImpl extends TransactionIdHelper {
   private static final int FFID = 65309;
   private final DebugLogger debugWSAT = DebugLogger.getDebugLogger("DebugWSAT");
   private Map<String, Xid> tids2xids = new HashMap();
   private Map<Xid, String> xids2tids = new HashMap();

   public TransactionIdHelperImpl() throws NoSuchAlgorithmException {
   }

   public String xid2wsatid(WLXid var1) {
      return var1.toString();
   }

   public WLXid wsatid2xid(String var1) {
      return XidImpl.create(var1);
   }

   public synchronized Xid getOrCreateXid(byte[] var1) {
      Xid var2 = this.getXid(var1);
      if (var2 != null) {
         return var2;
      } else {
         byte[] var3 = WSATHelper.assignUUID().getBytes();
         var2 = XIDFactory.createXID(65309, var3, (byte[])null);
         String var4 = new String(var1);
         this.tids2xids.put(var4, var2);
         this.xids2tids.put(var2, var4);
         if (this.debugWSAT.isDebugEnabled()) {
            this.debugWSAT.debug("created mapping foreign Transaction Id " + var4 + " to local Xid " + var2);
         }

         return var2;
      }
   }

   public synchronized byte[] getTid(Xid var1) {
      String var2 = (String)this.xids2tids.get(var1);
      return var2 == null ? null : var2.getBytes();
   }

   public synchronized Xid getXid(byte[] var1) {
      return (Xid)this.tids2xids.get(new String(var1));
   }

   public synchronized Xid remove(byte[] var1) {
      if (this.getXid(var1) == null) {
         return null;
      } else {
         Xid var2 = (Xid)this.tids2xids.remove(var1);
         this.xids2tids.remove(var2);
         return var2;
      }
   }

   public synchronized byte[] remove(Xid var1) {
      if (this.getTid(var1) == null) {
         return null;
      } else {
         String var2 = (String)this.xids2tids.remove(var1);
         this.tids2xids.remove(var2);
         return var2.getBytes();
      }
   }

   public String toString() {
      return this.tids2xids.toString();
   }
}
