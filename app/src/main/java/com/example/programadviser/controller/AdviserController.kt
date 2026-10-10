package com.example.programadviser.controller

import com.example.programadviser.view.AdviserContract

/**
 * MVC 中的 Controller 层：
 *  - 监听 View 上按钮/输入框的事件（“添加文本”“查询”“确定”）；
 *  - 调用 Model 保存与查询数据；
 *  - 把结果交回 View 渲染。
 *
 * Controller 同时依赖 View 接口与 Model 接口，两者互不认识，实现解耦。
 */
class AdviserController(
    private val view: AdviserContract.View,
    private val model: AdviserContract.Model
) {

    /** 由 Activity 在 onCreate 中调用：初始化区 2 的分类下拉列表 */
    fun start() {
        view.showCategories(model.categoryNames())
    }

    /**
     * 按钮“添加文本”的事件响应：
     *  - 输入框有内容：保存该内容并动态添加一条；
     *  - 输入框为空：由 View 自动生成“This is a new textTextView：编号”加一条（不保存）。
     */
    fun onAddClicked() {
        val text = view.inputText()
        if (text.isEmpty()) {
            view.appendMessage(null)
            return
        }
        model.add(text)
        view.appendMessage(text)
        view.clearInput()
    }

    /** 按钮“查询”的事件响应：查询模型层，把结果交回 View 显示 */
    fun onQueryClicked() {
        val text = view.inputText()
        if (text.isEmpty()) {
            view.showQueryResult(view.emptyInputHint())
            return
        }
        val found = model.contains(text)
        view.showQueryResult(view.queryResultText(text, found))
    }

    /** 按钮“确定”的事件响应：查询模型层并返回相应结果（区 2 精简显示分类名） */
    fun onConfirmClicked() {
        view.showCategoryResult(model.categoryOf(view.selectedCategoryIndex()))
    }
}
