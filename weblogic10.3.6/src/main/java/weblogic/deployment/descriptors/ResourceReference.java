package weblogic.deployment.descriptors;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.naming.CompositeName;
import javax.naming.InvalidNameException;
import javax.naming.Name;

public class ResourceReference extends BaseDescriptor implements DDValidationErrorCodes {
   public static final Set VALID_RESOURCE_TYPES = new HashSet(Arrays.asList((Object[])(new String[]{"javax.sql.DataSource", "javax.jms.QueueConnectionFactory", "javax.jms.TopicConnectionFactory", "java.net.URL", "javax.mail.Session"})));
   public static final Set VALID_AUTH_MODES = new HashSet(Arrays.asList((Object[])(new String[]{"Application", "Container", "CONTAINER", "SERVLET"})));
   private String refDesc;
   private String refName;
   private String resType;
   private String resAuthMode;
   private String resSharingScope;
   private String jndiName;
   private String mappedName;

   public ResourceReference(String var1, String var2, String var3, String var4, String var5) {
      super("weblogic.deployment.descriptors.DDValidationBundle");
      this.refDesc = var1;
      this.refName = var2;
      this.setResourceType(var3);
      this.setResourceAuthMode(var4);
      this.setResourceSharingScope(var5);
   }

   public void setDescription(String var1) {
      this.refDesc = var1;
   }

   public String getDescription() {
      return this.refDesc;
   }

   public void setName(String var1) {
      this.refName = var1;
   }

   public String getName() {
      return this.refName;
   }

   public void validateName() {
      if (this.refName == null || this.refName.length() == 0) {
         this.addError("NO_RES_REF_NAME_SET");
      }

   }

   public void setResourceType(String var1) {
      this.resType = var1;
   }

   public String getResourceType() throws IllegalValueException {
      return this.resType;
   }

   public void validateType() {
      if (this.resType != null && this.resType.length() != 0) {
         try {
            this.getResourceType();
         } catch (IllegalValueException var2) {
            this.addError("INVALID_RES_TYPE");
         }
      } else {
         this.addError("NO_RES_REF_TYPE_SET");
      }

   }

   public String getResourceTypeString() {
      return this.resType;
   }

   public void setResourceAuthMode(String var1) {
      this.resAuthMode = var1;
   }

   public String getResourceAuthModeString() {
      return this.resAuthMode;
   }

   public void setResourceSharingScope(String var1) {
      this.resSharingScope = var1;
   }

   public String getResourceSharingScope() {
      return this.resSharingScope;
   }

   public void validateAuthMode() {
      if (this.resAuthMode != null && this.resAuthMode.length() != 0) {
         try {
            this.getResourceAuthMode();
         } catch (IllegalValueException var2) {
            this.addError("INVALID_RES_AUTH");
         }
      } else {
         this.addError("NO_RES_AUTH_SET");
      }

   }

   public String getResourceAuthMode() throws IllegalValueException {
      if (!VALID_AUTH_MODES.contains(this.resAuthMode)) {
         throw new IllegalValueException("Invalid resource authorization mode");
      } else {
         return this.resAuthMode;
      }
   }

   public void setJNDIName(String var1) {
      this.jndiName = var1;
   }

   public String getJNDINameString() {
      return this.jndiName != null ? this.jndiName : this.mappedName;
   }

   public Name getJNDIName() throws InvalidNameException {
      String var1 = this.jndiName != null ? this.jndiName : this.mappedName;
      return var1 == null ? null : new CompositeName(var1);
   }

   public void validateJNDIName() {
      if (this.getJNDINameString() != null && this.getJNDINameString().length() != 0) {
         try {
            this.getJNDIName();
         } catch (InvalidNameException var2) {
            this.addError("INVALID_RES_JNDI_NAME");
         }
      } else {
         this.addError("NO_RES_JNDI_NAME", this.refName, this.resType);
      }

   }

   public void setMappedName(String var1) {
      this.mappedName = var1;
   }

   public String getMappedName() {
      return this.mappedName;
   }

   public void validateSelf() {
      this.validateName();
      this.validateType();
      this.validateAuthMode();
      this.validateJNDIName();
   }

   public Iterator getSubObjectsIterator() {
      return Collections.EMPTY_SET.iterator();
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("refName [" + this.refName + "] ");
      var1.append("refDesc [" + this.refDesc + "] ");
      var1.append("resType [" + this.resType + "] ");
      var1.append("resAuthMode [" + this.resAuthMode + "] ");
      var1.append("jndiName [" + this.jndiName + "] ");
      return var1.toString();
   }
}
