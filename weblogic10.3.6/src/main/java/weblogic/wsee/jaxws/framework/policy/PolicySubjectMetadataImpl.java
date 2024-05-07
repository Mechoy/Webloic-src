package weblogic.wsee.jaxws.framework.policy;

import javax.xml.namespace.QName;

public class PolicySubjectMetadataImpl implements PolicySubjectMetadata {
   private String applicationName = null;
   private String moduleName = null;
   private PolicySubjectMetadata.ModuleType moduleType = null;
   private QName portName = null;
   private String subjectName = null;
   private PolicySubjectMetadata.Type type = null;
   private String resourcePattern = null;

   public PolicySubjectMetadataImpl(String var1, String var2, PolicySubjectMetadata.ModuleType var3, QName var4, String var5, PolicySubjectMetadata.Type var6, String var7) {
      this.applicationName = var1;
      this.moduleName = var2;
      this.moduleType = var3;
      this.portName = var4;
      this.subjectName = var5;
      this.type = var6;
      this.resourcePattern = var7;
   }

   public String getApplicationName() {
      return this.applicationName;
   }

   public String getModuleName() {
      return this.moduleName;
   }

   public PolicySubjectMetadata.ModuleType getModuleType() {
      return this.moduleType;
   }

   public QName getPortQName() {
      return this.portName;
   }

   public String getSubjectName() {
      return this.subjectName;
   }

   public PolicySubjectMetadata.Type getType() {
      return this.type;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append(this.getClass().getName());
      var1.append(": applicationName=").append(this.applicationName);
      var1.append(", moduleName=").append(this.moduleName);
      var1.append(", moduleType=").append(this.moduleType);
      var1.append(", portName=").append(this.portName);
      var1.append(", subjectName=").append(this.subjectName);
      var1.append(", type=").append(this.type);
      var1.append(", resourcePattern=").append(this.resourcePattern);
      return var1.toString();
   }

   public String getResourcePattern() {
      return this.resourcePattern;
   }
}
