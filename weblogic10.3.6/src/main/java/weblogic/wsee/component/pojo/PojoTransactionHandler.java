package weblogic.wsee.component.pojo;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import javax.xml.namespace.QName;
import javax.xml.rpc.JAXRPCException;
import javax.xml.rpc.handler.GenericHandler;
import javax.xml.rpc.handler.MessageContext;
import weblogic.jws.Transactional;
import weblogic.transaction.ClientTransactionManager;
import weblogic.transaction.Transaction;
import weblogic.transaction.TransactionHelper;
import weblogic.transaction.UserTransaction;
import weblogic.wsee.handler.WLHandler;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.ws.WsMethod;
import weblogic.wsee.ws.WsParameterType;
import weblogic.wsee.ws.WsPort;

public class PojoTransactionHandler extends GenericHandler implements WLHandler {
   private static final String SAVEDTX = "weblogic.wsee.component.pojo.savedtx";
   private static final String TXBEGUN = "weblogic.wsee.component.pojo.txbegun";
   private static final String TXMETHOD = "weblogic.wsee.component.pojo.txmethod";
   private static boolean verbose = Verbose.isVerbose(PojoTransactionHandler.class);
   private static boolean debug = false;
   private UserTransaction utx = null;
   private ClientTransactionManager ctm = null;

   private Class[] toClassArray(Iterator var1) {
      ArrayList var2 = new ArrayList();

      while(var1.hasNext()) {
         WsParameterType var3 = (WsParameterType)var1.next();
         var2.add(var3.getJavaType());
      }

      return (Class[])((Class[])var2.toArray(new Class[var2.size()]));
   }

   private void handleStartTx(WlMessageContext var1) throws Throwable {
      if (verbose) {
         Verbose.log((Object)"POJOTxnhandler: start tx");
      }

      WsPort var2 = var1.getDispatcher().getWsPort();
      if (debug) {
         Verbose.log((Object)("wsport = " + var2));
      }

      WsMethod var3 = var1.getDispatcher().getWsMethod();
      if (debug) {
         Verbose.log((Object)("wsmethod = " + var3));
      }

      this.ctm = TransactionHelper.getTransactionHelper().getTransactionManager();
      this.utx = (UserTransaction)TransactionHelper.getTransactionHelper().getUserTransaction();
      int var4 = 30;
      Class var5 = var2.getEndpoint().getJwsClass();
      if (debug) {
         Verbose.log((Object)("class = " + var5));
      }

      Annotation var6 = var5.getAnnotation(Transactional.class);
      Transactional var7 = null;
      boolean var8 = false;
      if (var6 != null) {
         if (debug) {
            Verbose.log((Object)"class has transactional annotation");
         }

         var7 = (Transactional)var6;
         var4 = var7.timeout();
         if (debug) {
            Verbose.log((Object)("transaction enabled = " + var7.value() + " timeout = " + var4));
         }

         var8 = var7.value();
      }

      Method var9 = var5.getMethod(var3.getMethodName(), this.toClassArray(var3.getParameters()));
      if (debug) {
         Verbose.log((Object)("method = " + var9));
      }

      var6 = var9.getAnnotation(Transactional.class);
      if (var6 != null) {
         if (debug) {
            Verbose.log((Object)"method has transactional annotation");
         }

         var7 = (Transactional)var6;
         var4 = var7.timeout();
         if (debug) {
            Verbose.log((Object)("transaction enabled = " + var7.value() + " timeout = " + var4));
         }

         var8 = var7.value();
      }

      if (debug) {
         Verbose.log((Object)("transactional = " + var8));
      }

      var1.setProperty("weblogic.wsee.component.pojo.txmethod", new Boolean(var8));
      Transaction var10 = null;
      if (var8) {
         if (var4 < 0) {
            throw new IllegalArgumentException("Invalid transactional timeout value for operation " + var3.getOperationName());
         }

         var10 = (Transaction)TransactionHelper.getTransactionHelper().getTransaction();
         if (var10 == null) {
            if (debug) {
               Verbose.log((Object)"No preexisting txn = ");
            }

            this.utx.setTransactionTimeout(var4);
            this.utx.begin();
            if (debug) {
               Verbose.log((Object)"Begun txn");
            }

            var1.setProperty("weblogic.wsee.component.pojo.txbegun", new Boolean(true));
         } else {
            if (debug) {
               Verbose.log((Object)("Preexisting txn " + var10 + " - new one not started"));
            }

            var1.setProperty("weblogic.wsee.component.pojo.savedtx", var10);
         }
      } else {
         var10 = (Transaction)this.ctm.forceSuspend();
         if (var10 != null) {
            if (verbose) {
               Verbose.log((Object)("Suspending pre-existing txn " + var10));
            }

            var1.setProperty("weblogic.wsee.component.pojo.savedtx", var10);
         }
      }

   }

   private void handleEndTx(WlMessageContext var1) throws Throwable {
      boolean var2 = var1.containsProperty("weblogic.jws.wlw.rollback_on_checked_exception");
      this.ctm = TransactionHelper.getTransactionHelper().getTransactionManager();
      this.utx = (UserTransaction)TransactionHelper.getTransactionHelper().getUserTransaction();
      Transaction var3 = (Transaction)var1.getProperty("weblogic.wsee.component.pojo.savedtx");
      Boolean var4 = (Boolean)var1.getProperty("weblogic.wsee.component.pojo.txbegun");
      Boolean var5 = (Boolean)var1.getProperty("weblogic.wsee.component.pojo.txmethod");
      var1.removeProperty("weblogic.wsee.component.pojo.savedtx");
      var1.removeProperty("weblogic.wsee.component.pojo.txbegun");
      var1.removeProperty("weblogic.wsee.component.pojo.txmethod");
      boolean var6 = var1.hasFault();
      boolean var7 = false;
      if (var5 != null) {
         var7 = var5;
      }

      boolean var8 = false;
      if (var4 != null) {
         var8 = var4;
      }

      if (debug) {
         Verbose.log((Object)("hasFault = " + var6 + " transactional = " + var7 + " begun tx " + var8));
      }

      Throwable var9 = (Throwable)var1.getProperty("weblogic.wsee.service_specific_exception");
      boolean var10 = var9 != null;
      if (debug) {
         Verbose.log((Object)("isAppException = " + var10));
         Verbose.log((Object)("rollbackTxOnCheckedException = " + var2));
      }

      try {
         if (var7) {
            if (var6 || var10 && var2) {
               if (var8) {
                  if (verbose) {
                     Verbose.log((Object)"Rolling back");
                  }

                  this.utx.rollback();
               } else if (var3 != null) {
                  if (verbose) {
                     Verbose.log((Object)"Setting rollback only");
                  }

                  var3.setRollbackOnly("Failed to invoke method ", var1.getFault());
               }
            } else if (var8 && !var6) {
               if (this.utx.getStatus() == 1) {
                  if (verbose) {
                     Verbose.log((Object)"Can't commit - marked rb");
                  }

                  this.utx.rollback();
               } else {
                  if (verbose) {
                     Verbose.log((Object)"Commiting txn");
                  }

                  this.utx.commit();
               }
            }
         } else if (var3 != null) {
            if (verbose) {
               Verbose.log((Object)("resuming old txn " + var3));
            }

            this.ctm.forceResume(var3);
         }

      } catch (Exception var12) {
         throw new JAXRPCException(var12);
      }
   }

   public boolean handleRequest(MessageContext var1) {
      WlMessageContext var2 = WlMessageContext.narrow(var1);

      try {
         this.handleStartTx(var2);
         return true;
      } catch (RuntimeException var4) {
         throw var4;
      } catch (Throwable var5) {
         throw new JAXRPCException(var5);
      }
   }

   public boolean handleResponse(MessageContext var1) {
      WlMessageContext var2 = WlMessageContext.narrow(var1);

      try {
         this.handleEndTx(var2);
         return true;
      } catch (RuntimeException var4) {
         throw var4;
      } catch (Throwable var5) {
         throw new JAXRPCException(var5);
      }
   }

   public boolean handleClosure(MessageContext var1) {
      return this.handleResponse(var1);
   }

   public QName[] getHeaders() {
      return new QName[0];
   }
}
