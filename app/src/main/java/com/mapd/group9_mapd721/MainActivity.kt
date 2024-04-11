package com.mapd.group9_mapd721

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mapd.group9_mapd721.model.HotelDetailRoute
import com.mapd.group9_mapd721.screen.BookingPage
import com.mapd.group9_mapd721.screen.HomePage
import com.mapd.group9_mapd721.screen.HotelDetailActivity
import com.mapd.group9_mapd721.screen.HotelDetailView
import com.mapd.group9_mapd721.screen.NotificationPage
import com.mapd.group9_mapd721.screen.ProfilePage
import com.mapd.group9_mapd721.ui.theme.BG
import com.mapd.group9_mapd721.ui.theme.Group9_MAPD721Theme
import com.mapd.group9_mapd721.ui.theme.TertiaryColor

data class TabBarItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val badgeAmount: Int? = null
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Group9_MAPD721Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyApp()
                }
            }
        }
    }
}

@Composable
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
fun MyApp(modifier: Modifier = Modifier) {
// setting up the individual tabs
    val homeTab = TabBarItem(title = "Home", selectedIcon = Icons.Filled.Home, unselectedIcon = Icons.Outlined.Home)
    val alertsTab = TabBarItem(title = "Notification", selectedIcon = Icons.Filled.Notifications, unselectedIcon = Icons.Outlined.Notifications, badgeAmount = 7)
    val settingsTab = TabBarItem(title = "Booking", selectedIcon = Icons.Filled.DateRange, unselectedIcon = Icons.Outlined.DateRange)
    val moreTab = TabBarItem(title = "Profile", selectedIcon = Icons.Filled.Person, unselectedIcon = Icons.Outlined.Person)

    // creating a list of all the tabs
    val tabBarItems = listOf(homeTab, alertsTab, settingsTab, moreTab)

    // creating our navController
    val navController = rememberNavController()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(bottomBar = { TabView(tabBarItems, navController) }) {
            NavHost(navController = navController, startDestination = homeTab.title) {
                composable(homeTab.title) {
                    HomePage(navController)
                }
                composable(alertsTab.title) {
                    NotificationPage()
                }
                composable(settingsTab.title) {
                    BookingPage()
                }
                composable(moreTab.title) {
                    ProfilePage()
                }
                composable(HotelDetailRoute) {
                    HotelDetailView()
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TabView(tabBarItems: List<TabBarItem>, navController: NavController) {
    val selectedTabIndex = rememberSaveable {
        mutableIntStateOf(0)
    }
    val navOptions = remember {
        NavOptions.Builder()
            .setEnterAnim(0) // Set enter animation to 0 to disable animation
            .setExitAnim(0) // Set exit animation to 0 to disable animation
            .build()
    }

    NavigationBar(
        containerColor = Color(0xFFfafaf6)) {
        // looping over each tab to generate the views and navigation for each item
        tabBarItems.forEachIndexed { index, tabBarItem ->
            NavigationBarItem(
                selected = selectedTabIndex.intValue == index,
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = TertiaryColor
                ),
                onClick = {
                    selectedTabIndex.intValue = index
                    navController.navigate(tabBarItem.title, navOptions)
                },
                icon = {
                    TabBarIconView(
                        isSelected = selectedTabIndex.intValue == index,
                        selectedIcon = tabBarItem.selectedIcon,
                        unselectedIcon = tabBarItem.unselectedIcon,
                        title = tabBarItem.title,
                        badgeAmount = tabBarItem.badgeAmount
                    )
                },
                label = {Text(tabBarItem.title)})
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabBarIconView(
    isSelected: Boolean,
    selectedIcon: ImageVector,
    unselectedIcon: ImageVector,
    title: String,
    badgeAmount: Int? = null
) {
    BadgedBox(badge = { TabBarBadgeView(badgeAmount) }) {
        Icon(
            imageVector = if (isSelected) {selectedIcon} else {unselectedIcon},
            contentDescription = title
        )
    }
}


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TabBarBadgeView(count: Int? = null) {
    if (count != null) {
        Badge (containerColor = TertiaryColor){
            Text(count.toString())
        }
    }
}

@Composable
fun MoreView() {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text("Thing 1")
        Text("Thing 2")
        Text("Thing 3")
        Text("Thing 4")
        Text("Thing 5")
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Group9_MAPD721Theme {
        MyApp()
    }
}