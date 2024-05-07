package weblogic.transaction.internal;

import weblogic.server.AbstractServerService;
import weblogic.server.ServiceFailureException;
import weblogic.transaction.TransactionHelper;

public class PrimordialTransactionService extends AbstractServerService {
   public void start() throws ServiceFailureException {
      TransactionHelper.setTransactionHelper(new TransactionHelperImpl());
   }
}
