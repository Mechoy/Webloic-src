package weblogic.deployment;

import java.lang.reflect.InvocationHandler;

public final class EntityManagerInvocationHandlerFactory {
   public static InvocationHandler createTransactionalEntityManagerInvocationHandler(String var0, String var1, String var2, PersistenceUnitRegistry var3) {
      return new TransactionalEntityManagerProxyImpl(var0, var1, var2, var3);
   }

   public static InvocationHandler createExtendedEntityManagerInvocationHandler(String var0, String var1, String var2, PersistenceUnitRegistry var3) {
      return new ExtendedEntityManagerProxyImpl(var0, var1, var2, var3);
   }
}
