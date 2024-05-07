package weblogic.wsee.bind.buildtime.internal;

import com.bea.util.jam.JClass;
import javax.xml.namespace.QName;

public class Java2SchemaTypeWrapperElement {
   private JClass mServiceClass;
   private QName mWrapperName;
   private JClass mElementType;

   public Java2SchemaTypeWrapperElement(JClass var1, QName var2, JClass var3) {
      if (var1 == null) {
         throw new IllegalArgumentException("Java2SchemaTypeWrapperElement:  null serviceClass");
      } else if (var2 == null) {
         throw new IllegalArgumentException("Java2SchemaTypeWrapperElement:  null wrapperElementName");
      } else if (var3 == null) {
         throw new IllegalArgumentException("Java2SchemaTypeWrapperElement:  null elementJavaType");
      } else if (!var3.isVoidType() && !var3.isEnumType()) {
         this.mServiceClass = var1;
         this.mWrapperName = var2;
         this.mElementType = var3;
      } else {
         throw new IllegalArgumentException("invalid element java type: '" + var3.getQualifiedName() + "'");
      }
   }

   public JClass getServiceClass() {
      return this.mServiceClass;
   }

   public QName getWrapperName() {
      return this.mWrapperName;
   }

   public JClass getElementJavaType() {
      return this.mElementType;
   }
}
