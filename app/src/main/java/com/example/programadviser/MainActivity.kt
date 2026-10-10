package com.example.programadviser

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.programadviser.controller.AdviserController
import com.example.programadviser.databinding.ActivityMainBinding
import com.example.programadviser.databinding.ItemGreetingBinding
import com.example.programadviser.model.AdviserModel
import com.example.programadviser.view.AdviserContract

/**
 * 作业二：UI 控件代码生成以及 MVC
 *
 * 本 Activity 是 MVC 模式中的 View 层（实现 AdviserContract.View）：
 *  - 区 1：输入框 + 添加文本 + 查询，动态列表里的 TextView 全部由代码创建；
 *  - 区 2：分类下拉框 + 确定，结果由 Model 层提供、精简显示。
 *
 * 外层用 ScrollView 嵌套 LinearLayout 实现动态列表的滚动显示；
 * 界面文字全部来自 res/values/strings.xml，布局文件与代码中不出现硬编码字符串。
 * ViewBinding 已在 app/build.gradle.kts 中开启：buildFeatures { viewBinding = true }
 */
class MainActivity : AppCompatActivity(), AdviserContract.View {

    /** ① 开启 ViewBinding 后自动生成的绑定类 */
    private lateinit var binding: ActivityMainBinding

    /** Controller：把 View 与 Model 连接起来 */
    private lateinit var adviserController: AdviserController

    /** 上一次自动生成的编号（从 4 开始，所以第一条是 5，与参考界面一致） */
    private var lastNumber = FIRST_MESSAGE_NUMBER - 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ② 用 ViewBinding 加载布局并作为内容视图
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ③ 应用栏（紫色标题栏，标题在布局里通过 app:title 指定）
        setSupportActionBar(binding.toolbar)

        // ④ MVC 装配：Model（数据）→ Controller（事件响应与调度）→ View（本 Activity）
        adviserController = AdviserController(this, AdviserModel(this))
        adviserController.start()

        // ⑤ 事件响应：View 只把点击转交 Controller，逻辑在 Controller / Model
        binding.btnAddMessage.setOnClickListener { adviserController.onAddClicked() }
        binding.btnQuery.setOnClickListener { adviserController.onQueryClicked() }
        binding.btnConfirm.setOnClickListener { adviserController.onConfirmClicked() }
        // 输入框回车等同于“添加文本”
        binding.etContent.setOnEditorActionListener { _, _, _ ->
            adviserController.onAddClicked()
            true
        }

        // 刚打开时列表是空的，没有任何预置条目
    }

    // ------------------------------------------------------------------
    // MVC 的 View 接口实现（只做展示与取输入，不含业务逻辑）
    // ------------------------------------------------------------------

    override fun inputText(): String = binding.etContent.text.toString().trim()

    override fun clearInput() {
        binding.etContent.setText("")
    }

    override fun showQueryResult(text: String) {
        binding.tvQueryResult.text = text
    }

    override fun showCategoryResult(text: String) {
        binding.tvAdviserResult.text = text
    }

    override fun showCategories(categories: List<String>) {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCategory.adapter = adapter
    }

    override fun selectedCategoryIndex(): Int = binding.spinnerCategory.selectedItemPosition

    override fun emptyInputHint(): String = getString(R.string.query_no_input)

    override fun queryResultText(text: String, found: Boolean): String =
        if (found) getString(R.string.query_found, text) else getString(R.string.query_not_found, text)

    // ------------------------------------------------------------------
    // 用代码动态生成 TextView（作业要求：掌握控件用代码动态生成）
    // ------------------------------------------------------------------

    /**
     * 动态创建一个 TextView 并追加到 LinearLayout 中。
     * 滚动由外层 ScrollView 自动完成：添加后滚动到最新一条。
     *
     * @param text 指定文本；传 null 时自动生成“This is a new textTextView：编号”，编号递增
     */
    override fun appendMessage(text: String?) {
        lastNumber++

        // ItemGreetingBinding 由 ViewBinding 为 item_greeting.xml 自动生成：
        // 不写死布局 XML，完全由代码创建 View
        val itemBinding = ItemGreetingBinding.inflate(
            layoutInflater, binding.llMessages, false
        )
        itemBinding.tvMessage.text = text ?: getString(R.string.record_format, lastNumber)

        binding.llMessages.addView(itemBinding.root)

        // 让 ScrollView 滚动到刚添加的控件上
        binding.svMessages.post {
            binding.svMessages.fullScroll(View.FOCUS_DOWN)
        }
    }

    private companion object {
        /** 自动编号起始值：参考界面里从 5 开始 */
        const val FIRST_MESSAGE_NUMBER = 5
    }
}
