package com.phantom.automath.ui.Screen

import android.annotation.SuppressLint
import android.database.Cursor
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.phantom.automath.*
import com.phantom.automath.ui.composables.ExpandableCard

@ExperimentalMaterialApi
@Composable
fun AlgebraCreator(navigation: NavHostController, input_value: String? = null, db: AlgebraDatabaseHandler){
	var temp_str = ""
	if(input_value != null)
		temp_str = input_value.toString()

	val algebra_input = remember{ mutableStateOf(temp_str) }
	val description_input = remember { mutableStateOf("")}

	val cursor = remember { mutableStateOf(db.getDataCursor())}
	
	Column() {
		Spacer(modifier = Modifier.height(40.dp))
		OutlinedTextField(
			value = algebra_input.value,
			onValueChange = {algebra_input.value = it},
			label = {Text("Expression")},
			colors = TextFieldDefaults.outlinedTextFieldColors(
				focusedBorderColor = Color(0xfffaa0a0),
				focusedLabelColor = Color(0xfffaa0a0),
				unfocusedBorderColor = Color.DarkGray
			),
			modifier = Modifier
				.padding(horizontal = 20.dp)
				.fillMaxWidth()
		)

		Spacer(modifier = Modifier.height(40.dp))

		OutlinedTextField(
			value = description_input.value,
			onValueChange = {description_input.value = it},
			label = {Text("Description")},
			colors = TextFieldDefaults.outlinedTextFieldColors(
				focusedBorderColor = Color(0xfffaa0a0),
				focusedLabelColor = Color(0xfffaa0a0),
				unfocusedBorderColor = Color.DarkGray
			),
			modifier = Modifier
				.height(200.dp)
				.padding(horizontal = 20.dp)
				.fillMaxWidth()
		)

		Spacer(modifier = Modifier.height(40.dp))

		Row(
			modifier = Modifier
				.fillMaxWidth()
				.padding(horizontal = 20.dp),
			horizontalArrangement = Arrangement.SpaceBetween
		){
			Button(
				onClick =
				{
					navigation.navigate(Screen.MainScreen.route) {
						popUpTo(Screen.AlgebraCreator.route) {
						inclusive = true
						}
					}
				},
				colors = ButtonDefaults.buttonColors(
					backgroundColor = Color(0xfffaa0a0),
					contentColor = Color.Black
				),

			) {
				Text(
					"Cancle ",
					modifier = Modifier.padding(20.dp)
				)
			}
			Button(
				onClick =
				{
					if(ExpressionStream.isValidExpression(algebra_input.value)){
						if(algebra_input.value.length < 255 && description_input.value.length < 255)
							db.insertData(algebra_input.value, description_input.value)
					}
					navigation.navigate(Screen.MainScreen.withArgs(algebra_input.value)) {
						popUpTo(Screen.AlgebraCreator.route) {
							inclusive = true
						}
					}
				},
				colors = ButtonDefaults.buttonColors(
					backgroundColor = Color(0xfffaa0a0),
					contentColor = Color.Black
				),

			) {
				Text(
					"Save",
					modifier = Modifier.padding(20.dp)
				)
			}
		}

//		Button(onClick = { db.clear_table() }) {
//			Text(db.getDataCursor().count.toString())
//		}
//		Button(onClick = { cursor.value = db.getDataCursor() }) {
//			Text("Read Data")
//		}
//
//		LazyColumn(state = rememberLazyListState()){
//			cursor.value.moveToFirst()
//			items(cursor.value.count){
//				DisplayAlgebra(cursor = cursor.value)
//				cursor.value.moveToNext()
////                Text("${it.expression} : ${it.description}")
//			}
//		}
	}
}

@SuppressLint("Range")
@Composable
fun DisplayAlgebra(cursor: Cursor){
	val algebra = Algebra(
		0,
		cursor.getString(cursor.getColumnIndex(AlgebraTable.expression)),
		cursor.getString(cursor.getColumnIndex(AlgebraTable.description))
	)
	Text("${algebra.id} : ${algebra.expression} : ${algebra.description}")
}