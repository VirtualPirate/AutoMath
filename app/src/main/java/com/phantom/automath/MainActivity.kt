package com.phantom.automath

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.phantom.automath.ui.theme.AutoMathTheme
import com.phantom.automath.ui.theme.CARDCOLOR
import java.lang.NumberFormatException
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import com.phantom.automath.ui.Screen.MainDrawerContent
import com.phantom.automath.ui.composables.ExpandableCard
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    val itemslist = listOf('A', 'B', 'C', 'D', 'e', 'f')

    val expression_font_family = FontFamily(
        Font(R.font.saira_regular, FontWeight.Normal),
        Font(R.font.saira_bold, FontWeight.Bold)
    )
    val expression_text_style = TextStyle(
        fontFamily = expression_font_family,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp
    )

    companion object {
        fun checkAlphabet(character: Char): Boolean {
            return (character in 'a'..'z') || (character in 'A'..'Z')
        }
      init {
         System.loadLibrary("automath")
      }
    }

    @ExperimentalComposeUiApi
    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //val database_handler = AlgebraDatabaseHandler(this)
        //val commonData = CommonData(this, database_handler)
        setContent {
            AutoMathTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    val database_handler = remember {AlgebraDatabaseHandler(this)}
                    val drawerList = remember{database_handler.readDataSnapshot()}
                    val commonData = remember{CommonData(this, database_handler, drawerList)}
                    Navigation(commonData = commonData)
                }
            }
        }
    }

}

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
fun ScaffoldMainScreen(navigation: NavHostController, inputValue: String? = null, databaseHandler: AlgebraDatabaseHandler, drawerList: SnapshotStateList<Algebra>){
    var scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar =
        {
            TopAppBar(
                title = {

                    Text(
                        text = "AutoMath",
                        color = Color.Black
                    )
                },
                navigationIcon = {

                    IconButton(onClick = {
                        coroutineScope.launch { scaffoldState.drawerState.open() }
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = "TopActionBar"
                        )
                    }
                },
                backgroundColor = CARDCOLOR.PALEBLUE001,
                contentColor = Color.White,
                elevation = 12.dp
            )
        },
        scaffoldState = scaffoldState,
        drawerContent = { MainDrawerContent(navigation = navigation, database = databaseHandler, drawerList = drawerList) },
        content = {
            if(inputValue != null)
                MainScreen(navigation = navigation, inputValue = inputValue)
            else
                MainScreen(navigation = navigation, inputValue = "")
        }
    )
}

@ExperimentalComposeUiApi
@Composable
fun MainScreen(navigation: NavHostController, inputValue: String){
    Column(modifier = Modifier.padding(20.dp)) {

        val keyboardController = LocalSoftwareKeyboardController.current

        val text = remember { mutableStateOf<String>(inputValue) }
        val output  = remember { mutableStateOf<String>("") }
        val var_list = remember { mutableStateOf(listOf<Pair<Char, MutableState<String>>>())}

        TextField(
            value = text.value,
            onValueChange = { text.value = it },
            modifier = Modifier
                .fillMaxWidth(1f),
//                            textStyle = expression_text_style,
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Red,
                cursorColor = Color.Red,
                backgroundColor =  CARDCOLOR.ULTRALIGHT_GREY,
                textColor = Color.Black
            )

        )

        Spacer(modifier = Modifier.padding(10.dp))
        //Greeting("Android")
        Row(
            modifier = Modifier
                .padding(all = 10.dp)

        ){
            OperatorButton(operator_char = '+', text)
            OperatorButton(operator_char = '-', text)
            OperatorButton(operator_char = '*', text)
            OperatorButton(operator_char = '/', text)
            OperatorButton(operator_char = '^', text)
            OperatorButton(operator_char = '(', text)
            OperatorButton(operator_char = ')', text)
        }

        Row{
            Card(elevation = 4.dp,
                modifier =
                Modifier
                    .padding(all = 10.dp)
                    .height(200.dp)
                    .width(170.dp),
                backgroundColor = Color(0xffB7CDD5)
            ) {
                LazyColumn(state = rememberLazyListState()){
                    items(var_list.value){
                        EachSubtitute(name = it.first, text= it.second)
                    }
                }
            }

            Column(modifier = Modifier
                .padding(all = 10.dp)
                .height(200.dp)
                .width(200.dp)) {
                Button(onClick = {
                    val hash_map = HashMap<Char, MutableState<String>>()
                    for(each_char in text.value) {
                        if(MainActivity.checkAlphabet(each_char) && !(hash_map.contains(each_char)))
                            hash_map[each_char] = mutableStateOf("")
                    }
                    for(each_pair in var_list.value) {
                        if(hash_map.containsKey(each_pair.first))
                            hash_map.plusAssign(each_pair)
                    }

                    var_list.value = hash_map.toList()

                }, modifier = Modifier
                    .fillMaxWidth(1f)
                    .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(0xfffaa0a0),
                        contentColor = Color.Black
                    )
                ) {
                    Text("Plug-In")
                }
                Spacer(modifier = Modifier.padding(10.dp))

                val simplify_coroutine = rememberCoroutineScope()
                Button(
                    onClick = {
                        simplify_coroutine.launch {
                            val expresssion_stream = ExpressionStream(text.value)
                            var each_double: Double
                            for (each_pair in var_list.value) {
                                try {
                                    each_double = each_pair.second.value.toDouble()
                                    expresssion_stream.addSubtitute(each_pair.first, each_double)
                                } catch (e: NumberFormatException) {
                                    continue
                                }
                            }
                            keyboardController?.hide()
                            output.value = "The expression is being calculated.. Please Wait"
//                                        output.value = PseudoStream.calculate(text.value)
                            output.value = "=> " + expresssion_stream.simplify_output()
                                .replace("\n", "\n=> ")
                                .replaceAfterLast("\n", "")
                        }

                    },
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .height(50.dp)
                    ,
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(0xfffaa0a0),
                        contentColor = Color.Black
                    )
                ) {
                    Text("Simplify")
                }
                Spacer(modifier = Modifier.padding(10.dp))
                Button(onClick = {
                    if(text.value != "")
                        navigation.navigate(Screen.AlgebraCreator.withArgs(text.value))
                    else
                        navigation.navigate(Screen.AlgebraCreator.route)
                }, modifier = Modifier
                    .fillMaxWidth(1f)
                    .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(0xfffaa0a0),
                        contentColor = Color.Black
                    )) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .padding(0.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Create,
                            contentDescription = "Delete Expression",
                            tint = Color.DarkGray
                        )
                        Text(
                            "Save",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        val OutputCardScrollState = rememberScrollState()

        Card(
            elevation = 4.dp,
            modifier = Modifier
                .padding(all = 10.dp)
                .fillMaxHeight()
                .fillMaxWidth()
                .verticalScroll(OutputCardScrollState)

            ,
            backgroundColor = CARDCOLOR.ULTRALIGHT_GREY
        ) {
            Text(
                output.value,
                modifier = Modifier.padding(20.dp),
                color = Color.Black,
                lineHeight = 30.sp,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }
    }
}

@Composable
fun EachSubtitute(name: Char, text: MutableState<String>){
//    val text = remember { mutableStateOf(" ") }
    Row(modifier = Modifier.padding(10.dp)) {
        Text(text = "$name  ",
            modifier = Modifier.fillMaxWidth(0.3f),
            color = Color.Black
        )
        BasicTextField(
            value = text.value,
            onValueChange = { text.value = it },
            modifier = Modifier
                .height(25.dp)
                .background(Color.White),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
//            textStyle = TextStyle.Default.copy(background = Color.White)

        )
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Composable
fun OperatorButton(operator_char: Char, text: MutableState<String>){
    val operator_font_family = FontFamily(Font(R.font.vast_shadow_regular, FontWeight.Normal))
    Button(onClick = {text.value += operator_char},
        modifier = Modifier
            .padding(1.dp)
            .height(30.dp)
            .width(40.dp),
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color(0xff90EE90),
            contentColor = Color.Black
        )
    ) {
        Text(
            text = "$operator_char",
            modifier= Modifier.padding(0.dp),
            fontSize = 15.sp,
            fontFamily = operator_font_family
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AutoMathTheme {
        Greeting("Android")
    }
}