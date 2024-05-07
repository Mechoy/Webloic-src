package weblogic.wsee.ws;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import javax.xml.namespace.QName;
import weblogic.wsee.util.ToStringWriter;
import weblogic.wsee.wsdl.WsdlMessage;

public class WsFault {
   private WsMethod method;
   private String name;
   private WsdlMessage faultMessage;
   private Class exceptionClass;
   private Class marshalPropertyClass;
   private Constructor marshalPropertyExceptionConstructor;
   private Method marshalPropertyGetterMethod;
   private QName marshalPropertyQName;
   private boolean isSimpleType = false;
   private boolean marshalProperty = false;

   WsFault(WsMethod var1, String var2, WsdlMessage var3) {
      this.method = var1;
      this.name = var2;
      this.faultMessage = var3;
   }

   public WsMethod getMethod() {
      return this.method;
   }

   public String getName() {
      return this.name;
   }

   public WsdlMessage getFaultMessage() {
      return this.faultMessage;
   }

   public void setExceptionClass(Class var1) {
      this.exceptionClass = var1;
   }

   public Class getExceptionClass() {
      return this.exceptionClass;
   }

   public Class getMarshalPropertyClass() {
      return this.marshalPropertyClass;
   }

   public void setMarshalPropertyClass(Class var1) {
      this.marshalPropertyClass = var1;
   }

   public Constructor getMarshalPropertyExceptionConstructor() {
      return this.marshalPropertyExceptionConstructor;
   }

   public void setMarshalPropertyExceptionConstructor(Constructor var1) {
      this.marshalPropertyExceptionConstructor = var1;
   }

   public Method getMarshalPropertyGetterMethod() {
      return this.marshalPropertyGetterMethod;
   }

   public void setMarshalPropertyGetterMethod(Method var1) {
      this.marshalPropertyGetterMethod = var1;
   }

   public void setMarshalPropertyQName(QName var1) {
      this.marshalPropertyQName = var1;
   }

   public QName getMarshalPropertyQName() {
      return this.marshalPropertyQName;
   }

   public void setIsSimpleType(boolean var1) {
      this.isSimpleType = var1;
   }

   public boolean getIsSimpleType() {
      return this.isSimpleType;
   }

   public void computeMarshalProperty() {
      if (this.marshalPropertyClass != null && this.marshalPropertyClass.isArray()) {
         this.marshalProperty = true;
      } else if (this.marshalPropertyClass != null && this.isSimpleType) {
         this.marshalProperty = true;
      } else {
         this.marshalProperty = false;
      }

   }

   public boolean marshalProperty() {
      return this.marshalProperty;
   }

   public String toString() {
      ToStringWriter var1 = new ToStringWriter();
      this.toString(var1);
      return var1.toString();
   }

   public void toString(ToStringWriter var1) {
      var1.start(this);
      var1.writeField("name", this.name);
      var1.writeField("message", this.faultMessage);
      var1.writeField("exception class", this.exceptionClass);
      var1.writeField("single scbema-builtin-type java class", this.marshalPropertyClass);
      var1.end();
   }
}
