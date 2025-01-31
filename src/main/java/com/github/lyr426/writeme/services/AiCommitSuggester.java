package com.github.lyr426.writeme.services;

import com.github.lyr426.writeme.settings.SettingsState;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class AiCommitSuggester {

  private static final String API_URL = "https://api.openai.com/v1/chat/completions";

  public static String generate(String prompt) {
    try {
      SettingsState settings = SettingsState.getInstance();
      String apiKey = settings.getApiKey();
      // 1. OpenAI API 요청 생성
      URL url = new URL(API_URL);
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("POST");
      conn.setRequestProperty("Content-Type", "application/json");
      conn.setRequestProperty("Authorization", "Bearer " + apiKey);
      conn.setDoOutput(true);

      // 2. GPT 모델 설정 (커밋 메시지 생성을 위한 프롬프트)
      JSONObject requestBody = new JSONObject();
      requestBody.put("model", "gpt-4");  // 사용할 모델 지정
      requestBody.put("temperature", 0.7);  // 다양성 조절
      requestBody.put("max_tokens", 250);  // 최대 토큰 길이 제한

      // 시스템 역할 추가 (커밋 메시지 스타일 정의)
      requestBody.put("messages", new JSONObject[]{
        new JSONObject().put("role", "system").put("content", "You are an AI that generates concise Git commit messages in the conventional commit style."),
        new JSONObject().put("role", "user").put("content", "Generate a commit message in korean for the following changes:\n" + prompt)
      });

      // 3. 요청 본문 전송
      try (OutputStream os = conn.getOutputStream()) {
        byte[] input = requestBody.toString().getBytes("utf-8");
        os.write(input, 0, input.length);
      }

      // 4. 응답 수신 및 처리
      int responseCode = conn.getResponseCode();
      if (responseCode == HttpURLConnection.HTTP_OK) { // 200 응답 확인
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
          response.append(line);
        }
        reader.close();

        // JSON 응답 파싱
        JSONObject jsonResponse = new JSONObject(response.toString());
        String commitMessage = jsonResponse.getJSONArray("choices")
          .getJSONObject(0)
          .getJSONObject("message")
          .getString("content")
          .trim();

        return commitMessage;
      } else {
        System.out.println("re = " + conn.getResponseMessage());
        return "Error: Received HTTP response code " + responseCode;
      }
    } catch (Exception e) {
      e.printStackTrace();
      return "Error generating commit message: " + e.getMessage();
    }

  }
}
