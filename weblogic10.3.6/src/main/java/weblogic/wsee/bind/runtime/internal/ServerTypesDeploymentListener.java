package weblogic.wsee.bind.runtime.internal;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import javax.jws.WebMethod;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.HandlerInfo;
import weblogic.jws.Types;
import weblogic.wsee.handler.HandlerException;
import weblogic.wsee.handler.HandlerList;
import weblogic.wsee.ws.WsEndpoint;
import weblogic.wsee.ws.WsPort;
import weblogic.wsee.ws.WsService;
import weblogic.wsee.ws.init.WsDeploymentContext;
import weblogic.wsee.ws.init.WsDeploymentException;
import weblogic.wsee.ws.init.WsDeploymentListener;

public class ServerTypesDeploymentListener implements WsDeploymentListener {
   public void process(WsDeploymentContext var1) throws WsDeploymentException {
      WsService var2 = var1.getWsService();
      Iterator var3 = var2.getPorts();

      while(var3.hasNext()) {
         WsPort var4 = (WsPort)var3.next();

         try {
            if (this.hasTypesAnnotation(var2)) {
               HandlerList var5 = var4.getInternalHandlerList();
               this.insertHandler(var5);
            }
         } catch (HandlerException var6) {
            throw new WsDeploymentException("Failed to register SERVER_TYPES_HANDLER", var6);
         }
      }

   }

   private boolean hasTypesAnnotation(WsService var1) {
      Iterator var2 = var1.getEndpoints();

      while(var2.hasNext()) {
         WsEndpoint var3 = (WsEndpoint)var2.next();
         Class var4 = var3.getJwsClass();
         Method[] var5 = var4.getMethods();

         for(int var6 = 0; var6 < var5.length; ++var6) {
            Method var7 = var5[var6];
            if (var7.isAnnotationPresent(WebMethod.class)) {
               if (var7.isAnnotationPresent(Types.class)) {
                  return true;
               }

               Annotation[][] var8 = var7.getParameterAnnotations();

               for(int var9 = 0; var9 < var8.length; ++var9) {
                  Annotation[] var10 = var8[var9];

                  for(int var11 = 0; var11 < var10.length; ++var11) {
                     Annotation var12 = var10[var11];
                     if (var12.annotationType().equals(Types.class)) {
                        return true;
                     }
                  }
               }
            }
         }
      }

      return false;
   }

   private void insertHandler(HandlerList var1) throws HandlerException {
      HandlerInfo var2 = this.getHandlerInfo();
      ArrayList var3 = this.getPrecedingHandlers();
      ArrayList var4 = this.getFollowingHandlers();
      var1.lenientInsert("SERVER_TYPES_HANDLER", var2, var3, var4);
   }

   protected ArrayList getPrecedingHandlers() {
      ArrayList var1 = new ArrayList();
      var1.add("CODEC_HANDLER");
      return var1;
   }

   protected ArrayList getFollowingHandlers() {
      ArrayList var1 = new ArrayList();
      var1.add("COMPONENT_HANDLER");
      return var1;
   }

   protected HandlerInfo getHandlerInfo() {
      HashMap var1 = new HashMap();
      return new HandlerInfo(ServerTypesHandler.class, var1, (QName[])null);
   }
}
