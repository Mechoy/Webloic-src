package weblogic.jws.soap;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface SOAPBinding {
   javax.jws.soap.SOAPBinding.Style style() default Style.DOCUMENT;

   javax.jws.soap.SOAPBinding.Use use() default Use.LITERAL;

   javax.jws.soap.SOAPBinding.ParameterStyle parameterStyle() default ParameterStyle.WRAPPED;
}
