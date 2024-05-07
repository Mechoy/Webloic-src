package weblogic.wsee.jaxws;

import javax.xml.namespace.QName;

public class VisitableJWSBuilder extends weblogic.wsee.jws.VisitableJWSBuilder {
   private Class sei;
   private Class impl;
   private QName portName;
   private QName serviceName;

   public weblogic.wsee.jws.VisitableJWSBuilder sei(Class var1) {
      this.sei = var1;
      return this;
   }

   public weblogic.wsee.jws.VisitableJWSBuilder impl(Class var1) {
      this.impl = var1;
      return this;
   }

   public weblogic.wsee.jws.VisitableJWSBuilder portName(QName var1) {
      this.portName = var1;
      return this;
   }

   public weblogic.wsee.jws.VisitableJWSBuilder ServiceName(QName var1) {
      this.serviceName = var1;
      return this;
   }

   public weblogic.wsee.jws.VisitableJWS build() {
      return new VisitableJWS(this.sei, this.impl, this.portName, this.serviceName);
   }
}
