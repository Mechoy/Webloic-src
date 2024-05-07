package weblogic.ejb.spi;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Collection;
import weblogic.cluster.GroupMessage;
import weblogic.ejb.container.deployer.EJBModule;
import weblogic.rmi.spi.HostID;

public class InvalidationMessage implements GroupMessage {
   private String applicationName;
   private String componentName;
   private String ejbName;
   private Object pk;
   private Collection pks;

   public String toString() {
      return "[InvalidationMessage]: applicationName: " + this.applicationName + " componentName: " + this.componentName + " ejbName: " + this.ejbName + " pk: " + this.pk + " pks: " + this.pks;
   }

   public InvalidationMessage() {
   }

   public InvalidationMessage(String var1, String var2, String var3) {
      this.applicationName = var1;
      this.componentName = var2;
      this.ejbName = var3;
      this.pk = null;
      this.pks = null;
   }

   public InvalidationMessage(String var1, String var2, String var3, Object var4) {
      this.applicationName = var1;
      this.componentName = var2;
      this.ejbName = var3;
      this.pk = var4;
      this.pks = null;
   }

   public InvalidationMessage(String var1, String var2, String var3, Collection var4) {
      this.applicationName = var1;
      this.componentName = var2;
      this.ejbName = var3;
      this.pk = null;
      this.pks = var4;
   }

   public String getApplicationName() {
      return this.applicationName;
   }

   public String getComponentName() {
      return this.componentName;
   }

   public String getEjbName() {
      return this.ejbName;
   }

   public Object getPrimaryKey() {
      return this.pk;
   }

   public Collection getPrimaryKeys() {
      return this.pks;
   }

   public void execute(HostID var1) {
      EJBModule var2 = EJBModule.findModule(this.applicationName, this.componentName);
      if (var2 != null) {
         var2.invalidate(this);
      }

   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeObject(this.applicationName);
      var1.writeObject(this.componentName);
      var1.writeObject(this.ejbName);
      var1.writeObject(this.pk);
      var1.writeObject(this.pks);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      this.applicationName = (String)var1.readObject();
      this.componentName = (String)var1.readObject();
      this.ejbName = (String)var1.readObject();
      this.pk = var1.readObject();
      this.pks = (Collection)var1.readObject();
   }
}
