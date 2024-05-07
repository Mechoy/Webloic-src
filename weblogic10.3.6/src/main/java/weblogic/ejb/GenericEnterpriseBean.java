package weblogic.ejb;

import java.io.IOException;
import java.io.ObjectInputStream;
import javax.ejb.EJBException;
import javax.ejb.EnterpriseBean;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.transaction.SystemException;
import weblogic.logging.LogOutputStream;
import weblogic.transaction.Transaction;
import weblogic.transaction.TransactionManager;
import weblogic.transaction.TxHelper;

public abstract class GenericEnterpriseBean implements EnterpriseBean {
   private static final long serialVersionUID = 2011822578998863408L;
   private transient Context ic;
   private boolean tracingEnabled = false;
   private final String beanClassName = this.getClass().getName();
   private transient LogOutputStream log;

   public GenericEnterpriseBean() {
      this.log = new LogOutputStream(this.beanClassName);

      try {
         this.ic = new InitialContext();
      } catch (NamingException var4) {
         throw new EJBException(var4);
      }

      try {
         Boolean var1 = (Boolean)this.ic.lookup("java:/comp/env/_WL_TracingEnabled");
         this.tracingEnabled = var1;
      } catch (NamingException var2) {
         this.tracingEnabled = false;
      } catch (ClassCastException var3) {
         throw new EJBException("_WL_TracingEnabled was found in your  environment entries, but it's type was not Boolean. Please modify your ejb-jar.xml and change the env-entry-type to be java.lang.Boolean.");
      }

   }

   protected final boolean isTracingEnabled() {
      return this.tracingEnabled;
   }

   protected void setTracingEnabled(boolean var1) {
      this.tracingEnabled = var1;
   }

   private String buildTraceString(String var1) {
      return "Method: " + var1 + " called in bean class: " + this.beanClassName + " SystemIdentityHashCode: " + System.identityHashCode(this) + " Trasaction ID: " + this.getCurrentXID();
   }

   protected void trace(String var1) {
      this.logDebugMessage(this.buildTraceString(var1));
   }

   protected void trace(String var1, Object var2) {
      this.logDebugMessage(this.buildTraceString(var1) + " Primary Key: " + var2);
   }

   protected void logDebugMessage(String var1) {
      this.log.debug(var1);
   }

   protected void logDebugMessage(String var1, Throwable var2) {
      this.log.debug(var1, var2);
   }

   protected void logErrorMessage(String var1) {
      this.log.error(var1);
   }

   protected void logErrorMessage(String var1, Throwable var2) {
      this.log.error(var1, var2);
   }

   protected Context getInitialContext() {
      return this.ic;
   }

   protected Object getEnvEntry(String var1) {
      try {
         return this.ic.lookup("java:comp/env/" + var1);
      } catch (NamingException var3) {
         throw new EJBException(var3);
      }
   }

   protected Transaction getCurrentTransaction() {
      return TxHelper.getTransaction();
   }

   protected String getCurrentXID() {
      Transaction var1 = this.getCurrentTransaction();
      return var1 == null ? TxHelper.status2String(6) : var1.getXID().toString();
   }

   protected TransactionManager getTransactionManager() {
      return TxHelper.getTransactionManager();
   }

   protected String getCurrentTransactionStatus() {
      Transaction var1 = TxHelper.getTransaction();
      if (var1 == null) {
         return TxHelper.status2String(6);
      } else {
         try {
            return TxHelper.status2String(var1.getStatus());
         } catch (SystemException var3) {
            throw new EJBException(var3);
         }
      }
   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      var1.defaultReadObject();

      try {
         this.ic = new InitialContext();
      } catch (NamingException var3) {
         throw new EJBException(var3);
      }

      this.log = new LogOutputStream(this.beanClassName);
   }
}
