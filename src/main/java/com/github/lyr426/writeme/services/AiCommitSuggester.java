package com.github.lyr426.writeme.services;

public class AiCommitSuggester {

  private static final String API_KEY = "<KEY>";
  private static final String API_URL = "https://api.openai.com/v1";

  public static String generate(String prompt) {
    try{
      String output = "feat(api): API 라우터에 새로운 번역 엔드포인트 추가\n"
        + "\n"
        + "- `api-router.js`에 `/translate` POST 라우트를 여러 개 추가했습니다.\n"
        + "- `controller.js`에 번역 기능을 추가할 가능성을 위한 코드 수정.\n"
        + "\n"
        + "fix(api): `controller.js`에서 불필요한 빈 줄 제거\n"
        + "\n"
        + "- `controller.js`의 코드 형식을 정리했습니다.";

      return output;
    } catch (Exception e) {
      e.printStackTrace();
      return "Error generating commit Message: " + e.getMessage() ;
    }

  }
}
