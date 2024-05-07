package weblogic.wsee.wstx;

import java.security.NoSuchAlgorithmException;
import javax.transaction.xa.Xid;
import weblogic.transaction.WLXid;
import weblogic.wsee.wstx.internal.TransactionIdHelperImpl;

public abstract class TransactionIdHelper {
   private static TransactionIdHelper singleton;

   public static TransactionIdHelper getInstance() {
      return singleton;
   }

   public abstract String xid2wsatid(WLXid var1);

   public abstract WLXid wsatid2xid(String var1);

   public abstract Xid getOrCreateXid(byte[] var1);

   public abstract Xid getXid(byte[] var1);

   public abstract byte[] getTid(Xid var1);

   public abstract Xid remove(byte[] var1);

   public abstract byte[] remove(Xid var1);

   static {
      try {
         singleton = new TransactionIdHelperImpl();
      } catch (NoSuchAlgorithmException var1) {
         var1.printStackTrace();
      }

   }
}
