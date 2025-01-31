package com.github.lyr426.writeme.settings;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(name="WriteMeSettings", storages = @Storage("WriteMePluginSettings.xml"))
@Service
public final class SettingsState implements PersistentStateComponent<SettingsState> {

  private String apiKey;

  public static SettingsState getInstance() {
    return ServiceManager.getService(SettingsState.class);
  }

  @Override
  public @Nullable SettingsState getState() {
    return this;
  }

  @Override
  public void loadState(@NotNull SettingsState settingsState) {
    this.apiKey = settingsState.apiKey;
  }

  public String getApiKey() {
    return apiKey;
  }

  public void setApiKey(String apiKey) {
    this.apiKey = apiKey;
  }

}
