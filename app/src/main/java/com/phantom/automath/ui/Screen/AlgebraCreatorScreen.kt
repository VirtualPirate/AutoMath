package com.phantom.automath.ui.Screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.phantom.automath.ui.composables.ExpandableCard

@ExperimentalMaterialApi
@Composable
fun AlgebraCreator(input_value: String?){
	val algebra_input = remember{ mutableStateOf("") }
	if(input_value != null)
		algebra_input.value = input_value

	val description_input = remember { mutableStateOf("")}
	
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
				onClick = { /*TODO*/ },
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
				onClick = { /*TODO*/ },
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

		ExpandableCard(title = "2x + y", description = "This is simple equation")
	}
}