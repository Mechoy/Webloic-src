package weblogic.deployment;

import java.lang.reflect.Method;
import java.net.URL;
import javax.persistence.Query;
import kodo.jdbc.conf.descriptor.PersistenceUnitConfigurationBean;
import weblogic.application.ApplicationContextInternal;
import weblogic.j2ee.descriptor.PersistenceUnitBean;
import weblogic.utils.classloaders.GenericClassLoader;

public class PersistenceUnitInfoImpl extends BasePersistenceUnitInfoImpl {
   PersistenceUnitInfoImpl(PersistenceUnitBean var1, PersistenceUnitConfigurationBean var2, GenericClassLoader var3, String var4, URL var5, URL var6, String var7) throws EnvironmentException {
      super(var1, var2, var3, var4, var5, var6, var7);
   }

   PersistenceUnitInfoImpl(PersistenceUnitBean var1, PersistenceUnitConfigurationBean var2, GenericClassLoader var3, String var4, URL var5, URL var6, String var7, ApplicationContextInternal var8) throws EnvironmentException {
      super(var1, var2, var3, var4, var5, var6, var7, var8);
   }

   Query createNonTransactionalQueryProxy(TransactionalEntityManagerProxyImpl var1, Method var2, Object[] var3) {
      return new QueryProxyImpl(var1, var2, var3);
   }
}
