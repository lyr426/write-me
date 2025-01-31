package com.github.lyr426.writeme.settings;

import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class SettingsComponent {

  private final JPanel panel;
  private final JTextField apiKeyField;

  public SettingsComponent() {
    // 패널 생성 및 레이아웃 설정
    panel = new JPanel(new BorderLayout());

    // 라벨과 텍스트 필드 생성
    JLabel label = new JLabel("OpenAI API Key:");
    apiKeyField = new JTextField();

    // 라벨과 텍스트 필드를 상단에 위치시키기
    JPanel topPanel = new JPanel(new BorderLayout());
    topPanel.add(label, BorderLayout.WEST);
    topPanel.add(apiKeyField, BorderLayout.CENTER);

    // 패널 상단에 배치
    panel.add(topPanel, BorderLayout.NORTH);

    // 텍스트 필드의 초기 크기 설정
    apiKeyField.setColumns(20); // 텍스트 필드 너비를 설정
  }

  public JPanel getPanel() {
    return panel;
  }

  public boolean isModified() {
    String savedKey = SettingsState.getInstance().getApiKey();
    return !apiKeyField.getText().equals(savedKey);
  }

  public void saveSettings() {
    SettingsState.getInstance().setApiKey(apiKeyField.getText());
  }

  public void loadSettings() {
    apiKeyField.setText(SettingsState.getInstance().getApiKey());
  }
}
