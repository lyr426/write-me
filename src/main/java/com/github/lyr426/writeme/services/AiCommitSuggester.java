package com.github.lyr426.writeme.services;

import com.github.lyr426.writeme.settings.SettingsState;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class AiCommitSuggester {

  private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";
  private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=";
  public static String generate(String diff) {
    SettingsState settings = SettingsState.getInstance();
    String provider = settings.getSelectedProvider(); // OPENAI 또는 GEMINI

    if ("GEMINI".equals(provider)) {
      String apiKey = settings.getGoogleApiKey();
      if (apiKey == null || apiKey.isEmpty()) {
        return "Error: Google API Key is missing. Please check your settings.";
      }
      return generateWithGemini(diff, apiKey);
    }

    else {
      String apiKey = settings.getApiKey();
      if (apiKey == null || apiKey.isEmpty()) {
        return "Error: OpenAI API Key is missing. Please check your settings.";
      }
      return generateWithOpenAI(diff, apiKey);
    }
  }

  private static String generateWithOpenAI(String diff, String apiKey) {
    try {
      URL url = new URL(OPENAI_API_URL);
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("POST");
      conn.setRequestProperty("Content-Type", "application/json");
      conn.setRequestProperty("Authorization", "Bearer " + apiKey);
      conn.setDoOutput(true);

      String systemPrompt = createSystemPrompt(diff);

      JSONObject requestBody = new JSONObject();
      requestBody.put("model", "gpt-4"); // gpt-3.5-turbo 등 변경 가능
      requestBody.put("temperature", 0.7);

      JSONArray messages = new JSONArray();
      messages.put(new JSONObject().put("role", "system").put("content", "You are a helpful assistant."));
      messages.put(new JSONObject().put("role", "user").put("content", systemPrompt));

      requestBody.put("messages", messages);

      return sendRequestAndParse(conn, requestBody, "OPENAI");

    } catch (Exception e) {
      e.printStackTrace();
      return "OpenAI Error: " + e.getMessage();
    }
  }

  private static String generateWithGemini(String diff, String apiKey) {
    try {
      URL url = new URL(GEMINI_API_URL + apiKey);
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("POST");
      conn.setRequestProperty("Content-Type", "application/json");
      conn.setDoOutput(true);

      String systemPrompt = createSystemPrompt(diff);

      JSONObject textPart = new JSONObject();
      textPart.put("text", systemPrompt);

      JSONObject partObj = new JSONObject();
      partObj.put("parts", new JSONArray().put(textPart));

      JSONObject requestBody = new JSONObject();
      requestBody.put("contents", new JSONArray().put(partObj));

      return sendRequestAndParse(conn, requestBody, "GEMINI");

    } catch (Exception e) {
      e.printStackTrace();
      return "Gemini Error: " + e.getMessage();
    }
  }

  private static String createSystemPrompt(String diff) {
    SettingsState settings = SettingsState.getInstance();
    String language = settings.getCommitLanguage(); // 설정된 언어 가져오기

    if (language == null || language.isEmpty()) {
      language = "Korean";
    }

    return "You are an AI that generates concise Git commit messages in the conventional commit style.\n" +
            "Generate a commit message in " + language + " for the following changes:\n" +
            diff;
  }

  private static String sendRequestAndParse(HttpURLConnection conn, JSONObject requestBody, String providerType) throws Exception {
    try (OutputStream os = conn.getOutputStream()) {
      byte[] input = requestBody.toString().getBytes(StandardCharsets.UTF_8);
      os.write(input, 0, input.length);
    }

    int responseCode = conn.getResponseCode();
    if (responseCode != HttpURLConnection.HTTP_OK) {
      try(BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8))) {
        StringBuilder errorResponse = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) errorResponse.append(line);
        return "Error (" + responseCode + "): " + errorResponse.toString();
      }
    }

    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
    StringBuilder response = new StringBuilder();
    String line;
    while ((line = reader.readLine()) != null) {
      response.append(line);
    }
    reader.close();

    JSONObject jsonResponse = new JSONObject(response.toString());

    if ("GEMINI".equals(providerType)) {
      return jsonResponse.getJSONArray("candidates")
              .getJSONObject(0)
              .getJSONObject("content")
              .getJSONArray("parts")
              .getJSONObject(0)
              .getString("text")
              .trim();
    } else {
      return jsonResponse.getJSONArray("choices")
              .getJSONObject(0)
              .getJSONObject("message")
              .getString("content")
              .trim();
    }
  }
}