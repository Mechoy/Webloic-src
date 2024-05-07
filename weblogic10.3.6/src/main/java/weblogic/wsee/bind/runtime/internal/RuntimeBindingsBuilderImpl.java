package weblogic.wsee.bind.runtime.internal;

import com.bea.staxb.buildtime.internal.tylar.Tylar;
import com.bea.xbean.xb.xsdschema.SchemaDocument;
import com.bea.xml.XmlException;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.xml.namespace.QName;
import weblogic.j2ee.descriptor.JavaWsdlMappingBean;
import weblogic.wsee.bind.runtime.RuntimeBindings;
import weblogic.wsee.bind.runtime.RuntimeBindingsBuilder;
import weblogic.wsee.wsdl.WsdlDefinitions;

public class RuntimeBindingsBuilderImpl implements RuntimeBindingsBuilder {
   private JavaWsdlMappingBean m109Mappings = null;
   private WsdlDefinitions mWsdl = null;
   private List<Tylar> tylars = new LinkedList();
   private List<SchemaDocument> schemas = new LinkedList();
   private List<TypeInfo> typesToBind = new LinkedList();
   private boolean treatEnumsAsSimpleTypes = false;

   public void set109Mappings(JavaWsdlMappingBean var1) {
      if (this.m109Mappings != null) {
         throw new IllegalStateException("109 mappings already set");
      } else {
         this.m109Mappings = var1;
      }
   }

   public void setWsdl(WsdlDefinitions var1) {
      this.mWsdl = var1;
   }

   public void addTylar(Tylar var1) {
      this.tylars.add(var1);
   }

   public void addGlobalTypeToBind(QName var1, Class var2) {
      this.typesToBind.add(new TypeInfo(var1, var2));
   }

   public void addGlobalElementToBind(QName var1, Class var2) {
      TypeInfo var3 = new TypeInfo(var1, var2);
      var3.setGlobalElement(true);
      this.typesToBind.add(var3);
   }

   public void addLocalElementToBind(QName var1, String var2, Class var3) {
      TypeInfo var4 = new TypeInfo(var1, var3);
      var4.setLocalElement(true);
      var4.setLocalElementName(var2);
      this.typesToBind.add(var4);
   }

   public void addSchema(SchemaDocument var1) {
      this.schemas.add(var1);
   }

   public void setTreatEnumsAsSimpleTypes(boolean var1) {
      this.treatEnumsAsSimpleTypes = var1;
   }

   public RuntimeBindings createRuntimeBindings() throws IOException, XmlException {
      SchemaDocument[] var1 = new SchemaDocument[this.schemas.size()];
      this.schemas.toArray(var1);
      Deploytime109MappingHelper var2 = new Deploytime109MappingHelper(this.m109Mappings, this.mWsdl, var1, this.treatEnumsAsSimpleTypes);
      Iterator var3 = this.typesToBind.iterator();

      while(var3.hasNext()) {
         TypeInfo var4 = (TypeInfo)var3.next();
         if (var4.isGlobalElement) {
            var2.registerElement(var4.getTypeName());
         } else if (!var4.isLocalElement()) {
            var2.registerType(var4.getTypeName());
         }
      }

      Tylar var5 = var2.buildTylar();
      this.tylars.add(var5);
      return new RuntimeBindingsImpl((Tylar[])((Tylar[])this.tylars.toArray(new Tylar[0])));
   }

   public RuntimeBindings createGenericRuntimeBindings() throws IOException, XmlException {
      return new GenericRuntimeBindingsImpl();
   }

   private static class TypeInfo {
      private boolean isLocalElement = false;
      private boolean isGlobalElement = false;
      private QName typeName;
      private String localElementName;
      private Class javaType;

      TypeInfo(QName var1, Class var2) {
         this.typeName = var1;
         this.javaType = var2;
      }

      public QName getTypeName() {
         return this.typeName;
      }

      public Class getJavaType() {
         return this.javaType;
      }

      public boolean isLocalElement() {
         return this.isLocalElement;
      }

      public void setLocalElement(boolean var1) {
         this.isLocalElement = var1;
      }

      public boolean isGlobalElement() {
         return this.isGlobalElement;
      }

      public void setGlobalElement(boolean var1) {
         this.isGlobalElement = var1;
      }

      public String getLocalElementName() {
         return this.localElementName;
      }

      public void setLocalElementName(String var1) {
         this.localElementName = var1;
      }
   }
}
