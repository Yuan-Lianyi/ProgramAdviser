package com.example.programadviser.model

import android.content.Context
import com.example.programadviser.R
import com.example.programadviser.view.AdviserContract

/**
 * MVC 中的 Model 层：
 *  - 保存区 1 用户添加的内容，并支持查询；
 *  - 提供区 2 的分类数据。
 *
 * 完全不知道界面长什么样，可以独立替换或扩展。
 */
class AdviserModel(private val context: Context) : AdviserContract.Model {

    /** 区 1：已保存的内容（按添加顺序保存） */
    private val items = mutableListOf<String>()

    /** “全部分类”这个选项名属于界面文案，从字符串资源读取 */
    private val allCategoryLabel: String = context.getString(R.string.category_all)

    override fun add(text: String) {
        items.add(text)
    }

    override fun contains(text: String): Boolean = items.contains(text)

    override fun categoryNames(): List<String> =
        listOf(allCategoryLabel) + DefaultAdviserCorpus.categories.map { it.name }

    /** 取某个分类的名称：categoryIndex 为 0 表示“全部分类” */
    override fun categoryOf(categoryIndex: Int): String {
        val categories = DefaultAdviserCorpus.categories
        return if (categoryIndex in 1..categories.size) {
            categories[categoryIndex - 1].name
        } else {
            allCategoryLabel
        }
    }
}
