package com.phantom.automath.ui.Screen

import android.annotation.SuppressLint
import android.database.Cursor
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation.NavHostController
import com.phantom.automath.Algebra
import com.phantom.automath.AlgebraDatabaseHandler
import com.phantom.automath.AlgebraTable
import com.phantom.automath.ui.composables.ExpandableCard

@ExperimentalMaterialApi
@Composable
fun MainDrawerContent(navigation: NavHostController, database: AlgebraDatabaseHandler, drawerList: SnapshotStateList<Algebra>){
	//val cursor = remember{ mutableStateOf(database.getDataCursor())}
//	var_list = database.readDataSnapshot()
	LazyColumn(state = rememberLazyListState()){
//		items(cursor.value.count){
//			DisplayAlgebra(cursor = cursor.value)
//			cursor.value.moveToNext()
////                Text("${it.expression} : ${it.description}")
//		}
		//cursor.value.moveToFirst()
		items(drawerList.size){
			ExpandableCard(
				algebra = drawerList[it],
				navigation = navigation,
				databaseHandler = database,
				drawerList = drawerList
			)
//			cursor.value.moveToNext()
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