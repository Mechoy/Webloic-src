package weblogic.wsee.jws;

import javax.xml.namespace.QName;

public abstract class VisitableJWSBuilder {
   public static VisitableJWSBuilder jaxws() {
      return new weblogic.wsee.jaxws.VisitableJWSBuilder();
   }

   public abstract VisitableJWSBuilder sei(Class var1);

   public abstract VisitableJWSBuilder impl(Class var1);

   public abstract VisitableJWSBuilder portName(QName var1);

   public abstract VisitableJWSBuilder ServiceName(QName var1);

   public abstract VisitableJWS build();
}
