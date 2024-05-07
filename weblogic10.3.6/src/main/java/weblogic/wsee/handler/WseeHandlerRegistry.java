package weblogic.wsee.handler;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.rpc.JAXRPCException;
import javax.xml.rpc.handler.Handler;
import javax.xml.rpc.handler.HandlerInfo;
import javax.xml.rpc.handler.HandlerRegistry;
import weblogic.wsee.util.ToStringWriter;

public class WseeHandlerRegistry implements HandlerRegistry {
   private Map chainMap = Collections.synchronizedMap(new HashMap());

   public List getHandlerChain(QName var1) {
      Object var2 = (List)this.chainMap.get(var1.getLocalPart());
      if (var2 == null) {
         var2 = new ArrayList();
         this.setHandlerChain(var1, (List)var2);
      }

      return (List)var2;
   }

   public void setHandlerChain(QName var1, List var2) {
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         Object var4 = var3.next();
         if (!(var4 instanceof HandlerInfo)) {
            throw new JAXRPCException("The List argument to  setHandlerChain must contain instances of javax.xml.rpc.handler.HandlerInfo.  The list contained " + var4.getClass().getName() + " which is not an instanceof" + " HandlerInfo.");
         }

         this.validateHandlerInfo((HandlerInfo)var4, var1);
      }

      this.chainMap.put(var1.getLocalPart(), var2);
   }

   private void validateHandlerInfo(HandlerInfo var1, QName var2) {
      Class var3 = var1.getHandlerClass();
      if (var3 == null) {
         throw new JAXRPCException("The HandlerInfo for the name:" + var2 + " had a null HandlerClass.");
      } else if (!Handler.class.isAssignableFrom(var3)) {
         throw new JAXRPCException("The HandlerInfo for the name:" + var2 + " contains a handler class: " + var3.getName() + " which is not an instanceof javax.xml.rpc.handler.Handler");
      } else {
         try {
            Constructor var4 = var3.getConstructor();
            if (!Modifier.isPublic(var4.getModifiers())) {
               throw new JAXRPCException("The HandlerInfo for the name:" + var2 + " contains a handler class: " + var3.getName() + " which has a default, no argument constructor.  However," + "this constructor is not public.");
            }
         } catch (NoSuchMethodException var5) {
            throw new JAXRPCException("The HandlerInfo for the name:" + var2 + " contains a handler class: " + var3.getName() + " which does not have a public, no argument constructor.");
         }
      }
   }

   public String toString() {
      ToStringWriter var1 = new ToStringWriter();
      this.toString(var1);
      return var1.toString();
   }

   public void toString(ToStringWriter var1) {
      var1.start(this);
      var1.writeMap("chainMap", this.chainMap);
      var1.end();
   }
}
