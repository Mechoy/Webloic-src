package weblogic.wsee.tools.jws.validation.annotation;

import com.bea.util.jam.JAnnotation;

public interface MatchingRule {
   boolean isMatch(JAnnotation var1);
}
