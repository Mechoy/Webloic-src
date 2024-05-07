package weblogic.jms.adapter51;

import javax.resource.ResourceException;
import weblogic.jms.bridge.LocalTransaction;

public class AdapterLocalTransactionImpl implements LocalTransaction {
   private JMSManagedConnection mc;
   private LocalTransactionImpl localTran;

   public AdapterLocalTransactionImpl(JMSManagedConnection var1) {
      this.mc = var1;
      this.localTran = new LocalTransactionImpl(var1);
   }

   public void begin() throws ResourceException {
      this.localTran.begin();
   }

   public void commit() throws ResourceException {
      this.localTran.commit();
   }

   public void rollback() throws ResourceException {
      this.localTran.rollback();
   }
}
