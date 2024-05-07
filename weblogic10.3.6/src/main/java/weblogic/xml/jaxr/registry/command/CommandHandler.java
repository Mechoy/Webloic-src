package weblogic.xml.jaxr.registry.command;

import java.util.ArrayList;
import java.util.Collections;
import javax.xml.registry.BulkResponse;
import javax.xml.registry.JAXRException;
import weblogic.auddi.util.uuid.UUIDException;
import weblogic.auddi.util.uuid.UUIDGeneratorFactory;
import weblogic.xml.jaxr.registry.BulkResponseImpl;
import weblogic.xml.jaxr.registry.util.JAXRUtil;

public class CommandHandler {
   public static BulkResponse runBulkQueryCommand(BulkQueryCommand var0) throws JAXRException {
      try {
         Object var1;
         if (var0.getRegistryServiceImpl().getConnectionImpl().isSynchronous()) {
            var1 = var0.execute();
         } else {
            var1 = new BulkResponseImpl(var0.getRegistryServiceImpl());
            String var2 = UUIDGeneratorFactory.getGenerator().uuidGen();
            ((BulkResponseImpl)var1).setRequestId(var2);
            ((BulkResponseImpl)var1).setStatus(3);
            AsynchronousRequest var3 = new AsynchronousRequest(var0, (BulkResponseImpl)var1);
            var3.start();
            var0.getRegistryServiceImpl().addBulkResponse(var2, (BulkResponse)var1);
         }

         return (BulkResponse)var1;
      } catch (UUIDException var4) {
         throw JAXRUtil.mapException(var4);
      }
   }

   private static class AsynchronousRequest extends Thread {
      private BulkQueryCommand m_bulkQueryCommand;
      private BulkResponseImpl m_bulkResponseImpl;

      public AsynchronousRequest(BulkQueryCommand var1, BulkResponseImpl var2) {
         this.m_bulkQueryCommand = var1;
         this.m_bulkResponseImpl = var2;
      }

      public void run() {
         Object var1;
         try {
            var1 = this.m_bulkQueryCommand.execute();
         } catch (JAXRException var7) {
            ArrayList var3 = new ArrayList();
            var3.add(var7);
            var1 = new BulkResponseImpl(this.m_bulkQueryCommand.getRegistryServiceImpl());
            ((BulkResponseImpl)var1).setResponse(Collections.EMPTY_LIST, var3, false);
         }

         try {
            this.m_bulkResponseImpl.setResponse(((BulkResponse)var1).getCollection(), ((BulkResponse)var1).getExceptions(), ((BulkResponse)var1).isPartialResponse());
            synchronized(this.m_bulkResponseImpl) {
               this.m_bulkResponseImpl.notify();
            }
         } catch (JAXRException var6) {
            System.out.println("Log: could not complete asynchronous query of " + this.m_bulkQueryCommand);
         }

      }
   }
}
