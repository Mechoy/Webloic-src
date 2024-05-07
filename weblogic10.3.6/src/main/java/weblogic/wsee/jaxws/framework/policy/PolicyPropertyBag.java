package weblogic.wsee.jaxws.framework.policy;

import com.sun.istack.NotNull;
import com.sun.xml.ws.api.PropertySet;
import com.sun.xml.ws.api.PropertySet.Property;
import weblogic.wsee.jaxws.framework.PropertySetUtil;
import weblogic.wsee.policy.util.PolicySelectionPreference;

public class PolicyPropertyBag extends PropertySet {
   public static final String POLICY_SELECTION_PREFERENCE = "weblogic.wsee.policy.selection.preference";
   public static PropertySetUtil.PropertySetRetriever<PolicyPropertyBag> propertySetRetriever = PropertySetUtil.getRetriever(PolicyPropertyBag.class);
   private static final PropertySet.PropertyMap model = parse(PolicyPropertyBag.class);
   private PolicySelectionPreference _pref;

   protected PropertySet.PropertyMap getPropertyMap() {
      return model;
   }

   @Property({"weblogic.wsee.policy.selection.preference"})
   @NotNull
   public PolicySelectionPreference getPolicySelectionPreference() {
      if (this._pref == null) {
         this._pref = new PolicySelectionPreference();
      }

      return this._pref;
   }

   public void setPolicySelectionPreference(@NotNull PolicySelectionPreference var1) {
      this._pref = var1;
   }
}
