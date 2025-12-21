# Write Me ⚡️
[![IntelliJ Platform](https://img.shields.io/badge/IntelliJ-Platform-nk?logo=intellij-idea)](https://plugins.jetbrains.com/)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](LICENSE)

**Write Me** is an IntelliJ plugin that utilizes **Google Gemini** and **OpenAI GPT** models to analyze code changes (Git Diff) and **automatically generate appropriate commit messages**.

**Write Me**는 **Google Gemini**와 **OpenAI GPT** 모델을 활용하여 코드 변경 사항(Git Diff)을 분석하고, 상황에 맞는 **커밋 메시지를 자동으로 생성해주는 인텔리제이(IntelliJ) 플러그인**입니다.

---

## 🇺🇸 English

### 🚀 Features
* **Multi-Model Support:** Choose between **Google Gemini** or **OpenAI GPT** as your AI engine.
* **Context-Aware Analysis:** Automatically analyzes the context of your code changes (Git Diff) to understand the intent.
* **One-Click Generation:** Generate commit messages with a single click directly inside the Commit window.
* **Smart Summaries:** Generates concise, meaningful, and ready-to-use commit messages.

### ⚙️ Configuration
You need a valid API Key for the service you wish to use (**Google Gemini** or **OpenAI**).

1. **Get your API Key:**
    * **Google Gemini:** [Google AI Studio](https://aistudio.google.com/app/apikey)
    * **OpenAI GPT:** [OpenAI Platform](https://platform.openai.com/api-keys)
2. Open **Settings** (Windows/Linux) or **Settings / Preferences** (macOS) in IntelliJ. (`Ctrl` + `Alt` + `S` / `Cmd` + `,`)
3. Find **Write Me** in the left-hand menu.
4. Select your preferred **AI Model** and enter the corresponding **API Key**.
5. Click `Apply`.

> **⚠️ Privacy Disclaimer**
>
> Please be aware that when using AI services (Google Gemini or OpenAI), **your data (such as code diffs) is sent to external servers for processing.**
> * **Google Gemini:** Free tier usage may be used to improve their models.
> * **OpenAI:** Please refer to OpenAI's API data usage policies.
>
> Please exercise caution when using this plugin with sensitive code, proprietary projects, or files containing personal information.

> ![Settings Screenshot](https://raw.githubusercontent.com/lyr426/write-me/refs/heads/main/docs/images/writeme_setting_screenshot.PNG)
> *(Settings Screen Example)*

### 📝 Usage
1. Open the **Commit** tool window (`Alt` + `0` or `Cmd` + `K`).
2. Select the files you want to include in the commit.
3. Click the **⚡️ (Generate Commit Message)** icon located on the commit toolbar.
4. The AI-generated message will be automatically populated in the input field.

---

## 🇰🇷 한국어 (Korean)

### 🚀 주요 기능
* **멀티 모델 지원:** **Google Gemini**와 **OpenAI GPT** 중 원하는 AI 모델을 선택하여 사용할 수 있습니다.
* **AI 자동 분석:** 변경된 코드의 내용을 AI가 분석하여 문맥을 파악합니다.
* **원클릭 생성:** 커밋 창에서 버튼 하나만 누르면 메시지가 작성됩니다.
* **다국어 지원:** 한국어로 자연스러운 커밋 메시지를 생성합니다.

### ⚙️ 설정 방법 (Configuration)
사용하고자 하는 서비스(**Google Gemini** 또는 **OpenAI**)의 API Key가 필요합니다.

1. **API Key 발급:**
    * **Google Gemini:** [Google AI Studio](https://aistudio.google.com/app/apikey)에서 발급
    * **OpenAI GPT:** [OpenAI Platform](https://platform.openai.com/api-keys)에서 발급
2. IntelliJ 상단 메뉴에서 **Settings** (Mac: `Preferences`)를 엽니다. (`Ctrl` + `Alt` + `S` / `Cmd` + `,`)
3. 좌측 메뉴 목록에서 **Write Me**를 찾습니다.
4. 원하는 **AI 모델**을 선택하고 해당 **API Key**를 입력한 뒤 `Apply`를 누릅니다.

> **⚠️ 데이터 프라이버시 주의사항 (Privacy Disclaimer)**
>
> AI 서비스(Google Gemini, OpenAI)를 사용할 경우, 분석을 위해 **데이터(코드 변경 내역 등)가 외부 서버로 전송됩니다.**
> * **Google Gemini:** 무료 버전(Free Tier) 사용 시 데이터가 모델 학습에 사용될 수 있습니다.
> * **OpenAI:** OpenAI의 API 데이터 정책을 참고하시기 바랍니다.
>
> 보안이 중요한 사내 프로젝트나 민감한 정보(비밀번호, 개인정보 등)가 포함된 코드에서는 사용에 주의해주시기 바랍니다.

> ![Settings Screenshot](https://raw.githubusercontent.com/lyr426/write-me/refs/heads/main/docs/images/writeme_setting_screenshot.PNG)
> *(설정 화면 예시)*

### 📝 사용 방법 (Usage)
1. Git 커밋 창을 엽니다. (`Alt` + `0` 또는 `Cmd` + `K`)
2. Stage에 커밋할 파일들을 체크합니다.
3. 커밋 메시지 입력란 상단 툴바에 있는 **⚡️ (Generate Commit Message)** 아이콘을 클릭합니다.
4. AI가 분석한 커밋 메시지가 자동으로 입력됩니다.

---

### 🛠 Tech Stack
* **JDK:** 17+
* **Platform:** IntelliJ Platform SDK
* **API:** Google Gemini / OpenAI GPT

## 📄 License
This project is licensed under the Apache 2.0 License - see the [LICENSE](LICENSE) file for details.