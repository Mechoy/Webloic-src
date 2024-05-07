package weblogic.wsee.jaxws;

import com.sun.xml.ws.server.EndpointFactory;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import javax.jws.Oneway;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.namespace.QName;
import javax.xml.ws.Provider;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceProvider;
import weblogic.wsee.jws.JWSVisitor;
import weblogic.wsee.util.ClassLoaderUtil;
import weblogic.wsee.util.StringUtil;

class VisitableJWS extends weblogic.wsee.jws.VisitableJWS {
   MyJWSClass jws;

   VisitableJWS(Class var1, Class var2, QName var3, QName var4) {
      this.jws = new MyJWSClass(var1, var2, var3, var4);
   }

   public void accept(JWSVisitor var1) {
      var1.visitClass(this.jws);
      if (!this.jws.isProviderBased()) {
         this.vistitSEIWebMethods(var1);
      }

   }

   private void vistitSEIWebMethods(JWSVisitor var1) {
      boolean var2 = this.jws.getServiceInterface() != null;
      Method[] var3 = var2 ? this.jws.getServiceInterface().getMethods() : this.jws.getServiceImpl().getMethods();

      for(int var4 = 0; var4 < var3.length; ++var4) {
         Method var5 = var3[var4];
         WebMethod var6 = (WebMethod)var5.getAnnotation(WebMethod.class);
         MyWsMethod var7 = null;
         if (var2) {
            try {
               Method var11 = this.jws.getServiceImpl().getMethod(var5.getName(), var5.getParameterTypes());
               var7 = new MyWsMethod(var5, var11);
            } catch (NoSuchMethodException var10) {
               throw new WebServiceException("the service impl class doesn't implement method: " + var5.getName(), var10);
            }
         } else if (!Modifier.isStatic(var5.getModifiers())) {
            boolean var8 = var6 != null && !var6.exclude();
            boolean var9 = var6 == null && var5.getDeclaringClass().getAnnotation(WebService.class) != null;
            if (var8 || var9) {
               var7 = new MyWsMethod(var5, var5);
            }
         }

         if (var7 != null) {
            var1.visitMethod(var7);
         }
      }

   }

   private static class MyWsMethod implements JWSVisitor.WsMethod {
      Method seiMethod;
      Method implMethod;

      private MyWsMethod(Method var1, Method var2) {
         this.seiMethod = var1;
         this.implMethod = var2;
      }

      public String getOperationName() {
         WebMethod var1 = (WebMethod)this.seiMethod.getAnnotation(WebMethod.class);
         boolean var2 = var1 != null && !StringUtil.isEmpty(var1.operationName());
         return var2 ? var1.operationName() : this.seiMethod.getName();
      }

      public Method getImplMethod() {
         return this.implMethod;
      }

      public Method getSeiMethod() {
         return this.seiMethod;
      }

      public boolean isOneway() {
         return this.seiMethod.isAnnotationPresent(Oneway.class);
      }

      // $FF: synthetic method
      MyWsMethod(Method var1, Method var2, Object var3) {
         this(var1, var2);
      }
   }

   private static class MyJWSClass implements JWSVisitor.JWSClass {
      Class sei;
      Class serivceImpl;
      private QName portName;
      private QName serviceName;

      private MyJWSClass(Class var1, Class var2, QName var3, QName var4) {
         this.sei = var1;
         this.serivceImpl = var2;
         this.portName = var3;
         this.serviceName = var4;
         if (var1 != null && var1.getAnnotation(WebService.class) == null) {
            throw new IllegalArgumentException("the service endpoint interface should be annotated with @WebService!");
         } else if (var2 == null) {
            throw new IllegalArgumentException("the service implementation class can't be null!");
         } else {
            WebService var5 = (WebService)var2.getAnnotation(WebService.class);
            if (var5 == null && var2.getAnnotation(WebServiceProvider.class) == null) {
               throw new IllegalArgumentException("the service implementation class should be annotated with @WebService or @WebServiceProvider!");
            } else {
               if (this.sei == null && var5 != null && !StringUtil.isEmpty(var5.endpointInterface())) {
                  try {
                     this.sei = (new ClassLoaderUtil.DelegatingLoader(var2.getClassLoader(), Thread.currentThread().getContextClassLoader())).loadClass(var5.endpointInterface());
                  } catch (ClassNotFoundException var7) {
                     throw new WebServiceException("can't load service endpoint interface:\t" + var5.endpointInterface(), var7);
                  }
               }

            }
         }
      }

      public Class getServiceImpl() {
         return this.serivceImpl;
      }

      public Class getServiceInterface() {
         return this.sei;
      }

      public QName getServiceName() {
         if (this.serviceName == null) {
            this.serviceName = EndpointFactory.getDefaultServiceName(this.serivceImpl);
         }

         return this.serviceName;
      }

      public QName getPortName() {
         if (this.portName == null) {
            this.portName = EndpointFactory.getDefaultPortName(this.getServiceName(), this.serivceImpl);
         }

         return this.portName;
      }

      public boolean isProviderBased() {
         return this.serivceImpl.getAnnotation(WebServiceProvider.class) != null;
      }

      public Method getInvokeMethod() {
         if (!this.isProviderBased()) {
            return null;
         } else {
            Type[] var1 = this.serivceImpl.getGenericInterfaces();
            Type[] var2 = null;
            if (var1 != null) {
               Type[] var3 = var1;
               int var4 = var1.length;

               for(int var5 = 0; var5 < var4; ++var5) {
                  Type var6 = var3[var5];
                  if (var6 instanceof ParameterizedType) {
                     ParameterizedType var7 = (ParameterizedType)var6;
                     if (Provider.class.equals(var7.getRawType())) {
                        var2 = var7.getActualTypeArguments();
                        break;
                     }
                  }
               }
            }

            if (var2 != null && var2.length == 1) {
               try {
                  return this.serivceImpl.getMethod("invoke", (Class)var2[0]);
               } catch (NoSuchMethodException var8) {
               }
            }

            return null;
         }
      }

      // $FF: synthetic method
      MyJWSClass(Class var1, Class var2, QName var3, QName var4, Object var5) {
         this(var1, var2, var3, var4);
      }
   }
}
