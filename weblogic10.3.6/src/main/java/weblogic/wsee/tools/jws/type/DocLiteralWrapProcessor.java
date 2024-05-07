package weblogic.wsee.tools.jws.type;

import com.bea.staxb.buildtime.WildcardUtil;
import com.bea.staxb.buildtime.internal.bts.XmlTypeName;
import com.bea.util.jam.JClass;
import java.util.ArrayList;
import java.util.Iterator;
import javax.xml.namespace.QName;
import weblogic.wsee.bind.buildtime.J2SBindingsBuilder;
import weblogic.wsee.tools.WsBuildException;
import weblogic.wsee.tools.jws.decl.WebMethodDecl;
import weblogic.wsee.tools.jws.decl.WebParamDecl;
import weblogic.wsee.tools.jws.jaxrpc.JAXRPCWebServiceInfo;
import weblogic.wsee.util.Verbose;

public class DocLiteralWrapProcessor extends MethodProcessor {
   private static final boolean IGNORE_WEBRESULT = Boolean.getBoolean("weblogic.wsee.jaxrpc.ignore.webresult");
   private static final boolean VERBOSE = Verbose.isVerbose(DocLiteralWrapProcessor.class);

   public DocLiteralWrapProcessor(JAXRPCWebServiceInfo var1) {
      super(var1);
   }

   public void processMethod(WebMethodDecl var1) throws WsBuildException {
      byte var2 = 73;
      J2SBindingsBuilder var3 = this.webServiceInfo.getBindingsBuilder();
      ArrayList var4 = new ArrayList();
      ArrayList var5 = new ArrayList();
      Iterator var6 = var1.getWebParams();
      JClass var7 = (JClass)var1.getJMethod().getParent();

      String var10;
      while(var6.hasNext()) {
         WebParamDecl var8 = (WebParamDecl)var6.next();
         if (var8.isHeader()) {
            var3.javaTypeToSchemaElement(var7, var8.getRealType(), var8.getElementQName(), 137);
         } else {
            JClass var9 = var8.getJParameter().getType();
            var4.add(var9);
            var10 = var9.getQualifiedName();
            if (!WildcardUtil.WILDCARD_CLASSNAMES.contains(var10)) {
               if (VERBOSE) {
                  Verbose.log((Object)(" add actual message name for non-SOAPElement/XmlObject ->  any = '" + var8.getName() + "'"));
               }

               var5.add(var8.getWebTypeDeclName());
            } else if (var8.isBoundToAnyType()) {
               if (VERBOSE) {
                  Verbose.log((Object)(" add actual message name for SOAPElement/XmlObject ->  anyType = '" + var8.getName() + "'"));
               }

               var5.add(var8.getWebTypeDeclName());
            } else if (var9.isArrayType()) {
               if (VERBOSE) {
                  Verbose.log((Object)(" add pseudo name for SOAPElement[]/XmlObject[] -> any ='" + XmlTypeName.forElementWildCardArrayElement().getQName().getLocalPart() + "'"));
               }

               var5.add(XmlTypeName.forElementWildCardArrayElement().getQName().getLocalPart());
            } else {
               if (VERBOSE) {
                  Verbose.log((Object)(" add pseudo name for SOAPElement/XmlObject -> any ='" + XmlTypeName.forElementWildCardElement().getQName().getLocalPart() + "'"));
               }

               var5.add(XmlTypeName.forElementWildCardElement().getQName().getLocalPart());
            }
         }
      }

      var3.wrapJavaTypeToSchemaElement(var7, (JClass[])var4.toArray(new JClass[0]), (String[])var5.toArray(new String[0]), new QName(var1.getWebService().getTargetNamespace(), var1.getName()));
      this.registerParamTypes(var7, var1, var2);
      if (!var1.isOneway()) {
         JClass var11 = var1.getJMethod().getReturnType();
         QName var12 = new QName(var1.getWebService().getTargetNamespace(), var1.getName() + "Response");
         if (var11 != null && !var11.isVoidType()) {
            var10 = var1.getWebResult().getName();
            if (!IGNORE_WEBRESULT && var1.getWebResult().isAnnotationSet()) {
               var10 = var1.getWebResult().getWebTypeDeclName();
            }

            var3.wrapJavaTypeToSchemaElement(var7, new JClass[]{var11}, new String[]{var10}, var12);
            this.registerReturnTypes(var7, var1, var2);
         } else {
            var3.wrapJavaTypeToSchemaElement(var7, new JClass[0], new String[0], var12);
         }
      }

      this.processExceptionAsElement(var1, var2);
   }
}
