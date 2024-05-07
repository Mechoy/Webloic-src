package weblogic.servlet.internal;

import java.util.List;
import java.util.Set;

public interface WebAppHelper {
   Set getTagListeners(boolean var1);

   Set getTagHandlers(boolean var1);

   Set getManagedBeanClasses();

   Set getManagedBeanClasses(Set<String> var1);

   List getAnnotatedClasses(WebAnnotationProcessor var1);
}
