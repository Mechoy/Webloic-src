package weblogic.corba.cos.transactions;

import javax.transaction.xa.Xid;
import org.omg.CosTransactions.Coordinator;
import org.omg.CosTransactions.Terminator;
import org.omg.CosTransactions.Unavailable;
import org.omg.CosTransactions._ControlImplBase;
import weblogic.iiop.Connection;

public final class ControlImpl extends _ControlImplBase {
   private Xid xid;
   private Connection connection;

   ControlImpl(Xid var1, Connection var2) {
      this.xid = var1;
      this.connection = var2;
   }

   public Coordinator get_coordinator() throws Unavailable {
      return new CoordinatorImpl(this.xid);
   }

   public Terminator get_terminator() throws Unavailable {
      return new TerminatorImpl(this.xid, this.connection, this);
   }
}
