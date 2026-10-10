# 作业二：UI 控件代码生成以及 MVC（ProgramAdviser）

Android 基础操作实验（2 学时）完整工程。一个界面里包含两个功能区：

| 功能区 | 作业要求 | 本工程实现 |
| --- | --- | --- |
| 区 1 | 通过交互**用代码动态添加 TextView**，并用 `ScrollView` **嵌套** `LinearLayout` 滚动显示 | `MainActivity.appendMessage()` 用 `ItemGreetingBinding.inflate(...)` 在代码中创建 TextView 并 `addView` 到 `llMessages`，外层 `svMessages` 自动滚动 |
| 区 2 | 参考 Kotlin MVC 教学视频，用 **MVC 模式**完成交互式 ProgramAdviser，按钮事件响应 + 查询模型层并返回结果 | View = `MainActivity`（实现 `AdviserContract.View`）；Controller = `AdviserController`；Model = `AdviserModel` + `DefaultAdviserCorpus` |

其他硬性要求：

- **ViewBinding**：`app/build.gradle.kts` 中 `buildFeatures { viewBinding = true }`，布局全部通过绑定类访问，不用 `findViewById`。
- **禁止硬编码字符串**：界面文字全部放在 `res/values/strings.xml`，布局用 `@string/...`，代码用 `getString(R.string.xxx)`；动态生成的 TextView 文本同样来自字符串资源。
- **使用字符串资源间接应用**：含 `%1$d`、`%1$s` 等格式化占位符的文案也统一放在资源文件。
- **界面严格按老师提供的参考界面实现**，只有参考图里出现的东西，没有多余控件：
  顶部紫色应用栏（`Toolbar`，标题取 `app_name`＝“姓名+学号”）；
  区 1 只有一个居中的「添加」按钮 + 青色滚动列表；
  区 2 只有一个分类下拉框 + 居中的「确定」按钮 + 大字结果区。

---

## 1.1 两个功能区的界面与交互

| 位置 | 参考界面 | 本工程实现 |
| --- | --- | --- |
| 顶部应用栏 | 紫底白字标题 | `toolbar` + `setSupportActionBar`，标题 `app_name` |
| 区 1 按钮 | 居中的 `ADD TEXTVIEW` | 居中的「添加」（`btnAddMessage`） |
| 区 1 滚动区 | 青色底，逐条 `new textView:n` | 青色底 `svMessages` 嵌套 `llMessages`，逐条「新的 TextView：n」 |
| 区 2 选择框 | 「新语言」下拉框 | 分类下拉框（`spinnerCategory`，选项由 Model 提供） |
| 区 2 主按钮 | 居中的「确定」 | 居中的「确定」（`btnConfirm`）：按当前分类取一条问答 |
| 区 2 结果区 | 绿色底显示结果文本 | 绿色底 `tvAdviserResult`，显示「【分类】问题 + 答案」 |

---

## 1. 快速运行

### 方式 A：Android Studio（推荐）

1. Android Studio → **Open** → 选择本目录 `ProgramAdviser`（含 `settings.gradle.kts` 的那一层）。
2. 等待 Gradle Sync 完成（首次会下载 Gradle 与依赖）。
3. 顶部选择 `app` 配置，连接模拟器或真机，点击 ▶ Run。

### 方式 B：命令行

```bash
cd ProgramAdviser
# 需要 JDK 17+；sdk.dir 已写在 local.properties（该文件不提交，换机器请自行生成）
./gradlew assembleDebug          # Linux / macOS
gradlew.bat assembleDebug        # Windows
```

产物：`app/build/outputs/apk/debug/app-debug.apk`

---

## 2. 提交前必须做的两件事

1. **确认应用名称是自己的“姓名+学号”**：`app/src/main/res/values/strings.xml` 里的
   `app_name` 已经是本人的信息（桌面图标名 + AndroidManifest 的 `android:label`）：

   ```xml
   <string name="app_name">袁连一 2024110545</string>
   ```

   > 注意：字符串值不能以 `@string/...` 引用**开头**（aapt2 会把整串当成资源名解析，
   > 报 `resource string/xxx not found`，本工程调试时就是这样报错的）。需要组合文案时，
   > 请用 `%1$s` 占位符 + `getString(R.string.x, ...)` 的方式。

2. **推送到自己的 gitee 仓库**（`local.properties`、`build/`、`.idea/` 已被 `.gitignore` 忽略）：

   ```bash
   cd ProgramAdviser
   git init
   git add .
   git commit -m "作业二：UI 控件代码生成以及 MVC"
   git remote add origin https://gitee.com/<你的用户名>/<仓库名>.git
   git push -u origin master
   ```

   最后把仓库地址写进实验报告末尾（见报告第八节）。

---

## 2.1 Android Studio 打不开布局设计器 / 同步失败怎么办

现象：打开 `activity_main.xml` 时提示 **“Design editor is unavailable until next gradle sync”**，
或者 Gradle Sync 卡在下载 `gradle-9.0.0-bin.zip`。

原因有两类，按顺序排查：

1. **工程没同步**：点右侧提示条里的 **Sync Now**，或菜单 `File → Sync Project with Gradle Files`。
   同步成功后提示会自动消失。

2. **网络下载不动**（本机实测 `services.gradle.org` 和 Maven Central 都不通）：
   让 Android Studio 直接用本机已装好的 Gradle，跳过下载：

   `File → Settings → Build, Execution, Deployment → Build Tools → Gradle`
   → 把 **Gradle distribution** 选成 **Local installation**，路径填：

   ```
   C:\Users\yuanl\.gradle\wrapper\dists\gradle-9.0.0-bin\eoo0lxrvoa5ndeman00yjmmhy\gradle-9.0.0
   ```

   然后重新 Sync。依赖包（AGP 8.13.0、Kotlin 2.2.0、androidx）本机 Gradle 缓存里都已经有，
   同步不需要联网。

> 另外建议用 Android Studio **直接打开 `ProgramAdviser` 这一个目录**（而不是打开它的上级目录 `second2`），
> 这样工程结构最标准，上面的提示条和 Sync 行为都更正常。

---

## 2.2 Android SDK 的位置（本机已重新安装）

原来的 Android SDK 在 `C:\Users\yuanl\AppData\Local\Android\Sdk`，这个目录被系统清理掉了
（构建时报 `SDK location not found`）。现已用 Google 官方命令行工具重新安装到**工程附近**：

```
D:\DeepSeek\second2\android-sdk
├── build-tools\36.0.0
├── platforms\android-36
├── platform-tools\          （adb）
├── cmdline-tools\latest\    （sdkmanager）
├── emulator\                （Android 模拟器）
└── system-images\android-36\google_apis\x86_64
```

`local.properties` 已指向这个新位置，所以直接构建即可，不用改任何东西：

```
sdk.dir=D\:\\DeepSeek\\second2\\android-sdk
```

**为什么装到这里**：放在 `AppData\Local` 下容易被 Windows 存储感知当缓存清理，
装在工程旁边既不容易被误删，也方便整体备份或迁移。

**如果以后又被删了**，用命令行一条命令就能装回来：

```powershell
$sdk = 'D:\DeepSeek\second2\android-sdk'
# 1) 下载并展开官方命令行工具到 $sdk\cmdline-tools\latest
#    https://dl.google.com/android/repository/commandlinetools-win-11076708_latest.zip
# 2) 安装需要的组件
& "$sdk\cmdline-tools\latest\bin\sdkmanager.bat" --sdk_root=$sdk `
    'platform-tools' 'platforms;android-36' 'build-tools;36.0.0'
```

> 也可以在 Android Studio 里走 `Tools → SDK Manager`，把 **SDK location** 指到
> `D:\DeepSeek\second2\android-sdk` 即可，不必再改工程文件。

---

## 3. 工程结构

```
ProgramAdviser/
├── build.gradle.kts                 # 插件版本（AGP 8.13.0 / Kotlin 2.2.0）
├── settings.gradle.kts              # 仓库与模块声明
├── gradle.properties
├── local.properties                 # 本机 SDK 路径（不提交）
├── gradle/wrapper/                  # Gradle 9.0.0 wrapper
└── app/
    ├── build.gradle.kts             # compileSdk 36 / minSdk 24 / viewBinding = true
    └── src/main/
        ├── AndroidManifest.xml
        ├── java/com/example/programadviser/
        │   ├── MainActivity.kt              # 区 1 动态生成 UI + 区 2 的 View 层
        │   ├── view/AdviserContract.kt      # MVC 的 View / Model 契约接口
        │   ├── controller/AdviserController.kt  # MVC 的 Controller 层
        │   └── model/
        │       ├── AdviserModel.kt          # MVC 的 Model 层（按分类取问答）
        │       ├── DefaultAdviserCorpus.kt  # 问答语料数据
        │       ├── AdviserData.kt           # AdviserCategory / AdviserItem
        │       └── AdviserAnswer.kt         # 一条问答结果
        └── res/
            ├── layout/activity_main.xml     # 应用栏 + 两个功能区
            ├── layout/section_add.xml       # 区 1：按钮 + ScrollView 嵌套 LinearLayout
            ├── layout/section_adviser.xml   # 区 2：Spinner + 确定按钮 + 结果区
            ├── layout/item_greeting.xml     # 区 1 动态 TextView 的条目模板
            ├── values/strings.xml           # 全部界面文案
            ├── values/colors.xml, dimens.xml, themes.xml
            └── drawable/, mipmap-*/
```

---

## 4. MVC 调用链（区 2）

```
用户点击“确定”
   │
   ▼
MainActivity（View） ── btnConfirm.setOnClickListener ──▶ AdviserController.onConfirmClicked()
                                                              │  读取 selectedCategoryIndex
                                                              ▼
                                                    AdviserModel.browse(分类下标)   ← Model 层取数据
                                                              │  返回 AdviserAnswer
                                                              ▼
MainActivity.showResult(...)  ◀────────────────────────  Controller 回传结果
   │
   ▼
tvAdviserResult 渲染（文案模板来自 R.string.adviser_result_format）
```

- View 只负责「展示 / 提供选中项」，不写业务逻辑；
- Controller 负责「事件响应 + 调度」，不依赖具体 Activity（只看 `AdviserContract.View`）；
- Model 负责「数据」，完全不知道界面存在；分类下拉框的选项也由 Model 提供。

### Model 层取数据（`AdviserModel.browse`）

1. 按分类下标取出对应分类的问答条目（下标 0 表示“全部分类”，此时用整份语料）；
2. 从该范围的条目里随机取一条；
3. 返回 `AdviserAnswer(分类名, 问题, 答案)` 给 Controller。

---

## 5. 自查清单（对照实验要求）

- [x] 区 1 点击按钮后由**代码**动态创建 TextView（未在布局中预置条目）
- [x] 区 1 采用 **ScrollView 嵌套 LinearLayout**，添加后自动滚动到最新一条
- [x] 区 2 为 **MVC** 结构，View / Controller / Model 三层目录清晰
- [x] 区 2 实现按钮事件响应，并**查询模型层返回结果**
- [x] 布局文件与 Kotlin 代码中**没有**硬编码界面字符串
- [x] 使用 **ViewBinding**（`ActivityMainBinding` / `ItemGreetingBinding`）
- [x] 应用标题为“姓名+学号”（占位符待替换为本人信息）
- [x] 界面严格按参考界面实现，没有多余控件

---

## 6. 本次验证记录

| 验证项 | 命令 / 操作 | 结果 |
| --- | --- | --- |
| 编译构建 | `clean assembleDebug`（Gradle 9.0.0 + AGP 8.13.0 + Kotlin 2.2.0 + JDK 21） | BUILD SUCCESSFUL，产出 `app-debug.apk` |
| 资源链接 | 构建过程 `processDebugResources` | 通过（过程中修复了一个真实的字符串资源错误，见下） |
| 安装运行 | 模拟器 Android 16 / API 36 上 `adb install -r` | 安装成功，`MainActivity` 正常启动 |
| 区 1 功能 | 连点「添加」 | 动态生成「新的 TextView：n」，ScrollView 正常滚动 |
| 区 2 功能 | 选分类 → 点「确定」 | Model 层返回该分类的问答并正确渲染 |

### 调试中真实遇到的两个问题

1. **字符串值以 `@string/...` 开头导致资源链接失败**：最初把应用标题写成
   `<string name="app_name">@string/student_name 的作业二（@string/student_no）</string>`，
   aapt2 会把以 `@` 开头的整串当成资源名解析，报
   `resource string/student_name 的作业二（@string/student_no） not found`。
   解决办法：`app_name` 直接用“姓名 学号”字面量；需要拼接时改用占位符
   `%1$s` + `getString(R.string.x, ...)`。

2. **命令行构建报 `Could not initialize native services / Failed to load native platform.dll`**：
   属于本机 `JAVA_HOME` 未配置、受限环境下无法加载本机库的环境问题，
   与工程代码无关。解决办法：显式指定 Android Studio 自带 JBR 作为 `JAVA_HOME`。
