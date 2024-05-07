package weblogic.deployment.descriptors;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class EJBReference extends BaseDescriptor implements DDValidationErrorCodes {
   public static final Set VALID_EJB_REF_TYPES = new HashSet(Arrays.asList((Object[])(new String[]{"Session", "Entity"})));
   private String refDesc;
   private String refName;
   private String refType;
   private String refHome;
   private String refRemote;
   private String refEjbName;
   private String refJndiName;
   private String refMappedName;
   private boolean isLocalLink;

   public EJBReference(String var1, String var2, String var3, String var4, String var5, String var6) {
      super("weblogic.deployment.descriptors.DDValidationBundle");
      this.setDescription(var1);
      this.setName(var2);
      this.setRefType(var3);
      this.setHomeInterfaceName(var4);
      this.setRemoteInterfaceName(var5);
      this.setLinkedEjbName(var6);
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

   public void setRefType(String var1) {
      this.refType = var1;
   }

   public String getRefTypeString() {
      return this.refType;
   }

   public String getRefType() throws IllegalValueException {
      if (VALID_EJB_REF_TYPES.contains(this.refType)) {
         return this.refType;
      } else {
         throw new IllegalValueException("INVALID_EJB_REF_TYPE");
      }
   }

   public void validateType() {
      if (this.refType != null && this.refType.length() != 0) {
         try {
            this.getRefType();
         } catch (IllegalValueException var2) {
            this.addError("INVALID_EJB_REF_TYPE");
         }
      } else {
         this.addError("NO_EJB_REF_TYPE_SET");
      }

   }

   public void setHomeInterfaceName(String var1) {
      this.refHome = var1;
   }

   public String getHomeInterfaceName() {
      return this.refHome;
   }

   public void validateHomeInterfaceName() {
      if (this.refHome == null || this.refHome.length() == 0) {
         this.addError("NO_EJB_REF_HOME_SET");
      }

   }

   public void setRemoteInterfaceName(String var1) {
      this.refRemote = var1;
   }

   public String getRemoteInterfaceName() {
      return this.refRemote;
   }

   public void validateRemoteInterfaceName() {
      if (this.refRemote == null || this.refRemote.length() == 0) {
         this.addError("NO_EJB_REF_REMOTE_SET");
      }

   }

   public void setLinkedEjbName(String var1) {
      this.refEjbName = var1;
   }

   public String getLinkedEjbName() {
      return this.refEjbName;
   }

   public void setJNDIName(String var1) {
      this.refJndiName = var1;
   }

   public String getJNDIName() {
      return this.refJndiName;
   }

   public void validateJNDIName() {
      if (this.refEjbName == null && (this.refJndiName == null || this.refJndiName.length() == 0)) {
         this.addError("NO_EJB_REF_JNDI_NAME_SET", this.refName);
      }

   }

   public void setMappedName(String var1) {
      this.refMappedName = var1;
   }

   public String getMappedName() {
      return this.refMappedName;
   }

   public void validateSelf() {
      this.validateName();
      this.validateType();
      this.validateHomeInterfaceName();
      this.validateRemoteInterfaceName();
      this.validateJNDIName();
   }

   public Iterator getSubObjectsIterator() {
      return Collections.EMPTY_SET.iterator();
   }

   public boolean isLocalLink() {
      return this.isLocalLink;
   }

   public void setLocalLink(boolean var1) {
      this.isLocalLink = var1;
   }
}
