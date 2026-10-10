package com.example.programadviser.model

import com.example.programadviser.view.AdviserContract

/**
 * MVC 中的 Model 层：保存用户添加的内容，并支持查询。
 *
 * 完全不知道界面长什么样，可以独立替换或扩展（例如改成数据库、网络请求）。
 */
class AdviserModel : AdviserContract.Model {

    /** 已保存的内容（用列表按添加顺序保存） */
    private val items = mutableListOf<String>()

    override fun add(text: String) {
        items.add(text)
    }

    override fun contains(text: String): Boolean = items.contains(text)
}
