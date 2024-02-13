package com.app.vanshavali.landing.entity

import androidx.annotation.Nullable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "main_list")
class MainEntity (@PrimaryKey
                  @ColumnInfo(name = "member_id") var memberId: String,
                  @ColumnInfo(name = "parent_id")@Nullable  var parentId: String,
                  @ColumnInfo(name = "name_english") var nameEnglish: String,
                  @ColumnInfo(name = "name_hindi") var nameHindi: String,
                  @ColumnInfo(name = "is_alive")@Nullable  var isAlive: String,
                  @ColumnInfo(name = "no_of_children") @Nullable var noOfChildren: String,
                  @ColumnInfo(name = "wife_id") @Nullable var wifeId: String,
                  @ColumnInfo(name = "no_of_marriage") @Nullable var noOfMarriage: String,
                  @ColumnInfo(name = "page_no") @Nullable var pageNo: String,
                  @ColumnInfo(name = "category_id") var categoryId: String,
                  @ColumnInfo(name = "dob")@Nullable  var dob: String,
                  @ColumnInfo(name = "doe")@Nullable  var doe: String,
                  @ColumnInfo(name = "profile_image")@Nullable  var profileImage: String,
                  @ColumnInfo(name = "address")@Nullable  var address: String,
                  @ColumnInfo(name = "mobile_number")@Nullable  var mobileNumber: String,
                  @ColumnInfo(name = "occupation") @Nullable  var occupation: String,
                  @ColumnInfo(name = "position_hold")@Nullable  var positionHold: String,
                  @ColumnInfo(name = "from_to_year")@Nullable  var fromToYear: String,
                  @ColumnInfo(name = "remarks")@Nullable  var remarks: String
)
