package com.ithebk.spendeasy

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.materialIcon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.ithebk.spendeasy.ui.theme.SpendEasyTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @ExperimentalFoundationApi
    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SpendEasyTheme {
                LayoutSpendEasy(this)
            }
        }
    }
}

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun LayoutSpendEasy(context : Context) {
    Scaffold(
        topBar = {
                CustomTopBar {
                    //println("Holla")
                    Toast.makeText(context, "hello", Toast.LENGTH_LONG).show()
                }
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = {  Text(text = "Add new") },
                onClick = {

                },
                icon ={ Icon(Icons.Filled.Add,"")}
            )
        },
        floatingActionButtonPosition = FabPosition.Center
    ) {
        BodyContent(modifier = Modifier
            .fillMaxWidth()
            .padding(it)
            .padding(16.dp))
    }
}

@Composable
fun ImageListItem(index: Int) {
    Row(verticalAlignment =  Alignment.CenterVertically) {
        Image(
            painter = rememberImagePainter(
                data = "https://developer.android.com/images/brand/Android_Robot.png"
            ),
            contentDescription = "Android Logo",
            modifier = Modifier.size(50.dp)
        )
        Spacer(modifier = Modifier.width(20.dp))
        Text("Android Logo $index")
    }
}
@ExperimentalFoundationApi
@Composable
fun LazyList(modifier: Modifier) {
    val listSize = 100;
    val scrollState = rememberLazyListState(0);
    val  coroutineScope = rememberCoroutineScope()
    Column() {
        Row {
            Button(onClick = { coroutineScope.launch {
                scrollState.animateScrollToItem(0)
            } }) {
                Text(text = "Scroll to Top")
            }
            Button(onClick = {
                coroutineScope.launch {
                    scrollState.animateScrollToItem(listSize)
                }
            }) {
                Text("Scroll to Bottom")
            }
        }
        LazyColumn(
            modifier =  modifier,
            state = scrollState) {
            stickyHeader {
                Text(text = "Hello")
            }
            items(listSize){
                ImageListItem(it)
            }
        }
    }

}

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun BodyContent(modifier: Modifier) {

    val (gesturesEnabled, toggleGesturesEnabled) = remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .toggleable(
                    value = gesturesEnabled,
                    onValueChange = toggleGesturesEnabled
                )
        ) {
            Checkbox(gesturesEnabled, null)
            Text(text = if (gesturesEnabled) "Gestures Enabled" else "Gestures Disabled")
        }
        val drawerState = rememberBottomDrawerState(BottomDrawerValue.Closed)
        BottomDrawer(
            gesturesEnabled = gesturesEnabled,
            drawerState = drawerState,
            drawerContent = {
                Button(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 16.dp),
                    onClick = {
                        scope.launch { drawerState.close() }
                              },
                    content = { Text("Close Drawer") }
                )
                LazyList(modifier = modifier)
            },
            content = {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val openText = if (gesturesEnabled) "▲▲▲ Pull up ▲▲▲" else "Click the button!"
                    Text(text = if (drawerState.isClosed) openText else "▼▼▼ Drag down ▼▼▼")
                    Spacer(Modifier.height(20.dp))
                    Button(onClick = { scope.launch { drawerState.open() } }) {
                        Text("Click to open")
                    }
                }
            }
        )
    }
}

@Composable
fun PhotographerCard(modifier: Modifier = Modifier) {
    Row(
        modifier
            .padding(8.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(MaterialTheme.colors.surface)
            .clickable(onClick = { /* Ignoring onClick */ })
            .padding(16.dp)) {
        Surface(
            modifier = Modifier
                .size(50.dp)
                .align(Alignment.CenterVertically),
            shape = CircleShape,
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.2f)
        ){}
        Column (modifier = Modifier
            .padding(start = 10.dp)
            .align(Alignment.CenterVertically)) {
            Text(text = "Alfred Sisley", fontWeight = FontWeight.Bold)
            // LocalContentAlpha is defining opacity level of its children
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text("3 minutes ago", style = MaterialTheme.typography.body2)
            }
            Button(onClick = { /*TODO*/ }) {
                Text(text = "View")
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SpendEasyTheme {
        PhotographerCard(Modifier.padding(20.dp))
    }
}