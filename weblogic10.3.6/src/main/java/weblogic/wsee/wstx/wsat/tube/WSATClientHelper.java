package weblogic.wsee.wstx.wsat.tube;

import com.sun.xml.ws.api.message.Header;
import com.sun.xml.ws.api.message.Headers;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.transaction.InvalidTransactionException;
import javax.transaction.SystemException;
import javax.xml.ws.WebServiceException;
import org.w3c.dom.Element;
import weblogic.transaction.ClientTransactionManager;
import weblogic.transaction.Transaction;
import weblogic.wsee.WseeWsatLogger;
import weblogic.wsee.wstx.wsat.WSATHelper;
import weblogic.wsee.wstx.wsc.common.WSATCoordinationContextBuilder;
import weblogic.wsee.wstx.wsc.common.WSCBuilderFactory;
import weblogic.wsee.wstx.wsc.common.types.CoordinationContextIF;

public class WSATClientHelper implements WSATClient {
   public List<Header> doHandleRequest(TransactionalAttribute var1, Map<String, Object> var2) {
      if (!var1.isEnabled()) {
         return null;
      } else {
         Transaction var3 = WSATTubeHelper.getTransaction();
         boolean var4 = var3 != null;
         if (!var4) {
            if (WSATHelper.isDebugEnabled()) {
               WseeWsatLogger.logOutboundApplicationMessageNoTransaction();
            }

            if (var1.isRequired()) {
               throw new WebServiceException("no transaction to be exported!");
            } else {
               return null;
            }
         } else {
            if (WSATHelper.isDebugEnabled()) {
               WseeWsatLogger.logOutboundApplicationMessageTransactionBeforeAddingContext(var3);
            }

            List var5 = this.processTransactionalRequest(var1, var2);
            if (WSATHelper.isDebugEnabled()) {
               WseeWsatLogger.logOutboundApplicationMessageTransactionAfterAddingContext(var3);
            }

            return var5;
         }
      }
   }

   public boolean doHandleResponse(Map<String, Object> var1) {
      if (WSATHelper.isDebugEnabled()) {
         WseeWsatLogger.logInboundApplicationMessage();
      }

      Transaction var2 = this.getWSATTransactionFromMap(var1);
      return var2 == null || this.resume(var2);
   }

   public void doHandleException(Map<String, Object> var1) {
      if (WSATHelper.isDebugEnabled()) {
         WseeWsatLogger.logInboundApplicationMessage();
      }

      Transaction var2 = this.getWSATTransactionFromMap(var1);
      if (var2 != null) {
         this.resume(var2);
      }

   }

   private Transaction getWSATTransactionFromMap(Map var1) {
      Transaction var2 = (Transaction)var1.get("wsat.transaction");
      return var2;
   }

   private boolean resume(Transaction var1) {
      if (WSATHelper.isDebugEnabled()) {
         WseeWsatLogger.logWillResumeInClientSideHandler(var1, Thread.currentThread());
      }

      try {
         WSATTubeHelper.getTransactionHelper().getTransactionManager().resume(var1);
         if (WSATHelper.isDebugEnabled()) {
            WseeWsatLogger.logResumedInClientSideHandler(var1, Thread.currentThread());
         }

         return true;
      } catch (InvalidTransactionException var3) {
         if (WSATHelper.isDebugEnabled()) {
            WseeWsatLogger.logInvalidTransactionExceptionInClientSideHandler(var3, var1, Thread.currentThread());
         }

         WSATTubeHelper.getTransactionHelper().getTransactionManager().forceResume(var1);
         var1.setRollbackOnly(var3);
         return false;
      } catch (SystemException var4) {
         if (WSATHelper.isDebugEnabled()) {
            WseeWsatLogger.logSystemExceptionInClientSideHandler(var4, var1, Thread.currentThread());
         }

         WSATTubeHelper.getTransactionHelper().getTransactionManager().forceResume(var1);
         var1.setRollbackOnly(var4);
         return false;
      }
   }

   private List<Header> processTransactionalRequest(TransactionalAttribute var1, Map var2) {
      Transaction var3 = this.suspend(var2);
      if (var3 == null) {
         return null;
      } else {
         ArrayList var4 = new ArrayList();
         if (WSATHelper.isDebugEnabled()) {
            WseeWsatLogger.logSuspendSuccessfulInClientSideHandler(var3, Thread.currentThread());
         }

         String var5 = WSATTubeHelper.getWSATTxIdForTransaction(var3);
         long var6 = var3.getTimeToLiveMillis();
         if (WSATHelper.isDebugEnabled()) {
            WseeWsatLogger.logWSATInfoInClientSideHandler(var5, var6, var3, Thread.currentThread());
         }

         WSCBuilderFactory var8 = WSCBuilderFactory.newInstance(var1.getVersion());
         WSATCoordinationContextBuilder var9 = var8.newWSATCoordinationContextBuilder();
         CoordinationContextIF var10 = var9.txId(var5).expires(var6).soapVersion(var1.getSoapVersion()).mustUnderstand(true).build();
         Header var11 = Headers.create(var10.getJAXBRIContext(), var10.getDelegate());
         var4.add(var11);
         if (WSATHelper.isDebugEnabled()) {
            WseeWsatLogger.logOutboundApplicationMessageTransactionAfterAddingContext(var3);
         }

         if (WSATTubeHelper.isIssuedTokenEnabled()) {
            Element var12 = var8.newIssuedTokenBuilder().buildFromContext(var10);
            var4.add(Headers.create(var12));
         }

         return var4;
      }
   }

   private Transaction suspend(Map var1) {
      Transaction var2 = null;

      try {
         ClientTransactionManager var3 = WSATTubeHelper.getTransactionHelper().getTransactionManager();
         if (WSATHelper.isDebugEnabled()) {
            WseeWsatLogger.logAboutToSuspendInClientSideHandler(var3, Thread.currentThread());
         }

         var2 = (Transaction)var3.suspend();
         var2.setLocalProperty("weblogic.transaction.otsTransactionExport", (Object)null);
         var1.put("wsat.transaction", var2);
         if (WSATHelper.isDebugEnabled()) {
            WseeWsatLogger.logSuspendedInClientSideHandler(var2, Thread.currentThread());
         }

         return var2;
      } catch (SystemException var4) {
         WseeWsatLogger.logSystemExceptionDuringSuspend(var4, var2, Thread.currentThread());
         return null;
      }
   }
}
