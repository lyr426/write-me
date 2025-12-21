package com.github.lyr426.writeme.settings;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(
        name = "WriteMeSettings",
        storages = @Storage("WriteMePluginSettings.xml")
)
@Service
public final class SettingsState implements PersistentStateComponent<SettingsState> {

  public String apiKey = "";
  public String googleApiKey = "";
  public String selectedProvider = "OPENAI";
  public String commitLanguage = "Korean";

  public static SettingsState getInstance() {
    return ApplicationManager.getApplication().getService(SettingsState.class);
  }

  @Override
  public @Nullable SettingsState getState() {
    return this;
  }

  @Override
  public void loadState(@NotNull SettingsState state) {
    XmlSerializerUtil.copyBean(state, this);
  }

  public String getApiKey() {
    return apiKey;
  }
  public void setApiKey(String apiKey) {
    this.apiKey = apiKey;
  }
  public String getGoogleApiKey() {
    return googleApiKey;
  }
  public void setGoogleApiKey(String googleApiKey) {
    this.googleApiKey = googleApiKey;
  }
  public String getSelectedProvider() {
    return selectedProvider;
  }
  public void setSelectedProvider(String selectedProvider) {
    this.selectedProvider = selectedProvider;
  }
  public String getCommitLanguage() {
    return commitLanguage;
  }
  public void setCommitLanguage(String commitLanguage) {
    this.commitLanguage = commitLanguage;
  }
}