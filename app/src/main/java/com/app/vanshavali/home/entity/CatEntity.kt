package com.app.vanshavali.home.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category")
class CatEntity (@PrimaryKey
                 @ColumnInfo(name = "category_id") var categoryId: String,
                 @ColumnInfo(name = "category_name") var categoryName: String,
                 @ColumnInfo(name = "category_detail") var categoryDetail: String,
                 @ColumnInfo(name = "category_image") var categoryImage: String
)
