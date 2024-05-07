package weblogic.upgrade.domain.directoryselection;

import com.bea.plateng.plugin.PlugInContext;
import com.bea.plateng.plugin.ia.Choice;
import com.bea.plateng.plugin.ia.DefaultChoiceInputAdapter;
import com.bea.plateng.wizard.plugin.gui.InputAdapterGUIBinder;
import com.oracle.cie.common.ui.gui.MultiLineLabelUI;
import com.oracle.cie.common.util.StringUtil;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Enumeration;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JToggleButton;
import javax.swing.UIManager;

public class OptionalGroupsInputAdapterGUIPanel extends InputAdapterGUIBinder {
   protected Choice[] _choices = null;
   protected JToggleButton[] _buttons = null;
   protected JLabel[] _labels = null;

   public void init(PlugInContext var1) {
      final ButtonGroup var2 = new ButtonGroup();
      this.setLayout(new BorderLayout());
      JPanel var3 = new JPanel(new GridBagLayout());
      GridBagConstraints var4 = new GridBagConstraints();
      var4.gridx = 0;
      var4.gridy = 0;
      var4.weightx = 1.0;
      var4.anchor = 23;
      String var5 = ((DefaultChoiceInputAdapter)this._adapter).getGroupDescription();
      if (!StringUtil.isNullOrEmpty(var5)) {
         JLabel var6 = new JLabel(var5);
         var6.setFont(var6.getFont().deriveFont(1));
         var4.insets.bottom = UIManager.getInt("PlugIn.choiceHeaderVSpacing");
         var3.add(var6, var4);
         var4.gridy = 1;
         var4.insets.bottom = 0;
      }

      boolean var11 = ((DefaultChoiceInputAdapter)this._adapter).isMulptipleSelectionPermitted();
      this._choices = ((DefaultChoiceInputAdapter)this._adapter).getChoices();
      this._buttons = new JToggleButton[this._choices.length];
      this._labels = new JLabel[this._choices.length];

      for(int var7 = 0; var7 < this._choices.length; ++var7) {
         final Object var8;
         if (var11) {
            var8 = new JCheckBox(this._choices[var7].getPrompt(), this._choices[var7].isSelected());
         } else {
            var8 = new JRadioButton(this._choices[var7].getPrompt(), this._choices[var7].isSelected());
            var2.add((AbstractButton)var8);
         }

         JLabel var9 = new JLabel(this._choices[var7].getDescription());
         MultiLineLabelUI var10 = new MultiLineLabelUI();
         var10.setWordWrap(110);
         var9.setUI(var10);
         var9.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent var1) {
               ((JToggleButton)var8).requestFocus();
               ((JToggleButton)var8).doClick();
            }
         });
         this._labels[var7] = var9;
         this._buttons[var7] = (JToggleButton)var8;
         ((JToggleButton)var8).putClientProperty("choice", this._choices[var7]);
         ((JToggleButton)var8).putClientProperty("choices", this._choices);
         ((JToggleButton)var8).putClientProperty("buttons", this._buttons);
         ((JToggleButton)var8).putClientProperty("labels", this._labels);
         ((JToggleButton)var8).addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
               if (var2.getButtonCount() > 0) {
                  Enumeration var2x = var2.getElements();

                  while(var2x.hasMoreElements()) {
                     JToggleButton var3 = (JToggleButton)var2x.nextElement();
                     Choice var4 = (Choice)var3.getClientProperty("choice");
                     var4.setSelected(false);
                  }
               }

               Choice var6 = (Choice)((JToggleButton)var8).getClientProperty("choice");
               var6.setSelected(((JToggleButton)var8).isSelected());
               Choice[] var7 = (Choice[])((Choice[])((JToggleButton)var8).getClientProperty("choices"));
               JToggleButton[] var8x = (JToggleButton[])((JToggleButton[])((JToggleButton)var8).getClientProperty("buttons"));
               JLabel[] var5 = (JLabel[])((JLabel[])((JToggleButton)var8).getClientProperty("labels"));
               if (!var7[0].isSelected()) {
                  var7[1].setSelected(false);
                  var8x[1].setEnabled(false);
                  var5[1].setEnabled(false);
               } else {
                  var8x[1].setEnabled(true);
                  var5[1].setEnabled(true);
               }

            }
         });
         var4.insets.top = var11 ? UIManager.getInt("PlugIn.checkboxVSpacing") : UIManager.getInt("PlugIn.radiobuttonVSpacing");
         if (var7 == 0) {
            var4.insets.top = 0;
         }

         var3.add((Component)var8, var4);
         ++var4.gridy;
         var4.insets.top = UIManager.getInt("PlugIn.inlineHelpSpacing");
         var3.add(var9, var4);
         ++var4.gridy;
      }

      this.add(var3, "North");
   }

   public String[] getSelectedChoiceIds() {
      ArrayList var1 = new ArrayList();

      for(int var2 = 0; var2 < this._choices.length; ++var2) {
         if (this._choices[var2].isSelected()) {
            var1.add(this._choices[var2].getId());
         }
      }

      return (String[])((String[])var1.toArray(new String[0]));
   }
}
