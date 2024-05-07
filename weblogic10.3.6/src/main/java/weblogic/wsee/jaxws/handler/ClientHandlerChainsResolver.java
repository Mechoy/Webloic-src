package weblogic.wsee.jaxws.handler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.HandlerResolver;
import javax.xml.ws.handler.PortInfo;
import weblogic.j2ee.descriptor.ServiceRefHandlerBean;
import weblogic.j2ee.descriptor.ServiceRefHandlerChainBean;
import weblogic.j2ee.descriptor.ServiceRefHandlerChainsBean;
import weblogic.wsee.jaxws.PortInfoImpl;
import weblogic.wsee.jaxws.injection.ObjectFactory;

public class ClientHandlerChainsResolver implements HandlerResolver {
   private Map<PortInfo, List<Handler>> portHandlers = new HashMap();
   private ObjectFactory objectFactory;

   public ClientHandlerChainsResolver(QName var1, Iterator<QName> var2, ServiceRefHandlerChainsBean var3, ObjectFactory var4) throws Exception {
      this.objectFactory = var4;
      this.loadHandlers(var1, var2, var3);
   }

   private void loadHandlers(QName var1, Iterator<QName> var2, ServiceRefHandlerChainsBean var3) throws Exception {
      if (var3 != null) {
         while(var2.hasNext()) {
            QName var4 = (QName)var2.next();
            ServiceRefHandlerChainBean[] var5 = var3.getHandlerChains();
            int var6 = var5.length;

            for(int var7 = 0; var7 < var6; ++var7) {
               ServiceRefHandlerChainBean var8 = var5[var7];
               HandlerChain var9 = new HandlerChain(var8.getServiceNamePattern(), var8.getPortNamePattern(), var8.getProtocolBindings());
               if (var9.isMatch(var1, var4, (String)null)) {
                  Iterator var10 = var9.getApplicableProtocols().iterator();

                  while(var10.hasNext()) {
                     String var11 = (String)var10.next();
                     PortInfoImpl var12 = new PortInfoImpl(var11, var4, var1);
                     this.registerHandlers(var12, var8);
                  }
               }
            }
         }
      }

   }

   private void registerHandlers(PortInfo var1, ServiceRefHandlerChainBean var2) throws Exception {
      List var3 = this.initHandlerChain(var1);
      ServiceRefHandlerBean[] var4 = var2.getHandlers();
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         ServiceRefHandlerBean var7 = var4[var6];
         if (this.isMatchingPort(var7.getPortNames(), var1.getPortName().getLocalPart())) {
            Handler var8 = (Handler)this.objectFactory.newInstance(var7.getHandlerClass());
            var3.add(var8);
         }
      }

   }

   private boolean isMatchingPort(String[] var1, String var2) {
      return var1 != null && var1.length != 0 ? Arrays.asList(var1).contains(var2) : true;
   }

   private List<Handler> initHandlerChain(PortInfo var1) {
      Object var2 = (List)this.portHandlers.get(var1);
      if (var2 == null) {
         var2 = new ArrayList();
         this.portHandlers.put(var1, var2);
      }

      return (List)var2;
   }

   public List<Handler> getHandlerChain(PortInfo var1) {
      PortInfoImpl var2 = new PortInfoImpl(var1.getBindingID(), var1.getPortName(), var1.getServiceName());
      Object var3 = (List)this.portHandlers.get(var2);
      if (var3 == null) {
         var3 = new ArrayList();
      }

      return (List)var3;
   }
}
