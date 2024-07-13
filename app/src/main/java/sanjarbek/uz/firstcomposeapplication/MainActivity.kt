package sanjarbek.uz.firstcomposeapplication

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp()
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "AutoboxingStateCreation")
@Composable
fun MyApp() {
    MaterialTheme {
        ///List of Navigation Items that will be clicked
        val items = listOf(
            NavigationItems(
                title = "Home",
                selectedIcon = Icons.Filled.Home,
                unselectedIcon = Icons.Outlined.Home
            ),
            NavigationItems(
                title = "Info",
                selectedIcon = Icons.Filled.Info,
                unselectedIcon = Icons.Outlined.Info
            ),
            NavigationItems(
                title = "Edit",
                selectedIcon = Icons.Filled.Edit,
                unselectedIcon = Icons.Outlined.Edit,
                badgeCount = 105
            ),
            NavigationItems(
                title = "Settings",
                selectedIcon = Icons.Filled.Settings,
                unselectedIcon = Icons.Outlined.Settings
            )
        )

        //Remember Clicked index state
        var selectedItemIndex by rememberSaveable {
            mutableStateOf(0)
        }

        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()

        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet {
                    Spacer(modifier = Modifier.height(16.dp)) //space (margin) from top
                    items.forEachIndexed { index, item ->
                        NavigationDrawerItem(
                            label = { Text(text = item.title) },
                            selected = index == selectedItemIndex,
                            onClick = {
                                //  navController.navigate(item.route)

                                selectedItemIndex = index
                                scope.launch {
                                    drawerState.close()
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = if (index == selectedItemIndex) {
                                        item.selectedIcon
                                    } else item.unselectedIcon,
                                    contentDescription = item.title
                                )
                            },
                            badge = {  // Show Badge
                                item.badgeCount?.let {
                                    Text(text = item.badgeCount.toString())
                                }
                            },
                            modifier = Modifier
                                .padding(NavigationDrawerItemDefaults.ItemPadding) //padding between items
                        )
                    }

                }
            },
            gesturesEnabled = true
        ) {
            Scaffold(
                topBar = {
                    MyTopAppBar("My App", drawerState)
                }
            ) {
                MyListView(MyList.userList)

            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar(title: String, drawerState: DrawerState) {
    val scope = rememberCoroutineScope()
    TopAppBar(
        title = { Text(title) },
        navigationIcon = {
            IconButton(onClick = {
                scope.launch {
                    if (drawerState.isClosed) drawerState.open() else drawerState.close()
                }
            }) {
                Icon(Icons.Filled.Menu, contentDescription = "Menu")
            }
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = Color.Blue,
            titleContentColor = Color.White,
            navigationIconContentColor = Color.White
        )
    )
}

@Composable
fun MyListView(users: List<User>) {
    LazyColumn {
        items(users) { user ->
            ListItem(user)
        }
    }

}

@Composable
fun ListItem(user: User) {
    Column(modifier = Modifier.padding(8.dp)) {
        androidx.compose.material3.ListItem(
            leadingContent = {
                Image(
                    painter = painterResource(R.drawable.profile_image),
                    contentDescription = "avatar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.Gray, CircleShape)
                )
            },
            headlineContent = {
                Text(
                    text = user.userName ?: "",
                    fontSize = 20.sp,
                    modifier = Modifier.clickable {
                    }
                )
            },
            supportingContent = {
                Text("${user.phoneNumber}")
            },
            trailingContent = {
                Icon(Icons.Filled.Delete, contentDescription = null)
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyApp()
}


object MyList {
    val userList = listOf(
        User("Suxrob", "+998901234567"),
        User("Sanjarbek", "+998901236603"),
        User("Elyor", "+998901231234"),
        User("Suxrob", "+998901234567"),
        User("Sanjarbek", "+998901236603"),
        User("Elyor", "+998901231234"),
        User("Suxrob", "+998901234567"),
        User("Sanjarbek", "+998901236603"),
        User("Elyor", "+998901231234"),
        User("Suxrob", "+998901234567"),
        User("Sanjarbek", "+998901236603"),
        User("Elyor", "+998901231234"),
        User("Suxrob", "+998901234567"),
        User("Sanjarbek", "+998901236603"),
        User("Elyor", "+998901231234"),
        User("Suxrob", "+998901234567"),
        User("Sanjarbek", "+998901236603"),
        User("Elyor", "+998901231234"),
        User("Suxrob", "+998901234567"),
        User("Sanjarbek", "+998901236603"),
        User("Elyor", "+998901231234"),
        User("Suxrob", "+998901234567"),
        User("Sanjarbek", "+998901236603"),
        User("Elyor", "+998901231234"),
        User("Suxrob", "+998901234567"),
        User("Sanjarbek", "+998901236603"),
        User("Elyor", "+998901231234"),
        User("Suxrob", "+998901234567"),
        User("Sanjarbek", "+998901236603"),
        User("Elyor", "+998901231234"),
        User("Suxrob", "+998901234567"),
        User("Sanjarbek", "+998901236603"),
        User("Elyor", "+998901231234"),
        User("Suxrob", "+998901234567"),
        User("Sanjarbek", "+998901236603"),
        User("Elyor", "+998901231234"),
    )
}

data class User(
    val userName: String? = null,
    val phoneNumber: String? = null,
)

data class NavigationItems(
    val title: String,
    val selectedIcon: androidx.compose.ui.graphics.vector.ImageVector,
    val unselectedIcon: androidx.compose.ui.graphics.vector.ImageVector,
    val badgeCount: Int? = null,

    )