package weblogic.ejb.container.persistence;

import java.util.Iterator;
import weblogic.ejb.container.dd.BaseDescriptor;
import weblogic.ejb.container.persistence.spi.EjbEntityRef;
import weblogic.utils.collections.Iterators;

public final class EjbEntityRefImpl extends BaseDescriptor implements EjbEntityRef {
   private static final boolean debug = System.getProperty("weblogic.ejb.container.persistence.debug") != null;
   private static final boolean verbose = System.getProperty("weblogic.ejb.container.persistence.verbose") != null;
   protected String description;
   protected String remoteEjbName;
   protected String ejbRefName;
   protected String home;
   protected String remote;
   protected String ejbLink;
   protected String jndiName;

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("[EjbEntityRef( remoteEjbName:" + this.remoteEjbName + " ejbRefName:" + this.ejbRefName + " home:" + this.home + " remote:" + this.remote + " ejbLink:" + this.ejbLink + " jndiName:" + this.jndiName + " description:" + this.description + " )]");
      return var1.toString();
   }

   public EjbEntityRefImpl() {
      super((String)null);
   }

   public void setDescription(String var1) {
      this.description = var1;
   }

   public String getDescription() {
      return this.description;
   }

   public void setRemoteEjbName(String var1) {
      this.remoteEjbName = var1;
   }

   public String getRemoteEjbName() {
      return this.remoteEjbName;
   }

   public void setEjbRefName(String var1) {
      this.ejbRefName = var1;
   }

   public String getEjbRefName() {
      return this.ejbRefName;
   }

   public void setHome(String var1) {
      this.home = var1;
   }

   public String getHome() {
      return this.home;
   }

   public void setRemote(String var1) {
      this.remote = var1;
   }

   public String getRemote() {
      return this.remote;
   }

   public void setEjbLink(String var1) {
      this.ejbLink = var1;
   }

   public String getEjbLink() {
      return this.ejbLink;
   }

   public String getJndiName() {
      return this.jndiName;
   }

   public void setJndiName(String var1) {
      this.jndiName = var1;
   }

   public void validateSelf() {
   }

   public Iterator getSubObjectsIterator() {
      return Iterators.EMPTY_ITERATOR;
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof EjbEntityRefImpl)) {
         return false;
      } else {
         EjbEntityRefImpl var2 = (EjbEntityRefImpl)var1;
         return this.remoteEjbName.equals(var2.getRemoteEjbName());
      }
   }

   public int hashCode() {
      return this.remoteEjbName.hashCode();
   }
}
