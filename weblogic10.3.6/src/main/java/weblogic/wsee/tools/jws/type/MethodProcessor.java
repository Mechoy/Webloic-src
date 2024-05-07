package weblogic.wsee.tools.jws.type;

import com.bea.util.jam.JClass;
import com.bea.util.jam.JMethod;
import javax.xml.namespace.QName;
import weblogic.wsee.bind.buildtime.J2SBindingsBuilder;
import weblogic.wsee.bind.buildtime.internal.WLW81TylarJ2SBindingsBuilderImpl;
import weblogic.wsee.tools.WsBuildException;
import weblogic.wsee.tools.jws.JwsLogEvent;
import weblogic.wsee.tools.jws.TypesUtil;
import weblogic.wsee.tools.jws.decl.WebMethodDecl;
import weblogic.wsee.tools.jws.jaxrpc.JAXRPCWebServiceInfo;
import weblogic.wsee.util.ExceptionUtil;
import weblogic.wsee.util.Verbose;

abstract class MethodProcessor implements SOAPBindingProcessor {
   private static final boolean verbose = Verbose.isVerbose(Java2SchemaProcessor.class);
   JAXRPCWebServiceInfo webServiceInfo;

   public MethodProcessor(JAXRPCWebServiceInfo var1) {
      this.webServiceInfo = var1;
   }

   void registerReturnTypes(JClass var1, WebMethodDecl var2, int var3) throws WsBuildException {
      final J2SBindingsBuilder var4 = this.webServiceInfo.getBindingsBuilder();
      TypesUtil.ProcessStrategy var5 = new TypesUtil.ProcessStrategy() {
         public void process(JClass var1, JClass var2, int var3) {
            var4.javaTypeToSchemaType(var1, var2, var3);
         }
      };
      TypesUtil.ReportStrategy var6 = new TypesUtil.ReportStrategy() {
         public void report(JwsLogEvent var1) throws WsBuildException {
            if (!MethodProcessor.this.webServiceInfo.getWebService().isWlw81UpgradedService()) {
               throw new WsBuildException(var1.toString());
            }
         }
      };
      TypesUtil.processReturnTypes(var1, var2, var3, var5, var6);
   }

   void registerParamTypes(JClass var1, WebMethodDecl var2, int var3) throws WsBuildException {
      final J2SBindingsBuilder var4 = this.webServiceInfo.getBindingsBuilder();
      TypesUtil.ProcessStrategy var5 = new TypesUtil.ProcessStrategy() {
         public void process(JClass var1, JClass var2, int var3) {
            var4.javaTypeToSchemaType(var1, var2, var3);
         }
      };
      TypesUtil.ReportStrategy var6 = new TypesUtil.ReportStrategy() {
         public void report(JwsLogEvent var1) throws WsBuildException {
            if (!MethodProcessor.this.webServiceInfo.getWebService().isWlw81UpgradedService()) {
               throw new WsBuildException(var1.toString());
            }
         }
      };
      TypesUtil.processParamTypes(var1, var2, var3, var5, var6);
   }

   void processExceptionAsType(WebMethodDecl var1, int var2) {
      this.processException(var1, 5, true);
   }

   void processExceptionAsElement(WebMethodDecl var1, int var2) {
      this.processException(var1, var2, true);
   }

   void processException(WebMethodDecl var1, int var2, boolean var3) {
      J2SBindingsBuilder var4 = this.webServiceInfo.getBindingsBuilder();
      JClass[] var5 = var1.getJMethod().getExceptionTypes();
      JMethod var6 = var1.getJMethod();
      JClass var7 = (JClass)var6.getParent();
      String var8 = null;
      if (var4 instanceof WLW81TylarJ2SBindingsBuilderImpl) {
         var8 = ((WLW81TylarJ2SBindingsBuilderImpl)var4).getNamespaceFromServiceClass(var7);
      }

      JClass[] var9 = var5;
      int var10 = var5.length;

      for(int var11 = 0; var11 < var10; ++var11) {
         JClass var12 = var9[var11];
         if (verbose) {
            Verbose.log((Object)("++++  processException " + var12));
         }

         if (ExceptionUtil.isUserException(var12)) {
            if (var3) {
               QName var13 = ExceptionUtil.getDefaultExceptionElement(var12, var8);
               if (verbose) {
                  Verbose.log((Object)("++++  setting exception element name to " + var13));
               }

               var4.javaTypeToSchemaElement(var7, var12, var13, var2);
               if (ExceptionUtil.isMarshalPropertyException(var12)) {
                  if (verbose) {
                     Verbose.log((Object)("++++ " + var12 + " is a MarshalPropertyException  "));
                  }

                  QName var14 = ExceptionUtil.exceptionMarshalPropertyElementName(var7, var12);
                  JClass var15 = ExceptionUtil.getMarshalPropertyJClass(var12);
                  if (verbose) {
                     Verbose.log((Object)("++++  setting singleBuiltin element name to " + var14 + ", for marshalPropertyJClass '" + var15 + "'"));
                  }

                  var4.javaTypeToSchemaElement(var7, var15, var14, var2);
               } else if (verbose) {
                  Verbose.log((Object)("++++ exception '" + var12 + "'  is NOT a marshalPropertyException."));
               }
            }
         } else if (verbose) {
            Verbose.log((Object)("++++ exception '" + var12 + "'  is NOT a ServiceSpecificException."));
         }
      }

   }
}
