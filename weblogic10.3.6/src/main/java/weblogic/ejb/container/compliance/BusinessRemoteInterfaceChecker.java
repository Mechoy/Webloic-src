package weblogic.ejb.container.compliance;

import java.lang.reflect.Method;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.ejb.EJBObject;
import weblogic.ejb.container.dd.xml.DDUtils;
import weblogic.ejb.container.interfaces.DeploymentInfo;
import weblogic.ejb.container.interfaces.Ejb3SessionBeanInfo;
import weblogic.ejb.container.utils.MethodUtils;

public final class BusinessRemoteInterfaceChecker extends BaseComplianceChecker {
   private final Ejb3SessionBeanInfo sbi;
   private final Set rbi;
   private final DeploymentInfo di;

   public BusinessRemoteInterfaceChecker(Ejb3SessionBeanInfo var1) {
      this.sbi = var1;
      this.rbi = var1.getBusinessRemotes();
      this.di = var1.getDeploymentInfo();
   }

   public void checkRBIIsNotLBI() throws ComplianceException {
      Set var1 = this.sbi.getBusinessLocals();

      Iterator var2;
      Class var3;
      for(var2 = var1.iterator(); var2.hasNext(); var3 = (Class)var2.next()) {
      }

      var2 = this.rbi.iterator();

      do {
         if (!var2.hasNext()) {
            return;
         }

         var3 = (Class)var2.next();
      } while(!var1.contains(var3));

      throw new ComplianceException(EJBComplianceTextFormatter.getInstance().REMOTE_INTERFACE_IS_LOCAL(var3.toString()));
   }

   public void checkBIMethodsMatchBeanMethods() throws ComplianceException {
      Method[] var1 = this.sbi.getBeanClass().getMethods();
      HashSet var2 = new HashSet(var1.length);
      Method[] var3 = var1;
      int var4 = var1.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Method var6 = var3[var5];
         var2.add(DDUtils.getMethodSignature(var6));
      }

      Iterator var14 = this.rbi.iterator();

      while(var14.hasNext()) {
         Class var15 = (Class)var14.next();
         Method[] var16 = var15.getMethods();
         int var17 = var16.length;

         for(int var7 = 0; var7 < var17; ++var7) {
            Method var8 = var16[var7];
            String var9 = DDUtils.getMethodSignature(var8);
            if (!var2.contains(var9)) {
               Method[] var10 = var1;
               int var11 = var1.length;

               for(int var12 = 0; var12 < var11; ++var12) {
                  Method var13 = var10[var12];
                  if (MethodUtils.potentialBridgeCandidate(var8, var13)) {
                     throw new ComplianceException(EJBComplianceTextFormatter.getInstance().EJB_MAY_BE_MISSING_BRIDGE_METHOD(var9, var15.getName()));
                  }
               }

               throw new ComplianceException(EJBComplianceTextFormatter.getInstance().REMOTE_METHOD_NOT_FOUND_IN_BEAN(var9, var15.getName(), this.sbi.getBeanClass().toString()));
            }
         }
      }

   }

   public void checkBIExtendsEJBObject() throws ComplianceException {
      Iterator var1 = this.rbi.iterator();

      Class var2;
      do {
         if (!var1.hasNext()) {
            return;
         }

         var2 = (Class)var1.next();
      } while(!EJBObject.class.isAssignableFrom(var2));

      throw new ComplianceException(EJBComplianceTextFormatter.getInstance().REMOTE_INTERFACE_EXTEND_EJBOBJECT(var2.toString()));
   }

   public void checkBIHasMethods() throws ComplianceException {
      Iterator var1 = this.rbi.iterator();

      Class var2;
      do {
         if (!var1.hasNext()) {
            return;
         }

         var2 = (Class)var1.next();
      } while(var2.getMethods().length != 0);

      throw new ComplianceException(EJBComplianceTextFormatter.getInstance().REMOTE_BUSINESS_INTERFACE_NO_METHOD(var2.getName()));
   }

   public void checkBIMethodsThrowRemoteException() throws ComplianceException {
      Iterator var1 = this.rbi.iterator();

      while(true) {
         while(var1.hasNext()) {
            Class var2 = (Class)var1.next();
            Method[] var3;
            int var4;
            if (!Remote.class.isAssignableFrom(var2)) {
               var3 = var2.getMethods();

               for(var4 = 0; var4 < var3.length; ++var4) {
                  Class[] var8 = var3[var4].getExceptionTypes();

                  for(int var9 = 0; var9 < var8.length; ++var9) {
                     if (RemoteException.class == var8[var9]) {
                        throw new ComplianceException(EJBComplianceTextFormatter.getInstance().REMOTE_BUSINESS_INTERFACE_THROW_REMOTEEXCEPTION(var3[var4].toString(), var2.toString()));
                     }
                  }
               }
            } else {
               var3 = var2.getMethods();

               for(var4 = 0; var4 < var3.length; ++var4) {
                  boolean var5 = false;
                  Class[] var6 = var3[var4].getExceptionTypes();
                  if (var6 != null && var6.length != 0) {
                     for(int var7 = 0; var7 < var6.length; ++var7) {
                        if (RemoteException.class == var6[var7]) {
                           var5 = true;
                           break;
                        }
                     }
                  }

                  if (!var5) {
                     throw new ComplianceException(EJBComplianceTextFormatter.getInstance().REMOTE_INTERFACE_NOT_THROW_REMOTEEXCEPTION(var3[var4].toString(), var2.toString()));
                  }
               }
            }
         }

         return;
      }
   }
}
