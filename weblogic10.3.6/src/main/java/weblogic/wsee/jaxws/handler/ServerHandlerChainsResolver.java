package weblogic.wsee.jaxws.handler;

import com.sun.xml.ws.api.WSBinding;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import javax.xml.namespace.QName;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.soap.SOAPBinding;
import weblogic.j2ee.descriptor.HandlerChainBean;
import weblogic.j2ee.descriptor.HandlerChainsBean;
import weblogic.j2ee.descriptor.PortComponentHandlerBean;
import weblogic.wsee.jaxws.injection.ObjectFactory;

public class ServerHandlerChainsResolver {
   private final QName serviceName;
   private final QName portName;
   private final String protocolBinding;
   private final List<PortComponentHandlerBean> matchingHandlers;

   public ServerHandlerChainsResolver(QName var1, QName var2, String var3, HandlerChainsBean var4) {
      this.serviceName = var1;
      this.portName = var2;
      this.protocolBinding = var3;
      this.matchingHandlers = this.resolveHandlers(var4);
   }

   private List<PortComponentHandlerBean> resolveHandlers(HandlerChainsBean var1) {
      ArrayList var2 = new ArrayList();
      if (var1 != null) {
         HandlerChainBean[] var3 = var1.getHandlerChains();
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            HandlerChainBean var6 = var3[var5];
            HandlerChain var7 = new HandlerChain(var6.getServiceNamePattern(), var6.getPortNamePattern(), var6.getProtocolBindings());
            if (var7.isMatch(this.serviceName, this.portName, this.protocolBinding)) {
               PortComponentHandlerBean[] var8 = var6.getHandlers();
               int var9 = var8.length;

               for(int var10 = 0; var10 < var9; ++var10) {
                  PortComponentHandlerBean var11 = var8[var10];
                  var2.add(var11);
               }
            }
         }
      }

      return var2;
   }

   public List<PortComponentHandlerBean> getMatchingHandlers() {
      return this.matchingHandlers;
   }

   public void configureHandlers(WSBinding var1, ObjectFactory var2) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
      ArrayList var3 = new ArrayList();
      HashSet var4 = new HashSet();
      Iterator var5 = this.matchingHandlers.iterator();

      while(true) {
         String[] var8;
         do {
            if (!var5.hasNext()) {
               var1.setHandlerChain(var3);
               if (var1 instanceof SOAPBinding) {
                  ((SOAPBinding)var1).setRoles(var4);
               }

               return;
            }

            PortComponentHandlerBean var6 = (PortComponentHandlerBean)var5.next();
            Handler var7 = (Handler)var2.newInstance(var6.getHandlerClass());
            var3.add(var7);
            var8 = var6.getSoapRoles();
         } while(var8 == null);

         String[] var9 = var8;
         int var10 = var8.length;

         for(int var11 = 0; var11 < var10; ++var11) {
            String var12 = var9[var11];
            var4.add(var12);
         }
      }
   }
}
