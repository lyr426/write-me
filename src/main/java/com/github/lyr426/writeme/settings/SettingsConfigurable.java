package com.github.lyr426.writeme.settings;

import com.intellij.openapi.options.Configurable;
import javax.swing.JComponent;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

public class SettingsConfigurable implements Configurable {

  private SettingsComponent settingsComponent;

  @Nls(capitalization = Nls.Capitalization.Title)
  @Override
  public String getDisplayName() {
    return "Write Me Plugin Settings";
  }

  @Nullable
  @Override
  public JComponent createComponent() {
    settingsComponent = new SettingsComponent();
    return settingsComponent.getPanel();
  }

  @Override
  public boolean isModified() {
    return settingsComponent != null && settingsComponent.isModified();
  }

  @Override
  public void apply() {
    if (settingsComponent != null) {
      settingsComponent.saveSettings();
    }
  }

  @Override
  public void reset() {
    if (settingsComponent != null) {
      settingsComponent.loadSettings();
    }
  }

  @Override
  public void disposeUIResources() {
    settingsComponent = null;
  }
}
