package weblogic.wsee.server.ejb;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import weblogic.ejb.spi.BaseWSObjectIntf;
import weblogic.ejb.spi.MethodUtils;
import weblogic.ejb.spi.WSObjectFactory;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.ContextHandler;
import weblogic.wsee.server.WsSecurityContextHandler;
import weblogic.wsee.util.Verbose;

public class WsEjb {
   private static final boolean verbose = Verbose.isVerbose(WsEjb.class);
   private BaseWSObjectIntf target;
   private Method postInvokeMethod = null;
   private static final boolean dumpException = Boolean.getBoolean("weblogic.wsee.component.exception");
   private static final Map argValueMap = new HashMap();

   public WsEjb(WSObjectFactory var1) throws NoSuchMethodException {
      this.target = var1.create();
      String var2 = MethodUtils.getWSOPostInvokeMethodName();
      this.postInvokeMethod = this.target.getClass().getMethod(var2, (Class[])null);
   }

   public void preInvoke(Method var1, Object var2, WsSecurityContextHandler var3) throws InvocationTargetException, IllegalAccessException {
      Object[] var4 = this.getPreinvokeArgs(var1, var2, var3);
      var1.invoke(this.target, var4);
   }

   public Object invoke(Method var1, Object[] var2) throws Throwable {
      Object var3 = var1.invoke(this.target, var2);
      if (this.target.__WL_encounteredException()) {
         Throwable var4 = this.target.__WL_getException();
         throw var4;
      } else {
         return var3;
      }
   }

   public void postInvoke() {
      try {
         this.postInvokeMethod.invoke(this.target);
      } catch (IllegalAccessException var2) {
         assert false;
      } catch (InvocationTargetException var3) {
         if (verbose || dumpException) {
            var3.getTargetException().printStackTrace();
         }

         assert false;
      }

      this.target.__WL_methodComplete();
   }

   private Object[] getPreinvokeArgs(Method var1, Object var2, WsSecurityContextHandler var3) {
      Class[] var4 = var1.getParameterTypes();
      Object[] var5 = new Object[var4.length];

      assert var4.length >= 2;

      var5[0] = var2;
      var5[1] = var3;

      for(int var6 = 2; var6 < var4.length; ++var6) {
         var5[var6] = argValueMap.get(var4[var6]);
      }

      return var5;
   }

   public static Class[] getPreinvokeParams(Class[] var0) {
      int var1 = 2;
      if (var0 != null) {
         var1 = var0.length + 2;
      }

      Class[] var2 = new Class[var1];
      var2[0] = AuthenticatedSubject.class;
      var2[1] = ContextHandler.class;

      for(int var3 = 2; var3 < var1; ++var3) {
         var2[var3] = var0[var3 - 2];
      }

      return var2;
   }

   static {
      argValueMap.put(Integer.TYPE, new Integer(100));
      argValueMap.put(Float.TYPE, new Float(10.1F));
      argValueMap.put(Long.TYPE, new Long(1000L));
      argValueMap.put(Double.TYPE, new Double(20.2));
      argValueMap.put(Short.TYPE, new Short((short)10));
      argValueMap.put(Boolean.TYPE, new Boolean(true));
      argValueMap.put(Byte.TYPE, new Byte((byte)1));
      argValueMap.put(Character.TYPE, new Character('c'));
   }
}
