package weblogic.wsee.jws;

import com.bea.util.jam.JClass;
import weblogic.wsee.WebServiceType;

public class HandlerChainDeclFactory {
   private HandlerChainDeclFactory() {
   }

   public static HandlerChainDecl newInstance(JClass var0, JClass var1, WebServiceType var2) {
      return (HandlerChainDecl)(var2 == WebServiceType.JAXRPC ? new weblogic.wsee.jws.jaxrpc.HandlerChainDecl(var0, var1) : new weblogic.wsee.jws.jaxws.HandlerChainDecl(var0, var1));
   }

   public static HandlerChainDecl newInstance(JClass var0, JClass var1, WebServiceType var2, ClassLoader var3) {
      return (HandlerChainDecl)(var2 == WebServiceType.JAXRPC ? new weblogic.wsee.jws.jaxrpc.HandlerChainDecl(var0, var1) : new weblogic.wsee.jws.jaxws.HandlerChainDecl(var0, var1, var3));
   }
}
