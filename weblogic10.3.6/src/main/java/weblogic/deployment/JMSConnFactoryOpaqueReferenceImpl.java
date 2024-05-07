package weblogic.deployment;

import java.util.Map;
import javax.jms.JMSException;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NamingException;
import weblogic.deployment.jms.PooledConnectionFactory;
import weblogic.jndi.OpaqueReference;

public final class JMSConnFactoryOpaqueReferenceImpl implements OpaqueReference {
   private final String poolName;
   private final Map poolProps;
   private final int wrapStyle;
   private final boolean containerAuth;

   public JMSConnFactoryOpaqueReferenceImpl(String var1, int var2, boolean var3, Map var4) {
      this.poolName = var1;
      this.wrapStyle = var2;
      this.containerAuth = var3;
      this.poolProps = var4;
   }

   public Object getReferent(Name var1, Context var2) throws NamingException {
      try {
         return new PooledConnectionFactory(this.poolName, 1, this.containerAuth, this.poolProps);
      } catch (JMSException var5) {
         NamingException var4 = new NamingException(var5.toString());
         var4.setRootCause(var5);
         throw var4;
      }
   }
}
