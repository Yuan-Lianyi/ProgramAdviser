package com.example.programadviser.view

/**
 * MVC 中 View 层与 Controller 层之间的契约。
 *
 * View（MainActivity）只实现这里的渲染方法；
 * Controller 只依赖该接口，不依赖任何具体 Activity，便于替换与测试。
 */
interface AdviserContract {

    /** View 需要提供的能力：渲染数据、收集用户输入、提供界面文案 */
    interface View {
        /** 取输入框里的内容 */
        fun inputText(): String

        /** 清空输入框 */
        fun clearInput()

        /**
         * 用代码动态添加一条到列表。
         * @param text 传 null 时由 View 自动生成“This is a new textTextView：编号”
         */
        fun appendMessage(text: String?)

        /** 显示查询结果（“找到：xxx”／“未找到：xxx”） */
        fun showQueryResult(text: String)

        /** 输入为空时的提示文案（文案来自字符串资源） */
        fun emptyInputHint(): String

        /** 根据查询结果生成提示文案（文案来自字符串资源） */
        fun queryResultText(text: String, found: Boolean): String
    }

    /** Controller 需要 Model 提供的能力 */
    interface Model {
        /** 保存一条内容 */
        fun add(text: String)

        /** 查询是否已存在该内容 */
        fun contains(text: String): Boolean
    }
}
