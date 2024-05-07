package weblogic.wsee.jws;

import java.lang.reflect.Method;
import javax.xml.namespace.QName;

public interface JWSVisitor {
   void visitClass(JWSClass var1);

   void visitMethod(WsMethod var1);

   public interface WsMethod {
      String getOperationName();

      Method getImplMethod();

      Method getSeiMethod();

      boolean isOneway();
   }

   public interface JWSClass {
      Class getServiceImpl();

      Class getServiceInterface();

      QName getServiceName();

      QName getPortName();

      boolean isProviderBased();

      Method getInvokeMethod();
   }
}
