package weblogic.ejb20.internal;

import java.io.Serializable;
import javax.ejb.EJBLocalHome;
import javax.naming.InitialContext;
import javax.naming.Name;
import javax.naming.NamingException;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.internal.EJBRuntimeUtils;
import weblogic.ejb20.interfaces.LocalHomeHandle;

public final class LocalHomeHandleImpl implements LocalHomeHandle, Serializable {
   private static final boolean debug = false;
   private static final boolean verbose = false;
   private static final long serialVersionUID = -4325019294372383553L;
   private Name localJNDIName;
   private transient EJBLocalHome home = null;

   public LocalHomeHandleImpl() {
   }

   public LocalHomeHandleImpl(EJBLocalHome var1, Name var2) {
      this.home = var1;
      this.localJNDIName = var2;
   }

   public EJBLocalHome getEJBLocalHome() {
      if (this.home == null) {
         try {
            InitialContext var1 = new InitialContext();
            this.home = (EJBLocalHome)var1.lookup(this.localJNDIName);
         } catch (ClassCastException var2) {
            EJBLogger.logStackTraceAndMessage(var2.getMessage(), var2);
            EJBRuntimeUtils.throwEJBException("ClassCastException: ", var2);
         } catch (NamingException var3) {
            EJBRuntimeUtils.throwEJBException("Unable to locate EJBLocalHome: '" + this.localJNDIName, var3);
         }
      }

      return this.home;
   }
}
