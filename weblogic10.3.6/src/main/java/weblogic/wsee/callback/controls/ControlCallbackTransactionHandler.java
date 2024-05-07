package weblogic.wsee.callback.controls;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import javax.transaction.Transaction;
import javax.transaction.UserTransaction;
import javax.xml.namespace.QName;
import javax.xml.rpc.JAXRPCException;
import javax.xml.rpc.handler.GenericHandler;
import javax.xml.rpc.handler.MessageContext;
import weblogic.transaction.TransactionHelper;
import weblogic.wsee.handler.WLHandler;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.ws.WsPort;
import weblogic.wsee.ws.WsRegistry;

public class ControlCallbackTransactionHandler extends GenericHandler implements WLHandler {
   private static final String SAVED_TXN = "com.bea.workshop.controlcallback.savedtxn";
   private static final String USER_TXN = "com.bea.workshop.controlcallback.usertxn";
   private static boolean verbose = Verbose.isVerbose(ControlCallbackTransactionHandler.class);
   private static final String TRANSACTION_SEETING_REQUIRED = "Required";
   private static final String TRANSATION_SETTING_REQUIRES_NEW = "RequiresNew";

   public boolean handleRequest(MessageContext var1) {
      WlMessageContext var2 = WlMessageContext.narrow(var1);
      Object var3 = var2.getProperty("weblogic.wsee.callback.controls.ControlCallbackData");
      if (var3 == null) {
         return true;
      } else {
         try {
            this.setUpWLWTransaction(var2, (ControlCallbackData)var3);
            return true;
         } catch (RuntimeException var5) {
            var2.setProperty("weblogic.wsee.local.invoke.throwable", var5);
            throw var5;
         } catch (Throwable var6) {
            if (verbose) {
               var6.printStackTrace();
            }

            Verbose.log("Unable to start transaction for control callback", var6);
            var2.setProperty("weblogic.wsee.local.invoke.throwable", var6);
            throw new JAXRPCException(var6);
         }
      }
   }

   public boolean handleResponse(MessageContext var1) {
      try {
         WlMessageContext var2 = WlMessageContext.narrow(var1);
         Object var3 = var2.getProperty("weblogic.wsee.callback.controls.ControlCallbackData");
         if (var3 == null) {
            return true;
         } else {
            this.handleEndTxn(var2);
            return true;
         }
      } catch (RuntimeException var4) {
         throw var4;
      } catch (Throwable var5) {
         if (verbose) {
            var5.printStackTrace();
         }

         Verbose.log("Unable to finish transaction for control callback", var5);
         throw new JAXRPCException(var5);
      }
   }

   public boolean handleClosure(MessageContext var1) {
      return this.handleResponse(var1);
   }

   public boolean handleFault(MessageContext var1) {
      return this.handleResponse(var1);
   }

   public QName[] getHeaders() {
      return new QName[0];
   }

   private void setUpWLWTransaction(WlMessageContext var1, ControlCallbackData var2) throws Exception {
      Object var3 = var2.getEventRef();
      WsPort var4 = WsRegistry.instance().lookup(var2.getServiceUri());
      Class var5 = var4.getEndpoint().getJwsClass();
      Class var6 = this.getEventHandlerClass();
      Class var7 = this.getEventRefClass();
      Method var8 = null;
      Class var9 = null;
      Method[] var10 = var5.getMethods();
      Method[] var11 = var10;
      int var12 = var10.length;

      for(int var13 = 0; var13 < var12; ++var13) {
         Method var14 = var11[var13];
         if (var14.isAnnotationPresent(var6)) {
            Annotation var15 = var14.getAnnotation(var6);
            Method var16 = var15.getClass().getMethod("eventSet");
            var9 = (Class)var16.invoke(var15);
            Method var17 = var15.getClass().getMethod("eventName");
            String var18 = (String)var17.invoke(var15);
            Method var19 = var9.getMethod(var18, var14.getParameterTypes());
            Constructor var20 = var7.getConstructor(Method.class);
            Object var21 = var20.newInstance(var19);
            if (var3.equals(var21)) {
               var8 = var19;
               break;
            }
         }
      }

      if (var8 != null) {
         Class var22 = this.getTxnAttributeClass();
         Annotation var23 = var8.getAnnotation(var22);
         if (var23 == null) {
            Class var24 = this.getControlInf(var9);
            if (var24 != null) {
               var23 = var24.getAnnotation(var22);
            }

            if (var23 == null) {
               return;
            }
         }

         Method var25 = var23.getClass().getMethod("value");
         Object var26 = var25.invoke(var23);
         Method var27 = var26.getClass().getMethod("value");
         String var28 = (String)var27.invoke(var26);
         if (var28.equals("Required") || var28.equals("RequiresNew")) {
            this.handleStartTxn(var1, var28.equals("RequiresNew"));
         }

         if (verbose) {
            Verbose.say("WLW Transaction setting for callback is " + var28);
         }
      }

   }

   private void handleStartTxn(WlMessageContext var1, boolean var2) throws Exception {
      int var3 = 6;
      Transaction var4 = TransactionHelper.getTransactionHelper().getTransaction();
      if (var4 != null) {
         if (var2) {
            Transaction var5 = TransactionHelper.getTransactionHelper().getTransactionManager().suspend();
            var1.setProperty("com.bea.workshop.controlcallback.savedtxn", var5);
            beginTxn(var1);
            return;
         }

         var3 = var4.getStatus();
      }

      if (var4 == null || var3 != 0) {
         beginTxn(var1);
      }

   }

   private static void beginTxn(WlMessageContext var0) throws Exception {
      UserTransaction var1 = TransactionHelper.getTransactionHelper().getUserTransaction();
      var1.begin();
      var0.setProperty("com.bea.workshop.controlcallback.usertxn", var1);
   }

   private void handleEndTxn(WlMessageContext var1) throws Exception {
      boolean var7 = false;

      label86: {
         try {
            var7 = true;
            UserTransaction var2 = (UserTransaction)var1.getProperty("com.bea.workshop.controlcallback.usertxn");
            if (var2 == null) {
               var7 = false;
               break label86;
            }

            int var3 = var2.getStatus();
            if (var3 == 1) {
               var2.rollback();
               var7 = false;
            } else if (var3 != 4) {
               if (var3 != 9) {
                  var2.commit();
                  var7 = false;
               } else {
                  var7 = false;
               }
            } else {
               var7 = false;
            }
         } finally {
            if (var7) {
               Transaction var5 = (Transaction)var1.getProperty("com.bea.workshop.controlcallback.savedtxn");
               if (var5 != null) {
                  TransactionHelper.getTransactionHelper().getTransactionManager().resume(var5);
               }

               var1.removeProperty("com.bea.workshop.controlcallback.usertxn");
               var1.removeProperty("com.bea.workshop.controlcallback.savedtxn");
            }
         }

         Transaction var9 = (Transaction)var1.getProperty("com.bea.workshop.controlcallback.savedtxn");
         if (var9 != null) {
            TransactionHelper.getTransactionHelper().getTransactionManager().resume(var9);
         }

         var1.removeProperty("com.bea.workshop.controlcallback.usertxn");
         var1.removeProperty("com.bea.workshop.controlcallback.savedtxn");
         return;
      }

      Transaction var10 = (Transaction)var1.getProperty("com.bea.workshop.controlcallback.savedtxn");
      if (var10 != null) {
         TransactionHelper.getTransactionHelper().getTransactionManager().resume(var10);
      }

      var1.removeProperty("com.bea.workshop.controlcallback.usertxn");
      var1.removeProperty("com.bea.workshop.controlcallback.savedtxn");
   }

   private Class<?> getEventHandlerClass() throws ClassNotFoundException {
      return this.getContextClassLoader().loadClass("org.apache.beehive.controls.api.events.EventHandler");
   }

   private Class<?> getEventRefClass() throws ClassNotFoundException {
      return this.getContextClassLoader().loadClass("org.apache.beehive.controls.api.events.EventRef");
   }

   private Class<?> getTxnAttributeClass() throws ClassNotFoundException {
      return this.getContextClassLoader().loadClass("com.bea.control.annotations.TransactionAttribute");
   }

   private ClassLoader getContextClassLoader() {
      return Thread.currentThread().getContextClassLoader();
   }

   private Class<?> getControlInf(Class var1) throws Exception {
      Class var2 = this.getContextClassLoader().loadClass("org.apache.beehive.controls.api.bean.ControlExtension");
      Class var3 = this.getContextClassLoader().loadClass("org.apache.beehive.controls.api.bean.ControlInterface");
      Class var4 = var1.getEnclosingClass();
      return var4 != null ? this.getControlInf(var4, var3, var2) : null;
   }

   private Class<?> getControlInf(Class var1, Class var2, Class var3) {
      if (var1 != null && var1.isAnnotationPresent(var2)) {
         return var1;
      } else {
         if (var1 != null && var1.isAnnotationPresent(var3)) {
            Class[] var4 = var1.getInterfaces();
            Class[] var5 = var4;
            int var6 = var4.length;

            for(int var7 = 0; var7 < var6; ++var7) {
               Class var8 = var5[var7];
               Class var9 = this.getControlInf(var8, var2, var3);
               if (var9 != null) {
                  return var9;
               }
            }
         }

         return null;
      }
   }
}
