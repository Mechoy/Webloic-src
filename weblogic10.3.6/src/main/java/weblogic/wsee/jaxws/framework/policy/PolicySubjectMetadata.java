package weblogic.wsee.jaxws.framework.policy;

import javax.xml.namespace.QName;

public interface PolicySubjectMetadata {
   Type getType();

   String getApplicationName();

   String getSubjectName();

   QName getPortQName();

   ModuleType getModuleType();

   String getModuleName();

   String getResourcePattern();

   public static enum ModuleType {
      EJB,
      WEB,
      JSE;
   }

   public static enum Type {
      SERVICE,
      REFERENCE,
      CALLBACK;
   }
}
