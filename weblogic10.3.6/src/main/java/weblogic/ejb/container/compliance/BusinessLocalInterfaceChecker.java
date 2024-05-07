package weblogic.ejb.container.compliance;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.ejb.EJBLocalObject;
import weblogic.ejb.container.dd.xml.DDUtils;
import weblogic.ejb.container.interfaces.Ejb3SessionBeanInfo;
import weblogic.ejb.container.utils.MethodUtils;

public final class BusinessLocalInterfaceChecker extends BaseComplianceChecker {
   private final Ejb3SessionBeanInfo sbi;
   private final Set lbi;

   public BusinessLocalInterfaceChecker(Ejb3SessionBeanInfo var1) {
      this.sbi = var1;
      this.lbi = var1.getBusinessLocals();
   }

   public void checkLBIIsNotRBI() throws ComplianceException {
      Set var1 = this.sbi.getBusinessRemotes();
      Iterator var2 = this.lbi.iterator();

      Class var3;
      do {
         if (!var2.hasNext()) {
            return;
         }

         var3 = (Class)var2.next();
      } while(!var1.contains(var3));

      throw new ComplianceException(EJBComplianceTextFormatter.getInstance().LOCAL_INTERFACE_CANNOT_REMOTE(var3.getName()));
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

      Iterator var14 = this.lbi.iterator();

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

               throw new ComplianceException(EJBComplianceTextFormatter.getInstance().LOCAL_INTERFACE_NOT_FOUND_IN_BEAN(var9, var15.getName(), this.sbi.getBeanClass().toString()));
            }
         }
      }

   }

   public void checkBIExtendsEJBLocalObject() throws ComplianceException {
      Iterator var1 = this.lbi.iterator();

      Class var2;
      do {
         if (!var1.hasNext()) {
            return;
         }

         var2 = (Class)var1.next();
      } while(!EJBLocalObject.class.isAssignableFrom(var2));

      throw new ComplianceException(EJBComplianceTextFormatter.getInstance().LOCAL_INTERFACE_CANNOT_EXTEND_EJBLocalObject(var2.toString()));
   }

   public void checkBIHasMethods() throws ComplianceException {
      Iterator var1 = this.lbi.iterator();

      Class var2;
      do {
         if (!var1.hasNext()) {
            return;
         }

         var2 = (Class)var1.next();
      } while(var2.getMethods().length != 0);

      throw new ComplianceException(EJBComplianceTextFormatter.getInstance().BUSINESS_INTERFACE_WITHOUT_METHOD(var2.getName()));
   }
}
