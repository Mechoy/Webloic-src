package weblogic.wsee.component.ejb;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import javax.xml.rpc.handler.MessageContext;
import weblogic.ejb.spi.MethodUtils;
import weblogic.ejb.spi.WSObjectFactory;
import weblogic.wsee.component.BaseComponent;
import weblogic.wsee.component.Component;
import weblogic.wsee.component.ComponentException;
import weblogic.wsee.server.WsSecurityContextHandler;
import weblogic.wsee.server.ejb.WsEjb;

public class EjbComponent extends BaseComponent implements Component {
   public static final String EJBTARGET = "weblogic.wsee.ejb.target";
   public static final String ALT_RUN_AS = "weblogic.wsee.ejb.altRunAs";
   private WSObjectFactory wsofact;
   private final Class sei;
   private final Map preOps = new HashMap();
   private final Map operations = new HashMap();

   public EjbComponent(WSObjectFactory var1, Class var2) {
      this.wsofact = var1;
      this.sei = var2;
   }

   public void preinvoke(String var1, MessageContext var2) throws ComponentException {
      Method var3 = (Method)this.preOps.get(var1);

      try {
         WsEjb var4 = new WsEjb(this.wsofact);
         var4.preInvoke(var3, var2.getProperty("weblogic.wsee.ejb.altRunAs"), new WsSecurityContextHandler(var2));
         var2.setProperty("weblogic.wsee.ejb.target", var4);
      } catch (Exception var5) {
         throw new ComponentException("Failed to do preinvoke for operation '" + var3.getName() + "'", var5);
      }
   }

   public Object invoke(String var1, Object[] var2, MessageContext var3) throws ComponentException {
      WsEjb var4 = (WsEjb)var3.getProperty("weblogic.wsee.ejb.target");
      Method var5 = (Method)this.operations.get(var1);

      try {
         return var4.invoke(var5, var2);
      } catch (Throwable var7) {
         throw new ComponentException("Failed to do invoke for operation  " + var5.getName() + var7, var7);
      }
   }

   public void postinvoke(String var1, MessageContext var2) throws ComponentException {
      WsEjb var3 = (WsEjb)var2.getProperty("weblogic.wsee.ejb.target");
      var3.postInvoke();
   }

   public void registerOperation(String var1, String var2, Class[] var3) throws ComponentException {
      assert var1 != null;

      assert var2 != null;

      Method var4 = this.findMethod(this.sei, var2, var3);
      if (var4 == null) {
         throw new ComponentException("Unable to find method '" + var2 + "' " + "operation " + var1 + "' in service endpoint interface :" + " " + this.sei);
      } else {
         Class var5 = this.wsofact.create().getClass();
         String var6 = MethodUtils.getWSOPreInvokeMethodName(var4);
         Method var7 = this.findMethod(var5, var6, WsEjb.getPreinvokeParams(var3));
         if (var7 == null) {
            throw new AssertionError("preinvoke method is not found for method " + var4);
         } else {
            String var8 = MethodUtils.getWSOBusinessMethodName(var4);
            Method var9 = this.findMethod(var5, var8, var3);
            if (var7 == null) {
               throw new AssertionError("business method is not found for method " + var4);
            } else {
               this.preOps.put(var1, var7);
               this.operations.put(var1, var9);
            }
         }
      }
   }

   public String toString() {
      return this.sei.getName() + " (EJB)";
   }
}
