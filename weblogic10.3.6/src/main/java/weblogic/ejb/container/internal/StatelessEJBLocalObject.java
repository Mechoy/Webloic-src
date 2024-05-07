package weblogic.ejb.container.internal;

import javax.ejb.EJBException;
import javax.ejb.EJBLocalObject;
import javax.ejb.RemoveException;
import weblogic.ejb.container.interfaces.BaseEJBLocalObjectIntf;
import weblogic.ejb20.interfaces.LocalHandle;
import weblogic.ejb20.internal.LocalHandleImpl;

public abstract class StatelessEJBLocalObject extends StatelessLocalObject implements BaseEJBLocalObjectIntf {
   public void remove(MethodDescriptor var1) throws EJBException, RemoveException {
      this.checkMethodPermissions(var1, EJBContextHandler.EMPTY);
   }

   public LocalHandle getLocalHandleObject() throws EJBException {
      if (debugLogger.isDebugEnabled()) {
         debug("Getting handle in elo:" + this);
      }

      return new LocalHandleImpl(this);
   }

   protected boolean isIdentical(MethodDescriptor var1, EJBLocalObject var2) throws EJBException {
      if (super.isIdentical(var1, var2)) {
         return var2 instanceof StatelessEJBLocalObject;
      } else {
         return false;
      }
   }

   private static void debug(String var0) {
      debugLogger.debug("[StatelessEJBLocalObject] " + var0);
   }
}
