package weblogic.jws.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.xml.ws.spi.WebServiceFeatureAnnotation;
import weblogic.jws.jaxws.WssConfigurationFeature;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@WebServiceFeatureAnnotation(
   id = "http://www.bea.com/wsee/wss-configuration",
   bean = WssConfigurationFeature.class
)
public @interface WssConfiguration {
   String value();

   boolean enabled() default true;
}
