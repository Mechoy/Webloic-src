package weblogic.ejb.container.compliance;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import weblogic.ejb.container.interfaces.ClientDrivenBeanInfo;
import weblogic.ejb.container.interfaces.SessionBeanInfo;

final class SessionHomeInterfaceChecker extends HomeInterfaceChecker {
   private final boolean isStateful;
   private final boolean isStateless;
   private SessionBeanInfo sbi;

   SessionHomeInterfaceChecker(Class var1, Class var2, Class var3, ClientDrivenBeanInfo var4, Class var5) {
      super(var1, var2, var3, var4, var5);
      this.sbi = (SessionBeanInfo)var4;
      this.isStateful = this.sbi.isStateful();
      this.isStateless = !this.isStateful;
   }

   public void checkStatefulCreates() throws ComplianceException {
      if (!this.isStateless) {
         List var1 = this.getCreateMethods();
         if (var1.size() == 0) {
            if (this.checkingRemoteClientView()) {
               throw new ComplianceException(this.fmt.STATEFUL_HOME_CREATE(this.ejbName));
            } else {
               throw new ComplianceException(this.fmt.STATEFUL_LOCAL_HOME_CREATE(this.ejbName));
            }
         }
      }
   }

   public void checkStatelessNoArgCreate() throws ComplianceException {
      if (!this.isStateful) {
         List var1 = this.getCreateMethods();
         if (var1.size() != 1) {
            if (this.checkingRemoteClientView()) {
               throw new ComplianceException(this.fmt.STATELESS_HOME_NOARG_CREATE(this.ejbName));
            } else {
               throw new ComplianceException(this.fmt.STATELESS_LOCAL_HOME_NOARG_CREATE(this.ejbName));
            }
         } else {
            Method var2 = (Method)var1.get(0);
            if (!"create".equals(var2.getName())) {
               if (this.checkingRemoteClientView()) {
                  throw new ComplianceException(this.fmt.STATELESS_HOME_NOARG_CREATE(this.ejbName));
               } else {
                  throw new ComplianceException(this.fmt.STATELESS_LOCAL_HOME_NOARG_CREATE(this.ejbName));
               }
            } else if (!ComplianceUtils.methodTakesNoArgs(var2)) {
               if (this.checkingRemoteClientView()) {
                  throw new ComplianceException(this.fmt.STATELESS_HOME_NOARG_CREATE(this.ejbName));
               } else {
                  throw new ComplianceException(this.fmt.STATELESS_LOCAL_HOME_NOARG_CREATE(this.ejbName));
               }
            }
         }
      }
   }

   public void checkNoHomeMethods() throws ComplianceException {
      List var1 = this.getHomeMethods();
      var1.addAll(this.getHomeInterfaceHomeMethods());
      if (var1.size() > 0) {
         Iterator var2 = var1.iterator();
         String var3 = ((Method)var2.next()).getName();
         throw new ComplianceException(this.fmt.HOME_METHODS_NOT_ALLOWED_ON_SESSION_20(this.ejbName, this.methodSig(var3)));
      }
   }

   protected List getHomeInterfaceHomeMethods() {
      List var1 = super.getHomeInterfaceHomeMethods();
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         Method var3 = (Method)var2.next();
         if (var3.getName().equals("remove")) {
            var2.remove();
         }
      }

      return var1;
   }
}
