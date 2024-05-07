package weblogic.wsee.component;

import javax.xml.rpc.handler.MessageContext;

public interface Component {
   void preinvoke(String var1, MessageContext var2) throws ComponentException;

   Object invoke(String var1, Object[] var2, MessageContext var3) throws ComponentException;

   void postinvoke(String var1, MessageContext var2) throws ComponentException;

   void registerOperation(String var1, String var2, Class[] var3) throws ComponentException;

   void destroy();
}
