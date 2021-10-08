package com.phantom.automath.ui.Screen

import android.annotation.SuppressLint
import android.database.Cursor
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import com.phantom.automath.Algebra
import com.phantom.automath.AlgebraDatabaseHandler
import com.phantom.automath.AlgebraTable
import com.phantom.automath.ui.composables.ExpandableCard

@ExperimentalMaterialApi
@Composable
fun MainDrawerContent(navigation: NavHostController, database: AlgebraDatabaseHandler){
	val cursor = remember{ mutableStateOf(database.getDataCursor())}
	LazyColumn(state = rememberLazyListState()){
//		items(cursor.value.count){
//			DisplayAlgebra(cursor = cursor.value)
//			cursor.value.moveToNext()
////                Text("${it.expression} : ${it.description}")
//		}
		cursor.value.moveToFirst()
		items(cursor.value.count){
			ExpandableCard(
				algebra = CursorToAlgebra(cursor.value),
				navigation = navigation,
				databaseHandler = database,
				cursor = cursor
			)
			cursor.value.moveToNext()
		}
	}
}

@SuppressLint("Range")
fun CursorToAlgebra(cursor: Cursor): Algebra {
	return Algebra(
		cursor.getInt(cursor.getColumnIndex(AlgebraTable.primary_id)),
		cursor.getString(cursor.getColumnIndex(AlgebraTable.expression)),
		cursor.getString(cursor.getColumnIndex(AlgebraTable.description))
	)
}