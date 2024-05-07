package weblogic.ejb.container.compliance;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import javax.ejb.TimedObject;
import javax.ejb.Timer;
import weblogic.ejb.container.dd.xml.DDUtils;
import weblogic.ejb.container.interfaces.BeanInfo;
import weblogic.ejb.container.interfaces.SessionBeanInfo;
import weblogic.utils.ErrorCollectionException;

public final class TimeoutCheckHelper {
   public static void validateTimeoutMethodsIdentical(Method var0, Method var1) throws ComplianceException {
      if (var0 != null && var1 != null && !var0.equals(var1)) {
         throw new ComplianceException(EJBComplianceTextFormatter.getInstance().TIMEOUT_IN_DD_NOT_COMPATIBLE_WITH_ANNOTATION());
      }
   }

   public static void validateOnlyOneTimeoutMethod(Collection var0) throws ComplianceException {
      if (var0 != null) {
         if (var0.size() > 1) {
            throw new ComplianceException(EJBComplianceTextFormatter.getInstance().BEAN_CAN_HAVE_ONE_TIMEOUT_METHOD());
         }
      }
   }

   public static void validateTimeoutMethodIsejbTimeout(Class var0, Method var1) throws ComplianceException {
      if (var1 != null) {
         if (TimedObject.class.isAssignableFrom(var0) && !var1.getName().equals("ejbTimeout")) {
            throw new ComplianceException(EJBComplianceTextFormatter.getInstance().TIMEOUT_CAN_ONLY_SPECIFY_EJBTIMEOUT_METHOD(var0.getSimpleName()));
         }
      }
   }

   public static void validateTimeoutMethodExistsInBC(Method var0, Class var1, String var2) throws ComplianceException {
      if (var0 == null) {
         EJBComplianceTextFormatter var3 = new EJBComplianceTextFormatter();
         throw new ComplianceException(var3.EJB_TIMEOUT_METHOD_NOT_FOUND(var1.getSimpleName(), DDUtils.getMethodSignature(var2, new String[]{"javax.ejb.Timer"})));
      }
   }

   public static void validateTimeoutMethod(BeanInfo var0) throws ComplianceException, ErrorCollectionException {
      Method var1 = var0.getTimeoutMethod();
      if (var1 != null) {
         String var2 = var0.getEJBName();
         Class var3 = var0.getBeanClass();
         ErrorCollectionException var4 = new ErrorCollectionException();
         if (var0.isSessionBean()) {
            SessionBeanInfo var5 = (SessionBeanInfo)var0;
            if (var5.isStateful()) {
               var4.add(new ComplianceException(EJBComplianceTextFormatter.getInstance().STATEFULE_BEAN_CANNOT_IMPLEMENTS_TIMEOUT()));
            }
         }

         int var9 = var1.getModifiers();
         if (Modifier.isFinal(var9) || Modifier.isStatic(var9)) {
            var4.add(new ComplianceException(EJBComplianceTextFormatter.getInstance().TIMEOUT_METHOD_CANNOT_BE_FINAL_OR_STATIC(var2)));
         }

         validateTimeoutMethodIsejbTimeout(var3, var1);
         if (var0.isEJB30()) {
            Class[] var6 = var1.getParameterTypes();
            if (var6.length != 1 || !Timer.class.equals(var6[0]) || !var1.getReturnType().isAssignableFrom(Void.TYPE)) {
               var4.add(new ComplianceException(EJBComplianceTextFormatter.getInstance().TIMEOUT_METHOD_WITH_INVALID_SIGNATURE(var2)));
            }

            Class[] var7 = var1.getExceptionTypes();

            for(int var8 = 0; var8 < var7.length; ++var8) {
               if (ComplianceUtils.isApplicationException(var0, var1, var7[var8])) {
                  var4.add(new ComplianceException(EJBComplianceTextFormatter.getInstance().TIMEOUT_METHOD_CANNOT_THROW_APPLICATION_EXCEPTION(var2)));
               }
            }
         }

         if (!var4.isEmpty()) {
            throw var4;
         }
      }
   }
}
