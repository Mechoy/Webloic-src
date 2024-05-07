package weblogic.wsee.bind.runtime.internal;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.Stack;
import java.util.TreeSet;
import java.util.Vector;
import javax.xml.namespace.QName;
import javax.xml.rpc.JAXRPCException;
import javax.xml.rpc.handler.GenericHandler;
import javax.xml.rpc.handler.MessageContext;
import weblogic.jws.Types;
import weblogic.wsee.handler.WLHandler;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.ws.WsEndpoint;
import weblogic.wsee.ws.WsMethod;
import weblogic.wsee.ws.WsMethodImpl;
import weblogic.wsee.ws.WsParameterType;
import weblogic.wsee.ws.dispatch.Dispatcher;

public class ServerTypesHandler extends GenericHandler implements WLHandler {
   private static List collectionTypes = new ArrayList();

   public QName[] getHeaders() {
      return new QName[0];
   }

   public boolean handleRequest(MessageContext var1) {
      Dispatcher var2 = this.getDispatcher(var1);
      Map var3 = var2.getInParams();
      WsMethodImpl var4 = (WsMethodImpl)var2.getWsMethod();
      Object[] var5 = var4.getMethodArgs(var3);
      List var6 = this.getParameterClasses(var4);
      Method var7 = this.getMethod(var2, var4, var6);
      Annotation[][] var8 = this.getParameterAnnotations(var7);

      for(int var9 = 0; var9 < var5.length; ++var9) {
         Object var10 = var5[var9];
         if (var10 != null) {
            Class var11 = (Class)var6.get(var9);
            if (!this.isDeclaredClass(var10, var11)) {
               Annotation[] var12 = var8[var9];
               this.checkAnnotatedClass(var10, var12);
            }
         }
      }

      return true;
   }

   private Annotation[][] getParameterAnnotations(Method var1) {
      Annotation[][] var2 = var1.getParameterAnnotations();
      return var2;
   }

   private Method getMethod(Dispatcher var1, WsMethod var2, List var3) {
      WsEndpoint var4 = var1.getWsPort().getEndpoint();
      Class var5 = var4.getJwsClass();
      Method var6 = null;

      try {
         var6 = var5.getMethod(var2.getOperationName().getLocalPart(), (Class[])((Class[])var3.toArray(new Class[var3.size()])));
         return var6;
      } catch (NoSuchMethodException var8) {
         throw new JAXRPCException("Could not get Method from operation name " + var2.getOperationName().getLocalPart());
      }
   }

   private List getParameterClasses(WsMethod var1) {
      ArrayList var2 = new ArrayList();
      Iterator var3 = var1.getParameters();

      while(var3.hasNext()) {
         WsParameterType var4 = (WsParameterType)var3.next();
         Class var5 = var4.getJavaType();
         var2.add(var5);
      }

      return var2;
   }

   private Class getReturnClass(WsMethod var1) {
      return var1.getReturnType().getJavaType();
   }

   private void checkAnnotatedClass(Object var1, Annotation[] var2) {
      for(int var3 = 0; var3 < var2.length; ++var3) {
         Annotation var4 = var2[var3];
         if (var4.annotationType().equals(Types.class)) {
            Types var5 = (Types)var4;
            String[] var6 = var5.value();
            if (!this.isCollection(var1.getClass())) {
               if (!this.isType(var1, var6)) {
                  throw new JAXRPCException("Found parameter object of invalid type: " + var1.getClass().getName());
               }
            } else {
               Collection var7 = (Collection)var1;
               Iterator var8 = var7.iterator();

               while(var8.hasNext()) {
                  Object var9 = var8.next();
                  if (!this.isType(var9, var6)) {
                     throw new JAXRPCException("Found object in collection parameter of invalid type: " + var9.getClass().getName());
                  }
               }
            }
         }
      }

   }

   private boolean isDeclaredClass(Object var1, Class var2) {
      return !this.isCollection(var1.getClass()) && (var1.getClass().equals(var2) || this.isInterface(var1, var2));
   }

   private boolean isInterface(Object var1, Class var2) {
      Class[] var3 = var1.getClass().getInterfaces();

      for(int var4 = 0; var4 < var3.length; ++var4) {
         Class var5 = var3[var4];
         if (var5.equals(var2)) {
            return true;
         }
      }

      return false;
   }

   private boolean isCollection(Class var1) {
      return collectionTypes.contains(var1.getName());
   }

   private boolean isType(Object var1, String[] var2) {
      for(int var3 = 0; var3 < var2.length; ++var3) {
         String var4 = var2[var3];
         if (var4.equals(var1.getClass().getName())) {
            return true;
         }
      }

      return false;
   }

   private Dispatcher getDispatcher(MessageContext var1) {
      WlMessageContext var2 = (WlMessageContext)var1;
      Dispatcher var3 = var2.getDispatcher();
      return var3;
   }

   public boolean handleResponse(MessageContext var1) {
      Dispatcher var2 = this.getDispatcher(var1);
      WsMethod var3 = var2.getWsMethod();
      Map var4 = var2.getOutParams();
      if (var4.size() <= 0) {
         return true;
      } else {
         Object var5 = var4.values().iterator().next();
         Class var6 = this.getReturnClass(var3);
         List var7 = this.getParameterClasses(var3);
         Method var8 = this.getMethod(var2, var3, var7);
         Annotation[] var9 = var8.getAnnotations();
         if (this.isDeclaredClass(var5, var6)) {
            return true;
         } else {
            this.checkAnnotatedClass(var5, var9);
            return true;
         }
      }
   }

   public boolean handleFault(MessageContext var1) {
      return true;
   }

   public boolean handleClosure(MessageContext var1) {
      return true;
   }

   static {
      collectionTypes.add(Collection.class.getName());
      collectionTypes.add(List.class.getName());
      collectionTypes.add(ArrayList.class.getName());
      collectionTypes.add(LinkedList.class.getName());
      collectionTypes.add(Vector.class.getName());
      collectionTypes.add(Stack.class.getName());
      collectionTypes.add(Set.class.getName());
      collectionTypes.add(TreeSet.class.getName());
      collectionTypes.add(SortedSet.class.getName());
      collectionTypes.add(HashSet.class.getName());
   }
}
