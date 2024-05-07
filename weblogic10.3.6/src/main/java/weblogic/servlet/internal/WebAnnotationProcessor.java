package weblogic.servlet.internal;

import java.util.List;
import weblogic.j2ee.descriptor.WebAppBean;
import weblogic.utils.ErrorCollectionException;
import weblogic.utils.classloaders.ClassFinder;
import weblogic.utils.classloaders.GenericClassLoader;

public interface WebAnnotationProcessor {
   void processJ2eeAnnotations(ClassLoader var1, WebAppBean var2) throws ClassNotFoundException, ErrorCollectionException;

   WebAppBean processAnnotationsOnClone(GenericClassLoader var1, WebAppBean var2, WebAppHelper var3) throws ClassNotFoundException, ErrorCollectionException;

   void processAnnotations(GenericClassLoader var1, WebAppBean var2, WebAppHelper var3) throws ClassNotFoundException, ErrorCollectionException;

   List getAnnotatedClasses(ClassFinder var1);
}
