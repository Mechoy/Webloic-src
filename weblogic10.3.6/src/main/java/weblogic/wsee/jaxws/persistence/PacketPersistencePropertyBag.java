package weblogic.wsee.jaxws.persistence;

import com.sun.xml.ws.api.PropertySet;
import com.sun.xml.ws.api.PropertySet.Property;
import java.util.HashSet;
import java.util.Set;
import weblogic.wsee.jaxws.framework.PropertySetUtil;

public class PacketPersistencePropertyBag extends PropertySet {
   public static final String PERSISTABLE_PROPERTY_NAMES = "weblogic.wsee.jaxws.persistence.PersistablePropertyMap";
   public static final String PERSISTABLE_INVOCATION_PROPERTY_NAMES = "weblogic.wsee.jaxws.persistence.PersistableInvocationPropertyMap";
   public static final String PERSISTABLE_PROPERTY_BAG_CLASS_NAMES = "weblogic.wsee.jaxws.persistence.PersistablePropertyClassNames";
   public static PropertySetUtil.PropertySetRetriever<PacketPersistencePropertyBag> propertySetRetriever = PropertySetUtil.getRetriever(PacketPersistencePropertyBag.class);
   private static final PropertySet.PropertyMap model = parse(PacketPersistencePropertyBag.class);
   private Set<String> _persistableProps = new HashSet();
   private Set<String> _persistableInvocationProps = new HashSet();
   private Set<String> _persistablePropBagClassNames = new HashSet();

   protected PropertySet.PropertyMap getPropertyMap() {
      return model;
   }

   public PacketPersistencePropertyBag() {
      this.addStandardProps();
   }

   private void addStandardProps() {
      this._persistableProps.add("javax.xml.ws.soap.http.soapaction.uri");
   }

   @Property({"weblogic.wsee.jaxws.persistence.PersistablePropertyMap"})
   public Set<String> getPersistablePropertyNames() {
      return this._persistableProps;
   }

   public void setPersistablePropertyNames(Set<String> var1) {
      this._persistableProps = var1;
   }

   @Property({"weblogic.wsee.jaxws.persistence.PersistableInvocationPropertyMap"})
   public Set<String> getPersistableInvocationPropertyNames() {
      return this._persistableInvocationProps;
   }

   public void setPersistableInvocationPropertyNames(Set<String> var1) {
      this._persistableInvocationProps = var1;
   }

   @Property({"weblogic.wsee.jaxws.persistence.PersistablePropertyClassNames"})
   public Set<String> getPersistablePropertyBagClassNames() {
      return this._persistablePropBagClassNames;
   }

   public void setPersistablePropertyBagClassNames(Set<String> var1) {
      this._persistablePropBagClassNames = var1;
   }
}
