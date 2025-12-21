package com.github.lyr426.writeme.settings;

import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;
import javax.swing.*;

public class SettingsComponent {

  private final JPanel mainPanel;

  private final JBTextField apiKeyField = new JBTextField();
  private final JBTextField googleApiKeyField = new JBTextField();

  private final JRadioButton rbOpenAI = new JRadioButton("OpenAI (GPT-4 / GPT-3.5)", true);
  private final JRadioButton rbGemini = new JRadioButton("Google (Gemini 1.5 Flash - Free)", false);

  private final ComboBox<String> languageComboBox = new ComboBox<>(new String[]{"Korean", "English"});

  public SettingsComponent() {
    ButtonGroup providerGroup = new ButtonGroup();
    providerGroup.add(rbOpenAI);
    providerGroup.add(rbGemini);

    apiKeyField.setColumns(30);
    googleApiKeyField.setColumns(30);

    mainPanel = FormBuilder.createFormBuilder()
            // --- OpenAI 섹션 ---
            .addComponent(new JBLabel("Select AI Provider:"))
            .addComponent(rbOpenAI)
            .addLabeledComponent(new JBLabel("OpenAI API Key:"), apiKeyField)
            .addSeparator(5)
            // --- Google Gemini 섹션 ---
            .addComponent(rbGemini)
            .addLabeledComponent(new JBLabel("Gemini API Key:"), googleApiKeyField)
            .addSeparator(10)
            .addLabeledComponent(new JBLabel("Commit Language:"), languageComboBox)

            .addComponentFillVertically(new JPanel(), 0)
            .getPanel();
  }

  public JPanel getPanel() {
    return mainPanel;
  }

  public JComponent getPreferredFocusedComponent() {
    return apiKeyField;
  }

  public boolean isModified() {
    SettingsState settings = SettingsState.getInstance();

    boolean isOpenAIKeyChanged = !apiKeyField.getText().equals(settings.getApiKey());
    boolean isGoogleKeyChanged = !googleApiKeyField.getText().equals(settings.getGoogleApiKey());

    boolean isProviderChanged = false;
    if (rbOpenAI.isSelected() && !"OPENAI".equals(settings.getSelectedProvider())) isProviderChanged = true;
    if (rbGemini.isSelected() && !"GEMINI".equals(settings.getSelectedProvider())) isProviderChanged = true;

    boolean isLanguageChanged = !languageComboBox.getItem().equals(settings.getCommitLanguage());

    return isOpenAIKeyChanged || isGoogleKeyChanged || isProviderChanged || isLanguageChanged;
  }

  public void saveSettings() {
    SettingsState settings = SettingsState.getInstance();

    settings.setApiKey(apiKeyField.getText());
    settings.setGoogleApiKey(googleApiKeyField.getText());

    if (rbOpenAI.isSelected()) {
      settings.setSelectedProvider("OPENAI");
    } else {
      settings.setSelectedProvider("GEMINI");
    }

    settings.setCommitLanguage((String) languageComboBox.getItem());
  }

  public void loadSettings() {
    SettingsState settings = SettingsState.getInstance();

    apiKeyField.setText(settings.getApiKey());
    googleApiKeyField.setText(settings.getGoogleApiKey());

    if ("GEMINI".equals(settings.getSelectedProvider())) {
      rbGemini.setSelected(true);
    } else {
      rbOpenAI.setSelected(true);
    }
    languageComboBox.setItem(settings.getCommitLanguage());
  }
}