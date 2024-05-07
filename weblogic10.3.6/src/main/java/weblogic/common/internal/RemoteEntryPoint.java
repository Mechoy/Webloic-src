package weblogic.common.internal;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import weblogic.common.ParamSet;
import weblogic.common.T3Client;
import weblogic.common.T3Exception;
import weblogic.common.WLObjectInput;
import weblogic.common.WLObjectOutput;

public class RemoteEntryPoint implements Externalizable {
   private static final long serialVersionUID = -6860805964340299268L;
   private transient T3Client t3;
   protected transient Object theObject;
   protected String className;
   protected ParamSet params;

   public String className() {
      return this.className;
   }

   public ParamSet params() {
      return this.params;
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      WLObjectInput var2 = (WLObjectInput)var1;
      this.className = var2.readAbbrevString();
      this.params = (ParamSet)var2.readObjectWL();
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      WLObjectOutput var2 = (WLObjectOutput)var1;
      var2.writeAbbrevString(this.className);
      var2.writeObjectWL(this.params);
   }

   public void initialize() {
   }

   public void destroy() {
      this.t3 = null;
      this.theObject = null;
      this.className = null;
      this.params = null;
   }

   public RemoteEntryPoint() {
      this.initialize();
   }

   public RemoteEntryPoint(Object var1, ParamSet var2) {
      this.theObject = var1;
      this.className = var1.getClass().getName();
      this.params = var2;
      this.t3 = null;
   }

   public RemoteEntryPoint(Object var1) {
      this((Object)var1, (ParamSet)null);
   }

   public RemoteEntryPoint(String var1, ParamSet var2) {
      this((T3Client)null, var1, var2);
   }

   public RemoteEntryPoint(String var1) {
      this((T3Client)null, var1, (ParamSet)null);
   }

   public RemoteEntryPoint(T3Client var1, String var2, ParamSet var3) {
      this.t3 = var1;
      this.className = var2;
      this.params = var3;
   }

   public T3Client getT3() {
      return this.t3;
   }

   public ParamSet getParamSet() {
      if (this.params == null) {
         this.params = new ParamSet(0);
      }

      return this.params;
   }

   public String getName() {
      return this.className;
   }

   void advertise() {
   }

   void retract() {
   }

   public Object newInstance() throws T3Exception {
      if (this.theObject == null) {
         try {
            this.theObject = Class.forName(this.getName(), true, Thread.currentThread().getContextClassLoader()).newInstance();
         } catch (NoSuchMethodError var2) {
            throw new T3Exception("Class " + this.getName() + " must implement a default constructor.", var2);
         } catch (ClassNotFoundException var3) {
            throw new T3Exception("class: " + this.getName(), var3);
         } catch (InstantiationException var4) {
            throw new T3Exception("class: " + this.getName(), var4);
         } catch (IllegalAccessException var5) {
            throw new T3Exception("class: " + this.getName(), var5);
         }
      }

      return this.theObject();
   }

   public Object theObject() {
      return this.theObject;
   }

   public boolean equals(Object var1) {
      return var1 instanceof RemoteEntryPoint && this.getName().equals(((RemoteEntryPoint)var1).getName());
   }

   public int hashCode() {
      return this.getName().hashCode();
   }
}
