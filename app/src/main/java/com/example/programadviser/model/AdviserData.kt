package com.example.programadviser.model

/**
 * Model 层的数据结构：问题分类。
 *
 * @param name  分类名（会显示在下拉框和结果区）
 * @param items 该分类下的问答条目（仅作为数据保留，界面不显示）
 */
data class AdviserCategory(
    val name: String,
    val items: List<AdviserItem>
)

/**
 * 单条问答。
 *
 * @param question 问题
 * @param keywords 关键词
 * @param answer   答案
 */
data class AdviserItem(
    val question: String,
    val keywords: List<String>,
    val answer: String
)
